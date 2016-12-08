package com.alcatraz.fclogcat;
import android.content.*;
import android.app.*;

public class BootRec extends BroadcastReceiver
{
	public static String START_FROM_BOOT="startboot";
	@Override
	public void onReceive(Context p1, Intent p2)
	{
		if(!isServiceRunning(p1)){
		p1.startService(new Intent(p1,BackGroundCatcher.class).setAction(START_FROM_BOOT));
		}
		// TODO: Implement this method
	}
	public boolean isServiceRunning(Context c)
	{
	    ActivityManager manager = (ActivityManager)c.getSystemService(Context.ACTIVITY_SERVICE);
	    for(ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
	        if((c.getPackageName()+".BackGroundCatcher").equals(service.service.getClassName())){
	            return true;
	        }
	    }
	    return false;
	}
}
