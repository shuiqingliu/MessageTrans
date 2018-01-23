package com.jyt.message.example5;

import java.util.Date;

import com.jyt.message.Message;
import com.jyt.message.MessageListener;
import com.jyt.message.MessageServerTcpClient;
import com.jyt.util.MyPrint;
import com.jyt.util.MySerializable;

public class EntityG extends MessageServerTcpClient {
	public String entity_name;
	
	public EntityG(String server_ip,String server_name,String entity_name)
	{
		super(server_ip,server_name,entity_name);
		this.entity_name = entity_name;
	}	
	
	public void init_a1_g(){
		for(int i=0;i<10;i++)
		{
			Message msg = new Message();
			msg.setContent(MySerializable.object_bytes("nothing"));
			msg.setCreated(new Date());
			msg.setFrom("a1_g");
			msg.setTo("a1_f");
			msg.setType("t"+i);
			this.send(msg);
			MyPrint.print("===>"+msg.toString(), new Exception());
		}
		
		
	}
	
	public void init_a2_g(){
		for(int i=0;i<10;i++)
		{
			Message msg = new Message();
			msg.setContent(MySerializable.object_bytes("nothing"));
			msg.setCreated(new Date());
			msg.setFrom("a2_g");
			msg.setTo("a2_f");
			msg.setType("t"+i);
			this.send(msg);
			MyPrint.print("===>"+msg.toString(), new Exception());
		}
	}
	
	public class FilterListener implements MessageListener
	{
		EntityG eg = null;
		
		public FilterListener(EntityG eg)
		{
			this.eg = eg;
		}
		
		public void g_g_message_performed(EntityG eg,Message message)
		{
			Message new_message = new Message();
			new_message.setContent(message.getContent());
			new_message.setCreated(new Date());
			new_message.setFrom("g_g");
			new_message.setTo("g_f");
			new_message.setId(message.getId());
			new_message.setType(message.getType());
			eg.send(new_message);
			MyPrint.print("===>"+new_message.toString(), new Exception());
		}
		
		public void messagePerformed(Message message)
		{
			MyPrint.print("<==="+message.toString(),new Exception());
			if(eg.entity_name.equals("a1_g"))
			{
				
			}
			else if(eg.entity_name.equals("a2_g"))
			{
			}
			else if(eg.entity_name.equals("g_g"))
			{
				g_g_message_performed(eg,message);				
			}
			else{
				MyPrint.print("ERROR", new Exception());
				System.exit(0);
			}
		}
	
	}	
	
	public void init_g_g()
	{
		addListener("t1",new FilterListener(this));
		addListener("t2",new FilterListener(this));
		addListener("t3",new FilterListener(this));
		addListener("t4",new FilterListener(this));
		addListener("t5",new FilterListener(this));
		addListener("t6",new FilterListener(this));
		addListener("t7",new FilterListener(this));
		addListener("t8",new FilterListener(this));
		addListener("t9",new FilterListener(this));
		addListener("t0",new FilterListener(this));
	}
	
	public void init()
	{
		if(entity_name.equals("g_g"))
		{
			init_g_g();
		}
		else if(entity_name.equals("a1_g"))
		{
			init_a1_g();
		}
		else if(entity_name.equals("a2_g"))
		{
			init_a2_g();
		}

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
		EntityG eg;
		if(server_name.equals("gbus")){
			eg = new EntityG(server_ip,server_name,"g_g");
			MyPrint.print("g_g begin...",new Exception());
		}
		else if(server_name.equals("abus1")){
			eg = new EntityG(server_ip,server_name,"a1_g");
			MyPrint.print("a1_g begin...",new Exception());
		}
		else if(server_name.equals("abus2")){
			eg = new EntityG(server_ip,server_name,"a2_g");
			MyPrint.print("a2_g begin...",new Exception());
		}
		else
		{
			eg = null;
			MyPrint.print("error",new Exception());
			System.exit(0);
		}
		eg.init();
		eg.work();
		
		
	}
	

}
