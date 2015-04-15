package com.datang.miou.nlog.msg;

/**
 * 
 * 
 * @author suntongwei
 * @version 1.0.0
 */
public interface ILogMsg {

	/**
	 * 返回LOG信息
	 * 
	 * @return
	 */
	public String toLogMsg();
	/**
	 * 返回时间
	 * 
	 * @return
	 */
	public long getTime();
	/**
	 * 该记录标识
	 */
	public String identity();
}
