package com.cxyliuyu.cyjszs.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.cxyliuyu.cyjszs.R;
import com.cxyliuyu.cyjszs.httpclient.MyHttpClient;
import com.cxyliuyu.cyjszs.location.TencentLocationService;
import com.cxyliuyu.cyjszs.tools.MyTools;
import com.cxyliuyu.cyjszs.value.UserLocationValue;

public class LookAroundActivity extends Activity {

	List<UserLocationValue> userLocationValues = null;
	ListView listView = null;
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_look_around);
		listView = (ListView) findViewById(R.id.listView_location);
		context = LookAroundActivity.this;

		// 更新所在位置
		updateLocation();
		if (MyTools.isConnectionAvailable(context)) {
			ShowLocationList showLocationList = new ShowLocationList();
			showLocationList.execute("");
		} else {
			Toast.makeText(context, "网络连接不可用，查看附近的人失败", Toast.LENGTH_LONG)
					.show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.look_around, menu);
		return true;
	}

	public void updateLocation() {
		// 更新服务器中的位置信息
		startService(new Intent(this, TencentLocationService.class));

	}

	class ShowLocationList extends AsyncTask<String, Integer, String> {

		String URL = "http://1.cyjszs1.sinaapp.com/index.php/Admin/Vicinity/vicinity";

		@Override
		protected String doInBackground(String... arg0) {
			// TODO 自动生成的方法存根
			userLocationValues = getUserLocationValues();
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO 自动生成的方法存根
			super.onPostExecute(result);
			// 将数据放入ListView中
			ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
			Iterator<UserLocationValue> itr = userLocationValues.iterator();
			while (itr.hasNext()) {
				UserLocationValue userLocationValue = (UserLocationValue) itr
						.next();
				HashMap<String, String> hashMap = new HashMap<String, String>();
				hashMap.put("userId", userLocationValue.getUserId());
				hashMap.put("distance", userLocationValue.getDistance());
				hashMap.put("userName", userLocationValue.getUserName());
				arrayList.add(hashMap);
			}

			// 生成适配器
			SimpleAdapter simpleAdapter = new SimpleAdapter(context, arrayList,
					R.layout.item_userlocation, new String[] { "userId",
							"userName", "distance" }, new int[] {
							R.id.UserLocation_item_userId,
							R.id.UserLocation_item_userName,
							R.id.UserLocation_item_distance });

			listView.setAdapter(simpleAdapter);
		}

		public List<UserLocationValue> getUserLocationValues() {

			List<NameValuePair> list = new ArrayList<NameValuePair>();
			SharedPreferences shared = context.getSharedPreferences("user",
					Context.MODE_PRIVATE);
			String userId = shared.getString("user_id", "null");
			String content;
			list.add(new BasicNameValuePair("userId", userId));
			MyHttpClient myHttpClient = new MyHttpClient();
			content = myHttpClient.getHttpClient(URL, list);
			// Log.i("cyjszs","content="+content);
			// Log.i("cyjszs","随便什么");
			return analysisLocationJson(content);
		}

		public List<UserLocationValue> analysisLocationJson(String content) {

			// content =
			// "[{\"userId\":\"0001\",\"userName\":\"张鹏\",\"distance\":\"100米\"},{\"userId\":\"0002\",\"userName\":\"汪明达\",\"distance\":\"500米\"}]";
			// Log.i("cyjszs",content);
			List<UserLocationValue> list = new ArrayList<UserLocationValue>();
			try {
				JSONArray jsonArray = new JSONArray(content);
				int length = jsonArray.length();
				for (int i = 0; i < length; i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					String userId = jsonObject.getString("userId");
					String userName = jsonObject.getString("userName");
					String distance = jsonObject.getString("distance");
					UserLocationValue userLocationValue = new UserLocationValue(
							userName, distance, userId);
					list.add(userLocationValue);
				}
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}

			return list;
		}

	}

}

// URL还未初始化