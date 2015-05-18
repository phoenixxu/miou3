package com.datang.miou.views.percept.connect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.datang.miou.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by leo on 5/14/15.
 */
public class ConnectAdapter extends BaseAdapter {

    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private final List<PingBean> mDatas=new ArrayList<PingBean>();


    public ConnectAdapter(Context context, Collection<PingBean> datas) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mDatas.addAll(datas);

    }


    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.connect_listview_item, null);
            holder.name = (TextView) convertView.findViewById(R.id.ping_name);
            holder.time = (TextView) convertView.findViewById(R.id.ping_time);
            holder.sucess = (TextView) convertView.findViewById(R.id.ping_success);
            holder.progress = (ProgressBar) convertView.findViewById(R.id.ping_progress);
            holder.result = (TextView) convertView.findViewById(R.id.ping_result);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PingBean pingBean = mDatas.get(position);
        holder.progress.setProgress(pingBean.progress);
        holder.name.setText(pingBean.name);
        holder.sucess.setText("成功率：" + pingBean.sucess);
        holder.time.setText("时延：" + pingBean.time);
        holder.result.setText(pingBean.result);
        return convertView;
    }

    class ViewHolder {
        TextView name;
        TextView time;
        TextView sucess;
        TextView result;
        ProgressBar progress;
    }

}
