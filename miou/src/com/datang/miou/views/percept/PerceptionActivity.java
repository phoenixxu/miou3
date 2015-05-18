package com.datang.miou.views.percept;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.datang.miou.datastructure.Globals;

import com.datang.miou.R;


/**
 * Created by dingzhongchang on 2015/3/7.
 */
public class PerceptionActivity extends FragmentActivity implements CompoundButton.OnCheckedChangeListener, ViewPager.OnPageChangeListener {

    //    public static PerceptionActivity Perception;
    private MainTabView mView = null;

    private int mCurrentPageIndex = MainTabView.TAB_INDEX_HOME;
    private TextView mTitleTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
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
        //	add via chenzm
        if(!Globals.isHigherUserPermission())
        	mBackButton.setVisibility(View.INVISIBLE);
        //	add via chenzm end

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
            if(mTitleTextView==null) return;
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

    //	add via chenzm
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
    	//	LOWER用户
    	if(!Globals.isHigherUserPermission())
    	{
    		if (keyCode == KeyEvent.KEYCODE_BACK)
    		{
    			//快速点击两次后退退出程序
    			exit();
    		}
    	}
		return super.onKeyDown(keyCode, event);
	}
    
    private void exit() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("退出")
			   .setMessage("是否真的退出？")
			   .setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						//	ProcessInterface.Close();
						System.exit(0);
					}
			   })
			   .setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
			   }).show();
	}
    //	add via chenzm end

}
