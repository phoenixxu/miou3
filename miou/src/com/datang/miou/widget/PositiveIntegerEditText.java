package com.datang.miou.widget;

import com.datang.miou.R;
import com.datang.miou.exceptions.NegativeException;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

public class PositiveIntegerEditText extends EditText {

	Context mContext;
	
	public PositiveIntegerEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		// TODO Auto-generated constructor stub
	}

	public int getInt() throws Exception{
		// TODO Auto-generated method stub		
		String string = super.getText().toString();
		float fValue;
		
		fValue = Float.parseFloat(string);
		if (fValue < 0) {
			throw new NegativeException(null);
		}
		
		return Math.round(fValue);
	}

	
}
