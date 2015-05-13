package com.datang.miou.views.gen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.datang.miou.FragmentSupport;
import com.datang.miou.ProcessInterface;
import com.datang.miou.HoldLastRecieverClient;
import com.datang.miou.R;
import com.datang.miou.annotation.AfterView;
import com.datang.miou.annotation.AutoView;
import com.datang.miou.datastructure.Event;
import com.datang.miou.datastructure.EventType;
import com.datang.miou.datastructure.RealData;
import com.datang.miou.datastructure.Signal;
import com.datang.miou.detailtree.TreeMainActivity;
import com.datang.miou.views.dialogs.LabelInfoDialogFragment;
import com.datang.miou.MiouApp;

/**
 * 信令
 * 
 * @author suntongwei
 */
@AutoView(R.layout.gen_signal)
public class GenSignalFragment extends FragmentSupport {
	public static final String EXTRA_SIGNAL = "extra_signal";
	private static final String DIALOG_LABEL_INFO = "label_info";
	public static final String TAG = "GenSignalFragment";
	
	private EditText searchEventEditText;
	private EditText searchSignalEditText;
	private Button searchEventButton;
	private Button searchSignalButton;
	private ListView mEventListView;
	private ListView mSignalListView;
	//private ArrayAdapter<Signal> mSignalAdapter;
	//private ArrayAdapter<Event> mEventAdapter;
	
	protected boolean isSearchEvent;
	protected boolean isSearchSignal;
	private Button clearSignalButton;
	private Button clearEventButton;
	private List<EventType> mSelectedEventTypes;
	private EventAdapter mEventAdapter;
	private SignalAdapter mSignalAdapter;
	
	public interface Callbacks {
		public void setSignalFragment(Fragment fragment);
	}
	private Callbacks cb;
	
	
	
	/*
	@Override
	public void ProcessData(Map<String, String> mapIDValue) {
		//Log.i(TAG, "mapIDValue = " + mapIDValue);
		
		if (mapIDValue.containsKey("s000")){

			Signal signal = parseSignal(mapIDValue);
			//添加到临时信令列表
			//如果直接追加到ListView的数据源，会造成ListView刷新错误
			//所以先放到一个临时列表中，更新UI时在追加到数据源中
			mTempSignalList.add(0, signal);	
			
		}
		if (mapIDValue.containsKey("e000")){

			//添加到临时事件列表
			Event event = parseEvent(mapIDValue);
			mTempEventList.add(0, event);
			//Log.i(TAG, "add a event");
		}	
		
	}
	*/

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

	/*
	 * 事件列表Adapter
	 */
	private class EventAdapter extends ArrayAdapter<Event> {
		private Context mAppContext;
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				convertView = ((Activity) mAppContext).getLayoutInflater().inflate(R.layout.item_display, null);
			}
			
			Event event = this.getItem(position);

			int color =  getActivity().getResources().getColor(R.color.black);
			
			//根据事件类型设置文字颜色
			for (EventType type : mSelectedEventTypes) {
				if (event.getType().equals(type.getId())) {
					color = getActivity().getResources().getColor(type.getColor());
					break;
				}
			}
			
			TextView mTime = (TextView) convertView.findViewById(R.id.time);
			mTime.setText(event.getHour() + ":" + event.getMinute() + ":" + event.getSecond());
			mTime.setTextColor(color);
			
			TextView mContent = (TextView) convertView.findViewById(R.id.content);
			mContent.setText(event.getContent());
			mContent.setTextColor(color);
			
