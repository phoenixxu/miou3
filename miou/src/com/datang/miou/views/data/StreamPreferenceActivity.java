package com.datang.miou.views.data;

import java.util.ArrayList;
import java.util.List;

import com.datang.miou.MiouApp;
import com.datang.miou.R;
import com.datang.miou.datastructure.Globals;
import com.datang.miou.datastructure.TestCommand;
import com.datang.miou.exceptions.NegativeException;
import com.datang.miou.widget.PositiveIntegerEditText;
import com.datang.miou.xml.XmlRW;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class StreamPreferenceActivity extends FragmentActivity {
	
	private TextView mTitleTextView;
	private TextView mSaveButton;
	private ImageView mBackButton;
	private List<TestCommand> mCommands;
	private TestCommand mCurrentCommand;
	private Spinner mStreamType;
	private Spinner mStreamQuality;
	private EditText mUrl;
	private PositiveIntegerEditText mPlayTimeout;
	private PositiveIntegerEditText mInterval;
	private PositiveIntegerEditText mNoStreamTimeout;
	private Button mTime;
	private Button mFile;
	protected boolean isTimeMode = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preference_stream);
		
		mTitleTextView = (TextView) findViewById(R.id.app_title_value);
		mTitleTextView.setText(R.string.pref_stream_title);
		
		mSaveButton = (TextView) findViewById(R.id.app_title_right_txt);
		mSaveButton.setText(R.string.gen_script_save_button);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO 自动生成的方法存根
				saveTestScheme();
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
		
		mTime = (Button) findViewById(R.id.time_button);
		mTime.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!isTimeMode) {
					mTime.setBackgroundResource(R.color.blue);
					mFile.setBackgroundResource(R.color.white);
					isTimeMode = true;
				}
			}
		});
		
		mFile = (Button) findViewById(R.id.file_button);
		mFile.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isTimeMode) {
					mTime.setBackgroundResource(R.color.white);
					mFile.setBackgroundResource(R.color.blue);		
					isTimeMode = false;
				}
			}
		});
		
		mStreamType = (Spinner) findViewById(R.id.stream_type_spinner);
		ArrayList<String> types = new ArrayList<String>();
		types.add("搜狐");
		types.add("优酷");
		ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, types);
		typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mStreamType.setAdapter(typeAdapter);
		
		mStreamQuality = (Spinner) findViewById(R.id.stream_quality_spinner);
		ArrayList<String> qualities = new ArrayList<String>();
		qualities.add("1080P");
		qualities.add("720P");
		qualities.add("360P");
		ArrayAdapter<String> QualityAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, qualities);
		QualityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mStreamQuality.setAdapter(QualityAdapter);
		
		mUrl = (EditText) findViewById(R.id.url_edit_text);
		mPlayTimeout = (PositiveIntegerEditText) findViewById(R.id.play_timeout_edit_text);
		mInterval = (PositiveIntegerEditText) findViewById(R.id.interval_edit_text);
		mNoStreamTimeout = (PositiveIntegerEditText) findViewById(R.id.nostream_timeout_edit_text);
		
		mCommands = ((MiouApp) getApplication()).getTestSchemes();
		
		for (TestCommand command : mCommands) {
			if (command.getId().equals(Globals.TEST_COMMAND_STREAM)) {
				mInterval.setText(command.getInterval());
				break;
			}
		}

	}

	protected void saveTestScheme() {
		// TODO Auto-generated method stub
		int playTimeout;
		int noStreamTimeout;
		int interval;
		try {
			interval = mInterval.getInt();
		} catch (NegativeException e) {
			Toast.makeText(this, getResources().getString(R.string.warning_no_negative), Toast.LENGTH_SHORT).show();
			return;
		} catch (Exception e) {
			Toast.makeText(this, getResources().getString(R.string.warning_no_number), Toast.LENGTH_SHORT).show();
			return;
		}
				
		for (TestCommand command : mCommands) {
			if (command.getId().equals(Globals.TEST_COMMAND_STREAM)) {
				command.setInterval(String.valueOf(interval));
				XmlRW.writeTestSchemeToXml(this, mCommands);
				break;
			}
		}
		
		Toast.makeText(this, getResources().getString(R.string.save_ok), Toast.LENGTH_SHORT).show();

	}
}
