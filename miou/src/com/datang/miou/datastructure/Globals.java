package com.datang.miou.datastructure;

import com.datang.miou.R;

public class Globals {
	public static final String PREFS = "MIOU_PREFS";
	
	//地图轨迹图层参数
	public static final int INDEX_LEVEL_NUM = 7;
	public static final int INDEX_LEVEL_ONE = 1;
	public static final int INDEX_LEVEL_TWO = 2;
	public static final int INDEX_LEVEL_THREE = 3;
	public static final int INDEX_LEVEL_FOUR = 4;
	public static final int INDEX_LEVEL_FIVE = 5;
	public static final int INDEX_LEVEL_SIX = 6;
	public static final int INDEX_LEVEL_SEVEN = 7;
	public static int[] mapLineColor = {0xAAFFFF00, 0xAAFFFF00, 0xAAFFFF00, 0xAAFFFF00, 0xAAFFFF00, 0xAAFFFF00, 0xAAFFFF00};
	
	//实时指标折线图参数
	public static final int CHART_INDEX_NUM = 1;
	public static final int CHART_LINE_PARAM_RXLEVSUB = 0;
	public static final int CHART_LINE_PARAM_GTXPOWER = 1;
	public static final int CHART_LINE_PARAM_RSCP = 2;
	public static final int CHART_LINE_PARAM_SNR = 3;
	public static final int CHART_LINE_PARAM_BLER = 4;
	public static final int CHART_LINE_PARAM_SPEED = 5;
	public static int chartLineOneParam = 0;
	public static int chartLineTwoParam = 1;
	public static int chartLineThreeParam = 2;
	public static int[] chartLineColor = {  R.color.menu_border_gray, R.color.title_blue, R.color.black };
	
	//事件类型
	public static final int EVENT_TYPE_TASK_FINISHED_CALL = 0;
	public static final int EVENT_TYPE_STOP_LOGGING = 1;
	public static final int EVENT_TYPE_START_TASK_PING = 2;
	public static final int EVENT_TYPE_PPP_DIAL_START = 3;
	public static final int EVENT_TYPE_DIAL_SUCCESS = 4;
	public static final int EVENT_TYPE_ONE_PING_START = 5;
	
	public static final String[] EVENT_TYPE_STRINGS = { "Task Finished Call", 
														"Stop Logging", 
														"Start Task Ping",
														"PPP Dial", 
														"Dial Success", 
														"One Ping Start" };
	
	//事件类型对应颜色，被设置
	public static int EVENT_TYPE_COLOR1 = 0;
	public static int EVENT_TYPE_COLOR2 = 0;
	public static int EVENT_TYPE_COLOR3 = 0;
	public static int EVENT_TYPE_COLOR4 = 0;
	public static int EVENT_TYPE_COLOR5 = 0;
	public static int EVENT_TYPE_COLOR6 = 0;
	
	public static final int MAX_PARAMS = 128;
	public static final int NEIGHBOR_CELL_SIGNAL_MOST_STRONG = 6;
	public static final int PARAM_BANDWIDTH = 0;
	public static final int PARAM_ECGI = 1;
	public static final int PARAM_EMM = 2;
	public static final int PARAM_FREQ = 3;
	public static final int PARAM_MMC = 4;
	public static final int PARAM_MODE = 5;
	public static final int PARAM_PCI= 6;
	public static final int PARAM_RRC = 7;
	public static final int PARAM_TM = 8;

	public static void setMapLineColor(int level, int color) {
		mapLineColor[level] = color;
	}
	
	public static void setChartLineColor(int num, int color) {
		chartLineColor[num] = color;
	}
	
	public static void setChartLineOneParam(int param) {
		chartLineOneParam = param;
	}
	
	public static void setChartLineTwoParam(int param) {
		chartLineTwoParam = param;
	}
	
	public static void setChartLineThreeParam(int param) {
		chartLineThreeParam = param;
	}
}
