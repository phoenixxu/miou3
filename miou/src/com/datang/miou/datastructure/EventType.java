package com.datang.miou.datastructure;

import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;

import com.datang.miou.R;

/*
 * 事件类型
 */
public class EventType implements Serializable{

	private static final long serialVersionUID = -187395469428464043L;
	private static final String JSON_ID = "id";
	private static final String JSON_DESCRIPTION = "description";
	private static final String JSON_COLOR = "color";
	
	//事件ID
	private int mId;
	//事件描述
	private String mDescription;
	//事件对应条目背景色
	private int mColor = R.color.black;
	//在待选事件列表中，该事件是否被选中
	private boolean mSelected;
	//在已选事件列表中，该事件是否将要被移除
	private boolean mWillRemove;
	
	public EventType(String des, int id) {
		this.mId = id;
		this.mDescription = des;
		this.mSelected = false;
		this.mWillRemove = false;
	}
	
	public EventType(JSONObject json) throws JSONException {
		this.mId = json.getInt(JSON_ID);
		this.mColor = json.getInt(JSON_COLOR);
		this.mDescription = json.getString(JSON_DESCRIPTION);
		this.mSelected = false;
		this.mWillRemove = false;
	}
	
	public int getId() {
		return mId;
	}
	
	public void setId(int mId) {
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
		JSONObject json = new JSONObject();
		json.put(JSON_ID, this.mId);
		json.put(JSON_DESCRIPTION, this.mDescription);
		json.put(JSON_COLOR, this.mColor);
		return json;
	}
}
