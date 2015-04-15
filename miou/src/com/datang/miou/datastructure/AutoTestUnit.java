package com.datang.miou.datastructure;

public class AutoTestUnit {
	private String mVersion;
	private NetWork mNetWork;
	private LogProcess mLogProcess;
	private GeneralItem mGeneralItem;
	
	public AutoTestUnit() {
		this.mNetWork = new NetWork();
		this.mLogProcess = new LogProcess();
		this.mGeneralItem = new GeneralItem();
	}
	
	public String getVersion() {
		return mVersion;
	}
	public void setVersion(String version) {
		mVersion = version;
	}
	public NetWork getNetWork() {
		return mNetWork;
	}
	public void setNetWork(NetWork netWork) {
		mNetWork = netWork;
	}
	public LogProcess getLogProcess() {
		return mLogProcess;
	}
	public void setLogProcess(LogProcess logProcess) {
		mLogProcess = logProcess;
	}
	public GeneralItem getGeneralItem() {
		return mGeneralItem;
	}
	public void setGeneralItem(GeneralItem generalItem) {
		mGeneralItem = generalItem;
	}
	
	@Override
	public String toString() {
		return this.mVersion + ", " + this.mNetWork.toString() + ", " + this.mLogProcess.toString() + ", " + this.mGeneralItem.toString();
	}
}
