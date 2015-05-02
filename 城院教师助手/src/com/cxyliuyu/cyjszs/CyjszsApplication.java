package com.cxyliuyu.cyjszs;

import android.app.Application;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;


public class CyjszsApplication extends Application{
	
	private static final String TAG = "JPush";
	
	@Override
	public void onCreate() {    	     
   	 Log.d(TAG, "[ExampleApplication] onCreate");
        super.onCreate();
        
        
        Log.i("JMessageDemoApplication", "Application onCreate");
        
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
        
        //JMessageClient.init(getApplicationContext());
        //JPushInterface.setDebugMode(true);
   }

}
