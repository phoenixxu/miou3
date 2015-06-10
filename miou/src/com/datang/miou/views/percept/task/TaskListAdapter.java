package com.datang.miou.views.percept.task;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.datang.miou.R;

import java.util.List;

/**
 * Created by leo on 6/1/15.
 */
public class TaskListAdapter extends BaseAdapter {

    private final Context mContext;
    List<Task> list = TaskParser.getTasks();


    public TaskListAdapter(Context ctx) {
        mContext = ctx;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
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
            convertView = View.inflate(mContext, R.layout.new_task_item, null);
            holder.count = (TextView) convertView.findViewById(R.id.count);
            holder.index = (TextView) convertView.findViewById(R.id.index);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.checked = (CheckBox) convertView.findViewById(R.id.checked);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(list.get(position).type);
        holder.count.setText(list.get(position).count);
        holder.index.setText(position + "");
        holder.checked.setChecked(list.get(position).isChecked);


        return convertView;
    }


    static class ViewHolder {
        CheckBox checked;
        TextView index;
        TextView name;
        TextView count;

    }
}
