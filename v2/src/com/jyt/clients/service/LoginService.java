package com.jyt.clients.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.jyt.clients.db.ConnectionPool;
import com.jyt.clients.db.ConnectionPoolUtils;
import com.jyt.clients.model.User;

public class LoginService {
	public static boolean isLoginSuccess(User user){
		ConnectionPool connPool=ConnectionPoolUtils.GetPoolInstance();//单例模式创建连接池对象  
		String sql="select * from user where uid='"+user.getId()+"' and passwd='"+user.getPasswd()+"'";
		try {
			Connection conn = connPool.getConnection();
			Statement stmt = conn.createStatement();  
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next()){
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
