package com.alcatraz.fclogcat;

import android.annotation.*;
import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.support.v4.widget.*;
import android.support.v7.app.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.text.*;
import java.util.*;
import com.alcatraz.support.v4.appcompat.*;
import android.support.design.widget.*;
import android.view.View.*;
import android.net.*;

public class MainActivity extends AppCompatActivity 
{
	public static String ACTION_TAG="LogCatService";
	List<String> parent = new ArrayList<String>();;
	Map<String, List<String>> map = new HashMap<String,List<String>>();
	android.support.v7.widget.Toolbar tb;
	DrawerLayout dl;
	ListView lv;
	LinearLayout emp_1;
	LinearLayout emp_2;
	innerRec rec=new innerRec();
	AppBarLayout abl;
	View v;
	SwipeRefreshLayout srl;
	LinkedList<String> data=new LinkedList<String>();
	List<String> card_data_key=new ArrayList<String>();
	ExpandableListView elv;
	ExpandableAdapter file_adp;
	Map<String,List<String>> card_data=new HashMap<String,List<String>>();
	Map<Integer,String> selected=new HashMap<Integer,String>();
	/*____输出参数______*/
	String file_buffer;
	boolean record=false;
	String time_file;
	ListCardAdapter lca;
	Boolean ce;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		ce=getSharedPreferences(getPackageName()+"preferences",MODE_PRIVATE).getBoolean("ce",false);
		initData();
		init_main_card_list();
		initViews();
		regist();
		if(!isServiceRunning()){
			startService(new Intent(this,BackGroundCatcher.class));
		}

    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// TODO: Implement this method
		MenuInflater mi=new MenuInflater(this);
		mi.inflate(R.menu.main_menu,menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onResume()
	{
		// TODO: Implement this method
		super.onResume();
		if(!isServiceRunning()){
			startService(new Intent(this,BackGroundCatcher.class));
		}
		ref();
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// TODO: Implement this method
		switch(item.getItemId()){
			case R.id.item1:
				startActivity(new Intent(this,Preferences.class));
				break;
			case R.id.item2:
				startActivity(new Intent(this,Author.class));
				break;
			case R.id.item3:
				sendBroadcast(new Intent().setAction(BackGroundCatcher.RESTART_TAG));
				break;
			case R.id.item4:
				sendBroadcast(new Intent().setAction(BackGroundCatcher.SHUTDOWN_TAG));
				finish();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void initViews()
	{
		tb=(android.support.v7.widget.Toolbar) findViewById(R.id.mainToolbar1);
		dl=(DrawerLayout) findViewById(R.id.mainDrawerLayout1);
		elv=(ExpandableListView) findViewById(R.id.drawerfileExpandableListView1);
		lv=(ListView) findViewById(R.id.mainListView1);
		abl=(AppBarLayout) findViewById(R.id.mainAppBarLayout1);
		lca=new ListCardAdapter(this,card_data,card_data_key);
		lv.setAdapter(lca);
		emp_1=(LinearLayout) findViewById(R.id.mainLinearLayout1);
		emp_2=(LinearLayout) findViewById(R.id.emptyviewLinearLayout1);
		v=findViewById(R.id.mainView1);
		srl=(SwipeRefreshLayout) findViewById(R.id.drawerfileSwipeRefreshLayout1);
		srl.setColorSchemeResources(R.color.default_colorPrimary);
		srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){

				@Override
				public void onRefresh()
				{
					ref();
					srl.setRefreshing(false);
					// TODO: Implement this method
				}
			});

		file_adp=new ExpandableAdapter(this,parent,map);
		elv.setAdapter(file_adp);
		setSupportActionBar(tb);
		new DrawerLayoutUtil().setImmersiveToolbarWithDrawer(tb,dl,this,v,"#3f51b5",Build.VERSION.SDK_INT);
		if(Build.VERSION.SDK_INT<Build.VERSION_CODES.LOLLIPOP){
			v.setVisibility(View.GONE);
		}
	}
	public void ref()
	{
		initData();
		init_main_card_list();
		if(map.size()==0||card_data.size()==0){
			emp_1.setVisibility(View.VISIBLE);
			emp_2.setVisibility(View.VISIBLE);
		}else{
			emp_1.setVisibility(View.GONE);
			emp_2.setVisibility(View.GONE);
		}
		file_adp.notifyDataSetChanged();
		lca.notifyDataSetChanged();
	}

	public void initData()/*drawerlayout的文件列表加载*/
	{
		parent.clear();
		File dir=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Android/data/com.alcatraz.fclogcat/");
		File[] dirs=dir.listFiles();
		if(dirs!=null){
			for(File i:dirs){
				parent.add(i.getName());
			}
			map.clear();
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
		card_data_key.clear();
		card_data.clear();
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
		if(ce){
		sendBroadcast(new Intent().setAction(BackGroundCatcher.SHUTDOWN_TAG));
		}
		unregisterReceiver(rec);
	}
	public void regist()
	{
		IntentFilter ifil=new IntentFilter();
		ifil.addAction(ACTION_TAG);
		registerReceiver(rec,ifil);
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
	class innerRec extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context p1, Intent p2)
		{
			ref();
			// TODO: Implement this method
		}
	}
}
