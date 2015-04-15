package com.datang.miou.views.gen;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;
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
import com.datang.miou.datastructure.TestType;

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
	@AutoView(R.id.gen_map_controller)
	private View mMapController;
	
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

	private IntentFilter mFilter;
	
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
	
	public interface Callbacks {
		public void setHandler(Fragment fragment);
	}
	private Callbacks mCb;

	private boolean isMapControllerShown;

	private Spinner mSpeedSpinner;

	private SeekBar mTimeSeekBar;

	protected int mTimeProgress;
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mCb = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		mCb = null;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mCb.setHandler(this);
		
		SDKInitializer.initialize(getActivity().getApplicationContext());
		setRetainInstance(true);
		
		mLastPoint = new LatLng(0, 0);
		mTracePoints = new ArrayList<LatLng>();
		
		//设置地图轨迹线颜色
		//Globals.setMapLineColor(Globals.MAP_PARAM_LEVEL_ONE, 0xAAFFFF00);
		//Globals.setMapLineColor(Globals.MAP_PARAM_LEVEL_TWO, 0xAAFF0000);
		//Globals.setMapLineColor(Globals.MAP_PARAM_LEVEL_THREE, 0xAA000000);
		//Globals.setMapLineColor(Globals.MAP_PARAM_LEVEL_FOUR, 0xAAFFFFFF);
	}
	
	@Override
	public void onResume() {
		/*
		 * 返回该页面时需要：
		 * 1.定位到最后的位置<不需要>
		 * 2.重绘历史位置连线，这个绘制的工作量太大，需要其他线程
		 */
		super.onResume();
		
		//设置地图轨迹线颜色
		setMapLineColor();
		
		//MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(mLastPoint);
		//mBaiduMap.animateMapStatus(u);
		//如果轨迹点不多于两个则无法画线		
		if (mTracePoints.size() >= 2) {
				new ReDrawLinesTask().execute();
		}
	}

	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (mReceiver != null) {
			getActivity().unregisterReceiver(mReceiver);
			mReceiver = null;
		}
	}

	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}

	private void setMapLineColor() {
		// TODO Auto-generated method stub
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(Globals.PREFS, Activity.MODE_PRIVATE);
		int color;
		color = sharedPreferences.getInt("PREF_RSRP_COLOR_LEVEL_0", 0);
		Log.i(TAG, "PREF_RSRP_COLOR_0 = " + color);
		Globals.setMapLineColor(Globals.PARAM_RSRP, Globals.MAP_PARAM_LEVEL_ONE, color);
		
		color = sharedPreferences.getInt("PREF_RSRP_COLOR_LEVEL_1", 0);
		Log.i(TAG, "PREF_RSRP_COLOR_1 = " + color);
		Globals.setMapLineColor(Globals.PARAM_RSRP, Globals.MAP_PARAM_LEVEL_TWO, color);
		
		color = sharedPreferences.getInt("PREF_RSRP_COLOR_LEVEL_2", 0);
		Log.i(TAG, "PREF_RSRP_COLOR_2 = " + color);
		Globals.setMapLineColor(Globals.PARAM_RSRP, Globals.MAP_PARAM_LEVEL_THREE, color);
		
		color = sharedPreferences.getInt("PREF_RSRP_COLOR_LEVEL_3", 0);
		Log.i(TAG, "PREF_RSRP_COLOR_3 = " + color);
		Globals.setMapLineColor(Globals.PARAM_RSRP, Globals.MAP_PARAM_LEVEL_FOUR, color);
		
		color = sharedPreferences.getInt("PREF_RSRP_COLOR_LEVEL_4", 0);
		Log.i(TAG, "PREF_RSRP_COLOR_4 = " + color);
		Globals.setMapLineColor(Globals.PARAM_RSRP, Globals.MAP_PARAM_LEVEL_FIVE, color);
	}

	@AfterView
	public void init() {
		
		// 注册 SDK 广播监听者
		mFilter = new IntentFilter();
		mFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
		mFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
		if (mReceiver == null) {
			mReceiver = new SDKReceiver();
			getActivity().registerReceiver(mReceiver, mFilter);
		}
		
		//设置回放控制图层的显示
		if (isMapControllerShown) {
			showMapController();
		}
		
		//回放速度选择器
		initSpeedSpinner();
		
		initTimeSeekBar();
		
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
	
	private void initTimeSeekBar() {
		mTimeSeekBar = (SeekBar) mMapController.findViewById(R.id.time_seekbar);
		mTimeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				// TODO Auto-generated method stub
				Log.i(TAG, "progress = " + progress);
				mTimeProgress = progress;
			}
		});
	}

	private void initSpeedSpinner() {
		mSpeedSpinner = (Spinner) mMapController.findViewById(R.id.speed_spinner);
		ArrayList<String> speeds = new ArrayList<String>();
		speeds.add("正常速度");
		speeds.add("2倍速度");
		speeds.add("4倍速度");
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, speeds);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpeedSpinner.setAdapter(adapter);
		
		mSpeedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
					case 0:
						break;
					case 1:
						break;
					case 2:
						break;
					default:
						break;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
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
			if (location == null || baiduMapView == null || mBaiduMap == null)
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
				if (data.getPosition() == null) {
					data.setPosition(mLastPoint);
				}
				mMapUpdater.updateMapOnReceiveDataPacket(data);
			}
		}
	}
	
	public void showMapController() {

		isMapControllerShown = true;
		mMapController.setVisibility(View.VISIBLE);
	}

	public void hideMapController() {
		// TODO Auto-generated method stub
		isMapControllerShown = false;
		mMapController.setVisibility(View.INVISIBLE);
	}
}
