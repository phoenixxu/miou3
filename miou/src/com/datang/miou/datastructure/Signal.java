package com.datang.miou.datastructure;

import java.io.Serializable;
import java.util.Date;

/*
 * 信令
 */
public class Signal implements Serializable{

	private static final long serialVersionUID = 4042455828299626115L;
	
	//信令发生时间
	private String mHour;
	private String mMinute;
	private String mSecond;
	private String mLat;
	private String mLon;
	private String mType;
	private String mContent;
	
	public Signal() {
	}

	
	public String getHour() {
		return mHour;
	}


	public void setHour(String hour) {
		int h = Integer.parseInt(hour);
		if (h < 10) {
			mHour = "0" + h;
		} else {
			mHour = hour;
		}
	}


	public String getMinute() {
		return mMinute;
	}


	public void setMinute(String minute) {
		int m = Integer.parseInt(minute);
		if (m < 10) {
			mMinute = "0" + m;
		} else {
			mMinute = minute;
		}
	}


	public String getSecond() {
		return mSecond;
	}


	public void setSecond(String second) {
		Float doubleS = Float.parseFloat(second);
		int s = Math.round(doubleS);
		if (s < 10) {
			mSecond = "0" + s;
		} else {
			mSecond = "" + s;
		}
	}


	public String getLat() {
		return mLat;
	}


	public void setLat(String lat) {
		mLat = lat;
	}


	public String getLon() {
		return mLon;
	}


	public void setLon(String lon) {
		mLon = lon;
	}


	public String getType() {
		return mType;
	}


	public void setType(String signalType) {
		mType = signalType;
	}


	public void setContent(String content) {
		mContent = content;
	}
	
	public String getContent() {
		return mContent;
	}
}
