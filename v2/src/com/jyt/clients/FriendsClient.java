package com.jyt.clients;


import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.jyt.clients.model.Friend;
import com.jyt.clients.model.User;
import com.jyt.clients.service.FriendsService;
import com.jyt.message.Message;
import com.jyt.message.MessageConfig;
import com.jyt.message.MessageListener;
import com.jyt.message.MessageServerTcpClient;
import com.jyt.util.ArgumentString;
import com.jyt.util.MyDate;
import com.jyt.util.MySerializable;

public class FriendsClient extends MessageServerTcpClient {

	public FriendsClient(String server_ip, String server_name) {
		super(server_ip, server_name, "sys_friends");
		addListener("addFriConf", new ResponseListener(this));
		addListener("delFri", new ResponseListener(this));
		addListener("fetchFris", new ResponseListener(this));
	}

	public class ResponseListener implements MessageListener {
		FriendsClient client = null;

		public ResponseListener(FriendsClient client) {
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
			Friend friend=new Gson().fromJson(content,Friend.class);
			String res = "";
			byte[] bs = null;
			if (type.equals("addFriConf")) {
				// 添加好友成功后处理，返回好友信息
				res = "{\"result\":\"success\"}";
				bs = MySerializable.object_bytes(new JsonParser().parse(res)
						.toString());
				Message msg = new Message("sys_friend", from, "addFriConfRes", bs);
				client.send(msg);
				FriendsService.addFri(friend.getUid(), friend.getFid());
			} else if (type.equals("delFri")) {
				// 删除好友处理
				res = "{\"result\":\"success\"}";
				bs = MySerializable.object_bytes(new JsonParser().parse(res)
						.toString());
				Message msg = new Message("sys_friend", from, "delFriRes", bs);
				client.send(msg);
				FriendsService.delFri(friend.getUid(), friend.getFid());
			}else if (type.equals("fetchFris")) {
				// 获取好友列表
				List<User> fris=FriendsService.fetchFris(friend.getUid());
				bs = MySerializable.object_bytes(new Gson().toJson(fris));
				Message msg = new Message("sys_friend", from, "fetchFrisRes", bs);
				client.send(msg);	
			}
		}
	}

	public static void main(String[] args) {
		FriendsClient client = new FriendsClient(MessageConfig.server_ip,
				MessageConfig.server_name);
		client.register();
		client.work();
	}
}
