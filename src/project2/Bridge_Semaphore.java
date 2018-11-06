package project2;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Bridge_Semaphore {
	volatile int []waiting = new int[2];
	volatile int []active = new int[2];
	Semaphore []direction;
	Semaphore mutex;;
	Bridge_Semaphore(){
		waiting[0]=0;
		waiting[1]=0;
		active[0]=0;
		active[1]=0;
		direction=new Semaphore[] {new Semaphore(0),new Semaphore(0)};
		mutex = new Semaphore(1);
	}
	public int reverse(int myDirection)
	{
		return 1-myDirection;
	}
	public void arriveBridge(int myDirection) throws InterruptedException
	{
		mutex.acquire();
		System.out.println(Thread.currentThread().getId()+" has arrived bridge  direction: "+myDirection);
		if(active[reverse(myDirection)]>0||waiting[reverse(myDirection)]>0)
		{ //need to wait if there are cars on opposite side
			waiting[myDirection]++;
			mutex.release();
			direction[myDirection].acquire();
		}
			
		else //only cars on one side
		{
			active[myDirection]++;
			mutex.release();
		}
		
	}
	public void leaveBridge(int myDirection) throws InterruptedException
	{
		mutex.acquire();
		System.out.println(Thread.currentThread().getId()+" has leave bridge  direction: "+myDirection);
		if(active[myDirection]-1==0 && waiting[reverse(myDirection)]>0 && waiting[myDirection]>0)
		{ // if there are cars on both sides let opposite direction go after this direction leave
			active[myDirection]--;
			direction[reverse(myDirection)].release(); //if there is one waiting on opposite direction, let it go
			waiting[reverse(myDirection)]--;
			active[reverse(myDirection)]++;
		}
		else if( active[myDirection]-1==0 && waiting[reverse(myDirection)]>0 && waiting[myDirection]==0 )
		{ //if no cars on my direction, let all cars in opposite direction go
			active[myDirection]--;
			while(waiting[reverse(myDirection)]>0)
			{
				direction[reverse(myDirection)].release();
				active[reverse(myDirection)]++;
				waiting[reverse(myDirection)]--;
			}
		}
		else //if there is only cars on one side, just let it go
		{
			active[myDirection]--;
		}
		
		mutex.release();
	}
	static class myThread implements Runnable
	{
		private int direction;
		Bridge_Semaphore bridge;
		public myThread(int direction,Bridge_Semaphore bridge)   //1 represent go  0 represent come
		{
			this.direction=direction;
			this.bridge = bridge;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				bridge.arriveBridge(direction);
				
				bridge.leaveBridge(direction);
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	public static void test(int num)
	{
		Bridge_Semaphore bridge = new Bridge_Semaphore();
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
		Bridge_Semaphore bridge = new Bridge_Semaphore();
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
