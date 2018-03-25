package com.jyt.clients;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.jyt.clients.model.Group;
import com.jyt.clients.service.GroupService;
import com.jyt.clients.service.GroupServiceOfWeb;
import com.jyt.message.Message;
import com.jyt.message.MessageConfig;
import com.jyt.message.MessageListener;
import com.jyt.message.MessageServerTcpClient;
import com.jyt.util.ArgumentString;
import com.jyt.util.MyDate;
import com.jyt.util.MySerializable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GroupsClientOfWeb extends MessageServerTcpClient {

    public GroupsClientOfWeb(String server_ip, String server_name) {
        super(server_ip, server_name, "sys_groups");

        addListener("pullMember", new ResponseListener(this));
        addListener("delMember", new ResponseListener(this));
        addListener("quitGroup", new ResponseListener(this));
        addListener("modifyGroupName", new ResponseListener(this));

        addListener("groupMsg", new ResponseListener(this));
        addListener("createGroup", new ResponseListener(this));
        addListener("getGroupListOfUser", new ResponseListener(this));
        addListener("addMemberToGroup", new ResponseListener(this));
        addListener("delMemberFromGroup", new ResponseListener(this));
        addListener("modifyGroupName", new ResponseListener(this));
    }

    public class ResponseListener implements MessageListener {
        GroupsClientOfWeb client = null;

        public ResponseListener(GroupsClientOfWeb client) {
            this.client = client;
        }

        public void messagePerformed(Message message) {
            String field = "**************\n%1 from %2 \n%3\n**************";
            String type = message.getType();
            String from = message.getFrom();
            String time_str = MyDate.f2(message.getCreated());
            String content = (String) MySerializable.bytes_object(message
                    .getContent());
            String[] ss = new String[]{time_str, from, content};
            String result = ArgumentString.replace(field, ss);
            System.out.println(result);

            if (type.equals("OLDgroupMsg")) {
                // TODO 收到群组消息，转发给每个人
                //****************************************
                try {
                    JSONObject jsonObject = new JSONObject(content);
                    String groupId = jsonObject.getString("to");
                    String content_ = jsonObject.getString("content");
                    String time = jsonObject.getString("ft");

                    Group group = GroupService.getGroup(groupId);
                    List<String> members = group.getMembers();
//					String[] userids = jsonObject.getString("member").split("、");
//					List<String> members= Arrays.asList(userids);
                    if (members != null) {
                        for (String m : members) {
                            if (!m.equals(from)) {
                                JSONObject sendMsg = new JSONObject();
                                sendMsg.put("from", groupId);
                                sendMsg.put("to", m);
                                sendMsg.put("ft", time);
                                sendMsg.put("content", content_);
                                byte[] bs = MySerializable.object_bytes(sendMsg.toString());

                                Message msg = new Message("sys_group", m, "msg", bs);
                                client.send(msg);
                                System.out.println(msg);
                            }

                        }
                        // TODO 更新数据库
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if(type.equals("groupMsg")){

                System.out.println(content);
                try {
                    JSONObject json=new JSONObject(content);
                    String gid = json.getString("gid");
                    String from1 = json.getString("uid");

                    String groupMembers = GroupServiceOfWeb.getGroupMembers(Integer.parseInt(gid));
                    System.out.println(groupMembers);
                    JSONArray jsonArray = new JSONArray(groupMembers);
                    for(int i =0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String userid = jsonObject.getString("userid");
                        if(from1.equals(userid)) continue;
                        byte[] bs = MySerializable.object_bytes(content);
                        Message msg = new Message("sys_groups", userid, "groupMsg", bs);
                        client.send(msg);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else if (type.equals("createGroup")) {
                String group = "";
                // 创建群组，通知每个人
                try {
                    ArrayList<String> members = new ArrayList<>();
                    JSONObject _content = new JSONObject(content);
                    String uid = _content.getString("uid");
                    JSONArray members1 = _content.getJSONArray("members");
                    for (int i = 0; i < members1.length(); i++) {
                        String s = (String) members1.get(i);
                        members.add(s);
                    }
                    if (members.size() != 0) {
                        // TODO 更新数据库
                        group = GroupServiceOfWeb.createGroup(uid, members);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String res = group;
                byte[] bs = null;
                bs = MySerializable.object_bytes(new JsonParser().parse(res).toString());
                Message msg = new Message("sys_groups", from, "createGroup", bs);
                client.send(msg);
//				Group group=new Gson().fromJson(content, Group.class);
//				List<String> members=group.getMembers();
//				for(String m : members){
//					Message msg = new Message("sys_group", m, "createGroupRes", message.getContent());
//					client.send(msg);
//				}
                // TODO 更新数据库
                //GroupService.createGroup(group);
            } else if (type.equals("getGroupListOfUser")) {
                // 创建群组，通知每个人
                String res = "";
                try {
                    JSONObject jsonObject = new JSONObject(content);
                    String uid = jsonObject.getString("uid");
                    res = GroupServiceOfWeb.getGroupListByUid(uid);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                byte[] bs = null;
                bs = MySerializable.object_bytes(new JsonParser().parse(res).toString());
                Message msg = new Message("sys_groups", from, "getGroupListOfUser", bs);
                client.send(msg);

            } else if (type.equals("addMemberToGroup")) {
                // 拉好友入群
                String res = "{'success':'no'}";
                boolean success =false;
                try {
                    JSONObject jsonObject = new JSONObject(content);
                    String fid = jsonObject.getString("fid");
                    String groupid = jsonObject.getString("groupid");
                    int gid = Integer.parseInt(groupid);

                    success  = GroupServiceOfWeb.addGroupMember(gid, fid);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(success){
                    res = "{'success':'yes'}";
                }

                byte[] bs = null;
                bs = MySerializable.object_bytes(new JsonParser().parse(res).toString());
                Message msg = new Message("sys_groups", from, "getGroupListOfUser", bs);
                client.send(msg);

            }else if (type.equals("delMemberFromGroup")) {
                // 删除群成员；
                String res = "{'success':'no'}";

                try {
                    JSONObject jsonObject = new JSONObject(content);
                    String fid = jsonObject.getString("fid");
                    String groupid = jsonObject.getString("groupid");
                    int gid = Integer.parseInt(groupid);

                    res = GroupServiceOfWeb.deleteMember(gid, fid);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                byte[] bs = null;
                bs = MySerializable.object_bytes(new JsonParser().parse(res).toString());
                Message msg = new Message("sys_groups", from, "delMemberFromGroup", bs);
                client.send(msg);

            }else if (type.equals("modifyGroupName")) {
                // 拉好友入群
                String res = "{'success':'no'}";
                boolean success =false;
                try {
                    JSONObject jsonObject = new JSONObject(content);
                    String groupname = jsonObject.getString("groupname");
                    String groupid = jsonObject.getString("groupid");
                    int gid = Integer.parseInt(groupid);
                    res  = GroupServiceOfWeb.modifyGroupName(gid,groupname );
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                byte[] bs = null;
                bs = MySerializable.object_bytes(new JsonParser().parse(res).toString());
                Message msg = new Message("sys_groups", from, "modifyGroupName", bs);
                client.send(msg);
            }else if (type.equals("pullMember")) {
                // 用户进入群组后修改数据库
                //********************************
                try {
                    JSONObject jsonObject = new JSONObject(content);
                    String groupId = jsonObject.getString("gid");
                    String groupName = jsonObject.getString("gname");
                    String[] userids = jsonObject.getString("member").split("、");
                    List<String> members = Arrays.asList(userids);
                    Group group = new Group();
                    group.setGid(groupId);
                    group.setGname(groupName);
                    group.setMembers(members);
                    if (members != null) {
                        for (String m : members) {
                            Message msg = new Message("sys_group", m, "pullMemberRes", message.getContent());
                            client.send(msg);
                            System.out.println(msg);
                        }
                        // TODO 更新数据库
                        GroupService.pullMember(group);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (type.equals("modifyGroupName")) {
                // 修改群组名称
                //*****************************
                Group group = new Gson().fromJson(content, Group.class);
                List<String> members = GroupService.getGroup(group.getGid()).getMembers();
                if (members != null) {
                    for (String m : members) {
                        Message msg = new Message("sys_group", m, "modifyGroupNameRes", message.getContent());
                        client.send(msg);
                        System.out.println(msg);
                    }
                    // TODO 更新数据库
                    GroupService.modifyGroupName(group);
                }
            } else if (type.equals("delMember")) {
                //***************************
                try {
                    JSONObject jsonObject = new JSONObject(content);
                    String groupName = jsonObject.getString("gname");
                    String[] userids = jsonObject.getString("member").split("、");
                    List<String> members = Arrays.asList(userids);
                    Group group = new Group();
                    group.setGname(groupName);
                    group.setMembers(members);
                    if (members != null) {
                        for (String m : members) {
                            Message msg = new Message("sys_group", m, "delMemberRes", message.getContent());
                            client.send(msg);
                            System.out.println(msg);
                        }
                        Message msg = new Message("sys_group", from, "delMemberRes", message.getContent());
                        client.send(msg);
                        System.out.println(msg);
                        // TODO 更新数据库
                        GroupService.delMember(group);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // 删除成员，直接修改数据库，通知每个人
//				Group group=new Gson().fromJson(content, Group.class);
//				List<String> members=GroupService.getGroup(group.getGid()).getMembers();
//				group.setMembers(members);
//				if(members!=null){
//					for(String m : members){
//						Message msg = new Message("sys_group", m, "delMemberRes", message.getContent());
//						client.send(msg);
//					}
//					// TODO 更新数据库
//					GroupService.delMember(group);
//				}
            } else if (type.equals("quitGroup")) {
                // 退群，修改数据库，通知每个人
                //************************

                try {
                    JSONObject jsonObject = new JSONObject(content);
                    String groupName = jsonObject.getString("gname");
                    String[] userids = jsonObject.getString("member").split("、");
                    List<String> members = Arrays.asList(userids);
                    Group group = new Group();
                    group.setGname(groupName);
                    group.setMembers(members);
                    //List<String> members=GroupService.getGroup(group.getGid()).getMembers();
                    if (members != null) {
                        for (String m : members) {
                            Message msg = new Message("sys_group", m, "quitGroupRes", message.getContent());
                            client.send(msg);
                            System.out.println(msg);
                        }
                        Message msg = new Message("sys_group", from, "quitGroupRes", message.getContent());
                        client.send(msg);
                        System.out.println(msg);
                        // TODO 更新数据库
                        GroupService.quitGroup(group);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public static void main(String[] args) {
        GroupsClientOfWeb client = new GroupsClientOfWeb(MessageConfig.server_ip,
                MessageConfig.server_name);
        client.register();
        client.work();
    }
}
