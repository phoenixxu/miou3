/**
 *
 */
package com.datang.miou.views.data;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.datang.miou.R;
import com.datang.miou.testplan.TestSchemeId;
import com.datang.miou.utils.SDCardUtils;

import java.util.Map;

/**
 * @author dingzhongchang
 */
public class PlanManagerFragment extends Fragment {

    private ArrayAdapter<String> mPlanListAdapter;
    private FragmentActivity mContext;
    private ListView mPlanListView;
    private SharedPreferences sharedPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getActivity();
        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(TestSchemeId.x0500, "0x0500");
        editor.putString(TestSchemeId.x0501, "0x0501");
        editor.putString(TestSchemeId.x0502, "0x0502");
        editor.putString(TestSchemeId.x0602, "0x0602");
        editor.putString(TestSchemeId.x0603, "0x0603");
        editor.putString(TestSchemeId.x0604, "0x0604");
        editor.putString(TestSchemeId.x0608, "0x0608");
        editor.putString(TestSchemeId.x0609, "0x0609");
        editor.putString(TestSchemeId.x060A, "0x060A");
        editor.putString(TestSchemeId.x060B, "0x060B");
        editor.putString(TestSchemeId.x060C, "0x060C");
        editor.putString(TestSchemeId.x0611, "0x0611");
        editor.putString(TestSchemeId.x0612, "0x0612");
        editor.putString(TestSchemeId.x0613, "0x0613");
        editor.commit();
        TestSchemeCache.read(null);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_plan, container, false);
        mPlanListView = (ListView) root.findViewById(R.id.plan_listView);
        // initiate the listadapter
        mPlanListAdapter =
                new ArrayAdapter<String>(mContext, R.layout.newplan_list_item, R.id.tv_plan_name, this
                        .getResources().getStringArray(R.array.test_type_gen));

        // assign the list adapter
        mPlanListView.setAdapter(mPlanListAdapter);
        mPlanListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String key = mPlanListAdapter.getItem(position);
                String schemeId = sharedPref.getString(key, "");
                if (schemeId.length() == 0) {
                    Toast.makeText(mContext, "Not Found ID " + key,
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                //读取sdcard／config／目录下的TestPlanTemplate.xml 显示相应的业务模块
                Intent intent = new Intent(mContext, TestSchemeActivity.class);
                intent.putExtra(TestSchemeId.ID, schemeId);
                startActivity(intent);
            }
        });
        return root;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
