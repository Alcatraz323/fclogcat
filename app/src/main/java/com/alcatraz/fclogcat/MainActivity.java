package com.alcatraz.fclogcat;

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
import android.content.pm.*;
import android.content.pm.PackageManager.*;

public class MainActivity extends AppCompatActivity 
{
	public static String ACTION_TAG="LogCatService";
	List<String> parent = null;
	Map<String, List<String>> map = null;
	android.support.v7.widget.Toolbar tb;
	DrawerLayout dl;
	View padding;
	ListView lv;
	SwipeRefreshLayout srl;
	LinkedList<String> data=new LinkedList<String>();
	List<String> card_data_key=null;
	ListViewAdapter lva;
	ExpandableListView elv;
	ExpandableAdapter file_adp;
	Map<String,List<String>> card_data=null;
	Map<Integer,String> selected=new HashMap<Integer,String>();
	/*____输出参数______*/
	String file_buffer;
	boolean record=false;
	String time_file;
	ListCardAdapter lca;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		initData();
		init_main_card_list();
		initViews();
		if(!isServiceRunning()){
			startService(new Intent(this,BackGroundCatcher.class));
		}
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
		lca=new ListCardAdapter(this,card_data,card_data_key);
		lv.setAdapter(lca);
		srl=(SwipeRefreshLayout) findViewById(R.id.drawerfileSwipeRefreshLayout1);
		srl.setColorSchemeResources(R.color.default_colorPrimary);
		srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){

				@Override
				public void onRefresh()
				{
					initData();
					srl.setRefreshing(false);
					file_adp.notifyDataSetChanged();
					// TODO: Implement this method
				}
			});
		//lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		/*lv.setMultiChoiceModeListener(new MultiChoiceModeListener(){

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
					selected.clear();
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
			*/
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
		File dir=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Android/data/com.alcatraz.fclogcat/");
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
	public void init_main_card_list()
	{
		card_data_key=new ArrayList<String>();
		card_data=new HashMap<String,List<String>>();
		File dir=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Android/data/com.alcatraz.fclogcat/");
		File[] dirs=dir.listFiles();
		if(dirs!=null){
			for(String h:parent){
				File inner=new File(dir.getPath()+"/"+h+"/");
				File[] temp2=inner.listFiles();
				if(temp2!=null){
					for(File t:temp2){
						String[] pkg_t=t.getName().split(" ")[2].split("\\.");
						String pkg="";
						int k=0;
						for(String n:pkg_t){
							if(n.equals(pkg_t[pkg_t.length-1])){
								break;
							}
							if(k!=0){
							pkg=pkg+"."+n;
							}else{
							pkg=pkg+n;
							}
							k++;
						}
						if(!card_data_key.contains(pkg)){
							card_data_key.add(pkg);
							List<String> temp=new ArrayList<String>();
							temp.add(t.getName());
							card_data.put(pkg,temp);
						}else{
							card_data.get(pkg).add(t.getName());
						}
					}
				}
			}
		}
	}
	@Override
	protected void onDestroy()
	{
		// TODO: Implement this method
		super.onDestroy();

	}
	public boolean isServiceRunning()
	{
	    ActivityManager manager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
	    for(ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
	        if((getPackageName()+".BackGroundCatcher").equals(service.service.getClassName())){
	            return true;
	        }
	    }
	    return false;
	}
	public void output(String content, String time)
	{
		/*根据日期输出，你可以写你要的输出*/
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		Date curDate = new Date(System.currentTimeMillis());
		String str = formatter.format(curDate);
		File root=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Android/data/com.alcatraz.fclogcat/");
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
