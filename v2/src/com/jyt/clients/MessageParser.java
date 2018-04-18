package com.jyt.clients;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jyt.clients.model.GroupMessage;
import com.jyt.clients.model.MessageBean;

/**
 * Created by qingliu on 1/24/18.
 */
public class MessageParser {
    private Gson gson;

    public MessageParser() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("yyyy-MM-dd HH:mm:ss");
        this.gson = gsonBuilder.create();
    }

    /**
     * 将 json 解析 Bean.MessageBean 对象
     * @param messageContent 客户端发过来的 json 字符串
     **/
    public MessageBean getMessageBean(String messageContent){
        MessageBean messageBean = gson.fromJson(messageContent,MessageBean.class);
        return messageBean;
    }

    /**
     * 将 Bean.MessageBean 对象转换为 json 字符串以便发送给客户端
     * @param messageBean 根据消息机制转发过来的消息解析出的内容来产生的 Bean.MessageBean 对象
     **/
    public String messageToJson(MessageBean messageBean){
        String messageJson = gson.toJson(messageBean);
        return messageJson;
    }

    public GroupMessage getGroupMsgInfo(String groupContent){
        GroupMessage groupMessage = gson.fromJson(groupContent,GroupMessage.class);
        return  groupMessage;
    }


}
