package com.cxyliuyu.cyjszs.value;

public class PushNotificationValue {
	public String alert;
	public String title;
	public String url=null;
	public int _id;
	public String getAlert() {
		return alert;
	}
	public void setAlert(String alert) {
		this.alert = alert;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int get_id() {
		return _id;
	}
	public void set_id(int myNotificationId) {
		this._id = myNotificationId;
	}
	public PushNotificationValue(String alert,String title,String url,int _id){
		this.alert = alert;
		this._id = _id;
		this.title =  title;
		this.url = url;
	}
	public PushNotificationValue(){
		
	}
	
}
