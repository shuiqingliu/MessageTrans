package com.jyt.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyDate {
	
	public static String displayDate(String pattern,Date date)
	{
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		String output = formatter.format(date);
		return output;
	}
	
	public static String displayDate2(String pattern,Date date)
	{
		String output = displayDate(pattern,date);
		output = MyString.replace(output, "-0", "-");
		output = MyString.replace(output, "/0", "/");
		return output;
	}	
	
	public static String f1(Date date)
	{
		String pattern = "yyyy-MM-dd";
		String ret = displayDate(pattern,date);
		return ret;
	}
	
	public static String now1()
	{
		Date d = new Date();
		return f1(d);
	}
	
	public static String f2(Date date)
	{
		String pattern = "yyyy-MM-dd HH:mm:ss";
		String ret = displayDate(pattern,date);
		return ret;
	}
	
	public static String f3(Date date)
	{
		String pattern = "HH:mm:ss";
		String ret = displayDate(pattern,date);
		return ret;
		
	}
	
	
	public static String f4(Date date)
	{
		String pattern = "HHmm";
		String ret = displayDate(pattern,date);
		return ret;
		
	}
	public static String f5(Date date)
	{
		String pattern = "yyyyMMdd";
		String ret = displayDate(pattern,date);
		return ret;
		
	}	
	public static String now2()
	{
		Date d = new Date();
		return f2(d);
	}	
	
	public static String now3()
	{
		Date d = new Date();
		return f3(d);
	}
	
	public static Date dateFromString(String str,String pattern){
		Date ret = null;
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try{
			ret = sdf.parse(str);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return ret;	
	}
	 
	public static String format_change(String date_str,String from_format,String to_format)
	{
		Date date = dateFromString2(date_str,from_format);
		String ret = displayDate2(to_format,date);
		return  ret;
	}
	
	public static Date dateFromString2(String str){
		Date ret = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try{
			ret = sdf.parse(str);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return ret;	
	}	
	
	public static Date dateFromString2(String str,String format)
	{
		Date ret = null;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try{
			ret = sdf.parse(str);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return ret;			
	}
	
	public static void main(String[] args)
	{
		long l1 = new Date().getTime();
		int length = 100000;
		for(int i=0;i<length*length;i++)
		{
			int j = i*i+2*i+1;
		}
		long l2 = new Date().getTime();
		System.out.println(l2-l1);
	}

}
