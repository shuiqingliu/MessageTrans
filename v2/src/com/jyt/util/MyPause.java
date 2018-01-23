package com.jyt.util;

public class MyPause {
	public static void main(String[] args)
	{
		if (args.length != 1) {
			System.out.println("MyPause <seconds>");
			System.exit(1);
		}
		String puase_second = args[0];
		try{
			int pause = Integer.parseInt(puase_second);
			System.out.println("系统暂停开始。。。");
			Thread.sleep(pause*1000);
			System.out.println("系统暂停开结束");
			System.exit(0);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
}
