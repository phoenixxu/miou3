package com.datang.miou.testplan.ftp;
import com.datang.miou.nlog.msg.cmcc.LteEvtInfo;
import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

import com.datang.miou.ProcessInterface;

import android.os.SystemClock;
import android.util.Log;


/*import com.datang.outum.nlog.LogType;
import com.datang.outum.nlog.LogWriterHandler;
import com.datang.outum.nlog.msg.cmcc.LabInfo;
import com.datang.outum.nlog.msg.cmcc.LteEvtInfo;*/

/**
 * FTP公用抽象类
 * 
 * @author suntongwei
 */
public abstract class TestplanFtpHelper 
{
	// LOG记录
	//	private static final String TAG = "TestplanFtpHelper";
	private static final String TAG = "chenzm";
	
	// 重连次数10
	protected int reLinkCount = 5;
	
	// 是否需要暂停
	protected boolean isPause = false;
	protected boolean isWritePause = true;
	
	/**
	 * 登录FTP服务器
	 * 
	 * @throws IOException
	 * @throws SocketException
	 */
	public void login(FTPClient client, String hostname, int port, String username,String password) throws SocketException, IOException
	{
		//	执行登录
		client.configure(new FTPClientConfig(FTPClientConfig.SYST_UNIX));
		
		//	设置服务器IP和端口
		client.connect(hostname, port);
		
		//	设置用户名和密码
		client.login(username, password);
		
		//	设置控制编码
		client.setControlEncoding("GBK");
		
		//	设置文件类型
		client.setFileType(FTP.BINARY_FILE_TYPE);
		
		//	设置主动or被动模式
		client.enterLocalPassiveMode();
		
		//	响应信息
		if (!FTPReply.isPositiveCompletion(client.getReplyCode()))
		{
			// 登录失败
			Log.i(TAG,"TestplanFtpHelper Login Fail.");
			disconnect(client);
			throw new IOException();
		}
		
		Log.i(TAG,"TestplanFtpHelper Login success.");
	}
	
