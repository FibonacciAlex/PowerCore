package core.game.frontend;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import core.game.GameNetMessage;
import core.game.GameProtocol;
import core.game.network.GameNetManeger;
import core.game.player.PlayerSessoin;


public class GameFrontendHandler extends ChannelHandlerAdapter implements GameProtocol{

	
	private static final Logger log = LoggerFactory.getLogger(GameFrontendHandler.class);
	
	private GameFrontend fe;
	public final static ConcurrentHashMap<Channel, PlayerSessoin> handshakeMap = new ConcurrentHashMap<Channel, PlayerSessoin>();

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
	
		PlayerSessoin session = handshakeMap.get(channel);
		
		switch (msgID) {
			case MID_PING:
			if(session == null){
				channel.close();
				break;
			}
			
			//TODO 检查是否正常连接      是否存在作弊判断
			
			
			break;
			
			//step1-握手消息
			case MID_HANDSHAKE:
				
				String code = kmsg.readUTF8String();
				String client = kmsg.readUTF8String();
				log.debug("MID_HANDSHAKE  code:{}, client{}", code, client);
				
				//TODO 对握手消息进行安全验证
				session = new PlayerSessoin(channel);
				session.setClientType(clientType);
				session.setCurrentDevice(client);
				
				handshakeMap.put(channel, session);
				
				GameNetMessage shakeHandResp = GameNetManeger.newMessage(msgType, clientType, MID_HANDSHAKE);
				shakeHandResp.writeUTF8String("This is FE!");
				ctx.writeAndFlush(shakeHandResp);
				//默认推荐GS
				break;
			//step2-版本检查	
			case MID_CHECKVERSION:
				
				break;
			//step-3账号检查
			case MID_ACCOUNT_VERIFY:
				String verifyName = kmsg.readUTF8String();
				String verifyPwd = kmsg.readUTF8String();
				if(session == null){
					GameNetMessage newMessage = GameNetManeger.newMessage(msgType, clientType, MID_ACCOUNT_VERIFY);
					newMessage.writeUTF8String(verifyName);
					
				}
				
				
				
				break;
		default:
			break;
		}
	}
	
	
}
