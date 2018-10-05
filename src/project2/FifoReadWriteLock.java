package project2;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;

import assign2.SavingAccount;

/**
 * 公平的读写锁。相比{@link SimpleReadWriteLock}，提供了保证写锁相比读锁具有优先级的机制。
 */
public class FifoReadWriteLock implements ReadWriteLock {

    private volatile int readAcquires;

    private volatile int readReleases;

    private volatile boolean writer;

    private Lock lock;

    private Condition condition;

    private SimpleLock readLock;

    private SimpleLock writeLock;

    public FifoReadWriteLock() {
        this.readAcquires = 0;
        this.readReleases = 0;
        this.writer = false;
        this.lock = new ReentrantLock(true);
        this.condition = lock.newCondition();
        this.readLock = new ReadLock();
        this.writeLock = new WriteLock();
    }

    @Override
    public Lock readLock() {
        return readLock;
    }

    @Override
    public Lock writeLock() {
        return writeLock;
    }

    private class ReadLock implements SimpleLock {
        @Override
        public void lock() {
            lock.lock();
            try {
                while (writer) {
                    condition.await();
                }
                readAcquires++;
            } catch (InterruptedException e) {
                // empty
            } finally {
                lock.unlock();
            }
        }

        @Override
        public void unlock() {
            lock.lock();
            try {
                readReleases++;
                // 最后一个读锁被释放，唤醒所有在condition上等待的线程。
                if (readAcquires == readReleases) {
                    condition.signalAll();
                }
            } finally {
                lock.unlock();
            }
        }
    }

    private class WriteLock implements SimpleLock {
        @Override
        public void lock() {
            lock.lock();
            try {
                while (writer) {
                    condition.await();
                }
                /*
                 * 只要没有其它线程正在写，则置writer为true,此后不会再有新增的获得读锁的线程了。
                 * 后继的试图获得写锁的请求也会在condition上等待。再接下来该线程会等待所有当前读请求释放读锁。
                 */
                writer = true;
                while (readAcquires != readReleases) {
                    condition.await();
                }
            } catch (InterruptedException e) {
                // empty
            } finally {
                lock.unlock();
            }
        }

        @Override
        public void unlock() {
            writer = false;
            condition.signalAll();
        }
    }
    static class MyThread implements Runnable
	{
    	FifoReadWriteLock rw;
		String action;
		double amount;
		String type="ordinary";
		int condition=1;
		MyThread(FifoReadWriteLock rw,int condition)
		{
			this.rw=rw;
			this.condition=condition;
		}
//		public MyThread(SavingAccount account,String action,double amount,String type)
//		{
//			this.account = account;
//			this.action = action;
//			this.amount = amount;
//			this.type = type;
//		}
//		public void setAction(String action)
//		{
//			this.action = action;
//		}
//		public void setAccount(SavingAccount account)
//		{
//			this.account = account;
//		}
//		public void setAmount(double amount)
//		{
//			this.amount = amount;
//		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(this.condition==1)
			{
			rw.writeLock().lock();
			rw.writeLock().unlock();
			}
			else {
			rw.readLock().lock();
			rw.readLock().unlock();
			}
		}
		
	}
	public static void test(int thread_number)
	{
		FifoReadWriteLock f = new FifoReadWriteLock();
		Thread a = new Thread(new MyThread(f,1));
		Thread b = new Thread(new MyThread(f,0));
		Thread c = new Thread(new MyThread(f,0));
		Thread d = new Thread(new MyThread(f,0));
		a.start();
		b.start();
		c.start();
		d.start();
				try {
					a.join();
					b.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
			test(1);
	}
}