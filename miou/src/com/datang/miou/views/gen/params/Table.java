package com.datang.miou.views.gen.params;

import java.util.ArrayList;
import java.util.List;

public class Table {
	private String mId;
	private String mName;
	private List<Row> mRows;
	
	public Table() {
		mRows = new ArrayList<Row>();
	}
	
	
	public List<Row> getRows() {
		return mRows;
	}

	public void setRows(List<Row> mRows) {
		this.mRows = mRows;
	}

	public String getId() {
		return mId;
	}
	
	public void setId(String mId) {
		this.mId = mId;
	}
	
	public String getName() {
		return mName;
	}

	public void setName(String name) {
		// TODO Auto-generated method stub
		this.mName = name;
	}
}
