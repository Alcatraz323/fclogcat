package com.alcatraz.fclogcat;
import android.support.v7.app.*;
import android.os.*;
import android.support.v7.widget.*;
import android.widget.*;
import java.util.*;
import android.widget.AbsListView.*;
import android.view.*;
import android.content.*;
import android.graphics.drawable.*;
import com.alcatraz.support.v4.appcompat.*;
import android.graphics.*;
import java.io.*;
import android.content.pm.*;

public class LogViewer extends AppCompatActivity
{
	android.support.v7.widget.Toolbar tb;
	ListView lv;
	TextView txv;
	ListViewAdapter lva;
	Map<Integer,String> selected=new HashMap<Integer,String>();
	List<String> data=new ArrayList<String>();

	String pkg;
	String label;
	String path;
	Drawable icon;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewer);
		getData(getIntent());
		initData();
		initViews();
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
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// TODO: Implement this method
		switch(item.getItemId()){
			case R.id.item1:
				android.support.v7.app.AlertDialog a=new android.support.v7.app.AlertDialog.Builder(this)
					.setTitle("确认")
					.setMessage("是否删除")
					.setNegativeButton("否",null)
					.setPositiveButton("是",new DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface p1, int p2)
						{
							File f=new File(path);
							f.delete();
							finish();
							// TODO: Implement this method
						}
					}).create();
				new AlertDialogUtil().setSupportDialogColor(a,Color.parseColor("#3f51b5"));
				a.show();
				break;
			case android.R.id.home:
				finish();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void initViews()
	{
		tb=(android.support.v7.widget.Toolbar) findViewById(R.id.viewerToolbar1);
		StatusBarUtil.setColor(this,Color.parseColor("#3f51b5"));
		setSupportActionBar(tb);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		tb.setTitle(label);
		tb.setLogo(icon);
		txv=(TextView) findViewById(R.id.viewercontentTextView1);
		txv.setText(path);
		tb.setSubtitle(pkg);
		lv=(ListView) findViewById(R.id.viewercontentListView1);
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
								copy(t,LogViewer.this);
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
	}
	public void initData(){
		File f=new File(path);
		try{
			FileInputStream fis=new FileInputStream(f);
			if(fis!=null){
				InputStreamReader isr=new InputStreamReader(fis);
				BufferedReader reader=new BufferedReader(isr);
				String line;
				while((line=reader.readLine())!=null){
					data.add(line);
				}
				fis.close();
			}
		}catch(Exception e){}
	}
	public void getData(Intent i)
	{
		Bundle data=i.getExtras();
		pkg=data.getString("pkg");
		label=data.getString("label");
		path=data.getString("path");
		icon=getIcon(pkg);
	}
	public Drawable getIcon(String pkg){
		PackageManager pm=getPackageManager();
		try{
			ApplicationInfo ai=pm.getApplicationInfo(pkg,PackageManager.GET_META_DATA);
			return pm.getApplicationIcon(ai);
		}catch(PackageManager.NameNotFoundException e){
			return null;
		}
	}
}
