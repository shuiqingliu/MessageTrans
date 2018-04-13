package com.jyt.clients.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
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

        //System.out.println("fid: "+gid);
		String sql = "SELECT * FROM group_user WHERE groupid='" + gid + "'";
		String sql1 = "SELECT * FROM groups WHERE groupid='" + gid + "'";

		try {
			Connection conn = connPool.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			List<String> m = new ArrayList<String>();
				while (rs.next()) {

					String userId = rs.getString("userid");
					m.add(userId);
				}
			    group.setMembers(m);
			    ResultSet rs1 = stmt.executeQuery(sql1);
			    if (rs1.next()) {//rs.getString 之前要rs.next()
					group.setUid(rs1.getString("manager"));
					group.setGname(rs1.getString("groupname"));
				}
			conn.close();
			stmt.close();
			rs.close();


		} catch (Exception e) {
			e.printStackTrace();
			return group;

		}
		return group;
	}

	public static Group getGroupByName(String gname) {
		Group group = new Group();
		ConnectionPool connPool = ConnectionPoolUtils.GetPoolInstance();// 单例模式创建连接池对象



		String sql1 = "SELECT * FROM groups WHERE groupname='" + gname + "'";
		try {
			Connection conn = connPool.getConnection();
			Statement stmt = conn.createStatement();


			List<String> m = new ArrayList<String>();
			ResultSet rs1 = stmt.executeQuery(sql1);
			if (rs1.next()) {//rs.getString 之前要rs.next()
				group.setGid(rs1.getString("groupid"));
				group.setUid(rs1.getString("manager"));
				group.setGname(rs1.getString("groupname"));
			}
			String sql = "SELECT * FROM group_user WHERE groupid='" + rs1.getString("groupid") + "'";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {

				String userId = rs.getString("userid");
				m.add(userId);

			}
			group.setMembers(m);


		} catch (Exception e) {
			e.printStackTrace();
			return group;
		}
		return group;
	}

	public static void createGroup(Group group) {
		ConnectionPool connPool = ConnectionPoolUtils.GetPoolInstance();// 单例模式创建连接池对象
		//String memstr = group.getUid();


		String sql = "INSERT INTO groups (groupname,groupavatar,manager) VALUES('" + group.getGname() + "', '" + group.getAvatar() + "', '"+ group.getUid()+"')";
		String sql1 ="SELECT * FROM groups WHERE groupname='" + group.getGname() + "'";
		String[] member= group.getMember().split("、");

		List<String> members = Arrays.asList(member);

//		String memstr = members.get(0);
//		for (String m : members) {
//			memstr += ("、" + m);
//		}
		try {
			Connection conn = connPool.getConnection();
			Statement stmt = conn.createStatement();
			stmt.execute(sql);
			ResultSet rs = stmt.executeQuery(sql1);
			while (rs.next()) {

				String gid = rs.getString("groupid");
				group.setGid(gid);
			}
			for (String m:members){
				stmt.execute("INSERT INTO group_user VALUES('" + group.getGid() + "', '" + m+"')");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void pullMember(Group group) {
		List<String> members = group.getMembers();
//		String memstr = "";
//		for (String m : members) {
//			memstr += (m + "、");
//		}
//		memstr += group.getMid();
		String sql = "INSERT INTO group_user VALUES('" + group.getGid() + "', '" + group.getMid() +"')";
		try {
			ConnectionPool connPool = ConnectionPoolUtils.GetPoolInstance();
			Connection conn = connPool.getConnection();
			Statement stmt = conn.createStatement();
			stmt.execute(sql);
			conn.close();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void modifyGroupName(Group group) {
		String sql = "UPDATE groups SET groupname='" + group.getGname() + "' WHERE groupid='"+group.getGid()+"'";
		try {
			ConnectionPool connPool = ConnectionPoolUtils.GetPoolInstance();
			Connection conn = connPool.getConnection();
			Statement stmt = conn.createStatement();
			stmt.execute(sql);
			conn.close();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public static void delMember(Group group) {
		String gid = group.getGid();
		String mid = group.getMid();
		String sql = "DELETE FROM group_user WHERE groupid = '" + gid + "' AND userid = '" + mid + "'";
		try {
			ConnectionPool connPool = ConnectionPoolUtils.GetPoolInstance();
			Connection conn = connPool.getConnection();
			Statement stmt = conn.createStatement();
			stmt.execute(sql);
			conn.close();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void quitGroup(Group group) {
		//List<String> members = group.getMembers();
		//String memstr = "";
//		for (String m : members) {
//			if(!m.equals(group.getUid())){
//				memstr += (m + "、");
//			}
//		}
		//memstr=memstr.substring(0, memstr.length() - 1);
		String gid = group.getGid();
		String mid = group.getMid();
		String sql = "DELETE FROM group_user WHERE groupid = '" + gid + "' AND userid = '" + mid + "'";
		try {
			ConnectionPool connPool = ConnectionPoolUtils.GetPoolInstance();
			Connection conn = connPool.getConnection();
			Statement stmt = conn.createStatement();
			stmt.execute(sql);
			conn.close();
			stmt.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Group> pullGroup(String id){
		ConnectionPool connPool = ConnectionPoolUtils.GetPoolInstance();
		String sql1="select groupid from groups";
		String sql2="select * from groups where groupid=? limit 1";


		List<Group> list=new ArrayList<>();
		try {
			conn = connPool.getConnection();
			ps = conn.prepareStatement(sql1);
			rs=ps.executeQuery();
			List<String> groupidList=new ArrayList<>();
			int i=0;
			//System.out.println(rs.toString());
			while(rs.next()){

				groupidList.add(rs.getString("groupid"));
			}

			int num = 0;
			for (String fid:groupidList) {
				ps = conn.prepareStatement(sql2);
				ps.setString(1,fid);
				rs=ps.executeQuery();




				if(rs.next()){
					num++;
					System.out.println("num: "+num);
					Group groupMembers = getGroup(fid);
//					String[] groupList = rs.getString("members").split("、");
//					List<String> groupMemberList=Arrays.asList(groupList);
					List<String> groupMemberList=groupMembers.getMembers();
					String groupMemberListString =groupMembers.getUid();
					for (String m:groupMemberList){
						if (m.equals(groupMembers.getUid())) {
							continue;
						}
						groupMemberListString=groupMemberListString+"、"+m;

					}

					if (groupMemberList.contains(id)) {
						Group group = new Group();
						group.setGid(rs.getString("groupid"));
						group.setGname(rs.getString("groupname"));
						group.setMember(groupMemberListString);
						list.add(group);
					}
				}

			}

		}
		catch (Exception e) {
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
