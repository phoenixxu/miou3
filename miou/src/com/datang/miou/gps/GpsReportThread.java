package com.datang.miou.gps;

import com.datang.miou.nlog.LogType;
import com.datang.miou.nlog.LogWriterStatus.LogStatus;
import com.datang.miou.nlog.msg.cmcc.GpsInfo;

import android.location.Location;
import android.os.SystemClock;
import android.util.Log;


import com.datang.miou.ProcessInterface;
//import com.datang.outum.nlog.LogType;
//import com.datang.outum.nlog.LogWriterStatus.LogStatus;
//import com.datang.outum.nlog.msg.cmcc.GpsInfo;
//import com.datang.utils.GPSHelper;

/**
 * GPS涓婃姤绾跨▼
 */
public class GpsReportThread implements Runnable {
    private LogType mLogType = null;
	private String TAG = "GpsReportThread";

    public GpsReportThread(LogType logType) {
        Thread.currentThread().setName("GpsReportThread");
        mLogType = logType;
    }

    @Override
    public void run() {
        if (null == mLogType) {
            return;
        }
        GPSHelper helper = GPSHelper.getGPSHelper();
        while (true) {
            // mConfig.getHandler().sendEmptyMessage(0);
           // LogStatus logStatus = getLogStatus(mLogType);
           // if (logStatus == LogStatus.Start) {
                Location loc = helper.getLocation();
                
                if (loc != null) {
                    GpsInfo gpsInfo = new GpsInfo();
                    gpsInfo.setLatitude(String.valueOf(loc.getLatitude()));
                    gpsInfo.setAltitude(String.valueOf(loc.getAltitude()));
                    gpsInfo.setLongitude(String.valueOf(loc.getLongitude()));
                    gpsInfo.setSpeed(String.valueOf(loc.getSpeed()));
                    gpsInfo.setTime(System.currentTimeMillis());
                    //writeLog(mLogType, gpsInfo);
                    //Log.v("gps",gpsInfo.toString());
                    
                    Log.i(TAG, "location......");
                    Log.i(TAG, "lat = " + Double.parseDouble(gpsInfo.getLatitude()));
                    Log.i(TAG, "lon = " + (int)Double.parseDouble(gpsInfo.getLongitude()));
                    Log.i(TAG, "alt = " + Double.parseDouble(gpsInfo.getAltitude()));
                    Log.i(TAG, "speed = " + (int)Double.parseDouble(gpsInfo.getSpeed()));
                    //ProcessInterface.RpGPS(Double.parseDouble(gpsInfo.getLatitude()),Double.parseDouble(gpsInfo.getAltitude()),(int)Double.parseDouble(gpsInfo.getLongitude()),(int)Double.parseDouble(gpsInfo.getSpeed()));
                    ProcessInterface.RpGPS(Double.parseDouble(gpsInfo.getLongitude()),Double.parseDouble(gpsInfo.getLatitude()),(int)Double.parseDouble(gpsInfo.getAltitude()),(int)Double.parseDouble(gpsInfo.getSpeed()));

             //   }
           // } else if (logStatus == LogStatus.Stoping || logStatus == LogStatus.Stop || logStatus == LogStatus.Unknow) {
           //     break;
            }
            SystemClock.sleep(1000);
        }
        // mConfig.getHandler().sendEmptyMessage(1);
    }
}