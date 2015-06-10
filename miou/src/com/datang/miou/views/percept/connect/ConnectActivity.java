package com.datang.miou.views.percept.connect;

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

import org.json.JSONArray;
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
    private SharedPreferences sharedPref;

    public static void startTest(Activity mContext, String name) {
        SharedPreferences sharedPref = mContext.getSharedPreferences("TASK", Context.MODE_PRIVATE);
        String webs = sharedPref.getString("urls", "");
        if (webs.isEmpty()) return;
        try {
            JSONArray jsonArray = new JSONArray(webs);
            for (int index = 0; index < jsonArray.length(); index++) {
                JSONObject object = (JSONObject) jsonArray.get(index);
                String key = object.getString("key");
                String value = object.getString("value");
                Map<String, String> params = new HashMap<String, String>();
                params.put("target", value);
                PingTask.PingDesc desc = new PingTask.PingDesc(name, value,
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
                        Toast.makeText(mContext, "测试正在执行中...", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    Toast.makeText(mContext, "测试 " + key + " 失败", Toast.LENGTH_LONG).show();
                }
            }


        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
        }

    }

    public static void stopTask() {
        if (MainActivity.App.getScheduler() != null)
            MainActivity.App.getScheduler().clean(PingTask.TYPE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = getSharedPreferences("TASK", Context.MODE_PRIVATE);
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
//                    textResult.setText(intent.getStringExtra(UpdateIntent.STATS_MSG_PAYLOAD));

                } else if (intent.getAction().equals(UpdateIntent.SYSTEM_STATUS_UPDATE_ACTION)) {
                    int completed = intent.getIntExtra("completed", 0);
                    if (completed == beanMap.size()) {
                        onTaskFinish();
                    }
                    String stringExtra = intent.getStringExtra(UpdateIntent.STATS_MSG_PAYLOAD);
                    if (stringExtra == null) return;
                    textResult.setText(stringExtra);
                }
            }
        };
        IntentFilter filter = new IntentFilter();
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
        ctl = (TextView) f(R.id.bt_connect_ctl);
        ctl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ctl();
            }
        });

        ListView connectLv = (ListView) findViewById(R.id.lv_connect);
        adapter = new ConnectAdapter(mContext, beanMap.values());
        connectLv.setAdapter(adapter);
    }


    @Override
    protected void onStart() {
        super.onStart();
        beanMap.clear();
        adapter.removeAll();
        String webs = sharedPref.getString("urls", "");
        if (webs.isEmpty()) {
            JSONArray jsonArray = new JSONArray();
            String[] names = this.getResources().getStringArray(R.array.ping_names);
            String[] urls = this.getResources().getStringArray(R.array.ping_urls);
            try {
                for (int index = 0; index < names.length; index++) {
                    PingBean bean = new PingBean(urls[index]);
                    bean.name = names[index];
                    beanMap.put(bean.url, bean);
                    jsonArray.put(new JSONObject("{'key':" + bean.name + ",'value':" + bean.url + "}"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("urls", jsonArray.toString());
            editor.commit();
            adapter.addAll(beanMap.values());

        } else {
            try {
                JSONArray jsonArray = new JSONArray(webs);
                int length = jsonArray.length();
                for (int index = 0; index < length; index++) {
                    JSONObject object = (JSONObject) jsonArray.get(index);
                    String name = object.getString("key");
                    String url = object.getString("value");
                    PingBean bean = new PingBean(url);
                    bean.name = name;
                    beanMap.put(bean.url, bean);
                }
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage(), e);
            }
            adapter.addAll(beanMap.values());
        }


    }

    private void ctl() {
        if (!isStop.get()) {
            isStop.set(true);
            ctl.setText("开始测试");
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
            startTest(this, "_-1");

        }
    }

    private void onTaskFinish() {
        isStop.set(true);
        ctl.setText("开始测试");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(this.receiver);
    }
}
