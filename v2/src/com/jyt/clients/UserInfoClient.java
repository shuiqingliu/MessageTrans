package com.jyt.clients;

import com.google.gson.Gson;
import com.jyt.clients.model.User;
import com.jyt.clients.service.UserInfoService;
import com.jyt.message.Message;
import com.jyt.message.MessageConfig;
import com.jyt.message.MessageListener;
import com.jyt.message.MessageServerTcpClient;
import com.jyt.util.ArgumentString;
import com.jyt.util.MyDate;
import com.jyt.util.MySerializable;

public class UserInfoClient extends MessageServerTcpClient{
	
	public UserInfoClient(String server_ip, String server_name) {
		super(server_ip, server_name,"sys_userinfo");
		addListener("fetchUserInfo",new ResponseListener(this));
		addListener("modifyUserInfo",new ResponseListener(this));
	}

	public class ResponseListener implements MessageListener
	{
		UserInfoClient client = null;

		public ResponseListener(UserInfoClient client) {
			this.client = client;
		}
		public void messagePerformed(Message message)
		{
			String field = "**************\n%1 from %2 \n%3\n**************";
			String type=message.getType();
			String from = message.getFrom();
			String time_str = MyDate.f2(message.getCreated());
			String content = (String)MySerializable.bytes_object(message.getContent());
			String[] ss = new String[] { time_str, from, content };
			String result = ArgumentString.replace(field, ss);
			System.out.println(result);
			String res = "";
			byte[] bs = null;
			User user = new Gson().fromJson(content, User.class);
			if(type.equals("fetchUserInfo")){
				// 获取用户信息
				user=UserInfoService.fetchUserInfo(user.getId());
				bs = MySerializable.object_bytes(new Gson().toJson(user));
				Message msg = new Message("sys_userinfo",from,"fetchUserInfoRes",bs);
				client.send(msg);
			}else if(type.equals("modifyUserInfo")){
				// 修改用户信息
				UserInfoService.modifyUserInfo(user);
			}
		}
	}
	
	public static void main(String[] args) {
		UserInfoClient client=new UserInfoClient(MessageConfig.server_ip,MessageConfig.server_name);
		client.work();
	}
}
