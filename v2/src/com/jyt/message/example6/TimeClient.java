package com.jyt.message.example6;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.jyt.message.Message;
import com.jyt.message.MessageListener;
import com.jyt.message.MessageServerTcpClient;
import com.jyt.util.ArgumentString;
import com.jyt.util.MyPrint;
import com.jyt.util.MySerializable;

public class TimeClient extends MessageServerTcpClient {

	public final int COUNT = 100;
	public long[] l = new long[COUNT];
	public int num = 0;

	public TimeClient(String server_ip, String server_name) {
		super(server_ip, server_name, "client");
	}

	public class RequestListener implements MessageListener {
		TimeClient client = null;

		public RequestListener(TimeClient client) {
			this.client = client;
		}

		public void change_time(long delta)
		{
			SimpleDateFormat df = new SimpleDateFormat(
					"yyyy-MM-dd#HH:mm:ss");// 设置日期格式
			String current_clock = df.format(new Date());
			System.out.println("current_time = " + current_clock);

			Date new_clock = new Date();

			try {
				new_clock = df.parse(current_clock);
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			long time = (long) (new_clock.getTime() + delta);
			new_clock.setTime(time);
			String time_date = df.format(new_clock);
			String[] splits = time_date.split("#");
			String new_date = splits[0];
			String new_time = splits[1];
			
			String cmd = "  cmd /c  %1  %2";

			String osName = System.getProperty("os.name");
			try {
				if (osName.matches("^(?i)Windows.*$")) {// Window 系统
					String[] info_time = new String[] { "time", new_time };
					String cmd_time = ArgumentString
							.replace(cmd, info_time);
					Runtime.getRuntime().exec(cmd_time);
					
					String[] info_date = new String[] { "date", new_date };
					String cmd_date = ArgumentString
							.replace(cmd, info_date);
					Runtime.getRuntime().exec(cmd_date);
					
					
					
				} else {// Linux 系统
					cmd = "  date -s 20090326";
					Runtime.getRuntime().exec(cmd);
					// 格式 HH:mm:ss
					cmd = "  date -s 22:35:00";
					Runtime.getRuntime().exec(cmd);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		}
		
		public void messagePerformed(Message message) {
			Date date = message.getCreated();
			byte[] bs = message.getContent();
			TimeStruct ts = (TimeStruct) MySerializable.bytes_object(bs);
			ts.setT4(new Date().getTime());
			l[num] = ts.get_middle_range();
			num++;
			if (num == COUNT) {
				long sum = 0;
				for (int i = 0; i < COUNT; i++) {
					sum = sum + l[i];
				}
				// float seconds = ((float)sum/COUNT)/1000;
				long ms_seconds = (sum / COUNT);
				change_time(ms_seconds);

				MyPrint.print(
						"The time difference is " + ms_seconds + " ms_seconds.",
						new Exception());
			} else {
				send_msg(client);
			}
		}
	}

	public void init() {
		addListener("response", new RequestListener(this));
	}

	public static void send_msg(TimeClient client) {
		Message request = new Message();
		request.setFrom("client");
		request.setTo("server");
		request.setType("request");
		TimeStruct ts = new TimeStruct();
		ts.setT1(new Date().getTime());
		ts.setT2(0);
		ts.setT3(0);
		ts.setT4(0);
		byte[] bs2 = MySerializable.object_bytes(ts);
		request.setContent(bs2);
		client.send(request);
	}

	public static void main(String[] args) {

		if (args.length != 2) {
			System.out.println("command server_ip server_name");
			System.exit(1);
		}
		String server_ip = args[0];
		String server_name = args[1];
		MyPrint.print("TimeClient begin...", new Exception());
		TimeClient time_client = new TimeClient(server_ip, server_name);
		time_client.init();
		time_client.work();

		send_msg(time_client);
//		time_client.num++;

	}

}
