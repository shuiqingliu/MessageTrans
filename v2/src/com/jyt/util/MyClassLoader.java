
package com.jyt.util;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.util.HashMap;



/**
 *  MyClassLoader <b>��Ŀ��</b>��������Ϊ�ҵ����������<p>
 * �书�����£�
 * 1�����Դ�ĳ���ļ�filename����ĳ����classname
 * ʹ�÷�����
 * <list>
 * <li>���캯��
 * <li>���뷽����������
 * </list>
 * <pre>
 *    	String filename = "e:/gupiao2/login/src/com/jyt/example/classloader/Add.class";
    	String classname = "com.jyt.example.classloader.Add";
    	MyClassLoader cls_load = new MyClassLoader(filename,classname);
    	cls_load.add("add", new Class[]{int.class,int.class});
    	cls_load.add("times", new Class[]{int.class,int.class});
 * </pre>
 * 
 * @author YZQ
 * @since 2011/2/15
 * 
 */

public class MyClassLoader extends ClassLoader { 

	/** 
	 * Ҫ���ص��ļ���
	 */
	public String filename;
	/** Ҫ���ص��������
	 */
	public String classname;
	/**
	 * �տ���Ϊ�ա�����һ�γ�ʼ��֮�󣬱�Ϊ�������ֵ��
	 */
	public Class cls = null;
	/**
	 * ������еķ����������������<p>
	 * key��ʾ����������<p>
	 * class[]��ʾ����������
	 */
	public HashMap<String,Class[]> method_defintion_map = new HashMap<String,Class[]>(); 
	
    public MyClassLoader(String filename,String classname)
    {
    	this.filename = filename;
    	this.classname = classname;
    }
    
    /**
     * ����һ����������������������Ͷ��塣
     * @param method ��ʾ����
     * @param cls_arr ��ʾ����������
     */
    public void add(String method,Class[] cls_arr)
    {
    	method_defintion_map.put(method, cls_arr);
    }
    
    /**
     * ��ӡmap�е����еķ�����
     */
    public void print_map()
    {
    	for(Object key:method_defintion_map.keySet())
    	{
    		String s = (String)key;
    		Class[] arr = method_defintion_map.get(key);
    		String value = "null";
    		if((arr!=null)&&(arr.length!=0))
    		{
    			for(int i=0;i<arr.length;i++)
    			{
    				Class cls = arr[i];
    				if(i==0)
    					value = cls.getName();
    				else
    					value = value+"#"+cls.getName();
    			}
    		}
    		MyPrint.print("key="+s+" class[]="+value, new Exception());
    	}
    }
    
    /**
     * ����д�ķ��������clsΪ�գ������load�������ļ��м������ݣ�������javaԭ�е�<p>
     * defineClass�����Ӽ����ࡣ
     * @see java.lang.ClassLoader#findClass(java.lang.String)
     */
    public Class findClass(String name) {
  
    	if(cls==null)
    	{
            byte[] b = loadClassData(filename);
            cls =  defineClass(name, b, 0, b.length);    		
    	}
    	return cls;
    		
    }
    

    
    /**
     * ���ļ��м������ݣ���ɶ��������ڴ档
     */
    private byte[] loadClassData(String filename) {
    	
    	byte[] ret = null;
    	try{
            File f = new File(filename);
            ret = new byte[(int)f.length()];
            FileInputStream fis = new FileInputStream(f);
            fis.read(ret);
            
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	return ret;
       
    }
    
    /**
     * �����еĲ������ַ������ͱ���������͡�
     */
    public Object[] shift_from_string_to_int(String[] args)
    {
    	Object[] ret = new Object[args.length];
    	for(int i=0;i<ret.length;i++)
    	{
    		Integer a = Integer.parseInt(args[i]);
    		ret[i] = a;
    	}
    	return ret;
    }
    
    /**
     * ��һ���ַ������ĳ�����͵����ݡ��൱�ڱ�����̡�
     */
    public Object shift(String s,Class c)
    {
    	Object ret = s;
    	if(c==int.class)
    	{
    		Integer I = Integer.parseInt(s);
    		ret = I;
    	}
    	else if(c==Integer.class)
    	{
    		Integer I = Integer.parseInt(s);
    		ret = I;
    	}
    	return ret;
    }
    
    /**
     * ����shift������һ�������е����ͱ�ɺ��ʵ����͡�<p>
     * ��Ҫ��ɵ�������map���Ѿ�˵�����ˡ�
     */
    public Object[] shift_arr(String method_name,String[] args)
    {
    	Object[] ret = new Object[args.length];
    	Class[] clses = method_defintion_map.get(method_name);
    	for(int i=0;i<ret.length;i++)
    	{
    		String s = args[i];
    		Class c = clses[i];
    		ret[i]  = shift(s,c);
    	}
    	return ret;
    }
    
    /**
     * ����һ�����������������÷����Ĳ��������ÿ�����ĺ���ִ�еõ������
     */
    
 
    
    public int exe(String method_name,String[] args)
    {
    	int ret = 0;
    	try{
    		CountTime cs = new CountTime(method_name);
       		Class cls = findClass(classname);
     //  		MyPrint.print(cs.sprint(), new Exception());
    	   	Object ao = cls.newInstance();
     //  		MyPrint.print(cs.sprint(), new Exception());
    	   	Class[] clses = method_defintion_map.get(method_name);
     //  		MyPrint.print(cs.sprint(), new Exception());
		   	Method method = cls.getMethod(method_name,clses);
     //  		MyPrint.print(cs.sprint(), new Exception());
		   	Object[] parameters = shift_arr(method_name,args);
     //  		MyPrint.print(cs.sprint(), new Exception());
		   	Integer r = (Integer)method.invoke(ao, parameters);
		 //  	Integer r = MyInvoke(ao,method,parameters);
     //  		MyPrint.print(cs.sprint(), new Exception());
	   		ret = r.intValue();
     //  		MyPrint.print(cs.sprint(), new Exception());
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	return ret;
    }
    
    /**
     * һ��ʵ�ʵ����ӡ�
     */
    public static void main(String[] args)
    {
    	String filename = "e:/gupiao2/login/src/com/jyt/example/classloader/Add.class";
    	String classname = "com.jyt.example.classloader.Add";
    	MyClassLoader cls_load = new MyClassLoader(filename,classname);
    	cls_load.add("add", new Class[]{int.class,int.class});
    	cls_load.add("times", new Class[]{int.class,int.class});
    	
    	System.out.println(cls_load.exe("add",new String[]{"73","84"}));
    	System.out.println(cls_load.exe("times",new String[]{"39","5"}));
 
    }


}

