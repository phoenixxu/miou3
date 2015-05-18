package com.datang.miou.testplan.http;

import android.util.Log;

import com.datang.miou.ProcessInterface;
import com.datang.miou.datastructure.Globals;
import com.datang.miou.nlog.msg.cmcc.LteEvtInfo;

/**
 * HTTP测试公用抽象类
 */

public abstract class TestplanHttpHelper {

		static final String TAG = "TestplanHttpHelper";

		/**
		 * 记录Attempt
		 */
		public static void writeHttpAttempt() 
		{
			LteEvtInfo lteEvtInfo = new LteEvtInfo();
		
			lteEvtInfo.setEvent(Globals.EVENT_ID_HTTP_ATTEMPT);
			lteEvtInfo.setEventInfo(Globals.EVENT_HTTP_ATTEMPT);
			lteEvtInfo.setTime(System.currentTimeMillis());
			
			try
			{
				ProcessInterface.RpAppEVT(Integer.parseInt(lteEvtInfo.getEvent(), 16), lteEvtInfo.getEventInfo());
			} 
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		/**
		 * 记录Success
		 */
		public static void writeHttpSuccess() 
		{
			LteEvtInfo lteEvtInfo = new LteEvtInfo();
		
			lteEvtInfo.setEvent(Globals.EVENT_ID_HTTP_SUCCESS);
			lteEvtInfo.setEventInfo(Globals.EVENT_HTTP_SUCCESS);
			Log.i(TAG, "time = " + System.currentTimeMillis());
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
		 * 记录Fail
		 */
		public static void writeHttpFail() 
		{
			LteEvtInfo lteEvtInfo = new LteEvtInfo();
		
			lteEvtInfo.setEvent(Globals.EVENT_ID_HTTP_FAIL);
			lteEvtInfo.setEventInfo(Globals.EVENT_HTTP_FAIL);
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
