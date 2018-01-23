package com.jyt.clients;

import com.jyt.message.Message;
import com.jyt.message.MessageConfig;
import com.jyt.message.MessageListener;
import com.jyt.message.MessageServerTcpClient;
import com.jyt.util.ArgumentString;
import com.jyt.util.MyDate;
import com.jyt.util.MySerializable;

public class GroupClient extends MessageServerTcpClient {

	public GroupClient(String server_ip, String server_name) {
		super(server_ip, server_name, "sys_group");
		addListener("createGroup", new ResponseListener(this));
		addListener("pullMemberConf", new ResponseListener(this));
		addListener("delMember", new ResponseListener(this));
		addListener("modifyGroupName", new ResponseListener(this));
		addListener("groupMsg", new ResponseListener(this));
	}

	public class ResponseListener implements MessageListener {
		GroupClient client = null;

		public ResponseListener(GroupClient client) {
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
			if (type.equals("groupMsg")) {
				// TODO 收到群组消息，转发给每个人
			} else if (type.equals("createGroup")) {
				// TODO 创建群组，通知每个人
			} else if (type.equals("pullMemberConf")) {
				// TODO 用户确认进入群组后修改数据库
			} else if (type.equals("modifyGroupName")) {
				// TODO 修改群组名称
			} else if (type.equals("delMember")) {
				// TODO 删除成员，直接修改数据库，通知每个人
			}else if(type.equals("quitGroup")){
				// TODO 退群，修改数据库，通知每个人
			}
		}
	}

	public static void main(String[] args) {
		GroupClient client = new GroupClient(MessageConfig.server_ip,
				MessageConfig.server_name);
		client.work();
	}
}
