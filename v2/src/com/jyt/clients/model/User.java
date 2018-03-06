package com.jyt.clients.model;

public class User {
	private String id;
	private String name;
	private String passwd;
	private String avatar;
	private String department;
	private String phone;
	private String email;
	
	public User(){
	}
	
	public User(String id, String name, String avater, String department,
			String phone, String email) {
		super();
		this.id = id;
		this.name = name;
		this.avatar = avater;
		this.department = department;
		this.phone = phone;
		this.email = email;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avater) {
		this.avatar = avater;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	
	
}
