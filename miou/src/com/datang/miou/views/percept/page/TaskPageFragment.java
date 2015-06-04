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

import com.datang.business.Config;
import com.datang.business.UpdateIntent;
import com.datang.miou.R;
import com.datang.miou.views.percept.BasePageFragment;
import com.datang.miou.views.percept.task.NewTaskActivity;
import com.datang.miou.views.percept.task.TaskParser;
import com.datang.miou.views.percept.task.TasksAdapter;

public class TaskPageFragment extends BasePageFragment implements
        View.OnClickListener {

    private TasksAdapter tasksAdapter;
    BroadcastReceiver receiver;

    @Override
    protected View initUI(LayoutInflater inflater, ViewGroup container,
                          Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_page_task, container,
                false);
        root.findViewById(R.id.tv_add_task).setOnClickListener(this);
        ListView taskListView = (ListView) root.findViewById(R.id.tasks_listView);
        if (TaskParser.readTasks()) {
            tasksAdapter = new TasksAdapter(this.getActivity());
            taskListView.setAdapter(tasksAdapter);
        }


        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        tasksAdapter.notifyDataSetChanged();
        IntentFilter filter = new IntentFilter();
        filter.addAction(UpdateIntent.SCHEDULER_CONNECTED_ACTION);
        filter.addAction(UpdateIntent.MEASUREMENT_PROGRESS_UPDATE_ACTION);
        filter.addAction(UpdateIntent.SYSTEM_STATUS_UPDATE_ACTION);
        this.receiver = new BroadcastReceiver() {
            @Override
            // All onXyz() callbacks are single threaded
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(UpdateIntent.MEASUREMENT_PROGRESS_UPDATE_ACTION)) {
                    if(intent.getStringExtra(UpdateIntent.TASK_KEY).equals("http")){

                    }
                    textResult.setText(intent.getStringExtra(UpdateIntent.STATUS_MSG_PAYLOAD));
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
    public void onDestroyView() {
        super.onDestroyView();
        this.getActivity().unregisterReceiver(this.receiver);
    }

}