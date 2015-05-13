package com.datang.miou.views.gen;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
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
import android.widget.Toast;

import com.datang.miou.datastructure.Event;
import com.datang.miou.datastructure.Globals;
import com.datang.miou.datastructure.Signal;
import com.datang.miou.datastructure.TestLog;
import com.datang.miou.datastructure.TestScheme;
import com.datang.miou.ftp.FtpConfigParser;
import com.datang.miou.ftp.FtpDownThread;
import com.datang.miou.services.ResultService;
import com.datang.miou.services.TestplanService;
import com.datang.miou.test.testplanparser;
import com.datang.miou.testplan.bean.Ftp;
import com.datang.miou.testplan.ftp.TestplanFtpDown;
import com.datang.miou.testplan.ftp.TestplanFtpUpload;
import com.datang.miou.testplan.task.cases.AppThread;
import com.datang.miou.testplan.voice.TestplanVoiceCalling;
import com.datang.miou.utils.SDCardUtils;
import com.datang.miou.views.dialogs.LogPickerDialogFragment;
import com.datang.miou.views.gen.GenTelStatFragment;
import com.datang.miou.xml.PullTestPlanParser;
import com.datang.miou.FragmentSupport;
import com.datang.miou.HoldLastRecieverClient;
import com.datang.miou.MiouApp;

import com.datang.miou.ProcessInterface;
import com.datang.miou.R;
import com.datang.miou.PhoneCallListener;

/**
 * 
 * 
 * @author suntongwei
 */
public class GenActivity extends FragmentActivity implements HoldLastRecieverClient, PhoneCallListener.Callbacks,TestplanVoiceCalling.Callbacks, TestplanFtpDown.Callbacks, TestplanFtpUpload.Callbacks, LogPickerDialogFragment.Callbacks, FragmentSupport.Callbacks, GenMapFragment.Callbacks, GenSignalFragment.Callbacks{

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
	
	//	20150429 add via chenzm
	private TestplanVoiceCalling mTestplanVoiceCalling;
	private PhoneCallListener mPhoneCallListener = new PhoneCallListener();
	private TestplanFtpDown mTestplanFtpDown;
	private TestplanFtpUpload mTestplanFtpUpload;
	private Handler mHandler;
	private int mTestplansCount = 0;
	private List<TestScheme> mTestPlans = null;
	
	public enum ActivityUiAction
	{
		REFRESH(0);
		
		private final int action;
		
		ActivityUiAction(int action)
		{
			this.action = action;
		}
		
		public int getAction()
		{
			return action;
		}
	}
	//	20150429 add via chenzm end 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.gen);  
        initView();  
		
        if (((MiouApp) getApplication()).mGenActivity == null) {
        	((MiouApp) getApplication()).mGenActivity = this;
        }
        
		 //	add via chenzm
        handleTestingMessage();
        //	add via chenzm end
        
        mProcessHandler = new Handler() {
        	public void handleMessage( Message msg)
        	{
        		Signal signal = (Signal) msg.obj;
        		Log.i("update signal", "msg: " + signal.getType() + ", " + signal.getMinute() + ", " + signal.getSecond());	
        		
        		if (msg.what == MSG_NEW_SIGNAL) {
    				((MiouApp) getApplication()).getGenSignals().add((Signal) msg.obj);
    				if (mCurrentSignalFragment != null) {
    					mCurrentSignalFragment.updateSignalListView();
    				}
    			} else if (msg.what == MSG_NEW_EVENT) {
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
				// TODO 自动生成的方法存根
				
					//	add via chenzm 
				Log.i(TAG, "GenActivity mBackButton.");
								
				//	返回判断是否业务在进行
				if(isTesting())
				{
					showBackDialog();
				}
				else
				{
					backPageUp();
				}
				
				Log.i(TAG, "GenActivity mBackButton end.");
				//	end
			}
		});
		
		// 测试按钮
		mStartButton = (Button) findViewById(R.id.gen_map_start_button);
		mStartButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (((MiouApp) getApplication()).isGenTesting()) {
					stopTesting();
				} else {
					startTesting();
				}
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

	
	//	20150429 add via chenzm
	
	private void handleTestingMessage()
	{
		mHandler = new Handler()
        {
        	public void handleMessage( Message msg)
        	{
        		Log.i(TAG, "msg: " + msg.what);	
        		
        		if(msg.what == ActivityUiAction.REFRESH.getAction())
        		{
        			//	执行测试计划
            		handleTestplans(mTestPlans);
        		}
        		
        		super.handleMessage(msg);
        	}
        };
	}
	
