package project2;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Bridge_Monitor {
	Lock lock=new ReentrantLock();
	volatile int []waitingCars = new int[2];
	volatile int []activeCars = new int[2];
	Condition [] condition = new Condition[2];
	Bridge_Monitor() {
		waitingCars[0] = 0;
		waitingCars[1] = 0;
		activeCars[0]=0;
		activeCars[1]=0;
		condition[0]=lock.newCondition();
		condition[1]=lock.newCondition();
	}
	public int reverse(int myDirection)
	{
		return 1-myDirection;
	}
	void ArriveBridge (int myDir) throws InterruptedException 
	{
		lock.lock();
		System.out.println(Thread.currentThread().getId()+" has arrived bridge  direction: "+myDir);
		if (activeCars[reverse(myDir)]>0 || waitingCars[reverse(myDir)]>0) // if there is someone on other side, stuck this one
		{
			waitingCars[myDir]++;       
			condition[myDir].await();
		}
		else //ok to go
		{
			activeCars[myDir]++;
		}
		lock.unlock();
	}
	void LeaveBridge(int myDir)
	{
		lock.lock();
		System.out.println(Thread.currentThread().getId()+" has leaved bridge  direction: "+myDir);
	    if (activeCars[myDir]-1 == 0 && waitingCars[reverse(myDir)]>0 && waitingCars[myDir]>0) 
	    	// if there are cars on both sides let opposite direction go after this direction leave
		{
	        activeCars[myDir]--;
	        condition[reverse(myDir)].signal();
	        waitingCars[reverse(myDir)]--;
	        activeCars[reverse(myDir)]++; 
	    }
	    else if(activeCars[myDir]-1 == 0 && waitingCars[reverse(myDir)]>0 && waitingCars[myDir]==0)
	     //if no cars on my direction, let all cars in opposite direction go
	    {
	    	activeCars[myDir]--;
	    	while(waitingCars[reverse(myDir)]>0)
	    	{
	    		activeCars[reverse(myDir)]++;
		    	condition[reverse(myDir)].signal();
		    	waitingCars[reverse(myDir)]--;
	    	}
	    }
	    else
	    {
	      activeCars[myDir]--;
	    }
		lock.unlock();
	}
	static class myThread implements Runnable
	{
		private int direction;
		Bridge_Monitor bridge;
		public myThread(int direction,Bridge_Monitor bridge)   //1 represent go  0 represent come
		{
			this.direction=direction;
			this.bridge = bridge;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				bridge.ArriveBridge(direction);
				
				bridge.LeaveBridge(direction);
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	public static void test(int num)
	{
		Bridge_Monitor bridge = new Bridge_Monitor();
		Thread[] t = new Thread[num];
		int dir=0;
		for(int i=0;i<num;i++)
		{
			t[i]=new Thread(new myThread(1-dir,bridge));
			dir=1-dir;
		}
		for(int i=0;i<num;i++)
		{
			t[i].start();
		}
		for(int i=0;i<num;i++)
		{
			try {
				t[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static void testOneSide(int num)
	{
		Bridge_Monitor bridge = new Bridge_Monitor();
		Thread[] t = new Thread[num];
	
		for(int i=0;i<num;i++)
		{
			t[i]=new Thread(new myThread(1,bridge));
		
		}
		t[num/3]=new Thread(new myThread(0,bridge));
		t[num/2]=new Thread(new myThread(0,bridge));
		for(int i=0;i<num;i++)
		{
			t[i].start();
		}
		for(int i=0;i<num;i++)
		{
			try {
				t[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//test(20);
		test(10);
	}


}
