package core.game;

public interface KGameServerType {

	public static final int SERVER_TYPE_FE = 1;
	public static final int SERVER_TYPE_GS = 2;
	
	
	int getType();
	
	String getName();
}
