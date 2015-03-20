package com.datang.miou.views.gen;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.model.LatLng;
import com.datang.miou.BaiduMapFragment;
import com.datang.miou.R;
import com.datang.miou.annotation.AfterView;
import com.datang.miou.annotation.AutoView;
import com.datang.miou.datastructure.Globals;
import com.datang.miou.datastructure.MapLayerOptions;
import com.datang.miou.datastructure.RealData;

/**
 * 地图
 * 
 * @author suntongwei
 */
@AutoView(R.layout.gen_map)
public class GenMapFragment extends BaiduMapFragment {

	private static final String TAG = "GenMapFragment";
	
	@AutoView(R.id.gen_map_view)
	private MapView baiduMapView;
	@AutoView(R.id.cover_checkBox)
	private CheckBox mCoverCheckBox;
	@AutoView(R.id.quality_checkBox)
	private CheckBox mQualityCheckBox;
	@AutoView(R.id.speed_checkBox)
	private CheckBox mSpeedCheckBox;
	@AutoView(R.id.station_checkBox)
	private CheckBox mStationCheckBox;
	@AutoView(R.id.line_checkBox)
	private CheckBox mLineCheckBox;
	@AutoView(R.id.station_info_checkBox)
	private CheckBox mStationInfoCheckBox;
	
	private BaiduMap mBaiduMap;
	
	private SDKReceiver mReceiver;
	
	protected LocationClient mLocationClient;
	private LocationMode mCurrentMode;
	public MyLocationListenner myListener = new MyLocationListenner();
	boolean isFirstLoc = true;
	private LocationClient mLocClient;
	
	//轨迹点列表
	private List<LatLng> mTracePoints;	
	//最后一个轨迹点
	private LatLng mLastPoint;
		
	private MapUpdater mMapUpdater;
	private MapLayerOptions mMapLayerOptions;
	
	//回到本页时，后台线程会绘制所有记录的轨迹点，这时如果接受地理位置并追加点会产生问题
	private boolean canAppendPoint = true;
	
