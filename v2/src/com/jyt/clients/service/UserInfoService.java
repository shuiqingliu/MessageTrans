package com.jyt.clients.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.jyt.clients.db.ConnectionPool;
import com.jyt.clients.db.ConnectionPoolUtils;
import com.jyt.clients.model.User;

public class UserInfoService {
	
	public static User fetchUserInfo(String uid) {
		ConnectionPool connPool = ConnectionPoolUtils.GetPoolInstance();
		User user = new User();
		String sql = "select * from user where name='"+uid+"'";
		try {
			Connection conn = connPool.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				user.setId(rs.getString("id"));
				user.setAvatar(rs.getString("avatar"));
				user.setDepartment(rs.getString("department"));
				user.setEmail(rs.getString("email"));
				user.setName(rs.getString("name"));
				user.setPhone(rs.getString("phone"));
			}
			return user;
		} catch (Exception e) {
			e.printStackTrace();
			return user;
		}

	}

	public static User fetchUserInfoById(String uid){
		ConnectionPool connPool = ConnectionPoolUtils.GetPoolInstance();
		User user = new User();
		String sql = "select * from user where id ='"+uid+"'";
		Connection connection =null;
		try {
			connection = connPool.getConnection();
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				user.setId(rs.getString("id"));
				user.setAvatar(rs.getString("avatar"));
				user.setDepartment(rs.getString("department"));
				user.setName(rs.getString("name"));
				user.setPhone(rs.getString("phone"));
				user.setEmail(rs.getString("email"));

			}
			return user;
		} catch (Exception e) {
			e.printStackTrace();
			return user;
		}finally {
			connPool.returnConnection(connection);
		}
	}

	public static void modifyUserInfo(User user) {
/*		String sql = "UPDATE user SET name='" + user.getName()
				+ "', passwd='" + user.getPasswd() + "', avater='"
				+ user.getAvatar() + "', department='" + user.getDepartment()
				+ "', phone='" + user.getPhone() + "', email='"
				+ user.getEmail() + "' WHERE id='" + user.getId() + "'";*/

		String sql="UPDATE user SET password ='"+ user.getPasswd() + "',email ='"+user.getEmail()+"',phone = '"+user.getPhone()+"'  where id='"+user.getId()+"'";
		Connection connection =null;
		ConnectionPool connPool = ConnectionPoolUtils.GetPoolInstance();
		try {

			connection = connPool.getConnection();
			Statement stmt = connection.createStatement();
			stmt.execute(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			connPool.returnConnection(connection);
		}
	}

	public static List<String> getAllUsers() {
		ConnectionPool connPool = ConnectionPoolUtils.GetPoolInstance();// ����ģʽ�������ӳض���
		List<String> uids = new ArrayList<String>();
		String sql = "select * from user";
		try {
			Connection conn = connPool.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				uids.add(rs.getString("id"));
			}
			return uids;
		} catch (Exception e) {
			e.printStackTrace();
			return uids;
		}
	}
}
