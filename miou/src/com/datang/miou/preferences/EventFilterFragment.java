package com.datang.miou.preferences;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.datang.miou.R;

import com.datang.miou.FragmentSupport;
import com.datang.miou.annotation.AfterView;
import com.datang.miou.annotation.AutoView;
import com.datang.miou.datastructure.EventType;
import com.datang.miou.datastructure.TestScript;
import com.datang.miou.utils.MiscUtils;
import com.datang.miou.utils.SDCardUtils;
import com.datang.miou.widget.ColorPickerPrefrence;
import com.datang.miou.xml.PullEventTypeParser;
import com.datang.miou.xml.PullTestCommandParser;
import com.datang.miou.xml.PullTestScriptParser;
import com.datang.miou.MiouApp;

@AutoView(R.layout.preference_filter_event)
public class EventFilterFragment extends FragmentSupport {

	public static final String TAG = "EventFilterFragment";
	
	public static final String KEY_BASE = "PREF_EVENT_COLOR";
	private static final String FILE_NAME_EVENT_TYPE = "event_types.json";

	private static final String XML_FILE_EVENT_TYPES = "EventTypes.xml";
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
	
	private class EventTypeAdapter extends ArrayAdapter<EventType> {

		private Context mAppContext;
		
		@SuppressLint("InflateParams")
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
		
		@SuppressLint("InflateParams")
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO 自动生成的方法存根
			if (convertView == null) {
				convertView = ((Activity) mAppContext).getLayoutInflater().inflate(R.layout.item_list_option_event_type, null);
			}
			
			EventType type = getItem(position);
			
			ColorPickerPrefrence picker = (ColorPickerPrefrence) convertView.findViewById(R.id.item_event_option);
			picker.setId(String.valueOf(type.getId()));
			//picker.setKey(KEY_BASE + "_" + type.getId());
			picker.setTitle(type.getDescription());
			picker.setItem(type);
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
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setRetainInstance(true);
	}

	@AfterView
	void init() {
		
		initTypeArray();
		
		EventTypeAdapter adapter = new EventTypeAdapter(getActivity(), mTypes);
		mEventListView.setAdapter(adapter);	
		
		SelectedEventTypeAdapter adapterSelected = new SelectedEventTypeAdapter(getActivity(), (ArrayList<EventType>) ((MiouApp) getActivity().getApplication()).getSelectedEventTypes());
		mEventSelectedListView.setAdapter(adapterSelected);
		
		//事件全选复选框
		mAllCheckBox.setChecked(false);
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
		
		//已选事件全选复选框
		mAllSelectedCheckBox.setChecked(false);
		mAllSelectedCheckBox.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO 自动生成的方法存根
				if (((CheckBox) view).isChecked()) {
					for (EventType type : ((MiouApp) getActivity().getApplication()).getSelectedEventTypes()) {
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
		
		//向已选事件列表添加事件
		mAddButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO 自动生成的方法存根
				boolean shouldAdd = true;
				
				for (EventType type : mTypes) {
					
					if (type.isSelected()) {
						shouldAdd = true;
						for (EventType selectedType : ((MiouApp) getActivity().getApplication()).getSelectedEventTypes()) {
							if (selectedType.equals(type)) {
								shouldAdd = false;
								break;
							}
						}
						if (shouldAdd){
							((MiouApp) getActivity().getApplication()).getSelectedEventTypes().add(type);
						}
					}
				}	
				
				mSearchSelectedEventEditText.setText("");
				mSearchEventEditText.setText("");
				refreshSelectedEventList();
			}		
		});
		
		//从已选事件列表移除事件
		mRemoveButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO 自动生成的方法存根	
				Iterator<EventType> iter = ((MiouApp) getActivity().getApplication()).getSelectedEventTypes().listIterator();
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
		
		//搜索事件文本框
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
		
