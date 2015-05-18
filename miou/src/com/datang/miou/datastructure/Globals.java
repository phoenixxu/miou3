package com.datang.miou.datastructure;

import java.util.HashMap;
import java.util.Map;

import com.datang.miou.R;

public class Globals {
	//	add via chenzm
	//	高级	HIGHER;低级 LOWER
	//	HIGHER	支持普通测试；单站测试；室内测试；用户感知
	//	LOWER	支持用户感知
	public static final String USER_PERMISSION = "HIGHER";
	
	//	add via chenzm end
	
	public static final String PREFS = "MIOU_PREFS";

	//测试命令
	public static final String TEST_COMMAND_VOICE_MASTER = "0x0500";
	public static final String TEST_COMMAND_VOICE_SLAVE = "0x0501";
	public static final String TEST_COMMAND_IDLE = "0x0502";
	public static final String TEST_COMMAND_ATTACH = "0x0602";
	public static final String TEST_COMMAND_PDP = "0x0603";
	public static final String TEST_COMMAND_PING = "0x0604";
	public static final String TEST_COMMAND_WAP = "0x0608";
	public static final String TEST_COMMAND_SMS = "0x0609";
	public static final String TEST_COMMAND_SEND_SMS = "0x060A";
	public static final String TEST_COMMAND_RECV_SMS = "0x060B";
	public static final String TEST_COMMAND_FTP = "0x060C";
	public static final String TEST_COMMAND_HTTP = "0x060F";
	public static final String TEST_COMMAND_STREAM = "0x0611";
	public static final String TEST_COMMAND_POP3 = "0x0612";
	public static final String TEST_COMMAND_SMTP = "0x0613";
	
	//地图参数等级
	public static final int MAP_PARAM_LEVEL_NUM = 7;
	public static final int MAP_PARAM_LEVEL_ONE = 1;
	public static final int MAP_PARAM_LEVEL_TWO = 2;
	public static final int MAP_PARAM_LEVEL_THREE = 3;
	public static final int MAP_PARAM_LEVEL_FOUR = 4;
	public static final int MAP_PARAM_LEVEL_FIVE = 5;
	public static final int MAP_PARAM_LEVEL_SIX = 6;
	public static final int MAP_PARAM_LEVEL_SEVEN = 7;
	
	//public static int[] mapLineColor = {0xAAFFFF00, 0xAAFFFF00, 0xAAFFFF00, 0xAAFFFF00, 0xAAFFFF00, 0xAAFFFF00, 0xAAFFFF00};
	public static final int MAP_PARAM_MAX_COLOR_NUMS = 128;
	//通过参数类型*10+参数确定下标，存放SharedPreference中存放的颜色值
	public static Map<String, Integer> mapParamColor = new HashMap<String, Integer>();
	//public static int[] mapParamColor = new int[MAP_PARAM_MAX_COLOR_NUMS];
	
	//地图参数范围值
	public static final int MAP_PARAM_RSRP_LEVEL1_MAX = -115;
	public static final int MAP_PARAM_RSRP_LEVEL2_MAX = -105;
	public static final int MAP_PARAM_RSRP_LEVEL3_MAX = -95;
	public static final int MAP_PARAM_RSRP_LEVEL4_MAX = -85;
	
	public static final int MAP_PARAM_SINR_LEVEL1_MAX = 0;
	public static final int MAP_PARAM_SINR_LEVEL2_MAX = 5;
	public static final int MAP_PARAM_SINR_LEVEL3_MAX = 15;
	public static final int MAP_PARAM_SINR_LEVEL4_MAX = 22;
	
	public static final int MAP_PARAM_THROUGHPUT_DL_LEVEL1_MAX = 4;
	public static final int MAP_PARAM_THROUGHPUT_DL_LEVEL2_MAX = 20;
	public static final int MAP_PARAM_THROUGHPUT_DL_LEVEL3_MAX = 40;
	public static final int MAP_PARAM_THROUGHPUT_DL_LEVEL4_MAX = 50;
	
	public static final double MAP_PARAM_THROUGHPUT_UL_LEVEL1_MAX = 0.5;
	public static final double MAP_PARAM_THROUGHPUT_UL_LEVEL2_MAX = 5;
	public static final double MAP_PARAM_THROUGHPUT_UL_LEVEL3_MAX = 10;
	public static final double MAP_PARAM_THROUGHPUT_UL_LEVEL4_MAX = 15;
	
	public static final double MAP_PARAM_PCCPCH_RSCP_LEVEL1_MIN = -116.0;
	public static final double MAP_PARAM_PCCPCH_RSCP_LEVEL1_MAX = -100.0;
	public static final double MAP_PARAM_PCCPCH_RSCP_LEVEL2_MAX = -95.0;
	public static final double MAP_PARAM_PCCPCH_RSCP_LEVEL3_MAX = -85.0;
	public static final double MAP_PARAM_PCCPCH_RSCP_LEVEL4_MAX = -80.0;
	public static final double MAP_PARAM_PCCPCH_RSCP_LEVEL5_MAX = -75.0;
	public static final double MAP_PARAM_PCCPCH_RSCP_LEVEL6_MAX = -25.0;
	
