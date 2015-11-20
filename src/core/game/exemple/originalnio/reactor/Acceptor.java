package core.game.exemple.originalnio.reactor;

import java.nio.channels.SocketChannel;

public class Acceptor implements Runnable{

	private Reactor reactor;
	
	public Acceptor(Reactor reactor) {
		this.reactor = reactor;
	}

	@Override
	public void run() {

		try {
			
			
			SocketChannel channel = reactor.sChannel.accept();
			if(channel != null){
				new SocketReadHandler(reactor.selector, channel);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	

}
