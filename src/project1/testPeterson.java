package project1;

import project1.testTornament.Mythread;

public class testPeterson {
	public int thread_num=2;
	public int request=100000;
	private int counter=0;
	Peterson pt=new Peterson();
	Mythread tds[]=new Mythread[thread_num];
	class Mythread extends Thread 
	{
		Mythread(int i)
		{
			id=i;
		}
		private int id;
		@Override
	    public void run() 
	    {
	      for (int i = 0; i < request; i++) 
	      {
			  pt.lock(id);
			  try 
			  {
			      counter = counter + 1;
			  } 
			  finally 
			  {
				  pt.unlock(id);
			  }
	      }
	    }
	  }
	public int testrun() throws Exception
	{
		for(int i=0;i<tds.length;i++)
		{
			tds[i]=new Mythread(i);
		}
		long start = System.currentTimeMillis();
		for(int i=0;i<tds.length;i++)
		{
			tds[i].start();
		}
		for(int i=0;i<tds.length;i++)
		{
			tds[i].join();
		}
		long end = System.currentTimeMillis();
//		System.out.println(counter);
//		System.out.println("Total time taken: " + (end - start));
		//System.out.println((int) (end-start));
		return (int) (end-start);
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		testPeterson t=new testPeterson();
			try {
				t.testrun();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

}
