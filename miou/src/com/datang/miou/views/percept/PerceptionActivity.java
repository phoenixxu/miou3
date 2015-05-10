package com.datang.miou.views.percept;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.datang.business.MeasurementScheduler;
import com.datang.business.UpdateIntent;
import com.datang.business.util.Logger;
import com.datang.miou.R;


/**
 * Created by dingzhongchang on 2015/3/7.
 */
public class PerceptionActivity extends FragmentActivity implements CompoundButton.OnCheckedChangeListener, ViewPager.OnPageChangeListener {

    public static PerceptionActivity Perception;
    private MainTabView mView = null;

    private int mCurrentPageIndex = MainTabView.TAB_INDEX_HOME;
    private TextView mTitleTextView;

    private MeasurementScheduler scheduler;
    private boolean isBound = false;
    private boolean isBindingToService = false;
    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private ServiceConnection serviceConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            Logger.d("onServiceConnected called");
            // We've bound to LocalService, cast the IBinder and get LocalService
            // instance
            MeasurementScheduler.SchedulerBinder binder = (MeasurementScheduler.SchedulerBinder) service;
            scheduler = binder.getService();
            isBound = true;
            isBindingToService = false;
            PerceptionActivity.this.sendBroadcast(new UpdateIntent("",
                    UpdateIntent.SCHEDULER_CONNECTED_ACTION));
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Logger.d("onServiceDisconnected called");
            isBound = false;
        }
    };

    /**
     * Returns the scheduler singleton instance. Should only be called from the UI thread.
     */
    public MeasurementScheduler getScheduler() {
        if (isBound) {
            return this.scheduler;
        } else {
            bindToService();
            return null;
        }
    }


    private void bindToService() {
        if (!isBindingToService && !isBound) {
            // Bind to the scheduler service if it is not bounded
            Intent intent = new Intent(this, MeasurementScheduler.class);
            bindService(intent, serviceConn, Context.BIND_AUTO_CREATE);
            isBindingToService = true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Perception = this;
        mView = new MainTabView(this);
        if (savedInstanceState == null) {
            mCurrentPageIndex = MainTabView.TAB_INDEX_HOME;
        } else {
            //TODO
        }
        mView.changeCheckedTabOnPageSelectedChanged(mCurrentPageIndex);
        mTitleTextView = (TextView) findViewById(R.id.app_title_value);
        mTitleTextView.setText("用户感知");
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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            mTitleTextView.setText(buttonView.getText());
            mView.changeSelectedPageOnTabCheckedChanged(buttonView);
        }
    }

    @Override
    public void onPageSelected(int index) {
        mView.changeCheckedTabOnPageSelectedChanged(index);
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBound) unbindService(serviceConn);
    }
}
