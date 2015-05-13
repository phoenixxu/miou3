package com.datang.miou.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;
import com.datang.miou.datastructure.TestCommand;
import com.datang.miou.datastructure.TestScheme;
import com.datang.miou.utils.SDCardUtils;

import android.util.Log;
import android.util.Xml;

/**
 * testplan xml操作
 * 
 * @author chenzeming
 */
public class PullTestPlanParser implements TestPlanParser
{
	//	private static final String TAG = "PullTestSchemeParser";
	private static final String TAG = "chenzm";
	
	/**
     * 获取xml媒体流
     */ 
	public FileInputStream GetStream(String filename) throws Exception
	{
		
		FileInputStream inputStream = null;
		
		//	获取testplan目录名
		String filePath = SDCardUtils.getTestPlanPath() ;

		//	获取xml文件名
		File xmlFile = new File(filePath,filename);
		
		//	打印 testplan xml file name
	    Log.v(TAG,"file:" + filename);	//	add via chenzm 
	    Log.v(TAG,"testplanparser file:" + filePath + "/" + xmlFile.getName());	//	add via chenzm 
	    
		try
		{
			if(xmlFile.exists())
			{
				inputStream = new FileInputStream(xmlFile);	
				Log.i(TAG, "Get xml FileInputStream");	//	add via chenzm
				return inputStream;
			}
			else
			{
				return null;
			}
		}
		catch(FileNotFoundException e)
		{
			Log.w("open file", "Error while new XML file from ", e);
		    return null;
		}
	}
	