	/** 
     * 开始业务
     */
	private void startTesting() 
	{
		// TODO 自动生成的方法存根
		
		//	判断是否有测试计划是否未完成
		if(isTesting())
		{
			Log.i(TAG, "testplan business progress.");
			return;
		}
		
		//	重置计数
		resetTestplansCount();
		
		//	开始记录log
		if (((MiouApp) getApplication()).isGenLogging()) 
		{	
			ProcessInterface.StartLogWrite();
		}
		
		//	更新界面图标
		updateUIOnTesting(true);
		((MiouApp) getApplication()).setGenTesting(true);
		
		try
		{
			//	获得最近的测试计划
			File dir = new File(SDCardUtils.getTestPlanPath());
			String latestFile = null;
			long lastTime = 0;
			for (String string : dir.list()) 
			{
				if (string.startsWith("TestScript")) 
				{
					File currentFile = new File(SDCardUtils.getTestPlanPath(), string);
					if (currentFile.lastModified() > lastTime)
					{
						lastTime = currentFile.lastModified();
						latestFile = string;
					}
				}
			}
			
			//	创建  testplan 测试类
			PullTestPlanParser parser = new PullTestPlanParser();
			
			//	获取 testplan xml 媒体流
			FileInputStream is = parser.GetStream(latestFile);
			if(is == null)
			{
				Log.i(TAG, "not find testplan xml file.");
				return ;
			}
			
			//	解析 testplan xml
			mTestPlans = parser.parse(is);
			handleTestplans(mTestPlans);
		}
		catch(Exception e)
		{
			Log.i(TAG, "startTesting Exception: " + e);
			e.printStackTrace();
			
			//	异常
			stopTesting();
		}
	}
	
	/** 
     * 停止业务
     */ 
	private void stopTesting() 
	{
		// TODO 自动生成的方法存根
		
		//	停止测试业务
		stopTestplanVoiceMaster();
		stopTestplanVoiceSlave();
		stopTestplanFtp();
		
		//	更新界面图标
		updateUIOnTesting(false);
		((MiouApp) getApplication()).setGenTesting(false);
		
		//	停止记录log
		if (((MiouApp) getApplication()).isGenLogging()) 
		{
			ProcessInterface.StopLogWrite();
		}
	}
	
	/** 
     * 查询业务	
     * 返回：true 有业务进行中；false 无业务进行中
     */ 
	private boolean isTesting() 
	{
		return (isTestplanVoiceMaster() || isTestplanVoiceSlave() || isTestplanFtpDown() || isTestplanFtpUpload());
	}
	
	/** 
     * 执行testplan
     */
	private void handleTestplans(List<TestScheme> testPlans)
	{
		if(testPlans != null)
		{
			Log.i(TAG, "testPlansCount: " + mTestplansCount + ",testPlansSize: " + testPlans.size());
			
			//	更新界面
			if(isEndForTestplans(testPlans))
			{
				//	停止业务
    			stopTesting();
			}
			
			for(int i = mTestplansCount;i < testPlans.size(); i++)
			{
				TestScheme testplan = testPlans.get(i);
				if(Integer.parseInt(testplan.getEnable()) == 1)
				{
					//	voice 主叫
					if(testplan.getCommandList().getCommand().getId().equals(Globals.TEST_COMMAND_VOICE_MASTER))
					{
						startTestplanVoiceMaster(testplan);
						mTestplansCount++;
						break;
					}
					//	voice 被叫
					else if(testplan.getCommandList().getCommand().getId().equals(Globals.TEST_COMMAND_VOICE_SLAVE))
					{
						startTestplanVoiceSlave(testplan);
						mTestplansCount++;
						break;
					}
					//	ftp
					else if(testplan.getCommandList().getCommand().getId().equals(Globals.TEST_COMMAND_FTP))
					{
						//	判断当前网络是否可用
						if(((MiouApp) getApplication()).isAvailableNet())
						{
							Log.i(TAG, "==================== fap");
							startTestplanFtp(testplan);
							mTestplansCount++;
							break;
						}
						else
						{
							Log.i(TAG, "==================== dialog");
							
							//	提示开启数据
							showWirelessSettingDialog();
						}
					}
				}
				else
				{
					Log.i(TAG, "disable testplan id: " + testplan.getCommandList().getCommand().getId());
				}
				
				//	更新当前计数
				mTestplansCount++;
				
				//	更新界面
				if(isEndForTestplans(testPlans))
				{
					//	停止业务
	    			stopTesting();
				}
			}
			
			//	打印
			Log.i(TAG, "isEndForTestplans testPlansCount: " + mTestplansCount + ",testPlansSize: " + testPlans.size());
		}
		else
		{
			Log.i(TAG, "testPlans is null");
		}
		
		return;
	}
	
	/** 
     * testplan是否结束
     */
	private boolean isEndForTestplans(List<TestScheme> testPlans)
	{
		if(testPlans != null)
		{
			if(mTestplansCount >= testPlans.size())
			{
				return true;
			}
		}
		else
		{
			Log.i(TAG, "testPlans is null");
		}
		
		return false;
	}
	
	/** 
     * 重置统计
     */
	private void resetTestplansCount()
	{
		mTestplansCount = 0;
	}
	
	/** 
     * 更新界面
     */
	@Override
	public void updateUIOnFinnished()
	{
		// TODO Auto-generated method stub
		Log.i(TAG, "updateUIOnVoiceFinnished");
		
		Message msg = new Message();
		msg.what = ActivityUiAction.REFRESH.getAction();
		mHandler.sendMessage(msg);
	}
	
