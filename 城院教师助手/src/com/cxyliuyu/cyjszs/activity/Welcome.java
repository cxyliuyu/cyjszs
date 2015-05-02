package com.cxyliuyu.cyjszs.activity;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import cn.jpush.android.api.JPushInterface;

import com.cxyliuyu.cyjszs.R;

public class Welcome extends Activity {

	private final int SPLASH_DISPLAY_LENGHT = 3000; //延迟三秒    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		  requestWindowFeature(Window.FEATURE_NO_TITLE);
		    requestWindowFeature(SPLASH_DISPLAY_LENGHT);
		setContentView(R.layout.activity_welcome);
		new Handler().postDelayed(new Runnable(){ 
			   
	         @Override
	         public void run() { 
	             Intent mainIntent = new Intent(Welcome.this,LoginActivity.class); 
	             Welcome.this.startActivity(mainIntent); 
	             Welcome.this.finish(); 
	            
	         } 
	             
	        }, SPLASH_DISPLAY_LENGHT); 
	    }
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		JPushInterface.onPause(Welcome.this);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		JPushInterface.onResume(Welcome.this);
	} 


	

}