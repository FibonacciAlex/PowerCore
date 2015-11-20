package core.test.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class LockTest {

	static boolean isHold;
	
	public static void main(String[] args){
		ExecutorService pool = Executors.newFixedThreadPool(100);
		pool.submit(() -> {
			Father.write(true);
		});
		pool.submit(() -> {
			Father.read();
		});
		
	}
	
	
}
