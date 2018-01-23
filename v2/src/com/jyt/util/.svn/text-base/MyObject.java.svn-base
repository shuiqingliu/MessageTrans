package com.jyt.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;



public class MyObject {
	
	private static String wipeoff_get(String m_name)
	{
		String ret = m_name;
		if(MyString.begin_with(m_name, "get"))
		{
			ret = m_name.substring(3);
			String s1 = ret.charAt(0)+"";
			String s2 = ret.substring(1);
			s1 = s1.toLowerCase();
			ret = s1+s2;
			
		}
		return ret;
	}
	
	public static String toString(Object obj) throws Exception
	{
		Class myClass = obj.getClass();
		Class[] parameterTypes = new Class[]{String.class};
		Field[] fields = myClass.getDeclaredFields();
		Method[] methods = myClass.getDeclaredMethods();
		String ret = "";
		String item = "";
		int count = 1;
		for(Method m : methods)
		{
			String m_name = m.getName();
			//MyPrint.print(m_name,new Exception());
			if(MyString.begin_with(m_name,"get"))
			{
				String name = wipeoff_get(m_name);
				String v = "";
				Object[] os = null;
				Object value = m.invoke(obj, os);
				
				if(value==null){
					v = "null";
				}
				else if(value.getClass()==Integer.class)
				{
					v = value+"";
				}
				else if(value.getClass()==Float.class)
				{
					v = value+"";
				}
				else if(value.getClass()==Double.class)
				{
					v = value+"";
				}
				else if(value.getClass()==String.class)
				{
					v = (String)value;
				}
				else if(value.getClass()==String[].class)
				{
					String[] vs = (String[])value;
					v = MyString.toString(vs);
				}
				String count_str = count+"";
				String s1 = "[%3]:%1(%2)\r\n";
				String[] ss = new String[]{name,v,count_str};
				item = ArgumentString.replace(s1, ss);				
				count++;
				ret = ret + item;
			}
			

			
		}
		return ret;
	}
	
