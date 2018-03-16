package com.jyt.clients.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.jyt.clients.db.ConnectionPool;
import com.jyt.clients.db.ConnectionPoolUtils;
import com.jyt.clients.model.User;

public class FriendsService {

	private Connection conn=null;
	private PreparedStatement ps=null;
	private ResultSet rs=null;

	public  void addFri(String uid, String fid) {
		ConnectionPool connPool = ConnectionPoolUtils.GetPoolInstance();
		String sql1 = "INSERT INTO friends(uid, fid) VAlUES ('" + uid + "', '"
				+ fid + "')";
		String sql2 = "INSERT INTO friends(uid, fid) VAlUES ('" + fid + "', '"
				+ uid + "')";
		String sql3="select * friends where uid='"+uid+"'AND fid='"+fid+"'";
		String sql4="select * friends where uid='"+fid+"'AND fid='"+uid+"'";
		try {
			Connection conn = connPool.getConnection();
			Statement stmt = conn.createStatement();
			rs=stmt.executeQuery(sql3);
			if(!rs.next()){
				stmt.execute(sql1);
			}
			rs=stmt.executeQuery(sql4);
			if(!rs.next()){
				stmt.execute(sql2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static boolean delFri(String uid, String fid) {
		ConnectionPool connPool = ConnectionPoolUtils.GetPoolInstance();

		String sql = "DELETE FROM friends WHERE uid = '" + uid + "' AND fid = '"
				+ fid + "'";
		String sql2 = "DELETE FROM friends WHERE uid = '" + fid + "' AND fid = '"
				+ uid + "'";
		try {
			Connection conn = connPool.getConnection();
			Statement stmt = conn.createStatement();
			System.out.println(sql);
			if (stmt.execute(sql) == false) {
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static List<User> fetchFris(String uid){
		ConnectionPool connPool = ConnectionPoolUtils.GetPoolInstance();// ����ģʽ�������ӳض���
		List<User> fris = new ArrayList<User>();
		String sql = "SELECT * FROM friends WHERE uid='"+uid+"'";
		try {
			Connection conn = connPool.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String fid=rs.getString("fid");
				System.out.println(fid);
				User user=UserInfoService.fetchUserInfo(fid);
				fris.add(user);
			}
			return fris;
		} catch (Exception e) {
			e.printStackTrace();
			return fris;
		}
	}
	public static User findUser(String id) {
		String sql="SELECT * from user WHERE id ='"+id+"'";
		Connection conn=null;
		Statement stmt=null;
		ResultSet rs=null;
		try {
			ConnectionPool connPool = ConnectionPoolUtils.GetPoolInstance();
			conn = connPool.getConnection();
			stmt = conn.createStatement();
			rs=stmt.executeQuery(sql);
			User user=new User();
			if(rs.next()){
				user.setId(rs.getString("id"));
				user.setName(rs.getString("name"));
				user.setDepartment(rs.getString("department"));
				user.setPhone(rs.getString("phone"));
				user.setEmail(rs.getString("email"));
				return user;
			} else{
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally {
			try{
				if(rs!=null){
					rs.close();
				}
				if(stmt!=null){
					stmt.close();
				}
				if(conn!=null){
					conn.close();
				}
			} catch (Exception e){
				e.printStackTrace();
			}
		}
	}

	public List<User> pullFri(String id){
		ConnectionPool connPool = ConnectionPoolUtils.GetPoolInstance();
		String sql1="select fid from friends where uid='"+id+"'";
		String sql2="select * from user where id=? limit 1";
		List<User> list=new ArrayList<>();
		try {
			conn = connPool.getConnection();
			ps = conn.prepareStatement(sql1);
			rs=ps.executeQuery();
			List<String> fidList=new ArrayList<>();
			int i=0;
			System.out.println(rs.toString());
			while(rs.next()){
				fidList.add(rs.getString("fid"));
			}

			for (String fid:fidList) {
				ps = conn.prepareStatement(sql2);
				ps.setString(1,fid);
				rs=ps.executeQuery();
				if(rs.next()){
					User user=new User();
					user.setId(rs.getString("id"));
					user.setName(rs.getString("name"));
					user.setDepartment(rs.getString("department"));
					user.setPhone(rs.getString("phone"));
					user.setEmail(rs.getString("email"));

					list.add(user);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			this.close();
		}
		return list;
	}

	public List<User> pullUser(){
		ConnectionPool connPool = ConnectionPoolUtils.GetPoolInstance();
		String sql="select * from user";
		List<User> list=new ArrayList<>();
		try {
			conn = connPool.getConnection();
			ps = conn.prepareStatement(sql);
			rs=ps.executeQuery();
			//List<String> fidList=new ArrayList<>();
			//int i=0;
			//System.out.println(rs.toString());
			while (rs.next()) {
				User user = new User();
				user.setId(rs.getString("id"));
				user.setName(rs.getString("name"));
				user.setDepartment(rs.getString("department"));
				user.setPhone(rs.getString("phone"));
				user.setEmail(rs.getString("email"));
				list.add(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			this.close();
		}
		return list;
	}

	private void close(){
		try {
			if(rs!=null){
				rs.close();
			}
			if(ps!=null){
				ps.close();
			}
			if(conn!=null){
				conn.close();
			}
		}catch (Exception e){
			e.printStackTrace();
		}

	}

}
