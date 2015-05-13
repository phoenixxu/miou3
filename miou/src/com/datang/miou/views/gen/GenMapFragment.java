package com.datang.miou.views.gen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.datang.miou.BaiduMapFragment;
import com.datang.miou.HoldLastRecieverClient;
import com.datang.miou.MiouApp;
import com.datang.miou.ProcessInterface;
import com.datang.miou.R;
import com.datang.miou.datastructure.Event;
import com.datang.miou.datastructure.Globals;
import com.datang.miou.datastructure.MapLayerOptions;
import com.datang.miou.datastructure.RealData;
import com.datang.miou.datastructure.Signal;

/**
 * 地图
 * 
 * @author suntongwei
 */

public class GenMapFragment extends BaiduMapFragment {

	private static final String TAG = "GenMapFragment";
	
	// 轨迹点列表
	private List<ParamPoint> mTracePoints;	
	
	private MapView baiduMapView;
	private CheckBox mCoverCheckBoxLTE;
	private CheckBox mQualityCheckBoxLTE;
	private CheckBox mStationCheckBox;
	private CheckBox mLineCheckBox;
	private CheckBox mStationInfoCheckBox;
	private View mMapController;
	
	private BaiduMap mBaiduMap;
	
	protected LocationClient mLocationClient;
	private LocationMode mCurrentMode;
	public MyLocationListenner myListener = new MyLocationListenner();
	boolean isFirstLoc = true;
	private LocationClient mLocClient;
	
	
	// 最后一个轨迹点
	private LatLng mLastPoint;
		
	private MapUpdater mMapUpdater;
	private MapLayerOptions mMapLayerOptions;
	
	// 回到本页时，后台线程会绘制所有记录的轨迹点，这时如果接受地理位置并追加点会产生问题
	private boolean canAppendPoint = true;

	private IntentFilter mFilter;
	
