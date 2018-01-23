package com.jyt.message.example;

import java.util.Date;

import com.jyt.message.Message;
import com.jyt.message.MessageConfig;
import com.jyt.message.MessageListener;
import com.jyt.message.MessageServerTcpClient;
import com.jyt.util.ArgumentString;
import com.jyt.util.MyDate;
import com.jyt.util.MyPrint;
import com.jyt.util.MySerializable;

public class Washer extends MessageServerTcpClient {
	
	public Washer(String server_ip,String server_name)
	{
		super(server_ip,server_name,"washer");

	}
	
	public class RequestListener implements MessageListener
	{
		Washer washer = null;
		
		public RequestListener(Washer washer)
		{
			this.washer = washer;
		}
		
		public void messagePerformed(Message message)
		{
			MyPrint.print("message receied:"+message.toString(),new Exception());
			Date date = message.getCreated();
			byte[] bs = message.getContent();
			String command = (String)MySerializable.bytes_object(bs);
			String time = MyDate.f2(date);
			String from = message.getFrom();
			String field = "����ϴ�¹�����%1�յ���%2�����\n%3";
			String[] ss = new String[]{time,from,command};
			String info = ArgumentString.replace(field,ss);
			MyPrint.print(info, new Exception());
			Message response = new Message();
			response.setFrom(message.getTo());
			response.setTo(message.getFrom());
			response.setType("response");
			byte[] bs2 = MySerializable.object_bytes(info);
			response.setContent(bs2);
			washer.send(response);
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
		Washer washer = new Washer(server_ip,MessageConfig.server_name);
		washer.init();
		washer.work();
		
		
	}

}
