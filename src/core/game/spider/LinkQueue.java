package core.game.spider;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

public class LinkQueue {

	private static Set<String> visitUrl = new HashSet<String>();
	
	private static Queue unVisitUrl = new Queue();
	
	public static Queue getUnVisitUrl(){
		return unVisitUrl;
	}

	public static Set<String> getVisitUrl() {
		return visitUrl;
	}
	
	public static void addVisitUrl(String url){
		visitUrl.add(url);
	}
	
	public static void removeVisitUrl(String url){
		visitUrl.remove(url);
	}

	/**
	 * 获取一个没访问的url
	 * @return
	 */
	public static Object unVisitUrlDeQueue(){
		return unVisitUrl.outQueue();
	}
	
	//保证每个URL只被访问一次,url不能为空,同时已经访问的URL队列中不能包含该url,而且因为已经出队列了所未访问的URL队列中也不能包含该url
	public static void addUnVisitUrl(String url){
		if(url != null && !url.trim().equals("") && !visitUrl.contains(url) && ! unVisitUrl.contain(url)){
			unVisitUrl.inQueue(url);
		}
	}
	
	
	public static int getVisitUrlSize(){
		return visitUrl.size();
	}
	
	public static boolean unVisitUrlIsEmpty(){
		return unVisitUrl.isEmpty();
	}
	
}
