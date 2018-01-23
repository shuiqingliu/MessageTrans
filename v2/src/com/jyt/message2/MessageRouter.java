package com.jyt.message2;

import com.jyt.util.MyPrint;

/**
 * 表示一个消息路由器实体。
 * <p>一个消息路由器会连接两个服务器。同时也具有两个客户端。
 * <p>两个服务器为server1,server2。
 * <p>两个客户端为client1,client2。
 * @author yzq
 *
 */
public class MessageRouter implements Runnable {
	public String server1_ip;
	public int  port1;
	public String server2_ip;
	public int  port2;
	public String server1_name;
	public String server2_name;
	public MessageServerClient client1,client2;
	
	public MessageRouter(String server1_ip,int port1,String server1_name,String server2_ip,int port2,String server2_name)
	{
		this.server1_ip = server1_ip;
		this.port1 = port1;
		this.server2_ip = server2_ip;
		this.port2 = port2;
		this.server1_name = server1_name;
		this.server2_name = server2_name;
		this.client1 = new MessageServerClient(server1_ip,port1,server2_name);
		this.client2 = new MessageServerClient(server2_ip,port2,server1_name);
	}

	
	public  String[] get_scoped_names(String name)
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
	
	public String get_name_from_scoped_name(String scoped_name)
	{
		String[] ss = get_scoped_names(scoped_name);
		String ret = ss[0];
		return ret;
	}
	
	public void run()
	{
		while(true)
		{
			Message m1 = client1.receive();
			Message m2 = client2.receive();
			if(m1!=null)
			{
				MyPrint.print("<==="+m1.toString(),new Exception());
				String from = m1.getFrom();
				String new_from = from+"@"+server1_name;
				String to = m1.getTo();
				String new_to = get_name_from_scoped_name(to);
				m1.setFrom(new_from);
				m1.setTo(new_to);
				client2.send(m1);
				MyPrint.print("===>"+m1.toString(),new Exception());
			}
			if(m2!=null)
			{
				MyPrint.print("<==="+m2.toString(),new Exception());
				String from = m2.getFrom();
				String new_from = from+"@"+server2_name;
				String to = m2.getTo();
				String new_to = get_name_from_scoped_name(to);
				m2.setFrom(new_from);
				m2.setTo(new_to);
				client1.send(m2);
				MyPrint.print("===>"+m2.toString(),new Exception());
			}			
		}
	}	
	
	
	public void work()
	{
		Thread t = new Thread(this);
		t.start();
	}
	
	public static void main(String[] args)
	{
		if(args.length!=6)
		{
			System.out.println("command server1_ip port1 server1_name server2_ip port2 server2_name");
			System.exit(1);
		}
		String server1_ip = args[0];
		String port1_str = args[1];
		int port1 = Integer.parseInt(port1_str);
		String server1_name = args[2];		
		String server2_ip = args[3];
		String port2_str = args[4];
		int port2 = Integer.parseInt(port2_str);
		String server2_name = args[5];	
		MessageRouter r = new MessageRouter(server1_ip,port1,server1_name,server2_ip,port2,server2_name);
		r.work();
	}

}
