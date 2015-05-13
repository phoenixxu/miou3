package com.datang.miou.testplan.ftp;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.net.ftp.FTPClient;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import com.datang.miou.datastructure.TestScheme;
import com.datang.miou.testplan.ftp.TestplanFtpUpload.Callbacks;

/**
 * FTP下载主线程,定时器机制
 * 
 * @author chenzeming
 */
public class TestplanFtpDown extends TestplanFtpHelper
{
	public interface Callbacks {
		public void updateUIOnFinnished();
	}
	
	//	private static final String TAG = "TestplanFtpDown";
	private static final String TAG = "chenzm";

	// CTX
	private Context mContext;
	
	// FTP参数集合
	private TestScheme mTestPlan = null;
	
	//	FTP业务线程数
	private int mThreadNum = 5;
	
	//	下载参数_是否根据时间下载
	private Boolean isDownByTime = false;
	
	//	下载参数_下载时间
	private Integer downTime = 0;
	
	// 已执行次数
	private int curNum = 0;
	
	// 每次下载的开始时间
	private long startTime = 0l;
	
	// 无速率时间
	private long notChangeLenTime = 0l;
	
	//	创建下载子线程池
	private ExecutorService executorServices = Executors.newCachedThreadPool();
	private List<Future<?>> mFutureList = null;
	
	// 统计线程
	private TestplanFtpStat statThread = null;
	private ExecutorService statExec = Executors.newSingleThreadExecutor();
	private Future<?> curStatFuture = null;
	
	//	xx
	private Handler mHandler;
	
	//	ftp Down 用例
	private Runnable mRunnableFtpDown;
	
	//	xx
	private long mTotalLen = 0l;
	
	private enum FtpRunState
	{
		STOP,
		WAITING,
		START
	}
	
	//	xx
	private FtpRunState mRunState = FtpRunState.STOP;
	
	/**
	 * 构造类
	 * @param ctx
	 * @param ftp
	 */
	public TestplanFtpDown(Context ctx, TestScheme testplan,int threadnum)
	{
		// ctx
		mContext = ctx;
		
		// 参数
		mTestPlan = testplan;
		
		//	ftp 业务线程数
		mThreadNum = threadnum;
		
		//	xx
		mHandler = new Handler();
		
		//	呼叫操作
		mRunnableFtpDown = new Runnable(){
			public void run()
			{	
				Log.i(TAG,"@@@@ mRunnableFtpDown: " + Thread.currentThread().getName());
				new Thread(new Runnable()
				{
					@Override
					public void run() 
					{
						handleFtpDown();
					}
				}).start();
			}
		};
		
		//	初始化统计线程
		statThread = new TestplanFtpStat(mContext);
		
		//	初始化状态值
		mRunState = FtpRunState.STOP;
	}
	
	/**
	 * 开始ftp下载
	 */
	public void StartFtpDown()
	{
		Log.i(TAG, "=========== StartFtpDown.");
		
		Log.i(TAG,"@@@@ StartFtpDown: " + Thread.currentThread().getName());
		
		//	判断状态
		if(mRunState != FtpRunState.STOP)
		{
			Log.i(TAG, "Ftp Down is running.");
			return;
		}
		
		//	置状态
		mRunState = FtpRunState.START;
		
		//	重置计数
		curNum = 0;
		
		//	启动ftp线程，同一个线程中会异常
		new Thread(new Runnable()
		{
			@Override
			public void run() 
			{
				handleFtpDown();
			}
		}).start();
		
		Log.i(TAG, "=========== StartFtpDown end.");
	}
	
	/**
	 * 停止ftp下载
	 */
	public void StopFtpDown()
	{
		Log.i(TAG, "=========== StopFtpDown.");
		
		//	
		mHandler.removeCallbacks(mRunnableFtpDown);
		
		//	置状态
		mRunState =  FtpRunState.WAITING;
		
		Log.i(TAG, "=========== StopFtpDown end.");
	}
	