		//搜索按钮
		mSearchEventImageButton.setOnClickListener(new View.OnClickListener() {
			
			@SuppressLint("DefaultLocale")
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				String filter = mSearchEventEditText.getText().toString().toLowerCase();
				ArrayList<EventType> part = new ArrayList<EventType>();
				for (EventType type : mTypes) {
					if (type.getDescription().toLowerCase().contains(filter)) {
						part.add(type);
					}
				}
				EventTypeAdapter adapterSelected = new EventTypeAdapter(getActivity(), part);
				mEventListView.setAdapter(adapterSelected);
			}
		});
		
		//搜索已选事件文本框
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
		
		//搜索按钮
		mSearchSelectedEventImageButton.setOnClickListener(new View.OnClickListener() {
			
			@SuppressLint("DefaultLocale")
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				String filter = mSearchSelectedEventEditText.getText().toString().toLowerCase();
				ArrayList<EventType> part = new ArrayList<EventType>();
				for (EventType type : ((MiouApp) getActivity().getApplication()).getSelectedEventTypes()) {
					if (type.getDescription().toLowerCase().contains(filter)) {
						part.add(type);
					}
				}
				SelectedEventTypeAdapter adapterSelected = new SelectedEventTypeAdapter(getActivity(), part);
				mEventSelectedListView.setAdapter(adapterSelected);
			}
		});
	}
	
	//初始化数据源
	private void initTypeArray() {
		// TODO 自动生成的方法存根
		if (mTypes == null) {
			mTypes = getEventTypes();
		}
		
		//if (mSelectedTypes == null) {
		//mSelectedTypes = new ArrayList<EventType>();
		for (EventType type : mTypes) {
			if (type.isSelected()) {
				 ((MiouApp) getActivity().getApplication()).getSelectedEventTypes().add(type);
			}
		}
	}

	//刷新已选事件列表
	private void refreshSelectedEventList() {
		// TODO 自动生成的方法存根
		//((EventTypeAdapter) mEventSelectedListView.getAdapter()).notifyDataSetChanged();
		SelectedEventTypeAdapter adapterSelected = new SelectedEventTypeAdapter(getActivity(),  (ArrayList<EventType>) ((MiouApp) getActivity().getApplication()).getSelectedEventTypes());
		mEventSelectedListView.setAdapter(adapterSelected);
	}
	
	//刷新事件列表
	private void refreshEventList() {
		// TODO 自动生成的方法存根
		EventTypeAdapter adapterSelected = new EventTypeAdapter(getActivity(), mTypes);
		mEventListView.setAdapter(adapterSelected);
	}
	
	/*
    private ArrayList<EventType> initEventTypes() {
		// TODO Auto-generated method stub
    	ArrayList<EventType> types = null;
    	
    	String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			File file = new File(getActivity().getExternalFilesDir(null), FILE_NAME_EVENT_TYPE);
			if (!file.exists()) {
				Toast.makeText(getActivity(), FILE_NAME_EVENT_TYPE + " does not exist", Toast.LENGTH_SHORT).show();
				try {
					file.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//初始化文件
				types = getEventTyps();
				types = new ArrayList<EventType>();
				types.add(new EventType("Outgoing Call Attempt", "0x2000"));
				types.add(new EventType("Outgoing Call Alerting", "0x2001"));
				types.add(new EventType("Outgoing Call Connected", "0x2002"));
				types.add(new EventType("Outgoing Call Failure", "0x2003"));
				types.add(new EventType("Incoming Call Attempt", "0x2004"));
				types.add(new EventType("Incoming Call Alerting", "0x2005"));
				types.add(new EventType("Incoming Call Connected", "0x2006"));
				types.add(new EventType("Incoming Call Failure", "0x2007"));
				types.add(new EventType("Call Complete", "0x2008"));
				types.add(new EventType("Drop Call", "0x2009"));
				types.add(new EventType("FTP server logon success", "0x4100"));
				types.add(new EventType("FTP server logon fail", "0x4101"));
				types.add(new EventType("FTP Download Attempt", "0x4102"));
				types.add(new EventType("Ftp Download Success", "0x4103"));
				
				String path = file.getAbsolutePath();
				
				EventTypeJSONSerializer serializer = new EventTypeJSONSerializer(getActivity(), path);
				try {
					serializer.saveEventTypes(types);
				} catch (Exception e) {
					Toast.makeText(getActivity(), "json init error!", Toast.LENGTH_SHORT).show();
				}
			} else {
				String path = file.getAbsolutePath();
				EventTypeJSONSerializer serializer = new EventTypeJSONSerializer(getActivity(), path);
				try {
					types = serializer.loadEventTypes();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Toast.makeText(getActivity(), "json load error!", Toast.LENGTH_SHORT).show();
				}
			}
		}
		return types;
	}
	*/

	private ArrayList<EventType> getEventTypes() {
		// TODO Auto-generated method stub
		try {
			File file = MiscUtils.getExternalFileForRead(getActivity(), SDCardUtils.getConfigPath(), XML_FILE_EVENT_TYPES);			
			InputStream is = new FileInputStream(file);
			//InputStream is = getActivity().getAssets().open(XML_FILE_TEST_SCHEME);
			PullEventTypeParser parser = new PullEventTypeParser(getActivity());
			return (ArrayList<EventType>) parser.parse(is);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			Toast.makeText(getActivity(), "parse xml error!", Toast.LENGTH_SHORT).show();
			return null;
		}
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//保存
		if (mTypes != null) {
			writeToXml(mTypes);
		}
		//saveEventTypes();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
    
	//保存事件类型到JSON文件，主要是颜色改动
	/*
	private void saveEventTypes() {
		// TODO Auto-generated method stub
    	String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			File file = new File(getActivity().getExternalFilesDir(null), FILE_NAME_EVENT_TYPE);
			if (file.exists()) {
				file.delete();
			}
			try {
				file.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String path = file.getAbsolutePath();
			EventTypeJSONSerializer serializer = new EventTypeJSONSerializer(getActivity(), path);
			try {
				serializer.saveEventTypes(mTypes);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Toast.makeText(getActivity(), "json save error!", Toast.LENGTH_SHORT).show();
			}
		}
	}
    */
	
	/*
	 * 将时间类型写入XML文件
	 */
	@SuppressLint("SimpleDateFormat")
	private void writeToXml(List<EventType> types) {

		File file = MiscUtils.getExternalFileForWrite(getActivity(), SDCardUtils.getConfigPath(), XML_FILE_EVENT_TYPES);
		
		Writer writer = null;
		try {
			FileOutputStream out = new FileOutputStream(file);
			writer = new OutputStreamWriter(out);
			PullEventTypeParser.writeXml(types, writer);
		} catch (Exception e) {
			Toast.makeText(getActivity(), "Xml Write Error : " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (Exception e) {
					Toast.makeText(getActivity(), "File Close Error : " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
}
