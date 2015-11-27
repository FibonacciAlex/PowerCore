package core.game.spider;

import java.util.LinkedList;

/**
 * 管理网页url的实现队列
 * @author Alex
 * 2015年11月26日 上午11:04:18
 */
public class Queue {

	private LinkedList<Object> queueList= new LinkedList<Object>();
	
	public void inQueue(Object obj){
		queueList.add(obj);
	}
	
	public Object outQueue(){
		return queueList.removeFirst();
	}
	
	public boolean isEmpty(){
		return queueList.isEmpty();
	}
	
	public boolean contain(Object obj){
		return queueList.contains(obj);
	}
	
}
