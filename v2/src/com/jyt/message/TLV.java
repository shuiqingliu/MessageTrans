package com.jyt.message;


import com.jyt.util.ArgumentString;
import com.jyt.util.BinEdit;
import com.jyt.util.MyPrint;

public class TLV {
	public long tag;
	public long length;
	public byte[] value;
	public TLV(long tag,long length,byte[] value)
	{
		this.tag = tag;
		this.length = length;
		this.value = value;
	}
	
	public byte[] encode(){
		int value_len = 0;
		if(value!=null)
			value_len = value.length;
		byte[] bs = new byte[value_len+4];
		bs[0] = (byte)(tag/128);
		bs[1] = (byte)(tag%128);
		bs[2] = (byte)(length/128);
		bs[3] = (byte)(length%128);
		for(int i=0;i<value_len;i++)
		{
			bs[i+4] = value[i];
		}
		return bs;
	}
	
	public static TLV decode(byte[] bs)
	{
		long tag = bs[0]*128+bs[1];
		long l = bs[2]*128+bs[2];
		byte[] value = new byte[(int)l];
		for(int i=0;i<l;i++)
			value[i]= bs[i+4];
		TLV ret = new TLV(tag,l,value);
		return ret;
	}
	
	public void print()
	{
		MyPrint.print("===============", new Exception());
		String field = "tag(%1)length(%2)value(%3)";
		String tag_str = tag+"";
		String length_str = length+"";
		String value_str = "";
		if(value!=null)
			value_str = BinEdit.toByteString(value);
		String result = ArgumentString.replace(field, new String[]{tag_str,length_str,value_str});
		System.out.println(result);
	}
	
	public static void main(String[] args)
	{
		String v = "hello";
		TLV tlv = new TLV(10,v.getBytes().length,v.getBytes());
		tlv.print();
		byte[] bs = tlv.encode();
		System.out.println(BinEdit.toByteString(bs));
		TLV ret = decode(bs);
		ret.print();
	}
	

}
