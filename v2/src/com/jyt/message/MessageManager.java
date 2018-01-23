package com.jyt.message;

import java.util.List;

import com.jyt.util.ArgumentString;
import com.jyt.util.MyDate;
import com.jyt.util.MyPrint;
import com.jyt.util.MySerializable;
import com.jyt.util.NameValue;

/**
 * 表示消息的管理器。
 * <p>管理器主要实现下列功能：
 * 1、注册所有的实体名称；
 * 2、实现管理的命令
 * @author yzq
 *
 */
public class MessageManager {
	/** 管理实体也是一个特殊的客户端 */
	public MessageServerTcpClient manager = null;
	/** 消息服务器的IP */
	public String server_ip;
	/** 消息服务器的服务器名称 */
	public String server_name;
	
	/** 构造函数 */
	public MessageManager(String server_ip,String server_name)
	{
		this.server_ip = server_ip;
		this.server_name = server_name;
		manager = new MessageServerTcpClient(server_ip,server_name,"manager");
		
	}

	/** 从标准输入上读入一行命令 
	 * @return 返回读入的信息。其中不包含回车和换行信息 
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
	 * 能够支撑的命令的名称
	 * 1、查询所有的实体名称。包括：实体名称、目前有的消息队列的长度。
	 * 2、查询一个实体的详细信息。包括消息的具体内容。
	 * 3、查询实体的配置信息。
	 * 4、设置一个实体的配置信息。
	 * 5、设置系统的配置信息。
	 * 6、杀死所有的客户端。
	 * 7、退出。
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
	 * 显示每个命令的解释信息。
	 */
	public String[] cmd_explanations = new String[] { 
			"查看所有的实体情况",
			"查看一个实体的所有消息内容",
			"查看一个实体的配置信息",
			"设置一个实体的配置",
			"查看系统配置",
			"设置系统配置",
			"杀死所有的实体",
			"退出",
	};

	/**
	 * 显示一个命令的语法形式。在help的时候会显示这些信息。
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
	 * 处理命令解释
	 * @param cmd_arr 命令名、命令参数列表。目前输入信息没有意义。
	 * @return 返回字符串。该字符串中包括：命令的序号、命令的解释信息、命令的定义。
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
	 * 命令处理的主函数。根据输入的命令名称及其参数来进行处理。
	 * @param cmd_arr 命令名、命令参数列表。
	 */
	public void process_main(String[] cmd_arr) {
		String head = cmd_arr[0];
		if (head.equals("exit")) {
			System.out.println("再见!");
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
		MyPrint.print("该命令没有执行", new Exception());

	}
	
	/**
	 * 杀死所有的客户端。
	 * <p>管理程序给服务器发送一个杀死所有客户端的要求。
	 * <p>服务器将给所有的客户端发送一条命令，客户端接受到该命令就会自动退出。
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
	 * 完成查询所有实体的命令的处理过程
	 * @param cmd_arr 命令名、命令参数列表。
	 * @return 返回处理的结果。该结果是可以直接显示在界面上的内容。
	 */
	public String process_cmd_lookup_all_entities(String[] cmd_arr)
	{
		String ret = "目前没有实体登录到系统中！";
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
	 * 完成查询一个实体的情况的命令的处理过程
	 * @param cmd_arr 命令名、命令参数列表。
	 * @return 返回处理的结果。该结果是可以直接显示在界面上的内容。
	 */	
	
	public String process_cmd_lookup_one_entity(String[] cmd_arr)
	{
		String ret = "命令不合法！";
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
	 * 完成查询系统配置的处理过程
	 * @param cmd_arr 命令名、命令参数列表。
	 * @return 返回处理的结果。该结果是可以直接显示在界面上的内容。
	 */		
	public String process_cmd_lookup_system_conf(String[] cmd_arr)
	{
		String ret = "命令不合法！";
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
	 * 完成查询实体配置的处理过程
	 * @param cmd_arr 命令名、命令参数列表。
	 * @return 返回处理的结果。该结果是可以直接显示在界面上的内容。
	 */		
	public String process_cmd_lookup_entity_conf(String[] cmd_arr)
	{
		String ret = "命令不合法！";
		if(cmd_arr.length==2)
		{
			String entity_name = cmd_arr[1];
			List<NameValue> list = manager.get_entity_conf(entity_name);
			ret = "";
			if(list==null)
			{
				ret = "实体不存在！";
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
	 * 完成设置系统配置的处理过程
	 * @param cmd_arr 命令名、命令参数列表。
	 * @return 返回处理的结果。该结果是可以直接显示在界面上的内容。
	 */		
	public String process_cmd_set_system_conf(String[] cmd_arr)
	{
		String ret = "命令不合法！";
		if(cmd_arr.length==3)
		{		
			String name = cmd_arr[1];
			String value = cmd_arr[2];
			manager.set_system_conf(name, value);
			ret = "设置成功！";
		}
		return ret;
	}
	
	/**
	 * 完成设置实体配置的处理过程
	 * @param cmd_arr 命令名、命令参数列表。
	 * @return 返回处理的结果。该结果是可以直接显示在界面上的内容。
	 */		
	public String process_cmd_set_entity_conf(String[] cmd_arr)
	{
		String ret = "命令不合法！";
		if(cmd_arr.length==4)
		{		
			String entity_name = cmd_arr[1];
			String name = cmd_arr[2];
			String value = cmd_arr[3];
			int ret_code = manager.set_entity_conf(entity_name, name, value);
			if(ret_code==0)
				ret = "设置成功！";
			else
				ret = "实体不存在！";
		}
		return ret;
	}	

	/**
	 * 完成杀死所有实体的处理过程
	 * @param cmd_arr 命令名、命令参数列表。
	 * @return 返回处理的结果。该结果是可以直接显示在界面上的内容。
	 */
	public String process_cmd_kill_all_entities(String[] cmd_arr)
	{
		kill_all();
		String ret = "已经杀死了所有的客户端实体！";
		return ret;
	}

	/**
	 * 输入处理的主函数。
	 * <p>需要将输入的信息经过汉字编码的处理，这样就可以在控制台显示汉字信息。
	 * <p>将命令按照空格分解为字符数组，交给process_main进行处理。
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
	 * 主函数
	 * <p>命令格式为：command server_ip server_name
	 * <p>执行方式为：
	 * <li>循环地接受每个输入，然后处理输入
	 * <li>如果输入end则退出
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
