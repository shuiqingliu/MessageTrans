package com.jyt.clients;

import java.util.List;

import com.jyt.clients.service.UserInfoService;
import com.jyt.message.MessageConfig;
import com.jyt.message.MessageManager;

public class ClientsManager extends MessageManager {
	public ClientsManager(String server_ip, String server_name) {
		super(server_ip, server_name);
		manager.register("sys_login");
		manager.register("sys_friend");
		manager.register("sys_group");
		manager.register("sys_userinfo");
		manager.register("sys_search");
		manager.register("sys_test");
		manager.register("sys_check");
		//web组新添加的两个模块
		manager.register("sys_groups");
		manager.register("sys_friends");

		// ע�������û�
		List<String> uids = UserInfoService.getAllUsers();
		for (String uid : uids) {
			manager.register(uid);
		}
	}

	public static void main(String[] args) {
		String server_ip = "127.0.0.1";

		ClientsManager m = new ClientsManager(server_ip, MessageConfig.server_name);
		// m.build_menu();

		while (true) {
			m.input_main();
		}

	}

}
