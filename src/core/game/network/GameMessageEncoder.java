package core.game.network;

import java.util.List;

import core.game.Game;
import core.game.GameNetMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

public class GameMessageEncoder extends MessageToMessageEncoder<Object>{

	@Override
	protected void encode(ChannelHandlerContext ctx, Object msg,
			List<Object> out) throws Exception {
		if(!(msg instanceof GameNetMessage)){
			return;
		}
		
		GameNetMessageImpl kmsg = (GameNetMessageImpl) msg;
		kmsg.closeWriteOp();//设置不可再写
		int writeIndex = kmsg.buffer.writerIndex();
		int payloadLength = writeIndex - GameNetMessage.LENGTH_OF_HEADER;
		


		//TODO 加密部分暂时不理
//		byte encode = kmsg.getEncrytion();
		
		
		//发送前先更新payload length
		kmsg.buffer.setInt(GameNetMessage.INDEX_PAYLOADLENGTH, payloadLength);
		
		
		int checksum = Game.calculateChecksum(kmsg.buffer, GameNetMessage.LENGTH_OF_HEADER, kmsg.buffer.readableBytes());
		kmsg.buffer.setInt(GameNetMessage.INDEX_START_CHECKSUM, checksum);
	
		out.add(kmsg);
	}


	
	
}
