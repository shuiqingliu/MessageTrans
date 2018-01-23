package com.jyt.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

public class MyTreeImpl<T> implements MyTree<T> {
	
	public Map<String,T> map = new HashMap<String,T>();
	public String current_key = null;
	public List<String> current_key_vector = new ArrayList<String>();
	public String top_key = null;
	
	public Map<String,T> get_map()
	{
		return map;
	}
	public void set_current_key(String key)
	{
		current_key = key;
	}
	
	public void push()
	{
		current_key_vector.add(current_key);
	}
	
	public void pop()
	{
		int size = current_key_vector.size();

		if(size>0)
		{
			String ret = current_key_vector.get(size-1);
			current_key_vector.remove(size-1);
			current_key = ret;			
		}

		
	}
	

	public void set_top_key(String oid)
	{
		top_key = oid;
		current_key = top_key;
	}
	
	public String get_top_key()
	{
		return top_key;
	}
	
	public String get_current_key()
	{
		return current_key;
	}
	
	public int get_level()
	{
		String[] cs = current_key.split("\\.");
		String[] ts = top_key.split("\\.");
		int level = cs.length-ts.length+1;
		return level;
	}
	

	public boolean relation_brother(String oid1,String oid2)
	{
		
		boolean ret = false;
		String[] oo1 = oid1.split("\\.");
		String[] oo2 = oid2.split("\\.");
		if(oo1.length==oo2.length)
		{
			String m1 = oo1[oo1.length-1];
			String m2 = oo2[oo2.length-1];
			if(m1.equals(m2))
			{
				ret = false;
			}
			else{
				ret = true;
				for(int i=0;i<oo1.length-1;i++)
				{
					if(oo1[i].equals(oo2[i]))
					{
					}
					else{
						ret = false;
						break;
					}
				}				
			}

		}
		return ret;
	}	
	
	public boolean relation_elder_brother(String elder,String less)
	{
		
		boolean ret = relation_brother(elder,less);
		if(elder.compareTo(less)>0)
		{
			ret = true;
		}
		else{
			ret = false;
		}
		return ret;
	}		
	
	public boolean has_children()
	{
		List<String> list = get_son_keys();
		boolean ret = false;
		if(list.size()>0)
			ret = true;
		return ret;
	}
	
	public boolean has_brother()
	{
		List<String> list = get_brother_keys();
		boolean ret = false;
		if(list.size()>0)
			ret = true;
		return ret;		
	}
	
	
	public boolean is_empty()
	{
		boolean ret = true;
		if(map.size()>0)
			ret=false;
		return ret;
		
	}
	public int size()
	{
		return map.size();
	}
	
	public void set_top(T t)
	{
		if(top_key==null){
			map.put("1", t);
			top_key = "1";
		}
		else
			map.put(top_key, t);
		current_key = top_key;
		
	}
	
	public T get_top()
	{
		T ret = null;
		if(top_key==null)
		{
			ret = null;
		}
		else{
			ret = map.get(top_key);

		}
		return ret;
	}
	
	public T get_current()
	{
		T ret = null;
		if(current_key==null)
		{
			ret = null;
		}
		else{
			ret = map.get(current_key);

		}
		return ret;		
	}
	
	public void set_top(T t,String oid)
	{
		map.put(oid, t);
		top_key = oid;
		current_key = oid;
	}
	
	public int get_oid_first(String oid)
	{
		return get_oid_no(oid,0);
	}
	
	public int get_oid_last(String oid)
	{
		int length = get_oid_length(oid);
		return get_oid_no(oid,length-1);
	}
	
	public int get_oid_length(String oid)
	{
		String[] ss = oid.split("\\.");
		return ss.length;		
	}
	
	public int get_oid_no(String oid,int n)
	{
		int ret = -1;
		String[] ss = oid.split("\\.");
		if(n<ss.length)
		{
			ret = Integer.parseInt(ss[n]);
		}
		return ret;
		
	}
	
	public String get_parent_oid(String oid)
	{
		String[] ss = oid.split("\\.");
		String ret = null;
		if(ss.length>=0)
		{
			ret = "";
			for(int i=0;i<ss.length-1;i++)
			{
				if(i==0)
					ret = ss[i];
				else
					ret = ret+"."+ss[i];
			}
		}
		return ret;
	}
	
	private String prepare_add_key()
	{
		int max = 0;
		String key = null;
		String c_key = get_current_key();
		List<String> son_key_list = get_son_keys();
		if(son_key_list.size()==0)
		{
			key = current_key+".1";
		}
		else{
			for(String s:son_key_list)
			{
				int last = get_oid_last(s);
				if(last>max)
				{
					max = last;
				}
			}
			key = current_key+"."+(max+1);
		}
		return key;
	}
	public int  add_son(T t)
	{
		int ret = 0;
		String key = prepare_add_key();
		map.put(key, t);
		return ret;
	}
	
	public MyTree copy()
	{
		MyTreeImpl<T> new_mytree = new MyTreeImpl<T>();

		for(String s:map.keySet())
		{
			new_mytree.get_map().put(s, map.get(s));
			new_mytree.top_key = top_key;
			new_mytree.current_key = current_key;
		}
		return new_mytree;
	}
	
