package com.datang.miou.testplan.ftp;

import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicLong;

//import org.apache.log4j.Logger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import com.datang.miou.ProcessInterface;
import com.datang.miou.testplan.bean.Ftp;

import com.datang.miou.datastructure.TestScheme;
import com.datang.miou.nlog.msg.cmcc.*;

/*import com.datang.outum.nlog.LogType;
import com.datang.outum.nlog.LogWriterHandler;
import com.datang.outum.nlog.msg.cmcc.LteEvtInfo;
import com.datang.miou.nlog.LteFtpInfo;
*/
/**
 * FTP统计线程
 * 
 * @author suntongwei
 */
public class TestplanFtpStat implements Runnable 
{
	private static final String TAG = "TestplanFtpStat";
	//	private static final String TAG = "chenzm";
	
	// DecimalFormat
	private static LteFtpInfo outFtpInfo = new LteFtpInfo();
	
	private static final DecimalFormat DF = new DecimalFormat("######.##");
	
	// ctx
	private Context mContext;

	// 开始时间
	private long startTime;
	
	// 结束时间
	private long endTime = -1l;

	// 已下载或已上传字节数
	private AtomicLong len = new AtomicLong();

	// 需要下载或上传的总字节数
	private long totalLen = 0l;
	
	// FTP参数
	private TestScheme mTestPlan = null;
	
	//
	private boolean mIsDown;
	
	/**
	 * 
	 * @param ctx
	 */
	public TestplanFtpStat(Context ctx)
	{
		mContext = ctx;
	}

	/**
	 * 初始化统计线程
	 * 
	 * @param isDown
	 * @param startTime
	 * @param totalLen
	 */
	public void init(TestScheme testplan, boolean isDown,long startTime, long totalLen) 
	{
		len.set(0l);
		endTime = -1l;
		this.mTestPlan = testplan;
		this.mIsDown = isDown;
		this.startTime = startTime;
		this.totalLen = totalLen;
	}


