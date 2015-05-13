package com.datang.miou.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import com.datang.miou.datastructure.EventType;
import com.datang.miou.datastructure.TestScript;
import com.datang.miou.views.gen.params.Column;
import com.datang.miou.views.gen.params.Row;
import com.datang.miou.views.gen.params.Table;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

public class PullEventTypeParser {

	private EventType mType;
	private List<EventType> mEventTypes;
	private Context mContext;

	public PullEventTypeParser(Context context) {
		mContext = context;
	}
	
	public List<EventType> parse(InputStream is) throws Exception {
		
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(is, "UTF-8");
		
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					mEventTypes = new ArrayList<EventType>();
					break;
				case XmlPullParser.START_TAG:
					if (parser.getName().equals("type")) {
						mType = new EventType();
						String id = parser.getAttributeValue(null, "id");
						String description = parser.getAttributeValue(null, "description");
						String color = parser.getAttributeValue(null, "color");
						
						mType.setId(id);
						mType.setDescription(description);
						if (color != null) {
							mType.setColor(Integer.parseInt(color));
						}
					}
					break;
				case XmlPullParser.END_TAG:
					if (parser.getName().equals("type")) {
						mEventTypes.add(mType);
					}
					
					break;
			}
			eventType = parser.next();
		}
		return mEventTypes;
	}
	
	public String serialize(List<Table> tables) throws Exception {
		return null;
	}

	public static void writeXml(List<EventType> types, Writer writer) throws IllegalArgumentException, IllegalStateException, IOException {
		XmlSerializer s = Xml.newSerializer();
		s.setOutput(writer);
		
		s.startDocument("UTF-8", true);
		s.startTag(null, "root");
		
		for (EventType type : types) {

			s.startTag(null, "type");
			
			s.attribute(null, "id", type.getId());
			s.attribute(null, "description", type.getDescription());
			s.attribute(null, "color", String.valueOf(type.getColor()));
			s.endTag(null, "type");
		}
		
		s.endTag(null, "root");
		
		s.endDocument();
	}
}
