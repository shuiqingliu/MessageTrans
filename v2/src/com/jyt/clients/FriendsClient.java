package com.jyt.clients;

import com.jyt.clients.service.FriendsService;
import com.jyt.message.Message;
import com.jyt.message.MessageConfig;
import com.jyt.message.MessageListener;
import com.jyt.message.MessageServerTcpClient;
import com.jyt.util.ArgumentString;
import com.jyt.util.MyDate;
import com.jyt.util.MySerializable;

public class FriendsClient extends MessageServerTcpClient{
	
	public FriendsClient(String server_ip, String server_name) {
		super(server_ip, server_name,"sys_friends");
		addListener("addFriConf",new ResponseListener(this));
		addListener("delFri",new ResponseListener(this));
	}

	public class ResponseListener implements MessageListener
	{
		FriendsClient client = null;

		public ResponseListener(FriendsClient client) {
			this.client = client;
		}
		public void messagePerformed(Message message)
		{
			String field = "**************\n%1 from %2 \n%3\n**************";
			String type=message.getType();
			String from = message.getFrom();
			String time_str = MyDate.f2(message.getCreated());
			String content = (String)MySerializable.bytes_object(message.getContent());
			String uid="";
			String fid="";
			String[] ss = new String[] { time_str, from, content };
			String result = ArgumentString.replace(field, ss);
			System.out.println(result);
			if(type.equals("addFriConf")){
				//TODO 添加好友成功后处理，返回好友信息
				FriendsService.addFri(uid, fid);
			}else if(type.equals("delFri")){
				//TODO 删除好友处理
				FriendsService.delFri(uid, fid);
			}
		}
	}
	
	public static void main(String[] args) {
		FriendsClient client=new FriendsClient(MessageConfig.server_ip,MessageConfig.server_name);
		client.work();
	}
}
