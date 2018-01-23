package com.jyt.util;

public class CountTime {
	
	private long start = -1;
	private String title = null;
	public CountTime(String title)
	{
	   start = System.currentTimeMillis();
	   this.title = title;
	 //  System.out.println("==="+title+"====>Begin Count Time...");
	}
	
	public void print()
	{
		
	   System.out.println("==="+title+"====>Time cost:"+(System.currentTimeMillis()-start)+"ms");
		
	}
	
	public String sprint()
	{
		   String s = ("==="+title+"====>Time cost:"+(System.currentTimeMillis()-start)+"ms");
		   return s;

	}
	

	public long getCost()
	{
		
		long cost=System.currentTimeMillis()-start;
		return cost;
		
	}
	public static void main(String[] args)
	{
		CountTime ct = new CountTime("hello");
		for(int i=0;i<10000;i++)
		{
			for(int j=0;j<10000;j++)
			{
				int a = 200;
				
			}
		}
		long l = ct.getCost();
		System.out.println(l);
	}	

}
