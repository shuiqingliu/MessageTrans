package com.jyt.clients;

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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class FriendsClient extends MessageServerTcpClient {

	public FriendsClient(String server_ip, String server_name) {
		super(server_ip, server_name, "sys_friend");
		addListener("addFri", new ResponseListener(this));
		addListener("delFri", new ResponseListener(this));
		addListener("addFriRes",new ResponseListener(this));
		addListener("pullFri",new ResponseListener(this));
		addListener("pullUser",new ResponseListener(this));
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
			String res = "";
			byte[] bs = null;

			if (type.equals("addFri")) {
				// 添加好友成功后处理，返回好友信息
				try {
					JSONObject json=new JSONObject(content);
					String toUserId=json.getString("id");
					json.remove("id");
					json.put("id",from);
					bs = MySerializable.object_bytes(json.toString());
					Message msg = new Message("sys_friend", toUserId, "addFri", bs);
					client.send(msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}


			} else if (type.equals("delFri")) {
				// 删除好友处理
				try {
					JSONObject json=new JSONObject(content);
					String fid=json.getString("id");
					FriendsService fs=new FriendsService();
					fs.delFri(from,fid);
				} catch (JSONException e) {
					e.printStackTrace();
				}

//				bs = MySerializable.object_bytes(new JsonParser().parse(res)
//						.toString());
//				Message msg = new Message("sys_friend", from, "delFriRes", bs);
//				client.send(msg);

			} else if(type.equals("addFriRes")){

				JSONObject json= null;
				String toUserId="";
				try {
					json = new JSONObject(content);
					toUserId=json.getString("id");
					//在friend表中添加
					FriendsService fs=new FriendsService();
					fs.addFri(from,toUserId);

					//给from发送添加
					User user=FriendsService.findUser(toUserId);
					JSONObject userJson=new JSONObject();
					userJson.put("userID",user.getId());
					userJson.put("userName",user.getName());
					userJson.put("avatar",user.getAvatar());
					userJson.put("department",user.getDepartment());
					userJson.put("phone",user.getPhone());
					userJson.put("email",user.getEmail());
					bs = MySerializable.object_bytes(userJson.toString());
					Message msg = new Message("sys_friend", from, "addFriRes", bs);
					client.send(msg);

					//给toUserId发送添加
					User user2=FriendsService.findUser(from);
					JSONObject userJson2=new JSONObject();
					userJson2.put("userID",user2.getId());
					userJson2.put("userName",user2.getName());
					userJson2.put("avatar",user2.getAvatar());
					userJson2.put("department",user2.getDepartment());
					userJson2.put("phone",user2.getPhone());
					userJson2.put("email",user2.getEmail());
					System.out.println();
					bs = MySerializable.object_bytes(userJson2.toString());
					Message msg2 = new Message("sys_friend", toUserId, "addFriRes", bs);
					client.send(msg2);
				} catch (JSONException e) {
					e.printStackTrace();
				}

			} else if(type.equals("pullFri")){
				FriendsService fs=new FriendsService();
				List<User> list=fs.pullFri(from);
				Gson gson=new Gson();
				bs = MySerializable.object_bytes(gson.toJson(list));
				Message msg = new Message("sys_friend", from, "pullFri", bs);
				client.send(msg);
			} else if(type.equals("pullUser")){
				FriendsService fs=new FriendsService();
				List<User> list=fs.pullUser();
				Gson gson=new Gson();
				bs = MySerializable.object_bytes(gson.toJson(list));
				Message msg = new Message("sys_friend", from, "pullUser", bs);
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