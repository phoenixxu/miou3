package com.datang.miou.xml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import com.datang.miou.datastructure.TestCommand;

import android.util.Log;
import android.util.Xml;

public class PullTestCommandParser implements TestCommandParser {

	private static final String TAG = "PullTestCommandParser";
	private TestCommand mTestScheme;
	private List<TestCommand> mTestSchemes;
	
	public PullTestCommandParser() {
	}
	
	@Override
	public List<TestCommand> parse(InputStream is) throws Exception {
		mTestScheme = null;
		
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(is, "UTF-8");
		
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					mTestSchemes = new ArrayList<TestCommand>();
					break;
				case XmlPullParser.START_TAG:
					if (parser.getName().equals("TestScheme")) {
						mTestScheme = new TestCommand();
					} else if (parser.getName().equals("ID")) {
						eventType = parser.next();
						mTestScheme.setId(parser.getText());		
					} else if (parser.getName().equals("CallNumber")) {
						eventType = parser.next();
						mTestScheme.setCallNumber(parser.getText());		
					} else if (parser.getName().equals("RandomCall")) {
						eventType = parser.next();
						mTestScheme.setRandomCall(parser.getText());		
					} else if (parser.getName().equals("Duration")) {
						eventType = parser.next();
						mTestScheme.setDuration(parser.getText());		
					} else if (parser.getName().equals("Interval")) {
						eventType = parser.next();
						mTestScheme.setInterval(parser.getText());		
					} else if (parser.getName().equals("MaxTime")) {
						eventType = parser.next();
						mTestScheme.setMaxTime(parser.getText());		
					} else if (parser.getName().equals("TestMOS")) {
						eventType = parser.next();
						mTestScheme.setTestMos(parser.getText());		
					} else if (parser.getName().equals("CallMOSServer")) {
						eventType = parser.next();
						mTestScheme.setCallMosServer(parser.getText());		
					} else if (parser.getName().equals("MOSLimit")) {
						eventType = parser.next();
						mTestScheme.setMosLimit(parser.getText());		
					} else if (parser.getName().equals("WaitTimes")) {
						eventType = parser.next();
						mTestScheme.setWaitTimes(parser.getText());		
					} else if (parser.getName().equals("Keeptime")) {
						eventType = parser.next();
						mTestScheme.setKeepTime(parser.getText());		
					} else if (parser.getName().equals("APN")) {
						eventType = parser.next();
						mTestScheme.setApn(parser.getText());		
					} else if (parser.getName().equals("IP")) {
						eventType = parser.next();
						mTestScheme.setIp(parser.getText());		
					} else if (parser.getName().equals("Packagesize")) {
						eventType = parser.next();
						mTestScheme.setPackageSize(parser.getText());		
					} else if (parser.getName().equals("TimeOut")) {
						Log.i(TAG, "TimeOut" + parser.getText());
						eventType = parser.next();
						mTestScheme.setTimeOut(parser.getText());		
					} else if (parser.getName().equals("SyncMSNOs")) {
						eventType = parser.next();
						Log.i(TAG, "SyncMSNOs" + parser.getText());
						mTestScheme.setSyncMsno(parser.getText());		
					} else if (parser.getName().equals("URL")) {
						eventType = parser.next();
						mTestScheme.setUrl(parser.getText());		
					} else if (parser.getName().equals("Agent")) {
						eventType = parser.next();
						mTestScheme.setAgent(parser.getText());		
					} else if (parser.getName().equals("ConnectionMode")) {
						eventType = parser.next();
						mTestScheme.setConnectionMode(parser.getText());		
					} else if (parser.getName().equals("Gateway")) {
						eventType = parser.next();
						mTestScheme.setGateWay(parser.getText());		
					} else if (parser.getName().equals("Port")) {
						eventType = parser.next();
						mTestScheme.setPort(parser.getText());		
					} else if (parser.getName().equals("ServerCenterAddress")) {
						eventType = parser.next();
						mTestScheme.setServerCenterAddress(parser.getText());		
					} else if (parser.getName().equals("Destination")) {
						eventType = parser.next();
						mTestScheme.setDestination(parser.getText());		
					} else if (parser.getName().equals("Mode")) {
						eventType = parser.next();
						mTestScheme.setMode(parser.getText());		
					} else if (parser.getName().equals("Text")) {
						eventType = parser.next();
						mTestScheme.setText(parser.getText());		
					} else if (parser.getName().equals("Report")) {
						eventType = parser.next();
						mTestScheme.setReport(parser.getText());		
					} else if (parser.getName().equals("Content")) {
						eventType = parser.next();
						mTestScheme.setContent(parser.getText());		
					} else if (parser.getName().equals("Account")) {
						eventType = parser.next();
						mTestScheme.setAccount(parser.getText());		
					} else if (parser.getName().equals("Password")) {
						eventType = parser.next();
						mTestScheme.setPassword(parser.getText());		
					} else if (parser.getName().equals("ServerAddress")) {
						eventType = parser.next();
						mTestScheme.setServerAddress(parser.getText());		
					} else if (parser.getName().equals("MediaFileSize")) {
						eventType = parser.next();
						mTestScheme.setMediaFileSize(parser.getText());		
					} else if (parser.getName().equals("PTimeOut")) {
						eventType = parser.next();
						mTestScheme.setPTimeOut(parser.getText());		
					} else if (parser.getName().equals("RemoteHost")) {
						eventType = parser.next();
						mTestScheme.setRemoteHost(parser.getText());		
					} else if (parser.getName().equals("Passive")) {
						eventType = parser.next();
						mTestScheme.setPassive(parser.getText());		
					} else if (parser.getName().equals("Binary")) {
						eventType = parser.next();
						mTestScheme.setBinary(parser.getText());		
					} else if (parser.getName().equals("Download")) {
						eventType = parser.next();
						mTestScheme.setDownload(parser.getText());		
					} else if (parser.getName().equals("RemoteFile")) {
						eventType = parser.next();
						mTestScheme.setRemoteFile(parser.getText());		
					} else if (parser.getName().equals("Version")) {
						eventType = parser.next();
						mTestScheme.setVersion(parser.getText());		
					} else if (parser.getName().equals("Username")) {
						eventType = parser.next();
						mTestScheme.setUserName(parser.getText());		
					} else if (parser.getName().equals("RTP")) {
						eventType = parser.next();
						mTestScheme.setRtp(parser.getText());		
					} else if (parser.getName().equals("RtspHttpPort")) {
						eventType = parser.next();
						mTestScheme.setRtspHttpPort(parser.getText());		
					} else if (parser.getName().equals("LocalRTPport")) {
						eventType = parser.next();
						mTestScheme.setLocalRtpPort(parser.getText());		
					} else if (parser.getName().equals("PreBufferLength")) {
						eventType = parser.next();
						mTestScheme.setPreBufferLength(parser.getText());		
					} else if (parser.getName().equals("RebufferLength")) {
						eventType = parser.next();
						mTestScheme.setRebufferLength(parser.getText());		
					} else if (parser.getName().equals("PlayTime")) {
						eventType = parser.next();
						mTestScheme.setPlayTime(parser.getText());		
					} else if (parser.getName().equals("BufferLength")) {
						eventType = parser.next();
						mTestScheme.setBufferLength(parser.getText());		
					} else if (parser.getName().equals("BufferPlayThreshold")) {
						eventType = parser.next();
						mTestScheme.setBufferPlayThreshold(parser.getText());		
					} else if (parser.getName().equals("MailServer")) {
						eventType = parser.next();
						mTestScheme.setMailServer(parser.getText());		
					} else if (parser.getName().equals("Deletemail")) {
						eventType = parser.next();
						mTestScheme.setDeleteMail(parser.getText());		
					} else if (parser.getName().equals("Path")) {
						eventType = parser.next();
						mTestScheme.setPath(parser.getText());		
					} else if (parser.getName().equals("SSL")) {
						eventType = parser.next();
						mTestScheme.setSsl(parser.getText());		
					} else if (parser.getName().equals("Sender")) {
						eventType = parser.next();
						mTestScheme.setSender(parser.getText());		
					} else if (parser.getName().equals("From")) {
						eventType = parser.next();
						mTestScheme.setFrom(parser.getText());		
					} else if (parser.getName().equals("To")) {
						eventType = parser.next();
						mTestScheme.setTo(parser.getText());		
					} else if (parser.getName().equals("FileSize")) {
						eventType = parser.next();
						mTestScheme.setFileSize(parser.getText());		
					} else if (parser.getName().equals("Subject")) {
						eventType = parser.next();
						mTestScheme.setSubject(parser.getText());		
					} else if (parser.getName().equals("Body")) {
						eventType = parser.next();
						mTestScheme.setBody(parser.getText());		
					} else if (parser.getName().equals("Address")) {
						eventType = parser.next();
						mTestScheme.setAddress(parser.getText());		
					} else if (parser.getName().equals("Authentication")) {
						eventType = parser.next();
						mTestScheme.setAuthentication(parser.getText());		
					} else if (parser.getName().equals("Encoding")) {
						eventType = parser.next();
						mTestScheme.setEncoding(parser.getText());		
					} else if (parser.getName().equals("HTML")) {
						eventType = parser.next();
						mTestScheme.setHtml(parser.getText());		
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
	public String serialize(List<TestCommand> tables) throws Exception {
		return null;
	}

}
