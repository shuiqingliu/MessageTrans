package com.jyt.clients.model;

public class Friend {
	private String uid;
	private String fid;
	
	public Friend() {
	}
	
	public Friend(String uid, String fid) {
		super();
		this.uid = uid;
		this.fid = fid;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}
	
	
}
