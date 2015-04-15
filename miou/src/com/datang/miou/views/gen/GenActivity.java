package com.datang.miou.views.gen;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabWidget;
import android.widget.TextView;
import com.datang.miou.datastructure.Globals;
import com.datang.miou.datastructure.TestLog;
import com.datang.miou.ftp.FtpConfigParser;
import com.datang.miou.ftp.FtpDownThread;
import com.datang.miou.services.ResultService;
import com.datang.miou.test.testplanparser;
import com.datang.miou.testplan.bean.Ftp;
import com.datang.miou.testplan.task.cases.AppThread;
import com.datang.miou.views.dialogs.LogPickerDialogFragment;
import com.datang.miou.views.gen.GenTelStatFragment;
import com.datang.miou.FragmentSupport;
import com.datang.miou.MiouApp;

import com.datang.miou.ProcessInterface;
import com.datang.miou.R;

/**
 * 
 * 
 * @author suntongwei
 */
public class GenActivity extends FragmentActivity implements LogPickerDialogFragment.Callbacks, FragmentSupport.Callbacks, GenMapFragment.Callbacks{

	private static final String DIALOG_LOG_PICKER = "dialog_log_picker";

	//定义FragmentTabHost对象  
    public FragmentTabHost mTabHost;  
      
    //定义一个布局  
    private LayoutInflater layoutInflater;  
          
    //定义数组来存放Fragment界面  
    private Class<?> fragmentArray[] = {GenMapFragment.class, GenParamsFragment.class, 
    		GenSignalFragment.class, GenTelStatFragment.class};  
      
    //Tab选项卡的文字  
    private String mTextviewArray[] = {"地图", "指标", "信令", "话务统计"};

	private TextView mTitleTextView;

	private TextView mScriptButton;

	private ImageView mBackButton;

	private Button mStartButton;

	private Button mReviewButton;

	private Button mLogButton;


	protected boolean isLogging = false;
    
	private Fragment mCurrentMapFragment;


