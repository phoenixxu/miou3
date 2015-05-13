package com.datang.miou.testplan.voice;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.datang.miou.ProcessInterface;
import com.datang.miou.datastructure.TestScheme;
import com.datang.miou.services.TestplanService;
import com.datang.miou.testplan.ftp.TestplanFtpHelper;
import com.datang.miou.testplan.ftp.TestplanFtpDown.Callbacks;


/**
 * VoiceCalling主线程,定时器机制
 * 
 * @author chenzeming
 */
public class TestplanVoiceCalling extends TestplanVoiceHelper
{
	public interface Callbacks {
		public void updateUIOnFinnished();
	}
	
	//	private static final String TAG = "VoiceThread";
	private static final String TAG = "chenzm";

	// CTX
	private Context mContext;
	
	// Voice参数集合
	private TestScheme mTestPlan = null;
	
	// 已执行次数
	private int curNum = 0;
	
	//	xx
	private Handler mHandler;
	
	//	voice用例
	private Runnable mRunnableCall;
	private Runnable mRunnableHandup;
	
	private enum VoiceRunState
	{
		STOP,
		WAITING,
		START
	}
	
	//	xx
	private VoiceRunState mRunState = VoiceRunState.STOP;
	
	/**
	 * 
	 * 构造类
	 * @param ctx
	 * @param voice
	 */
	public TestplanVoiceCalling(Context ctx, TestScheme testplan)
	{
		// ctx
		mContext = ctx;
		
		// 参数
		mTestPlan = testplan;
		
		mHandler = new Handler();
		
		//	呼叫操作
		mRunnableCall = new Runnable(){
			public void run()
			{
				//	呼叫操作
				StartCall();
			}
		};
		
		//	挂断操作
		mRunnableHandup =  new Runnable(){
			public void run()
			{
				//	挂掉操作
				StopCall();
			}
		}; 
	}
	
	/**
	 * 
	 * 开始主叫测试
	 */
	public void startVoiceCalling()
	{
		//	设置状态
		mRunState = VoiceRunState.START;
		
		//	呼叫
		StartCall();
	}
	
	/**
	 * 
	 * 停止主叫测试
	 * 
	 */
	public void stopVoiceCalling()
	{
		mHandler.removeCallbacks(mRunnableCall);
		mHandler.removeCallbacks(mRunnableHandup);
		
		//	设置状态
		mRunState = VoiceRunState.STOP;
	}
	
	/**
	 * 是否在主叫测试中
	 */
	public boolean isVoiceCalling()
	{
		return (mRunState != VoiceRunState.STOP);
	}
	
	/**
	 * 
	 * 呼叫
	 */
	private void StartCall()
	{
		//	打印
		Log.i(TAG, "call count = " + (curNum+1));
		
		//	写信令
		writeCallAttempt(true);
		writeCallAlerting(true);
		
		//	呼叫
		TestplanService.sendVoiceMasterBroadcast(true,getCallNumber());
		//	ProcessInterface.StartCall(getCallNumber(),false,false,mContext);
		
		// 设置挂掉操作定时器
		mHandler.postDelayed(mRunnableHandup, 1000 * getDuration());
	}
	
	/**
	 * 
	 * 挂断
	 */
	private void StopCall()
	{
		//	写信令
		writeCallComplete(true);
			
		//	挂断
		//	ProcessInterface.StopCall(mContext);
		TestplanService.sendVoiceMasterBroadcast(false,getCallNumber());
		
		//	统计
		curNum++;
		
		//	设置呼叫操作定时器
		if(curNum < getRepeat())
		{
			mHandler.postDelayed(mRunnableCall, 1000 * getInterval());
		}
		//	calling plantest finished
		else
		{
			Log.i(TAG, "calling plantest finished.");
			
			//	判断当前是否处于通用测试界面
			((Callbacks) mContext).updateUIOnFinnished();
			
			//	设置状态
			mRunState = VoiceRunState.STOP;
		}
	}
	
	/**
	 * 返回执行次数
	 */
	public int getExecNum() 
	{
		return curNum;
	}
	
	/**
	 * 
	 * 获取电话号码
	 */
	private String getCallNumber()
	{
		return mTestPlan.getCommandList().getCommand().getCallNumber();
	}
	
	/**
	 * 
	 * 获取xx
	 */
	private int getRandomCall()
	{
		return Integer.parseInt(mTestPlan.getCommandList().getCommand().getRandomCall());
	}
	
	/**
	 * 
	 * 获取保持呼叫时长
	 */
	private int getDuration()
	{
		return Integer.parseInt(mTestPlan.getCommandList().getCommand().getDuration());
	}
	
	/**
	 * 
	 * 获取呼叫间隔时长
	 */
	private int getInterval()
	{
		return Integer.parseInt(mTestPlan.getCommandList().getCommand().getInterval());
	}
	
	/**
	 * 
	 * 获取xx
	 */
	private int getMaxTime()
	{
		return Integer.parseInt(mTestPlan.getCommandList().getCommand().getMaxTime());
	}
	
	/**
	 * 
	 * 获取xx
	 */
	private int getTestMOS()
	{
		return Integer.parseInt(mTestPlan.getCommandList().getCommand().getTestMos());
	}
	
	/**
	 * 
	 * 获取xx
	 */
	private int getCallMOSServer()
	{
		return Integer.parseInt(mTestPlan.getCommandList().getCommand().getCallMosServer());
	}
	
	/**
	 * 
	 * 获取重复次数
	 */
	private int getRepeat()
	{
		return Integer.parseInt(mTestPlan.getCommandList().getCommand().getRepeat());
	}
	
	/**
	private float getMOSLimit()
	{
		return Integer.parseInt(mTestPlan.getCommandList().getCommand().getCallNumber());
	}
	*/
}
