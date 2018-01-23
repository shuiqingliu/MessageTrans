package com.jyt.message.example8;

import java.io.Serializable;


public class OpContent implements Serializable {
	public String a1;
	public String a2;
	public String result;
	public OpContent(int i1,int i2)
	{
		a1 = i1+"";
		a2 = i2+"";
	}
	public void compute()
	{
		int inta1 = Integer.parseInt(a1);
		int inta2 = Integer.parseInt(a2);
		int intresult = inta1+inta2;
		result = intresult+"";
	}
}
