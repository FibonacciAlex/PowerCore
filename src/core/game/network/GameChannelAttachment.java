package core.game.network;

import java.nio.channels.Channel;

import core.game.player.KPlayerSessoin;


/**
 * 为了将{@link Channel}和{@link KPlayerSession}绑定，在{@link Channel}对象上会set一个本对象。<br>
 * 注意：只有登录后的{@link Channel}可以获取到{@link GameChannelAttachment}
 *
 * @author Alex
 * 2015年7月6日 下午12:01:35
 */
public class GameChannelAttachment {
	
	private final long playerID;
	
	/**玩家退出原因*/
	private int disconnectReason;
	
	/**是否账号顶替登录*/
	private boolean isOverlap;
	
	private KPlayerSessoin playerSession;

	public GameChannelAttachment(long playerID) {
		super();
		this.playerID = playerID;
	}

	public int getDisconnectReason() {
		return disconnectReason;
	}

	public void setDisconnectReason(int disconnectReason) {
		this.disconnectReason = disconnectReason;
	}


	public void setOverlap(boolean isOverlap) {
		this.isOverlap = isOverlap;
	}

	public KPlayerSessoin getPlayerSession() {
		return playerSession;
	}

	public void setPlayerSession(KPlayerSessoin playerSession) {
		this.playerSession = playerSession;
	}

	public long getPlayreID(){
		return this.playerID;
	}
	
	
	public boolean isOverlap(){
		return isOverlap;
	}
}
