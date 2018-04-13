package com.jyt.clients;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.google.gson.Gson;
import com.jyt.clients.model.Group;
import com.jyt.clients.model.User;
import com.jyt.clients.service.FriendsService;
import com.jyt.clients.service.GroupService;
import com.jyt.message.Message;
import com.jyt.message.MessageConfig;
import com.jyt.message.MessageListener;
import com.jyt.message.MessageServerTcpClient;
import com.jyt.util.ArgumentString;
import com.jyt.util.MyDate;
import com.jyt.util.MySerializable;
import com.sun.deploy.util.StringUtils;
import org.json.JSONObject;
import org.json.JSONException;
public class GroupClient extends MessageServerTcpClient {

	public GroupClient(String server_ip, String server_name) {
		super(server_ip, server_name, "sys_group");
		addListener("createGroup", new ResponseListener(this));
		addListener("pullMember", new ResponseListener(this));
		addListener("delMember", new ResponseListener(this));
		addListener("quitGroup", new ResponseListener(this));
		addListener("modifyGroupName", new ResponseListener(this));
		addListener("groupMsg", new ResponseListener(this));
		addListener("pullGroup",new ResponseListener(this));
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
			byte[] bs = null;
			System.out.println(result);
			if (type.equals("groupMsg")) {
				// TODO 收到群组消息，转发给每个人
				try {
					JSONObject jsonObject = new JSONObject(content);
					String groupId=jsonObject.getString("to");
					String content_=jsonObject.getString("content");
					String time=jsonObject.getString("ft");

					Group group = GroupService.getGroup(groupId);
					List<String> members= group.getMembers();
//					String[] userids = jsonObject.getString("member").split("、");
//					List<String> members= Arrays.asList(userids);
					if(members!=null){
						for(String m : members){
							if (!m.equals(from)){
								JSONObject sendMsg=new JSONObject();
								sendMsg.put("from",groupId);
								sendMsg.put("to",m);
								sendMsg.put("ft",time);
								sendMsg.put("content",content_);
								bs= MySerializable.object_bytes(sendMsg.toString());

								Message msg = new Message("sys_group", m, "msg", bs);
								client.send(msg);
								System.out.println(msg);
							}

						}
						// TODO 更新数据库
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else if (type.equals("createGroup")) {
				// 创建群组，通知每个人
				try {
					JSONObject jsonObject = new JSONObject(content);
					String uid=jsonObject.getString("uid");
					//String groupId=jsonObject.getString("gid");
					String groupName=jsonObject.getString("gname");
					String mem = jsonObject.getString("mem");
					String[] userids = jsonObject.getString("mem").split("、");
					List<String> members= Arrays.asList(userids);
					Group group = new Group();
					//group.setGid(groupId);
					group.setGname(groupName);
					group.setMembers(members);
					group.setUid(uid);
					group.setMember(mem);
					GroupService.createGroup(group);
					group.setGid(GroupService.getGroupByName(groupName).getGid());
					final JSONObject jsonObject1=new JSONObject();
						try {


						jsonObject1.put("uid",group.getUid());
						jsonObject1.put("gid",group.getGid());
						jsonObject1.put("gname",group.getGname());
						jsonObject1.put("mem",mem);}
						catch (JSONException e) {
							e.printStackTrace();
						}

					if(members!=null){
						for(String m : members){
							byte[] bs2;
							bs2 = MySerializable.object_bytes(jsonObject1.toString());
							Message msg = new Message("sys_group", m, "createGroupRes", bs2);
							client.send(msg);
							System.out.println(msg);
						}


						// TODO 更新数据库
						GroupService.createGroup(group);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else if (type.equals("pullMember")) {
				// 用户进入群组后修改数据库

				try {
					JSONObject jsonObject = new JSONObject(content);
					String groupId=jsonObject.getString("gid");
					String uid=jsonObject.getString("uid");
					String mid=jsonObject.getString("mid");
					Group grouplist = GroupService.getGroup(groupId);
					List<String> members= grouplist.getMembers();
					Group group = new Group();
					group.setGid(groupId);
					group.setUid(uid);
					group.setMid(mid);
					final JSONObject json=new JSONObject();
					try {
						json.put("uname",FriendsService.findUser(grouplist.getUid()).getName());
						json.put("gid",groupId);
						json.put("mname",FriendsService.findUser(mid).getName());
					} catch (JSONException e) {
						e.printStackTrace();
					}
					if(members!=null){
						for(String m : members){
							byte[] bs1;
							bs1 = MySerializable.object_bytes(json.toString());
							Message msg = new Message("sys_group", m, "pullMemberRes", bs1);
							client.send(msg);
							System.out.println(msg);
						}
					// TODO 更新数据库
					GroupService.pullMember(group);
				}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else if (type.equals("modifyGroupName")) {
				// 修改群组名称
				Group group=new Gson().fromJson(content, Group.class);
				List<String> members=GroupService.getGroup(group.getGid()).getMembers();
				if(members!=null){
					for(String m : members){
						Message msg = new Message("sys_group", m, "modifyGroupNameRes", message.getContent());
						client.send(msg);
						System.out.println(msg);
					}
					// TODO 更新数据库 
					GroupService.modifyGroupName(group);
				}
			} else if (type.equals("delMember")) {
				try {
					JSONObject jsonObject = new JSONObject(content);
					String groupId=jsonObject.getString("gid");
					String mId=jsonObject.getString("mid");
					Group groupList = GroupService.getGroup(groupId);

					List<String> member;
					member=groupList.getMembers();
					String s = StringUtils.join(member , "、");
					String[] members = s.split("、");//todo 解析群成员

					Group group = new Group();
					group.setGid(groupId);
					group.setMid(mId);
					final JSONObject json=new JSONObject();
					try {
						json.put("uname",FriendsService.findUser(groupList.getUid()).getName());
						json.put("gid",groupId);
						json.put("mname",FriendsService.findUser(mId).getName());
					} catch (JSONException e) {
						e.printStackTrace();
					}
					byte[] bs1;
					bs1 = MySerializable.object_bytes(json.toString());
					if(members!=null){
						for(String m : members){

							Message msg = new Message("sys_group", m, "delMemberRes", bs1);
							client.send(msg);
							System.out.println(msg);
						};
						// TODO 更新数据库
						GroupService.delMember(group);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}else if(type.equals("quitGroup")){
				// 退群，修改数据库，通知每个人

				try {
					JSONObject jsonObject = new JSONObject(content);
					String groupId=jsonObject.getString("gid");
					String mId=jsonObject.getString("mid");
					//String[] userids = jsonObject.getString("member").split("、");
					//List<String> members= Arrays.asList(userids);
					Group group = new Group();
					group.setGid(groupId);
					group.setMid(mId);
				//List<String> members=GroupService.getGroup(group.getGid()).getMembers();
					Group groupList = GroupService.getGroup(groupId);

					List<String> member;
					member=groupList.getMembers();
					String s = StringUtils.join(member , "、");
					String[] members = s.split("、");
				if(members!=null){
					for(String m : members){
						Message msg = new Message("sys_group", m, "quitGroupRes", message.getContent());
						client.send(msg);
						System.out.println(msg);
					}
					Message msg = new Message("sys_group", from, "quitGroupRes", message.getContent());
					client.send(msg);
					System.out.println(msg);
					// TODO 更新数据库 
					GroupService.quitGroup(group);
				}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}else if (type.equals("pullGroup")){
				sleep(2000);
				GroupService fs=new GroupService();
				List<Group> list=fs.pullGroup(from);
				Gson gson=new Gson();
				bs = MySerializable.object_bytes(gson.toJson(list).toString());
				Message msg = new Message("sys_group", from, "pullGroup",bs);
				client.send(msg);

			}
		}
	}

	public static void main(String[] args) {
		GroupClient client = new GroupClient(MessageConfig.server_ip,
				MessageConfig.server_name);
		client.work();
	}
}
