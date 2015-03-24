/**
 * 
 */
package com.datang.miou.views.data;

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

import com.datang.miou.R;

/**
 * @author dingzhongchang
 *
 */
public class PlanManagerFragment  extends Fragment{

  private ArrayAdapter<String> mPlanListAdapter;
  private FragmentActivity mContext;
  private ListView mPlanListView;

  @Override
  public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      mContext = this.getActivity();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View root = inflater.inflate(R.layout.fragment_plan, container, false);
      mPlanListView = (ListView) root.findViewById(R.id.plan_listView);
      // initiate the listadapter
      mPlanListAdapter = new ArrayAdapter<String>(mContext,
              R.layout.newplan_list_item, R.id.tv_plan_name, this.getResources().getStringArray(R.array.test_type_gen));

      // assign the list adapter
      mPlanListView.setAdapter(mPlanListAdapter);
      mPlanListView.setOnItemClickListener(new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
          // TODO Auto-generated method stub
          
        }});
      return root;
  }




  @Override
  public void onSaveInstanceState(Bundle outState) {
      super.onSaveInstanceState(outState);
  }
}