	/**
     * 解析 testplan xml
     */ 
	@Override
	public List<TestScheme> parse(InputStream is) throws Exception 
	{
		XmlPullParser parser = Xml.newPullParser();
		TestScheme mTestScheme = null;
		List<TestScheme> mTestSchemes = null;
		
		try
		{
			parser.setInput(is, "UTF-8");
			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT)
			{
				//	Log.i(TAG, "eventType name: " + XmlPullParser.TYPES[eventType]);	//	add via chenzm
				switch (eventType)
				{
					case XmlPullParser.START_DOCUMENT:		
						break;
						
					case XmlPullParser.START_TAG:
						//	Log.i(TAG, "|- start_tag name: " + parser.getName());	//	add via chenzm
						if (parser.getName().equals("AutoTestUnit")) 
						{
							Log.i(TAG, "new AutoTestUnit unit");
							parser.nextToken();
						} 
						else if (parser.getName().equals("TestUnit"))
						{
							Log.i(TAG, "new TestUnit unit");
							mTestSchemes = new ArrayList<TestScheme>();
						} 
						else if (parser.getName().equals("TestScheme")) 
						{
							Log.i(TAG, "new scheme");
							mTestScheme = new TestScheme();
						}
						else if (parser.getName().equals("Enable")) 
						{
							eventType = parser.next();
							String enable = parser.getText();
							Log.i(TAG, "enable = " + enable);
							if(mTestScheme != null)
								mTestScheme.setEnable(enable);
						} 
						else if (parser.getName().equals("ModelLock")) 
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.setModelLock(parser.getText());
						} 
						else if (parser.getName().equals("DESC")) 
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.setDesc(parser.getText());
						}
						else if (parser.getName().equals("EXNO"))
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.setExno(parser.getText());
						} 
						else if (parser.getName().equals("TimeCondition")) 
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.setTimeCondition(parser.getText());
						} 
						else if (parser.getName().equals("ExecutiveDate"))
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.setExecutiveDate(parser.getText());
						} 
						else if (parser.getName().equals("BeginTime")) 
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getTime().setBeginTime(parser.getText());
						} 
						else if (parser.getName().equals("EndTime")) 
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getTime().setEndTime(parser.getText());
						} 
						else if (parser.getName().equals("CommandList")) 
						{
							String repeat = parser.getAttributeValue(null, "Repeat");
							if(mTestScheme != null)
								mTestScheme.setRepeat(repeat);
						} 
						else if (parser.getName().equals("Synchronize"))
						{
							String type = parser.getAttributeValue(null, "type");
							if(mTestScheme != null)
								mTestScheme.getCommandList().setType(type);
						} 
						else if (parser.getName().equals("Command"))
						{
							String repeat = parser.getAttributeValue(null, "Repeat");
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setRepeat(repeat);
						} 
						else if (parser.getName().equals("ID")) 
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setId(parser.getText());		
						}
						else if (parser.getName().equals("CallNumber"))
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setCallNumber(parser.getText());		
						} 
						else if (parser.getName().equals("RandomCall"))
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setRandomCall(parser.getText());		
						} 
						else if (parser.getName().equals("Duration")) 
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setDuration(parser.getText());		
						} 
						else if (parser.getName().equals("Interval"))
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setInterval(parser.getText());		
						} 
						else if (parser.getName().equals("MaxTime"))
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setMaxTime(parser.getText());		
						} 
						else if (parser.getName().equals("TestMOS")) 
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setTestMos(parser.getText());		
						} 
						else if (parser.getName().equals("CallMOSServer")) 
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setCallMosServer(parser.getText());		
						}
						else if (parser.getName().equals("MOSLimit")) 
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setMosLimit(parser.getText());		
						}
						else if (parser.getName().equals("WaitTimes"))
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setWaitTimes(parser.getText());		
						}
						else if (parser.getName().equals("Keeptime")) 
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setKeepTime(parser.getText());		
						}
						else if (parser.getName().equals("APN"))
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setApn(parser.getText());	
						} 
						else if (parser.getName().equals("IP"))
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setIp(parser.getText());		
						}
						else if (parser.getName().equals("Packagesize")) 
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setPackageSize(parser.getText());		
						} 
						else if (parser.getName().equals("TimeOut")) 
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setTimeOut(parser.getText());		
						}
						else if (parser.getName().equals("URL")) 
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setUrl(parser.getText());		
						} 
						else if (parser.getName().equals("Agent"))
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setAgent(parser.getText());		
						}
						else if (parser.getName().equals("ConnectionMode")) 
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setConnectionMode(parser.getText());		
						} 
						else if (parser.getName().equals("Gateway"))
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setGateWay(parser.getText());		
						} 
						else if (parser.getName().equals("Port"))
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setPort(parser.getText());		
						} 
						else if (parser.getName().equals("ServerCenterAddress"))
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setServerCenterAddress(parser.getText());		
						} 
						else if (parser.getName().equals("Destination"))
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setDestination(parser.getText());		
						} 
						else if (parser.getName().equals("Mode"))
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setMode(parser.getText());		
						} 
						else if (parser.getName().equals("Text"))
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setText(parser.getText());		
						} 
						else if (parser.getName().equals("Report")) 
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setReport(parser.getText());		
						} 
						else if (parser.getName().equals("Content"))
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setContent(parser.getText());		
						} 
						else if (parser.getName().equals("Account"))
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setAccount(parser.getText());		
						} 
						else if (parser.getName().equals("Password"))
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setPassword(parser.getText());	
						} 
						else if (parser.getName().equals("ServerAddress"))
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setServerAddress(parser.getText());		
						} 
						else if (parser.getName().equals("MediaFileSize"))
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setMediaFileSize(parser.getText());		
						} 
						else if (parser.getName().equals("PTimeOut"))
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setPTimeOut(parser.getText());		
						} 
						else if (parser.getName().equals("SyncMSNOs"))
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setSyncMsno(parser.getText());		
						} 
						else if (parser.getName().equals("RemoteHost")) 
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setRemoteHost(parser.getText());		
						} 
						else if (parser.getName().equals("Passive"))
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setPassive(parser.getText());		
						} 
						else if (parser.getName().equals("Binary")) 
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setBinary(parser.getText());		
						} 
						else if (parser.getName().equals("Download"))
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setDownload(parser.getText());		
						} 
						else if (parser.getName().equals("RemoteFile"))
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setRemoteFile(parser.getText());		
						}
						else if (parser.getName().equals("Version")) 
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setVersion(parser.getText());	
						}
						else if (parser.getName().equals("Username")) 
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setUserName(parser.getText());		
						} 
						else if (parser.getName().equals("RTP")) 
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setRtp(parser.getText());		
						}
						else if (parser.getName().equals("RtspHttpPort")) 
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setRtspHttpPort(parser.getText());		
						}
						else if (parser.getName().equals("LocalRTPport")) 
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setLocalRtpPort(parser.getText());		
						} 
						else if (parser.getName().equals("PreBufferLength")) 
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setPreBufferLength(parser.getText());		
						}
						else if (parser.getName().equals("RebufferLength"))
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setRebufferLength(parser.getText());		
						}
						else if (parser.getName().equals("PlayTime")) 
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setPlayTime(parser.getText());		
						}
						else if (parser.getName().equals("BufferLength"))
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setBufferLength(parser.getText());		
						}
						else if (parser.getName().equals("BufferPlayThreshold"))
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setBufferPlayThreshold(parser.getText());		
						} 
						else if (parser.getName().equals("MailServer")) 
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setMailServer(parser.getText());		
						}
						else if (parser.getName().equals("Deletemail"))
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setDeleteMail(parser.getText());		
						} 
						else if (parser.getName().equals("Path"))
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setPath(parser.getText());		
						}
						else if (parser.getName().equals("SSL")) 
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setSsl(parser.getText());		
						} 
						else if (parser.getName().equals("Sender"))
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setSender(parser.getText());		
						} 
						else if (parser.getName().equals("From")) 
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setFrom(parser.getText());		
						} 
						else if (parser.getName().equals("To"))
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setTo(parser.getText());		
						}
						else if (parser.getName().equals("FileSize"))
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setFileSize(parser.getText());		
						}
						else if (parser.getName().equals("Subject")) 
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setSubject(parser.getText());		
						} 
						else if (parser.getName().equals("Body")) 
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setBody(parser.getText());		
						} 
						else if (parser.getName().equals("Address"))
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setAddress(parser.getText());		
						}
						else if (parser.getName().equals("Authentication"))
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setAuthentication(parser.getText());		
						} 
						else if (parser.getName().equals("Encoding")) 
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setEncoding(parser.getText());		
						} 
						else if (parser.getName().equals("HTML"))
						{
							eventType = parser.next();
							if(mTestScheme != null)
								mTestScheme.getCommandList().getCommand().setHtml(parser.getText());		
						}
						break;
						
					case XmlPullParser.END_TAG:
						if ((mTestScheme != null) && (parser.getName().equals("TestScheme")))
						{
							if(mTestSchemes != null)
							{
								//	Log.i(TAG, "end_tag name: " + parser.getName());	//	add via chenzm
								mTestSchemes.add(mTestScheme);
								mTestScheme = null;
							}
						}
						break;
				}
				eventType = parser.next();
			}
		}
		catch(XmlPullParserException e)
		{
			Log.i(TAG, "during parse by pull set input.",e);	//	add via chenzm
		}
		catch(IOException e)
		{
			Log.i(TAG, "during parse by pull next.",e);	//	add via chenzm
		}

		return mTestSchemes;
	}
	
	/**
     * 生成 testplan xml
     */
	public static void writeXml(List<TestCommand> schemes, Writer writer) throws IllegalArgumentException, IllegalStateException, IOException
	{
		XmlSerializer s = Xml.newSerializer();
		s.setOutput(writer);
		
		s.startDocument("UTF-8", true);
		s.startTag(null, "root");
		
		for (TestCommand command : schemes)
		{
			formCommand(s, command);
		}
		
		s.endTag(null, "root");
		
		s.endDocument();
	}
	
	/**
     * 形成Command
     */
	private static void formCommand(XmlSerializer s, TestCommand command) throws IllegalStateException, IOException 
	{
		// TODO Auto-generated method stub
		s.startTag(null, "TestScheme");
		
		if (command.getId() != null)
		{
			s.startTag(null, "ID");
			s.text(command.getId());
			s.endTag(null, "ID");
		}
		
		if (command.getCallNumber() != null)
		{
			s.startTag(null, "CallNumber");
			s.text(command.getCallNumber());
			s.endTag(null, "CallNumber");
		}
		
		if (command.getRandomCall() != null) 
		{
			s.startTag(null, "RandomCall");
			s.text(command.getRandomCall());
			s.endTag(null, "RandomCall");
		}
		
		if (command.getDuration() != null)
		{
			s.startTag(null, "Duration");
			s.text(command.getDuration());
			s.endTag(null, "Duration");
		}
		
		if (command.getInterval() != null) 
		{
			s.startTag(null, "Interval");
			s.text(command.getInterval());
			s.endTag(null, "Interval");
		}
		
		if (command.getMaxTime() != null) 
		{
			s.startTag(null, "MaxTime");
			s.text(command.getMaxTime());
			s.endTag(null, "MaxTime");
		}
		
		if (command.getTestMos() != null)
		{
			s.startTag(null, "TestMOS");
			s.text(command.getTestMos());
			s.endTag(null, "TestMOS");
		}
		
		if (command.getCallMosServer() != null)
		{
			s.startTag(null, "CallMOSServer");
			s.text(command.getCallMosServer());
			s.endTag(null, "CallMOSServer");
		}
		
		if (command.getMosLimit() != null) 
		{
			s.startTag(null, "MOSLimit");
			s.text(command.getMosLimit());
			s.endTag(null, "MOSLimit");
		}
		
		if (command.getWaitTimes() != null) 
		{
			s.startTag(null, "WaitTimes");
			s.text(command.getWaitTimes());
			s.endTag(null, "WaitTimes");
		}
		
		if (command.getKeepTime() != null)
		{
			s.startTag(null, "Keeptime");
			s.text(command.getKeepTime());
			s.endTag(null, "Keeptime");
		}
		
		if (command.getApn() != null)
		{
			s.startTag(null, "APN");
			s.text(command.getApn());
			s.endTag(null, "APN");
		}
		
		if (command.getIp() != null) 
		{
			s.startTag(null, "IP");
			s.text(command.getIp());
			s.endTag(null, "IP");
		}
		
		if (command.getPackageSize() != null)
		{
			s.startTag(null, "Packagesize");
			s.text(command.getPackageSize());
			s.endTag(null, "Packagesize");
		}
		
		if (command.getTimeOut() != null) 
		{
			s.startTag(null, "TimeOut");
			s.text(command.getTimeOut());
			s.endTag(null, "TimeOut");
		}
		
		if (command.getUrl() != null) 
		{
			s.startTag(null, "URL");
			s.text(command.getUrl());
			s.endTag(null, "URL");
		}
		
		if (command.getAgent() != null)
		{
			s.startTag(null, "Agent");
			s.text(command.getAgent());
			s.endTag(null, "Agent");
		}
		
		if (command.getConnectionMode() != null) 
		{
			s.startTag(null, "ConnectionMode");
			s.text(command.getConnectionMode());
			s.endTag(null, "ConnectionMode");
		}
		
		if (command.getGateWay() != null) 
		{
			s.startTag(null, "Gateway");
			s.text(command.getGateWay());
			s.endTag(null, "Gateway");
		}
		
		if (command.getPort() != null) 
		{
			s.startTag(null, "Port");
			s.text(command.getPort());
			s.endTag(null, "Port");
		}
		
		if (command.getServerCenterAddress() != null)
		{
			s.startTag(null, "ServerCenterAddress");
			s.text(command.getServerCenterAddress());
			s.endTag(null, "ServerCenterAddress");
		}
		
		if (command.getDestination() != null) 
		{
			s.startTag(null, "Destination");
			s.text(command.getDestination());
			s.endTag(null, "Destination");
		}
		
		if (command.getMode() != null) 
		{
			s.startTag(null, "Mode");
			s.text(command.getMode());
			s.endTag(null, "Mode");
		}
		
		if (command.getText() != null)
		{
			s.startTag(null, "Text");
			s.text(command.getText());
			s.endTag(null, "Text");
		}
		
		if (command.getReport() != null) 
		{
			s.startTag(null, "Report");
			s.text(command.getReport());
			s.endTag(null, "Report");
		}
		
		if (command.getContent() != null) 
		{
			s.startTag(null, "Content");
			s.text(command.getContent());
			s.endTag(null, "Content");
		}
		
		if (command.getAccount() != null)
		{
			s.startTag(null, "Account");
			s.text(command.getAccount());
			s.endTag(null, "Account");
		}
		
		if (command.getPassword() != null)
		{
			s.startTag(null, "Password");
			s.text(command.getPassword());
			s.endTag(null, "Password");
		}
		
		if (command.getServerAddress() != null)
		{
			s.startTag(null, "ServerAddress");
			s.text(command.getServerAddress());
			s.endTag(null, "ServerAddress");
		}
		
		if (command.getSyncMsno() != null) 
		{
			s.startTag(null, "SyncMSNOs");
			s.text(command.getSyncMsno());
			s.endTag(null, "SyncMSNOs");
		}
		
		if (command.getMediaFileSize() != null)
		{
			s.startTag(null, "MediaFileSize");
			s.text(command.getMediaFileSize());
			s.endTag(null, "MediaFileSize");
		}
		
		if (command.getPTimeOut() != null) 
		{
			s.startTag(null, "PTimeOut");
			s.text(command.getPTimeOut());
			s.endTag(null, "PTimeOut");
		}
		
		if (command.getRemoteHost() != null)
		{
			s.startTag(null, "RemoteHost");
			s.text(command.getRemoteHost());
			s.endTag(null, "RemoteHost");
		}
		
		if (command.getPassive() != null)
		{
			s.startTag(null, "Passive");
			s.text(command.getPassive());
			s.endTag(null, "Passive");
		}
		
		if (command.getBinary() != null) 
		{
			s.startTag(null, "Binary");
			s.text(command.getBinary());
			s.endTag(null, "Binary");
		}
		
		if (command.getDownload() != null)
		{
			s.startTag(null, "Download");
			s.text(command.getDownload());
			s.endTag(null, "Download");
		}
		
		if (command.getRemoteFile() != null)
		{
			s.startTag(null, "RemoteFile");
			s.text(command.getRemoteFile());
			s.endTag(null, "RemoteFile");
		}
		
		if (command.getVersion() != null)
		{
			s.startTag(null, "Version");
			s.text(command.getVersion());
			s.endTag(null, "Version");
		}
		
		if (command.getUserName() != null)
		{
			s.startTag(null, "Username");
			s.text(command.getUserName());
			s.endTag(null, "Username");
		}
		
		if (command.getRtp() != null) 
		{
			s.startTag(null, "RTP");
			s.text(command.getRtp());
			s.endTag(null, "RTP");
		}
		
		if (command.getRtspHttpPort() != null)
		{
			s.startTag(null, "RtspHttpPort");
			s.text(command.getRtspHttpPort());
			s.endTag(null, "RtspHttpPort");
		}
		
		if (command.getLocalRtpPort() != null)
		{
			s.startTag(null, "LocalRTPport");
			s.text(command.getLocalRtpPort());
			s.endTag(null, "LocalRTPport");
		}
		
		if (command.getPreBufferLength() != null)
		{
			s.startTag(null, "PreBufferLength");
			s.text(command.getPreBufferLength());
			s.endTag(null, "PreBufferLength");
		}
		
		if (command.getRebufferLength() != null)
		{
			s.startTag(null, "RebufferLength");
			s.text(command.getRebufferLength());
			s.endTag(null, "RebufferLength");
		}
		
		if (command.getPlayTime() != null)
		{
			s.startTag(null, "PlayTime");
			s.text(command.getPlayTime());
			s.endTag(null, "PlayTime");
		}
		
		if (command.getBufferLength() != null) 
		{
			s.startTag(null, "BufferLength");
			s.text(command.getBufferLength());
			s.endTag(null, "BufferLength");
		}
		
		if (command.getBufferPlayThreshold() != null)
		{
			s.startTag(null, "BufferPlayThreshold");
			s.text(command.getBufferPlayThreshold());
			s.endTag(null, "BufferPlayThreshold");
		}
		
		if (command.getMailServer() != null) 
		{
			s.startTag(null, "MailServer");
			s.text(command.getMailServer());
			s.endTag(null, "MailServer");
		}
		
		if (command.getDeleteMail() != null)
		{
			s.startTag(null, "Deletemail");
			s.text(command.getDeleteMail());
			s.endTag(null, "Deletemail");
		}
		
		if (command.getPath() != null) 
		{
			s.startTag(null, "Path");
			s.text(command.getPath());
			s.endTag(null, "Path");
		}
		
		if (command.getSsl() != null)
		{
			s.startTag(null, "SSL");
			s.text(command.getSsl());
			s.endTag(null, "SSL");
		}
		
		if (command.getSender() != null)
		{
			s.startTag(null, "Sender");
			s.text(command.getSender());
			s.endTag(null, "Sender");
		}
		
		if (command.getFrom() != null) 
		{
			s.startTag(null, "From");
			s.text(command.getFrom());
			s.endTag(null, "From");
		}
		
		if (command.getTo() != null) 
		{
			s.startTag(null, "To");
			s.text(command.getTo());
			s.endTag(null, "To");
		}
		
		if (command.getFileSize() != null)
		{
			s.startTag(null, "FileSize");
			s.text(command.getFileSize());
			s.endTag(null, "FileSize");
		}
		
		if (command.getSubject() != null)
		{
			s.startTag(null, "Subject");
			s.text(command.getSubject());
			s.endTag(null, "Subject");
		}
		
		if (command.getBody() != null)
		{
			s.startTag(null, "Body");
			s.text(command.getBody());
			s.endTag(null, "Body");
		}
		
		if (command.getAuthentication() != null)
		{
			s.startTag(null, "Authentication");
			s.text(command.getAuthentication());
			s.endTag(null, "Authentication");
		}
		
		if (command.getEncoding() != null)
		{
			s.startTag(null, "Encoding");
			s.text(command.getEncoding());
			s.endTag(null, "Encoding");
		}
		
		if (command.getHtml() != null) 
		{
			s.startTag(null, "HTML");
			s.text(command.getHtml());
			s.endTag(null, "HTML");
		}
		
		if (command.getAddress() != null)
		{
			s.startTag(null, "Address");
			s.text(command.getAddress());
			s.endTag(null, "Address");
		}
		
		if (command.getProxy() != null) {
			s.startTag(null, "Proxy");
			s.text(command.getProxy());
			s.endTag(null, "Proxy");
		}
		
		if (command.getProxyType() != null) {
			s.startTag(null, "ProxyType");
			s.text(command.getProxyType());
			s.endTag(null, "ProxyType");
		}
		
		s.endTag(null, "TestScheme");
	}
	
	public String serialize(List<TestScheme> schemes) throws Exception 
	{
		return null;
	}

}
