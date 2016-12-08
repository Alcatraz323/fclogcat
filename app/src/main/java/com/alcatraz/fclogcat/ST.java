package com.alcatraz.fclogcat;
import android.app.*;
import android.os.*;
import android.content.*;

public class ST extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		sendBroadcast(new Intent().setAction(BackGroundCatcher.SHUTDOWN_TAG));
		finish();
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
	}
	
}
