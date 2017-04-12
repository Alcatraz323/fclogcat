package com.alcatraz.fclogcat;
import java.util.*;
import android.os.*;

public class SpfConstants
{
	public static Set<String> getDefSet(){
		Set<String> s=new HashSet<String>();
		s.add("<font color=\"#f44336\">Process</font>");
		s.add("<font color=\"#e91e63\">Caused by:</font>");
		s.add("<font color=\"#3f51b5\">$pkg$</font>");
		return s;
	}
	public static String getDefaultStoragePosition(){
		return Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.alcatraz.fclogcat/";
	}
}
