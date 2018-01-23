package com.jyt.message2;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.jyt.util.ArgumentString;
import com.jyt.util.CountTime;
import com.jyt.util.MyPrint;
import com.jyt.util.MySerializable;
import com.jyt.util.MyString;
import com.jyt.util.NameValue;

public class MessageServerImpl implements MessageServerInterface {

	// private static final long serialVersionUID = 1L;

	private Socket socket = null;

	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void print_thread_info(Exception e) {
		Map<Thread, StackTraceElement[]> map = Thread.getAllStackTraces();
		MyPrint.print(map.size() + "", e);
		String ret = "";
		int i = 1;
		for (Thread t : map.keySet()) {
			String name = t.getName();
			String state = t.getState().name();
			String ls = t.getId() + "";
			String field = "%1\n(%2)----[%3]name=%4 state=%5";
			String[] ss = new String[] { ret, i + "", ls, name, state };
			ret = ArgumentString.replace(field, ss);
			i++;
		}
		MyPrint.print(ret, e);
	}

	private int get_current_thread_number() {
		return Thread.getAllStackTraces().size();
	}

	public MessageServerImpl(Socket socket) {
		super();
		Thread.currentThread().setName("MessageServerRMI");
		this.socket = socket;
		// print_thread_info(new Exception());
	}

	public String[] get_scoped_names(String name) {
		String[] ret = new String[2];
		int index = name.indexOf("@");
		if (index == -1) {
			ret[0] = name;
			ret[1] = null;
		} else {
			ret[0] = name.substring(0, index);
			ret[1] = name.substring(index + 1);
		}
		return ret;
	}

	private void process_length_limit(String entity) {
		List<NameValue> list = MessagePool.entity_conf_map.get(entity);
		String length_str = NameValue.get(list,
				MessageConfig.ENTITY_MAX_MSG_LEN);
		int length = 0;
		if (length_str != null)
			length = Integer.parseInt(length_str);
		if (length != 0) {
			List<Message> message_list = MessagePool.map.get(entity);
			if (message_list.size() > length) {
				int num = message_list.size() - length;
				for (int i = 0; i < num; i++) {
					// String s = message_list.get(0).toString();
					// MyPrint.print(s,new Exception());
					// Message x = message_list.remove(0);
					// x = null;
					message_list.remove(0);
				}
			}
			message_list = null;
		}
		list = null;

	}

	public void process_trace(Message message) {
		String to = message.getTo();
		if (NameValue.get(MessagePool.system_conf, MessageConfig.SYSTEM_TRACE)
				.equals("true")) {
			String entity_arr_str = NameValue.get(MessagePool.system_conf,
					MessageConfig.TRACED_ENITITIES);
			String[] entity_arr = MyString.str_to_arr(entity_arr_str);
			// if((MyString.in(to, entity_arr)||(MyString.in("*",entity_arr))))
			// {
			// MyPrint.log(message.toString(),new Exception());
			// }
		}
	}

	public int add(Message message) {
		Thread.currentThread().setName(
				"add_" + message.getFrom() + "_" + message.getTo());
		// print_thread_info(new Exception());
		int ret = 0;
		String[] scoped_names = get_scoped_names(message.getTo());
		String entity = scoped_names[0];
		if (scoped_names[1] != null) {
			entity = scoped_names[1];
		}
		synchronized (MessagePool.map) {
			List<Message> list = MessagePool.map.get(entity);
			;
			if (list == null) {
				ret = 1;
				MyPrint.print("This entity (" + entity + ")not registered!",
						new Exception());
			} else {
				// list.add(Message.copy(message));
				list.add(message);
			}
			// process_trace(message);
			process_length_limit(entity);
			list = null;
		}
		return ret;
	}

	public Message get(String entity) {
		Thread.currentThread().setName("get_" + entity);
		// print_thread_info(new Exception());

		Message ret = null;
		int thread_size = get_current_thread_number();
		if (thread_size > MessageConfig.MAX_THREAD_COUNT)
			return ret;
		synchronized (MessagePool.map) {
			List<Message> list = MessagePool.map.get(entity);
			if (list != null) {
				if (list.size() > 0) {
					Message to_be_deleted = list.remove(0);
					ret = Message.copy(to_be_deleted);
					to_be_deleted = null;
					if (list.size() == 0) {
						MessagePool.map.put(entity, new ArrayList<Message>());
						list.clear();
						list = null;
					}
				}
			}
			list = null;
		}
		return ret;
	}

