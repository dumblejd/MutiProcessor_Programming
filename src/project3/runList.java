package project3;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class runList {
	public static AtomicInteger expectedLength = new AtomicInteger(0);

	static class myThread implements Runnable {
		private static ThreadLocal<Integer> myChangeNumber = new ThreadLocal<>();
		int value;
		int action;
		CL_Lazy cl;

		public static int getChangeNumber() {
			return myChangeNumber.get();
		}

		public myThread(int value, int action, CL_Lazy cl) // 1 represent go 0 represent come
		{
			this.value = value;
			this.action = action;
			this.cl = cl;
			// myChangeNumber.set(0);
		}

		public myThread(CL_Lazy cl) {
			this.cl = cl;
		}

		@Override
		public void run() {
			myChangeNumber.set(0);// set a local value to calculate expected change numebr of one thread
			for (int i = 0; i < 100000; i++) {
				int action = new Random().nextInt(3) + 1;
				switch (action) {
				case 1: // add
					int n = new Random().nextInt(20) + 1;
					// System.out.println("add: "+n);
					int ifadd = cl.add(n) == true ? 1 : 0;
					// System.out.println(myChangeNumber.get());
					myChangeNumber.set(myChangeNumber.get() + ifadd);
					// System.out.println(Thread.currentThread().getId()%10+" after add: "+n+" :
					// "+myChangeNumber.get());
					// System.out.println(Thread.currentThread().getId()%10+" add: "+n);
					break;
				case 2:// remove
					int o = new Random().nextInt(20) + 1;
					// System.out.println("remove: "+o);
					int ifremove = cl.remove(o) == true ? -1 : 0;

					myChangeNumber.set(myChangeNumber.get() + ifremove);
					// System.out.println(Thread.currentThread().getId()%10+" after remove: "+o+" :
					// "+myChangeNumber.get());
					// System.out.println(Thread.currentThread().getId()%10+" remove: "+o);
					break;
				case 3:// replace

					int oo = new Random().nextInt(20) + 1;
					int nn = new Random().nextInt(20) + 1;
					// System.out.println("replace: "+oo+" "+nn);
					int ifreplace = cl.replace_return_int(oo, nn);
					// System.out.println(myChangeNumber.get());
					myChangeNumber.set(myChangeNumber.get() + ifreplace);
					// System.out.println(Thread.currentThread().getId()%10+" after replace: "+oo+"
					// "+nn+" : "+myChangeNumber.get());
					// System.out.println(Thread.currentThread().getId()%10+" replace: "+oo+" "+nn);
					break;
				}
			}
			// System.out.println(myChangeNumber.get());
			expectedLength.set(expectedLength.get() + myChangeNumber.get());
		}
	}

	public static void test(int num) {
		CL_Lazy<Integer> cl = new CL_Lazy<Integer>(Integer.MIN_VALUE, Integer.MAX_VALUE);
		Thread[] t = new Thread[num];
		long startTime = System.nanoTime();
		for (int i = 0; i < num; i++) {
			t[i] = new Thread(new myThread(cl));
		}
		for (int i = 0; i < num; i++) {
			t[i].start();
		}
		for (int i = 0; i < num; i++) {
			try {
				t[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("final list length(not include head and tail): " + (cl.length() - 2));
		System.out.println("Expected length：" + expectedLength.get());
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		test(10);

	}

}
