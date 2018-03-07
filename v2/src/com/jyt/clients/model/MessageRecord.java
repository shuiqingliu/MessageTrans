package com.jyt.clients.model;

import java.util.Date;

public class MessageRecord {
	private String uid;
	private String from;
	private String to;
	private String type;
	private Date time;
	private String content;
	
	public MessageRecord(){
		
	}

	public MessageRecord(String from, String to, String type, Date time,
			String content) {
		super();
		this.from = from;
		this.to = to;
		this.type = type;
		this.time = time;
		this.content = content;
	}



	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}
	
	
	
}
