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
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.datang.miou.ActivitySupport;
import com.datang.miou.R;
import com.datang.miou.annotation.AfterView;
import com.datang.miou.annotation.AutoView;
import com.datang.miou.datastructure.TestCommand;
import com.datang.miou.gps.GPSHelper;
import com.datang.miou.gps.GpsReportThread;
import com.datang.miou.nlog.LogType;
import com.datang.miou.preferences.UserPreferenceActivity;
import com.datang.miou.services.ResultService;
import com.datang.miou.services.TestplanService;
import com.datang.miou.views.gen.GenActivity;
import com.datang.miou.views.inner.InnerActivity;

import com.datang.miou.views.data.DataActivity;//加载数据管理
import com.datang.miou.views.percept.PerceptionActivity;//加载用户感知
import com.datang.miou.xml.XmlRW;

import com.datang.miou.utils.*;
import com.datang.miou.ProcessInterface;
import com.datang.miou.MiouApp;
import com.datang.miou.services.TestplanService;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.datang.business.MeasurementScheduler;
import com.datang.business.UpdateIntent;
import com.datang.business.util.Logger;

/**
 * 程序主界面
 * 
 * @author suntongwei
 */
@AutoView(R.layout.main)
public class MainActivity extends ActivitySupport {

	//打开Trace口数据方法实现
	private Handler mHandler = null;
	private long mExitTime;
	private static Process m_Process = null;
	private static final int SERVER_PORT = 50000;

    private MeasurementScheduler scheduler;
    private boolean isBound = false;
    private boolean isBindingToService = false;
    //-----------------------------
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
            MainActivity.this.sendBroadcast(new UpdateIntent("",
                    UpdateIntent.SCHEDULER_CONNECTED_ACTION));
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Logger.d("onServiceDisconnected called");
            isBound = false;
        }
    };
    public static MainActivity App;

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
	public static void RmProcess(String strName) 
	{
		DataInputStream is = RunCmd("ps " + strName);
		
		String strPID = null;
		while (true)
		{
			try 
			{
				String strLine = is.readLine();
				Log.v("chenzm","strLine: " + strLine);
				if (strLine == null || strLine.isEmpty())
				{
					Log.v("chenzm","quit.");
					break;
				}
				
				if (strLine.indexOf(strName) > 0)
				{
					String[] strInfo = strLine.split(" ");
					if (strInfo.length > 1)
					{
						strPID = strInfo[6];
						Log.v("chenzm","strPID: " + strPID);
						if (strPID != null)
						{
							RunCmd("kill -9 " + strPID);
						}
					}
					
				}
			}
			catch (Exception e)
			{
				break;
			}
		}
	}	
	
    public static void OpenTrace(){
		System.out.println( ProcessInterface.hello());
		
		//	add via chenzm
		RmProcess("diag_socket_log");
		//	add via chenzm end
		
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
		
		//	add via chenzm
		RmProcess("diag_mdlog");
		//	add via chenzm end
		
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
            	//System.exit(0);
            	exit();
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

		//	add via chenzm
        TestplanService.setTestplanService(this,true);
        //	add via chenzm end
		
		ResultService.setServiceAlarm(this, true);
		
		SDCardUtils.getSDCardExist();
		
		
		try
		{
		
		//copy TestPlanTemplate.xml
		String dst	= SDCardUtils.getConfigPath()+"/TestPlanTemplate.xml";
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
		
		//copy TestPlanTemplate.xml
		dst	= SDCardUtils.getConfigPath()+"/TestTypeGen.xml";
		if (!new File(dst).exists()) {  
	        OutputStream out = new FileOutputStream(dst);  
	        byte[] buffer = new byte[1024];  
	        InputStream in;  
	        int readLen = 0;  
	         
	            // 获得输入流  
	            in =  getAssets().open("TestTypeGen.xml"); 
	            while((readLen = in.read(buffer)) != -1){  
	                out.write(buffer, 0, readLen);  
	            }  
	            out.flush();  
	            in.close();  
	          
	        // 把所有小文件都进行写操作后才关闭输出流，这样就会合并为一个文件了  
	        out.close();  
	    }
			
		//copy EventTypes.xml
		dst	= SDCardUtils.getConfigPath()+"/EventTypes.xml";
		if (!new File(dst).exists()) {  
	        OutputStream out = new FileOutputStream(dst);  
	        byte[] buffer = new byte[1024];  
	        InputStream in;  
	        int readLen = 0;  
	         
	            // 获得输入流  
	            in =  getAssets().open("EventTypes.xml"); 
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
		dst  = SDCardUtils.getConfigPath()+"/Tables.xml";
		if (!new File(dst).exists()) {  
	        OutputStream out = new FileOutputStream(dst);  
	        byte[] buffer = new byte[1024];  
	        InputStream in;  
	        int readLen = 0;  
	         
	            // 获得输入流  
	            in =  getAssets().open("Tables.xml");
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
		
		//copy TemplateTestScript.xml
		dst  = SDCardUtils.getConfigPath()+"/TemplateTestScript.xml";
		if (!new File(dst).exists()) {  
	        OutputStream out = new FileOutputStream(dst);  
	        byte[] buffer = new byte[1024];  
	        InputStream in;  
	        int readLen = 0;  
	         
	            // 获得输入流  
	            in =  getAssets().open("TemplateTestScript.xml");
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
				
		List<TestCommand> commands = XmlRW.getTestSchemes(this);
		((MiouApp) getApplication()).setTestSchemes(commands);
		
//		OpenTrace();//打开Trace
		
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			//快速点击两次后退退出程序
			exit();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void exit() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("退出")
			   .setMessage("是否真的退出？")
			   .setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						ProcessInterface.Close();
						System.exit(0);
					}
			   })
			   .setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
			   }).show();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App=this;
        bindToService();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBound) unbindService(serviceConn);
    }

}
