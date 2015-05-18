package com.datang.miou.views;

import android.content.Intent;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.datang.miou.ActivitySupport;
import com.datang.miou.R;
import com.datang.miou.annotation.AfterView;
import com.datang.miou.annotation.AutoView;
import com.datang.miou.datastructure.Globals;
import com.datang.miou.shared.Shared;
import com.datang.miou.views.percept.PerceptionActivity;

/**
 * 启动界面
 * 
 * @author suntongwei
 */
@AutoView(R.layout.start)
public class StartActivity extends ActivitySupport 
{

	@AutoView(R.id.start_txt_imei)
	private EditText txtImei;
	@AutoView(R.id.start_txt_email)
	private EditText txtEmail;
	
	@AfterView
	private void init() {
		
		String imei = Shared.getImei(mContext);
		if(imei != null && !"".equals(imei)) {
			//	add via chenzm
			if(Globals.isHigherUserPermission())
				startActivity(new Intent(mContext, MainActivity.class));
			else
				startActivity(new Intent(mContext, PerceptionActivity.class));
			//	add via chenzm end
			finish();
		}
		
		f(R.id.start_btn_ok).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				Editable strImei = txtImei.getText();
				Editable strEmail = txtEmail.getText();
				
				if(null == strImei || "".equals(strImei.toString().trim())) {
					Toast.makeText(mContext, "请输入IMEI", Toast.LENGTH_SHORT).show();
					return;
				}
				
				if(null == strEmail || "".equals(strEmail.toString().trim())) {
					Toast.makeText(mContext, "请输入Email", Toast.LENGTH_SHORT).show();
					return;
				}
				
				Shared.setImei(mContext, strImei.toString());
				Shared.setEmail(mContext, strEmail.toString());
				//	add via chenzm
				if(Globals.isHigherUserPermission())
					startActivity(new Intent(mContext, MainActivity.class));
				else
					startActivity(new Intent(mContext, PerceptionActivity.class));
				//	add via chenzm end
				finish();
			}
		});
		
		f(R.id.start_btn_cancel).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
	}
	
}
