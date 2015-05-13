package com.datang.miou.test;

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

public class testplanparser {

	Integer repeatime;
	String currentid;
	String currentnodename;
	String currentnodevalue;
	
	List<Ftp> ftps = null;
	
	
	public static List<File> getFileSort(String path) {
		 
        List<File> list = getFiles(path, new ArrayList<File>());
 
        if (list != null && list.size() > 0) {
 
            Collections.sort(list, new Comparator<File>() {
                public int compare(File file, File newFile) {
                    if (file.lastModified() < newFile.lastModified()) {
                        return 1;
                    } /*else if (file.lastModified() == newFile.lastModified()) {
                        return 0;
                    }*/
                    else {
                        return -1;
                    }
 
                }
            });
 
        }
 
        return list;
    }
 
    /**
     * 
     * 获取目录下所有文件
     * 
     * @param realpath
     * @param files
     * @return
     */
    public static List<File> getFiles(String realpath, List<File> files) {
 
        File realFile = new File(realpath);
        if (realFile.isDirectory()) {
            File[] subfiles = realFile.listFiles();
            for (File file : subfiles) {
                if (file.isDirectory()) {
                    getFiles(file.getAbsolutePath(), files);
                } else {
                    files.add(file);
                }
            }
        }
        return files;
    }
	
	
	public  FileInputStream GetStream() throws Exception {
		
		FileInputStream inputStream = null;
		//String filePath = SDCardUtils.getSystemLogPath();
		String filePath = SDCardUtils.getTestPlanPath() ;
		
		
		
		/*if (filePath != null) {
			File logFilePath = new File(filePath);
			if (!logFilePath.exists()) {
				logFilePath.mkdirs();
			}
			File logFile = new File(logFilePath.getAbsoluteFile()
					+ File.separator + "ou.log");*/
		
		
		
		//String sdpath = Environment.getExternalStorageDirectory() + "/";
		//String sdpath =mContext.getFilesDir().toString()+"/";
		//String mSavePath = filePath + "/4GTest.xml";
		
		// String path = "d:\\test";
		 
	    List<File> list = getFileSort(filePath);
		
		//Log.v("abc",list.get(0));
		//File xmlFile = new File(filePath, "4GTest.xml");
	    File xmlFile = list.get(0);
	    Log.v("abc*******",xmlFile.getName());
		try{
			if(xmlFile.exists()){
				inputStream = new FileInputStream(xmlFile);	
				return inputStream;
			}else{
				return null;
			}
		}catch(FileNotFoundException e){
			Log.w("open file", "Error while new XML file from ", e);
		    return null;
		}
		/*finally
		{
			return null;
		}*/
		
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
		//return inputStream;
		
	}

	

	
	
	
	
	
	
	
	// @Override
	//public List<Ftp> parse(InputStream is) throws Exception {
	public List<Ftp> parse() throws Exception {
		   int current_enable;
		
		   Ftp ftp = null;
 		
		   FileInputStream is = GetStream();
		   if(is == null)
		   {
			   return null;
		   }
			
//			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
//			XmlPullParser parser = factory.newPullParser();
			
			XmlPullParser parser = Xml.newPullParser();	//由android.util.Xml创建一个XmlPullParser实例
	    	parser.setInput(is, "UTF-8");				//设置输入流 并指明编码方式

			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					ftps = new ArrayList<Ftp>();
					break;
				case XmlPullParser.START_TAG:
					if (parser.getName().equals("Enable")) {
						current_enable = Integer.parseInt(parser.nextText());
						while((current_enable == 0)&&(eventType != XmlPullParser.END_DOCUMENT))
						{
							
							eventType = parser.next();									
							currentnodename = parser.getName();	
							if("Enable".equals(currentnodename))
							{
								current_enable = Integer.parseInt(parser.nextText());
								
							}
							
						}
						Log.v("testplanparser","now get enable = 1,ye");
						
					}										
					else if (parser.getName().equals("Command")) {
							
						repeatime = Integer.parseInt(parser.getAttributeValue(0));
						
					} else if (parser.getName().equals("ID")) {
						currentid = parser.nextText();
						
						if(currentid.equalsIgnoreCase( "0x060C"))
						{
							Log.v("testplanparser","now get f");
							
							ftp = new Ftp();
							ftp.setNum(repeatime);
							
							
							eventType = parser.next();
							
							currentnodename = parser.getName(); 
							
							//currentnodevalue = parser.getText();
							
							
							while(! ((eventType == XmlPullParser.END_TAG)&&("Command".equals(currentnodename))))
							{
							
								//这句话应该能够提高效率
								/*if (parser.getEventType() != XmlPullParser.START_TAG) 
								{
									eventType = parser.next();
									currentnodename = parser.getName();
									continue;

								}*/


								
								
								/* <RemoteHost>222.68.172.16</RemoteHost> 
								  <Port>23</Port> 
								  <>test</Account> 
								  <>test</Password> 
								  <>60</TimeOut> 
								  <Passive>0</Passive> 
								  <Binary>1</Binary> 
								  <Download>1</Download> 
								  <RemoteFile>/ADTDOWNFILE/2M.rar</RemoteFile> 
								  <Interval>60</Interval> 
								  <APN>2</APN> 
(*/
								
								
								
								 /*if("RemoteHost".equals(currentnodename))
								 {							         
							         ftp.setHostname(parser.getText());							         
							     }else if("Port".equals(currentnodename)){						         
							    	 ftp.setPort(Integer.parseInt(parser.getText()));
							     }else if("Account".equals(currentnodename)){					         
							    	 ftp.setHostname(parser.getText());
							     }else if("Password".equals(currentnodename)){
							    	 ftp.setPassword(parser.getText());
							     
							     }else if("TimeOut".equals(currentnodename)){						         
							    	 ftp.setTimeout(Integer.parseInt(parser.getText()));
							     }else if("Download".equals(currentnodename)){	
							    	 if(0 == Integer.parseInt(parser.getText()))
							    	 ftp.setDown(false);
							    	 else
							    	 ftp.setDown(true);	 
							     }else if("RemoteFile".equals(currentnodename)){
							    	 ftp.setDownFilePath(parser.getText()); 
							     }else if("Interval".equals(currentnodename)){
							    	 ftp.setInterval(Integer.parseInt(parser.getText())); 
							     }
							     else
							     {
							    	  	 
							     }*/
								
								
								if("RemoteHost".equals(currentnodename))
								 {							         
							         ftp.setHostname(parser.nextText());							         
							     }else if("Port".equals(currentnodename)){						         
							    	 ftp.setPort(Integer.parseInt(parser.nextText()));
							     }else if("Account".equals(currentnodename)){					         
							    	 ftp.setUsername(parser.nextText());
							     }else if("Password".equals(currentnodename)){
							    	 ftp.setPassword(parser.nextText());							     
							     }else if("TimeOut".equals(currentnodename)){						         
							    	 ftp.setTimeout(Integer.parseInt(parser.nextText()));
							     }else if("Download".equals(currentnodename)){	
							    	 if(0 == Integer.parseInt(parser.nextText()))
							    	 ftp.setDown(false);
							    	 else
							    	 ftp.setDown(true);	 
							     }else if("RemoteFile".equals(currentnodename)){
							    	 ftp.setDownFilePath(parser.nextText()); 
							     }else if("Interval".equals(currentnodename)){
							    	 ftp.setInterval(Integer.parseInt(parser.nextText())); 
							     }
							     else
							     {
							    	  	 
							     }
								
								
							
								 eventType = parser.next();									
								 currentnodename = parser.getName();							
																							
								
							}
							
							ftps.add(ftp);
							ftp = null;	
							//eventType = parser.next();
							
							// currentnodename = parser.getName();		
								
						}
						else //unkonwn id
						{
                            eventType = parser.next();							
							currentnodename = parser.getName(); 
							
							//currentnodevalue = parser.getText();
							
							
							while(! (eventType == XmlPullParser.END_TAG)&&("Command".equals(currentnodename)))
							{
								eventType = parser.next();
								currentnodename = parser.getName();		
							}							
						}
					
						
						eventType = parser.next();
						
						//book.setId(Integer.parseInt(parser.getText()));
						
					} 
					else
					{
						eventType = parser.next();
						
					}
					
					
					
					
					break;
				case XmlPullParser.END_TAG:
					/*if (parser.getName().equals("Command")) {
						books.add(book);
						book = null;	
					}*/
					break;
				}
				eventType = parser.next();
			}
			return ftps;
		}

	//@Override
	/*public String serialize(List<Ftp> ftps) throws Exception {
		// XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		// XmlSerializer serializer = factory.newSerializer();

		XmlSerializer serializer = Xml.newSerializer(); // 由android.util.Xml创建一个XmlSerializer实例
		StringWriter writer = new StringWriter();
		serializer.setOutput(writer); // 设置输出方向为writer
		serializer.startDocument("UTF-8", true);
		serializer.startTag("", "books");
		for (Book book : books) {
			serializer.startTag("", "book");
			serializer.attribute("", "id", book.getId() + "");

			serializer.startTag("", "name");
			serializer.text(book.getName());
			serializer.endTag("", "name");

			serializer.startTag("", "price");
			serializer.text(book.getPrice() + "");
			serializer.endTag("", "price");

			serializer.endTag("", "book");
		}
		serializer.endTag("", "books");
		serializer.endDocument();

		return writer.toString();
	}*/

}
