package com.jyt.clients;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.jyt.clients.model.User;
import com.jyt.clients.service.FriendsServiceOfWeb;
import com.jyt.clients.service.UserInfoService;
import com.jyt.message.Message;
import com.jyt.message.MessageConfig;
import com.jyt.message.MessageListener;
import com.jyt.message.MessageServerTcpClient;
import com.jyt.util.ArgumentString;
import com.jyt.util.MyDate;
import com.jyt.util.MySerializable;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FriendsClientOfWeb extends MessageServerTcpClient {

	public FriendsClientOfWeb(String server_ip, String server_name) {
		super(server_ip, server_name, "sys_friends");
		addListener("addFri", new ResponseListener(this));
		addListener("delFri", new ResponseListener(this));
		addListener("addFriRes",new ResponseListener(this));
		addListener("fetchFris",new ResponseListener(this));
	}



	public class ResponseListener implements MessageListener {
		FriendsClientOfWeb client = null;

		public ResponseListener(FriendsClientOfWeb client) {
			this.client = client;
		}

		public void noticeToFriends(List<String> membersList, String msgType, String content){

			for(String memberId:membersList){
				byte[] bs = MySerializable.object_bytes(content);
				Message msg = new Message("sys_friends", memberId, msgType, bs);
				client.send(msg);
				System.out.println(msg);
			}
		}

		public void noticeToOne(String member, String msgType, String content){
				byte[] bs = MySerializable.object_bytes(content);
				Message msg = new Message("sys_friends", member, msgType, bs);
				client.send(msg);
				System.out.println(msg);
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

			if (type.equals("delFri")) {
				// 删除好友处理
                String uid="";
                String fid= "";

                try {
                    JSONObject json=new JSONObject(content);
                    fid = json.getString("fid");
                    uid = json.getString("uid");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                boolean a = FriendsServiceOfWeb.delFri(uid, fid);
				boolean b = FriendsServiceOfWeb.delFri(fid, uid);

				ArrayList<String> friend = new ArrayList<>();
				friend.add(fid);
				ArrayList<String> somebody = new ArrayList<>();
				somebody.add(uid);

				noticeToFriends(friend,"delFriRes","{'fid':'"+uid+"'}");
				noticeToFriends(somebody,"delFriRes","{'fid':'"+fid+"'}");

			} else if(type.equals("fetchFris")){

                String uid="";
                try {
                    JSONObject json=new JSONObject(content);
                    uid = json.getString("uid");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


				List<User> list=FriendsServiceOfWeb.fetchFris(uid);
				Gson gson=new Gson();
				bs = MySerializable.object_bytes(gson.toJson(list).toString());
				Message msg = new Message("sys_friends", from, "fetchFris", bs);
				client.send(msg);
			}else if(type.equals("addFri")){

				String fid1="";
				try {
					JSONObject json=new JSONObject(content);
					 fid1 = json.getString("fid1");
					String fid2 = json.getString("fid2");
					String message1 = json.getString("message");

					User user = UserInfoService.fetchUserInfoById(fid1);

					noticeToOne(fid2,"addFri","{'fname1':'"+user.getName()+"','fid1':'"+fid1+"','avatar':'"+user.getAvatar()+"','message':'"+message1+"'}");

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}else if(type.equals("addFriRes")){

				String fid1="";
				try {
					JSONObject json=new JSONObject(content);
					fid1 = json.getString("fid1");
					String fid2 = json.getString("fid2");
					String message1 = json.getString("message");
					if(message1.equals("yes")) {

						FriendsServiceOfWeb.addFri(fid1, fid2);

						User user1 = FriendsServiceOfWeb.findUser(fid1);
						User user2 = FriendsServiceOfWeb.findUser(fid2);

						noticeToOne(fid1, "addFriRes", "{'userID':'"+user2.getId()+"','userName':'"+user2.getName()+"','avatar':'"+user2.getAvatar()+"','department':'"+user2.getDepartment()+"','phone':'"+user2.getPhone()+"','email':'"+user2.getEmail()+"'}");
						noticeToOne(fid2, "addFriRes", "{'userID':'"+user1.getId()+"','userName':'"+user1.getName()+"','avatar':'"+user1.getAvatar()+"','department':'"+user1.getDepartment()+"','phone':'"+user1.getPhone()+"','email':'"+user1.getEmail()+"'}");
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}


		}
	}



	public static void main(String[] args) {
		FriendsClientOfWeb client = new FriendsClientOfWeb(MessageConfig.server_ip,
				MessageConfig.server_name);
		client.register();
		client.work();
	}
}