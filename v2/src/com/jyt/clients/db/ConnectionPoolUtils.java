package com.jyt.clients.db;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;

import com.mysql.jdbc.Statement;

/**
 * ���ӳع����࣬����Ψһ��һ�����ݿ����ӳض���,����ģʽ
 *
 */
public class ConnectionPoolUtils {
	private ConnectionPoolUtils(){};//˽�о�̬����
	private static ConnectionPool poolInstance = null;
	public static ConnectionPool GetPoolInstance(){
		if(poolInstance == null) {
			poolInstance = new ConnectionPool(					 
					"com.mysql.jdbc.Driver",				 
					"jdbc:mysql://localhost:3306/im?useUnicode=true&characterEncoding=utf-8",				 
					"root", "pwalan");
			try {
				poolInstance.createPool();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return poolInstance;
	}
}