	/**
	 * 查询业务
	 *  返回：true 有业务进行中；false 无业务进行中
	 */
	public boolean isFtpDown()
	{
		return (mRunState != FtpRunState.STOP);
	}
	
	/**
	 * FtpDown
	 */
	public void handleFtpDown()
	{
		Log.i(TAG,"@@@@ handleFtpDown: " + Thread.currentThread().getName());
		
		//	Ftp用例
		DoFtpDown();
		
		//	增加已执行次数
		curNum++;
		
		Log.i(TAG, "curNum: " + curNum + " Repeat:" + getRepeat());
		
		// 设置ftpdown操作定时器
		if((curNum < getRepeat()) && (mRunState != FtpRunState.WAITING))
		{	
			//	定时业务
			mHandler.postDelayed(mRunnableFtpDown,(1000 * getInterval()));
			
			//	打印
			Log.i(TAG, "ftp upload set timer @@@@@.");
		}
		else
		{
			//	判断当前是否处于通用测试界面
			((Callbacks) mContext).updateUIOnFinnished();
			
			//	置状态
			mRunState = FtpRunState.STOP;
			
			//	打印
			Log.i(TAG, "ftp upload set state stop @@@@@.");
		}
	}
	
	/**
	 * FtpDown
	 */
	private void DoFtpDown()
	{
		Log.d(TAG, "======== DoFtpDown.");
		
		//	创建一个FTP连接
		FTPClient client = new FTPClient();
		
		//	判断线程是否还在执行，停止子线程
		if(Thread.currentThread().isInterrupted())
		{
			//	终止子线程，并且终止当前线程
			stopFtpDownChild();
			Log.i(TAG, "DoFtpDown quit.");
			return;
		}
		
		//	当已写入暂停日志时
		if(!isWritePause) 
		{
			//	记录继续执行日志
			isWritePause = !isWritePause;
			writeTestContinue();
		}
		
		// 记录每次下载的开始时间
		startTime = System.currentTimeMillis();
		
		// 登陆ftp服务器
		if(!DoFtpLogin(client))
		{
			//	登陆失败
			return;
		}
		
		//	启动统计子线程
		startStatChild();
		
		//	启动ftp业务子线程
		startFtpDownChild();
		
		// 	停止ftp业务子线程
		stopFtpDownChild();
		
		//	停止统计子线程
		stopStatChild();
		
		Log.d(TAG, "======== DoFtpDown end.");
	}
	
	/**
	 * 登陆
	 */
	private boolean DoFtpLogin(FTPClient client)
	{
		try 
		{
			//	打印 登录,如果不成功，则启用重连，重连次数为10
			Log.i(TAG, getRemoteHost() + "," +getPort() + "," + getAccount() + "," + getPassword());
			Log.i(TAG,"DownFilePath: " + getRemoteFile());
			
			//	登录,如果不成功
			login(client, getRemoteHost(), getPort(), getAccount(), getPassword());
			
			//	获取下载文件长度
			mTotalLen = getFileSize(client, getRemoteFile());
			Log.i(TAG,"Remote File Total Len: " + mTotalLen + " Bytes");
			Log.i(TAG,"Remote File Total Len: " + mTotalLen/(1024*1024) + " MB");
			
			// 执行断开连接
			disconnect(client);
			
			Log.i(TAG,"login success.");
			
			logLogin(true);
			
			return true;
		} 
		catch (SocketException e)
		{
			Log.i(TAG,"Socket Error: " + e);
		}
		catch (IOException e1) 
		{
			Log.i(TAG,"Ftp Restart Link Server: " + e1);
			
			//	执行断开连接
			disconnect(client);
		}
		
		Log.e(TAG, "login fail.");
		
		logLogin(false);
		
		return false;
	}
	
	private void startStatChild()
	{
		// 启动统计线程
		if(curStatFuture != null) 
		{
			curStatFuture.cancel(true);
		}
		
		//	初始化统计
		statThread.init(mTestPlan, isDown(),startTime, mTotalLen);
		curStatFuture = statExec.submit(statThread);
	}
	
