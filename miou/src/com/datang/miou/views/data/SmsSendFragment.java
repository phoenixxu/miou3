package com.datang.miou.views.data;

import java.util.List;

import com.datang.miou.MiouApp;
import com.datang.miou.R;
import com.datang.miou.datastructure.Globals;
import com.datang.miou.datastructure.TestCommand;
import com.datang.miou.exceptions.NegativeException;
import com.datang.miou.exceptions.NoEmailException;
import com.datang.miou.exceptions.NoTelephoneNumberException;
import com.datang.miou.widget.PositiveIntegerEditText;
import com.datang.miou.widget.TelephoneNumberEditText;
import com.datang.miou.xml.XmlRW;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

public class SmsSendFragment extends PreferenceBaseFragment {

	private List<TestCommand> mCommands;
	private EditText mContent;
	private PositiveIntegerEditText mTimeout;
	private EditText mTaskName;
	private TelephoneNumberEditText mServerCenter;
	private TelephoneNumberEditText mDestination;
	private PositiveIntegerEditText mInterval;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.preference_sms_send, container, false);
		
		mCommands = ((MiouApp) getActivity().getApplication()).getTestSchemes();
		
		mTimeout = (PositiveIntegerEditText) view.findViewById(R.id.timeout_edit_text);
		mServerCenter = (TelephoneNumberEditText) view.findViewById(R.id.server_center_edit_text);
		mDestination = (TelephoneNumberEditText) view.findViewById(R.id.destination_edit_text);
		mTaskName = (EditText) view.findViewById(R.id.task_name_edit_text);
		mContent = (EditText) view.findViewById(R.id.content_edit_text);
		mInterval = (PositiveIntegerEditText) view.findViewById(R.id.interval_edit_text);
		
		for (TestCommand command : mCommands) {
			if (command.getId().equals(Globals.TEST_COMMAND_SEND_SMS)) {
				mTimeout.setText(command.getTimeOut());
				mServerCenter.setText(command.getServerCenterAddress());
				mDestination.setText(command.getDestination());
				mContent.setText(command.getContent());
				mInterval.setText(command.getInterval());
			}
		}
		return view;
	}
	
	@Override
	public void saveTestScheme() {
		int timeout;
		String destination;
		String serverCenter;
		int interval;
		String content;
		int size;
		try {
			timeout = mTimeout.getInt();
			//from = 
			interval = mInterval.getInt();
			destination = mDestination.getText().toString();
			content = mContent.getText().toString();
			serverCenter = String.valueOf(mServerCenter.getInt());
		} catch (NegativeException e) {
			Toast.makeText(getActivity(), getResources().getString(R.string.warning_no_negative), Toast.LENGTH_SHORT).show();
			return;
		} catch (NoEmailException e) {
			Toast.makeText(getActivity(), getResources().getString(R.string.warning_no_email), Toast.LENGTH_SHORT).show();
			return;
		} catch (NoTelephoneNumberException e) {
			Toast.makeText(getActivity(), getResources().getString(R.string.warning_no_telephone), Toast.LENGTH_SHORT).show();
			return;
		} catch (Exception e) {
			Toast.makeText(getActivity(), getResources().getString(R.string.warning_no_number), Toast.LENGTH_SHORT).show();
			return;
		}
		
		for (TestCommand command : mCommands) {
			if (command.getId().equals(Globals.TEST_COMMAND_SMTP)) {
				command.setTimeOut(String.valueOf(timeout));
				//command.setFrom(from);
				command.setInterval(String.valueOf(interval));
				command.setDestination(destination);
				command.setContent(content);
				command.setServerCenterAddress(serverCenter);			
				XmlRW.writeTestSchemeToXml(getActivity(), mCommands);
				break;
			}
		}
		
		Toast.makeText(getActivity(), getResources().getString(R.string.save_ok), Toast.LENGTH_SHORT).show();

	}

}
