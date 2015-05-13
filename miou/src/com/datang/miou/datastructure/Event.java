package com.datang.miou.datastructure;

import java.io.Serializable;
import java.util.Date;

import android.util.Log;

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

	/*
	 * 这个设置时间的函数保证了字段的设置顺序
	 */
	public void setTime(String hour, String minute, String second) {
		this.setHour(hour);
		this.setMinute(minute);
		this.setSecond(second);
	}
	
	private void setHour(String hour) {

		int h = Integer.parseInt(hour);
		if (h < 10) {
			mHour = "0" + h;
		} else {
			mHour = hour;
		}
		
		if (h == 24) {
			mHour = "00";
		}
	}


	public String getMinute() {
		return mMinute;
	}


	private void setMinute(String minute) {
		

		int m = Integer.parseInt(minute);
		if (m < 10) {
			mMinute = "0" + m;
		} else {
			mMinute = minute;
		}
		
		if (m == 60) {
			mMinute = "00";
			setHour(String.valueOf(Integer.parseInt(getHour()) + 1));
		}
	}


	public String getSecond() {
		return mSecond;
	}


	private void setSecond(String second) {
		Float doubleS = Float.parseFloat(second);
		
		int s = Math.round(doubleS);
		if (s < 10) {
			mSecond = "0" + s;
		} else {
			mSecond = "" + s;
		}
		
		if (s == 60) {
			mSecond = "00";
			setMinute(String.valueOf(Integer.parseInt(getMinute()) + 1));
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
}
