package core.game.server;

import java.nio.channels.Channel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import core.game.GameNetMessage;
import core.game.exception.KException;
import core.game.player.KPlayerSessoin;
import core.game.timer.GameTimeSignal;
import core.game.timer.GameTimerTask;

public final class GameServerHandler /*extends SimpleChannelHandler*/{

//	
//	private static final Logger log = LoggerFactory.getLogger(GameServerHandler.class);
//
//	public static final ConcurrentHashMap<Channel, KPlayerSessoin> handshakeSession = new ConcurrentHashMap<Channel, KPlayerSessoin>();
//
//	//延迟移除的队列
//	private final Map<Long, Integer> delayQueue = new ConcurrentHashMap<Long, Integer>();
//	
//	private final int delayMillisSecond;//延迟时间
//	
//	
//	
//	
//	GameServerHandler(int delaySecond) {
//		super();
//		this.delayMillisSecond = (int) TimeUnit.MILLISECONDS.convert(delaySecond, TimeUnit.SECONDS);
//		
//	}
//
//
//
//
//	@Override
//	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
//			throws Exception {
//		// TODO Auto-generated method stub
//		super.messageReceived(ctx, e);
//		
//		if(!(e.getMessage() instanceof GameNetMessage)){
//			log.warn("illegal messag!!" + e.getMessage());
//			return;
//		}
//		
//		
//	}
//	
	
	
	
	
	
	private class DelayRemoveTask implements GameTimerTask{

		@Override
		public String getTaskName() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object onTimeExecuter(GameTimeSignal signal) throws KException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void done(GameTimeSignal signal) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void rejectedExecute(RejectedExecutionException e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	

	
	
	
	
	
	
	
	
	
	
	
}
