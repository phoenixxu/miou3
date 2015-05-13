package com.datang.miou.views.inner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.datang.miou.R;
import com.datang.miou.views.gen.ScriptSettingFragment;

public class InnerScriptSettingActivity extends FragmentActivity {

	protected static final int INNER_TEST_SCRIPT_DOT = 0;
	protected static final int INNER_TEST_SCRIPT_TRACE = 1;
	protected static final int INNER_TEST_SCRIPT_PAGE_NUM = 2;
	private ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.innner_script_setting);
		
		mViewPager = (ViewPager) findViewById(R.id.script_viewPager);
		FragmentManager fm = getSupportFragmentManager();
		mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {

			@Override
			public Fragment getItem(int position) {
				// TODO 自动生成的方法存根
				switch (position) {
					case INNER_TEST_SCRIPT_DOT:
						return new ScriptSettingFragment(ScriptSettingFragment.TEST_SCRIPT_TYPE_DOT);
					case INNER_TEST_SCRIPT_TRACE:
						return new ScriptSettingFragment(ScriptSettingFragment.TEST_SCRIPT_TYPE_TRACE);
					default:
							return null;
				}
			}

			@Override
			public int getCount() {
				// TODO 自动生成的方法存根
				return INNER_TEST_SCRIPT_PAGE_NUM;
			}
		});
		
		Button mDotButton = (Button) findViewById(R.id.dot_script_button);
		mDotButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				mViewPager.setCurrentItem(INNER_TEST_SCRIPT_DOT);
			}
		});
		
		Button mTraceButton = (Button) findViewById(R.id.trace_script_button);
		mTraceButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				mViewPager.setCurrentItem(INNER_TEST_SCRIPT_TRACE);
			}
		});
	}
}
