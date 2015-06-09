package com.datang.miou.views.percept.web;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.datang.miou.ActivitySupport;
import com.datang.miou.R;
import com.datang.miou.annotation.AutoView;
import com.datang.miou.widget.FlowLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dingzhongchang on 2015/3/18.
 */
@AutoView(R.layout.edit_web_activity)
public class EditWebActivity extends ActivitySupport {
    private static final String TAG = "EditWebActivity";
    private SharedPreferences sharedPref;
    private FlowLayout flowLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = getSharedPreferences("TASK", Context.MODE_PRIVATE);
        TextView mTitleTextView = (TextView) findViewById(R.id.app_title_value);
        mTitleTextView.setText("网页测试");
        TextView mRight = (TextView) findViewById(R.id.app_title_right_txt);
        mRight.setText("完成");
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
                finish();
            }
        });

        final Spinner spinner = (Spinner) f(R.id.sp_urls);
        String[] webNames = this.getResources().getStringArray(R.array.web_names);
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, webNames));
        spinner.setEnabled(false);

        final EditText editText = (EditText) f(R.id.self_define_url);
        editText.setEnabled(false);

        final CheckBox selectCheckBox = (CheckBox) f(R.id.select_checkBox);
        final CheckBox selfDefineCheckBox = (CheckBox) f(R.id.self_define_checkbox);

        selectCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                spinner.setEnabled(isChecked);
            }
        });
        selfDefineCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editText.setEnabled(isChecked);
            }
        });

        flowLayout = (FlowLayout) this.f(R.id.fl_web_task);
        this.f(R.id.bt_del_web).setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                for (int index = 0; index < flowLayout.getChildCount(); index++) {
                    CheckBox checkBox = (CheckBox) flowLayout.getChildAt(index);
                    if (checkBox.isChecked()) {
                        flowLayout.removeView(checkBox);

                        try {
                            JSONObject json = (JSONObject) checkBox.getTag();
                            SharedPreferences.Editor editor = sharedPref.edit();
                            String webs = sharedPref.getString("webs", "");
                            JSONArray jsonArray = new JSONArray(webs);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = (JSONObject) jsonArray.get(index);
                                if (jsonObject.getString("key").equals(json.getString("key"))) {
                                    jsonArray.remove(i);
                                }
                            }
                            editor.putString("webs", jsonArray.toString());
                            editor.commit();
                        } catch (JSONException e) {
                            Log.e(TAG, e.getMessage(), e);
                        }
                    }
                }
            }
        });


        String webs = sharedPref.getString("webs", "");
        if (!webs.isEmpty()) {
            try {
                JSONArray jsonArray = new JSONArray(webs);
                int length = jsonArray.length();
                for (int index = 0; index < length; index++) {
                    JSONObject object = (JSONObject) jsonArray.get(index);
                    String name = object.getString("key");
                    String url = object.getString("value");
                    addWeb(name, url);
                }
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage(), e);
            }

        }

        final String[] webURLs = this.getResources().getStringArray(R.array.web_urls);
        final Map<String, String> webUrlMap = new HashMap<String, String>();
        for (int index = 0; index < webURLs.length; index++) {
            webUrlMap.put(webNames[index], webURLs[index]);
        }

        f(R.id.bt_web_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "";
                if (selectCheckBox.isChecked()) {
                    String webName = spinner.getSelectedItem().toString();
                    url = webUrlMap.get(webName);
                }
                if (selfDefineCheckBox.isChecked()) {
                    url = editText.getText().toString();
                }

                try {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    String webs = sharedPref.getString("webs", "");
                    JSONArray jsonArray = new JSONArray(webs);
                    int length = jsonArray.length();
                    if (length >= 6) {
                        Toast.makeText(mContext, "最多添加6个网址", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String name = url.split("\\.")[1];
                    for (int index = 0; index < length; index++) {
                        JSONObject object = (JSONObject) jsonArray.get(index);
                        String key = object.getString("key");
                        String value = object.getString("value");
                        if (value.equals(url)) {
                            Toast.makeText(mContext, value + " 已经添加过了！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (key.equals(name)) {
                            object.put("value", url);
                        }
                    }
                    jsonArray.put(new JSONObject("{'key':" + name + ",'value':" + url + "}"));
                    editor.putString("webs", jsonArray.toString());
                    editor.commit();
                    addWeb(name, url);


                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
                Toast.makeText(mContext, url, Toast.LENGTH_SHORT).show();


            }
        });

    }

    private void addWeb(String name, String url) {
        CheckBox textView = new CheckBox(this);
        textView.setText(name);
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundResource(R.drawable.html);
        FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(200, 200);
        params.rightMargin = 10;
        params.leftMargin = 10;
        textView.setLayoutParams(params);
        flowLayout.addView(textView);
        try {
            textView.setTag(new JSONObject("{'key':" + name + ",'value':" + url + "}"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
