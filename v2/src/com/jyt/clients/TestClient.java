package com.jyt.clients;

import java.util.Scanner;

import com.google.gson.Gson;
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
		
		/*while (true) {
			str = sc.nextLine();
			Message msg = new Message("sys_test","sys_login","login",bs);
			client.send(msg);
		}*/
		
		User user=new User();
		user.setId("123");
		user.setName("liunan");
		user.setPasswd("123");
		bs = MySerializable.object_bytes(new Gson().toJson(user));
		Message msg = new Message("sys_test","sys_login","login",bs);
		client.send(msg);
		
	}
}
