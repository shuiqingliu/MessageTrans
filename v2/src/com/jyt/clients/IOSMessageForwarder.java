package com.jyt.clients;

import com.jyt.clients.model.GroupMessage;
import com.jyt.clients.model.MessageBean;
import com.jyt.message.Message;
import com.jyt.message.MessageConfig;
import com.jyt.message.MessageListener;
import com.jyt.message.MessageServerTcpClient;
import com.jyt.util.ArgumentString;
import com.jyt.util.MyDate;
import com.jyt.util.MySerializable;
import com.sun.tools.javac.util.Log;

import java.net.Socket;
import java.util.HashMap;

/**
 * Created by qingliu on 1/18/18.
 */
public class IOSMessageForwarder extends MessageServerTcpClient {
    public String messageResult;

    public IOSMessageForwarder(String server_ip, String server_name, String entity) {
        super(server_ip,server_name,entity);
        this.register(entity);
        addListener("msg", new ResponseListener());
        addListener("addFriRes",new AddFriResResponseListener());
        addListener("addFri",new AddFriResponseListener());
        addListener("pullFri",new PullFriResponseListener());
        addListener("pullUser",new PullFriResponseListener());
        addListener("pullGroup",new PullGroupResponseListener());
        addListener("fetchUserInfo", new FetchUserInfoResponseListener());
        addListener("loginRes", new LoginResponseListener());
        addListener("modifyUserInfo", new ModifyResponseListener());
        addListener("createGroupRes", new CreateGroupResponseListener());
        addListener("pullMemberRes", new PullMemberResponseListener());
        addListener("delMemberRes", new DelMemberResponseListener());
        addListener("quitGroupRes", new QuitGroupResponseListener());
        addListener("modifyGroupNameRes", new ModifyGroupNmaeResponseListener());
        addListener("msg_pic", new ResponseListener());
        addListener("msg_file", new ResponseListener());
        addListener("msg_voice", new ResponseListener());
        addListener("msg_lfile", new ResponseListener());
        addListener("delFriRes",new delFriResponseListener());
    }

    public class delFriResponseListener implements MessageListener{
        @Override
        public void messagePerformed(Message message) {
            String field = "**************\n%1 from %2 \n%3\n**************";
            String type=message.getType();
            String from = message.getFrom();
            String time_str = MyDate.f2(message.getCreated());
            String content = (String)MySerializable.bytes_object(message.getContent());
            String[] ss = new String[]{time_str,from,content};
            String result = ArgumentString.replace(field,ss);

            System.out.println( "get message: "+result);
        }
    }

    public class PullFriResponseListener implements MessageListener{
        public void messagePerformed(Message message){
            String field = "**************\n%1 from %2 \n%3\n**************";
            String type=message.getType();
            String from = message.getFrom();
            String time_str = MyDate.f2(message.getCreated());
            String content = (String)MySerializable.bytes_object(message.getContent());
            String[] ss = new String[]{time_str,from,content};
            String result = ArgumentString.replace(field,ss);

            System.out.println( "get message: "+result);
        }

    }

    public class AddFriResResponseListener implements MessageListener{
        public void messagePerformed(Message message){
            String field = "**************\n%1 from %2 \n%3\n**************";
            String type=message.getType();
            String from = message.getFrom();
            String time_str = MyDate.f2(message.getCreated());
            String content = (String)MySerializable.bytes_object(message.getContent());
            String[] ss = new String[]{time_str,from,content};
            String result = ArgumentString.replace(field,ss);
        }

    }

    public class AddFriResponseListener implements MessageListener{
        public void messagePerformed(Message message){
            String field = "**************\n%1 from %2 \n%3\n**************";
            String type=message.getType();
            String from = message.getFrom();
            String time_str = MyDate.f2(message.getCreated());
            String content = (String)MySerializable.bytes_object(message.getContent());
            String[] ss = new String[]{time_str,from,content};
            String result = ArgumentString.replace(field,ss);
        }

    }

