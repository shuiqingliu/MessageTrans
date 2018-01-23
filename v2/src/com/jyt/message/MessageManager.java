package com.jyt.message;

import java.util.List;

import com.jyt.util.ArgumentString;
import com.jyt.util.MyDate;
import com.jyt.util.MyPrint;
import com.jyt.util.MySerializable;
import com.jyt.util.NameValue;

/**
 * ��ʾ��Ϣ�Ĺ�������
 * <p>��������Ҫʵ�����й��ܣ�
 * 1��ע�����е�ʵ�����ƣ�
 * 2��ʵ�ֹ��������
 * @author yzq
 *
 */
public class MessageManager {
	/** ����ʵ��Ҳ��һ������Ŀͻ��� */
	public MessageServerTcpClient manager = null;
	/** ��Ϣ��������IP */
	public String server_ip;
	/** ��Ϣ�������ķ��������� */
	public String server_name;
	
	/** ���캯�� */
	public MessageManager(String server_ip,String server_name)
	{
		this.server_ip = server_ip;
		this.server_name = server_name;
		manager = new MessageServerTcpClient(server_ip,server_name,"manager");
		
	}

	/** �ӱ�׼�����϶���һ������ 
	 * @return ���ض������Ϣ�����в������س��ͻ�����Ϣ 
	 * */
	public static String read_line() {
		int c;
		char ch;
		String ret = "";

		try {
			StringBuffer sb = new StringBuffer();
			while (true) {
				c = System.in.read();
				ch = (char) c;
				if (ch == '\r')
					continue;
				if (ch == '\n')
					break;
				sb.append(ch);
			}
			ret = sb.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * �ܹ�֧�ŵ����������
	 * 1����ѯ���е�ʵ�����ơ�������ʵ�����ơ�Ŀǰ�е���Ϣ���еĳ��ȡ�
	 * 2����ѯһ��ʵ�����ϸ��Ϣ��������Ϣ�ľ������ݡ�
	 * 3����ѯʵ���������Ϣ��
	 * 4������һ��ʵ���������Ϣ��
	 * 5������ϵͳ��������Ϣ��
	 * 6��ɱ�����еĿͻ��ˡ�
	 * 7���˳���
	 */
	public String[] cmd_names = new String[] { 
			"lookup_all_entities",
			"lookup_one_entity",
			"lookup_entity_conf",
			"set_entity_conf",
			"lookup_system_conf",
			"set_system_conf",
			"kill_all_entities",
			"exit"
	};

	/**
	 * ��ʾÿ������Ľ�����Ϣ��
	 */
	public String[] cmd_explanations = new String[] { 
			"�鿴���е�ʵ�����",
			"�鿴һ��ʵ���������Ϣ����",
			"�鿴һ��ʵ���������Ϣ",
			"����һ��ʵ�������",
			"�鿴ϵͳ����",
			"����ϵͳ����",
			"ɱ�����е�ʵ��",
			"�˳�",
	};

	/**
	 * ��ʾһ��������﷨��ʽ����help��ʱ�����ʾ��Щ��Ϣ��
	 */
	public String[] cmd_definitions = new String[] {
			"lookup_all_entities",
			"lookup_one_entity <entity_name>",
			"lookup_entity_conf <entity_name>",
			"set_entity_conf <entity_name> <property_name> <property_value>",
			"lookup_system_conf",
			"set_system_conf <property_name> <property_value>",
			"kill_all_entities",
			"exit"
	};
	
	/**
	 * �����������
	 * @param cmd_arr ����������������б�Ŀǰ������Ϣû�����塣
	 * @return �����ַ��������ַ����а������������š�����Ľ�����Ϣ������Ķ��塣
	 */
	public String process_cmd_help(String[] cmd_arr)
	{
		String ret = "";
		for(int i=0;i<cmd_names.length;i++)
		{
			String field = "[%1]\t%2\r\n\t%3\r\n\t%4\r\n";
			String[] ss = new String[]{i+1+"",cmd_names[i],cmd_explanations[i],cmd_definitions[i]};
			String s = ArgumentString.replace(field,ss);
			ret = ret+s;
		}
		return ret;
	}
	
	/**
	 * ������������������������������Ƽ�����������д���
	 * @param cmd_arr ����������������б�
	 */
	public void process_main(String[] cmd_arr) {
		String head = cmd_arr[0];
		if (head.equals("exit")) {
			System.out.println("�ټ�!");
			System.exit(0);
		}
		if(head.equals("help"))
		{
			String result = process_cmd_help(cmd_arr);
			System.out.println(result);
			return;
		}
		if (head.equals("lookup_all_entities")) {
			String result = process_cmd_lookup_all_entities(cmd_arr);
			System.out.println(result);
			return;
		}
		if (head.equals("lookup_one_entity")) {
			String result = process_cmd_lookup_one_entity(cmd_arr);
			System.out.println(result);
			return;
		}
		if (head.equals("set_entity_conf")) {
			String result = process_cmd_set_entity_conf(cmd_arr);
			System.out.println(result);
			return;
		}
		if (head.equals("lookup_system_conf")) {
			String result = process_cmd_lookup_system_conf(cmd_arr);
			System.out.println(result);
			return;
		}
		if (head.equals("set_system_conf")) {
			String result = process_cmd_set_system_conf(cmd_arr);
			System.out.println(result);
			return;
		}
		if (head.equals("lookup_entity_conf")) {
			String result = process_cmd_lookup_entity_conf(cmd_arr);
			System.out.println(result);
			return;
		}
		if (head.equals("kill_all_entities")) {
			String result = process_cmd_kill_all_entities(cmd_arr);
			System.out.println(result);
			return;
		}
		MyPrint.print("������û��ִ��", new Exception());

	}
	
	/**
	 * ɱ�����еĿͻ��ˡ�
	 * <p>������������������һ��ɱ�����пͻ��˵�Ҫ��
	 * <p>�������������еĿͻ��˷���һ������ͻ��˽��ܵ�������ͻ��Զ��˳���
	 */
	public void kill_all()
	{
		List<String> list = manager.return_all_entity();
		for(String e:list)
		{
			byte[] bs = MySerializable.object_bytes("");
			Message m = new Message("manager",e,"kill",bs);
			manager.send(m);
		}
	}	
	
	/**
	 * ��ɲ�ѯ����ʵ�������Ĵ������
	 * @param cmd_arr ����������������б�
	 * @return ���ش���Ľ�����ý���ǿ���ֱ����ʾ�ڽ����ϵ����ݡ�
	 */
	public String process_cmd_lookup_all_entities(String[] cmd_arr)
	{
		String ret = "Ŀǰû��ʵ���¼��ϵͳ�У�";
		List<String> info_list = manager.return_all_entity_info();
		if(info_list.size()>0)
		{
			int count = 1;
			ret = "";
			String ret_ascii;
			for(String str:info_list)
			{
				if((count+0)%4 == 0)
				{
					ret_ascii = "\n";
				}
				else{
					if(str.length()<8)
						ret_ascii = "\t\t";
					else
						ret_ascii = "\t";
				}
				String one_entity_info_field = "%1%2";
				String[] ss = new String[]{str,ret_ascii};
				String result = ArgumentString.replace(one_entity_info_field,ss);
				ret = ret+result;
				count++;
			}
		}
		return ret;
	}
	
	/**
	 * ��ɲ�ѯһ��ʵ������������Ĵ������
	 * @param cmd_arr ����������������б�
	 * @return ���ش���Ľ�����ý���ǿ���ֱ����ʾ�ڽ����ϵ����ݡ�
	 */	
	
	public String process_cmd_lookup_one_entity(String[] cmd_arr)
	{
		String ret = "����Ϸ���";
		if(cmd_arr.length==2)
		{		
			ret ="";
			String entity_name = cmd_arr[1];
			List<Message> list = manager.peek_all_message(entity_name);
			if(list == null)
			{
				ret = "message is null";
			}
			else{
				for(int i=0;i<list.size();i++)
				{
					Message message = list.get(i);
					String time = MyDate.f3(message.getCreated());
					String id = message.getId();
					String size = message.getContent().length+"";
					String from = message.getFrom();
					String to = message.getTo();
					String type = message.getType();
					String info_field = "(%1)type(%2)from(%3)to(%4)size(%5)time(%6)\n";
					String[] info_ss = new String[]{i+"",type,from,to,size,time};
					String result =  ArgumentString.replace(info_field,info_ss);
					ret = ret + result;
				}
				
			}
		}
		return ret;
	}
	

	/**
	 * ��ɲ�ѯϵͳ���õĴ������
	 * @param cmd_arr ����������������б�
	 * @return ���ش���Ľ�����ý���ǿ���ֱ����ʾ�ڽ����ϵ����ݡ�
	 */		
	public String process_cmd_lookup_system_conf(String[] cmd_arr)
	{
		String ret = "����Ϸ���";
		if(cmd_arr.length==1)
		{
			List<NameValue> list = manager.get_system_conf();
			ret = "";
			if(list!=null)
			{
				for(int i=0;i<list.size();i++)
				{
					String field = "[%1] %2=%3\n";
					String[] ss = new String[]{i+"",list.get(i).name,list.get(i).value};
					String result = ArgumentString.replace(field,ss);
					ret = ret + result;
				}
				
			}
		}
		return ret;
	}
	
	/**
	 * ��ɲ�ѯʵ�����õĴ������
	 * @param cmd_arr ����������������б�
	 * @return ���ش���Ľ�����ý���ǿ���ֱ����ʾ�ڽ����ϵ����ݡ�
	 */		
	public String process_cmd_lookup_entity_conf(String[] cmd_arr)
	{
		String ret = "����Ϸ���";
		if(cmd_arr.length==2)
		{
			String entity_name = cmd_arr[1];
			List<NameValue> list = manager.get_entity_conf(entity_name);
			ret = "";
			if(list==null)
			{
				ret = "ʵ�岻���ڣ�";
				return ret;
			}
			for(int i=0;i<list.size();i++)
			{
				String field = "[%1] %2=%3\n";
				String[] ss = new String[]{i+"",list.get(i).name,list.get(i).value};
				String result = ArgumentString.replace(field,ss);
				ret = ret + result;
			}
		}
		return ret;
	}	
	
	/**
	 * �������ϵͳ���õĴ������
	 * @param cmd_arr ����������������б�
	 * @return ���ش���Ľ�����ý���ǿ���ֱ����ʾ�ڽ����ϵ����ݡ�
	 */		
	public String process_cmd_set_system_conf(String[] cmd_arr)
	{
		String ret = "����Ϸ���";
		if(cmd_arr.length==3)
		{		
			String name = cmd_arr[1];
			String value = cmd_arr[2];
			manager.set_system_conf(name, value);
			ret = "���óɹ���";
		}
		return ret;
	}
	
	/**
	 * �������ʵ�����õĴ������
	 * @param cmd_arr ����������������б�
	 * @return ���ش���Ľ�����ý���ǿ���ֱ����ʾ�ڽ����ϵ����ݡ�
	 */		
	public String process_cmd_set_entity_conf(String[] cmd_arr)
	{
		String ret = "����Ϸ���";
		if(cmd_arr.length==4)
		{		
			String entity_name = cmd_arr[1];
			String name = cmd_arr[2];
			String value = cmd_arr[3];
			int ret_code = manager.set_entity_conf(entity_name, name, value);
			if(ret_code==0)
				ret = "���óɹ���";
			else
				ret = "ʵ�岻���ڣ�";
		}
		return ret;
	}	

	/**
	 * ���ɱ������ʵ��Ĵ������
	 * @param cmd_arr ����������������б�
	 * @return ���ش���Ľ�����ý���ǿ���ֱ����ʾ�ڽ����ϵ����ݡ�
	 */
	public String process_cmd_kill_all_entities(String[] cmd_arr)
	{
		kill_all();
		String ret = "�Ѿ�ɱ�������еĿͻ���ʵ�壡";
		return ret;
	}

	/**
	 * ���봦�����������
	 * <p>��Ҫ���������Ϣ�������ֱ���Ĵ��������Ϳ����ڿ���̨��ʾ������Ϣ��
	 * <p>������տո�ֽ�Ϊ�ַ����飬����process_main���д���
	 * @see process_main(String[]);
	 */
	public void input_main() {
		System.out.print("==>");
		String cmd = read_line().trim();
		try {
			cmd = new String(cmd.getBytes("ISO-8859-1"), "GBK");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String[] cmd_arr = cmd.split(" ");
		process_main(cmd_arr);
	}

	/**
	 * ������
	 * <p>�����ʽΪ��command server_ip server_name
	 * <p>ִ�з�ʽΪ��
	 * <li>ѭ���ؽ���ÿ�����룬Ȼ��������
	 * <li>�������end���˳�
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("command server_ip server_name");
			System.exit(1);
		}
		String server_ip = args[0];
		String server_name = args[1];

		MessageManager m = new MessageManager(server_ip, server_name);
		// m.build_menu();
		while (true) {
			m.input_main();
		}

	}

}
