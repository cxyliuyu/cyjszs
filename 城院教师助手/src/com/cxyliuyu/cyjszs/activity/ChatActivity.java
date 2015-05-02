package com.cxyliuyu.cyjszs.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cxyliuyu.cyjszs.R;
import com.cxyliuyu.cyjszs.SQL.ChatDBManager;
import com.cxyliuyu.cyjszs.activity.LoginActivity.LoginBroadcastReceiver;
import com.cxyliuyu.cyjszs.httpclient.MyHttpClient;
import com.cxyliuyu.cyjszs.tools.MyTools;
import com.cxyliuyu.cyjszs.value.ChatValue;

public class ChatActivity extends Activity implements OnClickListener {

	


	Intent intent;
	TextView textView_chat_userName1;
	TextView textView_chat_userName2;
	TextView textView_chat_userName3;
	TextView textView_chat_userName4;
	TextView textView_chat_userName5;
	TextView textView_chat_userName6;
	TextView textView_chat_userName7;
	TextView textView_chat_userName8;
	TextView textView_chat_userName9;

	TextView textView_chat_imessage1;
	TextView textView_chat_imessage2;
	TextView textView_chat_imessage3;
	TextView textView_chat_imessage4;
	TextView textView_chat_imessage5;
	TextView textView_chat_imessage6;
	TextView textView_chat_imessage7;
	TextView textView_chat_imessage8;
	TextView textView_chat_imessage9;

	TextView textView_chat_userName;

	EditText editText_chat_message;
	Button button_chat_send;

	String userId;
	String userName;

	ArrayList<TextView> textImessageList;
	ArrayList<TextView> textUserNameList;

	String fUserId;
	String fUserName;
	
	ChatBroadcastReceiver receiver = new ChatBroadcastReceiver();
	
	String URL = "http://1.cyjszs1.sinaapp.com/index.php/Admin/Message/index";

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		IntentFilter filter = new IntentFilter();
		filter.addAction("getnewmessage");
		registerReceiver(receiver, filter);
		super.onStart();
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		intent = getIntent();
		userId = intent.getStringExtra("userId");
		userName = intent.getStringExtra("userName");
		// Log.i("cyjszs","userId="+userId);
		// Log.i("cyjszs","userName"+userName);
		setTextViews();
		textView_chat_userName.setText(userName);
		ShowImessageAsyncTask show = new ShowImessageAsyncTask();
		show.execute(userId);
		button_chat_send.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		String imessage = editText_chat_message.getText().toString();
		if(MyTools.isConnectionAvailable(ChatActivity.this)){
		SendImessage sendImessage = new SendImessage();
		sendImessage.execute(imessage);
		editText_chat_message.setText("");
		}else{
			Toast.makeText(ChatActivity.this, "网络连接不可用，发送消息失败", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chat, menu);
		return true;
	}

	class ShowImessageAsyncTask extends
			AsyncTask<String, Integer, List<ChatValue>> {

		@Override
		protected List<ChatValue> doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			ChatDBManager manager = new ChatDBManager(ChatActivity.this);
			String userId = arg0[0];
			List<ChatValue> chatList = manager.query(userId);
			return chatList;
		}

