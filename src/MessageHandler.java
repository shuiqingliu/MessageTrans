import com.jyt.message.Message;
import com.jyt.message.MessageConfig;
import com.jyt.util.MySerializable;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * Created by qingliu on 1/15/18.
 */
public class MessageHandler implements Runnable{

    private Socket socket;
    private BufferedReader readerData = null;
    private BufferedWriter writerData = null;
    private DataInputStream clientData = null;
    public MessageForwarder forwarder;
    private MessageParser messageParser;
    private MessageBean messageBean;

    public MessageHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try {
            //将 socket 中的字节流转换成字符流并进行封装
            readerData = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println(socket);
            //存储接收到的数据
            String socketLine = "";
            StringBuffer jsonData = null;
            jsonData = new StringBuffer(socketLine);
            //阻塞接收消息，直到消息处理完成。
            while ( (socketLine = readerData.readLine()) != null ){
                jsonData.append(socketLine);
                System.out.println("接收到的消息：" + socketLine);
                //将 json 解析得到 MessageBean 对象
                messageParser = new MessageParser();
                messageBean = messageParser.getMessageBean(socketLine);
                //若为 login 类型的消息则创建连接,否则直接发送。
                if (messageBean.getType().equals("login")){
                    //在服务器中注册该客户端
                    this.startForwardThread(messageBean.getFrom());
                    //将连接添加到 proxy server 的客户端维护 Map 中。
                    ClientManager.getClientManager().add(messageBean.getFrom(),socket);
                    //TODO: send login message
                }
                forwarder.send(createMessage(socketLine));
                //打印发送的消息，并将暂存的消息置空
                System.out.println("jsonData:" + jsonData.toString());
                jsonData = new StringBuffer("");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                ClientManager.getClientManager().clientList.remove(messageBean.getFrom());
                readerData.close();
                socket.close();
            }
            catch (IOException e){
                e.printStackTrace();
            }

        }
    }

    //转换消息格式
    private String byteToJsonString(byte[] messageContent){
        return new String(messageContent, Charset.forName("UTF-8"));
    }

    private byte[] msgContentSerialize(String messageString){
        return MySerializable.object_bytes(messageString);
    }

    //开启转发消息线程来处理消息
    private void startForwardThread(String clientName){
        forwarder = new MessageForwarder("127.0.0.1", MessageConfig.server_name, 10001, clientName);
        forwarder.work();
    }

    //根据 json 字符串来创建 Message 对象
    private Message createMessage(String jsonData){
        MessageBean messageBean = new MessageParser().getMessageBean(jsonData);
        byte[] messageByte = msgContentSerialize(messageBean.getContent());
        String from = messageBean.getFrom();
        String to   = messageBean.getTo();
        String type = messageBean.getType();
        Message message = new Message(from,to,type,messageByte);
        System.out.println(message);
        return message;
    }
}
