/**
 * 
 */
package com.datang.miou.views.data;

import android.content.Context;
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
import com.datang.miou.widget.NextPrefrence;

/**
 * @author dingzhongchang
 *
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
    if (!sharedPref.contains(TestSchemeId.x0500)) {
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
    }

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	  View view = inflater.inflate(R.layout.fragment_plan, container, false);
	  
	  NextPrefrence idle = (NextPrefrence) view.findViewById(R.id.idle_next_preference);
	  idle.setActivity(IdlePreferenceActivity.class);
	  
	  NextPrefrence voice = (NextPrefrence) view.findViewById(R.id.voice_next_preference);
	  voice.setActivity(VoicePreferenceActivity.class);
	 
	  NextPrefrence volte = (NextPrefrence) view.findViewById(R.id.volte_next_preference);
	  volte.setActivity(VoltePreferenceActivity.class);
	  
	  NextPrefrence video = (NextPrefrence) view.findViewById(R.id.video_next_preference);
	  video.setActivity(VideoPreferenceActivity.class);
	  
	  NextPrefrence ping = (NextPrefrence) view.findViewById(R.id.ping_next_preference);
	  ping.setActivity(PingPreferenceActivity.class);
	  
	  NextPrefrence pdp = (NextPrefrence) view.findViewById(R.id.pdp_next_preference);
	  pdp.setActivity(PdpPreferenceActivity.class);
	  
	  NextPrefrence attach = (NextPrefrence) view.findViewById(R.id.attach_next_preference);
	  attach.setActivity(AttachPreferenceActivity.class);
	  
	  NextPrefrence ftp = (NextPrefrence) view.findViewById(R.id.ftp_next_preference);
	  ftp.setActivity(FtpPreferenceActivity.class);
	  
	  NextPrefrence mail = (NextPrefrence) view.findViewById(R.id.mail_next_preference);
	  mail.setActivity(MailPreferenceActivity.class);
	  
	  NextPrefrence sms = (NextPrefrence) view.findViewById(R.id.sms_next_preference);
	  sms.setActivity(SmsPreferenceActivity.class);
	  
	  NextPrefrence wap = (NextPrefrence) view.findViewById(R.id.wap_next_preference);
	  wap.setActivity(WapPreferenceActivity.class);
	  
	  NextPrefrence stream = (NextPrefrence) view.findViewById(R.id.stream_next_preference);
	  stream.setActivity(StreamPreferenceActivity.class);
	  
	  NextPrefrence http = (NextPrefrence) view.findViewById(R.id.http_next_preference);
	  http.setActivity(HttpPreferenceActivity.class);
	/*
    
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
        String schemeId = sharedPref.getString(mPlanListAdapter.getItem(position), "");
        if (schemeId.length() == 0) {
          Toast.makeText(mContext, "Not Found ID " + mPlanListAdapter.getItem(position),
              Toast.LENGTH_SHORT).show();
          return;
        }
        // TODO 模板存储


      }
    });
    */
    return view;
  }



  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
  }
}
