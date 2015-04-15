package com.datang.miou.datastructure;

public class NetWork {
	private String mPortalIp;
	private String mPortalPort;
	private String mUser;
	private String mPassword;
	private String mUseLan;
	private String mSendData;
	private String mDialNumber;
	private String mApn;
	private String mDialUpUser;
	private String mDialUpPassword;
	
	public String getPortalIp() {
		return mPortalIp;
	}
	public void setPortalIp(String portalIp) {
		mPortalIp = portalIp;
	}
	public String getPortalPort() {
		return mPortalPort;
	}
	public void setPortalPort(String portalPort) {
		mPortalPort = portalPort;
	}
	public String getUser() {
		return mUser;
	}
	public void setUser(String user) {
		mUser = user;
	}
	public String getPassword() {
		return mPassword;
	}
	public void setPassword(String password) {
		mPassword = password;
	}
	public String getUserLan() {
		return mUseLan;
	}
	public void setUserLan(String userLan) {
		mUseLan = userLan;
	}
	public String getSendData() {
		return mSendData;
	}
	public void setSendData(String sendData) {
		mSendData = sendData;
	}
	public String getDialNumber() {
		return mDialNumber;
	}
	public void setDialNumber(String dialNumber) {
		mDialNumber = dialNumber;
	}
	public String getApn() {
		return mApn;
	}
	public void setApn(String apn) {
		mApn = apn;
	}
	public String getDialUpUser() {
		return mDialUpUser;
	}
	public void setDialUpUser(String dialUpUser) {
		mDialUpUser = dialUpUser;
	}
	public String getDialUpPassword() {
		return mDialUpPassword;
	}
	public void setDialUpPassword(String dialUpPassword) {
		mDialUpPassword = dialUpPassword;
	}
	
	@Override
	public String toString() {
		return "(" + this.mApn + ", " + this.mDialNumber + ", " + this.mDialUpPassword + ", " 
				+ this.mPassword + ", " + this.mPortalIp + ", " + this.mPortalPort + "," +
				this.mSendData + ", " + this.mUser + ", " + this.mUseLan + ", " + this.mDialUpUser + ")";
	}
}
