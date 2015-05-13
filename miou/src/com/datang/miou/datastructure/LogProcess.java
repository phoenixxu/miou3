package com.datang.miou.datastructure;

public class LogProcess {
	private SwitchLog mSwitchLog;

	public LogProcess() {
		this.mSwitchLog = new SwitchLog();
	}
	
	public SwitchLog getSwitchLog() {
		return mSwitchLog;
	}

	public void setSwitchLog(SwitchLog switchLog) {
		mSwitchLog = switchLog;
	}
	
	@Override
	public String toString() {
		return this.mSwitchLog.toString();
	}
}
