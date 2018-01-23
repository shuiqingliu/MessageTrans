package com.jyt.message;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.jyt.util.ArgumentString;
import com.jyt.util.BinEdit;
import com.jyt.util.MyPrint;
import com.jyt.util.MySerializable;
import com.jyt.util.MyString;
import com.jyt.util.NameValue;

public class MessageTcpServerWorker implements Runnable {
	private Socket theSocket = null;
	


	private BufferedInputStream theReader = null;
	private BufferedOutputStream theWriter = null;
	
	private long last_active_timestamp = 0;
	
	public MessageTcpServerWorker(Socket theSocket) {
		this.theSocket = theSocket;
		this.last_active_timestamp = new Date().getTime();
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
		String t_name = Thread.currentThread().getName();
	//	MyPrint.print(t_name, new Exception());
		long last = 1000*3;
		TLV tlv = null;
		long begin_time = new Date().getTime();
		try{
		//	MyPrint.print(t_name, new Exception());
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
		//	MyPrint.print(t_name, new Exception());


		}
		catch(Exception e)
		{
			MyPrint.print(t_name, new Exception());
			e.printStackTrace();
		}
	//	MyPrint.print(t_name, new Exception());

		return tlv;
	}
	

	public byte[] process_message(TLV request)
	{
		byte[] ret = null;
		if(request.tag == 10)
		{
			byte[] value = request.value;
			String entity_name = new String(value);
			int result = RemoteObjectServer.register(entity_name);
			byte[] v2 = new byte[2];
			v2[0] = (byte)(result/128);
			v2[1] = (byte)(result%128);
			TLV response = new TLV(11,2,v2);
			ret = response.encode();
		}
		else if(request.tag == 12)
		{
			MyPrint.print("not implement yet", new Exception());
		}
		else if(request.tag == 14)
		{
			byte[] value = request.value;
			String entity_name = new String(value);
			int result = RemoteObjectServer.unregister(entity_name);
			byte[] v2 = new byte[2];
			v2[0] = (byte)(result/128);
			v2[1] = (byte)(result%128);
			TLV response = new TLV(11,2,v2);
			ret = response.encode();			
		}
		else if(request.tag == 16)
		{
		//	request.print();
			List<String> list = RemoteObjectServer.return_all_entity_info();
			String arr_str = MyString.to_string(list);
			byte[] value = arr_str.getBytes();
			TLV tlv  = new TLV(17,value.length,value);
			ret = tlv.encode();
		}
		else if(request.tag == 18)
		{
		//	request.print();
			byte[] bs_entity = request.value;
			String entity = EncodeDecode.decode_string(bs_entity);
			List<Message> list = RemoteObjectServer.peek_all_message(entity);
			byte[] reponse = EncodeDecode.encode_list_msg(list);
			byte[] head = EncodeDecode.prepare_head(19, reponse.length);
			ret = BinEdit.append(head, reponse);
		}
		else if(request.tag == 20)
		{
		//	request.print();
			byte[] bs_namevalue = request.value;
			NameValue nv = EncodeDecode.decode_namevalue(bs_namevalue);
			String name = nv.name;
			String value = nv.value;
			int ret_int = RemoteObjectServer.set_system_conf(name,value);
			byte[] bs_int = EncodeDecode.encode_integer_2(ret_int);
			byte[] head = EncodeDecode.prepare_head(21, bs_int.length);
			ret = BinEdit.append(head, bs_int);
		}
		else if(request.tag == 22)
		{
		//	request.print();
			List<NameValue> list = RemoteObjectServer.get_system_conf();
			byte[] bs_list = EncodeDecode.encode_list_namevalue(list);
			byte[] head = EncodeDecode.prepare_head(23, bs_list.length);
			ret = BinEdit.append(head, bs_list);
		}
		else if(request.tag == 24)
		{
		//	request.print();
			byte[] bs_three = request.value;
			int offset = 0;
			byte[] bs_entity_name = EncodeDecode.get_tlv(bs_three, offset);
			offset = offset +bs_entity_name.length;
			byte[] bs_name = EncodeDecode.get_tlv(bs_three, offset);
			offset = offset +bs_name.length;
			byte[] bs_value = EncodeDecode.get_tlv(bs_three, offset);
			String entity_name = EncodeDecode.decode_string(bs_entity_name);
			String name = EncodeDecode.decode_string(bs_name);
			String value = EncodeDecode.decode_string(bs_value);
			int ret_int = RemoteObjectServer.set_entity_conf(entity_name,name,value);
			byte[] bs_ret = EncodeDecode.encode_integer_2(ret_int);
			byte[] head = EncodeDecode.prepare_head(25, bs_ret.length);
			ret = BinEdit.append(head, bs_ret);
		}
		else if(request.tag == 26)
		{
		//	request.print();
			byte[] bs_entity = request.value;
			String entity = EncodeDecode.decode_string(bs_entity);
			List<NameValue> list = RemoteObjectServer.get_entity_conf(entity);
			byte[] bs_list = EncodeDecode.encode_list_namevalue(list);
			byte[] head = EncodeDecode.prepare_head(27, bs_list.length);
			ret = BinEdit.append(head, bs_list);
		}
		else if(request.tag == 28)
		{
		//	request.print();
			byte[] bs_value = request.value;
			int offset = 0;
			byte[] bs_entity_name = EncodeDecode.get_tlv(bs_value, offset);
			offset = offset+bs_entity_name.length;
			byte[] bs_list = EncodeDecode.get_tlv(bs_value, offset);
			String entity_name = EncodeDecode.decode_string(bs_entity_name);
			List<NameValue> list = EncodeDecode.decode_list_namevalue(bs_list);
			String[] names = new String[list.size()];
			String[] values = new String[list.size()];
			for(int i=0;i<names.length;i++)
			{
				names[i] = list.get(i).getName();
				values[i] = list.get(i).getValue();
			}
			int ret_int = RemoteObjectServer.register(entity_name,names,values);
			byte[] bs_ret = EncodeDecode.encode_integer_2(ret_int);
			byte[] head = EncodeDecode.prepare_head(29, bs_ret.length);
			ret = BinEdit.append(head, bs_ret);
		}
		else if(request.tag==30)
		{
		//	request.print();
			RemoteObjectServer.print_all_entity();
			byte[] head = EncodeDecode.prepare_head(31, 0);
			ret = head;
		}
		else if(request.tag == 32)
		{
		//	request.print();
			byte[] bs_value = request.value;
			String entity = EncodeDecode.decode_string(bs_value);
			int ret_int = RemoteObjectServer.clear(entity);
			byte[] bs_ret = EncodeDecode.encode_integer_2(ret_int);
			byte[] head = EncodeDecode.prepare_head(33, bs_ret.length);
			ret = BinEdit.append(head, bs_ret);
		}
		else if(request.tag == 34)
		{
		//	request.print();
			List<String> ret_list = RemoteObjectServer.return_all_entity();
			byte[] bs_list = EncodeDecode.encode_list_string(ret_list);
			byte[] head = EncodeDecode.prepare_head(35, bs_list.length);
			ret = BinEdit.append(head, bs_list);
		}
		else if(request.tag == 36)
		{
		//	request.print();
			Message msg = EncodeDecode.decode_msg(request.value);
			int ret_int = RemoteObjectServer.add(msg);
			MyPrint.print("ret_int="+ret_int, new Exception());
			byte[] bs_int = EncodeDecode.encode_integer_s(ret_int);
			byte[] head = EncodeDecode.prepare_head(37, bs_int.length);
			ret = BinEdit.append(head, bs_int);
		}
		else if(request.tag == 38)
		{
		//	request.print();
			String entity = EncodeDecode.decode_string(request.value);
			Message msg = RemoteObjectServer.get(entity);
			byte[] bs_msg = EncodeDecode.encode_msg(msg);
			byte[] head = EncodeDecode.prepare_head(39, bs_msg.length);
			ret = BinEdit.append(head, bs_msg);
			
		}
		else if(request.tag == 40)
		{
		//	request.print();
			String entity = EncodeDecode.decode_string(request.value);
			Message msg = RemoteObjectServer.get_asyn(entity);
			byte[] bs_msg = EncodeDecode.encode_msg(msg);
			byte[] head = EncodeDecode.prepare_head(41, bs_msg.length);
			ret = BinEdit.append(head, bs_msg);		
		}
		else if(request.tag == 42)
		{
		//	request.print();
			byte[] bs_value = request.value;
			int offset = 0;
			byte[] bs_entity = EncodeDecode.get_tlv(bs_value, offset);
			offset = offset+bs_entity.length;
			byte[] bs_type = EncodeDecode.get_tlv(bs_value, offset);
			offset = offset+bs_type.length;
			byte[] bs_id = EncodeDecode.get_tlv(bs_value, offset);
			String entity = EncodeDecode.decode_string(bs_entity);
			String type = EncodeDecode.decode_string(bs_type);
			String id = EncodeDecode.decode_string(bs_id);
			Message msg = RemoteObjectServer.get_syn(entity,type,id);
			byte[] bs_msg = EncodeDecode.encode_msg(msg);
			byte[] head = EncodeDecode.prepare_head(43, bs_msg.length);
			ret = BinEdit.append(head, bs_msg);
		}
		
		return ret;
	}
	



	
	public void run() {
		try {
			InetAddress inet = theSocket.getInetAddress();
			String clientName = inet.getHostName();
			//System.out.println(clientName + " is connected");
			theReader = new BufferedInputStream(theSocket.getInputStream());
			theWriter = new BufferedOutputStream(theSocket.getOutputStream());
			while (true) {

			//	MyPrint.print("", new Exception());
				TLV request = read_singling();
				if(request==null){
					Thread.sleep(1000);
					continue;
				}
		//		MyPrint.print("receive the request", new Exception());				
				this.last_active_timestamp = new Date().getTime();
				byte[] output_bs = process_message(request);
				send_singling(output_bs);
		//		MyPrint.print("", new Exception());
		//		MyPrint.print("sendout the response", new Exception());
				try {
					Thread.sleep(10);
				} catch (Exception e) {
					e.printStackTrace();
				}
		//		MyPrint.print("", new Exception());

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				theWriter.close();
				theReader.close();
				theSocket.close();
			} catch (Exception e) {

			}
		}
		
	}


}
