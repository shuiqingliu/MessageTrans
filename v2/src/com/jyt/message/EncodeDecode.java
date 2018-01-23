package com.jyt.message;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jyt.util.BinEdit;
import com.jyt.util.MyPrint;
import com.jyt.util.NameValue;

public class EncodeDecode {
	
	public static byte[] prepare_head(int tag,int length)
	{
		byte[] bs = new byte[4];
		bs[0] = (byte)(tag/128);
		bs[1] = (byte)(tag%128);
		bs[2] = (byte)(length/128);
		bs[3] = (byte)(length%128);
		return bs;
	}
	public static int get_tag(byte[]bs ,int from)
	{
		int ret = bs[from]*128+bs[from+1];
		return ret;
	}
	
	public static void set_tag(byte[] bs,int new_tag)
	{
		bs[0] = (byte)(new_tag / 128);
		bs[1] = (byte)(new_tag % 128);
	}
	
	public static int get_length(byte[]bs ,int from)
	{
	//	MyPrint.print("bs.length="+bs.length, new Exception());
	//	MyPrint.print("from="+from, new Exception());
		int ret = bs[from+2]*128+bs[from+3];
		return ret;		
	}
	
	public static byte[] get_value(byte[] bs , int from)
	{
		int length = get_length(bs,from);
		byte[] ret = new byte[length];
		for(int i=0;i<length;i++)
		{
			ret[i] = bs[from+i+4];
		}
		return ret;
	}
	
	public static byte[] get_tlv(byte[] bs, int from)
	{
		if(bs.length<from+4)
		{
			return null;
		}
		int length = get_length(bs,from);
		byte[] ret = new byte[length+4];
		for(int i=0;i<ret.length;i++)
		{
			ret[i] = bs[from+i];
		}
		return ret;
	}
	
	//tag = 100
	public static byte[] encode_string(String s){
		String s1 = "";
		if(s!=null)
			s1 = s;
		byte[] v = s1.getBytes();
		int t = 100;
		int l = v.length;
		byte[] bs = prepare_head(t,l);
		byte[] ret = BinEdit.append(bs, v);
		return ret;
	}
	public static String decode_string(byte[] bs)
	{
		byte[] str_bs = get_value(bs,0);
		return new String(str_bs);
	}
	
	//tag = 101
	public static byte[] encode_integer_2(int i)
	{
		int t = 101;
		int l = 2;
		byte[] head = prepare_head(t,l);
		byte[] v = new byte[2];
		v[0] = (byte)(i/128);
		v[1] = (byte)(i%128);
		byte[] ret = BinEdit.append(head, v);
		return ret;
	}
	

	public static int decode_integer_2(byte[] bs)
	{
		int ret = bs[4]*128+bs[5];
		return ret;
	}
	
	//tag = 102
	public static byte[] encode_integer_s(int i)
	{
		int t = 102;
		String s = i+"";
		byte[] v = s.getBytes();
		int l = v.length;
		byte[] head = prepare_head(t,l);
		byte[] ret = BinEdit.append(head, v);
		return ret;
	}
	
	public static int decode_integer_s(byte[] bs)
	{
		byte[] v = get_value(bs,0);
		int ret = 0;
		String s = new String(v);
		if(s.length()==0)
			ret  = 0;
		else{
	//		MyPrint.print("s="+s+"#", new Exception());
		    ret = Integer.parseInt(s);			
		}

		return ret;
		
	}
	
	//tag = 103
	public static byte[] encode_long_2(long i)
	{
		int t = 103;
		int l = 2;
		byte[] head = prepare_head(t,l);
		byte[] v = new byte[2];
		v[0] = (byte)(i/128);
		v[1] = (byte)(i%128);
		byte[] ret = BinEdit.append(head, v);
		return ret;
	}	
	
	public static int decode_long_2(byte[] bs)
	{
		int ret = bs[4]*128+bs[5];
		return ret;
	}
	
	//tag = 104
	public static byte[] encode_long_s(long i)
	{
		int t = 104;
		String s = i+"";
		byte[] v = s.getBytes();
		int l = v.length;
		byte[] head = prepare_head(t,l);
		byte[] ret = BinEdit.append(head, v);
		return ret;
	}
	
