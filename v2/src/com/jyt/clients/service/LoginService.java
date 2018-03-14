package com.jyt.clients.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.gson.JsonObject;
import com.jyt.clients.db.ConnectionPool;
import com.jyt.clients.db.ConnectionPoolUtils;
import com.jyt.clients.model.User;

public class LoginService {
	public static String isLoginSuccess(User user){
		ConnectionPool connPool=ConnectionPoolUtils.GetPoolInstance();//����ģʽ�������ӳض���  
		String sql="select * from user where name='"+user.getName()+"' and password='"+user.getPasswd()+"'";
		try {
			Connection conn = connPool.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next()){
				return "{'success':'yes'," +
						"'uid':'"+rs.getString("id")+"'," +
						"'uname':'"+rs.getString("name")+"'," +
						"'udepartment':'"+rs.getString("department")+"'," +
						"'uphone':'"+rs.getString("phone")+"'," +
						"'uemail':'"+rs.getString("email")+"'}";
			}else{
				return "{'success':'no'}";
			}

		} catch (Exception e) {
			e.printStackTrace();
			return "{'success':'no'}";
		}

	}
}
