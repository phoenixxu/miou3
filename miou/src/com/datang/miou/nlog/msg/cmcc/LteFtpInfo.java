package com.datang.miou.nlog.msg.cmcc;

import com.datang.miou.nlog.msg.ILogMsg;

/**
 * 
 * 
 * @author suntongwei
 * @version 1.0.0
 */
public class LteFtpInfo implements ILogMsg {

	/**
	 * 该记录标识
	 */
	public String identity() {
		return "LTEFTP";
	}
	
	@Override
	public String toLogMsg() {
		return "LTEFTP\t" + getAppBytesReceivedTotal() + "\t"
				+ getAppBytesReceivedHSDPA() + "\t" + getAppBytesReceivedR4()
				+ "\t" + getAppBytesReceivedLTE() + "\t"
				+ getAppBytesSendTotal() + "\t" + getAppBytesSendHSUPA() + "\t"
				+ getAppBytesSendR4() + "\t" + getAppBytesSendLTE() + "\t"
				+ getApp_currentThroughputdl() + "\t"
				+ getApp_currentThroughputul() + "\r\n";
	}

	/**
	 * 时间
	 */
	private Long time;
	/**
	 * AppBytesReceived_Total
	 */
	private String appBytesReceivedTotal = "";
	/**
	 * AppBytesReceived_HSDPA
	 */
	private String appBytesReceivedHSDPA = "";
	/**
	 * AppBytesReceived_R4
	 */
	private String appBytesReceivedR4 = "";
	/**
	 * AppBytesReceived_LTE
	 */
	private String appBytesReceivedLTE = "";
	/**
	 * AppBytesSend_Total
	 */
	private String appBytesSendTotal = "";
	/**
	 * AppBytesSend_HSUPA
	 */
	private String appBytesSendHSUPA = "";
	/**
	 * AppBytesSend_R4
	 */
	private String appBytesSendR4 = "";
	/**
	 * AppBytesSend_LTE
	 */
	private String appBytesSendLTE = "";
	/**
	 * App_currentThroughput_dl
	 */
	private String app_currentThroughputdl = "";
	/**
	 * App_currentThroughput_ul
	 */
	private String app_currentThroughputul = "";

	public String getAppBytesReceivedTotal() {
		return appBytesReceivedTotal;
	}

	public void setAppBytesReceivedTotal(String appBytesReceivedTotal) {
		this.appBytesReceivedTotal = appBytesReceivedTotal;
	}

	public String getAppBytesReceivedHSDPA() {
		return appBytesReceivedHSDPA;
	}

	public void setAppBytesReceivedHSDPA(String appBytesReceivedHSDPA) {
		this.appBytesReceivedHSDPA = appBytesReceivedHSDPA;
	}

	public String getAppBytesReceivedR4() {
		return appBytesReceivedR4;
	}

	public void setAppBytesReceivedR4(String appBytesReceivedR4) {
		this.appBytesReceivedR4 = appBytesReceivedR4;
	}

	public String getAppBytesReceivedLTE() {
		return appBytesReceivedLTE;
	}

	public void setAppBytesReceivedLTE(String appBytesReceivedLTE) {
		this.appBytesReceivedLTE = appBytesReceivedLTE;
	}

	public String getAppBytesSendTotal() {
		return appBytesSendTotal;
	}

	public void setAppBytesSendTotal(String appBytesSendTotal) {
		this.appBytesSendTotal = appBytesSendTotal;
	}

	public String getAppBytesSendHSUPA() {
		return appBytesSendHSUPA;
	}

	public void setAppBytesSendHSUPA(String appBytesSendHSUPA) {
		this.appBytesSendHSUPA = appBytesSendHSUPA;
	}

	public String getAppBytesSendR4() {
		return appBytesSendR4;
	}

	public void setAppBytesSendR4(String appBytesSendR4) {
		this.appBytesSendR4 = appBytesSendR4;
	}

	public String getAppBytesSendLTE() {
		return appBytesSendLTE;
	}

	public void setAppBytesSendLTE(String appBytesSendLTE) {
		this.appBytesSendLTE = appBytesSendLTE;
	}

	public String getApp_currentThroughputdl() {
		return app_currentThroughputdl;
	}

	public void setApp_currentThroughputdl(String app_currentThroughputdl) {
		this.app_currentThroughputdl = app_currentThroughputdl;
	}

	public String getApp_currentThroughputul() {
		return app_currentThroughputul;
	}

	public void setApp_currentThroughputul(String app_currentThroughputul) {
		this.app_currentThroughputul = app_currentThroughputul;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	@Override
	public long getTime() {
		return time;
	}

}
