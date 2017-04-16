package com.alcatraz.fclogcat;
import android.view.*;
import java.util.*;

public class Windows
{
	public Windows(View view,boolean milti){
		v=view;
		multi=milti;
	}
	View v;
	boolean multi;
	Map<Integer,String> selected=new HashMap<Integer,String>();
	public void setmult(boolean vb){
		multi=vb;
	}
}
