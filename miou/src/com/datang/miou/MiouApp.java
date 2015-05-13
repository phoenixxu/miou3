package com.datang.miou;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.os.Handler;

import com.baidu.mapapi.model.LatLng;
import com.datang.miou.datastructure.*;
import com.datang.miou.views.gen.ColorPoints;
import com.datang.miou.views.gen.ParamPoint;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.datang.business.MeasurementScheduler;
import com.datang.business.UpdateIntent;
import com.datang.business.util.Logger;

/**
 * 
 * 
 * @author suntongwei
 */
public class MiouApp extends Application {
    public static MiouApp APP;
	private boolean isGenTesting;
	private boolean isGenReviewing;
	private boolean isGenLogging;
	private boolean isHoldLastServerRunning;
	private Handler mHandler;
	
	public Activity mGenActivity;
	
	private List<TestCommand> mTestSchemes;
	
	private List<Event> mGenEvents = new ArrayList<Event>();
	private List<Signal> mGenSignals = new ArrayList<Signal>();
	
	private List<EventType> mSelectedEventTypes = new ArrayList<EventType>();
	
	// 一组折线绘制配置，每次指标发生重要变化则启用一个新的绘制配置，绘制不同颜色的折线
	// 覆盖轨迹 	质量轨迹 	速度轨迹
	// LTE		GSM		TD
	// 无法使用泛型数组？
	private ArrayList<ColorPoints> mColorPointsListForCoverLTE = new ArrayList<ColorPoints>();
	private ArrayList<ColorPoints> mColorPointsListForCoverTD = new ArrayList<ColorPoints>();
	private ArrayList<ColorPoints> mColorPointsListForCoverGSM = new ArrayList<ColorPoints>();
	
	private ArrayList<ColorPoints> mColorPointsListForQualityLTE = new ArrayList<ColorPoints>();
	private ArrayList<ColorPoints> mColorPointsListForQualityTD = new ArrayList<ColorPoints>();
	private ArrayList<ColorPoints> mColorPointsListForQualityGSM = new ArrayList<ColorPoints>();
	
	private ArrayList<ColorPoints> mColorPointsListForSpeedUp = new ArrayList<ColorPoints>();
	private ArrayList<ColorPoints> mColorPointsListForSpeedDown = new ArrayList<ColorPoints>();
	
	public ArrayList<ColorPoints> getColorPointsListForCoverLTE() {
		return mColorPointsListForCoverLTE;
	}

	public void setColorPointsListForCoverLTE(
			ArrayList<ColorPoints> colorPointsListForCoverLTE) {
		mColorPointsListForCoverLTE = colorPointsListForCoverLTE;
	}

	public ArrayList<ColorPoints> getColorPointsListForCoverTD() {
		return mColorPointsListForCoverTD;
	}

	public void setColorPointsListForCoverTD(
			ArrayList<ColorPoints> colorPointsListForCoverTD) {
		mColorPointsListForCoverTD = colorPointsListForCoverTD;
	}

	public ArrayList<ColorPoints> getColorPointsListForCoverGSM() {
		return mColorPointsListForCoverGSM;
	}

	public void setColorPointsListForCoverGSM(
			ArrayList<ColorPoints> colorPointsListForCoverGSM) {
		mColorPointsListForCoverGSM = colorPointsListForCoverGSM;
	}

	public ArrayList<ColorPoints> getColorPointsListForQualityLTE() {
		return mColorPointsListForQualityLTE;
	}

	public void setColorPointsListForQualityLTE(
			ArrayList<ColorPoints> colorPointsListForQualityLTE) {
		mColorPointsListForQualityLTE = colorPointsListForQualityLTE;
	}

	public ArrayList<ColorPoints> getColorPointsListForQualityTD() {
		return mColorPointsListForQualityTD;
	}

	public void setColorPointsListForQualityTD(
			ArrayList<ColorPoints> colorPointsListForQualityTD) {
		mColorPointsListForQualityTD = colorPointsListForQualityTD;
	}

	public ArrayList<ColorPoints> getColorPointsListForQualityGSM() {
		return mColorPointsListForQualityGSM;
	}

