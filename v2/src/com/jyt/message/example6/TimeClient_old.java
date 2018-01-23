package com.jyt.message.example6;

import java.util.Date;

import com.jyt.message.Message;
import com.jyt.message.MessageListener;
import com.jyt.message.MessageServerTcpClient;
import com.jyt.util.MyPrint;
import com.jyt.util.MySerializable;

public class TimeClient_old  extends MessageServerTcpClient {
	
	public final int COUNT = 1000;
	public long[] l = new long[COUNT];
	public int num = 0;
	
	public TimeClient_old(String server_ip,String server_name)
	{
		super(server_ip,server_name,"client");
	}
	
	public class RequestListener implements MessageListener
	{
		TimeClient_old client = null;
		
		public RequestListener(TimeClient_old client)
		{
			this.client = client;
		}
		
		public void messagePerformed(Message message)
		{
			Date date = message.getCreated();
			byte[] bs = message.getContent();
			TimeStruct ts = (TimeStruct)MySerializable.bytes_object(bs);
			ts.setT4(new Date().getTime());
			l[num] = ts.get_middle_range();
			num++;
			if(num==COUNT)
			{
				long sum = 0;
				for(int i=0;i<COUNT;i++)
				{
					sum = sum + l[i];
				}
				float seconds = ((float)sum/COUNT)/1000;
				MyPrint.print("The time difference is "+seconds+" seconds.",new Exception());
			}
			else{
				send_msg(client);
			}
		}
	}
	
	public void init()
	{
		addListener("response",new RequestListener(this));
	}
	
	public static void send_msg(TimeClient_old client)
	{
		Message request = new Message();
		request.setFrom("client");
		request.setTo("server");
		request.setType("request");
		TimeStruct ts = new TimeStruct();
		ts.setT1(new Date().getTime());
		ts.setT2(0);
		ts.setT3(0);
		ts.setT4(0);
		byte[] bs2 = MySerializable.object_bytes(ts);
		request.setContent(bs2);
		client.send(request);		
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
		MyPrint.print("TimeClient begin...",new Exception());
		TimeClient_old time_client = new TimeClient_old(server_ip,server_name);
		time_client.init();
		time_client.work();
		
		send_msg(time_client);
		time_client.num++;
		
	}
	

}
