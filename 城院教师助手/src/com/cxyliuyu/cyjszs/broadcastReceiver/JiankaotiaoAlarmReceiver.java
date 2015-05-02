package com.cxyliuyu.cyjszs.broadcastReceiver;

import android.R;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.cxyliuyu.cyjszs.activity.MainActivity;

public class JiankaotiaoAlarmReceiver extends BroadcastReceiver{
	

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO 自动生成的方法存根
		//System.out.println("广播接受者成功的被调用了");		//检测广播接收者是否可用，经检查功能正常可用
		//System.out.println("id="+id);
		sendNotification(context);
	}
	private void sendNotification(Context context){
		Intent intent = new Intent(context,MainActivity.class);
		PendingIntent pi= PendingIntent.getActivity(context, 0, intent, 0);
		Builder builder = new Notification.Builder(context);
		builder.setSmallIcon(R.drawable.ic_btn_speak_now);
		builder.setTicker("有新的监考信息");
		builder.setWhen(System.currentTimeMillis());
		builder.setContentTitle("您明天有监考");
		builder.setContentText("点击查看详情");
		builder.setDefaults(Notification.DEFAULT_SOUND);
		builder.setContentIntent(pi);
		Notification notification = builder.getNotification();
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(0,notification);
	}
}
