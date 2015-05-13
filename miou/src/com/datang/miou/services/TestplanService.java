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

/**
 * Testplan Service
 * 
 * @author chenzeming
 */
public class TestplanService extends Service implements HoldLastRecieverClient
{
	//	private static final String TAG = "TestplanService";
	private static final String TAG = "chenzm";
	
	public int mAllowAnswerCount = 3;
	private MyTestplanReceiver mTestplanReveicer = null;
	static Context mContext = null;
	private static boolean mRunState = false;
	
	private Class<?> ClassManagerClass;
	private Method getInstanceMethod;
	private Object mCallManager;
	
	private Method getActiveFgMethod;
	private Object res;
	
	private Method getCallMethod;
	
	//	电话状态改变后回调的监听
	private Handler mCallHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			//switch(msg.what)
			{
				Log.i(TAG, "======= mCallHandler handleMessage =======");
				//break;
			}
		}
	};
	
	/**
	 * 启动Testplan Service
	 */
	public static void setTestplanService(Context context, boolean isOn) 
	{
		mContext = context;
		
		 // 创建Intent
        Intent intent = new Intent();
        
        // 设置Action属性
        intent.setAction("com.datang.miou.services.action.TestplanService");
        
        // 启动该Service
        context.startService(intent);
	}
	
	public Context getTestplanServiceContext()
	{
		return this;
	}
	
	/**
	 * Service创建时调用
	 */
	public void onCreate() 
	{
		Log.i(TAG, "TestplanService onCreate..............");
		
		//	注册广播接收器
		mTestplanReveicer = new MyTestplanReceiver(this);
		
		//	注册只接收指定 action 的广播接收器
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.datang.miou.views.gen.action.VOICE_SLAVE_ACTION");
		filter.addAction("com.datang.miou.views.gen.action.VOICE_MASTER_ACTION");
		filter.addAction("android.intent.action.PRECISE_CALL_STATE");
		filter.addAction("ACTION_NEW_OUTGOING_CALL");
		registerReceiver(mTestplanReveicer,filter);
		
		//	注册信令上报
		ProcessInterface.mHoldLastServer.RegisterClient(this);
	}
	
	/**
	 *  当客户端调用startService()方法启动Service时，该方法被调用
	 */
	public void onStart(Intent intent, int startId)
	{
		Log.i(TAG, "TestplanService onStart..............");
	}
	
	/**
	 * 当Service不再使用时调用
	 */
	public void onDestroy()
	{
		Log.i(TAG, "TestplanService onDestroy..............");
		
		//	注销
		unregisterReceiver(mTestplanReveicer);
	}
	
	/**
	 * 可以返回null，通常返回一个有aidl定义的接口
	 */
	public IBinder onBind(Intent intent) 
	{
		Log.i(TAG, "TestplanService onBind..............");
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
			Log.i(TAG, "MyTestplanReceiver");
		}
		
		@Override
		public void onReceive(Context context, Intent intent)
		{
			//	可用 intent 的 getAction() 区分接收到的不同广播
	        if(intent.getAction().equals("com.datang.miou.views.gen.action.VOICE_SLAVE_ACTION"))
	        {
	        	if(intent.getBooleanExtra("enable",false))
	        	{
	        		Log.i(TAG, "VOICE_SLAVE_ACTION on");
	        		
	        		//	设置状态
	        		mRunState = true;
	        		
	        		//m_AutoAnswer.register(context);
	        		
	        		//	启动
	        		//m_AutoAnswer.start(intent.getIntExtra("repeatnum", 1));
	        	}
	        	else
	        	{
	        		Log.i(TAG, "VOICE_SLAVE_ACTION off");
	        		
	        		//	停止
	        		//m_AutoAnswer.stop();
	        		
	        		//m_AutoAnswer.unregister();
	        		
	        		//	设置状态
	        		mRunState = false;
	        	}
	        }
	        else if(intent.getAction().equals("com.datang.miou.views.gen.action.VOICE_MASTER_ACTION"))
	        {
	        	Log.i(TAG, "===== VOICE_MASTER_ACTION");
	        	if(intent.getBooleanExtra("enable",false))
	        	{
	        		Log.i(TAG, "VOICE_MASTER_ACTION on");
	        		ProcessInterface.StartCall(intent.getStringExtra("num"),false,false,mContext);
	        	}
	        	else
	        	{
	        		Log.i(TAG, "VOICE_MASTER_ACTION off");
	        		ProcessInterface.StopCall(mContext);
	        	}
	        }
	        else if(intent.getAction().equals("android.intent.action.PRECISE_CALL_STATE"))
	        {
	        	Log.i(TAG, "=====================");
	        }
	        else if(intent.getAction().equals("ACTION_NEW_OUTGOING_CALL"))
	        {
	        	Log.i(TAG, "===== ACTION_NEW_OUTGOING_CALL");
	        }
		}
	}
	
	/**
	 * 发送VoiceSlave类型广播
	 */
	public static void sendVoiceSlaveBroadcast(boolean isOn,int repeatNum)
	{
		// 实例化Intent对象
        Intent intent = new Intent();
        
        // 设置Intent action属性
        intent.setAction("com.datang.miou.views.gen.action.VOICE_SLAVE_ACTION");
        
        // 为Intent添加附加信息
        intent.putExtra("enable", isOn);
        
        // 为Intent添加附加信息
        intent.putExtra("repeatnum", repeatNum);
        
        // 发出广播
        mContext.sendBroadcast(intent);
	}
	
	/**
	 * 发送VoiceMaster类型广播
	 */
	public static void sendVoiceMasterBroadcast(boolean isOn,String num)
	{
		// 实例化Intent对象
        Intent intent = new Intent();
        
        // 设置Intent action属性
        intent.setAction("com.datang.miou.views.gen.action.VOICE_MASTER_ACTION");
        
        // 为Intent添加附加信息
        intent.putExtra("enable", isOn);
        intent.putExtra("num", num);
        
        // 发出广播
        mContext.sendBroadcast(intent);
	}
	
	/**
	 * 获取VoiceSlave状态
	 */
	public static boolean getVoiceSlaveState()
	{
		return mRunState;
	}
	
	/**
	 * 信令处理
	 */
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

}


