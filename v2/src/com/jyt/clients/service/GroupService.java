package com.jyt.clients.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import com.jyt.clients.db.ConnectionPool;
import com.jyt.clients.db.ConnectionPoolUtils;
import com.jyt.clients.model.Group;

public class GroupService {

	public static Group getGroup(String gid) {
		Group group = new Group();
		ConnectionPool connPool = ConnectionPoolUtils.GetPoolInstance();// 单例模式创建连接池对象
		String sql = "SELECT * FROM t_group WHERE group_id='" + gid + "'";
		try {
			Connection conn = connPool.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				String members=rs.getString("members");
				String[] m=members.split("、");
				group.setMembers(Arrays.asList(m));
				return group;
			} else {
				return group;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return group;
		}
	}

	public static void createGroup(Group group) {
		ConnectionPool connPool = ConnectionPoolUtils.GetPoolInstance();// 单例模式创建连接池对象
		String memstr = group.getUid();
		List<String> members = group.getMembers();
		for (String m : members) {
			memstr += ("、" + m);
		}
		String sql = "INSERT INTO t_group VALUES('" + group.getGid() + "', '"
				+ group.getGname() + "', '" + memstr + "')";
		try {
			Connection conn = connPool.getConnection();
			Statement stmt = conn.createStatement();
			stmt.execute(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void pullMember(Group group) {
		List<String> members = group.getMembers();
		String memstr = "";
		for (String m : members) {
			memstr += (m + "、");
		}
		memstr += group.getMid();
		String sql = "UPDATE t_group SET members='" + memstr + "' WHERE group_id='"+group.getGid()+"'";
		try {
			ConnectionPool connPool = ConnectionPoolUtils.GetPoolInstance();
			Connection conn = connPool.getConnection();
			Statement stmt = conn.createStatement();
			stmt.execute(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void modifyGroupName(Group group) {
		String sql = "UPDATE t_group SET group_name='" + group.getGname() + "' WHERE group_id='"+group.getGid()+"'";
		try {
			ConnectionPool connPool = ConnectionPoolUtils.GetPoolInstance();
			Connection conn = connPool.getConnection();
			Statement stmt = conn.createStatement();
			stmt.execute(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void delMember(Group group) {
		List<String> members = group.getMembers();
		String memstr = "";
		for (String m : members) {
			if(!m.equals(group.getMid())){
				memstr += (m + "、");
			}	
		}
		memstr=memstr.substring(0, memstr.length() - 1);
		String sql = "UPDATE t_group SET members='" + memstr + "' WHERE group_id='"+group.getGid()+"'";
		try {
			ConnectionPool connPool = ConnectionPoolUtils.GetPoolInstance();
			Connection conn = connPool.getConnection();
			Statement stmt = conn.createStatement();
			stmt.execute(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void quitGroup(Group group) {
		List<String> members = group.getMembers();
		String memstr = "";
		for (String m : members) {
			if(!m.equals(group.getUid())){
				memstr += (m + "、");
			}	
		}
		memstr=memstr.substring(0, memstr.length() - 1);
		String sql = "UPDATE t_group SET members='" + memstr + "' WHERE group_name='"+group.getGname()+"'";
		try {
			ConnectionPool connPool = ConnectionPoolUtils.GetPoolInstance();
			Connection conn = connPool.getConnection();
			Statement stmt = conn.createStatement();
			stmt.execute(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
