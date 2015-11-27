package core.game.spider.simpleSpider;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.game.spider.HttpTools;
import core.game.spider.LinkFilter;
import core.game.spider.LinkQueue;

/**
 * 一个简单的单线程的网络爬虫   可抓取图片
 * @author Alex
 * 2015年11月26日 下午8:11:42
 */
public class MyCrawler {

	private final static Logger log = LoggerFactory.getLogger(MyCrawler.class);
	
	private String TARGET_SITE = "";
	
	/**
	 * 等待抓取的url个数
	 */
	private static int WAIT_CATCH_URL_SIZE = 50;
	
	private void initCrawlerWithSeed(String[] seeds){
		for (int i = 0; i < seeds.length; i++) {
			LinkQueue.addUnVisitUrl(seeds[i].trim());
		}
	}
	
	public void crawling(String[] seeds){
		//定义过滤器，用于过滤是否为目标链接
		
		LinkFilter filter = new LinkFilter() {
			
			@Override
			public boolean accept(String url) {
				if(url.contains(TARGET_SITE)){
					return true;
				}
				return false;
			}
		};		
		
		
		initCrawlerWithSeed(seeds);
		
		
//		The following code will get all the images inside a link tag.

//		Node node ;
//			 ImageTag imageTag;
//			 for (Enumeration e=linkTag.children();e.hasMoreElements();) {
//			      node = (Node)e.nextElement();
//			      if (node instanceof ImageTag) {
//			          imageTag = (ImageTag)node;
//			          // Process imageTag
//			      }
//			 }
		
		while (!LinkQueue.unVisitUrlIsEmpty() && LinkQueue.getVisitUrlSize() <= WAIT_CATCH_URL_SIZE) {
			
			
			//头url出队列
			String url = (String) LinkQueue.unVisitUrlDeQueue();
			System.err.println("-----excute link get image [{}]"+url);
			
			//获取图片链接
			Set<String> set = HttpTools.getImageLink(url);
			set.forEach(i -> {
				log.debug("-----------down load image[{}]",i);
				//下载图片
				HttpTools.downLoadFile(i);
			});
			
			
			LinkQueue.addVisitUrl(url);
			
			//提取的链接
			Set<String> extractLink = HttpTools.extractLink(url, filter);
			
			extractLink.forEach(i -> {
				LinkQueue.addUnVisitUrl(i);
				log.debug("----------add link[{}]",i);
			});
			
		}
		
		
		
		
	}
	
	public static void main(String[] args){
		MyCrawler crawler = new MyCrawler();
		crawler.TARGET_SITE = "163.com/photo";
		
		crawler.crawling(new String[]{"http://news.163.com/photo"});
		
	}
}
