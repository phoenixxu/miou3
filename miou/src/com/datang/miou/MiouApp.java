package com.datang.miou;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.datang.business.MeasurementScheduler;
import com.datang.business.UpdateIntent;
import com.datang.business.util.Logger;

/**
 * @author suntongwei
 */
public class MiouApp extends Application {
    public static MiouApp APP;
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

    @Override
    public void onCreate() {
        super.onCreate();
        APP = this;
    }




}
