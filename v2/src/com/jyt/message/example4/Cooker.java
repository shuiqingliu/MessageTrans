package com.jyt.message.example4;

import java.util.Date;

import com.jyt.message.Message;
import com.jyt.message.MessageListener;
import com.jyt.message.MessageServerTcpClient;
import com.jyt.util.ArgumentString;
import com.jyt.util.MyDate;
import com.jyt.util.MyPrint;
import com.jyt.util.MySerializable;

public class Cooker extends MessageServerTcpClient {
	
	public Cooker(String server_ip,String server_name)
	{
		super(server_ip,server_name,"cooker");

	}
	
	public class RequestListener implements MessageListener
	{
		Cooker cooker = null;
		
		public RequestListener(Cooker cooker)
		{
			this.cooker = cooker;
		}
		
		public void messagePerformed(Message message)
		{
			MyPrint.print("message receied:"+message.toString(),new Exception());
			Date date = message.getCreated();
			byte[] bs = message.getContent();
			String command = (String)MySerializable.bytes_object(bs);
			String time = MyDate.f2(date);
			String from = message.getFrom();
			String field = "我是厨子，于%1收到了%2的命令：%3，保证完成任务。";
			String[] ss = new String[]{time,from,command};
			String info = ArgumentString.replace(field,ss);
		//	jd.tell_you_something(info);
			Message response = new Message();
			response.setFrom(message.getTo());
			response.setTo(message.getFrom());
			response.setType(message.getType()+"#"+message.getId());
			byte[] bs2 = MySerializable.object_bytes(info);
			response.setContent(bs2);
			cooker.send(response);
			MyPrint.print("message send:"+response.toString(),new Exception());
					
		}
	
	}
	
	public void init()
	{
		addListener("request",new RequestListener(this));
		

	}
	
	public static void main(String[] args)
	{
		if(args.length!=2)
		{
			System.out.println("command server_ip server_name");
			System.exit(1);
		}
		String server_ip = args[0];
		String server_name = args[1];		
		MyPrint.print("Cooker begin...",new Exception());
		Cooker cooker = new Cooker(server_ip,server_name);
		cooker.init();
		cooker.work();		
		
	}
	

	

}
