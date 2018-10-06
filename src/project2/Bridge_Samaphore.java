package project2;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Bridge_Samaphore {
	volatile int []waiting = new int[2];
	volatile int []active = new int[2];
	Semaphore []direction;
	Semaphore mutex;;
	Bridge_Samaphore(){
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
		if(active[reverse(myDirection)]>0||waiting[reverse(myDirection)]>0)
		{ //need to wait
			waiting[myDirection]++;
			mutex.release();
			direction[myDirection].acquire();
			System.out.println(Thread.currentThread().getId()+" has arrived bridge  direction: "+myDirection+" opposite waiting:"+waiting[reverse(myDirection)]+"  active:"+active[reverse(myDirection)]+"  myside waiting:"+waiting[myDirection]+"  active:"+active[myDirection]);
		}
			
		else 
		{
			active[myDirection]++;
			System.out.println(Thread.currentThread().getId()+" has arrived bridge  direction: "+myDirection+" opposite waiting:"+waiting[reverse(myDirection)]+"  active:"+active[reverse(myDirection)]+"  myside waiting:"+waiting[myDirection]+"  active:"+active[myDirection]);
			mutex.release();
		}
		
	}
	public void leaveBridge(int myDirection) throws InterruptedException
	{
		mutex.acquire();
		if(active[myDirection]-1==0 && waiting[reverse(myDirection)]>0)
		{
			active[myDirection]--;
			System.out.println(Thread.currentThread().getId()+" has leave bridge  direction: "+myDirection+" opposite waiting:"+waiting[reverse(myDirection)]+"  active:"+active[reverse(myDirection)]+"  myside: waiting"+waiting[myDirection]+"  active:"+active[myDirection]);
			direction[reverse(myDirection)].release(); //if there is one waiting on opposite direction, let it go
			waiting[reverse(myDirection)]--;
			active[reverse(myDirection)]++;
		}
		else if( active[myDirection]-1==0 && waiting[myDirection]>0)
		{ //if no cars on opposite direction, let all cars in my direction go
			active[myDirection]--;
			System.out.println(Thread.currentThread().getId()+" has leave bridge  direction: "+myDirection+" opposite waiting:"+waiting[reverse(myDirection)]+"  active:"+active[reverse(myDirection)]+"  myside: waiting"+waiting[myDirection]+"  active:"+active[myDirection]);
			while(waiting[myDirection]>0)
			{
				direction[myDirection].release();
				active[myDirection]++;
				waiting[myDirection]--;
			}
		}
		else
		{
			active[myDirection]--;
			System.out.println(Thread.currentThread().getId()+" has leave bridge  direction: "+myDirection+" opposite waiting:"+waiting[reverse(myDirection)]+"  active:"+active[reverse(myDirection)]+"  myside: waiting"+waiting[myDirection]+"  active:"+active[myDirection]);
		}
		
		mutex.release();
	}
	static class myThread implements Runnable
	{
		private int direction;
		Bridge_Samaphore bridge;
		public myThread(int direction,Bridge_Samaphore bridge)   //1 represent go  0 represent come
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
		Bridge_Samaphore bridge = new Bridge_Samaphore();
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
		Bridge_Samaphore bridge = new Bridge_Samaphore();
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
		testOneSide(20);
	}

}
