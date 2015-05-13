package com.datang.miou.widget;

import com.datang.miou.R;
import com.datang.miou.datastructure.Globals;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SinglePrefrence extends RelativeLayout {

	private TextView mTitle;
	private Button[] mButton;
	private int baseId;
	private static final int MAX_OPTIONS = 4;
	private SharedPreferences sharedPreferences;
	protected String mKey;
	private int mSelected;
	
	@SuppressLint("Recycle")
	public SinglePrefrence(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO 自动生成的构造函数存根
		
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.preference_single, this, true);
		
		mButton = new Button[MAX_OPTIONS];
		
		mTitle = (TextView) view.findViewById(R.id.title);
		
		baseId = R.id.option1;
		
		OnClickListener listener = new OnClickListener() {
			@TargetApi(Build.VERSION_CODES.GINGERBREAD)
			@Override
			public void onClick(View view) {
				// TODO 自动生成的方法存根
				int index = view.getId() - baseId;
				if (index != mSelected) {
					Resources r = getResources();
					for (int i = 0; i < MAX_OPTIONS; i++) {
						mButton[i].setBackgroundColor(r.getColor(R.color.menu_gray));
					}
					view.setBackgroundColor(r.getColor(R.color.button_blue));
					mSelected = index;	
					sharedPreferences.edit().putInt(mKey, index).apply();
				} 
			}
		};
		
		for (int i = 0; i < MAX_OPTIONS; i++) {
			mButton[i] = (Button) view.findViewById(baseId + i);
			mButton[i].setOnClickListener(listener);
			mButton[i].setBackgroundColor(getResources().getColor(R.color.menu_gray));
		}

		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SinglePreference);
		for (int i = 0; i < array.getIndexCount(); i++) {
			int id = array.getIndex(i);
			switch (id) {
				case R.styleable.SinglePreference_title:
					mTitle.setText(array.getString(id));
					break;
				case R.styleable.SinglePreference_key:
					mKey = array.getString(id);
					break;
				case R.styleable.SinglePreference_option1:
					mButton[0].setText(array.getString(id));
					break;
				case R.styleable.SinglePreference_option2:
					mButton[1].setText(array.getString(id));
					break;
				case R.styleable.SinglePreference_option3:
					mButton[2].setText(array.getString(id));
					break;
				case R.styleable.SinglePreference_option4:
					mButton[3].setText(array.getString(id));
					break;
			}
		}
		
		sharedPreferences = context.getSharedPreferences(Globals.PREFS, Activity.MODE_PRIVATE);
		mSelected = sharedPreferences.getInt(mKey, 0);
		if (mSelected < 0) {
			mSelected = 0;
		}
		
		mButton[mSelected].setBackgroundColor(getResources().getColor(R.color.button_blue));
	}

}
