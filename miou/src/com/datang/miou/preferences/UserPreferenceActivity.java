package com.datang.miou.preferences;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.datang.miou.ActivitySupport;
import com.datang.miou.R;
import com.datang.miou.widget.NextPrefrence;

public class UserPreferenceActivity extends ActivitySupport {

	private TextView mTitleTextView;
	private ImageView mBackButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preference_main);
		
		NextPrefrence line = (NextPrefrence) findViewById(R.id.line_next_preference);
		line.setActivity(LinePreferenceActivity.class);
		
		NextPrefrence warning = (NextPrefrence) findViewById(R.id.warning_next_preference);
		warning.setActivity(WarningPreferenceActivity.class);
		
		NextPrefrence signal = (NextPrefrence) findViewById(R.id.signal_filter_next_preference);
		signal.setActivity(FilterPreferenceActivity.class);
		
		NextPrefrence sign = (NextPrefrence) findViewById(R.id.sign_next_preference);
		sign.setActivity(SignPreferenceActivity.class);
		
		NextPrefrence map = (NextPrefrence) findViewById(R.id.map_next_preference);
		map.setActivity(MapPreferenceActivity.class);
		
		//NextPrefrence inner = (NextPrefrence) findViewById(R.id.inner_map_next_preference);
		//inner.setActivity(InnerMapPreferenceActivity.class);
		
		//NextPrefrence params = (NextPrefrence) findViewById(R.id.params_next_preference);
		//params.setActivity(ParamsLinePreferenceActivity.class);
		
		mTitleTextView = (TextView) findViewById(R.id.app_title_value);
		mTitleTextView.setText(R.string.pref_title);
		
		mBackButton = (ImageView) findViewById(R.id.app_title_left);
		mBackButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO 自动生成的方法存根
				try {
					if (NavUtils.getParentActivityName((Activity) getApplicationContext()) != null) {
						NavUtils.navigateUpFromSameTask((Activity) getApplicationContext());
					}
				} catch (Exception e) {
					finish();
				}		
			}
		});
	}

}
