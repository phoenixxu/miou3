package com.datang.miou.utils;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

public class MiscUtils {
	
	//传入double，保留指定位数
	public static String reserveTwoBit(double data, int count) {
		for (int i = 0; i < count; i++) {
		}
		DecimalFormat df = new DecimalFormat("#.00");
		return df.format(data);
	}
	
	public static File getExternalFileForWrite(Context context, String path, String name) {
		File file = null;
		if (name == null) {
			return null;
		}
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			file = new File(path, name);
			if (file.exists()) {
				file.delete();
			}
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Toast.makeText(context, "File Read Error : " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
			}
		}
		return file;
	}
	
	public static File getExternalFileForRead(Context context, String path, String name) {
		File file = null;
		if (name == null) {
			return null;
		}
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			file = new File(path, name);
		}
		return file;
	}
}
