package com.datang.miou.xml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import com.datang.miou.datastructure.Synchronize;
import com.datang.miou.datastructure.TestCommand;
import com.datang.miou.datastructure.TestScheme;
import com.datang.miou.datastructure.Time;

import android.util.Log;
import android.util.Xml;

public class PullTestSchemeParser implements TestSchemeParser {

	private static final String TAG = "PullTestSchemeParser";
	private TestScheme mTestScheme;
	private List<TestScheme> mTestSchemes;
	private Time mTime;
	private Synchronize mCommandList;
	private TestCommand mCommand;

	public PullTestSchemeParser() {
	}
	
	@Override
	public List<TestScheme> parse(InputStream is) throws Exception {
		
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(is, "UTF-8");
		
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
				case XmlPullParser.START_DOCUMENT:		
					break;
				case XmlPullParser.START_TAG:
					if (parser.getName().equals("AutoTestUnit")) {
						parser.nextToken();
					} else if (parser.getName().equals("TestUnit")) {
						Log.i(TAG, "new unit");
						mTestSchemes = new ArrayList<TestScheme>();
					} else if (parser.getName().equals("TestScheme")) {
						Log.i(TAG, "new scheme");
						mTestScheme = new TestScheme();
					} else if (parser.getName().equals("Enable")) {
						eventType = parser.next();
						String enable = parser.getText();
						Log.i(TAG, "enable = " + enable);
						mTestScheme.setEnable(enable);
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
						mCommand = new TestCommand();
						mCommandList.setType(type);
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
					} else if (parser.getName().equals("APN")) {
						eventType = parser.next();
						mCommand.setApn(parser.getText());		
					} else if (parser.getName().equals("IP")) {
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
					} else if (parser.getName().equals("Password")) {
						eventType = parser.next();
						mCommand.setPassword(parser.getText());		
					} else if (parser.getName().equals("ServerAddress")) {
						eventType = parser.next();
						mCommand.setServerAddress(parser.getText());		
					} else if (parser.getName().equals("MediaFileSize")) {
						eventType = parser.next();
						mCommand.setMediaFileSize(parser.getText());		
					} else if (parser.getName().equals("PTimeOut")) {
						eventType = parser.next();
						mCommand.setPTimeOut(parser.getText());		
					} else if (parser.getName().equals("SyncMSNOs")) {
						eventType = parser.next();
						mCommand.setSyncMsno(parser.getText());		
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
						mCommand.setVersion(parser.getText());		
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
					}
					
					
					break;
				case XmlPullParser.END_TAG:
					if (parser.getName().equals("TestScheme")) {
						mTestSchemes.add(mTestScheme);
					}
					break;
			}
			eventType = parser.next();
		}
		return mTestSchemes;
	}
	
	@Override
	public String serialize(List<TestScheme> schemes) throws Exception {
		return null;
	}

}
