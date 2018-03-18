import Bean.GroupMessage;
import Bean.MessageBean;
import com.jyt.message.Message;
import com.jyt.message.MessageListener;
import com.jyt.message.MessageServerTcpClient;
import com.jyt.util.MyDate;
import com.jyt.util.MySerializable;

import java.net.Socket;
import java.util.HashMap;
import java.util.List;

/**
 * Created by qingliu on 1/18/18.
 */
public class MessageForwarder extends MessageServerTcpClient {
    public String messageResult;

    public MessageForwarder(String server_ip,String server_name, int port, String clientName) {
        super(server_ip,server_name, port, clientName);
        this.register(clientName);
        addListener("login",new ResponseListerner());
        addListener("msg",new ResponseListerner());
        addListener("addFri",new ResponseListerner());
        addListener("msgGroup",new ResponseListerner());
    }

    public class ResponseListerner implements MessageListener{
        @Override
        public void messagePerformed(Message message) {
            //根据 message 解析消息内容
            String msgFormat = "#######\n%1, from %2 \n%3\n########";
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
                    ClientManager.getClientManager().sendMessage(to,forwardMessage);
                    //将 messageBean 转为 json 字符串
                    System.out.println("forwardMessage:" + forwardMessage);
                case "msgGroup":
                    GroupMessage groupMessage = new MessageParser().getGroupMsgInfo(content);
                    for (String member : groupMessage.getGroupMembers()){
                        System.out.println("Group 中 ID 为:"  + member);
                        messageBean.setTo(member);
                        System.out.println("发送的消息为: " + groupMessage.getMessageContent());
                        messageBean.setContent(content);
                        String forwardGroupMessage = new MessageParser().messageToJson(messageBean);
                        ClientManager.getClientManager().sendMessage(member,forwardGroupMessage);
                        //将 messageBean 转为 json 字符串
                        System.out.println("forwardMessage:" + forwardGroupMessage);
                    }
            }

            /*客户端列表去重
            *不需要去重，因为 HashMap 的 put 方法当 key 一致的时候原来的值会被替换
            * 详见：https://docs.oracle.com/javase/7/docs/api/java/util/HashMap.html#put(K,%20V)
            *removeClosedSocket(ClientManager.getClientManager());
            * */
            //将消息发送给客户端
        }
    }

    private void addListenerServer(String tyep){
        addListener(tyep,new ResponseListerner());
    }

    //判断 socket 是否连接，并且去重
    private void removeClosedSocket(ClientManager clientManager){
        HashMap<String,Socket> tempHashMap = new HashMap<>();
        for (String key: clientManager.clientList.keySet()){
            if ((tempHashMap.containsValue(key)) ==  false &&
                !clientManager.serverClosed(clientManager.clientList.get(key))){
                tempHashMap.put(key,clientManager.clientList.get(key));
            }
        }
        clientManager.clientList = tempHashMap;
    }

}
