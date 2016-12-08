package com.alcatraz.fclogcat;
import de.robv.android.xposed.callbacks.XC_LoadPackage.*;
import de.robv.android.xposed.*;
import de.robv.android.xposed.callbacks.*;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.callMethod;
import static de.robv.android.xposed.XposedHelpers.callStaticMethod;
import static de.robv.android.xposed.XposedHelpers.findAndHookConstructor;
import static de.robv.android.xposed.XposedHelpers.findClass;
import static de.robv.android.xposed.XposedHelpers.findFirstFieldByExactType;
import static de.robv.android.xposed.XposedHelpers.getObjectField;
import static de.robv.android.xposed.XposedHelpers.newInstance;
import android.widget.*;
import android.util.*;
import android.graphics.*;
import android.app.*;
import android.content.*;
import de.robv.android.xposed.XC_MethodHook.*;
public class XposedImpl implements IXposedHookLoadPackage
{
	public static String TARGET_PKG="com.ryuunoakaihitomi.ForceCloseLogcat";
	Context c;
	@Override
	public void handleLoadPackage(XC_LoadPackage.LoadPackageParam p1) throws Throwable
	{
		
		// TODO: Implement this method
	}

}
