package com.alcatraz.fclogcat;
import android.content.*;
import android.graphics.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import android.graphics.drawable.*;
import android.text.*;

public class ListViewAdapter extends BaseAdapter
{
	List<String> content;
	LayoutInflater lf;
	Context ctx;
	ListView lv;
	String pkg;
	String hb_c;
	Set<String> hl_fil;
	SharedPreferences spf;
	boolean hnn;
	public ListViewAdapter(Context c, List<String> content, ListView lv, String pkg,Set<String> set,String h,boolean hbb)
	{
		this.content=content;
		this.lv=lv;
		this.pkg=pkg;
		ctx=c;
		hnn=hbb;
		hb_c=h;
		hl_fil=set;
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
		ImageView imgv=(ImageView) p2.findViewById(R.id.listboardImageView1);
		String raw=content.get(p1);
		for(String i:hl_fil){
			String org=Html.escapeHtml(Html.fromHtml(i));
			if(org.equals("$pkg$")){
				String f=i.replace("$pkg$",pkg);
				raw=raw.replace(pkg,f);
			}else{
				raw=raw.replace(org,i);
			}
		}
		if(hnn){
		boolean hasGotExc=false;
			if(p1>=2&&!hasGotExc){
				if(content.get(p1).contains("Exception")){
					raw="<b>"+raw+"</b>";
					hasGotExc=true;
				}
				
			}
			if(p1>=2){
				if(content.get(p1).contains("at "+pkg)){
					raw="<b>"+raw+"</b>";
					imgv.setImageResource(R.drawable.ic_star);
					
				}
			}
			}
		txv.setText(Html.fromHtml(raw));
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
	public void updateBackGround(int position, View v)
	{
		Log.e("Alcn","item"+lv.isItemChecked(position));
		if(lv.isItemChecked(position)){
			
			v.setBackgroundColor(Color.parseColor("#E0E0E0"));
		}else{
			v.setBackground(null);
		}
	}
	
}
