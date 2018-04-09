package com.jyt.clients.service;

import com.jyt.clients.db.ConnectionPool;
import com.jyt.clients.db.ConnectionPoolUtils;
import com.jyt.clients.model.Group;
import com.jyt.clients.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GroupServiceOfWeb {
	//1-创建聊天群，并初始化群成员-createGroup;
	//2-添加单个群组成员-addGroupMember
	//3-加入多个群成员-addGroupMembers-用于建群；
	//4-创建0成员群-doCreateGroup
	//5-修改群名字；modifyGroupName
	//6-获取id为x的群组；用于返回给即刻创建的诸位；getGroupById;
	//7-删除群成员；deleteMember
	//8-获取某id为x的用户所在的群组列表；getGroupListByUid
	//9-获取id为x的群组的成员（要成员详细信息）；getGroupMembers
	//10-获取id为x的群组的成员（只要成员id）

    //1-创建聊天群
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

	//2-添加单个群组成员
	//member为欲添加成员id；
	public static boolean addGroupMember(int groupId, String member){
        ArrayList<String> members = new ArrayList<>();
        members.add(member);
        boolean b = addGroupMembers(groupId, members);
        return b;
    }


	//3-加入多个群成员
	public static boolean addGroupMembers(int groupId,List<String> members){
        ConnectionPool connPool = ConnectionPoolUtils.GetPoolInstance();
        String sql = "INSERT INTO group_user(groupid, userid) VALUES(?,?)";
		Connection connection =null;

        try {
            connection = connPool.getConnection();
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
            connPool.returnConnection(connection);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
        	connPool.returnConnection(connection);
		}
		return  false;
    }


	//4-创建群
	public static int doCreateGroup(String uid){
		ConnectionPool connPool = ConnectionPoolUtils.GetPoolInstance();// 单例模式创建连接池对象
		String sql = "INSERT INTO groups(groupname, manager) VALUES('"+uid + "-group', '"+uid + "')";
        String sql2 = "SELECT LAST_INSERT_ID() AS value";
		Connection connection =null;

        try {
            connection = connPool.getConnection();
            Statement statement = connection.createStatement();
            int i = statement.executeUpdate(sql);
            ResultSet execute1 = statement.executeQuery(sql2);
            execute1.next();
            int groupId = execute1.getInt("value");

            return groupId;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
			connPool.returnConnection(connection);
		}
        return 0;
    }

	//5-修改群名字
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
		}finally {
			connPool.returnConnection(connection);
		}
		return res;
	}

    //6-获取id为x的群组；用于返回给即刻创建的诸位；
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
		}finally {
			connPool.returnConnection(connection);
		}

		if(groupList.equals("")) {
			return "{}";
		}else{
			return groupList;
		}
	}


	//7-删除群成员；
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
		}finally {
			connPool.returnConnection(connection);
		}

		return  res;
	}

    //8-获取某id为x的用户所在的群组列表；
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
		}finally {
			connPool.returnConnection(connection);
		}

		return "[" + groupList + "]";
	}

	//9-获取id为x的群组的成员；
	public static String getGroupMembers(int gid){
		String groupMembersList = "";
		ConnectionPool connPool = ConnectionPoolUtils.GetPoolInstance();
		String sql = "SELECT u.id as userid, u.name as username, u.avatar as avatar, u.department as department " +
				" FROM group_user INNER JOIN user as u" +
				" WHERE group_user.groupid = ?" +
				" AND group_user.userid=u.id";

		Connection connection = null;
		try {
			connection = connPool.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1,gid);
			ResultSet resultSet = preparedStatement.executeQuery();
			int i=0;
			while (resultSet.next()){
				if(i!=0){
					groupMembersList += ",";
				}
				int userid = resultSet.getInt("userid");
				String username = resultSet.getString("username");
				if(username==null)username="NO_NAME";
				String avatar = resultSet.getString("avatar");
				if(avatar==null)avatar="NO_AVATAR";
				String department = resultSet.getString("department");
				if(department==null)department="NO_DEPARTMENT";
				groupMembersList += "{'userid':'"+userid + "','username':'" +username+"','avatar':'"+avatar+"','department':'"+department+"'}";
				i++;
			}


		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			connPool.returnConnection(connection);
		}

		return "[" + groupMembersList + "]";
	}

	//10-获取id为x的群组的成员（只要成员id,用于向他们发消息；）
	public static ArrayList<String> getGroupMembersId(int gid){
		ArrayList<String> ids = new ArrayList<>();
		String groupMembersList = "";
		ConnectionPool connPool = ConnectionPoolUtils.GetPoolInstance();
		String sql = "SELECT u.id as userid" +
				" FROM group_user INNER JOIN user as u" +
				" WHERE group_user.groupid = ?" +
				" AND group_user.userid=u.id";

		Connection connection = null;
		try {
			connection = connPool.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1,gid);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()){
				String userid = resultSet.getString("userid");
				ids.add(userid);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			connPool.returnConnection(connection);
		}
		return ids;
	}

	//11-修改个人像
	public static String modifyUserAvatar(String uid, String useravatar){
		ConnectionPool connPool = ConnectionPoolUtils.GetPoolInstance();// 单例模式创建连接池对象
		String sql = "UPDATE user SET avatar = ? WHERE id = ?";
		Connection connection = null;
		String res = "{'success':'no'}";
		try {
			connection = connPool.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1,useravatar);
			preparedStatement.setString(2,uid);
			int i = preparedStatement.executeUpdate();

			if(i>0){
				res ="{'success':'yes'}";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			connPool.returnConnection(connection);
		}
		return res;
	}

	//12-根据名字搜索好友
	public static List<User> searchFriendByName(String searchName){

		ConnectionPool connPool = ConnectionPoolUtils.GetPoolInstance();
		ArrayList<User> users = new ArrayList<>();

		String sql = "select * from user where NAME LIKE '%"+searchName+"%'";
		Connection connection =null;

		try {
			connection = connPool.getConnection();
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				User user = new User();
				user.setId(rs.getString("id"));
				user.setAvatar(rs.getString("avatar"));
				user.setDepartment(rs.getString("department"));
				user.setEmail(rs.getString("email"));
				user.setName(rs.getString("name"));
				user.setPhone(rs.getString("phone"));
				users.add(user);
			}
			return users;
		} catch (Exception e) {
			e.printStackTrace();
			return users;
		}finally {
			connPool.returnConnection(connection);
		}
	}


}
