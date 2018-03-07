package com.jyt.clients;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;
import com.jyt.clients.FriendsClient.ResponseListener;
import com.jyt.clients.model.Friend;
import com.jyt.clients.model.Group;
import com.jyt.clients.model.MessageRecord;
import com.jyt.clients.model.User;
import com.jyt.message.Message;
import com.jyt.message.MessageConfig;
import com.jyt.message.MessageListener;
import com.jyt.message.MessageServerTcpClient;
import com.jyt.util.ArgumentString;
import com.jyt.util.MyDate;
import com.jyt.util.MySerializable;

public class TestClient extends MessageServerTcpClient{
	
	public TestClient(String server_ip, String server_name) {
		super(server_ip, server_name,"sys_test");
		addListener("loginRes",new ResponseListener(this));
		addListener("createGroupRes",new ResponseListener(this));
		addListener("pullMemberRes",new ResponseListener(this));
		addListener("delMemberRes",new ResponseListener(this));
		addListener("quitGroupRes",new ResponseListener(this));
		addListener("modifyGroupNameRes",new ResponseListener(this));
		addListener("groupMsgRes",new ResponseListener(this));
		addListener("addFriConfRes",new ResponseListener(this));
		addListener("delFriRes",new ResponseListener(this));
		addListener("fetchFrisRes", new ResponseListener(this));
		addListener("searchRecordsRes", new ResponseListener(this));
	}
	

	public class ResponseListener implements MessageListener
	{
		TestClient client = null;

		public ResponseListener(TestClient client) {
			this.client = client;
		}
		public void messagePerformed(Message message)
		{
			String field = "**************\n%1 from %2 \n%3\n**************";
			String type=message.getType();
			String from = message.getFrom();
			String time_str = MyDate.f2(message.getCreated());
			String content = (String)MySerializable.bytes_object(message.getContent());
			String[] ss = new String[]{time_str,from,content};
			String result = ArgumentString.replace(field,ss);
			System.out.println(result);
		}
	}
	
	public static void main(String[] args) {
		TestClient client=new TestClient(MessageConfig.server_ip,MessageConfig.server_name);
		client.work();
		byte[] bs = null;
		Scanner sc = new Scanner(System.in);
		String str = null;
		Message msg=null;
		
		//创建测试群组消息
		Group gp=new Group();
		gp.setGid("Android2017");
		gp.setUid("u1");
		gp.setGname("test");
		gp.setMid("u5");
		List<String> mem=new ArrayList<String>();
		mem.add("u2");
		mem.add("u3");
		mem.add("u4");
		gp.setMembers(mem);
		
		//循环获取输入，进行相应测试
		while (true) {
			System.out.print(">>>");
			str = sc.nextLine();
			String[] myargs=str.split(" ");
			if(myargs[0].equals("login")){
				//测试登录
				User user=new User();
				user.setId("123");
				user.setName("liunan");
				user.setPasswd("123");
				bs = MySerializable.object_bytes(new Gson().toJson(user));
				msg = new Message("sys_test","sys_login","login",bs);
			}else if(myargs[0].equals("createGroup")){
				//测试创建群组
				bs = MySerializable.object_bytes(new Gson().toJson(gp));
				msg = new Message("sys_test","sys_group","createGroup",bs);
			}else if(myargs[0].equals("pullMember")){
				//测试添加群成员
				bs = MySerializable.object_bytes(new Gson().toJson(gp));
				msg = new Message("sys_test","sys_group","pullMember",bs);
			}else if(myargs[0].equals("delMember")){
				//测试删除群成员
				bs = MySerializable.object_bytes(new Gson().toJson(gp));
				msg = new Message("sys_test","sys_group","delMember",bs);
			}else if(myargs[0].equals("quitGroup")){
				//测试退群
				bs = MySerializable.object_bytes(new Gson().toJson(gp));
				msg = new Message("sys_test","sys_group","quitGroup",bs);
			}else if(myargs[0].equals("modifyGroupName")){
				//测试修改群名称
				if(myargs.length>=2){
					gp.setGname(myargs[1]);
				}
				bs = MySerializable.object_bytes(new Gson().toJson(gp));
				msg = new Message("sys_test","sys_group","modifyGroupName",bs);
			}else if(myargs[0].equals("addFriConf")){
				//测试添加好友
				Friend f=new Friend("123","456");
				bs = MySerializable.object_bytes(new Gson().toJson(f));
				msg = new Message("sys_test","sys_friends","addFriConf",bs);
			}else if(myargs[0].equals("delFri")){
				//测试删除好友
				Friend f=new Friend("123","456");
				bs = MySerializable.object_bytes(new Gson().toJson(f));
				msg = new Message("sys_test","sys_friends","delFri",bs);
			}else if(myargs[0].equals("fetchFris")){
				//测试获取好友列表
				Friend f=new Friend("3","");
				bs = MySerializable.object_bytes(new Gson().toJson(f));
				msg = new Message("sys_test","sys_friends","fetchFris",bs);
			}else if(myargs[0].equals("searchRecords")){
				//测试搜索聊天记录
				MessageRecord mr=new MessageRecord();
				mr.setUid("1");
				mr.setContent("h");
				bs = MySerializable.object_bytes(new Gson().toJson(mr));
				msg = new Message("sys_test","sys_search","searchRecords",bs);
			}
			client.send(msg);
		}
		
	}
}
