package com.alcatraz.fclogcat;
import android.content.*;
import android.graphics.*;
import android.net.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.alcatraz.support.v4.appcompat.*;
import java.io.*;
import java.util.*;
import android.content.pm.*;

public class ExpandableAdapter extends BaseExpandableListAdapter
{
	List<String> parent;
	Map<String,List<String>> data;
	Context ctx;
	public ExpandableAdapter(Context context,List<String> parent,Map<String,List<String>> data){
		this.parent=parent;
		this.data=data;
		ctx=context;
	}
	@Override
	public int getGroupCount()
	{
		// TODO: Implement this method
		return parent.size();
	}

	@Override
	public int getChildrenCount(int p1)
	{
		// TODO: Implement this method
		String key = parent.get(p1);
		int size=data.get(key).size();
		return size;
	}

	@Override
	public Object getGroup(int p1)
	{
		// TODO: Implement this method
		return parent.get(p1);
	}

	@Override
	public Object getChild(int p1, int p2)
	{
		// TODO: Implement this method
		String key = parent.get(p1);
		return (data.get(key).get(p2));
	}

	@Override
	public long getGroupId(int p1)
	{
		// TODO: Implement this method
		return p1;
	}

	@Override
	public long getChildId(int p1, int p2)
	{
		// TODO: Implement this method
		return p2;
	}

	@Override
	public boolean hasStableIds()
	{
		// TODO: Implement this method
		return true;
	}

	@Override
	public View getGroupView(int p1, boolean p2, View p3, ViewGroup p4)
	{
		// TODO: Implement this method
		if (p3 == null) {
			LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			p3 = inflater.inflate(R.layout.drawer_parent_item, null);
		}
		TextView tv = (TextView) p3.findViewById(R.id.drawerparentitemTextView1);
		tv.setText(parent.get(p1));
		return p3;
	}

	@Override
	public View getChildView(int p1, int p2, boolean p3, View p4, ViewGroup p5)
	{
		// TODO: Implement this method
		final String key = parent.get(p1);
		final String info = data.get(key).get(p2);
		if (p4 == null) {
			LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			p4 = inflater.inflate(R.layout.drawer_child_item, null);
		}
		TextView tv = (TextView) p4.findViewById(R.id.drawerchilditemTextView1);
		tv.setText(info);
		ImageButton imgb_1=(ImageButton) p4.findViewById(R.id.drawerchilditemImageButton1);
		ImageButton imgb_2=(ImageButton) p4.findViewById(R.id.drawerchilditemImageButton2);
		ImageButton imgb_3=(ImageButton) p4.findViewById(R.id.drawerchilditemImageButton3);
		imgb_1.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					File f=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Android/data/com.alcatraz.fclogcat/"+key+"/"+info);
					Intent i=new Intent(ctx,LogViewer.class);
					i.putExtras(getNotiData(f.getPath()));
					ctx.startActivity(i);
					// TODO: Implement this method
				}
			});
		imgb_2.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					File f=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Android/data/com.alcatraz.fclogcat/"+key+"/"+info);
					Intent i=new Intent(Intent.ACTION_SEND);
					i.setType("*/*");
					i.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(f));
					ctx.startActivity(i);
					// TODO: Implement this method
				}
			});
		imgb_3.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					android.support.v7.app.AlertDialog a=new android.support.v7.app.AlertDialog.Builder(ctx)
					.setTitle("确认")
					.setMessage("是否删除")
					.setNegativeButton("否",null)
					.setPositiveButton("是",new DialogInterface.OnClickListener(){

							@Override
							public void onClick(DialogInterface p1, int p2)
							{
								File f=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Android/data/com.alcatraz.fclogcat/"+key+"/"+info);
								f.delete();
								ctx.sendBroadcast(new Intent(MainActivity.ACTION_TAG));
								// TODO: Implement this method
							}
						}).create();
						new AlertDialogUtil().setSupportDialogColor(a,Color.parseColor("#3f51b5"));
						a.show();
					// TODO: Implement this method
				}
			});
		return p4;
	}

	@Override
	public boolean isChildSelectable(int p1, int p2)
	{
		// TODO: Implement this method
		return true;
	}
	public Bundle getNotiData(String dir){
		File f=new File(dir);
		String file_name=f.getName();
		Bundle data=new Bundle();
		data.putString("path",dir);
		data.putString("pkg",getPkg(file_name));
		data.putString("label",getLabel(getPkg(file_name)));
		return data;
	}
	public String getPkg(String file_name){
		String[] pkg_t=file_name.split(" ")[2].split("\\.");
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
		return pkg;
	}

	public String getLabel(String pkg){
		PackageManager pm=ctx.getPackageManager();
		try{
			ApplicationInfo ai=pm.getApplicationInfo(pkg,PackageManager.GET_META_DATA);
			return pm.getApplicationLabel(ai).toString();
		}catch(PackageManager.NameNotFoundException e){
			return null;
		}
	}
}
