package com.jyt.clients.model;

import java.util.List;

public class Group {

	private String gid;  //群组id
	private String uid;  //群管理员id
	private String gname;  //群名称
	private String mid;  //加群或退群的用户的id
	private List<String> members;  //群里所有用户的id
	
	public Group() {
	}

	public Group(String gid, String uid, String gname, List<String> members) {
		super();
		this.gid = gid;
		this.uid = uid;
		this.gname = gname;
		this.members = members;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getGname() {
		return gname;
	}

	public void setGname(String gname) {
		this.gname = gname;
	}

	public List<String> getMembers() {
		return members;
	}

	public void setMembers(List<String> members) {
		this.members = members;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}
	

}
