package com.datang.miou.nlog.msg.cmcc;

import com.datang.miou.nlog.msg.ILogMsg;

/**
 * 
 * 
 * @author suntongwei
 * @version 1.0.0
 */
public class GpsInfo implements ILogMsg {

	/**
	 * 时间戳
	 */
	private Long time;
	/**
	 * 经纬度
	 */
	private String longitude;
	private String latitude;
	/**
	 * 高度
	 */
	private String altitude;
	/**
	 * 速度
	 */
	private String speed;
	
	@Override
	public String toLogMsg() {
		return "GPS\t" + longitude + "\t" + latitude + "\t" + altitude + "\t" + speed + "\r\n";
	}
	
	/**
	 * 该记录标识
	 */
	public String identity() {
		return "GPS";
	}
	
	@Override
	public long getTime() {
		return time;
	}

	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getAltitude() {
		return altitude;
	}
	public void setAltitude(String altitude) {
		this.altitude = altitude;
	}
	public String getSpeed() {
		return speed;
	}
	public void setSpeed(String speed) {
		this.speed = speed;
	}
	public void setTime(Long time) {
		this.time = time;
	}

}
