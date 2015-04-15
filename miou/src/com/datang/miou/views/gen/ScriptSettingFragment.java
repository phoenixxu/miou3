package com.datang.miou.views.gen;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.datang.miou.R;
import com.datang.miou.datastructure.TestPlan;
import com.datang.miou.datastructure.TestCommand;
import com.datang.miou.datastructure.TestScheme;
import com.datang.miou.datastructure.TestScript;
import com.datang.miou.datastructure.TestType;
import com.datang.miou.utils.MiscUtils;
import com.datang.miou.utils.SDCardUtils;
import com.datang.miou.xml.PullTestCommandParser;
import com.datang.miou.xml.PullTestScriptParser;
import com.datang.miou.xml.PullTestTypeParser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("InflateParams")
public class ScriptSettingFragment extends Fragment {

	private TextView mTitleTextView;
	private TextView mSaveButton;
	private ImageView mBackButton;
	private Button mAddButton;
	private Button mDeleteButton;
	private Spinner mTypeSpinner;
	private ListView mTestPlanListView;
	
	private ArrayList<TestPlan> mTestPlans;
	private ArrayAdapter<TestPlan> mAdapter;
	private List<TestCommand> mTestCommands;
	protected TestType mTestType;
	private TestScript mTestScript;
	private Activity mAppContext;
	private int mTestScriptType;
	private List<TestType> mTestTypes;
	
	public static final int TEST_SCRIPT_TYPE_DOT = 0;
	public static final int TEST_SCRIPT_TYPE_TRACE = 1;
	public static final int TEST_SCRIPT_TYPE_GEN = 2;
	private static final String XML_FILE_TEST_SCHEME = "TestPlanTemplate.xml";
	private static final String XML_FILE_TEST_PLAN = "20150331142958.xml";
	private static final String XML_FILE_TEST_TYPE_DOT = "TestTypeDot.xml";
	private static final String XML_FILE_TEST_TYPE_TRACE = "TestTypeTrace.xml";
	private static final String XML_FILE_TEST_TYPE_GEN = "TestTypeGen.xml";
	private static final String XML_FILE_NEW_TEST_SCRIPT = "TestScript.xml";
	
	private static final String TAG = "ScriptSettingFragment";
	
	public ScriptSettingFragment(int type) {
		this.mTestScriptType = type;
	}
	
	/*
	 * 测试类型下拉列表适配器
	 */
	private class TestTypeAdapter extends BaseAdapter {

		private Context mContext;
		private List<TestType> mTypes;

		public TestTypeAdapter(Context context, List<TestType> types) {
			mContext = context;
			mTypes = types;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mTypes.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mTypes.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_test_type, null);
			}
			
			TestType type = (TestType) getItem(position);
			
			TextView name = (TextView) convertView.findViewById(R.id.item_test_type);
			name.setText(type.getDescription());
			
