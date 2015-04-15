package com.datang.miou.views.gen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.datang.miou.FragmentSupport;
import com.datang.miou.R;
import com.datang.miou.datastructure.Globals;
import com.datang.miou.datastructure.RealData;
import com.datang.miou.views.gen.params.GenParamsGsmFragment;
import com.datang.miou.views.gen.params.GenParamsLteFragment;
import com.datang.miou.views.gen.params.GenParamsTdFragment;

/**
 * 参数
 * 
 * @author suntongwei
 */
public class GenParamsFragment extends FragmentSupport {
	public static final int PARAM_PAGES = 7;
	private static final int MODE_PAGES = 3;
	
	private static final String TAG = "GenParamsFragment";
	
	private static final int ID_PARAM_VIEW_PAGER = 0;
	private static final int ID_MODE_VIEW_PAGER = 1;
	private ViewPager mParamsViewPager;
	private ImageView[] paramsImageViews;
	private ImageView[] modeImageViews;
	private ImageView indicator;
	private View mView;
	public static boolean tableRowStyleFlag = true;
	private ViewPager mModeViewPager;
	public TextView[] paramTextViews;
	
	public class ViewPageChangeListener implements OnPageChangeListener {
		//这个Fragment上有两个ViewPager，用mId来识别
		private int mId;
		
		public void setId(int id) {
			this.mId = id;
		}
		public int getId() {
			return this.mId;
		}
		
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
			if (mId == ID_PARAM_VIEW_PAGER) {
				paramsImageViews[position].setBackgroundResource(R.drawable.dot_blue);
				for (int i = 0; i < PARAM_PAGES; i++) {		
					if (i != position) {
						paramsImageViews[i].setBackgroundResource(R.drawable.dot_gray);
					}
				}
			} else if (mId == ID_MODE_VIEW_PAGER) {
				modeImageViews[position].setBackgroundResource(R.drawable.dot_blue);
				for (int i = 0; i < MODE_PAGES; i++) {		
					if (i != position) {
						modeImageViews[i].setBackgroundResource(R.drawable.dot_gray);
					}
				}
			}
		}
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
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
		
		//生成一组与参数关联的TextView,在生成表格时与表格内的TextView关联起来
		//数组下标即代表了一种参数类型
		//XML文件中提供参数类型和参数名称
		//paramTextViews = new TextView[MAX_PARAMS];
		
		mView = inflater.inflate(R.layout.gen_params, container, false);
		mParamsViewPager = (ViewPager) mView.findViewById(R.id.paramsViewPager);
		FragmentManager fm = getFragmentManager();
		mParamsViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
			@Override
			public int getCount() {
				return PARAM_PAGES;
			}

