package com.cxyliuyu.cyjszs.httpclient;

import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class MyHttpClient {

	public String getHttpClient(String URL,List<NameValuePair> list){
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(URL);
		String content = null;
		
		UrlEncodedFormEntity uefEntity;
		
		try{
			uefEntity = new UrlEncodedFormEntity(list, "UTF-8");
			httpPost.setEntity(uefEntity);
			HttpResponse httpResponse = httpclient.execute(httpPost);
			HttpEntity entity = httpResponse.getEntity();
			if(entity!=null){
				content = EntityUtils.toString(entity);
				//测试是否成功得到了数据
				//System.out.println("aa"+content);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return content;
		
		
	}
}
