package com.alcatraz.fclogcat;
import android.widget.*;
import android.view.*;
import java.util.*;
import android.content.*;
import android.content.pm.*;
import android.graphics.drawable.*;
import android.content.pm.PackageManager.*;

public class ListCardAdapter extends BaseAdapter
{
	List<String> keys;
	Map<String,List<String>> data;
	Context ctx;
	public ListCardAdapter(Context ctx,Map<String,List<String>> data,List<String> keys){
		this.data=data;
		this.ctx=ctx;
		this.keys=keys;
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
		ImageView imgv=(ImageView) p2.findViewById(R.id.listcardcontentImageView1);
		TextView txv=(TextView) p2.findViewById(R.id.listcardcontentTextView1);
		ListView lv=(ListView) p2.findViewById(R.id.listcardcontentListView1);
		if(getIcon(keys.get(p1))!=null){
			imgv.setImageDrawable(getIcon(keys.get(p1)));
			txv.setText(getLabel(keys.get(p1)));
		}else{
			imgv.setImageResource(R.drawable.ic_alert);
			txv.setText(keys.get(p1));
		}
		
		ListAdapter lad=new ArrayAdapter<String>(ctx,android.R.layout.simple_list_item_1,data.get(keys.get(p1)));
		lv.setAdapter(lad);
		setListViewHeight(lv);
		// TODO: Implement this method
		return p2;
	}
	public Drawable getIcon(String pkg){
		PackageManager pm=ctx.getPackageManager();
		try{
			ApplicationInfo ai=pm.getApplicationInfo(pkg,PackageManager.GET_META_DATA);
			return pm.getApplicationIcon(ai);
		}catch(PackageManager.NameNotFoundException e){
		return null;
		}
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
    public void setListViewHeight(ListView listView){
        ListAdapter listAdapter = listView.getAdapter();
        if(listAdapter == null) {  
            return;  
        }  
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }  
        ViewGroup.LayoutParams params = listView.getLayoutParams();  
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
