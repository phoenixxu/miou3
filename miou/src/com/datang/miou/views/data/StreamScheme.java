package com.datang.miou.views.data;

import android.util.Xml;

import com.datang.miou.utils.SDCardUtils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by leo on 15/4/25.
 */
public class StreamScheme extends AbstractScheme {

    String Number,Interval,
            APN,
            Version,
            URL,
            Username,
            Password,
            Agent,
            RTP,
            RtspHttpPort,
            LocalRTPport,
            PreBufferLength,
            RebufferLength,
            PlayTime,
            BufferLength,
            BufferPlayThreshold;
    private HashMap<String, String> valueMap = new HashMap<String, String>();
    private LinkedHashMap<String, String> keyMap = new LinkedHashMap<String, String>();


    protected StreamScheme() {
        id = "0x0604";
        name = "流媒体测试";
    }

    @Override
    public Map<String, String> values() {
        if (!valueMap.isEmpty()) return valueMap;
        valueMap.put("Number", Number);
        valueMap.put("Version", Version);
        valueMap.put("URL", URL);
        valueMap.put("Interval", Interval);
        valueMap.put("Username", Username);
        valueMap.put("Password", Password);
        valueMap.put("Agent", Agent);
        valueMap.put("RTP", RTP);
        valueMap.put("RtspHttpPort", RtspHttpPort);
        valueMap.put("LocalRTPport", LocalRTPport);
        valueMap.put("PreBufferLength", PreBufferLength);
        valueMap.put("RebufferLength", RebufferLength);
        valueMap.put("PlayTime", PlayTime);
        valueMap.put("BufferLength", BufferLength);
        valueMap.put("BufferPlayThreshold", BufferPlayThreshold);
        valueMap.put("APN", APN);
        return valueMap;
    }

    @Override
    public Map<String, String> keys() {
        if (!keyMap.isEmpty()) return keyMap;
        keyMap.put("Number", "测试次数");
        keyMap.put("Version", "Version");
        keyMap.put("URL", "URL");
        keyMap.put("Interval", "Interval");
        keyMap.put("Username", "Username");
        keyMap.put("Password", "Password");
        keyMap.put("Agent", "Agent");
        keyMap.put("RTP", "RTP");
        keyMap.put("RtspHttpPort", "RtspHttpPort");
        keyMap.put("LocalRTPport", "LocalRTPport");
        keyMap.put("PreBufferLength", "PreBufferLength");
        keyMap.put("RebufferLength", "RebufferLength");
        keyMap.put("PlayTime", "PlayTime");
        keyMap.put("BufferLength", "BufferLength");
        keyMap.put("BufferPlayThreshold", "BufferPlayThreshold");
        keyMap.put("Interval", "间隔时长(s)");

        return keyMap;
    }

    @Override
    public void read(String path) {
        InputStream is = null;
        try {
            if (path == null) {
                is = new FileInputStream(new File(SDCardUtils.getConfigPath(), "TestPlanTemplate.xml"));
            } else {
                is = new FileInputStream(new File(path));
            }
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(is, "UTF-8");

            boolean isFoundScheme = false;
            boolean isFoundID = false;
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("TestScheme")) {
                            isFoundScheme = true;
                        } else if (isFoundScheme && parser.getName().equals("ID")) {
                            eventType = parser.next();
                            isFoundID = parser.getText().equals(id());
                        } else if (isFoundID && parser.getName().equals("Password")) {
                            eventType = parser.next();
                            Password = parser.getText();
                        } else if (isFoundID && parser.getName().equals("Agent")) {
                            eventType = parser.next();
                            Agent = parser.getText();
                        } else if (isFoundID && parser.getName().equals("RTP")) {
                            eventType = parser.next();
                            RTP = parser.getText();
                        } else if (isFoundID && parser.getName().equals("RtspHttpPort")) {
                            eventType = parser.next();
                            RtspHttpPort = parser.getText();
                        } else if (isFoundID && parser.getName().equals("LocalRTPport")) {
                            eventType = parser.next();
                            LocalRTPport = parser.getText();
                        } else if (isFoundID && parser.getName().equals("Version")) {
                            eventType = parser.next();
                            Version = parser.getText();
                        } else if (isFoundID && parser.getName().equals("Interval")) {
                            eventType = parser.next();
                            Interval = parser.getText();
                        } else if (isFoundID && parser.getName().equals("URL")) {
                            eventType = parser.next();
                            URL = parser.getText();
                        } else if (isFoundID && parser.getName().equals("Username")) {
                            eventType = parser.next();
                            Username = parser.getText();
                        } else if (isFoundID && parser.getName().equals("APN")) {
                            eventType = parser.next();
                            APN = parser.getText();
                        } else if (isFoundID && parser.getName().equals("BufferPlayThreshold")) {
                            eventType = parser.next();
                            BufferPlayThreshold = parser.getText();
                        } else if (isFoundID && parser.getName().equals("BufferLength")) {
                            eventType = parser.next();
                            BufferLength = parser.getText();
                        } else if (isFoundID && parser.getName().equals("PlayTime")) {
                            eventType = parser.next();
                            PlayTime = parser.getText();
                        } else if (isFoundID && parser.getName().equals("RebufferLength")) {
                            eventType = parser.next();
                            RebufferLength = parser.getText();
                        } else if (isFoundID && parser.getName().equals("PreBufferLength")) {
                            eventType = parser.next();
                            PreBufferLength = parser.getText();
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("TestScheme")) {
                            isFoundScheme = false;
                            isFoundID = false;
                        }
                        break;
                }
                eventType = parser.next();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void write(String path) {


    }

}
