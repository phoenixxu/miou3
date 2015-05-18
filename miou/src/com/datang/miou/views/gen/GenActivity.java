package com.datang.miou.views.gen;

import java.util.Map;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabWidget;
import android.widget.TextView;
import com.datang.miou.datastructure.Event;
import com.datang.miou.datastructure.Signal;
import com.datang.miou.datastructure.TestLog;
import com.datang.miou.services.ResultService;
import com.datang.miou.testplan.task.cases.AppThread;
import com.datang.miou.views.dialogs.LogPickerDialogFragment;
import com.datang.miou.views.gen.GenTelStatFragment;
import com.datang.miou.FragmentSupport;
import com.datang.miou.HoldLastRecieverClient;
import com.datang.miou.MiouApp;
import com.datang.miou.ProcessInterface;
import com.datang.miou.R;

/**
 * 
 * 
 * @author suntongwei
 */
public class GenActivity extends FragmentActivity implements HoldLastRecieverClient, LogPickerDialogFragment.Callbacks, FragmentSupport.Callbacks, GenMapFragment.Callbacks, GenSignalFragment.Callbacks{

	private static final String DIALOG_LOG_PICKER = "dialog_log_picker";

	private static final String TAG = "GenActivity";
	//private static final String TAG = "chenzm";

	protected static final int MSG_NEW_SIGNAL = 996;
	protected static final int MSG_NEW_EVENT = 998;

	private static final String FIELD_EVENT_TYPE = "e000";
	private static final String FIELD_EVENT_CONTENT = "e010";
	private static final String FIELD_SIGNAL_TYPE = "s000";
	private static final String FIELD_SIGNAL_CONTENT = "s010";
	private static final String FIELD_LATITUDE = "glat";
	private static final String FIELD_LONGITUDE = "glon";
	private static final String FIELD_HOUR = "thour";
	private static final String FIELD_MINUTE = "tmin";
	private static final String FIELD_SECOND = "tsec";
	
    public FragmentTabHost mTabHost;  
    private LayoutInflater layoutInflater;  
    private Class<?> fragmentArray[] = {GenMapFragment.class, GenParamsFragment.class, GenSignalFragment.class, GenTelStatFragment.class};  
    private String mTextviewArray[] = {"地图", "指标", "信令", "话务统计"};

	private TextView mTitleTextView;
	private TextView mScriptButton;
	private ImageView mBackButton;
	private Button mStartButton;
	private Button mReviewButton;
	private Button mLogButton;  
	private GenMapFragment mCurrentMapFragment;

	private AppThread mThread;

	private Handler mProcessHandler;

	private GenSignalFragment mCurrentSignalFragment;
	
