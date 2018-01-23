package com.jyt.message2;

import java.util.List;

import com.jyt.util.NameValue;

public interface MessageServerInterface extends Runnable  {
	
	public void init_debug();
	public int add(Message message)  ;
	public Message get(String entity) ;
	public Message get_asyn(String entity) ;
	public Message get_syn(String entity,String type,String id) ;
	public int register(String entity) ;
	public int register(String entity,String[] names,String[] values) ;
	public int unregister(String entity) ;	
	public void print_all_entity() ;
	public int clear(String entity) ;
	public List<String> return_all_entity() ;
	public List<String> return_all_entity_info() ;
	public List<Message> peek_all_message(String entity) ;
	public int set_system_conf(String name,String value) ;
	public List<NameValue> get_system_conf() ;
	public int set_entity_conf(String entity_name,String name,String value) ;
	public List<NameValue> get_entity_conf(String entity_name) ;
	
}
