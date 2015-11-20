package core.game.network;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.game.server.GameServer;


/**
 * 处理空闲时连接逻辑(是否断开，是否继续心跳等)
 * @author Alex
 * 2015年7月30日 下午4:33:36
 */
public class GameNetHeartbeat extends ChannelHandlerAdapter{

	private final static Logger log = LoggerFactory.getLogger(GameNetHeartbeat.class);
	
	private GameNetManeger netManager;

	public GameNetHeartbeat(GameNetManeger netManager) {
		super();
		this.netManager = netManager;
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		 if (evt instanceof IdleStateEvent) {
             IdleStateEvent e = (IdleStateEvent) evt;
             if (e.state() == IdleState.READER_IDLE) { // 暂时没有数据被接收    长时间没有收到客户消息的时候触发
            	 log.warn("system enter read idle stat!!!");
                switch (netManager.getServerType().getType()) {
				case GameServer.SERVER_TYPE_FE:
					break;
				case GameServer.SERVER_TYPE_GS:
					break;
                   
				default:
					break;
				}
             } else if (e.state() == IdleState.WRITER_IDLE) {//暂时没有数据被发送.
            	 //写空闲状态
            	 log.warn("system enter write idle stat!!");
//                 ctx.writeAndFlush(new PingMessage());  可以尝试发送一个ping信息
             }
         }

	}

	
	
}
