package com.jyt.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MyExec {
	
	public static String exe_dos_cmd(String dos_cmd)
	{
		String cmd = "cmd /c "+dos_cmd;
		String[] result = exe(cmd);
		String ret = "";
		for(int i=0;i<result.length;i++)
		{
			ret = ret  + result[i];
			if(i!=result.length-1)
			{
				ret = ret+"\n";
			}
		}
		return ret;
	}
	
	public static String replace_splash(String str)
	{
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<str.length();i++)
		{
			char c = str.charAt(i);
			if(c=='\\')
				c = '/';
			sb.append(c);
		}
		return sb.toString();
	}
	
	public static String exe_java_cmd(String java_cmd)
	{
		String cmd_field = "cmd /c %1/bin/%2";
		String java_home = MyEnv.get_user_prop("java_home");
		java_home = replace_splash(java_home);
		String[] ss = new String[]{java_home,java_cmd};
		String cmd = ArgumentString.replace(cmd_field, ss);
		String[] result = exe(cmd);
		String ret = "";
		for(int i=0;i<result.length;i++)
		{
			ret = ret  + result[i];
			if(i!=result.length-1)
			{
				ret = ret+"\r\n";
			}
		}
		return ret;
	}	
	
	public static String[] exe(String cmd)
	{
		String[] ret = null;
		try {
			String ls_1;
			Process process = Runtime.getRuntime().exec(cmd);
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(process.getInputStream()));
			while ((ls_1 = bufferedReader.readLine()) != null)
			{
				ret = MyString.add(ret, ls_1);
			}
			process.waitFor();

		} catch (Exception e) {
			e.printStackTrace();
		}	
		return ret;
	}
	
	public static void main2(String[] args) {
		String cmd = "cmd /c D:/software/develop/jdk1.6.0_02/bin/jps -m";
		String[] result = exe(cmd);
		MyString.print(result);
	}
	
	public static void main3(String[] args) {
		String cmd = "cmd /c D:/software/develop/jdk1.6.0_02/bin/jps";
		String[] result = exe(cmd);
		MyString.print(result);
	}	

	public static void main(String[] args) {
		String cmd = "jps -m";
		String result = exe_java_cmd(cmd);
		MyPrint.print(result,new Exception());
	}	
}
