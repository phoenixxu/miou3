package com.datang.miou.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import com.datang.miou.datastructure.AutoTestUnit;
import com.datang.miou.datastructure.GeneralItem;
import com.datang.miou.datastructure.LogProcess;
import com.datang.miou.datastructure.NetWork;
import com.datang.miou.datastructure.SwitchLog;
import com.datang.miou.datastructure.Synchronize;
import com.datang.miou.datastructure.TestCommand;
import com.datang.miou.datastructure.TestScheme;
import com.datang.miou.datastructure.TestScript;
import com.datang.miou.datastructure.Time;

import android.util.Log;
import android.util.Xml;

public class PullTestScriptParser {


	private static final String TAG = "PullTestScriptParser";
	private TestScript mTestScript;
	private NetWork mNetWork;
	private LogProcess mLogProcess;
	private SwitchLog mSwitchLog;
	private GeneralItem mGeneralItem;
	private AutoTestUnit mAutoTestUnit;
	private ArrayList<TestScheme> mTestUnit;
	private TestScheme mTestScheme;
	private Time mTime;
	private Synchronize mCommandList;
	private TestCommand mCommand;
	private boolean parseCommand;
	private boolean parseNetWork;
	private boolean parseLogProcess;

	
	public TestScript parse(InputStream is) throws Exception {
		mTestScript = null;
		
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(is, "UTF-8");
		
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					mTestScript = new TestScript();
					break;
				case XmlPullParser.START_TAG:
					if (parser.getName().equals("AutoTestUnit")) {
						mAutoTestUnit = new AutoTestUnit();
					} else if (parser.getName().equals("TestUnit")) {
						mTestUnit = new ArrayList<TestScheme>();
					} else if (parser.getName().equals("TestScheme")) {
						mTestScheme = new TestScheme();
						parseCommand = true;
					} else if (parser.getName().equals("Network")) {
						mNetWork = new NetWork();
						parseNetWork = true;
					} else if (parser.getName().equals("PortalIP")) {
						eventType = parser.next();
						mNetWork.setPortalIp(parser.getText());
					} else if (parser.getName().equals("PortalPort")) {
						eventType = parser.next();
						mNetWork.setPortalPort(parser.getText());
					} else if (parser.getName().equals("User")) {
						eventType = parser.next();
						mNetWork.setUser(parser.getText());
					} else if (parser.getName().equals("Password")) {
						eventType = parser.next();
						if (parseNetWork) {
							mNetWork.setPassword(parser.getText());
						} else {
							mCommand.setPassword(parser.getText());	
						}		
					} else if (parser.getName().equals("UseLAN")) {
						eventType = parser.next();
						mNetWork.setUserLan(parser.getText());
					} else if (parser.getName().equals("SendData")) {
						eventType = parser.next();
						mNetWork.setSendData(parser.getText());
					} else if (parser.getName().equals("DialNumber")) {
						eventType = parser.next();
						mNetWork.setDialNumber(parser.getText());
					} else if (parser.getName().equals("APN")) {
						eventType = parser.next();
						if (parseNetWork) {
							mNetWork.setApn(parser.getText());
						} else {
							mCommand.setApn(parser.getText());		
						}
					} else if (parser.getName().equals("DialUpUser")) {
						eventType = parser.next();
						mNetWork.setDialUpUser(parser.getText());
					} else if (parser.getName().equals("DialUpPassword")) {
						eventType = parser.next();
						mNetWork.setDialUpPassword(parser.getText());
					} else if (parser.getName().equals("LogProcess")) {
						mLogProcess = new LogProcess();
					} else if (parser.getName().equals("SwitchLog")) {
						mSwitchLog = new SwitchLog();
					} else if (parser.getName().equals("Enable")) {
						eventType = parser.next();
						if (parseCommand) {
							mTestScheme.setEnable(parser.getText());
						} else {
							mSwitchLog.setEnable(parser.getText());
						}
					} else if (parser.getName().equals("Type")) {
						eventType = parser.next();
						mSwitchLog.setType(parser.getText());
					} else if (parser.getName().equals("TestTime")) {
						eventType = parser.next();
						mSwitchLog.setTestTime(parser.getText());
					} else if (parser.getName().equals("Condition")) {
						eventType = parser.next();
						mSwitchLog.setCondition(parser.getText());
					} else if (parser.getName().equals("GeneralItem")) {
						mGeneralItem = new GeneralItem();
					} else if (parser.getName().equals("SpeedCondition")) {
						eventType = parser.next();
						mGeneralItem.setSpeedCondition(parser.getText());
					} else if (parser.getName().equals("GPSCondition")) {
						eventType = parser.next();
						mGeneralItem.setGpsCondition(parser.getText());
					} else if (parser.getName().equals("ModelLock")) {
						eventType = parser.next();
						mTestScheme.setModelLock(parser.getText());
					} else if (parser.getName().equals("DESC")) {
						eventType = parser.next();
						mTestScheme.setDesc(parser.getText());
					} else if (parser.getName().equals("EXNO")) {
						eventType = parser.next();
						mTestScheme.setExno(parser.getText());
					} else if (parser.getName().equals("TimeCondition")) {
						eventType = parser.next();
						mTestScheme.setTimeCondition(parser.getText());
					} else if (parser.getName().equals("ExecutiveDate")) {
						eventType = parser.next();
						mTestScheme.setExecutiveDate(parser.getText());
					} else if (parser.getName().equals("Time")) {
						mTime = new Time();
					} else if (parser.getName().equals("BeginTime")) {
						eventType = parser.next();
						mTime.setBeginTime(parser.getText());
					} else if (parser.getName().equals("EndTime")) {
						eventType = parser.next();
						mTime.setEndTime(parser.getText());
					} else if (parser.getName().equals("CommandList")) {
						mCommandList = new Synchronize();
						String repeat = parser.getAttributeValue(null, "Repeat");
						mTestScheme.setRepeat(repeat);
					} else if (parser.getName().equals("Synchronize")) {
						String type = parser.getAttributeValue(null, "type");
						mCommandList.setType(type);
					} else if (parser.getName().equals("Command")) {
						mCommand = new TestCommand();
						String repeat = parser.getAttributeValue(null, "Repeat");
						mCommand.setRepeat(repeat);
					} else if (parser.getName().equals("ID")) {
						eventType = parser.next();
						mCommand.setId(parser.getText());		
					} else if (parser.getName().equals("CallNumber")) {
						eventType = parser.next();
						mCommand.setCallNumber(parser.getText());		
					} else if (parser.getName().equals("RandomCall")) {
						eventType = parser.next();
						mCommand.setRandomCall(parser.getText());		
					} else if (parser.getName().equals("Duration")) {
						eventType = parser.next();
						mCommand.setDuration(parser.getText());		
					} else if (parser.getName().equals("Interval")) {
						eventType = parser.next();
						mCommand.setInterval(parser.getText());		
					} else if (parser.getName().equals("MaxTime")) {
						eventType = parser.next();
						mCommand.setMaxTime(parser.getText());		
					} else if (parser.getName().equals("TestMOS")) {
						eventType = parser.next();
						mCommand.setTestMos(parser.getText());		
					} else if (parser.getName().equals("CallMOSServer")) {
						eventType = parser.next();
						mCommand.setCallMosServer(parser.getText());		
					} else if (parser.getName().equals("MOSLimit")) {
						eventType = parser.next();
						mCommand.setMosLimit(parser.getText());		
					} else if (parser.getName().equals("WaitTimes")) {
						eventType = parser.next();
						mCommand.setWaitTimes(parser.getText());		
					} else if (parser.getName().equals("Keeptime")) {
						eventType = parser.next();
						mCommand.setKeepTime(parser.getText());		
					}  else if (parser.getName().equals("IP")) {
						eventType = parser.next();
						mCommand.setIp(parser.getText());		
					} else if (parser.getName().equals("Packagesize")) {
						eventType = parser.next();
						mCommand.setPackageSize(parser.getText());		
					} else if (parser.getName().equals("TimeOut")) {
						eventType = parser.next();
						mCommand.setTimeOut(parser.getText());		
					} else if (parser.getName().equals("URL")) {
						eventType = parser.next();
						mCommand.setUrl(parser.getText());		
					} else if (parser.getName().equals("Agent")) {
						eventType = parser.next();
						mCommand.setAgent(parser.getText());		
					} else if (parser.getName().equals("ConnectionMode")) {
						eventType = parser.next();
						mCommand.setConnectionMode(parser.getText());		
					} else if (parser.getName().equals("Gateway")) {
						eventType = parser.next();
						mCommand.setGateWay(parser.getText());		
					} else if (parser.getName().equals("Port")) {
						eventType = parser.next();
						mCommand.setPort(parser.getText());		
					} else if (parser.getName().equals("ServerCenterAddress")) {
						eventType = parser.next();
						mCommand.setServerCenterAddress(parser.getText());		
					} else if (parser.getName().equals("Destination")) {
						eventType = parser.next();
						mCommand.setDestination(parser.getText());		
					} else if (parser.getName().equals("Mode")) {
						eventType = parser.next();
						mCommand.setMode(parser.getText());		
					} else if (parser.getName().equals("Text")) {
						eventType = parser.next();
						mCommand.setText(parser.getText());		
					} else if (parser.getName().equals("Report")) {
						eventType = parser.next();
						mCommand.setReport(parser.getText());		
					} else if (parser.getName().equals("Content")) {
						eventType = parser.next();
						mCommand.setContent(parser.getText());		
					} else if (parser.getName().equals("Account")) {
						eventType = parser.next();
						mCommand.setAccount(parser.getText());		
					} else if (parser.getName().equals("ServerAddress")) {
						eventType = parser.next();
						mCommand.setServerAddress(parser.getText());		
					} else if (parser.getName().equals("MediaFileSize")) {
						eventType = parser.next();
						mCommand.setMediaFileSize(parser.getText());		
					} else if (parser.getName().equals("SyncMSNOs")) {
						eventType = parser.next();
						Log.i(TAG, "syncmsnos : " + parser.getText());
						mCommand.setSyncMsno(parser.getText());		
					}else if (parser.getName().equals("PTimeOut")) {
						eventType = parser.next();
						mCommand.setPTimeOut(parser.getText());		
					} else if (parser.getName().equals("RemoteHost")) {
						eventType = parser.next();
						mCommand.setRemoteHost(parser.getText());		
					} else if (parser.getName().equals("Passive")) {
						eventType = parser.next();
						mCommand.setPassive(parser.getText());		
					} else if (parser.getName().equals("Binary")) {
						eventType = parser.next();
						mCommand.setBinary(parser.getText());		
					} else if (parser.getName().equals("Download")) {
						eventType = parser.next();
						mCommand.setDownload(parser.getText());		
					} else if (parser.getName().equals("RemoteFile")) {
						eventType = parser.next();
						mCommand.setRemoteFile(parser.getText());		
					} else if (parser.getName().equals("Version")) {
						eventType = parser.next();
						if (parseCommand) {
							mCommand.setVersion(parser.getText());
						} else {
							mAutoTestUnit.setVersion(parser.getText());
						}
					} else if (parser.getName().equals("Username")) {
						eventType = parser.next();
						mCommand.setUserName(parser.getText());		
					} else if (parser.getName().equals("RTP")) {
						eventType = parser.next();
						mCommand.setRtp(parser.getText());		
					} else if (parser.getName().equals("RtspHttpPort")) {
						eventType = parser.next();
						mCommand.setRtspHttpPort(parser.getText());		
					} else if (parser.getName().equals("LocalRTPport")) {
						eventType = parser.next();
						mCommand.setLocalRtpPort(parser.getText());		
					} else if (parser.getName().equals("PreBufferLength")) {
						eventType = parser.next();
						mCommand.setPreBufferLength(parser.getText());		
					} else if (parser.getName().equals("RebufferLength")) {
						eventType = parser.next();
						mCommand.setRebufferLength(parser.getText());		
					} else if (parser.getName().equals("PlayTime")) {
						eventType = parser.next();
						mCommand.setPlayTime(parser.getText());		
					} else if (parser.getName().equals("BufferLength")) {
						eventType = parser.next();
						mCommand.setBufferLength(parser.getText());		
					} else if (parser.getName().equals("BufferPlayThreshold")) {
						eventType = parser.next();
						mCommand.setBufferPlayThreshold(parser.getText());		
					} else if (parser.getName().equals("MailServer")) {
						eventType = parser.next();
						mCommand.setMailServer(parser.getText());		
					} else if (parser.getName().equals("Deletemail")) {
						eventType = parser.next();
						mCommand.setDeleteMail(parser.getText());		
					} else if (parser.getName().equals("Path")) {
						eventType = parser.next();
						mCommand.setPath(parser.getText());		
					} else if (parser.getName().equals("SSL")) {
						eventType = parser.next();
						mCommand.setSsl(parser.getText());		
					} else if (parser.getName().equals("Sender")) {
						eventType = parser.next();
						mCommand.setSender(parser.getText());		
					} else if (parser.getName().equals("From")) {
						eventType = parser.next();
						mCommand.setFrom(parser.getText());		
					} else if (parser.getName().equals("To")) {
						eventType = parser.next();
						mCommand.setTo(parser.getText());		
					} else if (parser.getName().equals("FileSize")) {
						eventType = parser.next();
						mCommand.setFileSize(parser.getText());		
					} else if (parser.getName().equals("Subject")) {
						eventType = parser.next();
						mCommand.setSubject(parser.getText());		
					} else if (parser.getName().equals("Body")) {
						eventType = parser.next();
						mCommand.setBody(parser.getText());		
					} else if (parser.getName().equals("Address")) {
						eventType = parser.next();
						mCommand.setAddress(parser.getText());		
					} else if (parser.getName().equals("Authentication")) {
						eventType = parser.next();
						mCommand.setAuthentication(parser.getText());		
					} else if (parser.getName().equals("Encoding")) {
						eventType = parser.next();
						mCommand.setEncoding(parser.getText());		
					} else if (parser.getName().equals("HTML")) {
						eventType = parser.next();
						mCommand.setHtml(parser.getText());		
					} else if (parser.getName().equals("Proxy")) {
						eventType = parser.next();
						mCommand.setProxy(parser.getText());
					} else if (parser.getName().equals("ProxyType")) {
						eventType = parser.next();
						mCommand.setProxyType(parser.getText());
					}
					
					break;
				case XmlPullParser.END_TAG:
					if (parser.getName().equals("GeneralItem")) {
						mAutoTestUnit.setGeneralItem(mGeneralItem);
					}
					if (parser.getName().equals("SwitchLog")) {
						mLogProcess.setSwitchLog(mSwitchLog);
					}
					if (parser.getName().equals("LogProcess")) {
						mAutoTestUnit.setLogProcess(mLogProcess);
					}
					if (parser.getName().equals("Network")) {
						mAutoTestUnit.setNetWork(mNetWork);
						parseNetWork = false;
					}
					if (parser.getName().equals("AutoTestUnit")) {
						
					}
					if (parser.getName().equals("Time")) {
						mTestScheme.setTime(mTime);
					}
					if (parser.getName().equals("CommandList")) {
						mTestScheme.setCommandList(mCommandList);
						parseCommand = false;
					}
					if (parser.getName().equals("Synchronize")) {
						mCommandList.setCommand(mCommand);
					}
					if (parser.getName().equals("TestScheme")) {
						mTestUnit.add(mTestScheme);
					}
					if (parser.getName().equals("TestUnit")) {
						mTestScript.setTestUnit(mTestUnit);
					}
					if (parser.getName().equals("AutoTestUnit")) {
						mTestScript.setAutoTestUnit(mAutoTestUnit);
					}
					break;
			}
			eventType = parser.next();
		}
		return mTestScript;
	}
	
	public static void writeXml(TestScript script, Writer writer) throws IllegalArgumentException, IllegalStateException, IOException {
		XmlSerializer s = Xml.newSerializer();
		s.setOutput(writer);
		
		s.startDocument("UTF-8", true);
		s.startTag(null, "root");
		
		formAutoTestUnit(s, script);
			
		formTestUnit(s, script);
		
		s.endTag(null, "root");
		
		s.endDocument();
	}
	
	private static void formTestUnit(XmlSerializer s, TestScript script) throws IllegalArgumentException, IllegalStateException, IOException {
		// TODO Auto-generated method stub
		s.startTag(null, "TestUnit");
		
		Log.i(TAG, "shemes = " + script.getTestUnit().size());
		for (TestScheme scheme : script.getTestUnit()) {
			formTestScheme(s, scheme);
		}
		
		s.endTag(null, "TestUnit");
	}

	private static void formTestScheme(XmlSerializer s, TestScheme scheme) throws IllegalArgumentException, IllegalStateException, IOException {
		// TODO Auto-generated method stub
		s.startTag(null, "TestScheme");
		
		s.startTag(null, "Enable");
		s.text(scheme.getEnable());
		s.endTag(null, "Enable");
		
		s.startTag(null, "ModelLock");
		s.text(scheme.getModelLock());
		s.endTag(null, "ModelLock");
		
		s.startTag(null, "DESC");
		s.text(scheme.getDesc());
		s.endTag(null, "DESC");
		
		s.startTag(null, "EXNO");
		s.text(scheme.getExno());
		s.endTag(null, "EXNO");
		
		s.startTag(null, "TimeCondition");
		s.text(scheme.getTimeCondition());
		s.endTag(null, "TimeCondition");
		
		s.startTag(null, "ExecutiveDate");
		s.text(scheme.getExecutiveDate());
		s.endTag(null, "ExecutiveDate");
		
		formTime(s, scheme);	
		
		formCommandList(s, scheme);
		
		s.endTag(null, "TestScheme");
	}

	private static void formCommandList(XmlSerializer s, TestScheme scheme) throws IllegalArgumentException, IllegalStateException, IOException {
		// TODO Auto-generated method stub
		s.startTag(null, "CommandList");
		
		s.attribute(null, "Repeat", scheme.getRepeat());
		formSynchronize(s, scheme.getCommandList());
		
		s.endTag(null, "CommandList");
	}

	private static void formSynchronize(XmlSerializer s, Synchronize commandList) throws IllegalArgumentException, IllegalStateException, IOException {
		// TODO Auto-generated method stub
		s.startTag(null, "Synchronize");
		
		s.attribute(null, "type", commandList.getType());
		formCommand(s, commandList.getCommand());
		
		s.endTag(null, "Synchronize");
	}

	private static void formCommand(XmlSerializer s, TestCommand command) throws IllegalStateException, IOException {
		// TODO Auto-generated method stub
		s.startTag(null, "Command");
		
		s.attribute(null, "Repeat", command.getRepeat());
		
		if (command.getId() != null) {
			s.startTag(null, "ID");
			s.text(command.getId());
			s.endTag(null, "ID");
		}
		
		if (command.getCallNumber() != null) {
			s.startTag(null, "CallNumber");
			s.text(command.getCallNumber());
			s.endTag(null, "CallNumber");
		}
		
		if (command.getRandomCall() != null) {
			s.startTag(null, "RandomCall");
			s.text(command.getRandomCall());
			s.endTag(null, "RandomCall");
		}
		
		if (command.getDuration() != null) {
			s.startTag(null, "Duration");
			s.text(command.getDuration());
			s.endTag(null, "Duration");
		}
		
		if (command.getInterval() != null) {
			s.startTag(null, "Interval");
			s.text(command.getInterval());
			s.endTag(null, "Interval");
		}
		
		if (command.getMaxTime() != null) {
			s.startTag(null, "MaxTime");
			s.text(command.getMaxTime());
			s.endTag(null, "MaxTime");
		}
		
		if (command.getTestMos() != null) {
			s.startTag(null, "TestMOS");
			s.text(command.getTestMos());
			s.endTag(null, "TestMOS");
		}
		
		if (command.getCallMosServer() != null) {
			s.startTag(null, "CallMOSServer");
			s.text(command.getCallMosServer());
			s.endTag(null, "CallMOSServer");
		}
		
		if (command.getMosLimit() != null) {
			s.startTag(null, "MOSLimit");
			s.text(command.getMosLimit());
			s.endTag(null, "MOSLimit");
		}
		
		if (command.getWaitTimes() != null) {
			s.startTag(null, "WaitTimes");
			s.text(command.getWaitTimes());
			s.endTag(null, "WaitTimes");
		}
		
		if (command.getKeepTime() != null) {
			s.startTag(null, "Keeptime");
			s.text(command.getKeepTime());
			s.endTag(null, "Keeptime");
		}
		
		if (command.getApn() != null) {
			s.startTag(null, "APN");
			s.text(command.getApn());
			s.endTag(null, "APN");
		}
		
		if (command.getIp() != null) {
			s.startTag(null, "IP");
			s.text(command.getIp());
			s.endTag(null, "IP");
		}
		
		if (command.getPackageSize() != null) {
			s.startTag(null, "Packagesize");
			s.text(command.getPackageSize());
			s.endTag(null, "Packagesize");
		}
		
		if (command.getTimeOut() != null) {
			s.startTag(null, "TimeOut");
			s.text(command.getTimeOut());
			s.endTag(null, "TimeOut");
		}
		
		if (command.getUrl() != null) {
			s.startTag(null, "URL");
			s.text(command.getUrl());
			s.endTag(null, "URL");
		}
		
		if (command.getAgent() != null) {
			s.startTag(null, "Agent");
			s.text(command.getAgent());
			s.endTag(null, "Agent");
		}
		
		if (command.getConnectionMode() != null) {
			s.startTag(null, "ConnectionMode");
			s.text(command.getConnectionMode());
			s.endTag(null, "ConnectionMode");
		}
		
		if (command.getGateWay() != null) {
			s.startTag(null, "Gateway");
			s.text(command.getGateWay());
			s.endTag(null, "Gateway");
		}
		
		if (command.getPort() != null) {
			s.startTag(null, "Port");
			s.text(command.getPort());
			s.endTag(null, "Port");
		}
		
		if (command.getServerCenterAddress() != null) {
			s.startTag(null, "ServerCenterAddress");
			s.text(command.getServerCenterAddress());
			s.endTag(null, "ServerCenterAddress");
		}
		
		if (command.getDestination() != null) {
			s.startTag(null, "Destination");
			s.text(command.getDestination());
			s.endTag(null, "Destination");
		}
		
		if (command.getMode() != null) {
			s.startTag(null, "Mode");
			s.text(command.getMode());
			s.endTag(null, "Mode");
		}
		
		if (command.getText() != null) {
			s.startTag(null, "Text");
			s.text(command.getText());
			s.endTag(null, "Text");
		}
		
		if (command.getReport() != null) {
			s.startTag(null, "Report");
			s.text(command.getReport());
			s.endTag(null, "Report");
		}
		
		if (command.getContent() != null) {
			s.startTag(null, "Content");
			s.text(command.getContent());
			s.endTag(null, "Content");
		}
		
		if (command.getAccount() != null) {
			s.startTag(null, "Account");
			s.text(command.getAccount());
			s.endTag(null, "Account");
		}
		
		if (command.getPassword() != null) {
			s.startTag(null, "Password");
			s.text(command.getPassword());
			s.endTag(null, "Password");
		}
		
		if (command.getServerAddress() != null) {
			s.startTag(null, "ServerAddress");
			s.text(command.getServerAddress());
			s.endTag(null, "ServerAddress");
		}
		
		if (command.getSyncMsno() != null) {
			s.startTag(null, "SyncMSNOs");
			s.text(command.getSyncMsno());
			s.endTag(null, "SyncMSNOs");
		}
		
		if (command.getMediaFileSize() != null) {
			s.startTag(null, "MediaFileSize");
			s.text(command.getMediaFileSize());
			s.endTag(null, "MediaFileSize");
		}
		
		if (command.getPTimeOut() != null) {
			s.startTag(null, "PTimeOut");
			s.text(command.getPTimeOut());
			s.endTag(null, "PTimeOut");
		}
		
		if (command.getRemoteHost() != null) {
			s.startTag(null, "RemoteHost");
			s.text(command.getRemoteHost());
			s.endTag(null, "RemoteHost");
		}
		
		if (command.getPassive() != null) {
			s.startTag(null, "Passive");
			s.text(command.getPassive());
			s.endTag(null, "Passive");
		}
		
		if (command.getBinary() != null) {
			s.startTag(null, "Binary");
			s.text(command.getBinary());
			s.endTag(null, "Binary");
		}
		
		if (command.getDownload() != null) {
			s.startTag(null, "Download");
			s.text(command.getDownload());
			s.endTag(null, "Download");
		}
		
		if (command.getRemoteFile() != null) {
			s.startTag(null, "RemoteFile");
			s.text(command.getRemoteFile());
			s.endTag(null, "RemoteFile");
		}
		
		if (command.getVersion() != null) {
			s.startTag(null, "Version");
			s.text(command.getVersion());
			s.endTag(null, "Version");
		}
		
		if (command.getUserName() != null) {
			s.startTag(null, "Username");
			s.text(command.getUserName());
			s.endTag(null, "Username");
		}
		
		if (command.getRtp() != null) {
			s.startTag(null, "RTP");
			s.text(command.getRtp());
			s.endTag(null, "RTP");
		}
		
		if (command.getRtspHttpPort() != null) {
			s.startTag(null, "RtspHttpPort");
			s.text(command.getRtspHttpPort());
			s.endTag(null, "RtspHttpPort");
		}
		
		if (command.getLocalRtpPort() != null) {
			s.startTag(null, "LocalRTPport");
			s.text(command.getLocalRtpPort());
			s.endTag(null, "LocalRTPport");
		}
		
		if (command.getPreBufferLength() != null) {
			s.startTag(null, "PreBufferLength");
			s.text(command.getPreBufferLength());
			s.endTag(null, "PreBufferLength");
		}
		
		if (command.getRebufferLength() != null) {
			s.startTag(null, "RebufferLength");
			s.text(command.getRebufferLength());
			s.endTag(null, "RebufferLength");
		}
		
		if (command.getPlayTime() != null) {
			s.startTag(null, "PlayTime");
			s.text(command.getPlayTime());
			s.endTag(null, "PlayTime");
		}
		
		if (command.getBufferLength() != null) {
			s.startTag(null, "BufferLength");
			s.text(command.getBufferLength());
			s.endTag(null, "BufferLength");
		}
		
		if (command.getBufferPlayThreshold() != null) {
			s.startTag(null, "BufferPlayThreshold");
			s.text(command.getBufferPlayThreshold());
			s.endTag(null, "BufferPlayThreshold");
		}
		
		if (command.getMailServer() != null) {
			s.startTag(null, "MailServer");
			s.text(command.getMailServer());
			s.endTag(null, "MailServer");
		}
		
		if (command.getDeleteMail() != null) {
			s.startTag(null, "Deletemail");
			s.text(command.getDeleteMail());
			s.endTag(null, "Deletemail");
		}
		
		if (command.getPath() != null) {
			s.startTag(null, "Path");
			s.text(command.getPath());
			s.endTag(null, "Path");
		}
		
		if (command.getSsl() != null) {
			s.startTag(null, "SSL");
			s.text(command.getSsl());
			s.endTag(null, "SSL");
		}
		
		if (command.getSender() != null) {
			s.startTag(null, "Sender");
			s.text(command.getSender());
			s.endTag(null, "Sender");
		}
		
		if (command.getFrom() != null) {
			s.startTag(null, "From");
			s.text(command.getFrom());
			s.endTag(null, "From");
		}
		
		if (command.getTo() != null) {
			s.startTag(null, "To");
			s.text(command.getTo());
			s.endTag(null, "To");
		}
		
		if (command.getFileSize() != null) {
			s.startTag(null, "FileSize");
			s.text(command.getFileSize());
			s.endTag(null, "FileSize");
		}
		
		if (command.getSubject() != null) {
			s.startTag(null, "Subject");
			s.text(command.getSubject());
			s.endTag(null, "Subject");
		}
		
		if (command.getBody() != null) {
			s.startTag(null, "Body");
			s.text(command.getBody());
			s.endTag(null, "Body");
		}
		
		if (command.getAuthentication() != null) {
			s.startTag(null, "Authentication");
			s.text(command.getAuthentication());
			s.endTag(null, "Authentication");
		}
		
		if (command.getEncoding() != null) {
			s.startTag(null, "Encoding");
			s.text(command.getEncoding());
			s.endTag(null, "Encoding");
		}
		
		if (command.getHtml() != null) {
			s.startTag(null, "HTML");
			s.text(command.getHtml());
			s.endTag(null, "HTML");
		}
		
		if (command.getAddress() != null) {
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
		
		s.endTag(null, "Command");
	}

	private static void formTime(XmlSerializer s, TestScheme scheme) throws IllegalArgumentException, IllegalStateException, IOException {
		// TODO Auto-generated method stub
		s.startTag(null, "Time");
		
		s.startTag(null, "BeginTime");	
		s.text(scheme.getTime().getBeginTime());	
		s.endTag(null, "BeginTime");
		
		s.startTag(null, "EndTime");	
		s.text(scheme.getTime().getEndTime());	
		s.endTag(null, "EndTime");
		
		s.endTag(null, "Time");
	}

	private static void formAutoTestUnit(XmlSerializer s, TestScript script) throws IllegalArgumentException, IllegalStateException, IOException {
		// TODO Auto-generated method stub
		s.startTag(null, "AutoTestUnit");
		
		s.startTag(null, "Version");
		s.text(script.getAutoTestUnit().getVersion());
		s.endTag(null, "Version");
		
		formNetwork(s, script);	
		
		formLogProcess(s, script);
		
		formGeneralItem(s, script);
		
		s.endTag(null, "AutoTestUnit");
	}

	private static void formGeneralItem(XmlSerializer s, TestScript script) throws IllegalArgumentException, IllegalStateException, IOException {
		// TODO Auto-generated method stub
		s.startTag(null, "GeneralItem");
		
		s.startTag(null, "SpeedCondition");
		s.text(script.getAutoTestUnit().getGeneralItem().getSpeedCondition());
		s.endTag(null, "SpeedCondition");
		
		
		s.startTag(null, "GPSCondition");
		s.text(script.getAutoTestUnit().getGeneralItem().getGpsCondition());
		s.endTag(null, "GPSCondition");
		
		s.endTag(null, "GeneralItem");
	}

	private static void formLogProcess(XmlSerializer s, TestScript script) throws IllegalArgumentException, IllegalStateException, IOException {
		// TODO Auto-generated method stub
		s.startTag(null, "LogProcess");
		
		formSwitchLog(s, script);
		
		s.endTag(null, "LogProcess");
	}

	private static void formSwitchLog(XmlSerializer s, TestScript script) throws IllegalArgumentException, IllegalStateException, IOException {
		// TODO Auto-generated method stub
		s.startTag(null, "SwitchLog");
		
		s.startTag(null, "Enable");
		s.text(script.getAutoTestUnit().getLogProcess().getSwitchLog().getEnable());
		s.endTag(null, "Enable");
		
		
		s.startTag(null, "Type");
		s.text(script.getAutoTestUnit().getLogProcess().getSwitchLog().getType());
		s.endTag(null, "Type");
		
		s.startTag(null, "TestTime");
		s.text(script.getAutoTestUnit().getLogProcess().getSwitchLog().getTestTime());
		s.endTag(null, "TestTime");
		
		s.startTag(null, "Condition");
		s.text(script.getAutoTestUnit().getLogProcess().getSwitchLog().getCondition());
		s.endTag(null, "Condition");
		
		s.endTag(null, "SwitchLog");
	}

	private static void formNetwork(XmlSerializer s, TestScript script) throws IllegalArgumentException, IllegalStateException, IOException {
		// TODO Auto-generated method stub
		s.startTag(null, "Network");
		
		s.startTag(null, "PortalIP");
		s.text(script.getAutoTestUnit().getNetWork().getPortalIp());
		s.endTag(null, "PortalIP");
		
		
		s.startTag(null, "PortalPort");
		s.text(script.getAutoTestUnit().getNetWork().getPortalPort());
		s.endTag(null, "PortalPort");
		
		s.startTag(null, "User");
		s.text(script.getAutoTestUnit().getNetWork().getUser());
		s.endTag(null, "User");
		
		s.startTag(null, "Password");
		s.text(script.getAutoTestUnit().getNetWork().getPassword());
		s.endTag(null, "Password");
		
		s.startTag(null, "UseLAN");
		s.text(script.getAutoTestUnit().getNetWork().getUserLan());
		s.endTag(null, "UseLAN");
		
		s.startTag(null, "SendData");
		s.text(script.getAutoTestUnit().getNetWork().getSendData());
		s.endTag(null, "SendData");
		
		s.startTag(null, "DialNumber");
		s.text(script.getAutoTestUnit().getNetWork().getDialNumber());
		s.endTag(null, "DialNumber");
		
		s.startTag(null, "APN");
		s.text(script.getAutoTestUnit().getNetWork().getApn());
		s.endTag(null, "APN");
		
		s.startTag(null, "DialUpUser");
		s.text(script.getAutoTestUnit().getNetWork().getDialUpUser());
		s.endTag(null, "DialUpUser");
		
		s.startTag(null, "DialUpPassword");
		s.text(script.getAutoTestUnit().getNetWork().getDialUpPassword());
		s.endTag(null, "DialUpPassword");
		
		s.endTag(null, "Network");
	}

	public String serialize(TestScript testScript) throws Exception {
		return null;
	}

}
