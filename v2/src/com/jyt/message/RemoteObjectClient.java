package com.jyt.message;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jyt.util.BinEdit;
import com.jyt.util.MyPrint;
import com.jyt.util.NameValue;

public class RemoteObjectClient {
	
	public String server_ip;
	public String server_name;
	public Socket theSocket = null;
	public BufferedInputStream theReader = null;
	public BufferedOutputStream theWriter = null;
	
	public long not_active_time = 0;
	
	public RemoteObjectClient(String server_ip,String server_name)
	{

		this.server_ip = server_ip;
		this.server_name = server_name;
		try{
			theSocket = new Socket(server_ip, 1098);
			theReader = new BufferedInputStream(theSocket.getInputStream());
			theWriter = new BufferedOutputStream(theSocket.getOutputStream());

			this.not_active_time =  new Date().getTime();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}
	
	public void TimeOutProcess()
	{
		long now = new Date().getTime();
		if(now-not_active_time>MessageConfig.MAX_NON_MESSAGE_PERIOD)
		{
			try{
				theSocket.close();
				theReader.close();
				theWriter.close();
				theSocket = new Socket(server_ip, 1098);
				theReader = new BufferedInputStream(theSocket.getInputStream());
				theWriter = new BufferedOutputStream(theSocket.getOutputStream());

				this.not_active_time =  new Date().getTime();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
	}
	
	public void send_singling(byte[] bs)
	{
		try{
			theWriter.write(bs, 0, bs.length);
			theWriter.flush();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}	
	}
	
	public TLV read_singling()
	{
		long last = 1000*3;
		TLV tlv = null;
		long begin_time = new Date().getTime();
		try{
			while(true)
			{
				if(theReader.available()>=3){
					byte[] bs1 = new byte[4];
					theReader.read(bs1,0,4);
					int tag = bs1[0]*128+bs1[1];
					int length = bs1[2]*128+bs1[3];
					byte[] bs2 = new byte[length];
					theReader.read(bs2, 0, length);
					tlv = new TLV(tag,length,bs2);	
					break;
				}
				else{
					Thread.sleep(20);
					long now = new Date().getTime();
				    if(now>last+begin_time)
					   break;			
			   }

			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return tlv;
	}
	
	public  int add(Message message) { 
		int tag = 36;
		int ret = 0;
		byte[] bs_msg = EncodeDecode.encode_msg(message);
		byte[] head = EncodeDecode.prepare_head(tag, bs_msg.length);
		byte[] request = BinEdit.append(head, bs_msg);
		send_singling(request);
	//	MyPrint.print("send the request", new Exception());
		TLV response = read_singling();
		if(response != null){
     //      MyPrint.print("receive the response", new Exception());
     //      response.print();
           int ret_int = 0;
           if(response.length!=0){
               if(response.value.length!=0)
            	   ret_int = EncodeDecode.decode_integer_s(response.value);
      	   
           }
  
  		   ret = ret_int;
		}
		else{
	//		MyPrint.print("not receive the response", new Exception());
		}
		return ret;
		
	};
	
	
	public  Message get(String entity) { 
		int tag = 38;
		Message msg = null;
		byte[] bs_entity = EncodeDecode.encode_string(entity);
		byte[] head = EncodeDecode.prepare_head(tag, bs_entity.length);
		byte[] request = BinEdit.append(head, bs_entity);
		send_singling(request);
		MyPrint.print("send the request", new Exception());
		TLV response = read_singling();
		if(response != null){
		   MyPrint.print("receive the response", new Exception());
		   Message ret_msg = EncodeDecode.decode_msg(response.value);
		   msg = ret_msg;
		}
		else{
			MyPrint.print("not receive the response", new Exception());
		}
		return msg;
	}
	
	public  Message get_asyn(String entity) { 
		int tag = 40;
		Message msg = null;
		byte[] bs_entity = EncodeDecode.encode_string(entity);
		byte[] head = EncodeDecode.prepare_head(tag, bs_entity.length);
		byte[] request = BinEdit.append(head, bs_entity);
		send_singling(request);
	//	MyPrint.print("send the request", new Exception());
		TLV response = read_singling();
		if(response != null){
	 //  MyPrint.print("receive the response", new Exception());
	 //	   response.print();
		   Message ret_msg = EncodeDecode.decode_msg(response.value);
		   msg = ret_msg;
		}
		else{
	//		MyPrint.print("not receive the response", new Exception());
		}
		return msg;		
	}
	
	public  Message get_syn(String entity,String type,String id) { 
	    int tag = 42;
	    Message ret = null;
	    int length = 0;
	    byte[] bs_entity = EncodeDecode.encode_string(entity);
	    length = bs_entity.length;
	    byte[] bs_type = EncodeDecode.encode_string(type);
	    length = length+bs_type.length;
	    byte[] bs_id = EncodeDecode.encode_string(id);
	    length = length+bs_id.length;
	    byte[] head = EncodeDecode.prepare_head(tag, length);
	    byte[] request = BinEdit.append(head, bs_entity);
	    request = BinEdit.append(request, bs_type);
	    request = BinEdit.append(request, bs_id);
	    send_singling(request);
		MyPrint.print("send the request", new Exception());
		TLV response = read_singling();
		if(response != null){
		   MyPrint.print("receive the response", new Exception());
		   Message ret_msg = EncodeDecode.decode_msg(response.value);
		   ret = ret_msg;
		}
		else{
			MyPrint.print("not receive the response", new Exception());
		}
		return ret;		
	}
	
	
	public  int register(String entity) { 
		int tag = 10;
		int ret = 0;
		byte[] value = entity.getBytes();
		int length = value.length;
		TLV request = new TLV(tag,length,value);
		send_singling(request.encode());
	//	MyPrint.print("send the request", new Exception());
		TLV response = read_singling();
		if(response != null){
		   ret = BinEdit.byte4_to_int(response.value);
	//	   MyPrint.print("receive the response", new Exception());
		}
		else{
	//		MyPrint.print("not receive the response", new Exception());
		}
		return ret;
	};
	
	public  int register(String entity,String[] names,String[] values)  {
		int tag = 28;
		int ret =  0;	
		List<NameValue> list = new ArrayList<NameValue>();
		for(int i=0;i<names.length;i++)
			list.add(new NameValue(names[i],values[i]));
		byte[] bs_entity = EncodeDecode.encode_string(entity);
		byte[] bs_list = EncodeDecode.encode_list_namevalue(list);
		byte[] head = EncodeDecode.prepare_head(tag, bs_entity.length+bs_list.length);
		byte[] request = BinEdit.append(head, bs_entity);
		request = BinEdit.append(request, bs_list);
		send_singling(request);
	//	MyPrint.print("send the request", new Exception());
		TLV response = read_singling();
		if(response != null){
			   byte[] bs_ret = response.value;
			   ret = EncodeDecode.decode_integer_2(bs_ret);
	//		   MyPrint.print("receive the response", new Exception());
			}
			else{
	//			MyPrint.print("not receive the response", new Exception());
			}
			return ret;
		
	};
	
	public  int unregister(String entity)  { 
		int tag = 14;
		int ret = 0;
		byte[] value = entity.getBytes();
		int length = value.length;
		TLV request = new TLV(tag,length,value);
		send_singling(request.encode());
		MyPrint.print("send the request", new Exception());
		TLV response = read_singling();
		if(response != null){
		   ret = BinEdit.byte4_to_int(response.value);
		   MyPrint.print("receive the response", new Exception());
		}
		else{
			MyPrint.print("not receive the response", new Exception());
		}
		return ret;		
	};	
	
	public  void print_all_entity()  { 
		int tag = 30;
		byte[] request = EncodeDecode.prepare_head(tag, 0);
		send_singling(request);
		MyPrint.print("send the request", new Exception());
		TLV response = read_singling();
		if(response != null){
		   MyPrint.print("receive the response", new Exception());
		}
		else{
			MyPrint.print("not receive the response", new Exception());
		}
		return ;		
		
	};
	
	public  int clear(String entity) {
		int tag = 32;
		int ret_int = 0;
		byte[] bs_entity = EncodeDecode.encode_string(entity);
		byte[] head = EncodeDecode.prepare_head(tag, bs_entity.length);
		byte[] request = BinEdit.append(head,bs_entity);
		send_singling(request);
		TLV response = read_singling();
		if(response != null){
		   MyPrint.print("receive the response", new Exception());
		   ret_int = EncodeDecode.decode_integer_2(response.value);
		}
		else{
			MyPrint.print("not receive the response", new Exception());
		}
		return ret_int;		

		
	};
	
	public  List<String> return_all_entity() { 
		int tag = 34;
		List<String> ret = null;
		byte[] request = EncodeDecode.prepare_head(tag, 0);
		send_singling(request);
		TLV response = read_singling();
		if(response != null){
		   MyPrint.print("receive the response", new Exception());
		   ret = EncodeDecode.decode_list_string(response.value);
		}
		else{
			MyPrint.print("not receive the response", new Exception());
		}
		return ret;		
	}
	
	public  List<String> return_all_entity_info() { 
		
		int tag = 16;
		 List<String> list = new ArrayList<String>();
		TLV request = new TLV(tag,0,null);
	//	request.print();
		send_singling(request.encode());
	//	MyPrint.print("send the request", new Exception());
		TLV response = read_singling();
		if(response != null){
		   byte[] value = response.value;
		   String s = new String(value);
	//	   MyPrint.print("receive the response:"+s, new Exception());
		   
		   String[] ss = s.split(" ");
		   for(int i=0;i<ss.length;i++)
			   list.add(ss[i]);
		   
		}
		else{
			MyPrint.print("not receive the response", new Exception());
		}
		return list;			
	}
	
	public  List<Message> peek_all_message(String entity) { 
		int tag = 18;
		List<Message> list = null;
		byte[] bs_entity = EncodeDecode.encode_string(entity);
		byte[] head = EncodeDecode.prepare_head(tag, bs_entity.length);
		byte[] bs_request = BinEdit.append(head, bs_entity);
		send_singling(bs_request);
		MyPrint.print("send the request", new Exception());
		TLV response = read_singling();
		if(response != null){
			byte[] value = response.value;
	    	list = EncodeDecode.decode_list_msg(value);
	    	if(list == null)
	    		MyPrint.print("list is null", new Exception());
	    	else
	    		MyPrint.print(list.toString(), new Exception());
		}
		else{
			MyPrint.print("not receive the response", new Exception());
		}
		return list;
		
	}
	
	
	public  int set_system_conf(String name,String value)  { 
		int tag = 20;
		int ret = 0;
		NameValue nv = new NameValue(name,value);
		byte[] bs_namevalue = EncodeDecode.encode_namevalue(nv);
		byte[] head = EncodeDecode.prepare_head(tag, bs_namevalue.length);
		byte[] bs_request = BinEdit.append(head, bs_namevalue);
		send_singling(bs_request);
		MyPrint.print("send the request", new Exception());
		TLV response = read_singling();
		if(response != null){
			byte[] bs_value = response.value;
			ret = EncodeDecode.decode_integer_2(bs_value);
			MyPrint.print(ret+"", new Exception());
		}
		else{
			MyPrint.print("not receive the response", new Exception());
		}
		return ret;
	}
	
	public  List<NameValue> get_system_conf() { 
		int tag = 22;
		List<NameValue> ret = null;
		byte[] head = EncodeDecode.prepare_head(tag, 0);
		send_singling(head);
		MyPrint.print("send the request", new Exception());
		TLV response = read_singling();
		if(response!=null)
		{
			byte[] bs_value = response.value;
			ret = EncodeDecode.decode_list_namevalue(bs_value);
			if(ret == null)
				MyPrint.print("ret is null", new Exception());
			else
				MyPrint.print(ret.toString(), new Exception());
		}
		else{
			MyPrint.print("not receive the response", new Exception());
		}
		return ret;
	}
	
	public  int set_entity_conf(String entity_name,String name,String value)  { 
		int tag = 24;
		int ret = 0;
		byte[] bs_entity_name = EncodeDecode.encode_string(entity_name);
		byte[] bs_name = EncodeDecode.encode_string(name);
		byte[] bs_value = EncodeDecode.encode_string(value);
		byte[] bs_three = BinEdit.append(bs_entity_name, bs_name);
		bs_three = BinEdit.append(bs_three,bs_value);
		byte[] head = EncodeDecode.prepare_head(tag, bs_three.length);
		byte[] request = BinEdit.append(head, bs_three);
		send_singling(request);
		MyPrint.print("send the request", new Exception());
		TLV response = read_singling();
		if(response != null){
			byte[] bs_int = response.value;
			int ret_int = EncodeDecode.decode_integer_2(bs_int);
			MyPrint.print(ret_int+"", new Exception());
			ret = ret_int;
		}
		else{
			MyPrint.print("not receive the response", new Exception());
		}
		return ret;
	};
	
	public  List<NameValue> get_entity_conf(String entity_name) { 
		int tag = 26;
		List<NameValue> ret = null;
		byte[] bs_entity_name = EncodeDecode.encode_string(entity_name);
		byte[] head = EncodeDecode.prepare_head(tag, bs_entity_name.length);
		byte[] request = BinEdit.append(head, bs_entity_name);
		send_singling(request);
		MyPrint.print("send the request", new Exception());
		TLV response = read_singling();
		if(response != null){
			byte[] bs_list_namevalue = response.value;
			List<NameValue> list = EncodeDecode.decode_list_namevalue(bs_list_namevalue);
			MyPrint.print(list.toString(), new Exception());
			ret = list;
		}
		else{
			MyPrint.print("not receive the response", new Exception());
		}
		return ret;

	}

}
