package com.datang.miou.datastructure;

import java.util.ArrayList;

public class TestScript {
	private AutoTestUnit mAutoTestUnit;
	private ArrayList<TestScheme> mTestUnit;
	
	public TestScript() {
		mTestUnit = new ArrayList<TestScheme>();
	}
	
	public AutoTestUnit getAutoTestUnit() {
		return mAutoTestUnit;
	}
	public void setAutoTestUnit(AutoTestUnit autoTestUnit) {
		mAutoTestUnit = autoTestUnit;
	}
	public ArrayList<TestScheme> getTestUnit() {
		return mTestUnit;
	}
	public void setTestUnit(ArrayList<TestScheme> testUnit) {
		mTestUnit = testUnit;
	}
	
	@Override
	public String toString() {
		return this.mAutoTestUnit.toString() + " " + mTestUnit.toString();
	}
}
