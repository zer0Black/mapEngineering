package com.example.mapengineering.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Environment;

public class constants {

	public static final String UID="UNION_ID";
	public static final String IDCODER="UNION_IDCODER";
	
	//判断文本中是否包含字母
	public static Boolean containLetter(String str){
		Pattern pattern =Pattern.compile("[a-zA-Z]");
	    Matcher matcher = pattern.matcher(str);
	    if (matcher.find()) {
	    	// 如匹配成功即走到这里
	    	return true;
	    }else{
	    	return false;
	    }
	}
	
    public static String clearSpaceAndLine(String value){
    	String result = value.replaceAll("\\s+", "");
    	return result;
    }
    
	public static String getSDPath(){ 
	       String sdDir = ""; 
	       boolean sdCardExist = Environment.getExternalStorageState()   
	                           .equals(Environment.MEDIA_MOUNTED);   //判断sd卡是否存在 
	       if   (sdCardExist)   
	       {                               
	         sdDir = Environment.getExternalStorageDirectory().getPath();//获取跟目录 
	         System.out.println("sd卡路径="+sdDir);
	      }   
	       return sdDir; 
	}
}
