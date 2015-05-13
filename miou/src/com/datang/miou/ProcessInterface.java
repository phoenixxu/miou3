package com.datang.miou;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.TelephonyManager;

public class ProcessInterface {
	public static HoldLastRecieverServer mHoldLastServer = new HoldLastRecieverServer();
	
	private static ITelephony m_iTelephony = null;
	public static native void RpGPS(double dbLon,double dbLat,int nAltitude,int nSpeed);
	public static native void RpMOS(float fMosValue);
	public static native void RpLab(char chBusinessType, boolean bStart);
	public static native void RpAppEVT(int nEventID, String strEventInfo);
	public static native void RpApplicationInfo(int nAppRecTotal,int nAppSendTotal,
			int nApp_currentThroughput_dl,int nApp_currentThroughput_ul,String strKey);

	//write log interface
	public static native boolean StartLogWrite();
	public static native boolean StopLogWrite();
	public static native void SetLogMaxLen(int nBufferTime, int nMaxLen);
	
	//read log interface
	public static native boolean StartLogRead(String strLogPath);
	public static native boolean StopLogRead();
	public static native boolean PauseLogRead();
	public static native boolean StepLogRead();
	public static native boolean SeekLogPosition(char chPercent);
	public static native int GetLogPosition();
	public static native boolean SetReadSpeed(char chSpeed);

	//read hd interface
	public static native boolean StartHdConnect();
	public static native boolean StopHdConnect();
	//query interface
	public static native String GetIEByID(String strIEID);
	public static native String GetStatInfo(int nStatType);
	public static native boolean GetIEColByCallBack();
	public static native String GetHoldLastIE();
	public void CallBackHoldLastIE(String strValue)
	{
		System.out.println("call back "+strValue);
	}
	public static native void Close();
	public static native String hello();
	
	public static void getTelephony(Context context) {
		TelephonyManager telMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		Class<TelephonyManager> c = TelephonyManager.class;
		Method getITelephonyMethod = null;
		try {
			getITelephonyMethod = c.getDeclaredMethod("getITelephony", (Class[]) null);
			getITelephonyMethod.setAccessible(true);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		try {
			m_iTelephony = (ITelephony) getITelephonyMethod.invoke(telMgr, (Object[]) null);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean StartCall(String strNumber,boolean bVideo,boolean bMos,Context context) {
		if (strNumber == null || strNumber.isEmpty())
			return false;

		if (bVideo) {
			
		} else {
			Uri uri = Uri.parse("tel:" + strNumber);
			Intent it = new Intent(Intent.ACTION_CALL, uri);
			context.startActivity(it);
			System.out.println(uri.toString());
		}
		return true;
	}

	public static boolean StopCall(Context context) {
		try {
			if (m_iTelephony == null)
				getTelephony(context);
			if (m_iTelephony != null) {
				System.out.println("end call");
				m_iTelephony.endCall();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	static {
		System.loadLibrary("datadecoder");
		System.loadLibrary("ltel3decoder");
		System.loadLibrary("tdsl3decoder");
		System.loadLibrary("umdecoder");
		System.loadLibrary("nasdecoder");
		System.loadLibrary("miouinterface");
	}
}
