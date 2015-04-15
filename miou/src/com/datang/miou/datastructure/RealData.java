package com.datang.miou.datastructure;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.baidu.mapapi.model.LatLng;

/*
 * 实时数据
 */
public class RealData implements Serializable{
	
	private static final long serialVersionUID = -323488059208600970L;
	
	//参数MAP
	private Map<String, Double> params;
	//位置信息
	private LatLng position;
	
	public RealData() {
		params = new HashMap<String, Double>();
		position = null;
	}

	public Map<String, Double> getParams() {
		return params;
	}

	public void setParams(String key, double value) {
		this.params.put(key, value);
	}

	public LatLng getPosition() {
		return position;
	}

	public void setPosition(LatLng position) {
		this.position = position;
	}
}
