package com.jyt.util;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;


public class MyString {
	
	public static String to_hz(String s)
	{
		String ret = null;
		try{
			ret = new String(s.getBytes(),"gb2312");			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return ret;
	}
	
	public static boolean in(String s,String[] ss)
	{
		boolean ret = false;
		if(ss == null)
			return ret;
		for(String str : ss)
		{
			if(str.equals(s))
			{
				ret = true;
				break;
			}
		}
		return ret;
	}
	
	public static long length(String s)
	{
		long ret = 0;
		if(s!=null)
		{
			ret = s.length();
		}
		return ret;
	}
	
	public static long length(String[] ss)
	{
		long ret = 0;
		for(int i=0;i<ss.length;i++)
		{
			ret = ret + length(ss[i]);
		}
		return ret;
	}
	 
	public static String[] add(String[] arr,String e)
	{
		if(arr == null)
			return new String[]{e};
		String[] ret = new String[arr.length+1];
		for(int i=0;i<arr.length;i++)
			ret[i] = arr[i];
		ret[arr.length]=e;
		return ret;
	}
	
	public static boolean in(String[] arr,String e)
	{
		boolean ret = false;
		if(arr==null)
			return ret;
		for(String s : arr)
		{
			if(s.equals(e))
			{
				ret = true;
				break;
			}
		}
		return ret;
	}
	
	public static String[] concat(String[] arr1,String[] arr2)
	{
		List<String> list = new ArrayList<String>();
		if(arr1!=null)
		{
			for(String s : arr1)
			{
				list.add(s);
			}			
		}
		if(arr2!=null)
		{
			for(String s : arr2)
			{
				list.add(s);
			}			
		}
		return to_array(list);
	}
	
	public static String[] add_not_equal(String[] arr,String e)
	{
		
		if(in(arr,e))
		{
			return arr;
		}
		else{
			return add(arr,e);
		}
	}

	public static int find(String[] arr,String s)
	{
		int ret = -1;
		if(arr==null)
			return ret;
		if(s==null)
			return ret;
		for(int i=0;i<arr.length;i++)
		{
			if(s.equals(arr[i]))
			{
				ret = i;
				break;
			}
		}
		return ret;
	}
	
	public static String[] del(String[] arr,String str)
	{
		String[] ret = null;
		int index = find(arr,str);
		if(index==-1)
		{
		}
		else{
			ret = del(arr,index);
		}
		return ret;
	}
	
	public static String[] reverse(String[] arr)
	{
		String[] ret = null;
		if(arr!=null)
		{
			ret = new String[arr.length];
			for(int i=0;i<arr.length;i++)
			{
				ret[arr.length-i-1] = arr[i];
			}
		}
		return ret;
	}
	
	public static String[] del(String[] arr,int no)
	{
		if(arr.length==0)
			return null;
		String[] ret = new String[arr.length-1];
		for(int i=0;i<arr.length-1;i++)
		{
			if(i<no)
			{
				ret[i] = arr[i];
			}
			else if(i>=no)
			{
				ret[i] = arr[i+1];
			}
		}
		return ret;
	}
	
	public static String[] substring_arr(String[] arr,int n)
	{
		String[] ret = null;
		if(arr.length<n)
		{
			
		}
		else{
			ret = new String[arr.length-n];
			for(int i=n;i<arr.length;i++)
			{
				ret[i-n] = arr[i];
			}
		}
		return ret;
	}
	
	
	public static String[] mysplit(String field,String fg)
	{
		String[] ss = field.split(fg);
		return ss;
	}
	
	public static void print(String[] arr)
	{
		if(arr==null)
		{
			System.out.println("类MyString方法print:数组为空！");
		}
		else if(arr.length==0)
		{
			System.out.println("类MyString方法print:数组元素个数为零！");			
		}
		else{
			System.out.println("size="+arr.length);
			for(int i=0;i<arr.length;i++)
			{
				String s1 = "arr[%1]=%2 ";
				String s2 = ArgumentString.replace(s1,new String[]{i+"", arr[i]});
				System.out.println(s2);
			}			
		}

	}
	
	public static void print(String[] arr1,String[] arr2)
	{
		if(arr1.length!=arr2.length)
		{
			System.out.println("兩個數組不一樣長。");
			return;
		}
		String[] arr = new String[arr1.length];
		for(int i=0;i<arr1.length;i++)
		{
			arr[i] = arr1[i]+"("+arr2[i]+")";
		}
		print(arr);
	}
	
	public static String[] to_array(Vector<String> vector)
	{
		String[] ret = new String[vector.size()];
		for(int i=0;i<vector.size();i++)
		{
			ret[i] = (String)vector.get(i);
		}
		return ret;
	}

	public static String[] to_array(List<String> list)
	{
		String[] ret = new String[list.size()];
		for(int i=0;i<list.size();i++)
		{
			ret[i] = (String)list.get(i);
		}
		return ret;
	}
	
	public static String[] to_array(Set<String> set)
	{
		int count = 0;
		String[] ret = new String[set.size()];
		for(String str:set)
		{
			ret[count] = str;
			count++;
		}
		return ret;
	}	
	
	
	public static String to_string(String[] arr)
	{
		String ret = "";
		if(arr==null)
		{
		
		}
		else if(arr.length==0)
		{
					
		}
		else{
			for(int i=0;i<arr.length;i++)
			{
				ret = ret+arr[i]+" ";
				
			}			
		}

		return ret;
	}
	
	public static String to_string(List<String> arr)
	{
		String ret = "";
		if(arr==null)
		{
		
		}
		else if(arr.size()==0)
		{
					
		}
		else{
			for(int i=0;i<arr.size();i++)
			{
				ret = ret+arr.get(i)+" ";
				
			}			
		}

		return ret;		
	}
	
	public static String format_string_middle2(String s,int length)
	{
		String s2 = "";
		if(s.length()>length)
		{
			s2 = s.substring(0, length);
		}
		else{
			int dlt = length - s.length();
			for(int i=0;i<dlt/2;i++)
			{
				s2 = s2+" ";
			}
			s2 = s2+ s;
			for(int i=0;i<dlt/2;i++)
			{
				s2 = s2+" ";
			}
		}
		return s2;
	}
	
	public static String  format_string_middle(String s,int length)
	{
		StringBuffer sb = new StringBuffer(length);
		if(s.length()<length)
		{
			int k = length - s.length();
			int j = k/2;
			for(int i=0;i<j;i++)
				sb.append(" ");
			sb.append(s);
			for(int i=0;i<k-j;i++)
				sb.append(" ");
		}
		else{
			for(int i=0;i<length;i++)
				sb.append(s.charAt(i));
		}
		return sb.toString();
		
	}
	
	public static String format_string(String content,int length,int from,char t)
	{
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<from;i++)
		{
			sb.append(t);
		}
		for(int i=from;i<length;i++)
		{
			if(i-from<content.length())
				sb.append(content.charAt(i-from));
			else
				sb.append(t);
		}
		return sb.toString();
	}
	
