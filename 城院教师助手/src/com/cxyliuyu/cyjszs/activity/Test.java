package com.cxyliuyu.cyjszs.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.cxyliuyu.cyjszs.SQL.JiankaotiaoDBManager;
import com.cxyliuyu.cyjszs.alarmManager.JiankaotiaoAlarmManager;
import com.cxyliuyu.cyjszs.value.JiankaotiaoValue;

public class Test {
	// 测试类 用于测试各项功能
	public void test(Context context) {
		Toast.makeText(context, "操作UI成功", Toast.LENGTH_LONG).show();
	}

	public static void testJiankaotiaoQuery(Context context) {
		// 测试jiankaotiao表的查询操作是否可用
		JiankaotiaoDBManager jiankaotiaoDBManager = new JiankaotiaoDBManager(
				context);
		List<JiankaotiaoValue> jiankaotiaos = new ArrayList<JiankaotiaoValue>();
		jiankaotiaos = jiankaotiaoDBManager.query();
		// 将查询到的数据打印出来
		Iterator itr = jiankaotiaos.iterator();
		while (itr.hasNext()) {
			JiankaotiaoValue jiankaotiao = (JiankaotiaoValue) itr.next();
			System.out.println(jiankaotiao.getId()
					+ jiankaotiao.getFuser_name());
		}

		// 测试结果，数据库的查询操作是正确的
	}

	public static void testAlarmManagerset(Context context) {
		//测试设置提醒功能是否可用
		JiankaotiaoAlarmManager.setJiankaotiaoNotification(
				new JiankaotiaoValue(1566, "刘宇", "计算机", "201503271610", "jt201", "0001"),
				context);
		Log.i("alarm","程序成功执行到了这里");
	}

	public static void testAlarmManagercancel(Context context) {
		//设置取消提醒是否可用
		JiankaotiaoAlarmManager.cancelJiankaotiaoAlarmManager(
				new JiankaotiaoValue(1566, "刘宇", "计算机", "201503271610", "jt201", "0001"),
				context);
	}
	public static void testPendingIntent(Context context){
		//测试怎么得到两个相同的pendingIntent
		
		
		//经检验，要产生两个相同的PendingIntent第二个参数和传入的intent都必须完全相同。才能产生完全相同的PendingIntent
		Intent intent1 = new Intent();
		Intent intent2 = new Intent();
		intent1.setAction("testpendingIntent");
		intent2.setAction("testpendingIntent");
		PendingIntent pi1 = PendingIntent.getBroadcast(context, 2567, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
		PendingIntent pi2 = PendingIntent.getBroadcast(context, 2567, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
		if(pi1.equals(pi2)){
			Log.i("pendingIntent", "相等");
		}else{
			Log.i("pendingIntent", "不相等");
		}
	}
	public static void testAddJiankaotiao(Context context){
		//此函数的功能是往数据库中插入一些数据
		JiankaotiaoDBManager jiankaotiaoDBManager = new JiankaotiaoDBManager(context);
		List<JiankaotiaoValue> jiankaotiaos = new ArrayList<JiankaotiaoValue>();
		JiankaotiaoValue jiankaotiao1 = new JiankaotiaoValue(12, "刘宇", "随便什么吧", "201503272140", "jt301", "0001");
		JiankaotiaoValue jiankaotiao2 = new JiankaotiaoValue(13, "刘宇", "爱啥是啥", "201503272145", "jt201", "0001");
		jiankaotiaos.add(jiankaotiao1);
		jiankaotiaos.add(jiankaotiao2);
		jiankaotiaoDBManager.add(jiankaotiaos);
	}
	public static void testGetTrueTestTime(){
		JiankaotiaoValue jiankaotiao =  new JiankaotiaoValue(12, "刘宇", "随便什么吧", "201503272140", "jt301", "0001");
		jiankaotiao.getTrueTestTime();
	}
}