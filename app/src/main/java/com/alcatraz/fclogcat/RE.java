package com.alcatraz.fclogcat;
import android.app.*;
import android.os.*;
import android.content.*;

public class RE extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		sendBroadcast(new Intent().setAction(BackGroundCatcher.RESTART_TAG));
		finish();
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		
	}
	
}
