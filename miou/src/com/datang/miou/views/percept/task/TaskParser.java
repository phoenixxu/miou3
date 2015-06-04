package com.datang.miou.views.percept.task;

import android.util.Log;
import android.util.Xml;

import com.datang.miou.utils.SDCardUtils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 负责web任务的解析和序列化
 * <p/>
 * <web>
 * <name>百度</name>
 * <url>www.baidu.com</url>
 * </web>
 * Created by leo on 5/31/15.
 */
public class TaskParser {

    public static final List<Task> taskList = new ArrayList<Task>();
    private static final String TAG = "TaskParser";
    private static final String path = SDCardUtils.getPerScript() + File.separator + "tasks.xml";

    public static boolean readTasks() {
        XmlPullParser xmlParser = Xml.newPullParser();
        File file = new File(path);
        if (!file.exists()) {
            Log.d(TAG, "file :" + path + " is not exist, create it!");
            return false;
        }

        int eventType = 0;
        try {
            FileInputStream fis = new FileInputStream(file);
            xmlParser.setInput(fis, "UTF-8");
            eventType = xmlParser.getEventType();

            Task task = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                Log.d(TAG, "eventType=" + eventType);
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        Log.d(TAG, "name=" + xmlParser.getName());
                        if ("task".equals(xmlParser.getName())) {
                            task = new Task();
                        } else if ("name".equals(xmlParser.getName())) {
                            if (task != null) {
                                task.name = xmlParser.nextText();
                            }
                        } else if ("type".equals(xmlParser.getName())) {
                            if (task != null) {
                                task.type = xmlParser.nextText();
                            }
                        } else if ("interval".equals(xmlParser.getName())) {
                            if (task != null) {
                                task.interval = xmlParser.nextText();
                            }
                        } else if ("count".equals(xmlParser.getName())) {
                            if (task != null) {
                                task.count = xmlParser.nextText();
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("task".equals(xmlParser.getName())) {
                            Log.d(TAG, "name:" + xmlParser.getName() + ", task=" + task.name);
                            taskList.add(task);
                            task = null;
                        }
                        break;
                    default:
                        break;
                }
                eventType = xmlParser.next();
            }
        } catch (FileNotFoundException e) {
            Log.d(TAG, "file not found exception!");
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            Log.d(TAG, "XmlPullParserException!");
            e.printStackTrace();
        } catch (IOException e) {
            Log.d(TAG, "IOException!");
            e.printStackTrace();
        }

        return true;
    }

    public static boolean writeTasks() {
        FileOutputStream fos = null;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }

        try {
            file = new File(path);
            if (!file.createNewFile()) {
                Log.d(TAG, "file already exist!");
                file.delete();
                if (!file.createNewFile()) {
                    Log.d(TAG, "Create file failed!");
                    return false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            Log.d(TAG, "create xml file failed!");
        }
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return false;
        }
        XmlSerializer serializer = Xml.newSerializer();
        try {
            serializer.setOutput(fos, "UTF-8");

            //缩进
            serializer.setFeature(
                    "http://xmlpull.org/v1/doc/features.html#indent-output",
                    true);
            serializer.startDocument("UTF-8", true);
            serializer.startTag(null, "tasks");
            for (Task task : taskList) {
                if (task == null) continue;
                serializer.startTag(null, "task");

                serializer.startTag(null, "name");
                serializer.text(task.name);
                serializer.endTag(null, "name");

                serializer.startTag(null, "type");
                serializer.text(task.type);
                serializer.endTag(null, "type");

                serializer.startTag(null, "count");
                serializer.text(task.count);
                serializer.endTag(null, "count");

                serializer.startTag(null, "interval");
                serializer.text(task.interval);
                serializer.endTag(null, "interval");

                serializer.endTag(null, "task");
            }
            serializer.endTag(null, "tasks");
            serializer.endDocument();


        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            Log.d(TAG, "IOException write xml file failed!");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Exception write xml file failed!");
            return false;
        } finally {
            if (fos != null) {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

}
