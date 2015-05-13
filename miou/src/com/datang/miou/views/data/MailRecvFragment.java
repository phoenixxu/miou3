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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MailRecvFragment extends PreferenceBaseFragment {

	private List<TestCommand> mCommands;
	protected boolean isTimeMode = true;
	private PositiveIntegerEditText mTimeout;
	private EditText mTaskName;
	private EditText mMailServer;
	private PositiveIntegerEditText mPort;
	private CheckBox mSSL;
	private EmailEditText mMailbox;
	private EditText mUserName;
	private EditText mPassword;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.preference_mail_recv, container, false);
		
		mCommands = ((MiouApp) getActivity().getApplication()).getTestSchemes();
		
		mTimeout = (PositiveIntegerEditText) view.findViewById(R.id.timeout_edit_text);
		mTaskName = (EditText) view.findViewById(R.id.task_name_edit_text);
		mMailServer = (EditText) view.findViewById(R.id.mailserver_edit_text);
		mMailbox = (EmailEditText) view.findViewById(R.id.mailbox_edit_text);
		mSSL = (CheckBox) view.findViewById(R.id.ssl_check_box);
		mUserName = (EditText) view.findViewById(R.id.username_edit_text);
		mPassword = (EditText) view.findViewById(R.id.password_edit_text);
		mPort = (PositiveIntegerEditText) view.findViewById(R.id.port_edit_text);
		
		for (TestCommand command : mCommands) {
			if (command.getId().equals(Globals.TEST_COMMAND_POP3)) {
				mTimeout.setText(command.getTimeOut());
				mMailServer.setText(command.getMailServer());
				mSSL.setChecked(command.getSsl().equals("0") ? false : true);
				mUserName.setText(command.getUserName());
				mPassword.setText(command.getPassword());
				mPort.setText(command.getPort());
			}
		}
		
		return view;
	}
	
	@Override
	public void saveTestScheme() {
		int timeout;
		String mailServer;
		String username;
		String password;
		int port;
		try {
			timeout = mTimeout.getInt();
			port = mPort.getInt();
			mailServer = mMailServer.getText().toString();
			username = mUserName.getText().toString();
			password = mPassword.getText().toString();
		} catch (NegativeException e) {
			Toast.makeText(getActivity(), getResources().getString(R.string.warning_no_negative), Toast.LENGTH_SHORT).show();
			return;
		} catch (NoEmailException e) {
			Toast.makeText(getActivity(), getResources().getString(R.string.warning_no_email), Toast.LENGTH_SHORT).show();
			return;
		} catch (Exception e) {
			Toast.makeText(getActivity(), getResources().getString(R.string.warning_no_number), Toast.LENGTH_SHORT).show();
			return;
		}
		
		for (TestCommand command : mCommands) {
			if (command.getId().equals(Globals.TEST_COMMAND_POP3)) {
				command.setTimeOut(String.valueOf(timeout));
				//command.setFrom(from);
				command.setMailServer(mailServer);
				command.setUserName(username);
				command.setPassword(password);
				command.setPort(String.valueOf(port));
				command.setSsl(mSSL.isChecked() ? "1" : "0");
				XmlRW.writeTestSchemeToXml(getActivity(), mCommands);
				break;
			}
		}
		
		Toast.makeText(getActivity(), getResources().getString(R.string.save_ok), Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

}
