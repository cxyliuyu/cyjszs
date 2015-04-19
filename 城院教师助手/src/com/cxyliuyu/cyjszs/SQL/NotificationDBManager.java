package com.cxyliuyu.cyjszs.SQL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cxyliuyu.cyjszs.value.PushNotificationValue;

public class NotificationDBManager {
	private NotificationSqliteOpenHelper helper;
	private SQLiteDatabase db;
	public NotificationDBManager(Context context){
		helper = new NotificationSqliteOpenHelper(context, "PushNotification.db", null, 1);
		db = helper.getWritableDatabase();
	}
	public void add(PushNotificationValue pushNotificationValue){
		db.execSQL("INSERT INTO pushNotifications values(?,?,?,?)",new Object[]{pushNotificationValue.getAlert(),pushNotificationValue.getTitle(),pushNotificationValue.getUrl(),pushNotificationValue.get_id()});
	}
	public List<PushNotificationValue> query(){
		ArrayList<PushNotificationValue> pushNotifications= new ArrayList<PushNotificationValue>();
		Cursor c = queryTheCursor();
		while(c.moveToNext()){
			PushNotificationValue pushNotificationValue = new PushNotificationValue();
			pushNotificationValue.setAlert(c.getString(c.getColumnIndex("alert")));
			pushNotificationValue.setTitle(c.getString(c.getColumnIndex("title")));
			pushNotificationValue.setUrl(c.getString(c.getColumnIndex("url")));
			pushNotificationValue.set_id(c.getInt(c.getColumnIndex("_id")));
			pushNotifications.add(pushNotificationValue);
			//Log.i("cyjszs","id:"+c.getInt(c.getColumnIndex("_id")));
		}
		
		return pushNotifications;
	}
	
	public Cursor queryTheCursor(){
		//Log.i("cyjszs","查表前");
		Cursor c = db.rawQuery("select * from pushNotifications order by _id desc", null);
		//Log.i("cyjszs","查表后");
		return c;
	}
	
	public PushNotificationValue queryBySequence(int sequence){
		int i = 0;
		List<PushNotificationValue> pushNotifications = new ArrayList<PushNotificationValue>();
		PushNotificationValue pushNotification = null;
		String url=null;
		int id=-1;
		pushNotifications = query();
		
		//Log.i("cyjszs","sequence = "+sequence);
		Iterator<PushNotificationValue> itr = pushNotifications.iterator();
		while (itr.hasNext()&&i<=sequence) {
			pushNotification = (PushNotificationValue) itr
					.next();
			url = pushNotification.getUrl();
			id = pushNotification.get_id();
			//Log.i("cyjszs","循环了"+i+"次");
			i++;
			
		}
		//Log.i("cyjszs","数据库中对应的ID是"+id);
		return pushNotification;	
	}
	
	public void delete(PushNotificationValue pnv){
		int id =pnv.get_id();
		db.execSQL("delete from pushNotifications where _id="+id);
	}
}
