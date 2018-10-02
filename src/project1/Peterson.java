package project1;

import java.util.concurrent.atomic.AtomicBoolean;

public class Peterson {
	private volatile boolean[] flag;
	private volatile int victim;
	
	Peterson()
	{
		flag = new boolean[2];
	}
	
	public void lock(int i) {
		// either 0 or 1 int j = 1-i;
		i = i%2;
		flag[i] = true;
		victim = i;
		int j=1-i;
		while (flag[j] && victim == i) {}; // spin 
		}
	public void unlock(int i)
	{
		i = i%2;
		flag[i] = false;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
//	private AtomicBoolean[] flag=new AtomicBoolean[2];
//	private int victim;
//	
//	public Peterson(){
//        for(int i=0 ; i<flag.length ; ++i)
//            flag[i] = new AtomicBoolean();
//    }
//	
//	public void lock(int i) {
//		// either 0 or 1 int j = 1-i;
//		i = i%2;
//		flag[i].set(true);
//		victim = i;
//		int j=1-i;
//		while (flag[j].get() && victim == i) {}; // spin 
//		}
//	public void unlock(int i)
//	{
//		i = i%2;
//		flag[i].set(false);
//	}
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//
//	}
}
