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

public class MailSendFragment extends PreferenceBaseFragment {

	private List<TestCommand> mCommands;
	private Button mTime;
	private Button mSize;
	protected boolean isTimeMode = true;
	private TextView mFromMailbox;
	private EmailEditText mToMailbox;
	private EditText mSubject;
	private EditText mContent;
	private PositiveIntegerEditText mAttachmentSize;
	private PositiveIntegerEditText mTimeout;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.preference_mail_send, container, false);
			
		mTime = (Button) view.findViewById(R.id.time_button);
		mTime.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!isTimeMode) {
					mTime.setBackgroundResource(R.color.blue);
					mSize.setBackgroundResource(R.color.white);
					isTimeMode = true;
				}
			}
		});
		
		mSize = (Button) view.findViewById(R.id.size_button);
		mSize.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isTimeMode) {
					mTime.setBackgroundResource(R.color.white);
					mSize.setBackgroundResource(R.color.blue);		
					isTimeMode = false;
				}
			}
		});
		
		mCommands = ((MiouApp) getActivity().getApplication()).getTestSchemes();
		
		mTimeout = (PositiveIntegerEditText) view.findViewById(R.id.timeout_edit_text);
		mFromMailbox = (TextView) view.findViewById(R.id.mailbox_text_view);
		mToMailbox = (EmailEditText) view.findViewById(R.id.mailbox_edit_text);
		mSubject = (EditText) view.findViewById(R.id.subject_edit_text);
		mContent = (EditText) view.findViewById(R.id.content_edit_text);
		mAttachmentSize = (PositiveIntegerEditText) view.findViewById(R.id.attachment_size_edit_text);
		
		for (TestCommand command : mCommands) {
			if (command.getId().equals(Globals.TEST_COMMAND_SMTP)) {
				mTimeout.setText(command.getTimeOut());
				mFromMailbox.setText(command.getFrom());
				mToMailbox.setText(command.getTo());
				mSubject.setText(command.getSubject());
				mContent.setText(command.getBody());
				mAttachmentSize.setText(command.getFileSize());
			}
		}
		return view;
	}
	
	@Override
	public void saveTestScheme() {
		int timeout;
		String from;
		String to;
		String subject;
		String content;
		int size;
		try {
			timeout = mTimeout.getInt();
			//from = 
			to = mToMailbox.getEmail();
			subject = mSubject.getText().toString();
			content = mContent.getText().toString();
			size = mAttachmentSize.getInt();
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
			if (command.getId().equals(Globals.TEST_COMMAND_SMTP)) {
				command.setTimeOut(String.valueOf(timeout));
				//command.setFrom(from);
				command.setTo(to);
				command.setSubject(subject);
				command.setBody(content);
				command.setFileSize(String.valueOf(size));
				XmlRW.writeTestSchemeToXml(getActivity(), mCommands);
				break;
			}
		}
		
		Toast.makeText(getActivity(), getResources().getString(R.string.save_ok), Toast.LENGTH_SHORT).show();

	}

}
