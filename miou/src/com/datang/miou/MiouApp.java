package com.datang.miou;

import android.app.Application;
import android.support.v4.app.Fragment;

/**
 * 
 * 
 * @author suntongwei
 */
public class MiouApp extends Application {
	private boolean isGenTesting;
	private boolean isGenReviewing;

	public boolean isGenTesting() {
		return isGenTesting;
	}

	public void setGenTesting(boolean isGenTesting) {
		this.isGenTesting = isGenTesting;
	}

	public boolean isGenReviewing() {
		return isGenReviewing;
	}

	public void setGenReviewing(boolean isGenReviewing) {
		this.isGenReviewing = isGenReviewing;
	}
}
