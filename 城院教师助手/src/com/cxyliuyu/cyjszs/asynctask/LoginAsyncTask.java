package com.cxyliuyu.cyjszs.asynctask;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.cxyliuyu.cyjszs.activity.MainActivity;
import com.cxyliuyu.cyjszs.alarmManager.UpdateJiankaotiaoAlarmManager;
import com.cxyliuyu.cyjszs.httpclient.MyHttpClient;
import com.cxyliuyu.cyjszs.R;

public class LoginAsyncTask extends AsyncTask<Object,Integer,String>{
	private Context context = null;
	String alias;

	@Override
	protected String doInBackground(Object... arg0) {
		// TODO 自动生成的方法存根
		//异步任务执行的方法体
		//System.out.print("bbb");
		
		MyHttpClient myHttpClient = new MyHttpClient();
		//System.out.println("arg0[0]"+arg0[0]);
		//System.out.println("arg0[1]"+arg0[1]);
		//System.out.println("arg0[2]"+arg0[2]);
		
		String URL = (String) arg0[0];
		String userId= (String) arg0[1];
		String password = (String) arg0[2];
		context = (Context) arg0[3];
		alias = userId;
		
		//将userId和password封装到list中
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("id",userId));
		list.add(new BasicNameValuePair("password",password));
		list.add(new BasicNameValuePair("isClient", "true"));
		String content=myHttpClient.getHttpClient(URL, list);
		
		//测试是否成功得到返回的数据
		//System.out.println(content);
		
		if(content.equals("idorpwerror")){
			//用户名密码错误
			//System.out.println("用户名密码错误");
			return "idorpwerror";
			
			
		}else{
			//用户名密码正确，登录成功
			//System.out.println("用户名密码正确，登录成功");
			
			//解析用户名密码的json数据,并将数据保存到sharedPreferences中
			try {
				JSONArray jsonArray = new JSONArray(content);
				JSONObject jsonObject = jsonArray.getJSONObject(0);
				String user_id = jsonObject.getString("user_id");
				String user_name = jsonObject.getString("user_name");
				String user_password = jsonObject.getString("user_password");
				String user_sex = jsonObject.getString("user_sex");
				String user_age = jsonObject.getString("user_age");
				String user_faculty = jsonObject.getString("user_faculty");
				//以下代码测试是否将得到的json数据解析成功
				/*System.out.println(user_id);
				System.out.println(user_name);
				System.out.println(user_password);
				System.out.println(user_sex);
				System.out.println(user_age);
				System.out.println(user_faculty);*/
				
				//数据解析成功后，将该数据保存到SharedPreferences中
				SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
				Editor editor = sharedPreferences.edit();
				editor.putString("user_id", user_id);
				editor.putString("user_name", user_name);
				editor.putString("user_password", user_password);
				editor.putString("user_sex", user_sex);
				editor.putString("user_age", user_age);
				editor.putString("user_faculty", user_faculty);
				editor.commit();
				
				
				//设置定时更新监考条的AlarmManager
				UpdateJiankaotiaoAlarmManager updateJiankaotiaoAlarmManager = new UpdateJiankaotiaoAlarmManager(context);
				//Log.i("jiankaotiao", "程序执行到这里还没死掉11111");
				updateJiankaotiaoAlarmManager.setUpdateJiankaotiaoAlarmManager();
				
				
				
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			return content;
		}
		
		
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO 自动生成的方法存根
		super.onPostExecute(result);
		if(result.equals("idorpwerror")){
			Toast.makeText(context, "登录失败,请重试", Toast.LENGTH_LONG).show();
		}else{
			//用户名密码正确
			
			//跳转到下一个页面中去
			Intent intent = new Intent(context,MainActivity.class);
			context.startActivity(intent);
			
			//发送广播，告诉LoginActivity登录成功 ，可以调用finish()了
			Intent intentBroadcast = new Intent();
			intentBroadcast.setAction(context.getString(R.string.loginokAction));
			context.sendBroadcast(intentBroadcast);
			
			//注册push的alias
			setAlias();
		}		
	}
	public void setAlias(){
		mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias)); 
	}
	private static final int MSG_SET_ALIAS = 1001;
	private static final int MSG_SET_TAGS = 1002;
	private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
            case MSG_SET_ALIAS:
                Log.d("cyjszs", "Set alias in handler.");
                JPushInterface.setAliasAndTags(context.getApplicationContext(), (String) msg.obj, null, mAliasCallback);
                break;    
            default:
                Log.i("cyjszs", "Unhandled msg - " + msg.what);
            }
        }
    };
    
    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
            case 0:
                logs = "Set tag and alias success";
                Log.i("cyjszs", logs);
                break;
                
            case 6002:
                logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                Log.i("cyjszs", logs);
                
                break;
            
            default:
                logs = "Failed with errorCode = " + code;
                Log.e("cyjszs", logs);
            }
        }
	    
	};
	
	

}
