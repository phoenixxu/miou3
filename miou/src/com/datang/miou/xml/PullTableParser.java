package com.datang.miou.xml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import com.datang.miou.views.gen.params.Column;
import com.datang.miou.views.gen.params.Row;
import com.datang.miou.views.gen.params.Table;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

public class PullTableParser implements TableParser {

	private static final String TAG = "PullTableParser";
	private Table mTable;
	private Row mRow;
	private Column mColumn;
	private List<Table> mTables;
	private Context mContext;

	public PullTableParser(Context context) {
		mContext = context;
	}
	
	@Override
	public List<Table> parse(InputStream is) throws Exception {
		mRow = null;
		
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(is, "UTF-8");
		
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					mTables = new ArrayList<Table>();
					break;
				case XmlPullParser.START_TAG:
					if (parser.getName().equals("table")) {
						mTable = new Table();
						String id = parser.getAttributeValue(null, "id");
						String name = parser.getAttributeValue(null, "name");
						mTable.setId(id);
						mTable.setName(name);
					}
					
					if (parser.getName().equals("row")) {
						mRow = new Row();
						String color = parser.getAttributeValue(null, "color");
						mRow.setColor(mContext.getResources().getIdentifier(color, "color", "com.datang.miou"));		
					}
					
					if (parser.getName().equals("column")) {
						mColumn = new Column();
						String id = parser.getAttributeValue(null, "id");
						String text = parser.getAttributeValue(null, "text");
						String subId = parser.getAttributeValue(null, "sub_id");
						String weight = parser.getAttributeValue(null, "weight");
						mColumn.setWeight(weight);
						mColumn.setId(id);
						mColumn.setText(text);
						mColumn.setSubId(subId);
					}
					break;
				case XmlPullParser.END_TAG:
					if (parser.getName().equals("table")) {
						mTables.add(mTable);
					}
					
					if (parser.getName().equals("row")) {
						mTable.getRows().add(mRow);
					}
					
					if (parser.getName().equals("column")) {
						mRow.getColumns().add(mColumn);
					}	
					break;
			}
			eventType = parser.next();
		}
		return mTables;
	}
	
	@Override
	public String serialize(List<Table> tables) throws Exception {
		return null;
	}

}
