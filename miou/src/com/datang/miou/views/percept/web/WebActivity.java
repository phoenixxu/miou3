package com.datang.miou.views.percept.web;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.datang.business.Config;
import com.datang.business.MeasurementScheduler;
import com.datang.business.MeasurementTask;
import com.datang.business.UpdateIntent;
import com.datang.business.measurements.HttpTask;
import com.datang.business.util.Logger;
import com.datang.miou.ActivitySupport;
import com.datang.miou.R;
import com.datang.miou.annotation.AfterView;
import com.datang.miou.annotation.AutoView;
import com.datang.miou.views.percept.PerceptionActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 网页测试 Created by dingzhongchang on 2015/3/16.
 */
@AutoView(R.layout.web_activity)
public class WebActivity extends ActivitySupport {

    public static final String TAG = "WebActivity";
    AtomicBoolean isStop = new AtomicBoolean(true);
    BroadcastReceiver receiver;
    private Button webCtl;
    private HashMap<String, ProgressBar> barHashMap = new LinkedHashMap<String, ProgressBar>();
    private HashMap<String, TextView> tHashMap = new LinkedHashMap<String, TextView>();
    private HashMap<String, TextView> vHashMap = new LinkedHashMap<String, TextView>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PerceptionActivity.Perception.getScheduler();
        TextView mTitleTextView = (TextView) findViewById(R.id.app_title_value);
        mTitleTextView.setText("网页测试");
        TextView mRight = (TextView) findViewById(R.id.app_title_right_txt);
        mRight.setText("编辑");
        ImageView mBackButton = (ImageView) findViewById(R.id.app_title_left);
        mBackButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                try {
                    if (NavUtils.getParentActivityName((Activity) getApplicationContext()) != null) {
                        NavUtils.navigateUpFromSameTask((Activity) getApplicationContext());
                    }
                } catch (Exception e) {
                    finish();
                }
            }
        });
        mRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, EditWebActivity.class));
            }
        });
        IntentFilter filter = new IntentFilter();
        filter.addAction(UpdateIntent.SCHEDULER_CONNECTED_ACTION);
        filter.addAction(UpdateIntent.MEASUREMENT_PROGRESS_UPDATE_ACTION);
        filter.addAction(UpdateIntent.SYSTEM_STATUS_UPDATE_ACTION);


        final TextView textResult = (TextView) this.findViewById(R.id.tv_web_test_result);
        final ProgressBar baidu = (ProgressBar) this.f(R.id.pb_baidu);
        baidu.setTag("www.baidu.com");
        barHashMap.put("baidu", baidu);
        tHashMap.put("baidu", (TextView) this.f(R.id.baidu_t));
        vHashMap.put("baidu", (TextView) this.f(R.id.baidu_v));
        final ProgressBar mobile = (ProgressBar) this.f(R.id.pb_mobile);
        mobile.setTag("www.10086.cn/sh/");
        barHashMap.put("mobile", mobile);
        tHashMap.put("mobile", (TextView) this.f(R.id.mobile_t));
        vHashMap.put("mobile", (TextView) this.f(R.id.mobile_v));
        final ProgressBar youku = (ProgressBar) this.f(R.id.pb_youku);
        youku.setTag("www.youku.com");
        barHashMap.put("youku", youku);
        tHashMap.put("youku", (TextView) this.f(R.id.youku_t));
        vHashMap.put("youku", (TextView) this.f(R.id.youku_v));
        final ProgressBar qq = (ProgressBar) this.f(R.id.pb_tengxu);
        qq.setTag("www.qq.com");
        barHashMap.put("qq", qq);
        tHashMap.put("qq", (TextView) this.f(R.id.tengxu_t));
        vHashMap.put("qq", (TextView) this.f(R.id.tengxu_v));
        final ProgressBar sina = (ProgressBar) this.f(R.id.pb_sina);
        sina.setTag("sina.cn");
        barHashMap.put("sina", sina);
        tHashMap.put("sina", (TextView) this.f(R.id.sina_t));
        vHashMap.put("sina", (TextView) this.f(R.id.sina_v));


        this.receiver = new BroadcastReceiver() {
            @Override
            // All onXyz() callbacks are single threaded
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(UpdateIntent.MEASUREMENT_PROGRESS_UPDATE_ACTION)) {
                    int progress = intent.getIntExtra(UpdateIntent.PROGRESS_PAYLOAD,
                            Config.INVALID_PROGRESS);
                    String key = intent.getStringExtra(UpdateIntent.TASK_KEY);
                    upgradeProgress(key, progress, intent.getStringExtra(UpdateIntent.STRING_PAYLOAD));
                    textResult.setText(intent.getStringExtra(UpdateIntent.STATUS_MSG_PAYLOAD));

                } else if (intent.getAction().equals(UpdateIntent.SYSTEM_STATUS_UPDATE_ACTION)) {
                    textResult.setText(intent.getStringExtra(UpdateIntent.STATS_MSG_PAYLOAD));
                }
            }
        };
        this.registerReceiver(this.receiver, filter);

    }

    @AfterView
    private void init() {

        webCtl = (Button) this.f(R.id.bt_web_ctl);
        webCtl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ctl();
            }
        });
    }

    private void showBusySchedulerStatus() {
        Intent intent = new Intent();
        intent.setAction(UpdateIntent.MEASUREMENT_PROGRESS_UPDATE_ACTION);
        intent.putExtra(
                UpdateIntent.STATUS_MSG_PAYLOAD, "The scheduler is busy, your measurement will start shortly");
        sendBroadcast(intent);
    }

    /**
     * Upgrades the progress bar in the UI.
     */
    private void upgradeProgress(String key, int progress, String msg) {
        Logger.d("Progress is " + key + ":" + progress + "--" + msg);
        if (!barHashMap.containsKey(key)) return;
        if (progress >= 0 && progress <= Config.MAX_PROGRESS_BAR_VALUE) {
            barHashMap.get(key).setProgress(progress);
        } else {
      /* UserMeasurementTask broadcast a progress greater than max to indicate the
       * termination of the measurement
       */
            if (msg != null) {
                try {
                    JSONObject json = new JSONObject(msg);
                    tHashMap.get(key).setText("耗时:" + json.getString("T"));
                    vHashMap.get(key).setText("速率:" + json.getString("V"));
                } catch (JSONException e) {
                    Log.w(TAG, e.getMessage(), e);
                }
            }
            barHashMap.get(key).setProgress(100);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(this.receiver);
    }

    private void ctl() {
        if (!isStop.get()) {
            for (String key : barHashMap.keySet()) {
                barHashMap.get(key).setProgress(0);
            }
            isStop.set(true);
            webCtl.setText("开始测试");
            PerceptionActivity.Perception.getScheduler().clean();
            for(String key : tHashMap.keySet()){
                tHashMap.get(key).setText("耗时:-s");
            }
            for(String key:vHashMap.keySet()){
                vHashMap.get(key).setText("速率:-M/s");
            }


        } else {
            isStop.set(false);
            webCtl.setText("停止测试");
            startTest();

        }
    }

    private void startTest() {
        for (String key : barHashMap.keySet()) {
//            if(!key.equals("sina")) continue;
            Map<String, String> params = new HashMap<String, String>();
            params.put("url", barHashMap.get(key).getTag().toString());
            params.put("method", "get");
            HttpTask.HttpDesc desc = new HttpTask.HttpDesc(key,
                    Calendar.getInstance().getTime(),
                    Calendar.getInstance().getTime(),
                    Config.DEFAULT_USER_MEASUREMENT_INTERVAL_SEC,
                    Config.DEFAULT_USER_MEASUREMENT_COUNT,
                    MeasurementTask.USER_PRIORITY,
                    params);
            HttpTask newTask = new HttpTask(desc, WebActivity.this.getApplicationContext());
            MeasurementScheduler scheduler = PerceptionActivity.Perception.getScheduler();
            if (scheduler != null && scheduler.submitTask(newTask)) {
//                Toast.makeText(WebActivity.this, "开始测试 " + key, Toast.LENGTH_SHORT).show();
                /*
                 * Broadcast an intent with MEASUREMENT_ACTION so that the scheduler will immediately
                 * handles the user measurement
                 */
                WebActivity.this.sendBroadcast(
                        new UpdateIntent(newTask.getDescriptor(), UpdateIntent.MEASUREMENT_ACTION));


                if (scheduler.getCurrentTask() != null) {
                    showBusySchedulerStatus();
                }
            } else {
                Toast.makeText(WebActivity.this, "测试 " + key + " 失败", Toast.LENGTH_LONG).show();
            }
        }

    }
}
