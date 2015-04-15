package com.datang.miou.datastructure;

public class GeneralItem {
	private String mSpeedCondition;
	private String mGpsCondition;
	
	public String getSpeedCondition() {
		return mSpeedCondition;
	}
	public void setSpeedCondition(String speedCondition) {
		mSpeedCondition = speedCondition;
	}
	public String getGpsCondition() {
		return mGpsCondition;
	}
	public void setGpsCondition(String gpsCondition) {
		mGpsCondition = gpsCondition;
	}
	
	@Override
	public String toString() {
		return "(" + this.mSpeedCondition + ", " + this.mGpsCondition + ")";
	}
	
}
