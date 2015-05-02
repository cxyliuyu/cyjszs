package com.cxyliuyu.cyjszs.alarmManager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class UpdateJiankaotiaoAlarmManager {
	
	
	Context context;
	AlarmManager alarmManager;
	Intent intent;
	
	public UpdateJiankaotiaoAlarmManager(Context context){
		this.context=context;
		alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		intent = new Intent(); 
	}
	
	
	public void setUpdateJiankaotiaoAlarmManager() {
		// 本方法的作用是设置一个循环触发的alarmmanager,用于定时的更新监考条的信息
		
		intent.setAction("com.cxyliuyu.cyjszs.updateJiankaotiao");
		PendingIntent pi = PendingIntent.getBroadcast(context, 172800000,	//两天的毫秒数，用于唯一的标记此pendingIntent
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		alarmManager.setRepeating(0, System.currentTimeMillis(), 86400000, pi);
	}
	public void deleteJiankaotiaoAlarmManager(){
		//本方法的作用是取消 【定时更新】 监考条信息的alarmmanager
		intent.setAction("com.cxyliuyu.cyjszs.updateJiankaotiao");
		PendingIntent pi = PendingIntent.getBroadcast(context, 172800000,	//两天的毫秒数，用于唯一的标记此pendingIntent
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		alarmManager.cancel(pi);
	}
}
