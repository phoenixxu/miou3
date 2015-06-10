package com.datang.miou.views.percept.result;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.datang.business.UpdateIntent;
import com.datang.business.measurements.HttpTask;
import com.datang.miou.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leo on 5/14/15.
 */
public class WebFragment extends Fragment {

    List<JSONObject> result = new ArrayList<JSONObject>();
    private BroadcastReceiver receiver;
    private WebResultAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter();
        filter.addAction(UpdateIntent.MEASUREMENT_PROGRESS_UPDATE_ACTION);


        this.receiver = new BroadcastReceiver() {
            @Override
            // All onXyz() callbacks are single threaded
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(UpdateIntent.MEASUREMENT_PROGRESS_UPDATE_ACTION)) {
                    if (intent.getStringExtra(UpdateIntent.TASK_TYPE).equals(HttpTask.TYPE)) {
                        String payload = intent.getStringExtra(UpdateIntent.STATS_RESULT);
                        if (payload == null) return;
                        try {
                            result.add(new JSONObject(payload));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();
                    }

                }
            }
        };
        this.getActivity().registerReceiver(this.receiver, filter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_web, container, false);
        ListView resultListView = (ListView) root.findViewById(R.id.web_result);
        adapter = new WebResultAdapter(this.getActivity(), result);
        resultListView.setAdapter(adapter);


        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.getActivity().unregisterReceiver(receiver);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


}
