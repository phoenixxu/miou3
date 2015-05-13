package com.datang.miou.views.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.datang.miou.MiouApp;
import com.datang.miou.R;
import com.datang.miou.datastructure.Globals;
import com.datang.miou.datastructure.TestCommand;
import com.datang.miou.datastructure.TestScript;
import com.datang.miou.exceptions.NegativeException;
import com.datang.miou.exceptions.NoTelephoneNumberException;
import com.datang.miou.utils.MiscUtils;
import com.datang.miou.utils.SDCardUtils;
import com.datang.miou.widget.PositiveIntegerEditText;
import com.datang.miou.widget.TelephoneNumberEditText;
import com.datang.miou.xml.PullTestSchemeParser;
import com.datang.miou.xml.PullTestScriptParser;
import com.datang.miou.xml.XmlRW;

public class VoicePreferenceActivity extends FragmentActivity {

	private TextView mTitleTextView;
	private ImageView mBackButton;
	private TextView mSaveButton;
	private EditText mTime;
	private List<TestCommand> mCommands;
	private TestCommand mCurrentCommand;
	private EditText mCount;
	private TelephoneNumberEditText mNumber;
	private PositiveIntegerEditText mCallTime;
	private PositiveIntegerEditText mConnectionTime;
	private PositiveIntegerEditText mGapTime;
	private Button mMaster;
	private Button mSlave;
	protected boolean isMaster = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preference_voice);
		
		mTitleTextView = (TextView) findViewById(R.id.app_title_value);
		mTitleTextView.setText(R.string.pref_voice_title);
		
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
			if (command.getId().equals(Globals.TEST_COMMAND_VOICE_MASTER)) {
				mCurrentCommand = command;
				break;
			}
		}
		
		mNumber = (TelephoneNumberEditText) findViewById(R.id.number_edit_text);
		mNumber.setText(mCurrentCommand.getCallNumber());
		
		mCallTime = (PositiveIntegerEditText) findViewById(R.id.call_time_edit_text);
		mCallTime.setText(mCurrentCommand.getDuration());
		
		mConnectionTime = (PositiveIntegerEditText) findViewById(R.id.connection_time_edit_text);
		mConnectionTime.setText(mCurrentCommand.getMaxTime());
		
		mGapTime = (PositiveIntegerEditText) findViewById(R.id.gap_time_edit_text);
		mGapTime.setText(mCurrentCommand.getInterval());
		
		mMaster = (Button) findViewById(R.id.master_button);
		mMaster.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!isMaster) {
					mMaster.setBackgroundResource(R.color.blue);
					mSlave.setBackgroundResource(R.color.white);
					isMaster = true;
				}
			}
		});
		
		mSlave = (Button) findViewById(R.id.slave_button);
		mSlave.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isMaster) {
					mMaster.setBackgroundResource(R.color.white);
					mSlave.setBackgroundResource(R.color.blue);		
					isMaster = false;
				}
			}
		});
	}

	protected void saveTestScheme() {
		// TODO Auto-generated method stub
		long number;
		int callTime;
		int connectionTime;
		int gapTime;
		try {
			number = mNumber.getInt();
			callTime = mCallTime.getInt();
			connectionTime = mConnectionTime.getInt();
			gapTime = mGapTime.getInt();
		} catch (NoTelephoneNumberException e) {
			Toast.makeText(this, getResources().getString(R.string.warning_no_telephone), Toast.LENGTH_SHORT).show();
			return;
		} catch (NegativeException e) {
			Toast.makeText(this, getResources().getString(R.string.warning_no_negative), Toast.LENGTH_SHORT).show();
			return;
		} catch (Exception e) {
			Toast.makeText(this, getResources().getString(R.string.warning_no_number), Toast.LENGTH_SHORT).show();
			return;
		}
				
		for (TestCommand command : mCommands) {
			if (command.getId().equals(Globals.TEST_COMMAND_VOICE_MASTER)) {
				command.setCallNumber(String.valueOf(number));
				command.setMaxTime(String.valueOf(connectionTime));
				command.setInterval(String.valueOf(gapTime));
				command.setDuration(String.valueOf(callTime));
				writeToXml(mCommands);
				Log.i("测试序列管理", "set number = " + number);
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
