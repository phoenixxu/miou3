package com.datang.miou.datastructure;

public class MapLayerOptions {
	//是否显示覆盖轨迹图层
	private boolean hasCoverTraceLayerLTE = false;
	//是否显示质量轨迹图层
	private boolean hasQualityTraceLayerLTE = false;

	//是否显示覆盖轨迹图层
	private boolean hasCoverTraceLayerTD = false;
	//是否显示质量轨迹图层
	private boolean hasQualityTraceLayerTD = false;
	
	//是否显示覆盖轨迹图层
	private boolean hasCoverTraceLayerGSM = false;
	//是否显示质量轨迹图层
	private boolean hasQualityTraceLayerGSM = false;
	
	//是否显示速率轨迹图层
	private boolean hasSpeedTraceLayerUp = false;

	private boolean hasSpeedTraceLayerDown = false;
	
	//是否显示基站图层
	private boolean hasStationLayer = false;
	//是否显示小区连线图层
	private boolean hasLineToCellLayer = false;
	//是否显示基站信息图层
	private boolean hasStationInfoLayer = false;
	
	public boolean hasCoverTraceLayerLTE() {
		return hasCoverTraceLayerLTE;
	}
	public void setHasCoverTraceLayerLTE(boolean hasCoverTraceLayerLTE) {
		this.hasCoverTraceLayerLTE = hasCoverTraceLayerLTE;
	}
	public boolean hasQualityTraceLayerLTE() {
		return hasQualityTraceLayerLTE;
	}
	public void setHasQualityTraceLayerLTE(boolean hasQualityTraceLayerLTE) {
		this.hasQualityTraceLayerLTE = hasQualityTraceLayerLTE;
	}

	public boolean hasCoverTraceLayerTD() {
		return hasCoverTraceLayerTD;
	}
	public void setHasCoverTraceLayerTD(boolean hasCoverTraceLayerTD) {
		this.hasCoverTraceLayerTD = hasCoverTraceLayerTD;
	}
	public boolean hasQualityTraceLayerTD() {
		return hasQualityTraceLayerTD;
	}
	public void setHasQualityTraceLayerTD(boolean hasQualityTraceLayerTD) {
		this.hasQualityTraceLayerTD = hasQualityTraceLayerTD;
	}

	public boolean hasCoverTraceLayerGSM() {
		return hasCoverTraceLayerGSM;
	}
	public void setHasCoverTraceLayerGSM(boolean hasCoverTraceLayerGSM) {
		this.hasCoverTraceLayerGSM = hasCoverTraceLayerGSM;
	}
	public boolean hasQualityTraceLayerGSM() {
		return hasQualityTraceLayerGSM;
	}
	public void setHasQualityTraceLayerGSM(boolean hasQualityTraceLayerGSM) {
		this.hasQualityTraceLayerGSM = hasQualityTraceLayerGSM;
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
	public boolean hasStationInfoLayer() {
		return hasStationInfoLayer;
	}
	public void setHasStationInfoLayer(boolean hasStationInfoLayer) {
		this.hasStationInfoLayer = hasStationInfoLayer;
	}
	public boolean hasSpeedTraceLayerUp() {
		return hasSpeedTraceLayerUp;
	}
	public void setHasSpeedTraceLayerUp(boolean hasSpeedTraceLayerUp) {
		this.hasSpeedTraceLayerUp = hasSpeedTraceLayerUp;
	}
	public boolean hasSpeedTraceLayerDown() {
		return hasSpeedTraceLayerDown;
	}
	public void setHasSpeedTraceLayerDown(boolean hasSpeedTraceLayerDown) {
		this.hasSpeedTraceLayerDown = hasSpeedTraceLayerDown;
	}
	
	
}
