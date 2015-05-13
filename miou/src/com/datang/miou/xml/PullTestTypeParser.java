package com.datang.miou.xml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import com.datang.miou.datastructure.TestType;

import android.util.Log;
import android.util.Xml;

public class PullTestTypeParser {

	private static final String TAG = "PullTestTypeParser";
	private TestType mTestType;
	private List<TestType> mTestTypes;
	
	public List<TestType> parse(InputStream is) throws Exception {
		
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(is, "UTF-8");
		
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					mTestTypes = new ArrayList<TestType>();
					break;
				case XmlPullParser.START_TAG:
					if (parser.getName().equals("type")) {
						mTestType = new TestType();
						String id = parser.getAttributeValue(null, "id");
						String description = parser.getAttributeValue(null, "description");
						String note = parser.getAttributeValue(null, "note");
						mTestType.setId(id);
						mTestType.setDescription(description);
						if (note != null) {
							mTestType.setNote(note);
						}
					}
					
					break;
				case XmlPullParser.END_TAG:
					if (parser.getName().equals("type")) {
						mTestTypes.add(mTestType);
					}
					break;
			}
			eventType = parser.next();
		}
		return mTestTypes;
	}
	
	public String serialize(List<TestType> tables) throws Exception {
		return null;
	}

}
