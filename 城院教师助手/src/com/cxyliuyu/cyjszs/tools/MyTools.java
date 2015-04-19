package com.cxyliuyu.cyjszs.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;
import android.webkit.URLUtil;

import com.cxyliuyu.cyjszs.value.UserValue;

public class MyTools {
	
	private static final String SDCARD = Environment.getExternalStorageDirectory().getPath();
	
	public static boolean isConnectionAvailable(Context context){
		
		//本类为工具类，将不能单独构成的函数写在本类中
		//本类的所有方法为静态方法，不用实例化就可以调用
		
		//本工具测试网络连接是否可用
		
		if(context != null){	
			ConnectivityManager myConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetWorkInfo = myConnectivityManager.getActiveNetworkInfo();
			if(mNetWorkInfo!=null){
				return mNetWorkInfo.isAvailable();
			}
		}
		
		return false;
	}
	public static boolean isLogged(Context context){
		
		//本工具用于测试用户是否已经登录
		
		String user_id=null;
		SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
		user_id = sharedPreferences.getString("user_id", "null");
		//System.out.println("SharedPreferences"+user_id);
		if(user_id.equals("null")){
			return false;
		}else{
			return true;
		}
	}
	public static UserValue getUserValue(Context context){
		
		//此函数用于的到sharedpreference中保存的User
		
		SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
		UserValue userValue = new UserValue();
		
		
		String user_id = sharedPreferences.getString("user_id", "null");
		String user_password = sharedPreferences.getString("user_password", "null");
		String user_sex = sharedPreferences.getString("user_sex", "null");
		String user_age = sharedPreferences.getString("user_age", "null");
		String user_faculty = sharedPreferences.getString("user_faculty", "null");
		
		userValue.setUser_id(user_id);
		userValue.setUser_password(user_password);
		userValue.setUser_sex(user_sex);
		userValue.setUser_age(user_age);
		userValue.setUser_faculty(user_faculty);
		
		return userValue;
	}
	
	
	
		
}