	/*
	 * 重新绘制后台线程
	 */
	private class ReDrawLinesTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... arg0) {
			// 不能再追加点
			canAppendPoint = false;
			// 重绘轨迹
			mMapUpdater.reDrawTraces();
			// 设置最后的点？
			mMapUpdater.setLastPoint(mLastPoint);
			// 可以再追加点
			canAppendPoint = true;
			return null;
		}
	}
	
	/*
	 * 告知托管的Activity本Fragment的对象引用
	 * Activity可以在适宜的时候调用本Fragment的方法
	 */
	public interface Callbacks {
		public void setMapFragment(Fragment fragment);
		public void updateUIOnReviewFinished();
	}
	
	private Callbacks mCb;

	// 地图控制面板是否可见
	private boolean isMapControllerShown;

	private Spinner mSpeedSpinner;
	private SeekBar mTimeSeekBar;
	protected int mTimeProgress = 0;

	// 当前信令，从中可以获得地理信息
	
	// 当前位置，来自百度地图的监听函数，在没有信令地理信息时使用
	public LatLng mBaiduPosition;

	private List<Event> mGlobalEvents;

	private List<Signal> mGlobalSignals;

	private TextView mCoverTextView;

	private TextView mQualityTextView;

	private TextView mSpeedTextView;

	private TextView mBaseTextView;

	private LinearLayout mCoverLayout;

	private LinearLayout mQualityLayout;

	private LinearLayout mSpeedLayout;

	private LinearLayout mBaseLayout;

	protected boolean isCoverLayoutShown;

	protected boolean isQualityLayoutShown;

	protected boolean isSpeedLayoutShown;

	protected boolean isBaseLayoutShown;

	private CheckBox mCoverCheckBoxGSM;

	private CheckBox mQualityCheckBoxGSM;

	private CheckBox mCoverCheckBoxTD;

	private CheckBox mQualityCheckBoxTD;

	private CheckBox mSpeedCheckBoxUp;

	private CheckBox mSpeedCheckBoxDown;

	private boolean hasToasted = false;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mCb = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCb = null;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		SDKInitializer.initialize(getActivity().getApplicationContext());
		setRetainInstance(true);

		mLastPoint = ((MiouApp) getActivity().getApplication()).getLastPoint();
		mTracePoints = ((MiouApp) getActivity().getApplication()).getTracePoints();
		
		mGlobalEvents = ((MiouApp) getActivity().getApplication()).getGenEvents();
		mGlobalSignals = ((MiouApp) getActivity().getApplication()).getGenSignals();

	}
	
	@Override
	public void onResume() {
		
		// 返回该页面时需要重绘历史位置连线，这个绘制的工作量大，需要后台线程执行
		super.onResume();
		
		// 注册 SDK 广播监听者
		mFilter = new IntentFilter();
		mFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
		mFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
		
		// 设置地图轨迹线颜色
		setMapLineColor();

		// 如果轨迹点不多于两个则无法画线		
		if (mTracePoints.size() >= 2) {
			new ReDrawLinesTask().execute();
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		// 退出时销毁定位，否则会出现service泄露的错误
		mLocClient.stop();

	}

	/*
	 * 设置地图轨迹颜色
	 */
	private void setMapLineColor() {
		SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Globals.PREFS, Activity.MODE_PRIVATE);
		int color;
		int defaultColor = R.color.black;
		Resources r = getActivity().getResources();
		
		for (int i = 0; i < Globals.MAP_PARAM_LEVEL_NUM; i++) {
			color = r.getColor(sharedPreferences.getInt("PREF_RSRP_COLOR_LEVEL_" + i, defaultColor));
			Globals.setMapLineColor(Globals.PARAM_RSRP, Globals.MAP_PARAM_LEVEL_ONE + i, color);

			color = r.getColor(sharedPreferences.getInt("PREF_SINR_COLOR_LEVEL_" + i, defaultColor));
			Globals.setMapLineColor(Globals.PARAM_SINR, Globals.MAP_PARAM_LEVEL_ONE + i, color);
			
			color = r.getColor(sharedPreferences.getInt("PREF_PCCPCH_RSCP_COLOR_LEVEL_" + i, defaultColor));
			Globals.setMapLineColor(Globals.PARAM_PCCPCH_RSCP, Globals.MAP_PARAM_LEVEL_ONE + i, color);
			
			color = r.getColor(sharedPreferences.getInt("PREF_PCCPCH_SIR_COLOR_LEVEL_" + i, defaultColor));
			Globals.setMapLineColor(Globals.PARAM_PCCPCH_SIR, Globals.MAP_PARAM_LEVEL_ONE + i, color);
			
			color = r.getColor(sharedPreferences.getInt("PREF_RXLEV_SUB_COLOR_LEVEL_" + i, defaultColor));
			Globals.setMapLineColor(Globals.PARAM_RXLEV_SUB, Globals.MAP_PARAM_LEVEL_ONE + i, color);
			
			color = r.getColor(sharedPreferences.getInt("PREF_RXQUAL_SUB_COLOR_LEVEL_" + i, defaultColor));
			Globals.setMapLineColor(Globals.PARAM_RXQUAL_SUB, Globals.MAP_PARAM_LEVEL_ONE + i, color);
			
			color = r.getColor(sharedPreferences.getInt("PREF_THROUGHPUT_DL_COLOR_LEVEL_" + i, defaultColor));
			Globals.setMapLineColor(Globals.PARAM_THROUGHPUT_DL, Globals.MAP_PARAM_LEVEL_ONE + i, color);
			
			color = r.getColor(sharedPreferences.getInt("PREF_THROUGHPUT_UL_COLOR_LEVEL_" + i, defaultColor));
			Globals.setMapLineColor(Globals.PARAM_THROUGHPUT_UL, Globals.MAP_PARAM_LEVEL_ONE + i, color);
		}
	}

	private void initViews(View view) {
		baiduMapView = (MapView) view.findViewById(R.id.gen_map_view);
		
		mCoverCheckBoxLTE = (CheckBox) view.findViewById(R.id.cover_lte);
		mQualityCheckBoxLTE = (CheckBox) view.findViewById(R.id.quality_lte);
		
		mCoverCheckBoxGSM = (CheckBox) view.findViewById(R.id.cover_gsm);
		mQualityCheckBoxGSM = (CheckBox) view.findViewById(R.id.quality_gsm);
		
		mCoverCheckBoxTD = (CheckBox) view.findViewById(R.id.cover_td);
		mQualityCheckBoxTD = (CheckBox) view.findViewById(R.id.quality_td);
		
		mSpeedCheckBoxUp = (CheckBox) view.findViewById(R.id.speed_up);
		mSpeedCheckBoxDown = (CheckBox) view.findViewById(R.id.speed_down);
		
		mStationCheckBox = (CheckBox) view.findViewById(R.id.base_station);
		mLineCheckBox = (CheckBox) view.findViewById(R.id.base_line);
		mStationInfoCheckBox = (CheckBox) view.findViewById(R.id.base_info);
		
		mMapController = view.findViewById(R.id.gen_map_controller);	
		
		mCoverTextView = (TextView) view.findViewById(R.id.cover_textview);
		mQualityTextView = (TextView) view.findViewById(R.id.quality_textview);
		mSpeedTextView = (TextView) view.findViewById(R.id.speed_textview);
		mBaseTextView = (TextView) view.findViewById(R.id.base_textview);
		
		mCoverLayout = (LinearLayout) view.findViewById(R.id.pop_layout_cover);	
		mQualityLayout = (LinearLayout) view.findViewById(R.id.pop_layout_quality);	
		mSpeedLayout = (LinearLayout) view.findViewById(R.id.pop_layout_speed);
		mBaseLayout = (LinearLayout) view.findViewById(R.id.pop_layout_base);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.gen_map, container, false);
		initViews(view);
		init();
		
		return view;
	}

	private void init() {
				
		// 设置回放控制图层的显示
		
		// 测试
		//isMapControllerShown = true;
		if (isMapControllerShown) {
			showMapController();
		}
		
		initSpeedSpinner();	
		initTimeSeekBar();
		
		// 将本Fragment传递给Activity,Activity可能会调用Fragment中的函数显示控制面板
		mCb.setMapFragment(this);
		
		mBaiduMap = baiduMapView.getMap();
		mMapUpdater = MapUpdater.newInstance(mBaiduMap, getActivity());
		
		mCoverTextView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) { 
				// TODO Auto-generated method stub
				if (isCoverLayoutShown) {
					mCoverLayout.setVisibility(View.INVISIBLE);
					cleanTabHint(v);
					isCoverLayoutShown = false;
				} else {
					hideAllLayout();
					
					LinearLayout.LayoutParams lp = (LayoutParams) mCoverTextView.getLayoutParams();
					lp.setMargins(0, 10, 0, 0);
					mCoverTextView.setLayoutParams(lp);
					
					mCoverLayout.setVisibility(View.VISIBLE);
					isCoverLayoutShown = true;
				}
			}
		});
		
		mQualityTextView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				// TODO Auto-generated method stub
				if (isQualityLayoutShown) {
					mQualityLayout.setVisibility(View.INVISIBLE);
					cleanTabHint(v);
					isQualityLayoutShown = false;
				} else {
					hideAllLayout();
					
					LinearLayout.LayoutParams lp = (LayoutParams) mQualityTextView.getLayoutParams();
					lp.setMargins(0, 10, 0, 0);
					mQualityTextView.setLayoutParams(lp);
					
					mQualityLayout.setVisibility(View.VISIBLE);
					isQualityLayoutShown = true;
				}
			}
		});
		
		mSpeedTextView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				// TODO Auto-generated method stub
				if (isSpeedLayoutShown) {			
					mSpeedLayout.setVisibility(View.INVISIBLE);
					cleanTabHint(v);
					isSpeedLayoutShown = false;
				} else {
					hideAllLayout();
					
					LinearLayout.LayoutParams lp = (LayoutParams) mSpeedTextView.getLayoutParams();
					lp.setMargins(0, 10, 0, 0);
					mSpeedTextView.setLayoutParams(lp);
					
					mSpeedLayout.setVisibility(View.VISIBLE);
					isSpeedLayoutShown = true;
				}
			}
		});

		mBaseTextView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				// TODO Auto-generated method stub
				if (isBaseLayoutShown) {
					mBaseLayout.setVisibility(View.INVISIBLE);
					cleanTabHint(v);
					isBaseLayoutShown = false;
				} else {
					hideAllLayout();
					
					LinearLayout.LayoutParams lp = (LayoutParams) mBaseTextView.getLayoutParams();
					lp.setMargins(0, 10, 0, 0);
					mBaseTextView.setLayoutParams(lp);
					
					mBaseLayout.setVisibility(View.VISIBLE);
					isBaseLayoutShown = true;
				}
			}
		});
		
		// 获得图层显示信息
		mMapLayerOptions = ((MiouApp) getActivity().getApplication()).getMapLayerOptions();
		mCoverCheckBoxLTE.setChecked(mMapLayerOptions.hasCoverTraceLayerLTE());
		mQualityCheckBoxLTE.setChecked(mMapLayerOptions.hasQualityTraceLayerLTE());

		mCoverCheckBoxGSM.setChecked(mMapLayerOptions.hasCoverTraceLayerGSM());
		mQualityCheckBoxGSM.setChecked(mMapLayerOptions.hasQualityTraceLayerGSM());
	
		mCoverCheckBoxTD.setChecked(mMapLayerOptions.hasCoverTraceLayerTD());
		mQualityCheckBoxTD.setChecked(mMapLayerOptions.hasQualityTraceLayerTD());

		mSpeedCheckBoxUp.setChecked(mMapLayerOptions.hasSpeedTraceLayerUp());
		mSpeedCheckBoxDown.setChecked(mMapLayerOptions.hasSpeedTraceLayerDown());
		
		mStationCheckBox.setChecked(mMapLayerOptions.hasStationLayer());
		mStationInfoCheckBox.setChecked(mMapLayerOptions.hasStationInfoLayer());
		mLineCheckBox.setChecked(mMapLayerOptions.hasLineToCellLayer());
		mMapUpdater.setMapLayerOptions(mMapLayerOptions);
		
		// 一组配置图层是否显示的CheckBox
		mCoverCheckBoxLTE.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				mMapLayerOptions.setHasCoverTraceLayerLTE(isChecked);
				// 最初是针对特定的图层单独清除，但是有问题，所以直接使用清除所有图层，再全部重绘
				refreshMap();
			}	
		});
		mQualityCheckBoxLTE.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				mMapLayerOptions.setHasQualityTraceLayerLTE(isChecked);
				refreshMap();
			}
		});
		mSpeedCheckBoxUp.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				mMapLayerOptions.setHasSpeedTraceLayerUp(isChecked);
				refreshMap();
			}
		});
		
		mCoverCheckBoxGSM.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				mMapLayerOptions.setHasCoverTraceLayerGSM(isChecked);
				// 最初是针对特定的图层单独清除，但是有问题，所以直接使用清除所有图层，再全部重绘
				refreshMap();
			}	
		});
		mQualityCheckBoxGSM.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				mMapLayerOptions.setHasQualityTraceLayerGSM(isChecked);
				refreshMap();
			}
		});
		mSpeedCheckBoxDown.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				mMapLayerOptions.setHasSpeedTraceLayerDown(isChecked);
				refreshMap();
			}
		});
		
		mCoverCheckBoxTD.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				mMapLayerOptions.setHasCoverTraceLayerTD(isChecked);
				// 最初是针对特定的图层单独清除，但是有问题，所以直接使用清除所有图层，再全部重绘
				refreshMap();
			}	
		});
		mQualityCheckBoxTD.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				mMapLayerOptions.setHasQualityTraceLayerTD(isChecked);
				refreshMap();
			}
		});
		
		mStationCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				mMapLayerOptions.setHasStationLayer(isChecked);
				refreshMap();
			}
		});
		mStationInfoCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				mMapLayerOptions.setHasStationInfoLayer(isChecked);
				refreshMap();
			}
		});
		mLineCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				mMapLayerOptions.setHasLineToCellLayer(isChecked);
				refreshMap();
			}
		});
		
		// 地图的图标点击事件
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			private InfoWindow mInfoWindow;

			public boolean onMarkerClick(final Marker marker) {
				Bundle args = marker.getExtraInfo();
				int type = args.getInt(MapUpdater.EXTRA_MARKER_TYPE);
				// 根据图标类型确定点击事件
				// 基站点
				if (type == MapUpdater.MARKER_BASE_STATION) {
					Button button = new Button(getActivity().getApplicationContext());
					button.setBackgroundResource(R.drawable.popup);
					button.setText("我在" + marker.getPosition().latitude + ", " + marker.getPosition().longitude);
					LatLng ll = marker.getPosition();
					mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(button), ll, -47, null);
					mBaiduMap.showInfoWindow(mInfoWindow);
				} 
				// 异常事件点
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
		
		// 添加基站点
		mMapUpdater.addBaseStationLayer();
	}
	
	protected void cleanTabHint(View v) {
		// TODO Auto-generated method stub
		LinearLayout.LayoutParams lp = (LayoutParams) v.getLayoutParams();
		lp.setMargins(0, 0, 0, 0);
		v.setLayoutParams(lp);
	}

	protected void hideAllLayout() {
		// TODO Auto-generated method stub
		mCoverLayout.setVisibility(View.INVISIBLE);
		mSpeedLayout.setVisibility(View.INVISIBLE);
		mQualityLayout.setVisibility(View.INVISIBLE);
		mBaseLayout.setVisibility(View.INVISIBLE);
		
		cleanTabHint(mCoverTextView);
		cleanTabHint(mSpeedTextView);
		cleanTabHint(mBaseTextView);
		cleanTabHint(mQualityTextView);
		/*
		LinearLayout.LayoutParams lp = (LayoutParams) mCoverTextView.getLayoutParams();
		lp.setMargins(0, 0, 0, 0);
		mCoverTextView.setLayoutParams(lp);
		
		lp = (LayoutParams) mQualityTextView.getLayoutParams();
		lp.setMargins(0, 0, 0, 0);
		mQualityTextView.setLayoutParams(lp);
		
		lp = (LayoutParams) mSpeedTextView.getLayoutParams();
		lp.setMargins(0, 0, 0, 0);
		mSpeedTextView.setLayoutParams(lp);
		
		lp = (LayoutParams) mBaseTextView.getLayoutParams();
		lp.setMargins(0, 0, 0, 0);
		mBaseTextView.setLayoutParams(lp);
		*/
		
		isCoverLayoutShown = false;
		isSpeedLayoutShown = false;
		isQualityLayoutShown = false;
		isBaseLayoutShown = false;
	}

	/*
	 * 重绘地图所有图层
	 */
	protected void refreshMap() {
		mBaiduMap.clear();
		new ReDrawLinesTask().execute();	
	}
		
	/*
	 * 初始化回放时间轴
	 */
	private void initTimeSeekBar() {
		mTimeSeekBar = (SeekBar) mMapController.findViewById(R.id.time_seekbar);
		mTimeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// 结束拖动 重绘地图
				
				// 获得当前进度条百分比
				int currentProgerss = ProcessInterface.GetLogPosition();
				
				if (seekBar.getProgress() > currentProgerss) {
					// 向后拖动, 直接定位到目标点开始正常绘制轨迹
					ProcessInterface.SeekLogPosition((char) seekBar.getProgress());
					Log.i(TAG, "goto position " + seekBar.getProgress());
					// 设置LastPoint为空？
					mLastPoint = null;
					mMapUpdater.setLastPoint(null);
				}
				else {
					// 向前拖动, 清理图层, 快速绘制轨迹, 然后定位到目标点开始正常绘制轨迹
					int count = mTracePoints.size() * seekBar.getProgress() / 100;
					mTracePoints = mTracePoints.subList(0, count);
					
					((MiouApp) getActivity().getApplication()).getColorPointsListForCoverLTE().clear();
					((MiouApp) getActivity().getApplication()).getColorPointsListForCoverTD().clear();
					((MiouApp) getActivity().getApplication()).getColorPointsListForCoverGSM().clear();
					
					((MiouApp) getActivity().getApplication()).getColorPointsListForQualityLTE().clear();
					((MiouApp) getActivity().getApplication()).getColorPointsListForQualityTD().clear();
					((MiouApp) getActivity().getApplication()).getColorPointsListForQualityGSM().clear();
					
					((MiouApp) getActivity().getApplication()).getColorPointsListForSpeedUp().clear();
					((MiouApp) getActivity().getApplication()).getColorPointsListForSpeedDown().clear();

					mBaiduMap.clear();
					
					canAppendPoint = false;
					for (ParamPoint point : mTracePoints) {
						RealData data = new RealData();
						data.setPosition(point.getLocation());
						
						data.getParams().put(Globals.PARAM_RSRP, point.getRsrp());
						data.getParams().put(Globals.PARAM_SINR, point.getSinr());
						
						mMapUpdater.updateMapOnReceiveDataPacket(data);
					}

					mLastPoint = mTracePoints.get(mTracePoints.size() - 1).getLocation();
					mMapUpdater.setLastPoint(mLastPoint);
					
					ProcessInterface.SeekLogPosition((char) seekBar.getProgress());
					Log.i(TAG, "goto position " + seekBar.getProgress());

					canAppendPoint = true;
					
					//TODO: 向前拖动 需要刷新全局数据 通过百分比移除
				}
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				// TODO Auto-generated method stub
				mTimeProgress = progress;
			}
		});
	}

	/*
	 * 初始化回放速度选择器
	 */
	private void initSpeedSpinner() {
		mSpeedSpinner = (Spinner) mMapController.findViewById(R.id.speed_spinner);
		ArrayList<String> speeds = new ArrayList<String>();
		speeds.add("正常速度");
		speeds.add("2倍速度");
		speeds.add("4倍速度");
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, speeds);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpeedSpinner.setAdapter(adapter);
		
		mSpeedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
					case 0:
						ProcessInterface.SetReadSpeed((char) 1);
						break;
					case 1:
						ProcessInterface.SetReadSpeed((char) 2);
						break;
					case 2:
						ProcessInterface.SetReadSpeed((char) 4);
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
	 * 现在这个监听函数只是将接受的地理信息存放在mCurrentPosition中
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// MapView销毁后不在处理新接收的位置
			if (location == null || baiduMapView == null || mBaiduMap == null)
				return;
			
			mBaiduPosition = new LatLng(location.getLatitude(), location.getLongitude());
			Log.i(TAG, "onReceiveLocation -- location = (" + mBaiduPosition.latitude + ", " + mBaiduPosition.longitude + ")");

			/*
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					.direction(location.getDirection())
					.latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
			
			Log.i(TAG, "onReceiveLocation -- location = (" + ll.latitude + ", " + ll.longitude + ")");
			//只有当再次定位距离大于50米时，才刷新轨迹
			//if (getDistance(ll.latitude, ll.longitude, mLastPoint.latitude, mLastPoint.longitude) * KM_RATE > 0.05){
				mTracePoints.add(ll);
				if (isFirstLoc) {
					isFirstLoc = false;		
					MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
					mBaiduMap.animateMapStatus(u);
				} else {
					if (canAppendPoint) {
						//if (data.getPosition() == null) {
						//	data.setPosition(mLastPoint);
						//}
						RealData data = new RealData();
						data.setPosition(ll);
						mMapUpdater.updateMapOnReceiveDataPacket(data);
					}
				}
			//}
			mLastPoint = ll;
			mMapUpdater.setLastPoint(ll);
			*/
			
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
		
		super.updateUI(data);
		Log.i(TAG, "update ui in Map Fragment");
		/*
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
		*/
		
		// 只有在测试或回放时更新地图轨迹
		if (((MiouApp) getActivity().getApplication()).isGenTesting() || ((MiouApp) getActivity().getApplication()).isGenReviewing()) {
			// 通常情况下通过数据中的位置信息定位
			// 如果没有数据或者数据中的位置为(0,0)则使用百度地图提供的位置信息,只定位不绘制轨迹

			List<Signal> signals = ((MiouApp) getActivity().getApplication()).getGenSignals();
			if (signals.size() == 0) {
				return;
			}
			
			Signal lastSignal = signals.get(signals.size() - 1);
			
			Log.i(TAG, "last signal = " + lastSignal);
			if (lastSignal != null) {
				//if (mSignal.getLat().startsWith("0.") && mSignal.getLon().startsWith("0.")) {
				//	locateDueToBaiduMap();
				//} else {
					// 更新进度条

					int progress = ProcessInterface.GetLogPosition();
					Log.i(TAG, "progress = " + progress);
					if ( !hasToasted && progress >= 100) {
						Toast.makeText(getActivity(), "回放完成", Toast.LENGTH_LONG).show();
						hasToasted = true;
					}
					
					if (progress > 100) {
						//mCb.updateUIOnReviewFinished();
					} else {
						mTimeSeekBar.setProgress(progress);
					}
					
					double lat = Double.parseDouble(lastSignal.getLat());
					double lon = Double.parseDouble(lastSignal.getLon());
					
					LatLng ll = convert2BaiduPosition(lat, lon);
					//LatLng ll = new LatLng(lat, lon);
					
					Log.i(TAG, "updateUI -- location = (" + ll.latitude + ", " + ll.longitude + ")");
					
					data.setPosition(ll);
					MyLocationData locData = new MyLocationData.Builder().latitude(ll.latitude).longitude(ll.longitude).build();
					mBaiduMap.setMyLocationData(locData);
					
					double rsrp = Math.random() * 40 - 115;
					double sinr = Math.random() * 30;
					double rscp = Math.random() * 100 - 115;
					double sir = Math.random() * 40 - 10;
					double rxlev = Math.random() * 100 - 115;;
					double rxqual = Math.random() * 10;
					double throughputUl = Math.random() * 20;
					double throughputDl = Math.random() * 50;
					
					String strRsrp = ProcessInterface.GetIEByID("i637");
					if (!strRsrp.equals("")) {
						rsrp = Double.parseDouble(strRsrp);
					}
					String strSinr = ProcessInterface.GetIEByID("i642");
					if (!strSinr.equals("")) {
						sinr = Double.parseDouble(strSinr);
					}
					String strRscp = ProcessInterface.GetIEByID("i168");
					if (!strRscp.equals("")) {
						rscp = Double.parseDouble(strSinr);
					}
					String strSir = ProcessInterface.GetIEByID("i1238");
					if (!strSir.equals("")) {
						sir = Double.parseDouble(strSinr);
					}
					String strRxlev = ProcessInterface.GetIEByID("i451");
					if (!strRxlev.equals("")) {
						rxlev = Double.parseDouble(strSinr);
					}
					String strRxqual = ProcessInterface.GetIEByID("i498");
					if (!strRxqual.equals("")) {
						rxqual = Double.parseDouble(strSinr);
					}
					String strUl = ProcessInterface.GetIEByID("i556");
					if (!strUl.equals("")) {
						throughputUl = Double.parseDouble(strSinr);
					}
					String strDl = ProcessInterface.GetIEByID("i566");
					if (!strDl.equals("")) {
						throughputDl = Double.parseDouble(strSinr);
					}
					
					data.getParams().put(Globals.PARAM_RSRP, rsrp);
					data.getParams().put(Globals.PARAM_SINR, sinr);
					data.getParams().put(Globals.PARAM_PCCPCH_RSCP, rscp);
					data.getParams().put(Globals.PARAM_PCCPCH_SIR, sir);
					data.getParams().put(Globals.PARAM_RXLEV_SUB, rxlev);
					data.getParams().put(Globals.PARAM_RXQUAL_SUB, rxqual);
					data.getParams().put(Globals.PARAM_THROUGHPUT_DL, throughputUl);
					data.getParams().put(Globals.PARAM_THROUGHPUT_UL, throughputDl);
					
					// 保存到轨迹点集中
					ParamPoint point = new ParamPoint();
					point.setLocation(ll);
					point.setRsrp(rsrp);
					point.setSinr(sinr);
					mTracePoints.add(point);
					
					if (canAppendPoint) {
						mMapUpdater.updateMapOnReceiveDataPacket(data);
					}
					
					mLastPoint = ll;
					mMapUpdater.setLastPoint(ll);
				//}	
			} else {
				locateDueToBaiduMap();
			}
		} else {
			locateDueToBaiduMap();
		}
		
		
		//测试代码
		/*
		if (((MiouApp) getActivity().getApplication()).isGenTesting()) {
			//在测试的时候更新位置
			double lat = 31 + Math.random() * 0.01;
			
			double lon = 121 + Math.random() * 0.01;
			
			LatLng ll = new LatLng(lat, lon);
			
			//mTracePoints.add(ll);
			
			data.setPosition(ll);
			MyLocationData locData = new MyLocationData.Builder().latitude(ll.latitude).longitude(ll.longitude).build();
			mBaiduMap.setMyLocationData(locData);
			
			mTimeSeekBar.setProgress(mTimeProgress++);
			
			double rsrp = Math.random() * 40 - 115;
			double sinr = Math.random() * 30;
			double rscp = Math.random() * 100 - 115;
			double sir = Math.random() * 40 - 10;
			double rxlev = Math.random() * 100 - 115;;
			double rxqual = Math.random() * 10;
			double throughputUl = Math.random() * 20;
			double throughputDl = Math.random() * 50;
			String strRsrp = ProcessInterface.GetIEByID("i637");
			if (!strRsrp.equals("")) {
				rsrp = Double.parseDouble(strRsrp);
			}
			String strSinr = ProcessInterface.GetIEByID("i642");
			if (!strSinr.equals("")) {
				sinr = Double.parseDouble(strSinr);
			}
			
			data.getParams().put(Globals.PARAM_RSRP, rsrp);
			data.getParams().put(Globals.PARAM_SINR, sinr);
			data.getParams().put(Globals.PARAM_PCCPCH_RSCP, rscp);
			data.getParams().put(Globals.PARAM_PCCPCH_SIR, sir);
			data.getParams().put(Globals.PARAM_RXLEV_SUB, rxlev);
			data.getParams().put(Globals.PARAM_RXQUAL_SUB, rxqual);
			data.getParams().put(Globals.PARAM_THROUGHPUT_DL, throughputUl);
			data.getParams().put(Globals.PARAM_THROUGHPUT_UL, throughputDl);
			
			// 保存到轨迹点集中
			ParamPoint point = new ParamPoint();
			point.setLocation(ll);
			point.setRsrp(rsrp);
			point.setSinr(sinr);
			mTracePoints.add(point);
			
			if (canAppendPoint) {
				mMapUpdater.updateMapOnReceiveDataPacket(data);
			}
			
			mLastPoint = ll;
			mMapUpdater.setLastPoint(ll);
			
		}
		*/
	}
	
	private LatLng convert2BaiduPosition(double lat, double lon) {
		// TODO Auto-generated method stub
		CoordinateConverter converter = new CoordinateConverter();
		LatLng newLl = converter.coord(new LatLng(lat, lon)).convert();
		Log.i(TAG, "new lat = " + newLl.latitude + ", new lon = " + newLl.longitude);
		return newLl;
	}

	/*
	 * 根据百度地图提供的位置信息定位
	 */
	private void locateDueToBaiduMap() {
		if (mBaiduPosition != null) {
			MyLocationData locData = new MyLocationData.Builder().latitude(mBaiduPosition.latitude).longitude(mBaiduPosition.longitude).build();
			mBaiduMap.setMyLocationData(locData);
		}
	}

	/*
	 * 显示回放控制器图层
	 */
	public void showMapController() {
		isMapControllerShown = true;
		mMapController.setVisibility(View.VISIBLE);
	}

	/*
	 * 隐藏回放控制器图层
	 */
	public void hideMapController() {
		// TODO Auto-generated method stub
		isMapControllerShown = false;
		mMapController.setVisibility(View.INVISIBLE);
	}

	/*
	@Override
	public void ProcessData(Map<String, String> mapIDValue) {

		Log.i(TAG, "mapIDValue = " + mapIDValue);
		if (mapIDValue.containsKey("s000")){
			mSignal = parseSignal(mapIDValue);
			mGlobalSignals.add(0, mSignal);
		} 
		
		if (mapIDValue.containsKey("e000")){
			mEvent = parseEvent(mapIDValue);
			mGlobalEvents.add(0, mEvent);
		} 
		
	}
	*/
}
