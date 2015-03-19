package com.datang.miou.datastructure;

import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

public class EventType {
	private static final String JSON_ID = "id";
	private static final String JSON_DESCRIPTION = "description";
	private static final String JSON_COLOR = "color";
	
	//事件ID
	//private int mId;
	private UUID mId;
	//事件描述
	private String mDescription;
	//事件对应条目背景色，实际颜色存放在SharePreference里
	private int mColor;
	//在待选事件列表中，该事件是否被选中
	private boolean mSelected;
	//在已选事件列表中，该事件是否将要被移除
	private boolean mWillRemove;
	//用来递增的生成事件ID
	//private static int count = 0;
	
	public EventType(String des, int color) {
		//this.mId = count;	
		this.mId = UUID.randomUUID();
		this.mDescription = des;
		this.mColor = color;
		this.mSelected = false;
		this.mWillRemove = false;
		//count++;
	}
	
	public EventType(JSONObject json) throws JSONException {
		this.mId = UUID.fromString(json.getString(JSON_ID));
		this.mColor = json.getInt(JSON_COLOR);
		this.mDescription = json.getString(JSON_DESCRIPTION);
		this.mSelected = false;
		this.mWillRemove = false;
	}
	
	public UUID getId() {
		return mId;
	}
	public void setId(UUID mId) {
		this.mId = mId;
	}
	public String getDescription() {
		return mDescription;
	}
	public void setDescription(String mDescription) {
		this.mDescription = mDescription;
	}
	public int getColor() {
		return mColor;
	}
	public void setColor(int mColor) {
		this.mColor = mColor;
	}
	public boolean isSelected() {
		return mSelected;
	}
	public void setSelected(boolean mSelected) {
		this.mSelected = mSelected;
	}

	public boolean isWillRemove() {
		return mWillRemove;
	}
	public void setWillRemove(boolean mWillRemove) {
		this.mWillRemove = mWillRemove;
	}

	public JSONObject toJSON() throws JSONException {
		// TODO 自动生成的方法存根
		JSONObject json = new JSONObject();
		json.put(JSON_ID, this.mId.toString());
		json.put(JSON_DESCRIPTION, this.mDescription);
		json.put(JSON_COLOR, this.mColor);
		return json;
	}
}
