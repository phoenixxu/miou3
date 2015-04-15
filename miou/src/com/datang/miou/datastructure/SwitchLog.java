package com.datang.miou.datastructure;

public class SwitchLog {
	private String mEnable;
	private String mType;
	private String mTestTime;
	private String mCondition;
	
	public String getEnable() {
		return mEnable;
	}
	public void setEnable(String enable) {
		mEnable = enable;
	}
	public String getType() {
		return mType;
	}
	public void setType(String type) {
		mType = type;
	}
	public String getTestTime() {
		return mTestTime;
	}
	public void setTestTime(String testTime) {
		mTestTime = testTime;
	}
	public String getCondition() {
		return mCondition;
	}
	public void setCondition(String condition) {
		mCondition = condition;
	}
	
	@Override
	public String toString() {
		return "(" + this.mEnable + ", " + this.mType + ", " + this.mTestTime + ", " + this.mCondition + ")";
	}
}
