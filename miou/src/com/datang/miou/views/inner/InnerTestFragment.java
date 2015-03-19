package com.datang.miou.views.inner;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.datang.miou.FragmentSupport;
import com.datang.miou.R;
import com.datang.miou.annotation.AfterView;
import com.datang.miou.annotation.AutoView;

/**
 * 地图
 * 
 * @author suntongwei
 */
@AutoView(R.layout.innner_test)
public class InnerTestFragment extends FragmentSupport {

	protected static final int INNER_TEST_PAGE_NUM = 3;

	protected static final int INNER_TEST_PAGE_DOT = 0;
	protected static final int INNER_TEST_PAGE_TRACE = 1;
	protected static final int INNER_TEST_PAGE_LIFT = 2;
	
	private ViewPager mViewPager;
	private Button mDotButton;
	private Button mTraceButton;
	private Button mLiftButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);	
	}
	
	@AfterView
	public void init() {
		FragmentManager fm = getActivity().getSupportFragmentManager();
		mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {

			@Override
			public Fragment getItem(int position) {
				// TODO 自动生成的方法存根
				switch (position) {
					case INNER_TEST_PAGE_DOT:
						return new InnerTestDotFragment();
					case INNER_TEST_PAGE_TRACE:
						return new InnerTestTraceFragment();
					case INNER_TEST_PAGE_LIFT:
						return new InnerTestLiftFragment();
					default:
							return null;
				}
			}

			@Override
			public int getCount() {
				// TODO 自动生成的方法存根
				return INNER_TEST_PAGE_NUM;
			}
		});
		
		mDotButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				mViewPager.setCurrentItem(INNER_TEST_PAGE_DOT);
			}
		});
		
		mTraceButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				mViewPager.setCurrentItem(INNER_TEST_PAGE_TRACE);
			}
		});
		
		mLiftButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				mViewPager.setCurrentItem(INNER_TEST_PAGE_LIFT);
			}
		});
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		if (mView != null) {
			ViewGroup parent = (ViewGroup) mView.getParent();
			if (parent != null) {
				parent.removeView(mView);
			}
			return mView;
		}
		
		mView = inflater.inflate(R.layout.innner_test, container, false);
		
		mViewPager = (ViewPager) mView.findViewById(R.id.inner_test_viewPager);
		FragmentManager fm = getActivity().getSupportFragmentManager();
		mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {

			@Override
			public Fragment getItem(int position) {
				// TODO 自动生成的方法存根
				switch (position) {
					case INNER_TEST_PAGE_DOT:
						return new InnerTestDotFragment();
					case INNER_TEST_PAGE_TRACE:
						return new InnerTestTraceFragment();
					case INNER_TEST_PAGE_LIFT:
						return new InnerTestLiftFragment();
					default:
							return null;
				}
			}

			@Override
			public int getCount() {
				// TODO 自动生成的方法存根
				return INNER_TEST_PAGE_NUM;
			}
		});
		
		mDotButton = (Button) f(R.id.dot_test_button);
		mDotButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				mViewPager.setCurrentItem(INNER_TEST_PAGE_DOT);
			}
		});
		
		mTraceButton = (Button) f(R.id.trace_test_button);
		mTraceButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				mViewPager.setCurrentItem(INNER_TEST_PAGE_TRACE);
			}
		});
		
		mLiftButton = (Button) f(R.id.lift_test_button);
		mLiftButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				mViewPager.setCurrentItem(INNER_TEST_PAGE_LIFT);
			}
		});
		
		TextView scriptButton = (TextView) getActivity().findViewById(R.id.app_title_right_txt);
		//TextView saveButton = InnerActivity.mScriptButton;
		scriptButton.setText("脚本");
		scriptButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				Intent intent = new Intent(getActivity(), InnerScriptSettingActivity.class);
				startActivity(intent);
			}
		});
		return mView;
	}

	protected void getTestScript() {
		// TODO 自动生成的方法存根
		
	}
	
}
