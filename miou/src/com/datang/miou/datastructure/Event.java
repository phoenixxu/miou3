package com.datang.miou.datastructure;

import java.io.Serializable;
import java.util.Date;

/*
 * 事件
 */
public class Event implements Serializable {

	private static final long serialVersionUID = -2232374450076620889L;
	
	private String mHour;
	private String mMinute;
	private String mSecond;
	private String mLat;
	private String mLon;
	private String mContent;

	//事件附加信息
	private String mLabel;
	//事件类型
	private String mType;
	
	public Event() {
	}
	
	public String getLabel() {
		return mLabel;
	}

	public void setLabel(String label) {
		this.mLabel = label;
	}
	
	public void setContent(String content) {
		mContent = content;
	}
	
	public String getContent() {
		return mContent;
	}
	
	
	public String getType() {
		return mType;
	}

	public void setType(String mType) {
		this.mType = mType;
	}

	public String getHour() {
		return mHour;
	}

	public void setHour(String hour) {
		mHour = hour;
	}

	public String getMinute() {
		return mMinute;
	}

	public void setMinute(String minute) {
		mMinute = minute;
	}

	public String getSecond() {
		return mSecond;
	}

	public void setSecond(String second) {
		mSecond = second;
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
}
