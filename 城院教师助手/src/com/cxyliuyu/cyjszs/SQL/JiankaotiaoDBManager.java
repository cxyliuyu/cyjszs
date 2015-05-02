package com.cxyliuyu.cyjszs.SQL;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cxyliuyu.cyjszs.value.JiankaotiaoValue;

public class JiankaotiaoDBManager {
	private JiankaotiaoSqliteOpenHelper helper;
	private SQLiteDatabase db;
	public JiankaotiaoDBManager(Context context){
		helper= new JiankaotiaoSqliteOpenHelper(context,"TeacherHelper.db",null,1);
		db=helper.getWritableDatabase();
	}
	public void add(List<JiankaotiaoValue> jiankaotiaos){
		db.beginTransaction();
		try{
			for(JiankaotiaoValue jiankaotiao :jiankaotiaos){
				db.execSQL("INSERT INTO jiankaotiao values(?,?,?,?,?,?)",new Object[]{jiankaotiao.id,jiankaotiao.fuser_name,jiankaotiao.course,jiankaotiao.test_time,jiankaotiao.classroom,jiankaotiao.fuser_id});
				//Log.i("jiankaotiao",jiankaotiao.id+jiankaotiao.fuser_name);
			}
			db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
		}
	}
	public List<JiankaotiaoValue> query(){
		ArrayList<JiankaotiaoValue> jiankaotiaos = new ArrayList<JiankaotiaoValue>();
		Cursor c=queryTheCursor();
		while(c.moveToNext()){
			JiankaotiaoValue jiankaotiao = new JiankaotiaoValue();
			jiankaotiao.id=c.getInt(c.getColumnIndex("id"));
			jiankaotiao.fuser_name=c.getString(c.getColumnIndex("fuser_name"));
			jiankaotiao.course=c.getString(c.getColumnIndex("course"));
			jiankaotiao.classroom=c.getString(c.getColumnIndex("classroom"));
			jiankaotiao.test_time=c.getString(c.getColumnIndex("test_time"));
			jiankaotiao.fuser_id=c.getString(c.getColumnIndex("fuser_id"));
			jiankaotiaos.add(jiankaotiao);
		}
		c.close();
		return jiankaotiaos;
	}
	
	public void delete(JiankaotiaoValue jiankaotiao){
		db.execSQL("delete from jiankaotiao where id="+jiankaotiao.id);
	}
	
	public void deletebyid(int id){
		db.execSQL("delete from jiankaotiao where id="+id);
	}
	
	public Cursor queryTheCursor(){
		Cursor c = db.rawQuery("select * from jiankaotiao", null);
		return c;
	}
}
