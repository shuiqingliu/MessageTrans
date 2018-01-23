package com.jyt.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyPrint { 
	
	public static String log_file = "MyPrint.log";
	
	public static String get_save_content_file_name(String file_name,int count)
	{
		String part1 = "";
		String part2 = "";
		String ret = "";
		String count_str = count+"";
		if(count_str.length()==1)
			count_str = "00"+count_str;
		else if(count_str.length()==2)
			count_str = "0"+count_str;
		int n1 = file_name.indexOf('.');
		if(n1==-1)
		{
			part1 = file_name;
			part2 = "";
		}
		else{
			part1 = file_name.substring(0,n1);
			part2 = file_name.substring(n1);
		}
		ret = part1+"_"+count_str+part2;
		return ret;
	}
	
	public static File get_proper_file(File f)
	{
		File ret = null;
		File parent = null;
		try{
			String s1 = f.getName();
			parent = f.getAbsoluteFile().getParentFile();
			int count = 1;
			while(true){
				if(count>99)
					count = 0;
				String s2 = get_save_content_file_name(s1,count);
				File f2 = new File(parent,s2);
				if(f2.exists())
				{
					count++;
				}
				else{
					ret = f2;
					int count_next = (count+1)%100;
					String s3 = get_save_content_file_name(s1,count_next);
					File f3 = new File(parent,s3);
					if(f3.exists())
					{
						f3.delete();
					}
					break;
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return ret;
	}
	
	public static void copy_file(File source,File target)
	{
		try{
			FileInputStream fis = new FileInputStream(source);
			FileOutputStream fos = new FileOutputStream(target);
			while(true)
			{
				int n = fis.read();
				if(n==-1)
				{
					break;
				}
				fos.write(n);
			}
			fis.close();
			fos.close();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void empty_file(File file)
	{
		try{
			file.delete();
			file.createNewFile();			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void log(String info,Exception e)
	{
		SimpleDateFormat sdf = new SimpleDateFormat();
		
		String filename = getLineFile(e);
		int line = getLineNumber(e);
		String time = MyDate.now2();
		String s = "####"+time+"::"+filename+"::"+line+"------"+info+"\r\n";
		try{
			File f = new File(log_file);
			if(f.exists()==false)
				f.createNewFile();
			else if(f.length()>1000*1000){
				File f2 = get_proper_file(f);
				copy_file(f,f2);
				empty_file(f);
			}
			FileOutputStream fos = new FileOutputStream(f,true);
			fos.write(s.getBytes());
			fos.close();
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
	}
	
	public static void log(String logfile,String info,Exception e)
	{
		SimpleDateFormat sdf = new SimpleDateFormat();
		
		String filename = getLineFile(e);
		int line = getLineNumber(e);
		String time = MyDate.now2();
		String s = "####"+time+"::"+filename+"::"+line+"------"+info+"\r\n";
		try{
			File f = new File(logfile);
			if(f.exists()==false)
				f.createNewFile();
			else if(f.length()>1000*1000){
				File f2 = get_proper_file(f);
				copy_file(f,f2);
				empty_file(f);
			}
			FileOutputStream fos = new FileOutputStream(f,true);
			fos.write(s.getBytes());
			fos.close();
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		
	}	
	
	public static void print(String info,Exception e)
	{		
		String filename = getLineFile(e);
		int line = getLineNumber(e);
		String time = MyDate.now2();
		String s = "####"+time+"::"+filename+"::"+line+"------"+info;
		System.out.println(s);
		
	}
	
	public static void print_simple(String info,Exception e)
	{		
		String filename = getLineFile(e);
		int line = getLineNumber(e);
		String s = "["+filename+"::"+line+"] "+info;
		System.out.println(s);
		
	}
	
	
	
	public static int getLineNumber(Exception e){
		StackTraceElement[] trace =e.getStackTrace();
		if(trace==null||trace.length==0) return -1; //
		return trace[0].getLineNumber();
	}
	
	public static String getLineFile(Exception e){
		String ret = null;
		StackTraceElement[] trace =e.getStackTrace();
		if(trace==null||trace.length==0)
		{
			
		}
		else{
			ret = trace[0].getFileName();
		}
		return ret;
	}	
	
	public static String getTime()
	{
		Date d = new Date();
		String s = d.toString();
		return s;
		
	}
	
	public static void set_log_file(String file_name)
	{
		log_file = file_name;
		try{
			File f = new File(file_name);
			if(f.exists())
			{
				FileInputStream fis = new FileInputStream(f);
				fis.close();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public static void main1(String[] args)
	{
		print("hello",new Exception());
		set_log_file("new_myprint.log");
		log("hello",new Exception());
	}
	
	public static void main2(String[] args)
	{
		File f = new File("new_myprint.log");
		set_log_file("new_myprint.log");
		log("hello",new Exception());
	//	empty_file(f);
	}

	public static void main3(String[] args)
	{
		File f = new File("new_myprint.log");
		File f2 = new File("old_myprint.log");
		set_log_file("new_myprint.log");
		log("hello",new Exception());
		copy_file(f,f2);
	
	}	
	
	public static void main4(String[] args)
	{
		File f = new File("new_myprint.log");
		set_log_file("new_myprint.log");
		log("hello",new Exception());
		File f2 = get_proper_file(f);
		
	}
	
	public static void main5(String[] args)
	{
		File f = new File("new_myprint.log");
		set_log_file("new_myprint.log");
		log("hello",new Exception());
		File f2 = get_proper_file(f);
		copy_file(f,f2);
		System.out.println(f2);
	}
	
	public static void main6(String[] args)
	{
		File f = new File("new_myprint.log");
		set_log_file("new_myprint.log");
		log("hello",new Exception());
		long l = f.length();
		System.out.println(l+"");
	}	
	
	public static void main(String[] args)
	{
//		File f = new File("new_myprint.log");
		String s = "";
		for(int i=0;i<1000;i++)
			s = s+i;
//		set_log_file("new_myprint.log");
		for(int i=0;i<100000;i++){
			CountTime ct = new CountTime("log"+i);
			log(i+"__"+s,new Exception());
			ct.print();
		}

	}		
}
