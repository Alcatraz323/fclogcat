package com.alcatraz.fclogcat;
import android.app.*;
import android.os.*;
import android.widget.*;
import android.content.*;

public class S extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		if(isServiceRunning()){
			Toast.makeText(this,R.string.already_start,Toast.LENGTH_SHORT).show();
		}else{
			startService(new Intent(this,BackGroundCatcher.class));
		}
		finish();
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
	}
	public boolean isServiceRunning()
	{
	    ActivityManager manager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
	    for(ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
	        if((getPackageName()+".BackGroundCatcher").equals(service.service.getClassName())){
	            return true;
	        }
	    }
	    return false;
	}
}
