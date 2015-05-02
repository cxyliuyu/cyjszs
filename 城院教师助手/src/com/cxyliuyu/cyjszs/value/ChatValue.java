package com.cxyliuyu.cyjszs.value;

public class ChatValue {
	private String userId;
	private String imessage;
	private String userName;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getImessage() {
		return imessage;
	}
	public void setImessage(String imessage) {
		this.imessage = imessage;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public ChatValue(){
		userId = null;
		imessage = null;
		userName = null;
	}
	public ChatValue(String userId,String imessage,String userName){
		this.userId = userId;
		this.imessage = imessage;
		this.userName = userName;
	}
}
