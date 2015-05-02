package com.cxyliuyu.cyjszs.value;

public class UserLocationValue {
	private String userName;
	private String distance;
	private String userId;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}
	

	public UserLocationValue() {

	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public UserLocationValue(String userName,String distance,String userId) {
		this.userName = userName;
		this.distance = distance;
		this.userId = userId;
	}
}
