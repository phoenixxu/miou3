package com.datang.miou.views.single;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.datang.miou.FragmentSupport;
import com.datang.miou.R;
import com.datang.miou.annotation.AfterView;
import com.datang.miou.annotation.AutoView;
import com.datang.miou.datastructure.SingleTestResult;
import com.datang.miou.utils.MiscUtils;

@AutoView(R.layout.fragment_single_history)
public class SingleHistoryFragment extends FragmentSupport {

	private class ResultAdapter extends ArrayAdapter<SingleTestResult> {
		
		private Context mAppContext;
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO 自动生成的方法存根
			if (convertView == null) {
				convertView = ((Activity) mAppContext).getLayoutInflater().inflate(R.layout.item_list_single_test_result, null);
			}
			
			SingleTestResult result = getItem(position);	
			
			TextView name = (TextView) convertView.findViewById(R.id.name);
			name.setText(result.getName());
			
			TextView time = (TextView) convertView.findViewById(R.id.time);
			time.setText(String.valueOf(result.getTime()));
			
			TextView rsrp = (TextView) convertView.findViewById(R.id.rsrp);
			rsrp.setText(String.valueOf(MiscUtils.reserveTwoBit(result.getRSRP(), 2)));
			
			TextView sinr = (TextView) convertView.findViewById(R.id.sinr);
			sinr.setText(String.valueOf(MiscUtils.reserveTwoBit(result.getSINR(), 2)));
			
			TextView ul = (TextView) convertView.findViewById(R.id.ftp_ul);
			ul.setText(String.valueOf(MiscUtils.reserveTwoBit(result.getFtpUpload(), 2)));
			
			TextView ping = (TextView) convertView.findViewById(R.id.ping);
			ping.setText(String.valueOf(MiscUtils.reserveTwoBit(result.getPingDelay(), 2)));
			
			TextView dl = (TextView) convertView.findViewById(R.id.ftp_dl);
			dl.setText(String.valueOf(MiscUtils.reserveTwoBit(result.getFtpDownload(), 2)));
			
			TextView csfb = (TextView) convertView.findViewById(R.id.csfb);
			csfb.setText(String.valueOf(MiscUtils.reserveTwoBit(result.getCsfb(), 2)));
			
			return convertView;
		}

		public ResultAdapter(Context context, ArrayList<SingleTestResult> results) {
			super(context, 0, results);
			// TODO 自动生成的构造函数存根
			mAppContext = context;
		}	
	}
	
	@AutoView(R.id.data_list_view)
	ListView mListView;
	
	ArrayList<SingleTestResult> mResults;
	
	@AfterView
	void init() {
		getResults();
		
		ResultAdapter adapter = new ResultAdapter(getActivity(), mResults);
		mListView.setAdapter(adapter);
		
	}

	private void getResults() {
		// TODO Auto-generated method stub
		mResults = new ArrayList<SingleTestResult>();
		for (int i = 0; i < 100; i++) {
			SingleTestResult result = new SingleTestResult();
			result.setName("基站" + i);
			result.setTime((long) (Math.random() * 10000));
			result.setRSRP(Math.random() * 100);
			result.setSINR(Math.random() * 100);
			result.setFtpDownload(Math.random() * 1000);
			result.setFtpUpload(Math.random() * 1000);
			result.setPingDelay(Math.random() * 100);
			result.setCsfb(Math.random() * 100);
			mResults.add(result);
		}
	}
}
