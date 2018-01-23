package com.jyt.message.example5;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.jyt.message.Message;
import com.jyt.message.MessageListener;
import com.jyt.message.MessageServerTcpClient;
import com.jyt.util.MyPrint;
import com.jyt.util.MyString;

public class EntityF extends MessageServerTcpClient {
	public String entity_name;
	public static Set<String> set = new HashSet<String>();
	
	public EntityF(String server_ip,String server_name,String entity_name)
	{
		super(server_ip,server_name,entity_name);
		this.entity_name = entity_name;
	}
	
	public class FilterListener implements MessageListener
	{
		EntityF ef = null;
		
		public FilterListener(EntityF ef)
		{
			this.ef = ef;
		}
		
		public void a1_f_message_performed(EntityF ef,Message message)
		{
			String type = message.getType();
			if(MyString.in(type,new String[]{"t0","t2","t4","t6","t8"}))
			{
				MyPrint.print("拋棄此消息",new Exception());
			}
			else{
				Message new_message = new Message();
				new_message.setContent(message.getContent());
				new_message.setCreated(new Date());
				new_message.setFrom("a1_f");
				new_message.setTo("g_g@gbus");
				new_message.setId(message.getId());
				new_message.setType(message.getType());
				ef.send(new_message);
				MyPrint.print("===>"+new_message.toString(),new Exception());
				
			}
		}
		
		public void a2_f_message_performed(EntityF ef,Message message)
		{
			String type = message.getType();
			if(MyString.in(type,new String[]{"t1","t3","t5","t7","t9"}))
			{
				MyPrint.print("拋棄此消息",new Exception());
			}
			else{
				Message new_message = new Message();
				new_message.setContent(message.getContent());
				new_message.setCreated(new Date());
				new_message.setFrom("a2_f");
				new_message.setTo("g_g@gbus");
				new_message.setId(message.getId());
				new_message.setType(message.getType());
				ef.send(new_message);
				MyPrint.print("===>"+new_message.toString(),new Exception());
				
			}
		}
		
		public void g_f_message_performed(EntityF ef,Message message)
		{
			ef.set.add(message.getType());
			if(ef.set.size()==10)
			{
				MyPrint.print("All Message Received!", new Exception());
				System.exit(0);
			}
		}

		public void messagePerformed(Message message)
		{
			MyPrint.print("<==="+message.toString(),new Exception());
			if(ef.entity_name.equals("a1_f"))
			{
				a1_f_message_performed(ef,message);
			}
			else if(ef.entity_name.equals("a2_f"))
			{
				a2_f_message_performed(ef,message);
			}
			else if(ef.entity_name.equals("g_f"))
			{
				g_f_message_performed(ef,message);				
			}
			else{
				MyPrint.print("ERROR", new Exception());
				System.exit(0);
			}
		}
	
	}	
	
	public void init_g_f()
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
	
	public void init_a1_f()
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
	
	public void init_a2_f()
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
		if(entity_name.equals("g_f"))
		{
			init_g_f();
		}
		else if(entity_name.equals("a1_f"))
		{
			init_a1_f();
		}
		else if(entity_name.equals("a2_f"))
		{
			init_a2_f();
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
		EntityF ef;
		if(server_name.equals("gbus")){
			ef = new EntityF(server_ip,server_name,"g_f");
			MyPrint.print("gf begin...",new Exception());
		}
		else if(server_name.equals("abus1")){
			ef = new EntityF(server_ip,server_name,"a1_f");
			MyPrint.print("a1_f begin...",new Exception());
		}
		else if(server_name.equals("abus2")){
			ef = new EntityF(server_ip,server_name,"a2_f");
			MyPrint.print("a2_f begin...",new Exception());
		}
		else
		{
			ef = null;
			MyPrint.print("error",new Exception());
			System.exit(0);
		}
		ef.init();
		ef.work();
		
		
	}
		
}
