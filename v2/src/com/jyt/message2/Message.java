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
 * @author ������
 * ˵������������һ����Ϣ��
 */
public class Message implements Serializable {
	/** ��Ϣ��Ψһ��ʶ */
	public String id;
	/** ��Ϣ������ */
	public String type;
	/** ��Ϣ������ */
	public byte[] content;
	/** ��Ϣ�Ĵ���ʱ�� */
	public Date created;
	/** ��Ϣ��Դʵ�� */
	public String from;
	/** ��Ϣ��Ŀ��ʵ�� */
	public String to;
	
	/** ��Ϣ���캯��֮һ */
	public Message()
	{
		this.id = UniqueStringGenerator.getUniqueString();
		this.created = new Date();
		
	}
	
	/** 
	 * ��Ϣ���캯��֮�� 
	 * <p>����id���ò���Ψһ����ĺ�����ʵ�֡�
	 * @see UniqueStringGenerator#getUniqueString()
	 * @param from Դʵ��
	 * @param to Ŀ��ʵ��
	 * @param type ��Ϣ������
	 * @param bs ��Ϣ�����ݣ��Ƕ���������
	 * @return ����һ����Ϣ������ʱ��Ϊ��ʱ������Ϣ��ʱ��
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
	 * ��Ϣ��ʾ��ʽ֮����
	 * ����һ������ĸ�ʽ����Ϣ��ӡ��Ϊһ���ַ�������Ŀ������һ���ڽ���Ϣ��������ʾ������
	 * <p>�������ַ�ʽ��ʾ��Ϣ����Ϣ���ݵĶ��󽫱���ӡ������
	 * @return ��Ϣ�ַ�������ʾ��ʽ
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
	 * ����һ������ĸ�ʽ����Ϣ��ӡ��Ϊһ���ַ�������Ŀ������һ���ڽ���Ϣ��������ʾ������
	 *  <p>�������ַ�ʽ��ʾ��Ϣ����Ϣ���ݵĶ��󽫲�����ӡ������
	 * @return ��Ϣ�ַ�������ʾ��ʽ
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
	 * ����Ϣ�����������Ӷ��������ڴ��ϲ���ǣ����
	 * @param msg ԭʼ��Ϣ
	 * @return ����֮�����Ϣ
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
	 * �ڴ�ӡ�ĵط���ʾ��Ϣ���ݡ�
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
