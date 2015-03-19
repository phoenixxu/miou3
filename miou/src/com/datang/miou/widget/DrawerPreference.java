package com.datang.miou.widget;

import com.datang.miou.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DrawerPreference extends LinearLayout{

	private static final String TAG = "DrawerPreference";
	private RelativeLayout mLayout;
	private TextView mTitle;
	private ImageView mPointerImageView;
	private String mKeyBase;
	private LinearLayout mColorSetterLayout;
	private boolean isColorSetterLayoutShown = false;
	private int mColorSelecterNum = 5;
	private ColorPickerPrefrence mColorPicker[];
	
	@SuppressLint("Recycle")
	public DrawerPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO 自动生成的构造函数存根
		
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.preference_drawer, this, true);
		
		mLayout = (RelativeLayout) view.findViewById(R.id.dropdown_layout);
		mTitle = (TextView) view.findViewById(R.id.title);	
		mPointerImageView = (ImageView) view.findViewById(R.id.pointer);
		mPointerImageView.setBackgroundResource(R.drawable.arraw_right);
		
		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.DrawerPrefrence);
		for (int i = 0; i < array.getIndexCount(); i++) {
			int id = array.getIndex(i);
			switch (id) {
				case R.styleable.DrawerPrefrence_title:
					mTitle.setText(array.getString(id));
					break;
				case R.styleable.DrawerPrefrence_key:
					mKeyBase = array.getString(id);
					break;
			}
		}
		
		mColorSetterLayout = (LinearLayout) view.findViewById(R.id.color_setter_layout);
		mLayout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO 自动生成的方法存根
				if (!isColorSetterLayoutShown) {
					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
					mColorSetterLayout.setLayoutParams(lp);
					
					mPointerImageView.setBackgroundResource(R.drawable.arraw_down);
					isColorSetterLayoutShown = true;
				} else {
					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
					mColorSetterLayout.setLayoutParams(lp);
					isColorSetterLayoutShown = false;
					mPointerImageView.setBackgroundResource(R.drawable.arraw_right);
				}
			}
		});
		
		mColorPicker = new ColorPickerPrefrence[mColorSelecterNum];
		int baseId = R.id.option1;
		for (int i = 0; i < mColorSelecterNum ; i++) {
			mColorPicker[i] = (ColorPickerPrefrence) view.findViewById(baseId + i);
			
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			mColorPicker[i].setLayoutParams(lp);
			
			Log.i(TAG, "i = " + i);
			mColorPicker[i].setVisibility(View.VISIBLE);
			mColorPicker[i].setKey(mKeyBase + "_LEVEL_" + i);
			mColorPicker[i].refreshOptionView();
			mColorPicker[i].setTitle("LEVEL " + i);
		}
	}
}
