package com.jyt.util;

import java.io.Serializable;
import java.util.Iterator;

public interface MyIterator extends Iterator,Serializable {
	void set(String name,Object value);
	void open();
	void close();
}
