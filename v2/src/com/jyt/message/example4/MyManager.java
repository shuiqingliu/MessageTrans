package com.jyt.message.example4;

import com.jyt.message.MessageManager;

public class MyManager extends MessageManager {
	public MyManager(String server_ip,String server_name)
	{
		super(server_ip,server_name);
		manager.register("leader");
		manager.register("cooker");
		manager.register("washer"); 
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
		
		MyManager m = new MyManager(server_ip,server_name);
	}

	

	
}
