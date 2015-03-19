package com.datang.miou.views.dialogs;

import java.util.ArrayList;

import com.datang.miou.R;
import com.datang.miou.datastructure.InnerMap;
import com.datang.miou.datastructure.TestLog;
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
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

public class InnerMapPickerDialogFragment extends DialogFragment {
	public static final String EXTRA_BUILDING = "extra_building";

	public interface Callbacks {
		public void refreshActivity(TestLog log);
	}
	
	private static ArrayList<InnerMap> innerMaps;
	private static ArrayList<InnerMap> selectedMaps;
	
	private class InnerMapAdapter extends ArrayAdapter<InnerMap> {
		private Context mAppContext;
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO 自动生成的方法存根
			if (convertView == null) {
				convertView = ((Activity) mAppContext).getLayoutInflater().inflate(R.layout.item_list_inner_map, null);
			}
			
			InnerMap map = this.getItem(position);

			TextView mapTextView = (TextView) convertView.findViewById(R.id.map_textView);
			mapTextView.setText(map.getName());
			
			CheckBox mapCheckBox = (CheckBox) convertView.findViewById(R.id.map_checkBox);
			mapCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton view, boolean isChecked) {
					// TODO 自动生成的方法存根
					if (isChecked) {
						selectedMaps.add(getItem(position));
					} else {
						selectedMaps.remove(getItem(position));
					}
				}
			} );

			return convertView;
		}

		public InnerMapAdapter(Context context, ArrayList<InnerMap> maps) {
			super(context, 0, maps);
			// TODO 自动生成的构造函数存根
			mAppContext = context;
		}	
	}
	
	public static InnerMapPickerDialogFragment newInstance(Context context) {
		innerMaps = new ArrayList<InnerMap>();
		selectedMaps = new ArrayList<InnerMap>();
		
		for (int i = 0; i < 50; i++) {
			InnerMap map = new InnerMap("Map #" + i);
			innerMaps.add(map);
		}
		
		InnerMapPickerDialogFragment fragment = new InnerMapPickerDialogFragment();
		return fragment;
	}

	
	@Override
	@NonNull
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_map_picker, null);
		ListView listView = (ListView) view.findViewById(R.id.map_picker_listView);
		
		InnerMapAdapter adapter = new InnerMapAdapter(getActivity(), innerMaps);
		listView.setAdapter(adapter);
		/*
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
				// TODO 自动生成的方法存根
				Building building = (Building) listView.getAdapter().getItem(position);
				sendResult(building, Activity.RESULT_OK);
				
			}
		});
		*/
		return new AlertDialog.Builder(getActivity())
					.setView(view)
					.setTitle(R.string.dialog_building_picker)
					.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO 自动生成的方法存根
							sendResult(selectedMaps, Activity.RESULT_OK);
						}
					})
					.setNegativeButton(android.R.string.cancel, null)
					.create();
	}

	private void sendResult(ArrayList<InnerMap> maps, int resultCode) {
		// TODO 自动生成的方法存根
		if (getTargetFragment() == null) {
			return;
		}
		Intent intent = new Intent();
		intent.putExtra(EXTRA_BUILDING, maps);
		getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
	}
	
}
