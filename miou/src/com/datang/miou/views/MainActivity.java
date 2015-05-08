package com.datang.miou.views;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import com.datang.business.MeasurementScheduler;
import com.datang.business.UpdateIntent;
import com.datang.business.util.Logger;
import com.datang.miou.ActivitySupport;
import com.datang.miou.R;
import com.datang.miou.annotation.AfterView;
import com.datang.miou.annotation.AutoView;
import com.datang.miou.gps.GPSHelper;
import com.datang.miou.gps.GpsReportThread;
import com.datang.miou.nlog.LogType;
import com.datang.miou.preferences.UserPreferenceActivity;
import com.datang.miou.services.ResultService;
import com.datang.miou.views.gen.GenActivity;
import com.datang.miou.views.inner.InnerActivity;

import com.datang.miou.views.data.DataActivity;//加载数据管理
import com.datang.miou.views.percept.PerceptionActivity;//加载用户感知

import com.datang.miou.utils.*;
import com.datang.miou.ProcessInterface;

/**
 * 程序主界面
 * 
 * @author suntongwei
 */
@AutoView(R.layout.main)
public class MainActivity extends ActivitySupport {

	//打开Trace口数据方法实现
	private Handler mHandler = null;
	private static Process m_Process = null;
	private static final int SERVER_PORT = 50000;
	public static DataInputStream RunCmd(String strCmd) {
		DataOutputStream os = null;
		DataInputStream is = null;
		try {
			m_Process = Runtime.getRuntime().exec("su");
			os = new DataOutputStream(m_Process.getOutputStream());
			os.writeBytes(strCmd + "\nexit\n");
			os.flush();
			m_Process.waitFor();
			is = new DataInputStream(m_Process.getInputStream());
		} catch (Exception e) {
			return null;
		} finally {
			try {
				if (os != null) {
					os.close();
				}
			} catch (Exception e) {
			}
		}
		return is;
	}

	@SuppressLint("NewApi")
	public static void RmProcess(String strName) {
		DataInputStream is = RunCmd("ps |grep "+strName);
		String strPID = null;
		while (true) {
			try {
				String strLine = is.readLine();
				if (strLine == null || strLine.isEmpty())
					break;
				if (strLine.indexOf(strName) > 0) {
					String[] strInfo = strLine.split(" ");
					if (strInfo.length > 1) {
						strPID = strInfo[1];
						break;
					}
				}
			} catch (Exception e) {
				break;
			}
		}
		if (strPID != null)
			RunCmd("kill -9 " + strPID);
	}	
	
    public static void OpenTrace(){
		System.out.println( ProcessInterface.hello());
		RmProcess("diag_socket_log");
		new Thread(new Runnable() {
			public void run() {
				RunCmd("diag_socket_log -a 127.0.0.1 -p " + SERVER_PORT + " -r 100");
			}
		}).start();		
		//发送logmask
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new Thread(new Runnable() {
			public void run() {
				RunCmd("diag_mdlog -f /sdcard/miou/config/logmask.cfg");
			}
		}).start();			
		ProcessInterface.StartHdConnect();
		
		System.out.println( ProcessInterface.hello());
    }
	//-----------------------------
	
