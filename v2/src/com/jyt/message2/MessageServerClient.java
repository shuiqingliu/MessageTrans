package com.jyt.message2;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jyt.util.ArgumentString;
import com.jyt.util.MyPrint;
import com.jyt.util.MySerializable;
import com.jyt.util.NameValue;

public class MessageServerClient implements Runnable {
	
	
	public String[] entity_list;
	public int port;
	public String server_ip;
	public MessageConnection connection = null;
	
	/**
	 * level=0 表示普通客户端，很少接收消息。
	 * level=1表示高级客户端，需要经常接收消息。
	 * level=2表示关键客户端。需要时刻处理消息，一刻也不能休息。
	 */
	public int level=0;
	public List<Message> list = new ArrayList<Message>();
	public Map<String,MessageListener> handler = new HashMap<String,MessageListener>();
	
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
				String error_url = ArgumentString.replace("//%1/%2", new String[]{server_ip,port+""});
				MyPrint.print(error_url, new Exception());
				
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
	
	public MessageServerClient(String server_ip,int port)
	{
		try{
			this.entity_list = null;
			this.server_ip = server_ip;
			this.port = port;
			level = 0;
		}
		catch(Exception e)
		{
			process_exception(e);
		}		
	}
	
	public MessageServerClient(String server_ip,int port,String entity)
	{
		try{
			this.entity_list = new String[]{entity};
			this.server_ip = server_ip;
			this.port = port;
			level = 0;
		}
		catch(Exception e)
		{
			process_exception(e);
		}
		
	}
	
	public MessageServerClient(String server_ip,int port,String[] entity_list)
	{
		try{
			this.entity_list = entity_list;
			this.server_ip = server_ip;
			this.port = port;
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
	
	public synchronized TLV send_and_receive(TLV out)
	{
		TLV read_tlv = null;
		try{
			connection = MessageConnection.get_available_connection(server_ip, port);	
			ReadWrite.send(out, connection.output_stream);
			read_tlv = ReadWrite.read(connection.input_stream);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return read_tlv;
	}
	
	public int register()
	{

		
		int ret = 0;
		try{

			for(String entity:entity_list)
			{
				TLV out = new TLV(12,entity);
				TLV in = send_and_receive(out);
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
		//	ret = remoteObj.set_system_conf(name,value);
			TLV out = new TLV(8,new String[]{name,value});
			TLV in = send_and_receive(out);
			Integer result = (Integer)MySerializable.bytes_object(in.v);
			ret = result.intValue();
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
		//	ret = remoteObj.get_system_conf();
			TLV out = new TLV(9,"");
			TLV in = send_and_receive(out);
			List<NameValue> result = (List<NameValue>)MySerializable.bytes_object(in.v);
			ret = result;
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
		//	ret = remoteObj.set_entity_conf(entity_name,name,value);
			TLV out = new TLV(10,new String[]{entity_name,name,value});
			TLV in = send_and_receive(out);
			Integer result = (Integer)MySerializable.bytes_object(in.v);
			ret = result.intValue();

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
		//	ret = remoteObj.get_entity_conf(entity_name);
			TLV out = new TLV(11,entity_name);
			TLV in = send_and_receive(out);
			List<NameValue> result = (List<NameValue>)MySerializable.bytes_object(in.v);
			ret = result;
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
		//	remoteObj.register(other_entity);
			TLV out = new TLV(12,other_entity);
			TLV in = send_and_receive(out);
			Integer result = (Integer)MySerializable.bytes_object(in.v);
			ret = result.intValue();
			
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
		//	remoteObj.register(other_entity,names,values);
			MyPrint.print("Not Implementation Yet", new Exception());
			System.exit(0);
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
		//	remoteObj.unregister(other_entity);
			TLV out = new TLV(14,other_entity);
			TLV in = send_and_receive(out);
			Integer result = (Integer)MySerializable.bytes_object(in.v);
			ret = result.intValue();

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
		//	ret = remoteObj.add(message);
			TLV out = new TLV(2,message);
			TLV in = send_and_receive(out);
			Integer result = (Integer)MySerializable.bytes_object(in.v);
			ret = result.intValue();

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
			else{
				try{
					Thread.sleep(2);
				}
				catch(Exception e)
				{
					e.printStackTrace();
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
		//		ret = remoteObj.get_asyn(entity);
				TLV out = new TLV(5,entity);
				TLV in = send_and_receive(out);
				Message result = (Message)MySerializable.bytes_object(in.v);
				ret = result;
				
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
				//	ret = remoteObj.get_syn(entity);
					TLV out = new TLV(4,new String[]{entity,type,id});
					TLV in = send_and_receive(out);
					Message result = (Message)MySerializable.bytes_object(in.v);
					ret = result;

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
		//	remoteObj.print_all_entity();
			TLV out = new TLV(16,"");
			TLV in = send_and_receive(out);			
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
		//	ret = remoteObj.return_all_entity();
			TLV out = new TLV(17,"");
			TLV in = send_and_receive(out);
			List<String> result = (List<String>)MySerializable.bytes_object(in.v);
			ret = result;

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
		//	ret = remoteObj.return_all_entity_info();
			TLV out = new TLV(18,"");
			TLV in = send_and_receive(out);
			List<String> result = (List<String>)MySerializable.bytes_object(in.v);
			ret = result;
			
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
			//	ret = remoteObj.clear(entity);		
				TLV out = new TLV(15,entity);
				TLV in = send_and_receive(out);

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
		//	ret = remoteObj.peek_all_message(entity);
			TLV out = new TLV(19,"");
			TLV in = send_and_receive(out);
			List<Message> result = (List<Message>)MySerializable.bytes_object(in.v);
			ret = result;

		}
		catch(Exception e)
		{
			process_exception(e);
		}		
		return ret;

	}
}
