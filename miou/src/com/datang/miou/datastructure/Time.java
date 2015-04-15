package com.datang.miou.datastructure;

public class Time {
	private String mBeginTime;
	private String mEndTime;
	
	public String getBeginTime() {
		return mBeginTime;
	}
	public void setBeginTime(String beginTime) {
		mBeginTime = beginTime;
	}
	public String getEndTime() {
		return mEndTime;
	}
	public void setEndTime(String endTime) {
		mEndTime = endTime;
	}
	
	@Override
	public String toString() {
		return "(" + this.mBeginTime + " " + this.mEndTime + ")";
	}
}
