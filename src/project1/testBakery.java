package project1;


public class testBakery {
	public int thread_num=8;
	public int request=100000;
	public Backery bac=new Backery(thread_num);
	private int counter=0;
	Mythread []tds;
	public void setnum(int num,int request)
	{
		this.thread_num= num;
		this.bac= new Backery(num);
		this.request= request;
		tds=new Mythread[thread_num];
	}
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
	    	  bac.lock(id);
			  try 
			  {
			      counter = counter + 1;
			  } 
			  finally 
			  {
				  bac.unlock(id);
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
		System.out.println(counter==thread_num*request?"bakery match with thread:"+thread_num+" each request:"+request:"dismatch");
		System.out.println("Total time bakery taken: " + (end - start)+"ms");
		return (int) (end-start);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
			testBakery t=new testBakery();
			try {
				t.testrun();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}


}
