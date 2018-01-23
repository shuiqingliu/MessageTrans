package com.jyt.message2;

import java.io.Serializable;
import java.util.Date;

import com.jyt.util.ArgumentString;
import com.jyt.util.MyDate;
import com.jyt.util.MyPrint;
import com.jyt.util.MySerializable;
import com.jyt.util.UniqueStringGenerator;

/**
 * 
 * @author 杨正球
 * 说明：用来定义一个消息。
 */
public class Message implements Serializable {
	/** 消息的唯一标识 */
	public String id;
	/** 消息的类型 */
	public String type;
	/** 消息的内容 */
	public byte[] content;
	/** 消息的创建时间 */
	public Date created;
	/** 消息的源实体 */
	public String from;
	/** 消息的目的实体 */
	public String to;
	
	/** 消息构造函数之一 */
	public Message()
	{
		this.id = UniqueStringGenerator.getUniqueString();
		this.created = new Date();
		
	}
	
	/** 
	 * 消息构造函数之二 
	 * <p>其中id采用产生唯一号码的函数来实现。
	 * @see UniqueStringGenerator#getUniqueString()
	 * @param from 源实体
	 * @param to 目的实体
	 * @param type 消息的类型
	 * @param bs 消息的内容，是二进制码流
	 * @return 返回一个消息。其中时间为当时产生消息的时间
	 * */	
	public Message(String from,String to,String type,byte[] bs){
		this.id = UniqueStringGenerator.getUniqueString();
		this.from = from;
		this.type = type;
		this.to = to;
		this.content = bs;
		this.created = new Date();
	}
	
	/**
	 * 消息显示方式之二。
	 * 按照一种特殊的格式将消息打印成为一个字符串。其目的是在一行内将消息的内容显示出来。
	 * <p>按照这种方式显示消息，消息内容的对象将被打印出来。
	 * @return 消息字符串的显示方式
	 */
	public String toString2()
	{
		String field = "type(%2)created(%3)from(%4)to(%5)content(%6)id(%1)";
		String date_str = MyDate.f2(getCreated());
		byte[] content = getContent();
		Serializable obj = MySerializable.bytes_object(content);
		String str = MySerializable.call_tostring(obj);
		String[] ss = new String[]{id,type,date_str,from,to,str};
		String result = ArgumentString.replace(field,ss);
		return result;
		
	}
	/**
	 * 按照一种特殊的格式将消息打印成为一个字符串。其目的是在一行内将消息的内容显示出来。
	 *  <p>按照这种方式显示消息，消息内容的对象将不被打印出来。
	 * @return 消息字符串的显示方式
	 */	
	public String toString()
	{
		String field = "type(%2)created(%3)from(%4)to(%5)content(%6)id(%1)";
		String date_str = MyDate.f2(getCreated());
		byte[] content = getContent();
		int length = 0;
		if(content!=null)
			length = content.length;
		String[] ss = new String[]{id,type,date_str,from,to,length+""};
		String result = ArgumentString.replace(field,ss);
		return result;		
	}

	/**
	 * 将消息拷贝出来。从而避免在内存上产生牵扯。
	 * @param msg 原始消息
	 * @return 拷贝之后的消息
	 */
	public static Message copy(Message msg)
	{
		Message ret = new Message();
			
		byte[] bs = new byte[0];
		if(msg.getContent()!=null)
		{
			bs = new byte[msg.getContent().length];
			for(int i=0;i<bs.length;i++)
			{
				bs[i] = msg.getContent()[i];
			}
			
		}
		ret.setContent(bs);
		long l = msg.getCreated().getTime();
		ret.setCreated(new Date(l));
		ret.setFrom(new String(msg.from));
		ret.setId(new String(msg.id));
		ret.setTo(new String(msg.to));
		ret.setType(new String(msg.type));
		msg = null;
		return ret;
	}
	
	/**
	 * 在打印的地方显示消息内容。
	 * @param e
	 */
	public void print_title(Exception e)
	{
		String field = "id(%1)type(%2)path(%3)created(%4)from(%5)to(%6)";
		String date_str = MyDate.f2(getCreated());
		String[] ss = new String[]{id,type,date_str,from,to};
		String result = ArgumentString.replace(field,ss);
		MyPrint.print(result,e);
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public byte[] getContent() {
		return content;
	}
	public void setContent(byte[] content) {
		this.content = content;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}

}
