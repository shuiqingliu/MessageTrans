package com.jyt.message.example5;

import com.jyt.message.MessageManager;

public class AMgr extends MessageManager {
	public AMgr(String server_ip, String server_name) {
		super(server_ip, server_name);
		if(server_name.equals("abus1"))
		{
			manager.register("a1_g");
			manager.register("a1_f");	
			manager.register("gbus");
		}
		else{
			manager.register("a2_g");
			manager.register("a2_f");						
			manager.register("gbus");
		}
	}

	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("command server_ip server_name");
			System.exit(1);
		}
		String server_ip = args[0];
		String server_name = args[1];

	}

}
