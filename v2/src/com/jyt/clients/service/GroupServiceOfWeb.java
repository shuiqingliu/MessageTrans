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
	//1-��������Ⱥ������ʼ��Ⱥ��Ա-createGroup;
	//2-��ӵ���Ⱥ���Ա-addGroupMember
	//3-������Ⱥ��Ա-addGroupMembers-���ڽ�Ⱥ��
	//4-����0��ԱȺ-doCreateGroup
	//5-�޸�Ⱥ���֣�modifyGroupName
	//6-��ȡidΪx��Ⱥ�飻���ڷ��ظ����̴�������λ��getGroupById;
	//7-ɾ��Ⱥ��Ա��deleteMember
	//8-��ȡĳidΪx���û����ڵ�Ⱥ���б�getGroupListByUid
	//9-��ȡidΪx��Ⱥ��ĳ�Ա��Ҫ��Ա��ϸ��Ϣ����getGroupMembers
	//10-��ȡidΪx��Ⱥ��ĳ�Ա��ֻҪ��Աid��

    //1-��������Ⱥ
    public static String createGroup(String uid, List<String> members){
    	//����ԭʼȺ��
        int groupId = doCreateGroup(uid);
        if (groupId==0){
            return "{'success':'no'}";
        }
        //����Ա
        boolean success = addGroupMembers(groupId, members);


        //�����½�Ⱥ��Ϣ
		String groupById = getGroupById(groupId);
		return groupById;
    }

	//2-��ӵ���Ⱥ���Ա
	//memberΪ����ӳ�Աid��
	public static boolean addGroupMember(int groupId, String member){
        ArrayList<String> members = new ArrayList<>();
        members.add(member);
        boolean b = addGroupMembers(groupId, members);
        return b;
    }


	//3-������Ⱥ��Ա
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


	//4-����Ⱥ
	public static int doCreateGroup(String uid){
		ConnectionPool connPool = ConnectionPoolUtils.GetPoolInstance();// ����ģʽ�������ӳض���
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

	//5-�޸�Ⱥ����
	public static String modifyGroupName(int groupid, String groupname){
		ConnectionPool connPool = ConnectionPoolUtils.GetPoolInstance();// ����ģʽ�������ӳض���
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

    //6-��ȡidΪx��Ⱥ�飻���ڷ��ظ����̴�������λ��
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


	//7-ɾ��Ⱥ��Ա��
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

    //8-��ȡĳidΪx���û����ڵ�Ⱥ���б�
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

	//9-��ȡidΪx��Ⱥ��ĳ�Ա��
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

	//10-��ȡidΪx��Ⱥ��ĳ�Ա��ֻҪ��Աid,���������Ƿ���Ϣ����
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

	//11-�޸ĸ�����
	public static String modifyUserAvatar(String uid, String useravatar){
		ConnectionPool connPool = ConnectionPoolUtils.GetPoolInstance();// ����ģʽ�������ӳض���
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

	//12-����������������
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
