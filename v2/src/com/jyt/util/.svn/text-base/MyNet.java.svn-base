package com.jyt.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.net.InetAddress;

public class MyNet {
	public static String get_local_ip() {
		String ret = null;
		try {
			InetAddress addr = InetAddress.getLocalHost();
			ret = addr.getHostAddress();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static String build_new_ip_name(String ip) {
		String[] ss = ip.split("\\.");
		String new_ip_str = "IP_";
		for (int i = 0; i < 4; i++) {
			new_ip_str = new_ip_str + ss[i];
			if (i != 3) {
				new_ip_str = new_ip_str + "_";
			}
		}
		return new_ip_str;
	}

	public static String get_ethernet_address() throws Exception {
		String line;
		String ret = null;
		Process pro = Runtime.getRuntime().exec("cmd /c ipconfig /all");
		BufferedReader buf = new BufferedReader(new InputStreamReader(pro
				.getInputStream()));
		while ((line = buf.readLine()) != null) {
			if (line.indexOf("ÎïÀíµØÖ·") != -1
					|| line.indexOf("Physical Address") != -1) {
				ret = line.substring(line.indexOf(":") + 2);
				break;
			}
		}
		return ret;
	}

	public static String get_host_name() throws Exception
	{
		String ret = InetAddress.getLocalHost().getHostName();
		return ret;
	}
	
	public static String get_os_name() throws Exception
	{
		String os = System.getProperty("os.name");
		return os;
	}
	
	public static String get_os_version() throws Exception
	{
		OperatingSystemMXBean osmxb = (OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
		String ret = osmxb.getVersion();
		return ret;
	}
	
	public static void main(String[] args) throws Exception {
		String ip = get_local_ip();
		String ethernet = get_ethernet_address();
		String host_name = get_host_name();
		String os_name = get_os_name();
		String os_version = get_os_version();
		System.out.println(ip);
		System.out.println(ethernet);
		System.out.println(host_name);
		System.out.println(os_name);
		System.out.println(os_version);
		
	}

}
