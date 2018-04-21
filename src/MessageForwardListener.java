import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by qingliu on 1/15/18.
 */
public class MessageForwardListener {

    public MessageForwardListener(int port) {

        ServerSocket listeningSocket = null;
        try {
            //用于监听 Socket 连接
            listeningSocket = new ServerSocket(port);

            while (true) {
                System.out.println("Waiting for client request");
                Socket linkSocket = listeningSocket.accept();
                //parse username
                //TODO: parser connection message to get username
                System.out.println("Client from:" + linkSocket.getRemoteSocketAddress() + ", connect success");
                //创建一个后台线程去处理连接
                handleLink(linkSocket);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleLink(Socket socket){
        Thread dealLinkTheard = new Thread(new MessageHandler(socket));
        dealLinkTheard.setDaemon(true);
        dealLinkTheard.setName(socket.toString());
        dealLinkTheard.start();
    }
    public static void main(String args[]) {

        if (args.length != 1) {
            System.out.println("please input the correct port number!");
            System.exit(1);
        }
        int port = Integer.parseInt(args[0]);
        MessageForwardListener messageForwardListener = new MessageForwardListener(port);
    }
}