	/** 
     * voice主叫业务
     */ 
	private void startTestplanVoiceMaster(TestScheme testplan) 
	{
		Log.i(TAG, "start voice calling testplan.");
		try
		{
			mTestplanVoiceCalling = new TestplanVoiceCalling(this,testplan);	
			mTestplanVoiceCalling.startVoiceCalling();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void stopTestplanVoiceMaster() 
	{
		if(mTestplanVoiceCalling != null)
		{
			mTestplanVoiceCalling.stopVoiceCalling();
			mTestplanVoiceCalling = null;
		}
	}
	
	private boolean isTestplanVoiceMaster() 
	{
		if(mTestplanVoiceCalling != null)
		{
			return mTestplanVoiceCalling.isVoiceCalling();
		}
		return false;
	}
	
	/** 
     * voice被叫业务
     */ 
	private void startTestplanVoiceSlave(TestScheme testplan)
	{
		Log.i(TAG, "startTestplanVoiceSlave.");
		int repeatNum = Integer.parseInt(testplan.getCommandList().getCommand().getRepeat());
		
		mPhoneCallListener.register(this);
		mPhoneCallListener.start(repeatNum);
		
		TestplanService.sendVoiceSlaveBroadcast(true,repeatNum);
	}
	
	private void stopTestplanVoiceSlave() 
	{
		Log.i(TAG, "stopTestplanVoiceSlave.");
		mPhoneCallListener.stop();
		mPhoneCallListener.unregister();
		TestplanService.sendVoiceSlaveBroadcast(false,0);
	}
	
	private boolean isTestplanVoiceSlave() 
	{
		return TestplanService.getVoiceSlaveState();
	}
	
	/** 
     * ftp业务
     */ 
	private void startTestplanFtp(TestScheme testplan) 
	{
		Log.i(TAG, "start ftp testing.");
		 
		//	设置网络模式
		Globals.setNetMode(Globals.MODE_LTE);
		
		try
		{
			//	获取Ftp业务进程数
			FtpConfigParser configparser = new FtpConfigParser();
			configparser.parse();
			
			int threadnum = Integer.parseInt(configparser.getThreadNum());
			
			//	启动 ftp 线程
			if(testplan.getCommandList().getCommand().getDownload().equals("1"))	//	test test
			{
				Log.i(TAG, "======= DOWN ========");
				mTestplanFtpDown = new TestplanFtpDown(this,testplan,threadnum);	
				Log.i(TAG, "======= DOWN WAIT ========");
				mTestplanFtpDown.StartFtpDown();
				Log.i(TAG, "======= DOWN END ========");
			}
			else
			{
				Log.i(TAG, "======= UPLOAD ========");
				mTestplanFtpUpload = new TestplanFtpUpload(this,testplan,threadnum);	//	dt dt	
				Log.i(TAG, "======= UPLOAD WAIT ========");
				mTestplanFtpUpload.StartFtpUpload();
				Log.i(TAG, "======= UPLOAD END ========");
			}
		}
		catch(Exception e)
		{
			Log.i(TAG, "startTestplanFtp Exception: " + e);
		}
		
		Log.i(TAG, "start ftp testing end.");
	}
	
	private void stopTestplanFtp() 
	{
		//	下载
		if(mTestplanFtpDown != null)
		{
			mTestplanFtpDown.StopFtpDown();
			mTestplanFtpDown = null;
		}
		
		//	上传
		if(mTestplanFtpUpload != null)
		{
			mTestplanFtpUpload.StopFtpUpload();
			mTestplanFtpUpload = null;
		}
	}
	
	private boolean isTestplanFtpUpload() 
	{
		if(mTestplanFtpUpload != null)
		{
			return mTestplanFtpUpload.isFtpUpload();
		}
		
		return false;
	}
	
	private boolean isTestplanFtpDown() 
	{
		if(mTestplanFtpDown != null)
		{
			return mTestplanFtpDown.isFtpDown();
		}
		
		return false;
	}
	
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
			if(isTesting())
			{
				//	返回判断是否业务在进行
				Log.i(TAG, "GenActivity KEYCODE_BACK.");
				showBackDialog();
				Log.i(TAG, "GenActivity KEYCODE_BACK end.");
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
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
				Log.i(TAG, "showBackDialog.");
				
				//	停止业务
				stopTesting();
				
				//	返回上一级
				backPageUp();
				
				Log.i(TAG, "showBackDialog end.");
			}
		});
		builder.setNegativeButton("取消", null);
		builder.create().show();
	}
	
	 /** 
     * 返回提示对话框
     */ 
	private void showWirelessSettingDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("提示:");
		builder.setMessage("网络不可用，请至 设定-更多设置-移动网络 ，打开移动数据开关");
		builder.setPositiveButton("确认", new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog,int which)
			{
				Log.i(TAG, "showWirelessSettingDialog.");
			}
		});
		builder.create().show();
	}
	
	//	20150429 add via chenzm end

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
}
