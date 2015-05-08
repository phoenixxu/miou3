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
    private MeasurementScheduler scheduler;
    private boolean isBound = false;
    private boolean isBindingToService = false;
    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private ServiceConnection serviceConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            Logger.d("onServiceConnected called");
            // We've bound to LocalService, cast the IBinder and get LocalService
            // instance
            MeasurementScheduler.SchedulerBinder binder = (MeasurementScheduler.SchedulerBinder) service;
            scheduler = binder.getService();
            isBound = true;
            isBindingToService = false;
            MiouApp.this.sendBroadcast(new UpdateIntent("",
                    UpdateIntent.SCHEDULER_CONNECTED_ACTION));
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Logger.d("onServiceDisconnected called");
            isBound = false;
        }
    };

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

    /**
     * Returns the scheduler singleton instance. Should only be called from the UI thread.
     */
    public MeasurementScheduler getScheduler() {
        if (isBound) {
            return this.scheduler;
        } else {
            bindToService();
            return null;
        }
    }


    private void bindToService() {
        if (!isBindingToService && !isBound) {
            // Bind to the scheduler service if it is not bounded
            Intent intent = new Intent(this, MeasurementScheduler.class);
            bindService(intent, serviceConn, Context.BIND_AUTO_CREATE);
            isBindingToService = true;
        }
    }


}
