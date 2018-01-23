package com.jyt.message.example2;

import com.jyt.message.Message;
import com.jyt.message.MessageConfig;
import com.jyt.message.MessageListener;
import com.jyt.message.MessageServerTcpClient;
import com.jyt.util.MyPrint;
import com.jyt.util.MySerializable;

public class Computer extends MessageServerTcpClient {
	
	public int count = 0;
	public Computer(String server_ip,String server_name)
	{
		super(server_ip,server_name,"computer");
	}	
	
	public class RequestListener implements MessageListener
	{
		Computer computer = null;
		public RequestListener(Computer computer)
		{
			this.computer = computer;
		}
		public void messagePerformed(com.jyt.message.Message message)
		{
			count++;
			MyPrint.print(count+":"+message.toString(),new Exception());
			String cs = (String)MySerializable.bytes_object(message.getContent());
			String r = new String(cs);
			String[] ss = r.split("#");
			double x = Double.parseDouble(ss[0]);
			double y = Double.parseDouble(ss[1]);
			double r2 = (x-0.5)*(x-0.5)+(y-0.5)*(y-0.5);
			byte[] bs = MySerializable.object_bytes("false");
			Message sm = new Message("computer","task","response",bs);
			if(r2<=0.25)
			{
				sm.setContent("true".getBytes());
			}			
			computer.send(sm);
			
		}
		
	}	
	
	public void init()
	{
		addListener("request", new RequestListener(this));

	}
	
	public static void main(String[] args)
	{
		
		/*if(args.length!=2)
		{
			System.out.println("command server_ip server_name");
			System.exit(1);
		}		
		String server_ip = args[0];
		String server_name = args[1];	*/			
		
		Computer computer = new Computer("127.0.0.1",MessageConfig.server_name);
		computer.init();
		computer.work();
	}

}