	public static final double MAP_PARAM_PCCPCH_SIR_LEVEL1_MIN = -11.0;
	public static final double MAP_PARAM_PCCPCH_SIR_LEVEL1_MAX = -8.0;
	public static final double MAP_PARAM_PCCPCH_SIR_LEVEL2_MAX = -4.0;
	public static final double MAP_PARAM_PCCPCH_SIR_LEVEL3_MAX = 0;
	public static final double MAP_PARAM_PCCPCH_SIR_LEVEL4_MAX = 4.0;
	public static final double MAP_PARAM_PCCPCH_SIR_LEVEL5_MAX = 8.0;
	public static final double MAP_PARAM_PCCPCH_SIR_LEVEL6_MAX = 16.0;
	public static final double MAP_PARAM_PCCPCH_SIR_LEVEL7_MAX = 20.0;
	
	public static final double MAP_PARAM_RXLEV_SUB_LEVEL1_MIN = -116.0;
	public static final double MAP_PARAM_RXLEV_SUB_LEVEL1_MAX = -100.0;
	public static final double MAP_PARAM_RXLEV_SUB_LEVEL2_MAX = -95.0;
	public static final double MAP_PARAM_RXLEV_SUB_LEVEL3_MAX = -85.0;
	public static final double MAP_PARAM_RXLEV_SUB_LEVEL4_MAX = -80.0;
	public static final double MAP_PARAM_RXLEV_SUB_LEVEL5_MAX = -75.0;
	public static final double MAP_PARAM_RXLEV_SUB_LEVEL6_MAX = -25.0;
	
	public static final double MAP_PARAM_RXQUAL_SUB_LEVEL1_MIN = 0.0;
	public static final double MAP_PARAM_RXQUAL_SUB_LEVEL1_MAX = 1.0;
	public static final double MAP_PARAM_RXQUAL_SUB_LEVEL2_MAX = 2.0;
	public static final double MAP_PARAM_RXQUAL_SUB_LEVEL3_MAX = 3.0;
	public static final double MAP_PARAM_RXQUAL_SUB_LEVEL4_MAX = 4.0;
	public static final double MAP_PARAM_RXQUAL_SUB_LEVEL5_MAX = 5.0;
	public static final double MAP_PARAM_RXQUAL_SUB_LEVEL6_MAX = 6.0;
	public static final double MAP_PARAM_RXQUAL_SUB_LEVEL7_MAX = 7.0;
	
	//实时指标折线图参数
	public static final int CHART_PARAM_NUM = 1;
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
	
	//事件类型对应颜色，被设置，不只6种
	public static int EVENT_TYPE_COLOR1 = 0;
	public static int EVENT_TYPE_COLOR2 = 0;
	public static int EVENT_TYPE_COLOR3 = 0;
	public static int EVENT_TYPE_COLOR4 = 0;
	public static int EVENT_TYPE_COLOR5 = 0;
	public static int EVENT_TYPE_COLOR6 = 0;
	
	public static final int MAX_PARAMS = 128;
	public static final int NEIGHBOR_CELL_SIGNAL_MOST_STRONG = 6;
	
	//指标参数键
	public static final String PARAM_LTE_DL_BANDWIDTH = "i629";
	public static final String PARAM_LTE_UL_BANDWIDTH = "i632";
	public static final String PARAM_LTE_EMM_STATE = "i1063";
	public static final String PARAM_LTE_FREQ = "i627";
	public static final String PARAM_LTE_MMC = "i617";
	public static final String PARAM_LTE_MNC = "i618";
	public static final String PARAM_LTE_MODE = "i633";
	public static final String PARAM_LTE_PCI= "i624";
	public static final String PARAM_LTE_RRC_STATE = "i1065";
	public static final String PARAM_LTE_TM = "i652";
	public static final String PARAM_LTE_BAND = "i628";
	public static final String PARAM_LTE_EARFCN = "i626";
	public static final String PARAM_LTE_EMM_SUBSTATE = "i1064";
	
	public static final String PARAM_TD_SERV_LAC = "i83";
	public static final String PARAM_TD_SERV_MCC = "i81";
	public static final String PARAM_TD_SERV_MNC = "i82";
	public static final String PARAM_TD_SERV_NMO = "i92";
	public static final String PARAM_TD_SERV_CELL_BARRED = "i96";
	public static final String PARAM_TD_SERV_CELL_PARAMETER_ID = "i87";
	public static final String PARAM_TD_SERV_CI = "i85";
	public static final String PARAM_TD_SERV_UARFCN = "i86";

