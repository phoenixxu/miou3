package com.datang.miou.views.dialogs;

import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import com.datang.miou.R;
import com.datang.miou.datastructure.Station;

public class StationPickerDialogFragment extends DialogFragment {
	protected static final String TAG = "StationPickerDialogFragment";
	public interface Callbacks {
		public void onDone(StationPickerDialogFragment dialog, ArrayList<Station> stations);
	}
	private Callbacks cb;
	
	private class StationAdapter extends ArrayAdapter<Station> {
		
		private Context mAppContext;
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO 自动生成的方法存根
			if (convertView == null) {
				convertView = ((Activity) mAppContext).getLayoutInflater().inflate(R.layout.item_list_station, null);
			}
			
			Station station = getItem(position);
			CheckBox item = (CheckBox) convertView.findViewById(R.id.station_check_box);
			item.setText(station.toString());
			item.setChecked(station.isSelected());
			
			item.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Station station = getItem(position);
					CheckBox box = (CheckBox) v;
					if (box.isChecked()) {
						station.setSelected(true);
					} else {
						station.setSelected(false);
					}
				}
			});
			
			return convertView;
		}

		public StationAdapter(Context context, ArrayList<Station> stations) {
			super(context, 0, stations);
			// TODO 自动生成的构造函数存根
			mAppContext = context;
		}	
	}
	
	ArrayList<Station> mStations;
	private ListView mListView;
	private EditText mSearchEditText;
	private Button mSearchButton;
	private Button mUndoButton;
	private Button mSelectedButton;
	protected boolean showSelected = true;
	private Button mDoneButton;
	private Button mCancelButton;
	private StationPickerDialogFragment mDialog;

	
	public StationPickerDialogFragment(ArrayList<Station> stations) {
		super();
		// TODO Auto-generated constructor stub
		this.mStations = stations;
	}


	@Override
	@NonNull
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		mDialog = this;
		View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_station_picker, null);
		
		mListView = (ListView) view.findViewById(R.id.list_view);
		StationAdapter adapter = new StationAdapter(getActivity(), mStations);
		mListView.setAdapter(adapter);
		
		mSearchEditText = (EditText) view.findViewById(R.id.search_edit_text);
		mSearchEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO 自动生成的方法存根
				
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO 自动生成的方法存根
				
			}

			@Override
			public void onTextChanged(CharSequence c, int arg1, int arg2,
					int arg3) {
				// TODO 自动生成的方法存根
				if (c.toString().equals("")) {
					StationAdapter adapter = new StationAdapter(getActivity(), mStations);
					mListView.setAdapter(adapter);
				}
			}
			
		});
		
		mSearchButton = (Button) view.findViewById(R.id.search_button);
		mSearchButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ArrayList<Station> match = new ArrayList<Station>();
				String text = mSearchEditText.getText().toString();
				for (Station s : mStations) {
					if (s.getName().contains(text)) {
						match.add(s);
					}
				}
				StationAdapter adapter = new StationAdapter(getActivity(), match);
				mListView.setAdapter(adapter);
			}
		});
		
		mUndoButton = (Button) view.findViewById(R.id.undo_button);
		mUndoButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for (Station s : mStations) {
					s.setSelected(false);
				}
				((StationAdapter) mListView.getAdapter()).notifyDataSetChanged();
			}
		});
		
		mSelectedButton = (Button) view.findViewById(R.id.selected_button);
		mSelectedButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (showSelected) {
					ArrayList<Station> selected = getSelectedStations();
					//如果清空编辑框的操作放在刷新listview之后，则listview的变化会被覆盖
					mSelectedButton.setText(getResources().getString(R.string.all_show));
					mSearchEditText.setText(null);
					StationAdapter adapter = new StationAdapter(getActivity(), selected);
					mListView.setAdapter(adapter);
					showSelected = false;
				} else {
					mSelectedButton.setText(getResources().getString(R.string.selected));
					mSearchEditText.setText(null);
					StationAdapter adapter = new StationAdapter(getActivity(), mStations);
					mListView.setAdapter(adapter);
					showSelected = true;
				}
			}
		});
		
		mDoneButton = (Button) view.findViewById(R.id.done_button);
		mDoneButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sendResult(true);
			}
		});
		
		mCancelButton = (Button) view.findViewById(R.id.cancel_button);
		mCancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sendResult(false);
			}
		});
		return new AlertDialog.Builder(getActivity())
					.setView(view)
					.setTitle(getResources().getString(R.string.dialog_station_picker))
					.create();
	}
	
	
	protected void sendResult(boolean done) {
		// TODO Auto-generated method stub
		if (done) {
			cb.onDone(mDialog, getSelectedStations());
		} else {
			cb.onDone(mDialog, null);
		}
	}


	protected ArrayList<Station> getSelectedStations() {
		// TODO Auto-generated method stub
		ArrayList<Station> selected = new ArrayList<Station>();
		for (Station s : mStations) {
			if (s.isSelected()) {
				selected.add(s);
			}
		}
		return selected;
	}


	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.cb = (Callbacks) activity;
	}


	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		this.cb = null;
	}
}
