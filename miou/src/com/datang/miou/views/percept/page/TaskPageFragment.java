package com.datang.miou.views.percept.page;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.datang.business.MeasurementTask;
import com.datang.business.UpdateIntent;
import com.datang.miou.R;
import com.datang.miou.views.percept.BasePageFragment;
import com.datang.miou.views.percept.task.NewTaskActivity;
import com.datang.miou.views.percept.task.Task;
import com.datang.miou.views.percept.task.TaskParser;
import com.datang.miou.views.percept.task.TasksAdapter;

public class TaskPageFragment extends BasePageFragment implements
        View.OnClickListener {

    BroadcastReceiver receiver;
    private TasksAdapter tasksAdapter;

    @Override
    protected View initUI(LayoutInflater inflater, ViewGroup container,
                          Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_page_task, container,
                false);
        root.findViewById(R.id.tv_add_task).setOnClickListener(this);
        ListView taskListView = (ListView) root.findViewById(R.id.tasks_listView);
        TaskParser.readTasks();
        tasksAdapter = new TasksAdapter(this.getActivity());
        taskListView.setAdapter(tasksAdapter);


        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        tasksAdapter.notifyDataSetChanged();
        IntentFilter filter = new IntentFilter();
        filter.addAction(UpdateIntent.SYSTEM_STATUS_UPDATE_ACTION);
        this.receiver = new BroadcastReceiver() {
            @Override
            // All onXyz() callbacks are single threaded
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(UpdateIntent.SYSTEM_STATUS_UPDATE_ACTION)) {
                    String index = intent.getStringExtra(UpdateIntent.TASK_INDEX);
                    if (index != null) {
                        int i = Integer.parseInt(index.split("_")[1]);
                        if (i < 0) return;
                        int status = intent.getIntExtra(UpdateIntent.STATUS_MSG_PAYLOAD, 0);
                        Task task = tasksAdapter.getItem(i);
                        if (status == MeasurementTask.IDLE_STATUS) {
                            task.status = "空闲";
                        } else if (status == MeasurementTask.EXE_STATUS) {
                            task.status = "执行中";
                        } else if (status == MeasurementTask.WAIT_STATUS) {
                            task.status = "等待";
                        } else if (status == MeasurementTask.END_STSATUS) {
                            task.status = "结束";
                        }
                        String time = intent.getStringExtra(UpdateIntent.TASK_LAST_TIME);
                        if (time != null)
                            task.lastTimeStamp = time;
                        tasksAdapter.notifyDataSetChanged();
                    }

                }
            }
        };
        this.getActivity().registerReceiver(this.receiver, filter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_add_task:
                mContext.startActivity(new Intent(mContext, NewTaskActivity.class));
                break;

        }

    }

    @Override
    public void onStop() {
        super.onStop();
        this.getActivity().unregisterReceiver(this.receiver);
    }


}