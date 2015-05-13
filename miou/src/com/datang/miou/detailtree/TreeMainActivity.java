package com.datang.miou.detailtree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;




import com.zhy.bean.Bean;
import com.zhy.bean.FileBean;
import com.zhy.tree.bean.Node;
import com.zhy.tree.bean.TreeListViewAdapter;
import com.zhy.tree.bean.TreeListViewAdapter.OnTreeNodeClickListener;
import com.zhy.tree_view.*;



import com.datang.miou.R;
import com.datang.miou.utils.*;
import com.datang.miou.views.gen.GenScriptSettingActivity;
import com.datang.miou.views.gen.GenSignalFragment;







public class TreeMainActivity extends Activity
{
	private List<Bean> mDatas = new ArrayList<Bean>();
	private List<FileBean> mDatas2 = new ArrayList<FileBean>();
	private ListView mTree;
	private TreeListViewAdapter mAdapter;
	
	


	 public ArrayList<String> readFile() throws IOException {
		 
		 String filePath = SDCardUtils.getSDPath();
		 
		 filePath = filePath + "/test.txt";
		 
		 Log.d("filepath",filePath);
		 
	        FileInputStream fis=new FileInputStream(filePath);
	        InputStreamReader isr=new InputStreamReader(fis, "UTF-8");
	        BufferedReader br = new BufferedReader(isr);
	        //绠€鍐欏涓?
	        //BufferedReader br = new BufferedReader(new InputStreamReader(
	        //        new FileInputStream("E:/phsftp/evdokey/evdokey_201103221556.txt"), "UTF-8"));
	        String line="";
	        //String[] arrs=null;
	        //Array a = new Array[String];
	       
	        ArrayList<String> list = new ArrayList<String>();  
	        
	        while ((line=br.readLine())!=null) {
	           // arrs=line.split(",");
	           // System.out.println(arrs[0] + " : " + arrs[1] + " : " + arrs[2]);
	           String aftertrimline = line.trim();
	        	
	           if(aftertrimline.length() != 0)
	           {
	        	  list.add(aftertrimline);
	           }
	        }
	        br.close();
	        isr.close();
	        fis.close();
	        
	        return list;
	    }
	
	
	
	  
	
	
	
