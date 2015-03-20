package com.datang.miou.views.gen;

import java.util.ArrayList;
import android.os.Bundle;
import android.util.Log;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.datang.miou.R;
import com.datang.miou.datastructure.Globals;
import com.datang.miou.datastructure.MapLayerOptions;
import com.datang.miou.datastructure.RealData;

public class MapUpdater {
	public static final String EXTRA_MARKER_TYPE = "extra_marker_type";
	
	public static final int MARKER_BASE_STATION = 0;
	public static final int MARKER_EXCEPTION_LTE_DROP = 1;
	public static final int MARKER_EXCEPTION_LTE_OFFLINE = 2;
	public static final int MARKER_EXCEPTION_LTE_ACCESS_FAIL = 3;
	public static final int MARKER_EXCEPTION_LTE_PDP_ACTIVE_FAIL = 4;
	public static final int MARKER_EXCEPTION_TD_DROP= 5;
	public static final int MARKER_EXCEPTION_TD_OFFLINE = 6;
	public static final int MARKER_EXCEPTION_TD_ACCESS_FAIL = 7;
	public static final int MARKER_EXCEPTION_TD_PDP_ACTIVE_FAIL_= 8;
	public static final int MARKER_EXCEPTION_GSM_DROP= 9;
	public static final int MARKER_EXCEPTION_GSM_OFFLINE = 10;
	public static final int MARKER_EXCEPTION_GSM_ACCESS_FAIL = 11;
	public static final int MARKER_EXCEPTION_GSM_PDP_ACTIVE_FAIL_ = 12;

	private static final String TAG = "MapUpdater";

	public static final int TRACE_TYPE_COVER = 0;
	public static final int TRACE_TYPE_QUALITY = 1;
	public static final int TRACE_TYPE_SPEED = 2;
	
	@SuppressWarnings("unused")
	private int[] mLineColors = { 0xAAFFFF00, 0xAAFF0000, 0xAA000000, 0xAAFFFFFF, 0x00AA0000, 0x0000AA00, 0X000000AA };
	
	//图标:基站和异常点
	private BitmapDescriptor bdStation;
	private BitmapDescriptor[] bdExceptions;
	
	private class ExceptionPoint {
		private LatLng position;
		private int type;
		
		@SuppressWarnings("unused")
		public ExceptionPoint(LatLng position, int type) {
			this.position = position;
			this.type = type;
		}
		
		@SuppressWarnings("unused")
		public LatLng getPosition() {
			return this.position;
		}
		
		@SuppressWarnings("unused")
		public int getType() {
			return this.type;
		}
	}
	@SuppressWarnings("unused")
	private ArrayList<ExceptionPoint> mExceptionPoints = new ArrayList<ExceptionPoint>();
	
	//色彩点集，指定了一组处于同一指标水平的坐标点，他们在轨迹上具有同一种颜色
	private class PointsWithColor {
		//该点集对应的Overlay，方便删除
		private Overlay overlay;
		//对某一指标有相同范围值的点集
		private ArrayList<LatLng> pointsList;
		//指标范围类型
		private int type;
		
		public PointsWithColor(int type) {
			this.type = type;
			this.pointsList = new ArrayList<LatLng>();
		}
		
		public void addPoint(LatLng point) {
			this.pointsList.add(point);
		}
		
		public ArrayList<LatLng> getPointsList() {
			return this.pointsList;
		}
		
		public Overlay getOverlay() {
			return this.overlay;
		}
		
		public void setOverlay(Overlay overlay) {
			this.overlay = overlay;
		}
		
		public int getType() {
			return type;
		}
	}
	//一组折线绘制配置，每次指标发生重要变化则启用一个新的绘制配置，绘制不同颜色的折线
	private static ArrayList<PointsWithColor> mPointsWithColorListForCover = new ArrayList<PointsWithColor>();
	private static ArrayList<PointsWithColor> mPointsWithColorListForQuality = new ArrayList<PointsWithColor>();
	private static ArrayList<PointsWithColor> mPointsWithColorWithColorsForSpeed = new ArrayList<PointsWithColor>();

