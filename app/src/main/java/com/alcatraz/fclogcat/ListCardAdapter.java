package com.alcatraz.fclogcat;
import android.widget.*;
import android.view.*;
import java.util.*;
import android.content.*;
import android.content.pm.*;
import android.graphics.drawable.*;
import android.content.pm.PackageManager.*;
import android.widget.AdapterView.*;
import android.os.*;
import java.io.*;
import android.support.v7.widget.*;
import android.graphics.*;

public class ListCardAdapter extends BaseAdapter
{
	List<String> keys;
	Map<String,List<String>> data;
	Context ctx;
	Utils u;
	String location;
	public ListCardAdapter(Context ctx, Map<String,List<String>> data, List<String> keys,OverallOperate app)
	{
		this.data=data;
		this.ctx=ctx;
		this.keys=keys;
		u=app.getUtilInstance();
		location=(String) u.getPreference(Utils.PreferenceType.STRING,"location",SpfConstants.getDefaultStoragePosition());
	}
	@Override
	public int getCount()
	{
		// TODO: Implement this method
		return keys.size();
	}

	@Override
	public Object getItem(int p1)
	{
		// TODO: Implement this method
		return keys.get(p1);
	}

	@Override
	public long getItemId(int p1)
	{
		// TODO: Implement this method
		return p1;
	}

	@Override
	public View getView(int p1, View p2, ViewGroup p3)
	{
		if(p2==null){
			LayoutInflater lf=(LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			p2=lf.inflate(R.layout.list_card,null);
		}
		final int id=p1;
		ImageView imgv=(ImageView) p2.findViewById(R.id.listcardcontentImageView1);
		TextView txv=(TextView) p2.findViewById(R.id.listcardcontentTextView1);
		ListView lv=(ListView) p2.findViewById(R.id.listcardcontentListView1);
		CardView cv=(CardView) p2.findViewById(R.id.listcardCardView1);
		LinearLayout llh=(LinearLayout) p2.findViewById(R.id.listcardcontentLinearLayout1);
		if(u.getPreference(Utils.PreferenceType.STRING,"theme","blue").equals("night")){
			llh.setBackgroundColor(ctx.getResources().getColor(R.color.nightmode_colorPrimary));
		}
		final LinearLayout ll=(LinearLayout) p2.findViewById(R.id.listcardLinearLayout1);
		ll.setVisibility(View.GONE);
		cv.setOnLongClickListener(new OnLongClickListener(){

				@Override
				public boolean onLongClick(View p1)
				{
					AnimateUtil.playstart(ll);
					Button btn_1=(Button) ll.findViewById(R.id.listcardactionButton1);
					Button btn_2=(Button) ll.findViewById(R.id.listcardactionButton2);
					btn_1.setOnClickListener(new OnClickListener(){

							@Override
							public void onClick(View p1)
							{
								AnimateUtil.playEnd(ll);
								// TODO: Implement this method
							}
						});
					btn_2.setOnClickListener(new OnClickListener(){

							@Override
							public void onClick(View p1)
							{
								deleteAll(data.get(keys.get(id)));
								ctx.sendBroadcast(new Intent().setAction(MainActivity.ACTION_TAG));
								// TODO: Implement this method
							}
						});
					// TODO: Implement this method
					return false;
				}
			});
		if(getIcon(keys.get(p1))!=null){
			imgv.setImageDrawable(getIcon(keys.get(p1)));
			txv.setText(getLabel(keys.get(p1)));
		}else{
			imgv.setImageResource(R.drawable.ic_alert);
			txv.setText(keys.get(p1));
		}
		lv.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
				{
					String path=getPath(data.get(keys.get(id)).get(p3));
					Intent i=new Intent(ctx,LogViewer.class);
					i.putExtras(getNotiData(path,keys.get(id)));
					ctx.startActivity(i);
					// TODO: Implement this method
				}
			});
		ListAdapter lad=new ArrayAdapter<String>(ctx,android.R.layout.simple_list_item_1,data.get(keys.get(p1)));
		lv.setAdapter(lad);
		setListViewHeight(lv);
		// TODO: Implement this method
		return p2;
	}
	public Drawable getIcon(String pkg)
	{
		PackageManager pm=ctx.getPackageManager();
		try{
			ApplicationInfo ai=pm.getApplicationInfo(pkg,PackageManager.GET_META_DATA);
			return pm.getApplicationIcon(ai);
		}catch(PackageManager.NameNotFoundException e){
			return null;
		}
	}
	public String getLabel(String pkg)
	{
		PackageManager pm=ctx.getPackageManager();
		try{
			ApplicationInfo ai=pm.getApplicationInfo(pkg,PackageManager.GET_META_DATA);
			return pm.getApplicationLabel(ai).toString();
		}catch(PackageManager.NameNotFoundException e){
			return null;
		}
	}
    public void setListViewHeight(ListView listView)
	{
        ListAdapter listAdapter = listView.getAdapter();
        if(listAdapter==null){  
            return;  
        }  
        int totalHeight = 0;
        for(int i = 0; i<listAdapter.getCount(); i++){
            View listItem = listAdapter.getView(i,null,listView);
            listItem.measure(0,0);
            totalHeight+=listItem.getMeasuredHeight();
        }  
        ViewGroup.LayoutParams params = listView.getLayoutParams();  
        params.height=totalHeight+(listView.getDividerHeight()*(listAdapter.getCount()-1));
        listView.setLayoutParams(params);
    }
	public Bundle getNotiData(String dir, String pkg)
	{
		Bundle data=new Bundle();
		data.putString("path",dir);
		data.putString("pkg",pkg);
		data.putString("label",getLabel(pkg));
		return data;
	}
	public String getPath(String file_name)
	{
		String[] process=file_name.split(" ");
		File dir=new File(location);
		File[] dirs=dir.listFiles();
		if(dirs!=null){
			for(File i:dirs){
				if(i.getName().indexOf(process[0])>0){
					return i.getPath()+"/"+file_name;
				}
			}
		}
		return null;
	}
	public void deleteAll(List<String> file){
		File dir=new File(location);
		File[] dirs=dir.listFiles();
		List<String> parent=new ArrayList<String>();
		if(dirs!=null){
			for(File i:dirs){
				parent.add(i.getName());
			}
			
			for(String h:parent){
				File inner=new File(dir.getPath()+"/"+h+"/");
				File[] temp2=inner.listFiles();
				if(temp2!=null){
					for(File t:temp2){
						for(String l:file){
							if(t.getName().equals(l)){
								t.delete();
							}
						}
					}
					
				}
			}
			}
	}
}
