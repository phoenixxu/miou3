package com.datang.miou.gps;

import android.content.Context;
import android.location.*;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import com.datang.miou.utils.*;
/**
 * GPS工具类
 *
 * @author suntongwei
 * @version 1.0.0
 */
public class GPSHelper {
    public static final String TAG = "GPSHelper";
    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //Log.e(TAG, "onLocationChanged:" + location.getLatitude() + ":" + location.getLongitude());
        	Log.v(TAG, "*********************************************************************");
        	Log.v(TAG, "onLocationChanged:" + location.getLatitude() + ":" + location.getLongitude());
        	
        	/*try{
        	      String data = " This content will append to the end of the file";

        	      File file =new File(SDCardUtils.getConfigPath()+"/abc.cfg");

        	      //if file doesnt exists, then create it
        	      if(!file.exists()){
        	       file.createNewFile();
        	      }

        	      //true = append file
        	      FileWriter fileWritter = new FileWriter(file.getName(),true);
        	             BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
        	             bufferWritter.write(data);
        	             bufferWritter.close();

        	         //System.out.println("Done");

        	     }catch(IOException e){
        	      e.printStackTrace();
        	     }*/

        	
        	
        	
        	
            GPSHelper.this.location = location;
       
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled1：" + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled：" + locationManager.getLastKnownLocation(provider));
        }

        /**
         * GPS状态变化时触发
         */
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                // GPS状态为可见时
                case LocationProvider.AVAILABLE:
                    Log.e(TAG, "GPS.AVAILABLE");
                    break;
                // GPS状态为服务区外时
                case LocationProvider.OUT_OF_SERVICE:
                    Log.e(TAG, "GPS.OUT_OF_SERVICE");
                    break;
                // GPS状态为暂停服务时
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.e(TAG, "GPS.TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }

    };
    // 状态监听
    GpsStatus.Listener listener = new GpsStatus.Listener() {
        public void onGpsStatusChanged(int event) {
            switch (event) {
                // 第一次定位
                case GpsStatus.GPS_EVENT_FIRST_FIX:
                    Log.e(TAG, "first loc");
                    break;
                // 卫星状态改变
                case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                    // log.error("卫星状态改变");
                    // 获取当前状态
                	
                	//Log.e(TAG, "卫星状态改变");
                	
                    GpsStatus gpsStatus = locationManager.getGpsStatus(null);
                    // 获取卫星颗数的默认最大值
                    int maxSatellites = gpsStatus.getMaxSatellites();
                    // 创建一个迭代器保存所有卫星
                    Iterator<GpsSatellite> iters = gpsStatus.getSatellites().iterator();
                    int count = 0;
                    while (iters.hasNext() && count <= maxSatellites) {
                        GpsSatellite s = iters.next();
                        count++;
                    }
                    //Log.e(TAG,"搜索到：" + count + "颗卫星");
                    break;
                // 定位启动
                case GpsStatus.GPS_EVENT_STARTED:
                    Log.e(TAG, "start loc");
                    break;
                // 定位结束
                case GpsStatus.GPS_EVENT_STOPPED:
                    Log.e(TAG, "stop loc");
                    break;
            }
        }

        ;
    };

    // 获取位置管理服务
    LocationManager locationManager;
    String provider;
    private Context ctx;
    private Location location = null;

    private GPSHelper() {
    }

    public static GPSHelper getGPSHelper(Context ctx) {
        GPSHelperHolder.gpsHelper.init(ctx);
        return GPSHelperHolder.gpsHelper;
    }

    public static GPSHelper getGPSHelper() {
        return GPSHelperHolder.gpsHelper;
    }

    private void openGPSSettings() {
        LocationManager alm = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        if (alm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // Toast.makeText(this, "GPS模块正常", Toast.LENGTH_SHORT).show();
            getLocationServer();
        }
        // Toast.makeText(this, "请开启GPS！", Toast.LENGTH_SHORT).show();
        // Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
        // ctx.startActivityForResult(intent, 0); // 此为设置完成后返回到获取界面
    }

    private void getLocationServer() {
        String serviceName = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) ctx.getSystemService(serviceName);
        // 查找到服务信息
        Criteria criteria = new Criteria();
        //设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        //设置是否要求速度
        criteria.setSpeedRequired(true);
        // 设置是否允许运营商收费
        criteria.setCostAllowed(false);
        //设置是否需要方位信息
        criteria.setBearingRequired(false);
        //设置是否需要海拔信息
        criteria.setAltitudeRequired(false);
        // 设置对电源的需求
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        // locationManager.setTestProviderEnabled(LocationManager.GPS_PROVIDER,true);
        provider = locationManager.getBestProvider(criteria, true); // 获取GPS信息
        if (StringUtils.isEmpty(provider)) {
        	 Log.e(TAG, "isEmpty");
            provider = LocationManager.GPS_PROVIDER;
        }
        this.location = locationManager.getLastKnownLocation(provider); // 通过GPS获取位置
        //注意：此处更新准确度非常低，推荐在service里面启动一个Thread，在run中sleep(10000);然后执行handler.sendMessage(),更新位置
       
        Log.e(TAG, "*******requied");
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
        // 绑定监听状态
        locationManager.addGpsStatusListener(listener);

    }

    public void requestLoc() {
        if (locationManager != null)
            // 设置监听器，自动更新的最小时间为间隔N秒(1秒为1*1000，这样写主要为了方便)或最小位移变化超过N米
            locationManager.requestLocationUpdates(provider, 1000, 0, locationListener);
    }

    public void removeLoc() {
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
            // locationManager.setTestProviderEnabled(LocationManager.GPS_PROVIDER,false);
        }
    }

    public void init(Context ctx) {
        this.ctx = ctx;
        openGPSSettings();
    }

    public Location getLocation() {
        return this.location;
    }

    static class GPSHelperHolder {
        private static GPSHelper gpsHelper = new GPSHelper();
    }
}
