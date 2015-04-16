package com.datang.miou.views.gen.params;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.datang.miou.ProcessInterface;
import com.datang.miou.R;
import com.datang.miou.datastructure.RealData;
import com.datang.miou.utils.MiscUtils;
import com.datang.miou.utils.SDCardUtils;
import com.datang.miou.xml.PullTableParser;

public class TableManager {
	
	private static final String XML_FILE_TABLE = "Tables.xml";
	private static final String TAG = "TableManager";

	private static List<Table> getParamsfromXml(Context context) {
		// TODO 自动生成的方法存根
		//Test
		try {
			File file = MiscUtils.getExternalFileForRead(context, SDCardUtils.getConfigPath(), XML_FILE_TABLE);			
			InputStream is = new FileInputStream(file);
			//InputStream is = context.getAssets().open(XML_FILE_TABLE);
			PullTableParser parser = new PullTableParser(context);
			List<Table> tables = parser.parse(is);
			return tables;
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			Toast.makeText(context, "parse xml error!", Toast.LENGTH_SHORT).show();
			return null;
		}
	}
	
	public static void updateTable(Table table) {
		Log.i(TAG, "updateTable, table = " + table);
		for (Row row : table.getRows()) {
			for (Column col : row.getColumns()) {
				if (col.getId() != null) {
					String key = col.getId();
					
					if (col.getSubId() != null) {
						key += "_";
						key += col.getSubId();
					}
					Log.i(TAG, "key = " + key);
					String value = ProcessInterface.GetIEByID(key);
					Log.i(TAG, "value = " + value);
					col.getView().setText(value);
				}
			}
		}
	}
	
	public static Table createTable(Context context, LinearLayout tableLayout, String tableId) {
		float minWeight = 100;
		Table table = null;
		
		List<Table> tables = getParamsfromXml(context);
		for (Table t : tables) {
			if (t.getId().equals(tableId)) {
				table = t;
				break;
			}
		}
		
		if (table == null) {
			return null;
		}
		
		TextView title = new TextView(context);
		title.setText(table.getName());
		LinearLayout.LayoutParams titleLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		titleLp.setMargins(5, 5, 5, 0);
		title.setBackgroundResource(R.color.white);
		title.setGravity(Gravity.CENTER);
		title.setLayoutParams(titleLp);
		tableLayout.addView(title);

		for (int j = 0; j < table.getRows().size(); j++) {
			LinearLayout tableRow = new LinearLayout(context);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.setMargins(5, 5, 5, 0);
			if (j == table.getRows().size() - 1) {
				lp.setMargins(5, 5, 5, 5);
			}
			tableRow.setBackgroundResource(R.color.white);	
			tableRow.setWeightSum(100);
			tableRow.setLayoutParams(lp);
			
			Row row = table.getRows().get(j);
			
			
			
			for (int i = 0; i < row.getColumns().size(); i++) {
				Column col = row.mColumns.get(i);
				
				float weight = (float) 100 / row.mColumns.size();
				if (weight < minWeight) {
					minWeight = weight;
				}
				
				TextView field = new TextView(context);
				
				lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
				if (i != row.getColumns().size() - 1) {
					lp.setMargins(0, 0, 5, 0);
				} 
				
				if (col.getWeight() != null) {
					lp.weight = Float.parseFloat(col.getWeight());
				} else {
					lp.weight = minWeight;
				}
				
				field.setLayoutParams(lp);
				field.setTextSize(12);
				
				String text = col.getText();
				field.setText("-");
				if (text != null) {
					field.setText(text);
				} 
				
				field.setGravity(Gravity.CENTER);
				field.setBackgroundResource(row.getColor());
				
				String id = col.getId();
				if (id != null) {
					col.setView(field);
				}
				
				tableRow.addView(field);
			}
			tableLayout.addView(tableRow);
		}
		
		return table;
	}
}
