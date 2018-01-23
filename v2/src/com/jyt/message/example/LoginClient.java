package com.jyt.message.example;

import java.util.Scanner;

import com.jyt.message.Message;
import com.jyt.message.MessageConfig;
import com.jyt.message.MessageListener;
import com.jyt.message.MessageServerTcpClient;
import com.jyt.message.example.Leader.ResponseListener;
import com.jyt.util.ArgumentString;
import com.jyt.util.MyDate;
import com.jyt.util.MyPrint;
import com.jyt.util.MySerializable;

public class LoginClient extends MessageServerTcpClient{
	
	public LoginClient(String server_ip, String server_name) {
		super(server_ip, server_name,"login");
		addListener("msg",new ResponseListener());
		addListener("login",new ResponseListener());
	}

	public class ResponseListener implements MessageListener
	{
		public void messagePerformed(Message message)
		{
			String field = "**************\n%1 from %2 \n%3\n**************";
			String type=message.getType();
			String from = message.getFrom();
			String time_str = MyDate.f2(message.getCreated());
			String content = (String)MySerializable.bytes_object(message.getContent());
			if(type.equals("msg")){
				String[] ss = new String[]{time_str,from,content};
				String result = ArgumentString.replace(field,ss);
				System.out.println(result);
			}else if(type.equals("login")){
				System.out.println(from+" is login");
			}
		}
	}

	public static void main(String[] args) {
		LoginClient ca=new LoginClient("127.0.0.1",MessageConfig.server_name);
		ca.work();
		Scanner sc = new Scanner(System.in);
		String str = null;
		while (true) {
			str = sc.nextLine();
			byte[] bs;
			bs = MySerializable.object_bytes(str);
			Message msg = new Message("login","clientB","msg",bs);
			ca.send(msg);
		}
	}
}
