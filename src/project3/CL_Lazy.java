package project3;

public class CL_Lazy<T> {
	public Node<T> head;

	public CL_Lazy(T min, T max) {
		// Add sentinels to start and end
		this.head = new Node<>(min);
		this.head.next = new Node<>(max);
	}

	public boolean validate(Node<T> pred, Node<T> curr) {// to make sure the node is not marked or from a incomplete
															// replace action
		return !pred.marked && !curr.marked && pred.next == curr;
		// && (curr.pair == null || curr.pair.marked)&& (pred.pair == null ||
		// pred.pair.marked); //no need to judge this because if other thread can lock new added node and the next node, the replace has been finished
	}

	// ====without replace version
	// public boolean contains(T item) {
	// int key = item.hashCode();
	// Node<T> curr = this.head;
	// while (curr.key < key)
	// curr = curr.next;
	// return curr.key == key && !curr.marked;
	// }
	// =============================
	// =====with replace version
	public boolean contains(T item) {
		int key = item.hashCode();
		Node<T> curr = this.head;
		while (curr.key < key)
			curr = curr.next;
		// make sure this is not an 'add' node in an incomplete 'replace' action
		return curr.key == key && !curr.marked && (curr.pair == null || curr.pair.marked);
	}

	// =========================
	public boolean add(T item) {
		// 1.find the window
		int key = item.hashCode();
		while (true) {
			Node<T> pred = head;
			Node<T> curr = head.next;
			while (curr.key < key) {
				pred = curr;
				curr = curr.next;
			}
			// 2.lock the window
			pred.lock.lock();
			try {
				curr.lock.lock();
				try {
					// 3.validate the window
					if (validate(pred, curr)) {
						if (curr.key == key) {
							return false;
						} else {
							// 4.add the new node
							Node<T> node = new Node<T>(item);
							node.next = curr;
							pred.next = node;
							return true;
						}
					}
				} finally {
					curr.lock.unlock();
				}
			} finally {
				pred.lock.unlock();
			}
		}
	}

	public boolean remove(T item) {
		// 1.find the windows
		int key = item.hashCode();
		while (true) {
			Node<T> pred = this.head;
			Node<T> curr = head.next;
			while (curr.key < key) {
				pred = curr;
				curr = curr.next;
			}
			// 2.lock the window
			pred.lock.lock();
			try {
				curr.lock.lock();
				try {
					// 3.validate the window
					if (validate(pred, curr)) {
						if (curr.key != key) {
							return false;
						} else {
							// 4.mark and remove the node
							curr.marked = true; // mark to ture
							pred.next = curr.next; // actually removed
							return true;
						}
					}
				} finally { // always unlock curr
					curr.lock.unlock();
				}
			} finally { // always unlock pred
				pred.lock.unlock();
			}
		}
	}

	public Node[] sort(Node a, Node b, Node c, Node d) {
		Node[] s = new Node[4];
		s[0] = a;
		s[1] = b;
		s[2] = c;
		s[3] = d;
		for (int i = 0; i < s.length; i++) {
			for (int j = i + 1; j < s.length; j++) {
				if (s[i].key > s[j].key) {
					Node temp = s[i];
					s[i] = s[j];
					s[j] = temp;
				}
			}
		}
		return s;
	}

	// test version with return numebr of change to the list
	public int replace_return_int(T oldItem, T newItem) {
		// in order to test I set return value to the change of the list
		int newkey = newItem.hashCode();
		int oldkey = oldItem.hashCode();
		if (newkey == oldkey) {
			return 0;
		}
		while (true) {
			Node<T> addpred = this.head;
			Node<T> addcurr = head.next;
			while (addcurr.key < newkey) {
				addpred = addcurr;
				addcurr = addcurr.next;
			}
			Node<T> delpred = this.head;
			Node<T> delcurr = head.next;
			while (delcurr.key < oldkey) {
				delpred = delcurr;
				delcurr = delcurr.next;
			}
			Node[] s = this.sort(addpred, addcurr, delpred, delcurr);
			s[0].lock.lock();
			try {
				s[1].lock.lock();
				try {
					s[2].lock.lock();
					try {
						s[3].lock.lock();
						try {

							if (validate(addpred, addcurr) && validate(delpred, delcurr)) {
								if (addcurr.key == newkey && delcurr.key != oldkey) {
									// if there is a new key and there isn't an oldkey do nothing
									return 0;
								} else if (addcurr.key != newkey && delcurr.key != oldkey) {
									// just add
									Node<T> node = new Node<T>(newItem);
									node.pair = null;
									node.next = addcurr;
									addpred.next = node;
									return 1;
								} else if (addcurr.key == newkey && delcurr.key == oldkey) {
									// just remove
									delcurr.marked = true; // mark
									delpred.next = delcurr.next; // physically remove
									return -1;
								} else { // if there isn't new node and there is an old node add then remove
									// add
									Node<T> node = new Node<T>(newItem);
									node.pair = delcurr;// it should be set before it insert into list
									node.next = addcurr;
									addpred.next = node;
									// remove
									delcurr.marked = true; // mark
									if (delcurr.key == addcurr.key) {
										node.next = delcurr.next;// physically remove in condition window is the
																	// same
										node.pair = null;
									} else {
										delpred.next = delcurr.next; // physically remove
										node.pair = null;
									}
									return 0;
								}
							}
						} // vali
						finally {
							s[3].lock.unlock();
						}
					} finally {
						s[2].lock.unlock();
					}
				} finally {
					s[1].lock.unlock();
				}
			} finally {
				s[0].lock.unlock();
			}

		}
	}

	public void print() {
		Node pt = this.head;
		System.out.print(pt.key + " ");
		while (pt.next != null) {
			pt = pt.next;
			System.out.print(pt.key + " ");
		}
		System.out.println();
	}

	public int length()// length contains head and tail
	{
		int result = 0;
		Node current = this.head;
		while (null != current) {
			result++;
			current = current.next;
		}
		return result;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
