package com.jyt.message.example2;

import com.jyt.message.Message;
import com.jyt.message.MessageConfig;
import com.jyt.message.MessageListener;
import com.jyt.message.MessageServerTcpClient;
import com.jyt.util.MyPrint;
import com.jyt.util.MySerializable;

public class Task extends MessageServerTcpClient {
	
	public double count1 = 0;
	public double count2 = 0;
	public double total = 1000;
	public double result;
	
	public Task(String server_ip,String server_name)
	{
		super(server_ip,server_name,"task");
	}
	
	public class ResponseListener implements MessageListener
	{
		Task task = null;
		public ResponseListener(Task task)
		{
			this.task = task;
		}
		public void messagePerformed(com.jyt.message.Message message)
		{
			String r = new String(message.getContent());
			count2 = count2 + 1;
			if(r.equals("true"))
			{
				count1 = count1 + 1;
			}
			if(count2==total)
			{
				result = count1/count2*4;
				MyPrint.print(result+"",new Exception());
				System.exit(0);
			}
		}
		
	}
	
	public void init()
	{
		addListener("response",new ResponseListener(this));
		for(int i=0;i<total;i++)
		{
			String x = Math.random()+"";
			String y = Math.random()+"";
			String content = x+"#"+y;
			byte[] bs = MySerializable.object_bytes(content);
			Message m = new Message("task","computer","request",bs);
			send(m);
		}
		
	}
	
	public static void main(String[] args)
	{
		/*if(args.length!=2)
		{
			System.out.println("command server_ip server_name");
			System.exit(1);
		}		
		String server_ip = args[0];
		String server_name = args[1];*/				
		
		Task task = new Task("127.0.0.1",MessageConfig.server_name);
		task.init();
		task.work();
	}
	

}
