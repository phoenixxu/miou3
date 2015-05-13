package com.datang.miou.nlog.msg.cmcc;

import com.datang.miou.nlog.msg.ILogMsg;

/**
 * @author suntongwei
 * @version 1.0.0
 */
public class LteEvtInfo implements ILogMsg {

    /**
     * 时间
     */
    private Long time;
    /**
     * Event
     */
    private String event;
    /**
     * EventInfo
     */
    private String eventInfo;
    /**
     * 文件已下载大小
     */
    private String fileSize;

    /**
     * 该记录标识
     */
    public String identity() {
        return "LTEEVT";
    }

    @Override
    public String toLogMsg() {
        String logStr = "LTEEVT\t" + getEvent() + "\t" + getEventInfo();
        if (getFileSize() != null && !"".equals(getFileSize())) {
            logStr += "_" + getFileSize();
        }
        return logStr + "\r\n";
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getEventInfo() {
        return eventInfo;
    }

    public void setEventInfo(String eventInfo) {
        this.eventInfo = eventInfo;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    @Override
    public long getTime() {
        return time;
    }
}
