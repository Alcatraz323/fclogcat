package com.alcatraz.fclogcat;
import android.support.v7.app.*;
import android.os.*;
import java.util.*;
import android.content.pm.*;
import android.content.pm.PackageManager.*;
import android.widget.*;
import android.support.v7.widget.*;
import android.widget.AdapterView.*;
import android.view.*;
import android.content.*;
import android.net.*;
import com.alcatraz.support.v4.appcompat.*;
import android.graphics.*;
import android.support.design.widget.*;

public class Author extends ThemedActivity
{
	List<Integer> imgs=new ArrayList<Integer>();
	Map<Integer,List<String>> data=new HashMap<Integer,List<String>>();
	ListView lv;
	android.support.v7.widget.Toolbar tb;
	AppBarLayout abl;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.author);
		initData();
		initViews();
	}
	public void showDetailDev(){
		android.support.v7.app.AlertDialog g=new android.support.v7.app.AlertDialog.Builder(this)
		.setTitle(R.string.au_l_2)
		.setMessage("主代码:Alcatraz\n应用图标:busstop12\nShortcuts图标:busstop12\n简体中文:Alcatraz\n英文:Alcatraz\n繁体中文:busstop12\n感谢busstop12的帮助")
		.setPositiveButton(R.string.ad_pb,null)
		.create();
		new AlertDialogUtil().setSupportDialogColor(g,Color.parseColor("#3f51b5"));
		g.show();
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// TODO: Implement this method
		switch (item.getItemId())
		{
			case android.R.id.home:
				finish();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void initViews()
	{
		tb = (android.support.v7.widget.Toolbar) findViewById(R.id.mytoolbar_1);
		setSupportActionBar(tb);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		abl=(AppBarLayout) findViewById(R.id.appbar);
		setupStaticColorPadding(rgb);
		lv = (ListView) findViewById(R.id.authorcontentListView1);
		AuthorAdapter aa=new AuthorAdapter(this, data, imgs);
		lv.setAdapter(aa);
		lv.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
				{
					if (p1.getItemAtPosition(p3).toString().equals(getString(R.string.au_l_3)))
					{
						startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Alcatraz323")));
					}else if (p1.getItemAtPosition(p3).toString().equals(getString(R.string.au_l_4))){
						String str = "market://details?id=" + getPackageName();
						Intent localIntent = new Intent("android.intent.action.VIEW");
						localIntent.setData(Uri.parse(str));
						startActivity(localIntent);
					}else if(p1.getItemAtPosition(p3).toString().equals(getString(R.string.au_l_2))){
						showDetailDev();
					}
				}
				// TODO: Implement this metho
			});
	}
	public void initData()
	{
		imgs.add(R.drawable.ic_information_outline);
		imgs.add(R.drawable.ic_account);
		imgs.add(R.drawable.ic_github);
		imgs.add(R.drawable.ic_arrow_right);
		List<String> l1=new ArrayList<String>();
		l1.add(getString(R.string.au_l_1));
		l1.add("---");
		List<String> l2=new ArrayList<String>();
		l2.add(getString(R.string.au_l_2));
		l2.add(getString(R.string.au_l_2_1));
		List<String> l3=new ArrayList<String>();
		l3.add(getString(R.string.au_l_3));
		l3.add("");
		List<String> l4=new ArrayList<String>();
		l4.add(getString(R.string.au_l_4));
		l4.add(getString(R.string.au_l_4_1));
		data.put(0, l1);
		data.put(1, l2);
		data.put(2, l3);
		data.put(3, l4);

	}
}
