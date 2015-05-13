package com.datang.miou.testplan.ftp;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;

import android.util.Log;

import com.datang.miou.datastructure.TestScheme;
import com.datang.miou.testplan.bean.Ftp;

/**
 * FTP多线程下载子线程类，采用apache的FTPClient
 * 
 * @author chenzeming
 */
public class TestplanFtpDownChild implements Runnable 
{
//	private static final String TAG = "TestplanFtpDownChild";
	private static final String TAG = "chenzm";
	
	// 每个子线程FTP_CLIENT
	private FTPClient cFtpClient = new FTPClient();

	// 缓冲区大小
	private final int ByteSize = 1048576;
	
	// FTP参数集合
	private TestScheme mTestPlan = null;
	
	// 统计线程
	private TestplanFtpStat ftpStatThread;
	
	public TestplanFtpDownChild(TestScheme testplan, TestplanFtpStat ftpStatThrea)
	{
		this.mTestPlan = testplan;
		this.ftpStatThread = ftpStatThrea;
	}
	
	@Override
	public void run() 
	{
		Log.i(TAG,"TestplanFtpDownChild: " + Thread.currentThread().getName());
		
		InputStream in = null;
		try
		{
			// 设置数据超时时间
			cFtpClient.setDataTimeout(getTimeOut() * 1000);
			
			//	执行登录
			cFtpClient.configure(new FTPClientConfig(FTPClientConfig.SYST_UNIX));
			
			//	设置服务器IP和端口
			cFtpClient.connect(getRemoteHost(), getPort());
			
			//	设置用户名和密码
			cFtpClient.login(getAccount(), getPassword());
			
			//	设置控制编码格式
			cFtpClient.setControlEncoding("GBK");
			
			//	设置文件类型
			cFtpClient.setFileType(FTP.BINARY_FILE_TYPE);
			
			//	设置被动模式
			cFtpClient.enterLocalPassiveMode();
			
			//	获取响应信息
			if (!FTPReply.isPositiveCompletion(cFtpClient.getReplyCode())) 
			{
				// 登录失败
				Log.i(TAG,"ftp Down Child Login Fail and quit.");
				
				//	子线程终止
				Thread.interrupted();
				return;
			}
			
			Log.i(TAG,"ftp Down Child Login success.");
			
			// 设置缓冲区大小
			cFtpClient.setBufferSize(ByteSize);
			
			//	数据传输
			in = cFtpClient.retrieveFileStream(getRemoteFile());
			byte[] bytes = new byte[ByteSize];
			int c;
			while ((c = in.read(bytes)) != -1) 
			{
				// 判断线程是否被终止
				if(Thread.currentThread().isInterrupted())
				{
					Log.e(TAG,"Ftp Down Child Thread is Interrupted.");
					break;
				}
				
				if(ftpStatThread.isEnd()) 
				{
            		Log.e(TAG,"Ftp Down Child Thread End.");
            		break;
            	}
				
				//	设置统计
				if(ftpStatThread != null)
				{
					Log.i(TAG,"file size: " + c);
					ftpStatThread.setLen(c);
				}
			}
		} 
		catch (IOException e)
		{
			Log.e(TAG,"Ftp Down Child Thread Exception:" + Thread.currentThread().getName() + e);
		} 
		finally
		{
			if(cFtpClient != null && cFtpClient.isConnected()) 
			{
				try 
				{
					Log.i(TAG,"ftp Down Child disconnect.");
					cFtpClient.disconnect();
				} 
				catch (IOException e) 
				{
					Log.e(TAG,"Ftp Down Child Ftp LogOut Exception.", e);
				}
			}
			
			if(in != null)
			{
				try 
				{
					in.close();
				} 
				catch (IOException e)
				{
					e.printStackTrace();
				}
				in = null;
			}
		}
		
		Log.i(TAG, "TestplanFtpDownChild end.");
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