	@AfterView
	private void init() {
		
		Log.v("aaa","begin aaaaaaaa************************************");
		LogType x = LogType.CMCC;
        
        GPSHelper helper = GPSHelper.getGPSHelper(this);       
        GpsReportThread a = new GpsReportThread(x);
        Thread b = new Thread(a);
        b.start();
        Log.v("bbb","end bbbbbbbbbbbbbbbbbbbbbbbbb************************************");
		
		/**
		 * 通用测试
		 */
		f(R.id.main_btn_gen).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(mContext, GenActivity.class));
			}
		});
		
		/**
		 * 室内测试
		 */
		f(R.id.main_btn_inner).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				startActivity(new Intent(mContext, InnerActivity.class));
			}
		});
		
		/**
		 * 设置
		 */
		f(R.id.main_btn_setting).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				startActivity(new Intent(mContext, UserPreferenceActivity.class));
			}
		});
		
        /**
         * 数据管理
         */
        f(R.id.main_btn_datas).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, DataActivity.class));
            }
        });

        
        /**
         * 退出
         */
        f(R.id.main_btn_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	System.exit(0);
            }
        });
        
        /**
         * 用户感知
         */
        f(R.id.main_btn_cus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, PerceptionActivity.class));
            }
        });

		ResultService.setServiceAlarm(this, true);
		
		SDCardUtils.getSDCardExist();
		
		
		try
		{
		
		//copy TestPlanTemplate.xml
		String dst  = SDCardUtils.getConfigPath()+"/TestPlanTemplate.xml";
		if (!new File(dst).exists()) {  
	        OutputStream out = new FileOutputStream(dst);  
	        byte[] buffer = new byte[1024];  
	        InputStream in;  
	        int readLen = 0;  
	         
	            // 获得输入流  
	            in =  getAssets().open("TestPlanTemplate.xml"); 
	            while((readLen = in.read(buffer)) != -1){  
	                out.write(buffer, 0, readLen);  
	            }  
	            out.flush();  
	            in.close();  
	          
	        // 把所有小文件都进行写操作后才关闭输出流，这样就会合并为一个文件了  
	        out.close();  
	    }
		
		//copy logmask.cfg
		dst  = SDCardUtils.getConfigPath()+"/logmask.cfg";
		if (!new File(dst).exists()) {  
	        OutputStream out = new FileOutputStream(dst);  
	        byte[] buffer = new byte[1024];  
	        InputStream in;  
	        int readLen = 0;  
	         
	            // 获得输入流  
	            in =  getAssets().open("logmask.cfg");
	            while((readLen = in.read(buffer)) != -1){  
	                out.write(buffer, 0, readLen);  
	            }  
	            out.flush();  
	            in.close();  
	          
	        // 把所有小文件都进行写操作后才关闭输出流，这样就会合并为一个文件了  
	        out.close();  
	    }  
		
		//copy QualComm_IE.xml
		dst  = SDCardUtils.getConfigPath()+"/QualComm_IE.xml";
		if (!new File(dst).exists()) {  
	        OutputStream out = new FileOutputStream(dst);  
	        byte[] buffer = new byte[1024];  
	        InputStream in;  
	        int readLen = 0;  
	         
	            // 获得输入流  
	            in =  getAssets().open("QualComm_IE.xml");
	            while((readLen = in.read(buffer)) != -1){  
	                out.write(buffer, 0, readLen);  
	            }  
	            out.flush();  
	            in.close();  
	          
	        // 把所有小文件都进行写操作后才关闭输出流，这样就会合并为一个文件了  
	        out.close();  
	    }
		
		//copy tables.xml
		dst  = SDCardUtils.getConfigPath()+"/tables.xml";
		if (!new File(dst).exists()) {  
	        OutputStream out = new FileOutputStream(dst);  
	        byte[] buffer = new byte[1024];  
	        InputStream in;  
	        int readLen = 0;  
	         
	            // 获得输入流  
	            in =  getAssets().open("tables.xml");
	            while((readLen = in.read(buffer)) != -1){  
	                out.write(buffer, 0, readLen);  
	            }  
	            out.flush();  
	            in.close();  
	          
	        // 把所有小文件都进行写操作后才关闭输出流，这样就会合并为一个文件了  
	        out.close();  
	    }  

		//copy QualComm_IE.xml
		dst  = SDCardUtils.getConfigPath()+"/otherconfig.xml";
		if (!new File(dst).exists()) {  
	        OutputStream out = new FileOutputStream(dst);  
	        byte[] buffer = new byte[1024];  
	        InputStream in;  
	        int readLen = 0;  
	         
	            // 获得输入流  
	            in =  getAssets().open("otherconfig.xml");
	            while((readLen = in.read(buffer)) != -1){  
	                out.write(buffer, 0, readLen);  
	            }  
	            out.flush();  
	            in.close();  
	          
	        // 把所有小文件都进行写操作后才关闭输出流，这样就会合并为一个文件了  
	        out.close();  
	    }
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
				
		//OpenTrace();//打开Trace
		
	}
	
	
	 /**
     * copy
     *
     * @param src
     * @param dest
     * @return
     */
    public static void copy(FileInputStream  in, File dest) {
        int length = 2097152;
       // FileInputStream in = null;
        FileOutputStream out = null;
        try {
           // in = new FileInputStream(src);
            out = new FileOutputStream(dest);
            FileChannel inC = in.getChannel();
            FileChannel outC = out.getChannel();
            ByteBuffer b = null;
            while (true) {
                if (inC.position() == inC.size()) {
                    inC.close();
                    outC.close();
                    return;
                }
                if ((inC.size() - inC.position()) < length) {
                    length = (int) (inC.size() - inC.position());
                } else
                    length = 2097152;
                b = ByteBuffer.allocateDirect(length);
                inC.read(b);
                b.flip();
                outC.write(b);
                outC.force(false);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(in!=null) try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(out!=null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

	
}
