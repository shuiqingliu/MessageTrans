import com.jyt.message.Message;
import com.jyt.message.MessageListener;
import com.jyt.message.MessageServerTcpClient;
import com.jyt.util.ArgumentString;
import com.jyt.util.CountTime;
import com.jyt.util.MyDate;
import com.jyt.util.MySerializable;

import java.net.Socket;
import java.util.HashMap;

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
            messageBean.setTo(to);
            messageBean.setContent(content);
            messageBean.setTime(message.getCreated());
            //将 messageBean 转为 json 字符串
            String forwardMessage = new MessageParser().messageToJson(messageBean);
            //客户端列表去重
            removeClosedSocket(ClientManager.getClientManager());
            //将消息发送给客户端
            ClientManager.getClientManager().sendMessage(to,forwardMessage);
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