	private BaiduMap mBaiduMap;
	private LatLng mLastPoint;
	private static MapUpdater sMapUpdater;
	private MapLayerOptions mMapLayerOptions;

	//基站信息Overlay集
	private static ArrayList<Overlay> mStationInfoOverlays;
	//基站Overlay集
	private static ArrayList<Marker> mStationOverlays;
	//当前点与基站连线的Overlay
	private static Overlay mLineToCellOverlay;
	
	public static MapUpdater newInstance(BaiduMap baiduMap) {
		if (sMapUpdater != null) {
			return sMapUpdater;
		}
		return new MapUpdater(baiduMap);
	}
	
	private MapUpdater(BaiduMap baiduMap) {
		this.mBaiduMap = baiduMap;
		init();
	}
		
	public MapLayerOptions getMapLayerOptions() {
		return mMapLayerOptions;
	}

	public void setMapLayerOptions(MapLayerOptions mMapLayerOptions) {
		this.mMapLayerOptions = mMapLayerOptions;
	}

	public LatLng getLastPoint() {
		return mLastPoint;
	}

	public void setLastPoint(LatLng mLastPoint) {
		this.mLastPoint = mLastPoint;
	}

	private void init() {
		//基站图标
		bdStation = BitmapDescriptorFactory.fromResource(R.drawable.icon_mark_station);
		//异常事件点图标
		bdExceptions = new BitmapDescriptor[] {
			BitmapDescriptorFactory.fromResource(R.drawable.icon_mark_exception),
			BitmapDescriptorFactory.fromResource(R.drawable.icon_mark_exception),
			BitmapDescriptorFactory.fromResource(R.drawable.icon_mark_exception),
			BitmapDescriptorFactory.fromResource(R.drawable.icon_mark_exception),
			BitmapDescriptorFactory.fromResource(R.drawable.icon_mark_exception),
			BitmapDescriptorFactory.fromResource(R.drawable.icon_mark_exception),
			BitmapDescriptorFactory.fromResource(R.drawable.icon_mark_exception),
			BitmapDescriptorFactory.fromResource(R.drawable.icon_mark_exception),
			BitmapDescriptorFactory.fromResource(R.drawable.icon_mark_exception),
			BitmapDescriptorFactory.fromResource(R.drawable.icon_mark_exception),
			BitmapDescriptorFactory.fromResource(R.drawable.icon_mark_exception),
			BitmapDescriptorFactory.fromResource(R.drawable.icon_mark_exception),	
		};
	}
	
	//当接收到测试数据时，结合当前位置更新地图
	public void updateMapOnReceiveDataPacket(RealData data) {
		LatLng ll = data.position;
		
		updateCoverTrace(data);
		
		updateQualityTrace(data);
		
		updateSpeedTrace(data);
		
		if (mMapLayerOptions.hasLineToCellLayer()) {
			addLineToCellLayer(data);
		}
		
		addExceptionEvent(data);
	}

	private void addExceptionEvent(RealData data) {
		// TODO 自动生成的方法存根
		int type = getLastException();
		if (type > 0) {
			OverlayOptions oo = new MarkerOptions().position(data.position).icon(bdExceptions[type])
					.zIndex(9).draggable(true);
			Marker marker = (Marker) (mBaiduMap.addOverlay(oo));
			Bundle args = new Bundle();
			args.putInt(EXTRA_MARKER_TYPE, MARKER_EXCEPTION_LTE_DROP);
			marker.setExtraInfo(args);
		}
	}

	//测试函数
	private int getLastException() {
		// TODO 自动生成的方法存根
		return -1;
	}

	//更新速率轨迹
	private void updateSpeedTrace(RealData data) {
		// TODO 自动生成的方法存根
		updateTrace(data, TRACE_TYPE_SPEED, mMapLayerOptions.hasSpeedTraceLayer());
	}

	//更新质量轨迹
	private void updateQualityTrace(RealData data) {
		// TODO 自动生成的方法存根
		updateTrace(data, TRACE_TYPE_QUALITY, mMapLayerOptions.hasQualityTraceLayer());
	}

