package com.alcatraz.fclogcat;
import android.content.*;
import android.graphics.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import android.graphics.drawable.*;

public class ListViewAdapter extends BaseAdapter
{
	List<String> content;
	LayoutInflater lf;
	Context ctx;
	ListView lv;
	public ListViewAdapter(Context c, List<String> content,ListView lv)
	{
		this.content=content;
		this.lv=lv;
		ctx=c;
		lf=(LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount()
	{
		// TODO: Implement this method
		return content.size();
	}

	@Override
	public Object getItem(int p1)
	{
		// TODO: Implement this method
		return content.get(p1);
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
			p2=lf.inflate(R.layout.list_board,null);
		}
		TextView txv=(TextView) p2.findViewById(R.id.listboardTextView_time);
		txv.setText(content.get(p1));
		setColor(LogCatAnalyser.getPriority(content.get(p1)),txv);
		updateBackGround(p1,p2);
		return p2;
		
		// TODO: Implement this method
		
	}
	public void addItem(String item)
	{  
		content.add(item);  
	}  
	public void remItem(String item)
	{  
		content.remove(item);  
	}  
	public void remItembypos(int pos)
	{  
		content.remove(pos);  
	} 

	private void setColor(String priority,TextView txv){
		try{
			switch(priority){
				case "E":
					txv.setTextColor(Color.RED);
					break;
				case "D":
					txv.setTextColor(Color.parseColor("#64FFDA"));
					break;
				case "I":
					txv.setTextColor(Color.parseColor("#76FF03"));
					break;
				case "W":
					txv.setTextColor(Color.parseColor("#ff9800"));
					break;
				case "V":

					break;
				
			}
		}catch(Exception e){

		}
	}
	public void updateBackGround(int position,View v){
		if (lv.isItemChecked(position)) {
			v.setBackgroundColor(Color.parseColor("#E0E0E0"));
		} else {
			v.setBackground(null);
		}
	}
}
