package com.cxyliuyu.cyjszs.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.jpush.android.api.InstrumentedActivity;

import com.cxyliuyu.cyjszs.R;
import com.cxyliuyu.cyjszs.asynctask.LoginAsyncTask;
import com.cxyliuyu.cyjszs.tools.MyTools;


public class LoginActivity extends InstrumentedActivity {

	EditText edtTxt_userId;
	EditText edtTxt_password;
	Button imgBtn_submit;
	Button imgBtn_cancel;
	LoginBroadcastReceiver receiver = new LoginBroadcastReceiver();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        edtTxt_userId = (EditText) findViewById(R.id.edtTxt_login_userId);
        edtTxt_password = (EditText) findViewById(R.id.edtTxt_login_password);
        imgBtn_submit = (Button) findViewById(R.id.imgBtn_login_submit);
        imgBtn_cancel = (Button) findViewById(R.id.imgBtn_login_cancel);
        
        //检测用户是否已经登录，如果已经登录，直接跳转到用户主界面，否则什么也不做
        Boolean isLogged = MyTools.isLogged(LoginActivity.this);
        if(isLogged){
        	Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        	startActivity(intent);
        	finish();
        }
        
        
        //为submit按钮设置监听器,
        imgBtn_submit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				//得到文本框中的信息
				String userId = edtTxt_userId.getText().toString().trim();
		        String password = edtTxt_password.getText().toString().trim();
		        
		        //测试是否成功得到了页面中的信息
		        //System.out.println(userId);
		        //System.out.println(password);
		        //System.out.println("登录按钮的监听器成功被触发了");
		        
		        //首先判断网络连接是否可以使用
				Boolean isConnectionAvailable = MyTools.isConnectionAvailable(LoginActivity.this);
				if(isConnectionAvailable){
					
					//System.out.println("网络连接可用");
		        
					//取得页面信息后，访问远程服务器。确定用户是否能够登录成功
					//首先建立并执行异步任务
					LoginAsyncTask loginAsyncTask = new LoginAsyncTask();
					//System.out.println(getString(R.string.URL));
					String url=getString(R.string.URL);
					loginAsyncTask.execute(url+"Login/login",userId,password,LoginActivity.this);
					
				}else{
					Toast.makeText(LoginActivity.this, "网络连接不可用，请检查您的网络连接", Toast.LENGTH_LONG).show();
				}
		        
			}
			
		});
    }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



	@Override
	public void onStart() {
		// TODO 自动生成的方法存根
		//为LoginActivity设置了一个广播接受者，用于在完成登录后finish()掉LoginActivity
		IntentFilter filter = new IntentFilter();
		filter.addAction(getString(R.string.loginokAction));
		registerReceiver(receiver, filter);
		super.onStart();
	}
    public class LoginBroadcastReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO 自动生成的方法存根
			unregisterReceiver(receiver);
			finish();
		}
    	
    }
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
    


}
