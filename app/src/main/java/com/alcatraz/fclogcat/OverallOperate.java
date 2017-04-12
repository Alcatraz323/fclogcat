package com.alcatraz.fclogcat;
import android.app.*;
import android.content.*;

public class OverallOperate extends Application
{
	private static Context ctx;
	private static Utils u;
	@Override
	public void onCreate()
	{
		// TODO: Implement this method
		super.onCreate();
		ctx=getApplicationContext();
		u=new Utils(ctx);
	}
	public static Utils getUtilInstance(){
		return u;
	}
	public static Context getOverallContext(){
		return ctx;
	}
}
