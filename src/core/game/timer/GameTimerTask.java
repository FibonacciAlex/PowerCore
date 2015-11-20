package core.game.timer;

import java.util.concurrent.RejectedExecutionException;

import core.game.exception.KException;

public interface GameTimerTask {

	/**
	 * 时效名，用于识别
	 * @return
	 */
	String getTaskName();
	
	
	/**
	 * 到达指定时间，收到到定时器的定时信号，开始执行指定业务逻辑
	 * @param signal 定时信号
	 * @return
	 * @throws KException
	 */
	Object onTimeExecuter(GameTimeSignal signal) throws KException;
	
	/**
	 * <pre>
	 * 处理完成逻辑后的通知，在线程池执行完{@link #onTimeExecuter(GameTimeSignal)} 后调用
	 * 此方法不一定要求有实际逻辑
	 * </pre>
	 * @param signal
	 */
	void done(GameTimeSignal signal);
	
	
	/**
	 * 拒绝执行异常，表示执行线程池出现严重问题
	 * @param e
	 */
	void rejectedExecute(RejectedExecutionException e);
}
