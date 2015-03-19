package com.datang.miou.views.dialogs;

import java.io.Serializable;
import java.util.ArrayList;

import com.datang.miou.R;
import com.datang.miou.datastructure.Building;
import com.datang.miou.datastructure.TestLog;
import com.datang.miou.views.dialogs.ParamPickerDialogFragment.Callbacks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

@SuppressWarnings("serial")
public class ParamPickerDialogFragment extends DialogFragment implements Serializable{
	public static final String EXTRA_PARAM_ID = "extra_param_id";
	public static final String EXTRA_SElF = "extra_self";
	private static Context mContext;
	private ArrayList<String> mOptions;
	
	public interface Callbacks {
		public void refreshActivity(int option, int id, ParamPickerDialogFragment fragment);
	}
	private Callbacks cb;
	private int mId;

	public static ParamPickerDialogFragment newInstance(Context context) {
		mContext = context;
		ParamPickerDialogFragment fragment = new ParamPickerDialogFragment();
		return fragment;
	}

	private class ParamAdapter extends ArrayAdapter<String> {
		private Context mAppContext;
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO 自动生成的方法存根
			if (convertView == null) {
				convertView = ((Activity) mAppContext).getLayoutInflater().inflate(R.layout.item_list_param, null);
			}
			
			String string = this.getItem(position);

			TextView mParam = (TextView) convertView.findViewById(R.id.item_param);
			mParam.setText(string);

			return convertView;
		}

		public ParamAdapter(Context context, ArrayList<String> options) {
			super(context, 0, options);
			// TODO 自动生成的构造函数存根
			mAppContext = context;
		}	
	}
	
	@Override
	@NonNull
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_param_picker, null);
		ListView listView = (ListView) view.findViewById(R.id.param_picker_listView);

		ParamAdapter adapter = new ParamAdapter(mContext, mOptions);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> list, View view, int position, long id) {
				// TODO 自动生成的方法存根
				sendResult(position, Activity.RESULT_OK);
			}
		});
		
		view.setBackgroundResource(R.color.white);
		AlertDialog dialog =  new AlertDialog.Builder(getActivity()).setView(view).create();
		return dialog;
		
	}
	
	private void sendResult(int option, int resultCode) {
		// TODO 自动生成的方法存根
		if (cb != null) {
			cb.refreshActivity(option, this.mId, this);
		}
		if (getTargetFragment() == null) {
			return;
		}
		Intent intent = new Intent();
		intent.putExtra(EXTRA_PARAM_ID, option);
		intent.putExtra(EXTRA_SElF, this);
		getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
	}
	
	public void setOptions(ArrayList<String> list) {
		mOptions = list;
	}

	public void setId(int id) {
		this.mId = id;
	}
	
	public void setCallBack(Callbacks cb) {
		// TODO 自动生成的方法存根
		this.cb = cb;
	}
}