			@Override
			public Fragment getItem(int pos) {
				// TODO 自动生成的方法存根		
				Log.i(TAG, "down position = " + pos);	
				switch (pos) {
					case 0:
						return new GenParamsIndexFragment(Globals.TABLE_INDEX_VOICE_LTE);
					case 1:
						return new GenParamsIndexFragment(Globals.TABLE_INDEX_DATA_LTE);
					case 2:
						return new GenParamsIndexFragment(Globals.TABLE_INDEX_VOICE_TD);
					case 3:
						return new GenParamsIndexFragment(Globals.TABLE_INDEX_DATA_TD);
					case 4:
						return new GenParamsIndexFragment(Globals.TABLE_INDEX_VOICE_GSM);
					case 5:
						return new GenParamsIndexFragment(Globals.TABLE_INDEX_DATA_GSM);
					case 6:
						return new GenParamsIndexFragment(Globals.TABLE_INDEX_TRIPLE);
					default:
						return null;	
				}
				
			}
		});
		ViewPageChangeListener paramListener = new ViewPageChangeListener();
		paramListener.setId(ID_PARAM_VIEW_PAGER);
		mParamsViewPager.setOnPageChangeListener(paramListener);
		
		mModeViewPager = (ViewPager) mView.findViewById(R.id.modeViewPager);
		mModeViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
			@Override
			public int getCount() {
				return MODE_PAGES;
			}

			@Override
			public Fragment getItem(int pos) {
				// TODO 自动生成的方法存根		
				switch (pos) {
					case 0:
						return new GenParamsLteFragment();
					case 1:
						return new GenParamsTdFragment();
					case 2:
						return new GenParamsGsmFragment();
					default:
						return null;	
				}
				
			}
		});
		ViewPageChangeListener modeListener = new ViewPageChangeListener();
		modeListener.setId(ID_MODE_VIEW_PAGER);
		mModeViewPager.setOnPageChangeListener(modeListener);
		
		AddPageViewrContents();

		return mView;
	}

	/*
	public static TextView[] addSevenColumnsForTable(Context context, TableLayout table, String name) {
		TableRow row;
		if (tableRowStyleFlag) {
			row = (TableRow) LayoutInflater.from(context).inflate(R.layout.table_row_7_items_blue, null); 
			tableRowStyleFlag = false;
		} else {
			row = (TableRow) LayoutInflater.from(context).inflate(R.layout.table_row_7_items_white, null); 
			tableRowStyleFlag = true;
		}
		
		TextView nameField = (TextView) row.findViewById(R.id.name_textView);
		nameField.setText(name);
		
		table.addView(row);
		
		TextView[] views = { (TextView) row.findViewById(R.id.value1_textView), 
							 (TextView) row.findViewById(R.id.value2_textView), 
							 (TextView) row.findViewById(R.id.value3_textView), 
							 (TextView) row.findViewById(R.id.value4_textView), 
							 (TextView) row.findViewById(R.id.value5_textView), 
							 (TextView) row.findViewById(R.id.value6_textView) };
		return views;
	}
	
	public static TextView addTwoColumnsForTable(Context context, TableLayout table, String name) {
		
		TableRow row;
		if (tableRowStyleFlag) {
			row = (TableRow) LayoutInflater.from(context).inflate(R.layout.table_row_2_items_blue, null); 
			tableRowStyleFlag = false;
		} else {
			row = (TableRow) LayoutInflater.from(context).inflate(R.layout.table_row_2_items_white, null); 
			tableRowStyleFlag = true;
		}
		
		TextView nameField = (TextView) row.findViewById(R.id.name_textView);
		nameField.setText(name);
		
		table.addView(row);
		
		return (TextView) row.findViewById(R.id.value_textView);
	}
	
	public static TextView[] addFourColumnsForTable(Context context, TableLayout table, String name1, String name2) {
		TableRow row;
		if (tableRowStyleFlag) {
			row = (TableRow) LayoutInflater.from(context).inflate(R.layout.table_row_4_items_blue, null); 
			tableRowStyleFlag = false;
		} else {
			row = (TableRow) LayoutInflater.from(context).inflate(R.layout.table_row_4_items_white, null); 
			tableRowStyleFlag = true;
		}
		
		TextView name1Field = (TextView) row.findViewById(R.id.name1_textView);
		name1Field.setText(name1);
		
		TextView name2Field = (TextView) row.findViewById(R.id.name2_textView);
		name2Field.setText(name2);
		
		table.addView(row);
		
		TextView[] views = { (TextView) row.findViewById(R.id.value1_textView), (TextView) row.findViewById(R.id.value2_textView) };	
		return views;
	}
	*/
	
	private void AddPageViewrContents() {
		// TODO 自动生成的方法存根
		paramsImageViews = new ImageView[PARAM_PAGES];
		
		int base = R.id.params_indicator_1;
		for (int i = 0; i < PARAM_PAGES; i++) {
			indicator = (ImageView) mView.findViewById(base + i);
			if (i == 0) {
				indicator.setBackgroundResource(R.drawable.dot_blue);
			} else {
				indicator.setBackgroundResource(R.drawable.dot_gray);
			}
			paramsImageViews[i] = indicator;
		}
		
		modeImageViews = new ImageView[MODE_PAGES];
		base = R.id.modes_indicator_1;
		for (int i = 0; i < MODE_PAGES; i++) {
			indicator = (ImageView) mView.findViewById(base + i);
			if (i == 0) {
				indicator.setBackgroundResource(R.drawable.dot_blue);
			} else {
				indicator.setBackgroundResource(R.drawable.dot_gray);
			}
			modeImageViews[i] = indicator;
		}
	}	
	
	@Override
	public void updateUI(RealData data) {
	}

}
