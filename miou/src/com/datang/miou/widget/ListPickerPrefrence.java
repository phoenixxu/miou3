package com.datang.miou.widget;

import java.util.ArrayList;

import com.datang.miou.R;
import com.datang.miou.datastructure.Globals;
import com.datang.miou.views.dialogs.ParamPickerDialogFragment;
import com.datang.miou.views.dialogs.ParamPickerDialogFragment.Callbacks;

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

public class ListPickerPrefrence extends RelativeLayout implements ParamPickerDialogFragment.Callbacks{

	protected static final int REQUEST_OPTION = 0;
	protected static final String DIALOG_PARAM_PICKER = "DIALOG_PARAM_PICKER";
	private static final String TAG = "ListPickerPrefrence";
	private TextView mTitle;
	private String mKey;
	private SharedPreferences sharedPreferences;
	private TextView mOptionTextView;
	private int mSelected;
	private ArrayList<String> mOptionsList;
	private Context mContext;
	private RelativeLayout mLayout;
	private int mId;
	@SuppressLint("Recycle")
	public ListPickerPrefrence(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mContext = context;
		
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.preference_list_picker, this, true);
		
		mOptionsList = new ArrayList<String>();
		mOptionsList.add("RxLevSub");
		mOptionsList.add("G-TxPower");
		mOptionsList.add("Total RSCP");
		
		mLayout = (RelativeLayout) view.findViewById(R.id.action_layout);
		mTitle = (TextView) view.findViewById(R.id.title);	
		mOptionTextView = (TextView) view.findViewById(R.id.option);
		
		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ListPickerPrefrence);
		for (int i = 0; i < array.getIndexCount(); i++) {
			int id = array.getIndex(i);
			switch (id) {
				case R.styleable.ListPickerPrefrence_title:
					mTitle.setText(array.getString(id));
					break;
				case R.styleable.ListPickerPrefrence_key:
					mKey = array.getString(id);
					break;
				case R.styleable.ListPickerPrefrence_id:
					mId = array.getInt(id, 0);
					break;
			}
		}
		
		OnClickListener listener = new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO 自动生成的方法存根
				FragmentManager fm = ((FragmentActivity)mContext).getSupportFragmentManager();
				ParamPickerDialogFragment dialog = ParamPickerDialogFragment.newInstance(mContext);
				dialog.setOptions(mOptionsList);
				dialog.setId(mId);
				dialog.setCallBack((Callbacks) view.getParent());
				//dialog.setTargetFragment(mFragment, REQUEST_OPTION);
				dialog.show(fm, DIALOG_PARAM_PICKER);
			}
			
		};
		mLayout.setOnClickListener(listener);
		//mTitle.setOnClickListener(listener);
		//mOptionTextView.setOnClickListener(listener);
		
		sharedPreferences = mContext.getSharedPreferences(Globals.PREFS, Activity.MODE_PRIVATE);
		
		refreshOptionView();
	}
	
	private void refreshOptionView() {
		// TODO 自动生成的方法存根
		mSelected = sharedPreferences.getInt(mKey, 0);
		if (mSelected < mOptionsList.size()) {
			mOptionTextView.setText(mOptionsList.get(mSelected));
		}
	}

	public void addOption(String option) {
		mOptionsList.add(option);
		refreshOptionView();
	}
	
	public ArrayList<String> getOption() {
		return this.mOptionsList;
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	public void refreshActivity(int option, int id, ParamPickerDialogFragment fragment) {
		// TODO 自动生成的方法存根
		if (mId == id) {
			mOptionTextView.setText(mOptionsList.get(option));
			sharedPreferences.edit().putInt(mKey, option).apply();
			Log.i(TAG, "set param to " + mKey);
			fragment.dismiss();
		}
	}
}
