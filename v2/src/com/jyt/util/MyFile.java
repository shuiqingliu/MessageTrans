package com.jyt.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MyFile {
	
	   public static String readline(FileInputStream fis) {
	        String ret = null;

	        try {
	            StringBuffer sb = new StringBuffer();

	            while (true) {
	                int ch = fis.read();

	                if (ch == -1) {
	                    ret = null;
	                    break;
	                } 
	                else if (ch == '\r') {
	                	continue;
	                } 
	                else if (ch == '\n') {
	                    ret = new String(sb.toString().getBytes("ISO-8859-1"),"GBK");
	                    break;
	                } 
	                else {
	                    sb.append((char) ch);
	                }
						
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return ret;
	    }
	   
	   public static String readfile(File f) {
		    String ret = "";
	        try {
	            FileInputStream fis = new FileInputStream(f);
	            int lineno = 1;

	            while (true) {
	                String line = readline(fis);

	                if (line == null) {
	                    break;
	                }
	                ret = ret + line +"\n";
	                lineno++;
	            }
	            fis.close();
	        } catch (Exception e) {
	        	String fname = f.getAbsolutePath();
	            System.out.println("文件["+fname+"]打开错误！");
	        }
	        return ret;
	    }
	   
	   public static String[] readfile_arr(File f) {
		    Vector<String> v = new Vector<String>();
	        try {
	            FileInputStream fis = new FileInputStream(f);

	            while (true) {
	                String line = readline(fis);

	                if (line == null) {
	                    break;
	                }
	                v.add(line);
	            }
	            fis.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        String[] ret = MyString.to_array(v);
	        return ret;
	    }	   
	   
	   
	   public static byte[] readfile2(File f) {

		    Vector<Integer> vi = new Vector<Integer>();
	        try {
	            FileInputStream fis = new FileInputStream(f);
	            
	            while (true) {
	            	int a = fis.read();
	            	if(a==-1)
	            	{
	            		break;
	            	}
	            	else{
	            		vi.add(a);
	            	}
	            }
	            fis.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        byte[] ret = new byte[vi.size()];
	        for(int i=0;i<ret.length;i++)
	        {
	        	ret[i] = vi.get(i).byteValue();
	        }
	        return ret;
	    }   
	   
	public static List<String> list(String dir)
	{
		List<String> ret = new ArrayList<String>();
		File f = new File(dir);
		File[] fs = f.listFiles();
		for(File one:fs)
		{
			ret.add(one.toString());
		}		
		return ret;
	}
	
	public static void save(String s,File f)
	{
		try{
			FileOutputStream fos = new FileOutputStream(f,true);
			fos.write(s.getBytes());
			fos.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static String readfile3(File file)
	{
		String ret = "";
		int buffer_size = 1024*1024;
		byte[] buffer = new byte[buffer_size];
		byte[] result = null;
		try
		{
			int size = (int)file.length();
			int left = size;
			FileInputStream fis = new FileInputStream(file);
			while(true)
			{
				if(left==0)
					break;
				int n = fis.read(buffer, 0,left);
				if(n==-1)
				{
					break;
				}
				else{
					byte[] bs = new byte[n];
					for(int i=0;i<n;i++)
						bs[i] = buffer[i];
					result = BinEdit.append(result,bs);
					left = left - n;
				}
				
			}
			ret = new String(result);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return ret;
	}
	
	public static void append(String s,FileOutputStream fos)
	{
		try{
			fos.write(s.getBytes());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public static void main3(String[] args)
	{
		
		List<String> list = list("D:\\new_xczq\\RemoteSZ");
		for(String s:list)
		{
			System.out.println(s);
		}
		
	}
	public static void main2(String[] args)
	{
		String s = readfile(new File("e:/tmp/log/info.txt"));
		System.out.println(s);
		
		
	}
	
	public static void main1(String[] args)
	{
		try{
			FileInputStream fis = new FileInputStream(new File("e:/pa/v1/data/gome.html"));
			String s = readline(fis);
			System.out.println(s);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{
		try{

			String s = readfile3(new File("e:/pa/v1/data/dangdang.html"));
			System.out.println(s);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}	
}
