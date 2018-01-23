package com.jyt.util;

import java.util.HashMap;
import java.util.Map;

public class MyError {
	public static Map<String,String> Error = new HashMap<String,String>();
	
	public static void put_error_message(String userid,String code,String message)
	{
		String err_formula = "Time(%1)No(%2)User(%3)Code(%4)Message(%5)";
		String time = MyDate.now2();
		String serial_no = UniqueStringGenerator.getUniqueString();
		String user = userid;

		String[] ss = new String[]{time,serial_no,userid,code,message};
		String error_message = ArgumentString.replace(err_formula,ss);
		Error.put(userid, error_message);
	}
	
	// type is one of Time,No,User,Code,Message
	public static String get_error_info(String error_message,String type)
	{
		String ret = "";
		if(type.equals("Time"))
		{
			int a1 = error_message.indexOf("Time(");
			int a2 = error_message.indexOf(")No(",a1);
			ret = error_message.substring(a1+"Time(".length(),a2);
		}
		else if(type.equals("No"))
		{
			int a1 = error_message.indexOf(")No(");
			int a2 = error_message.indexOf(")User(",a1);
			ret = error_message.substring(a1+")No(".length(),a2);
		}
		else if(type.equals("User"))
		{
			int a1 = error_message.indexOf(")User(");
			int a2 = error_message.indexOf(")Code(",a1);
			ret = error_message.substring(a1+")User(".length(),a2);
		}
		else if(type.equals("Code"))
		{
			int a1 = error_message.indexOf(")Code(");
			int a2 = error_message.indexOf(")Message(",a1);
			ret = error_message.substring(a1+")Code(".length(),a2);
		}
		else if(type.equals("Message"))
		{
			int a1 = error_message.indexOf(")Message(");
			int a2 = error_message.indexOf(")",a1+1);
			MyPrint.print("a1="+a1+" a2="+a2,new Exception());
			ret = error_message.substring(a1+")Message(".length(),a2);
		}
		return ret;
	}
	
	public static String get_error_message(String userid)
	{
		String ret =  Error.get(userid);
		return ret;
	}
	
	public static void main(String[] args)
	{
		for(int i = 0;i<1;i++)
		{
			MyError.put_error_message("yzq", "01", "doesnot have money!");
			String ret = MyError.get_error_message("yzq");
			System.out.println(ret);
			String time = MyError.get_error_info(ret, "Time");
			String code = MyError.get_error_info(ret, "Code");
			String message = MyError.get_error_info(ret, "Message");
			String user = MyError.get_error_info(ret, "User");
			String no = MyError.get_error_info(ret, "No");
			System.out.println(message);
		}
	}

}
