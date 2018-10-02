package assign2;

public class SavingAccount {

	private double balance=10000;
	private int priority = 0;
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public synchronized void deposit(double k) 
	{
		this.balance += k;
		System.out.println("balance "+k+"added: "+this.balance);
		this.notify();
	}
	public synchronized void withdraw(double k,String type) 
	{
		while(balance<k||(priority>0&&!type.equals("preferred")))
		{
			try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.balance -= k;
		System.out.println("balance "+k+type+" reduced: "+this.balance);
		this.notify();
	}
	
	static class AccThread implements Runnable
	{
		SavingAccount account;
		String action;
		double amount;
		String type="ordinary";
		public AccThread(SavingAccount account,String action,double amount,String type)
		{
			this.account = account;
			this.action = action;
			this.amount = amount;
			this.type = type;
		}
		public void setAction(String action)
		{
			this.action = action;
		}
		public void setAccount(SavingAccount account)
		{
			this.account = account;
		}
		public void setAmount(double amount)
		{
			this.amount = amount;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(action.equals("deposit"))
			{
				account.deposit(amount);
			}
			else if(action.equals("withdraw"))
			{
				if(type.equals("preferred"))
				{
					account.setPriority(account.getPriority()+1);
				}
				account.withdraw(amount,type);
				if(type.equals("preferred"))
				{
					account.setPriority(account.getPriority()-1);
				}
			}
		}
		
	}
	public static void test(int thread_number)
	{
		SavingAccount sa = new SavingAccount();
		Thread []tds=new Thread[thread_number];
		for(int i=0;i<thread_number-2;i++)
		{
			String temp=Math.random()>0.8?"deposit":"withdraw";//test the method with more withdraw
			System.out.println(temp);
			tds[i]=new Thread(new AccThread(sa,temp,3000,"ordinary"));
		}
		tds[thread_number-2]=new Thread(new AccThread(sa,"withdraw",3000,"preferred"));
		tds[thread_number-1]=new Thread(new AccThread(sa,"deposit",3000,"ordinary"));
		for(int i=0;i<thread_number;i++)
		{
			tds[i].start();
		}
		for(int i=0;i<thread_number;i++)
		{
			try {
				tds[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		test(10);
	}

}