	public int add_son_tree(MyTree<T> another)
	{
		int ret = 0;
		String key = prepare_add_key();
		MyTree<T> new_tree = another.copy();
		new_tree.adjust_top(key);
		for(String s:new_tree.get_map().keySet())
		{
			map.put(s, new_tree.get_map().get(s));
		}
		return ret;
		
	}
	
	public void goto_top()
	{
		current_key = top_key;
		
	}
	
	public void goto_key(String key)
	{
		T t = map.get(key);
		if(t==null)
		{
			MyPrint.print("This Key does not exist!", new Exception());
		}
		else
			current_key = key;
	}
	
	public int goto_dn(List<T> dn)
	{
		int ret = 0;
		if(dn.size()==0)
		{
			ret = 1;
		}
		else if(dn.size()==1)
		{
			goto_top();
			T t = get_current();
		//	MyPrint.print("", new Exception());
		//	MyPrint.print(t.toString(), new Exception());
		//	MyPrint.print(dn.get(0).toString(), new Exception());
			if(t.equals(dn.get(0)))
			{
		//		MyPrint.print("", new Exception());
			}
			else{
				MyPrint.print("", new Exception());
				ret = 2;
			}
		}
		else {
			goto_top();
			T t = get_current();
			if(t.equals(dn.get(0)))
			{
			}
			else{
				ret = 2;
			}
			for(int i=1;i<dn.size();i++)
			{
				T rdn = dn.get(i);
				int find = goto_matched_son(rdn);
				if(find==0)
				{
					
				}
				else{
					ret =3;
					break;
				}
			}
		}
		return ret;
	}
	
	public void goto_first_son()
	{
		if(has_children())
		{
			List<String> list = get_son_keys();
			Collections.sort(list);
			set_current_key(list.get(0));
		
		}
	}
	
	public int goto_matched_son(T t)
	{
		int ret = 1;
		if(has_children())
		{
			List<String> list = get_son_keys();
			for(String key:list)
			{
				T son = map.get(key);
				if(son.equals(t))
				{
					set_current_key(key);
					ret = 0;
					break;
				}
			}
		
		}	
		return ret;
	}
	
	
	public int get_son_number()
	{
		List<String> list = get_son_keys();
		return list.size();
		
	}
	public int goto_son_no(int n)
	{
		int ret = 0;
		List<String> list = get_son_keys();
		if(n<=0)
		{
			ret = 1;
			MyPrint.print("BEGIN with 1", new Exception());
		}
		else if(n>list.size())
		{
			ret = 2;
			MyPrint.print("ERROR", new Exception());
		}
		Collections.sort(list);
		set_current_key(list.get(n-1));
		return ret;
	}
	
	public int goto_next()
	{
		int ret = 0;
		List<String> list = get_brother_keys();
		if(list.size()==0)
		{
			ret =1;
		}
		else{
			Collections.sort(list);
			String oid_result = null;
			for(int i=0;i<list.size();i++)
			{
				String oid1 = list.get(i);
				int n1 = get_oid_last(oid1);
				int n2 = get_oid_last(current_key);
				if(n1<n2)
				{
					
				}
				else{
					oid_result = oid1;
					break;
				}
			}
			if(oid_result==null)
			{
				ret =2;
			}
			else{
				set_current_key(oid_result);
				ret = 0;
			}
		}
		return ret;
		
	}
	
	public int goto_previous()
	{
		int ret = 0;
		List<String> list = get_brother_keys();
		if(list.size()==0)
		{
			ret =1;
		}
		else{
			Collections.sort(list);
			String oid_result = null;
			for(int i=0;i<list.size();i++)
			{
				String oid1 = list.get(i);
				int n1 = get_oid_last(oid1);
				int n2 = get_oid_last(current_key);
				if(n1<n2)
					oid_result = oid1;
				else{
					break;
				}
			}
			if(oid_result==null)
			{
				ret =2;
			}
			else{
				set_current_key(oid_result);
				ret = 0;
			}
		}
		return ret;	
	}
	
	public boolean is_leaf()
	{
		boolean ret = false;
		List<String> sons = get_son_keys();
		if(sons==null)
		{
			ret = true;
		}
		else if(sons.size()==0)
		{
			ret = true;
		}
		return ret;
	}
	
	public List<T> get_sons()
	{
		List<String> list = get_son_keys();
		List<T> ret = new ArrayList<T>();
		for(String s:list)
		{
			ret.add(map.get(s));
		}
		return ret;
	}
	
	public List<String> get_son_keys()
	{
		List<String> ret = new ArrayList<String>();
		for(String s:map.keySet())
		{
			if(relation_parent_son(current_key,s))
			{
				ret.add(s);
			}
		}
		return ret;
	}
	
	public List<String> get_brother_keys()
	{
		List<String> ret = new ArrayList<String>();
		for(String s:map.keySet())
		{
			if(relation_brother(current_key,s))
			{
				ret.add(s);
			}
		}
		return ret;		
	}

