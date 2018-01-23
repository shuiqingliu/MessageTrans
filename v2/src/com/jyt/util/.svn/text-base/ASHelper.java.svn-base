package com.jyt.util;

import java.util.Vector;

public class ASHelper {
	public String text;
	public int type; // 0 for normal; 1 for %1 ; n for %n;
	public int mapFrom; //在对方的 字符串中的对应关系 
	public int mapTo;
	public int from;  //在自己的字符串中的开始和结束
	public int to;

	
	public String getText() {
		return text;
	}


	public void setText(String text) {
		this.text = text;
	}


	public int getType() {
		return type;
	}


	public void setType(int type) {
		this.type = type;
	}


	public int getMapFrom() {
		return mapFrom;
	}


	public void setMapFrom(int mapFrom) {
		this.mapFrom = mapFrom;
	}


	public int getMapTo() {
		return mapTo;
	}


	public void setMapTo(int mapTo) {
		this.mapTo = mapTo;
	}


	public int getFrom() {
		return from;
	}


	public void setFrom(int from) {
		this.from = from;
	}


	public int getTo() {
		return to;
	}


	public void setTo(int to) {
		this.to = to;
	}
	
	public static int[] sorti(Vector<Integer> ivec)
	{
		int[] ret = new int[ivec.size()];
		for(int i=0;i<ivec.size();i++)
		{
			int min_no = -1;
			int min_value = -1;
			for(int j=0;j<ivec.size();j++)
			{
				int v = ivec.get(j);
				if(v==-1)
					continue;
				if(min_value==-1)
				{
					min_no = j;
					min_value = v;
				}
				else{
					if(min_value>v)
					{
						min_no=j;
						min_value=v;
					}
				}
			}
			ret[i]=min_no;
			ivec.set(min_no, -1);
		}

		return ret;
	}
	
	public static String[] sort(Vector<String> vec,Vector<Integer> ivec)
	{
		int[] sx = sorti(ivec);
		String[] ret = new String[ivec.size()];
		for(int i=0;i<ret.length;i++)
		{
			ret[i] = vec.get(sx[i]);
		}
		return ret;
		
		
	}
	
	public static String[] sequence(String str)
	{
		Vector<String> vec = new Vector<String>();
		Vector<Integer> ivec = new Vector<Integer>();
		int n = 1;
		while(true)
		{
			String pattern = "%"+n;
			int index = str.indexOf(pattern,0);
			if(index!=-1)
			{
				vec.add(pattern);
				ivec.add(index);
				n++;
			}
			else{
				break;
			}
		}
		String[] ret = sort(vec,ivec);
		return ret;
	}

	public static int got_type_from_pattern(String pattern)
	{
		int ret = 0;
		String s1 = pattern.substring(1);
		ret = Integer.parseInt(s1);
		return ret;
	}
	
	public static Vector<ASHelper> scan(String str)
	{
		Vector<ASHelper> vec = new Vector<ASHelper>();
		String[] pattern_arr = sequence(str);
		boolean found = true;
		int n = 0;
		int from = 0;
		while(true)
		{
			String pattern = pattern_arr[n];
			found = false;
			while(true)
			{
				int pos = str.indexOf(pattern,from);
				if(pos==-1)
				{
					break;
				}
				else{
					found = true;
					if(pos!=from)
					{
						ASHelper as1 = new ASHelper();
						as1.setFrom(from);
						as1.setTo(pos);
						as1.setText(str.substring(from,pos));
						as1.setMapFrom(0);
						as1.setMapTo(0);
						as1.setType(0);
						vec.add(as1);
					}
					
					ASHelper as = new ASHelper();
					as.setFrom(pos);
					as.setTo(pos+pattern.length());
					as.setText(pattern);
					as.setMapFrom(0);
					as.setMapTo(0);
					int type = got_type_from_pattern(pattern);
					as.setType(type);
					vec.add(as);
					from = pos+pattern.length();
				}	
			}
			if(found==false)
			{
				break;
			}
			else{
				n++;
				if(n>pattern_arr.length-1)
					break;
			}
		}
		if(from!=str.length())
		{
			ASHelper as = new ASHelper();
			as.setFrom(from);
			as.setTo(str.length());
			as.setMapFrom(0);
			as.setMapTo(0);
			as.setText(str.substring(from,str.length()));
			as.setType(0);
			vec.add(as);
		}
		return vec;
	}
	
	public static boolean fill1(String field,Vector<ASHelper> vec)
	{
		int f1 = 0;
		int f2 = 0;
		boolean ret = true;
		for(ASHelper as : vec)
		{
			if(as.type==0)
			{
				f1 = field.indexOf(as.getText(),f1);
				if(f1==-1){
					ret = false;
					break;
				}
				as.setMapFrom(f1);
				as.setMapTo(f1+as.getText().length());
				f1 = f1+as.getText().length();
				f2 = f2+as.getText().length();
			}
			else{
				f2=f2+2;
			}
		}
		return ret;
	}
	
	public static void fill2(String field,Vector<ASHelper> vec)
	{
		int f = 0;
		int t = field.length();
		for(int i=0;i<vec.size();i++)
		{
			ASHelper as = vec.get(i);
			if(as.type!=0){
				if(i==0)
				{
					as.setMapFrom(f);
				}
				else{
					as.setMapFrom(vec.get(i-1).getMapTo());
				}
				if(i==vec.size()-1)
				{
					as.setMapTo(t);
				}
				else{
					as.setMapTo(vec.get(i+1).getMapFrom());
				}			
			}
	
		}
	}

	public String toString(){
		String ret = null;
		try{
			String s = MyObject.toString(this);
			ret = s;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return ret;
		
	}
	
	
	public static ASHelper find_as(int n,Vector<ASHelper> v)
	{
		ASHelper ret = null;
		for(ASHelper a : v)
		{
			if(a.type>0)
			{
				if(a.getType()==n)
				{
					ret = a;
					break;
				}
			}
		}
		return ret;
	}
	
	public static String[] collect(String field,Vector<ASHelper> v)
	{
		Vector<String> vec = new Vector<String>();
		int n = 1;
		while(true)
		{
			ASHelper a = find_as(n,v);
			if(a!=null)
			{
				String s = field.substring(a.getMapFrom(),a.getMapTo());
				vec.add(s);
				n++;
			}
			else{
				break;
			}
		}
		String[] ret = MyString.to_array(vec);
		return ret;
		 
	}
	
	public void print()
	{
		System.out.println(toString());
	}
	
	public static void main1(String[] args)
	{
	
		ASHelper as = new ASHelper();
		as.setFrom(10);
		as.setTo(18);
		as.setMapFrom(12);
		as.setMapTo(22);
		as.setText("hello,how are you？");
		as.setType(0);
		
		as.print();
	}
	
	
	public static String[] find(String field,String pattern)
	{
		String[] ret = null;
		Vector<ASHelper> as = ASHelper.scan(pattern);

		boolean b = ASHelper.fill1(field,as);
		if(b)
		{	
			ASHelper.fill2(pattern,as);
			ret = ASHelper.collect(field,as);
		}
		return ret;
		
	}
	
	public static void main2(String[] args)
	{
		String s = "Yes , I %2 a lot %3 of %1 !";
		String s2 = "Yes , I have a lot  of money !";
		String[] ss = find(s2,s);
		MyString.print(ss);
		
	}	
	
	public static void main(String[] args)
	{
		main2(args);
	}

}
