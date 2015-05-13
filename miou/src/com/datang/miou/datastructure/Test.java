package com.datang.miou.datastructure;

import java.util.Date;

public class Test {
	//一个测试线程
	
	//是否正在测试
	private boolean isTesting;
	//测试计划
	private TestPlan mTestPlan;	
	//测试日志
	private TestLog mTestLog;
	//测试开始时间
	private Date mStartDate;
	//测试结束时间
	private Date mStopDate;

	public boolean isTesting() {
		return isTesting;
	}

	public void setTesting(boolean isTesting) {
		this.isTesting = isTesting;
	}

	public TestPlan getTestPlan() {
		return mTestPlan;
	}

	public void setTestPlan(TestPlan mTestPlan) {
		this.mTestPlan = mTestPlan;
	}

	public TestLog getTestLog() {
		return mTestLog;
	}

	public void setTestLog(TestLog mTestLog) {
		this.mTestLog = mTestLog;
	}

	public Date getStartDate() {
		return mStartDate;
	}

	public void setStartDate(Date mStartDate) {
		this.mStartDate = mStartDate;
	}

	public Date getStopDate() {
		return mStopDate;
	}

	public void setStopDate(Date mStopDate) {
		this.mStopDate = mStopDate;
	}
	
	
}
