package com.datang.miou.testplan;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import com.datang.business.MeasurementError;
import com.datang.miou.datastructure.Globals;
import com.datang.miou.datastructure.TestScheme;
import com.datang.miou.ftp.FtpConfigParser;
import com.datang.miou.services.TestplanService;
import com.datang.miou.testplan.ftp.TestplanFtpDown;
import com.datang.miou.testplan.ftp.TestplanFtpUpload;
import com.datang.miou.testplan.http.TestplanHttp;
import com.datang.miou.testplan.ping.TestplanPing;
import com.datang.miou.testplan.voice.TestplanVoiceCalling;
import com.datang.miou.utils.SDCardUtils;
import com.datang.miou.xml.PullTestPlanParser;
import com.datang.miou.PhoneCallListener;
import com.datang.miou.ProcessInterface;
import com.datang.miou.MiouApp;

public class Testplan
{
	//	private static final String TAG = "Testplan";
	private static final String TAG = "chenzm";
	
	
	private TestplanVoiceCalling mTestplanVoiceCalling = null;		
	private PhoneCallListener mPhoneCallListener = null;
	private TestplanFtpDown mTestplanFtpDown = null;
	private TestplanFtpUpload mTestplanFtpUpload = null;
	private TestplanHttp mTestplanHttp  = null;
	private TestplanPing mTestplanPing  = null;
	
	private List<TestScheme> mTestPlans = null;
	private int mTestplansCount = 0;
	
	private boolean mPhoneCallState = false;
	
	private Context mContext = null;
	
	private Context mTestplanContext = null;
	
	/** 
     * 构造类
     */
	public Testplan(Context context)
	{
		//	主页上下文
		mContext = context;
	}
	
	/** 
     * 开始业务
     */
	public void startTesting() 
	{
		// TODO 自动生成的方法存根
		Log.i(TAG, "---------------- startTesting ----------------");
		
		//	判断是否有测试计划是否未完成
		if(((MiouApp) mContext.getApplicationContext()).isGenReviewing())
		{
			Log.i(TAG, "testplan business progress.");
			stopTesting();
			return;
		}
		
		//	设置测试计划标志为ture
		((MiouApp) mContext.getApplicationContext()).setGenTesting(true);
		
		//	开始记录log
		if (((MiouApp) mContext.getApplicationContext()).isGenLogging()) 
		{	
			ProcessInterface.StartLogWrite();
		}
		
		//	重置计数
		resetTestplansCount();
		
		try
		{
			//	获取测试计划脚本文件
			String latestFile = getLatestFile();
			
			//	创建  testplan 测试类
			PullTestPlanParser parser = new PullTestPlanParser();
			
			//	获取 testplan xml 媒体流
			FileInputStream is = parser.GetStream(latestFile);
			if(is == null)
			{
				Log.i(TAG, "not find testplan xml file.");
				stopTesting();
				return ;
			}
			
			//	解析 testplan xml
			mTestPlans = parser.parse(is);
			
			//	执行测试计划
			if(handleTestplans())
			{
				Log.i(TAG, "not find testplan.");
				stopTesting();
			}
			else
			{
				//	更新图标至停止
				sendGenActivityBroadcast(false);
			}
		}
		catch(Exception e)
		{
			Log.i(TAG, "startTesting Exception: " + e);
			e.printStackTrace();
			
			//	停止测试
			stopTesting();
		}
	}
	
	/** 
     * 停止业务
     */ 
	public void stopTesting() 
	{
		// TODO 自动生成的方法存根
		Log.i(TAG, "---------------- stopTesting ----------------");
		
		//	停止测试业务
		stopTestplanVoiceMaster();
		stopTestplanVoiceSlave();
		stopTestplanFtp();
		
		//	停止记录log
		if (((MiouApp) mContext.getApplicationContext()).isGenLogging()) 
		{
			ProcessInterface.StopLogWrite();
		}
		
		//	更新图标至开始
		sendGenActivityBroadcast(true);
		
		//	设置测试计划标志为false
		((MiouApp) mContext.getApplicationContext()).setGenTesting(false);
	}
	
	/** 
     * 执行testplan
     */
	public boolean handleTestplans()
	{
		if(mTestPlans != null)
		{
			Log.i(TAG, "testPlansCount: " + mTestplansCount + ",testPlansSize: " + mTestPlans.size());
			
			for(int i = mTestplansCount;i < mTestPlans.size(); i++)
			{
				TestScheme testplan = mTestPlans.get(i);
				if(Integer.parseInt(testplan.getEnable()) == 1)
				{
					//	延时
					SystemClock.sleep(500);
					
					//	voice 主叫
					if(testplan.getCommandList().getCommand().getId().equals(Globals.TEST_COMMAND_VOICE_MASTER))
					{
						startTestplanVoiceMaster(testplan);
						mTestplansCount++;
						return false;
					}
					//	voice 被叫
					else if(testplan.getCommandList().getCommand().getId().equals(Globals.TEST_COMMAND_VOICE_SLAVE))
					{
						startTestplanVoiceSlave(testplan);
						mTestplansCount++;
						return false;
					}
					//	ftp
					else if(testplan.getCommandList().getCommand().getId().equals(Globals.TEST_COMMAND_FTP))
					{
						//	判断当前网络是否可用
						if(((MiouApp) mContext.getApplicationContext()).isAvailableNet())
						{
							startTestplanFtp(testplan);
							mTestplansCount++;
							return false;
						}
					}
					else if (testplan.getCommandList().getCommand().getId().equals(Globals.TEST_COMMAND_HTTP))
					{
						// http
						startTestplanHttp(testplan);
						mTestplansCount++;
						return false;
					} 
					else if (testplan.getCommandList().getCommand().getId().equals(Globals.TEST_COMMAND_PING))
					{
						// ping
						startTestplanPing(testplan);
						mTestplansCount++;
						return false;
					}
				}
				else
				{
					Log.i(TAG, "disable testplan id: " + testplan.getCommandList().getCommand().getId());
				}
				
				//	更新当前计数
				mTestplansCount++;
			}
		}
		else
		{
			Log.i(TAG, "testPlans is null");
		}
		
		return true;
	}
	
