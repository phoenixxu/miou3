/**
 *
 */
package com.datang.miou.views.percept.task;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.datang.miou.ActivitySupport;
import com.datang.miou.R;
import com.datang.miou.annotation.AfterView;
import com.datang.miou.annotation.AutoView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

/**
 * <root>
 * <task>
 * <name>测试序列1</name>
 * <interval>10</interval>
 * <count>5</count>
 * <type>http<type/>
 * <list>
 * <key>百度</key>
 * <url>www.baidu.com</url>
 * <p/>
 * </list>
 * <p/>
 * <task/>
 * </root>
 *
 * @author dingzhongchang
 */
@AutoView(R.layout.newtask_activity)
public class NewTaskActivity extends ActivitySupport {


    private ListView taskListView;
    private TaskListAdapter taskListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView mTitleTextView = (TextView) findViewById(R.id.app_title_value);
        mTitleTextView.setText("新增任务");
        TextView mRight = (TextView) findViewById(R.id.app_title_right_txt);
        mRight.setText("完成");
        mRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ImageView mBackButton = (ImageView) findViewById(R.id.app_title_left);
        mBackButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                try {
                    if (NavUtils.getParentActivityName((Activity) getApplicationContext()) != null) {
                        NavUtils.navigateUpFromSameTask((Activity) getApplicationContext());
                    }
                } catch (Exception e) {
                    finish();
                }
            }
        });
        taskListView = (ListView) f(R.id.newtask_listView);
        taskListAdapter = new TaskListAdapter(this);
        taskListView.setAdapter(taskListAdapter);
        CheckBox allChecked = (CheckBox) this.f(R.id.all_checked);
        allChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (Task task : TaskParser.getTasks()) {
                    task.isChecked = isChecked;
                }
                taskListAdapter.notifyDataSetChanged();
            }
        });
    }

    @AfterView
    private void init() {
        final EditText editTextCount = (EditText) f(R.id.count);
        final EditText editTextInterval = (EditText) f(R.id.interval);
        final Spinner spinner = (Spinner) f(R.id.sp_newtask);
        final EditText taskEditText = (EditText) f(R.id.task_name);
        String[] taskNames = this.getResources().getStringArray(R.array.task_names);
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, taskNames));
        this.f(R.id.bt_save_task).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task task = new Task();
                task.name = taskEditText.getText().toString();
                String type = spinner.getSelectedItem().toString();
                if (type.equals("网页")) {
                    task.type = "http";
                } else if (type.equals("连接")) {
                    task.type = "ping";
                } else if (type.equals("视频")) {
                    task.type = "video";
                } else if (type.equals("语音")) {
                    task.type = "voice";
                } else if (type.equals("测速")) {
                    task.type = "ftp";
                }
                task.count = editTextCount.getText().toString();
                task.interval = editTextInterval.getText().toString();
                task.timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
                if (!TaskParser.addTask(task)) {
                    Toast.makeText(mContext, task.name + "任务更新成功", Toast.LENGTH_SHORT).show();
                }
                TaskParser.writeTasks();
                taskListAdapter.notifyDataSetChanged();
            }
        });

        this.f(R.id.del_task).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Iterator<Task> iterator = TaskParser.getTasks().iterator();
                while (iterator.hasNext()) {
                    Task task = iterator.next();
                    if (task.isChecked) {
                        iterator.remove();
                    }
                }
                taskListAdapter.notifyDataSetChanged();
                TaskParser.writeTasks();
            }
        });
    }
}