	//	add via chenzm
	private MyGenActivityReceiver mGenActivityReveicer = null;
	//	add via chenzm end

	
	@Override
	public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.gen);  
        initView();  
		
        if (((MiouApp) getApplication()).mGenActivity == null) {
        	((MiouApp) getApplication()).mGenActivity = this;
        }
        
        mProcessHandler = new Handler() {
        	public void handleMessage( Message msg)
        	{
        		if (msg.what == MSG_NEW_SIGNAL) 
        		{
    				((MiouApp) getApplication()).getGenSignals().add((Signal) msg.obj);
    				if (mCurrentSignalFragment != null) {
    					mCurrentSignalFragment.updateSignalListView();
    				}
    			}
        		else if (msg.what == MSG_NEW_EVENT)
        		{
    				((MiouApp) getApplication()).getGenEvents().add((Event) msg.obj);
    				if (mCurrentSignalFragment != null) {
    					mCurrentSignalFragment.updateEventListView();
    				}
    			}
        		super.handleMessage(msg);
        	}
    	};
    	
        // 后台数据线程只能启动一次
 		if (!((MiouApp) getApplication()).isHoldLastServerRunning()) {
 	        ProcessInterface.mHoldLastServer.StartThread();
 	        ((MiouApp) getApplication()).setHoldLastServerRunning(true);
 	        ProcessInterface.mHoldLastServer.RegisterClient(this);
 		} 
 		
 		// 		add via chenzm
 		//	注册广播接收器
 		mGenActivityReveicer = new MyGenActivityReceiver(this);
 		IntentFilter filter = new IntentFilter();
		filter.addAction("com.datang.miou.views.gen.action.GENACTIVITY_ACTION");
		registerReceiver(mGenActivityReveicer,filter);
 		//	add via chenzm end
    }  
       
    @Override
	protected void onDestroy() {
		super.onDestroy();
		
		if (((MiouApp) getApplication()).isGenReviewing()) {
			ProcessInterface.StopLogRead();
		}	
	}

    private void initView(){  
 
        layoutInflater = LayoutInflater.from(this);  
 
        // 标签页
        mTabHost = (FragmentTabHost)findViewById(R.id.gen_tabhost);  
        mTabHost.setup(this, getSupportFragmentManager(), R.id.gen_realtabcontent);   
        int count = fragmentArray.length;     
        for(int i = 0; i < count; i++){    

            TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i]).setIndicator(getTabItemView(i));  
            mTabHost.addTab(tabSpec, fragmentArray[i], null);  
        }  
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				TabWidget widget = mTabHost.getTabWidget();
				for (int i = 0; i < widget.getChildCount(); i++) {
					TextView textView = (TextView) widget.getChildAt(i);
					if (mTextviewArray[i].equals(tabId)) {
						textView.setBackgroundResource(R.color.title_blue);
						textView.setTextColor(getResources().getColor(R.color.white));
					} else {
						textView.setBackgroundResource(R.color.menu_gray);
						textView.setTextColor(getResources().getColor(R.color.black));
					}
				}
			}
		});
        
        // 标题栏-标题
        mTitleTextView = (TextView) findViewById(R.id.app_title_value);
		mTitleTextView.setText(R.string.gen_title);
		
		// 标题栏-脚本按钮
		mScriptButton = (TextView) findViewById(R.id.app_title_right_txt);
		mScriptButton.setText(R.string.gen_map_title_script_button);
		mScriptButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(getApplicationContext(), GenScriptSettingActivity.class);
				startActivity(intent);
			}
		});
		
		// 标题栏-后退按钮
		mBackButton = (ImageView) findViewById(R.id.app_title_left);
		mBackButton.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View view)
			{
				//	add via chenzm
				if(((MiouApp) getApplication()).isGenTesting())
				{
					//	返回判断是否业务在进行
					showBackDialog();
				}
				else
				{
					backPageUp();
				}
				//	add via chenzm end
			}
		});
		
		// 测试按钮
		mStartButton = (Button) findViewById(R.id.gen_map_start_button);
		mStartButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) 
			{
				//	add via chenzm 
				if (((MiouApp) getApplication()).isGenTesting())
				{
					sendTestplanBroadcast(false);
				}
				else 
				{
					sendTestplanBroadcast(true);
				}
				//	add via chenzm end
			}
		});
		
		// 回放按钮
		mReviewButton = (Button) findViewById(R.id.gen_map_review_button);
		mReviewButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (((MiouApp) getApplication()).isGenReviewing()) {			
					stopReviewing();
				} else {
					startReviewing();
				}

			}
		});
		
		// 日志按钮
		mLogButton = (Button) findViewById(R.id.gen_map_log_button);
		updateUIOnLoging(((MiouApp) getApplication()).isGenLogging());
		mLogButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (((MiouApp) getApplication()).isGenLogging()) {
					updateUIOnLoging(false);
					((MiouApp) getApplication()).setGenLogging(false);
				} else {
					updateUIOnLoging(true);
					((MiouApp) getApplication()).setGenLogging(true);
					
				}
			}	
		});
		
		// 根据测试或回放标志更新UI
		updateUIOnTesting(((MiouApp) getApplication()).isGenTesting());
		updateUIOnReviewing(((MiouApp) getApplication()).isGenReviewing());
		
		// 如果在回放,则显示地图控制面板
		if (((MiouApp) getApplication()).isGenReviewing()) {
			if (mCurrentMapFragment != null) {
				((GenMapFragment) mCurrentMapFragment).showMapController();
			}
		}
    }  
         
    /*
     * 给Tab按钮设置图标和文字 
     */  
    @SuppressLint("InflateParams")
	private View getTabItemView(int index) {  
        TextView view = (TextView) layoutInflater.inflate(R.layout.gen_menu_item, null, false);
        view.setText(mTextviewArray[index]);
        if(index == 0) {
        	view.setBackgroundResource(R.color.title_blue);
        	view.setTextColor(getResources().getColor(R.color.white));
        }
        return view;  
    }
    
    /*
     * 更新日志按钮
     */
    private void updateUIOnLoging(boolean log) {
		if (log) {
			mLogButton.setBackgroundResource(R.drawable.switch_on);
		} else {
			mLogButton.setBackgroundResource(R.drawable.switch_off);
		}
	}
    
    /*
     * 更新测试按钮
     */
	private void updateUIOnTesting(boolean test) {
		if (test) {
			mStartButton.setText(R.string.gen_map_stop);
			mReviewButton.setEnabled(false);
			mLogButton.setEnabled(false);
		} else {
			mStartButton.setText(R.string.gen_map_start);
			mReviewButton.setEnabled(true);
			mLogButton.setEnabled(true);
		}
	}
	
	/*
	 * 更新回放按钮
	 */
	private void updateUIOnReviewing(boolean review) {
		if (review) {
			mReviewButton.setText(R.string.gen_map_stop);
			mStartButton.setEnabled(false);
			mLogButton.setEnabled(false);
		} else {
			mReviewButton.setText(R.string.gen_map_review);
			mStartButton.setEnabled(true);
			mLogButton.setEnabled(true);
		}
	}
	
	/*
	 * 停止回放
	 */
	protected void stopReviewing() {
		// 更新UI
		updateUIOnReviewing(false);
		// 清除回放标志
		((MiouApp) getApplication()).setGenReviewing(false);
		// 隐藏地图控制面板
		((GenMapFragment) mCurrentMapFragment).hideMapController();
		// 清除全局变量
		clearGlobals();
		// 停止读取日志, 还需要处理其他情况
		ProcessInterface.StopLogRead();
		
		//这里需要一段小延时 否则会崩溃
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 开始实时数据读取
		// 切换会出问题 暂时注释掉
		ProcessInterface.StartHdConnect();
		//Log.i(TAG, "开始实时数据读取");
	}

	/*
	 * 清除全局变量,以便开始一次新的测试或回放
	 */
	private void clearGlobals() {
		((MiouApp) getApplication()).getGenEvents().clear();
		((MiouApp) getApplication()).getGenSignals().clear();
		
		((MiouApp) getApplication()).getColorPointsListForCoverLTE().clear();
		((MiouApp) getApplication()).getColorPointsListForCoverTD().clear();
		((MiouApp) getApplication()).getColorPointsListForCoverGSM().clear();
		
		((MiouApp) getApplication()).getColorPointsListForQualityLTE().clear();
		((MiouApp) getApplication()).getColorPointsListForQualityTD().clear();
		((MiouApp) getApplication()).getColorPointsListForQualityGSM().clear();
		
		((MiouApp) getApplication()).getColorPointsListForSpeedUp().clear();
		((MiouApp) getApplication()).getColorPointsListForSpeedDown().clear();
		
		((MiouApp) getApplication()).getTracePoints().clear();
		((MiouApp) getApplication()).setLastPoint(null);
	}

	/*
	 * 开始回放,选择LOG的对话框
	 */
	private void startReviewing() {
		FragmentManager fm = getSupportFragmentManager();
		LogPickerDialogFragment dialog = LogPickerDialogFragment.newInstance(this);
		dialog.show(fm, DIALOG_LOG_PICKER);
	}

	
	

    /*
     * 只有在选中日志后才会真正的回放，所以对数据或UI的改变都放在这里
     */
	@Override
	public void refreshActivity(TestLog log) {
		// 清空当前的信令和事件列表
		((MiouApp) getApplication()).getGenEvents().clear();
		((MiouApp) getApplication()).getGenSignals().clear();
		
		// 显示地图控制面板
		((GenMapFragment) mCurrentMapFragment).showMapController();
		
		// 更新UI
		updateUIOnReviewing(true);
		
		// 设置回放标志
		((MiouApp) getApplication()).setGenReviewing(true);
		
		// 结束实时数据读取
		ProcessInterface.StopHdConnect();
		
		// 开始读取日志
		// 切换有问题 暂时注释掉
		String path = log.getPath();
		ProcessInterface.StartLogRead(path);
	}

	@Override
	public void setMapFragment(Fragment fragment) {
		mCurrentMapFragment = (GenMapFragment) fragment;
		if (((MiouApp) getApplication()).isGenReviewing()) {
			mCurrentMapFragment.showMapController();
		}
	}

	@Override
	public void setSignalFragment(Fragment fragment) {
		// TODO Auto-generated method stub
		//if (((GenActivity) (((MiouApp) getApplication()).mGenActivity)).mCurrentSignalFragment == null) {
		//	mCurrentSignalFragment = (GenSignalFragment) fragment;
		//} else {
			//mCurrentSignalFragment = ((GenActivity) (((MiouApp) getApplication()).mGenActivity)).mCurrentSignalFragment;
		
		// 用全局变量来保存第一次Create的Activity,因为数据更新的后台线程获取了这个对象并且没有释放
		((GenActivity) (((MiouApp) getApplication()).mGenActivity)).mCurrentSignalFragment = (GenSignalFragment) fragment;
		//}
	}
	
	@Override
	public void addDataReceiver(BroadcastReceiver r) {
		IntentFilter filter = new IntentFilter(ResultService.ACTION_SHOW_NOTIFICATION);
		registerReceiver(r, filter);
	}

	@Override
	public void removeDataReceiver(BroadcastReceiver r) {
		unregisterReceiver(r);
	}

	@Override
	public void startReceivingData() {
	}
	
	@Override
	public void stopReceivingData() {
	}

	@Override
	public void updateUIOnReviewFinished() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ProcessData(Map<String, String> mapIDValue) {
		// TODO Auto-generated method stub
		//Log.i(TAG, "mapIDValue = " + mapIDValue);
		if (mapIDValue.containsKey("s000")){
			//Log.i(TAG,mapIDValue.get("thour")+":"+mapIDValue.get("tmin")+":"+mapIDValue.get("tsec"));
			//Log.i(TAG,mapIDValue.get("s020")+"--"+mapIDValue.get("s030")+"--"+mapIDValue.get("s040"));
			//Log.i(TAG,mapIDValue.get("s000")+"--"+mapIDValue.get("s010"));
			//Log.i(TAG, "lat = " + mapIDValue.get("glat") + ", lon = " + mapIDValue.get("glon"));
			Signal signal = new Signal();
			signal.setLat(mapIDValue.get(FIELD_LATITUDE));
			signal.setLon(mapIDValue.get(FIELD_LONGITUDE));
			signal.setType(mapIDValue.get(FIELD_SIGNAL_TYPE));
			signal.setContent(mapIDValue.get(FIELD_SIGNAL_CONTENT));
			signal.setTime(mapIDValue.get(FIELD_HOUR), mapIDValue.get(FIELD_MINUTE), mapIDValue.get(FIELD_SECOND));

			//Log.i(TAG, "lat = " + signal.getLat() + ", lon = " + signal.getLon());

			
			//((MiouApp) getApplication()).getGenSignals().add(0, signal);
			mProcessHandler.obtainMessage(MSG_NEW_SIGNAL, signal).sendToTarget();
		} 
		
		if (mapIDValue.containsKey("e000")){
			//Log.i(TAG, mapIDValue.get("thour")+":"+mapIDValue.get("tmin")+":"+mapIDValue.get("tsec"));
			//Log.i(TAG, mapIDValue.get("e000")+"--"+mapIDValue.get("e010"));
			
			Event event = new Event();
			event.setLat(mapIDValue.get(FIELD_LATITUDE));
			event.setLon(mapIDValue.get(FIELD_LONGITUDE));
			event.setType("0x" + mapIDValue.get(FIELD_EVENT_TYPE));
			event.setContent(mapIDValue.get(FIELD_EVENT_CONTENT));
			event.setTime(mapIDValue.get(FIELD_HOUR), mapIDValue.get(FIELD_MINUTE), mapIDValue.get(FIELD_SECOND));
			//((MiouApp) getApplication()).getGenEvents().add(0, event);
			mProcessHandler.obtainMessage(MSG_NEW_EVENT, event).sendToTarget();
		} 
	}
	
	//	add via chenzm
	
	/** 
     * 返回上一页
     */
	private void backPageUp()
	{
		try 
		{
			if (NavUtils.getParentActivityName((Activity) getApplicationContext()) != null)
			{
				NavUtils.navigateUpFromSameTask((Activity) getApplicationContext());
			}
		}
		catch (Exception e)
		{
			finish();
		}
	}
	
	/** 
     * 复写返回键方法
     */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			if(((MiouApp) getApplication()).isGenTesting())
			{
				//	返回判断是否业务在进行
				showBackDialog();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * 发送类型广播
	 * 功能：启动或关闭测试计划
	 */
	private void sendTestplanBroadcast(boolean isOn)
	{
		// 实例化Intent对象
        Intent intent = new Intent();
        
        // 设置Intent action属性
        intent.setAction("com.datang.miou.views.gen.action.TESTPLAN_ACTION");
        
        // 为Intent添加附加信息
        intent.putExtra("enable", isOn);
        
        // 发出广播
        this.sendBroadcast(intent);
	}
	
	/**
	 * 定义一个广播接收器
	 * 功能：更新界面
	 */
	public class MyGenActivityReceiver extends BroadcastReceiver 
	{
		Context mCxt = null;
		
		public MyGenActivityReceiver(Context cxt)
		{
			mCxt = cxt;
		}
		
		@Override
		public void onReceive(Context context, Intent intent)
		{
			//	可用 intent 的 getAction() 区分接收到的不同广播
	        if(intent.getAction().equals("com.datang.miou.views.gen.action.GENACTIVITY_ACTION"))
	        {
	        	Log.i(TAG, "GENACTIVITY_ACTION: " + intent.getBooleanExtra("enable",false));
	        	//	图标更新
	        	if(intent.getBooleanExtra("enable",false))
	        	{
					updateUIOnTesting(false);
	        	}
	        	else
	        	{
					updateUIOnTesting(true);
	        	}
	        }
		}
	}
	
	 /** 
     * 返回对话框
     */ 
	private void showBackDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("提示:");
		builder.setMessage("业务正在进行中，退出将停止业务?");
		builder.setPositiveButton("确认", new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog,int which)
			{
				//	停止业务
				sendTestplanBroadcast(false);
				
				//	返回上一级
				backPageUp();
			}
		});
		builder.setNegativeButton("取消", null);
		builder.create().show();
	}
	//	add via chenzm end
}