	/**
	 * 登录事件
	 */
	protected void logLogin(boolean isLogin)
	{
		LteEvtInfo logInfo = new LteEvtInfo();
		if (isLogin) 
		{
			logInfo.setTime(System.currentTimeMillis());
			logInfo.setEvent("4100");
			logInfo.setEventInfo("FTP server logon success");
		}
		else 
		{
			logInfo.setTime(System.currentTimeMillis());
			logInfo.setEvent("4101");
			logInfo.setEventInfo("FTP server logon fail");
		}
		
		try 
		{
			Log.i(TAG, "start Report" + 0);
			ProcessInterface.RpAppEVT(Integer.parseInt(logInfo.getEvent(),16), logInfo.getEventInfo());
			Log.i(TAG, "End Report" + Integer.parseInt(logInfo.getEvent(),16) + logInfo.getEventInfo());
			//LogWriter.writeLog(LogType.CMCC, logInfo);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 返回文件长度
	 * 
	 * @return
	 * @throws IOException
	 */
	public long getFileSize(FTPClient client, String path) throws IOException
	{
		FTPFile[] files = client.listFiles(path);
		if(files.length == 1) 
		{
			long size = files[0].getSize();
			if(size > 0) 
			{
				return size;
			} 
			else
			{
				throw new IOException();
			}
		} 
		else
		{
			throw new IOException();
		}
	}
	
	/**
	 * 
	 * 
	 * @param client
	 */
	protected void disconnect(FTPClient client) 
	{
		if(client != null && client.isConnected())
		{
			try 
			{
				client.logout();
				client.disconnect();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 暂停继续操作
	 * 
	 * @param isPause
	 */
	public void setPause(boolean isPause)
	{
		this.isPause = isPause;
	} 
	
	/**
	 * 记录日志FTP开始
	 */
	protected void writeTestBegin() 
	{
		/*LabInfo sLabInfo = new LabInfo();
		sLabInfo.setTime(System.currentTimeMillis());
		sLabInfo.setLabel(LabInfo.FTP_BEGIN_CODE);
		sLabInfo.setLabelInfo(LabInfo.FTP_BEGIN_INFO);
		LogWriter.writeLog(LogType.CMCC, sLabInfo);*/
	}
	
	/**
	 * 记录日志FTP结束
	 */
	protected void writeTestEnd()
	{
		/*LabInfo eLabInfo = new LabInfo();
		eLabInfo.setTime(System.currentTimeMillis());
		eLabInfo.setLabel(LabInfo.FTP_END_CODE);
		eLabInfo.setLabelInfo(LabInfo.FTP_END_INFO);
		LogWriter.writeLog(LogType.CMCC, eLabInfo);*/
	}
	
	/**
	 * 记录Attempt
	 */
	protected void writeAttempt(boolean isDown) 
	{
		LteEvtInfo lteEvtInfo = new LteEvtInfo();
		if(isDown)
		{
			lteEvtInfo.setEvent("4102");
			lteEvtInfo.setEventInfo("FTP Download Attempt");
		} 
		else
		{
			lteEvtInfo.setEvent("4105");
			lteEvtInfo.setEventInfo("FTP Upload Attempt");
		}
		
		lteEvtInfo.setTime(System.currentTimeMillis());
		try
		{
			Log.i(TAG, "start Report");
			ProcessInterface.RpAppEVT(Integer.parseInt(lteEvtInfo.getEvent(), 16), lteEvtInfo.getEventInfo());
			Log.i(TAG, "End Report" + Integer.parseInt(lteEvtInfo.getEvent(), 16) + lteEvtInfo.getEventInfo());
			//LogWriter.writeLog(LogType.CMCC, lteEvtInfo);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 记录FTP FAIL
	 * 
	 * @param isDown
	 */
	protected void writeTestFail(boolean isDown)
	{
		LteEvtInfo logInfo = new LteEvtInfo();
		logInfo.setTime(System.currentTimeMillis());
		if(isDown) 
		{
			logInfo.setEvent("4103");
			logInfo.setEventInfo("FTP Download Fail");
		} 
		else 
		{
			logInfo.setEvent("4106");
			logInfo.setEventInfo("FTP Upload Fail");
		}
		
		try
		{
			Log.i(TAG, "Start Report");
			ProcessInterface.RpAppEVT(Integer.parseInt(logInfo.getEvent(),16), logInfo.getEventInfo());
			Log.i(TAG, "End Report" + Integer.parseInt(logInfo.getEvent(),16) + logInfo.getEventInfo());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		//LogWriterHandler.getInstance().writeLog(LogType.CMCC, logInfo);
	}
	
	/**
	 * 记录FTP Success
	 * 
	 * @param isDown
	 */
	protected void writeTestSuccess(boolean isDown)
	{
		LteEvtInfo logInfo = new LteEvtInfo();
		logInfo.setTime(System.currentTimeMillis());
		if(isDown) 
		{
			logInfo.setEvent("4104");
			logInfo.setEventInfo("FTP Download Success");
		} 
		else 
		{
			logInfo.setEvent("4107");
			logInfo.setEventInfo("FTP Upload Success");
		}
		
		try
		{
			Log.i(TAG, "Start Report");
			ProcessInterface.RpAppEVT(Integer.parseInt(logInfo.getEvent(),16), logInfo.getEventInfo());
			Log.i(TAG, "End Report" + Integer.parseInt(logInfo.getEvent(),16) + logInfo.getEventInfo());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		//LogWriterHandler.getInstance().writeLog(LogType.CMCC, logInfo);
	}
	
	/**
	 * 记录FTP DROP
	 * 
	 * @param isDown
	 */
	protected void writeTestDrop(boolean isDown) 
	{
		LteEvtInfo logInfo = new LteEvtInfo();
		logInfo.setTime(System.currentTimeMillis());
		if(isDown) 
		{
			logInfo.setEvent("4108");
			logInfo.setEventInfo("FTP Download Drop");
		} 
		else
		{
			logInfo.setEvent("410A");
			logInfo.setEventInfo("FTP Upload Drop");
		}
		
		try
		{
			Log.i(TAG, "Start Report");
			ProcessInterface.RpAppEVT(Integer.parseInt(logInfo.getEvent(), 16), logInfo.getEventInfo());
			Log.i(TAG, "End Report" + Integer.parseInt(logInfo.getEvent(), 16) + logInfo.getEventInfo());
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
			
			//LogWriterHandler.getInstance().writeLog(
		//		LogType.CMCC, logInfo);
	}
	
	/**
	 * 记录FTP Disconnet
	 * 
	 * @param isDown
	 */
	protected void writeTestDisconnet(boolean isDown) 
	{
		LteEvtInfo logInfo = new LteEvtInfo();
		logInfo.setTime(System.currentTimeMillis());
		if(isDown) 
		{
			logInfo.setEvent("4109");
			logInfo.setEventInfo("FTP Download Disconnet");
		} 
		else
		{
			logInfo.setEvent("410B");
			logInfo.setEventInfo("FTP Upload Disconnet");
		}
		
		try
		{
			Log.i(TAG, "Start Report");
			ProcessInterface.RpAppEVT(Integer.parseInt(logInfo.getEvent(), 16), logInfo.getEventInfo());
			Log.i(TAG, "End Report" + Integer.parseInt(logInfo.getEvent(), 16) + logInfo.getEventInfo());
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
			
			//LogWriterHandler.getInstance().writeLog(
		//		LogType.CMCC, logInfo);
	}
	
	/**
	 * 记录日志FTP暂停
	 */
	protected void writeTestSuspend() 
	{
		/*LabInfo eLabInfo = new LabInfo();
		eLabInfo.setTime(System.currentTimeMillis());
		eLabInfo.setLabel("4000");
		eLabInfo.setLabelInfo("Test Suspend");
		LogWriter.writeLog(LogType.CMCC, eLabInfo);*/
	}
	
	/**
	 * 记录日志FTP暂停
	 */
	protected void writeTestContinue() 
	{
		/*LabInfo eLabInfo = new LabInfo();
		eLabInfo.setTime(System.currentTimeMillis());
		eLabInfo.setLabel("4001");
		eLabInfo.setLabelInfo("Test Continue");
		LogWriter.writeLog(LogType.CMCC, eLabInfo);*/
	}
}
