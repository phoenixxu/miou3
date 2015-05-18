package com.datang.miou.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.datang.miou.HoldLastRecieverClient;
import com.datang.miou.PhoneCallListener;
import com.datang.miou.ProcessInterface;
import com.datang.miou.datastructure.Globals;
import com.datang.miou.datastructure.RealData;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;
import com.datang.miou.services.TestplanService;
import com.datang.miou.testplan.Testplan;
import com.datang.miou.testplan.ftp.TestplanFtpDown;
import com.datang.miou.testplan.ftp.TestplanFtpUpload;
import com.datang.miou.testplan.voice.TestplanVoiceCalling;

/**
 * Testplan Service
 * 
 * @author chenzeming
 */
public class TestplanService extends Service
{
	//	private static final String TAG = "TestplanService";
	private static final String TAG = "chenzm";
	
	public int mAllowAnswerCount = 3;
	private MyTestplanReceiver mTestplanReveicer = null;
	private static Context mContext = null;
	private Testplan mTestplan = null;
	
	/**
	 * 启动Testplan Service
	 */
	public static void setTestplanService(Context context, boolean isOn) 
	{
		//	主页上下文
		mContext = context;
		
		 // 创建Intent
        Intent intent = new Intent();
        
        // 设置Action属性
        intent.setAction("com.datang.miou.services.action.TestplanService");
        
        // 启动该Service
        mContext.startService(intent);
	}
	
	/**
	 * Service创建时调用
	 */
	public void onCreate() 
	{
		Log.i(TAG, "TestplanService onCreate");
		
		//	创建测试计划实例
		mTestplan = new Testplan(mContext);
		
		//	注册信令上报
		//	ProcessInterface.mHoldLastServer.RegisterClient(this);
		
		//	注册广播接收器
		mTestplanReveicer = new MyTestplanReceiver(this);
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.datang.miou.views.gen.action.TESTPLAN_ACTION");
		filter.addAction("com.datang.miou.views.gen.action.TESTPLAN_FINISH_ACTION");
		registerReceiver(mTestplanReveicer,filter);
	}
	
	/**
	 *  当客户端调用startService()方法启动Service时，该方法被调用
	 */
	public void onStart(Intent intent, int startId)
	{
		Log.i(TAG, "TestplanService onStart.");
	}
	
	/**
	 * 当Service不再使用时调用
	 */
	public void onDestroy()
	{
		Log.i(TAG, "TestplanService onDestroy");
		
		//	注销
		unregisterReceiver(mTestplanReveicer);
	}
	
	/**
	 * 可以返回null，通常返回一个有aidl定义的接口
	 */
	public IBinder onBind(Intent intent) 
	{
		Log.i(TAG, "TestplanService onBind");
		return null;
	}
	
	/**
	 * 定义一个广播接收器
	 */
	public class MyTestplanReceiver extends BroadcastReceiver 
	{
		Context mCxt = null;
		
		public MyTestplanReceiver(Context cxt)
		{
			mCxt = cxt;
		}
		
		@Override
		public void onReceive(Context context, Intent intent)
		{
			//	可用 intent 的 getAction() 区分接收到的不同广播
	        if(intent.getAction().equals("com.datang.miou.views.gen.action.TESTPLAN_ACTION"))
	        {
	        	if(intent.getBooleanExtra("enable", false))
	        	{
	        		mTestplan.startTesting();
	        	}
	        	else
	        	{
	        		mTestplan.stopTesting();
	        	}
	        }
	        else if(intent.getAction().equals("com.datang.miou.views.gen.action.TESTPLAN_FINISH_ACTION"))
	        {
	        	Log.i(TAG, "testplan updateUIOnFinnished.");
	    		if(mTestplan.handleTestplans())
	    		{
	    			Log.i(TAG, "testplan finish.");
	    			
	    			mTestplan.stopTesting();
	    		}
	        }
		}
	}
	
	/**
	 * 信令处理
	 */
	/**
	@Override
	public void ProcessData(Map<String, String> mapIDValue)
	{
		// TODO Auto-generated method stub
		//	信令
		if (mapIDValue.containsKey("s000") && mapIDValue.get("s020").equals("GSM"))
		{
			Log.i(TAG, "Type:" + mapIDValue.get("s000") + "," + mapIDValue.get("s020") 
						+ "," + mapIDValue.get("s030") + "," + mapIDValue.get("s040"));
		} 

	}
	*/
}


