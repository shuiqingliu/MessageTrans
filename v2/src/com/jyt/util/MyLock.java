package com.jyt.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class MyLock {
	public static Map<String,String[]> resource_operator_xlock_map = new HashMap<String,String[]>();
	public static Map<String,String[]> resource_operator_slock_map = new HashMap<String,String[]>();

	public static boolean is_empty(String[] arr)
	{
		boolean ret = false;
		if(arr==null)
			ret = true;
		else if(arr.length==0)
			ret = true;
		return ret;
	}
	
	public static  void xlock(String operator,String resource)
	{
	//	MyPrint.print("====>"+operator+" XLock "+resource, new Exception());
		boolean ending = false;
		while(!ending)
		{
			String[] operator_arr_x;
			String[] operator_arr_s;
			synchronized(resource_operator_xlock_map){
				synchronized(resource_operator_slock_map){
					operator_arr_x = resource_operator_xlock_map.get(resource);
					operator_arr_s = resource_operator_slock_map.get(resource);
					if(is_empty(operator_arr_x)&&is_empty(operator_arr_s))
					{
						resource_operator_xlock_map.put(resource, new String[]{operator});
						ending = true;
					}
					else if(is_empty(operator_arr_x))
					{
						if(operator_arr_s[0].equals(operator))
						{
							String[] new_operator_arr_s = delete_item(operator_arr_s,operator);
							resource_operator_slock_map.put(resource,new_operator_arr_s);
							resource_operator_xlock_map.put(resource, new String[]{operator});
							ending = true;
							
						}
					}
					else if(is_empty(operator_arr_s))
					{
						if(operator_arr_x[0].equals(operator))
						{
							ending = true;
						}
					}
				}
			}

			if(ending==false)
			{
				try{
					Thread.sleep(100);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
	
		}

	//	MyPrint.print("<====="+operator+" XLock "+resource, new Exception());
	}
	
	public static String[] delete_item(String[] ss,String s)
	{
		String[] new_arr = ss;
		if(!is_empty(ss))
		{
			Vector<String> v = new Vector<String>();
			for(String s1:ss)
			{
				if(s1.equals(s))
				{
					continue;
				}
				else{
					v.add(s1);
				}
			}
			Object[] oarr = v.toArray();
			new_arr = new String[oarr.length];
			for(int i=0;i<oarr.length;i++)
			{
				new_arr[i] = (String)oarr[i];
			}		
		}
		return new_arr;
	}
	
	public static  void xunlock(String operator,String resource)
	{
	//	MyPrint.print("====>"+operator+" XunLock "+resource, new Exception());
		String[] operator_arr_x;
		String[] operator_arr_s;
		synchronized(resource_operator_xlock_map){
			synchronized(resource_operator_slock_map){
				operator_arr_x = resource_operator_xlock_map.get(resource);
				operator_arr_s = resource_operator_slock_map.get(resource);
				String[] new_arr1 = delete_item(operator_arr_x,operator);
				resource_operator_xlock_map.put(resource, new_arr1);
				String[] new_arr2 = delete_item(operator_arr_s,operator);
				resource_operator_slock_map.put(resource, new_arr2);				
			}
		}

	//	MyPrint.print("<====="+operator+" XunLock "+resource, new Exception());
		
	}

	public static  void slock(String operator,String resource)
	{
	//	MyPrint.print("====>"+operator+" sLock "+resource, new Exception());
		boolean ending = false;
		while(!ending)
		{
			String[] operator_arr_x;
			String[] operator_arr_s;
			synchronized(resource_operator_xlock_map){
				synchronized(resource_operator_slock_map){
					operator_arr_s = resource_operator_slock_map.get(resource);
					operator_arr_x = resource_operator_xlock_map.get(resource);
					if(is_empty(operator_arr_x))
					{
						String[] new_operator_arr_s = MyString.add(operator_arr_s, operator);
						resource_operator_slock_map.put(resource, new_operator_arr_s);
						ending = true;
					}				
				}	
			}
			if(!ending){
				try{
					Thread.sleep(100);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		
		}
	
		
	//	MyPrint.print("<====="+operator+" sLock "+resource, new Exception());
	}
	
	public static  void sunlock(String operator,String resource)
	{
	//	MyPrint.print("====>"+operator+" sunLock "+resource, new Exception());
		String[] operator_arr_s;
		synchronized(resource_operator_slock_map){
			operator_arr_s = resource_operator_slock_map.get(resource);
			String[] new_arr = delete_item(operator_arr_s,operator);
			resource_operator_slock_map.put(resource, new_arr);		
		}

	//	MyPrint.print("<====="+operator+" sunLock "+resource, new Exception());
		
	}	
	
	public static synchronized void print()
	{
		for(String key : resource_operator_xlock_map.keySet())
		{
			String s = "%1:(xlock)%2";
			String s1 = MyString.to_string(resource_operator_xlock_map.get(key));
			String[] ss = new String[]{key,s1};
			String s3 = ArgumentString.replace(s, ss);
			MyPrint.print(s3, new Exception());
		}
		for(String key : resource_operator_slock_map.keySet())
		{
			String s = "%1:(slock)%2";
			String s1 = MyString.to_string(resource_operator_slock_map.get(key));
			String[] ss = new String[]{key,s1};
			String s3 = ArgumentString.replace(s, ss);
			MyPrint.print(s3, new Exception());
		}		
	}
	
	public static void main(String[] args)
	{
		MyLock.xlock("yzq", "r1");
		MyLock.slock("yzq", "r2");
		MyLock.slock("ym", "r2");
		MyLock.print();
		MyLock.xunlock("yzq", "r1");
		MyLock.sunlock("ym", "r2");
		MyLock.print();
	}
	
}
