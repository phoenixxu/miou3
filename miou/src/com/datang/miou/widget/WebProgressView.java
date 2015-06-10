package com.datang.miou.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.datang.miou.R;

/**
 * Created by leo on 15/6/8.
 */
public class WebProgressView extends RelativeLayout {
    private TextView key;
    private ProgressBar pb;
    private TextView webT;
    private TextView webV;

    public WebProgressView(Context context) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.web_progress_item, this, true);
        pb = (ProgressBar) view.findViewById(R.id.pb_web);
        webT = (TextView) view.findViewById(R.id.web_t);
        webV = (TextView) view.findViewById(R.id.web_v);
        key = (TextView) view.findViewById(R.id.key);
        FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(220, 330);
        params.rightMargin = 10;
        params.leftMargin = 10;
        this.setLayoutParams(params);
    }

    public WebProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.web_progress_item, this, true);
        pb = (ProgressBar) view.findViewById(R.id.pb_web);
        webT = (TextView) view.findViewById(R.id.web_t);
        webV = (TextView) view.findViewById(R.id.web_v);
        FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(220, 330);
        params.rightMargin = 10;
        params.leftMargin = 10;
        this.setLayoutParams(params);
    }


    public void setName(String name) {
        key.setText(name);
    }

    public void setProgress(int progress) {
        pb.setProgress(progress);
    }

    public ProgressBar getPb() {
        return pb;
    }

    public TextView getWebT() {
        return webT;
    }

    public TextView getWebV() {
        return webV;
    }

}
