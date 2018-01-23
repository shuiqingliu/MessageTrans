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
				// TODO �յ�Ⱥ����Ϣ��ת����ÿ����
			} else if (type.equals("createGroup")) {
				// TODO ����Ⱥ�飬֪ͨÿ����
			} else if (type.equals("pullMemberConf")) {
				// TODO �û�ȷ�Ͻ���Ⱥ����޸����ݿ�
			} else if (type.equals("modifyGroupName")) {
				// TODO �޸�Ⱥ������
			} else if (type.equals("delMember")) {
				// TODO ɾ����Ա��ֱ���޸����ݿ⣬֪ͨÿ����
			}else if(type.equals("quitGroup")){
				// TODO ��Ⱥ���޸����ݿ⣬֪ͨÿ����
			}
		}
	}

	public static void main(String[] args) {
		GroupClient client = new GroupClient(MessageConfig.server_ip,
				MessageConfig.server_name);
		client.work();
	}
}
