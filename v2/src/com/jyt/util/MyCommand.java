package com.jyt.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

public class MyCommand {

	public static String work_dir = "E:/tenant2/bat/message/example5";
	public static Map<String, Process> process_map = new HashMap<String, Process>();

	public static String[] cmd_string_arr;
	private static Properties env_props = new Properties();
	
	public static List<MyComandStruct> cmd_list = new ArrayList<MyComandStruct>();

	private static String[] trim(String[] arr) {
		Vector<String> v = new Vector<String>();
		for (int i = 0; i < arr.length; i++) {
			String s = arr[i].trim();
			if (s.length() != 0) {
				v.add(s);
			}
		}
		String[] ret = new String[v.size()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = v.get(i);
		}
		return ret;
	}

	public static void init_from_cmd_file(String init_cmd_file)
	{
		File f = new File(init_cmd_file);
		if(f.exists())
		{
			try{
				cmd_string_arr = MyFile.readfile_arr(f);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		else{
			MyPrint.print("文件不存在，退出",new Exception());
			System.exit(0);
		}
	}
	
	public static void init_from_env_file(String init_env_file)
	{
		File f = new File(init_env_file);
		if(f.exists())
		{
			try{
				env_props.load(new FileInputStream(f));
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		else{
			MyPrint.print("文件不存在，退出",new Exception());
			System.exit(0);
		}
		
	}
	
	public static void init_cmd_list() {

		for (int i = 0; i < cmd_string_arr.length; i++) {
			String cmd = cmd_string_arr[i];
			MyPrint.print(cmd, new Exception());
			String[] cmd_args = cmd.split(" ");
			cmd_args = trim(cmd_args);
			if(cmd_args.length<2)
			{
				continue;
			}
			String cmd_name = cmd_args[0];
			if (cmd_name.equals("pause")) {
				MyComandStruct command = new MyComandStruct();
				command.command_name = "pause";
				command.args = new String[] { cmd_args[1] };
				cmd_list.add(command);
			} else if (cmd_name.startsWith("#")) {

			} else {
				MyComandStruct command = new MyComandStruct();
				command.command_name = cmd_args[0];
				command.args = new String[cmd_args.length - 1];
				for (int j = 0; j < command.args.length; j++) {
					command.args[j] = cmd_args[j + 1];
				}
				cmd_list.add(command);
			}

		}
	}

	public static void print_cmd_list() {
		for (int i = 0; i < cmd_list.size(); i++) {
			System.out.println(cmd_list.get(i).toString());
		}
	}

	public static String run_cmd_get_response(MyComandStruct command) throws Exception {
		String ret ="";
		List<String> list = new ArrayList<String>();
		for (String s : command.args) {
			list.add(s);
		}
		ProcessBuilder pb = new ProcessBuilder(list);
		Map<String, String> env = pb.environment();
		for(Object key:env_props.keySet())
		{
			String key_str = (String)key;
			String value = env_props.getProperty(key_str);
			env.put(key_str, value);
		}
		pb.directory(new File(work_dir));
		try{
			Process p = pb.start();
			InputStream is = p.getInputStream();
			OutputStream os = p.getOutputStream();
			byte[] bs  = new byte[]{};
			while(true)
			{
				int m = is.available();
				if(m==0)
				{
					break;
				}
				else{
					int n = is.read();
					bs = BinEdit.append(bs,new byte[]{(byte)n});
				}
				
			}
			ret = new String(bs);
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return ret;
	}
	
	public static void exe_one_cmd(MyComandStruct command) throws Exception {

		List<String> list = new ArrayList<String>();
		for (String s : command.args) {
			list.add(s);
		}
		ProcessBuilder pb = new ProcessBuilder(list);
		Map<String, String> env = pb.environment();
		for(Object key:env_props.keySet())
		{
			String key_str = (String)key;
			String value = env_props.getProperty(key_str);
			env.put(key_str, value);
		}
		pb.directory(new File(work_dir));
		try{
			Process p = pb.start();
			process_map.put(command.command_name, p);
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}

	}

	public static void exe_cmd_list() {
		try {

			for (int i = 0; i < cmd_list.size(); i++) {
				MyComandStruct command = cmd_list.get(i);
				String display_info = ArgumentString.replace("(%1)%2:%3",new String[]{i+"",command.command_name,MyDate.now2()});
				MyPrint.print(display_info,new Exception());
				if (command.command_name.equals("pause")) {
					int l = 5;
					if (command.args != null) {
						l = Integer.parseInt(command.args[0]);
						Thread.sleep(l * 1000);
					}
				} else
					exe_one_cmd(command);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void display_cmd_status() {

		MyCommandFrame f = new MyCommandFrame(process_map);
	}

	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("command cmd_list env_list");
			System.exit(1);
		}
		String cmd_list = args[0];
		String env_list = args[1];
		
		init_from_cmd_file(cmd_list);
		init_from_env_file(env_list);
		init_cmd_list();
		print_cmd_list();
		exe_cmd_list();
		display_cmd_status();

	}

}
