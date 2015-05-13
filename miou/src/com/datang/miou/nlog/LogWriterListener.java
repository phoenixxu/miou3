package com.datang.miou.nlog;

import com.datang.miou.nlog.msg.ILogMsg;

/**
 * 日志操作接口
 * 
 * @author suntongwei
 * @version 1.0.0
 */
public interface LogWriterListener {

	/**
	 * 写入日志消息之前
	 * 
	 * @param msg
	 */
	public void onBeforeLogWriter(ILogMsg msg);
	
	/**
	 * 日志消息已写入队列之后
	 * 
	 * @param msg
	 */
	public void onAfterLogWriter(ILogMsg msg);
}
