package com.datang.miou.views.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.datang.miou.MiouApp;
import com.datang.miou.R;
import com.datang.miou.datastructure.Globals;
import com.datang.miou.datastructure.TestCommand;
import com.datang.miou.exceptions.NegativeException;
import com.datang.miou.exceptions.NoIPAddressException;
import com.datang.miou.utils.MiscUtils;
import com.datang.miou.utils.SDCardUtils;
import com.datang.miou.widget.IPEditText;
import com.datang.miou.widget.PositiveIntegerEditText;
import com.datang.miou.xml.PullTestSchemeParser;
import com.datang.miou.xml.XmlRW;

public class PingPreferenceActivity extends FragmentActivity {

	private TextView mTitleTextView;
	private ImageView mBackButton;
	private TextView mSaveButton;
	private List<TestCommand> mCommands;
	private TestCommand mCurrentCommand;
	private IPEditText mIP;
	private PositiveIntegerEditText mPacket;
	private PositiveIntegerEditText mTimeout;
	private PositiveIntegerEditText mInterval;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preference_ping);
		
		mTitleTextView = (TextView) findViewById(R.id.app_title_value);
		mTitleTextView.setText(R.string.pref_ping_title);
		
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
			if (command.getId().equals(Globals.TEST_COMMAND_PING)) {
				mCurrentCommand = command;
				break;
			}
		}
		
		mIP = (IPEditText) findViewById(R.id.ip_edit_text);
		mIP.setText(mCurrentCommand.getIp());
		
		mPacket = (PositiveIntegerEditText) findViewById(R.id.packet_edit_text);
		mPacket.setText(mCurrentCommand.getPackageSize());
		
		mInterval = (PositiveIntegerEditText) findViewById(R.id.interval_edit_text);
		mInterval.setText(mCurrentCommand.getInterval());
		
		mTimeout = (PositiveIntegerEditText) findViewById(R.id.timeout_edit_text);
		mTimeout.setText(mCurrentCommand.getTimeOut());
	}

	protected void saveTestScheme() {
		// TODO Auto-generated method stub
		String ip;
		int packet;
		int interval;
		int timeout;
		try {
			packet = mPacket.getInt();
			interval = mInterval.getInt();
			timeout = mTimeout.getInt();
			ip = mIP.getIp();
		} catch (NoIPAddressException e) {
			Toast.makeText(this, getResources().getString(R.string.warning_no_ip), Toast.LENGTH_SHORT).show();
			return;
		} catch (NegativeException e) {
			Toast.makeText(this, getResources().getString(R.string.warning_no_negative), Toast.LENGTH_SHORT).show();
			return;
		} catch (Exception e) {
			Toast.makeText(this, getResources().getString(R.string.warning_no_number), Toast.LENGTH_SHORT).show();
			return;
		}
				
		for (TestCommand command : mCommands) {
			if (command.getId().equals(Globals.TEST_COMMAND_PING)) {
				command.setIp(ip);
				command.setPackageSize(String.valueOf(packet));
				command.setInterval(String.valueOf(interval));
				command.setTimeOut(String.valueOf(timeout));
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