	private class ReDrawLinesTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... arg0) {
			canAppendPoint = false;
			mMapUpdater.reDrawTraces();
			mMapUpdater.setLastPoint(mLastPoint);
			canAppendPoint = true;
			return null;
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getActivity().getApplicationContext());
		setRetainInstance(true);
		
		mLastPoint = new LatLng(0, 0);
		mTracePoints = new ArrayList<LatLng>();
		
		//设置地图轨迹线颜色
		Globals.setMapLineColor(Globals.INDEX_LEVEL_ONE, 0xAAFFFF00);
		Globals.setMapLineColor(Globals.INDEX_LEVEL_TWO, 0xAAFF0000);
		Globals.setMapLineColor(Globals.INDEX_LEVEL_THREE, 0xAA000000);
		Globals.setMapLineColor(Globals.INDEX_LEVEL_FOUR, 0xAAFFFFFF);
	}
	
	@Override
	public void onResume() {
		/*
		 * 返回该页面时需要：
		 * 1.定位到最后的位置<不需要>
		 * 2.重绘历史位置连线，这个绘制的工作量太大，需要其他线程
		 */
		super.onResume();
		
		//MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(mLastPoint);
		//mBaiduMap.animateMapStatus(u);
		//如果轨迹点不多于两个则无法画线		
		if (mTracePoints.size() >= 2) {
				new ReDrawLinesTask().execute();
		}
	}

	@AfterView
	public void init() {
		
		// 注册 SDK 广播监听者
		IntentFilter iFilter = new IntentFilter();
		iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
		iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
		mReceiver = new SDKReceiver();
		getActivity().registerReceiver(mReceiver, iFilter);
				
		mBaiduMap = baiduMapView.getMap();
		mMapUpdater = MapUpdater.newInstance(mBaiduMap);
		
		//获得图层显示信息
		mMapLayerOptions = new MapLayerOptions();
		mMapLayerOptions.setHasCoverTraceLayer(mCoverCheckBox.isChecked());
		mMapLayerOptions.setHasQualityTraceLayer(mQualityCheckBox.isChecked());
		mMapLayerOptions.setHasSpeedTraceLayer(mSpeedCheckBox.isChecked());
		mMapLayerOptions.setHasStationLayer(mStationCheckBox.isChecked());
		mMapLayerOptions.setHasLineToCellLayer(mLineCheckBox.isChecked());
		mMapLayerOptions.setHasStationInfoLayer(mStationInfoCheckBox.isChecked());
		mMapUpdater.setMapLayerOptions(mMapLayerOptions);
		
		//一组配置图层是否显示的CheckBox
		mCoverCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				mMapLayerOptions.setHasCoverTraceLayer(isChecked);
				if (isChecked) {
					//显示该图层，则重绘
					new ReDrawLinesTask().execute();
				} else {
					//不显示该图层，则清除
					mMapUpdater.clearTrace(MapUpdater.TRACE_TYPE_COVER);
				}
			}	
		});
		mQualityCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				mMapLayerOptions.setHasQualityTraceLayer(isChecked);
				if (isChecked) {
					new ReDrawLinesTask().execute();
				} else {
					mMapUpdater.clearTrace(MapUpdater.TRACE_TYPE_QUALITY);
				}
			}	
		});
		mSpeedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				mMapLayerOptions.setHasSpeedTraceLayer(isChecked);
				if (isChecked) {
					new ReDrawLinesTask().execute();
				} else {
					mMapUpdater.clearTrace(MapUpdater.TRACE_TYPE_SPEED);
				}
			}	
		});
		mStationCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				mMapLayerOptions.setHasStationLayer(isChecked);
				if (isChecked) {
					mMapUpdater.addBaseStationLayer();
				} else {
					mMapUpdater.removeBaseStationLayer();
				}
			}	
		});
		mStationInfoCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				mMapLayerOptions.setHasStationInfoLayer(isChecked);
				if (isChecked) {
					mMapUpdater.addBaseStationInfoLayer();
				} else {
					mMapUpdater.removeBaseStationInfoLayer();
				}
			}	
		});
		mLineCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				mMapLayerOptions.setHasLineToCellLayer(isChecked);
				if (isChecked) {
					mMapUpdater.addLineToCellLayer(null);
				} else {
					mMapUpdater.removeLineToCellLayer();
				}
			}	
		});
		
		//地图的图标点击事件
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			private InfoWindow mInfoWindow;

			public boolean onMarkerClick(final Marker marker) {
				Bundle args = marker.getExtraInfo();
				int type = args.getInt(MapUpdater.EXTRA_MARKER_TYPE);
				//根据图标类型确定点击事件
				//基站点
				if (type == MapUpdater.MARKER_BASE_STATION) {
					Button button = new Button(getActivity().getApplicationContext());
					button.setBackgroundResource(R.drawable.popup);
					button.setText("我在" + marker.getPosition().latitude + ", " + marker.getPosition().longitude);
					LatLng ll = marker.getPosition();
					mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(button), ll, -47, null);
					mBaiduMap.showInfoWindow(mInfoWindow);
				} 
				//异常事件点
				else if (type == MapUpdater.MARKER_EXCEPTION_GSM_ACCESS_FAIL) {
					
				}
				return true;
			}
		});
		
		mCurrentMode = LocationMode.FOLLOWING;
		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, true, null));
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		
		// 定位初始化
		mLocClient = new LocationClient(getActivity());
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		
		// 打开GPS
		option.setOpenGps(true);
		option.setCoorType("bd09ll"); 
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();	
		
		//添加基站点
		mMapUpdater.addBaseStationLayer();
	}
	
	@Override
	public MapView getMapView() {
		return baiduMapView;
	}
	
	/**
	 * 构造广播监听类，监听 SDK key 验证以及网络异常广播
	 */
	public class SDKReceiver extends BroadcastReceiver {
		@SuppressLint("ShowToast")
		public void onReceive(Context context, Intent intent) {
			String s = intent.getAction();
			if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
				Toast.makeText(getActivity(), "key 验证出错! 请在 AndroidManifest.xml 文件中检查 key 设置", Toast.LENGTH_SHORT);
			} else if (s.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
				Toast.makeText(getActivity(), "网络出错", Toast.LENGTH_SHORT);
			}
		}
	}
	
	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		private static final double KM_RATE = 1.609344;

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || baiduMapView == null)
				return;
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					.direction(location.getDirection())
					.latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
			//只有当再次定位距离大于50米时，才刷新轨迹
			if (getDistance(ll.latitude, ll.longitude, mLastPoint.latitude, mLastPoint.longitude) * KM_RATE > 0.05){
				mTracePoints.add(ll);
				if (isFirstLoc) {
					isFirstLoc = false;		
					MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
					mBaiduMap.animateMapStatus(u);
				} else {
					//从第二次接收地理位置信息开始更新地图轨迹
					//TODO: 添加过滤，如果位置没有明显的变化，除非指标状态变化
					/*
					if (canAppendPoint) {
						mMapUpdater.updateMapOnReceiveLocation(ll);
					}
					*/
				}
			}
			mLastPoint = ll;
			mMapUpdater.setLastPoint(ll);
		}

		public void onReceivePoi(BDLocation poiLocation) {		
		
		}
		
		public double getDistance(double lat1, double lon1, double lat2, double lon2) {
			float[] results = new float[1];
			Location.distanceBetween(lat1, lon1, lat2, lon2, results);
			return results[0];
		}
	}

	@Override
	protected void updateUI(RealData data) {
		// TODO 自动生成的方法存根
		super.updateUI(data);
		if (!isFirstLoc) {
			//从第二次接收地理位置信息开始更新地图轨迹
			//TODO: 添加过滤，如果位置没有明显的变化，除非指标状态变化
			if (canAppendPoint) {
				Log.i(TAG, "update map, " + mLastPoint.latitude + ", " + mLastPoint.longitude);
				if (data.position == null) {
					data.position = mLastPoint;
				}
				mMapUpdater.updateMapOnReceiveDataPacket(data);
			}
		}
	}
	
}
