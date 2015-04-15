package com.datang.miou.widget;

import java.util.ArrayList;

import com.datang.miou.R;
import com.datang.miou.datastructure.EventType;
import com.datang.miou.datastructure.Globals;
import com.datang.miou.views.dialogs.ColorPickerDialogFragment;
import com.datang.miou.views.dialogs.ColorPickerDialogFragment.Callbacks;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ColorPickerPrefrence extends RelativeLayout implements ColorPickerDialogFragment.Callbacks{

	protected static final int REQUEST_OPTION = 0;
	protected static final String DIALOG_COLOR_PICKER = "DIALOG_COLOR_PICKER";
	private static final String TAg = null;
	
	private TextView mTitle;
	private String mKey;
	private SharedPreferences sharedPreferences;
	private TextView mOptionTextView;
	private int mSelected;
	private ArrayList<String> mColorString;
	private ArrayList<Integer> mColorResources;
	private Context mContext;
	private RelativeLayout mLayout;
	private String mId;
	private Object mItem;
	private String mType;
	
	@SuppressLint("Recycle")
	public ColorPickerPrefrence(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mContext = context;
		
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.preference_color_picker, this, true);
		
		mColorString = new ArrayList<String>();
		mColorResources = new ArrayList<Integer>();
		mColorResources.add(R.color.red);
		mColorString.add("红色");
		mColorResources.add(R.color.pink);
		mColorString.add("粉红");
		mColorResources.add(R.color.orange);
		mColorString.add("橙色");
		mColorResources.add(R.color.yellow);
		mColorString.add("黄色");
		mColorResources.add(R.color.green);
		mColorString.add("绿色");
		mColorResources.add(R.color.blue);
		mColorString.add("蓝色");
		mColorResources.add(R.color.purple);
		mColorString.add("紫色");
		mColorResources.add(R.color.black);
		mColorString.add("黑色");
		
		mLayout = (RelativeLayout) view.findViewById(R.id.action_layout);
		mTitle = (TextView) view.findViewById(R.id.title);	
		mOptionTextView = (TextView) view.findViewById(R.id.option);
		
		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ColorPickerPrefrence);
		for (int i = 0; i < array.getIndexCount(); i++) {
			int id = array.getIndex(i);
			switch (id) {
				case R.styleable.ColorPickerPrefrence_title:
					mTitle.setText(array.getString(id));
					break;
				case R.styleable.ColorPickerPrefrence_key:
					mKey = array.getString(id);
					break;
				case R.styleable.ColorPickerPrefrence_id:
					mId = array.getString(id);
					break;
				case R.styleable.ColorPickerPrefrence_type:
					mType = array.getString(id);
				case R.styleable.ColorPickerPrefrence_colors:
					break;
			}
		}
		
		OnClickListener listener = new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO 自动生成的方法存根
				FragmentManager fm = ((FragmentActivity)mContext).getSupportFragmentManager();
				ColorPickerDialogFragment dialog = ColorPickerDialogFragment.newInstance(mContext);
				dialog.setOptions(mColorResources);
				dialog.setId(mId);
				dialog.setCallBack((Callbacks) view.getParent());
				dialog.show(fm, DIALOG_COLOR_PICKER);
			}
			
		};
		mLayout.setOnClickListener(listener);
		
		sharedPreferences = mContext.getSharedPreferences(Globals.PREFS, Activity.MODE_PRIVATE);
		
		refreshOptionView();
	}
	
	public void refreshOptionView() {
		// TODO 自动生成的方法存根
		if (mType != null) {
			if (mType.equals("event_type")) {
				if (this.mItem != null) {
					mSelected = ((EventType) this.mItem).getColor();
					Log.i(TAg, "get color " + mSelected);
				}
			} 
		}else {
			mSelected = sharedPreferences.getInt(mKey, 0);
		}
		mOptionTextView.setBackgroundResource(mSelected);
	}

	public void addOption(String option) {
		mColorString.add(option);
		refreshOptionView();
	}
	
	public ArrayList<String> getOption() {
		return this.mColorString;
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	public void refreshActivity(int option, String id, ColorPickerDialogFragment fragment) {
		// TODO 自动生成的方法存根
		if (mId.equals(id)) {
			if (mType != null) {
				if (mType.equals("event_type")) {
					if (this.mItem != null) {
						((EventType) this.mItem).setColor( mColorResources.get(option));
						Log.i(TAg, "set color" + mColorResources.get(option));
					}
				} 
			}else {
				sharedPreferences.edit().putInt(mKey, mColorResources.get(option)).apply();
			}
			mOptionTextView.setBackgroundResource(mColorResources.get(option));
			fragment.dismiss();
		}
	}

	public void setKey(String key) {
		this.mKey = key;
	}
	
	public void setTitle(String title) {
		this.mTitle.setText(title);
	}
	
	public void setId(String id) {
		this.mId = id;
	}
	
	public void setItem(Object item) {
		this.mItem = item;
	}
}
