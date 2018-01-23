package com.jyt.message.example2;

import com.jyt.message.MessageConfig;
import com.jyt.message.MessageManager;

public class PieManager extends MessageManager {
	public PieManager(String server_ip,String server_name)
	{
		super(server_ip,server_name);
		String[] names = new String[]{MessageConfig.ENTITY_MAX_MSG_LEN};
		String[] values = new String[]{"0"};
		manager.register("task");
		manager.register("computer",names,values);
	}
	
	public static void main(String[] args) {
		/*if (args.length != 2) {
			System.out.println("command server_ip server_name");
			System.exit(1);
		}
		String server_ip = args[0];
		String server_name = args[1];*/

		PieManager m = new PieManager("127.0.0.1",MessageConfig.server_name);
		// m.build_menu();
		while (true) {
			m.input_main();
		}

	}

}
