package com.datang.miou.nlog;

/**
 * 业务类型
 * 
 * @author suntongwei
 * @version 1.0.0
 */
public interface ServiceType {

	/**
	 * 默认初始状态
	 */
	public static final int INIT = -1;
	/**
	 * FTP下载
	 */
	public static final int FTP_DOWN = 1;
	/**
	 * FTP上传
	 */
	public static final int FTP_UP = 2;
	/**
	 * PING业务
	 */
	public static final int PING = 3;
	/**
	 * IDLE态
	 */
	public static final int IDLE = 0;
}
