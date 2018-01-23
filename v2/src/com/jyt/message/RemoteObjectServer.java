package com.jyt.message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.jyt.util.ArgumentString;
import com.jyt.util.MyPrint;
import com.jyt.util.MySerializable;
import com.jyt.util.MyString;
import com.jyt.util.NameValue;

public class RemoteObjectServer {
	public static  int register(String entity) 
	{
		Thread.currentThread().setName("register_"+entity);
//		print_thread_info(new Exception());
		int ret = 0;
		List<Message> list= MessagePool.map.get(entity);
		if(list==null)
		{
			MessagePool.map.put(entity, new ArrayList<Message>());
			List<NameValue> conf_list = new ArrayList<NameValue>();
			NameValue nv = new NameValue(MessageConfig.ENTITY_MAX_MSG_LEN,"0");
			conf_list.add(nv);
			MessagePool.entity_conf_map.put(entity, conf_list);
			
		}
		else{
			ret = 1;
			MyPrint.print("This entity has registered", new Exception());
		}
		list = null;
		return ret;
	}
	
	public static int unregister(String entity) 
	{
		Thread.currentThread().setName("unregister_"+entity);
		int ret = 0;
		MessagePool.map.remove(entity);
		return ret;
	}	
	
	public static List<String> return_all_entity_info() 
	{
		Thread.currentThread().setName("return_all_entity_info");
		List<String> ret = new ArrayList<String>();
		int count = 1;
		for(String s:MessagePool.map.keySet())
		{
			String field = "%1[%2]";
			List<Message> list = MessagePool.map.get(s);
			String[] ss = new String[]{s,list.size()+""};
			String result = ArgumentString.replace(field,ss);
			count++;
			ret.add(result);
		}
		Collections.sort(ret);
		for(int i=0;i<ret.size();i++)
		{
			ret.set(i, "("+i+")"+ret.get(i));
		}
		
		MyPrint.print(MyString.to_string(ret),new Exception());
			
		return ret;

		
	}	
	
	public static List<Message> peek_all_message(String entity) 
	{
		Thread.currentThread().setName("peek_all_message");
		List<Message> ret = MessagePool.map.get(entity);
		return ret;
	}
	
	public static int set_system_conf(String name,String value) 
	{
		Thread.currentThread().setName("set_system_conf");
		int ret = 0;
		synchronized (MessagePool.system_conf)
		{
			if(MyString.in(name, MessageConfig.valid_system_attributes))
			{
				if(name.equals(MessageConfig.SYSTEM_TRACE))
				{
					if(MyString.in(value, new String[]{"true","false"}))
					{
						MessagePool.system_conf = NameValue.set(MessagePool.system_conf, name, value);		
					}
					else{
						ret = 1;
						MyPrint.print("设置的属性值不正确，应该为true或false。", new Exception());
					}
				}
				if(name.equals(MessageConfig.TRACED_ENITITIES))
				{
					if(value.equals("*"))
					{
						MessagePool.system_conf = NameValue.set(MessagePool.system_conf, name, "[1]*");
					}
					else if(value.indexOf("[1]")==0)
					{
						MessagePool.system_conf = NameValue.set(MessagePool.system_conf, name, value);
					}
					else {
						ret = 1;
						MyPrint.print("设置的属性值不正确，应该为\"*\"或者\"[1]entity_name1[2]entity_name2\"的形式。", new Exception());
					}
				}
				
			}
			else{
				MyPrint.print("所输入的系统属性名称不正确", new Exception());
				ret = 1;
			}
			
		}
		return ret;
	}
	
	public static List<NameValue> get_system_conf()
	{	
		Thread.currentThread().setName("get_system_conf");
		synchronized(MessagePool.system_conf)
		{
			byte[] bs = MySerializable.object_bytes((Serializable)(MessagePool.system_conf));
			MyPrint.print(bs.length+"", new Exception());
			return MessagePool.system_conf;	
		}
	}
	
	public static int set_entity_conf(String entity_name,String name,String value) 
	{
		Thread.currentThread().setName("set_entity_conf_"+entity_name);
		int ret = 0;
		List<NameValue> list = MessagePool.entity_conf_map.get(entity_name);
		if(list==null)
		{
			ret = 1;
			MyPrint.print("实体不存在！", new Exception());
			return ret;
		}
		synchronized (list)
		{
			if(MyString.in(name, MessageConfig.valid_entity_attributes))
			{
				list = NameValue.set(list, name, value);
				MessagePool.entity_conf_map.put(entity_name, list);
			}
			else{
				MyPrint.print("所输入的系统属性名称不正确", new Exception());
				ret = 1;
			}
			
		}
		list = null;
		return ret;
	}
	
	public static List<NameValue> get_entity_conf(String entity_name) 
	{	
		Thread.currentThread().setName("get_entity_conf_"+entity_name);
		List<NameValue> list = MessagePool.entity_conf_map.get(entity_name);
		if(list==null){
			MyPrint.print("实体不存在！", new Exception());
			return null;
		}
		synchronized(list)
		{
			return list;	
		}
		
	}

	public static void print_all_entity()
	{
		Thread.currentThread().setName("print_all_entity");
		String str_list = "";
		int count = 1;
		MyPrint.print("================ Print All Entities ===================", new Exception());
		for(String s:MessagePool.map.keySet())
		{
			String field = "%1(%2)%3[%4]";
			List<Message> list = MessagePool.map.get(s);
			String[] ss = new String[]{str_list,count+"",s,list.size()+""};
			String result = ArgumentString.replace(field,ss);
			str_list = result;
			count++;
		}
		MyPrint.print(str_list, new Exception());
	}
	