			return convertView;
		}

		public EventAdapter(Context context, ArrayList<Event> events) {
			super(context, 0, events);
			mAppContext = context;
		}	
	}
	
	/*
	 * 信令列表Adapter
	 */
	private class SignalAdapter extends ArrayAdapter<Signal> {
		private Context mAppContext;
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = ((Activity) mAppContext).getLayoutInflater().inflate(R.layout.item_display, null);
			}
			
			Signal signal = this.getItem(position);

			TextView mTime = (TextView) convertView.findViewById(R.id.time);
			mTime.setText(signal.getHour() + ":" + signal.getMinute() + ":" + signal.getSecond());
			
			TextView mContent = (TextView) convertView.findViewById(R.id.content);
			mContent.setText(signal.getType());
			return convertView;
		}

		public SignalAdapter(Context context, ArrayList<Signal> signals) {
			super(context, 0, signals);
			mAppContext = context;
		}	
	}
	
	private void setupEventAdapter(List<Event> events) {
		
		if (getActivity() == null || mEventListView == null) {
			return;
		}
		
		if (events != null) {
			mEventListView.setAdapter(new EventAdapter(getActivity(), (ArrayList<Event>) events));
		} else {
			mEventListView.setAdapter(null);
		}
		
	}

	private void setupSignalAdapter(List<Signal> signals) {
		
		if (getActivity() == null || mSignalListView == null) {
			return;
		}
		
		if (signals != null) {
			mSignalListView.setAdapter(new SignalAdapter(getActivity(), (ArrayList<Signal>) signals));
		} else {
			mSignalListView.setAdapter(null);
		}
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		this.setRetainInstance(true);
		
		/*
		//后台数据线程只能启动一次
		if (!((MiouApp) getActivity().getApplication()).isHoldLastServerRunning()) {
	        ProcessInterface.mHoldLastServer.StartThread();
	        ((MiouApp) getActivity().getApplication()).setHoldLastServerRunning(true);
		}
		ProcessInterface.mHoldLastServer.RegisterClient(this);
		*/
	}

	@AfterView
	private void init() {
        
		cb.setSignalFragment(this);
		
		searchEventEditText = (EditText) f(R.id.search_event_editText);
		searchSignalEditText = (EditText) f(R.id.search_signal_editText);
		
		searchEventButton = (Button) f(R.id.search_event_button);
		searchEventButton.setOnClickListener(new View.OnClickListener() {
			
			@SuppressLint("DefaultLocale")
			@Override
			public void onClick(View view) {
				ArrayList<Event> result = new ArrayList<Event>();
				String string = searchEventEditText.getText().toString().toLowerCase();
				for (Event e : ((MiouApp) getActivity().getApplication()).getGenEvents()) {
					if (e.getContent().toLowerCase().contains(string)) {
						result.add(e);
					}
				}
				setupEventAdapter(result);
				isSearchEvent = true;
			}
		});
		
		searchSignalButton = (Button) f(R.id.search_signal_button);
		searchSignalButton.setOnClickListener(new View.OnClickListener() {
			
			@SuppressLint("DefaultLocale")
			@Override
			public void onClick(View view) {
				ArrayList<Signal> result = new ArrayList<Signal>();
				String string = searchSignalEditText.getText().toString().toLowerCase();
				for (Signal s : ((MiouApp) getActivity().getApplication()).getGenSignals()) {
					if (s.getContent().toLowerCase().contains(string)) {
						result.add(s);
					}
				}
				setupSignalAdapter(result);
				isSearchSignal = true;
			}
		});
		
		clearEventButton = (Button) f(R.id.clear_event_button);
		clearEventButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				searchEventEditText.setText(null);
				isSearchEvent = false;
				mEventListView.setAdapter(mEventAdapter);
			}
		});
		
		clearSignalButton = (Button) f(R.id.clear_signal_button);
		clearSignalButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				searchSignalEditText.setText(null);
				isSearchSignal = false;
				mSignalListView.setAdapter(mSignalAdapter);
				Log.i("update signal", "clear mSignalAdapter = " + mSignalAdapter);

			}
		});
		
		mEventListView = (ListView) f(R.id.event_listView);
		mEventListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		mEventListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> listView, View view, int position, long id) {

				//长按添加附加信息
				Event event = (Event) listView.getAdapter().getItem(position);
				FragmentManager fm = getActivity().getSupportFragmentManager();
				LabelInfoDialogFragment dialog = LabelInfoDialogFragment.newInstance(event);
				dialog.show(fm, DIALOG_LABEL_INFO);

				//TODO:添加附加信息完成后返回刷新列表
				//((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
				return true;
			}
			
		});

		mSignalListView = (ListView) f(R.id.signal_listView);
		mSignalListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		mSignalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> listView, View view, int position, long id) {

				Signal signal = (Signal) listView.getAdapter().getItem(position);
				Intent intent = new Intent(getActivity(), TreeMainActivity.class);
				intent.putExtra(EXTRA_SIGNAL, signal.getContent());
				startActivity(intent);
			}
		});
		
		//从全局变量中获得事件类型，其中包含了字体颜色
		//这个集合是在“信令筛选设置”中选中的
		mSelectedEventTypes = ((MiouApp) getActivity().getApplication()).getSelectedEventTypes();
		
		mEventAdapter = new EventAdapter(getActivity(), (ArrayList<Event>) ((MiouApp) getActivity().getApplication()).getGenEvents());
		mSignalAdapter = new SignalAdapter(getActivity(), (ArrayList<Signal>) ((MiouApp) getActivity().getApplication()).getGenSignals());

		Log.i("update signal", "mSignalAdapter = " + mSignalAdapter);

		mEventListView.setAdapter(mEventAdapter);
		mSignalListView.setAdapter(mSignalAdapter);
	}

	@Override
	protected void updateUI(RealData data) {

		super.updateUI(data);
		
		//Log.i(TAG, "update ui in signal fragment");
		
		//从临时列表里添加到全局列表
		/*
		if (mTempSignalList.size() > 0) {
			for (Signal s : mTempSignalList) {
				((MiouApp) getActivity().getApplication()).getGenSignals().add(0, s);
			}
			mTempSignalList.clear();
			//Log.i(TAG, "add signals to global");
		}
		
		if (mTempEventList.size() > 0) {
			((MiouApp) getActivity().getApplication()).getGenEvents().addAll(mTempEventList);
			mTempEventList.clear();
			//Log.i(TAG, "add events to global");
		}
		*/
		
		//updateEventListView();
		
		//updateSignalListView();
	
	}

	public void updateSignalListView() {
		// TODO Auto-generated method stub
		//如果不在搜索模式则更新ListView

		Log.i("update signal", "updateSignalListView, mSignalAdapter = " + mSignalAdapter);
		if (!isSearchSignal) {
			mSignalAdapter.notifyDataSetChanged();
		}	
	}

	public void updateEventListView() {
		// TODO Auto-generated method stub
		//如果不在搜索模式则更新ListView
		if (!isSearchEvent) {	
			mEventAdapter.notifyDataSetChanged();
		}
	}
}
