package com.cxyliuyu.cyjszs.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.cxyliuyu.cyjszs.R;
import com.cxyliuyu.cyjszs.SQL.NotificationDBManager;
import com.cxyliuyu.cyjszs.value.PushNotificationValue;
import com.zxt.download2.DownloadListActivity;
import com.zxt.download2.DownloadNotificationListener;
import com.zxt.download2.DownloadTask;
import com.zxt.download2.DownloadTaskManager;
import com.zxt.download2.Res;

public class NotificationActivity extends Activity {

	ListView listView;
	List<PushNotificationValue> pushNotifications = null;
	String urls[];
	private static final String SDCARD = Environment
			.getExternalStorageDirectory().getPath();
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification);
		mContext = this;
		Res.getInstance(mContext);
		listView = (ListView) findViewById(R.id.listview_pushNotification);
		ShowNotificationAsyncTask showNotificationAsyncTask = new ShowNotificationAsyncTask();
		showNotificationAsyncTask.execute("");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		menu.add(Menu.NONE, Menu.FIRST + 1, 5, "查看下载列表");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO 自动生成的方法存根
		switch (item.getItemId()) {
		case Menu.FIRST + 1:
			// 进入查看下载页面
			Intent i = new Intent(NotificationActivity.this, DownloadListActivity.class);
			i.putExtra(DownloadListActivity.DOWNLOADED, false);
			startActivity(i);
		}
		return super.onOptionsItemSelected(item);
	}

	class ShowNotificationAsyncTask extends
			AsyncTask<String, Integer, List<PushNotificationValue>> {

		@Override
		protected List<PushNotificationValue> doInBackground(String... arg0) {
			// TODO 自动生成的方法存根
			List<PushNotificationValue> pushNotifications = new ArrayList<PushNotificationValue>();

			Log.i("notification", "测试建表是否成功");
			NotificationDBManager notificationDBManager = new NotificationDBManager(
					NotificationActivity.this);
			pushNotifications = notificationDBManager.query();
			return pushNotifications;
		}

		@Override
		protected void onPostExecute(
				List<PushNotificationValue> pushNotifications) {
			// TODO 自动生成的方法存根
			super.onPostExecute(pushNotifications);
			ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
			Iterator<PushNotificationValue> itr = pushNotifications.iterator();
			while (itr.hasNext()) {
				PushNotificationValue pushNotification = (PushNotificationValue) itr
						.next();
				HashMap<String, String> hashMap = new HashMap<String, String>();
				hashMap.put("alert", pushNotification.getAlert());
				hashMap.put("title", pushNotification.getTitle());
				hashMap.put("url", pushNotification.getUrl());
				String id = Integer.toString(pushNotification.get_id());
				hashMap.put("id", id + " ");
				arrayList.add(hashMap);
			}
			// 生成适配器
			SimpleAdapter simpleAdapter = new SimpleAdapter(
					NotificationActivity.this, arrayList,
					R.layout.item_notification, new String[] { "id", "alert",
							"title" }, new int[] { R.id.notification_item_id,
							R.id.notification_item_alert,
							R.id.notification_item_title });
			listView.setAdapter(simpleAdapter);
			listView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

				@Override
				public void onCreateContextMenu(ContextMenu menu, View view,
						ContextMenuInfo arg2) {
					// TODO 自动生成的方法存根
					menu.setHeaderTitle("选择操作");
					menu.add(0, 0, 0, "删除通知");
					menu.add(0, 1, 0, "下载附件");
				}
			});
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO 自动生成的方法存根

				}
			});

		}
	}

	public boolean onContextItemSelected(MenuItem item) {

		ContextMenuInfo menuInfo = (ContextMenuInfo) item.getMenuInfo();
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		int item_id = (int) info.id;
		View view = info.targetView;
		// Log.i("cyjszs","这个Id"+item_id);
		ItemSelectedAsyncTask itemSelectedAsyncTask = new ItemSelectedAsyncTask();
		itemSelectedAsyncTask.execute(item, item_id);

		return super.onContextItemSelected(item);
	}

	class ItemSelectedAsyncTask extends AsyncTask<Object, Integer, String> {

		String url = null;
		PushNotificationValue notificationValue;

		@Override
		protected void onPostExecute(String result) {
			// TODO 自动生成的方法存根
			super.onPostExecute(result);
			url = notificationValue.getUrl();
			if (result.equals("delete")) {
				Intent intent = new Intent(NotificationActivity.this,
						NotificationActivity.class);
				startActivity(intent);
				finish();
			} else if (result.equals("download")) {
				if (!URLUtil.isHttpUrl(url)) {
					// url不是httpurl
					Toast.makeText(NotificationActivity.this,
							"not valid http url", Toast.LENGTH_SHORT).show();
				}
				DownloadTask downloadTask = new DownloadTask(url, SDCARD,
						url.substring(url.lastIndexOf("/")), url.substring(url
								.lastIndexOf("/")), null);
				DownloadTaskManager
						.getInstance(NotificationActivity.this)
						.registerListener(
								downloadTask,
								new DownloadNotificationListener(
										mContext, downloadTask));
				DownloadTaskManager.getInstance(mContext)
						.startDownload(downloadTask);
				Log.i("cyjszs","断点0");
				Intent i = new Intent(NotificationActivity.this, DownloadListActivity.class);
				Log.i("cyjszs","断点1");
				i.putExtra(DownloadListActivity.DOWNLOADED, false);
				Log.i("cyjszs","断点2");
				startActivity(i);
				Log.i("cyjszs","断点3");

			}

		}

		@Override
		protected String doInBackground(Object... arg0) {
			// TODO 自动生成的方法存根
			MenuItem item = (MenuItem) arg0[0];
			int item_id = (Integer) arg0[1];
			NotificationDBManager notificationDBManager = new NotificationDBManager(
					NotificationActivity.this);
			notificationValue = notificationDBManager.queryBySequence(item_id);
			switch (item.getItemId()) {
			case 0:
				// Log.i("cyjszs","删除菜单被点击了");
				// Log.i("cyjszs","被点击的是item是第"+item_id+"个");
				// Log.i("cyjszs","被点击的是数据库中"+notificationValue.getUrl());
				notificationDBManager.delete(notificationValue);
				return "delete";
			case 1:
				// Log.i("cyjszs","下载菜单被点击了");
				// Log.i("cyjszs","被点击的是item是第"+item_id+"个");
				// Log.i("cyjszs","被点击的是数据库中"+notificationValue.get_id()+"个");
				return "download";
			}
			return null;
		}

	}

}
