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
	EditTextPreference etp;
	CheckBoxPreference cbp;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preference);
		addPreferencesFromResource(R.layout.preference_content);
		initPref();
	}
	public void initPref()
	{
		SharedPreferences spf=getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);
		tb = (android.support.v7.widget.Toolbar) findViewById(R.id.preferenceToolbar1);
		tb.setTitle(R.string.main_menu_1);
		tb.setNavigationOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					finish();
					// TODO: Implement this method
				}
			});
		StatusBarUtil.setColor(this, Color.parseColor("#3f51b5"));
		ps = (PreferenceScreen) findPreference("hl");
		ps.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){

				@Override
				public boolean onPreferenceClick(Preference p1)
				{
					showAlert();
					// TODO: Implement this method
					return true;
				}
			});
		etp = (EditTextPreference) findPreference("hb");
		etp.setSummary(spf.getString("hb", "#9FA8DA"));
		etp.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){

				@Override
				public boolean onPreferenceChange(Preference p1, Object p2)
				{
					try
					{
						Color.parseColor(etp.getEditText().getText().toString());
					}
					catch (Exception e)
					{
						Snackbar.make(getWindow().getDecorView(), R.string.setup_3_2, Snackbar.LENGTH_LONG)
							.setAction(R.string.ad_pb, new OnClickListener(){

								@Override
								public void onClick(View p1)
								{
									// TODO: Implement this method
								}
							}).show();
						return false;
					}
					etp.setSummary(etp.getEditText().getText().toString());
					// TODO: Implement this method
					return true;
				}
			});
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
}