    public class FetchUserInfoResponseListener implements MessageListener {
        public void messagePerformed(Message message)
        {
            String field = "**************\n%1 from %2 \n%3\n**************";
            String time_str = MyDate.f2(message.getCreated());
            String from = message.getFrom();
            String content = (String) MySerializable.bytes_object(message.getContent());
            String[] ss = new String[]{time_str,from,content};
            String result = ArgumentString.replace(field,ss);
        }
    }

    public class LoginResponseListener implements MessageListener {
        public void messagePerformed(Message message)
        {

            String field = "**************\n%1 from %2 \n%3\n**************";
            String type=message.getType();
            String from = message.getFrom();
            String time_str = MyDate.f2(message.getCreated());
            String content = (String)MySerializable.bytes_object(message.getContent());
            String[] ss = new String[]{time_str,from,content};
            String result = ArgumentString.replace(field,ss);
            System.out.println("login content:" + content);
        }
    }

    public class FetchUserResponseListener implements MessageListener {
        public void messagePerformed(Message message)
        {

            String field = "**************\n%1 from %2 \n%3\n**************";
            String type=message.getType();
            String from = message.getFrom();
            String time_str = MyDate.f2(message.getCreated());
            String content = (String)MySerializable.bytes_object(message.getContent());
            String[] ss = new String[]{time_str,from,content};
            String result = ArgumentString.replace(field,ss);


        }
    }

    public class ModifyResponseListener implements MessageListener {
        public void messagePerformed(Message message)
        {

            String field = "**************\n%1 from %2 \n%3\n**************";
            String type=message.getType();
            String from = message.getFrom();
            String time_str = MyDate.f2(message.getCreated());
            String content = (String)MySerializable.bytes_object(message.getContent());
            String[] ss = new String[]{time_str,from,content};
            String result = ArgumentString.replace(field,ss);


        }
    }

    /**
     * 群聊部分
     */

    public class CreateGroupResponseListener implements MessageListener {
        public void messagePerformed(Message message) {
            //这里构造了json串，不需要这样，因为发送过来的文字本身就是结构完好的
            String field = "**************\n%1 from %2 \n%3\n**************";
            String type=message.getType();
            String time_str = MyDate.f2(message.getCreated());
            String from = message.getFrom();
            String content = (String) MySerializable.bytes_object(message.getContent());
            String[] ss = new String[]{time_str, from, content};
            String result = ArgumentString.replace(field, ss);


        }
    }

    public class PullMemberResponseListener implements MessageListener {
        public void messagePerformed(Message message) {
            //这里构造了json串，不需要这样，因为发送过来的文字本身就是结构完好的
            String field = "**************\n%1 from %2 \n%3\n**************";
            String type=message.getType();
            String time_str = MyDate.f2(message.getCreated());
            String from = message.getFrom();
            String content = (String) MySerializable.bytes_object(message.getContent());
            String[] ss = new String[]{time_str, from, content};
            String result = ArgumentString.replace(field, ss);


        }
    }

    public class DelMemberResponseListener implements MessageListener {
        public void messagePerformed(Message message) {
            //这里构造了json串，不需要这样，因为发送过来的文字本身就是结构完好的
            String field = "**************\n%1 from %2 \n%3\n**************";
            String type=message.getType();
            String time_str = MyDate.f2(message.getCreated());
            String from = message.getFrom();
            String content = (String) MySerializable.bytes_object(message.getContent());
            String[] ss = new String[]{time_str, from, content};
            String result = ArgumentString.replace(field, ss);


        }
    }

    public class QuitGroupResponseListener implements MessageListener {
        public void messagePerformed(Message message) {
            //这里构造了json串，不需要这样，因为发送过来的文字本身就是结构完好的
            String field = "**************\n%1 from %2 \n%3\n**************";
            String type=message.getType();
            String time_str = MyDate.f2(message.getCreated());
            String from = message.getFrom();
            String content = (String) MySerializable.bytes_object(message.getContent());
            String[] ss = new String[]{time_str, from, content};
            String result = ArgumentString.replace(field, ss);


        }
    }