	public void setColorPointsListForQualityGSM(
			ArrayList<ColorPoints> colorPointsListForQualityGSM) {
		mColorPointsListForQualityGSM = colorPointsListForQualityGSM;
	}

	public ArrayList<ColorPoints> getColorPointsListForSpeedUp() {
		return mColorPointsListForSpeedUp;
	}

	public void setColorPointsListForSpeedUp(
			ArrayList<ColorPoints> colorPointsListForSpeedUP) {
		mColorPointsListForSpeedUp = colorPointsListForSpeedUP;
	}

	public ArrayList<ColorPoints> getColorPointsListForSpeedDown() {
		return mColorPointsListForSpeedDown;
	}

	public void setColorPointsListForSpeedDown(
			ArrayList<ColorPoints> colorPointsListForSpeedDown) {
		mColorPointsListForSpeedDown = colorPointsListForSpeedDown;
	}

	private MapLayerOptions mMapLayerOptions = new MapLayerOptions();
	
	// 轨迹点列表
	private List<ParamPoint> mTracePoints = new ArrayList<ParamPoint>();	
	
	// 最后一个轨迹点
	private LatLng mLastPoint;

	public boolean isGenTesting() {
		return isGenTesting;
	}

	public void setGenTesting(boolean isGenTesting) {
		this.isGenTesting = isGenTesting;
	}

	public boolean isGenReviewing() {
		return isGenReviewing;
	}

	public void setGenReviewing(boolean isGenReviewing) {
		this.isGenReviewing = isGenReviewing;
	}

	public List<TestCommand> getTestSchemes() {
		return mTestSchemes;
	}

	public void setTestSchemes(List<TestCommand> testSchemes) {
		mTestSchemes = testSchemes;
	}

	public List<Signal> getGenSignals() {
		return mGenSignals;
	}

	public void setGenSignals(List<Signal> genSignals) {
		mGenSignals = genSignals;
	}

	public List<Event> getGenEvents() {
		return mGenEvents;
	}

	public void setGenEvents(List<Event> genEvents) {
		mGenEvents = genEvents;
	}

	public List<EventType> getSelectedEventTypes() {
		return mSelectedEventTypes;
	}

	public void setSelectedEventTypes(List<EventType> selectedEventTypes) {
		mSelectedEventTypes = selectedEventTypes;
	}

	public boolean isGenLogging() {
		return isGenLogging;
	}

	public void setGenLogging(boolean isGenLogging) {
		this.isGenLogging = isGenLogging;
	}

	public boolean isHoldLastServerRunning() {
		return isHoldLastServerRunning;
	}

	public void setHoldLastServerRunning(boolean isHoldLastServerRunning) {
		this.isHoldLastServerRunning = isHoldLastServerRunning;
	}

	public MapLayerOptions getMapLayerOptions() {
		return mMapLayerOptions;
	}

	public void setMapLayerOptions(MapLayerOptions mapLayerOptions) {
		mMapLayerOptions = mapLayerOptions;
	}

	public List<ParamPoint> getTracePoints() {
		return mTracePoints;
	}

	public void setTracePoints(List<ParamPoint> tracePoints) {
		mTracePoints = tracePoints;
	}

	public LatLng getLastPoint() {
		return mLastPoint;
	}

	public void setLastPoint(LatLng lastPoint) {
		mLastPoint = lastPoint;
	}

	public Handler getHandler() {
		// TODO Auto-generated method stub
		return mHandler;
	}
	
	public void setHandler(Handler handler) {
		this.mHandler = handler;
	}
	
	//	add via chenzm
	 /** 
     * 判断当前网络是否可用
     * 返回：true 可用；false 不可用
     */ 
	public boolean isAvailableNet()
	{
		ConnectivityManager mgr = (ConnectivityManager)this.getApplicationContext().getSystemService(this.CONNECTIVITY_SERVICE);
		if(mgr == null)
		{
			return false;
		}
		NetworkInfo networkInfo = mgr.getActiveNetworkInfo();
		if(networkInfo == null || !networkInfo.isAvailable())
		{
			return false;
		}
		return true;
	}
	//	add via chenzm end
    @Override
    public void onCreate() {
        super.onCreate();
        APP = this;
    }
}
