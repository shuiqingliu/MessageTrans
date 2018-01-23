package com.jyt.message2;

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
	
	public static final int FIRST_LIFE_TIME = 60;
	public static final int SECOND_LIFE_TIME = 90;
	
	public static final int MAX_THREAD_COUNT = 100;

 
}
