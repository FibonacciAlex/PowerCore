package core.game.frontend;

import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.game.GameNetMessage;


public class GameFrontendHandler extends ChannelHandlerAdapter/*extends SimpleChannelHandler*/{

	
	private static final Logger log = LoggerFactory.getLogger(GameFrontendHandler.class);
	
	private GameFrontend fe;
	public final static ConcurrentHashMap<Channel, Object> handshakeMap = new ConcurrentHashMap<Channel, Object>();

	public GameFrontendHandler(GameFrontend fe) {
		super();
		this.fe = fe;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		super.exceptionCaught(ctx, cause);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		if(!(msg instanceof GameNetMessage)){
			log.warn("Illegal message type:[{}]",msg.toString());
			return;
		}
		Channel channel = ctx.channel();
		GameNetMessage kmsg = (GameNetMessage) msg;

		int msgID = kmsg.getMsgID();
		byte msgType = kmsg.getMsgType();
		byte clientType = kmsg.getClientType();

	
	
		log.debug("rec msg. msgid[{}],msgtype[{}],clienttype[{}]",msgID, msgType,clientType);
	
	}
	
	
}
