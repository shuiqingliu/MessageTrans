import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Vector;

/**
 * Created by qingliu on 1/17/18.
 */

public class ClientManager {
    //使用一个 clientManager 来管理所有的客户端，故采用单例模式
    private static final ClientManager clientManager = new ClientManager();

    public static ClientManager getClientManager() {
        return clientManager;
    }

    //用 HashMap 来存储 Socket  列表
    HashMap<String,Socket> clientList = new HashMap<>();

    public void add(String username,Socket socket) {clientList.put(username,socket);
    }

    /**
     * 将消息发送给 Vector 中的客户端
     *
     * @param userName 接收消息的用户名，取自 Message Content
     * @param message      发送的消息
     **/
    public void sendMessage(String userName, String message) {
        try {
            //将消息发送给除了自己的每一个客户端
            for (int userIndex = 0; userIndex < clientList.size(); userIndex++) {
                Socket socket = clientList.get(userName);
                System.out.println(socket);
                BufferedWriter writerMessage = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                if (serverClosed(socket)) {
                    clientList.remove(userIndex);
                    continue;
                } else if (!socket.equals(socket)) {
                    writerMessage.write(message);
                    writerMessage.newLine();
                    writerMessage.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Boolean serverClosed(Socket socket) {
        try {
            socket.sendUrgentData(0);
            return false;
        } catch (IOException e) {
            return true;
        }
    }
}