    public class ModifyGroupNmaeResponseListener implements MessageListener {
        public void messagePerformed(Message message) {
            //这里构造了json串，不需要这样，因为发送过来的文字本身就是结构完好的
            String field = "**************\n%1 from %2 \n%3\n**************";
            String type=message.getType();
            String time_str = MyDate.f2(message.getCreated());
            String from = message.getFrom();
            String content = (String) MySerializable.bytes_object(message.getContent());
            String[] ss = new String[]{time_str, from, content};
            String result = ArgumentString.replace(field, ss);

        }
    }
    public class PullGroupResponseListener implements MessageListener{
        public void messagePerformed(Message message){
            String field = "**************\n%1 from %2 \n%3\n**************";
            String type=message.getType();
            String from = message.getFrom();
            String time_str = MyDate.f2(message.getCreated());
            String content = (String)MySerializable.bytes_object(message.getContent());
            String[] ss = new String[]{time_str,from,content};
            String result = ArgumentString.replace(field,ss);


        }

    }

    public class ResponseListener implements MessageListener {
        @Override
        public void messagePerformed(Message message) {
            //根据 message 解析消息内容
            String msgTime = MyDate.f2(message.getCreated());
            String from = message.getFrom();
            String type=message.getType();
            String to = message.getTo();
            String content = (String) MySerializable.bytes_object(message.getContent());
            //生成 messageBean 对象
            MessageBean messageBean = new MessageBean();
            messageBean.setType(type);
            messageBean.setFrom(from);
            messageBean.setTime(message.getCreated());
            switch (type){
                case "msg":
                    //转发 Message json
                    messageBean.setTo(message.getTo());
                    messageBean.setContent(content);
                    String forwardMessage = new MessageParser().messageToJson(messageBean);
                    IOSClientManager.getIOSClientManager().sendMessage(to,forwardMessage);
                    //将 messageBean 转为 json 字符串
                    System.out.println("forwardMessage:" + forwardMessage);
                    break;
                case "msgGroup":
                    GroupMessage groupMessage = new MessageParser().getGroupMsgInfo(content);
                    for (String member : groupMessage.getGroupMembers()){
                        System.out.println("Group 中 ID 为:"  + member);
                        messageBean.setTo(member);
                        System.out.println("发送的消息为: " + groupMessage.getMessageContent());
                        messageBean.setContent("{groupID:" + from+ "," + "content:" + content +"}");
                        String forwardGroupMessage = new MessageParser().messageToJson(messageBean);
                        IOSClientManager.getIOSClientManager().sendMessage(member,forwardGroupMessage);
                        //将 messageBean 转为 json 字符串
                        System.out.println("forwardMessage:" + forwardGroupMessage);
                    }
                    break;
            }

            /*客户端列表去重
            *不需要去重，因为 HashMap 的 put 方法当 key 一致的时候原来的值会被替换
            * 详见：https://docs.oracle.com/javase/7/docs/api/java/util/HashMap.html#put(K,%20V)
            *removeClosedSocket(IOSClientManager.getIOSClientManager());
            * */
            //将消息发送给客户端
        }
    }


    //判断 socket 是否连接，并且去重
    private void removeClosedSocket(IOSClientManager IOSClientManager){
        HashMap<String,Socket> tempHashMap = new HashMap<>();
        for (String key: IOSClientManager.clientList.keySet()){
            if ((tempHashMap.containsValue(key)) ==  false &&
                !IOSClientManager.serverClosed(IOSClientManager.clientList.get(key))){
                tempHashMap.put(key, IOSClientManager.clientList.get(key));
            }
        }
        IOSClientManager.clientList = tempHashMap;
    }
}
