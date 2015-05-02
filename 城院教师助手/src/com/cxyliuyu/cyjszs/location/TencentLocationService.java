package com.cxyliuyu.cyjszs.location;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.cxyliuyu.cyjszs.httpclient.MyHttpClient;
import com.cxyliuyu.cyjszs.tools.MyTools;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;

public class TencentLocationService extends Service implements
		TencentLocationListener {

	private HandlerThread mThread;
	private TencentLocationManager mLocationManager;
	private Context context;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public void onCreate() {
		// TODO 自动生成的方法存根
		super.onCreate();

		context = TencentLocationService.this;
		mThread = new HandlerThread("cxyliuyu" + (int) (Math.random() * 10));
		mThread.start();

		mLocationManager = TencentLocationManager.getInstance(this);

		// 创建定位请求
		TencentLocationRequest request = TencentLocationRequest.create();

		// 修改定位请求参数
		request.setInterval(5 * 60 * 1000);

		Looper otherLooper = mThread.getLooper();
		// 开始定位，在mThread线程中
		mLocationManager.requestLocationUpdates(request, this, otherLooper);

	}

	@Override
	public void onLocationChanged(TencentLocation location, int error,
			String reason) {
		// TODO 自动生成的方法存根
		String msg = null;
		if (error == TencentLocation.ERROR_OK) {

			// 当前线程的名字
			String threadName = Thread.currentThread().getName();

			// 定位成功
			String latitude = Double.toString(location.getLatitude());
			String longitude = Double.toString(location.getLongitude());
			Log.i("cyjszs", "纬度" + latitude);
			Log.i("cyjszs", "经度" + longitude);
			// 将数据上传到服务器中
			commitLocation(latitude, longitude);
		} else {
			Log.i("cyjszs", "定位失败");
		}
	}

	@Override
	public void onStatusUpdate(String arg0, int arg1, String arg2) {
		// TODO 自动生成的方法存根

	}

	public void commitLocation(String latitude, String longitude) {
		// 提交经纬度数据到服务器中
		String URL = "http://1.cyjszs1.sinaapp.com/index.php/Admin/Vicinity/recieve";
		SharedPreferences shared = context.getSharedPreferences("user",
				Context.MODE_PRIVATE);
		String userId = shared.getString("user_id", "null");
		List<NameValuePair> list = null;
		if (!userId.equals("null")) {
			list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("latitude", latitude));
			list.add(new BasicNameValuePair("longitude", longitude));
			list.add(new BasicNameValuePair("userId", userId));

			if (MyTools.isConnectionAvailable(context)) {
				MyHttpClient myHttpClient = new MyHttpClient();
				myHttpClient.getHttpClient(URL, list);
			}else{
				Log.i("cyjszs","网络连接不可用，更新地址失败");
			}
		}
		// Log.i("cyjszs","userId="+userId);

	}

}
