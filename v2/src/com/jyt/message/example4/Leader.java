package com.jyt.message.example4;

import com.jyt.message.Message;
import com.jyt.message.MessageServerTcpClient;
import com.jyt.util.MyPrint;
import com.jyt.util.MySerializable;

public class Leader extends MessageServerTcpClient {
	
	public Leader(String server_ip,String server_name)
	{
		super(server_ip,server_name,"leader");

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
		Leader leader = new Leader(server_ip,server_name);
		byte[] bs;
		bs = MySerializable.object_bytes("做饭");
		Message cook_command1 = new Message("leader","cooker","request",bs);
		Message response1 = leader.send_and_wait(cook_command1,100);
		if(response1!=null){
			byte[] bs1 = response1.getContent();
			String s1 = (String)MySerializable.bytes_object(bs1);
			String s2 = "收到信息如下："+s1;
 			MyPrint.print(s2,new Exception());
		}
		else
			MyPrint.print("做饭返回为空",new Exception());
		
		bs = MySerializable.object_bytes("做菜");
		Message cook_command2 = new Message("leader","cooker","request",bs);
		Message response2 = leader.send_and_wait(cook_command2,100);
		if(response2!=null){
			byte[] bs2 = response2.getContent();
			String s1 = (String)MySerializable.bytes_object(bs2);
			String s2 = "收到信息如下："+s1;
 			MyPrint.print(s2,new Exception());
		}
		else
			MyPrint.print("做菜返回为空",new Exception());
		
		bs = MySerializable.object_bytes("洗衣服");
		Message cook_command3 = new Message("leader","washer","request",bs);
		Message response3 = leader.send_and_wait(cook_command3,100);
		if(response3!=null){
			byte[] bs3 = response3.getContent();
			String s1 = (String)MySerializable.bytes_object(bs3);
			String s2 = "收到信息如下："+s1;
 			MyPrint.print(s2,new Exception());
		}
		else
			MyPrint.print("洗衣服返回为空",new Exception());
			
		
	}

}
