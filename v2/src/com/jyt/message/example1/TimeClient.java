package com.jyt.message.example1;

import java.util.Date;

import com.jyt.message.Message;
import com.jyt.message.MessageConfig;
import com.jyt.message.MessageListener;
import com.jyt.message.MessageServerTcpClient;
import com.jyt.util.MyPrint;
import com.jyt.util.MySerializable;

public class TimeClient  extends MessageServerTcpClient {
	
	public final int COUNT = 100;
	public long[] l = new long[COUNT];
	public int num = 0;
	
	public TimeClient(String server_ip,String server_name)
	{
		super(server_ip,server_name,"client");
	}
	
	public class RequestListener implements MessageListener
	{
		TimeClient client = null;
		
		public RequestListener(TimeClient client)
		{
			this.client = client;
		}
		
		public void messagePerformed(Message message)
		{
			Date date = message.getCreated();
			byte[] bs = message.getContent();
			TimeStruct ts = (TimeStruct)MySerializable.bytes_object(bs);
			ts.setT4(new Date().getTime());
			MyPrint.print("num="+num+"", new Exception());
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
				num = 0;
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
	
	public static void send_msg(TimeClient client)
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
		
		if(args.length!=1)
		{
			System.out.println("command server_ip server_name");
			System.exit(1);
		}
		String server_ip = args[0];	
		MyPrint.print("TimeClient begin...",new Exception());
		TimeClient time_client = new TimeClient(server_ip,MessageConfig.server_name);
		time_client.init();
		time_client.work();
		
		send_msg(time_client);
		
	}
	

}
