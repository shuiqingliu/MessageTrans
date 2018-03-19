package com.jyt.clients.service;

import com.jyt.clients.db.ConnectionPool;
import com.jyt.clients.db.ConnectionPoolUtils;
import com.jyt.clients.model.Group;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GroupServiceOfWeb {

    //创建聊天群
    public static String createGroup(String uid, List<String> members){
    	//创建原始群组
        int groupId = doCreateGroup(uid);
        if (groupId==0){
            return "{'success':'no'}";
        }
        //填充成员
        boolean success = addGroupMembers(groupId, members);


        //返回新建群信息
		String groupById = getGroupById(groupId);
		return groupById;
    }

	//添加单个群组成员
	//member为欲添加成员id；
	public static boolean addGroupMember(int groupId, String member){
        ArrayList<String> members = new ArrayList<>();
        members.add(member);
        boolean b = addGroupMembers(groupId, members);
        return b;
    }


	//加入多个群成员
	public static boolean addGroupMembers(int groupId,List<String> members){
        ConnectionPool connPool = ConnectionPoolUtils.GetPoolInstance();
        String sql = "INSERT INTO group_user(groupid, userid) VALUES(?,?)";

        try {
            Connection connection = connPool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            connection.setAutoCommit(false);
            for(String member:members){
                preparedStatement.setInt(1,groupId);
                preparedStatement.setString(2,member);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            connection.commit();
            connection.setAutoCommit(true);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  false;
    }


	//创建群
	//
	public static int doCreateGroup(String uid){
		ConnectionPool connPool = ConnectionPoolUtils.GetPoolInstance();// 单例模式创建连接池对象
		String sql = "INSERT INTO groups(groupname, manager) VALUES('"+uid + "-group', '"+uid + "')";
        String sql2 = "SELECT LAST_INSERT_ID() AS value";

        try {
            Connection connection = connPool.getConnection();
            Statement statement = connection.createStatement();
            int i = statement.executeUpdate(sql);
            ResultSet execute1 = statement.executeQuery(sql2);
            execute1.next();
            int groupId = execute1.getInt("value");
            return groupId;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

	//创建群
	//
	public static String modifyGroupName(int groupid, String groupname){
		ConnectionPool connPool = ConnectionPoolUtils.GetPoolInstance();// 单例模式创建连接池对象
		String sql = "UPDATE groups SET groupname=? WHERE groupid = ?";
		Connection connection = null;
		String res = "{'success':'no'}";
		try {
			connection = connPool.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1,groupname);
			preparedStatement.setInt(2,groupid);
			int i = preparedStatement.executeUpdate();
			if(i>0){
				res ="{'success':'yes'}";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

    //获取id为x的群组；
	//用于返回给即刻创建的诸位；
	public static String getGroupById(int groupid){

		String groupList = "";
		ConnectionPool connPool = ConnectionPoolUtils.GetPoolInstance();
		String sql = "SELECT groupid, groupname, groupavatar, manager" +
				" FROM groups " +
				" WHERE groupid = ?";
		Connection connection = null;
		try {
			connection = connPool.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1,groupid);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()){

				groupid = resultSet.getInt("groupid");
				String groupname = resultSet.getString("groupname");
				if(groupname==null)groupname="NO_NAME";
				String groupavatar = resultSet.getString("groupavatar");
				if(groupavatar==null)groupavatar="NO_AVATAR";
				String manager = resultSet.getString("manager");
				if(manager==null)manager="NO_MANAGER";
				groupList += "{'groupid':'"+ groupid + "','groupname':'" +groupname+"','groupavatar':'"+groupavatar+"','manager':'"+manager+"'}";

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		if(groupList.equals("")) {
			return "{}";
		}else{
			return groupList;
		}
	}


	//删除群成员；
	public static String deleteMember(int groupid,String fid){

		String res = "{'success':'no'}";
		int resultSet = 0;
		ConnectionPool connPool = ConnectionPoolUtils.GetPoolInstance();
		String sql = "DELETE FROM group_user WHERE groupid = ? AND userid =?";
		Connection connection = null;
		try {
			connection = connPool.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1,groupid);
			preparedStatement.setString(2, fid);
			resultSet = preparedStatement.executeUpdate();
			if(resultSet>0){
				res = "{'success':'yes'}";
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return  res;
	}

    //获取某id为x的用户所在的群组列表；
	//
    public static String getGroupListByUid(String uid){
		String groupList = "";
		ConnectionPool connPool = ConnectionPoolUtils.GetPoolInstance();
		String sql = "SELECT a.groupid, a.groupname, groupavatar, manager" +
				" FROM group_user INNER JOIN groups as a" +
				" WHERE group_user.userid = ?" +
				" AND group_user.groupid=a.groupid";
		Connection connection = null;
		try {
			connection = connPool.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1,uid);
			ResultSet resultSet = preparedStatement.executeQuery();
			int i=0;
			while (resultSet.next()){
				if(i!=0){
					groupList += ",";
				}
				int groupid = resultSet.getInt("groupid");
				String groupname = resultSet.getString("groupname");
				if(groupname==null)groupname="NO_NAME";
				String groupavatar = resultSet.getString("groupavatar");
				if(groupavatar==null)groupavatar="NO_AVATAR";
				String manager = resultSet.getString("manager");
				if(manager==null)manager="NO_MANAGER";
				groupList += "{'groupid':'"+groupid + "','groupname':'" +groupname+"','groupavatar':'"+groupavatar+"','manager':'"+manager+"'}";
				i++;
			}


		} catch (SQLException e) {
			e.printStackTrace();
		}

		return "[" + groupList + "]";
	}










	public static Group getGroups(String gid) {
		Group group = new Group();
		ConnectionPool connPool = ConnectionPoolUtils.GetPoolInstance();// 单例模式创建连接池对象
		String sql = "SELECT * FROM t_group WHERE group_id='" + gid + "'";
		try {
			Connection conn = connPool.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				String members=rs.getString("members");
				String[] m=members.split("、");
				group.setMembers(Arrays.asList(m));
				return group;
			} else {
				return group;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return group;
		}
	}

	public static void createGroup(Group group) {
		ConnectionPool connPool = ConnectionPoolUtils.GetPoolInstance();// 单例模式创建连接池对象
		String memstr = group.getUid();
		List<String> members = group.getMembers();
		for (String m : members) {
			memstr += ("、" + m);
		}
		String sql = "INSERT INTO t_group VALUES('" + group.getGid() + "', '"
				+ group.getGname() + "', '" + memstr + "')";
		try {
			Connection conn = connPool.getConnection();
			Statement stmt = conn.createStatement();
			stmt.execute(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void pullMember(Group group) {
		List<String> members = group.getMembers();
		String memstr = "";
		for (String m : members) {
			memstr += (m + "、");
		}
		memstr += group.getMid();
		String sql = "UPDATE t_group SET members='" + memstr + "' WHERE group_id='"+group.getGid()+"'";
		try {
			ConnectionPool connPool = ConnectionPoolUtils.GetPoolInstance();
			Connection conn = connPool.getConnection();
			Statement stmt = conn.createStatement();
			stmt.execute(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void modifyGroupName(Group group) {
		String sql = "UPDATE t_group SET group_name='" + group.getGname() + "' WHERE group_id='"+group.getGid()+"'";
		try {
			ConnectionPool connPool = ConnectionPoolUtils.GetPoolInstance();
			Connection conn = connPool.getConnection();
			Statement stmt = conn.createStatement();
			stmt.execute(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void delMember(Group group) {
		List<String> members = group.getMembers();
		String memstr = "";
		for (String m : members) {
			if(!m.equals(group.getMid())){
				memstr += (m + "、");
			}	
		}
		memstr=memstr.substring(0, memstr.length() - 1);
		String sql = "UPDATE t_group SET members='" + memstr + "' WHERE group_id='"+group.getGid()+"'";
		try {
			ConnectionPool connPool = ConnectionPoolUtils.GetPoolInstance();
			Connection conn = connPool.getConnection();
			Statement stmt = conn.createStatement();
			stmt.execute(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void quitGroup(Group group) {
		List<String> members = group.getMembers();
		String memstr = "";
		for (String m : members) {
			if(!m.equals(group.getUid())){
				memstr += (m + "、");
			}	
		}
		memstr=memstr.substring(0, memstr.length() - 1);
		String sql = "UPDATE t_group SET members='" + memstr + "' WHERE group_name='"+group.getGname()+"'";
		try {
			ConnectionPool connPool = ConnectionPoolUtils.GetPoolInstance();
			Connection conn = connPool.getConnection();
			Statement stmt = conn.createStatement();
			stmt.execute(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
