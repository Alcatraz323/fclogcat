package com.alcatraz.fclogcat;
import android.graphics.*;
import android.os.*;
import android.support.v7.app.*;
import android.text.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.alcatraz.support.implutil.*;
import com.alcatraz.support.v4.appcompat.*;
import java.util.*;
import android.content.*;
import android.support.design.widget.*;
import android.widget.AdapterView.*;

public class MTerminal extends AppCompatActivity
{
	EditText et;
	TextView txv;
	Terminal tm=new Terminal();
	ImageButton imgb;
	Terminal.terminalCallBack tcbb;
	LinkedList<String> ll=new LinkedList<String>();
	android.support.v7.widget.Toolbar tb;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.terminal);
		initSetting();
		initInterface();
		initViews();
		initTerminal();
	}
	public void showRecentDia()
	{

		View v=getLayoutInflater().inflate(R.layout.pre_ad, null);
		FloatingActionButton fab=(FloatingActionButton) v.findViewById(R.id.preadFloatingActionButton1);
		fab.setVisibility(View.GONE);
		ListView lv=(ListView) v.findViewById(R.id.preadListView1);
		ListAdapter la=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ll);
		lv.setAdapter(la);
		final android.support.v7.app.AlertDialog a=new android.support.v7.app.AlertDialog.Builder(this)
			.setTitle(R.string.terminal_1)
			.setView(v)
			.show();
		lv.setPadding(0, 0, 0, 0);
		lv.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
				{
					et.setText(p1.getItemAtPosition(p3).toString());
					a.dismiss();
					// TODO: Implement this method
				}
			});

	}
	public void initInterface()
	{
		tcbb = new Terminal.terminalCallBack(){

			@Override
			public void callback(final String p1)
			{
				runOnUiThread(new Runnable(){

						@Override
						public void run()
						{
							txv.append("\n" + p1);
							et.setText(null);
							// TODO: Implement this method
						}
					});

				// TODO: Implement this method
			}
		};
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// TODO: Implement this method
		MenuInflater mi=new MenuInflater(this);
		mi.inflate(R.menu.terminal, menu);
		return super.onCreateOptionsMenu(menu);
	}
	public void initTerminal()
	{
		tm.startTerminal("help", tcbb);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
				finish();
				break;
			case R.id.item:
				showRecentDia();
				break;
		}
		// TODO: Implement this method
		return super.onOptionsItemSelected(item);
	}

	public void initViews()
	{
		imgb = (ImageButton) findViewById(R.id.terminalcontentImageButton1);
		imgb.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					String ed=et.getText().toString();
					if (ed != null && ed != "")
					{
						txv.append("\n");
						txv.append(Html.fromHtml("<font color=#3f51b5><u>" + "-" + ed + "</u></font>"));
						if (ll.size() >= 6)
						{
							ll.removeFirst();
							ll.add(ed);
						}
						else
						{
							ll.add(ed);
						}
						commitSet(ll);
						if (!tm.isOverRead())
						{
							tm.startTerminal(ed, tcbb);
						}
						else
						{
							tm.applyNewCommand(ed);
						}
						et.setText(null);
					}
					// TODO: Implement this method
				}
			});
		et = (EditText) findViewById(R.id.terminalcontentEditText1);
		txv = (TextView) findViewById(R.id.terminalcontentTextView1);
		tb = (android.support.v7.widget.Toolbar) findViewById(R.id.terminalToolbar1);
		tb.setTitle(R.string.main_drawer_1);
		setSupportActionBar(tb);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		StatusBarUtil.setColor(this, Color.parseColor("#3f51b5"));
	}
	public void initSetting()
	{
		SharedPreferences spf=getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);
		Set<String> ss=new HashSet<String>();
		ss.add("su");
		Set<String> s=spf.getStringSet("sav_ter", ss);
		if (s != null)
		{
			ll.addAll(s);
		}
	}
	public void commitSet(LinkedList<String> r)
	{
		Set<String> s=new LinkedHashSet<String>();
		s.addAll(r);
		SharedPreferences spf=getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);
		SharedPreferences.Editor edit=spf.edit();
		edit.putStringSet("sav_ter", s);
		edit.commit();
	}
}
