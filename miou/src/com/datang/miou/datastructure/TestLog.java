package com.datang.miou.datastructure;

public class TestLog {
	//测试日志名称
	private String mName;
	//测试日志文件路径
	private String mPath;
	//测试日志文件大小
	private long mSize;
	//测试耗时
	private long mTimeCost;
	//测试是否产生异常
	private boolean hasException;
	
	public TestLog(String name) {
		this.mName = name;
	}

	public String getName() {
		return mName;
	}

	public void setName(String mName) {
		this.mName = mName;
	}

	public String getPath() {
		return mPath;
	}

	public void setPath(String mPath) {
		this.mPath = mPath;
	}

	public long getSize() {
		return mSize;
	}

	public void setSize(long mSize) {
		this.mSize = mSize;
	}

	public long getTimeCost() {
		return mTimeCost;
	}

	public void setTimeCost(long mTimeCost) {
		this.mTimeCost = mTimeCost;
	}

	public boolean HasException() {
		return hasException;
	}

	public void setHasException(boolean hasException) {
		this.hasException = hasException;
	}
}
