package com.datang.miou.views.single;

import com.datang.miou.R;
import com.datang.miou.datastructure.Station;
import com.datang.miou.views.gen.GenScriptSettingActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class NewStationActivity extends Activity {

	public static final String EXTRA_STATION = "extra_station";
	protected static final String TAG = "NewStationActivity";
	private TextView mTitleTextView;
	private TextView mDoneButton;
	private ImageView mBackButton;
	private EditText mNameEditText;
	private EditText mLatitudeEditText;
	private EditText mLongitudeEditText;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_station);
		
	    mTitleTextView = (TextView) findViewById(R.id.app_title_value);
		mTitleTextView.setText(R.string.single_new);
		
		mDoneButton = (TextView) findViewById(R.id.app_title_right_txt);
		mDoneButton.setText(R.string.done);
		mDoneButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO 自动生成的方法存根
				Station station = formStation();
				if (station != null) {
					Intent intent = new Intent();
					intent.putExtra(EXTRA_STATION, station);
					setResult(Activity.RESULT_OK, intent);
					finish();
				}
			}
		});
		
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
		
		mNameEditText = (EditText) findViewById(R.id.name_edit_text);
		mLatitudeEditText = (EditText) findViewById(R.id.latitude_edit_text);
		mLongitudeEditText = (EditText) findViewById(R.id.longitude_edit_text);
	}

	protected Station formStation() {
		// TODO Auto-generated method stub
		String name = mNameEditText.getText().toString();
		if (name.equals("")) {
			Toast.makeText(this, getResources().getString(R.string.warning_blank_name), Toast.LENGTH_SHORT).show();
			return null;
		}
		Station station = new Station(name);
		
		try {
			double lat = Double.parseDouble(mLatitudeEditText.getText().toString());
			double lon = Double.parseDouble(mLongitudeEditText.getText().toString());
			station.setLatitude(lat);
			station.setLongitude(lon);
		} catch (Exception ex) {
			Toast.makeText(this, getResources().getString(R.string.warning_blank_position), Toast.LENGTH_SHORT).show();
			return null;
		}
		
		return station;
	}

}
