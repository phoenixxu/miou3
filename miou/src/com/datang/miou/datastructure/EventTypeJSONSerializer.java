package com.datang.miou.datastructure;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import android.content.Context;

public class EventTypeJSONSerializer {
	private String mFilename;

	public EventTypeJSONSerializer(Context context, String filename) {
		mFilename = filename;
	}
	
	public void saveEventTypes(ArrayList<EventType> list) throws JSONException, IOException {
		JSONArray array = new JSONArray();
		for (EventType type : list) {
			array.put(type.toJSON());
		}
		
		Writer writer = null;
		try {
			FileOutputStream out = new FileOutputStream(mFilename);
			writer = new OutputStreamWriter(out);
			writer.write(array.toString());
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}
	
	public ArrayList<EventType> loadEventTypes() throws JSONException, IOException {
		ArrayList<EventType> types = new ArrayList<EventType>();
		BufferedReader reader = null;
		try {
			FileInputStream in = new FileInputStream(mFilename);
			reader = new BufferedReader(new InputStreamReader(in));
			StringBuilder jsonString = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				jsonString.append(line);
			}
			JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
			for (int i = 0; i < array.length(); i++) {
				types.add(new EventType(array.getJSONObject(i)));
			}
		} catch (FileNotFoundException e) {

		} finally {
			if (reader != null) {
				reader.close();
			}
		}
		return types;
	}
}
