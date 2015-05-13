package com.datang.miou.views.data;

import java.util.List;

import com.datang.miou.MiouApp;
import com.datang.miou.R;
import com.datang.miou.datastructure.Globals;
import com.datang.miou.datastructure.TestCommand;
import com.datang.miou.exceptions.NegativeException;
import com.datang.miou.widget.PositiveIntegerEditText;
import com.datang.miou.xml.XmlRW;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FtpPreferenceActivity extends FragmentActivity {
	private static final String TAG = "FtpPreferenceActivity";
	public static final String EXTRA_CURRENT_COMMAND = "extra_current_command";
	protected static final int REQUEST_PICK_FILE = 0;
	private TextView mTitleTextView;
	private ImageView mBackButton;
	private TextView mSaveButton;
	private List<TestCommand> mCommands;
	private LinearLayout mServerLayout;
	private TextView mServerTextView;
	private TestCommand mCurrentCommand;
	private PositiveIntegerEditText mSize;
	private PositiveIntegerEditText mInterval;
	private LinearLayout mFileLayout;
	private String mFilePath;
	private TextView mFileTextView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preference_ftp);
		
		mTitleTextView = (TextView) findViewById(R.id.app_title_value);
		mTitleTextView.setText(R.string.pref_ftp_title);
		
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
		
		mSize = (PositiveIntegerEditText) findViewById(R.id.size_edit_text);
		mSize.setText(mCurrentCommand.getFileSize());
		
		mInterval = (PositiveIntegerEditText) findViewById(R.id.interval_edit_text);
		mInterval.setText(mCurrentCommand.getInterval());
		
		mServerLayout = (LinearLayout) findViewById(R.id.server_layout);
		mServerLayout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(FtpPreferenceActivity.this, ServerPreferenceActivity.class);
				startActivity(intent);
			}
		});
		
		mFileLayout = (LinearLayout) findViewById(R.id.file_layout);
		mFileLayout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(FtpPreferenceActivity.this, FtpFileSelectActivity.class);
				intent.putExtra(EXTRA_CURRENT_COMMAND, mCurrentCommand);
				startActivityForResult(intent, REQUEST_PICK_FILE);
			}
		});
		
		mFileTextView = (TextView) findViewById(R.id.file_text_view);
		mFileTextView.setText(mCurrentCommand.getRemoteFile());
		
		mServerTextView = (TextView) findViewById(R.id.server_text_view);
		mServerTextView.setText(mCurrentCommand.getRemoteHost());
		
	}


	protected void showSizeLayout(boolean show) {
		// TODO Auto-generated method stub
		LinearLayout sizeLayout = (LinearLayout) findViewById(R.id.size_layout);
		LinearLayout.LayoutParams lp;
		
		if (show) {
			lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.bottomMargin = 1;
		} else {
			lp = new LinearLayout.LayoutParams(0, 0);	
		}
		sizeLayout.setLayoutParams(lp);
	}


	protected void saveTestScheme() {
		// TODO Auto-generated method stub
			//TODO 输入检查
		int size;
		int interval;
		try {
			size = mSize.getInt();
			interval = mInterval.getInt();
		} catch (NegativeException e) {
			Toast.makeText(this, getResources().getString(R.string.warning_no_negative), Toast.LENGTH_SHORT).show();
			return;
		} catch (Exception e) {
			Toast.makeText(this, getResources().getString(R.string.warning_no_number), Toast.LENGTH_SHORT).show();
			return;
		}
		
		for (TestCommand command : mCommands) {
			if (command.getId().equals(Globals.TEST_COMMAND_FTP)) {
				//if (isUploadMode) {
				command.setFileSize(String.valueOf(size));
				command.setInterval(String.valueOf(interval));
				if (mFilePath != null) {
					command.setRemoteFile(mFilePath);
				}
				//}
				//command.setDownload(isUploadMode ? "1" : "0");
				XmlRW.writeTestSchemeToXml(this, mCommands);
				break;
			}
		}
		
		Toast.makeText(this, getResources().getString(R.string.save_ok), Toast.LENGTH_SHORT).show();

	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Check which request we're responding to
		if (requestCode == REQUEST_PICK_FILE) {
			if (resultCode == RESULT_OK) {
				Log.i(TAG, data.getExtras().getString(FtpFileSelectActivity.EXTRA_FILE));
				mFilePath = data.getExtras().getString(FtpFileSelectActivity.EXTRA_FILE);
				mFileTextView.setText(mFilePath);
			}
		}
	}

	
}
