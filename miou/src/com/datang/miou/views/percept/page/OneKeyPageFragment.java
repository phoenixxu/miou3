package com.datang.miou.views.percept.page;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.datang.miou.R;
import com.datang.miou.views.MainActivity;
import com.datang.miou.views.percept.BasePageFragment;
import com.datang.miou.views.percept.connect.ConnectActivity;
import com.datang.miou.views.percept.test.TestActivity;
import com.datang.miou.views.percept.video.VideoActivity;
import com.datang.miou.views.percept.voice.VoiceActivity;
import com.datang.miou.views.percept.web.WebActivity;


public class OneKeyPageFragment extends BasePageFragment {

    @Override
    protected View initUI(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_page_onekey, container, false);
        init(root);
        return root;
    }

    private void init(View root) {
        root.findViewById(R.id.tv_html_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (check()) return;
                mContext.startActivity(new Intent(mContext, WebActivity.class));
            }
        });
        root.findViewById(R.id.tv_video_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (check()) return;
                mContext.startActivity(new Intent(mContext, VideoActivity.class));
            }
        });
        root.findViewById(R.id.tv_voice_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (check()) return;
                mContext.startActivity(new Intent(mContext, VoiceActivity.class));
            }
        });
        root.findViewById(R.id.tv_test_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (check()) return;
                mContext.startActivity(new Intent(mContext, TestActivity.class));
            }
        });
        root.findViewById(R.id.tv_connect_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (check()) return;
                mContext.startActivity(new Intent(mContext, ConnectActivity.class));
            }
        });
    }

    private boolean check() {
        if (MainActivity.App.getScheduler().getCurrentTask() != null) {
            Toast.makeText(mContext, "正在进行任务测试，暂时不能进行一键测试", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
}