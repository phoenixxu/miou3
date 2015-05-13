package com.datang.miou.datastructure;

import java.io.Serializable;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

public class Station implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4963793773955390909L;

	private static final String JSON_ID = "json_id";

	private static final String JSON_NAME = "json_name";

	private static final String JSON_LATITUDE = "json_latitude";

	private static final String JSON_LONGITUDE = "json_longitude";

	//基站ID
	private UUID mId;
	
	//基站名称
	private String mName;
	
	//用于判断列表中基站是否被选中的辅助字段
	private boolean mSelected;
	
	//基站位置
	private double mLatitude;
	private double mLongitude;
	
	public Station(String name) {
		this.mId = UUID.randomUUID();
		this.mName = name;
		this.mSelected = false;
	}
	
	public Station(JSONObject json) throws JSONException {
		// TODO Auto-generated constructor stub
		this.mId = UUID.fromString(json.getString(JSON_ID));
		this.mName = json.getString(JSON_NAME);
		if (json.has(JSON_LATITUDE)) {
			this.mLatitude = json.getDouble(JSON_LATITUDE);
		}
		if (json.has(JSON_LONGITUDE)) {
			this.mLongitude = json.getDouble(JSON_LONGITUDE);
		}
		this.mSelected = false;
	}

	public String toString() {
		return this.mName + ": " + this.mLatitude + ", " + this.mLongitude;
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

	public boolean isSelected() {
		return mSelected;
	}

	public void setSelected(boolean mSelected) {
		this.mSelected = mSelected;
	}

	
	public double getLatitude() {
		return mLatitude;
	}

	public void setLatitude(double mLatitude) {
		this.mLatitude = mLatitude;
	}

	public double getLongitude() {
		return mLongitude;
	}

	public void setLongitude(double mLongitude) {
		this.mLongitude = mLongitude;
	}

	public Object toJSON() throws JSONException {
		// TODO Auto-generated method stub
		JSONObject json = new JSONObject();
		json.put(JSON_ID, this.mId.toString());
		json.put(JSON_NAME, this.mName);
		json.put(JSON_LATITUDE, this.mLatitude);
		json.put(JSON_LONGITUDE, this.mLongitude);
		return json;
	}
	
	
}
