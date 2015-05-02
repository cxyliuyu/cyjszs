package com.cxyliuyu.cyjszs.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import com.cxyliuyu.cyjszs.R;
import com.cxyliuyu.cyjszs.tools.MyTools;

import android.R.layout;
import android.os.AsyncTask;

import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import android.view.Menu;

import android.view.Window;

import android.widget.ListView;

import android.widget.TextView;
import android.widget.Toast;

public class TqActivity extends Activity {

	TextView t, cityt, datet, weathert, tempt, l_tmpt, h_tmpt, wst, sunriset,
			sunsett;
	ListView tianqiListView;

	String line = null;
	BufferedReader bufferedReader;
	private Context context = null;

	HttpGet get;
	String ss = null, city1, date1, weather1, temp1, l_tmp1, h_tmp1, ws1,
			sunrise1, sunset1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_tq);

		// Boolean isLogged = MyTools.isLogged(TqActivity.this);

		// if(isLogged){
		context = TqActivity.this;
		cityt = (TextView) findViewById(R.id.city);
		datet = (TextView) findViewById(R.id.date);
		weathert = (TextView) findViewById(R.id.weather);
		tempt = (TextView) findViewById(R.id.temp);
		l_tmpt = (TextView) findViewById(R.id.l_tmp);
		h_tmpt = (TextView) findViewById(R.id.h_tmp);
		wst = (TextView) findViewById(R.id.WS);
		sunriset = (TextView) findViewById(R.id.sunrise);
		sunsett = (TextView) findViewById(R.id.sunset);
		// TODO 自动生成的方法存根

		if (MyTools.isConnectionAvailable(context)){

			GetTqTask task = new GetTqTask();
			task.execute("http://apistore.baidu.com/microservice/weather?citypinyin=dalian");
		} else {

			Toast.makeText(TqActivity.this, "网络连接不可用，查看天气失败", Toast.LENGTH_LONG).show();

		}

	}

	@TargetApi(19)
	class GetTqTask extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... arg0) {
			// TODO 自动生成的方法存根

			publishProgress(0);

			HttpClient httpclient = new DefaultHttpClient();
			publishProgress(30);

			HttpGet get = new HttpGet(arg0[0]);
			final String bm;

			try {
				HttpResponse response = httpclient.execute(get);
				InputStream inStream = response.getEntity().getContent();
				bufferedReader = new BufferedReader(new InputStreamReader(
						inStream));

				StringBuffer result = new StringBuffer();

			} catch (IOException e) {
				return null;

			}

			publishProgress(100);

			return line;
		}

		protected void onProgressUpdate(Integer... progress) {

		}

		protected void onPostExecute(String result) {

			try {
				if ((line = bufferedReader.readLine()) != null) {

					// Toast.makeText(TqActivity.this, "成功",
					// Toast.LENGTH_LONG).show();

					JSONObject jsonObject = new JSONObject(line);
					jsonObject = jsonObject.getJSONObject("retData");

					city1 = jsonObject.getString("city");
					date1 = jsonObject.getString("date");
					weather1 = jsonObject.getString("weather");
					temp1 = jsonObject.getString("temp");
					l_tmp1 = jsonObject.getString("l_tmp");
					h_tmp1 = jsonObject.getString("h_tmp");
					ws1 = jsonObject.getString("WS");
					sunrise1 = jsonObject.getString("sunrise");
					sunset1 = jsonObject.getString("sunset");

					cityt.setText(city1 + "市");
					datet.setText(date1);
					weathert.setText(weather1);
					tempt.setText(temp1 + "℃");
					l_tmpt.setText("   " + l_tmp1 + "℃");
					h_tmpt.setText("   " + h_tmp1 + "℃");
					wst.setText("风力:" + ws1);
					sunriset.setText("日出时间：" + sunrise1);
					sunsett.setText("日落时间：" + sunset1);

				} else {

					Toast.makeText(TqActivity.this, "失败", Toast.LENGTH_LONG)
							.show();
				}
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}

		protected void onPreExecute() {

		}

		protected void onCancelled() {

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tq, menu);
		return true;

	}

}
