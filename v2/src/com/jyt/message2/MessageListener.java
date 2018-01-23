package com.jyt.message2;

/**
 * 定义消息的处理方式。
 * @author zqyang
 *
 */
public interface MessageListener {
	/**
	 * 消息的处理回调函数。
	 * <p>在一个实体中需要实现该接口。
	 * 当一个实体接收到消息后，一般的处理过程为：
	 * 1、解析消息的内容，得到消息的类型、创建的时间、消息内容二进制流等信息；
	 * 2、将消息内容二进制流转换为序列化的对象，从而得到对象的信息
	 * 3、从对象属性中得到相关信息，进行分析处理
	 * 4、组织新的消息发送出去
	 * @param message 接受到的消息。
	 */
	void messagePerformed(Message message);
}
