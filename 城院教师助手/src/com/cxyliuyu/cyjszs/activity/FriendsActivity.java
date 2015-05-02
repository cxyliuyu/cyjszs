package com.cxyliuyu.cyjszs.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.cxyliuyu.cyjszs.R;
import com.cxyliuyu.cyjszs.httpclient.MyHttpClient;
import com.cxyliuyu.cyjszs.tools.MyTools;

public class FriendsActivity extends Activity {

	// 好友列表

	ListView friendsListView = null;
	Context context = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friends);
		friendsListView = (ListView) findViewById(R.id.listView_friends);
		context = FriendsActivity.this;
		if (MyTools.isConnectionAvailable(context)) {
			GetFriendsList getFriendsList = new GetFriendsList();
			getFriendsList.execute("");
		} else {
			Toast.makeText(context, "网络连接不可用，获取用户列表失败", Toast.LENGTH_LONG)
					.show();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.friends, menu);
		return true;
	}

	class GetFriendsList extends AsyncTask<String, Integer, String> {

		String URL = "http://1.cyjszs1.sinaapp.com/index.php/Admin/Returnfriends/index";
		ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			SharedPreferences shared = context.getSharedPreferences("user",
					Context.MODE_PRIVATE);
			String userId = shared.getString("user_id", "null");
			String content;
			list.add(new BasicNameValuePair("userId", userId));
			MyHttpClient myHttpClient = new MyHttpClient();
			content = myHttpClient.getHttpClient(URL, list);
			// content =
			// "[{\"userId\":\"0001\",\"userName\":\"张鹏\"},{\"userId\":\"0002\",\"userName\":\"张三\"}]";
			return content;
		}

		@Override
		protected void onPostExecute(String content) {
			// TODO Auto-generated method stub
			super.onPostExecute(content);

			arrayList = analysisFriendsJson(content);

			// 生成适配器
			SimpleAdapter simpleAdapter = new SimpleAdapter(context, arrayList,
					R.layout.item_friends,
					new String[] { "userId", "userName" }, new int[] {
							R.id.friends_item_id, R.id.friends_item_name });
			friendsListView.setAdapter(simpleAdapter);

			// 为用户列表设置进入聊天界面的监听事件
			friendsListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(FriendsActivity.this,
							ChatActivity.class);
					String userId = arrayList.get(arg2).get("userId");
					String userName = arrayList.get(arg2).get("userName");
					intent.putExtra("userId", userId);
					intent.putExtra("userName", userName);
					startActivity(intent);

				}
			});

		}

		public ArrayList<HashMap<String, String>> analysisFriendsJson(
				String content) {

			// Log.i("cyjszs","程序成功的执行到了这里");
			// Log.i("cyjszs",content);
			ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
			try {
				JSONArray jsonArray = new JSONArray(content);
				int length = jsonArray.length();
				Log.i("cyjszs", "length=" + length);
				for (int i = 0; i < length; i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					String userId = jsonObject.getString("user_id");
					String userName = jsonObject.getString("user_name");
					HashMap<String, String> hashMap = new HashMap<String, String>();
					hashMap.put("userId", userId);
					hashMap.put("userName", userName);
					arrayList.add(hashMap);
					Log.i("cyjszs", userId);
					Log.i("cyjszs", userName);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return arrayList;
		}

	}

}
