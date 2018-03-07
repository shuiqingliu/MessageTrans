package com.jyt.message;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.net.Socket;
import java.rmi.Naming;
import java.sql.Connection;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jyt.clients.db.ConnectionPool;
import com.jyt.clients.db.ConnectionPoolUtils;
import com.jyt.util.ArgumentString;
import com.jyt.util.MyPrint;
import com.jyt.util.MySerializable;
import com.jyt.util.NameValue;

public class MessageServerTcpClient implements Runnable {

	public String[] entity_list;
	public String server_name;
	public String server_ip;
	public RemoteObjectClient remoteObj;
	/**
	 * level=0 表示普通客户端，很少接收消息。
	 * level=1表示高级客户端，需要经常接收消息。
	 * level=2表示关键客户端。需要时刻处理消息，一刻也不能休息。
	 */
	public int level=0;
	public List<Message> list = new ArrayList<Message>();
	public Map<String,MessageListener> handler = new HashMap<String,MessageListener>();
	
	public Socket theSocket = null;
	public BufferedInputStream theReader = null;
	public BufferedOutputStream theWriter = null;
	
	public void process_exception(Exception e)
	{
		String field = "远端连接发生错误，系统退出执行。具体故障信息如下：\r\n%1";
		String[] ss = new String[]{e.toString()};
		String result = ArgumentString.replace(field,ss);
		MyPrint.print(result,e);
		e.printStackTrace();
		//System.exit(1);
		while(true)
		{
			try{
				Thread.sleep(1000*5);

				
			}
			catch(Exception ex)
			{
				MyPrint.print("目前暂时不能连接服务器，等5秒钟再连接", ex);
			}
			
		}
	}
	
	public String[] get_entity_names()
	{
		return entity_list;
	}
	
	public MessageServerTcpClient(String server_ip,String server_name)
	{
		try{
			this.entity_list = null;
			this.server_ip = server_ip;
			this.server_name = server_name;
			this.remoteObj = new RemoteObjectClient(server_ip,server_name);
			level = 0;
		}
		catch(Exception e)
		{
			process_exception(e);
		}		
	}
	
	
	public MessageServerTcpClient(String server_ip,String server_name,String entity)
	{
		try{
			this.entity_list = new String[]{entity};
			this.server_ip = server_ip;
			this.server_name = server_name;
			this.remoteObj = new RemoteObjectClient(server_ip,server_name);
			this.entity_list = new String[]{entity};
			level = 0;
		}
		catch(Exception e)
		{
			process_exception(e);
		}
		
	}
	
	public MessageServerTcpClient(String server_ip,String server_name,String[] entity_list)
	{
		try{
			this.server_ip = server_ip;
			this.server_name = server_name;
			this.remoteObj = new RemoteObjectClient(server_ip,server_name);
			this.entity_list = entity_list;
			level = 0;
		}
		catch(Exception e)
		{
			process_exception(e);
		}
		
	}
	

	

	
	
	public void setLevel(int level)
	{
		this.level = level;
	}
	
	public void add_entity(String name)
	{
		if(entity_list==null)
		{
			entity_list = new String[]{name};
		}
		else 
		{
			String[] new_entity_list = new String[entity_list.length+1];
			for(int i=0;i<entity_list.length;i++)
			{
				new_entity_list[i] = entity_list[i];
			}
			new_entity_list[new_entity_list.length-1] = name;	
		}
		
	}
	
	public void work()
	{
		Thread t = new Thread(this);
		t.setName("message_receive_and_handle");
		t.start();
	}
	
	public void addListener(String type,MessageListener listener)
	{
		handler.put(type, listener);
	}
	
