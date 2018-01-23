package com.jyt.message.example6;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.jyt.util.ArgumentString;


public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd#HH:mm:ss");//设置日期格式
		System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
		String date_time = df.format(new Date());
		String[] splits = date_time.split("#");
		String date = splits[0];
		String time = splits[1];
		System.out.println(date);
		System.out.println(time);
		
		String cmd = "  cmd /c  %1  %2";  
		String[] info_date = new String[]{"date",date};
		String cmd_date =  ArgumentString.replace(cmd,info_date);
		
		String[] info_time = new String[]{"time",time};
		String cmd_time =  ArgumentString.replace(cmd,info_time);
		
		System.out.println(cmd_date);
		System.out.println(cmd_time);
		
	}

}
