package com.jyt.message2;

public class MessageConfig {
	

	/**
	 * 某个实体的最大消息长度，值的长度为一个整数，用字符串表达。
	 * 如果值的长度为0，那么表示对消息的个数没有限制。
	 */
	public static String ENTITY_MAX_MSG_LEN = "Entity_MaxMessageLength";
	/**
	 * 是否对进行跟踪，值的类型为 true,false
	 */
	public static String SYSTEM_TRACE = "System_Trace_On";
	/**
	 * 对那些实体进行跟踪，值的类型为 [1]entity_name1[2]entity_name2[3]entity_name3
	 */
	public static String TRACED_ENITITIES = "System_Traced_Entity_Names";
	
	/**
	 * 合法的实体属性列表
	 */
	public static String[] valid_entity_attributes = new String[] {
		ENTITY_MAX_MSG_LEN,
	};
	/**
	 * 合法的系统属性列表
	 */
	public static String[] valid_system_attributes = new String[] {
		SYSTEM_TRACE,
		TRACED_ENITITIES,
	};	
	
	public static final int FIRST_LIFE_TIME = 60;
	public static final int SECOND_LIFE_TIME = 90;
	
	public static final int MAX_THREAD_COUNT = 100;

 
}