	/*public  FileInputStream GetStream() throws Exception {
		
		FileInputStream inputStream = null;
		//String filePath = SDCardUtils.getSystemLogPath();
		String filePath = SDCardUtils.getSDPath();
		
		
		
		
		
		File xmlFile = new File(filePath, "test.txt");
		try{
			if(xmlFile.exists()){
				inputStream = new FileInputStream(xmlFile);	
				return inputStream;
			}else{
				return null;
			}
		}catch(FileNotFoundException e){
			Log.w("open file", "Error while new XML file from ", e);
		    return null;
		}
		
		
	}*/




 





	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signal_detail);
		
		
		ImageView mBackButton = (ImageView) findViewById(R.id.app_title_left);
		mBackButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO 自动生成的方法存根
				try {
					if (NavUtils.getParentActivityName((Activity) getApplicationContext()) != null) {
						NavUtils.navigateUpFromSameTask((Activity) getApplicationContext());
					}
				} catch (Exception e) {
					finish();
				}		
			}
		});
		
		Intent intent = getIntent();
		String data = intent.getStringExtra(GenSignalFragment.EXTRA_SIGNAL);
		Log.d("TreeMainActivity",data);
		
		

		initDatas(data);
		mTree = (ListView) findViewById(R.id.id_tree);
		try
		{
			mAdapter = new SimpleTreeAdapter<FileBean>(mTree, this, mDatas2, 10);
			mAdapter.setOnTreeNodeClickListener(new OnTreeNodeClickListener()
			{
				@Override
				public void onClick(Node node, int position)
				{
					if (node.isLeaf())
					{
						Toast.makeText(getApplicationContext(), node.getName(),
								Toast.LENGTH_SHORT).show();
					}
				}

			});

		} catch (Exception e)
		{
			e.printStackTrace();
		}
		mTree.setAdapter(mAdapter);

	}

	private void initDatas(String detaildata) 
	{
		/*ArrayList<String> abc = null;
		try
		{
		   abc = readFile();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
		finally
		{
			
		}*/
		
		String[] dataarray = detaildata.split("\n");
		
		Stack<Integer> ps = new Stack<Integer>();
		int number = 0;
		int count = 0;
		ps.push(count);
		Boolean islastbrace = false;
		
		
		for(String line : dataarray) {
            Log.i("TreeMainActivity",line);
            
			//if(line.startsWith("{"))
            // IMSI DETACH INDICATION这个消息结构有点不一样 做个特殊处理
            if (line.contains("{") || line.trim().endsWith(":"))
			{
            	if (line.trim().equals("{")) {
            		continue;
            	}
            	
            	Log.i("TreeMainActivity", "it is a branch, islastbrace = " + islastbrace);
            	
				//if(islastbrace == false)
				//{
					count++;
					int parent =  ((Integer)ps.peek()).intValue();
					
					Log.i("TreeMainActivity", "add " + line.split(" ::=")[0].trim());
					
					String text = "";
					
					if (line.trim().endsWith(":")) {
						text = line.trim().split(":")[0];
					} else {
						text = line.split(" ::=")[0].trim();
					}
					mDatas2.add(new FileBean(count, parent, text));
				//}
				
				
				
				number++;
				ps.push(count);
				//islastbrace = false;
			}
			//else if(line.startsWith("}"))
            else if (line.contains("}"))
				
			{
				//islastbrace = false;
				number--;
				ps.pop();
				
			}
			else
			{
				//islastbrace = true;
				count++;
				int parent =  ((Integer)ps.peek()).intValue();
				Log.i("TreeMainActivity", "it is a leaf");
				Log.i("TreeMainActivity", "add " + line.trim());

				mDatas2.add(new FileBean(count, parent, line.trim()));
				
			}
				

		}
		
		
		
		
		
		
		
		
		/*
		mDatas.add(new Bean(1, 0, "根目录1"));
		mDatas.add(new Bean(2, 0, "根目录2"));
		mDatas.add(new Bean(3, 0, "根目录3"));
		mDatas.add(new Bean(4, 0, "根目录4"));
		mDatas.add(new Bean(5, 1, "子目录1-1"));
		mDatas.add(new Bean(6, 1, "子目录1-2"));

		mDatas.add(new Bean(7, 5, "子目录1-1-1"));
		mDatas.add(new Bean(8, 2, "子目录2-1"));

		mDatas.add(new Bean(9, 4, "子目录4-1"));
		mDatas.add(new Bean(10, 4, "子目录4-2"));

		mDatas.add(new Bean(11, 10, "子目录4-2-1"));
		mDatas.add(new Bean(12, 10, "子目录4-2-3"));
		mDatas.add(new Bean(13, 10, "子目录4-2-2"));
		mDatas.add(new Bean(14, 9, "子目录4-1-1"));
		mDatas.add(new Bean(15, 9, "子目录4-1-2"));
		mDatas.add(new Bean(16, 9, "子目录4-1-3"));

		mDatas2.add(new FileBean(1, 0, "文件管理系统"));
		mDatas2.add(new FileBean(2, 1, "游戏"));
		mDatas2.add(new FileBean(3, 1, "文档"));
		mDatas2.add(new FileBean(4, 1, "程序"));
		mDatas2.add(new FileBean(5, 2, "war3"));
		mDatas2.add(new FileBean(6, 2, "刀塔传奇"));

		mDatas2.add(new FileBean(7, 4, "面向对象"));
		mDatas2.add(new FileBean(8, 4, "非面向对象"));

		mDatas2.add(new FileBean(9, 7, "C++"));
		mDatas2.add(new FileBean(10, 7, "JAVA"));
		mDatas2.add(new FileBean(11, 7, "Javascript"));
		mDatas2.add(new FileBean(12, 8, "C"));
		*/
	}
	
	
	/*private void initDatasByString(String x)
	{
		x.substring();
	}*/

}