	public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.gen);  
        initView();  
    }  
       
    /**
     * 初始化组件 
     */  
    private void initView(){  
        //实例化布局对象  
        layoutInflater = LayoutInflater.from(this);  
        //实例化TabHost对象，得到TabHost  
        mTabHost = (FragmentTabHost)findViewById(R.id.gen_tabhost);  
        mTabHost.setup(this, getSupportFragmentManager(), R.id.gen_realtabcontent);   
        //得到fragment的个数  
        int count = fragmentArray.length;     
        for(int i = 0; i < count; i++){    
            //为每一个Tab按钮设置图标、文字和内容  
            TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i]).setIndicator(getTabItemView(i));  
            //将Tab按钮添加进Tab选项卡中  
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
        
        mTitleTextView = (TextView) findViewById(R.id.app_title_value);
		mTitleTextView.setText(R.string.gen_title);
		
		mScriptButton = (TextView) findViewById(R.id.app_title_right_txt);
		mScriptButton.setText(R.string.gen_map_title_script_button);
		mScriptButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO 自动生成的方法存根
				Intent intent = new Intent(getApplicationContext(), GenScriptSettingActivity.class);
				startActivity(intent);
			}
		});
		
		mBackButton = (ImageView) findViewById(R.id.app_title_left);
		mBackButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO 自动生成的方法存根
				try {
					if (NavUtils.getParentActivityName((Activity) getApplicationContext()) != null) {
						NavUtils.navigateUpFromSameTask((Activity) getApplicationContext());
					}
				} catch (Exception e) {
					finish();
				}		
			}
		});
		
		//开始测试
		mStartButton = (Button) findViewById(R.id.gen_map_start_button);
		mStartButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (((MiouApp) getApplication()).isGenTesting()) {
					updateUIOnTesting(false);
					((MiouApp) getApplication()).setGenTesting(false);
					stopTesting();
				} else {
					updateUIOnTesting(true);
					((MiouApp) getApplication()).setGenTesting(true);
					startTesting();
				}
			}
		});
		
		mReviewButton = (Button) findViewById(R.id.gen_map_review_button);
		mReviewButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (((MiouApp) getApplication()).isGenReviewing()) {			
					updateUIOnReviewing(false);
					((MiouApp) getApplication()).setGenReviewing(false);
					stopReviewing();
				} else {
					startReviewing();
				}

			}
		});
		
		mLogButton = (Button) findViewById(R.id.gen_map_log_button);
		mLogButton.setBackgroundResource(R.drawable.switch_off);
		mLogButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//isLogging = mLogButton.isChecked();
				if (isLogging) {
					isLogging = false;
					mLogButton.setBackgroundResource(R.drawable.switch_off);
				} else {
					isLogging = true;
					mLogButton.setBackgroundResource(R.drawable.switch_on);
				}
			}
		});
		
		updateUIOnTesting(((MiouApp) getApplication()).isGenTesting());
		updateUIOnReviewing(((MiouApp) getApplication()).isGenReviewing());
		if (((MiouApp) getApplication()).isGenReviewing()) {
			if (mCurrentMapFragment != null) {
				((GenMapFragment) mCurrentMapFragment).showMapController();
			}
		}
    }  
         
	private void updateUIOnTesting(boolean test) {
		// TODO Auto-generated method stub
		if (test) {
			mStartButton.setText(R.string.gen_map_stop);
			mReviewButton.setEnabled(false);
		} else {
			mStartButton.setText(R.string.gen_map_start);
			mReviewButton.setEnabled(true);
		}
	}
	
	private void updateUIOnReviewing(boolean review) {
		// TODO Auto-generated method stub
		if (review) {
			mReviewButton.setText(R.string.gen_map_stop);
			mStartButton.setEnabled(false);
		} else {
			mReviewButton.setText(R.string.gen_map_review);
			mStartButton.setEnabled(true);
		}
	}
	
	protected void stopReviewing() {
		// TODO Auto-generated method stub
		((GenMapFragment) mCurrentMapFragment).hideMapController();
	}

	@Override
	public void startReceivingData() {
	}
	
	@Override
	public void stopReceivingData() {
	}

	
	private void startReviewing() {
		// TODO 自动生成的方法存根
		FragmentManager fm = getSupportFragmentManager();
		LogPickerDialogFragment dialog = LogPickerDialogFragment.newInstance(this);
		dialog.show(fm, DIALOG_LOG_PICKER);
	}

	private void stopTesting() {
		// TODO 自动生成的方法存根
		ProcessInterface.StopLogWrite();//停止记录log
	}

	private void startTesting() {
		// TODO 自动生成的方法存根
		//add by yzy
		
		//设置网络模式，应是从什么地方获得
		Globals.setNetMode(Globals.MODE_LTE);
		
		try
		{
			if (isLogging) 
			{	
				ProcessInterface.StartLogWrite();//开始记录log
			}
			FtpConfigParser configparser = new FtpConfigParser();
			configparser.parse();
			
			
			testplanparser parser = new testplanparser();
			//其实，这是个ArrayList，多态,这里其实用不用多态都无所谓了
			List<Ftp> ftplist = parser.parse();
			if(ftplist.size() > 0)
			{
				Ftp ftp = ftplist.get(0) ;
				int threadnum = Integer.parseInt(configparser.getThreadNum());
				if(threadnum  != 0)
				{
					ftp.setThreadNum(threadnum);
				}
				
					
			
				FtpDownThread ftpDownThread = new FtpDownThread(this, ftp);						
				AppThread thread = new AppThread(ftpDownThread);				
				thread.start();
			}
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
    /** 
     * 给Tab按钮设置图标和文字 
     */  
    private View getTabItemView(int index) {  
        TextView view = (TextView) layoutInflater.inflate(R.layout.gen_menu_item, null, false);
        view.setText(mTextviewArray[index]);
        if(index == 0) {
        	view.setBackgroundResource(R.color.title_blue);
        	view.setTextColor(getResources().getColor(R.color.white));
        }
        return view;  
    }

	@Override
	public void refreshActivity(TestLog log) {
		((GenMapFragment) mCurrentMapFragment).showMapController();
		updateUIOnReviewing(true);
		((MiouApp) getApplication()).setGenReviewing(true);
	}

	@Override
	public void setHandler(Fragment fragment) {
		// TODO Auto-generated method stub
		mCurrentMapFragment = fragment;
		if (((MiouApp) getApplication()).isGenReviewing()) {
			((GenMapFragment) mCurrentMapFragment).showMapController();
		}
	}

	@Override
	public void addDataReceiver(BroadcastReceiver r) {
		// TODO Auto-generated method stub
		
		IntentFilter filter = new IntentFilter(ResultService.ACTION_SHOW_NOTIFICATION);
		registerReceiver(r, filter);
	}

	@Override
	public void removeDataReceiver(BroadcastReceiver r) {
		// TODO Auto-generated method stub
		unregisterReceiver(r);
	}

}
