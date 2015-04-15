package com.datang.miou.views.single;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.datang.miou.datastructure.Station;
import com.datang.miou.datastructure.StationJSONSerializer;
import com.datang.miou.views.dialogs.LogPickerDialogFragment;
import com.datang.miou.views.dialogs.StationPickerDialogFragment;
import com.datang.miou.views.gen.GenMapFragment;
import com.datang.miou.views.gen.GenParamsFragment;
import com.datang.miou.views.gen.GenScriptSettingActivity;
import com.datang.miou.views.gen.GenSignalFragment;

import com.datang.miou.R;

/**
 * 
 * 
 * @author suntongwei
 */
public class SingleActivity extends FragmentActivity implements StationPickerDialogFragment.Callbacks{

	private static final String DIALOG_LOG_PICKER = "log_picker";

	protected static final String DIALOG_STATION_PICKER = "com.datang.miou.DIALOG_STATION_PICKER";

	private static final String FILE_NAME_STATION = "stations.json";

	private static final int REQUEST_NEW_STATION = 0;

	private static final String TAG = "SingleActivity";

	//定义FragmentTabHost对象  
    public FragmentTabHost mTabHost;  
      
    //定义一个布局  
    private LayoutInflater layoutInflater;  
          
    //定义数组来存放Fragment界面  
    private Class<?> fragmentArray[] = {GenMapFragment.class, 
    									GenSignalFragment.class,  
    									GenParamsFragment.class, 
    									SingleHistoryFragment.class };  
      
    //Tab选项卡的文字  
    private String mTextviewArray[] = {"单验测试", "信令", "指标", "历史测试结果"};

	private TextView mTitleTextView;

	public  TextView mScriptButton;

	private ImageView mBackButton;

	private Button mLogButton;

	protected boolean isLogging = false;
	
	protected boolean isTesting = false;

	private TextView mStationTextView;

	private Button mStationButton;
	
	private ArrayList<Station> mSelectedStations;

	private Button mNormalTestButton;

	private Button mCellTestButton;

	private Button mNewStationButton;
	
	private ArrayList<Station> mStations;

	private Button mReviewButton;

	protected boolean isPointTesting = false;
    
