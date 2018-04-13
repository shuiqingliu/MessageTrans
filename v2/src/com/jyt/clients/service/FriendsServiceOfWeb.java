package com.jyt.clients.service;

import com.jyt.clients.db.ConnectionPool;
import com.jyt.clients.db.ConnectionPoolUtils;
import com.jyt.clients.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class FriendsServiceOfWeb {

	//1-
	public static void addFri(String uid, String fid) {
		ConnectionPool connPool = ConnectionPoolUtils.GetPoolInstance();
		String sql1 = "INSERT INTO friends(uid, fid) VAlUES ('" + uid + "', '"
				+ fid + "')";
		String sql2 = "INSERT INTO friends(uid, fid) VAlUES ('" + fid + "', '"
				+ uid + "')";
		String sql3="select * from friends where uid='"+uid+"'AND fid='"+fid+"'";
		Connection connection =null;
		try {
			connection = connPool.getConnection();
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql3);
			if(!rs.next()){
				stmt.execute(sql1);
				stmt.execute(sql2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			connPool.returnConnection(connection);
		}

	}

	//2-
	public static boolean delFri(String uid, String fid) {
		ConnectionPool connPool = ConnectionPoolUtils.GetPoolInstance();

		String sql = "DELETE FROM friends WHERE uid = '" + uid + "' AND fid = '"
				+ fid + "'";
		String sql2 = "DELETE FROM friends WHERE uid = '" + fid + "' AND fid = '"
				+ uid + "'";
		Connection connection =null;
		try {
			connection = connPool.getConnection();
			Statement stmt = connection.createStatement();
			System.out.println(sql);
			if (stmt.execute(sql) == false) {
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally {
			connPool.returnConnection(connection);
		}
	}

	//3-
	public static List<User> fetchFris(String uid){
		ConnectionPool connPool = ConnectionPoolUtils.GetPoolInstance();// ����ģʽ�������ӳض���
		List<User> fris = new ArrayList<User>();
		String sql = "SELECT * FROM friends WHERE uid='"+uid+"'";
		Connection connection =null;
		try {
			connection = connPool.getConnection();
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String fid=rs.getString("fid");
				System.out.println(fid);
				User user=UserInfoService.fetchUserInfoById(fid);
				fris.add(user);
			}
			return fris;
		} catch (Exception e) {
			e.printStackTrace();
			return fris;
		}finally {
			connPool.returnConnection(connection);
		}
	}

	public static User findUser(String id) {
		ConnectionPool connPool = ConnectionPoolUtils.GetPoolInstance();
		String sql="SELECT * from user WHERE id ='"+id+"'";
		Connection connection=null;
		Statement stmt=null;
		ResultSet rs=null;
		try {
			connection = connPool.getConnection();
			stmt = connection.createStatement();
			rs=stmt.executeQuery(sql);
			User theUser=new User();
			if(rs.next()){
				theUser.setId(rs.getString("id"));
				theUser.setName(rs.getString("name"));
				theUser.setPhone(rs.getString("phone"));
				theUser.setEmail(rs.getString("email"));
				theUser.setDepartment(rs.getString("department"));
				return theUser;
			} else{
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally {
			connPool.returnConnection(connection);
		}
	}



}
