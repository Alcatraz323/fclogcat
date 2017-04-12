package com.alcatraz.fclogcat;
import android.widget.*;
import android.view.*;
import java.util.*;
import java.io.*;
import android.content.*;

public class FilePickAdapter extends BaseAdapter
{
	List<File> file;
	LayoutInflater lf;
	public FilePickAdapter(List<File> data,Context ctx){
		lf=(LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
		file=data;
	}
	@Override
	public int getCount()
	{
		// TODO: Implement this method
		return file.size();
	}

	@Override
	public Object getItem(int p1)
	{
		// TODO: Implement this method
		return file.get(p1);
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
			p2=lf.inflate(R.layout.fpitem,null);
		}
		ImageView imgv=(ImageView) p2.findViewById(R.id.fpitemImageView1);
		TextView txv=(TextView) p2.findViewById(R.id.fpitemTextView1);
		if(file.get(p1).isDirectory()){
			imgv.setImageResource(R.drawable.ic_folder);
		}else{
			imgv.setImageResource(R.drawable.ic_file);
		}
		txv.setText(file.get(p1).getName());
		// TODO: Implement this method
		return p2;
	}
	
}
