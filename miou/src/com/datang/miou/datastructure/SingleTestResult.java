package com.datang.miou.datastructure;

import java.util.UUID;

/*
 * 单验测试结果
 */
public class SingleTestResult {
	private UUID mId;
	private String mName;
	private long mTime;
	private double mRSRP;
	private double mSINR;
	private double mFtpDownload;
	private double mFtpUpload;
	private double mPingDelay;
	private double mCsfb;
	
	public SingleTestResult() {
		mId = UUID.randomUUID();
	}

	public UUID getId() {
		return mId;
	}

	public void setId(UUID mId) {
		this.mId = mId;
	}

	public String getName() {
		return mName;
	}

	public void setName(String mName) {
		this.mName = mName;
	}

	public long getTime() {
		return mTime;
	}

	public void setTime(long mTime) {
		this.mTime = mTime;
	}

	public double getRSRP() {
		return mRSRP;
	}

	public void setRSRP(double mRSRP) {
		this.mRSRP = mRSRP;
	}

	public double getSINR() {
		return mSINR;
	}

	public void setSINR(double mSINR) {
		this.mSINR = mSINR;
	}

	public double getFtpDownload() {
		return mFtpDownload;
	}

	public void setFtpDownload(double mFtpDownload) {
		this.mFtpDownload = mFtpDownload;
	}

	public double getFtpUpload() {
		return mFtpUpload;
	}

	public void setFtpUpload(double mFtpUpload) {
		this.mFtpUpload = mFtpUpload;
	}

	public double getPingDelay() {
		return mPingDelay;
	}

	public void setPingDelay(double mPingDelay) {
		this.mPingDelay = mPingDelay;
	}

	public double getCsfb() {
		return mCsfb;
	}

	public void setCsfb(double mCsfb) {
		this.mCsfb = mCsfb;
	}
	
	
}
