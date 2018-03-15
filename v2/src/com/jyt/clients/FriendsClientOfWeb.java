package com.jyt.clients;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.jyt.clients.model.User;
import com.jyt.clients.service.FriendsServiceOfWeb;
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

public class FriendsClientOfWeb extends MessageServerTcpClient {

	public FriendsClientOfWeb(String server_ip, String server_name) {
		super(server_ip, server_name, "sys_friends");
		//addListener("addFri", new ResponseListener(this));
		addListener("delFri", new ResponseListener(this));
		//addListener("addFriRes",new ResponseListener(this));
		addListener("fetchFris",new ResponseListener(this));
	}



	public class ResponseListener implements MessageListener {
		FriendsClientOfWeb client = null;

		public ResponseListener(FriendsClientOfWeb client) {
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

			String fid= "";
			try {
				JSONObject json=new JSONObject(content);
				fid = json.getString("uid");
			} catch (JSONException e) {
				e.printStackTrace();
			}


			if (type.equals("delFri")) {
				// 删除好友处理
				FriendsServiceOfWeb fs=new FriendsServiceOfWeb();
				fs.delFri(from,fid);
				res="{'success':'yes'}";
				bs = MySerializable.object_bytes(new JsonParser().parse(res).toString());
				Message msg = new Message("sys_friends", from, "delFri", bs);
				client.send(msg);
			} else if(type.equals("fetchFris")){
				FriendsServiceOfWeb fs=new FriendsServiceOfWeb();
				List<User> list=fs.fetchFris(fid);
				Gson gson=new Gson();
				bs = MySerializable.object_bytes(gson.toJson(list).toString());
				Message msg = new Message("sys_friends", from, "fetchFris", bs);
				client.send(msg);
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