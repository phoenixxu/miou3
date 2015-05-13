package com.datang.miou.datastructure;

import android.net.Uri;

public class InnerMap {
	private String mName;
	private Uri mUri;
	
	public InnerMap(String name) {
		this.mName = name;
	}

	public String getName() {
		return this.mName;
	}
	
	public void setName(String name) {
		this.mName = name;
	}
	
	
	public Uri getUri() {
		return mUri;
	}

	public void setUri(Uri uri) {
		this.mUri = uri;
	}

	@Override
	public String toString() {
		return this.mName;
	}
}
