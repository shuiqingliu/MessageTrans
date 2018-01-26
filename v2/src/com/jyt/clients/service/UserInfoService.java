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
		User user = null;
		return user;
	}
	
	public static void modifyUserInfo(User user){
		
	}
	
	public static List<String> getAllUsers(){
		ConnectionPool connPool=ConnectionPoolUtils.GetPoolInstance();//单例模式创建连接池对象  
		List<String> uids=new ArrayList<String>();
		String sql="select * from user";
		try {
			Connection conn = connPool.getConnection();
			Statement stmt = conn.createStatement();  
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){
				uids.add(rs.getString("id"));
			}
			return uids;
		} catch (Exception e) {
			e.printStackTrace();
			return uids;
		}
	}
}