		@Override
		protected void onPostExecute(List<ChatValue> chatList) {
			// TODO Auto-generated method stub
			super.onPostExecute(chatList);
			Iterator<ChatValue> itr = chatList.iterator();
			int i = 0;
			while (itr.hasNext()) {
				ChatValue chatValue = (ChatValue) itr.next();
				textUserNameList.get(i).setText(chatValue.getUserName());
				textImessageList.get(i).setText(chatValue.getImessage());
				i++;
			}
		}

	}

	class SendImessage extends AsyncTask<String, Integer, List<ChatValue>> {

		@Override
		protected List<ChatValue> doInBackground(String... arg0) {
			// TODO Auto-generated method stub

			String imessage = arg0[0];
			String tUserId = userId;
			SharedPreferences sharedPreferences = getSharedPreferences("user",
					Context.MODE_PRIVATE);
			fUserId = sharedPreferences.getString("user_id", "null");
			fUserName = sharedPreferences.getString("user_name", "null");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("msg_content", imessage));
			list.add(new BasicNameValuePair("fUserId", fUserId));
			list.add(new BasicNameValuePair("tUserId", tUserId));
			MyHttpClient myHttpClient = new MyHttpClient();
			if (MyTools.isConnectionAvailable(ChatActivity.this)) {
				String content = myHttpClient.getHttpClient(URL, list);

				ChatValue chatValue = new ChatValue();
				chatValue.setImessage(imessage);
				chatValue.setUserId(tUserId);
				chatValue.setUserName(fUserName);
				ChatDBManager manager = new ChatDBManager(ChatActivity.this);
				manager.add(chatValue);
				List<ChatValue> chatList = manager.query(tUserId);
				return chatList;
				
			} else {
				Toast.makeText(ChatActivity.this, "网络连接不可用", Toast.LENGTH_LONG)
						.show();
				return null;
			}
		}

		@Override
		protected void onPostExecute(List<ChatValue> chatList) {
			// TODO Auto-generated method stub
			super.onPostExecute(chatList);
			Iterator<ChatValue> itr = chatList.iterator();
			int i = 0;
			while (itr.hasNext()) {
				ChatValue chatValue = (ChatValue) itr.next();
				textUserNameList.get(i).setText(chatValue.getUserName());
				textImessageList.get(i).setText(chatValue.getImessage());
				i++;
			}
			
		}

	}
	public class ChatBroadcastReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			ShowImessageAsyncTask showImessageAsyncTask = new ShowImessageAsyncTask();
			showImessageAsyncTask.execute(userId);
		}
		
	}
	

	public void setTextViews() {
		textView_chat_userName1 = (TextView) findViewById(R.id.textView_chat_userName1);
		textView_chat_userName2 = (TextView) findViewById(R.id.textView_chat_userName2);
		textView_chat_userName3 = (TextView) findViewById(R.id.textView_chat_userName3);
		textView_chat_userName4 = (TextView) findViewById(R.id.textView_chat_userName4);
		textView_chat_userName5 = (TextView) findViewById(R.id.textView_chat_userName5);
		textView_chat_userName6 = (TextView) findViewById(R.id.textView_chat_userName6);
		textView_chat_userName7 = (TextView) findViewById(R.id.textView_chat_userName7);
		textView_chat_userName8 = (TextView) findViewById(R.id.textView_chat_userName8);
		textView_chat_userName9 = (TextView) findViewById(R.id.textView_chat_userName9);

		textView_chat_imessage1 = (TextView) findViewById(R.id.textView_chat_imessage1);
		textView_chat_imessage2 = (TextView) findViewById(R.id.textView_chat_imessage2);
		textView_chat_imessage3 = (TextView) findViewById(R.id.textView_chat_imessage3);
		textView_chat_imessage4 = (TextView) findViewById(R.id.textView_chat_imessage4);
		textView_chat_imessage5 = (TextView) findViewById(R.id.textView_chat_imessage5);
		textView_chat_imessage6 = (TextView) findViewById(R.id.textView_chat_imessage6);
		textView_chat_imessage7 = (TextView) findViewById(R.id.textView_chat_imessage7);
		textView_chat_imessage8 = (TextView) findViewById(R.id.textView_chat_imessage8);
		textView_chat_imessage9 = (TextView) findViewById(R.id.textView_chat_imessage9);

		textView_chat_userName = (TextView) findViewById(R.id.textView_chat_userName);

		editText_chat_message = (EditText) findViewById(R.id.editText_chat_message);
		button_chat_send = (Button) findViewById(R.id.button_chat_send);

		textImessageList = new ArrayList<TextView>();
		textImessageList.add(textView_chat_imessage1);
		textImessageList.add(textView_chat_imessage2);
		textImessageList.add(textView_chat_imessage3);
		textImessageList.add(textView_chat_imessage4);
		textImessageList.add(textView_chat_imessage5);
		textImessageList.add(textView_chat_imessage6);
		textImessageList.add(textView_chat_imessage7);
		textImessageList.add(textView_chat_imessage8);
		textImessageList.add(textView_chat_imessage9);

		textUserNameList = new ArrayList<TextView>();
		textUserNameList.add(textView_chat_userName1);
		textUserNameList.add(textView_chat_userName2);
		textUserNameList.add(textView_chat_userName3);
		textUserNameList.add(textView_chat_userName4);
		textUserNameList.add(textView_chat_userName5);
		textUserNameList.add(textView_chat_userName6);
		textUserNameList.add(textView_chat_userName7);
		textUserNameList.add(textView_chat_userName8);
		textUserNameList.add(textView_chat_userName9);

	}

}
