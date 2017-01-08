package com.alcatraz.fclogcat;
import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.graphics.*;
import android.net.*;
import android.os.*;
import android.support.v4.widget.*;
import android.view.*;
import android.view.View.*;
import android.view.WindowManager.*;
import android.widget.*;
import com.alcatraz.support.implutil.*;
import java.io.*;
import java.text.*;
import java.util.*;
import android.graphics.drawable.*;
import java.net.*;
import android.support.v4.content.*;

public class BackGroundCatcher extends Service
{
	public static String NO_ROOT_TAG="noroot";
	public static String SHUTDOWN_TAG="shutdown";
	public static String RESTART_TAG="restart";
	public static String DELETE="delete";
	public static String Add_fl="add_fl";
	boolean record=false;
	String file_buffer="";
	ShortcutManager mShortcutManager;
	String time_file;
	String packagen;
	Noroottagrec nrc;
	SharedPreferences spf;
	LogCat l;
	boolean packageget=false;
	int id=0;
	
	/*spf         */
	boolean boot;
	boolean clean_up;
	boolean stic_noti;
	boolean single_noti;
	@Override
	public IBinder onBind(Intent p1)
	{
		// TODO: Implement this method
		return null;
	}
	public void updatePref()
	{
		boot = spf.getBoolean("boot", true);
		stic_noti = spf.getBoolean("stic_noti", false);
		single_noti = spf.getBoolean("single_noti", false);
		clean_up=spf.getBoolean("clean_up",false);
	}
	@Override
	public void onCreate()
	{
		// TODO: Implement this method
		super.onCreate();
		//setupShortcuts();
		spf = getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);
		updatePref();
		
	}
	public Drawable getIcon(String pkg){
		PackageManager pm=getPackageManager();
		try{
			ApplicationInfo ai=pm.getApplicationInfo(pkg,PackageManager.GET_META_DATA);
			return pm.getApplicationIcon(ai);
		}catch(PackageManager.NameNotFoundException e){
			return null;
		}

	}
	
	public boolean isServiceRunning()
	{
	    ActivityManager manager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
	    for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
		{
	        if ((getPackageName() + ".FloatService").equals(service.service.getClassName()))
			{
	            return true;
	        }
	    }
	    return false;
	}
	public void setStaticNoti()
	{
		NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		Intent i=new Intent(this, MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, 0);
		Intent i2=new Intent();
		i2.setAction(SHUTDOWN_TAG);
		PendingIntent P2=PendingIntent.getBroadcast(this,0,i2,0);
		Notification.Builder nb=new Notification.Builder(this)
			.setContentIntent(pendingIntent)
			.setAutoCancel(true)
			.setVibrate(new long[]{2,2})
			.addAction(R.drawable.ic_power_grey600_24dp,getString(R.string.main_menu_4),P2)
			.setContentText(getString(R.string.service_running))
			.setSmallIcon(R.drawable.ic_alert)
			.setContentTitle(getString(R.string.app_name));
		Notification n=nb.build();
		n.flags = n.FLAG_NO_CLEAR;
		manager.notify(Integer.MAX_VALUE, n);
	}

	@Override
	public void onStart(Intent intent, int startId)
	{
		// TODO: Implement this metho
		super.onStart(intent, startId);
		
		if (intent != null && intent.getAction() != null)
		{
			if (intent.getAction().equals(BootRec.START_FROM_BOOT))
			{
				if (!boot)
				{
					stopSelf();
				}
			}
		}
		if (stic_noti)
		{
			setStaticNoti();
		}
		regist();
		l=new LogCat("logcat -v threadtime", LogCat.start_flag_require_root, getPackageCodePath());
		l.readLogCat(new LogCat.LogCatInterface(){

				@Override
				public void onUpdate(final String p1)
				{

					if (LogCatAnalyser.isCrash(p1))
					{//是否来自AndroidRuntime E等级
						if (record)
						{//记录
							file_buffer = file_buffer + p1 + "\n";
						}
						if (packageget)
						{
							packagen = LogCatAnalyser.getPackage(p1);
							packageget = false;
						}
						if (LogCatAnalyser.isCrashStart(p1))
						{//开始记录调用
							file_buffer = p1 + "\n";
							record = true;
							packageget = true;
							time_file = LogCatAnalyser.getTime(p1);
						}

					}
					else
					{
						//结束单次错误，并以日期形式输出
						record = false;
						if (file_buffer != null && time_file != null)
						{
							time_file = time_file + " " + packagen;
							notification(output(file_buffer, time_file));
							file_buffer = null;
							packageget = false;
							time_file = null;
							packagen = null;
							if(clean_up){
								try
								{
									Runtime.getRuntime().exec("su -c logcat -c");
								}
								catch (IOException e)
								{}
							}
						}
					}
					// TODO: Implement this method

					// TODO: Implement this method
				}
			}, new LogCat.RootChecker(){

				@Override
				public void onRequire(boolean p1)
				{
					if (!p1)
					{
						sendBroadcast(new Intent().setAction(NO_ROOT_TAG));
					}
					// TODO: Implement this method
				}
			});
	}
	public void notification(String dir)
	{
		if (dir != null)
		{
			if (!single_noti)
			{
				id = id + 1;
			}
			
			NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			Intent i=new Intent(this, LogViewer.class);
			i.putExtras(getNotiData(dir));
			Intent ijj=new Intent();
			ijj.putExtras(getNotiData(dir));
			ijj.setAction(FloatService.ADD_TAG);
			Intent i2=new Intent();
			i2.setAction(DELETE);
			i2.putExtra("dir",dir);
			i2.putExtra("id",id);
			File bhhd=new File(dir);
			Intent ig=new Intent(Intent.ACTION_SEND);
			ig.setType("*/*");
			if(Build.VERSION.SDK_INT>=24){
				ig.putExtra(Intent.EXTRA_STREAM,FileProvider.getUriForFile(this,"com.alcatraz.fclogcat.fileProvider",bhhd));
			}else{
			ig.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(bhhd));
			}
			PendingIntent p3=PendingIntent.getActivity(this,id,ig,PendingIntent.FLAG_UPDATE_CURRENT);
			PendingIntent p2=PendingIntent.getBroadcast(this,id,i2,PendingIntent.FLAG_UPDATE_CURRENT);
			PendingIntent p4=PendingIntent.getBroadcast(this,id,ijj,PendingIntent.FLAG_UPDATE_CURRENT);
			PendingIntent pendingIntent = PendingIntent.getActivity(this, id, i, PendingIntent.FLAG_UPDATE_CURRENT);
			Notification.Builder nb=new Notification.Builder(this)
				.setContentIntent(pendingIntent)
				.setAutoCancel(true)
				.setVibrate(new long[]{2,2})
				.setContentText(dir)
				.setSmallIcon(R.drawable.ic_alert)
				.setLargeIcon(drawableToBitmap(getIcon(getPkg(new File(dir).getName()))))
				.setContentTitle(getLabel(getPkg(new File(dir).getName())))
				.addAction(R.drawable.ic_share_variant_grey600_24dp,getString(R.string.notification_1),p3)
				.addAction(R.drawable.ic_delete_grey600_24dp,getString(R.string.notification_2),p2)
				.addAction(R.drawable.ic_flash_grey600_24dp,getString(R.string.notification_3),p4)
				.setFullScreenIntent(pendingIntent, true);
			Notification n=nb.build();
			manager.notify(id, n);
			
		}

	}
	public Bitmap drawableToBitmap(Drawable drawable) {
		try{
        Bitmap bitmap = Bitmap.createBitmap(
			drawable.getIntrinsicWidth(),
			drawable.getIntrinsicHeight(),
			drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
			: Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        //canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
		}catch(Exception e){
			Bitmap bit=BitmapFactory.decodeResource(getResources(),R.drawable.ic_alert);
			return bit;
		}
	}
	public String output(String content, String time)
	{
		/*根据日期输出，你可以写你要的输出*/
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		Date curDate = new Date(System.currentTimeMillis());
		String str = formatter.format(curDate);
		File root=new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.alcatraz.fclogcat/");
		File data_dir=new File(root.getPath() + "/" + str.split(" ")[0] + "/");
		data_dir.mkdirs();
		File data_file=new File(data_dir.getPath() + "/" + time + ".log");
		if (!data_file.exists())
		{
			try
			{
				data_file.createNewFile();
				FileOutputStream fos=new FileOutputStream(data_file);
				fos.write(content.getBytes());
				fos.close();
				sendBroadcast(new Intent(MainActivity.ACTION_TAG));
				return data_file.getPath();
			}
			catch (IOException e)
			{}
		}
		else
		{

		}
		return null;
	}
	public Bundle getNotiData(String dir)
	{
		File f=new File(dir);
		String file_name=f.getName();
		Bundle data=new Bundle();
		data.putString("path", dir);
		data.putString("pkg", getPkg(file_name));
		data.putString("label", getLabel(getPkg(file_name)));
		return data;
	}
	public String getPkg(String file_name)
	{
		String[] pkg_t=file_name.split(" ")[2].split("\\.");
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
		return pkg;
	}

	@Override
	public void onDestroy()
	{
		// TODO: Implement this method
		unregisterReceiver(nrc);
		l.kill();
		NotificationManager m=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		try
		{
			m.cancel(Integer.MAX_VALUE);
		}
		catch (Exception e)
		{}
		super.onDestroy();
	}
	public void regist()
	{
		nrc = new Noroottagrec();
		IntentFilter ifil=new IntentFilter();
		ifil.addAction(NO_ROOT_TAG);
		ifil.addAction(SHUTDOWN_TAG);
		ifil.addAction(RESTART_TAG);
		ifil.addAction(DELETE);
		registerReceiver(nrc, ifil);
	}
	public String getLabel(String pkg)
	{
		PackageManager pm=getPackageManager();
		try
		{
			ApplicationInfo ai=pm.getApplicationInfo(pkg, PackageManager.GET_META_DATA);
			return pm.getApplicationLabel(ai).toString();
		}
		catch (PackageManager.NameNotFoundException e)
		{
			return null;
		}
	}
	class Noroottagrec extends BroadcastReceiver
	{

		@Override
		public void onReceive(Context p1, Intent p2)
		{
			if (p2.getAction().endsWith(NO_ROOT_TAG))
			{
				android.app.AlertDialog a=new android.app.AlertDialog.Builder(BackGroundCatcher.this)
					.setTitle("Root")
					.setMessage(getString(R.string.setup_1_2))
					.setPositiveButton(R.string.ad_pb, new DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface p1, int p2)
						{
							stopSelf();
							android.os.Process.killProcess(android.os.Process.myPid());
							// TODO: Implement this method
						}
					})
					.create();
				a.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
				a.show();
			}
			else if (p2.getAction().equals(SHUTDOWN_TAG))
			{
				stopSelf();
			}
			else if (p2.getAction().equals(RESTART_TAG))
			{
				stopSelf();
				startService(new Intent(BackGroundCatcher.this, BackGroundCatcher.class));
			}
			else if(p2.getAction().equals(DELETE)){
				File f=new File(p2.getStringExtra("dir"));
				f.delete();
				NotificationManager nm=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
				nm.cancel(p2.getIntExtra("id",0));
			}
			// TODO: Implement this method
		}
	}
	
}
