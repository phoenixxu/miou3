package com.datang.miou.widget;

import com.datang.miou.R;
import com.datang.miou.exceptions.NegativeException;
import com.datang.miou.exceptions.NoIPAddressException;
import com.datang.miou.exceptions.NoTelephoneNumberException;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

public class IPEditText extends EditText {

	private static final String TAG = "IPEditText";
	Context mContext;
	
	public IPEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		// TODO Auto-generated constructor stub
	}

	public String getIp() throws NoIPAddressException{
		// TODO Auto-generated method stub		
		String string = super.getText().toString();
		String ip = "";
		
		String[] parts = string.split("\\.");
		
		
		if (parts.length != 4) {
			throw new NoIPAddressException(null);
		}
		
		for (int i = 0; i < 4; i++) {
			int value = Integer.parseInt(parts[i]);
			if (value < 0 || value > 255) {
				throw new NoIPAddressException(null);
			}
			ip += value;
			if (i != 3) {
				ip += ".";
			}
		}
		
		return ip;
	}

	
}