	public void perform()
	{
		while(list.size()>0)
		{
			Message m = list.remove(0);
			String type = m.getType();
			if(type.equals("kill"))
			{
				MyPrint.print("收到系统的要求退出信息！", new Exception());
				System.exit(0);
			}

			MessageListener l = handler.get(type);
			if(l==null)
			{
				MyPrint.print("This Message not register for listener:"+m.toString(),new Exception());
			}
			else{
		//		MyPrint.print("Begin to process message", new Exception());
				l.messagePerformed(m);
				m = null;
			}
		}
	}
	
	
	public void sleep(long mi_seconds)
	{
		try{
			Thread.sleep(mi_seconds);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void wait_time_0(int count)
	{
		int yz = 15;
		if(count>100)
		{
			sleep(100*yz);
			count = 0;
		}
		else if(count>10)
		{
			sleep(10*yz);
		}
		else if(count>5)
		{
			sleep(5*yz);
		}
		else {
			sleep(1*yz);
		}		
	}
	 
	public void wait_time_1(int count)
	{
		int yz = 10;
		if(count>100)
		{
			sleep(100*yz);
			count = 0;
		}
		else if(count>10)
		{
			sleep(10*yz);
		}
		else if(count>5)
		{
			sleep(5*yz);
		}
		else {
			sleep(1*yz);
		}		
	}	
	
	public void wait_time_2(int count)
	{
		if(count>100)
		{
			sleep(100);
			count = 0;
		}
		else if(count>10)
		{
			sleep(10);
		}
		else if(count>5)
		{
			sleep(5);
		}
		else {
			sleep(1);
		}		
	}	
	
	public void run()
	{
		Thread.currentThread().setName("message_client_"+entity_list[0]);
		int count = 0;
		while(true)
		{
			Message m = receive();
			synchronized(list)
			{
				if(m!=null)
				{
					list.add(m);
					m = null;
					count = 0;
				}
				else{
					count++;
					if(level==0)
					{
					   wait_time_0(count);
						
					}
					else if(level==1)
					{
					    wait_time_1(count);
						
					}
					else if(level==2)
					{
						wait_time_2(count);
						
					}
				}
				perform();	
			
			}

		}
	}
	
	


	
	
	public int register()
	{
		int ret = 0;
		try{
			for(String entity:entity_list)
			{
				remoteObj.register(entity);				
			}
		}
		catch(Exception e)
		{
			process_exception(e);
		}
		return ret;
	}
	
	public int set_system_conf(String name,String value)
	{
		int ret = 0;
		try{
			ret = remoteObj.set_system_conf(name,value);
		}
		catch(Exception e)
		{
			process_exception(e);
		}
		return ret;		
	}
	
	public List<NameValue> get_system_conf()
	{
		List<NameValue> ret = null;
		try{
			ret = remoteObj.get_system_conf();
		}
		catch(Exception e)
		{
			process_exception(e);
		}
		return ret;			
	}
	
	public int set_entity_conf(String entity_name,String name,String value)
	{
		int ret = 0;
		try{
			ret = remoteObj.set_entity_conf(entity_name,name,value);
		}
		catch(Exception e)
		{
			process_exception(e);
		}
		return ret;		
	}
	
	public List<NameValue> get_entity_conf(String entity_name)
	{
		List<NameValue> ret = null;
		try{
			ret = remoteObj.get_entity_conf(entity_name);
		}
		catch(Exception e)
		{
			process_exception(e);
		}
		return ret;			
	}	

	public int register(String other_entity)
	{
		int ret = 0;
		try{
			remoteObj.register(other_entity);
		}
		catch(Exception e)
		{
			process_exception(e);
		}
		return ret;
		
	}
	
	public int register(String other_entity,String[] names,String[] values)
	{
		int ret = 0;
		try{
			remoteObj.register(other_entity,names,values);
		}
		catch(Exception e)
		{
			process_exception(e);
		}
		return ret;
		
	}	
	
	public int unregister(String other_entity)
	{
		int ret = 0;
		try{
			remoteObj.unregister(other_entity);
		}
		catch(Exception e)
		{
			process_exception(e);
		}
		return ret;
		
	}	

	public int send(Message message)
	{
		int ret = 0;
		try{
			ret = remoteObj.add(message);
			
			//将要发送的消息存入数据库
			Date now=new Date();    
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
			ConnectionPool connPool = ConnectionPoolUtils.GetPoolInstance();
			String sql = "INSERT INTO message VALUES('" + message.getFrom() + "', '"
					+ message.getTo() + "', '" + message.getType() + "','"+df.format(now)+"','"+(String) MySerializable.bytes_object(message
							.getContent())+"')";
			try {
				Connection conn = connPool.getConnection();
				Statement stmt = conn.createStatement();
				stmt.execute(sql);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		catch(Exception e)
		{
			process_exception(e);
		}
		
		return ret;
	}
	
	public Message send_and_wait(Message message,long wait_time)
	{
		Message ret = null;
		int r1 = send(message);
	//	MyPrint.print("message sent:"+message.toString(),new Exception());
		String entity = message.getFrom();
		if(r1!=0)
		{
			return ret;
		}
		long start = System.currentTimeMillis();
		while(true)
		{
			long end = System.currentTimeMillis();
			if((end-start)>wait_time)
			{
				break;
			}
			Message msg = receive(entity,message.type,message.id);
			if(msg!=null)
			{
		//		MyPrint.print("message received:"+msg.toString(), new Exception());
				String type = msg.getType();
				if(type.equals(message.getType()+"#"+message.getId()))
				{
					ret = Message.copy(msg);
					break;
				}
			}
		}
		return ret;
	}
	

	public Message receive()
	{
		Message ret = null;
		try{
			for(String entity : entity_list)
			{
				ret = remoteObj.get_asyn(entity);
				if(ret!=null){
//					ret = Message.copy(ret);
					break;
				}

			}
		}
		catch(Exception e)
		{
			process_exception(e);
		}

		return ret;
	}
	
	public Message receive(String entity,String type,String id)
	{
		Message ret = null;
		try{
			for(String s : entity_list)
			{
				if(entity.equals(s)){
					ret = remoteObj.get_syn(entity,type,id);
					if(ret!=null)
					{
						ret = Message.copy(ret);		

					}
				}
			}
		}
		catch(Exception e)
		{
			process_exception(e);
		}
//		if(ret!=null)
//			MyPrint.print(ret.toString(), new Exception());
		return ret;
	}
	
	public void print_all_entity()
	{
		try{
			remoteObj.print_all_entity();
		}
		catch(Exception e)
		{
			process_exception(e);
		}		
	}
	
	public List<String> return_all_entity()
	{
		List<String> ret = null;
		try{
			ret = remoteObj.return_all_entity();
		}
		catch(Exception e)
		{
			process_exception(e);
		}		
		return ret;

	}
	
	public List<String> return_all_entity_info()
	{
		List<String> ret = null;
		try{
			ret = remoteObj.return_all_entity_info();
		}
		catch(Exception e)
		{
			process_exception(e);
		}		
		return ret;

	}	
	
	public int clear()
	{
		int ret = 0;
		try{
			for(String entity:entity_list)
			{
				ret = remoteObj.clear(entity);				
			}
		}
		catch(Exception e)
		{
			process_exception(e);
		}		
		return ret;
		
	}
	
	public List<Message> peek_all_message(String entity)
	{
		List<Message> ret = null;
		try{
			ret = remoteObj.peek_all_message(entity);
		}
		catch(Exception e)
		{
			process_exception(e);
		}		
		return ret;

	}

}
