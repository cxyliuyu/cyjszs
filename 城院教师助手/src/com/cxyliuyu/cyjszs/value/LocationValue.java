package com.cxyliuyu.cyjszs.value;

public class LocationValue {
	private String latitude;// 纬度
	private String longitude;// 经度

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public LocationValue() {

	}

	public LocationValue(String latitude, String longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
}
