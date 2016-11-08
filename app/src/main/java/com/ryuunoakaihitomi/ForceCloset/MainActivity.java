package com.ryuunoakaihitomi.ForceCloset;

import android.app.*;
import android.os.*;
import android.support.v7.app.*;
import android.support.v4.widget.*;
import android.view.*;
import com.alcatraz.support.v4.appcompat.*;
import android.widget.*;
import java.util.*;
import com.alcatraz.support.implutil.*;
import android.util.*;
import java.text.*;
import java.io.*;
import android.widget.AbsListView.*;
import android.content.*;
import android.net.*;

public class MainActivity extends AppCompatActivity 
{
	List<String> parent = null;
	Map<String, List<String>> map = null;
	android.support.v7.widget.Toolbar tb;
	DrawerLayout dl;
	View padding;
	ListView lv;
	LinkedList<String> data=new LinkedList<String>();
	ListViewAdapter lva;
	ExpandableListView elv;
	ExpandableAdapter file_adp;
	Map<Integer,String> selected=new HashMap<Integer,String>();
	/*____输出参数______*/
	String file_buffer;
	boolean record=false;
	String time_file;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		initData();
		initViews();

		LogCat l=new LogCat("logcat -v threadtime",LogCat.start_flag_require_root,getPackageCodePath());
		l.readLogCat(new LogCat.LogCatInterface(){

				@Override
				public void onUpdate(final String p1)
				{
					runOnUiThread(new Runnable(){

							@Override
							public void run()
							{
								if(LogCatAnalyser.isCrash(p1)){//是否来自AndroidRuntime E等级
									if(record){//记录
										file_buffer=file_buffer+p1+"\n";
									}
									if(LogCatAnalyser.isCrashStart(p1)){//开始记录调用
										file_buffer=p1+"\n";
										record=true;
										time_file=LogCatAnalyser.getTime(p1);
									}
									
									if(data.size()==128){//显示条数
										data.removeFirst();
										data.add(p1);
										lva.notifyDataSetChanged();
									}else{
										data.add(p1);
										lva.notifyDataSetChanged();
									}
								}else{
									//结束单次错误，并以日期形式输出
									record=false;
									if(file_buffer!=null){
										output(file_buffer,time_file);
										file_buffer=null;
										time_file=null;
									}
								}

								// TODO: Implement this method
							}
						});
					// TODO: Implement this method
				}
			});
    }

	@Override
	protected void onResume()
	{
		// TODO: Implement this method
		super.onResume();
		initData();//刷新后台抓取文件列表
		file_adp.notifyDataSetChanged();
	}
	
	public void initViews()
	{
		tb=(android.support.v7.widget.Toolbar) findViewById(R.id.mainToolbar1);
		dl=(DrawerLayout) findViewById(R.id.mainDrawerLayout1);
		elv=(ExpandableListView) findViewById(R.id.drawerfileExpandableListView1);
		lv=(ListView) findViewById(R.id.mainListView1);
		lva=new ListViewAdapter(this,data,lv);
		lv.setAdapter(lva);
		lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		lv.setMultiChoiceModeListener(new MultiChoiceModeListener(){

				@Override
				public boolean onCreateActionMode(ActionMode p1, Menu p2)
				{
					// TODO: Implement this method
					p1.getMenuInflater().inflate(R.menu.action_mode,p2);
					return true;
				}

				@Override
				public boolean onPrepareActionMode(ActionMode p1, Menu p2)
				{
					// TODO: Implement this method
					return true;
				}
				public void copy(String content, Context context)  
				{  

					ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);  
					cmb.setText(content.trim());  
				}  
				@Override
				public boolean onActionItemClicked(ActionMode p1, MenuItem p2)
				{
					//ActionMode动作
					switch(p2.getItemId()){
						case R.id.act_item1:
							if(selected!=null){
							String t="";
							for(int i=0;i<data.size();i++){
								if(selected.containsKey(i)){
									t=t+selected.get(i).toString()+"\n";
								}
							}
							copy(t,MainActivity.this);
							selected.clear();
							}
							break;
							case R.id.act_item2:
							if(selected!=null){
								String t="";
								for(int i=0;i<data.size();i++){
									if(selected.containsKey(i)){
										t=t+selected.get(i).toString()+"\n";
									}
								}
								Intent i=new Intent(Intent.ACTION_SEND);
								i.setType("text/plain");
								i.putExtra(Intent.EXTRA_TEXT,t);
								startActivity(i);
								selected.clear();
								}
								break;
					}
					lv.clearChoices();
					p1.finish();
					// TODO: Implement this method
					return true;
				}

				@Override
				public void onDestroyActionMode(ActionMode p1)
				{
					// TODO: Implement this method
				}

				@Override
				public void onItemCheckedStateChanged(ActionMode p1, int p2, long p3, boolean p4)
				{
					if(p4){
					selected.put(p2,lv.getItemAtPosition(p2).toString());
					}else{
						selected.remove(p2);
					}
					p1.setTitle("选择了"+selected.size()+"项");
					lva.notifyDataSetChanged();
					// TODO: Implement this method
				}
			});
		file_adp=new ExpandableAdapter(this,parent,map);
		elv.setAdapter(file_adp);
		padding=findViewById(R.id.mainView1);
		immersive();
	}

	private void immersive()
	{
		setSupportActionBar(tb);
		new DrawerLayoutUtil().setImmersiveToolbarWithDrawer(tb,dl,MainActivity.this,padding,"#3f51b5",Build.VERSION.SDK_INT);
		/*已进行SDK是否大于等于19判断*/
	}
	public void initData()/*drawerlayout的文件列表加载*/
	{
		parent=new ArrayList<String>();
		File dir=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Android/data/com.ryunnoakaihitomi.ForceCloset/");
		File[] dirs=dir.listFiles();
		if(dirs!=null){
		for(File i:dirs){
			parent.add(i.getName());
		}
		map=new HashMap<String,List<String>>();
		for(String h:parent){
			File inner=new File(dir.getPath()+"/"+h+"/");
			List<String> temp=new ArrayList<String>();
			File[] temp2=inner.listFiles();
			if(temp2!=null){
			for(File t:temp2){
				temp.add(t.getName());
			}
			map.put(h,temp);
			}
		}
		}
	}
	public void output(String content,String time){
		/*根据日期输出，你可以写你要的输出*/
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss"); 
		Date curDate = new Date(System.currentTimeMillis());
		String str = formatter.format(curDate);
		File root=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Android/data/com.ryunnoakaihitomi.ForceCloset/");
		File data_dir=new File(root.getPath()+"/"+str.split(" ")[0]+"/");
		data_dir.mkdirs();
		File data_file=new File(data_dir.getPath()+"/"+time+".log");
		if(!data_file.exists()){
		try{
			data_file.createNewFile();
			FileOutputStream fos=new FileOutputStream(data_file);
			fos.write(content.getBytes());
			fos.close();
		}catch(IOException e){}
		}else{
			
		}
		initData();
		file_adp.notifyDataSetChanged();
	}
}