	public Message get_syn(String entity,String type,String id) {
		Thread.currentThread().setName("get_syn_" + entity);
		// print_thread_info(new Exception());
		Message ret = null;
		int thread_size = get_current_thread_number();
		if (thread_size > MessageConfig.MAX_THREAD_COUNT)
			return ret;
		synchronized (MessagePool.map) {
			List<Message> list = MessagePool.map.get(entity);
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					Message m = list.get(i);
					if (m.getType().equals(type+"#"+id)) {
						ret = Message.copy(m);
						list.remove(i);
						break;
					}
				}

			}
			list = null;

		}
		return ret;
	}

	public Message get_asyn(String entity) {
		Thread.currentThread().setName("get_asyn_" + entity);
		// print_thread_info(new Exception());
		Message ret = null;
		int thread_size = get_current_thread_number();
		System.out.print("." + thread_size + "\r");
		if (thread_size > MessageConfig.MAX_THREAD_COUNT)
			return ret;
		synchronized (MessagePool.map) {
			List<Message> list = MessagePool.map.get(entity);
			if (list != null) {

				Iterator<Message> it = list.iterator();
				while (it.hasNext()) {
					Message m = it.next();
					if (m.getType().indexOf("#") == -1) {
						// ret = Message.copy(m);
						ret = m;
						it.remove();
						break;
					}
				}

			}
		}
		// System.gc();
		return ret;
	}

	public void init() {
		String[] names = new String[] { MessageConfig.SYSTEM_TRACE,
				MessageConfig.TRACED_ENITITIES };
		String[] values = new String[] { "false", "" };
		MessagePool.system_conf = NameValue.build(names, values);
	}

	public void init_debug() {
		String[] names = new String[] { MessageConfig.SYSTEM_TRACE,
				MessageConfig.TRACED_ENITITIES };
		String[] values = new String[] { "true", "[1]*" };
		MessagePool.system_conf = NameValue.build(names, values);

	}

	public int set_system_conf(String name, String value) {
		Thread.currentThread().setName("set_system_conf");
		int ret = 0;
		synchronized (MessagePool.system_conf) {
			if (MyString.in(name, MessageConfig.valid_system_attributes)) {
				if (name.equals(MessageConfig.SYSTEM_TRACE)) {
					if (MyString.in(value, new String[] { "true", "false" })) {
						MessagePool.system_conf = NameValue.set(
								MessagePool.system_conf, name, value);
					} else {
						ret = 1;
						MyPrint.print("设置的属性值不正确，应该为true或false。",
								new Exception());
					}
				}
				if (name.equals(MessageConfig.TRACED_ENITITIES)) {
					if (value.equals("*")) {
						MessagePool.system_conf = NameValue.set(
								MessagePool.system_conf, name, "[1]*");
					} else if (value.indexOf("[1]") == 0) {
						MessagePool.system_conf = NameValue.set(
								MessagePool.system_conf, name, value);
					} else {
						ret = 1;
						MyPrint
								.print(
										"设置的属性值不正确，应该为\"*\"或者\"[1]entity_name1[2]entity_name2\"的形式。",
										new Exception());
					}
				}

			} else {
				MyPrint.print("所输入的系统属性名称不正确", new Exception());
				ret = 1;
			}

		}
		return ret;
	}

	public List<NameValue> get_system_conf() {
		Thread.currentThread().setName("get_system_conf");
		synchronized (MessagePool.system_conf) {
			byte[] bs = MySerializable
					.object_bytes((Serializable) (MessagePool.system_conf));
			MyPrint.print(bs.length + "", new Exception());
			return MessagePool.system_conf;
		}
	}

	public int set_entity_conf(String entity_name, String name, String value) {
		Thread.currentThread().setName("set_entity_conf_" + entity_name);
		int ret = 0;
		List<NameValue> list = MessagePool.entity_conf_map.get(entity_name);
		if (list == null) {
			ret = 1;
			MyPrint.print("实体不存在！", new Exception());
			return ret;
		}
		synchronized (list) {
			if (MyString.in(name, MessageConfig.valid_entity_attributes)) {
				list = NameValue.set(list, name, value);
				MessagePool.entity_conf_map.put(entity_name, list);
			} else {
				MyPrint.print("所输入的系统属性名称不正确", new Exception());
				ret = 1;
			}

		}
		list = null;
		return ret;
	}

	public List<NameValue> get_entity_conf(String entity_name) {
		Thread.currentThread().setName("get_entity_conf_" + entity_name);
		List<NameValue> list = MessagePool.entity_conf_map.get(entity_name);
		if (list == null) {
			MyPrint.print("实体不存在！", new Exception());
			return null;
		}
		synchronized (list) {
			return list;
		}

	}

	public int register(String entity) {
		Thread.currentThread().setName("register_" + entity);
		// print_thread_info(new Exception());
		int ret = 0;
		List<Message> list = MessagePool.map.get(entity);
		if (list == null) {
			MessagePool.map.put(entity, new ArrayList<Message>());
			List<NameValue> conf_list = new ArrayList<NameValue>();
			NameValue nv = new NameValue(MessageConfig.ENTITY_MAX_MSG_LEN, "0");
			conf_list.add(nv);
			MessagePool.entity_conf_map.put(entity, conf_list);

		} else {
			ret = 1;
			MyPrint.print("This entity has registered", new Exception());
		}
		list = null;
		return ret;
	}

	public int register(String entity, String[] names, String[] values) {
		Thread.currentThread().setName("register2_" + entity);
		int ret = 0;
		List<Message> list = MessagePool.map.get(entity);
		if (list == null) {
			MessagePool.map.put(entity, new ArrayList<Message>());
			List<NameValue> conf_list = NameValue.build(names, values);
			MessagePool.entity_conf_map.put(entity, conf_list);
		} else {
			ret = 1;
			MyPrint.print("This entity has registered", new Exception());
		}
		list = null;
		return ret;
	}

	public int unregister(String entity) {
		Thread.currentThread().setName("unregister_" + entity);
		int ret = 0;
		MessagePool.map.remove(entity);
		return ret;
	}

	public int clear(String entity) {
		Thread.currentThread().setName("clear_" + entity);
		int ret = 0;
		List<Message> list = MessagePool.map.get(entity);
		list.clear();
		list = null;
		return ret;
	}

	public void print_all_entity() {
		Thread.currentThread().setName("print_all_entity");
		String str_list = "";
		int count = 1;
		MyPrint.print(
				"================ Print All Entities ===================",
				new Exception());
		for (String s : MessagePool.map.keySet()) {
			String field = "%1(%2)%3[%4]";
			List<Message> list = MessagePool.map.get(s);
			String[] ss = new String[] { str_list, count + "", s,
					list.size() + "" };
			String result = ArgumentString.replace(field, ss);
			str_list = result;
			count++;
		}
		MyPrint.print(str_list, new Exception());
	}

	public List<String> return_all_entity() {
		Thread.currentThread().setName("return_all_entity");
		List<String> ret = new ArrayList<String>();
		for (String s : MessagePool.map.keySet()) {
			ret.add(s);
		}
		Collections.sort(ret);
		return ret;

	}

	public List<String> return_all_entity_info() {
		Thread.currentThread().setName("return_all_entity_info");
		List<String> ret = new ArrayList<String>();
		int count = 1;
		for (String s : MessagePool.map.keySet()) {
			String field = "%1[%2]";
			List<Message> list = MessagePool.map.get(s);
			String[] ss = new String[] { s, list.size() + "" };
			String result = ArgumentString.replace(field, ss);
			count++;
			ret.add(result);
		}
		Collections.sort(ret);
		for (int i = 0; i < ret.size(); i++) {
			ret.set(i, "(" + i + ")" + ret.get(i));
		}

		return ret;

	}

	public List<Message> peek_all_message(String entity) {
		Thread.currentThread().setName("peek_all_message");
		List<Message> ret = MessagePool.map.get(entity);
		return ret;
	}

	public void process_command(InputStream is, OutputStream os) {
		TLV read_tlv = ReadWrite.read(is);
		// MyPrint.print("MessageType="+read_tlv.t+"", new Exception());
		switch (read_tlv.t) {
		case 1: {
			TLV write_tlv = new TLV(101, "");
			byte[] bs = read_tlv.v;
			Message message = (Message) MySerializable.bytes_object(bs);
			process_trace(message);
			write_tlv.l = 0;
			ReadWrite.send(write_tlv, os);
			break;
		}
		case 2: {
			TLV write_tlv = new TLV(102, "");
			byte[] bs = read_tlv.v;
			Message message = (Message) MySerializable.bytes_object(bs);
			int ret = add(message);
			byte[] bs2 = MySerializable.object_bytes(new Integer(ret));
			write_tlv.v = bs2;
			write_tlv.l = bs2.length;
			ReadWrite.send(write_tlv, os);
			break;
		}
		case 3: {
			TLV write_tlv = new TLV(103, "");
			byte[] bs = read_tlv.v;
			String entity = (String) MySerializable.bytes_object(bs);
			Message message = get(entity);
			byte[] bs2 = MySerializable.object_bytes(message);
			write_tlv.v = bs2;
			write_tlv.l = bs2.length;
			ReadWrite.send(write_tlv, os);
			break;
		}
		case 4: {
			TLV write_tlv = new TLV(104, "");
			byte[] bs = read_tlv.v;
			String[] ss = (String[]) MySerializable.bytes_object(bs);
			String entity = ss[0];
			String type = ss[1];
			String id = ss[2];
			Message message = get_syn(entity,type,id);
			byte[] bs2 = MySerializable.object_bytes(message);
			write_tlv.v = bs2;
			write_tlv.l = bs2.length;
			ReadWrite.send(write_tlv, os);
			break;
		}
		case 5: {
			TLV write_tlv = new TLV(105, "");
			byte[] bs = read_tlv.v;
			String entity = (String) MySerializable.bytes_object(bs);
			Message message = get_asyn(entity);
			byte[] bs2 = MySerializable.object_bytes(message);
			write_tlv.v = bs2;
			write_tlv.l = bs2.length;
			ReadWrite.send(write_tlv, os);
			break;
		}
		case 6: {
			TLV write_tlv = new TLV(106, "");
			init();
			byte[] bs2 = MySerializable.object_bytes("");
			write_tlv.v = bs2;
			write_tlv.l = bs2.length;
			ReadWrite.send(write_tlv, os);
			break;
		}
		case 7: {
			TLV write_tlv = new TLV(107, "");
			init_debug();
			byte[] bs2 = MySerializable.object_bytes("");
			write_tlv.v = bs2;
			write_tlv.l = bs2.length;
			ReadWrite.send(write_tlv, os);
			break;
		}
		case 8: {
			TLV write_tlv = new TLV(108, "");
			byte[] bs = read_tlv.v;
			String[] ss = (String[]) MySerializable.bytes_object(bs);
			int ret = set_system_conf(ss[0], ss[1]);
			byte[] bs2 = MySerializable.object_bytes(new Integer(ret));
			write_tlv.v = bs2;
			write_tlv.l = bs2.length;
			ReadWrite.send(write_tlv, os);
			break;
		}
		case 9: {
			TLV write_tlv = new TLV(109, "");
			List<NameValue> nv_list = get_system_conf();
			byte[] bs2 = MySerializable.object_bytes((Serializable) nv_list);
			write_tlv.v = bs2;
			write_tlv.l = bs2.length;
			ReadWrite.send(write_tlv, os);
			break;
		}
		case 10: {
			TLV write_tlv = new TLV(110, "");
			byte[] bs = read_tlv.v;
			String[] ss = (String[]) MySerializable.bytes_object(bs);
			int ret = set_entity_conf(ss[0], ss[1], ss[2]);
			byte[] bs2 = MySerializable.object_bytes(new Integer(ret));
			write_tlv.v = bs2;
			write_tlv.l = bs2.length;
			ReadWrite.send(write_tlv, os);
			break;
		}
		case 11: {
			TLV write_tlv = new TLV(111, "");
			byte[] bs = read_tlv.v;
			String s = (String) MySerializable.bytes_object(bs);
			List<NameValue> nv_list = get_entity_conf(s);
			byte[] bs2 = MySerializable.object_bytes((Serializable) nv_list);
			write_tlv.v = bs2;
			write_tlv.l = bs2.length;
			ReadWrite.send(write_tlv, os);
			break;
		}
		case 12: {
			TLV write_tlv = new TLV(112, "");
			byte[] bs = read_tlv.v;
			String s = (String) MySerializable.bytes_object(bs);
			int ret = register(s);
			byte[] bs2 = MySerializable.object_bytes(new Integer(ret));
			write_tlv.v = bs2;
			write_tlv.l = bs2.length;
			ReadWrite.send(write_tlv, os);
			break;
		}
		case 13: {
			/*
			 * 暂时取消这个命令 TLV write_tlv = new TLV(113,""); byte[] bs = read_tlv.v;
			 * String s = (String)MySerializable.bytes_object(bs); int ret =
			 * register(s); byte[] bs2 = MySerializable.object_bytes(new
			 * Integer(ret)); write_tlv.v= bs2; write_tlv.l = bs2.length;
			 * ReadWrite.send(write_tlv, os);
			 */
			break;
		}
		case 14: {
			TLV write_tlv = new TLV(114, "");
			byte[] bs = read_tlv.v;
			String s = (String) MySerializable.bytes_object(bs);
			int ret = unregister(s);
			byte[] bs2 = MySerializable.object_bytes(new Integer(ret));
			write_tlv.v = bs2;
			write_tlv.l = bs2.length;
			ReadWrite.send(write_tlv, os);
			break;
		}
		case 15: {
			TLV write_tlv = new TLV(115, "");
			byte[] bs = read_tlv.v;
			String s = (String) MySerializable.bytes_object(bs);
			int ret = clear(s);
			byte[] bs2 = MySerializable.object_bytes(new Integer(ret));
			write_tlv.v = bs2;
			write_tlv.l = bs2.length;
			ReadWrite.send(write_tlv, os);
			break;
		}
		case 16: {
			TLV write_tlv = new TLV(116, "");
			print_all_entity();
			byte[] bs2 = MySerializable.object_bytes("");
			write_tlv.v = bs2;
			write_tlv.l = bs2.length;
			ReadWrite.send(write_tlv, os);
			break;
		}
		case 17: {
			TLV write_tlv = new TLV(117, "");
			List<String> list = return_all_entity();
			byte[] bs2 = MySerializable.object_bytes((Serializable) list);
			write_tlv.v = bs2;
			write_tlv.l = bs2.length;
			ReadWrite.send(write_tlv, os);
			break;
		}
		case 18: {
			TLV write_tlv = new TLV(118, "");
			List<String> list = return_all_entity_info();
			byte[] bs2 = MySerializable.object_bytes((Serializable) list);
			write_tlv.v = bs2;
			write_tlv.l = bs2.length;
			ReadWrite.send(write_tlv, os);
			break;
		}
		case 19: {
			TLV write_tlv = new TLV(119, "");
			byte[] bs = read_tlv.v;
			String s = (String) MySerializable.bytes_object(bs);
			List<Message> list = peek_all_message(s);
			byte[] bs2 = MySerializable.object_bytes((Serializable) list);
			write_tlv.v = bs2;
			write_tlv.l = bs2.length;
			ReadWrite.send(write_tlv, os);
			break;
		}
		default: {
			MyPrint.print("", new Exception());
		}
		}
	}

	public void run() {
		try {
			MyPrint.print("", new Exception());
			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();
			CountTime ct = new CountTime("connection");
			int count = 0;
			while (true) {

				if (is.available() > 0) {
					process_command(is, os);
					count = 0;
				} else if (count > 1000) {
					if (ct.getCost() > MessageConfig.SECOND_LIFE_TIME * 1000)
					{
						is.close();
						os.close();
						socket.close();
						MyPrint.print("TIMEOUT", new Exception());
						break;
					}
					else{
						Thread.sleep(100);
						count = 0;
					}
				} else {
					count++;
					Thread.sleep(1);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
