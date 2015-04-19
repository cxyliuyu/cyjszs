package com.cxyliuyu.cyjszs.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.Toast;

import com.cxyliuyu.cyjszs.alarmManager.UpdateJiankaotiaoAlarmManager;
import com.cxyliuyu.cyjszs.R;
import com.zxt.download2.DownloadNotificationListener;
import com.zxt.download2.DownloadTask;
import com.zxt.download2.DownloadTaskManager;

public class MainActivity extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button getJiankaotiao = (Button)findViewById(R.id.btn_main_jiankaotiao);
		Button cancel = (Button)findViewById(R.id.btn_main_tonotification);
		Button test = (Button)findViewById(R.id.btn_main_test);
		getJiankaotiao.setOnClickListener(this);
		cancel.setOnClickListener(this);
		test.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
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
		case R.id.btn_main_jiankaotiao:
			Intent intent = new Intent(MainActivity.this,JiankaotiaoActivity.class);
			startActivity(intent);
			break;
			
		case R.id.btn_main_tonotification:
			Intent intent2 = new Intent(MainActivity.this,NotificationActivity.class);
			startActivity(intent2);
			break;
		case R.id.btn_main_test:
			final String SDCARD = Environment
			.getExternalStorageDirectory().getPath();
			String url;
			url="http://cyjszs1-uploads.stor.sinaapp.com/Public/Uploads/20150411/2.jpg";
			if (!URLUtil.isHttpUrl(url)) {
				Toast.makeText(MainActivity.this, "not valid http url",
						Toast.LENGTH_SHORT).show();
				return;
			}
			DownloadTask downloadTask6 = new DownloadTask(url,
					SDCARD, url.substring(url.lastIndexOf("/")), url
							.substring(url.lastIndexOf("/")), null);
			DownloadTaskManager.getInstance(this).registerListener(
					downloadTask6,
					new DownloadNotificationListener(MainActivity.this, downloadTask6));
			DownloadTaskManager.getInstance(this).startDownload(downloadTask6);
			break;
		}
	}

}
