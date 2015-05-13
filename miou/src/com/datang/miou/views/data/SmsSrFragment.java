package com.datang.miou.views.data;

import java.util.List;

import com.datang.miou.MiouApp;
import com.datang.miou.R;
import com.datang.miou.datastructure.Globals;
import com.datang.miou.datastructure.TestCommand;
import com.datang.miou.exceptions.NegativeException;
import com.datang.miou.exceptions.NoEmailException;
import com.datang.miou.widget.EmailEditText;
import com.datang.miou.widget.PositiveIntegerEditText;
import com.datang.miou.xml.XmlRW;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SmsSrFragment extends PreferenceBaseFragment {

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.blank, container, false);
			
		
		return view;
	}

	@Override
	protected void saveTestScheme() {
		// TODO Auto-generated method stub
		
	}
}