	private void stopStatChild()
	{
		// 退出统计线程
		if(curStatFuture != null)
		{
			curStatFuture.cancel(true);
		}
	}
	
	/**
	 * 开始子进程
	 */
	private void startFtpDownChild()
	{
		//	xx
		mFutureList = new ArrayList<Future<?>>();
		
		//	下载进程，开启子线程来进行下载，采用极速模式，无视文件分段
		Log.i(TAG, "mThreadNum: " + mThreadNum);
		for(int i = 0; i < mThreadNum; i++)
		{
			mFutureList.add(executorServices.submit(new TestplanFtpDownChild(mTestPlan, statThread)));
		}
		
		//	Ftp Download Attempt
		writeAttempt(true);
		
		//	打印
		Log.i(TAG, "===== ftp down waiting...");
		
		//	等待子线程下载结束
		long lastLen = 0l;
		while(!Thread.currentThread().isInterrupted() && (mRunState == FtpRunState.START))
		{
			//	如果根据时间下载，则当下载时长大于需要的下载时间，则退出循环
			if(isDownByTime() && (System.currentTimeMillis() - startTime) > (getDownTime() * 1000))
			{
				statThread.setTotalLen(statThread.getLen());
				Log.i(TAG, "startFtpDownChild quit.");
				
				//	FTP Download Disconnet
				writeTestDisconnet(true);
				break;
			}
			
			//	当下载结束则退出等待
			if(!isDownByTime() && statThread.isEnd()) 
			{
				Log.e(TAG,"startFtpDownChildn is End.");
				
				//	Ftp Download Success
				writeTestSuccess(true);
				break;
			}
			
			//	当前下载速率
			long curLen = statThread.getLen();
			
			//	判断速率是否发生变化
			if(curLen == lastLen) 
			{
				//	把开始无变化记录时间
				if(notChangeLenTime == 0)
				{
					notChangeLenTime = System.currentTimeMillis();
				} 
				else 
				{
					//	如果无速率时间超过超时时间
					if((System.currentTimeMillis() - notChangeLenTime) > (getTimeOut() * 1000)) 
					{
						//	如果速率为0, Fail
						if(curLen == 0) 
						{
							//	Ftp Download Fail
							writeTestFail(true);
						} 
						else 
						{
							//	FTP Download Drop
							writeTestDrop(true);
						}
						Log.i(TAG, "startFtpDownChild timeout.");
						break;
					}
				}
			} 
			else 
			{
				notChangeLenTime = 0;
			}
			
			//	记录长度
			lastLen = curLen;
			
			SystemClock.sleep(1000);
		}
		
		Log.i(TAG, "===== ftp down waiting end");
	}
	
	/**
	 * 终止子线程
	 */
	private void stopFtpDownChild() 
	{
		Log.i(TAG, "Ftp Child" + "(" + mFutureList.size() + ")" + " Stop...");
		if(mFutureList.size() > 0) 
		{
			for(Future<?> f : mFutureList) 
			{
				f.cancel(true);
			}
		}
		mFutureList = null;
	}

	/**
	 * 返回执行次数
	 */
	public int getExecNum() 
	{
		return curNum;
	}
	
	private boolean isDownByTime()
	{
		return isDownByTime;
	}
	
	public Integer getDownTime() 
	{
		return downTime;
	}
	
	public void setDownTime(Integer downTime) 
	{
		this.downTime = downTime;
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
	private int getRepeat()
	{
		return Integer.parseInt(mTestPlan.getCommandList().getCommand().getRepeat());
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
	private int getInterval()
	{
		return Integer.parseInt(mTestPlan.getCommandList().getCommand().getInterval());
	}
	
	/**
	 * 获取xx
	 */
	private String getApn()
	{
		return mTestPlan.getCommandList().getCommand().getApn();
	}
}
