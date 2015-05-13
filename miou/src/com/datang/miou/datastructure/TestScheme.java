package com.datang.miou.datastructure;

import java.util.ArrayList;
import java.util.List;

import com.datang.miou.datastructure.Time;

public class TestScheme {
	private String mEnable;
	private String mModelLock;
	private String mDesc;
	private String mExno;
	private String mTimeCondition;
	private String mExecutiveDate;
	private Time mTime;
	private String mRepeat;
	private Synchronize mCommandList;
	
	public TestScheme() {
		this.mTime = new Time();
		this.mCommandList = new Synchronize();
	}

	public String getEnable() {
		return mEnable;
	}

	public void setEnable(String enable) {
		mEnable = enable;
	}

	public String getModelLock() {
		return mModelLock;
	}

	public void setModelLock(String modelLock) {
		mModelLock = modelLock;
	}

	public String getDesc() {
		return mDesc;
	}

	public void setDesc(String desc) {
		mDesc = desc;
	}

	public String getExno() {
		return mExno;
	}

	public void setExno(String exno) {
		mExno = exno;
	}

	public String getTimeCondition() {
		return mTimeCondition;
	}

	public void setTimeCondition(String timeCondition) {
		mTimeCondition = timeCondition;
	}

	public String getExecutiveDate() {
		return mExecutiveDate;
	}

	public void setExecutiveDate(String executiveDate) {
		mExecutiveDate = executiveDate;
	}

	public Time getTime() {
		return mTime;
	}

	public void setTime(Time time) {
		mTime = time;
	}

	public Synchronize getCommandList() {
		return mCommandList;
	}

	public void setCommandList(Synchronize commandList) {
		mCommandList = commandList;
	}

	public String getRepeat() {
		return mRepeat;
	}

	public void setRepeat(String repeat) {
		mRepeat = repeat;
	}
	
	@Override
	public String toString() {
		return this.mEnable + " " + this.mModelLock + " " + this.mDesc + " " + this.mExno + " " 
				+ this.mTimeCondition + " " + this.mExecutiveDate + " " + this.mTime.toString() +
				" " + this.mRepeat + " " + this.mCommandList.toString();
	}
	
	public void getAttrs(TestScheme scheme) {
		this.mCommandList.setType(scheme.getCommandList().getType());
		this.mDesc = scheme.getDesc();
		this.mEnable = scheme.getEnable();
		this.mExecutiveDate = scheme.getExecutiveDate();
		this.mExno = scheme.getExno();
		this.mModelLock = scheme.getModelLock();
		this.mRepeat = scheme.getRepeat();
		this.mTime = scheme.getTime();
		this.mTimeCondition = scheme.getTimeCondition();
	}
}