	public static String  format_string_head(String s,int length)
	{
		StringBuffer sb = new StringBuffer(length);
		if(s.length()<length)
		{
			int k = length - s.length();
			int j = 0;
			for(int i=0;i<j;i++)
				sb.append(" ");
			sb.append(s);
			for(int i=0;i<k;i++)
				sb.append(" ");
		}
		else{
			for(int i=0;i<length;i++)
				sb.append(s.charAt(i));
		}
		return sb.toString();
		
	}	
	
	public static void print_format_string_middle(String s,int length)
	{
		String ss =  format_string_middle(s,length);
		System.out.print(ss);
	}

	public static void print_format_string_head(String s,int length)
	{
		String ss =  format_string_head(s,length);
		System.out.print(ss);
	}
	
	public static void print_format_string_lines(String s,int length)
	{
		int n = s.length();
		while(n>length)
		{
			String s1 = s.substring(0,length);
			String s2 = s.substring(length);
			System.out.println(s1);
			s = s2;
			n = s.length();
		}
		System.out.println(s);
	}	
	

	public static boolean begin_with(String s,String begin_pattern)
	{
		boolean ret = false;
		if(s.length()<begin_pattern.length())
		{
			return ret;
		}
		String prep = s.substring(0,begin_pattern.length());
		if(prep.equals(begin_pattern))
		{
			ret  =true;
		}
		return ret;
	}
	
