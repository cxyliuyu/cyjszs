package com.cxyliuyu.cyjszs.broadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

import com.cxyliuyu.cyjszs.SQL.NotificationDBManager;
import com.cxyliuyu.cyjszs.activity.NotificationActivity;
import com.cxyliuyu.cyjszs.value.PushNotificationValue;

public class MyJpushReceiver extends BroadcastReceiver{
	private static final String TAG = "cyjszs";
	private Context context;
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO 自动生成的方法存根
		this.context = context;
		Bundle bundle = intent.getExtras();
		String alert = null;
		String title = null;
		String extras = null;
		String url = null;
		String pushNotificationId = null;
		
		if(JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())){
			//监听到notification消息
			alert = bundle.getString(JPushInterface.EXTRA_ALERT);
			title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
			extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
			Log.i(TAG, "alert:"+alert);
			Log.i(TAG, "title:"+title);
			Log.i(TAG, "extras:"+extras);
			try {
				JSONObject jsonObject = new JSONObject(extras);
				url = jsonObject.getString("url");
				pushNotificationId =  jsonObject.getString("pushNotificationId");
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			Log.i(TAG, "url:"+url);
			Log.i(TAG, "pushNotificationId"+pushNotificationId);
			int iPushNotificationId = Integer.parseInt(pushNotificationId);
			PushNotificationValue pushNotificationValue = new PushNotificationValue(alert, title, url, iPushNotificationId);  
			PushNotificationAsyncTask pushNotificationAsyncTask = new PushNotificationAsyncTask();
			pushNotificationAsyncTask.execute(pushNotificationValue);
		}else if(JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())){
			Intent i = new Intent(context,NotificationActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
			context.startActivity(i);
		}
	}
	
	class PushNotificationAsyncTask extends AsyncTask<PushNotificationValue, integer, String>{

		@Override
		protected String doInBackground(PushNotificationValue... arg0) {
			// TODO 自动生成的方法存根
			NotificationDBManager notificationDBManager = new NotificationDBManager(context);
			notificationDBManager.add(arg0[0]);
			return null;
		}
		
	}

}
