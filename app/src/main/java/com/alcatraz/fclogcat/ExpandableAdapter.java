package com.alcatraz.fclogcat;
import android.app.*;
import android.content.*;
import android.view.*;
import android.widget.*;
import java.util.*;

public class ExpandableAdapter extends BaseExpandableListAdapter
{
	List<String> parent;
	Map<String,List<String>> data;
	Activity a;
	public ExpandableAdapter(Activity context,List<String> parent,Map<String,List<String>> data){
		this.parent=parent;
		this.data=data;
		a=context;
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
			LayoutInflater inflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
		String key = parent.get(p1);
		String info = data.get(key).get(p2);
		if (p4 == null) {
			LayoutInflater inflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			p4 = inflater.inflate(R.layout.drawer_child_item, null);
		}
		TextView tv = (TextView) p4.findViewById(R.id.drawerchilditemTextView1);
		tv.setText(info);
		return p4;
	}

	@Override
	public boolean isChildSelectable(int p1, int p2)
	{
		// TODO: Implement this method
		return true;
	}
	
}
