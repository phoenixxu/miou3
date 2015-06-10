package com.datang.miou.views.percept.task;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.datang.business.measurements.HttpTask;
import com.datang.business.measurements.PingTask;
import com.datang.miou.R;
import com.datang.miou.views.percept.connect.ConnectActivity;
import com.datang.miou.views.percept.web.WebActivity;

import java.util.List;

/**
 * Created by leo on 6/1/15.
 */
public class TasksAdapter extends BaseAdapter {

    private final Activity mContext;
    List<Task> list = TaskParser.getTasks();


    public TasksAdapter(Activity ctx) {
        mContext = ctx;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Task getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.task_item, null);
            holder.createTime = (TextView) convertView.findViewById(R.id.create_time);
            holder.lastTime = (TextView) convertView.findViewById(R.id.last_time);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.status = (TextView) convertView.findViewById(R.id.status);
            holder.ctl = (Button) convertView.findViewById(R.id.ctl);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Task task = list.get(position);
        holder.name.setText(task.name);
        holder.createTime.setText(task.timeStamp);
        holder.lastTime.setText(task.lastTimeStamp);
        holder.status.setText(task.status);
        if (!task.status.equals("")) {
            if (task.status.equals("结束")) {
                holder.ctl.setText("执行");
            } else {
                holder.ctl.setText("中止");
            }
        }

        final ViewHolder finalHolder = holder;
        holder.ctl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalHolder.ctl.getText().equals("执行")) {
                    finalHolder.ctl.setText("中止");
                    if (task.type.equals(HttpTask.TYPE)) {
                        WebActivity.startTest(mContext, task.name + "_" + position);
                    } else if (task.type.equals(PingTask.TYPE)) {
                        ConnectActivity.startTest(mContext, task.name + "_" + position);
                    }
                } else {
                    finalHolder.ctl.setText("执行");
                    if (task.type.equals(HttpTask.TYPE)) {
                        WebActivity.stopTask();
                    } else if (task.name.equals(PingTask.TYPE)) {
                        ConnectActivity.stopTask();
                    }
                }

            }
        });

        return convertView;
    }


    static class ViewHolder {
        TextView name;
        TextView createTime;
        TextView lastTime;
        TextView status;
        Button ctl;

    }
}
