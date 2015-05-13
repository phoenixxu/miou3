package com.datang.miou.nlog;

import java.util.ArrayList;
import java.util.List;

/**
 * 日志全局状态控制
 *
 * @author suntongwei
 * @version 1.0.0
 */
public class LogWriterStatus {

    /**
     * 当前状态
     */
    private LogStatus currentStatus = LogStatus.Unknow;
    /**
     * 前一次状态
     */
    private LogStatus lastStatus = null;

    /**
     * 监听器集合
     */
    private List<LogWriterStatusListener> listeners = new ArrayList<LogWriterStatusListener>();

    /**
     * 判断当前日志是否在运行，不包含暂停状态，即暂停状态也返回True
     *
     * @return
     */
    public boolean isRun() {
        return currentStatus == LogStatus.Start;
    }

    /**
     * 判断当前日志是否在进行中，即日志启动则返回True
     *
     * @return
     */
    public boolean isStart() {
        return currentStatus == LogStatus.Start
                || currentStatus == LogStatus.Pause
                || currentStatus == LogStatus.Stoping;
    }

    public void setOnLogWriterStatusListener(LogWriterStatusListener l) {
        listeners.add(l);
    }

    public void removeLogWriterStatusListener(LogWriterStatusListener l) {
        listeners.remove(l);
    }

    /**
     * LOG记录状态接口
     *
     * @author suntongwei
     * @version 1.0.0
     */
    public interface LogWriterStatusListener {
        /**
         * 当前状态
         *
         * @param status
         */
        public void onLogStatusChange(LogWriterStatus logStatus);
    }

    public LogStatus getStatus() {
        return currentStatus;
    }

    public void setStatus(LogStatus currentStatus) {
        this.lastStatus = this.currentStatus;
        this.currentStatus = currentStatus;
        // 通知状态改变
        if (listeners.size() > 0) {
            for (LogWriterStatusListener l : listeners) {
                if (l == null) continue;
                l.onLogStatusChange(this);
            }
        }
    }

    public LogStatus getLastStatus() {
        return lastStatus;
    }

    public void setLastStatus(LogStatus lastStatus) {
        this.lastStatus = lastStatus;
    }

    /**
     * LOG状态
     *
     * @author suntongwei
     * @version 1.0.0
     */
    public enum LogStatus {
        Create, Start, Stop, Pause, Stoping, Unknow
    }
}
