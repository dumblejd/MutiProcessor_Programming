package project1;

public class RunThis {

	public int runTournament(int thread_num,int request)
	{
		testTornament t=new testTornament(thread_num,request);
		int re=-9999999;
		try {
			 re=t.testrun();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return re;

	}
	public int runBackery(int thread_num,int request)
	{
		int re=-9999999;
		testBakery t=new testBakery();
		t.setnum(thread_num,request);
		try {
			re=t.testrun();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return re;
	}
	
	public static void main(String[] args) {
		RunThis run = new RunThis();
		
//		for(int i=8;i<9;i++)
//		{
//			double temp[]=new double[2];
//			for(int j=1;j<2;j++)
//			{
//				temp[j]=run.runBackery(i,100000
//						);
//			}
//			double sum=0;
//			double avg=0;
//			for(int k=0;k<temp.length;k++)
//			{
//				sum+=temp[k];
//			}
//			avg =sum/temp.length;
//			System.out.println("30 times bakery with thraed:"+i+", each time 1000000 times, avgerage time:"+avg);
//		}
//		for(int i=8;i<9;i++)
//		{
//			double temp[]=new double[2];
//			for(int j=1;j<2;j++)
//			{
//				temp[j]=run.runTournament(i,100000);
//			}
//			double sum=0;
//			double avg=0;
//			for(int k=0;k<temp.length;k++)
//			{
//				sum+=temp[k];
//			}
//			avg =sum/temp.length;
//			System.out.println("30 times tournament with thraed:"+i+", each time 1000000 times, avgerage time:"+avg);
//		}
//		int td1=Integer.valueOf(args[0]);
//		int rq1=Integer.valueOf(args[1]);
//		int td2=Integer.valueOf(args[2]);
//		int rq2=Integer.valueOf(args[3]);
		run.runBackery(4,10);
		run.runTournament(4, 10);
	}
}
