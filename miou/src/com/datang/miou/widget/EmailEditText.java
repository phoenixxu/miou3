package com.datang.miou.widget;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.datang.miou.exceptions.NoEmailException;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

public class EmailEditText extends EditText {

	Context mContext;
	
	public EmailEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		// TODO Auto-generated constructor stub
	}

	public String getEmail() throws Exception{
		// TODO Auto-generated method stub		
		String string = super.getText().toString();
		
		String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";    
		Pattern regex = Pattern.compile(check);    
		Matcher matcher = regex.matcher(string);    
		boolean isMatched = matcher.matches();    
		if (!isMatched) {
			throw new NoEmailException(null);
		} else {
			return string;
		}
	}
}
