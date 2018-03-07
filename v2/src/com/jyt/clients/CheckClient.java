package com.jyt.clients;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.jyt.clients.model.User;
import com.jyt.clients.service.LoginService;
import com.jyt.message.Message;
import com.jyt.message.MessageConfig;
import com.jyt.message.MessageListener;
import com.jyt.message.MessageServerTcpClient;
import com.jyt.util.ArgumentString;
import com.jyt.util.MyDate;
import com.jyt.util.MySerializable;

public class CheckClient extends MessageServerTcpClient {

	public CheckClient(String server_ip, String server_name) {
		super(server_ip, server_name, "sys_check");
		addListener("check", new ResponseListener(this));
	}

	public class ResponseListener implements MessageListener {
		CheckClient client = null;

		public ResponseListener(CheckClient client) {
			this.client = client;
		}

		public void messagePerformed(Message message) {
			String field = "**************\n%1 from %2 \n%3\n**************";
			String type = message.getType();
			String from = message.getFrom();
			String time_str = MyDate.f2(message.getCreated());
			String content = (String) MySerializable.bytes_object(message
					.getContent());
			String[] ss = new String[] { time_str, from, content };
			String result = ArgumentString.replace(field, ss);
			System.out.println(result);
			String res = "";
			byte[] bs = null;
			if (type.equals("check")) {
				res = "{\"result\":\"success\"}";
				bs = MySerializable.object_bytes(new JsonParser().parse(res)
						.toString());
				Message msg = new Message("sys_check", from, "msg", bs);
				client.send(msg);
				System.out.println(msg);
			}
		}
	}

	public static void main(String[] args) {
		CheckClient client = new CheckClient(MessageConfig.server_ip,
				MessageConfig.server_name);
		client.work();
	}
}