	/**
	 * 发送类型广播
	 */
	private void sendGenActivityBroadcast(boolean isOn)
	{
		// 实例化Intent对象
        Intent intent = new Intent();
        
        // 设置Intent action属性
        intent.setAction("com.datang.miou.views.gen.action.GENACTIVITY_ACTION");
        
        //	设置参数
        intent.putExtra("enable", isOn);

        // 发出广播
        mContext.sendBroadcast(intent);
	}

	/** 
     * 重置统计
     */
	private void resetTestplansCount()
	{
		mTestplansCount = 0;
	}
	
	/** 
     * 获取最新的测试计划脚本文件
     */
	private String getLatestFile()
	{
		File dir = new File(SDCardUtils.getTestPlanPath());
		String latestFile = null;
		long lastTime = 0;
		for (String string : dir.list()) 
		{
			if (string.startsWith("TestScript")) 
			{
				File currentFile = new File(SDCardUtils.getTestPlanPath(), string);
				if (currentFile.lastModified() > lastTime)
				{
					lastTime = currentFile.lastModified();
					latestFile = string;
				}
			}
		}
		
		return latestFile;
	}
	
	/*
	 * Ping测试业务
	 */
	private void startTestplanPing(TestScheme testplan) {
		mTestplanPing = new TestplanPing(mContext, testplan);
		
		try {
			mTestplanPing.startPingTest();
		} catch (MeasurementError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void stopTestplanPing() {
		// TODO Auto-generated method stub
		if (mTestplanPing != null) {
			mTestplanPing.stopPingTest();
		}
	}
	
	/*
	 * http测试业务
	 */
	private void startTestplanHttp(TestScheme testplan) {
		// TODO Auto-generated method stub
		
		
		mTestplanHttp = new TestplanHttp(mContext, testplan);
		
		try {
			mTestplanHttp.startHttpTest();
		} catch (MeasurementError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void stopTestplanHttp() {
		// TODO Auto-generated method stub
		if (mTestplanHttp != null) {
			mTestplanHttp.stopHttpTest();
			mTestplanHttp = null;
		}
	}
	
	
	/** 
     * voice主叫业务
     */ 
	private void startTestplanVoiceMaster(TestScheme testplan) 
	{
		Log.i(TAG, "start voice calling testplan.");
		try
		{
			mTestplanVoiceCalling = new TestplanVoiceCalling(mContext,testplan);	
			mPhoneCallListener = new PhoneCallListener();
			mPhoneCallListener.register(mContext,true,0);
			mTestplanVoiceCalling.startVoiceCalling();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void stopTestplanVoiceMaster() 
	{
		if(mTestplanVoiceCalling != null)
		{
			mTestplanVoiceCalling.stopVoiceCalling();
			mTestplanVoiceCalling = null;
			if(mPhoneCallListener != null)
			{
				mPhoneCallListener.unregister();
				mPhoneCallListener = null;
			}
		}
	}
	
	/** 
     * voice被叫业务
     */ 
	private void startTestplanVoiceSlave(TestScheme testplan)
	{
		Log.i(TAG, "startTestplanVoiceSlave.");
		mPhoneCallState = true;
		mPhoneCallListener = new PhoneCallListener();
		int repeatNum = Integer.parseInt(testplan.getCommandList().getCommand().getRepeat());
		mPhoneCallListener.register(mContext,false,repeatNum);
	}
	
	private void stopTestplanVoiceSlave() 
	{
		if(mPhoneCallListener != null)
		{
			mPhoneCallListener.unregister();
			mPhoneCallListener = null;
		}
		mPhoneCallState = false;
	}
	
	/** 
     * ftp业务
     */ 
	private void startTestplanFtp(TestScheme testplan) 
	{
		Log.i(TAG, "start ftp testing.");
		 
		//	设置网络模式
		Globals.setNetMode(Globals.MODE_LTE);
		
		try
		{
			//	获取Ftp业务进程数
			FtpConfigParser configparser = new FtpConfigParser();
			configparser.parse();
			
			int threadnum = Integer.parseInt(configparser.getThreadNum());
			
			//	启动 ftp 线程
			//	获取当前的activity
			if(testplan.getCommandList().getCommand().getDownload().equals("1"))	//	test test
			{
				mTestplanFtpDown = new TestplanFtpDown(mContext,testplan,threadnum);	
				mTestplanFtpDown.StartFtpDown();
			}
			else
			{
				mTestplanFtpUpload = new TestplanFtpUpload(mContext,testplan,threadnum);	//	dt dt	
				mTestplanFtpUpload.StartFtpUpload();
			}
		}
		catch(Exception e)
		{
			Log.i(TAG, "startTestplanFtp Exception: " + e);
		}
		
		Log.i(TAG, "start ftp testing end.");
	}

	private void stopTestplanFtp() 
	{
		//	下载
		if(mTestplanFtpDown != null)
		{
			mTestplanFtpDown.StopFtpDown();
			mTestplanFtpDown = null;
		}
		
		//	上传
		if(mTestplanFtpUpload != null)
		{
			mTestplanFtpUpload.StopFtpUpload();
			mTestplanFtpUpload = null;
		}
	}
}