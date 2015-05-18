package com.datang.miou.testplan.ping;

import android.util.Log;

import com.datang.miou.ProcessInterface;
import com.datang.miou.datastructure.Globals;
import com.datang.miou.nlog.msg.cmcc.LteEvtInfo;

/**
 * HTTP测试公用抽象类
 */

public abstract class TestplanPingHelper {

		static final String TAG = "TestplanPingHelper";

		/**
		 * 记录Attempt
		 */
		public static void writePingAttempt() 
		{
			LteEvtInfo lteEvtInfo = new LteEvtInfo();
		
			lteEvtInfo.setEvent(Globals.EVENT_ID_PING_ATTEMPT);
			lteEvtInfo.setEventInfo(Globals.EVENT_PING_ATTEMPT);
			lteEvtInfo.setTime(System.currentTimeMillis());
			
			try
			{
				ProcessInterface.RpAppEVT(Integer.parseInt(lteEvtInfo.getEvent(), 16), lteEvtInfo.getEventInfo());
				Log.i(TAG, "End Report" + Integer.parseInt(lteEvtInfo.getEvent(), 16) + lteEvtInfo.getEventInfo());
			} 
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		/**
		 * 记录Success
		 */
		public static void writePingSuccess() 
		{
			LteEvtInfo lteEvtInfo = new LteEvtInfo();
		
			lteEvtInfo.setEvent(Globals.EVENT_ID_PING_SUCCESS);
			lteEvtInfo.setEventInfo(Globals.EVENT_PING_SUCCESS);
			lteEvtInfo.setTime(System.currentTimeMillis());
			
			try
			{
				ProcessInterface.RpAppEVT(Integer.parseInt(lteEvtInfo.getEvent(), 16), lteEvtInfo.getEventInfo());
				Log.i(TAG, "End Report" + Integer.parseInt(lteEvtInfo.getEvent(), 16) + lteEvtInfo.getEventInfo());
			} 
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		/**
		 * 记录Fail
		 */
		public static void writePingFail() 
		{
			LteEvtInfo lteEvtInfo = new LteEvtInfo();
		
			lteEvtInfo.setEvent(Globals.EVENT_ID_PING_FAIL);
			lteEvtInfo.setEventInfo(Globals.EVENT_PING_FAIL);
			lteEvtInfo.setTime(System.currentTimeMillis());
			
			try
			{
				ProcessInterface.RpAppEVT(Integer.parseInt(lteEvtInfo.getEvent(), 16), lteEvtInfo.getEventInfo());
				Log.i(TAG, "End Report" + Integer.parseInt(lteEvtInfo.getEvent(), 16) + lteEvtInfo.getEventInfo());
			} 
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
}
