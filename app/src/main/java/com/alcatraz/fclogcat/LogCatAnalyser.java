package com.alcatraz.fclogcat;
import android.util.*;

public class LogCatAnalyser
{
	public static String getPriority(String line)
	{
		String result=null;
		try{
			String process=line.replace(" ","");
			if(swi(process.substring(22,23))!=null){
				result=swi(process.substring(22,23));
				return result;
			}
			if(swi(process.substring(23,24))!=null){
				result=swi(process.substring(23,24));
				return result;
			}
			if(swi(process.substring(24,25))!=null){
				result=swi(process.substring(24,25));
				return result;
			}
			if(swi(process.substring(25,26))!=null){
				result=swi(process.substring(25,26));
				return result;
			}
			if(swi(process.substring(26,27))!=null){
				result=swi(process.substring(26,27));
				return result;
			}
			if(swi(process.substring(27,28))!=null){
				result=swi(process.substring(27,28));
				return result;
			}
		}catch(Exception e){

		}

		return "V";
	}
	public static String getPackage(String line){
		String process=line.replace(" ","");
		String[] process_1=process.split(":");
		String[] process_2=process_1[4].split(",");
		return process_2[0];
	}
	public static String getSource(String line, String priority)
	{
		try{
			String t=line.replace(" ","");
			String[] process=t.split(":");
			String result=process[2].substring(process[2].indexOf(priority)+1,process[2].length());
			return result;
		}catch(Exception e){

		}
		return "b";
	}
	public static boolean isCrash(String line)
	{
		try{
			if(getPriority(line).equals("E")&&getSource(line,getPriority(line)).equals("AndroidRuntime")){
				return true;
			}
		}catch(Exception e){}
		return false;
	}
	public static String swi(String a)
	{
		switch(a){
			case "E":
				return "E";
			case "D":
				return "D";
			case "I":
				return "I";
			case "V":
				return "V";
			case "W":
				return "W";
			case "F":
				return "F";
			case "S":
				return "S";
		}
		return null;
	}
	public static boolean isCrashStart(String line)
	{
		try{
			String process=line.replace(" ","");
			String[] t=process.split(":",4);
			if(t[3].indexOf("FATAL")>=0){
				return true;
			}
		}catch(Exception e){

		}
		return false;
	}
	public static String getTime(String line){
		String[] process=line.split("\\.");
		return process[0];
	}
}
