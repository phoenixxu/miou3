package com.datang.miou.views.percept.connect;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.datang.business.Config;
import com.datang.business.MeasurementScheduler;
import com.datang.business.MeasurementTask;
import com.datang.business.UpdateIntent;
import com.datang.business.measurements.PingTask;
import com.datang.business.util.Logger;
import com.datang.miou.ActivitySupport;
import com.datang.miou.R;
import com.datang.miou.annotation.AfterView;
import com.datang.miou.annotation.AutoView;
import com.datang.miou.views.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 连接测试
 * Created by dingzhongchang on 2015/3/16.
 */
@AutoView(R.layout.connect_activity)
public class ConnectActivity extends ActivitySupport {
    private static final String TAG = "ConnectActivity";
    AtomicBoolean isStop = new AtomicBoolean(true);
    BroadcastReceiver receiver;
    Map<String, PingBean> beanMap = new LinkedHashMap<String, PingBean>();
    private TextView ctl;
    private ConnectAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView mTitleTextView = (TextView) findViewById(R.id.app_title_value);
        mTitleTextView.setText("连接测试");
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
                startActivity(new Intent(mContext, EditConnectActivity.class));
            }
        });

        final TextView textResult = (TextView) this.findViewById(R.id.tv_ping_test_result);
        this.receiver = new BroadcastReceiver() {
            @Override
            // All onXyz() callbacks are single threaded
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(UpdateIntent.MEASUREMENT_PROGRESS_UPDATE_ACTION)) {
                    int progress = intent.getIntExtra(UpdateIntent.PROGRESS_PAYLOAD,
                            Config.INVALID_PROGRESS);
                    String key = intent.getStringExtra(UpdateIntent.TASK_KEY);
                    progress(key, progress, intent.getStringExtra(UpdateIntent.STRING_PAYLOAD));
                    textResult.setText(intent.getStringExtra(UpdateIntent.STATUS_MSG_PAYLOAD));

                } else if (intent.getAction().equals(UpdateIntent.SYSTEM_STATUS_UPDATE_ACTION)) {
                    textResult.setText(intent.getStringExtra(UpdateIntent.STATS_MSG_PAYLOAD));
                    int completed = intent.getIntExtra("completed", 0);
                    if (completed == beanMap.size()) {
                        onTaskFinish();
                    }
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(UpdateIntent.SCHEDULER_CONNECTED_ACTION);
        filter.addAction(UpdateIntent.MEASUREMENT_PROGRESS_UPDATE_ACTION);
        filter.addAction(UpdateIntent.SYSTEM_STATUS_UPDATE_ACTION);
        this.registerReceiver(this.receiver, filter);
    }

    private void progress(String key, int progress, String extra) {
        Logger.d("Progress is " + key + ":" + progress + "--" + extra);
        if (!beanMap.containsKey(key)) return;
        PingBean bean = beanMap.get(key);
        if (progress >= 0 && progress <= Config.MAX_PROGRESS_BAR_VALUE) {
            bean.progress = progress;
        } else {
      /* UserMeasurementTask broadcast a progress greater than max to indicate the
       * termination of the measurement
       */
            if (extra != null) {
                try {
                    JSONObject json = new JSONObject(extra);
                    bean.time = json.getString("T");
                    bean.sucess = json.getString("S");
                } catch (JSONException e) {
                    Log.w(TAG, e.getMessage(), e);
                }
            }
            bean.progress = 100;
        }
        adapter.notifyDataSetChanged();
    }

    @AfterView
    private void init() {
        String[] names = this.getResources().getStringArray(R.array.ping_nams);
        String[] urls = this.getResources().getStringArray(R.array.ping_urls);
        for (int index = 0; index < names.length; index++) {
            PingBean bean = new PingBean(urls[index]);
            bean.name = names[index];
            beanMap.put(bean.url, bean);
        }
        ListView connectLv = (ListView) findViewById(R.id.lv_connect);
        adapter = new ConnectAdapter(mContext, beanMap.values());
        connectLv.setAdapter(adapter);

        ctl = (TextView) f(R.id.bt_connect_ctl);
        ctl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ctl();
            }
        });

    }

    private void ctl() {
        if (!isStop.get()) {
            isStop.set(true);
            ctl.setText("开始测试");
            if (MainActivity.App.getScheduler() != null)
                MainActivity.App.getScheduler().clean("ping");
            if (!beanMap.isEmpty()) {
                for (String key : beanMap.keySet()) {
                    PingBean bean = beanMap.get(key);
                    bean.sucess = "-";
                    bean.time = "-";
                    bean.progress = 0;
                }
                adapter.notifyDataSetChanged();
            }
        } else {
            isStop.set(false);
            ctl.setText("停止测试");
            startTest();

        }
    }

    private void onTaskFinish() {
        isStop.set(true);
        ctl.setText("开始测试");
    }

    private void startTest() {
        for (PingBean bean : beanMap.values()) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("target", bean.url);
            PingTask.PingDesc desc = new PingTask.PingDesc(bean.url,
                    Calendar.getInstance().getTime(),
                    Calendar.getInstance().getTime(),
                    Config.DEFAULT_USER_MEASUREMENT_INTERVAL_SEC,
                    Config.DEFAULT_USER_MEASUREMENT_COUNT,
                    MeasurementTask.USER_PRIORITY,
                    params);
            PingTask newTask = new PingTask(desc, mContext.getApplicationContext());
            MeasurementScheduler scheduler = MainActivity.App.getScheduler();
            if (scheduler != null && scheduler.submitTask(newTask)) {
//                Toast.makeText(WebActivity.this, "开始测试 " + key, Toast.LENGTH_SHORT).show();
                /*
                 * Broadcast an intent with MEASUREMENT_ACTION so that the scheduler will immediately
                 * handles the user measurement
                 */
                mContext.sendBroadcast(
                        new UpdateIntent(newTask.getDescriptor(), UpdateIntent.MEASUREMENT_ACTION));


                if (scheduler.getCurrentTask() != null) {
                    showBusySchedulerStatus();
                }
            } else {
                Toast.makeText(mContext, "测试 " + bean.name + " 失败", Toast.LENGTH_LONG).show();
            }
        }

    }

    private void showBusySchedulerStatus() {
        Intent intent = new Intent();
        intent.setAction(UpdateIntent.MEASUREMENT_PROGRESS_UPDATE_ACTION);
        intent.putExtra(
                UpdateIntent.STATUS_MSG_PAYLOAD, "The scheduler is busy, your measurement will start shortly");
        sendBroadcast(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(this.receiver);
    }
}
