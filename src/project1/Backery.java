package project1;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Backery {
//===failed version with volatile
//	private volatile boolean[] flag;
//	private volatile int [] label;
//	public Backery(int thread_num)
//	{
//		flag = new boolean[thread_num];
//		label = new int[thread_num];
//	}
//	public void lock(int id)
//	{
//		flag[id] = true;
//		label[id] = findmax()+1;
//		for(int i = 0; i < label.length;i++)
//		{
//			while ((i != id) && flag[i] && ((label[i] < label[id]) || (label[i] == label[id] && i < id))) 
//			{
//            }
//		}
//	}
//	public void unlock(int id)
//	{
//		flag[id] = false;
//	}
//	public int findmax() {
//		int max = 0;
//		for(int i:label)
//		{
//			max = max>i?max:i;
//		}
//		return max;
//	}
//	
	private AtomicBoolean[] flag;
	private AtomicInteger [] label;
	public Backery(int thread_num)
	{
		flag =new AtomicBoolean [thread_num];
		label =new AtomicInteger [thread_num];
		for (int i = 0; i < thread_num; i++) {
            flag[i] = new AtomicBoolean();
            label[i] = new AtomicInteger();
        }
	}
	public void lock(int id)
	{
		flag[id].set(true);
		label[id].set(findmax()+1);
		for(int i = 0; i < label.length;i++)
		{
			while ((i != id) && flag[i].get() && ((label[i].get() < label[id].get()) || (label[i].get() == label[id].get() && i < id))) 
			{
            }
		}
	}
	public void unlock(int id)
	{
		flag[id].set(false);;
	}
	public int findmax() {
		int max = 0;
		for(AtomicInteger i:label)
		{
			max = max>i.get()?max:i.get();
		}
		return max;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
