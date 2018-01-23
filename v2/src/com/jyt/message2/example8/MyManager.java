package com.jyt.message2.example8;

import com.jyt.message2.MessageManager;

public class MyManager extends MessageManager {
	public MyManager(String server_ip,int port)
	{
		super(server_ip,port);
		manager.register("op_server");
		manager.register("op_client");
	}
	
	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("command server_ip port");
			System.exit(1);
		}
		String server_ip = args[0];
		String port_str = args[1];
		int port = Integer.parseInt(port_str);

		MyManager m = new MyManager(server_ip, port);
		// m.build_menu();
		while (true) {
			m.input_main();
		}

	}
	

}
