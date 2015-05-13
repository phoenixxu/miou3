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
	private String mId;
	//事件描述
	private String mDescription;
	//事件对应条目背景色
	private int mColor = R.color.black;
	//在待选事件列表中，该事件是否被选中
	private boolean mSelected;
	//在已选事件列表中，该事件是否将要被移除
	private boolean mWillRemove;
	
	public EventType() {
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return mId;
	}
	
	public void setId(String mId) {
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
}
