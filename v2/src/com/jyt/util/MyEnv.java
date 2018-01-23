package com.jyt.util;

import java.util.Map;
import java.util.Properties;

public class MyEnv {
	
	public static Properties system_props = System.getProperties();
	public static Map<String,String> user_map = System.getenv();
	
	public static String get(String name)
	{
		String ret = user_map.get(name);
		if(ret==null)
		{
			ret = (String)system_props.get(name);
		}
		return ret;
	}
	
	public static void print_system_env()
	{
		int count = 1;
		for(Object key:system_props.keySet())
		{
			String field = "(%1)%2=%3";
			String[] ss = new String[]{count+"",(String)key,(String)system_props.get(key)};
			String result = ArgumentString.replace(field,ss);
			System.out.println(result);
			count++;
		}
	}
	
	public static void print_user_env()
	{
		int count = 1;
		for(Object key:user_map.keySet())
		{
			String field = "(%1)%2=%3";
			String[] ss = new String[]{count+"",(String)key,(String)user_map.get(key)};
			String result = ArgumentString.replace(field,ss);
			System.out.println(result);
			count++;
		}		
	}
	
	public static String get_user_prop(String prop_name)
	{
		String ret = null;
		ret = user_map.get(prop_name);
		return ret;
	}
	
	public static String get_system_prop(String prop_name)
	{
		String ret = null;
		ret = (String)system_props.get(prop_name);
		return ret;
	}	
	
	public static void main(String[] args)
	{
		System.out.println("================================");
		System.out.println("系统环境如下：");
		print_system_env();
		System.out.println("================================");
		System.out.println("用户环境如下：");
		print_user_env();
		System.out.println("=================================");
		String jh = get_user_prop("JAVA_HOME");
		MyPrint.print(jh,new Exception());
		
	}

}