	@Override
	public void run() 
	{
		
		//Log.error("FtpStatThread Run...");
		Log.v(TAG,"FtpStatThread Run...");
		
		/*LteEvtInfo startLteEvtInfo = new LteEvtInfo();
		startLteEvtInfo.setTime(System.currentTimeMillis());
		if(ftpParams.getIsDown())
		{
			startLteEvtInfo.setEvent("4101");
		}
		else 
		{
			startLteEvtInfo.setEvent("4102");
		}
		startLteEvtInfo.setEventInfo("FTP");
		
		ProcessInterface.RpAppEVT(Integer.parseInt(startLteEvtInfo.getEvent()), startLteEvtInfo.getEventInfo());
		//stopLteEvtInfo.setFileSize(String.valueOf(len.get()));
		
		Log.v("ftpstat",startLteEvtInfo.getEventInfo());*/
		
		
		Intent intent = new Intent("com.datang.action.data");
		
		// 最大速率
		long maxSpeed = 0l;
		
		// 上一次速率
		long lastLen = 0l;
		
		boolean isRun = true;
		
		//上报速率定义
		String strReceivedTotal;
		String strSendTotal;
		String strCurrentThroughputdl;
		String strCurrentThroughputul;
		int nReceivedTotal = 0;
		int nSendTotal = 0;
		int nCurrentThroughputdl = 0;
		int nCurrentThroughputul = 0;
		
		// 开始统计
		while(!Thread.currentThread().isInterrupted() || isRun) 
		{
			
			/**
			 * 为了保护最后一次统计的呈现
			 * 如果下载完成后，并没有到每秒统计时间，则会丢失最后一次统计的过程
			 * 以下代码保证了当下载或上传完成时，进行最后一次统计
			 */
			if(Thread.currentThread().isInterrupted())
			{
				Log.i(TAG,"Report out");
				isRun = false;
			}
			
			// 获得当前统计的结算时间
			double runTime = (System.currentTimeMillis() - startTime) / 1000;
			if(endTime > 0) 
			{
				runTime = (endTime - startTime) / 1000;
			}
			
			// 获得当前下载总字节数
			long curTotalLen = len.get();
			
			// 当前每秒下载速率
			long curLen = curTotalLen - lastLen;
			
			// 如果当前速率大于最大速率，则记录该最大速率
			if(curLen > maxSpeed) 
			{
				maxSpeed = curLen;
			}
			
			Bundle value = new Bundle();
	        
	        value.putBoolean("Ftp_Down", mIsDown);
	        value.putString("Ftp_Total", logFormat(curTotalLen));
	        value.putLong("Ftp_Total_Val", curTotalLen);
	        curLen = curLen > 0 ? curLen : 0;
	        value.putLong("Ftp_Current_Val", curLen);
	        value.putString("Ftp_Current", formatSpeed(curLen));
	        value.putDouble("Ftp_Max_Val", maxSpeed);
	        value.putString("Ftp_Max", formatSpeed(maxSpeed));
	        
	        if(runTime > 0) 
	        {
	        	 double avgLen = curTotalLen / runTime;
			     value.putDouble("Ftp_Avg_Val", avgLen);
			     value.putString("Ftp_Avg", formatSpeed(avgLen));
	        } 
	        else 
	        {
	        	value.putDouble("Ftp_Avg_Val", 0d);
			    value.putString("Ftp_Avg", formatSpeed(0d));
	        }
	        
	        value.putLong("Ftp_RunTime", (long) runTime);
	        
	        intent.putExtra("DATA", value);
	        mContext.sendBroadcast(intent);
	        
	        // 记录日志
	        if(curTotalLen != 0 || runTime > 1) 
	        {
	        	LteFtpInfo lteFtpInfo = new LteFtpInfo();
	 			lteFtpInfo.setTime(System.currentTimeMillis());
	 			if(mIsDown) 
	 			{
	 				lteFtpInfo.setApp_currentThroughputdl(DF.format((curTotalLen - lastLen) * 8 / 1024));
	 				lteFtpInfo.setAppBytesReceivedLTE(String.valueOf(curTotalLen / 1024));
	 				lteFtpInfo.setAppBytesReceivedTotal(String.valueOf(curTotalLen / 1024));
	 			} 
	 			else 
	 			{
	 				lteFtpInfo.setApp_currentThroughputul(DF.format((curTotalLen - lastLen) * 8 / 1024));
	 				lteFtpInfo.setAppBytesSendLTE(String.valueOf(curTotalLen / 1024));
	 				lteFtpInfo.setAppBytesSendTotal(String.valueOf(curTotalLen / 1024));
	 			}
	 			
	 			//LogWriterHandler.getInstance().writeLog(LogType.CMCC,lteFtpInfo);
	 			//ProcessInterface.RpApplicationInfo(Integer.parseInt(lteFtpInfo.getAppBytesReceivedTotal()), Integer.parseInt(lteFtpInfo.getAppBytesSendTotal()), Integer.parseInt(lteFtpInfo.getApp_currentThroughputdl()), Integer.parseInt(lteFtpInfo.getApp_currentThroughputul()), "FTP");
	 			strReceivedTotal = lteFtpInfo.getAppBytesReceivedTotal();
	 			if(!strReceivedTotal.isEmpty())
	 			{
	 				nReceivedTotal = Integer.parseInt(strReceivedTotal);
	 			}

	 			strSendTotal = lteFtpInfo.getAppBytesSendTotal();
	 			if(!strSendTotal.isEmpty())
	 			{
	 				nSendTotal = Integer.parseInt(strSendTotal);
	 			}

	 			strCurrentThroughputdl = lteFtpInfo.getApp_currentThroughputdl();
	 			if(!strCurrentThroughputdl.isEmpty())
	 			{
	 				nCurrentThroughputdl = Integer.parseInt(strCurrentThroughputdl);
	 			}

	 			strCurrentThroughputul = lteFtpInfo.getApp_currentThroughputul();
	 			if(!strCurrentThroughputul.isEmpty())
	 			{
	 				nCurrentThroughputul = Integer.parseInt(strCurrentThroughputul);
	 			}
	 			
	 			Log.i(TAG, "Start RpApplicationInfo");
	 			ProcessInterface.RpApplicationInfo(nReceivedTotal, nSendTotal, nCurrentThroughputdl, nCurrentThroughputul, "FTP");
	 			Log.i(TAG, "End RpApplicationInfo");
	 			Log.v("stat",lteFtpInfo.toString());
	        }
	        
	        // 把当前总长度赋予上一次总长度，来统计当前速率
	        lastLen = curTotalLen;
	        
	        SystemClock.sleep(1000);
		}
		
		// 如果下载或上传成功则记录成功事件
		if(isEnd()) 
		{
			// 写入最后次统计信息
			// LTEEVT   FTP_(totalLen)
			//	del via chenzm
			/**
			LteEvtInfo stopLteEvtInfo = new LteEvtInfo();
			stopLteEvtInfo.setTime(System.currentTimeMillis());
			if(mIsDown) 
			{
				stopLteEvtInfo.setEvent("4104");
				stopLteEvtInfo.setEventInfo("FTP Download Success");
			} 
			else 
			{
				stopLteEvtInfo.setEvent("4107");
				stopLteEvtInfo.setEventInfo("FTP Upload Suess");
			}
			stopLteEvtInfo.setFileSize(String.valueOf(len.get()));
			try
			{
				Log.i(TAG, "Start Report");
				ProcessInterface.RpAppEVT(Integer.parseInt(stopLteEvtInfo.getEvent(), 16), stopLteEvtInfo.getEventInfo());
				Log.i(TAG, "End Report" + Integer.parseInt(stopLteEvtInfo.getEvent(), 16) + stopLteEvtInfo.getEventInfo());
			} 
			catch (Exception e)
			{
				e.printStackTrace();
			}
			//LogWriterHandler.getInstance().writeLog(LogType.CMCC, stopLteEvtInfo);
			*/
			//	del via chenzm end
			
			if(len.get() > 0) 
			{
				LteFtpInfo lteFtpInfo = new LteFtpInfo();
				lteFtpInfo.setTime(System.currentTimeMillis());
				if(mIsDown)
				{
					lteFtpInfo.setAppBytesReceivedLTE(String.valueOf(len.get() / 1024));
					lteFtpInfo.setAppBytesReceivedTotal(String.valueOf(len.get() / 1024));
				} 
				else
				{
					lteFtpInfo.setAppBytesSendLTE(String.valueOf(len.get() / 1024));
					lteFtpInfo.setAppBytesSendTotal(String.valueOf(len.get() / 1024));
				}
				//LogWriterHandler.getInstance().writeLog(LogType.CMCC,lteFtpInfo);
				outFtpInfo = lteFtpInfo;
			}
			
			
			//ProcessInterface.StopLogWrite();//开始记录log
		}
		
		//Log.error("Ftp Stat Stop");
		Log.v(TAG,"Ftp Stat Stop");
	}

