package com.datang.miou;

import com.datang.miou.testplan.voice.TestplanVoiceHelper;

import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;

public class PhoneCallListener extends PhoneStateListener 
{
	public interface Callbacks {
		public void updateUIOnFinnished();
	}
	
	//	private static final String TAG = "PhoneCallListener";
	private static final String TAG = "chenzm";
	public static boolean AUTO_MOS = true;
	public static boolean AUTO_ANSWER_CALL = true;
	public static Context mContext = null;
	private TelephonyManager manager = null;
	public static boolean mRunState = false;
	private int mCount = 0;
	private int mCountMax = 0;
	
	/** 
     * 注册电话系统服务
     */
	public void register(Context context)
	{
		Log.i(TAG, "PhoneCallListener register.");
		
		mContext = context;
		
		if(manager == null)
		{
			Log.i(TAG, "register sucess.");
			
			//	获取电话系统服务
			manager = (TelephonyManager) mContext.getSystemService(context.TELEPHONY_SERVICE);
			
	        //	手动注册对PhoneStateListener中的listen_call_state状态进行监听  
	        manager.listen(this, PhoneStateListener.LISTEN_CALL_STATE);
		}
		
	}
	
	public void unregister()
	{
		if(manager != null)
		{
			 manager.listen(null, PhoneStateListener.LISTEN_NONE);
		}
	}
	
	/** 
     * 启动
     */
	public void start(int numRepeat)
	{
		Log.i(TAG, "PhoneCallListener start.");
		mCountMax = numRepeat;
		mRunState = true;
	}
	
	/** 
     * 关闭
     */
	public void stop()
	{
		Log.i(TAG, "PhoneCallListener stop.");
		mRunState = false;
	}
	
	/** 
     * 查询状态
     */
	public boolean isRunning()
	{
		return mRunState;
	}
	
	/** 
     * 是否结束
     */
	public boolean isEnd()
	{
		mCount++;
		Log.i(TAG, "mCount: " + mCount + ",mCountMax: " + mCountMax);
		if(mCount >= mCountMax)
		{
			return true;
		}
		return false;
	}
	
	/** 
     * 监听电话状态
     */ 
	@Override
	public void onCallStateChanged(int state, String incomingNumber) 
	{
		if(!mRunState)
		{
			Log.i(TAG, "PhoneCallListener onCallStateChanged close " + state);
			return;
		}
		
		Log.i(TAG, "phone state changed :"+state);
		Log.i(TAG, "incommingNumber :"+incomingNumber);
		
		switch (state) 
		{
		//	空闲状态，没有任何活动
		case TelephonyManager.CALL_STATE_IDLE: 
			
			TestplanVoiceHelper.writeCallAttempt(false);
			
			if (AUTO_MOS) 
			{
				StopRecordMos();
				StopReplayMos();
			}
			break;
		
		//	来电状态
		//	电话铃声响起的那段时间或正在通话又来新电，新来电话不得不等待的那段时间
		case TelephonyManager.CALL_STATE_RINGING: 
			
			TestplanVoiceHelper.writeCallAlerting(false);
			
			if (AUTO_ANSWER_CALL || SetAutoAnswer()) 
			{
				//	自动应答
				//	AnswerCall();
				
				//	统计次数
				if(isEnd())
				{
					//	更新界面
					Log.i(TAG, "============= updateUIOnFinnished");
					((Callbacks) mContext).updateUIOnFinnished();
					Log.i(TAG, "============= updateUIOnFinnished end");
				}
			}
			break;
		
		//	摘机（正在通话中）
		//	摘机状态，至少有个电话活动；
		//	该活动或是拨打或是通话，或是on hold 并且没有电话是ringing or waiting
		case TelephonyManager.CALL_STATE_OFFHOOK: 
			
			TestplanVoiceHelper.writeCallConnected(false);
			
			if (AUTO_MOS) 
			{
				StartRecordMos();
				StartReplayMos();
			}
			break;
		}
	}
	
	/** 
     * 接听来电
     */ 
	public boolean AnswerCall() 
	{
		try 
		{
			Intent intent = new Intent("android.intent.action.MEDIA_BUTTON");
			KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HEADSETHOOK);
			intent.putExtra("android.intent.extra.KEY_EVENT", keyEvent);
			mContext.sendOrderedBroadcast(intent, "android.permission.CALL_PRIVILEGED");

			intent = new Intent("android.intent.action.MEDIA_BUTTON");
			keyEvent = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK);
			intent.putExtra("android.intent.extra.KEY_EVENT", keyEvent);
			mContext.sendOrderedBroadcast(intent, "android.permission.CALL_PRIVILEGED");

			Intent localIntent1 = new Intent(Intent.ACTION_HEADSET_PLUG);
			localIntent1.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			localIntent1.putExtra("state", 1);
			localIntent1.putExtra("microphone", 1);
			localIntent1.putExtra("name", "Headset");
			mContext.sendOrderedBroadcast(localIntent1, "android.permission.CALL_PRIVILEGED");

			Intent localIntent2 = new Intent(Intent.ACTION_MEDIA_BUTTON);
			KeyEvent localKeyEvent1 = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HEADSETHOOK);
			localIntent2.putExtra("android.intent.extra.KEY_EVENT", localKeyEvent1);
			mContext.sendOrderedBroadcast(localIntent2, "android.permission.CALL_PRIVILEGED");

			Intent localIntent3 = new Intent(Intent.ACTION_MEDIA_BUTTON);
			KeyEvent localKeyEvent2 = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK);
			localIntent3.putExtra("android.intent.extra.KEY_EVENT", localKeyEvent2);
			mContext.sendOrderedBroadcast(localIntent3, "android.permission.CALL_PRIVILEGED");

			Intent localIntent4 = new Intent(Intent.ACTION_HEADSET_PLUG);
			localIntent4.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			localIntent4.putExtra("state", 0);
			localIntent4.putExtra("microphone", 1);
			localIntent4.putExtra("name", "Headset");
			mContext.sendOrderedBroadcast(localIntent4, "android.permission.CALL_PRIVILEGED");
		}
		catch (Exception e2) 
		{
			e2.printStackTrace();
			Intent meidaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
			KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK);
			meidaButtonIntent.putExtra(Intent.EXTRA_KEY_EVENT, keyEvent);
			mContext.sendOrderedBroadcast(meidaButtonIntent, null);
		}
		return true;
	}
	
	public boolean SetAutoAnswer()
	{
		return false;
	}
	
	private void StartRecordMos()
	{
		// TODO Auto-generated method stub
	}
	
	private void StartReplayMos()
	{
		// TODO Auto-generated method stub
		
	}
	
	private void StopReplayMos()
	{
		// TODO Auto-generated method stub
		
	}
	
	private void StopRecordMos() 
	{
		// TODO Auto-generated method stub
		
	}
}