	/**
	 * 得到一个对象的某个函数的参数类型。整数为i,字符串为s,用#分割
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static String returnTypeIndicators(Class myClass,String function_name) throws Exception
	{
		String ret = "";
		Method[] methods = myClass.getDeclaredMethods();
		for(Method m:methods)
		{
			String ms = m.getName();
			if(ms.equals(function_name))
			{
				Class[] clses = m.getParameterTypes();
				for(Class t:clses)
				{
					
					String ts = "u";
					if(t==java.lang.String.class)
					{
						ts = "s";
					}
					else if(t==java.lang.Integer.class)
					{
						ts = "i";
					}
					else if(t==int.class)
					{
						ts = "i";
					}
					else{
						System.out.println(t.getName());
					}
					if(ret.equals(""))
					{
						ret = ret+ts;
					}
					else{
						ret = ret+"#"+ts;						
					}
				}
				break;
			}
		}
		return ret;
	}
	
	public static String toStringNoLine(Object obj) throws Exception
	{
		Class myClass = obj.getClass();
		Class[] parameterTypes = new Class[]{String.class};
		Field[] fields = myClass.getDeclaredFields();
		Method[] methods = myClass.getDeclaredMethods();
		String ret = "";
		String item = "";
		int count = 1;
		for(Method m : methods)
		{
			String m_name = m.getName();
			if(MyString.begin_with(m_name,"get"))
			{
				String name = wipeoff_get(m_name);
				String v = "";
				Object[] os = null;
				Object value = m.invoke(obj, os);
				
				if(value==null){
					v = "null";
				}
				else if(value.getClass()==Boolean.class)
				{
					v = value+"";
				}
				else if(value.getClass()==Integer.class)
				{
					v = value+"";
				}
				else if(value.getClass()==Float.class)
				{
					v = value+"";
				}
				else if(value.getClass()==Double.class)
				{
					v = value+"";
				}
				else if(value.getClass()==String.class)
				{
					v = (String)value;
				}
				else if(value.getClass()==String[].class)
				{
					String[] vs = (String[])value;
					v = MyString.toString(vs);
				}
				String count_str = count+"";
				String s1 = "[%3]:%1(%2)";
				String[] ss = new String[]{name,v,count_str};
				item = ArgumentString.replace(s1, ss);				
				count++;
				ret = ret + item;
			}
		}
		return ret;
	}
	
	public static String toStringNoLine2(Object obj) 
	{
		String ret = null;
		try{
			ret = toStringNoLine(obj);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return ret;
	}	
	
	
	public static String toStringNoLine(Object obj,Map<String,Integer> format_map) throws Exception
	{
		Class myClass = obj.getClass();
		Class[] parameterTypes = new Class[]{String.class};
		Field[] fields = myClass.getDeclaredFields();
		Method[] methods = myClass.getDeclaredMethods();
		String ret = "";
		String item = "";
		int count = 1;
		for(Method m : methods)
		{
			String m_name = m.getName();
			if(MyString.begin_with(m_name,"get"))
			{
				String name = wipeoff_get(m_name);
				String v = "";
				Object[] os = null;
				Object value = m.invoke(obj, os);
				
				if(value==null){
					v = "null";
				}
				else if(value.getClass()==Integer.class)
				{
					v = value+"";
				}
				else if(value.getClass()==Float.class)
				{
					v = value+"";
				}
				else if(value.getClass()==Double.class)
				{
					v = value+"";
				}
				else if(value.getClass()==String.class)
				{
					v = (String)value;
				}
				else if(value.getClass()==String[].class)
				{
					String[] vs = (String[])value;
					v = MyString.toString(vs);
				}
				String count_str = count+"";
				String s1 = "[%3]:%1(%2)";
				int length = v.length();
				if(format_map.get(name)!=null)
				{
					length = format_map.get(name).intValue();
				}
				String print_v = MyString.format_string_head(v, length);
				String[] ss = new String[]{name,print_v,count_str};
				item = ArgumentString.replace(s1, ss);				
				count++;
				ret = ret + item;
			}
		}
		return ret;
	}
		
	
	
	public static String getObjectAttributeClassName(Object obj,String attrName)
	{
		String ret = "";
		Class cls = obj.getClass();
		try{
			Field f = cls.getField(attrName);
			ret = f.getType().getName();
		}
		catch(NoSuchFieldException e)
		{
			System.out.println(attrName +" not exist!");
		}
		return ret;
	}
	
	public static Object shift(Object src,String srctype,String targettype)
	{
		Object ret = src;
		if(!srctype.equals(targettype))
		{
			if(srctype.equals("java.lang.String"))
			{
				if(targettype.equals("java.util.Date"))
				{
					String ds = (String)src;
					java.util.Date date = MyDate.dateFromString(ds, "yyyy-MM-dd");
					ret = (Object)date;
				}
			}

		}
		return ret;
	}
	
	public static void copyProperties(Object dest,Object src) throws Exception
	{
		Map srcmap = BeanUtils.describe(src); 
		for(Object key:srcmap.keySet())
		{
			String attrName = (String)key;
			if(attrName.equals("class"))
				continue;
			String attrdesttype = getObjectAttributeClassName(dest,attrName);
			String attrsrctype = getObjectAttributeClassName(src,attrName);
		//	MyPrint.print("attrName="+attrName+" attrdest="+attrdesttype+" attrsrc="+attrsrctype,new Exception());
			Object srcattr = BeanUtils.getProperty(src, attrName);
			Object destattr = shift(srcattr,attrsrctype,attrdesttype);
			BeanUtils.setProperty(dest, attrName, destattr );

		}
		
	}
	
	public static void main(String[] args)
	{
		/*
		try{
			String s = MyObject.returnTypeIndicators(IndexLib.class,"add");
			System.out.println(s); 
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		*/
	}
}
