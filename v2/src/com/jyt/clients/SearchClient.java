package com.jyt.clients;

import com.jyt.message.Message;
import com.jyt.message.MessageConfig;
import com.jyt.message.MessageListener;
import com.jyt.message.MessageServerTcpClient;
import com.jyt.util.ArgumentString;
import com.jyt.util.MyDate;
import com.jyt.util.MySerializable;

public class SearchClient extends MessageServerTcpClient{
	
	public SearchClient(String server_ip, String server_name) {
		super(server_ip, server_name,"sys_search");
		addListener("searchRecords",new ResponseListener(this));
	}

	public class ResponseListener implements MessageListener
	{
		SearchClient client = null;

		public ResponseListener(SearchClient client) {
			this.client = client;
		}
		public void messagePerformed(Message message)
		{
			String field = "**************\n%1 from %2 \n%3\n**************";
			String type=message.getType();
			String from = message.getFrom();
			String time_str = MyDate.f2(message.getCreated());
			String content = (String)MySerializable.bytes_object(message.getContent());
			String[] ss = new String[] { time_str, from, content };
			String result = ArgumentString.replace(field, ss);
			System.out.println(result);
			if(type.equals("searchRecords")){
				//TODO 从数据库中查找聊天记录并返回
			}
		}
	}
	
	public static void main(String[] args) {
		SearchClient client=new SearchClient(MessageConfig.server_ip,MessageConfig.server_name);
		client.work();
	}
}
