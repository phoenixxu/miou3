package com.datang.miou.datastructure;

import java.io.Serializable;

import com.baidu.mapapi.model.LatLng;

@SuppressWarnings("serial")
public class RealData implements Serializable{
	public double[] params;
	public LatLng position;
	
	public RealData() {
		params = new double[Globals.MAX_PARAMS];
		position = null;
	}
	
	
}
