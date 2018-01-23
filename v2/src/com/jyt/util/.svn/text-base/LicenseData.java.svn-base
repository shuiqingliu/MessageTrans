package com.jyt.util;

import java.util.HashMap;
import java.util.Map;

public class LicenseData {
	public String user;
	public String connector;
	public String password;


	public String  ip;
	public String  ethernet;
	public String deadline;
	public String hostname;
	public String osname;
	public String osversion;

	public LicenseData()
	{
		user = "";
		connector = "";
		ip = "";
		ethernet = "";
		deadline = "";
		hostname = "";
		osname = "";
		osversion = "";
		password = "";
	}
	
	public String toString()
	{
		String field="[1]user:%1[2]connector:%2[3]ip:%3[4]ethernet:%4[5]osname:%5[6]osversion:%6[7]deadline:%7[8]hostname:%8[9]password:%9";
		String[] ss = new String[]{getUser(),getConnector(),getIp(),getEthernet(),getOsname(),getOsversion(),getDeadline(),getHostname(),getPassword()};
		String ret = ArgumentString.replace(field,ss);
		return ret;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUser() {
		return user;
	}


	public void setUser(String user) {
		this.user = user;
	}


	public String getConnector() {
		return connector;
	}


	public void setConnector(String connector) {
		this.connector = connector;
	}


	public String getIp() {
		return ip;
	}


	public void setIp(String ip) {
		this.ip = ip;
	}


	public String getEthernet() {
		return ethernet;
	}


	public void setEthernet(String ethernet) {
		this.ethernet = ethernet;
	}


	public String getDeadline() {
		return deadline;
	}


	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}


	public String getHostname() {
		return hostname;
	}


	public void setHostname(String hostname) {
		this.hostname = hostname;
	}


	public String getOsname() {
		return osname;
	}


	public void setOsname(String osname) {
		this.osname = osname;
	}


	public String getOsversion() {
		return osversion;
	}


	public void setOsversion(String osversion) {
		this.osversion = osversion;
	}


	public static LicenseData fromString(String s)
	{
		LicenseData ret = null;
		String[] ss = MyString.str_to_arr(s);
		Map<String,String> map = new HashMap<String,String>();
		if(ss.length==9)
		{
			for(int i=0;i<9;i++)
			{
				String str = ss[i];
				String[] ss2 = str.split(":");
				if(ss2.length==2)
				{
					map.put(ss2[0], ss2[1]);
				}
			}
			ret = new LicenseData();
			ret.setUser(map.get("user"));
			ret.setConnector(map.get("connector"));
			ret.setIp(map.get("ip"));
			ret.setEthernet(map.get("ethernet"));
			ret.setHostname(map.get("hostname"));
			ret.setOsname(map.get("osname"));
			ret.setOsversion(map.get("osversion"));
			ret.setDeadline(map.get("deadline"));
			ret.setPassword(map.get("password"));
		}
		return ret;
	}
	
	public boolean validate()
	{
		boolean ret = true;
		int error_code = 0;
		if(user==null){
			ret = false;
			error_code = 1;
		}
		else if(connector == null)
		{
			ret = false;
			error_code = 2;
		}		
		else if(ip==null)
		{
			ret = false;
			error_code = 3;
		}
		else if(ethernet == null)
		{
			ret = false;
			error_code = 4;
		}
		else if(osname == null)
		{
			ret = false;
			error_code = 5;
		}
		else if(osversion == null)
		{
			ret = false;
			error_code = 6;
		}
		else if(deadline==null)
		{
			ret = false;
			error_code = 7;			
		}
		else if(hostname==null)
		{
			ret = false;
			error_code = 8;			
		}
		else if(user.length()==0){
			ret = false;
			error_code = 9;
		}
		else if(connector.length()==0)
		{
			ret = false;
			error_code = 10;
		}
		return ret;
	}
	


}
