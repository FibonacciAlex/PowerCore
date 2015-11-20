package core.test.concurrent;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.management.timer.Timer;

public class Father {
	
	List<Integer> useList = new ArrayList<Integer>();
	
	List<Integer> emptyList = new ArrayList<Integer>();
	
	public Father(){
		System.out.println("------------init father!");
	}
	
	static boolean isHold;
	int tt;
	
	public synchronized static void write(boolean b){
		System.out.println("thread get write:" + Thread.currentThread().getName()+ ", current time:" + LocalTime.now().getMinute());
		try {
			Thread.sleep(1*Timer.ONE_MINUTE);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		isHold = b;
		System.out.println("-----writer sleep over and release object!, current time:" + LocalTime.now().getMinute());
		
	}
	
	public synchronized static boolean read(){
		System.out.println("thread  get read:" + Thread.currentThread().getName() + ", current time:" + LocalTime.now().getMinute());
		try {
			Thread.sleep(1*Timer.ONE_MINUTE);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("----reader  sleep over and release object!, current time:" + LocalTime.now().getMinute());
		return isHold;
	}
}
