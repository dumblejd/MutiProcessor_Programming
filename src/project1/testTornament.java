package project1;

public class testTornament {
	public int thread_num;
	public int request;
	public Tournament ptree;
	private int counter=0;
	Mythread tds[];
	public testTornament(int num,int request)
	{
		this.thread_num= num;
		this.ptree= new Tournament(thread_num);
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
			  ptree.lock(id);
			  try 
			  {
			      counter = counter + 1;
			  } 
			  finally 
			  {
				  ptree.unlock(id);
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
		System.out.println(counter==thread_num*request?"tournament match with thread:"+thread_num+" each request:"+request:"dismatch");
		System.out.println("Total time tournament taken: " + (end - start)+"ms");
		return (int)(end - start);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//			testTornament t=new testTornament();
//			try {
//				t.testrun();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		testTornament t=new testTornament(8,100000);
		try {
			System.out.println(t.testrun());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

}
