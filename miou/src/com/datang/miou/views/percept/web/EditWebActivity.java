package com.datang.miou.views.percept.web;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dingzhongchang on 2015/3/18.
 */
@AutoView(R.layout.edit_web_activity)
public class EditWebActivity extends ActivitySupport {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        String[] webURLs = this.getResources().getStringArray(R.array.web_urls);
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
                Toast.makeText(mContext, url, Toast.LENGTH_SHORT).show();


            }
        });

    }
}
