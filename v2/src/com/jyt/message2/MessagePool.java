package com.jyt.message2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jyt.util.NameValue;
/**
 * 将消息机制服务器所用到的主要数据放在这个类中。
 * @author yzq
 *
 */
public class MessagePool {
	
	/**
	 * 表示所有的消息的缓冲池。
	 * <p>用一个map来表示。
	 * <p>map的key表示实体的名称，map的值是一个消息队列。
	 * <p>在消息服务器启动的时候，需要将list初始化，使其不能为空指针。
	 */
	public static Map<String,List<Message>> map = new HashMap<String,List<Message>>();
	/**
	 * 表示实体配置信息。
	 * <p>用一个map来表示
	 * <p>其中key表示实体名称
	 * <p>其中的value部分用一个namevalue的队列来表示一个实体的具体配置信息。
	 */
	public static Map<String,List<NameValue>> entity_conf_map = new HashMap<String,List<NameValue>>();
	/**
	 * 表示系统配置信息。
	 * <p>用namevalue队列来表示系统的配置信息。
	 * @see MessageServerRMI
	 */
	public static List<NameValue> system_conf = new ArrayList<NameValue>();

}
