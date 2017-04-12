package com.alcatraz.fclogcat;
import android.content.*;
import java.util.*;
import android.util.*;
import android.graphics.*;

public class Utils
{
	SharedPreferences spf;
	SharedPreferences.Editor edit;
	Context ctx;
	public Utils(Context c){
		ctx=c;
		spf=c.getSharedPreferences(c.getPackageName()+"_preferences",c.MODE_PRIVATE);
		edit=spf.edit();
	}
	public void savePreference(PreferenceType t,String key,Object content){
		if(t==PreferenceType.STRING){
			edit.putString(key,(String)content);
		}else if(t==PreferenceType.INTEGER){
			edit.putInt(key,(Integer)content);
		}else if(t==PreferenceType.BOOLEAN){
			edit.putBoolean(key,(Boolean)content);
		}else if(t==PreferenceType.FLOAT){
			edit.putFloat(key,(Float)content);
		}else if(t==PreferenceType.LONG){
			edit.putLong(key,(Long)content);
		}else if(t==PreferenceType.STRINGSET){
			edit.putStringSet(key,(Set<String>)content);
		}
		edit.commit();
	}
	public Object getPreference(PreferenceType t,String key,Object defv){
		Object parameter = null;
		if(t==PreferenceType.STRING){
			parameter=spf.getString(key,(String)defv);
		}else if(t==PreferenceType.INTEGER){
			parameter=spf.getInt(key,(Integer)defv);
		}else if(t==PreferenceType.BOOLEAN){
			parameter=spf.getBoolean(key,(Boolean)defv);
		}else if(t==PreferenceType.FLOAT){
			parameter=spf.getFloat(key,(Float)defv);
		}else if(t==PreferenceType.LONG){
			parameter=spf.getLong(key,(Long)defv);
		}else if(t==PreferenceType.STRINGSET){
			parameter=spf.getStringSet(key,(Set<String>)defv);
		}
		
		return parameter;
	}
	
	public String convertToARGB(int color) {
        // alpha = Integer.toHexString(Color.alpha(color));
		String red = Color.red(color)+"";
        String green = Color.green(color)+"";
        String blue = Color.blue(color)+"";
        // System.out.println(Color.red(color))
        return "#" + red + green + blue;
    }
	
	public enum PreferenceType{
		STRING,STRINGSET,INTEGER,LONG,BOOLEAN,FLOAT
	}
}
