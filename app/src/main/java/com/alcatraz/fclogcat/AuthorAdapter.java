package com.alcatraz.fclogcat;
import android.widget.*;
import android.view.*;
import java.util.*;
import android.content.*;
import android.graphics.drawable.*;

public class AuthorAdapter extends BaseAdapter
{
	Map<Integer,List<String>> data;
	List<Integer> img;
	Context c;
	public AuthorAdapter(Context c,Map<Integer,List<String>> data,List<Integer> img){
		this.data=data;
		this.c=c;
		this.img=img;
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
		return data.get(p1).get(0);
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
			LayoutInflater lf=(LayoutInflater) c.getSystemService(c.LAYOUT_INFLATER_SERVICE);
			p2=lf.inflate(R.layout.author_item,null);
		}
		ImageView iv=(ImageView) p2.findViewById(R.id.authoritemImageView1);
		TextView txv1=(TextView) p2.findViewById(R.id.authoritemTextView1);
		TextView txv2=(TextView) p2.findViewById(R.id.authoritemTextView2);
		iv.setImageResource(img.get(p1));
		txv1.setText(data.get(p1).get(0));
		txv2.setText(data.get(p1).get(1));
		if(txv1.getText().toString().equals(c.getString(R.string.au_l_3))){
			txv2.setVisibility(View.GONE);
		}
		// TODO: Implement this method
		return p2;
	}
	
}
