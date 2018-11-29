package project3;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Node<T> {
	T item;
	int key;
	volatile Node<T> next;
	volatile boolean marked;
	ReentrantLock lock = new ReentrantLock();
	volatile Node<T> pair = null; // use for replace(new,old) new.paired=old
	// boolean tagForOld; //use for replace(new,old) for old node node to inform if
	// it's removed

	Node(T item) {
		this.item = item;
		this.key = item.hashCode();
		this.next = null;
		this.marked = false;
		this.lock = new ReentrantLock();
	}

	public static <T> T[] fun1(T... arg) { // get muti data
		return arg; // return T type
	}

	public void lock() {
		this.lock.lock();
	}

	public void unlock() {
		this.lock.unlock();
	}

	public static void main(String args[]) {
		Integer[] i = fun1(1, 2, 3, 4, 5, 6);
		Integer[] result = fun1(i);

	}

}
