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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ServerPreferenceActivity extends FragmentActivity {
	private TextView mTitleTextView;
	private ImageView mBackButton;
	private TextView mSaveButton;
	private List<TestCommand> mCommands;
	private TestCommand mCurrentCommand;
	private PositiveIntegerEditText mInterval;
	private PositiveIntegerEditText mKeepTime;
	private IPEditText mIp;
	private PositiveIntegerEditText mPort;
	private EditText mUserName;
	private EditText mPassWord;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preference_server);
		
		mTitleTextView = (TextView) findViewById(R.id.app_title_value);
		mTitleTextView.setText(R.string.pref_server_title);
		
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
			if (command.getId().equals(Globals.TEST_COMMAND_FTP)) {
				mCurrentCommand = command;
				break;
			}
		}
		
		mIp = (IPEditText) findViewById(R.id.ip_edit_text);
		mIp.setText(mCurrentCommand.getRemoteHost());
		
		mPort = (PositiveIntegerEditText) findViewById(R.id.port_edit_text);
		mPort.setText(mCurrentCommand.getPort());
		
		mUserName = (EditText) findViewById(R.id.username_edit_text);
		mUserName.setText(mCurrentCommand.getAccount());
		
		mPassWord = (EditText) findViewById(R.id.password_edit_text);
		mPassWord.setText(mCurrentCommand.getPassword());
	}

	protected void saveTestScheme() {
		// TODO Auto-generated method stub
		String ip;
		int port;
		String name;
		String password;
		try {
			ip = mIp.getIp();
			port = mPort.getInt();
			name = mUserName.getText().toString();
			password = mPassWord.getText().toString();
		} catch (NegativeException e) {
			Toast.makeText(this, getResources().getString(R.string.warning_no_negative), Toast.LENGTH_SHORT).show();
			return;
		} catch (Exception e) {
			Toast.makeText(this, getResources().getString(R.string.warning_no_number), Toast.LENGTH_SHORT).show();
			return;
		}
				
		for (TestCommand command : mCommands) {
			if (command.getId().equals(Globals.TEST_COMMAND_FTP)) {
				command.setRemoteHost(ip);
				command.setPort(String.valueOf(port));
				command.setAccount(name);
				command.setPassword(password);
				XmlRW.writeTestSchemeToXml(this, mCommands);
				break;
			}
		}
		
		Toast.makeText(this, getResources().getString(R.string.save_ok), Toast.LENGTH_SHORT).show();

	}

}
