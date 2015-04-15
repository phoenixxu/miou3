package com.datang.miou.views.gen;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.datang.miou.FragmentSupport;
import com.datang.miou.ProcessInterface;
import com.datang.miou.R;
import com.datang.miou.annotation.AfterView;
import com.datang.miou.annotation.AutoView;
import com.datang.miou.datastructure.Event;
import com.datang.miou.datastructure.RealData;
import com.datang.miou.datastructure.Signal;
import com.datang.miou.datastructure.TestLog;
import com.datang.miou.detailtree.TreeMainActivity;
import com.datang.miou.views.dialogs.LabelInfoDialogFragment;

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
	private ImageButton searchEventButton;
	private ImageButton searchSignalButton;
	private ListView eventListView;
	private ListView signalListView;
	private ArrayList<Event> mEvents;
	private ArrayList<Signal> mSignals;
	private List<Event> mCurrentEvents = new ArrayList<Event>();
	private List<Signal> mCurrentSignals = new ArrayList<Signal>();
	private ArrayAdapter<Signal> mSignalAdapter;
	private ArrayAdapter<Event> mEventAdapter;
	
	private class EventAdapter extends ArrayAdapter<Event> {
		private Context mAppContext;
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO 自动生成的方法存根
			if (convertView == null) {
				convertView = ((Activity) mAppContext).getLayoutInflater().inflate(R.layout.item_display, null);
			}
			
			Event event = this.getItem(position);

			TextView mTime = (TextView) convertView.findViewById(R.id.time);
			mTime.setText(event.getHour() + ":" + event.getMinute() + ":" + event.getSecond());
			
			TextView mContent = (TextView) convertView.findViewById(R.id.content);
			mContent.setText(event.getType());
			
			//TODO:根据事件类型设置文字颜色
			return convertView;
		}

		public EventAdapter(Context context, ArrayList<Event> events) {
			super(context, 0, events);
			// TODO 自动生成的构造函数存根
			mAppContext = context;
		}	
	}
	
	private class SignalAdapter extends ArrayAdapter<Signal> {
		private Context mAppContext;
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO 自动生成的方法存根
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
			// TODO 自动生成的构造函数存根
			mAppContext = context;
		}	
	}
	
	private void setupEventAdapter(ArrayList<Event> events) {
		// TODO 自动生成的方法存根
		if (getActivity() == null || eventListView == null) {
			return;
		}
		
		if (events != null) {
			eventListView.setAdapter(new ArrayAdapter<Event>(getActivity(), android.R.layout.simple_list_item_1, events));
		} else {
			eventListView.setAdapter(null);
		}
	}

	private void setupSignalAdapter(ArrayList<Signal> signals) {
		// TODO 自动生成的方法存根
		if (getActivity() == null || eventListView == null) {
			return;
		}
		
		if (signals != null) {
			eventListView.setAdapter(new ArrayAdapter<Signal>(getActivity(), android.R.layout.simple_list_item_1, signals));
		} else {
			eventListView.setAdapter(null);
		}
	}
	
	@AfterView
	private void init() {
		searchEventEditText = (EditText) f(R.id.search_event_editText);
		searchSignalEditText = (EditText) f(R.id.search_signal_editText);
		
		searchEventButton = (ImageButton) f(R.id.search_event_ImageButton);
		searchEventButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				ArrayList<Event> result = new ArrayList<Event>();
				String string = searchEventEditText.getText().toString();
				for (Event e : mEvents) {
					if (e.getContent().contains(string)) {
						result.add(0, e);
					}
				}
				setupEventAdapter(result);
			}
		});
		
		searchSignalButton = (ImageButton) f(R.id.search_signal_ImageButton);
		searchSignalButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				ArrayList<Signal> result = new ArrayList<Signal>();
				String string = searchSignalEditText.getText().toString();
				for (Signal s : mSignals) {
					if (s.getContent().contains(string)) {
						result.add(0, s);
					}
				}
				setupSignalAdapter(result);
			}
		});
		
		eventListView = (ListView) f(R.id.event_listView);
		eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO 自动生成的方法存根
				
			}
		});
		eventListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> listView, View view, int position, long id) {
				// TODO 自动生成的方法存根
				Event event = (Event) listView.getAdapter().getItem(position);
				FragmentManager fm = getActivity().getSupportFragmentManager();
				LabelInfoDialogFragment dialog = LabelInfoDialogFragment.newInstance(event);
				dialog.show(fm, DIALOG_LABEL_INFO);
				/*
				 * 貌似这个通知数据更改没有生效
				 */
				((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
				return true;
			}
			
		});

		mEvents = new ArrayList<Event>();
		mEventAdapter = new EventAdapter(getActivity(), mEvents);
		eventListView.setAdapter(mEventAdapter);
		
		signalListView = (ListView) f(R.id.signal_listView);
		signalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
				// TODO 自动生成的方法存根
				Signal signal = (Signal) listView.getAdapter().getItem(position);
				Intent intent = new Intent(getActivity(), TreeMainActivity.class);
				intent.putExtra(EXTRA_SIGNAL, signal.getContent());
				startActivity(intent);
			}
		});
		mSignals = new ArrayList<Signal>();
		mSignalAdapter = new SignalAdapter(getActivity(), mSignals);
		signalListView.setAdapter(mSignalAdapter);
	}

	@Override
	protected void updateUI(RealData data) {
		// TODO Auto-generated method stub
		super.updateUI(data);
		
		getLastIE();
		
		mSignalAdapter.notifyDataSetChanged();
		mEventAdapter.notifyDataSetChanged();
	}

	private void getLastIE() {
		// TODO Auto-generated method stub
		boolean newEvent = false;
		boolean newSignal = false;
		
		String result = ProcessInterface.GetHoldLastIE();
		
		Log.i(TAG, "=============original string=============");
		Log.i(TAG, result);
		
		//以分号分隔的多条信令或事件
		String[] messages = result.split(";");
		
		for (String string : messages) {
			//Log.i(TAG, "string = " + string);
			String[] parts = string.split("\t");
			
			for (String s :parts) {
				//Log.i(TAG, "=====section=====");
				//Log.i(TAG, s);
			}
			
			for (String s : parts) {

				
				if (s.startsWith("e000")) {
					//事件
					newEvent = true;
					break;
				}
				
				if (s.startsWith("s000")) {
					//信令
					newSignal = true;
					break;
				}
			}
			
			if (newEvent || newSignal) {
				
				String lat = parts[0].substring(5);
				//Log.i(TAG, "lat = " + lat);
				String lon = parts[1].substring(5);
				//Log.i(TAG, "lon = " + lon);
				String type = parts[2].substring(5);
				//Log.i(TAG, "type = " + type);
				String content = parts[3].substring(5);
				//Log.i(TAG, "content = " + content);
				String hour = parts[4].substring(6);
				//Log.i(TAG, "hour = " + hour);
				String min = parts[5].substring(5);
				//Log.i(TAG, "min = " + min);
				String sec = parts[6].substring(5);
				//Log.i(TAG, "sec = " + sec);
				
				if (newEvent) {
					Event event = new Event();
					event.setLat(lat);
					event.setLon(lon);
					event.setType(type);
					event.setContent(content);
					event.setHour(hour);
					event.setMinute(min);
					event.setSecond(sec);
					mEvents.add(0, event);
				} else if (newSignal) {
					Signal signal = new Signal();
					signal.setLat(lat);
					signal.setLon(lon);
					signal.setType(type);
					signal.setContent(content);
					signal.setHour(hour);
					signal.setMinute(min);
					signal.setSecond(sec);
					mSignals.add(0, signal);
				} 
			}
		}
	}
}
