package com.jyt.clients.service;

import java.sql.Connection;
import java.sql.Statement;

import com.jyt.clients.db.ConnectionPool;
import com.jyt.clients.db.ConnectionPoolUtils;

public class FriendsService {
	public static void addFri(String uid, String fid) {
		ConnectionPool connPool = ConnectionPoolUtils.GetPoolInstance();
		String sql1 = "INSERT INTO friends(uid, fid) VAlUES ('" + uid + "', '"
				+ fid + "')";
		String sql2 = "INSERT INTO friends(uid, fid) VAlUES ('" + fid + "', '"
				+ uid + "')";
		try {
			Connection conn = connPool.getConnection();
			Statement stmt = conn.createStatement();
			stmt.execute(sql1);
			stmt.execute(sql2);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static boolean delFri(String uid, String fid) {
		ConnectionPool connPool = ConnectionPoolUtils.GetPoolInstance();

		String sql = "DELETE FROM friends WHERE uid = '" + uid + "' AND fid = '"
				+ fid + "'";
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

}
