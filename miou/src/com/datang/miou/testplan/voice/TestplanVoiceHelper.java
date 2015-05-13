package com.datang.miou.testplan.voice;
import com.datang.miou.nlog.msg.cmcc.LteEvtInfo;
import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

import com.datang.miou.ProcessInterface;

import android.os.SystemClock;
import android.util.Log;


/*import com.datang.outum.nlog.LogType;
import com.datang.outum.nlog.LogWriterHandler;
import com.datang.outum.nlog.msg.cmcc.LabInfo;
import com.datang.outum.nlog.msg.cmcc.LteEvtInfo;*/

/**
 * VOICE公用抽象类
 * 
 * @author suntongwei
 */
public abstract class TestplanVoiceHelper 
{
	// LOG记录
	//	private static final String TAG = "TestplanVoiceHelper";
	private static final String TAG = "chenzm";
	
	
	/**
	 * 记录Attempt
	 */
	public static void writeCallAttempt(boolean isOutgoing) 
	{
		LteEvtInfo lteEvtInfo = new LteEvtInfo();
		if(isOutgoing)
		{
			lteEvtInfo.setEvent("2000");
			lteEvtInfo.setEventInfo("Outgoing Call Attempt");
		} 
		else
		{
			lteEvtInfo.setEvent("2004");
			lteEvtInfo.setEventInfo("Incoming Call Attempt");
		}
		
		lteEvtInfo.setTime(System.currentTimeMillis());
		try
		{
			Log.i(TAG, "start Report");
			ProcessInterface.RpAppEVT(Integer.parseInt(lteEvtInfo.getEvent(), 16), lteEvtInfo.getEventInfo());
			Log.i(TAG, "End Report" + Integer.parseInt(lteEvtInfo.getEvent(), 16) + lteEvtInfo.getEventInfo());
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 记录Alerting
	 */
	public static void writeCallAlerting(boolean isOutgoing) 
	{
		LteEvtInfo lteEvtInfo = new LteEvtInfo();
		if(isOutgoing)
		{
			lteEvtInfo.setEvent("2001");
			lteEvtInfo.setEventInfo("Outgoing Call Alerting");
		} 
		else
		{
			lteEvtInfo.setEvent("2005");
			lteEvtInfo.setEventInfo("Incoming Call Alerting");
		}
		
		lteEvtInfo.setTime(System.currentTimeMillis());
		try
		{
			Log.i(TAG, "start Report");
			ProcessInterface.RpAppEVT(Integer.parseInt(lteEvtInfo.getEvent(), 16), lteEvtInfo.getEventInfo());
			Log.i(TAG, "End Report" + Integer.parseInt(lteEvtInfo.getEvent(), 16) + lteEvtInfo.getEventInfo());
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 记录Connected
	 */
	public static void writeCallConnected(boolean isOutgoing) 
	{
		LteEvtInfo lteEvtInfo = new LteEvtInfo();
		if(isOutgoing)
		{
			lteEvtInfo.setEvent("2002");
			lteEvtInfo.setEventInfo("Outgoing Call Connected");
		} 
		else
		{
			lteEvtInfo.setEvent("2006");
			lteEvtInfo.setEventInfo("Incoming Call Connected");
		}
		
		lteEvtInfo.setTime(System.currentTimeMillis());
		try
		{
			Log.i(TAG, "start Report");
			ProcessInterface.RpAppEVT(Integer.parseInt(lteEvtInfo.getEvent(), 16), lteEvtInfo.getEventInfo());
			Log.i(TAG, "End Report" + Integer.parseInt(lteEvtInfo.getEvent(), 16) + lteEvtInfo.getEventInfo());
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 记录Failure
	 */
	public static void writeCallFailure(boolean isOutgoing) 
	{
		LteEvtInfo lteEvtInfo = new LteEvtInfo();
		if(isOutgoing)
		{
			lteEvtInfo.setEvent("2003");
			lteEvtInfo.setEventInfo("Outgoing Call Failure");
		} 
		else
		{
			lteEvtInfo.setEvent("2007");
			lteEvtInfo.setEventInfo("Incoming Call Failure");
		}
		
		lteEvtInfo.setTime(System.currentTimeMillis());
		try
		{
			Log.i(TAG, "start Report");
			ProcessInterface.RpAppEVT(Integer.parseInt(lteEvtInfo.getEvent(), 16), lteEvtInfo.getEventInfo());
			Log.i(TAG, "End Report" + Integer.parseInt(lteEvtInfo.getEvent(), 16) + lteEvtInfo.getEventInfo());
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 记录Complete
	 */
	public static void writeCallComplete(boolean isOutgoing) 
	{
		LteEvtInfo lteEvtInfo = new LteEvtInfo();

		if(isOutgoing)
		{
			lteEvtInfo.setEvent("2008");
			lteEvtInfo.setEventInfo("Outgoing Call Complete");
		} 
		else
		{
			lteEvtInfo.setEvent("2008");
			lteEvtInfo.setEventInfo("Incoming Call Complete");
		}
		
		lteEvtInfo.setTime(System.currentTimeMillis());
		try
		{
			Log.i(TAG, "start Report");
			ProcessInterface.RpAppEVT(Integer.parseInt(lteEvtInfo.getEvent(), 16), lteEvtInfo.getEventInfo());
			Log.i(TAG, "End Report" + Integer.parseInt(lteEvtInfo.getEvent(), 16) + lteEvtInfo.getEventInfo());
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 记录Drop
	 */
	public static void writeDropCall(boolean isOutgoing) 
	{
		LteEvtInfo lteEvtInfo = new LteEvtInfo();

		if(isOutgoing)
		{
			lteEvtInfo.setEvent("2009");
			lteEvtInfo.setEventInfo("Outgoing Drop Call");
		} 
		else
		{
			lteEvtInfo.setEvent("2009");
			lteEvtInfo.setEventInfo("Incoming Drop Call");
		}
		
		lteEvtInfo.setTime(System.currentTimeMillis());
		try
		{
			Log.i(TAG, "start Report");
			ProcessInterface.RpAppEVT(Integer.parseInt(lteEvtInfo.getEvent(), 16), lteEvtInfo.getEventInfo());
			Log.i(TAG, "End Report" + Integer.parseInt(lteEvtInfo.getEvent(), 16) + lteEvtInfo.getEventInfo());
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
