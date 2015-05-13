package com.datang.miou.views.gen;

import com.datang.miou.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class GenScriptSettingActivity extends FragmentActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.gen_script_setting);
        
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
        	fragment = new ScriptSettingFragment(ScriptSettingFragment.TEST_SCRIPT_TYPE_GEN);
        }
        fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
	}
}
