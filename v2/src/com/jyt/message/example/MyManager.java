package com.jyt.message.example;

import com.jyt.message.MessageConfig;
import com.jyt.message.MessageManager;

public class MyManager extends MessageManager {
	public MyManager(String server_ip,String server_name)
	{
		super(server_ip,server_name);
		manager.register("washer");
		manager.register("cooker");
		manager.register("leader");
		manager.register("clientA");
		manager.register("clientB");
		manager.register("login");
		manager.register("sys_login");
		manager.register("sys_friends");
		manager.register("sys_group");
		manager.register("sys_userinfo");
		manager.register("sys_search");
		manager.register("sys_test");
	}
	
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("command server_ip");
			System.exit(1);
		}
		String server_ip = args[0];

		MyManager m = new MyManager(server_ip, MessageConfig.server_name);
		// m.build_menu();
		while (true) {
			m.input_main();
		}

	}
	

}