	public static long decode_long_s(byte[] bs)
	{
		byte[] v = get_value(bs,0);
		String s = new String(v);
		long ret = Long.parseLong(s);
		return ret;
		
	}
	
	// tag = 105
	public static byte[] encode_datetime(Date d)
	{
		int t = 105;
		long dl = d.getTime();
		String ds = dl+"";
		byte[] v = ds.getBytes();
		int l = v.length;
		byte[] head = prepare_head(t,l);
		byte[] ret = BinEdit.append(head, v);
		return ret;		
	}
	
	public static Date decode_datetime(byte[] bs)
	{
		byte[] v = get_value(bs,0);
		String vs = new String(v);
		long vl = Long.parseLong(vs);
		Date ret = new Date(vl);
		return ret;
	}
	
	//tag = 106
	public static byte[] encode_bytes(byte[] bs)
	{
		int t = 106;
		int l = bs.length;
		byte[] head = prepare_head(t,l);
		byte[] ret = BinEdit.append(head, bs);
		return ret;
	}
	
	public static byte[] decode_bytes(byte[] bs)
	{
		byte[] ret = get_value(bs,0);
		return ret;
	}
	
	//tag = 107
	public static byte[] encode_msg(Message msg)
	{
		byte[] bs = null;
		if(msg!=null)
		{
			byte[] bs_id = encode_string(msg.id);
			byte[] bs_type = encode_string(msg.type);
			byte[] bs_from = encode_string(msg.from);
			byte[] bs_to = encode_string(msg.to);
			byte[] bs_created = encode_datetime(msg.created);
			byte[] bs_content = encode_bytes(msg.content);
			int length = bs_id.length+bs_type.length+bs_from.length+bs_to.length+
					bs_created.length+bs_content.length;
			byte[] head = prepare_head(107,length);
			bs = BinEdit.append(head, bs_id);
			bs = BinEdit.append(bs,bs_type);
			bs = BinEdit.append(bs,bs_from);
			bs = BinEdit.append(bs,bs_to);
			bs = BinEdit.append(bs,bs_created);
			bs = BinEdit.append(bs,bs_content);		
		}
		else{
			byte[] head = prepare_head(107,0);
			bs = head;
		}

		return bs;
	}
	
	public static Message decode_msg(byte[] bs)
	{
		Message ret = null;
		int length = get_length(bs,0);
	//	MyPrint.print("bs.length="+bs.length, new Exception());
	//	BinEdit.printBytes(bs);
		if(length==0)
		{
			
		}
		else{
			byte[] value = get_value(bs,0);
	//		MyPrint.print("value.length="+value.length, new Exception());
			if(value.length>4){
				int offset = 0;
				byte[] bs_id = get_tlv(value,offset);
				offset = offset+bs_id.length;
				byte[] bs_type = get_tlv(value,offset);
				offset = offset+bs_type.length;
				byte[] bs_from = get_tlv(value,offset);
				offset = offset+bs_from.length;
				byte[] bs_to = get_tlv(value,offset);
				offset = offset+bs_to.length;		
				byte[] bs_created = get_tlv(value,offset);
				offset = offset+bs_created.length;
				byte[] bs_content = get_tlv(value,offset);
				
				String id = decode_string(bs_id);
				String type = decode_string(bs_type);
				String from = decode_string(bs_from);
				String to = decode_string(bs_to);
				Date created = decode_datetime(bs_created);
				byte[] content = decode_bytes(bs_content);
				ret = new Message();
				ret.setId(id);
				ret.setType(type);
				ret.setFrom(from);
				ret.setTo(to);
				ret.setContent(content);
				ret.setCreated(created);				
			}
		
		}

		return ret;
		
	}
	
	//tag = 108 List<String>
	
	public static byte[] encode_list_string(List<String> list)
	{
		int length = 0;
		byte[] total = null;
		for(String s:list)
		{
			byte[]  bs = encode_string(s);
			total = BinEdit.append(total, bs);
			length = length+bs.length;
		}
		byte[] head = prepare_head(108,length);
		byte[] ret = BinEdit.append(head, total);
		return ret;
	}
	
