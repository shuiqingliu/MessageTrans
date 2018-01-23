package com.jyt.message.example4;

import com.jyt.message.Message;
import com.jyt.message.MessageServerTcpClient;
import com.jyt.util.MyPrint;
import com.jyt.util.MySerializable;

import junit.framework.TestCase;

public class MessageTester extends TestCase {
	public MessageTester(String name) {
		super(name);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(MessageTester.class);
	}

	public void setUp() {

	}

	public void tearDown() {
	}

	public void test_01()
	{
		for(int i=0;i<10;i++)
		{
			byte[] bs = MySerializable.object_bytes("nihao");
			Message m = new Message("yzq","ym","hello",bs);
			String s = m.toString();
			MyPrint.print(s,new Exception());
			
		}
	}
	
	public void _test_02()
	{
		MessageServerTcpClient yzq = new MessageServerTcpClient("127.0.0.1","yzq");
		yzq.register();
		MessageServerTcpClient ym = new MessageServerTcpClient("127.0.0.1","ym");
		ym.register();
		Message hello = new Message("yzq","ym","type","nihao".getBytes());
		yzq.send(hello);
		yzq.print_all_entity();
		Message m = ym.receive();
		m.print_title(new Exception());
		ym.clear();
		ym.print_all_entity();
	
		
	}

}


