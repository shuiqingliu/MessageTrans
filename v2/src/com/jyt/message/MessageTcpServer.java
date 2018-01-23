package com.jyt.message;

import java.net.ServerSocket;
import java.net.Socket;


import com.jyt.util.ArgumentString;
import com.jyt.util.MyPrint;

public class MessageTcpServer {
	public MessageTcpServer(int port)
	{
		ServerSocket server = null;
		Socket theSocket = null;
		
		try{
			server = new ServerSocket(port);
			MessageTcpServerWorker worker;
			Thread thread;
			while(true)
			{
				theSocket = server.accept();
				worker = new MessageTcpServerWorker(theSocket);
				thread = new Thread(worker);
				thread.start();

			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		finally{
			try{
				server.close();
			}
			catch(Exception e){}
		}
	}
	
	public static void main(String[] args)
	{
		try
		{
	//		args = ArgumentProcess.get_args(MessageServer.class, null);
			Thread.currentThread().setName("main");
			if(args.length!=0)
			{
				System.out.println("参数错误:"+args.length);
				System.exit(1);
			}
	

			
	
	
			MessageTcpServer tcp_server = new MessageTcpServer(MessageConfig.port);


			String info_f = "服務器%1（端口%2）已經啟動...";
			String[] info_ss = new String[]{MessageConfig.server_name,MessageConfig.port+""};
			String info = ArgumentString.replace(info_f,info_ss);
			MyPrint.print(info,new Exception());
	//		new JMemoryDemo(); 
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}	

}
