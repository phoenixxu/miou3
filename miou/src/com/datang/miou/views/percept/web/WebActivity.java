package com.datang.miou.views.percept.web;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NavUtils;
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
import com.datang.miou.MiouApp;
import com.datang.miou.R;
import com.datang.miou.annotation.AfterView;
import com.datang.miou.annotation.AutoView;
import com.datang.miou.views.percept.PerceptionActivity;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 网页测试 Created by dingzhongchang on 2015/3/16.
 */
@AutoView(R.layout.web_activity)
public class WebActivity extends ActivitySupport {

    AtomicBoolean isStop = new AtomicBoolean(false);
    private Button webCtl;  BroadcastReceiver receiver;

    private String[] array=new String[]{"www.baidu.com","www.sina.com",};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        final TextView testStatus = (TextView) findViewById(R.id.tv_web_test_status);

        IntentFilter filter = new IntentFilter();
        filter.addAction(UpdateIntent.SCHEDULER_CONNECTED_ACTION);
        filter.addAction(UpdateIntent.MEASUREMENT_PROGRESS_UPDATE_ACTION);


        final TextView textResult = (TextView) this.findViewById(R.id.tv_web_test_result);
        final ProgressBar baidu = (ProgressBar) this.f(R.id.pb_baidu);
        final ProgressBar sina = (ProgressBar) this.f(R.id.pb_sina);
        final ProgressBar mobile = (ProgressBar) this.f(R.id.pb_mobile);
        final ProgressBar tengxu = (ProgressBar) this.f(R.id.pb_tengxu);
        final ProgressBar youku = (ProgressBar) this.f(R.id.pb_youku);
        this.receiver = new BroadcastReceiver() {
            @Override
            // All onXyz() callbacks are single threaded
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(UpdateIntent.MEASUREMENT_PROGRESS_UPDATE_ACTION)) {
                    int progress = intent.getIntExtra(UpdateIntent.PROGRESS_PAYLOAD,
                            Config.INVALID_PROGRESS);
                    int priority = intent.getIntExtra(UpdateIntent.TASK_PRIORITY_PAYLOAD,
                            MeasurementTask.INVALID_PRIORITY);
                    // Show user results if we there is currently a user measurement running
                    if (priority == MeasurementTask.USER_PRIORITY) {
                        textResult.setText(MiouApp.APP.getScheduler().getUserResults().toString());
                    }
                    upgradeProgress(baidu,progress, Config.MAX_PROGRESS_BAR_VALUE);

                    if (MiouApp.APP.getScheduler().isPauseRequested()) {
                        testStatus.setText(WebActivity.this.getString(R.string.pauseMessage));
                    } else if (!MiouApp.APP.getScheduler().hasBatteryToScheduleExperiment()) {
                        testStatus.setText(WebActivity.this.getString(R.string.powerThreasholdReachedMsg));
                    } else {
                        MeasurementTask currentTask = MiouApp.APP.getScheduler().getCurrentTask();
                        if (currentTask != null) {
                            if (currentTask.getDescription().priority == MeasurementTask.USER_PRIORITY) {
                                testStatus.setText("User task " + currentTask.getDescriptor() + " is running");
                            } else {
                                testStatus.setText("System task " + currentTask.getDescriptor() + " is running");
                            }
                        } else {
                            testStatus.setText(WebActivity.this.getString(R.string.resumeMessage));
                        }
                    }
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
                Map<String, String> params = new HashMap<String, String>();
                params.put("url", "www.baidu.com");
                params.put("method", "get");
                HttpTask.HttpDesc desc = new HttpTask.HttpDesc(null,
                        Calendar.getInstance().getTime(),
                        null,
                        Config.DEFAULT_USER_MEASUREMENT_INTERVAL_SEC,
                        Config.DEFAULT_USER_MEASUREMENT_COUNT,
                        MeasurementTask.USER_PRIORITY,
                        params);
                HttpTask newTask = new HttpTask(desc, WebActivity.this.getApplicationContext());
                MeasurementScheduler scheduler = MiouApp.APP.getScheduler();
                if (scheduler != null && scheduler.submitTask(newTask)) {
                    /*
                     * Broadcast an intent with MEASUREMENT_ACTION so that the scheduler will immediately
                     * handles the user measurement
                     */
                    WebActivity.this.sendBroadcast(
                            new UpdateIntent("", UpdateIntent.MEASUREMENT_ACTION));
                    Toast.makeText(WebActivity.this, "MeasurementSuccess", Toast.LENGTH_LONG).show();

                    if (scheduler.getCurrentTask() != null) {
                        showBusySchedulerStatus();
                    }
                } else {
                    Toast.makeText(WebActivity.this, "MeasurementFailure", Toast.LENGTH_LONG).show();
                }
                scheduler.submitTask(newTask);
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
     *  Upgrades the progress bar in the UI.
     *  */
    private void upgradeProgress(ProgressBar progressBar,int progress, int max) {
        Logger.d("Progress is " + progress);
        if (progress >= 0 && progress <= max) {
            progressBar.setProgress(progress);
        } else {
      /* UserMeasurementTask broadcast a progress greater than max to indicate the
       * termination of the measurement
       */
            progressBar.setProgress(100);
        }
    }
    @Override
    protected void onResume() {
        isStop.set(false);
        webCtl.setText("停止测试");
        super.onResume();

    }

    @Override
    protected void onPause() {
        isStop.set(true);
        webCtl.setText("开始测试");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(this.receiver);
    }

    private void ctl() {
        if (!isStop.get()) {
            isStop.set(true);
            webCtl.setText("开始测试");
        } else {
            isStop.set(false);
            webCtl.setText("停止测试");
        }
    }
}