	//更新覆盖轨迹
	private void updateCoverTrace(RealData data) {
		updateTrace(data, TRACE_TYPE_COVER, mMapLayerOptions.hasCoverTraceLayer());
	}
	
	//清除轨迹
	public void clearTrace(int traceType) {
		// TODO 自动生成的方法存根
		ArrayList<PointsWithColor> pointsWithColorList = getPointsWithColorList(traceType);
		for (PointsWithColor pointsWithColor : pointsWithColorList) {
			pointsWithColor.getOverlay().remove();
		}
	}

	//更新轨迹
	private void updateTrace(RealData data, int traceType, boolean isVisible) {
		// TODO 自动生成的方法存根
		/*
		 * 这里需要根据实时指标的数值确定画线颜色
		 * 每一种状态使用一个OverLay，画线颜色不同
		 * 改变位置，若状态相同，则追加点，若状态不同，则新建Overlay
		 */
		ArrayList<PointsWithColor> pointsWithColorList = getPointsWithColorList(traceType);
		LatLng ll = data.position;
		int type = getRealIndex(traceType);
		if (pointsWithColorList.size() > 0) {
			if (type == pointsWithColorList.get(pointsWithColorList.size() - 1).type) {
				PointsWithColor pointsWithColors = pointsWithColorList.get(pointsWithColorList.size() - 1);
				ArrayList<LatLng> points = pointsWithColors.getPointsList();
				points.add(ll);
				if (isVisible) {
					pointsWithColors.getOverlay().remove();
					PolylineOptions options =  new PolylineOptions().width(5).color(Globals.mapLineColor[type]).points(pointsWithColors.getPointsList());
					Overlay overlay = mBaiduMap.addOverlay(options);
					pointsWithColors.setOverlay(overlay);	
				}
				
			} else {
				addNewOverlay(ll, traceType, type, isVisible);									
			}
		} else {
			addNewOverlay(ll, traceType, type, isVisible);
		}
	}

	private void addNewOverlay(LatLng ll, int traceType, int type, boolean isVisible) {
		// TODO 自动生成的方法存根
		ArrayList<PointsWithColor> pointsWithColorList = getPointsWithColorList(traceType);
		PointsWithColor pointsWithColor = new PointsWithColor(type);
		int color = Globals.mapLineColor[type];
		pointsWithColor.addPoint(mLastPoint);
		pointsWithColor.addPoint(ll);
		pointsWithColorList.add(pointsWithColor);
		if (isVisible) {
			PolylineOptions ooPolyline = new PolylineOptions().width(5).color(color).points(pointsWithColor.getPointsList());
			Overlay overlay = mBaiduMap.addOverlay(ooPolyline);
			pointsWithColor.setOverlay(overlay);	
		}
	}

	//获取对应轨迹的颜色点集
	private ArrayList<PointsWithColor> getPointsWithColorList(int traceType) {
		switch (traceType) {
			case TRACE_TYPE_COVER:
				return mPointsWithColorListForCover;
			case TRACE_TYPE_QUALITY:
				return mPointsWithColorListForQuality;
			case TRACE_TYPE_SPEED:
				return mPointsWithColorWithColorsForSpeed;
			default:
				return null;
		}
	}

	//测试函数
	private int getRealIndex(int traceType) {
		// TODO 自动生成的方法存根
		switch (traceType) {
			case TRACE_TYPE_COVER:
				return (int) (Math.random() * 2);
			case TRACE_TYPE_QUALITY:
				return (int) (Math.random() * 4);
			case TRACE_TYPE_SPEED:
				return (int) (Math.random() * 6);
			default:
				return 0;
	}
	}
	
	public void reDrawTraces() {
		if (mMapLayerOptions.hasCoverTraceLayer()) {
			reDrawSingleTrace(TRACE_TYPE_COVER);
		}
		if (mMapLayerOptions.hasQualityTraceLayer()) {
			reDrawSingleTrace(TRACE_TYPE_QUALITY);
		}
		if (mMapLayerOptions.hasSpeedTraceLayer()) {
			reDrawSingleTrace(TRACE_TYPE_SPEED);
		}
	}
	
