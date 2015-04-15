package com.datang.miou.ftp;

import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

//import com.scott.xml.model.Book;
import com.datang.miou.testplan.bean.*;
import com.datang.miou.utils.*;


import android.util.Log; 

import java.io.FileInputStream;  
import java.io.FileNotFoundException;

import java.util.Collections;

import java.util.Comparator;

public class FtpConfigParser {

	Integer repeatime;
	String currentid;
	String currentnodename;
	String currentnodevalue;
	static String ThreadNum = "0";
	
	
    
	public  String getThreadNum()
	{
		return ThreadNum;
	}
	
	public  FileInputStream GetStream() throws Exception {
		
		FileInputStream inputStream = null;
		//String filePath = SDCardUtils.getSystemLogPath();
		String filePath = SDCardUtils.getConfigPath() ;
		
		
		
		if (filePath != null) {
			File configFilePath = new File(filePath);
			if (!configFilePath.exists()) {
				configFilePath.mkdirs();
			}
		File xmlFile = new File(configFilePath.getAbsoluteFile()
					+ File.separator + "otherconfig.xml");
		
		
		
		
	    Log.v("abc*******",xmlFile.getName());
		try{
			if(xmlFile.exists()){
				inputStream = new FileInputStream(xmlFile);	
				return inputStream;
			}else{
				return inputStream;
			}
		}catch(FileNotFoundException e){
			Log.w("open file", "Error while new XML file from ", e);
		    return inputStream;
		}
		finally
		{
			return inputStream;
		}
		}
		
		/*try {
			if(inputStream!=null){	
				//xlh_add
			//parser.setInput(inputStream, null);
				Log.w("open file", "file opened ");
				return inputStream;
			}
		} catch (XmlPullParserException e) {
			Log.w("open file", "Error while reading XML file from ", e);
			return null;
		}*/
		return inputStream;
		
	}

	

	
	
	
	
	
	
	
	// @Override
	//public List<Ftp> parse(InputStream is) throws Exception {
	public void parse() throws Exception {
		   //String  strThreadNum = null;
		
		   Ftp ftp = null;
 		
		   FileInputStream is = GetStream();
		   if(is == null)
		   {
			   return ;
		   }
			
//			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
//			XmlPullParser parser = factory.newPullParser();
			
			XmlPullParser parser = Xml.newPullParser();	//由android.util.Xml创建一个XmlPullParser实例
	    	parser.setInput(is, "UTF-8");				//设置输入流 并指明编码方式

			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					//ftps = new ArrayList<Ftp>();
					break;
				case XmlPullParser.START_TAG:
					if (parser.getName().equals("ThreadNum")) {
						//ThreadNum = Integer.parseInt(parser.nextText());
						ThreadNum = parser.nextText();
						
					
					}
					
					
					break;
				case XmlPullParser.END_TAG:
					/*if (parser.getName().equals("Command")) {
						books.add(book);
						book = null;	
					}*/
					break;
					
				default:
						break;
				}
					
				eventType = parser.next();
			}
			//return ftps;
		}

	

}
