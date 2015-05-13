package com.datang.miou;

import android.annotation.SuppressLint;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class HoldLastRecieverServer extends Thread{	
	private boolean mRunning = false;
	private Vector<HoldLastRecieverClient> mHoldLastClients = new Vector<HoldLastRecieverClient>();
	public void RegisterClient(HoldLastRecieverClient client)
	{
		mHoldLastClients.add(client);
	}
	public void UnRegisterClient(HoldLastRecieverClient client)
	{
		mHoldLastClients.remove(client);
	}
	@SuppressLint("NewApi")
	public void run(){
		while(mRunning){
			String strAllInfo = ProcessInterface.GetHoldLastIE();
			if (strAllInfo != null && !strAllInfo.isEmpty())
				ParseHoldLastData(strAllInfo);
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void StartThread()
	{
		mRunning = true;
		this.start();
	}	
	public void StopThread()
	{
		mRunning = false;
	}	
	private boolean ParseHoldLastData(String strData){
		String[] strLines = strData.split(";");//split lines
		for (int i=0;i<strLines.length;i++){
			String[] strItems = strLines[i].split("\t");//split items
			if (strItems == null ||strItems.length == 0)
				continue;
			Map<String,String> mapIDValue = new HashMap<String,String>();
			for (int j=0;j<strItems.length;j++){
				String strIDValue = strItems[j];
				System.out.println("ParseHoldLastData "+strIDValue);
				int nSepratePos = strIDValue.indexOf(":");
				if (nSepratePos > 0)
				{
					String strID = strIDValue.substring(0,nSepratePos);//get ieid
					String strValue = strIDValue.substring(nSepratePos+1);//get value
					mapIDValue.put(strID, strValue);
				}
			}
			for(int j = 0;j < mHoldLastClients.size();j++){ 
				HoldLastRecieverClient client = mHoldLastClients.get(j);
				client.ProcessData(mapIDValue);
			} 
		}
		return true;
	}
}

