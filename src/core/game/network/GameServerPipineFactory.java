package core.game.network;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.IdleStateHandler;

public class GameServerPipineFactory extends ChannelInitializer<Channel>{

	private ChannelHandler handler;
	private IdleStateHandler idleStateHandler;
	private GameNetManeger netManager;
	
	


	public GameServerPipineFactory(ChannelHandler handler,
			IdleStateHandler idleStateHandler, GameNetManeger netManager) {
		super();
		this.handler = handler;
		this.idleStateHandler = idleStateHandler;
		this.netManager = netManager;
	}




	@Override
	protected void initChannel(Channel ch) throws Exception {
		// 设置心跳
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast("idleTimeOut", idleStateHandler);
		pipeline.addLast("gameHeartBeat", new GameNetHeartbeat(netManager));
		
		//设置编码解码器
		pipeline.addLast("gameEncoder", new GameMessageEncoder());
		pipeline.addLast("gameDecoder", new GameMessageDecoder());
		
		
		
		pipeline.addLast("gameHandler", handler);
	}

}
