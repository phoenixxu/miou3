package com.datang.miou.views.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import com.datang.miou.MiouApp;
import com.datang.miou.R;
import com.datang.miou.datastructure.Globals;
import com.datang.miou.datastructure.TestCommand;
import com.datang.miou.exceptions.NegativeException;
import com.datang.miou.utils.MiscUtils;
import com.datang.miou.utils.SDCardUtils;
import com.datang.miou.widget.PositiveIntegerEditText;
import com.datang.miou.xml.PullTestSchemeParser;
import com.datang.miou.xml.XmlRW;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class HttpPreferenceActivity extends FragmentActivity {
	private static final int APN_TYPES = 4;
	private static final int PROXY_TYPES = 3;
	private TextView mTitleTextView;
	private ImageView mBackButton;
	private TextView mSaveButton;
	private List<TestCommand> mCommands;
	private TestCommand mCurrentCommand;
	private PositiveIntegerEditText mInterval;
	private PositiveIntegerEditText mTimeout;
	private PositiveIntegerEditText mPort;
	private EditText mUrl;
	private CheckBox mProxy;
	private EditText mAddress;

	private Button[] mApnButtons = new Button[APN_TYPES];
	protected int mApn;
	protected int mProxyType;
	protected Button[] mProxyButtons = new Button[PROXY_TYPES];
	private boolean isProxy;
	private LinearLayout mProxyTypeLayout;
	private LinearLayout mProxyAddressLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preference_http);
		
		mTitleTextView = (TextView) findViewById(R.id.app_title_value);
		mTitleTextView.setText(R.string.pref_http_title);
		
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
		
		for (TestCommand command : mCommands) {
			if (command.getId().equals(Globals.TEST_COMMAND_HTTP)) {
				mCurrentCommand = command;
				break;
			}
		}
		
		mTimeout = (PositiveIntegerEditText) findViewById(R.id.timeout_edit_text);
		mTimeout.setText(mCurrentCommand.getTimeOut());
		
		mInterval = (PositiveIntegerEditText) findViewById(R.id.interval_edit_text);
		mInterval.setText(mCurrentCommand.getInterval());
		
		mPort = (PositiveIntegerEditText) findViewById(R.id.port_edit_text);
		mPort.setText(mCurrentCommand.getPort());
		
		mUrl = (EditText) findViewById(R.id.url_edit_text);
		mUrl.setText(mCurrentCommand.getUrl()); 
		
		mProxyTypeLayout = (LinearLayout) findViewById(R.id.proxy_type_layout);
		mProxyAddressLayout = (LinearLayout) findViewById(R.id.proxy_address_layout);
		
		isProxy = mCurrentCommand.getProxy().equals("0") ? false : true;
		mProxy = (CheckBox) findViewById(R.id.proxy_checkbox);
		mProxy.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (((CheckBox) v).isChecked()) {
					isProxy = true;
					showProxyFields();
				} else {
					isProxy = false;
					hideProxyFields();
				}
			}
		});
		mProxy.setChecked(isProxy);
		if (isProxy) {
			showProxyFields();
		} else {
			hideProxyFields();
		}
		
		mAddress = (EditText) findViewById(R.id.address_edit_text);
		mAddress.setText(mCurrentCommand.getAddress());
		
		
		View.OnClickListener httpListener = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for (int i = 0; i < PROXY_TYPES; i++) {
					if (mProxyButtons[i].equals(v)) {
						mProxyType = i;
					}
					mProxyButtons[i].setBackgroundResource(R.color.white);
				}
				v.setBackgroundResource(R.color.blue);
			}
		};
		
		int startRes = R.id.http_button;
		for (int i = 0; i < PROXY_TYPES; i++) {
			mProxyButtons[i] = (Button) findViewById(startRes + i);
			mProxyButtons[i].setOnClickListener(httpListener);
		}
		
		View.OnClickListener apnListener = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for (int i = 0; i < APN_TYPES; i++) {
					if (mApnButtons[i].equals(v)) {
						mApn = i;
					}
					mApnButtons[i].setBackgroundResource(R.color.white);
				}
				v.setBackgroundResource(R.color.blue);
			}
		};
		
		startRes = R.id.cmwap_button;
		for (int i = 0; i < APN_TYPES; i++) {
			mApnButtons[i] = (Button) findViewById(startRes + i);
			mApnButtons[i].setOnClickListener(apnListener);
		}
		
		mProxyType = Integer.parseInt(mCurrentCommand.getProxyType());
		mProxyButtons[mProxyType].setBackgroundResource(R.color.blue);
		
		mApn = Integer.parseInt(mCurrentCommand.getApn()) - 1;
		mApnButtons[mApn].setBackgroundResource(R.color.blue);
	}

	private void showProxyFields() {
		// TODO Auto-generated method stub
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		mProxyTypeLayout.setLayoutParams(lp);
		mProxyTypeLayout.setVisibility(View.VISIBLE);
		mProxyAddressLayout.setLayoutParams(lp);
		mProxyAddressLayout.setVisibility(View.VISIBLE);
	}

	private void hideProxyFields() {
		// TODO Auto-generated method stub
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, 0);
		mProxyTypeLayout.setLayoutParams(lp);
		mProxyTypeLayout.setVisibility(View.INVISIBLE);
		mProxyAddressLayout.setLayoutParams(lp);
		mProxyAddressLayout.setVisibility(View.INVISIBLE);
	}
	
	protected void saveTestScheme() {
		// TODO Auto-generated method stub
		int timeout;
		int interval;
		int port;
		String url;
		String address;
		try {
			timeout = mTimeout.getInt();
			interval = mInterval.getInt();
			port = mPort.getInt();
			url = mUrl.getText().toString();
			address = mAddress.getText().toString();
		} catch (NegativeException e) {
			Toast.makeText(this, getResources().getString(R.string.warning_no_negative), Toast.LENGTH_SHORT).show();
			return;
		} catch (Exception e) {
			Toast.makeText(this, getResources().getString(R.string.warning_no_number), Toast.LENGTH_SHORT).show();
			return;
		}
				
		for (TestCommand command : mCommands) {
			if (command.getId().equals(Globals.TEST_COMMAND_HTTP)) {
				command.setTimeOut(String.valueOf(timeout));
				command.setInterval(String.valueOf(interval));
				command.setPort(String.valueOf(port));
				command.setUrl(url);
				command.setProxy(isProxy ? "1" : "0");
				command.setAddress(address);
				command.setApn(String.valueOf(mApn + 1));
				command.setProxyType(String.valueOf(mProxyType));
				writeToXml(mCommands);
				break;
			}
		}
		
		Toast.makeText(this, getResources().getString(R.string.save_ok), Toast.LENGTH_SHORT).show();
	}

	private void writeToXml(List<TestCommand> commands) {

		File file = MiscUtils.getExternalFileForWrite(this, SDCardUtils.getConfigPath(), XmlRW.XML_FILE_TEST_SCHEME);
		
		Writer writer = null;
		try {
			FileOutputStream out = new FileOutputStream(file);
			writer = new OutputStreamWriter(out);
			PullTestSchemeParser.writeXml(commands, writer);
		} catch (Exception e) {
			Toast.makeText(this, "Xml Write Error : " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (Exception e) {
					Toast.makeText(this, "File Close Error : " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
}
