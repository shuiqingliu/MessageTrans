package com.jyt.message.example1;

import com.jyt.message.MessageConfig;
import com.jyt.message.MessageManager;

public class MyManager extends MessageManager {
	public MyManager(String server_ip,String server_name)
	{
		super(server_ip,server_name);
		manager.register("client");
		manager.register("server");
	}
	
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("command server_ip server_name");
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
