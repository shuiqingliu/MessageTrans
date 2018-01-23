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

public class ClientA extends MessageServerTcpClient{
	
	public ClientA(String server_ip, String server_name) {
		super(server_ip, server_name,"clientA");
		addListener("msg",new ResponseListener());
	}

	public class ResponseListener implements MessageListener
	{
		public void messagePerformed(Message message)
		{
			String field = "**************\n%1 from %2 \n%3\n**************";
			String time_str = MyDate.f2(message.getCreated());
			String from = message.getFrom();
			String content = (String)MySerializable.bytes_object(message.getContent());
			String[] ss = new String[]{time_str,from,content};
			String result = ArgumentString.replace(field,ss);
			System.out.println(result);
		}
	
	}

	public static void main(String[] args) {
		ClientA ca=new ClientA("127.0.0.1",MessageConfig.server_name);
		ca.work();
		Scanner sc = new Scanner(System.in);
		String str = null;
		while (true) {
			str = sc.nextLine();
			byte[] bs;
			bs = MySerializable.object_bytes(str);
			Message msg = new Message("clientA","login","login",bs);
			ca.send(msg);
		}
	}
}
