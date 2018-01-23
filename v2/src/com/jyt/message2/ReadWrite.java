package com.jyt.message2;

import java.io.InputStream;
import java.io.OutputStream;

public class ReadWrite {
	public static void send(TLV tlv,OutputStream os)
	{
		byte[] bs = tlv.encode();
		try{
			os.write(bs);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static byte[] read_n_bytes(InputStream is,int max) throws Exception
	{
		byte[] bs = new byte[max];
		int count = 0;
		int off = 0;
		int len = max;
		while(true)
		{	
			int n = is.read(bs, off, len);
			if(count==max)
				break;
			else{
				count = count+n;
				off = off+n;
				len = len-n;
			}
		}	
		return bs;
	}
	
	public static TLV read(InputStream is)
	{
		TLV ret = null;
		try{
			byte[] bs_t = read_n_bytes(is,10);
			byte[] bs_l = read_n_bytes(is,10);
			int t = TLV.bytes_int(bs_t);
			int l = TLV.bytes_int(bs_l);
			byte[] bs_v = read_n_bytes(is,l);
			ret = new TLV(t,l,bs_v);			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return ret;
	}

}
