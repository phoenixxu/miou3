package com.datang.miou.views.gen;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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
import com.datang.miou.MiouApp;

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

	private BaiduMap mBaiduMap;
	private LatLng mLastPoint;
	private static MapUpdater sMapUpdater;
	private MapLayerOptions mMapLayerOptions;

	private Context mContext;

	//基站信息Overlay集
	private static ArrayList<Overlay> mStationInfoOverlays;
	//基站Overlay集
	private static ArrayList<Marker> mStationOverlays;
	//当前点与基站连线的Overlay
	private static Overlay mLineToCellOverlay;
	
	public static MapUpdater newInstance(BaiduMap baiduMap, Context context) {
		if (sMapUpdater != null) {
			return sMapUpdater;
		}
		return new MapUpdater(baiduMap, context);
	}
	
	private MapUpdater(BaiduMap baiduMap, Context context) {
		this.mBaiduMap = baiduMap;
		this.mContext = context;
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
		
		updateCoverTraceLTE(data);
		updateCoverTraceTD(data);
		updateCoverTraceGSM(data);
		
		updateQualityTraceLTE(data);
		updateQualityTraceTD(data);
		updateQualityTraceGSM(data);
		
		updateSpeedTraceUp(data);
		updateSpeedTraceDown(data);
		
		if (mMapLayerOptions.hasLineToCellLayer()) {
			addLineToCellLayer(data);
		}
		
		addExceptionEvent(data);
	}

	private void addExceptionEvent(RealData data) {
		int type = getLastException();
		if (type > 0) {
			OverlayOptions oo = new MarkerOptions().position(data.getPosition()).icon(bdExceptions[type])
					.zIndex(9).draggable(true);
			Marker marker = (Marker) (mBaiduMap.addOverlay(oo));
			Bundle args = new Bundle();
			args.putInt(EXTRA_MARKER_TYPE, MARKER_EXCEPTION_LTE_DROP);
			marker.setExtraInfo(args);
		}
	}

	//测试函数
	private int getLastException() {
		return -1;
	}

	//更新速率轨迹
	private void updateSpeedTraceUp(RealData data) {
		updateTrace(data, TRACE_TYPE_SPEED, Globals.MODE_UPLOAD, mMapLayerOptions.hasSpeedTraceLayerUp());
	}
	private void updateSpeedTraceDown(RealData data) {
		updateTrace(data, TRACE_TYPE_SPEED, Globals.MODE_DOWNLOAD, mMapLayerOptions.hasSpeedTraceLayerDown());
	}

	//更新质量轨迹
	private void updateQualityTraceLTE(RealData data) {
		updateTrace(data, TRACE_TYPE_QUALITY, Globals.MODE_LTE, mMapLayerOptions.hasQualityTraceLayerLTE());
	}
	private void updateQualityTraceGSM(RealData data) {
		updateTrace(data, TRACE_TYPE_QUALITY, Globals.MODE_GSM, mMapLayerOptions.hasQualityTraceLayerGSM());
	}
	private void updateQualityTraceTD(RealData data) {
		updateTrace(data, TRACE_TYPE_QUALITY, Globals.MODE_TD, mMapLayerOptions.hasQualityTraceLayerTD());
	}
	
	//更新覆盖轨迹
	private void updateCoverTraceLTE(RealData data) {
		updateTrace(data, TRACE_TYPE_COVER, Globals.MODE_LTE, mMapLayerOptions.hasCoverTraceLayerLTE());
	}
	private void updateCoverTraceGSM(RealData data) {
		updateTrace(data, TRACE_TYPE_COVER, Globals.MODE_GSM, mMapLayerOptions.hasCoverTraceLayerGSM());
	}
	private void updateCoverTraceTD(RealData data) {
		updateTrace(data, TRACE_TYPE_COVER, Globals.MODE_TD, mMapLayerOptions.hasCoverTraceLayerTD());
	}
	
	//清除轨迹
	public void clearTrace(int traceType, int mode) {
		ArrayList<ColorPoints> colorPointsList = getColorPointsList(traceType, mode);
		for (ColorPoints colorPoints : colorPointsList) {
			Overlay overlay = colorPoints.getOverlay();
			if (overlay != null) {
				colorPoints.getOverlay().remove();
				colorPoints.setOverlay(null);
			}	
		}
	}

	//更新轨迹
	private void updateTrace(RealData data, int traceType, int mode, boolean isVisible) {
		// TODO 自动生成的方法存根
		/*
		 * 这里需要根据实时指标的数值确定画线颜色
		 * 每一种状态使用一个OverLay，画线颜色不同
		 * 改变位置，若状态相同，则追加点，若状态不同，则新建Overlay
		 */
		
		ArrayList<ColorPoints> colorPointsList = getColorPointsList(traceType, mode);
		LatLng ll = data.getPosition();
		//需要获得当前数据参数处于哪个LEVEL，据此选择颜色
		int level = getDataLevel(traceType, data, mode);
		if (colorPointsList.size() > 0) {
			if (level == colorPointsList.get(colorPointsList.size() - 1).getLevel()) {
				//如果该点和上一个点参数在同一范围，则加入到同一个Overlay里，否则新建Overlay
				ColorPoints colorPonits = colorPointsList.get(colorPointsList.size() - 1);
				ArrayList<LatLng> points = colorPonits.getPointsList();
				points.add(ll);
				//Log.i(TAG, "updateTrace -- 追加点");
				if (isVisible) {
					//如果该图层可见，则更新图层，否则什么都不做
					//会崩溃？
					colorPonits.getOverlay().remove();		
					PolylineOptions options =  new PolylineOptions().width(5).color(getMapLineColor(traceType, level, mode)).points(colorPonits.getPointsList());
					Overlay overlay = mBaiduMap.addOverlay(options);
					colorPonits.setOverlay(overlay);	
				}		
			} else {
				//Log.i(TAG, "updateTrace -- 创建新图层");
				addNewOverlay(ll, traceType, mode, level, isVisible);									
			}
		} else {
			//如果该图层还没有一个点集的Overlay则新建一个
			addNewOverlay(ll, traceType, mode, level, isVisible);
		}
	}

	private int getMapLineColor(int traceType, int level, int mode) {

		int result = 0xFF0000;
		
		if (traceType == TRACE_TYPE_COVER) {
			if (mode == Globals.MODE_LTE) {
				// LTE - RSRP		
				result = Globals.getMapLineColor(Globals.PARAM_RSRP, level);
			} else if (mode == Globals.MODE_GSM) {
				result = Globals.getMapLineColor(Globals.PARAM_RXLEV_SUB, level);
			} else if (mode == Globals.MODE_TD) {
				// TD - PCCPCH_RSCP
				result = Globals.getMapLineColor(Globals.PARAM_PCCPCH_RSCP, level);
			}
		} else if (traceType == TRACE_TYPE_QUALITY) {
			if (mode == Globals.MODE_LTE) {
				// LTE - SINR	
				result = Globals.getMapLineColor(Globals.PARAM_SINR, level);
			} else if (mode == Globals.MODE_GSM) {
				result = Globals.getMapLineColor(Globals.PARAM_RXQUAL_SUB, level);
			} else if (mode == Globals.MODE_TD) {
				// TD - PCCPCH_SIR
				result = Globals.getMapLineColor(Globals.PARAM_PCCPCH_SIR, level);
			}
		} else if (traceType == TRACE_TYPE_SPEED) {
			if (mode == Globals.MODE_UPLOAD) {
				// UP -
				result = Globals.getMapLineColor(Globals.PARAM_THROUGHPUT_UL, level);
			} else if (mode == Globals.MODE_DOWNLOAD) {
				// DOWN - 
				result = Globals.getMapLineColor(Globals.PARAM_THROUGHPUT_DL, level);
			}
		}

		return result;
	}

	private void addNewOverlay(LatLng ll, int traceType, int mode, int level, boolean isVisible) {
		ArrayList<ColorPoints> colorPointsList = getColorPointsList(traceType, mode);
		ColorPoints colorPoints = new ColorPoints(level);
		int color = getMapLineColor(traceType, level, mode);
		if (mLastPoint == null) {
			mLastPoint = ll;
		}
		colorPoints.addPoint(mLastPoint);
		colorPoints.addPoint(ll);
		colorPointsList.add(colorPoints);
		if (isVisible) {
			PolylineOptions ooPolyline = new PolylineOptions().width(5).color(color).points(colorPoints.getPointsList());
			Overlay overlay = mBaiduMap.addOverlay(ooPolyline);
			colorPoints.setOverlay(overlay);	
		}
	}

	/*
	 * 获取对应轨迹的颜色点集
	 */
	private ArrayList<ColorPoints> getColorPointsList(int traceType, int netMode) {
		if (traceType == TRACE_TYPE_COVER) {
			if (netMode == Globals.MODE_LTE) {
				return ((MiouApp) ((Activity) mContext).getApplication()).getColorPointsListForCoverLTE();
			} else if (netMode == Globals.MODE_TD) {
				return ((MiouApp) ((Activity) mContext).getApplication()).getColorPointsListForCoverTD();
			} else if (netMode == Globals.MODE_GSM) {
				return ((MiouApp) ((Activity) mContext).getApplication()).getColorPointsListForCoverGSM();
			}
		} else if (traceType == TRACE_TYPE_QUALITY) {
			if (netMode == Globals.MODE_LTE) {
				return ((MiouApp) ((Activity) mContext).getApplication()).getColorPointsListForQualityLTE();
			} else if (netMode == Globals.MODE_TD) {
				return ((MiouApp) ((Activity) mContext).getApplication()).getColorPointsListForQualityTD();
			} else if (netMode == Globals.MODE_GSM) {
				return ((MiouApp) ((Activity) mContext).getApplication()).getColorPointsListForQualityGSM();
			}
		} else if (traceType == TRACE_TYPE_SPEED) {
			if (netMode == Globals.MODE_DOWNLOAD) {
				return ((MiouApp) ((Activity) mContext).getApplication()).getColorPointsListForSpeedDown();
			} else if (netMode == Globals.MODE_UPLOAD) {
				return ((MiouApp) ((Activity) mContext).getApplication()).getColorPointsListForSpeedUp();
			} 
		}
		return null;
	}

	/*
	 * 根据网络类型和实时数据所处范围，决定该数据的LEVEL，最终确定画线颜色
	 */
	private int getDataLevel(int traceType, RealData data, int mode) {
		int result = -1;
		
		if (traceType == TRACE_TYPE_COVER) {
			if (mode == Globals.MODE_LTE) {
				// LTE - RSRP		
				if (data.getParams().containsKey(Globals.PARAM_RSRP)) {
					double param = data.getParams().get(Globals.PARAM_RSRP);
					
					if (param < Globals.MAP_PARAM_RSRP_LEVEL1_MAX) {
						result = Globals.MAP_PARAM_LEVEL_ONE;
					} else if (param >= Globals.MAP_PARAM_RSRP_LEVEL1_MAX && param < Globals.MAP_PARAM_RSRP_LEVEL2_MAX) {
						result = Globals.MAP_PARAM_LEVEL_TWO;
					} else if (param >= Globals.MAP_PARAM_RSRP_LEVEL2_MAX && param < Globals.MAP_PARAM_RSRP_LEVEL3_MAX) {
						result = Globals.MAP_PARAM_LEVEL_THREE;
					} else if (param >= Globals.MAP_PARAM_RSRP_LEVEL3_MAX && param < Globals.MAP_PARAM_RSRP_LEVEL4_MAX) {
						result = Globals.MAP_PARAM_LEVEL_FOUR;
					} else if (param >= Globals.MAP_PARAM_RSRP_LEVEL4_MAX) {
						result = Globals.MAP_PARAM_LEVEL_FIVE;
					}
				}
			} else if (mode == Globals.MODE_GSM) {
				// GSM - RXLEV_SUB
				if (data.getParams().containsKey(Globals.PARAM_RXLEV_SUB)) {
					double param = data.getParams().get(Globals.PARAM_RXLEV_SUB);
					if (param >= Globals.MAP_PARAM_RXLEV_SUB_LEVEL1_MIN && param < Globals.MAP_PARAM_RXLEV_SUB_LEVEL1_MAX) {
						result = Globals.MAP_PARAM_LEVEL_ONE;
					} else if (param >= Globals.MAP_PARAM_RXLEV_SUB_LEVEL1_MAX && param < Globals.MAP_PARAM_RXLEV_SUB_LEVEL2_MAX) {
						result = Globals.MAP_PARAM_LEVEL_TWO;
					} else if (param >= Globals.MAP_PARAM_RXLEV_SUB_LEVEL2_MAX && param < Globals.MAP_PARAM_RXLEV_SUB_LEVEL3_MAX) {
						result = Globals.MAP_PARAM_LEVEL_THREE;
					} else if (param >= Globals.MAP_PARAM_RXLEV_SUB_LEVEL3_MAX && param < Globals.MAP_PARAM_RXLEV_SUB_LEVEL4_MAX) {
						result = Globals.MAP_PARAM_LEVEL_FOUR;
					} else if (param >= Globals.MAP_PARAM_RXLEV_SUB_LEVEL4_MAX && param < Globals.MAP_PARAM_RXLEV_SUB_LEVEL5_MAX) {
						result = Globals.MAP_PARAM_LEVEL_FIVE;
					} else if (param >= Globals.MAP_PARAM_RXLEV_SUB_LEVEL5_MAX && param < Globals.MAP_PARAM_RXLEV_SUB_LEVEL6_MAX) {
						result = Globals.MAP_PARAM_LEVEL_SIX;
					}
				}
			} else if (mode == Globals.MODE_TD) {
				// TD - PCCPCH_RSCP
				if (data.getParams().containsKey(Globals.PARAM_PCCPCH_RSCP)) {
					double param = data.getParams().get(Globals.PARAM_PCCPCH_RSCP);
					//double param = data.params[Globals.PARAM_PCCPCH_RSCP];
					if (param >= Globals.MAP_PARAM_PCCPCH_RSCP_LEVEL1_MIN && param < Globals.MAP_PARAM_PCCPCH_RSCP_LEVEL1_MAX) {
						result = Globals.MAP_PARAM_LEVEL_ONE;
					} else if (param >= Globals.MAP_PARAM_PCCPCH_RSCP_LEVEL1_MAX && param < Globals.MAP_PARAM_PCCPCH_RSCP_LEVEL2_MAX) {
						result = Globals.MAP_PARAM_LEVEL_TWO;
					} else if (param >= Globals.MAP_PARAM_PCCPCH_RSCP_LEVEL2_MAX && param < Globals.MAP_PARAM_PCCPCH_RSCP_LEVEL3_MAX) {
						result = Globals.MAP_PARAM_LEVEL_THREE;
					} else if (param >= Globals.MAP_PARAM_PCCPCH_RSCP_LEVEL3_MAX && param < Globals.MAP_PARAM_PCCPCH_RSCP_LEVEL4_MAX) {
						result = Globals.MAP_PARAM_LEVEL_FOUR;
					} else if (param >= Globals.MAP_PARAM_PCCPCH_RSCP_LEVEL4_MAX && param < Globals.MAP_PARAM_PCCPCH_RSCP_LEVEL5_MAX) {
						result = Globals.MAP_PARAM_LEVEL_FIVE;
					} else if (param >= Globals.MAP_PARAM_PCCPCH_RSCP_LEVEL5_MAX) {
						result = Globals.MAP_PARAM_LEVEL_SIX;
					}
				}
			}
		} else if (traceType == TRACE_TYPE_QUALITY) {
			if (mode == Globals.MODE_LTE) {
				// LTE - SINR	
				if (data.getParams().containsKey(Globals.PARAM_SINR)) {
					double param = data.getParams().get(Globals.PARAM_SINR);
					//double param = data.params[Globals.PARAM_SINR];
					if (param < Globals.MAP_PARAM_SINR_LEVEL1_MAX) {
						result = Globals.MAP_PARAM_LEVEL_ONE;
					} else if (param >= Globals.MAP_PARAM_SINR_LEVEL1_MAX && param < Globals.MAP_PARAM_SINR_LEVEL2_MAX) {
						result = Globals.MAP_PARAM_LEVEL_TWO;
					} else if (param >= Globals.MAP_PARAM_SINR_LEVEL2_MAX && param < Globals.MAP_PARAM_SINR_LEVEL3_MAX) {
						result = Globals.MAP_PARAM_LEVEL_THREE;
					} else if (param >= Globals.MAP_PARAM_SINR_LEVEL3_MAX && param < Globals.MAP_PARAM_SINR_LEVEL4_MAX) {
						result = Globals.MAP_PARAM_LEVEL_FOUR;
					} else if (param >= Globals.MAP_PARAM_SINR_LEVEL4_MAX) {
						result = Globals.MAP_PARAM_LEVEL_FIVE;
					}
				}
			} else if (mode == Globals.MODE_GSM) {
				// GSM - RXQUAL_SUB
				if (data.getParams().containsKey(Globals.PARAM_RXQUAL_SUB)) {
					double param = data.getParams().get(Globals.PARAM_RXQUAL_SUB);
					if (param >= Globals.MAP_PARAM_RXQUAL_SUB_LEVEL1_MIN && param < Globals.MAP_PARAM_RXQUAL_SUB_LEVEL1_MAX) {
						result = Globals.MAP_PARAM_LEVEL_ONE;
					} else if (param >= Globals.MAP_PARAM_RXQUAL_SUB_LEVEL1_MAX && param < Globals.MAP_PARAM_RXQUAL_SUB_LEVEL2_MAX) {
						result = Globals.MAP_PARAM_LEVEL_TWO;
					} else if (param >= Globals.MAP_PARAM_RXQUAL_SUB_LEVEL2_MAX && param < Globals.MAP_PARAM_RXQUAL_SUB_LEVEL3_MAX) {
						result = Globals.MAP_PARAM_LEVEL_THREE;
					} else if (param >= Globals.MAP_PARAM_RXQUAL_SUB_LEVEL3_MAX && param < Globals.MAP_PARAM_RXQUAL_SUB_LEVEL4_MAX) {
						result = Globals.MAP_PARAM_LEVEL_FOUR;
					} else if (param >= Globals.MAP_PARAM_RXQUAL_SUB_LEVEL4_MAX && param < Globals.MAP_PARAM_RXQUAL_SUB_LEVEL5_MAX) {
						result = Globals.MAP_PARAM_LEVEL_FIVE;
					} else if (param >= Globals.MAP_PARAM_RXQUAL_SUB_LEVEL5_MAX && param < Globals.MAP_PARAM_RXQUAL_SUB_LEVEL6_MAX) {
						result = Globals.MAP_PARAM_LEVEL_SIX;
					} else if (param >= Globals.MAP_PARAM_RXQUAL_SUB_LEVEL6_MAX && param < Globals.MAP_PARAM_RXQUAL_SUB_LEVEL7_MAX) {
						result = Globals.MAP_PARAM_LEVEL_SEVEN;
					}
				}
			} else if (mode == Globals.MODE_TD) {
				// TD - PCCPCH_SIR
				if (data.getParams().containsKey(Globals.PARAM_PCCPCH_SIR)) {
					double param = data.getParams().get(Globals.PARAM_PCCPCH_SIR);
					//double param = data.params[Globals.PARAM_PCCPCH_SIR];
					if (param >= Globals.MAP_PARAM_PCCPCH_SIR_LEVEL1_MIN && param < Globals.MAP_PARAM_PCCPCH_SIR_LEVEL1_MAX) {
						result = Globals.MAP_PARAM_LEVEL_ONE;
					} else if (param >= Globals.MAP_PARAM_PCCPCH_SIR_LEVEL1_MAX && param < Globals.MAP_PARAM_PCCPCH_SIR_LEVEL2_MAX) {
						result = Globals.MAP_PARAM_LEVEL_TWO;
					} else if (param >= Globals.MAP_PARAM_PCCPCH_SIR_LEVEL2_MAX && param < Globals.MAP_PARAM_PCCPCH_SIR_LEVEL3_MAX) {
						result = Globals.MAP_PARAM_LEVEL_THREE;
					} else if (param >= Globals.MAP_PARAM_PCCPCH_SIR_LEVEL3_MAX && param < Globals.MAP_PARAM_PCCPCH_SIR_LEVEL4_MAX) {
						result = Globals.MAP_PARAM_LEVEL_FOUR;
					} else if (param >= Globals.MAP_PARAM_PCCPCH_SIR_LEVEL4_MAX && param < Globals.MAP_PARAM_PCCPCH_SIR_LEVEL5_MAX) {
						result = Globals.MAP_PARAM_LEVEL_FIVE;
					} else if (param >= Globals.MAP_PARAM_PCCPCH_SIR_LEVEL5_MAX && param < Globals.MAP_PARAM_PCCPCH_SIR_LEVEL6_MAX) {
						result = Globals.MAP_PARAM_LEVEL_SIX;
					} else if (param >= Globals.MAP_PARAM_PCCPCH_SIR_LEVEL6_MAX && param < Globals.MAP_PARAM_PCCPCH_SIR_LEVEL7_MAX) {
						result = Globals.MAP_PARAM_LEVEL_SEVEN;
					}
				}
			}
		} else if (traceType == TRACE_TYPE_SPEED) {
			if (mode == Globals.MODE_UPLOAD) {
				// UP - THROUGHPUT_UL
				if (data.getParams().containsKey(Globals.PARAM_THROUGHPUT_UL)) {
					double param = data.getParams().get(Globals.PARAM_THROUGHPUT_UL);
					if (param < Globals.MAP_PARAM_THROUGHPUT_UL_LEVEL1_MAX) {
						result = Globals.MAP_PARAM_LEVEL_ONE;
					} else if (param >= Globals.MAP_PARAM_THROUGHPUT_UL_LEVEL1_MAX && param < Globals.MAP_PARAM_THROUGHPUT_UL_LEVEL2_MAX) {
						result = Globals.MAP_PARAM_LEVEL_TWO;
					} else if (param >= Globals.MAP_PARAM_THROUGHPUT_UL_LEVEL2_MAX && param < Globals.MAP_PARAM_THROUGHPUT_UL_LEVEL3_MAX) {
						result = Globals.MAP_PARAM_LEVEL_THREE;
					} else if (param >= Globals.MAP_PARAM_THROUGHPUT_UL_LEVEL3_MAX && param < Globals.MAP_PARAM_THROUGHPUT_UL_LEVEL4_MAX) {
						result = Globals.MAP_PARAM_LEVEL_FOUR;
					} else if (param >= Globals.MAP_PARAM_THROUGHPUT_UL_LEVEL4_MAX) {
						result = Globals.MAP_PARAM_LEVEL_FIVE;
					} 
				}
			} else if (mode == Globals.MODE_DOWNLOAD) {
				// DOWN - THROUGHPUT_DL
				if (data.getParams().containsKey(Globals.PARAM_THROUGHPUT_DL)) {
					double param = data.getParams().get(Globals.PARAM_THROUGHPUT_DL);
					if (param < Globals.MAP_PARAM_THROUGHPUT_DL_LEVEL1_MAX) {
						result = Globals.MAP_PARAM_LEVEL_ONE;
					} else if (param >= Globals.MAP_PARAM_THROUGHPUT_DL_LEVEL1_MAX && param < Globals.MAP_PARAM_THROUGHPUT_DL_LEVEL2_MAX) {
						result = Globals.MAP_PARAM_LEVEL_TWO;
					} else if (param >= Globals.MAP_PARAM_THROUGHPUT_DL_LEVEL2_MAX && param < Globals.MAP_PARAM_THROUGHPUT_DL_LEVEL3_MAX) {
						result = Globals.MAP_PARAM_LEVEL_THREE;
					} else if (param >= Globals.MAP_PARAM_THROUGHPUT_DL_LEVEL3_MAX && param < Globals.MAP_PARAM_THROUGHPUT_DL_LEVEL4_MAX) {
						result = Globals.MAP_PARAM_LEVEL_FOUR;
					} else if (param >= Globals.MAP_PARAM_THROUGHPUT_DL_LEVEL4_MAX) {
						result = Globals.MAP_PARAM_LEVEL_FIVE;
					} 
				}
			}
		}
		return result;
	}
	
	public void reDrawTraces() {
		if (mMapLayerOptions.hasCoverTraceLayerLTE()) {
			reDrawSingleTrace(TRACE_TYPE_COVER, Globals.MODE_LTE);
		}
		if (mMapLayerOptions.hasQualityTraceLayerLTE()) {
			reDrawSingleTrace(TRACE_TYPE_QUALITY, Globals.MODE_LTE);
		}
		if (mMapLayerOptions.hasSpeedTraceLayerUp()) {
			reDrawSingleTrace(TRACE_TYPE_SPEED, Globals.MODE_UPLOAD);
		}
		
		if (mMapLayerOptions.hasCoverTraceLayerTD()) {
			reDrawSingleTrace(TRACE_TYPE_COVER, Globals.MODE_TD);
		}
		if (mMapLayerOptions.hasQualityTraceLayerTD()) {
			reDrawSingleTrace(TRACE_TYPE_QUALITY, Globals.MODE_TD);
		}
		if (mMapLayerOptions.hasSpeedTraceLayerDown()) {
			reDrawSingleTrace(TRACE_TYPE_SPEED, Globals.MODE_DOWNLOAD);
		}
		
		if (mMapLayerOptions.hasCoverTraceLayerGSM()) {
			reDrawSingleTrace(TRACE_TYPE_COVER, Globals.MODE_GSM);
		}
		if (mMapLayerOptions.hasQualityTraceLayerGSM()) {
			reDrawSingleTrace(TRACE_TYPE_QUALITY, Globals.MODE_GSM);
		}
		
		if (mMapLayerOptions.hasStationLayer()) {
			addBaseStationLayer();
		}
		if (mMapLayerOptions.hasStationInfoLayer()) {
			addBaseStationInfoLayer();
		}
		if (mMapLayerOptions.hasLineToCellLayer()) {
			addLineToCellLayer(null);
		}
	}
	
	private void reDrawSingleTrace(int traceType, int mode) {
		ArrayList<ColorPoints> colorPointsList = getColorPointsList(traceType, mode);
		for (ColorPoints colorPoints : colorPointsList) {
			//remove有问题
			/*
			if (colorPoints.getOverlay() != null) {
				Log.i(TAG, "reDrawSingleTrace -- 获得overlay不为空");
				colorPoints.getOverlay().remove();
				Log.i(TAG, "reDrawSingleTrace -- 移除overlay");
			}
			*/
			PolylineOptions option = new PolylineOptions()
			.width(5)
			.color(getMapLineColor(traceType, colorPoints.getLevel(), mode))
			.points(colorPoints.getPointsList());
			Overlay overlay = mBaiduMap.addOverlay(option);
			colorPoints.setOverlay(overlay);
		}
	}
	
	public void addBaseStationLayer() {
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
		}
	}

	public void addBaseStationInfoLayer() {
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
		for (int i = mStationInfoOverlays.size() - 1; i >= 0; i--) {
			mStationInfoOverlays.get(i).remove();
			mStationInfoOverlays.remove(i);
		}
	}

	public void addLineToCellLayer(RealData data) {
		removeLineToCellLayer();
		LatLng current;
		if (data == null) {
			current = mLastPoint;
			if (current == null) {
				return;
			}
		} else {
			current = data.getPosition();
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
		return new LatLng(31.227, 121.481);
	}

	public void removeLineToCellLayer() {
		if (mLineToCellOverlay != null) {
			mLineToCellOverlay.remove();
		}
	}
}
