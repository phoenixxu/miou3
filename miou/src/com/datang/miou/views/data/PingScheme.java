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
 * ping 测试
 */
public class PingScheme extends AbstractScheme {

    //测试次数/IP/包大小(Byte)/间隔时长(s)/超时时长(3)/TTL
    String Number, IP, Packagesize, Interval, TimeOut, TTL, APN;
    private HashMap<String, String> valueMap = new HashMap<String, String>();
    private LinkedHashMap<String, String> keyMap = new LinkedHashMap<String, String>();


    protected PingScheme() {
        id = "0x0604";
        name = "PING 测试";
    }

    @Override
    public Map<String, String> values() {
        if(!valueMap.isEmpty()) return valueMap;
        valueMap.put("Number", Number);
        valueMap.put("IP", IP);
        valueMap.put("Packagesize", Packagesize);
        valueMap.put("Interval", Interval);
        valueMap.put("TimeOut", TimeOut);
        valueMap.put("TTL", TTL);
        valueMap.put("APN", APN);
        return valueMap;
    }

    @Override
    public Map<String, String> keys() {
        if(!keyMap.isEmpty()) return keyMap;
        keyMap.put("Number", "测试次数");
        keyMap.put("IP", "IP");
        keyMap.put("Packagesize", "包大小(Byte)");
        keyMap.put("Interval", "间隔时长(s)");
        keyMap.put("TimeOut", "超时时长(3)");
        keyMap.put("TTL", "TTL");
//        keyMap.put("APN", "APN");
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
                        } else if (isFoundID && parser.getName().equals("IP")) {
                            eventType = parser.next();
                            IP = parser.getText();
                        } else if (isFoundID && parser.getName().equals("Interval")) {
                            eventType = parser.next();
                            Interval = parser.getText();
                        } else if (isFoundID && parser.getName().equals("Packagesize")) {
                            eventType = parser.next();
                            Packagesize = parser.getText();
                        } else if (isFoundID && parser.getName().equals("TimeOut")) {
                            eventType = parser.next();
                            TimeOut = parser.getText();
                        } else if (isFoundID && parser.getName().equals("APN")) {
                            eventType = parser.next();
                            APN = parser.getText();
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
