package com.alcatraz.fclogcat;
import android.support.v7.app.*;
import android.os.*;
import android.content.*;
import android.util.*;
import android.support.v4.widget.*;
import android.support.v7.widget.*;
import android.view.*;
import com.alcatraz.support.v4.appcompat.*;
import android.graphics.*;
import android.app.*;
import android.support.design.widget.*;
import android.annotation.*;

public class ThemedActivity extends AppCompatActivity
{
	OverallOperate application;
	Utils util;
	public static final int NOT_OVERRIDE=1;
	int rgb;
	private String theme;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		syncPreference();
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
	}
	private void syncPreference(){
		application=(OverallOperate)getApplication();
		util=application.getUtilInstance();
		theme=(String) util.getPreference(Utils.PreferenceType.STRING,"theme","blue");
		switch(theme){
			case "blue":
				rgb=getResources().getColor(R.color.default_colorPrimary);
				setTheme(R.style.AppTheme);
				break;
			case "yellow":
				rgb=getResources().getColor(R.color.yellow_colorPrimary);
				setTheme(R.style.Yellow);
				break;
			case "pink":
				rgb=getResources().getColor(R.color.pink_colorPrimary);
				setTheme(R.style.Pink);
				break;
			case "green":
				rgb=getResources().getColor(R.color.green_colorPrimary);
				setTheme(R.style.Green);
				break;
			case "addedblue":
				rgb=getResources().getColor(R.color.addedblue_colorPrimary);
				setTheme(R.style.AddBlue);
				break;
			case "umr":
				rgb=getResources().getColor(R.color.umr_colorPrimary);
				setTheme(R.style.UMR);
				break;
			case "night":
				rgb = getResources().getColor(R.color.nightmode_colorPrimary);
				setTheme(R.style.NightMode);
				break;
			case "cus":
				rgb=Color.parseColor((String)(util.getPreference(Utils.PreferenceType.STRING,"custom_rgb","#123456")));
				break;
		}
	}
	
	public void setupMaterialWithDrawer(DrawerLayout dl,android.support.v7.widget.Toolbar tb,View top) {
		new DrawerLayoutUtil().setImmersiveToolbarWithDrawer(tb,dl,this,top,"#3f51b5",Build.VERSION.SDK_INT);
		top.setBackgroundColor(rgb);
		tb.setBackgroundColor(rgb);
	}
	public void setupDefaultMaterial(android.support.v7.widget.Toolbar tb){
		Immersive(tb,true,this);
	}
	public void setupStaticColorPadding(int color){
		StatusBarUtil.setColor(this,color);
	}
	protected void Immersive(android.support.v7.widget.Toolbar mToolbar, boolean immersive, Activity activity)
	{
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
		int statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);
		int paddingTop = mToolbar.getPaddingTop();
		int paddingBottom = mToolbar.getPaddingBottom();
		int paddingLeft = mToolbar.getPaddingLeft();
		int paddingRight = mToolbar.getPaddingRight();

		ViewGroup.LayoutParams params = mToolbar.getLayoutParams();
		int height = params.height;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
		{
			setTranslucentStatus(true, activity);
			if (immersive)
			{
				paddingTop += statusBarHeight;
				height += statusBarHeight;
			}
			else
			{
				paddingTop -= statusBarHeight;
				height -= statusBarHeight;
			}

            params.height = height;
            mToolbar.setPadding(paddingLeft, 0, paddingRight, paddingBottom);

        }
    }
	@TargetApi(Build.VERSION_CODES.KITKAT)
    private void setTranslucentStatus(boolean on, Activity act)
	{
        Window win = act.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on)
		{
            winParams.flags |= bits;
        }
		else
		{
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}
