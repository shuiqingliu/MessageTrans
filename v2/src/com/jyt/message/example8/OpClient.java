package com.jyt.message.example8;

import com.jyt.message.Message;
import com.jyt.message.MessageServerTcpClient;
import com.jyt.message2.example8.OpContent;
import com.jyt.util.CountTime;
import com.jyt.util.MyPrint;
import com.jyt.util.MySerializable;

public class OpClient extends MessageServerTcpClient {
	
	public OpClient(String server_ip,String server_name)
	{
		super(server_ip,server_name,"op_client");
	}
	
	
	public void init()
	{
		long wait_time = 1000*5;
		int NUM = 1000;
		String[] result_arr = new String[NUM];
		CountTime ct = new CountTime("compute");
		for(int i=0;i<NUM;i++)
		{
			byte[] bs;
			OpContent oc = new OpContent(i,i);
			bs = MySerializable.object_bytes(oc);
			Message msg = new Message("op_client","op_server","request",bs);
			Message response = this.send_and_wait(msg, wait_time);
			byte[] bs2 = response.getContent();
			OpContent oc2 = (OpContent)MySerializable.bytes_object(bs2);
			result_arr[i] = oc2.result;
			System.out.print("."+i);
		}
		ct.print();
//		for(int i=0;i<NUM;i++)
//		{
//			System.out.print(" ("+i+")="+result_arr[i]);
//		}
		System.out.println("finished");
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
		MyPrint.print("Cooker begin...",new Exception());
		OpClient client = new OpClient(server_ip,server_name);
		client.init();
		
		
	}


}