	public boolean relation_parent_son(String pk,String sk)
	{
		boolean ret = false;
		String[] pks = pk.split("\\.");
		String[] sks = sk.split("\\.");
		if(sks.length==pks.length+1)
		{
			ret = true;
			for(int i=0;i<pks.length;i++)
			{
				if(sks[i].equals(pks[i]))
				{
					
				}
				else{
					ret = false;
					break;
				}
			}
			
		}
		return ret;
	}
	public int goto_parent()
	{
		int ret = 0;
		if(get_level()==1)
		{
			ret =1;
		}
		else{
			String oid = get_parent_oid(current_key);
			set_current_key(oid);			
		}
		return ret;
	}
	
	public int  replace(T t)
	{
		int ret = 0;
		if(current_key==null)
		{
			ret =1;
		}
		else{
			map.put(get_current_key(), t);
		}
		return ret;
	}
	
	public List<T> find(T t,Comparator c)
	{
		return null;
	}
	
	public void print_node(int tag,Exception e)
	{
		String s = "";
		for(int i=0;i<tag;i++)
		{
			s = s+"\t";
		}
		s = s +"("+ current_key+")"+get_current().toString();
		MyPrint.print(s, e);
	}
	
	public String sprint_node(int tag)
	{
		String s = "";
		for(int i=0;i<tag;i++)
		{
			s = s+"\t";
		}
		s = s +"("+ current_key+")"+get_current().toString();
		return s;
	}
	
	public void print_current_node(Exception e)
	{
		String s = "("+current_key+")"+get_current().toString();
		MyPrint.print(s, e);
	}
	
	public void print(Exception e)
	{
		if(is_empty())
		{
			MyPrint.print("Tree is Empty", e);
		}
		else{
			push();
			for(String s:map.keySet())
			{
				set_current_key(s);
				MyPrint.print(current_key, e);
				print_node(get_level(),e);
			}
			pop();
			
		}
	}
	
	public void print_sort(Exception e)
	{
		if(is_empty())
		{
			MyPrint.print("Tree is Empty", e);
		}
		else{
			push();
			Map sorted = new TreeMap(map);
			for(Iterator i=sorted.entrySet().iterator();i.hasNext();)
			{
				Map.Entry en = (Map.Entry)i.next();
				set_current_key((String)en.getKey());
				print_node(get_level(),e);
			}
			pop();
			
		}
	}	
	
	
	public String sprint_sort()
	{
		String ret = "";
		if(is_empty())
		{
			ret = "Tree is Empty";
		}
		else{
			push();
			Map sorted = new TreeMap(map);
			for(Iterator i=sorted.entrySet().iterator();i.hasNext();)
			{
				Map.Entry en = (Map.Entry)i.next();
				set_current_key((String)en.getKey());
				ret = ret +"\r\n" + sprint_node(get_level());
			}
			pop();
			
		}	
		return ret;
	}
	
	public int get_brother_number()
	{
		int ret = 0;
		List<String> list = get_brother_keys();
		ret = list.size();
		return ret;
	}
	
	public void adjust_top(String new_oid)
	{
		Map<String,T> new_map = new HashMap<String,T>();
		for(String s:map.keySet())
		{
			String new_key = new String(s);
			String left = new_key.substring(top_key.length());
			new_key = new_oid+left;
			new_map.put(new_key, map.get(s));
		}
		map = new_map;
		top_key = new_oid;
		current_key = top_key;
		
	}
	


	public int replace(MyTree tree)
	{
		int ret = 0;
		String key = current_key;
		MyTree<T> new_tree = tree.copy();
		new_tree.adjust_top(key);
		for(String s:new_tree.get_map().keySet())
		{
			map.put(s, new_tree.get_map().get(s));
		}
		return ret;		
	}
	
	public void cut()
	{
		List<String> list = new ArrayList<String>();
		for(String s:map.keySet())
		{
			if(s.indexOf(current_key)==0)
			{
				list.add(s);
			}
		}
		for(String s:list)
		{
			map.remove(s);
		}
	}
	
	public boolean equals(T t1,T t2)
	{
		boolean ret = t1.equals(t2);
		return ret;
	}
	
	public List<T> get_dn()
	{
		Vector<T> ret = new Vector<T>();
		String top_key = get_top_key();
		while(true)
		{
			T c = get_current();
			ret.add(c);
			String current_key = get_current_key();
			if(current_key.equals("")||current_key==null)
				break;
			if(current_key.equals(top_key))
			{
				break;
			}
			else{
				goto_parent();
			}
		}
		Collections.reverse(ret);
		return ret;
	}
	
	public List<T> get_dn(String key)
	{
		List<T> ret = null;
		push();
		set_current_key(key);
		ret = get_dn();
		pop();
		return ret;
	}
	
	public void arrange_top_current()
	{
		int count = -1;
		String top = null;
		for(String s:map.keySet())
		{
			String[] ss = s.split("\\.");
			int k = ss.length;
			if(count==-1){
				count = k;
				top = s;
			}
			else{
				if(k<count)
				{
					count = k;
					top = s;
				}
			}
		}
		set_top_key(top);
		goto_key(top);
		
	}


}