	public static String toString(String[] ss)
	{
		String ret = "";
		if(ss==null)
			ret = "null";
		else{
			for(int i=0;i<ss.length;i++)
			{
				String t1 = "("+i+")";
				String t2 = ss[i];
				String t3 = " ";
				ret = ret+ t1+t2+t3;
			}
		}
		return ret;
	}
	
	public static boolean is_empty(String s)
	{
		boolean ret = false;
		if(s==null)
			ret =  true;
		else{
			s.trim();
			if(s.length()==0)
				ret =  true;
		}
		return ret;

	}
	
	public static boolean is_empty(String[] ss)
	{
		boolean ret = false;
		if(ss==null)
			ret =  true;
		else{
			if(ss.length==0)
				ret =  true;
		}
		return ret;	
	}
	
	private static int find_first_begin(String s,String[] begins)
	{
		int min = -1;
		int ret = -1;
		for(int i=0;i<begins.length;i++)
		{
			String s1 = begins[i];
			int pos = s.indexOf(s1);
			if(pos==-1)
				continue;
			if(min==-1)
			{
				min = pos;
				ret = i;
			}
			else if(pos<min){
				min = pos;
				ret = i;
			}
		}
		return ret;
	}
	
	public static String between(String s,String[] begins,String[] ends)
	{
		String ret = null;
		int k1 = find_first_begin(s,begins);
		if(k1==-1)
			return ret;
		String begin = begins[k1];
		int start = s.indexOf(begin);
		String s1 = s.substring(start+begin.length());
		int k2 = find_first_begin(s1,ends);
		if(k2==-1)
			ret=s1;
		else{
			String end = ends[k2];
			int stop = s1.indexOf(end);
			ret = s.substring(start+begin.length(),start+begin.length()+stop);
		}
		return ret;
	}
	
	public static int between_left_pos(String s,String[] begins,String[] ends)
	{
		int ret_int = -1;
		String ret = null;
		int k1 = find_first_begin(s,begins);
		if(k1==-1)
			return ret_int;
		String begin = begins[k1];
		int start = s.indexOf(begin);
		String s1 = s.substring(start+begin.length());
		int k2 = find_first_begin(s1,ends);
		if(k2==-1)
			ret_int = s.length();
		else{
			String end = ends[k2];
			int stop = s1.indexOf(end);		
			ret_int = start+stop+begin.length();
		}
		return ret_int;
	}

	public static float parse_float(String s)
	{
		float ret = 0;
		try{
			ret = Float.parseFloat(s);
		}
		catch(Exception e)
		{
			ret = -1;
		}
		return ret;
	}
	
	private static Vector<Integer> get_vector(String field,String from)
	{
		Vector<Integer> v = new Vector<Integer>();
		int fromIndex = 0;
		while(true)
		{
			int n = field.indexOf(from, fromIndex);
			if(n==-1){
				break;
			}
			else{
				v.add(new Integer(n));
				fromIndex = n + from.length();
			}
		}
		return v;
	}
	
	private static int[] get_indicator(String field,Vector<Integer> v,String from)
	{
		int[] indicator = new int[field.length()];
		for(int i=0;i<field.length();i++) indicator[i] = 0;
		int j = 0;
		for(int i=0;i<field.length();i++)
		{
			if(j>=v.size())
				break;
			if(i<v.get(j).intValue())
			{
				indicator[i] = 0;
			}
			else if(i>=v.get(j).intValue() && (i<v.get(j).intValue()+from.length())){
				if(i==v.get(j).intValue()){
					indicator[i] = 1;
				}
				else{
					indicator[i] = 2;
				}
				if(i==v.get(j).intValue()+from.length()-1)
				{
					j++;
				}
			}
		}
		return indicator;
	}
	
