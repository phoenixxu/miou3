package com.datang.miou.testplan.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.io.Util;

import android.os.SystemClock;
import android.util.Log;

import com.datang.miou.datastructure.TestScheme;
import com.datang.miou.testplan.bean.Ftp;
import com.datang.miou.utils.StringUtils;

/**
 * FTP上传子线程
 * 
 * @author suntongwei
 */
public class TestplanFtpUploadChild implements Runnable 
{
	//	private static final String TAG = "TestplanFtpUploadChild";
	private static final String TAG = "chenzm";
	
	// 每个子线程FTP_CLIENT
	private FTPClient cFtpClient = new FTPClient();
	
	// FTP参数集合
	private TestScheme mTestPlan = null;
	
	// 统计线程
	private TestplanFtpStat ftpStatThread;
	
	// BUFFER_SIZE
	private static final int COPY_BUFFER_SIZE = Util.DEFAULT_COPY_BUFFER_SIZE * 50;
	
	public TestplanFtpUploadChild(TestScheme testPlan, TestplanFtpStat ftpStatThread) 
	{
		this.mTestPlan = testPlan;
		this.ftpStatThread = ftpStatThread;
	}
	
	@Override
	public void run() 
	{
		
		Log.i(TAG,"TestplanFtpUploadChild: " + Thread.currentThread().getName());
		
		//	获取输入输出媒体流
		TestplanFtpInputStream is = new TestplanFtpInputStream(getFileSize());
		OutputStream os = null;
		
		try
		{
			//	设置数据超时时间
			cFtpClient.setDataTimeout(getTimeOut() * 1000);
			
			//	执行登录
			cFtpClient.configure(new FTPClientConfig(FTPClientConfig.SYST_UNIX));
			
			//	设置服务器地址和端口
			cFtpClient.connect(getRemoteHost(), getPort());
			
			//	设置用户名和密码
			cFtpClient.login(getAccount(), getPassword());
			
			//	设置FTP控制连接的编码方式
			cFtpClient.setControlEncoding("GBK");
			
			//	设置文件类型
			cFtpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			
			//	设置主动Or被动模式
			cFtpClient.enterLocalPassiveMode();
			
			//	获取返回结果
			if (!FTPReply.isPositiveCompletion(cFtpClient.getReplyCode())) 
			{
				// 登录失败
				Log.e(TAG,"TestplanFtpUploadChild Login Fail.");
				Thread.interrupted();
				return;
			}
			
			Log.i(TAG,"TestplanFtpUploadChild Login Success.");
			
			cFtpClient.setBufferSize(COPY_BUFFER_SIZE);
			
			// 创建目录
			Log.i(TAG,"Path: " + getRemoteFile());
			cFtpClient.changeWorkingDirectory("/");
			if (!StringUtils.isEmpty(getRemoteFile()))
			{
				String[] files = getRemoteFile().split("/");
				
				//	根据给定的名字返回一个能够向服务器上传文件的OutputStream
				if (files.length > 2) 
				{
					for (int i = 1; i < (files.length - 1); i++)
					{
						Log.i(TAG,"makeDirectory: " + files[i]);
						cFtpClient.makeDirectory(files[i]);
						cFtpClient.changeWorkingDirectory(files[i]);
					}
				}
				
				//	上传保存文件名
				Log.i(TAG,"Store File: " + files[(files.length - 1)]);
				os = cFtpClient.storeFileStream(files[(files.length - 1)]);
			} 
			
			
			int bytes;
	        long total = 0l;
	        long lastTotal = 0l;
	        byte[] buffer = new byte[COPY_BUFFER_SIZE];

            while ((bytes = is.read(buffer)) != -1) 
            {
            	Log.i(TAG,"upload read: " + bytes);
            	
            	if(Thread.currentThread().isInterrupted()) 
            	{
            		Log.e(TAG,"TestplanFtpUploadChild Thread Interrupted.");
            		break;
            	}
            	
            	if(ftpStatThread.isEnd())
            	{
            		Log.e(TAG,"TestplanFtpUploadChild Thread End.");
            		break;
            	}
            	
                if (bytes == 0)
                {
                    bytes = is.read();
                    if (bytes < 0) 
                    {
                        break;
                    }
                    
                    //	往流中写一个字节bytes
                    os.write(bytes);
                    os.flush();
                    ++total;
                    
                    if(total > lastTotal)
                    {
						ftpStatThread.setLen(1);
					}
                    lastTotal = total;
                    continue;
                }

                //	字符数组buffer中从下标off开始，长度为bytes的字节写入流中
                os.write(buffer, 0, bytes);
                Log.i(TAG,"upload write: " + bytes);
                
                //	刷空输出流，并输出所有被缓存的字节，由于某些流支持缓存功能，该方法将把缓存中所有内容强制输出到流中
                os.flush();
                
                SystemClock.sleep(10);
                
                total += bytes;
                if(total > lastTotal)
                {
					ftpStatThread.setLen(bytes);
				}
            }
            
            Log.i(TAG,"TestplanFtpUploadChild write finish.");
		} 
		catch (IOException e)
		{
			Log.e(TAG,"TestplanFtpUploadChild Thread Exception:" + Thread.currentThread().getName() + e);
		} 
		finally
		{
			
			Log.e(TAG,"TestplanFtpUploadChild finish.");
			
			if(cFtpClient != null && cFtpClient.isConnected())
			{
				try 
				{
					Log.i(TAG,"TestplanFtpUploadChild disconnect.");
					cFtpClient.disconnect();
				} 
				catch (IOException e)
				{
					Log.e(TAG,"TestplanFtpUploadChild Exception.", e);
				}
			}
			
			//	关闭输出
			if(os != null) 
			{
				try 
				{
					os.flush();
					os.close();
				} 
				catch (IOException e1)
				{
					e1.printStackTrace();
				}
				os = null;
			}
			
			//	关闭输入
			if(is != null)
			{
				try 
				{
					is.close();
				} 
				catch (IOException e1) 
				{
					e1.printStackTrace();
				}
				is = null;
			}
		}
	}

