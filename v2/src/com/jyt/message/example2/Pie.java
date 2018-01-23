package com.jyt.message.example2;


public class Pie {
	public static void main(String[] args)
	{
		int total = 1000*1000*50;
		int count = 0;
		for(int i=0;i<total;i++)
		{
			double x = Math.random();
			double y = Math.random();
			double r2 = (x-0.5)*(x-0.5)+(y-0.5)*(y-0.5);
			if(r2<=0.25)
			{
				count++;
			}			
		}
		double d1 = (double)count;
		double d2 = (double)total;
		double result = d1/d2*4;
		System.out.println("pie is "+result);
		
	}

}