	public static int register(String entity,String[] names,String[] values) 
	{
		Thread.currentThread().setName("register2_"+entity);
		int ret = 0;
		List<Message> list= MessagePool.map.get(entity);
		if(list==null)
		{
			MessagePool.map.put(entity, new ArrayList<Message>());
			List<NameValue> conf_list = NameValue.build(names, values);
			MessagePool.entity_conf_map.put(entity, conf_list);
		}
		else{
			ret = 1;
			MyPrint.print("This entity has registered", new Exception());
		}
		list = null;
		return ret;
	}	
	
	public static int clear(String entity) 
	{
		Thread.currentThread().setName("clear_"+entity);
		int ret = 0;
		List<Message> list= MessagePool.map.get(entity);
		list.clear();
		list = null;
		return ret;
	}
	
	
	public static List<String> return_all_entity() 
	{
		Thread.currentThread().setName("return_all_entity");
		List<String> ret = new ArrayList<String>();
		for(String s:MessagePool.map.keySet())
		{
			ret.add(s);
		}
		Collections.sort(ret);
		return ret;
		
	}
	
	public static  String[] get_scoped_names(String name)
	{
		String[] ret = new String[2];
		int index = name.indexOf("@");
		if(index==-1){
			ret[0] = name;
			ret[1] = null;
		}
		else{
			ret[0] = name.substring(0,index);
			ret[1] = name.substring(index+1);
		}
		return ret;
	}	
	

	private static void process_length_limit(String entity)
	{
		List<NameValue> list = MessagePool.entity_conf_map.get(entity);
		String length_str = NameValue.get(list, MessageConfig.ENTITY_MAX_MSG_LEN);
		int length = 0;
		if(length_str!=null) 
		   length = Integer.parseInt(length_str);
		if(length != 0)
		{
			List<Message> message_list = MessagePool.map.get(entity);
			if(message_list.size()>length)
			{
				int num = message_list.size()-length;
				for(int i=0;i<num;i++)
				{
//					String s = message_list.get(0).toString();
//					MyPrint.print(s,new Exception());
//					Message x = message_list.remove(0);
//					x = null;
					message_list.remove(0);
				}
			}
			message_list = null;
		}
		list = null;
			
	}
	

	
	public static int add(Message message)
	{
		Thread.currentThread().setName("add_"+message.getFrom()+"_"+message.getTo());
	//	print_thread_info(new Exception());
		int ret = 0;
		String[] scoped_names = get_scoped_names(message.getTo());
		String entity = scoped_names[0];
		if(scoped_names[1]!=null)
		{
			entity = scoped_names[1];
		}
		synchronized(MessagePool.map)
		{
			List<Message>  list = MessagePool.map.get(entity);			;
			if(list==null)
			{
				ret = 1;
				MyPrint.print("This entity ("+entity+")not registered!",new Exception());
			}
			else{
//				list.add(Message.copy(message));
				list.add(message);
			}
//			process_trace(message);
			process_length_limit(entity);
			list = null;
		}
		return ret;
	}
	
	public static int get_current_thread_number()
	{
		return Thread.getAllStackTraces().size();
	}
	
	public static Message get(String entity)
	{
		Thread.currentThread().setName("get_"+entity);
	//	print_thread_info(new Exception());
		
		Message ret = null;
		int thread_size = get_current_thread_number();
		if(thread_size>MessageConfig.MAX_THREAD_COUNT)
			return ret;
		synchronized(MessagePool.map){
			List<Message> list = MessagePool.map.get(entity);
			if(list != null)
			{
				if(list.size()>0)
				{
					Message to_be_deleted = list.remove(0);
					ret = Message.copy(to_be_deleted);
					to_be_deleted = null;
					if(list.size()==0)
					{
						MessagePool.map.put(entity, new ArrayList<Message>());
						list.clear();
						list = null;
					}
				}
			}
			list = null;
		}
		return ret;
	}

	
	public static Message get_asyn(String entity) 
	{
		Thread.currentThread().setName("get_asyn_"+entity);
//		print_thread_info(new Exception());
		Message ret = null;
		int thread_size = get_current_thread_number();
		System.out.print("."+thread_size+"\r");
		if(thread_size>MessageConfig.MAX_THREAD_COUNT)
			return ret;
		synchronized(MessagePool.map){
			List<Message> list = MessagePool.map.get(entity);
			if(list!=null)
			{
				
				Iterator<Message> it = list.iterator();
				while(it.hasNext()){
					Message m = it.next();
					if(m.getType().indexOf("#")==-1)
					{		
//						ret = Message.copy(m);
						ret = m;
						it.remove();
						break;
					}
				}
				
			}
		}
	//	System.gc();
		return ret;
	}		

	public static Message get_syn(String entity,String type,String id) 
	{
		Thread.currentThread().setName("get_syn_"+entity);
	//	print_thread_info(new Exception());
		Message ret = null;
		int thread_size = get_current_thread_number();
		if(thread_size>MessageConfig.MAX_THREAD_COUNT)
			return ret;
		synchronized(MessagePool.map){
			List<Message> list = MessagePool.map.get(entity);
			if(list!=null)
			{
				for(int i=0;i<list.size();i++)
				{
					Message m = list.get(i);
					if(m.getType().equals(type+"#"+id))
					{
						ret = Message.copy(m);
						list.remove(i);
						break;
					}
				}
				
			}
			list = null;
			
		}
		return ret;
	}	
	

}
