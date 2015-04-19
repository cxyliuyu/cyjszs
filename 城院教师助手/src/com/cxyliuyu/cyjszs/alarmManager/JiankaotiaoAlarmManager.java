package com.cxyliuyu.cyjszs.alarmManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.cxyliuyu.cyjszs.value.JiankaotiaoValue;

public class JiankaotiaoAlarmManager {

	public static boolean setJiankaotiaoNotification(
			JiankaotiaoValue jiankaotiaoValue, Context context) {

		// 此方法的作用是为每一个监考条设置一个闹钟,用户提醒用户第二天有监考

		String stest_time = jiankaotiaoValue.getTest_time();
		Long ltest_time;
		int id = jiankaotiaoValue.getId();
		System.out.println("id=" + id);
		// 得到String类型的前一天的时间。
		String sYesterday = getSpecifiedDayBefore(stest_time);
		// System.out.println(sYesterday); //测试得带当前时间前一天的时间是否可用户 //测试结构，ok
		// 将string类型的时间转换为long类型的
		ltest_time = stringToLong(sYesterday);

		// 为监考条设置一个闹钟

		Intent intent = new Intent();
		intent.putExtra("id", id);
		intent.setAction("com.cxyliuyu.cyjszs.notification");
		PendingIntent pi = PendingIntent.getBroadcast(context, id, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		AlarmManager alarmManage = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		alarmManage.set(0, ltest_time, pi);// 0表示闹钟在睡眠状态下回唤醒系统并执行提示功能
		// System.out.println("程序执行完设置闹钟了");
		Log.i("Alarm", "设置提醒"+sYesterday);
		// alarmManage.cancel(pi);
		return true;
	}

	public static String getSpecifiedDayBefore(String specifiedDay) {
		Calendar calendar = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yyyyMMddHHmm").parse(specifiedDay);
		} catch (ParseException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		calendar.setTime(date);
		int day = calendar.get(Calendar.DATE);
		calendar.set(Calendar.DATE, day - 1);
		String dayBefore = new SimpleDateFormat("yyyyMMddHHmm").format(calendar
				.getTime());
		return dayBefore;
	}

	public static long stringToLong(String stest_time) {

		Date date;
		long ltest_time = 0;
		// 此方法的作用是将String类型的的时间转换为long类型的
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmm");
		try {
			date = simpleDateFormat.parse(stest_time);
			ltest_time = date.getTime();
		} catch (ParseException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		// System.out.println(ltest_time); //用于测试用于是否成功的将String类型的时间转换成了long类型
		// //经检验，此函数有效
		return ltest_time;
	}

	public static void cancelJiankaotiaoAlarmManager(
			JiankaotiaoValue jiankaotiao, Context context) {
		// 本类的作用是取消AlarmManager

		int id = jiankaotiao.getId();
		String stest_time = jiankaotiao.getTest_time();
		String sYesterday = getSpecifiedDayBefore(stest_time);
		Long ltest_time = stringToLong(sYesterday);

		Intent intent = new Intent();
		intent.putExtra("id", id);
		intent.setAction("com.cxyliuyu.cyjszs.notification");
		PendingIntent pi = PendingIntent.getBroadcast(context, id, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(pi);
		Log.i("Alarm", "取消现有提醒");
	}
	
	public boolean addJiankaotiaoAlarms(List<JiankaotiaoValue> jiankaotiaos,Context context){
		
		//遍历jiankaotiaos链表，为链表中的每一个jiankaotiao设置一个alarmmanager
		Iterator itr = jiankaotiaos.iterator();
		while(itr.hasNext()){
			JiankaotiaoValue jiankaotiao = (JiankaotiaoValue) itr.next();
			setJiankaotiaoNotification(jiankaotiao, context);
		}
		return true;
	}
}
