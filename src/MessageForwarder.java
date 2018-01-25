import com.jyt.message.Message;
import com.jyt.message.MessageListener;
import com.jyt.message.MessageServerTcpClient;
import com.jyt.util.ArgumentString;
import com.jyt.util.CountTime;
import com.jyt.util.MyDate;
import com.jyt.util.MySerializable;

/**
 * Created by qingliu on 1/18/18.
 */
public class MessageForwarder extends MessageServerTcpClient {
    public String messageResult;

    public MessageForwarder(String server_ip,String server_name, int port, String clientName,String msgServerType) {
        super(server_ip,server_name, port, clientName);
        this.register(clientName);
        addListener(msgServerType,new ResponseListerner());
    }

    public class ResponseListerner implements MessageListener{
        @Override
        public void messagePerformed(Message message) {
            String msgFormat = "#######\n%1, from %2 \n%3\n########";
            String msgTime = MyDate.f2(message.getCreated());
            String from = message.getFrom();
            String type=message.getType();
            String content = (String) MySerializable.bytes_object(message.getContent());
            String[] msgContentParse = new String[]{msgTime,from,content};
            String result = ArgumentString.replace(msgFormat,msgContentParse);
            messageResult = content;
            System.out.println("Log4:接收到的消息：" + result);
            //TODO: 查询 ClientManager 中的连接将接收的各种消息转发给客户端
        }
    }

    /**
     * 用获取到的 json 数据解析后的结果来生要发送的消息
     *
     * @param from     消息发送方标识
     * @param to       消息接受方标识
     * @param type     发送的消息的内容
     * @param byteData 消息实体
     **/
    public Message createMessage(String from, String to, String type, byte[] byteData) {
        Message message = new Message(from, to, type, byteData);
        return message;
    }
}
