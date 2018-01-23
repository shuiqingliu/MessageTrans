package com.jyt.util;

import java.util.Vector;

public class ArgumentString { 
	public String message = null;
	public Vector<String> vec = new Vector<String>();
	public ArgumentString(String message,Vector<String> vec)
	{
	    this.message = message;
	    this.vec = vec;
	}
	 
	/**
	 * 提供按照参数构造一个字符串的方法。
	 * 在message中描述字符串数据的格式，并将需要填充的值按照%1,%2等方法嵌入到message中。<p>
	 * 在ss中存放有需要填充的实际内容。
	 * <pre>
	 * 	public static void main1(String[] args)
	{
		String s1 = "Yes , I %1 a lof %2";
		String[] ss = new String[]{"have","money"};
		System.out.println(ArgumentString.replace(s1, ss));
	}
	 * </pre>
	 * @param message 格式
	 * @param ss 实际参数
	 */
	
	public ArgumentString(String message,String[] ss)
	{
		for(String s:ss)
		{
			vec.add(s);
		}
		this.message = message;
	}
	
	public Vector<int[]> find(String ss,String s)
	{
		Vector<int[]> ret = new Vector<int[]>();
		int from = 0;
		while(true)
		{
			int i1 = ss.indexOf(s,from);
			if(i1==-1)
			{
				break;
			}
			else{
				int i2 = i1+s.length()-1;
				int[] ii = new int[]{i1,i2};
				ret.add(ii);
				from = i2;
			}
		}
		return ret;
	}
	
	
	public void print_arr(Vector<int[]> vs)
	{
		for(int[] ii : vs)
		{
			int i1 = ii[0];
			int i2 = ii[1];
			System.out.println("i1="+i1+" i2="+i2);
		}
	}
	
	
	
	
	public String replace(String ss,String s,Vector<int[]> vs)
	{
		int begin = 0;
		int end = 0;
		String ret = "";
		for(int[] ii : vs)
		{
			int i1 = ii[0];
			int i2 = ii[1];
			String s1 = ss.substring(begin, i1);
			ret = ret + s1 + s ;
			end = i2+1;
			begin = i2+1;

		}
		String es = ss.substring(end);
		ret = ret+es;
		return ret;
	}
	
	public void print()
	{
		System.out.println(message);
	}
	
	public void replace()
	{
		int count = 1;
		for(String pat : vec)
		{
			String arg = "%"+count;
			Vector<int[]> vi = find(message,arg);
			String nm = replace(message,pat,vi);
			message = nm;
			count++;
		}
	}
	

	public static String replace(String field,String[] ss)
	{
		ArgumentString as = new ArgumentString(field,ss);
		as.replace();
		return as.message;
	}
	
	public static String[] get(String axiom,String field)
	{
		String[] ret = ASHelper.find(field, axiom);
		return ret;
	}
	
	

	public static void main1(String[] args)
	{
		String s1 = "Yes , I %1 a lof %2";
		String[] ss = new String[]{"have","money"};
		System.out.println(ArgumentString.replace(s1, ss));
	}
	
	
	public static void main2(String[] args)
	{
		String s1 = "Yes , I %1 a lof %2";
		String s2 = "Yes , I have a lof money";
		String[] ss = ArgumentString.get(s1,s2);
		MyString.print(ss);
	}
	
	public static void main3(String[] args)
	{
		String s1 = "Yes , I %1 a lot of %2";
		String[] ss = MyString.mysplit(s1,"%1");
		MyString.print(ss);
	}
	
	public static void main(String[] args)
	{
		main2(args);
	}

}
