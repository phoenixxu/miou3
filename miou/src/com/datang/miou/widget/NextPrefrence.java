package com.datang.miou.widget;

import com.datang.miou.R;
import com.datang.miou.preferences.LinePreferenceActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NextPrefrence extends RelativeLayout {

	private static final String TAG = "NextPrefrence";
	private TextView mTitle;
	protected String mKey;
	private Class<?> mActivity;
	private Context mContext;
	private ImageView mImage;
	private RelativeLayout layout;
	private ImageView mIcon;
	
	@SuppressLint("Recycle")
	public NextPrefrence(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO 自动生成的构造函数存根
		this.mContext = context;
		
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.preference_next, this, true);
		
		OnClickListener listener = new OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO 自动生成的方法存根
				Log.i(TAG, "activity = " + mActivity);
				Intent intent = new Intent(mContext, mActivity);
				mContext.startActivity(intent);
				
			}
			
		};
		
		mTitle = (TextView) view.findViewById(R.id.title);
		mTitle.setOnClickListener(listener);
		
		mIcon = (ImageView) view.findViewById(R.id.icon);
		
		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.NextPreference);
		for (int i = 0; i < array.getIndexCount(); i++) {
			int id = array.getIndex(i);
			switch (id) {
				case R.styleable.NextPreference_title:
					mTitle.setText(array.getString(id));
					break;
				case R.styleable.NextPreference_icon:
					mIcon.setBackgroundResource(array.getResourceId(id, 0));
			}
		}
		
		mImage = (ImageView) view.findViewById(R.id.pointer);
		mImage.setOnClickListener(listener);
		
		layout = (RelativeLayout) view.findViewById(R.id.action_layout);
		layout.setOnClickListener(listener);
	}

	public void setActivity(Class<?> activity) {
		this.mActivity = activity;
	}
}