	private static String replace_copy(String field,String from,String to,int[] indicator)
	{
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<indicator.length;i++)
		{
			if(indicator[i] == 0)
			{
				sb.append(field.charAt(i));
			}
			else if(indicator[i] == 1)
			{
				sb.append(to);
			}
			else if(indicator[i] == 2)
			{
				
			}
		}
		return sb.toString();
	}
	public static String replace(String field,String from, String to)
	{
		Vector<Integer> v = get_vector(field,from);
		int[] indicator = get_indicator(field,v,from);
		String ret = replace_copy(field,from,to,indicator);
		return ret;
		
	}
	
	public static String combine(String[] arr,String fg)
	{
		String ret = "";
		for(int i=0;i<arr.length;i++)
		{
			if(i==arr.length-1)
			{
				ret = ret+arr[i];
			}
			else{
				ret = ret + arr[i]+fg;			
			}

		}
		return ret;
	}
	
	public static String build_string_f_g(int length,int from,int to,char f,char b)
	{
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<from;i++)
		{
			sb.append(b);
		}
		for(int i=from;i<to;i++)
		{
			sb.append(f);
		}
		for(int i=to;i<length;i++)
			sb.append(b);
		return sb.toString();
	}
	
	public static List<String> sort(List<String> list)
	{
		Collections.sort(list);
		return list;
	}
	
	public static String[] str_to_arr(String str)
	{
		Vector<String> vect = new Vector<String>();
		String no_str = "[1]";
		int pos1 = -1;
		int pos2 = -1;
		int pos3 = -1;
		int pos4 = -1;
		int no = 1;
		boolean ending = false;
		while(ending==false)
		{
			if(no==1)
			{
				pos1 = 0;
				pos2 = no_str.length()+pos1-1;
				pos3 = pos2+1;
				no++;
				no_str = "["+no+"]";
				int pos_next = str.indexOf(no_str,pos3);
				if(pos_next>0)
				{
					pos4 = pos_next-1;
				}
				else{
					pos4 = str.length()-1;
					ending = true;
				}
			}
			else{
				pos1 = pos4+1;
				pos2 = no_str.length()+pos1-1;
				pos3 = pos2+1;
				no++;
				no_str = "["+no+"]";
				int pos_next = str.indexOf(no_str,pos3);
				if(pos_next>0)
				{
					pos4 = pos_next-1;
				}
				else{
					pos4 = str.length()-1;
					ending = true;
				}
			}
			String s1 = str.substring(pos3,pos4+1);
			vect.add(s1);	
		}
		String[] ret = new String[vect.size()];
		for(int i=0;i<vect.size();i++)
		{
			ret[i] = vect.get(i);
		}
		return ret;
	}
	
	public static String arr_to_str(String[] arr)
	{
		String ret = "";
		if(arr==null)
		{
			
		}
		else{
			for(int i=0;i<arr.length;i++)
			{
				int n = i+1;
				String no_str = "["+n+"]";
				String name_str = arr[i];
				String s1 = no_str+name_str;
				ret = ret + s1;
			}
			
		}
		return ret;
	}
	
	public static String arr_to_str_ln(String[] arr)
	{
		String ret = "";
		if(arr==null)
		{
			
		}
		else{
			for(int i=0;i<arr.length;i++)
			{
				int n = i+1;
				String no_str = "["+n+"]";
				String name_str = arr[i];
				String s1 = no_str+name_str;
				ret = ret + s1 +"\r\n";
			}
			
		}
		return ret;
	}
	
	public static String arr_to_str(String[] arr1,String[] arr2)
	{
		String ret = "";
		for(int i=0;i<arr1.length;i++)
		{
			int n = i+1;
			String no_str = "["+n+"]";
			String name_str = arr1[i]+"("+arr2[i]+")";
			String s1 = no_str+name_str;
			ret = ret + s1;
		}
		return ret;
	}	
	
	
	public static String[] set_to_arr(Set<String> my_set) {
		String[] ret = new String[my_set.size()];
		Object[] os = my_set.toArray();
		int n = 0;
		for(int i=0;i<ret.length;i++)
		{
			ret[i] = (String)os[i];
		}
		return ret;
	}
	

	public static String[] intersect(String[] ss1,String[] ss2)
	{
		String[] ret = null;
		if(MyString.is_empty(ss1)||(MyString.is_empty(ss2)))
		{
			return ret;
		}
		Vector<String> vect = new Vector<String>();
		for(String s:ss1)
		{
			if(MyString.in(s, ss2))
			{
				vect.add(s);
			}
		}
		ret = MyString.to_array(vect);
		return ret;
	}
	
	public static void main1(String[] args)
	{

		String[] ss = new String[]{"hello","yzq"};
		String s = toString(ss);
		System.out.println(s);
	}
	
	public static void main2(String[] args)
	{
		String s1 = "Yes , I have a lot of money";
		print_format_string_lines(s1,10);
	}
	
	public static void main3(String[] args)
	{
		print_format_string_middle("hello , I am yzq",30);
	}
	public static void main4(String[] args)
	{
		String s1 = "getCompany";
		String s2 = "get";
		System.out.println(MyString.begin_with(s1, s2));
	}
	
	public static void main5(String[] args)
	{
		String[] begins = new String[]{"b1","我们"};
		String[] ends = new String[]{"e1","他们 "};
		String s1 = "xxx 我们hello  yyy";
		String s2 = between(s1,begins,ends);
		System.out.println(s2);
		int pos = between_left_pos(s1,begins,ends);
		System.out.println(pos);
	}
	
	public static void main6(String[] args)
	{
		String s1 = "nihao, woshiyangmin, nihaoma?";
		String s2 = replace(s1,"ni","你");
		String s3 = replace(s2,"hao","好");
		String s4 = replace(s3,"wo","我");
		String s5 = replace(s4,"shi","是");
		System.out.println(s5);
	}
	
	public static void main7(String[] args)
	{
		String[] ss = new String[]{"hello","yzq"};
		String fg = "#";
		String result = combine(ss,fg);
		System.out.println(result);
	}
	
	public static void main8(String[] args)
	{
		String s = format_string("hello",100,30,'=');
		System.out.println(s);
	}

	public static void main9(String[] args)
	{
		List<String> list = new ArrayList<String>();
		list.add("yzq");
		list.add("xjp");
		list.add("ypi");
		for(int i=0;i<list.size();i++) System.out.println(i+":"+list.get(i));
		list = sort(list);
		for(int i=0;i<list.size();i++) System.out.println(i+":"+list.get(i));
		
	}
	
	public static void main10(String[] args)
	{
		String s = "[1]ni[2]asdfasdf[3]xxx[4]yzq";
		String[] ss = str_to_arr(s);
		print(ss);
	}
	
	public static void main11(String[] args)
	{
		String[] ss = new String[]{"ni","hao","yzq"};
		String s = arr_to_str(ss);
		System.out.println(s);
	}
	
	public static void main12(String[] args)
	{
		Set<String> set = new HashSet<String>();
		set.add("yzq");
		set.add("ym");
		set.add("yzq");
		String[] ss = set_to_arr(set);
		print(ss);
	}	
	
	public static String[] get_scoped_names(String name)
	{
		String[] ret = new String[2];
		int index = name.indexOf("@");
		if(index==-1){
			ret[0] = name;
			ret[1] = null;
		}
		else{
			ret[0] = name.substring(0,index);
			ret[1] = name.substring(index+1);
		}
		return ret;
	}	

	public static void main13(String[] args)
	{
		String s = "[1]127.0.0.1[2]10001[3]20[4]3[5]FF[6]0";
		String[] res = str_to_arr(s);
		String a = res[0];
		System.out.println(a);
	}
	
	
	public static void main14(String[] args)
	{
		String s1 = "0101020000b9fc";
		String s2 = "01020b00000000000000000000004959";
		String s3 = "0104a00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000010ac";
		System.out.println(s1.length()+"_"+s2.length()+"_"+s3.length());

	}
	

	public static int get_count(String field, String s)
	{
		int ret = 0;
		String f1 = field;
		while(true)
		{
			int pos = f1.indexOf(s);
			if(pos==-1)
			{
				break;
			}
			ret++;
			f1 = f1.substring(pos+s.length());
		}
		return ret;
	}
	
	public static String erase_space(String s)
	{
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<s.length();i++)
		{
			char c = s.charAt(i);
			if(c==' ')
				continue;
			sb.append(c);
		}
		return sb.toString();
	}
	
	public static void main(String[] args)
	{
		main6(args);
	}
	
	
	

	

}
