package com.jyt.clients.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.jyt.clients.db.ConnectionPool;
import com.jyt.clients.db.ConnectionPoolUtils;
import com.jyt.clients.model.MessageRecord;
import com.jyt.clients.model.User;

public class SearchService {
	/**
	 * 通过消息内容模糊匹配出消息
	 * @param uid
	 * @param content
	 * @return
	 */
	public static List<MessageRecord> searchRecords(String uid,String content){
		List<MessageRecord> result=new ArrayList<MessageRecord>();
		ConnectionPool connPool = ConnectionPoolUtils.GetPoolInstance();
		String sql = "SELECT * FROM message m WHERE m.from='"+uid+"' AND m.content LIKE '%"+content+"%'";
		try {
			Connection conn = connPool.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				MessageRecord msg=new MessageRecord();
				String type=rs.getString("type");
				if(type.equals("msg")){
					msg.setContent(rs.getString("content"));
					msg.setFrom(UserInfoService.fetchUserInfo(rs.getString("from")).getName());
					msg.setTo(UserInfoService.fetchUserInfo(rs.getString("to")).getName());
					msg.setTime(rs.getDate("time"));
					msg.setType(type);
					result.add(msg);
				}else if(type.equals("groupMsg")){
					msg.setContent(rs.getString("content"));
					msg.setFrom(UserInfoService.fetchUserInfo(rs.getString("from")).getName());
					msg.setTo(GroupService.getGroup(rs.getString("to")).getGname());
					msg.setTime(rs.getDate("time"));
					msg.setType(type);
					result.add(msg);
				}
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		}
	}
}
