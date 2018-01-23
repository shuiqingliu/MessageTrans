package com.jyt.message.example;

import com.jyt.message.Message;
import com.jyt.message.MessageConfig;
import com.jyt.message.MessageListener;
import com.jyt.message.MessageServerTcpClient;
import com.jyt.util.ArgumentString;
import com.jyt.util.MyDate;
import com.jyt.util.MyPrint;
import com.jyt.util.MySerializable;

public class Leader extends MessageServerTcpClient {
	
	public Leader(String server_ip,String server_name)
	{
		super(server_ip,server_name,"leader");
		init();
	}
	
	public class ResponseListener implements MessageListener
	{
		public void messagePerformed(Message message)
		{
			String field = "我是领导，于%1收到了%2的回应如下:\n%3";
			String time_str = MyDate.f2(message.getCreated());
			String from = message.getFrom();
			String content = (String)MySerializable.bytes_object(message.getContent());
			String[] ss = new String[]{time_str,from,content};
			String result = ArgumentString.replace(field,ss);
			MyPrint.print(result,new Exception());
			
		}
	
	}
	
	public void init()
	{
		addListener("response",new ResponseListener());
		byte[] bs;
		bs = MySerializable.object_bytes("做饭");
		Message cook_command1 = new Message("leader","cooker","request",bs);
		send(cook_command1);
		bs = MySerializable.object_bytes("做菜");
		Message cook_command2 = new Message("leader","cooker","request",bs);
		send(cook_command2);
		bs = MySerializable.object_bytes("洗衣服");
		Message cook_command3 = new Message("leader","washer","request",bs);
		send(cook_command3);
		work();
		

	}
	
	public static void main(String[] args)
	{
		if(args.length!=1)
		{
			System.out.println("command server_ip server_name");
			System.exit(1);
		}		
		String server_ip = args[0];
			
		Leader leader = new Leader(server_ip,MessageConfig.server_name);
		
		
	}

}
