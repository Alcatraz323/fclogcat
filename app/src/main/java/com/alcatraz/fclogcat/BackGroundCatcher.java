package com.alcatraz.fclogcat;
import android.app.*;
import android.content.*;
import android.os.*;
import com.alcatraz.support.implutil.*;
import android.util.*;
import java.text.*;
import java.io.*;
import java.util.*;
import android.graphics.*;
import android.widget.*;
import android.content.pm.*;
import android.graphics.drawable.*;
import android.view.*;

public class BackGroundCatcher extends Service
{
	public static String NO_ROOT_TAG="noroot";
	boolean record=false;
	String file_buffer="";
	String time_file;
	String packagen;
	Noroottagrec nrc;
	boolean packageget=false;
	int id=0;
	@Override
	public IBinder onBind(Intent p1)
	{
		// TODO: Implement this method
		return null;
	}

	@Override
	public void onCreate()
	{
		// TODO: Implement this method
		super.onCreate();
		
	}

	@Override
	public void onStart(Intent intent, int startId)
	{
		// TODO: Implement this method
		super.onStart(intent,startId);
		regist();
		LogCat l=new LogCat("logcat -v threadtime",LogCat.start_flag_require_root,getPackageCodePath());
		l.readLogCat(new LogCat.LogCatInterface(){

				@Override
				public void onUpdate(final String p1)
				{

					if(LogCatAnalyser.isCrash(p1)){//是否来自AndroidRuntime E等级
						if(record){//记录
							file_buffer=file_buffer+p1+"\n";
						}
						if(packageget){
							packagen=LogCatAnalyser.getPackage(p1);
							packageget=false;
						}
						if(LogCatAnalyser.isCrashStart(p1)){//开始记录调用
							file_buffer=p1+"\n";
							record=true;
							packageget=true;
							time_file=LogCatAnalyser.getTime(p1);
						}
						
					}else{
						//结束单次错误，并以日期形式输出
						record=false;
						if(file_buffer!=null&&time_file!=null){
							time_file=time_file+" "+packagen;
							notification(output(file_buffer,time_file));
							file_buffer=null;
							packageget=false;
							time_file=null;
							packagen=null;
						}
					}
					// TODO: Implement this method

					// TODO: Implement this method
				}
			},new LogCat.RootChecker(){

				@Override
				public void onRequire(boolean p1)
				{
					if(!p1){
						sendBroadcast(new Intent().setAction(NO_ROOT_TAG));
					}
					// TODO: Implement this method
				}
			});
	}
	public void notification(String dir){
		if(dir!=null){
		NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		Intent i=new Intent(this, MainActivity.class);
		i.putExtras(getNotiData(dir));
		PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,0);
			Notification.Builder nb=new Notification.Builder(this)
			.setContentIntent(pendingIntent)
			.setAutoCancel(true)
			.setVibrate(new long[]{2,2})
			.setContentText(dir)
			.setSmallIcon(R.drawable.ic_alert)
			.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_alert))
			.setContentTitle("日志记录器")
			.setFullScreenIntent(pendingIntent,true);
			Notification n=nb.build();
			n.bigContentView=getNotiLargeView(dir,pendingIntent);
			id=id+1;
			manager.notify(id,n);
		}
	}
	public RemoteViews getNotiLargeView(String txv,PendingIntent openintent){
		RemoteViews rv=new RemoteViews(getPackageName(),R.layout.notification);
		rv.setTextViewText(R.id.notificationTextView1,txv);
		rv.setOnClickPendingIntent(R.id.notificationButton1,openintent);
		return rv;
	}
	public String output(String content,String time){
		/*根据日期输出，你可以写你要的输出*/
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss"); 
		Date curDate = new Date(System.currentTimeMillis());
		String str = formatter.format(curDate);
		File root=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Android/data/com.alcatraz.fclogcat/");
		File data_dir=new File(root.getPath()+"/"+str.split(" ")[0]+"/");
		data_dir.mkdirs();
		File data_file=new File(data_dir.getPath()+"/"+time+".log");
		if(!data_file.exists()){
			try{
				data_file.createNewFile();
				FileOutputStream fos=new FileOutputStream(data_file);
				fos.write(content.getBytes());
				fos.close();
				sendBroadcast(new Intent(MainActivity.ACTION_TAG));
				return data_file.getPath();
			}catch(IOException e){}
		}else{

		}
		return null;
	}
	public Bundle getNotiData(String dir){
		File f=new File(dir);
		String file_name=f.getName();
		Bundle data=new Bundle();
		data.putString("path",dir);
		data.putString("pkg",getPkg(file_name));
		data.putString("label",getLabel(getPkg(file_name)));
		return data;
	}
	public String getPkg(String file_name){
		String[] pkg_t=file_name.split(" ")[2].split("\\.");
		String pkg="";
		int k=0;
		for(String n:pkg_t){
			if(n.equals(pkg_t[pkg_t.length-1])){
				break;
			}
			if(k!=0){
				pkg=pkg+"."+n;
			}else{
				pkg=pkg+n;
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
		super.onDestroy();
	}
	public void regist(){
		nrc=new Noroottagrec();
		IntentFilter ifil=new IntentFilter();
		ifil.addAction(NO_ROOT_TAG);
		registerReceiver(nrc,ifil);
	}
	public String getLabel(String pkg){
		PackageManager pm=getPackageManager();
		try{
			ApplicationInfo ai=pm.getApplicationInfo(pkg,PackageManager.GET_META_DATA);
			return pm.getApplicationLabel(ai).toString();
		}catch(PackageManager.NameNotFoundException e){
			return null;
		}
	}
	class Noroottagrec extends BroadcastReceiver
	{

		@Override
		public void onReceive(Context p1, Intent p2)
		{
			android.app.AlertDialog a=new android.app.AlertDialog.Builder(BackGroundCatcher.this)
				.setTitle("无Root权限")
				.setMessage("请注意在无root状态LogCat读取无法工作")
				.setPositiveButton("明白",new DialogInterface.OnClickListener(){

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
			// TODO: Implement this method
		}
		
		
	}
}
