package com.datang.miou;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.datang.miou.annotation.AfterView;
import com.datang.miou.annotation.AutoView;
import com.datang.miou.annotation.Extra;
import com.datang.miou.datastructure.Event;
import com.datang.miou.datastructure.RealData;
import com.datang.miou.datastructure.Signal;
import com.datang.miou.services.ResultService;

/**
 * 
 * 
 * @author suntongwei
 */
public class FragmentSupport extends Fragment {

	protected static final String TAG = "FragmentSupport";
	
	private static final String FIELD_EVENT_TYPE = "e000";
	private static final String FIELD_EVENT_CONTENT = "e010";
	private static final String FIELD_SIGNAL_TYPE = "s000";
	private static final String FIELD_SIGNAL_CONTENT = "s010";
	private static final String FIELD_LATITUDE = "glat";
	private static final String FIELD_LONGITUDE = "glon";
	private static final String FIELD_HOUR = "thour";
	private static final String FIELD_MINUTE = "tmin";
	private static final String FIELD_SECOND = "tsec";
	
	// 装载视图
	protected View mView;
	// Context
	protected Context mContext;
	// Intent
	protected Intent mIntent;

	public interface Callbacks {
		public void addDataReceiver(BroadcastReceiver r);
		public void removeDataReceiver(BroadcastReceiver r);
		public void startReceivingData();
		public void stopReceivingData();
	}
	private Callbacks mCb;
	
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mCb = (Callbacks) activity;
	}


	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		mCb = null;
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}


	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		// 自动装载视图
		AutoView autoView = getClass().getAnnotation(AutoView.class);
		if (autoView != null) {
			mView = inflater.inflate(autoView.value(), container, false);
			mContext = mView.getContext();
		}

		// 获取该类所有属性
		Field[] fields = getClass().getDeclaredFields();
		if (fields != null && fields.length > 0) {
			// 自动加载界面控件引用
			for (Field field : fields) {
				AutoView av = field.getAnnotation(AutoView.class);
				if (av != null) {
					try {
						if (av.value() != Integer.MIN_VALUE) {
							field.setAccessible(true);
							field.set(this, f(av.value()));
						} else {
							field.set(this,mContext.getResources().getIdentifier(field.getName(), "id",mContext.getPackageName()));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					Extra extra = field.getAnnotation(Extra.class);
					if (extra != null) {
						String val = extra.value();
						if ("".equals(val)) {
							val = field.getName();
						}
						if (getIntent().hasExtra(val)) {
							field.setAccessible(true);
							try {
								field.set(this, getIntent().getExtras().get(val));
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {
							if (extra.must()) {
								getActivity().finish();
							}
						}
					}
				}
			}
		}

		// 执行@AfterView的方法
		Method[] methods = getClass().getDeclaredMethods();
		for (Method method : methods) {
			AfterView afterView = method.getAnnotation(AfterView.class);
			if (afterView != null) {
				method.setAccessible(true);
				try {
					method.invoke(this, new Object[] {});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return mView;
	}
	
	
	protected View f(int id) {
		return mView.findViewById(id);
	}
	
	protected void setIntent(Intent intent) {
		mIntent = intent;
	}
	
	protected Intent getIntent() {
		return mIntent;
	}
	
	/*
	 * 这个receiver用来接收实时数据，更新各继承FragmentSupport的Fragment
	 */
	public BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO 自动生成的方法存根
			RealData data = (RealData) intent.getExtras().getSerializable(ResultService.EXTRA_REAL_DATA);
			updateUI(data);
		}	
	};
	
	protected void updateUI(RealData data) {
		// TODO 自动生成的方法存根
		//Log.i(TAG, "update ui in Fragment Support");
	}
	
	@Override
	public void onPause() {
		// TODO 自动生成的方法存根
		super.onPause();
		//这里不能把mReceiver设置为null，否则onResume的时候注册的就是null
		//屏幕暗了在点亮就收不到广播了
		mCb.removeDataReceiver(mReceiver);
	}

	@Override
	public void onResume() {
		// TODO 自动生成的方法存根
		super.onResume();
		mCb.addDataReceiver(mReceiver);
	}
	
	/*
	 * 获得信令
	 */
	/*
	protected Signal parseSignal(Map<String, String> mapIDValue) {
		Log.i(TAG,mapIDValue.get("thour")+":"+mapIDValue.get("tmin")+":"+mapIDValue.get("tsec"));
		Log.i(TAG,mapIDValue.get("s020")+"--"+mapIDValue.get("s030")+"--"+mapIDValue.get("s040"));
		Log.i(TAG,mapIDValue.get("s000")+"--"+mapIDValue.get("s010"));
		
		Signal signal = new Signal();
		signal.setLat(mapIDValue.get(FIELD_LATITUDE));
		signal.setLon(mapIDValue.get(FIELD_LONGITUDE));
		signal.setType(mapIDValue.get(FIELD_SIGNAL_TYPE));
		signal.setContent(mapIDValue.get(FIELD_SIGNAL_CONTENT));
		signal.setHour(mapIDValue.get(FIELD_HOUR));
		signal.setMinute(mapIDValue.get(FIELD_MINUTE));
		signal.setSecond(mapIDValue.get(FIELD_SECOND));
		
		return signal;
	}
	*/
	
	/*
	 * 获得事件
	 */
	/*
	protected Event parseEvent(Map<String, String> mapIDValue) {
		Log.i(TAG, mapIDValue.get("thour")+":"+mapIDValue.get("tmin")+":"+mapIDValue.get("tsec"));
		Log.i(TAG, mapIDValue.get("e000")+"--"+mapIDValue.get("e010"));
		
		Event event = new Event();
		event.setLat(mapIDValue.get(FIELD_LATITUDE));
		event.setLon(mapIDValue.get(FIELD_LONGITUDE));
		event.setType("0x" + mapIDValue.get(FIELD_EVENT_TYPE));
		event.setContent(mapIDValue.get(FIELD_EVENT_CONTENT));
		event.setHour(mapIDValue.get(FIELD_HOUR));
		event.setMinute(mapIDValue.get(FIELD_MINUTE));
		event.setSecond(mapIDValue.get(FIELD_SECOND));
		
		return event;
	}
	*/
}