	public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.single); 
        initMembers();
        mStations = initStations();
        Toast.makeText(this, mStations.toString(), Toast.LENGTH_SHORT).show();
        initView();  
    }  
       
    private ArrayList<Station> initStations() {
		// TODO Auto-generated method stub
    	ArrayList<Station> stations = null;
    	
    	String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			File file = new File(getExternalFilesDir(null), FILE_NAME_STATION);
			if (!file.exists()) {
				Toast.makeText(this, FILE_NAME_STATION + " does not exist", Toast.LENGTH_SHORT).show();
				try {
					file.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//初始化文件
				stations = new ArrayList<Station>();
				for (int i = 0; i < 10; i++) {
					stations.add(new Station("Station #" + i));
				}
				
				String path = file.getAbsolutePath();
				Log.i(TAG, "path = " + path);
				
				StationJSONSerializer serializer = new StationJSONSerializer(this, path);
				try {
					serializer.saveStations(stations);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Toast.makeText(this, "json error!", Toast.LENGTH_SHORT).show();
				}
			} else {
				String path = file.getAbsolutePath();
				StationJSONSerializer serializer = new StationJSONSerializer(this, path);
				try {
					stations = serializer.loadStations();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Toast.makeText(this, "json error!", Toast.LENGTH_SHORT).show();
				}
			}
		}
		return stations;
	}

	private void initMembers() {
		// TODO Auto-generated method stub
    	mSelectedStations = new ArrayList<Station>();
	}

	/**
     * 初始化组件 
     */  
    private void initView(){  

        layoutInflater = LayoutInflater.from(this);  
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
        
        //标题栏标题
        mTitleTextView = (TextView) findViewById(R.id.app_title_value);
		mTitleTextView.setText(R.string.single_title);
		
		//标题栏脚本按钮
		mScriptButton = (TextView) findViewById(R.id.app_title_right_txt);
		mScriptButton.setText(R.string.gen_map_title_script_button);
		mScriptButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(getApplicationContext(), GenScriptSettingActivity.class);
				startActivity(intent);
			}
		});
		
		//标题栏返回按钮
		mBackButton = (ImageView) findViewById(R.id.app_title_left);
		mBackButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				try {
					if (NavUtils.getParentActivityName((Activity) getApplicationContext()) != null) {
						NavUtils.navigateUpFromSameTask((Activity) getApplicationContext());
					}
				} catch (Exception e) {
					finish();
				}		
			}
		});
		
		//记录日志按钮
		mLogButton = (Button) findViewById(R.id.single_log_button);
		mLogButton.setBackgroundResource(R.drawable.switch_off);
		mLogButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (isLogging) {
					isLogging = false;
					mLogButton.setBackgroundResource(R.drawable.switch_off);
				} else {
					isLogging = true;
					mLogButton.setBackgroundResource(R.drawable.switch_on);
				}
			}
		});
		
		//当前基站
		mStationTextView = (TextView) findViewById(R.id.station_textview);
		mStationTextView.setText(getResources().getString(R.string.single_current_hint) + " " + getResources().getString(R.string.no_station_selected));	
		
		//基站选择按钮
		mStationButton = (Button) findViewById(R.id.station_button);
		mStationButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FragmentManager fm = getSupportFragmentManager();
				StationPickerDialogFragment dialog = new StationPickerDialogFragment(mStations);
				dialog.show(fm, DIALOG_STATION_PICKER);
			}
		});
		
		//常规测试按钮
		mNormalTestButton = (Button) findViewById(R.id.single_normal_test_button);
		mNormalTestButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isTesting) {
					//如果正在测试，则功能为结束测试
					stopTest();
				} else {
					//如果不在测试，则功能为常规测试
					startNormalTest();
				}
			}
		});
		
		//小区测试按钮
		mCellTestButton = (Button) findViewById(R.id.single_assigned_test_button);
		mCellTestButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isTesting) {
					//如果正在测试，则功能为定点测试
					startPointTest();	
				} else {
					//如果不在测试，则功能为小区测试
					startCellTest();
				}
			}
		});
		
		//只有选择了基站，才可开始测试
		if (mSelectedStations.size() == 0) {
			mCellTestButton.setEnabled(false);
			mNormalTestButton.setEnabled(false);
		}
		
		//新增基站按钮
		mNewStationButton = (Button) findViewById(R.id.single_new_button);
		mNewStationButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				createStation();		
			}
		});
		
		//回放按钮
		mReviewButton = (Button) findViewById(R.id.single_review_button);
		mReviewButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				reviewTest();
			}
		});
    }  
      
    //开始定点测试
	protected void startPointTest() {
		// TODO Auto-generated method stub
		mCellTestButton.setEnabled(false);
		Toast.makeText(this, "开始定点测试!", Toast.LENGTH_SHORT).show();
	}

	//结束测试
	protected void stopTest() {
		// TODO Auto-generated method stub
		isTesting = false;
		mCellTestButton.setText(getResources().getString(R.string.single_assigned_test));
		mCellTestButton.setEnabled(true);
		
		mNormalTestButton.setText(getResources().getString(R.string.single_normal_test));
		mNormalTestButton.setEnabled(true);
		
		Toast.makeText(this, "结束测试!", Toast.LENGTH_SHORT).show();
	}

	//开始小区测试
	protected void startCellTest() {
		// TODO Auto-generated method stub
		isTesting = true;
		mCellTestButton.setText(getResources().getString(R.string.single_point_test));
		mNormalTestButton.setText(getResources().getString(R.string.single_end_test));
		Toast.makeText(this, "开始小区测试!", Toast.LENGTH_SHORT).show();
	}

	//开始常规测试
	protected void startNormalTest() {
		// TODO Auto-generated method stub
		isTesting = true;
		mCellTestButton.setText(getResources().getString(R.string.single_point_test));
		mNormalTestButton.setText(getResources().getString(R.string.single_end_test));
		Toast.makeText(this, "开始常规测试!", Toast.LENGTH_SHORT).show();
	}

	protected void createStation() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, NewStationActivity.class);
		startActivityForResult(intent, REQUEST_NEW_STATION);
	}

	//回放日志，从日志列表中选择
	private void reviewTest() {
		FragmentManager fm = getSupportFragmentManager();
		LogPickerDialogFragment dialog = LogPickerDialogFragment.newInstance(this);
		dialog.show(fm, DIALOG_LOG_PICKER);
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

    //基站选择完成后的处理
	@Override
	public void onDone(StationPickerDialogFragment dialog, ArrayList<Station> stations) {
		// TODO Auto-generated method stub
		if (stations != null) {
			Toast.makeText(this, stations.toString(), Toast.LENGTH_SHORT).show();
			mSelectedStations = stations;
			mStationTextView.setText(getResources().getString(R.string.single_current_hint) + " " + stations.get(0).toString());
			mCellTestButton.setEnabled(true);
			mNormalTestButton.setEnabled(true);
		}
		dialog.dismiss();
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == REQUEST_NEW_STATION) {
				if (data != null) {
					Station station = (Station) data.getSerializableExtra(NewStationActivity.EXTRA_STATION);
					mStations.add(station);
					saveStations();		
				}
			}
		}
	}

	//新建基站后保存基站信息到JSON文件
	private void saveStations() {
		// TODO Auto-generated method stub
    	String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			File file = new File(getExternalFilesDir(null), FILE_NAME_STATION);
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
			StationJSONSerializer serializer = new StationJSONSerializer(this, path);
			try {
				serializer.saveStations(mStations);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Toast.makeText(this, "json error!", Toast.LENGTH_SHORT).show();
			}
		}
	}

}
