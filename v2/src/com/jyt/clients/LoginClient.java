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

public class LoginClient extends MessageServerTcpClient {

	public LoginClient(String server_ip, String server_name) {
		super(server_ip, server_name, "sys_login");
		addListener("login", new ResponseListener(this));
	}

	public class ResponseListener implements MessageListener {
		LoginClient client = null;

		public ResponseListener(LoginClient client) {
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
			if (type.equals("login")) {
				// TODO 通过数据库判断是否登录成功
				User user = new Gson().fromJson(content, User.class);
				if (LoginService.isLoginSuccess(user)) {
					res="{\"result\":\"success\",\"name\":\""+user.getName()+"\"}";
				} else {
					res = "{\"result\":\"fail\"}";
				}
				bs = MySerializable.object_bytes(new JsonParser().parse(res)
						.toString());
				Message msg = new Message("sys_login", from, "loginRes", bs);
				client.send(msg);
			}
		}
	}

	public static void main(String[] args) {
		LoginClient client = new LoginClient(MessageConfig.server_ip,
				MessageConfig.server_name);
		client.work();
	}
}
