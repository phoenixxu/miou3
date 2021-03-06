package com.datang.miou.views.dialogs;

import java.io.File;
import java.util.ArrayList;

import com.datang.miou.R;
import com.datang.miou.datastructure.TestLog;
import com.datang.miou.utils.MiscUtils;
import com.datang.miou.utils.SDCardUtils;
import com.datang.miou.xml.XmlRW;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class LogPickerDialogFragment extends DialogFragment {
	private static ArrayList<TestLog> mLogs;
	private static Context mAppContext;
	
	public interface Callbacks {
		public void refreshActivity(TestLog log);
	}
	private static Callbacks cb;
	private TestLog mSelectedTestLog;
	protected View mCurrentItem;
	
	private class TestLogAdapter extends ArrayAdapter<TestLog> {
		private Context mAppContext;
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO 自动生成的方法存根
			if (convertView == null) {
				convertView = ((Activity) mAppContext).getLayoutInflater().inflate(R.layout.item_list_test_log, null);
			}
			
			TestLog log = this.getItem(position);

			//TextView mName = (TextView) convertView.findViewById(R.id.name_textView);
			//mName.setText(log.getName().toString());
			
			TextView mPath = (TextView) convertView.findViewById(R.id.path_textView);
			mPath.setText(log.getPath().toString());
			
			//TextView mSize = (TextView) convertView.findViewById(R.id.size_textView);
			//mSize.setText(String.valueOf(log.getSize()));
			
			//TextView mTime = (TextView) convertView.findViewById(R.id.time_textView);
			//mTime.setText(String.valueOf(log.getTimeCost()));
			
			//TextView mException = (TextView) convertView.findViewById(R.id.exception_textView);
			//mException.setText(String.valueOf(log.HasException()));

			return convertView;
		}

		public TestLogAdapter(Context context, ArrayList<TestLog> plans) {
			super(context, 0, plans);
			// TODO 自动生成的构造函数存根
			mAppContext = context;
		}	
	}
	
	public static LogPickerDialogFragment newInstance(Context context) {
		mAppContext = context;
		cb = (Callbacks) context;
		mLogs = new ArrayList<TestLog>();
		/*
		for (int i = 0; i < 50; i++) {
			TestLog log = new TestLog("Log #" + i);
			log.setPath("/Application");
			log.setSize(1000);
			log.setTimeCost(1000);
			log.setHasException(true);
			mLogs.add(log);
		}
		*/
		LogPickerDialogFragment fragment = new LogPickerDialogFragment();
		return fragment;
	}

	
	@Override
	@NonNull
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_log_picker, null);
		ListView listView = (ListView) view.findViewById(R.id.log_picker_listView);
		
		File file = new File(SDCardUtils.getLogFilePath());
		
		File[] names = file.listFiles();

		for (int i = 0; i < names.length; i++) {
			TestLog log = new TestLog("Log #" + i);
			log.setPath(names[i].getAbsolutePath());
			log.setSize(1000);
			log.setTimeCost(1000);
			log.setHasException(true);
			mLogs.add(log);
		}
		
		TestLogAdapter adapter = new TestLogAdapter(mAppContext, mLogs);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
				// TODO 自动生成的方法存根
				mSelectedTestLog = (TestLog) listView.getAdapter().getItem(position);
				if (mCurrentItem != null) {
					mCurrentItem.setBackgroundResource(R.color.white);
				}
				mCurrentItem = view;
				mCurrentItem.setBackgroundResource(R.color.candy_blue);
			}
		});
		return new AlertDialog.Builder(getActivity())
					.setView(view)
					.setTitle(R.string.dialog_log_picker)
					.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO 自动生成的方法存根
							cb.refreshActivity(mSelectedTestLog);
						}
					})
					.setNegativeButton(android.R.string.cancel, null)
					.create();
	}

	
}
