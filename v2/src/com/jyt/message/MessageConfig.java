package com.jyt.message;

public class MessageConfig {
	

	/**
	 * ĳ��ʵ��������Ϣ���ȣ�ֵ�ĳ���Ϊһ�����������ַ�����
	 * ���ֵ�ĳ���Ϊ0����ô��ʾ����Ϣ�ĸ���û�����ơ�
	 */
	public static String ENTITY_MAX_MSG_LEN = "Entity_MaxMessageLength";
	/**
	 * �Ƿ�Խ��и��٣�ֵ������Ϊ true,false
	 */
	public static String SYSTEM_TRACE = "System_Trace_On";
	/**
	 * ����Щʵ����и��٣�ֵ������Ϊ [1]entity_name1[2]entity_name2[3]entity_name3
	 */
	public static String TRACED_ENITITIES = "System_Traced_Entity_Names";
	
	/**
	 * �Ϸ���ʵ�������б�
	 */
	public static String[] valid_entity_attributes = new String[] {
		ENTITY_MAX_MSG_LEN,
	};
	/**
	 * �Ϸ���ϵͳ�����б�
	 */
	public static String[] valid_system_attributes = new String[] {
		SYSTEM_TRACE,
		TRACED_ENITITIES,
	};	
	
	public static int port = 1098;
	public static String server_ip = "127.0.0.1";
	public static String server_name = "IMServer";
	public static int MAX_NON_MESSAGE_PERIOD = 60;
	public static int DELTA_PERIOD = 10;
	public static final int MAX_THREAD_COUNT = 100;
 
}
