package com.jyt.clients;

import java.util.List;

import com.google.gson.Gson;
import com.jyt.clients.model.Group;
import com.jyt.clients.service.GroupService;
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
		addListener("pullMember", new ResponseListener(this));
		addListener("delMember", new ResponseListener(this));
		addListener("quitGroup", new ResponseListener(this));
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
				// ����Ⱥ�飬֪ͨÿ����
				Group group=new Gson().fromJson(content, Group.class);
				List<String> members=group.getMembers();
				for(String m : members){
					Message msg = new Message("sys_group", m, "createGroupRes", message.getContent());
					client.send(msg);
				}
				// TODO �������ݿ�
				GroupService.createGroup(group);
			} else if (type.equals("pullMember")) {
				// �û�����Ⱥ����޸����ݿ�
				Group group=new Gson().fromJson(content, Group.class);
				List<String> members=GroupService.getGroup(group.getGid()).getMembers();
				group.setMembers(members);
				if(members!=null){
					for(String m : members){
						Message msg = new Message("sys_group", m, "pullMemberRes", message.getContent());
						client.send(msg);
					}
					// TODO �������ݿ� 
					GroupService.pullMember(group);
				}
			} else if (type.equals("modifyGroupName")) {
				// �޸�Ⱥ������
				Group group=new Gson().fromJson(content, Group.class);
				List<String> members=GroupService.getGroup(group.getGid()).getMembers();
				if(members!=null){
					for(String m : members){
						Message msg = new Message("sys_group", m, "modifyGroupNameRes", message.getContent());
						client.send(msg);
					}
					// TODO �������ݿ� 
					GroupService.modifyGroupName(group);
				}
			} else if (type.equals("delMember")) {
				// ɾ����Ա��ֱ���޸����ݿ⣬֪ͨÿ����
				Group group=new Gson().fromJson(content, Group.class);
				List<String> members=GroupService.getGroup(group.getGid()).getMembers();
				group.setMembers(members);
				if(members!=null){
					for(String m : members){
						Message msg = new Message("sys_group", m, "delMemberRes", message.getContent());
						client.send(msg);
					}
					// TODO �������ݿ� 
					GroupService.delMember(group);
				}
			}else if(type.equals("quitGroup")){
				// ��Ⱥ���޸����ݿ⣬֪ͨÿ����
				Group group=new Gson().fromJson(content, Group.class);
				List<String> members=GroupService.getGroup(group.getGid()).getMembers();
				group.setMembers(members);
				if(members!=null){
					for(String m : members){
						Message msg = new Message("sys_group", m, "quitGroupRes", message.getContent());
						client.send(msg);
					}
					// TODO �������ݿ� 
					GroupService.quitGroup(group);
				}
			}
		}
	}

	public static void main(String[] args) {
		GroupClient client = new GroupClient(MessageConfig.server_ip,
				MessageConfig.server_name);
		client.work();
	}
}
