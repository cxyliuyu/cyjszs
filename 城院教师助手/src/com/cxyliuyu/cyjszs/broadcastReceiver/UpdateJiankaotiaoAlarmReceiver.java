package com.cxyliuyu.cyjszs.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.cxyliuyu.cyjszs.asynctask.JiankaotiaoAsyncTask;

public class UpdateJiankaotiaoAlarmReceiver extends BroadcastReceiver{

	//本类是定时更新jiankaotiao表的监听器，当此类被调用时，就更新jiankaotiao
	
	@Override
	public void onReceive(Context context, Intent arg1) {
		// TODO 自动生成的方法存根
		
		Log.i("jiamkaotiao", "程序成功的执行到了更新监考条的广播接收者");
		JiankaotiaoAsyncTask jiankaotiaoAsyncTask = new JiankaotiaoAsyncTask(); 
		jiankaotiaoAsyncTask.execute(context);
	}
	
}
