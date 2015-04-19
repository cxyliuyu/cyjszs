package com.cxyliuyu.cyjszs.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.cxyliuyu.cyjszs.R;
import com.cxyliuyu.cyjszs.SQL.JiankaotiaoDBManager;
import com.cxyliuyu.cyjszs.value.JiankaotiaoValue;

public class JiankaotiaoActivity extends Activity {

	List<JiankaotiaoValue> jiankaotiaos = null;
	ListView jiankaotiaoListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jiankaotiao);

		jiankaotiaoListView= (ListView) findViewById(R.id.listView_jiankaotiao);
		ShowJiankaotiaoAsynctask showJiankaotiaoAsynctask = new ShowJiankaotiaoAsynctask();
		showJiankaotiaoAsynctask.execute(JiankaotiaoActivity.this);
	}

	public class ShowJiankaotiaoAsynctask extends
			AsyncTask<Object, Integer, List<JiankaotiaoValue>> {

		@Override
		protected void onPostExecute(List<JiankaotiaoValue> jiankaotiaos) {
			// TODO 自动生成的方法存根
			// 本方法在主线程中进行
			super.onPostExecute(jiankaotiaos);
			ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
			Iterator<JiankaotiaoValue> itr = jiankaotiaos.iterator();
			while (itr.hasNext()) {
				JiankaotiaoValue jiankaotiao = (JiankaotiaoValue) itr.next();
				HashMap<String, String> hashMap = new HashMap<String, String>();
				hashMap.put("id", jiankaotiao.getIdByString());
				hashMap.put("fuser_name", jiankaotiao.getFuser_name());
				hashMap.put("course", jiankaotiao.getCourse());
				hashMap.put("test_time", jiankaotiao.getTrueTestTime());
				hashMap.put("classroom", jiankaotiao.getClassroom());
				hashMap.put("fuser_id", jiankaotiao.getFuser_id());
				arrayList.add(hashMap);
			}

			// 生成适配器
			SimpleAdapter simpleAdapter = new SimpleAdapter(
					JiankaotiaoActivity.this, arrayList,
					R.layout.item_jiankaotiao, new String[] { "id",
							"fuser_name", "course", "fuser_id", "classroom",
							"test_time" }, new int[] { R.id.jiankaotiao_item_id,
							R.id.jiankaotiao_item_fuser_name,
							R.id.jiankaotiao_item_course,
							R.id.jiankaotiao_item_fuser_id,
							R.id.jiankaotiao_item_classroom,
							R.id.jiankaotiao_item_test_time });
			jiankaotiaoListView.setAdapter(simpleAdapter);
			
		}

		@Override
		protected List<JiankaotiaoValue> doInBackground(Object... arg0) {
			// TODO 自动生成的方法存根
			JiankaotiaoDBManager jiankaotiaoDBManager = new JiankaotiaoDBManager(
					JiankaotiaoActivity.this);
			List<JiankaotiaoValue> jiankaotiaos = jiankaotiaoDBManager.query();	
			return jiankaotiaos;
			
			
		}

	}

}


