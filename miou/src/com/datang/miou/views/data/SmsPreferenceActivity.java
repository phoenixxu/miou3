package com.datang.miou.views.data;

import com.datang.miou.R;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SmsPreferenceActivity extends FragmentActivity {
	Fragment[] mFragments = {new SmsSendFragment(), new SmsRecvFragment(), new SmsSrFragment()};
	private class MailPagerAdapter extends FragmentStatePagerAdapter {
		public MailPagerAdapter(FragmentManager fm) {
			super(fm);
		}
		
		@Override
		public Fragment getItem(int position) {
				return mFragments[position];
		}
		
		@Override
		public int getCount() {
			return 3;
		}
		
		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
				case 0:
					return "短信发送";
				case 1:
					return "短信接收";
				case 2:
					return "自发自收";
				default:
					return "短信发送";
			}
		}
	}

	private ViewPager mViewPager;
	private MailPagerAdapter mPagerAdapter;
	private ActionBar mActionBar;
	private TextView mTitleTextView;
	private TextView mSaveButton;
	private ImageView mBackButton;
	
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preference_sms);

		mTitleTextView = (TextView) findViewById(R.id.app_title_value);
		mTitleTextView.setText(R.string.pref_sms_title);
		
		mSaveButton = (TextView) findViewById(R.id.app_title_right_txt);
		mSaveButton.setText(R.string.gen_script_save_button);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO 自动生成的方法存根
				saveTestScheme();
			}
		});
        
		mBackButton = (ImageView) findViewById(R.id.app_title_left);
		mBackButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO 自动生成的方法存根
				try {
					if (NavUtils.getParentActivityName((Activity) getApplicationContext()) != null) {
						NavUtils.navigateUpFromSameTask((Activity) getApplicationContext());
					}
				} catch (Exception e) {
					finish();
				}		
			}
		});
		
		mPagerAdapter = new MailPagerAdapter(getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mPagerAdapter);
	}

	protected void saveTestScheme() {
		// TODO Auto-generated method stub
		((PreferenceBaseFragment) mFragments[mViewPager.getCurrentItem()]).saveTestScheme();
	}
}
