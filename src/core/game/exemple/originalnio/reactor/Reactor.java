package core.game.exemple.originalnio.reactor;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * <pre>
 * 一个基于原生java  nio编写的网络程序（以Server端TCP连接为例）
 * 主要流程如下：
 * 1.监听端口，建立Socket连接
 * 2.建立线程，处理内容
 *   a.读取Socket内容，并对协议进行解析
 *   b.进行逻辑处理
 *   c.回写响应内容
 *   d.如果是多次交互的应用(SMTP、FTP)，则需要保持连接多进行几次交互
 * 3.关闭连接
 * 
 * 以上为单一连接过程，但在实际产生环境里，我们会使用多线程机制，因为建立线程是一个比较耗时的操作，同时维护线程本身也有一些开销，
 * 此外，因为TCP连接的特性，我们还要使用连接池来进行管理：
 * 1.建立TCP连接是比较耗时的操作，对于频繁的通讯，保持连接效果更好
 * 2.对于并发请求，可能需要建立多个连接
 * 3.维护多个连接后，每次通讯，需要选择某一可用连接
 * 4.连接超时和关闭机制
 * 
 * 
 * 以下例子是反应器（Reactor）模式的实现 ，用于解决多用户访问并发问题 
 * 举个例子：餐厅服务问题 
 * 传统线程池做法：来一个客人(请求)去一个服务员(线程) 
 * 反应器模式做法：当客人点菜的时候，服务员就可以去招呼其他客人了，等客人点好了菜，直接招呼一声“服务员” 
 * </pre>
 * @author Alex
 * 2015年7月1日 上午10:57:47
 */
public class Reactor implements Runnable{
	
	public final Selector selector;
	
	public final ServerSocketChannel sChannel;
	
	

	public Reactor(int port) throws IOException{
		super();
		this.selector = Selector.open();
		this.sChannel = ServerSocketChannel.open();
		InetSocketAddress address = new InetSocketAddress(InetAddress.getLocalHost(), port);
		sChannel.socket().bind(address);//打开通道并绑定端口
		sChannel.configureBlocking(false);//设置通道为非阻塞模式
		
		SelectionKey register = sChannel.register(selector, SelectionKey.OP_ACCEPT);//向selector注册该channel   
		
		//利用selectionKey的attache功能绑定Acceptor 如果有事情，触发Acceptor  
		register.attach(new Acceptor(this));
		
	}



	@Override
	public void run() {
		try {
			
			while (!Thread.interrupted()) {

				selector.select();
				Set<SelectionKey> keys = selector.selectedKeys();


				Iterator<SelectionKey> it = keys.iterator();
				while (it.hasNext()) {
					SelectionKey key = it.next();
					this.dispatch(key);
				}
				
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 执行每个selector 里的 附加对象
	 * @param key
	 */
	public void dispatch(SelectionKey key){
		Runnable run = (Runnable) key.attachment();
		if(run != null){
			run.run();
		}
	}

}
