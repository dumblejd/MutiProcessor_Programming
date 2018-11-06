package project3;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Node<T> 
{
	T item;
	int key;
	Node<T> next;
	boolean marked;
	Lock lock;
	Node(T item) 
	{
		this.item = item;
		this.key = item.hashCode();
		this.next = null;
		this.marked = false;
		this.lock = new ReentrantLock();
	}
	//定义  
	public static <T> T[] fun1(T...arg){  // 接收可变参数    
	       return arg ;            // 返回泛型数组    
	}    
	//使用  
	public static void main(String args[]){    
	       Integer []i = fun1(1,2,3,4,5,6) ;  
	       Integer[] result = fun1(i) ;  
	}  

	

}
