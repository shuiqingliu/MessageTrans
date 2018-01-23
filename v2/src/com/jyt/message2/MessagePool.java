package com.jyt.message2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jyt.util.NameValue;
/**
 * ����Ϣ���Ʒ��������õ�����Ҫ���ݷ���������С�
 * @author yzq
 *
 */
public class MessagePool {
	
	/**
	 * ��ʾ���е���Ϣ�Ļ���ء�
	 * <p>��һ��map����ʾ��
	 * <p>map��key��ʾʵ������ƣ�map��ֵ��һ����Ϣ���С�
	 * <p>����Ϣ������������ʱ����Ҫ��list��ʼ����ʹ�䲻��Ϊ��ָ�롣
	 */
	public static Map<String,List<Message>> map = new HashMap<String,List<Message>>();
	/**
	 * ��ʾʵ��������Ϣ��
	 * <p>��һ��map����ʾ
	 * <p>����key��ʾʵ������
	 * <p>���е�value������һ��namevalue�Ķ�������ʾһ��ʵ��ľ���������Ϣ��
	 */
	public static Map<String,List<NameValue>> entity_conf_map = new HashMap<String,List<NameValue>>();
	/**
	 * ��ʾϵͳ������Ϣ��
	 * <p>��namevalue��������ʾϵͳ��������Ϣ��
	 * @see MessageServerRMI
	 */
	public static List<NameValue> system_conf = new ArrayList<NameValue>();

}
