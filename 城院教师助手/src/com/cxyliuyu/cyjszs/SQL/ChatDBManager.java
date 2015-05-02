package com.cxyliuyu.cyjszs.SQL;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cxyliuyu.cyjszs.value.ChatValue;

public class ChatDBManager {
	private ChatSqliteOpenHelper helper;
	private SQLiteDatabase db;
	
	public ChatDBManager(Context context){
		helper = new ChatSqliteOpenHelper(context,"ChatList.db",null,1);
		db = helper.getWritableDatabase();
	}
	
	public void add(ChatValue chatValue){
		//Log.i("cyjszs","qq1");
		int length;
		while(true){
			//Log.i("cyjszs", "死循环了？");
			length = selectCount(chatValue.getUserId());
			if(length>=9){
				int minId = getMinId(chatValue.getUserId());
				deleteById(minId);
			}else{
				break;
			}
			//Log.i("cyjszs", "死循环了？");
		}
		db.execSQL("INSERT INTO CHATLIST(id,userId,imessage,userName) values(?,?,?,?)",new Object[]{null,chatValue.getUserId(),chatValue.getImessage(),chatValue.getUserName()});
	}
	
	public List<ChatValue> query(String userId){
		ArrayList<ChatValue> list = new ArrayList<ChatValue>();
		Cursor c = queryTheCursor(userId);
		while(c.moveToNext()){
			ChatValue chatValue = new ChatValue();
			chatValue.setUserId(c.getString(c.getColumnIndex("userId")));
			chatValue.setImessage(c.getString(c.getColumnIndex("imessage")));
			chatValue.setUserName(c.getString(c.getColumnIndex("userName")));
			list.add(chatValue);
			int id = c.getInt(c.getColumnIndex("id"));
		}
		return list;
	}
	
	public void deleteById(int id){
		db.execSQL("delete from chatList where id="+id);
	}
	public int selectCount(String userId){
		int length = 0;
		//Log.i("cyjszs","userId= "+userId);
		Cursor c = db.rawQuery("select count(*) count from chatList where userId="+"\""+userId+"\"",null);
		while(c.moveToNext()){
			length = c.getInt(c.getColumnIndex("count"));
		}
		//Log.i("cyjszs","数据库中数据的长度是:"+length);
		return length;
	}
	public int getMinId(String userId){
		int minId = 0;
		Cursor c = db.rawQuery("select min(id) minId from chatList where userId="+"\""+userId+"\"",null);
		while(c.moveToNext()){
			minId = c.getInt(c.getColumnIndex("minId"));
		}
		return minId;
	}
	
	public void deleteAll(){
		db.execSQL("delete from chatList");
	}
	
	public Cursor queryTheCursor(String userId){
		Cursor c = db.rawQuery("select * from chatList where userId ="+"\""+userId+"\""+"order by id asc", null);
		return c;
	}
	
}
