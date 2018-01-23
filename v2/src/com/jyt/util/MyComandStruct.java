package com.jyt.util;


public class MyComandStruct {

		String command_name;
		String[] args;
		
		public MyComandStruct(){};
		
		public String toString()
		{
			String ret_f = "Cmd(%1)Args(%2)";
			String[] ss = new String[]{command_name,MyString.toString(args)};
			String ret = ArgumentString.replace(ret_f,ss);
			return ret;
		}
	
}
