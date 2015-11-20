package core.game.DBAccess;

public class GameDBAccessFactory {
	
	private static GameDBAccessFactory instance = null;
	

	public static GameDBAccessFactory getInstance(){
		if(instance == null){
			instance = new GameDBAccessFactory();
		}
		return instance;
	}
	
	
}
