package project3;

public class CL_Lazy<T> {
	public Node head;
	public Node tail;
	public Node curr;
	public boolean add(T item) 
	{
		int key = item.hashCode();
		while (true) {
		Node pred = head;
		Node curr = head.next;
		while(curr.key<key)
		{
			pred = curr;
			curr = curr.next;
			pred.lock();
			try 
			{
				curr.lock();
				try 
				{
					if (validate(pred, curr)) 
					{
						if (curr.key == key) 
						{
							return false;
						} 
						else 
						{
							Node node = new Node(item);
							node.next = curr;
							pred.next = node;
							return true;
						}
					}
				} 
				finally 
				{
					curr.unlock();
				}
			} 
			finally 
			{
				pred.unlock();
			}

	}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

}
