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

public class StationJSONSerializer {
	private String mFilename;

	public StationJSONSerializer(Context context, String filename) {
		mFilename = filename;
	}
	
	public void saveStations(ArrayList<Station> list) throws JSONException, IOException {
		JSONArray array = new JSONArray();
		for (Station s : list) {
			array.put(s.toJSON());
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
	
	public ArrayList<Station> loadStations() throws JSONException, IOException {
		ArrayList<Station> stations = new ArrayList<Station>();
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
				stations.add(new Station(array.getJSONObject(i)));
			}
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
		return stations;
	}
}