	public static List<String> decode_list_string(byte[] bs)
	{
		List<String> ret = new ArrayList<String>();
		int length = get_length(bs,0);
		byte[] value = get_value(bs,0);
		int offset = 0;
		while(true)
		{
			byte[] tlv = get_tlv(value,offset);
			String s = decode_string(tlv);
			ret.add(s);
			offset = offset+tlv.length;
			if(offset>=length)
				break;
		}
		return ret;
	}
	
	
	// tag=109 NameValue
	
	public static byte[] encode_namevalue(NameValue nv)
	{
		byte[] ret = null;
		byte[] bs_name = encode_string(nv.name);
		byte[] bs_value = encode_string(nv.value);
		int length = bs_name.length+bs_value.length;
		byte[] bs_head = prepare_head(109,length);
		ret = BinEdit.append(bs_head, bs_name);
		ret = BinEdit.append(ret, bs_value);
		return ret;
	}
	
	
	public static NameValue decode_namevalue(byte[] bs)
	{
		
		byte[] bs_name = get_tlv(bs,4);
		byte[] bs_value = get_tlv(bs,bs_name.length+4);
		String name = decode_string(bs_name);
		String value = decode_string(bs_value);
		return new NameValue(name,value);
	}
	
	// tag=110 List<NameValue>
	public static byte[] encode_list_namevalue(List<NameValue> list)
	{
		int length = 0;
		byte[] total = null;
		for(NameValue s:list)
		{
			byte[]  bs = encode_namevalue(s);
			total = BinEdit.append(total, bs);
			length = length+bs.length;
		}
		byte[] head = prepare_head(108,length);
		byte[] ret = BinEdit.append(head, total);
		return ret;
	}
	
	public static List<NameValue> decode_list_namevalue(byte[] bs)
	{
		List<NameValue> ret = null;
		int length = get_length(bs,0);
		byte[] value = get_value(bs,0);
		int offset = 0;
		if((value!=null)&&(value.length!=0))
		{
			ret = new ArrayList<NameValue>();
			while(true)
			{
				byte[] tlv = get_tlv(value,offset);
				NameValue s = decode_namevalue(tlv);
				ret.add(s);
				offset = offset+tlv.length;
				if(offset>=length)
					break;
			}
			
		}
		return ret;
	}
	
	// tag=111 List<Message>
	public static byte[] encode_list_msg(List<Message> list)
	{
		int length = 0;
		byte[] total = null;
		for(Message s:list)
		{
			byte[]  bs = encode_msg(s);
			total = BinEdit.append(total, bs);
			length = length+bs.length;
		}
		byte[] head = prepare_head(108,length);
		byte[] ret = BinEdit.append(head, total);
		return ret;
	}
	
	public static List<Message> decode_list_msg(byte[] bs)
	{
		List<Message> ret = null;
		int length = get_length(bs,0);
		byte[] value = get_value(bs,0);
		int offset = 0;
		if((value!=null)&&(value.length!=0))
		{
			
			MyPrint.print("offset="+offset+"", new Exception());
			MyPrint.print("value.length="+value.length, new Exception());
			BinEdit.printBytes(value);
			ret = new ArrayList<Message>();
			while(true)
			{
				byte[] tlv = get_tlv(value,offset);
				Message s = decode_msg(tlv);
				ret.add(s);
				offset = offset+tlv.length;
				if(offset>=length)
					break;
			}
		}
		return ret;
	}

	
	public static void main1(String[] args)
	{
		String s = null;
		byte[] bs = encode_string(s);
		BinEdit.printBytes(bs);
		String s2 = decode_string(bs);
		System.out.print("##==>"+s2+"<==##");
	}
	
	public static void main2(String[] args)
	{
		int x = 16383;
		byte[] bs = encode_integer_2(x);
		BinEdit.printBytes(bs);
		int y = decode_integer_2(bs);
		System.out.println(y);
	
	}
	
	public static void main3(String[] args)
	{
		int x = 65536;
		byte[] bs = encode_integer_s(x);
		BinEdit.printBytes(bs);
		int y = decode_integer_s(bs);
		System.out.println(y);
	
	}
	
