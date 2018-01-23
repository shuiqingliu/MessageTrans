package com.jyt.message2;

import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import com.jyt.util.DesUtils;
import com.jyt.util.MyDate;
import com.jyt.util.MyFile;
import com.jyt.util.MyNet;
import com.jyt.util.MyPrint;
import com.jyt.util.MyString;

public class MessageServer {
	
	
	public static void process_license() throws Exception
	{
		String license_info = MyFile.readfile(new File("license.dat"));
		if(license_info==null)
		{
			System.out.println("缺少license文件！");
			System.exit(1);
		}
		int pos = license_info.indexOf("$$$");
		license_info = license_info.substring(0, pos);
		MyPrint.print(license_info,new Exception());
		String license = new DesUtils("leemenz").decrypt(license_info);
		System.out.println(license);
		String[] arr = MyString.str_to_arr(license);
		boolean approve = false;
		if(arr.length==4)
		{
			String user = arr[0];
			String pwd = arr[1];
			String ip = arr[2];
			String until = arr[3];
			String local_ip = MyNet.get_local_ip();
			String current_date = MyDate.f1(new Date());
			if(local_ip.equals(ip)){
				if(current_date.compareTo(until)<=0)
				{
					approve = true;
				}
			}
		}
		
		if(approve==false)
		{
			System.out.println("您没有权限使用该款软件，再见！");
			System.exit(1);
		}		
	}
	
	public static void main(String[] args)
	{
		try
		{
	//		args = ArgumentProcess.get_args(MessageServer.class, null);
			Thread.currentThread().setName("main");
			if(args.length!=1)
			{
				System.out.println("参数错误");
				System.exit(1);
			}
			String port_str = args[0];
			int port = Integer.parseInt(port_str);
			ServerSocket server = new ServerSocket(port);
			MessageServerImpl worker;
			Thread thread;
			while(true)
			{
				Socket theSocket = server.accept();
				MyPrint.print(theSocket.toString(), new Exception());
				worker = new MessageServerImpl(theSocket);
				thread = new Thread(worker);
				thread.start();
				MyPrint.print(thread.toString(), new Exception());
			}			

	//		new JMemoryDemo(); 
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}	
}
