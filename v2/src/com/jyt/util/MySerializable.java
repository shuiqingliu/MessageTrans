package com.jyt.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Method;

public class MySerializable {
	public static byte[] object_bytes(Serializable obj)
	{
		byte[] ret = null;
		try{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(obj);
			ret = baos.toByteArray();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return ret;
	}
	
	
	public static Serializable bytes_object(byte[] b)
	{
		Serializable obj = null;
		try{
			ByteArrayInputStream bais = new ByteArrayInputStream(b);
			ObjectInputStream ois = new ObjectInputStream(bais);
			obj = (Serializable)ois.readObject();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return obj;
			
	}
	
	public static String call_tostring(Object obj)
	{
		String ret = null;
		try{
			Class cls = obj.getClass();
			Method m = cls.getMethod("toString", null);
			
			ret = (String)m.invoke(obj, null);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return ret;
	}


	
	/*
	public static void main(String[] args)
	{
		class MySerial implements Serializable {
			public int a;
			public String b;
			public String[] c;
			public MySerial(int a,String b,String[] c){
				this.a = a;
				this.b = b;
				this.c = c;
			}
			public String toString()
			{
				String field = "a(%1)b(%2)c(%3)";
				String cs = MyString.to_string(c);
				String[] ss = new String[]{a+"",b,cs};
				String result = ArgumentString.replace(field, ss);
				return result;
			}
		}
		
		MySerial ms = new MySerial(20,"hello",new String[]{"yzq","ym","ypy"});
		byte[] bs = object_bytes(ms);
		MySerial ms2 = (MySerial)bytes_object(bs);
		System.out.println(ms2.toString());
		
		
	}
	*/
	

}
