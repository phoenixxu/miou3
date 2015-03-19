package com.datang.miou.preferences;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.datang.miou.R;

import com.datang.miou.FragmentSupport;
import com.datang.miou.annotation.AfterView;
import com.datang.miou.annotation.AutoView;
import com.datang.miou.datastructure.EventType;
import com.datang.miou.datastructure.EventTypeLab;
import com.datang.miou.datastructure.Globals;
import com.datang.miou.datastructure.TestLog;
import com.datang.miou.datastructure.TestPlan;
import com.datang.miou.widget.ColorPickerPrefrence;

@AutoView(R.layout.preference_filter_event)
public class EventFilterFragment extends FragmentSupport {

	public static final String KEY_BASE = "PREF_EVENT_COLOR";
	@AutoView(R.id.all_checkBox)
	private CheckBox mAllCheckBox;
	@AutoView(R.id.search_event_editText)
	private EditText mSearchEventEditText;
	@AutoView(R.id.search_event_ImageButton)
	private ImageButton mSearchEventImageButton;
	@AutoView(R.id.all_selected_checkBox)
	private CheckBox mAllSelectedCheckBox;
	@AutoView(R.id.search_event_selected_editText)
	private EditText mSearchSelectedEventEditText;
	@AutoView(R.id.search_event_selected_ImageButton)
	private ImageButton mSearchSelectedEventImageButton;
	@AutoView(R.id.event_listView)
	private ListView mEventListView;
	@AutoView(R.id.event_selected_listView)
	private ListView mEventSelectedListView;
	@AutoView(R.id.add_to_selected)
	private ImageView mAddButton;
	@AutoView(R.id.remove_from_selected)
	private ImageView mRemoveButton;
	
	private ArrayList<EventType> mTypes;
	private ArrayList<EventType> mSelectedTypes;
	
	private class EventTypeAdapter extends ArrayAdapter<EventType> {

		private Context mAppContext;
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO 自动生成的方法存根
			if (convertView == null) {
				convertView = ((Activity) mAppContext).getLayoutInflater().inflate(R.layout.item_list_event_type, null);
			}
			
			EventType type = getItem(position);
		
			TextView mName = (TextView) convertView.findViewById(R.id.item_event_type);
			mName.setText(type.getDescription());
			
			CheckBox mCheckBox = (CheckBox) convertView.findViewById(R.id.item_event_checkBox);
			mCheckBox.setChecked(type.isSelected());

