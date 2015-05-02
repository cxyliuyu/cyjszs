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

import com.cxyliuyu.cyjszs.SQL.ChatDBManager;
import com.cxyliuyu.cyjszs.SQL.NotificationDBManager;
import com.cxyliuyu.cyjszs.activity.ChatActivity;
import com.cxyliuyu.cyjszs.activity.NotificationActivity;
import com.cxyliuyu.cyjszs.value.ChatValue;
import com.cxyliuyu.cyjszs.value.PushNotificationValue;

public class MyJpushReceiver extends BroadcastReceiver {
	private static final String TAG = "cyjszs";
	private Context context;
	String alert = null;
	String title = null;
	String extras = null;
	String url = null;
	String pushNotificationId = null;
	String type = null;
	String msg_content = null;
	String fUserId = null;
	String fUserName = null;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO 自动生成的方法存根
		this.context = context;
		Bundle bundle = intent.getExtras();
		

		if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
				.getAction())) {
			// 监听到notification消息
			alert = bundle.getString(JPushInterface.EXTRA_ALERT);
			title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
			extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
			

			//Log.i(TAG, "nalert:" + alert);
			//Log.i(TAG, "ntitle:" + title);
			//Log.i(TAG, "nextras:" + extras);
			try {
				JSONObject jsonObject = new JSONObject(extras);
				url = jsonObject.getString("url");
				pushNotificationId = jsonObject
						.getString("pushNotificationId");
				type = jsonObject.getString("type");
				msg_content = jsonObject.getString("msg_content");
				fUserId = jsonObject.getString("fUserId");
				fUserName = jsonObject.getString("fUserName");
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}

			
			 Log.i(TAG, "url:"+url);
			 Log.i(TAG, "pushNotificationId="+pushNotificationId);
			if (type.equals("notification")) {
				int iPushNotificationId = Integer.parseInt(pushNotificationId);
				PushNotificationValue pushNotificationValue = new PushNotificationValue(
						alert, title, url, iPushNotificationId);
				PushNotificationAsyncTask pushNotificationAsyncTask = new PushNotificationAsyncTask();
				pushNotificationAsyncTask.execute(pushNotificationValue);
			} else if (type.equals("chat")) {
				ChatValue chatValue = new ChatValue();
				chatValue.setImessage(msg_content);
				chatValue.setUserId(fUserId);
				chatValue.setUserName(fUserName);
				Log.i("cyjszs","fUserName"+chatValue.getUserName());
				PushChatAsyncTask pushChatAsyncTask = new PushChatAsyncTask();
				pushChatAsyncTask.execute(chatValue);
				
				Intent intent2 = new Intent();
				intent2.setAction("getnewmessage");
				context.sendBroadcast(intent2);
			}
			
		} 
		if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
				.getAction())) {
			
			
			alert = bundle.getString(JPushInterface.EXTRA_ALERT);
			title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
			extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
			try {
				JSONObject jsonObject = new JSONObject(extras);
				url = jsonObject.getString("url");
				pushNotificationId = jsonObject
						.getString("pushNotificationId");
				type = jsonObject.getString("type");
				msg_content = jsonObject.getString("msg_content");
				fUserId = jsonObject.getString("fUserId");
				fUserName = jsonObject.getString("fUserName");
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			//通知被点击了
			if(type.equals("chat")){
				Intent intent1 = new Intent(context,ChatActivity.class);
				intent1.putExtra("userId", fUserId);
				intent1.putExtra("userName", fUserName);
				intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(intent1);
			}else if(type.equals("notification")){
				Intent intent2 = new Intent(context, NotificationActivity.class);
				intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(intent2);	
			}
		}
	}

	class PushNotificationAsyncTask extends
			AsyncTask<PushNotificationValue, integer, String> {

		@Override
		protected String doInBackground(PushNotificationValue... arg0) {
			// TODO 自动生成的方法存根
			NotificationDBManager notificationDBManager = new NotificationDBManager(
					context);
			notificationDBManager.add(arg0[0]);
			return null;
		}

	}
	
	class PushChatAsyncTask extends AsyncTask<ChatValue,Integer,String>{

		@Override
		protected String doInBackground(ChatValue... arg0) {
			// TODO Auto-generated method stub
			ChatValue chatValue = arg0[0];
			ChatDBManager manager = new ChatDBManager(context);
			manager.add(chatValue);
			return null;
		}
		
	}

}
