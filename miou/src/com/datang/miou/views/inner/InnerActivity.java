package com.datang.miou.views.inner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
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
import com.datang.miou.views.dialogs.LogPickerDialogFragment;
import com.datang.miou.views.gen.GenParamsFragment;
import com.datang.miou.views.gen.GenScriptSettingActivity;

import com.datang.miou.R;
import com.datang.miou.FragmentSupport;
/**
 * 
 * 
 * @author suntongwei
 */
public class InnerActivity extends FragmentActivity implements FragmentSupport.Callbacks{

	private static final String DIALOG_LOG_PICKER = "log_picker";

	//定义FragmentTabHost对象  
    public FragmentTabHost mTabHost;  
      
    //定义一个布局  
    private LayoutInflater layoutInflater;  
          
    //定义数组来存放Fragment界面  
    private Class<?> fragmentArray[] = {InnerOptionsFragment.class, 
    									InnerTestFragment.class,  
    									InnerSignalFragment.class, 
    									GenParamsFragment.class,
    									InnerResultFragment.class };  
      
    //Tab选项卡的文字  
    private String mTextviewArray[] = {"设置", "测试", "信令", "指标", "结果"};

	private TextView mTitleTextView;

	public  TextView mScriptButton;

	private ImageView mBackButton;

	private Button mStartButton;

	private Button mReviewButton;

	private Button mLogButton;

	protected boolean isTesting;

	protected boolean isLogging;
    
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
		mTitleTextView.setText(R.string.innner_title);
		
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
		
		mStartButton = (Button) findViewById(R.id.gen_map_start_button);
		mStartButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (isTesting) {
					mStartButton.setText(R.string.gen_map_start);
					mReviewButton.setEnabled(true);
					isTesting = false;
					stopTesting();
				} else {
					mStartButton.setText(R.string.gen_map_stop);
					mReviewButton.setEnabled(false);
					isTesting = true;
					startTesting();
				}
			}
		});
		
		mReviewButton = (Button) findViewById(R.id.gen_map_review_button);
		mReviewButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				reviewTesting();
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
    }  
          
	private void reviewTesting() {
		// TODO 自动生成的方法存根
		FragmentManager fm = getSupportFragmentManager();
		LogPickerDialogFragment dialog = LogPickerDialogFragment.newInstance(this);
		dialog.show(fm, DIALOG_LOG_PICKER);
	}

	private void stopTesting() {
		// TODO 自动生成的方法存根
		
	}

	private void startTesting() {
		// TODO 自动生成的方法存根
		
	}
	
    /** 
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

	@Override
	public void addDataReceiver(BroadcastReceiver r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeDataReceiver(BroadcastReceiver r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startReceivingData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stopReceivingData() {
		// TODO Auto-generated method stub
		
	}

}
