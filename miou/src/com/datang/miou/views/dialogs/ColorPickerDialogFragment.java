package com.datang.miou.views.dialogs;

import java.io.Serializable;
import java.util.ArrayList;

import com.datang.miou.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

@SuppressWarnings("serial")
public class ColorPickerDialogFragment extends DialogFragment implements Serializable{
	public static final String EXTRA_PARAM_ID = "extra_param_id";
	public static final String EXTRA_SElF = "extra_self";
	private static Context mContext;
	private ArrayList<Integer> mOptions;
	
	public interface Callbacks {
		public void refreshActivity(int option, String id, ColorPickerDialogFragment fragment);
	}
	private Callbacks cb;
	private String mId;

	public static ColorPickerDialogFragment newInstance(Context context) {
		mContext = context;
		ColorPickerDialogFragment fragment = new ColorPickerDialogFragment();
		return fragment;
	}

	private class ParamAdapter extends ArrayAdapter<Integer> {
		private Context mAppContext;
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO 自动生成的方法存根
			if (convertView == null) {
				convertView = ((Activity) mAppContext).getLayoutInflater().inflate(R.layout.item_list_param, null);
			}
			

			TextView mParam = (TextView) convertView.findViewById(R.id.item_param);
			mParam.setBackgroundResource(mOptions.get(position));

			return convertView;
		}

		public ParamAdapter(Context context, ArrayList<Integer> mOptions) {
			super(context, 0, mOptions);
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
		/*
		if (getTargetFragment() == null) {
			return;
		}
		Intent intent = new Intent();
		intent.putExtra(EXTRA_PARAM_ID, id);
		intent.putExtra(EXTRA_SElF, this);
		getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
		*/
		cb.refreshActivity(option, this.mId, this);
	}
	
	public void setOptions(ArrayList<Integer> list) {
		mOptions = list;
	}
	
	public void setId(String id) {
		this.mId = id;
	}
	
	public void setCallBack(Callbacks cb) {
		// TODO 自动生成的方法存根
		this.cb = cb;
	}
}
