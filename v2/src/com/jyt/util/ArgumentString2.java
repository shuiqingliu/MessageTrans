package com.jyt.util;

import java.util.HashMap;
import java.util.Map;

public class ArgumentString2 {
	public String formula;
	public String field;
	public boolean match = false;
	
	public Map<String,String> result = new HashMap<String,String>();
	
	public ArgumentString2(String formula,String field)
	{
		this.formula = formula;
		this.field = field;
	}
	
	public int[] find_first_replaced(int start)
	{
		int[] ret = new int[2];
		int state = 1;
		int pos = start;
		while(true)
		{
			if(state==1)
			{
				ret[0] = -1;
				ret[1] = -1;
				if(formula.charAt(pos)=='%')
				{
					state = 2;
					ret[0] = pos;
				}
				if(pos==formula.length()-1)
				{
					break;
				}
				
			}
			else if(state==2)
			{
				if((formula.charAt(pos)<='9')&&(formula.charAt(pos)>='0'))
				{
					state = 3;
					ret[1] = pos;
				}
				else{
					state = 1;
					ret[0] = -1;
					ret[1] = -1;
				}
				if(pos==formula.length()-1)
				{
					break;
				}				
			}
			else if(state==3)
			{
				if((formula.charAt(pos)<='9')&&(formula.charAt(pos)>='0'))
				{
					ret[1] = pos;					
				}
				else{
					state = 4;
					break;
				}
				if(pos==formula.length()-1)
				{
					ret[1] = pos;
					break;
				}				
			}
			pos++;
		}
		return ret;

	}
	
	public int[] find_arr()
	{
		int[] ret = new int[6];
		int[] r1 = find_first_replaced(0);
		if((r1[0]==-1)||(r1[1]==-1))
		{
			ret[0] = -1;
			ret[1] = -1;
			ret[2] = -1;
			ret[3] = -1;
			ret[4] = -1;
			ret[5] = -1;
			return ret;
		}
		if(r1[0] == 0)
		{
			ret[0] = -1;
			ret[1] = -1;
		}
		else{
			ret[0] = 0;
			ret[1] = r1[0]-1;
		}
		ret[1] = r1[0]-1;
		ret[2] = r1[0];
		ret[3] = r1[1];
		
		if(formula.length()-1==r1[1])
		{
			ret[4] = -1;
			ret[5] = -1;
			return ret;
		}
		int[] r2 = find_first_replaced(r1[1]+1);
		if((r2[0]==-1)||(r2[1]==-1))
		{
			ret[4] = r1[1]+1;
			ret[5] = formula.length()-1;
		}
		else{
			ret[4] = r1[1]+1;
			ret[5] = r2[0]-1;
		}
		
		return ret;

	}	
	
	public static void main(String[] args)
	{
		
	}
}
