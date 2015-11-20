package core.test.concurrent;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class ConcurrentTest {

	
	private static final int MAX_SIGNAL = 10;
	
	
	final static Semaphore semaphore = new Semaphore(MAX_SIGNAL);
	
	public static void main(String [] args) throws InterruptedException{
		System.out.println("current signals:" + semaphore.availablePermits());
		//单个线程固定每秒释放信号量
		Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
			System.out.println("release 10  signals");
			semaphore.release(MAX_SIGNAL);
		}, 1000, 1000, TimeUnit.MILLISECONDS); //每秒释放10个信号
		
		
		//创建多个线程
		ExecutorService pool = Executors.newFixedThreadPool(100);
		for (int i = 0; i < 100; i++) {
			int x = i;
			pool.submit(() -> {
				semaphore.acquireUninterruptibly();
				remoteCall(x);
			});
		}
		pool.shutdown();
		pool.awaitTermination(1, TimeUnit.SECONDS);
		
	}
	
	
	private static void remoteCall(int i){
		System.out.println(String.format("%s - %s: %d",new Date(),
				            Thread.currentThread().getName(), i) + "---可用信号量:" + semaphore.availablePermits());
	}
	
	
}