	public static final String PARAM_GSM_SERV_MCC = "i428";
	public static final String PARAM_GSM_SERV_MNC = "i429";
	public static final String PARAM_GSM_GPRS_SUPPORT = "i435";
	public static final String PARAM_GSM_EGPRS_SUPPORT = "i436";
	public static final String PARAM_GSM_BCCH = "i433";
	public static final String PARAM_GSM_BSIC = "i434";
	public static final String PARAM_GSM_SERV_CI = "i431";
	public static final String PARAM_GSM_SERV_LAC = "i430";
	
	public static final String PARAM_NEIGHBOR_LTE_EARFCN = "i669_";
	public static final String PARAM_NEIGHBOR_LTE_PCI = "i670_";
	public static final String PARAM_NEIGHBOR_LTE_FREQ = "i668_";
	
	public static final String PARAM_NEIGHBOR_TD_PCCPCH_RSCP = "i174_";
	public static final String PARAM_NEIGHBOR_TD_UARFCN = "i172_";
	public static final String PARAM_NEIGHBOR_TD_PARAM_ID = "i173_";
	
	public static final String PARAM_NEIGHBOR_GSM_BCCH = "i464_";
	public static final String PARAM_NEIGHBOR_GSM_BSIC = "i463_";
	public static final String PARAM_NEIGHBOR_GSM_RXLEV_SUB = "i465_";
	
	public static final String PARAM_RSRP = "i0001";
	public static final String PARAM_SINR = "i0002";
	public static final String PARAM_PCCPCH_SIR = "i0003";
	public static final String PARAM_PCCPCH_RSCP = "i0004";
	public static final String PARAM_RXLEV_SUB = "i0005";
	public static final String PARAM_RXQUAL_SUB = "i0006";
	public static final String PARAM_THROUGHPUT_DL = "i0007";
	public static final String PARAM_THROUGHPUT_UL = "i0008";
	
	public static final String PARAM_GTX_POWER = "i0009";
	public static final String PARAM_BLER = "i0010";
	public static final String PARAM_SPEED = "i0011";
	
	public static final String TABLE_SERVICE_CELL_LTE = "0";
	public static final String TABLE_SERVICE_CELL_TD = "1";
	public static final String TABLE_SERVICE_CELL_GSM = "2";
	
	public static final String TABLE_NEIGHBOR_CELL_LTE = "3";
	public static final String TABLE_NEIGHBOR_CELL_TD = "4";
	public static final String TABLE_NEIGHBOR_CELL_GSM = "5";

	public static final String TABLE_INDEX_VOICE_LTE = "6";
	public static final String TABLE_INDEX_DATA_LTE = "7";
	public static final String TABLE_INDEX_VOICE_TD = "8";
	public static final String TABLE_INDEX_DATA_TD = "9";
	public static final String TABLE_INDEX_VOICE_GSM = "10";
	public static final String TABLE_INDEX_DATA_GSM = "11";
	public static final String TABLE_INDEX_TRIPLE = "12";
	
	public static final String TABLE_INDEX_VOICE = "13";
	public static final String TABLE_INDEX_SMS = "14";
	public static final String TABLE_INDEX_DATA_CONNECTION = "15";
	public static final String TABLE_INDEX_DATA_QUALITY = "16";
	
	public static void setMapLineColor(String type, int level, int color) {
		//mapParamColor[type * 10 + level] = color;
		mapParamColor.put(type + "_" + level, color);
	}
	
	public static int getMapLineColor(String type, int level) {
		//return mapParamColor[type * 10 + level];
		String key = type + "_" + level;
		if (mapParamColor.containsKey(key)) {
			return mapParamColor.get(type + "_" + level);
		} else {
			return 0;
		}
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
	
	public static final int MODE_LTE = 0;
	public static final int MODE_TD = 1;
	public static final int MODE_GSM = 2;
	public static final int MODE_UPLOAD = 3;
	public static final int MODE_DOWNLOAD = 4;

	public static final String EVENT_ID_HTTP_ATTEMPT = "2701";
	public static final String EVENT_ID_HTTP_SUCCESS = "2703";
	public static final String EVENT_ID_HTTP_FAIL = "2702";
	public static final String EVENT_ID_PING_ATTEMPT = "4409";
	public static final String EVENT_ID_PING_SUCCESS = "440A";
	public static final String EVENT_ID_PING_FAIL = "440B";
	
	public static final String EVENT_HTTP_ATTEMPT = "Http Attempt";
	public static final String EVENT_HTTP_SUCCESS = "Http Success";
	public static final String EVENT_HTTP_FAIL = "Http Fail";
	public static final String EVENT_PING_ATTEMPT = "Ping Attempt";
	public static final String EVENT_PING_SUCCESS = "Ping Success";
	public static final String EVENT_PING_FAIL = "Ping Fail";
	
	
	private static int netMode;

	public static int getNetMode() {
		return netMode;
	}

	public static void setNetMode(int mode) {
		netMode = mode;
	}
	
	//	add via chenzm 
	public static boolean isHigherUserPermission()
	{
		return USER_PERMISSION.equals("HIGHER");
	}
	//	add via chenzm end
}
