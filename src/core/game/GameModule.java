package core.game;

import org.jdom.Element;

import core.game.exception.KException;

public interface GameModule {

	void init(String moduleName, boolean isPlayerSessionListener, Element moduleDefiningConfig) throws KException;
	
	
	boolean isPlayerSessionListener();
	
	String getModuleName();
	
	
	void serverStartCompleted() throws KException;
	
	
	void exceptionCaught(KException ex);
	
	void serverShutDown() throws KException;
	
	
	boolean isInitFinished();
}
