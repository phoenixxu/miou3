package com.datang.miou.views.data;

import java.util.List;

import com.datang.miou.MiouApp;
import com.datang.miou.R;
import com.datang.miou.datastructure.Globals;
import com.datang.miou.datastructure.TestCommand;
import com.datang.miou.exceptions.NegativeException;
import com.datang.miou.widget.IPEditText;
import com.datang.miou.widget.PositiveIntegerEditText;
import com.datang.miou.xml.XmlRW;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class WapPreferenceActivity extends FragmentActivity {
	private TextView mTitleTextView;
	private ImageView mBackButton;
	private TextView mSaveButton;
	private List<TestCommand> mCommands;
	private EditText mUrl;
	private PositiveIntegerEditText mTimeout;
	private PositiveIntegerEditText mInterval;
	private Button mWap1;
	protected boolean isWap1 = true;
	private Button mWap2;
	private CheckBox mGate;
	private IPEditText mIp;
	private PositiveIntegerEditText mPort;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preference_wap);
		
		mTitleTextView = (TextView) findViewById(R.id.app_title_value);
		mTitleTextView.setText(R.string.pref_wap_title);
		
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
		
		mCommands = ((MiouApp) getApplication()).getTestSchemes();
		
		mTimeout = (PositiveIntegerEditText) findViewById(R.id.timeout_edit_text);
		
		mUrl = (EditText) findViewById(R.id.url_edit_text);
		
		mInterval = (PositiveIntegerEditText) findViewById(R.id.interval_edit_text);
		
		mWap1 = (Button) findViewById(R.id.wap_1_0_button);
		mWap1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!isWap1) {
					mWap1.setBackgroundResource(R.color.blue);
					mWap2.setBackgroundResource(R.color.white);
					isWap1 = true;
				}
			}
		});
		
		mWap2 = (Button) findViewById(R.id.wap_2_0_button);
		mWap2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isWap1) {
					mWap1.setBackgroundResource(R.color.white);
					mWap2.setBackgroundResource(R.color.blue);		
					isWap1 = false;
				}
			}
		});
		
		mGate = (CheckBox) findViewById(R.id.gate_edit_text);
		
		mIp = (IPEditText) findViewById(R.id.ip_edit_text);
		
		mPort = (PositiveIntegerEditText) findViewById(R.id.port_edit_text);
		
		for (TestCommand command : mCommands) {
			if (command.getId().equals(Globals.TEST_COMMAND_WAP)) {
				mPort.setText(command.getPort());
				mInterval.setText(command.getInterval());
				mTimeout.setText(command.getTimeOut());
				mUrl.setText(command.getUrl());
			}
		}
	}

	protected void saveTestScheme() {
		// TODO Auto-generated method stub
		int port;
		int interval;
		int timeout;
		String url;
		try {
			port = mPort.getInt();
			interval = mInterval.getInt();
			timeout = mTimeout.getInt();
			url = mUrl.getText().toString();
		} catch (NegativeException e) {
			Toast.makeText(this, getResources().getString(R.string.warning_no_negative), Toast.LENGTH_SHORT).show();
			return;
		} catch (Exception e) {
			Toast.makeText(this, getResources().getString(R.string.warning_no_number), Toast.LENGTH_SHORT).show();
			return;
		}
		
		for (TestCommand command : mCommands) {
			if (command.getId().equals(Globals.TEST_COMMAND_WAP)) {
				command.setPort(String.valueOf(port));
				command.setInterval(String.valueOf(interval));
				command.setTimeOut(String.valueOf(timeout));
				command.setUrl(url);
				XmlRW.writeTestSchemeToXml(this, mCommands);
				break;
			}
		}
		
		Toast.makeText(this, getResources().getString(R.string.save_ok), Toast.LENGTH_SHORT).show();

	}
}
