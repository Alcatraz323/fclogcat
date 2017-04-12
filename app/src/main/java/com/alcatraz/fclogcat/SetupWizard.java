package com.alcatraz.fclogcat;
import android.graphics.*;
import android.os.*;
import android.support.v7.app.*;
import android.view.*;
import android.widget.*;
import com.alcatraz.support.v4.appcompat.*;
import java.util.*;
import android.view.View.*;
import android.widget.RadioGroup.*;
import android.content.*;
import android.support.design.widget.*;
import java.io.*;
import android.net.*;
import android.text.*;
import android.support.v4.view.*;
import android.app.*;
import android.annotation.*;
import android.content.pm.*;
import android.provider.*;
import android.support.v4.content.*;
import android.support.v4.app.*;
import android.*;

public class SetupWizard extends AppCompatActivity
{
	public final static int FLAG_NEW=0;
	public final static int FLAG_NO_PERM=1;
	public final static int FLAG_UPDATE_LOG=2;
	public final static int FLAG_NO_PERM_WITH_UPD_LOG=3;
	ViewPagerMin vpm;
	TextView txv;
	AppBarLayout abl;
	List<View> views;
	LinearLayout ll;
	ViewPagerAdapter vpa;
	SharedPreferences spf;
	Button btn_p1;
	Button btn_p2;
	SharedPreferences.Editor edit;
	int OVERLAY=0;
	int STORAGE=1;
	int flag;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		
		if (isFirstGo())
		{
			startActivity(new Intent(this, MainActivity.class));
			finish();
		}
		initBasicSettings();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup);
		initViews();
		initViewPager();
	}
	@TargetApi(23)
	public static void checkPerm(Activity mContext)
	{
		if (Build.VERSION.SDK_INT >= 23)
		{
            int checkPermission =ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (checkPermission != PackageManager.PERMISSION_GRANTED)
			{
				ActivityCompat.requestPermissions(mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                return;
            }
        }
	}
	@TargetApi(23)
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
	{
		// TODO: Implement this method
		refButtonState();
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	@TargetApi(23)
    public void requestDrawOverLays()
	{
        if (Settings.canDrawOverlays(this))
		{
            return;
        }
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent,666);
    }
	public void initViews()
	{
		abl = (AppBarLayout) findViewById(R.id.appbar);
		Immersive(abl, true, this);
		vpm = (ViewPagerMin) findViewById(R.id.setupViewPagerMin1);
		txv = (TextView) findViewById(R.id.setupTextView1);
		ll = (LinearLayout) findViewById(R.id.setupLinearLayout1);
	}
	public boolean isFirstGo()
	{
		spf = getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);
		edit = spf.edit();
		if (!spf.getBoolean("first_go", false))
		{
			return false;
		}
		return true;
	}
	
	public void initBasicSettings()
	{
		
		edit.putBoolean("stic_noti", false);
		edit.putBoolean("single_noti", false);
		edit.putBoolean("ce", false);
		edit.putBoolean("clean_up", true);
		edit.putBoolean("boot", true);
		edit.putString("location",SpfConstants.getDefaultStoragePosition());
		edit.putString("hb", "#9fa8da");
		edit.putStringSet("hldb", SpfConstants.getDefSet());
	}
	protected void Immersive(AppBarLayout mToolbar, boolean immersive, Activity activity)
	{
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
		int statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);
		int paddingTop = mToolbar.getPaddingTop();
		int paddingBottom = mToolbar.getPaddingBottom();
		int paddingLeft = mToolbar.getPaddingLeft();
		int paddingRight = mToolbar.getPaddingRight();

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
            mToolbar.setPadding(paddingLeft, 0, paddingRight, paddingBottom);

        }
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
	public void initViewPager()
	{
		views = new ArrayList<View>();
		View v1=getLayoutInflater().inflate(R.layout.setup_1, null);
		View v2=getLayoutInflater().inflate(R.layout.setup_2, null);
		View v3=getLayoutInflater().inflate(R.layout.setup_3, null);
		View v4=getLayoutInflater().inflate(R.layout.setup_4, null);
		View v5=getLayoutInflater().inflate(R.layout.setup_permission, null);
		View v6=getLayoutInflater().inflate(R.layout.setup_update, null);
		views.add(v1);
		views.add(v2);
		views.add(v3);
		views.add(v4);
		views.add(v5);
		views.add(v6);
		setUPPage_1();
		setUPPage_2();
		setUPPage_3();
		setUPPage_4();
		setUPPage_5();
		setUPPage_6();
		vpa = new ViewPagerAdapter(views);
		vpm.setAdapter(vpa);
		vpm.getViewPager().setOnPageChangeListener(new ViewPager.OnPageChangeListener(){

				@Override
				public void onPageScrolled(int p1, float p2, int p3)
				{
					// TODO: Implement this method
				}

				@Override
				public void onPageSelected(int p1)
				{
					switch (p1)
					{
						case 0:
							AnimateUtil.textChange(txv, getString(R.string.setup_title_1));
							break;
						case 1:
							AnimateUtil.textChange(txv, getString(R.string.setup_title_2));
							break;
						case 2:
							AnimateUtil.textChange(txv, getString(R.string.setup_title_3));
							break;
						case 3:
							AnimateUtil.textChange(txv, getString(R.string.setup_title_4));
							break;
						case 4:
							AnimateUtil.textChange(txv, getString(R.string.setup_title_5));
							break;
						case 5:
							AnimateUtil.textChange(txv, getString(R.string.setup_title_6));
							break;
					}
					// TODO: Implement this method
				}

				@Override
				public void onPageScrollStateChanged(int p1)
				{
					// TODO: Implement this method
				}
			});
	}

	@Override
	protected void onResume()
	{
		// TODO: Implement this method
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
		{
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		super.onResume();
	}


	public void setUPPage_1()
	{
		View v=views.get(0);
		Button btn_1=(Button) v.findViewById(R.id.setupnavButton1);
		Button btn_2=(Button) v.findViewById(R.id.setupnavButton2);
		btn_1.setVisibility(View.GONE);
		btn_2.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					vpm.getViewPager().setCurrentItem(1);
					// TODO: Implement this method
				}
			});
	}
	public void setUPPage_2()
	{
		View v=views.get(1);
		CheckBox cb=(CheckBox) v.findViewById(R.id.setup2CheckBox1);
		CheckBox cb2=(CheckBox) v.findViewById(R.id.setup2CheckBox2);
		CheckBox cb3=(CheckBox) v.findViewById(R.id.setup2CheckBox3);
		Button btn_1=(Button) v.findViewById(R.id.setupnavButton1);
		Button btn_2=(Button) v.findViewById(R.id.setupnavButton2);
		btn_1.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					vpm.getViewPager().setCurrentItem(0);
					// TODO: Implement this method
				}
			});
		btn_2.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					vpm.getViewPager().setCurrentItem(2);
					// TODO: Implement this method
				}
			});
		cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

				@Override
				public void onCheckedChanged(CompoundButton p1, boolean p2)
				{
					edit.putBoolean("stic_noti", p2);
					edit.commit();
					// TODO: Implement this method
				}
			});
		cb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

				@Override
				public void onCheckedChanged(CompoundButton p1, boolean p2)
				{
					edit.putBoolean("boot", p2);
					edit.commit();
					// TODO: Implement this method
				}
			});
		cb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

				@Override
				public void onCheckedChanged(CompoundButton p1, boolean p2)
				{
					edit.putBoolean("single_noti", p2);
					edit.commit();
					// TODO: Implement this method
				}
			});
	}
	public void setUPPage_3()
	{
		View v=views.get(2);
		final TextInputLayout til_2=(TextInputLayout) v.findViewById(R.id.textInputLayout2);
		til_2.setErrorEnabled(true);
		til_2.getEditText().setText(spf.getString("hb", "#9fa8da"));
		Button btn_1=(Button) v.findViewById(R.id.setupnavButton1);
		Button btn_2=(Button) v.findViewById(R.id.setupnavButton2);
		btn_1.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					vpm.getViewPager().setCurrentItem(1);
					// TODO: Implement this method
				}
			});
		btn_2.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					boolean flag_2=true;
					try
					{
						Color.parseColor(til_2.getEditText().getText().toString());
					}
					catch (Exception e)
					{
						til_2.setError(getString(R.string.setup_3_2));
						flag_2 = false;
					}
					if (flag_2)
					{
						edit.putString("hb", til_2.getEditText().getText().toString());
						vpm.getViewPager().setCurrentItem(3);
					}
					// TODO: Implement this method
				}
			});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(requestCode==666){
			refButtonState();
		}
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
					setUPPage_4();
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
	public void setUPPage_4()
	{
		View v=views.get(3);
		final List<String> data=new ArrayList<String>();
		Button btn_1=(Button) v.findViewById(R.id.setupnavButton1);
		Button btn_2=(Button) v.findViewById(R.id.setupnavButton2);
		FloatingActionButton fab=(FloatingActionButton) v.findViewById(R.id.setup4FloatingActionButton1);
		ListView lv=(ListView) v.findViewById(R.id.setup4ListView1);
		final Set<String> n=spf.getStringSet("hldb", SpfConstants.getDefSet());
		for (String f:n)
		{
			data.add(f);
		}
		final PrefAdapter pa=new PrefAdapter(data, this);
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
									n.add(d);
									edit.putStringSet("hldb", n);
									edit.commit();
								}
								catch (Exception e)
								{
									Toast.makeText(SetupWizard.this, R.string.setup_3_2, Toast.LENGTH_SHORT).show();
								}
								// TODO: Implement this method
							}
						});
					// TODO: Implement this method
				}
			});
		lv.setAdapter(pa);
		Button btn_3=(Button) v.findViewById(R.id.setup4Button1);
		btn_3.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					chooseFile();
					// TODO: Implement this method
				}
			});
		btn_1.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					vpm.getViewPager().setCurrentItem(2);
					// TODO: Implement this method
				}
			});
		btn_2.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					vpm.getViewPager().setCurrentItem(4);
					// TODO: Implement this method
				}
			});
	}
	public void setUPPage_5()
	{
		View v=views.get(4);
		btn_p1 = (Button) v.findViewById(R.id.setuppermissionButton1);
		btn_p2 = (Button) v.findViewById(R.id.setuppermissionButton2);
		Button btn_1=(Button) v.findViewById(R.id.setupnavButton1);
		Button btn_2=(Button) v.findViewById(R.id.setupnavButton2);
		if(Build.VERSION.SDK_INT>=23){
			refButtonState();
		}
		if (Build.VERSION.SDK_INT < 23)
		{
			btn_p1.setEnabled(false);
			btn_p1.setText(R.string.perm_granted);
			btn_p2.setEnabled(false);
			btn_p2.setText(R.string.perm_granted);
		}

		btn_p1.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					if (Build.VERSION.SDK_INT >= 23)
					{
						requestDrawOverLays();
					}
					// TODO: Implement this method
				}
			});
		btn_p2.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					checkPerm(SetupWizard.this);
					// TODO: Implement this method
				}
			});
		btn_1.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					vpm.getViewPager().setCurrentItem(3);
					// TODO: Implement this method
				}
			});
		btn_2.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					if (Build.VERSION.SDK_INT >= 23)
					{
						refButtonState();
						if (!btn_p1.isEnabled() && !btn_p2.isEnabled())
						{
							vpm.getViewPager().setCurrentItem(5);
						}
					}
					else
					{
						vpm.getViewPager().setCurrentItem(5);
					}

					// TODO: Implement this method
				}
			});
	}
	@TargetApi(23)
	public void refButtonState()
	{
		if (Settings.canDrawOverlays(this))
		{
			btn_p1.setEnabled(false);
			btn_p1.setText(R.string.perm_granted);
		}
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
		{
			btn_p2.setEnabled(false);
			btn_p2.setText(R.string.perm_granted);
		}

	}
	public void setUPPage_6()
	{
		View v=views.get(5);
		Button btn_1=(Button) v.findViewById(R.id.setupnavButton1);
		Button btn_2=(Button) v.findViewById(R.id.setupnavButton2);
		btn_1.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					vpm.getViewPager().setCurrentItem(4);
					// TODO: Implement this method
				}
			});
		btn_2.setText(R.string.setup_step_next_final);
		btn_2.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					edit.putBoolean("Upd_1.6.0",true);
					edit.putBoolean("first_go", true);
					edit.commit();
					startActivity(new Intent(SetupWizard.this, MainActivity.class));
					finish();
					// TODO: Implement this method
				}
			});
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
