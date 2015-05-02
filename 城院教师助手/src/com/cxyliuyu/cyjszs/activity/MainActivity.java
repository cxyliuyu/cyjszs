package com.cxyliuyu.cyjszs.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cxyliuyu.cyjszs.R;
import com.cxyliuyu.cyjszs.SQL.ChatDBManager;
import com.cxyliuyu.cyjszs.SQL.NotificationDBManager;
import com.cxyliuyu.cyjszs.alarmManager.UpdateJiankaotiaoAlarmManager;
import com.example.view.button.view.SlidingMenu;

public class MainActivity extends Activity implements OnClickListener{
	private SlidingMenu mLeftMenu;////////
	public static ImageButton bb;///////
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		//Button getJiankaotiao = (Button)findViewById(R.id.btn_main_jiankaotiao);
		//Button cancel = (Button)findViewById(R.id.btn_main_tonotification);
		//Button test = (Button)findViewById(R.id.btn_main_test);
		mLeftMenu = (SlidingMenu)findViewById(R.id.id_menu);
		TextView getjkt = (TextView) findViewById(R.id.ckjkt); 
		TextView getcktz = (TextView) findViewById(R.id.cktz);  
		TextView exit = (TextView) findViewById(R.id.exit);   
		TextView tq = (TextView) findViewById(R.id.tq);   
		TextView chat = (TextView)findViewById(R.id.chat);
		TextView fjdr = (TextView)findViewById(R.id.fjdr);
        bb = (ImageButton) findViewById(R.id.person);
		
		getcktz.setOnClickListener(this);
		getjkt.setOnClickListener(this);
		exit.setOnClickListener(this);
		chat.setOnClickListener(this);
		fjdr.setOnClickListener(this);
		tq.setOnClickListener(this);
		
		
	
		//getJiankaotiao.setOnClickListener(this);
		//cancel.setOnClickListener(this);
		//test.setOnClickListener(this);
	}

	
	public void toggleMenu(View view) {
		//bb.setVisibility(View.INVISIBLE);  
		mLeftMenu.toggle();
	}

	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		 getMenuInflater().inflate(R.menu.main, menu);
		menu.add(Menu.NONE, Menu.FIRST + 1, 5, "退出登录");
	
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO 自动生成的方法存根
		switch (item.getItemId()) {
		case Menu.FIRST + 1:
			//本段代码实现用户的注销功能
			//测试菜单被选中后是否进入这里
			//Toast.makeText(MainActivity.this, "选择菜单了",Toast.LENGTH_LONG).show();
			
			//将sharedpreferences中的数据删除
			SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
			Editor editor = sharedPreferences.edit();
			editor.remove("user_id");
			editor.remove("user_name");
			editor.remove("user_password");
			editor.remove("user_sex");
			editor.remove("user_age");
			editor.remove("user_faculty");
			editor.commit();
			
			//删除定时更新监考条的AlarmManager
			UpdateJiankaotiaoAlarmManager updateJiankaotiaoAlarmManager = new UpdateJiankaotiaoAlarmManager(MainActivity.this);
			updateJiankaotiaoAlarmManager.deleteJiankaotiaoAlarmManager();
			//数据库中的信息这里就不删除了，重新登录的时候 ，如果系统中有jiankaotiao表的话，会将原来的表清空后插入新的数据
			
			//跳转到登录activity,并finish()当前activity
			Intent intent = new Intent(MainActivity.this,LoginActivity.class);
			startActivity(intent);
			finish();
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View view) {
		// TODO 自动生成的方法存根
		switch(view.getId()){
		//case R.id.btn_main_jiankaotiao:
			//Intent intent = new Intent(MainActivity.this,JiankaotiaoActivity.class);
			//startActivity(intent);
			///break;
		case R.id.ckjkt:
			Intent intent1 = new Intent(MainActivity.this,JiankaotiaoActivity.class);
			startActivity(intent1);
			break;
		//case R.id.btn_main_tonotification:
		//	Intent intent2 = new Intent(MainActivity.this,NotificationActivity.class);
		//	startActivity(intent2);
		//	break;
		case R.id.cktz:
			Intent intent22 = new Intent(MainActivity.this,NotificationActivity.class);
			startActivity(intent22);
			break;
		case R.id.tq:
			Intent intent3 = new Intent(MainActivity.this,TqActivity.class);
			startActivity(intent3);
			break;
		case R.id.chat:
			Intent intent5 = new Intent(MainActivity.this,FriendsActivity.class);
			startActivity(intent5);
			break;
		case R.id.fjdr:
			Intent intent6 = new Intent(MainActivity.this,LookAroundActivity.class);
			startActivity(intent6);
			break;
		case R.id.exit:
			SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
			Editor editor = sharedPreferences.edit();
			editor.remove("user_id");
			editor.remove("user_name");
			editor.remove("user_password");
			editor.remove("user_sex");
			editor.remove("user_age");
			editor.remove("user_faculty");
			editor.commit();
			
			//删除定时更新监考条的AlarmManager
			UpdateJiankaotiaoAlarmManager updateJiankaotiaoAlarmManager = new UpdateJiankaotiaoAlarmManager(MainActivity.this);
			updateJiankaotiaoAlarmManager.deleteJiankaotiaoAlarmManager();
			//数据库中的信息这里就不删除了，重新登录的时候 ，如果系统中有jiankaotiao表的话，会将原来的表清空后插入新的数据
			
			
			//删除数据库中现有的所有通知
			//删除数据库中现有的所有聊天信息
			ExitAsyncTask exitAsyncTask = new ExitAsyncTask();
			exitAsyncTask.execute("");
			
			//跳转到登录activity,并finish()当前activity
			Intent intent11 = new Intent(MainActivity.this,LoginActivity.class);
			startActivity(intent11);
			finish();
			break;
			
		}
		
		
		
	}

	
	class ExitAsyncTask extends AsyncTask<String,Integer,String>{

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			ChatDBManager cManager = new ChatDBManager(MainActivity.this);
			
			NotificationDBManager nManager = new NotificationDBManager(MainActivity.this);
			
			cManager.deleteAll();
			nManager.deleteAll();
			
			return null;
		}
		
	}

}
