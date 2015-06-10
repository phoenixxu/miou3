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
public class WebResultAdapter extends BaseAdapter {

    private final LayoutInflater mLayoutInflater;
    List<JSONObject> list;
    Activity mContext;

    public WebResultAdapter(Activity activity, List<JSONObject> result) {
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
            convertView = mLayoutInflater.inflate(R.layout.result_web_item, null);
            convertView.setTag(holder);
            holder.startTime = (TextView) convertView.findViewById(R.id.startTime);
            holder.stopTime = (TextView) convertView.findViewById(R.id.stopTime);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.lon = (TextView) convertView.findViewById(R.id.lon);
            holder.lat = (TextView) convertView.findViewById(R.id.lat);
            holder.url = (TextView) convertView.findViewById(R.id.url);
            holder.tac = (TextView) convertView.findViewById(R.id.tac);
            holder.size = (TextView) convertView.findViewById(R.id.size);
            holder.lostTime = (TextView) convertView.findViewById(R.id.lostTime);
            holder.speed = (TextView) convertView.findViewById(R.id.speed);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        JSONObject object = list.get(position);

        holder.startTime.setText(val(object,"startTime"));
        holder.stopTime.setText(val(object,"stopTime"));
        holder.name.setText(val(object,"name"));
        holder.lon.setText(val(object,"lon"));
        holder.lat.setText(val(object,"lat"));
        holder.url.setText(val(object,"url"));
        holder.tac.setText(val(object,"tac"));
        holder.size.setText(val(object,"size"));
        holder.lostTime.setText(val(object,"lostTime"));
        holder.speed.setText(val(object,"speed"));


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
        TextView url;//打开网页地址
        TextView tac;//终端TAC
        TextView size;//打开网页大小
        TextView lostTime;//耗费时间
        TextView speed;//速率

    }
}
