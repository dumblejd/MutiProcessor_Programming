package project2;

import java.util.concurrent.Semaphore;

public class Bridge_Samaphore {
	Semaphore bridge = new Semaphore(1);
	Semaphore back = new Semaphore(1);
	Semaphore forth = new Semaphore(1);
	private int num_back=0;
	private int num_forth=0;
	public void arriveBridge(int direction)
	{
		if(direction==0)//back
		{
			try {
				back.acquire();
				if(num_forth>0)
				{
					bridge.acquire();
				}
				num_back++;
				back.release();
				//===========cs
				back.acquire();
				num_back--;
				if(num_forth>0)
				{
					bridge.release();
				}
				back.release();
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else //forth
		{
			try {
				forth.acquire();
				num_forth++;
				if(num_back>0)
				{
					back.acquire();
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	public void leaveBridge(int direction)
	{
		if(direction==0)//back
		{
			try {
				num_back--;
				back.release();
				if(num_forth>0)
				{
					
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else //forth
		{
			
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

}
