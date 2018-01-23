package com.jyt.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NameValue implements Serializable {
	public String name;
	public String value;
	public String toString()
	{
		String field = "%1=%2";
		String[] ss = new String[]{name,value};
		String ret = ArgumentString.replace(field,ss);
		return ret;
	}
	
	public NameValue(String name,String value)
	{
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public static List<NameValue> build(String[] names,String[] values)
	{
		List<NameValue> ret = new ArrayList<NameValue>();
		if(names.length==values.length)
		{
			for(int i=0;i<names.length;i++)
			{
				NameValue nv = new NameValue(names[i],values[i]);
				ret.add(nv);
			}
		}
		return ret;
	}
	
	public static List<NameValue> build(String names_str,String values_str)
	{
		String[] names = MyString.str_to_arr(names_str);
		String[] values = MyString.str_to_arr(values_str);
		List<NameValue> ret = build(names,values);
		return ret;
	}
	
	public static String get(List<NameValue> list,String name)
	{
		String ret = null;
		if(list==null) 
			return ret;
		for(NameValue nv:list)
		{
			if(nv.getName().equals(name))
			{
				ret = nv.getValue();
				break;
			}
		}
		return ret;
	}
	
	public static List<NameValue> set(List<NameValue> list,String name,String value)
	{
		List<NameValue> ret = list;
		if(get(list,name)==null)
		{
			list.add(new NameValue(name,value));
			ret = list;
		}
		else{
			for(NameValue nv:list)
			{
				if(nv.getName().equals(name)){
					nv.setValue(value);
				}
			}
		}
		return ret;
	}
}
