package core.game.player;

import io.netty.channel.Channel;

public class PlayerSessoin {
	
	private Channel channel;
	
	private long sessionCreateMillin;

	private String currentDevice;

	private byte clientType;

	public PlayerSessoin(Channel channel) {
		this.channel = channel;
		this.sessionCreateMillin = System.currentTimeMillis();
	}

	public void setCurrentDevice(String client) {
		this.currentDevice = client;
	}

	public void setClientType(byte clientType) {
		this.clientType = clientType;
	}
	
	

}
