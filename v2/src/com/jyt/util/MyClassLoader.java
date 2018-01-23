
package com.jyt.util;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.util.HashMap;



/**
 *  MyClassLoader <b>的目的</b>是用来作为我的类加载器。<p>
 * 其功能如下：
 * 1、可以从某个文件filename加载某个类classname
 * 使用方法：
 * <list>
 * <li>构造函数
 * <li>加入方法及其类型
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
	 * 要加载的文件名
	 */
	public String filename;
	/** 要加载的类的名称
	 */
	public String classname;
	/**
	 * 刚开是为空。当第一次初始化之后，变为有意义的值。
	 */
	public Class cls = null;
	/**
	 * 存放所有的方法方法及其参数。<p>
	 * key表示方法的名字<p>
	 * class[]表示参数的类型
	 */
	public HashMap<String,Class[]> method_defintion_map = new HashMap<String,Class[]>(); 
	
    public MyClassLoader(String filename,String classname)
    {
    	this.filename = filename;
    	this.classname = classname;
    }
    
    /**
     * 加入一个方法，并加入其参数类型定义。
     * @param method 表示方法
     * @param cls_arr 表示参数的类型
     */
    public void add(String method,Class[] cls_arr)
    {
    	method_defintion_map.put(method, cls_arr);
    }
    
    /**
     * 打印map中的所有的方法。
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
     * 重新写的方法。如果cls为空，则调用load方法从文件中加载内容，并调用java原有的<p>
     * defineClass方法从加载类。
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
     * 从文件中加载数据，变成二进制流内存。
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
     * 将所有的参数从字符串类型变成整数类型。
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
     * 将一个字符串变成某种类型的数据。相当于编码过程。
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
     * 调用shift方法将一个方法中的类型变成合适的类型。<p>
     * 需要变成的类型在map中已经说明好了。
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
     * 给定一个方法名，并给定该方法的参数，调用库里面的函数执行得到结果。
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
     * 一个实际的例子。
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