			return convertView;
		}
		
	}
	
	/*
	 * 测试计划ListView适配器
	 */
	private class TestPlanAdapter extends ArrayAdapter<TestPlan> {

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO 自动生成的方法存根
			if (convertView == null) {
				convertView = mAppContext.getLayoutInflater().inflate(R.layout.item_list_test_plan, null);
			}
			
			TestPlan plan = this.getItem(position);
			
			TextView mId = (TextView) convertView.findViewById(R.id.id_textView);
			String idString = null;
			if (plan.getId() < 10) {
				idString = "0" + String.valueOf(plan.getId());
			} else {
				idString = String.valueOf(plan.getId());
			}
			mId.setText(idString);
			
			TextView mName = (TextView) convertView.findViewById(R.id.name_textView);
			mName.setText(plan.getName().toString());
			
			EditText mTimes = (EditText) convertView.findViewById(R.id.times_textView);
			mTimes.setRawInputType(InputType.TYPE_CLASS_NUMBER);
			mTimes.setText(String.valueOf(plan.getTimes()));
			
			/*
			 * 添加文本框监听是否获得焦点
			 * 很重要，直接添加文本框文本改变监听器，任何文本改变都会被捕捉
			 * 所以如果获得焦点了则监听文本变化
			 * 没有获得焦点什么都不做
			 */
			mTimes.setOnFocusChangeListener(new OnFocusChangeListener() {

				@Override
				public void onFocusChange(View view, boolean hasFocus) {
					// TODO Auto-generated method stub
					TextWatcher watcher = new TextWatcher() {

						@Override
						public void beforeTextChanged(CharSequence s, int start,
								int count, int after) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void onTextChanged(CharSequence s, int start,
								int before, int count) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void afterTextChanged(Editable s) {
							// TODO Auto-generated method stub
							TestPlan plan = getItem(position);
							if (!s.toString().equals("")) {
								String number = s.toString();
								String newNumber = number.replaceAll("^(0+)", "");
								plan.setTimes(Integer.parseInt(newNumber));
							}
						}	
					};
					if (hasFocus) {
						((TextView) view).addTextChangedListener(watcher);
					} else {
						((TextView) view).removeTextChangedListener(watcher);
					}
				}
				
			});
						
			CheckBox selectCheckBox = (CheckBox) convertView.findViewById(R.id.select_checkBox);
			/*
			 * 这个isChecked属性只是为了表明当前是否被选中，加载时当做都没有选中处理
			 * 如果此处想从类中获取属性值设置CheckBox，则会触发OnCheckedChangeListener监听的事件重复设置
			 * 还是有问题，旋转的时候
			 */
			selectCheckBox.setChecked(plan.isChecked());
			selectCheckBox.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View view) {
					// TODO 自动生成的方法存根
					TestPlan plan = getItem(position);
					if (((CheckBox) view).isChecked()) {
						plan.setChecked(true);
					} else {
						plan.setChecked(false);
					}
				}
			});
			
			Button increaseButton = (Button) convertView.findViewById(R.id.increase_button);	
			increaseButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View view) {
					// TODO 自动生成的方法存根
					TestPlan plan = getItem(position);
					plan.setTimes(plan.getTimes() + 1);
					mAdapter.notifyDataSetChanged();
				}
			});
			
			Button decreaseButton = (Button) convertView.findViewById(R.id.decrease_button);
			if (plan.getTimes() == 0) {
				decreaseButton.setEnabled(false);
			} else {
				decreaseButton.setEnabled(true);
			}
			decreaseButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View view) {
					// TODO 自动生成的方法存根
					TestPlan plan = getItem(position);
					plan.setTimes(plan.getTimes() - 1);
					mAdapter.notifyDataSetChanged();
				}
			});
			
			return convertView;
		}

		public TestPlanAdapter(Context context, ArrayList<TestPlan> plans) {
			super(context, 0, plans);
			// TODO 自动生成的构造函数存根
		}	
	}
	
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//this.saveTestPlans();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
        
		mAppContext = getActivity();
		
		View view = inflater.inflate(R.layout.fragment_script_setting, container, false);
        mTitleTextView = (TextView) getActivity().findViewById(R.id.app_title_value);
		mTitleTextView.setText(R.string.gen_script_title);
		
		mSaveButton = (TextView) getActivity().findViewById(R.id.app_title_right_txt);
		mSaveButton.setText(R.string.gen_script_save_button);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO 自动生成的方法存根
				saveTestPlans();
			}
		});
        
		mBackButton = (ImageView) getActivity().findViewById(R.id.app_title_left);   
		mBackButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO 自动生成的方法存根
				if (NavUtils.getParentActivityName(getActivity()) != null) {
					NavUtils.navigateUpFromSameTask(getActivity());
				}
			}
		});
		
		mAddButton = (Button) view.findViewById(R.id.add_testing_plan_button);
		mAddButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO 自动生成的方法存根
				addTestPlan();
			}
		});
		
		mDeleteButton = (Button) view.findViewById(R.id.delete_testing_plan_button);
		mDeleteButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO 自动生成的方法存根
				deleteTestPlan();
			}
		});
		
		mTypeSpinner = (Spinner) view.findViewById(R.id.scripts_spinner);
		mTypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				mTestType = (TestType) (((TestTypeAdapter) parent.getAdapter()).getItem(position));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		mTestPlanListView = (ListView) view.findViewById(R.id.test_plan_listView);
		
		getTemplateTestScript();
		getTestSchemes();
		fillSpinner();
		fillPlanList();
		
		return view;
	}

	/*
	 * 获取测试计划模板
	 */
	private void getTemplateTestScript() {

		try {
			File file = MiscUtils.getExternalFileForRead(getActivity(), SDCardUtils.getConfigPath(), XML_FILE_TEST_PLAN);			
			InputStream is = new FileInputStream(file);
			//InputStream is = getActivity().getAssets().open(XML_FILE_TEST_PLAN);
			PullTestScriptParser parser = new PullTestScriptParser();
			mTestScript = parser.parse(is);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			Toast.makeText(getActivity(), "Parse Xml Error!", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}

	/*
	 * 获取测试计划
	 */
	private TestScript getTestScript(File file) {

		try {
			InputStream is = new FileInputStream(file);
			PullTestScriptParser parser = new PullTestScriptParser();
			TestScript script = parser.parse(is);
			return script;
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			Toast.makeText(getActivity(), "Parse Xml Error!", Toast.LENGTH_SHORT).show();
			return null;
		}
	}
	
	/*
	 * 获取测试类型
	 */
	private void getTestSchemes() {
		try {
			File file = MiscUtils.getExternalFileForRead(getActivity(), SDCardUtils.getConfigPath(), XML_FILE_TEST_SCHEME);			
			InputStream is = new FileInputStream(file);
			//InputStream is = getActivity().getAssets().open(XML_FILE_TEST_SCHEME);
			PullTestCommandParser parser = new PullTestCommandParser();
			mTestCommands = parser.parse(is);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			Toast.makeText(getActivity(), "parse xml error!", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}
	
	/*
	 * 填充测试类型下拉列表
	 */
	private void fillSpinner() {

		String name = null;
		
		switch (mTestScriptType) {
			case TEST_SCRIPT_TYPE_DOT:
				name = XML_FILE_TEST_TYPE_DOT;
				break;
			case TEST_SCRIPT_TYPE_TRACE:
				name = XML_FILE_TEST_TYPE_TRACE;
				break;
			case TEST_SCRIPT_TYPE_GEN:
				name = XML_FILE_TEST_TYPE_GEN;
				break;
		}
		
		try {
			//TODO:改为从应用文件目录读取
			//InputStream is = getActivity().getAssets().open(file);
			File file = MiscUtils.getExternalFileForRead(getActivity(), SDCardUtils.getConfigPath(), name);	
			InputStream is = new FileInputStream(file);
			PullTestTypeParser parser = new PullTestTypeParser();
			mTestTypes = parser.parse(is);
		} catch (Exception e) {
			Toast.makeText(getActivity(), "Parse Xml Error!", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		
		TestTypeAdapter adapter = new TestTypeAdapter(getActivity(), mTestTypes);
		mTypeSpinner.setAdapter(adapter);
	}

	/*
	 * 移除测试计划
	 */
	private void deleteTestPlan() {
		Iterator<TestPlan> iter = mTestPlans.listIterator();
		while (iter.hasNext()) {
			TestPlan plan = iter.next();
			if (plan.isChecked()) {
				iter.remove();
			}
		}
		mAdapter.notifyDataSetChanged();
	}

	/*
	 * 添加一条测试计划并指定测试类型
	 */
	private void addTestPlan() {
		
		TestPlan plan = new TestPlan(mTestType);
		mTestPlans.add(plan);
		mAdapter.notifyDataSetChanged();
	}

	/*
	 * 填充测试计划列表
	 */
	private void fillPlanList() {
		
		if (mTestPlans == null) {
			mTestPlans = new ArrayList<TestPlan>();
		}
		File dir = new File(SDCardUtils.getTestPlanPath());
		String latestFile = null;
		long lastTime = 0;
		
		for (String string : dir.list()) {
			if (string.startsWith("TestScript")) {
				File currentFile = new File(SDCardUtils.getTestPlanPath(), string);
				
				
				if (currentFile.lastModified() > lastTime) {
					lastTime = currentFile.lastModified();
					latestFile = string;
				}
			}
		}
		
		
		File file = MiscUtils.getExternalFileForRead(getActivity(), SDCardUtils.getTestPlanPath(), latestFile);
		if (file != null) {
			if (file.exists()) {
				TestScript script = getTestScript(file);
				
				for (TestScheme ts : script.getTestUnit()) {
					TestType currentType = null;
					for (TestType type : mTestTypes) {
						if (type.getId().equals(ts.getCommandList().getCommand().getId())) {
							currentType = type;
							break;
						}
					}
					TestPlan plan = new TestPlan(currentType);
					plan.setTimes(Integer.parseInt(ts.getCommandList().getCommand().getRepeat()));
					
					mTestPlans.add(plan);
				}
			}
		}
		
		mAdapter = new TestPlanAdapter(mAppContext, (ArrayList<TestPlan>) mTestPlans);
		this.mTestPlanListView.setAdapter(mAdapter);
	}

	/*
	 * 保存测试计划集
	 */
	private void saveTestPlans() {
		
		TestScript script = new TestScript();
		script.setAutoTestUnit(mTestScript.getAutoTestUnit());
		ArrayList<TestScheme> schemes = new ArrayList<TestScheme>();
		
		//遍历测试序列
		for (TestPlan plan : mTestPlans) {

			//从示例文件中获得一个scheme模板
			TestScheme scheme = new TestScheme();
			scheme.getAttrs(mTestScript.getTestUnit().get(0));
			
			TestCommand currentCommand = null;
			//遍历命令集
			for (TestCommand command : mTestCommands) {
				//对比命令的ID和当前测试计划类型的ID，相同则作为当前命令
				if (command.getId().equals(plan.getType().getId())){
					currentCommand = command;
					break;
				}
			}
			
			//设置重复次数
			currentCommand.setRepeat(String.valueOf(plan.getTimes()));
			
			//填写scheme的命令部分
			scheme.getCommandList().setCommand(currentCommand);
			
			//加入结果scheme表
			schemes.add(scheme);
		}
		
		script.setTestUnit(schemes);
		
		writeToXml(script);
	}

	/*
	 * 将测试计划写入XML文件
	 */
	@SuppressLint("SimpleDateFormat")
	private void writeToXml(TestScript script) {

		String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		File file = MiscUtils.getExternalFileForWrite(getActivity(), SDCardUtils.getTestPlanPath(), "TestScript_" + timeStamp + ".xml");
		
		Writer writer = null;
		try {
			FileOutputStream out = new FileOutputStream(file);
			writer = new OutputStreamWriter(out);
			PullTestScriptParser.writeXml(script, writer);
		} catch (Exception e) {
			Toast.makeText(getActivity(), "Xml Write Error : " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (Exception e) {
					Toast.makeText(getActivity(), "File Close Error : " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
}
