package com.jyt.message2.example8;

import java.util.Date;

import com.jyt.message2.Message;
import com.jyt.message2.MessageListener;
import com.jyt.message2.MessageServerClient;
import com.jyt.util.MyPrint;
import com.jyt.util.MySerializable;

public class OpServer extends MessageServerClient {
		
		
		public OpServer(String server_ip,int port)
		{
			super(server_ip,port,"op_server");
		}

		public class RequestListener implements MessageListener
		{
			OpServer server = null;
			
			public RequestListener(OpServer server)
			{
				this.server = server;
			}
			
			public void messagePerformed(Message message)
			{
				Date date = message.getCreated();
				byte[] bs = message.getContent();
				OpContent cnt = (OpContent)MySerializable.bytes_object(bs);
				cnt.compute();
		//		MyPrint.print(cnt.a1, new Exception());
				System.out.print(cnt.a1+".");
				Message response = new Message();
				byte[] bs2 = MySerializable.object_bytes(cnt);
				
				response.setContent(bs2);
				response.setCreated(new Date());
				response.setFrom("op_server");
				response.setTo("op_client");
				response.setType(message.getType()+"#"+message.getId());
				server.send(response);
				
			}
		}
		
		public void init()
		{
			addListener("request",new RequestListener(this));
		}

		public static void main(String[] args)
		{
			
			if(args.length!=2)
			{
				System.out.println("command server_ip port");
				System.exit(1);
			}
			String server_ip = args[0];
			String port_str = args[1];	
			int port = Integer.parseInt(port_str);
			MyPrint.print("OpServer begin...",new Exception());
			OpServer op_server = new OpServer(server_ip,port);
			op_server.init();
			op_server.work();
			
			
		}

}
