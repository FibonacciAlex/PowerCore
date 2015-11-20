package core.game.exemple.originalnio.reactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class SocketReadHandler implements Runnable{

	
	private SocketChannel socketChannel;

	public SocketReadHandler(Selector selector,SocketChannel socketChannel) throws IOException {
		super();
		this.socketChannel = socketChannel;
		socketChannel.configureBlocking(false);
		
		//将SelectionKey绑定为本Handler 下一步有事件触发时，将调用本类的run方法。 
		//参看dispatch(SelectionKey key)  
		SelectionKey key = socketChannel.register(selector, 0);
		key.attach(this);
		
		//同时将SelectionKey标记为可读，以便读取。 
		key.interestOps(SelectionKey.OP_READ);
		selector.wakeup();
	}

	/**
	 * 处理读取数据
	 */
	@Override
	public void run() {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		buffer.clear();
		try {
			
			socketChannel.read(buffer);
			//激活线程池 处理这些request  
			//requestHandle(new Request(socket,btt));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
}
