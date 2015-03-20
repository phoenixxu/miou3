package com.datang.miou.views.percept.video;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.datang.miou.ActivitySupport;
import com.datang.miou.R;
import com.datang.miou.annotation.AfterView;
import com.datang.miou.annotation.AutoView;

/**
 * 视频测试
 * Created by dingzhongchang on 2015/3/16.
 */
@AutoView(R.layout.video_activity)
public class VideoActivity extends ActivitySupport {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView mTitleTextView = (TextView) findViewById(R.id.app_title_value);
        mTitleTextView.setText("视频测试");
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
    }

    @AfterView
    private void init() {

        VideoView mVv = (VideoView) findViewById(R.id.vv_video);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        MediaController mc = new MediaController(this);
        mc.setMediaPlayer(mVv);
        mVv.setMediaController(mc);
        mVv.setVideoURI(Uri.parse("http://static.youku.com/v1.0.0512/v/swf/loader.swf"));
        mVv.requestFocus();
        mVv.start();

    }
}
