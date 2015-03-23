package com.datang.miou.views.percept.connect;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.datang.miou.ActivitySupport;
import com.datang.miou.R;
import com.datang.miou.annotation.AfterView;
import com.datang.miou.annotation.AutoView;

/**
 * 连接测试
 * Created by dingzhongchang on 2015/3/16.
 */
@AutoView(R.layout.connect_activity)
public class ConnectActivity extends ActivitySupport {


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
    }

    @AfterView
    private void init() {

    }
}
