package com.datang.miou.widget;

import com.datang.miou.R;
import com.datang.miou.exceptions.NegativeException;
import com.datang.miou.exceptions.NoTelephoneNumberException;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

public class TelephoneNumberEditText extends EditText {

	private static final String TAG = "TelephoneNumberEditText";
	Context mContext;
	
	public TelephoneNumberEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		// TODO Auto-generated constructor stub
	}

	public long getInt() throws Exception{
		// TODO Auto-generated method stub		
		String string = super.getText().toString();
		
		//String[] parts = string.split(string);
		char[] array = string.toCharArray();

		// 11位标准号码和10086这样的特殊号码
		// TODO:识别号码
		if (array.length != 11 && array.length != 5) {
			throw new NoTelephoneNumberException(null);
		}
		
		Long iValue = Long.parseLong(string);
		
		if (iValue < 0) {
			throw new NoTelephoneNumberException(null);
		}
		
		return iValue;
	}

	
}
