package com.alcatraz.fclogcat;
import android.content.*;
import android.graphics.*;
import android.net.*;
import android.os.*;
import android.preference.*;
import android.support.design.widget.*;
import android.text.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.alcatraz.support.v4.appcompat.*;
import java.io.*;
import java.util.*;

import android.view.View.OnClickListener;
import android.util.*;

public class Preferences extends PreferenceActivity
{
	android.support.v7.widget.Toolbar tb;
	PreferenceScreen ps;
	PreferenceScreen ps1;
	EditTextPreference etp2;
	SharedPreferences spf;
	List<File> files;
	File current;
	Map<String,List<String>> card_data=new HashMap<String,List<String>>();
	List<String> card_data_key=new ArrayList<String>();
	SharedPreferences.Editor edit;
	Map<String, List<String>> map = new HashMap<String,List<String>>();
	List<String> parent = new ArrayList<String>();;
	String theme;
	OverallOperate app;
	Utils u;
	CheckBoxPreference cbp;
	int rgb;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		app = (OverallOperate) getApplication();
		u = app.getUtilInstance();
		
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preference);
		
		addPreferencesFromResource(R.layout.preference_content);
		initPref();
		findPreferences();
		StatusBarUtil.setColor(this,getResources().getColor(R.color.default_colorPrimary));
	}
	
	public void initPref()
	{
		spf=getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);
		edit=spf.edit();
		tb = (android.support.v7.widget.Toolbar) findViewById(R.id.preferenceToolbar1);
		tb.setTitle(R.string.main_menu_1);
		tb.setBackgroundColor(rgb);
		tb.setNavigationOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					finish();
					// TODO: Implement this method
				}
			});
		
		ps = (PreferenceScreen) findPreference("hl");
		ps1=(PreferenceScreen) findPreference("location");
		ps.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){

				@Override
				public boolean onPreferenceClick(Preference p1)
				{
					showAlert();
					// TODO: Implement this method
					return true;
				}
			});
		ps1.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){

				@Override
				public boolean onPreferenceClick(Preference p1)
				{
					showFilePick(new File(spf.getString("location",SpfConstants.getDefaultStoragePosition())));
					// TODO: Implement this method
					return true;
				}
			});
			ps1.setSummary(spf.getString("location",SpfConstants.getDefaultStoragePosition()));
		cbp = (CheckBoxPreference) findPreference("stic_noti");
		cbp.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){

				@Override
				public boolean onPreferenceChange(Preference p1, Object p2)
				{
					Snackbar.make(getWindow().getDecorView(), R.string.pref_apply, Snackbar.LENGTH_LONG)
						.setAction(R.string.ad_pb, new OnClickListener(){

							@Override
							public void onClick(View p1)
							{
								// TODO: Implement this method
							}
						}).show();
					// TODO: Implement this method
					return true;
				}
			});
	}

	@Override
	protected void onDestroy()
	{
		// TODO: Implement this method
		super.onDestroy();
		sendBroadcast(new Intent().setAction(BackGroundCatcher.RESTART_TAG));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (data != null)
		{
			Uri dat=data.getData();
			String[] path=dat.getPath().split(":");
			File f=new File(Environment.getExternalStorageDirectory() + "/" + path[1]);
			try
			{
				FileInputStream fis=new FileInputStream(f);
				InputStreamReader isr=new InputStreamReader(fis);
				BufferedReader reader=new BufferedReader(isr);
				String line;
				Set<String> set=new HashSet<String>();
				try
				{
					while ((line = reader.readLine()) != null)
					{
						set.add(line);

					}
					final SharedPreferences spf=getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);
					SharedPreferences.Editor edit=spf.edit();
					edit.putStringSet("hldb", set);
					edit.commit();
				}
				catch (IOException e)
				{}
			}
			catch (FileNotFoundException e)
			{}
		}
		// TODO: Implement this method
		super.onActivityResult(requestCode, resultCode, data);
	}
	private void chooseFile()
	{
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		try
		{
			startActivityForResult(Intent.createChooser(intent, getString(R.string.chooser_title)), 0);
		}
		catch (android.content.ActivityNotFoundException ex)
		{
			Toast.makeText(this, R.string.pref_no_file_chooser, Toast.LENGTH_SHORT).show();
		}
	}
	public void showAlert()
	{
		final SharedPreferences spf=getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);
		SharedPreferences.Editor edit=spf.edit();
		View v=getLayoutInflater().inflate(R.layout.pre_ad, null);
		ListView lv=(ListView) v.findViewById(R.id.preadListView1);
		lv.setPadding(0, 0, 0, 0);
		FloatingActionButton fab=(FloatingActionButton) v.findViewById(R.id.preadFloatingActionButton1);
		fab.setVisibility(View.GONE);
		final String[] c=new String[]{getString(R.string.highlight_1),getString(R.string.highlight_2),getString(R.string.highlight_3)};
		ListAdapter lad=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, c);
		final android.support.v7.app.AlertDialog a=new android.support.v7.app.AlertDialog.Builder(this)
			.setView(v)
			.setTitle(R.string.pref_c2_pcr_title)
			.show();
		lv.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
				{
					String c1=p1.getItemAtPosition(p3).toString();
					if (c1.equals(c[0]))
					{
						chooseFile();
						a.dismiss();
					}
					else if (c1.equals(c[1]))
					{
						File f=Environment.getExternalStorageDirectory();
						f.mkdirs();
						final File d=new File(f.getPath() + "/SavedState.sav");
						if (d.exists())
						{
							android.support.v7.app.AlertDialog a=new android.support.v7.app.AlertDialog.Builder(Preferences.this)
								.setTitle(R.string.output_file_existed)
								.setMessage(R.string.output_file_existed_msg)
								.setNegativeButton(R.string.delete_ad_nb, null)
								.setPositiveButton(R.string.delete_ad_pb, new DialogInterface.OnClickListener(){

									@Override
									public void onClick(DialogInterface p1, int p2)
									{
										try
										{
											FileOutputStream fos=new FileOutputStream(d);
											String ot="";
											Set<String> d1=spf.getStringSet("hldb", SpfConstants.getDefSet());
											for (String v:d1)
											{
												ot = ot + v + "\n";
											}
											fos.write(ot.getBytes());
											fos.close();
											Snackbar.make(getWindow().getDecorView(), getString(R.string.output_file_success) + d.getPath(), Snackbar.LENGTH_LONG).show();
										}
										catch (Exception e)
										{
											Snackbar.make(getWindow().getDecorView(), getString(R.string.output_file_failed), Snackbar.LENGTH_LONG)
												.setAction(R.string.ad_pb, new OnClickListener(){

													@Override
													public void onClick(View p1)
													{
														// TODO: Implement this method
													}
												}).show();
										}
										// TODO: Implement this method
									}
								})
								.show();
							new AlertDialogUtil().setSupportDialogColor(a, Color.parseColor("#3f51b5"));
						}
						else
						{
							try
							{
								FileOutputStream fos=new FileOutputStream(d);
								String ot="";
								Set<String> k=spf.getStringSet("hldb", SpfConstants.getDefSet());
								for (String v:k)
								{
									ot = ot + v + "\n";
								}
								fos.write(ot.getBytes());
								fos.close();
								Snackbar.make(getWindow().getDecorView(), getString(R.string.output_file_success) + d.getPath(), Snackbar.LENGTH_LONG).show();
							}
							catch (Exception e)
							{
								Snackbar.make(getWindow().getDecorView(), getString(R.string.output_file_failed), Snackbar.LENGTH_LONG)
									.setAction(R.string.ad_pb, new OnClickListener(){

										@Override
										public void onClick(View p1)
										{
											// TODO: Implement this method
										}
									}).show();
							}
						}
						a.dismiss();
					}else{
					showEditAlert();
					}
				// TODO: Implement this method
			}
	});
	lv.setAdapter(lad);

	}
	public void showEditAlert()
	{
		final List<String> data=new ArrayList<String>();
		SharedPreferences spf=getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);
		final SharedPreferences.Editor edit=spf.edit();
		final Set<String> db=spf.getStringSet("hldb", SpfConstants.getDefSet());
		for (String h:db)
		{
			data.add(h);
		}
		View v=getLayoutInflater().inflate(R.layout.pre_ad, null);
		ListView lv=(ListView) v.findViewById(R.id.preadListView1);
		FloatingActionButton fab=(FloatingActionButton) v.findViewById(R.id.preadFloatingActionButton1);
		final PrefAdapter pa=new PrefAdapter(data, this);
		lv.setAdapter(pa);
		fab.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					showAddAlert(new add(){

							@Override
							public void onInputComplete(String d)
							{
								try
								{
									Html.fromHtml(d);
									data.add(d);
									pa.notifyDataSetChanged();
									db.add(d);
									edit.putStringSet("hldb", db);
									edit.commit();
								}
								catch (Exception e)
								{
									Toast.makeText(Preferences.this, R.string.setup_3_2, Toast.LENGTH_SHORT).show();
								}

								// TODO: Implement this method
							}
						});
					// TODO: Implement this method
				}
			});

		android.support.v7.app.AlertDialog a=new android.support.v7.app.AlertDialog.Builder(this)
			.setTitle(R.string.highlight_3)
			.setView(v)
			.show();
	}
	public void showAddAlert(final add s)
	{
		View v=getLayoutInflater().inflate(R.layout.et, null);
		final EditText et=(EditText) v.findViewById(R.id.etEditText1);
		android.support.v7.app.AlertDialog a=new android.support.v7.app.AlertDialog.Builder(this)
			.setTitle(R.string.setup_4_2)
			.setView(v)
			.setNegativeButton(R.string.ad_nb, null)
			.setPositiveButton(R.string.ad_pb, new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					if (et.getText().toString() != null && et.getText().toString() != "")
					{
						s.onInputComplete(et.getText().toString());
					}
					// TODO: Implement this method
				}
			})
			.show();
		new AlertDialogUtil().setSupportDialogColor(a, Color.parseColor("#3f51b5"));
	}
	public interface add
	{
		public void onInputComplete(String d);
	}
	public void findPreferences()
	{
		StatusBarUtil.setColor(this, rgb);
		
	}
	public void updateSummary(String key, String defaulth, Preference v)
	{
		v.setSummary(u.getPreference(Utils.PreferenceType.STRING, key, defaulth));
	}
	public void showFilePick(File c){
		files=new LinkedList<File>();
		current=c;
		View root=getLayoutInflater().inflate(R.layout.file_pick,null);
		ListView lv=(ListView) root.findViewById(R.id.filepickListView1);
		Button btn_1=(Button) root.findViewById(R.id.filepickButton1);
		final Button btn_2=(Button) root.findViewById(R.id.filepickButton2);
		ImageButton ib=(ImageButton) root.findViewById(R.id.filepickImageButton1);
		final TextView txv=(TextView) root.findViewById(R.id.filepickTextView2);
		final android.support.v7.app.AlertDialog a=new android.support.v7.app.AlertDialog.Builder(this)
		.setView(root)
		.create();
		btn_1.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					a.dismiss();
					// TODO: Implement this method
				}
			});
		
			
		files=listFiles(current);
		btn_2.setText(getString(R.string.file_pick_3)+c.getName());
		txv.setText(current.getName());
		final FilePickAdapter fpa=new FilePickAdapter(files,this);
		lv.setAdapter(fpa);
		ib.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					
					if(!current.equals(Environment.getExternalStorageDirectory())){
						files.clear();
						files.addAll(listFiles(current.getParentFile()));
						fpa.notifyDataSetChanged();
						if(current.getParentFile()==Environment.getExternalStorageDirectory()){
							txv.setText(R.string.file_pick_4);
							btn_2.setText(getString(R.string.file_pick_3)+getString(R.string.file_pick_4));
						}else{
							txv.setText(current.getParentFile().getName());
							btn_2.setText(getString(R.string.file_pick_3)+current.getParentFile().getName());
						}
						current=current.getParentFile();
					}
					// TODO: Implement this method
				}
			});
		lv.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
				{
					
					File f=(File) p1.getItemAtPosition(p3);
					if(f.isDirectory()==true){
					files.clear();
					files.addAll(listFiles(f));
					fpa.notifyDataSetChanged();
					txv.setText(f.getName());
					btn_2.setText(getString(R.string.file_pick_3)+current.getParentFile().getName());
					current=f;
					}
					// TODO: Implement this method
				}
			});
		btn_2.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					
					try{
						initData(current.getPath()+"/");
						init_main_card_list(current.getPath()+"/");
					}catch(Exception e){
						Log.e("Alc","Error");
					}
					if(files.size()==0||card_data.size()!=0){
					edit.putString("location",current.getPath()+"/");
					edit.commit();
					ps1.setSummary(spf.getString("location",SpfConstants.getDefaultStoragePosition()));
					a.dismiss();
					Toast.makeText(Preferences.this,R.string.file_pick_7,Toast.LENGTH_SHORT).show();
					sendBroadcast(new Intent().setAction(MainActivity.THEME_ACTION));
					}else{
						Toast.makeText(Preferences.this,R.string.file_pick_5,Toast.LENGTH_SHORT).show();
					}
					// TODO: Implement this method
				}
			});
		a.show();
	}
	public List<File> listFiles(File f){
		List<File> tm=new LinkedList<File>();
		File[] fp=f.listFiles();
		for(File b:fp){
			tm.add(b);
		}
		Collections.sort(tm);
		return tm;
	}
	public void init_main_card_list(String e)
	{
		card_data_key.clear();
		card_data.clear();
		try{
			File dir=new File(e);
			File[] dirs=dir.listFiles();
			if (dirs != null)
			{
				for (String h:parent)
				{
					File inner=new File(dir.getPath() + "/" + h + "/");
					File[] temp2=inner.listFiles();
					if (temp2 != null)
					{
						for (File t:temp2)
						{
							String[] pkg_t=t.getName().split(" ")[2].split("\\.");
							String pkg="";
							int k=0;
							for (String n:pkg_t)
							{
								if (n.equals(pkg_t[pkg_t.length - 1]))
								{
									break;
								}
								if (k != 0)
								{
									pkg = pkg + "." + n;
								}
								else
								{
									pkg = pkg + n;
								}
								k++;
							}
							if (!card_data_key.contains(pkg))
							{
								card_data_key.add(pkg);
								List<String> temp=new ArrayList<String>();
								temp.add(t.getName());
								card_data.put(pkg, temp);
							}
							else
							{
								card_data.get(pkg).add(t.getName());
							}
						}
					}
				}
			}
		}catch(Exception r){
			Log.e("Alcn","Error");
		}
	}
	public void initData(String e)/*drawerlayout的文件列表加载*/
	{
		parent.clear();
		File dir=new File(e);
		File[] dirs=dir.listFiles();
		if (dirs != null)
		{
			for (File i:dirs)
			{
				parent.add(i.getName());
			}
			map.clear();
			for (String h:parent)
			{
				File inner=new File(dir.getPath() + "/" + h + "/");
				List<String> temp=new ArrayList<String>();
				File[] temp2=inner.listFiles();
				if (temp2 != null)
				{
					for (File t:temp2)
					{
						temp.add(t.getName());

					}
					map.put(h, temp);
				}
			}


		}
	}
}
