package com.jyt.clients;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.jyt.clients.model.Group;
import com.jyt.clients.model.User;
import com.jyt.clients.service.FriendsServiceOfWeb;
import com.jyt.clients.service.GroupService;
import com.jyt.clients.service.GroupServiceOfWeb;
import com.jyt.clients.service.UserInfoService;
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

        addListener("groupMsg", new ResponseListener(this));
        addListener("createGroup", new ResponseListener(this));
        addListener("getGroupListOfUser", new ResponseListener(this));
        addListener("addMemberToGroup", new ResponseListener(this));
        addListener("delMember", new ResponseListener(this));
        addListener("modifyGroupName", new ResponseListener(this));
        addListener("modifyGroupAvatar", new ResponseListener(this));
        addListener("modifyUserAvatar", new ResponseListener(this));
    }


    public class ResponseListener implements MessageListener {
        GroupsClientOfWeb client = null;

        //公用方法，
        //但是群消息不用这个，因为群消息不发给自己；
        public void noticeToAllGroupMembers(List<String> membersList, String msgType, String content){
            for(String memberId:membersList){
                byte[] bs = MySerializable.object_bytes(content);
                Message msg = new Message("sys_groups", memberId, msgType, bs);
                client.send(msg);
                System.out.println(msg);
            }
        }
        public void noticeToOne(String member, String msgType, String content){
            byte[] bs = MySerializable.object_bytes(content);
            Message msg = new Message("sys_groups", member, msgType, bs);
            client.send(msg);
            System.out.println(msg);
        }

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
					//String[] userids = jsonObject.getString("member").split("、");
					//List<String> members= Arrays.asList(userids);
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
                //群消息************************************
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
                ArrayList<String> members = new ArrayList<>();
                // 创建群组，通知每个人***********************
                try {
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

                        noticeToAllGroupMembers(members, "createGroupRes", group);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                    String fid = jsonObject.getString("mid");
                    String groupid = jsonObject.getString("gid");
                    String uid = jsonObject.getString("uid");
                    int gid = Integer.parseInt(groupid);

                    success  = GroupServiceOfWeb.addGroupMember(gid, fid);
                    String groupInfo = GroupServiceOfWeb.getGroupById(gid);

                    //给新成员该群的信息
                    ArrayList<String> singleMember = new ArrayList<>();
                    singleMember.add(fid);
                    noticeToAllGroupMembers(singleMember,"createGroupRes",groupInfo);
                    //通知所有群成员，有新人加入；
                    User friend = UserInfoService.fetchUserInfoById(fid);
                    User user = UserInfoService.fetchUserInfoById(uid);
                    ArrayList<String> groupMembersId = GroupServiceOfWeb.getGroupMembersId(gid);
                    noticeToAllGroupMembers(groupMembersId,"pullMemberRes","{'mname':'"+friend.getName()+"','gid':'"+gid+"','uname':'"+user.getName()+"'}");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if (type.equals("delMember")) {
                // 删除群成员；
                String res = "{'success':'no'}";

                try {
                    JSONObject jsonObject = new JSONObject(content);
                    String mid = jsonObject.getString("mid");
                    String uid = jsonObject.getString("uid");
                    String groupid = jsonObject.getString("gid");
                    int gid = Integer.parseInt(groupid);


                    res = GroupServiceOfWeb.deleteMember(gid, mid);
                    //通知所有群成员，有人被删除；
                    User user = UserInfoService.fetchUserInfoById(uid);
                    User member = UserInfoService.fetchUserInfoById(mid);

                    //得到新成员列表，通知他们张三被李四移除
                    ArrayList<String> groupMembersId = GroupServiceOfWeb.getGroupMembersId(gid);
                    noticeToAllGroupMembers(groupMembersId,"delMemberRes","{'uname':'"+user.getName()+"','gid':'"+gid+"','mname':'"+member.getName()+"'}");

                    //通知被移除者本人
                    ArrayList<String> memberSelf = new ArrayList<String>();
                    memberSelf.add(mid);
                    noticeToAllGroupMembers(memberSelf,"beDelMemberRes","{'gid':'"+gid+"'}");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else if (type.equals("modifyGroupName")) {
                // 更改群头像
                String res = "{'success':'no'}";
                boolean success =false;
                try {
                    JSONObject jsonObject = new JSONObject(content);
                    String groupname = jsonObject.getString("groupname");
                    String groupid = jsonObject.getString("groupid");
                    String uid = jsonObject.getString("uid");

                    User user = FriendsServiceOfWeb.findUser(uid);

                    int gid = Integer.parseInt(groupid);
                    res  = GroupServiceOfWeb.modifyGroupName(gid,groupname );

                    ArrayList<String> groupMembersId = GroupServiceOfWeb.getGroupMembersId(gid);
                    noticeToAllGroupMembers(groupMembersId,"modifyGroupNameRes","{'gid':'"+gid+"','gname':'"+groupname+"','modifyerName':'"+user.getName()+"'}");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if (type.equals("modifyGroupAvatar")) {
                // 更改群头像
                String res = "{'success':'no'}";
                boolean success =false;
                try {
                    JSONObject jsonObject = new JSONObject(content);
                    String groupAvatar = jsonObject.getString("groupAvatar");
                    String groupid = jsonObject.getString("groupid");
                    String uid = jsonObject.getString("uid");

                    User user = FriendsServiceOfWeb.findUser(uid);

                    int gid = Integer.parseInt(groupid);
                    res  = GroupServiceOfWeb.modifyGroupAvatar(gid,groupAvatar );

                    ArrayList<String> groupMembersId = GroupServiceOfWeb.getGroupMembersId(gid);
                    noticeToAllGroupMembers(groupMembersId,"modifyGroupAvatarRes","{'gid':'"+gid+"','gavatar':'"+groupAvatar+"','modifyerName':'"+user.getName()+"'}");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if (type.equals("modifyUserAvatar")) {
                // 更改群头像
                String res = "{'success':'no'}";
                boolean success =false;
                try {
                    JSONObject jsonObject = new JSONObject(content);
                    String userAvatar = jsonObject.getString("UserAvatar");
                    String uid = jsonObject.getString("uid");

                    String s = GroupServiceOfWeb.modifyUserAvatar(uid, userAvatar);

                    noticeToOne(uid,"modifyUserAvatarRes","{'avatar':'"+userAvatar+"','uid':'"+uid+"'}");
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
