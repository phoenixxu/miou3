package com.datang.miou.pdp;

import android.app.Activity;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
public class PdpAttachActivity  {

	

	
		private ConnectivityManager mConnectivityManager;
		
        
		
		public PdpAttachActivity(Context thiscontext)
		{
			super();
			mConnectivityManager = (ConnectivityManager)thiscontext.getSystemService(Context.CONNECTIVITY_SERVICE);
		}
		
		/** Called when the activity is first created. */
		//@Override
		/*public void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			//setContentView(R.layout.main);
			try { 
			mConnectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
			} catch(Exception e1) {
			
				Log.d("a","aaaa");
			
			
			
			
				
			}
			finally
			{
				
			}
			
			
		}*/

		
	/*	public void setMobileDataStatus(boolean enabled) 
		{
			
			try { 
				mConnectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
				} catch(Exception e1) {
				
					Log.d("a","aaaa");
				
				
				
				
					
				}
				finally
				{
					
				}
			
			try
			{
				Log.d("aa","eee");
			Class<?> conMgrClass = Class.forName(mConnectivityManager.getClass().getName());
			}
			catch(Exception e)
			{
				Log.d("aa","bbb");
				e.printStackTrace();
				Log.d("aa","ccc");
			}
			finally
			{
				
			};
		}
		
		*/
			
		// 通过反射实现开启或关闭移动数据
		public void setMobileDataStatus(boolean enabled) 
		{
			try 
			{
				
				Class<?> conMgrClass = Class.forName(mConnectivityManager.getClass().getName());
				
				//得到ConnectivityManager类的成员变量mService（ConnectivityService类型）
				Field iConMgrField = conMgrClass.getDeclaredField("mService");
				iConMgrField.setAccessible(true);
				//mService成员初始化
				Object iConMgr = iConMgrField.get(mConnectivityManager);
				//得到mService对应的Class对象
				Class<?> iConMgrClass = Class.forName(iConMgr.getClass().getName());
				
				Method setMobileDataEnabledMethod = iConMgrClass.getDeclaredMethod(
						"setMobileDataEnabled", Boolean.TYPE);
				setMobileDataEnabledMethod.setAccessible(true);
				
				setMobileDataEnabledMethod.invoke(iConMgr, enabled);
			} catch (ClassNotFoundException e) 
			{
				e.printStackTrace();
			} catch (NoSuchFieldException e) 
			{
				e.printStackTrace();
			} catch (SecurityException e) 
			{
				e.printStackTrace();
			} catch (NoSuchMethodException e) 
			{
				e.printStackTrace();
			} catch (IllegalArgumentException e) 
			{
				e.printStackTrace();
			} catch (IllegalAccessException e) 
			{
				e.printStackTrace();
			} catch (InvocationTargetException e) 
			{
				e.printStackTrace();
			}
			 catch (Exception e) 
				{
					e.printStackTrace();
				}
		}

		
		
	}

