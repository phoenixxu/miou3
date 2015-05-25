package com.datang.miou.views.percept.video;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.datang.miou.ActivitySupport;
import com.datang.miou.R;
import com.datang.miou.annotation.AfterView;
import com.datang.miou.annotation.AutoView;

/**
 * 视频测试 Created by dingzhongchang on 2015/3/16.
 */
@AutoView(R.layout.video_activity)
public class VideoActivity extends ActivitySupport {


    private WebView webview;
    private Button videoCtl;

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

        // VideoView mVv = (VideoView) findViewById(R.id.vv_video);
        // getWindow().setFormat(PixelFormat.TRANSLUCENT);
        // MediaController mc = new MediaController(this);
        // mc.setMediaPlayer(mVv);
        // mVv.setMediaController(mc);
        // mVv.setVideoURI(Uri.parse("http://static.youku.com/v1.0.0512/v/swf/loader.swf"));
        // mVv.requestFocus();
        // mVv.start();

        webview = (WebView) this.findViewById(R.id.vv_webview);
        // get the WebView's instance
        // 设置WebView属性,能够执行Javascript脚本
        webview.getSettings().setJavaScriptEnabled(true);
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        // settings.setPluginsEnabled(true);
        settings.setJavaScriptEnabled(true);
        settings.setPluginState(PluginState.ON);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setAllowFileAccess(true);
        // settings.setPluginsEnabled(true);
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setAllowFileAccess(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setAppCachePath("/data/data/" + getPackageName() + "/app_path/");
        settings.setAppCacheEnabled(true);
//        settings.setUserAgentString("0");
        settings.setLoadsImagesAutomatically(true);
//        settings.setSavePassword(true);
        settings.setLightTouchEnabled(true);
//      settings.setPluginsEnabled(true);
        settings.setPluginState(PluginState.ON);
//        settings.setUserAgentString("Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_4; zh-tw) AppleWebKit/533.16 (KHTML, like Gecko) Version/5.0 Safari/533.16");

        webview.setBackgroundColor(0);
        webview.setVisibility(View.VISIBLE);
        // 加载指定url的网页
        // mywebview.loadUrl("http://player.youku.com/player.php/Type/Folder/Fid/18443222/Ob/1/sid/XNDY1ODQ0MjU2/v.swf");
        webview.loadUrl("http://v.youku.com/v_show/id_XOTE4ODg4ODY0.html?from=y1.3-idx-uhome-1519-20887.205905.4-1.1-8-1-4-0");
        webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) { // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url);
                return true;
            }

        });

        videoCtl = (Button) this.f(R.id.bt_video_ctl);
        videoCtl.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View v) {
                if (videoCtl.getText().equals("停止测试")) {
                    videoCtl.setText("开始测试");
                    webview.onPause();
                } else {
                    videoCtl.setText("停止测试");
                    webview.onResume();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        webview.onResume();
        videoCtl.setText("停止测试");
        super.onResume();
    }

    @Override
    protected void onPause() {
        webview.onPause();
        videoCtl.setText("开始测试");
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
            // goBack()返回WebView的上一层页面
            webview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
