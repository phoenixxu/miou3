package com.datang.miou.views.dialogs;

import java.util.ArrayList;

import com.datang.miou.R;
import com.datang.miou.datastructure.Building;
import com.datang.miou.datastructure.TestLog;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class BuildingPickerDialogFragment extends DialogFragment {
	public static final String EXTRA_BUILDING = "extra_building";

	public interface Callbacks {
		public void refreshActivity(TestLog log);
	}
	
	private static ArrayList<Building> buildings;

	public static BuildingPickerDialogFragment newInstance(Context context) {
		buildings = new ArrayList<Building>();
		for (int i = 0; i < 50; i++) {
			Building building = new Building("Building #" + i);
			buildings.add(building);
		}
		
		BuildingPickerDialogFragment fragment = new BuildingPickerDialogFragment();
		return fragment;
	}

	
	@Override
	@NonNull
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_building_picker, null);
		ListView listView = (ListView) view.findViewById(R.id.building_picker_listView);
		
		ArrayAdapter<Building> adapter = new ArrayAdapter<Building>(getActivity(), android.R.layout.simple_list_item_1, buildings);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
				// TODO 自动生成的方法存根
				Building building = (Building) listView.getAdapter().getItem(position);
				sendResult(building, Activity.RESULT_OK);
				
			}
		});
		return new AlertDialog.Builder(getActivity())
					.setView(view)
					.setTitle(R.string.dialog_building_picker)
					.create();
	}

	private void sendResult(Building building, int resultCode) {
		// TODO 自动生成的方法存根
		if (getTargetFragment() == null) {
			return;
		}
		Intent intent = new Intent();
		intent.putExtra(EXTRA_BUILDING, building);
		getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
	}
	
}
