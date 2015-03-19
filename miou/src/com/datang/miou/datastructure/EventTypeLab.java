package com.datang.miou.datastructure;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;
import android.util.Log;

public class EventTypeLab {
	
	private static final String FILENAME = "event_types.json";
	private static final String TAG = "EventTypeLab";
	private static EventTypeLab sEventTypeLab;
	private Context mAppContext;
	private ArrayList<EventType> mEventTypes;
	
	private EventTypeJSONSerializer mSerializer;
	
	private EventTypeLab(Context context) {
		mAppContext = context;
		mEventTypes = new ArrayList<EventType>();
		mSerializer = new EventTypeJSONSerializer(mAppContext, FILENAME);
		
		try {
			mEventTypes = mSerializer.loadEventTypes();
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			mEventTypes = new ArrayList<EventType>();
			Log.e(TAG, "error loading crimes");
		} 
	}
	
	public static EventTypeLab get(Context c) {
		if (sEventTypeLab == null) {
			sEventTypeLab = new EventTypeLab(c.getApplicationContext());
		}
		return sEventTypeLab;
	}
	
	public ArrayList<EventType> getEventTypes() {
		return this.mEventTypes;
	}
	
	public EventType getEventType(UUID id) {
		for (EventType type : mEventTypes) {
			if (type.getId().equals(id)) {
				return type;
			}
		}
		return null;
	}
	
	public boolean saveEventTypes()	{
		try {
			mSerializer.saveEventTypes(this.mEventTypes);
			Log.d(TAG, "event types saved to file");
			return true;
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			Log.e(TAG, "error saving event types");
			return false;
		} 
	}
}
