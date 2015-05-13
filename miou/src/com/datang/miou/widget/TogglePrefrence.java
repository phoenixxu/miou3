package com.datang.miou.widget;

import com.datang.miou.R;
import com.datang.miou.datastructure.Globals;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TogglePrefrence extends RelativeLayout {

	private TextView mTitle;
	private Button mButton;
	private String mKey;
	private SharedPreferences sharedPreferences;
	private boolean mSelected;
	
	@SuppressLint("Recycle")
	public TogglePrefrence(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO �Զ���ɵĹ��캯����
		
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.preference_toggle, this, true);
		
		//mTitle = new TextView(context, attrs);
		mTitle = (TextView) view.findViewById(R.id.title);
		
		//mButton = new Button(context, attrs);
		mButton = (Button) view.findViewById(R.id.select);
		mButton.setOnClickListener(new View.OnClickListener() {
			
			@TargetApi(Build.VERSION_CODES.GINGERBREAD)
			@Override
			public void onClick(View view) {
				// TODO �Զ���ɵķ������
				if (mSelected) {
					mButton.setBackgroundResource(R.drawable.switch_off);
					mSelected = false;
					sharedPreferences.edit().putBoolean(mKey, false).apply();
				} else {
					mButton.setBackgroundResource(R.drawable.switch_on);
					mSelected = true;
					sharedPreferences.edit().putBoolean(mKey, true).apply();
				}
			}
		});
		
		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TogglePreference);
		for (int i = 0; i < array.getIndexCount(); i++) {
			int id = array.getIndex(i);
			switch (id) {
				case R.styleable.TogglePreference_title:
						mTitle.setText(array.getString(id));
						break;
				case R.styleable.TogglePreference_key:
						mKey = array.getString(id);
						break;
			}
		}
		
		sharedPreferences = context.getSharedPreferences(Globals.PREFS, Activity.MODE_PRIVATE);
		mSelected = sharedPreferences.getBoolean(mKey, false);
		
		if (mSelected) {
			mButton.setBackgroundResource(R.drawable.switch_on);
		} else {
			mButton.setBackgroundResource(R.drawable.switch_off);
		}
	}
}
