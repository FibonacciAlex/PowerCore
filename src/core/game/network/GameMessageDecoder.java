package core.game.network;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.game.Game;
import core.game.GameNetMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class GameMessageDecoder extends ByteToMessageDecoder{

	private final static Logger log = LoggerFactory.getLogger(GameMessageDecoder.class);
	private final static int _200KB = 204800;
	
	private void clearBuffer(ByteBuf buffer){
		int readableBytes = buffer.readableBytes();
		if(readableBytes > 0){
			buffer.skipBytes(readableBytes);
		}
	}
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) throws Exception {
		if(in.readableBytes() < GameNetMessage.LENGTH_OF_HEADER){
			return;
		}
		
		GameNetMessageImpl msg;
		in.markReaderIndex();
		
		byte msgType = in.readByte(); //消息类型
		byte clientType = in.readByte();//客户端类型
		int msgID = in.readInt(); //消息id
		
		int payloadLength = in.readInt(); //长度
		int checksum = in.readInt(); //校验
		
		
		if(payloadLength > 0){
			if(payloadLength > _200KB){
				log.warn("服务器收到一条payloadLength超过200k的消息！payloadLength={}, buffer.readableBytes()={}, channel={}, msgId={}", payloadLength, in.readableBytes(), ctx.channel().hashCode(), msgID);
				this.clearBuffer(in);
				//关闭这个不正常的连接
				ctx.close();
				return;
			}
			
			if(in.readableBytes() < payloadLength){
				in.resetReaderIndex();
				return;
			}
			
			ByteBuf copy = in.copy(in.readerIndex(), payloadLength);
			int checkResult = Game.calculateChecksum(copy, 0, payloadLength);
			copy.clear();
			copy = null;
			if(checkResult != checksum){
				log.error("检验不通过！消息id：{}，客户端校验码：{}，服务器校验码：{}，readerIndex={}", msgID, checksum, checkResult, in.readerIndex());
				clearBuffer(in);
				ctx.close();
				return;
			}
		}else{
			if(checksum != 0){
				log.error("消息校验不通过！payloadLength为0，但是客户端校验码不为0：{}！", checksum);
				this.clearBuffer(in);
				ctx.close();
				return;
			}
		}
		
		
		//TODO 暂时不增加解密部分 直接显示
		
		msg  = new GameNetMessageImpl(msgType, clientType, msgID, payloadLength, checksum, in);
		
		out.add(msg);
	}

}
