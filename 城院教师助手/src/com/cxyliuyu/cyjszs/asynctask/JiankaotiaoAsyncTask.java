package com.cxyliuyu.cyjszs.asynctask;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.cxyliuyu.cyjszs.R;
import com.cxyliuyu.cyjszs.SQL.JiankaotiaoDBManager;
import com.cxyliuyu.cyjszs.alarmManager.JiankaotiaoAlarmManager;
import com.cxyliuyu.cyjszs.httpclient.MyHttpClient;
import com.cxyliuyu.cyjszs.tools.MyTools;
import com.cxyliuyu.cyjszs.value.JiankaotiaoValue;

//本类的作用
//1.取得服务器上监考条的信息
//2.更新数据库中存储的监考条信息
//3.为每一个监考条设置一个闹钟，用与弹出通知栏提醒
public class JiankaotiaoAsyncTask extends AsyncTask<Object, Integer, String> {

	private Context context = null;

	@Override
	protected String doInBackground(Object... arg0) {
		// TODO 自动生成的方法存根
		
		List<JiankaotiaoValue> jiankaotiaos=null;
		
		context = (Context)arg0[0];
		
		Log.i("jiankaotiao", "程序执行到了更新jiankaotiao的异步任务");
		//判断网络连接是否可用，不可用的话直接return，可用就更新jiankaotiao的信息
		if(!MyTools.isConnectionAvailable(context)){
			//网络连接不可用
			return "noconnection";
		}else{
			//网络连接可用，得到服务器中的数据
			jiankaotiaos = getJiankaotiaoValueOnServer();
			//删除数据库中已有的数据，并取消与之对应的alarmmanager
			deleteOldJiankaotiaos();
			//将新的监考条信息保存到数据库中，并设置提醒
			addNewJiankaotiaos(jiankaotiaos);
			return "UpdataJiankaotiaoOK";
		}
			
		
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO 自动生成的方法存根
		super.onPostExecute(result);
		if ("noconnection".equals(result)) {
			// 网络连接不可用
			Toast.makeText(context, "网络连接不可用，更新监考条信息失败", Toast.LENGTH_LONG)
					.show();
		}else if("UpdataJiankaotiaoOK".equals(result)){
			Toast.makeText(context, "监考条信息更新成功", Toast.LENGTH_LONG);
		}
	}

	public List<JiankaotiaoValue> getJiankaotiaoValueOnServer() {
		// 本方法用于得到服务器中的jiankaotiao信息，并以List的方式返回
		MyHttpClient myHttpClient = new MyHttpClient();
		String user_id = MyTools.getUserValue(context).getUser_id();
		String URL = context.getString(R.string.URL)
				+ "JianKaoTiao/jianKaoTiao";
		List<JiankaotiaoValue> jiankaotiaos = new ArrayList<JiankaotiaoValue>();

		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("isClient", "true"));
		list.add(new BasicNameValuePair("user_id", user_id));
		String content = myHttpClient.getHttpClient(URL, list);
		//Log.i("jiankaotiaocontent",content);
		// 将得到的监考条封装成JiankaotiaoValue

		try {
			JSONArray jsonArray = new JSONArray(content);
			int length = jsonArray.length();
			for(int i = 0;i < length;i++){
				//System.out.println(i);
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				int id = jsonObject.getInt("id");
				String fuser_name = jsonObject.getString("fuser_name");
				String course = jsonObject.getString("course");
				String test_time = jsonObject.getString("test_time");
				String classroom = jsonObject.getString("classroom");
				String fuser_id = jsonObject.getString("fuser_id");
				
				// 构造监考条的value类
				JiankaotiaoValue jiankaotiaoValue = new JiankaotiaoValue(id,
						fuser_name, course, test_time, classroom, fuser_id);
				
				//Log.i("jiankaotiao", jiankaotiaoValue.course+jiankaotiaoValue.fuser_name);
				// 设置每一个监考前一天的提示
				JiankaotiaoAlarmManager.setJiankaotiaoNotification(
						jiankaotiaoValue, context);
				// 将jiankaotiao add 到jiankaotiaos中
				jiankaotiaos.add(jiankaotiaoValue);
			}

		} catch (JSONException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return jiankaotiaos;
	}

	public void deleteOldJiankaotiaos() {
		// 此方法用于删除系统中原有的原有的监考条信息
		// 查询jiankaotiao表，然后遍历查询到的信息。将已经设置的alarmmanager取消掉

		// 打开或创建TeacherHelper.db数据库
		JiankaotiaoDBManager jiankaotiaoDBManager = new JiankaotiaoDBManager(
				context);
		List<JiankaotiaoValue> oldJiankaotiaos = jiankaotiaoDBManager
				.query();
		Iterator itr = oldJiankaotiaos.iterator();
		while (itr.hasNext()) {
			//取出数据库中的每一条信息，撤销alarmManager。并将该数据从数据库中删除。
			JiankaotiaoValue jiankaotiao = (JiankaotiaoValue) itr.next();
			// 取消alarmmanager
			JiankaotiaoAlarmManager.cancelJiankaotiaoAlarmManager(jiankaotiao,
					context);
			//将jiankaotiao的信息打印一下，验证是否成功得到了数据库中的所有信息
			//Log.i("jiankaotiao", jiankaotiao.getCourse()+jiankaotiao.getFuser_name()+jiankaotiao.getTest_time());
			jiankaotiaoDBManager.delete(jiankaotiao);
		}
	}

	public void addNewJiankaotiaos(List<JiankaotiaoValue> jiankaotiaos) {
		//将新的监考条保存到数据库中
		//Log.i("jiankaotiao","程序执行到了addNewJiankaotiaos");
		JiankaotiaoDBManager jiankaotiaoDBManager = new JiankaotiaoDBManager(
				context);
		jiankaotiaoDBManager.add(jiankaotiaos);
		//设置新的jiankaotiao notification
		JiankaotiaoAlarmManager jiankaotiaoAlarmManager = new JiankaotiaoAlarmManager();
		jiankaotiaoAlarmManager.addJiankaotiaoAlarms(jiankaotiaos, context);
		
		
	}
}