			mCheckBox.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view) {
					// TODO 自动生成的方法存根
					EventType type = getItem(position);
					if (((CheckBox) view).isChecked()) {
						type.setSelected(true);
					} else {
						type.setSelected(false);
					}
				}
			} );
			return convertView;
		}
		
		public EventTypeAdapter(Context context, ArrayList<EventType> types) {
			super(context, 0, types);
			// TODO 自动生成的构造函数存根
			mAppContext = context;
		}	
	}
		
	private class SelectedEventTypeAdapter extends ArrayAdapter<EventType> {
		private Context mAppContext;
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO 自动生成的方法存根
			if (convertView == null) {
				convertView = ((Activity) mAppContext).getLayoutInflater().inflate(R.layout.item_list_option_event_type, null);
			}
			
			EventType type = getItem(position);
			
			ColorPickerPrefrence picker = (ColorPickerPrefrence) convertView.findViewById(R.id.item_event_option);
			picker.setId(type.getId().toString());
			picker.setKey(KEY_BASE + "_" + type.getId());
			picker.setTitle(type.getDescription());
			picker.refreshOptionView();
			
			CheckBox mCheckBox = (CheckBox) convertView.findViewById(R.id.item_event_checkBox);
			mCheckBox.setChecked(type.isWillRemove());
			
			mCheckBox.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view) {
					// TODO 自动生成的方法存根
					EventType type = getItem(position);
					if (((CheckBox) view).isChecked()) {
						type.setWillRemove(true);
					} else {
						type.setWillRemove(false);
					}
				}
			} );
			return convertView;
		}

		public SelectedEventTypeAdapter(Context context, ArrayList<EventType> types) {
			super(context, 0, types);
			// TODO 自动生成的构造函数存根
			mAppContext = context;
		}	
	}
	
	@AfterView
	void init() {
		
		initTypeArray();
		
		EventTypeAdapter adapter = new EventTypeAdapter(getActivity(), mTypes);
		mEventListView.setAdapter(adapter);	
		
		SelectedEventTypeAdapter adapterSelected = new SelectedEventTypeAdapter(getActivity(), mSelectedTypes);
		mEventSelectedListView.setAdapter(adapterSelected);
		
		mAllCheckBox.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO 自动生成的方法存根
				if (((CheckBox) view).isChecked()) {
					for (EventType type : mTypes) {
						type.setSelected(true);
					}	
				} else {
					for (EventType type : mTypes) {
						type.setSelected(false);
					}
				}
					
				refreshEventList();
			}
		});
		
		mAllSelectedCheckBox.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO 自动生成的方法存根
				if (((CheckBox) view).isChecked()) {
					for (EventType type : mSelectedTypes) {
						type.setWillRemove(true);
					}	
				} else {
					for (EventType type : mTypes) {
						type.setWillRemove(false);
					}
				}
					
				refreshSelectedEventList();
			}
		});
		
		mAddButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO 自动生成的方法存根
				boolean shouldAdd = true;
				
				for (EventType type : mTypes) {
					
					if (type.isSelected()) {
						shouldAdd = true;
						for (EventType selectedType : mSelectedTypes) {
							if (selectedType.equals(type)) {
								shouldAdd = false;
								break;
							}
						}
						if (shouldAdd){
							mSelectedTypes.add(type);
						}
					}
				}	
				
				mSearchSelectedEventEditText.setText("");
				mSearchEventEditText.setText("");
				refreshSelectedEventList();
			}		
		});
		
		mRemoveButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO 自动生成的方法存根	
				Iterator<EventType> iter = mSelectedTypes.listIterator();
				while (iter.hasNext()) {
					EventType type = iter.next();
					if (type.isWillRemove()) {
						type.setWillRemove(false);
						iter.remove();
					}
				}
				mAllSelectedCheckBox.setChecked(false);
				
				mSearchSelectedEventEditText.setText("");
				mSearchEventEditText.setText("");
				refreshSelectedEventList();
			}
		});
		
		mSearchEventEditText.addTextChangedListener(new TextWatcher() {

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
					refreshEventList();
				}
			}
			
		});
		
		mSearchEventImageButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				String filter = mSearchEventEditText.getText().toString();
				ArrayList<EventType> part = new ArrayList<EventType>();
				for (EventType type : mTypes) {
					if (type.getDescription().contains(filter)) {
						part.add(type);
					}
				}
				EventTypeAdapter adapterSelected = new EventTypeAdapter(getActivity(), part);
				mEventListView.setAdapter(adapterSelected);
			}
		});
		
		mSearchSelectedEventEditText.addTextChangedListener(new TextWatcher() {

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
					refreshSelectedEventList();
				}
			}
			
		});
		
		mSearchSelectedEventImageButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				String filter = mSearchSelectedEventEditText.getText().toString();
				ArrayList<EventType> part = new ArrayList<EventType>();
				for (EventType type : mSelectedTypes) {
					if (type.getDescription().contains(filter)) {
						part.add(type);
					}
				}
				SelectedEventTypeAdapter adapterSelected = new SelectedEventTypeAdapter(getActivity(), part);
				mEventSelectedListView.setAdapter(adapterSelected);
			}
		});
	}
	
	private void initTypeArray() {
		// TODO 自动生成的方法存根

		/*
		mTypes = new ArrayList<EventType>();
		mTypes.add(new EventType("Task Finished Call", 0));
		mTypes.add(new EventType("Stop Logging", 0));
		mTypes.add(new EventType("Start Task Ping", 0));
		mTypes.add(new EventType("PPP Dial", 0));
		mTypes.add(new EventType("Dial Success", 0));
		mTypes.add(new EventType("One Ping Start", 0));
		*/
		mTypes = EventTypeLab.get(getActivity()).getEventTypes();
		/*
		if (mTypes.size() == 0) {
			mTypes.add(new EventType("Task Finished Call", 0));
			mTypes.add(new EventType("Stop Logging", 0));
			mTypes.add(new EventType("Start Task Ping", 0));
			mTypes.add(new EventType("PPP Dial", 0));
			mTypes.add(new EventType("Dial Success", 0));
			mTypes.add(new EventType("One Ping Start", 0));
			 EventTypeLab.get(getActivity()).saveEventTypes();
		}
		*/
		
		mSelectedTypes = new ArrayList<EventType>();
		for (EventType type : mTypes) {
			if (type.isSelected()) {
				mSelectedTypes.add(type);
			}
		}
		
	}

	private void refreshSelectedEventList() {
		// TODO 自动生成的方法存根
		//((EventTypeAdapter) mEventSelectedListView.getAdapter()).notifyDataSetChanged();
		SelectedEventTypeAdapter adapterSelected = new SelectedEventTypeAdapter(getActivity(), mSelectedTypes);
		mEventSelectedListView.setAdapter(adapterSelected);
	}
	
	private void refreshEventList() {
		// TODO 自动生成的方法存根
		EventTypeAdapter adapterSelected = new EventTypeAdapter(getActivity(), mTypes);
		mEventListView.setAdapter(adapterSelected);
	}
}
