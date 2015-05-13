package com.datang.miou.views.data;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import com.datang.miou.R;
import com.datang.miou.datastructure.SingleTestResult;
import com.datang.miou.datastructure.TestCommand;
import com.datang.miou.ftp.FtpDownThread;
import com.datang.miou.testplan.bean.Ftp;
import com.datang.miou.utils.MiscUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class FtpFileSelectActivity extends Activity {

	public static final String EXTRA_FILE = "extra_file";
	public static final String TAG = "FtpFileSelectActivity";
	private ListView mListView;
	public TestCommand mCurrentCommand;
	private List<Node> mFiles;
	public FTPClient mClient;
	private ProgressBar mProgressBar;
	protected LinearLayout mSelectedItem;
	private String mCurrentPath;

	private Stack<String> mPathStack = new Stack<String>();
	private Stack<TextView> mDirButtons = new Stack<TextView>();
	private TextView mTitleTextView;
	private TextView mSaveButton;
	private ImageView mBackButton;
	protected String mSelectedFile;
	private LinearLayout mDirLayout;
	private TextView mParentDirButton;
	
	private class Node {
		private String mName;
		private boolean isDir;
		public String getName() {
			return mName;
		}
		public void setName(String name) {
			mName = name;
		}
		public boolean isDir() {
			return isDir;
		}
		public void setDir(boolean isDir) {
			this.isDir = isDir;
		}
	}
	private class FtpLoginTask extends AsyncTask<Void, Void, FTPClient> {

		@Override
		protected FTPClient doInBackground(Void... params) {
			// TODO Auto-generated method stub
			Ftp ftp = new Ftp();
			FTPClient client = new FTPClient();
			FtpDownThread ftpDownThread = new FtpDownThread(FtpFileSelectActivity.this, ftp);
			try {
				ftpDownThread.login(client, mCurrentCommand.getRemoteHost(), Integer.parseInt(mCurrentCommand.getPort()), mCurrentCommand.getAccount(), mCurrentCommand.getPassword());
				return client;
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(FTPClient client) {
			if (client != null) {
				mClient = client;
				new FtpFetchDataTask(client, "/").execute();
			}
		}
		
	}
	
	private class FtpFetchDataTask extends AsyncTask<Void, Void, FTPFile[]> {

		private FTPClient mClient;
		private String mPath;
		
		public FtpFetchDataTask(FTPClient client, String path) {
			mClient = client;
			mPath = path;
		}
		
		@Override
		protected FTPFile[] doInBackground(Void... params) {
			try {
				FTPFile[] files = mClient.listFiles(mPath);
				return files;
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(FTPFile[] files) {
			if (files != null) {
				List<Node> fileNodes = new ArrayList<Node>();
				mFiles = new ArrayList<Node>();
				
				for (FTPFile file : files) {
					Node node = new Node();
					node.setName(file.getName());
					node.setDir(file.isDirectory());
					if (file.isDirectory()) {
						mFiles.add(node);
					} else {
						fileNodes.add(node);
					}
				}
				mFiles.addAll(fileNodes);
				setupAdapter();
			}
		}
		
	}
	
	private class NodeAdapter extends ArrayAdapter<Node> {
		private Context mAppContext;
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO 自动生成的方法存根
			if (convertView == null) {
				convertView = ((Activity) mAppContext).getLayoutInflater().inflate(R.layout.item_list_node, null);
			}
			
			Node node = getItem(position);	
			
			TextView name = (TextView) convertView.findViewById(R.id.name);
			name.setText(node.getName());
			
			LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.node_layout);
			
			ImageView image = (ImageView) convertView.findViewById(R.id.icon);
			
			Log.i(TAG, "node.isDir() = " + node.isDir);
			if (node.isDir()) {
				//layout.setBackgroundResource(R.color.yellow);
				Log.i(TAG, "set dir");
				image.setImageResource(R.drawable.dir);
			} else {
				//layout.setBackgroundResource(R.color.white);
				image.setImageResource(R.color.white);
				Log.i(TAG, "do nothing");
			}
			return convertView;
		}

		public NodeAdapter(Context context, List<Node> nodes) {
			super(context, 0, nodes);
			// TODO 自动生成的构造函数存根
			mAppContext = context;
		}	
	}
	
	private void setupAdapter() {
		mProgressBar.setVisibility(View.INVISIBLE);
		
		NodeAdapter adapter = new NodeAdapter(this, mFiles);
		mListView.setAdapter(adapter);
		mListView.setVisibility(View.VISIBLE);
		
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				//获得点击的节点
				Node node = (Node) parent.getItemAtPosition(position);
				
				//无论如何，点击后都要恢复当前选择项的背景色，并且清空当前选中的文件
				mSelectedFile = null;
				if (mSelectedItem != null) {
					mSelectedItem.setBackgroundResource(R.color.white);
				}
				
				if (node.isDir()) {	
					//如果节点是一个目录，那么跳转
					if (node.getName().equals(".")) {
						//如果是当前目录					
						return;
					} else if (node.getName().equals("..")) {
						//如果是上一级目录
						jumpToParentDir();
					} else {
						//进入选中的目录
						enterDir(node.getName());
					}
					
				} else {
					//如果节点是一个文件
					//选中该项
					view.setSelected(true);
					
					//设置当前选择项，并设置背景色
					//mSelectedItem = (TextView) view.findViewById(R.id.name);
					mSelectedItem = (LinearLayout) view.findViewById(R.id.node_layout);
					mSelectedItem.setBackgroundResource(R.color.blue);
					
					//得到选中文件的绝对路径
					mSelectedFile = mCurrentPath + node.getName();
				}
			}
		});
		
		
	}
	
	protected void enterDir(String name) {
		//目录名进栈
		mPathStack.push(name);
		//组合当前路径
		mCurrentPath += name;
		mCurrentPath += "/";
		//添加导航按钮
		addDirButton(name);
		//刷新文件列表
		Log.i(TAG, "enter dir, path = " + mCurrentPath);
		refreshFileList();
	}

	private void refreshFileList() {
		mListView.setVisibility(View.INVISIBLE);
		mProgressBar.setVisibility(View.VISIBLE);	
		Toast.makeText(FtpFileSelectActivity.this, mCurrentPath, Toast.LENGTH_LONG).show();	
		new FtpFetchDataTask(mClient, mCurrentPath).execute();
	}

	protected void jumpToParentDir() {
		if (mPathStack.peek().equals("/")) {
			//栈顶是根目录
			return;
		} else {
			//否则向上一级
			/*
			String parts[] = mCurrentPath.split("/");
			mPathStack.pop();
			mCurrentPath = "/";
			for (int i = 1; i < parts.length - 1; i++) {
				mCurrentPath += parts[i];
				mCurrentPath += "/";
			}
			*/
			
			//移除栈顶的按钮
			mDirLayout.removeView(mDirButtons.peek());
			mDirButtons.pop();
			mPathStack.pop();
			
			//重新设置当前路径
			mCurrentPath = "/";
			for (int i = 1; i < mPathStack.size(); i++) {
				mCurrentPath += mPathStack.get(i);
				mCurrentPath += "/";
			}
			
			Log.i(TAG, "jump to parent, path = " + mCurrentPath);
			refreshFileList();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ftp_file_select);
		
		mTitleTextView = (TextView) findViewById(R.id.app_title_value);
		mTitleTextView.setText(R.string.pref_file_select_title);
		
		mSaveButton = (TextView) findViewById(R.id.app_title_right_txt);
		mSaveButton.setText(R.string.done);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO 自动生成的方法存根
				if (mSelectedFile == null) {
					return;
				}
				Intent intent = new Intent(null, Uri.parse(""));
				intent.putExtra(EXTRA_FILE, mSelectedFile);
				setResult(RESULT_OK, intent);
				finish();
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
		
		Bundle args = getIntent().getExtras();
		mCurrentCommand = (TestCommand) args.get(FtpPreferenceActivity.EXTRA_CURRENT_COMMAND);
		
		mListView = (ListView) findViewById(R.id.file_list_view);
		
		mProgressBar = (ProgressBar) findViewById(R.id.waiting);
		
		mParentDirButton = (Button) findViewById(R.id.parent_dir);
		mParentDirButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				jumpToParentDir();
			}
		});
		
		mCurrentPath = "/";
		mPathStack.push("/");
		
		mDirLayout = (LinearLayout) findViewById(R.id.dir_layout);
		
		addDirButton("/");
		
		new FtpLoginTask().execute();
	}

	private void addDirButton(String name) {
		TextView button = new TextView(this);
		mDirButtons.push(button);
		
		if (!name.equals("/")) {
			button.setText(name + "/");
		} else {
			button.setText("根目录/");
		}
		
		
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
		lp.gravity = Gravity.CENTER_VERTICAL;
		lp.rightMargin = 5;
		
		button.setGravity(Gravity.CENTER_VERTICAL);
		button.setLayoutParams(lp);
		button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
		//button.setBackgroundResource(R.color.candy_blue);
		
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				while (!mDirButtons.peek().equals(v)) {			
					mDirLayout.removeView(mDirButtons.peek());
					mDirButtons.pop();
					mPathStack.pop();					
				}
				
				mCurrentPath = "/";
				for (int i = 1; i < mPathStack.size(); i++) {
					mCurrentPath += mPathStack.get(i);
					mCurrentPath += "/";
				}
				
				Toast.makeText(FtpFileSelectActivity.this, mCurrentPath, Toast.LENGTH_LONG).show();
				
				new FtpFetchDataTask(mClient, mCurrentPath).execute();
			}
		});
		
		mDirLayout.addView(button);
	}
}
