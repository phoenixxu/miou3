package com.datang.miou.views.gen.params;

import android.widget.TextView;

public class Column {
	//参数ID
	String mId;
	//子ID，用来区分一组参数中的哪个
	String mSubId;
	//参数显示文本
	String mText;
	//关联的TextView
	TextView mView;
	//宽度权重
	String mWeight;
	

	public String getId() {
		return mId;
	}

	public void setId(String mId) {
		this.mId = mId;
	}

	public String getText() {
		return mText;
	}

	public void setText(String mText) {
		this.mText = mText;
	}

	public TextView getView() {
		return mView;
	}

	public void setView(TextView mView) {
		this.mView = mView;
	}

	public String getSubId() {
		return mSubId;
	}

	public void setSubId(String mSubId) {
		this.mSubId = mSubId;
	}

	public String getWeight() {
		return mWeight;
	}

	public void setWeight(String weight) {
		mWeight = weight;
	}
}
