package com.jyt.message2;

import java.io.Serializable;

import com.jyt.util.MySerializable;

public class TLV {
	
	private static String pad(String s)
	{
		String ret = "";
		if(s.length()>10)
			ret = s.substring(0,10);
		else{
			StringBuffer sb = new StringBuffer();
			sb.append(s);
			for(int i=0;i<10-s.length();i++)
			{
				sb.append(" ");
			}
			ret = sb.toString();
		}
		return ret;
	}
	
	public static byte[] int_bytes(int n)
	{
		String s = n+"";
		String s2 = pad(s);
		byte[] ret = s2.getBytes();
		return ret;
	}
	
	public static int bytes_int(byte[] bs)
	{
		String s1 = new String(bs).trim();
		int ret = Integer.parseInt(s1);
		return ret;
	}
	
	public int t;
	public int l;
	public byte[] v;
	
	public TLV(int t,int l,byte[] v)
	{
		this.t = t;
		this.l = l;
		this.v = v;
	}
	
	public TLV(int t,Serializable serial)
	{
		this.t = t;
		this.v = MySerializable.object_bytes(serial);
		this.l = this.v.length;
	}

	
	public byte[] encode()
	{
		byte[] b1 = pad(t+"").getBytes();
		byte[] b2 = pad(l+"").getBytes();
		byte[] ret = new byte[20+l];
		for(int i=0;i<10;i++)
		{
			ret[i] = b1[i];
		}
		for(int i=0;i<10;i++)
		{
			ret[i+10] = b2[i];
		}	
		for(int i=0;i<l;i++)
		{
			ret[i+20] = v[i];
		}
		return ret;
	}
	
	public static TLV decode(byte[] bs)
	{
		byte[] b1 = new byte[10];
		for(int i=0;i<10;i++)
			b1[i] = bs[i];
		int tt = bytes_int(b1);
		byte[] b2 = new byte[10];
		for(int i=0;i<10;i++)
			b2[i] = bs[10+i];
		int ll = bytes_int(b2);
		byte[] b3 = new byte[ll];
		for(int i=0;i<ll;i++)
		{
			b3[i] = bs[20+i];
		}

		TLV ret = new TLV(tt,ll,b3);
		return ret;
	}
	
	public String toString()
	{
		String ts = t+"";
		String ls = l+"";
		String vs = new String(v);
		String ret = "["+ts+"]"+"["+ls+"]"+vs;
		return ret;
	}
	
	public String toByteString()
	{
		String ret = "";
		for(int i=0;i<l;i++)
		{
			byte b = v[i];
			int n = (int)b;
			String s = Integer.toHexString(n);
			ret = ret+s;
		}
		return ret;
		
	}
	
	public static void main(String[] args)
	{
		String info = "hello";
		TLV tlv = new TLV(1,info.getBytes().length,info.getBytes());
		String s = tlv.toString();
		System.out.println(s);
		byte[] bs1 = tlv.encode();
		System.out.println(new String(bs1));
		System.out.println(bs1.length);
		TLV tlv2 = decode(bs1);
		System.out.println(tlv2.toString());
			
	}

}
