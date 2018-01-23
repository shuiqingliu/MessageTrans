package com.jyt.util;

import java.util.List;
import java.util.Map;

public interface MyTree<T> {
	
	/**
	 * (tested)将当前节点号保存起来；
	 */
	public void push();
	/**
	 * (tested)恢复保存的当前节点；
	 */
	public void pop();

	/**
	 * (tested)判断当前节点是否有子女。
	 * @return
	 */
	public boolean has_children();

	/**
	 * (tested)判断当前节点是否有兄弟。
	 * @return
	 */	
	public boolean has_brother();
	
	/**
	 * （tested）判断树是否为空。
	 * @return
	 */
	public boolean is_empty();
	
	/**
	 * (tested)得到树的大小。
	 * @return
	 */
	public int size();
	
	/**
	 * （tested）设置顶级节点。这是建立一个树之后首先要做的事情。
	 * @param t
	 */
	public void set_top(T t);
	
	/**
	 * (tested)设置顶级节点，并设置其健值。这是建立一个树之后首先要做的事情。
	 * @param t
	 */	
	public T get_current();
	
	/**
	 * (tested)返回顶级节点。
	 * @return
	 */

	public T get_top();
	
	/**
	 * 得到某个key节点的dn
	 * @param key
	 * @return
	 */
	public List<T> get_dn(String key);
	/**
	 * 得到当前节点的路径。
	 * @return
	 */

	public List<T> get_dn();
	/**
	 * (tested)设置顶级节点，并置其oid
	 * @param t
	 * @param oid
	 */
	public void set_top(T t,String oid);	
	/**
	 * (test)调整顶级节点的oid，并在所有的节点中进行。
	 * @param t
	 */
	public void adjust_top(String new_oid);

	/**
	 * (tested)在当前节点后加一个儿子节点。
	 * @param t
	 */
	public int add_son(T t);
	/**
	 * (tested)将当前指针指向定点节点。
	 */
	public void goto_top();
	/**
	 * (tested)将当前指针指向第一个儿子。
	 */	
	public void goto_first_son();
	
	/**
	 * (tested)返回儿子的数量；
	 * @return
	 */
	public int get_son_number();
	
	/**
	 * (tested)判断有几个兄弟
	 * @return
	 */
	public int get_brother_number();
	
	/**
	 * (tested)将第n个儿子作为当前节点
	 * @param n
	 */
	public int goto_son_no(int n);
	
	/**
	 * （tested）转到右边的兄弟
	 * 成功为0
	 */
	public int goto_next();
	/**
	 * (tested)访问左边的兄弟
	 * @return
	 */
	public int goto_previous();
	/**
	 * 得到所有儿子节点的内部key
	 * @return
	 */
	public List<String> get_son_keys();
	
	/**
	 * 返回所有的儿子的信息对象列表
	 * @return
	 */
	public List<T> get_sons();
	/**
	 * (tested)返回父母节点。
	 * @return
	 */
	public int  goto_parent();
	/**
	 * (tested)将当前节点的内容换成t
	 * @param t
	 */
	public int  replace(T t);
	
	/**
	 * 将当前节点换成tree所代表的树
	 * @param tree
	 * @return
	 */
	public int replace(MyTree tree);
	
	/**
	 * (tested)打印当前节点
	 * @param e
	 */
	public void print_current_node(Exception e);
	/**
	 * （tested）打印所有。
	 * @param e
	 */
	public void print(Exception e);
	/**
	 * （tested）按照顺序打印所有。
	 * @param e
	 */
	public void print_sort(Exception e);
	/**
	 * (tested)将另一颗树加到当前节点的下面，作为最后一个儿子，.如果是继承的类，copy函数要重写。
	 * @param another
	 */
	
	public String sprint_sort();

	public int add_son_tree(MyTree<T> another);
	
	/**
	 * (tested)将本身的内容重新拷贝一份到新的空间.如果是继承的类，copy函数要重写。
	 * @return
	 */
	public MyTree copy();
	/**
	 * (tested)得到map
	 * @return
	 */
	public Map<String,T> get_map();
	
	/**
	 * 将当前节点删除。当前节点为top。如果是top被删除，则当前节点和top都为null
	 */
	public void cut();
	
	/**
	 * 定义两个T是否是一样的。这个函数将用在下一个goto_dn的判断中。
	 * @param t1
	 * @param t2
	 * @return
	 */
	public boolean equals(T t1,T t2);
	/**
	 * 将当前按节点定位在dn所表示的路径中。如果找到返回为0，否则是其他值。
	 * @param dn
	 * @return
	 */
	public int goto_dn(List<T> dn);
	/**
	 * 直接到那个key的地方去
	 * @param key
	 */
	public void goto_key(String key);
	
	/**
	 * 如果树的top和current没有设置，在这里设置
	 */
	public void arrange_top_current();
	
	/**判断当前节点是否为叶节点
	 * 
	 * @return
	 */
	public boolean is_leaf();



	
}
