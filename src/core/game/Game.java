package core.game;

import io.netty.buffer.ByteBuf;

import java.util.concurrent.TimeUnit;

import core.game.network.GameNetMessageImpl;
import core.game.timer.GameTimeSignal;
import core.game.timer.GameTimerTask;

/**
 * 系统底层API入口
 * @author Alex
 * 2015年7月30日 下午5:54:24
 */
public class Game {

	
	public static GameTimeSignal newTimeSignal(GameTimerTask task, long delay, TimeUnit unit){
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static int calculateChecksum(ByteBuf buffer, int offset, int bufferTotalLength){
		int checksum = 0;
		if (bufferTotalLength > offset) {
			checksum += ((buffer.getByte(offset) & 0xff) << 24);
		}
		if (bufferTotalLength > (offset+1)) {
			checksum += ((buffer.getByte(offset + 1) & 0xff) << 16);
		}
		if (bufferTotalLength == (offset + 3)) {
			checksum += ((buffer.getByte(bufferTotalLength - 1) & 0xff) << 8);
		} else if (bufferTotalLength > (offset + 3)) {
			checksum += ((buffer.getByte(bufferTotalLength - 2) & 0xff) << 8);
			checksum += ((buffer.getByte(bufferTotalLength - 1) & 0xff));
		}
		return checksum;
	}
}
