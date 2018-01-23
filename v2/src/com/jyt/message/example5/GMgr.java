package com.jyt.message.example5;

import com.jyt.message.MessageManager;

public class GMgr extends MessageManager {
	public GMgr(String server_ip,String server_name)
	{
		super(server_ip,server_name);
		manager.register("g_g");
		manager.register("g_f");
		manager.register("abus1");
		manager.register("abus2");
	}	
	
	public static void main(String[] args)
	{
		if(args.length!=2)
		{
			System.out.println("command server_ip server_name");
			System.exit(1);
		}		
		String server_ip = args[0];
		String server_name = args[1];				
	}	

}
