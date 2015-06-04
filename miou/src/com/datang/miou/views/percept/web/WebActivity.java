package com.datang.miou.views.percept.web;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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
import com.datang.miou.views.MainActivity;

import org.json.JSONArray;
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
    private SharedPreferences sharedPref;

    public static void startTest(Activity context) {
        SharedPreferences sharedPref = context.getSharedPreferences("TASK", Context.MODE_PRIVATE);
        String webs = sharedPref.getString("webs", "");
        if (webs.isEmpty()) return;
        try {
            JSONArray jsonArray = new JSONArray(webs);
            for (int index = 0; index < jsonArray.length(); index++) {
                JSONObject object = (JSONObject) jsonArray.get(index);
                String key = object.getString("key");
                String value = object.getString("value");
                Map<String, String> params = new HashMap<String, String>();
                params.put("url", value);
                params.put("method", "get");
                HttpTask.HttpDesc desc = new HttpTask.HttpDesc(key,
                        Calendar.getInstance().getTime(),
                        Calendar.getInstance().getTime(),
                        Config.DEFAULT_USER_MEASUREMENT_INTERVAL_SEC,
                        Config.DEFAULT_USER_MEASUREMENT_COUNT,
                        MeasurementTask.USER_PRIORITY,
                        params);
                HttpTask newTask = new HttpTask(desc, context.getApplicationContext());
                MeasurementScheduler scheduler = MainActivity.App.getScheduler();
                if (scheduler != null && scheduler.submitTask(newTask)) {
//                Toast.makeText(WebActivity.this, "开始测试 " + key, Toast.LENGTH_SHORT).show();
                /*
                 * Broadcast an intent with MEASUREMENT_ACTION so that the scheduler will immediately
                 * handles the user measurement
                 */
                    context.sendBroadcast(
                            new UpdateIntent(newTask.getDescriptor(), UpdateIntent.MEASUREMENT_ACTION));


                    if (scheduler.getCurrentTask() != null) {
                        Intent intent = new Intent();
                        intent.setAction(UpdateIntent.MEASUREMENT_PROGRESS_UPDATE_ACTION);
                        intent.putExtra(
                                UpdateIntent.STATUS_MSG_PAYLOAD, MeasurementTask.WAIT_STATUS);
                        context.sendBroadcast(intent);
                    }
                } else {
                    Toast.makeText(context, "测试 " + key + " 失败", Toast.LENGTH_LONG).show();
                }
            }


        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
        }

    }

    public static void stopTask() {
        if (MainActivity.App.getScheduler() != null)
            MainActivity.App.getScheduler().clean("http");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = getSharedPreferences("TASK", Context.MODE_PRIVATE);
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
        JSONArray jsonArray = new JSONArray();

        try {
            final ProgressBar baidu = (ProgressBar) this.f(R.id.pb_baidu);
            baidu.setTag("www.baidu.com");
            jsonArray.put(new JSONObject("{'key':'百度','value':'www.baidu.com'}"));
            barHashMap.put("百度", baidu);
            tHashMap.put("百度", (TextView) this.f(R.id.baidu_t));
            vHashMap.put("百度", (TextView) this.f(R.id.baidu_v));
            final ProgressBar mobile = (ProgressBar) this.f(R.id.pb_mobile);
            mobile.setTag("www.10086.cn/sh/");
            jsonArray.put(new JSONObject("{'key':'移动','value':'www.10086.cn/sh/'}"));
            barHashMap.put("移动", mobile);
            tHashMap.put("移动", (TextView) this.f(R.id.mobile_t));
            vHashMap.put("移动", (TextView) this.f(R.id.mobile_v));
            final ProgressBar youku = (ProgressBar) this.f(R.id.pb_youku);
            youku.setTag("www.youku.com");
            jsonArray.put(new JSONObject("{'key':'优酷','value':'www.youku.com'}"));
            barHashMap.put("优酷", youku);
            tHashMap.put("优酷", (TextView) this.f(R.id.youku_t));
            vHashMap.put("优酷", (TextView) this.f(R.id.youku_v));
            final ProgressBar qq = (ProgressBar) this.f(R.id.pb_tengxu);
            qq.setTag("www.qq.com");
            jsonArray.put(new JSONObject("{'key':'QQ','value':'www.qq.com'}"));
            barHashMap.put("QQ", qq);
            tHashMap.put("QQ", (TextView) this.f(R.id.tengxu_t));
            vHashMap.put("QQ", (TextView) this.f(R.id.tengxu_v));
            final ProgressBar sina = (ProgressBar) this.f(R.id.pb_sina);
            sina.setTag("sina.cn");
            jsonArray.put(new JSONObject("{'key':'新浪','value':'sina.cn'}"));
            barHashMap.put("新浪", sina);
            tHashMap.put("新浪", (TextView) this.f(R.id.sina_t));
            vHashMap.put("新浪", (TextView) this.f(R.id.sina_v));
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("webs", jsonArray.toString());
            editor.commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
                    if (completed == barHashMap.size()) {
                        onTaskFinish();
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
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        String webs = sharedPref.getString("webs", "");
        if (webs.isEmpty()) return;
        try {
            JSONArray jsonArray = new JSONArray(webs);
            for (int index = 0; index < jsonArray.length(); index++) {
                JSONObject object = (JSONObject) jsonArray.get(index);
                String key = object.getString("key");
                String value = object.getString("value");

            }


        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
        }


    }

    /**
     * Upgrades the progress bar in the UI.
     */
    private void progress(String key, int progress, String msg) {
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
            isStop.set(true);
            webCtl.setText("开始测试");
            stop();
        } else {
            isStop.set(false);
            webCtl.setText("停止测试");
            startTest(this);
        }
    }

    private void onTaskFinish() {
        isStop.set(true);
        webCtl.setText("开始测试");
    }

    private void stop() {
        for (String key : barHashMap.keySet()) {
            barHashMap.get(key).setProgress(0);
        }
        if (MainActivity.App.getScheduler() != null)
            MainActivity.App.getScheduler().clean("http");
        for (String key : tHashMap.keySet()) {
            tHashMap.get(key).setText("耗时:-s");
        }
        for (String key : vHashMap.keySet()) {
            vHashMap.get(key).setText("速率:-M/s");
        }
    }
}
