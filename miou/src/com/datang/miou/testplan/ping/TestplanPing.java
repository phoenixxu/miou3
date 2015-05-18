package com.datang.miou.testplan.ping;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.datang.business.MeasurementDesc;
import com.datang.business.MeasurementError;
import com.datang.miou.datastructure.TestScheme;
import com.datang.miou.testplan.MeasurementResult;
import com.datang.miou.views.gen.GenActivity;

public class TestplanPing extends TestplanPingHelper {

	private Thread mPingThread;

	private int mCount;

	private int mInterval;

	protected boolean isTest = false;

	private Context mContext;

	private String mPacketSize;

	private String mIp;

	private String mTimeout;

	private PingTask mPingTask;

	public TestplanPing(Context context, TestScheme testplan) {
		
		mContext = context;
		mCount = Integer.parseInt(testplan.getCommandList().getCommand().getRepeat());
		mInterval = Integer.parseInt(testplan.getCommandList().getCommand().getInterval());
		mPacketSize = testplan.getCommandList().getCommand().getPackageSize();
		mIp = testplan.getCommandList().getCommand().getIp();
		mTimeout = testplan.getCommandList().getCommand().getTimeOut();
		
        Map<String, String> params = new HashMap<String, String>();
		params.put("packet_size_byte", mPacketSize);
		params.put("ping_timeout_sec", mTimeout);
		params.put("target", mIp);
		MeasurementDesc desc = new PingTask.PingDesc(null, null, null, mInterval, mCount, 100, params);
		mPingTask = new PingTask(desc, context);
	}
	
	public void startPingTest() throws MeasurementError {
		isTest = true;
		
		mPingThread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				MeasurementResult result;
				try {
					Log.i(TAG, "count = " + mCount + ", interval = " + mInterval);
					for (int i = 0; i < mCount; i++) {
						if (isTest) {
							writePingAttempt();
							
							result = mPingTask.call();
											
							Log.i(TAG, "result = " + result);
							if (result == null) {
								writePingFail();
							} else if (result.equals("Measurement has failed")) {
								writePingFail();
							} else {
								Log.i(TAG, result.toString());
								writePingSuccess();
							}
							
							if (i != mCount -1) {
								Thread.sleep(mInterval * 1000);
							}
							
							Log.i(TAG, "finish one test");
						}
					}
					sendFinishBroadcast();
					
				} catch (MeasurementError e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
		mPingThread.start();
	}
	
	@SuppressWarnings("deprecation")
	public void stopPingTest() {
		isTest = false;
		//mHttpThread.stop();
		mPingThread.interrupt();
		mPingThread = null;
		mPingTask.stop();
	}
	
	/**
	 * 发送类型广播
	 */
	private void sendFinishBroadcast()
	{
		// 实例化Intent对象
        Intent intent = new Intent();
        
        // 设置Intent action属性
        intent.setAction("com.datang.miou.views.gen.action.TESTPLAN_FINISH_ACTION");

        // 发出广播
        mContext.sendBroadcast(intent);
	}
}
