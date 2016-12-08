package com.alcatraz.fclogcat;
import android.widget.*;
import android.view.*;
import java.util.*;
import android.content.*;
import android.text.*;
import android.view.View.*;
import android.support.v4.app.*;

public class PrefAdapter extends BaseAdapter
{
	List<String> data;
	Context c;
	public PrefAdapter(List<String> data, Context c)
	{
		this.data=data;
		this.c=c;
	}
	@Override
	public int getCount()
	{
		// TODO: Implement this method
		return data.size();
	}

	@Override
	public Object getItem(int p1)
	{
		// TODO: Implement this method
		return data.get(p1);
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
			LayoutInflater lf=(LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			p2=lf.inflate(R.layout.filter_edit,null);
		}
		final int id=p1;
		TextView txv=(TextView) p2.findViewById(R.id.filtereditTextView1);
		ImageButton imgb=(ImageButton) p2.findViewById(R.id.filtereditImageButton1);
		txv.setText(Html.fromHtml(data.get(p1)));
		imgb.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					data.remove(id);
					notifyDataSetChanged();
					SharedPreferences spf=c.getSharedPreferences(c.getPackageName()+"_preferences",c.MODE_PRIVATE);
					SharedPreferences.Editor ed=spf.edit();
					ed.putStringSet("hldb",list2Set(data));
					ed.commit();
					// TODO: Implement this method
				}
			});
			
		// TODO: Implement this method
		return p2;
	}
	public Set<String> list2Set(List<String> l){
		Set<String> d=new HashSet<String>();
		for(String j:l){
			d.add(j);
		}
		return d;
	}
	
	
}
