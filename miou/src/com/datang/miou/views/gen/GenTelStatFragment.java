package com.datang.miou.views.gen;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import com.datang.miou.FragmentSupport;
import com.datang.miou.R;
import com.datang.miou.annotation.AutoView;
import com.datang.miou.datastructure.Chart;
import com.datang.miou.datastructure.Chart.DataSet;
import com.datang.miou.datastructure.Chart.Point;
import com.datang.miou.datastructure.Globals;
import com.datang.miou.datastructure.RealData;
import com.datang.miou.views.gen.telstat.GenTelStatDataConnectionFragment;
import com.datang.miou.views.gen.telstat.GenTelStatDataQualityFragment;
import com.datang.miou.views.gen.telstat.GenTelStatMessageFragment;
import com.datang.miou.views.gen.telstat.GenTelStatVoiceFragment;

/**
 * 话务统计
 * 
 * @author suntongwei
 */
@AutoView(R.layout.gen_tel_stat)
public class GenTelStatFragment extends FragmentSupport implements Chart.mCallback{

	protected static final int PARAM_PAGES = 4;
	private View mView;
	private ViewPager mViewPager;
	private ImageView[] imageViews;
	private ImageView indicator;
	private Chart mChart;
	public Button addButton;
	
	private class ViewPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO 自动生成的方法存根
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO 自动生成的方法存根
			
		}

		@Override
		public void onPageSelected(int position) {
			// TODO 自动生成的方法存根
			imageViews[position].setBackgroundResource(R.drawable.dot_blue);
			for (int i = 0; i < PARAM_PAGES; i++) {		
				if (i != position) {
					imageViews[i].setBackgroundResource(R.drawable.dot_gray);
				}
			}
		}
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		/*
		 * 这段代码为了解决切换到别的页面再返回这个PageViewer时显示不正常的问题
		 */
		if (mView != null) {
			ViewGroup parent = (ViewGroup) mView.getParent();
			if (parent != null) {
				parent.removeView(mView);
			}
			return mView;
		}
		
		mView = inflater.inflate(R.layout.gen_tel_stat, container, false);
		mViewPager = (ViewPager) mView.findViewById(R.id.viewPager);
		FragmentManager fm = getFragmentManager();
		mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
			@Override
			public int getCount() {
				return PARAM_PAGES;
			}

			@Override
			public Fragment getItem(int pos) {
				// TODO 自动生成的方法存根			
				switch (pos) {
					case 0:
						return new GenTelStatVoiceFragment();
					case 1:
						return new GenTelStatMessageFragment();
					case 2:
						return new GenTelStatDataConnectionFragment();
					case 3:
						return new GenTelStatDataQualityFragment();
					default:
						return null;	
				}
				
			}
		});
		mViewPager.setOnPageChangeListener(new ViewPageChangeListener());		
		AddPageViewrContents();

		//设置画线颜色
		SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Globals.PREFS, Activity.MODE_PRIVATE);
		Globals.setChartLineColor(0, sharedPreferences.getInt("PREF_LINE_COLOR_1", R.color.black));
		Globals.setChartLineColor(1, sharedPreferences.getInt("PREF_LINE_COLOR_2", R.color.black));
		Globals.setChartLineColor(2, sharedPreferences.getInt("PREF_LINE_COLOR_3", R.color.black));
		
		mChart = (Chart) mView.findViewById(R.id.chart);
		mChart.setHorizontalAxeNum(5);
		mChart.setVerticalAxeNum(20);
		mChart.setCb((Chart.mCallback) this);
		mChart.addItemInPopWindow("速率", Globals.CHART_LINE_PARAM_SPEED);
		mChart.addItemInPopWindow("SNR", Globals.CHART_LINE_PARAM_SNR);
		mChart.addItemInPopWindow("BLER", Globals.CHART_LINE_PARAM_BLER);
		
		//测试按钮
		addButton = (Button) mView.findViewById(R.id.add);
		addButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				DataSet data = mChart.new DataSet();
				data.setMaxParamNum(7);
				data.mParams[0] = (int) (Math.random() * 1000);
				data.mParams[1] = (int) (Math.random() * 1000);
				data.mParams[2] = (int) (Math.random() * 1000);
				data.mParams[Globals.CHART_LINE_PARAM_SNR] = (int) (Math.random() * 100);
				data.mParams[Globals.CHART_LINE_PARAM_SPEED] = (int) (Math.random() * 1000);
				data.mParams[Globals.CHART_LINE_PARAM_BLER] = (int) (Math.random() * 10);
				mChart.addPoint(data);
			}
		});
		return mView;
	}

	private void AddPageViewrContents() {
		// TODO 自动生成的方法存根
		imageViews = new ImageView[PARAM_PAGES];
		
		int base = R.id.indicator_1;
		for (int i = 0; i < PARAM_PAGES; i++) {
			indicator = (ImageView) mView.findViewById(base + i);
			if (i == 0) {
				indicator.setBackgroundResource(R.drawable.dot_blue);
			} else {
				indicator.setBackgroundResource(R.drawable.dot_gray);
			}
			imageViews[i] = indicator;
		}
	}
	
	@Override
	public void onClickOnPoint(Point p) {
		// TODO 自动生成的方法存根
		//Toast.makeText(getApplication(), String.valueOf(p.data), Toast.LENGTH_SHORT);
		addButton.setText(String.valueOf(p.data));
	}

	@Override
	protected void updateUI(RealData data) {
		// TODO Auto-generated method stub
		super.updateUI(data);
		DataSet dataSet = mChart.new DataSet();
		
		if (data.getParams().containsKey(Globals.PARAM_RXLEV_SUB)) {
			dataSet.mParams[Globals.CHART_LINE_PARAM_RXLEVSUB] = data.getParams().get(Globals.PARAM_RXLEV_SUB);
		}
		
		if (data.getParams().containsKey(Globals.PARAM_RSRP)) {
			dataSet.mParams[Globals.CHART_LINE_PARAM_RSCP] = data.getParams().get(Globals.PARAM_RSRP);
		}
		
		if (data.getParams().containsKey(Globals.PARAM_GTX_POWER)) {
			dataSet.mParams[Globals.CHART_LINE_PARAM_GTXPOWER] = data.getParams().get(Globals.PARAM_GTX_POWER);
		}
		
		if (data.getParams().containsKey(Globals.PARAM_BLER)) {
			dataSet.mParams[Globals.CHART_LINE_PARAM_BLER] = data.getParams().get(Globals.PARAM_BLER);
		}
		
		if (data.getParams().containsKey(Globals.PARAM_SINR)) {
			dataSet.mParams[Globals.CHART_LINE_PARAM_SNR] = data.getParams().get(Globals.PARAM_SINR);
		}
		
		if (data.getParams().containsKey(Globals.PARAM_SPEED)) {
			dataSet.mParams[Globals.CHART_LINE_PARAM_SPEED] = data.getParams().get(Globals.PARAM_SPEED);
		}
		/*
		 double number;
		number = data.params[Globals.PARAM_RXLEV_SUB];
		if (number != RealData.INVALID_VALUE) {
			dataSet.mParams[Globals.CHART_LINE_PARAM_RXLEVSUB] = number;
		}
		
		number = data.params[Globals.PARAM_RSRP];
		if (number != RealData.INVALID_VALUE) {
			dataSet.mParams[Globals.CHART_LINE_PARAM_RSCP] = number;
		}
		
		number = data.params[Globals.PARAM_GTX_POWER];
		if (number != RealData.INVALID_VALUE) {
			dataSet.mParams[Globals.CHART_LINE_PARAM_GTXPOWER] = number;
		}
		
		number = data.params[Globals.PARAM_BLER];
		if (number != RealData.INVALID_VALUE) {
			dataSet.mParams[Globals.CHART_LINE_PARAM_BLER] = number;
		}
		
		number = data.params[Globals.PARAM_SINR];
		if (number != RealData.INVALID_VALUE) {
			dataSet.mParams[Globals.CHART_LINE_PARAM_SNR] = number;
		}
		
		number = data.params[Globals.PARAM_RXLEV_SUB];
		if (number != RealData.INVALID_VALUE) {
			dataSet.mParams[Globals.CHART_LINE_PARAM_RXLEVSUB] = number;
		}
		
		number = data.params[Globals.PARAM_SPEED];
		if (number != RealData.INVALID_VALUE) {
			dataSet.mParams[Globals.CHART_LINE_PARAM_SPEED] = number;
		}
		*/
		
		mChart.addPoint(dataSet);
	}  
	
	
}
