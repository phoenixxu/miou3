package com.datang.miou.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import android.content.Context;
import android.widget.Toast;

import com.datang.miou.datastructure.TestCommand;
import com.datang.miou.utils.MiscUtils;
import com.datang.miou.utils.SDCardUtils;

public class XmlRW {
	public static final String XML_FILE_TEST_SCHEME = "TestPlanTemplate.xml";
	
	/*
	 * 获取测试类型
	 */
	public static List<TestCommand> getTestSchemes(Context context) {
		try {
			File file = MiscUtils.getExternalFileForRead(context, SDCardUtils.getConfigPath(), XML_FILE_TEST_SCHEME);			
			InputStream is = new FileInputStream(file);
			//InputStream is = getActivity().getAssets().open(XML_FILE_TEST_SCHEME);
			PullTestCommandParser parser = new PullTestCommandParser();
			return parser.parse(is);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			Toast.makeText(context, "parse xml error!", Toast.LENGTH_SHORT).show();
			return null;
		}
	}
	
	public static void writeTestSchemeToXml(Context context, List<TestCommand> commands) {

		File file = MiscUtils.getExternalFileForWrite(context, SDCardUtils.getConfigPath(), XmlRW.XML_FILE_TEST_SCHEME);
		
		Writer writer = null;
		try {
			FileOutputStream out = new FileOutputStream(file);
			writer = new OutputStreamWriter(out);
			PullTestSchemeParser.writeXml(commands, writer);
		} catch (Exception e) {
			Toast.makeText(context, "Xml Write Error : " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (Exception e) {
					Toast.makeText(context, "File Close Error : " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
}
