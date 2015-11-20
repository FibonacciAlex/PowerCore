package core.game.frontend;

import java.util.concurrent.RejectedExecutionException;

import core.game.exception.KException;
import core.game.timer.GameTimeSignal;
import core.game.timer.GameTimerTask;

public class GameGSManeger implements GameTimerTask{
	
	
	
	

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
