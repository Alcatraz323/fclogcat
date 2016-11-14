package com.alcatraz.fclogcat;
import android.content.*;

public class BootRec extends BroadcastReceiver
{

	@Override
	public void onReceive(Context p1, Intent p2)
	{
		p1.startService(new Intent(p1,BackGroundCatcher.class));
		// TODO: Implement this method
	}
	
}