	/**
	 * 返回总长度
	 * 
	 * @return
	 */
	public long getLen() 
	{
		return len.get();
	}

	/**
	 * 设置总长度，增加长度保护措施，当处理的总字节数超过了需要下载或上传的总字节数
	 * 
	 * @param l
	 * @return
	 */
	public void setLen(long l) 
	{
		if(len.get() < totalLen) 
		{
			len.addAndGet(l);
		} 
		else 
		{
			len.set(totalLen);
		}
	}

	/**
	 * 主要判断是否下载结束
	 * 如果下载结束了，并设置当前结束时间
	 * 
	 * @return
	 */
	public boolean isEnd() 
	{
		if(len.get() >= totalLen)
		{
			endTime = System.currentTimeMillis();
			return true;
		}
		return false;
	}
	
	public void setTotalLen(long len)
	{
		totalLen = len;
	}
	
	private String formatSpeed(double speed)
	{
		DecimalFormat df = null;
		double kbps = (speed * 8) / 1024;
		if(kbps > 1024) 
		{
			df = new DecimalFormat("######.##");
			return df.format(kbps / 1024) + "Mbps";
		}
		df = new DecimalFormat("######");
		return df.format(kbps) + "Kbps";
	}
	
	public String logFormat(long total) 
	{
        DecimalFormat df = null;
        double kbps = total / 1024;
        if (kbps > 1024) 
        {
            df = new DecimalFormat("#########.##");
            return df.format(kbps / 1024) + "MB";
        }
        df = new DecimalFormat("#########");
        return df.format(kbps) + "KB";
    }
	
	public static LteFtpInfo getoutFtpInfo()
	{ 
		return outFtpInfo;
	}
	
}
