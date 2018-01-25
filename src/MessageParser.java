import com.google.gson.Gson;

/**
 * Created by qingliu on 1/24/18.
 */
public class MessageParser {
    private Gson gson;

    public MessageParser() {
        this.gson = new Gson();
    }

    /**
     * 将 json 解析 MessageBean 对象
     * @param messageContent 客户端发过来的 json 字符串
     **/
    public MessageBean getMessageBean(String messageContent){
        MessageBean messageBean = gson.fromJson(messageContent,MessageBean.class);
        return messageBean;
    }

    /**
     * 将 MessageBean 对象转换为 json 字符串以便发送给客户端
     * @param messageBean 根据消息机制转发过来的消息解析出的内容来产生的 MessageBean 对象
     **/
    public String messageToJson(MessageBean messageBean){
        String messageJson = gson.toJson(messageBean);
        return messageJson;
    }


}
