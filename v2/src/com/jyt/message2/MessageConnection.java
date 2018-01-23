package com.jyt.message2;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.jyt.util.CountTime;

public class MessageConnection {

	private static MessageConnection available = null;

	public String ip;
	public int port;
	public InputStream input_stream = null;
	public OutputStream output_stream = null;
	public Socket socket = null;
	public CountTime ct = null;
	public MessageConnection(String ip,int port)
	{
		try{
			this.ip = ip;
			this.port = port;
			socket = new Socket(ip,port);
			input_stream = socket.getInputStream();
			output_stream = socket.getOutputStream();		
			ct = new CountTime("connection");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public boolean out_of_date()
	{
		boolean ret = false;
		if(ct.getCost()>MessageConfig.FIRST_LIFE_TIME*1000)
		{
			ret = true;
		}
		return ret;
	}
	
	public static MessageConnection get_available_connection(String ip,int port)
	{
		MessageConnection ret = available;
		if(available ==null)
		{
			ret = new MessageConnection(ip,port);
			available = ret;
		}
		else if(available.out_of_date())
		{
			try{
				available.input_stream.close();
				available.output_stream.close();
				available.socket.close();				
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			ret = new MessageConnection(ip,port);
			available = ret;
		}
		return ret;
	}

}
