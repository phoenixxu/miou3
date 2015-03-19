package com.datang.miou.datastructure;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class Event implements Serializable {
	//事件发生时间
	Date mTime;
	//事件内容
	String mContent;
	//事件附加信息
	String mLabel;
	//事件类型
	int mType;
	
	public Event() {
		mTime = new Date();
		mContent = "Default event";
		mLabel = "";
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
	
	
	public int getType() {
		return mType;
	}

	public void setType(int mType) {
		this.mType = mType;
	}

	@SuppressWarnings("deprecation")
	@Override
	public String toString() {
		return mTime.toGMTString() + " : " + mContent + " " + mLabel;
	}
}