	/**
	 * 获取xx
	 */
	private String getBeginTime()
	{
		return mTestPlan.getTime().getBeginTime();
	}
	
	/**
	 * 获取xx
	 */
	private String getEndTime()
	{
		return mTestPlan.getTime().getEndTime();
	}
	
	/**
	 * 获取xx
	 */
	private String getRepeat()
	{
		return mTestPlan.getCommandList().getCommand().getRepeat();
	}
	
	/**
	 * 获取服务器地址
	 */
	private String getRemoteHost()
	{
		return mTestPlan.getCommandList().getCommand().getRemoteHost();
	}
	
	/**
	 * 获取服务器端口
	 */
	private int getPort()
	{
		return Integer.parseInt(mTestPlan.getCommandList().getCommand().getPort());
	}
	
	/**
	 * 获取用户名
	 */
	private String getAccount()
	{
		return mTestPlan.getCommandList().getCommand().getAccount();
	}
	
	/**
	 * 获取密码
	 */
	private String getPassword()
	{
		return mTestPlan.getCommandList().getCommand().getPassword();
	}
	
	/**
	 * 获取超时时间
	 */
	private int getTimeOut()
	{
		return Integer.parseInt(mTestPlan.getCommandList().getCommand().getTimeOut());
	}
	
	/**
	 * 获取xx
	 */
	private String getPassive()
	{
		return mTestPlan.getCommandList().getCommand().getPassive();
	}
	
	/**
	 * 获取xx
	 */
	private String getBinary()
	{
		return mTestPlan.getCommandList().getCommand().getBinary();
	}
	
	/**
	 * 下载 or 上传
	 */
	private boolean isDown()
	{
		return mTestPlan.getCommandList().getCommand().getDownload().equals("1");
	}
	
	/**
	 * 获取xx
	 */
	private String getRemoteFile()
	{
		return mTestPlan.getCommandList().getCommand().getRemoteFile();
	}
	
	/**
	 * 获取文件大小
	 * 返回：Kbytes
	 */
	private int getFileSize()
	{
		if(mTestPlan.getCommandList().getCommand().getFileSize() != null)
		{
			return (Integer.parseInt(mTestPlan.getCommandList().getCommand().getFileSize()) * 1024);
		}
		else
		{
			return (1024 * 1024);
		}
	}
	
	/**
	 * 获取xx
	 */
	private String getInterval()
	{
		return mTestPlan.getCommandList().getCommand().getInterval();
	}
	
	/**
	 * 获取xx
	 */
	private String getApn()
	{
		return mTestPlan.getCommandList().getCommand().getApn();
	}
	
}
