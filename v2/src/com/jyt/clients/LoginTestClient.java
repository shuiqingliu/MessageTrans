package com.jyt.clients;

import com.google.gson.Gson;
import com.jyt.clients.model.Friend;
import com.jyt.clients.model.Group;
import com.jyt.clients.model.MessageRecord;
import com.jyt.clients.model.User;
import com.jyt.message.Message;
import com.jyt.message.MessageConfig;
import com.jyt.message.MessageListener;
import com.jyt.message.MessageServerTcpClient;
import com.jyt.util.ArgumentString;
import com.jyt.util.MyDate;
import com.jyt.util.MySerializable;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class LoginTestClient extends MessageServerTcpClient {
    int i=0;

    public LoginTestClient(String server_ip, String server_name) {
    super(server_ip, server_name,"sys_test");
        addListener("loginRes",new LoginTestClient.ResponseListener(this));


}


    public class ResponseListener implements MessageListener
    {
        LoginTestClient client = null;

        public ResponseListener(LoginTestClient client) {
            this.client = client;
        }
        public void messagePerformed(Message message)
        {
            String field = "**************\n%1 from %2 \n%3\n**************";
            String type=message.getType();
            String from = message.getFrom();
            String time_str = MyDate.f2(message.getCreated());
            String content = (String) MySerializable.bytes_object(message.getContent());
            String[] ss = new String[]{time_str,from,content};
            String result = ArgumentString.replace(field,ss);
            System.out.println(result);
            System.out.println(i++);
        }
    }

    public static void main(String[] args) {
        TestClient client=new TestClient(MessageConfig.server_ip,MessageConfig.server_name);
        client.work();
        byte[] bs = null;
        Scanner sc = new Scanner(System.in);
        String str = null;
        Message msg=null;

        //创建测试群组消息
        Group gp=new Group();
        gp.setGid("Android2017");
        gp.setUid("u1");
        gp.setGname("test");
        gp.setMid("u5");
        List<String> mem=new ArrayList<String>();
        mem.add("u2");
        mem.add("u3");
        mem.add("u4");
        gp.setMembers(mem);

        //循环获取输入，进行相应测试
        while (true) {

            System.out.print(">>>");


                User user=new User();
                user.setId("123");
                user.setName("liunan");
                user.setPasswd("123");
                bs = MySerializable.object_bytes(new Gson().toJson(user));
                msg = new Message("sys_test","sys_login","login",bs);

            client.send(msg);
        }

    }
}
