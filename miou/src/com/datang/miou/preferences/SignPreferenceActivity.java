package com.datang.miou.preferences;

import android.app.Activity;
import android.content.BroadcastReceiver;

import com.datang.miou.FragmentSupport;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;

import com.datang.miou.R;

public class SignPreferenceActivity extends FragmentActivity implements FragmentSupport.Callbacks{

	private TextView mTitleTextView;
	private ImageView mBackButton;
	
	//定义FragmentTabHost对象  
    public FragmentTabHost mTabHost;  
      
    //定义一个布局  
    private LayoutInflater layoutInflater;  
          
    //定义数组来存放Fragment界面  
    private Class<?> fragmentArray[] = {SignPreferenceDataServiceFragment.class};  
      
    //Tab选项卡的文字  
    private String mTextviewArray[] = {"数据业务"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preference_sign);
		
		mTitleTextView = (TextView) findViewById(R.id.app_title_value);
		mTitleTextView.setText(R.string.pref_sign_title);
		
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
	}

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
