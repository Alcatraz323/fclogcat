package com.alcatraz.fclogcat;
import android.support.v7.app.*;
import android.os.*;
import android.support.v7.widget.*;
import android.widget.*;
import java.util.*;
import android.widget.AbsListView.*;
import android.view.*;
import android.content.*;
import android.graphics.drawable.*;
import com.alcatraz.support.v4.appcompat.*;
import android.graphics.*;
import java.io.*;
import android.content.pm.*;
import android.annotation.*;
import android.app.*;
import android.support.design.widget.*;
import android.util.*;
import android.content.res.*;
import android.text.method.*;
import com.alcatraz.support.implutil.Terminal;

public class LogViewer extends ThemedActivity
{
	android.support.v7.widget.Toolbar tb;
	ListView lv;
	TextView txv;
	TextView txv2;
	TextView txv3;
	LinearLayout ll;
	ImageView imgv;
	ImageView imgv1;
	ImageView imgv2;
	String exc;
	String system;
	String cls="";
	ListViewAdapter lva;
	AppBarLayout abl;
	FloatingActionButton fab;
	List<String> keyline;
	Map<Integer,String> selected=new HashMap<Integer,String>();
	List<String> data=new ArrayList<String>();
	String pkg;
	String label;
	String path;
	Drawable icon;
	/*shared*/
	Set<String> set;
	String hb;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewer);
		initSetting();
		getData(getIntent());
		initData();
		initViews();
	}
	public void initSetting()
	{
		SharedPreferences spf=getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);
		set = spf.getStringSet("hldb", SpfConstants.getDefSet());
		hb = spf.getString("hb", "#9fa8da");
	}
	@TargetApi(Build.VERSION_CODES.KITKAT)
    private void setTranslucentStatus(boolean on, Activity act)
	{
        Window win = act.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on)
		{
            winParams.flags |= bits;
        }
		else
		{
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
	protected void Immersive(android.support.v7.widget.Toolbar mToolbar, boolean immersive, Activity activity)
	{
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
		int statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);
		int paddingTop = mToolbar.getPaddingTop();
		int paddingBottom = mToolbar.getPaddingBottom();
		int paddingLeft = mToolbar.getPaddingLeft();
		int paddingRight = mToolbar.getPaddingRight();
		ll.setPadding(ll.getPaddingLeft(), statusBarHeight, ll.getPaddingRight(), ll.getPaddingBottom());
		ViewGroup.LayoutParams params = mToolbar.getLayoutParams();
		int height = params.height;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
		{
			setTranslucentStatus(true, activity);
			if (immersive)
			{
				paddingTop += statusBarHeight;
				height += statusBarHeight;
			}
			else
			{
				paddingTop -= statusBarHeight;
				height -= statusBarHeight;
			}

            params.height = height;
            mToolbar.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);

        }
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
			case R.id.vitem:
				if(keyline.size()!=0){
				addr2line();
				}else{
					Toast.makeText(this,R.string.viewer_6,Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.vitem1:
				new File(path).delete();
				finish();
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	public boolean upgradeRootPermission(String pkgCodePath)
	{
		java.lang.Process process = null;
		DataOutputStream os = null;
		try
		{
			String cmd="chmod 777 " + pkgCodePath;
			process = Runtime.getRuntime().exec("su");
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes(cmd + "\n");
			os.writeBytes("exit\n");
			os.flush();
			process.waitFor();
		}
		catch (Exception e)
		{
			return false;
		}
		finally
		{
			try
			{
				if (os != null)
				{
					os.close();
				}
				process.destroy();
			}
			catch (Exception e)
			{
			}
		}
		try
		{
			return process.waitFor() == 0;
		}
		catch (InterruptedException e)
		{}
		return true;
	}
	private Boolean CopyAssetsFile(String filename, String des)
	{
		Boolean isSuccess = true;
		AssetManager assetManager = this.getAssets();

		InputStream in = null;
		OutputStream out = null;
		try
		{
			in = assetManager.open(filename);
			String newFileName = des + "/" + filename;
			out = new FileOutputStream(newFileName);

			byte[] buffer = new byte[1024];
			int read;
			while ((read = in.read(buffer)) != -1)
			{
				out.write(buffer, 0, read);
			}
			in.close();
			in = null;
			out.flush();
			out.close();
			out = null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			isSuccess = false;
		}

		return isSuccess;

	}
	public int dpToPx(Context context, int dp)
	{
	    DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
	    int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));       
	    return px;
	}
	public void initViews()
	{
		tb = (android.support.v7.widget.Toolbar) findViewById(R.id.mytoolbar_1);
		setSupportActionBar(tb);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		ll = (LinearLayout) findViewById(R.id.viewerLinearLayout1);
		tb.setTitle(label);
		tb.setBackgroundColor(getResources().getColor(R.color.default_colorPrimary));
		setupStaticColorPadding(getResources().getColor(R.color.default_colorPrimary));
		abl = (AppBarLayout) findViewById(R.id.appbar);
		txv = (TextView) findViewById(R.id.viewercontentTextView1);
		txv2 = (TextView) findViewById(R.id.viewercontentTextView2);
		txv3 = (TextView) findViewById(R.id.viewercontentTextView3);
		imgv1 = (ImageView) findViewById(R.id.viewercontentImageView1);
		imgv2 = (ImageView) findViewById(R.id.viewercontentImageView2);
		txv2.setText(txv2.getText().toString() + path);
		txv3.setText(txv3.getText().toString() + exc);
		txv.setText(txv.getText().toString() + cls);
		File f=new File(path);
		if (f.getName().contains("nativelog"))
		{
			imgv1.setVisibility(View.GONE);
			imgv2.setVisibility(View.GONE);
			txv3.setVisibility(View.GONE);
			txv.setVisibility(View.GONE);
		}
		lv = (ListView) findViewById(R.id.viewercontentListView1);
		lva = new ListViewAdapter(this, data, lv, pkg, set, hb, true);
		lv.setAdapter(lva);
		lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		lv.setMultiChoiceModeListener(new MultiChoiceModeListener(){

				@Override
				public boolean onCreateActionMode(ActionMode p1, Menu p2)
				{
					// TODO: Implement this method

					p1.getMenuInflater().inflate(R.menu.action_mode, p2);
					return true;
				}

				@Override
				public boolean onPrepareActionMode(ActionMode p1, Menu p2)
				{
					// TODO: Implement this method
					return true;
				}
				public void copy(String content, Context context)  
				{  

					ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);  
					cmb.setText(content.trim());  
				}  
				@Override
				public boolean onActionItemClicked(ActionMode p1, MenuItem p2)
				{
					//ActionMode动作
					switch (p2.getItemId())
					{
						case R.id.act_item1:
							if (selected != null)
							{
								String t="";
								for (int i=0;i < data.size();i++)
								{
									if (selected.containsKey(i))
									{
										t = t + selected.get(i).toString() + "\n";
									}
								}
								copy(t, LogViewer.this);
								selected.clear();
							}
							break;
						case R.id.act_item2:
							if (selected != null)
							{
								String t="";
								for (int i=0;i < data.size();i++)
								{
									if (selected.containsKey(i))
									{
										t = t + selected.get(i).toString() + "\n";
									}
								}
								Intent i=new Intent(Intent.ACTION_SEND);
								i.setType("text/plain");
								i.putExtra(Intent.EXTRA_TEXT, t);
								startActivity(i);
								selected.clear();
							}
							break;
						case R.id.act_item3:
							int i=0;
							for (String j:data)
							{
								if (!selected.containsKey(i))
								{
									selected.put(i, j);
									lv.setItemChecked(i, true);
								}
								i++;

							}
							lva.notifyDataSetChanged();
							return false;

					}
					lv.clearChoices();
					p1.finish();
					// TODO: Implement this method
					return true;
				}
				@Override
				public void onDestroyActionMode(ActionMode p1)
				{
					selected.clear();
					// TODO: Implement this method
				}

				@Override
				public void onItemCheckedStateChanged(ActionMode p1, int p2, long p3, boolean p4)
				{

					if (p4)
					{
						selected.put(p2, lv.getItemAtPosition(p2).toString());
					}
					else
					{
						selected.remove(p2);
					}
					p1.setTitle(getString(R.string.multi_1) + selected.size() + getString(R.string.multi_2));
					lva.notifyDataSetChanged();
					// TODO: Implement this method
				}
			});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater mi=new MenuInflater(this);
		mi.inflate(R.menu.viewerad, menu);
		// TODO: Implement this method
		return super.onCreateOptionsMenu(menu);


	}

	public void initData()
	{
		keyline = new ArrayList<String>();
		File f=new File(path);
		try
		{
			FileInputStream fis=new FileInputStream(f);
			if (fis != null)
			{
				InputStreamReader isr=new InputStreamReader(fis);
				BufferedReader reader=new BufferedReader(isr);
				String line;
				int i=0;
				if (!f.getName().contains("nativelog"))
				{
					boolean hasGotExc=false;
					while ((line = reader.readLine()) != null)
					{

						data.add(LogCatAnalyser.getContent(line));
						if (i >= 2 && !hasGotExc)
						{
							if (line.contains("Exception"))
							{
								exc = LogCatAnalyser.getException(line);
								hasGotExc = true;
							}
						}
						if (i >= 2)
						{
							if (line.contains("at " + pkg))
							{
								String[] pro_1=line.split(" ");
								cls = pro_1[pro_1.length - 1] + "\n";
							}
						}
						i++;
					}
				}
				else
				{
					while ((line = reader.readLine()) != null)
					{
						String x=LogCatAnalyser.getContent(line);
						data.add(x);
						x = x.trim();
						x = x.toLowerCase();
						if (x.contains(pkg.trim().toLowerCase()) && x.contains("/lib/"))
						{
							String process=LogCatAnalyser.getContent(line).trim();
							String[] process_1=process.split(" ");
							keyline.add(process_1[2] + ":" + process_1[4]);
							Log.e("Alcn",keyline.get(0));
						}

					}
				}
				fis.close();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}
	public void getData(Intent i)
	{
		Bundle data=i.getExtras();
		label = data.getString("label");
		path = data.getString("path");
		pkg = getPkgManual(path);
		icon = getIcon(pkg);
	}


	public Drawable getIcon(String pkg)
	{
		PackageManager pm=getPackageManager();
		try
		{
			ApplicationInfo ai=pm.getApplicationInfo(pkg, PackageManager.GET_META_DATA);
			return pm.getApplicationIcon(ai);
		}
		catch (PackageManager.NameNotFoundException e)
		{
			return null;
		}
	}
	public String getPkgManual(String p)
	{
		if (p != null)
		{
			String pkg1="";
			String[] pro=p.split(" ");
			String[] pkg_t=pro[2].split("\\.");

			int k=0;
			for (String n:pkg_t)
			{
				if (n.equals(pkg_t[pkg_t.length - 1]))
				{
					break;
				}
				if (k != 0)
				{
					pkg1 = pkg1 + "." + n;
				}
				else
				{
					pkg1 = pkg1 + n;
				}
				k++;
			}

			return pkg1;
		}
		return "";
	}

	@Override
	protected void onStop()
	{
		// TODO: Implement this method
		super.onStop();
		finish();
	}
	public void addr2line(){
		try
		{
			java.lang.Process pro=Runtime.getRuntime().exec("mount");
			BufferedReader br=new BufferedReader(new InputStreamReader(pro.getInputStream()));
			String line;
			while ((line = br.readLine()) != null)
			{
				if (line.contains("/system"))
				{
					system = line.split("system")[0] + "system";
					Log.e("Alcn", system);
				}
			}
			pro.destroy();
		}
		catch (IOException e)
		{}
		if (new File("/system/bin/addr2line").exists())
		{
			View ar=getLayoutInflater().inflate(R.layout.addr2line, null);
			final TextView logbox=(TextView) ar.findViewById(R.id.addr2lineTextView1);
			android.support.v7.app.AlertDialog b=new android.support.v7.app.AlertDialog.Builder(this)
				.setTitle("Addr2line")
				.setView(ar)
				.setPositiveButton(R.string.ad_pb, null)
				.create();
			new AlertDialogUtil().setSupportDialogColor(b, getColor(R.color.default_colorPrimary));
			b.show();
			for(String xmd:keyline){
				String[] te=xmd.split(":");
				logbox.append(">"+"addr2line -e -f " + te[1] + " " + te[0]+"\n");
				try
				{
					java.lang.Process pro=Runtime.getRuntime().exec("addr2line -f -e " + te[1] + " " + te[0]);
					BufferedReader br=new BufferedReader(new InputStreamReader(pro.getInputStream()));
					String line;
					while ((line = br.readLine()) != null)
					{
						logbox.append(line+"\n");
					}
					pro.destroy();
				}
				catch (IOException e)
				{}
			}
		}
		else
		{
			android.support.v7.app.AlertDialog a=new android.support.v7.app.AlertDialog.Builder(this)
				.setTitle(R.string.viewer_5)
				.setMessage(R.string.viewer_4)
				.setNegativeButton(R.string.ad_nb, null)
				.setPositiveButton(R.string.ad_pb, new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface p1, int p2)
					{
						upgradeRootPermission(getPackageCodePath());
						CopyAssetsFile("addr2line", "/sdcard/");
						java.lang.Process process = null;
						DataOutputStream os = null;

						try
						{
							String cmd="chmod 0755 " + "/system/bin/addr2line";
							process = Runtime.getRuntime().exec("su");
							os = new DataOutputStream(process.getOutputStream());
							os.writeBytes("mount -o remount,rw " + system + "/system\n");
							os.writeBytes("mv /sdcard/addr2line /system/bin\n");
							os.writeBytes("rm /sdcard/addr2line\n");
							os.writeBytes(cmd + "\n");
							os.writeBytes("exit\n");
							os.flush();
							process.waitFor();
						}
						catch (Exception e)
						{

						}


						finally
						{
							try
							{
								if (os != null)
								{
									os.close();
								}

								process.destroy();
								addr2line();
							}
							catch (Exception e)
							{
							}
						}

						// TODO: Implement this method
					}
				}).create();
			new AlertDialogUtil().setSupportDialogColor(a, getColor(R.color.default_colorPrimary));
			a.show();

		}
	}
}
