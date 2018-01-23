package com.jyt.message.example1;

import java.util.Date;

import com.jyt.message.Message;
import com.jyt.message.MessageConfig;
import com.jyt.message.MessageListener;
import com.jyt.message.MessageServerTcpClient;
import com.jyt.util.MyPrint;
import com.jyt.util.MySerializable;

public class TimeServer  extends MessageServerTcpClient {
	
	
	public TimeServer(String server_ip,String server_name)
	{
		super(server_ip,server_name,"server");
	}

	public class RequestListener implements MessageListener
	{
		TimeServer server = null;
		
		public RequestListener(TimeServer server)
		{
			this.server = server;
		}
		
		public void messagePerformed(Message message)
		{
	//		MyPrint.print("message receied:"+message.toString(),new Exception());
			Date date = message.getCreated();
			byte[] bs = message.getContent();
			TimeStruct ts = (TimeStruct)MySerializable.bytes_object(bs);
			ts.setT2(new Date().getTime());
			ts.setT3(new Date().getTime());
			Message response = new Message();
			byte[] bs2 = MySerializable.object_bytes(ts);
			response.setContent(bs2);
			response.setCreated(new Date());
			response.setFrom("server");
			response.setTo("client");
			response.setType("response");
			server.send(response);
		}
	}
	
	public void init()
	{
		addListener("request",new RequestListener(this));
	}

	public static void main(String[] args)
	{
		
		if(args.length!=1)
		{
			System.out.println("command server_ip server_name");
			System.exit(1);
		}
		String server_ip = args[0];	
		MyPrint.print("TimeServer begin...",new Exception());
		TimeServer time_server = new TimeServer(server_ip,MessageConfig.server_name);
		time_server.init();
		time_server.work();
		
		
	}



}
