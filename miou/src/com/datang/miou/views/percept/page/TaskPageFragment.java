package com.datang.miou.views.percept.page;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.datang.miou.R;
import com.datang.miou.views.percept.BasePageFragment;
import com.datang.miou.views.percept.task.NewTaskActivity;
import com.datang.miou.views.percept.task.TaskListAdapter;
import com.datang.miou.views.percept.task.TaskParser;
import com.datang.miou.views.percept.task.TasksAdapter;

public class TaskPageFragment extends BasePageFragment implements
		View.OnClickListener {

	@Override
	protected View initUI(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_page_task, container,
				false);
		root.findViewById(R.id.tv_add_task).setOnClickListener(this);
       ListView taskListView = (ListView) root.findViewById(R.id.tasks_listView);
        if(TaskParser.readTasks()){
            taskListView.setAdapter(new TasksAdapter(this.getActivity()));
        }

        return root;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.tv_add_task:
			mContext.startActivity(new Intent(mContext, NewTaskActivity.class));
			break;

		}

	}
}