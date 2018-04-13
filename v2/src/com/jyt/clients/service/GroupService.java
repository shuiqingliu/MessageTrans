package com.jyt.clients.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jyt.clients.db.ConnectionPool;
import com.jyt.clients.db.ConnectionPoolUtils;
import com.jyt.clients.model.Group;
import com.jyt.clients.model.User;

public class GroupService {
	private Connection conn=null;
	private PreparedStatement ps=null;
	private ResultSet rs=null;
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
		//String memstr = group.getUid();

		List<String> members = group.getMembers();
		String memstr = members.get(0);
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

	public List<Group> pullGroup(String id){
		ConnectionPool connPool = ConnectionPoolUtils.GetPoolInstance();
		String sql1="select group_id from t_group";
		String sql2="select * from t_group where group_id=? limit 1";
		List<Group> list=new ArrayList<>();
		try {
			conn = connPool.getConnection();
			ps = conn.prepareStatement(sql1);
			rs=ps.executeQuery();
			List<String> groupidList=new ArrayList<>();
			int i=0;
			System.out.println(rs.toString());
			while(rs.next()){
				groupidList.add(rs.getString("group_id"));
			}

			for (String fid:groupidList) {
				ps = conn.prepareStatement(sql2);
				ps.setString(1,fid);
				rs=ps.executeQuery();


				if(rs.next()){
					String[] groupList = rs.getString("members").split("、");
					List<String> groupMemberList=Arrays.asList(groupList);
					if (groupMemberList.contains(id)) {
						Group group = new Group();
						group.setGid(rs.getString("group_id"));
						group.setGname(rs.getString("group_name"));
						group.setMember(rs.getString("members"));
						list.add(group);
//					User user=new User();
//					user.setId(rs.getString("id"));
//					user.setName(rs.getString("name"));
//					user.setDepartment(rs.getString("department"));
//					user.setPhone(rs.getString("phone"));
//					user.setEmail(rs.getString("email"));
//
//					list.add(user);
					}
				}
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
