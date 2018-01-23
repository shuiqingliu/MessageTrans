package com.jyt.clients;

import com.jyt.message.MessageConfig;
import com.jyt.message.MessageManager;
import com.jyt.message.example.MyManager;

public class ClientsManager extends MessageManager {
	public ClientsManager(String server_ip,String server_name)
	{
		super(server_ip,server_name);
		manager.register("sys_login");
		manager.register("sys_friends");
		manager.register("sys_group");
		manager.register("sys_userinfo");
		manager.register("sys_search");
		manager.register("sys_test");
	}
	
	public static void main(String[] args) {
		String server_ip = "127.0.0.1";

		MyManager m = new MyManager(server_ip, MessageConfig.server_name);
		// m.build_menu();
		
		//TODO 从数据库中读取用户id然后注册到manager
		
		while (true) {
			m.input_main();
		}

	}
	
}