	private void reDrawSingleTrace(int traceType) {
		ArrayList<PointsWithColor> pointsWithColorList = getPointsWithColorList(traceType);
		for (PointsWithColor pointsWithColors : pointsWithColorList) {
			if (pointsWithColors.getOverlay() != null) {
				pointsWithColors.getOverlay().remove();
			}
			PolylineOptions option = new PolylineOptions()
			.width(5)
			.color(Globals.mapLineColor[pointsWithColors.getType()])
			.points(pointsWithColors.getPointsList());
			Overlay overlay = mBaiduMap.addOverlay(option);
			pointsWithColors.setOverlay(overlay);
		}
	}
	
	public void addBaseStationLayer() {
		// TODO 自动生成的方法存根
		if (mMapLayerOptions.hasStationLayer()) {
			mStationOverlays = new ArrayList<Marker>(); 
			ArrayList<LatLng> stations = new ArrayList<LatLng>();
			stations.add(new LatLng(31.227, 121.481));
			stations.add(new LatLng(32.227, 121.481));
			stations.add(new LatLng(31.227, 122.481));
			
			for (LatLng ll : stations) {
				OverlayOptions option = new MarkerOptions().position(ll).icon(bdStation).zIndex(9).draggable(true);
				Overlay overlay = mBaiduMap.addOverlay(option);
				Marker marker = (Marker) (overlay);
				mStationOverlays.add(marker);
				Bundle args = new Bundle();
				args.putInt(EXTRA_MARKER_TYPE, MARKER_BASE_STATION);
				marker.setExtraInfo(args);
			}
		}
	}
	
	public void removeBaseStationLayer() {
		for (int i = mStationOverlays.size() - 1; i >= 0; i--) {
			mStationOverlays.get(i).remove();
			mStationOverlays.remove(i);
			Log.i(TAG, "remove station overlay");
		}
	}

	public void addBaseStationInfoLayer() {
		// TODO 自动生成的方法存根
		if (mMapLayerOptions.hasStationInfoLayer()) {
			mStationInfoOverlays = new ArrayList<Overlay>(); 
			ArrayList<LatLng> stations = new ArrayList<LatLng>();
			stations.add(new LatLng(31.227, 121.481));
			stations.add(new LatLng(32.227, 121.481));
			stations.add(new LatLng(31.227, 122.481));
			
			for (LatLng ll : stations) {
				OverlayOptions ooText = new TextOptions()
						.fontSize(24)
						.fontColor(0xFFFF00FF)
						.text(ll.latitude + ", " + ll.longitude)
						.position(ll);
				Overlay overlay = mBaiduMap.addOverlay(ooText);
				mStationInfoOverlays.add(overlay);
			}
		}
	}

	public void removeBaseStationInfoLayer() {
		// TODO 自动生成的方法存根
		for (int i = mStationInfoOverlays.size() - 1; i >= 0; i--) {
			mStationInfoOverlays.get(i).remove();
			mStationInfoOverlays.remove(i);
			Log.i(TAG, "remove station info overlay");
		}
	}

	public void addLineToCellLayer(RealData data) {
		// TODO 自动生成的方法存根
		removeLineToCellLayer();
		LatLng current;
		if (data == null) {
			current = mLastPoint;
		} else {
			current = data.position;
		}
		LatLng cell = getCellPosition();
		ArrayList<LatLng> points = new ArrayList<LatLng>();
		points.add(current);
		points.add(cell);
		
		PolylineOptions option = new PolylineOptions()
		.width(5)
		.color(0xAAFF0000)
		.points(points);
		mLineToCellOverlay = mBaiduMap.addOverlay(option);
		
	}

	//测试函数
	private LatLng getCellPosition() {
		// TODO 自动生成的方法存根
		return new LatLng(31.227, 121.481);
	}

	public void removeLineToCellLayer() {
		// TODO 自动生成的方法存根
		if (mLineToCellOverlay != null) {
			mLineToCellOverlay.remove();
		}
	}
}
