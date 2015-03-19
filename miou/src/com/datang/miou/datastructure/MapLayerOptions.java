package com.datang.miou.datastructure;

public class MapLayerOptions {
	//是否显示覆盖轨迹图层
	private boolean hasCoverTraceLayer = false;
	//是否显示质量轨迹图层
	private boolean hasQualityTraceLayer = false;
	//是否显示速率轨迹图层
	private boolean hasSpeedTraceLayer = false;
	//是否显示基站图层
	private boolean hasStationLayer = false;
	//是否显示小区连线图层
	private boolean hasLineToCellLayer = false;
	//是否显示基站信息图层
	private boolean hasStationInfoLayer = false;
	
	
	public boolean hasStationInfoLayer() {
		return hasStationInfoLayer;
	}
	
	public void setHasStationInfoLayer(boolean hasStationInfoLayer) {
		this.hasStationInfoLayer = hasStationInfoLayer;
	}
	
	public boolean hasCoverTraceLayer() {
		return hasCoverTraceLayer;
	}
	
	public void setHasCoverTraceLayer(boolean hasCoverTraceLayer) {
		this.hasCoverTraceLayer = hasCoverTraceLayer;
	}
	
	public boolean hasQualityTraceLayer() {
		return hasQualityTraceLayer;
	}
	
	public void setHasQualityTraceLayer(boolean hasQualityTraceLayer) {
		this.hasQualityTraceLayer = hasQualityTraceLayer;
	}
	
	public boolean hasSpeedTraceLayer() {
		return hasSpeedTraceLayer;
	}
	
	public void setHasSpeedTraceLayer(boolean hasSpeedTraceLayer) {
		this.hasSpeedTraceLayer = hasSpeedTraceLayer;
	}
	
	public boolean hasStationLayer() {
		return hasStationLayer;
	}
	
	public void setHasStationLayer(boolean hasStationLayer) {
		this.hasStationLayer = hasStationLayer;
	}
	
	public boolean hasLineToCellLayer() {
		return hasLineToCellLayer;
	}
	
	public void setHasLineToCellLayer(boolean hasLineToCellLayer) {
		this.hasLineToCellLayer = hasLineToCellLayer;
	}
	
	
}
