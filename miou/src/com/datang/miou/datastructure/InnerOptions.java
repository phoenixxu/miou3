package com.datang.miou.datastructure;

import java.util.ArrayList;

import android.net.Uri;

public class InnerOptions {
	//建筑物
	private Building mBuilding;
	//层数
	private int mFloor;
	//室内测试图片表
	private ArrayList<Uri> mImages;
	//室内测试地图表
	private ArrayList<InnerMap> mInnerMaps;
	
	private static InnerOptions sInnerOptions;
	
	private InnerOptions() {
		mImages = new ArrayList<Uri>();
		mInnerMaps = new ArrayList<InnerMap>();
		mBuilding = new Building("默认建筑物");
		mFloor = 3;
	}
	
	public static InnerOptions newInstance() {
		if (sInnerOptions != null) {
			return sInnerOptions;
		}
		return new InnerOptions();
	}

	public Building getBuilding() {
		return mBuilding;
	}

	public void setBuilding(Building mBuilding) {
		this.mBuilding = mBuilding;
	}

	public int getFloor() {
		return mFloor;
	}

	public void setFloor(int mFloor) {
		this.mFloor = mFloor;
	}

	public ArrayList<Uri> getImages() {
		return mImages;
	}

	public ArrayList<InnerMap> getInnerMaps() {
		return mInnerMaps;
	}

	public void setImages(ArrayList<Uri> mImages) {
		this.mImages = mImages;
	}

	public void setInnerMaps(ArrayList<InnerMap> mInnerMaps) {
		this.mInnerMaps = mInnerMaps;
	}
	
	
}
