package com.datang.miou.views.gen.params;

import java.util.ArrayList;
import java.util.List;

public class Row {
	//每行所包含的列数
	List<Column> mColumns;
	//这行的背景颜色
	int mColor;
	
	public Row() {
		mColumns = new ArrayList<Column>();
	}

	public List<Column> getColumns() {
		return mColumns;
	}

	public void setColumns(List<Column> mColumns) {
		this.mColumns = mColumns;
	}

	public int getColor() {
		return mColor;
	}

	public void setColor(int mColor) {
		this.mColor = mColor;
	}
	
	
}
