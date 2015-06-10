package com.datang.miou.views.percept.result;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.datang.miou.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by leo on 15/6/10.
 */
public class ConnectResultAdapter extends BaseAdapter {

    private final LayoutInflater mLayoutInflater;
    List<JSONObject> list;
    Activity mContext;

    public ConnectResultAdapter(Activity activity, List<JSONObject> result) {
        list = result;
        mContext = activity;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        if (list != null) return list.get(position);
        return null;
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
            convertView = mLayoutInflater.inflate(R.layout.result_connect_item, null);
            convertView.setTag(holder);
            holder.startTime = (TextView) convertView.findViewById(R.id.startTime);
            holder.stopTime = (TextView) convertView.findViewById(R.id.stopTime);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.lon = (TextView) convertView.findViewById(R.id.lon);
            holder.lat = (TextView) convertView.findViewById(R.id.lat);
            holder.count = (TextView) convertView.findViewById(R.id.count);
            holder.tac = (TextView) convertView.findViewById(R.id.tac);
            holder.ip = (TextView) convertView.findViewById(R.id.ip);
            holder.avgTime = (TextView) convertView.findViewById(R.id.avgTime);
            holder.suc = (TextView) convertView.findViewById(R.id.suc);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        JSONObject object = list.get(position);
        holder.startTime.setText(val(object, "startTime"));
        holder.stopTime.setText(val(object, "stopTime"));
        holder.name.setText(val(object, "name"));
        holder.lon.setText(val(object, "lon"));
        holder.lat.setText(val(object, "lat"));
        holder.count.setText(val(object, "count"));
        holder.tac.setText(val(object, "tac"));
        holder.ip.setText(val(object, "ip"));
        holder.avgTime.setText(val(object, "avgTime"));
        holder.suc.setText(val(object, "suc"));
        return convertView;
    }

    private String val(JSONObject json, String key) {
        Object o = null;
        try {
            o = json.get(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (o == null) return "";
        return o.toString();
    }

    static class ViewHolder {
        TextView startTime;//测试开始时间
        TextView stopTime;//测试结束时间
        TextView name;//所在测试任务名称
        TextView lon;//经度
        TextView lat;//纬度
        TextView count;//ping网页数量
        TextView tac;//终端TAC
        TextView ip;//ing不同次数最多IP
        TextView suc;//总成功率
        TextView avgTime;//平均时延/时延最长值

    }
}