	public static void main4(String[] args)
	{
		long x = 16383;
		byte[] bs = encode_long_2(x);
		BinEdit.printBytes(bs);
		long y = decode_long_2(bs);
		System.out.println(y);
	
	}
	
	public static void main5(String[] args)
	{
		long x = 16383*65536;
		System.out.println(x);
		byte[] bs = encode_long_s(x);
		BinEdit.printBytes(bs);
		long y = decode_long_s(bs);
		System.out.println(y);
	
	}
	
	public static void main6(String[] args)
	{
		Date d = new Date();
		System.out.println(d.toString());
		byte[] bs = encode_datetime(d);
		BinEdit.printBytes(bs);
		Date d2 = decode_datetime(bs);
		System.out.println(d2);
	
	}
	
	
	public static void main7(String[] args)
	{
		byte[] bs = "hello".getBytes();
		
		byte[] v = encode_bytes(bs);
		BinEdit.printBytes(v);
		byte[] bs2 = decode_bytes(v);
		BinEdit.printBytes(bs2);
	
	}
	
	public static void main8(String[] args)
	{
		Message msg = new Message();
		msg.setId("12345");
		msg.setType("type");
		msg.setFrom("from");
		msg.setTo("to");
		msg.setCreated(new Date());
		msg.setContent("hello".getBytes());
		byte[] bs_msg = encode_msg(msg);
		Message msg2 = decode_msg(bs_msg);
		MyPrint.print(msg2.toString(),new Exception());
	}
	
	
	public static void main9(String[] args)
	{
		List<String> list = new ArrayList<String>();
		list.add("yzq");
		list.add("ym");
		list.add("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		list.add("ypy");
		byte[] bs = encode_list_string(list);
		List<String> list2 = decode_list_string(bs);
		MyPrint.print(list2.toString(), new Exception());
	}
	
	public static void main10(String[] args)
	{
		NameValue nv = new NameValue("height","20");
		byte[] bs = encode_namevalue(nv);
		NameValue nv2 = decode_namevalue(bs);
		MyPrint.print(nv2.toString(), new Exception());
	}
	
	public static void main11(String[] args)
	{
		NameValue nv1 = new NameValue("father","yzq");
		NameValue nv2 = new NameValue("mother","ym");
		NameValue nv3 = new NameValue("daughter","ypy");
		List<NameValue> list = new ArrayList<NameValue>();
		list.add(nv1);
		list.add(nv2);
		list.add(nv3);
		byte[] bs = encode_list_namevalue(list);
		List<NameValue> list2 = decode_list_namevalue(bs);
		MyPrint.print(list2.toString(), new Exception());
		
		
	}
	
	public static void main12(String[] args)
	{
		String s = "hello";
		byte[] bs = encode_string(s);
		MyPrint.print("", new Exception());
		BinEdit.printBytes(bs);
		set_tag(bs,17);
		MyPrint.print("", new Exception());
		BinEdit.printBytes(bs);
	}
	
	
	public static void main13(String[] args)
	{
		Message msg1 = new Message();
		msg1.setId("id1");
		msg1.setType("type1");
		msg1.setFrom("from1");
		msg1.setTo("to1");
		msg1.setCreated(new Date());
		msg1.setContent("hello1".getBytes());
		Message msg2 = new Message();
		msg2.setId("id2");
		msg2.setType("type2");
		msg2.setFrom("from2");
		msg2.setTo("to2");
		msg2.setCreated(new Date());
		msg2.setContent("hello2".getBytes());
		List<Message> list = new ArrayList<Message>();
		list.add(msg1);
		list.add(msg2);
		byte[] bs_list_msg = encode_list_msg(list);
		List<Message> list2 = decode_list_msg(bs_list_msg);
		MyPrint.print(list2.toString(),new Exception());
	}
	
	public static void main14(String[] args)
	{
		Message msg = null;
		byte[] bs_msg = encode_msg(msg);
		Message msg2 = decode_msg(bs_msg);
		System.out.println(msg2);
	}
	
	public static void main(String[] args)
	{
	     main14(args);
	}
	

}
