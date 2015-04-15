package com.datang.miou.services;

import java.util.HashMap;
import java.util.Map;

import com.datang.miou.ProcessInterface;
import com.datang.miou.datastructure.Globals;
import com.datang.miou.datastructure.RealData;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class ResultService extends IntentService{

	private static final String TAG = "ResultService";
	private static final long POLL_INTERVAL = 1000 * 1;
	public static final String ACTION_SHOW_NOTIFICATION = "show_notification";
	public static final String EXTRA_REAL_DATA = "extra_real_data";

	public ResultService() {
		super(TAG);
		// TODO 自动生成的构造函数存根
	}

	//在该方法中生成数据并广播给需要该数据更新UI的Fragment
	//数据来源有两个：测试时的实时数据和回放时的日志数据
	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO 自动生成的方法存根
		Intent i = new Intent(ACTION_SHOW_NOTIFICATION);
		RealData data = new RealData();
		
		for (int j = 0; j < 6; j++) {
			data.getParams().put(Globals.PARAM_NEIGHBOR_LTE_EARFCN + j, Math.random() * 10);
		}
		
		i.putExtra(EXTRA_REAL_DATA, data);
		sendBroadcast(i);
	}
	
	public static void setServiceAlarm(Context context, boolean isOn) {
		Intent intent = new Intent(context, ResultService.class);
		PendingIntent pi = PendingIntent.getService(context, 0, intent, 0);
		AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		if (isOn) {
			alarm.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), POLL_INTERVAL, pi);
		} else {
			alarm.cancel(pi);
			pi.cancel();
		}
	}

}
