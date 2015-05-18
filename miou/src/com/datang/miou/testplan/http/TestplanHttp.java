package com.datang.miou.testplan.http;

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
import com.datang.business.MeasurementResult;
import com.datang.miou.datastructure.TestScheme;
import com.datang.miou.views.gen.GenActivity;

public class TestplanHttp extends TestplanHttpHelper {


	private HttpTask mHttpTask;
	
	private Thread mHttpThread;

	private int mCount;

	private int mInterval;

	protected boolean isTest = false;

	private Context mContext;

	public TestplanHttp(Context context, TestScheme testplan) {
		
		mContext = context;
		mCount = Integer.parseInt(testplan.getCommandList().getCommand().getRepeat());
		mInterval = Integer.parseInt(testplan.getCommandList().getCommand().getInterval());
		
		// 默认get方法
		String url = testplan.getCommandList().getCommand().getUrl();
		String timeout = testplan.getCommandList().getCommand().getTimeOut();
		
        Map<String, String> params = new HashMap<String, String>();
		params.put("url", url);
		params.put("timeout", timeout);
		MeasurementDesc desc = new HttpTask.HttpDesc(null, null, null, mInterval, mCount, 100, params);
		mHttpTask = new HttpTask(desc, context);
		/*
		mHttpThread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					mHttpTask.call();
				} catch (MeasurementError e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
		*/
	}
	
	public void startHttpTest() throws MeasurementError {
		isTest = true;
		
		mHttpThread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				MeasurementResult result;
				try {
					for (int i = 0; i < mCount; i++) {
						if (isTest) {
							writeHttpAttempt();
							
							result = mHttpTask.call();
							
							Log.i(TAG, "result = " + result);
							
							if (result == null) {
								writeHttpFail();
							} else if (result.equals("Measurement has failed")) {
								writeHttpFail();
							} else {
								writeHttpSuccess();
							}
							
							if (i != mCount -1) {
								Thread.sleep(mInterval * 1000);
							}
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
		mHttpThread.start();
	}
	
	@SuppressWarnings("deprecation")
	public void stopHttpTest() {
		isTest = false;
		//mHttpThread.stop();
		mHttpThread.interrupt();
		mHttpThread = null;
		mHttpTask.stop();
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
