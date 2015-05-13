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
import com.datang.miou.exceptions.NoIPAddressException;
import com.datang.miou.utils.MiscUtils;
import com.datang.miou.utils.SDCardUtils;
import com.datang.miou.widget.IPEditText;
import com.datang.miou.widget.PositiveIntegerEditText;
import com.datang.miou.xml.PullTestSchemeParser;
import com.datang.miou.xml.XmlRW;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PdpPreferenceActivity extends FragmentActivity {
	private TextView mTitleTextView;
	private ImageView mBackButton;
	private TextView mSaveButton;
	private List<TestCommand> mCommands;
	private TestCommand mCurrentCommand;
	private PositiveIntegerEditText mInterval;
	private PositiveIntegerEditText mKeepTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preference_pdp);
		
		mTitleTextView = (TextView) findViewById(R.id.app_title_value);
		mTitleTextView.setText(R.string.pref_pdp_title);
		
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
			if (command.getId().equals(Globals.TEST_COMMAND_PDP)) {
				mCurrentCommand = command;
				break;
			}
		}
		
		mKeepTime = (PositiveIntegerEditText) findViewById(R.id.keeptime_edit_text);
		mKeepTime.setText(mCurrentCommand.getKeepTime());
		
		mInterval = (PositiveIntegerEditText) findViewById(R.id.interval_edit_text);
		mInterval.setText(mCurrentCommand.getInterval());
	}

	protected void saveTestScheme() {
		// TODO Auto-generated method stub
		int keeptime;
		int interval;
		try {
			keeptime = mKeepTime.getInt();
			interval = mInterval.getInt();
		} catch (NegativeException e) {
			Toast.makeText(this, getResources().getString(R.string.warning_no_negative), Toast.LENGTH_SHORT).show();
			return;
		} catch (Exception e) {
			Toast.makeText(this, getResources().getString(R.string.warning_no_number), Toast.LENGTH_SHORT).show();
			return;
		}
				
		for (TestCommand command : mCommands) {
			if (command.getId().equals(Globals.TEST_COMMAND_PDP)) {
				command.setKeepTime(String.valueOf(keeptime));
				command.setInterval(String.valueOf(interval));
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
