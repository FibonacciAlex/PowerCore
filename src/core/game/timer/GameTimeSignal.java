package core.game.timer;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * 报时信号的封装
 * @author Alex
 * 2015年7月22日 下午4:15:29
 */
public class GameTimeSignal implements RunnableFuture<Object>{

	
	private GameTimer timer;
	
	private GameTimerTask task;
	
	private final Sync sync;
	
	final long deadline;
	volatile int stopIndex;
	volatile long remainingRounds;
	
	final long createTimeMillis;
	
	

	public GameTimeSignal(GameTimer timer, GameTimerTask task, long deadline) {
		super();
		if(task == null)
			throw new NullPointerException();
		createTimeMillis = System.currentTimeMillis();
		this.timer = timer;
		this.task = task;
		this.deadline = deadline;
		
		sync = new Sync(task);
	}


	void done(){
		
	}


	public GameTimer getTimer(){
		return timer;
	}
	
	public GameTimerTask getTask(){
		return task;
	}
	


	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		if(sync.innerCancel(mayInterruptIfRunning)){
			
		}
		
		return true;
	}


	@Override
	public boolean isCancelled() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean isDone() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public Object get() throws InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Object get(long timeout, TimeUnit unit) throws InterruptedException,
			ExecutionException, TimeoutException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	
	
	private final class Sync extends AbstractQueuedSynchronizer{

		private static final long serialVersionUID = 1L;

		private static final int RUNNING = 1;

		private static final int RAN = 2;
		
		private static final int CANCELLED = 4;
		
	
		private final GameTimerTask callable;
		
		private Object result;
		
		private Throwable exception;
		
		private volatile Thread runner;

		Sync(GameTimerTask cllable) {
			super();
			this.callable = cllable;
		}
		
		private boolean ranOrCancelled(int state){
			return (state & (RAN | CANCELLED)) != 0;
		}
		
		
		boolean innerIsDone(){
			return ranOrCancelled(getState()) && runner == null;
		}
		
		
		protected int tryAcquireShared(int ingonre){
			return innerIsDone() ? 1 : -1;
		}
		
		
		protected boolean tryReleaseShared(int ignore){
			runner = null;
			return true;
		}
		
		
		boolean innerIsCancelled(){
			return getState() == CANCELLED;
					
		}
		
		Object innerGet() throws InterruptedException, ExecutionException{
			acquireInterruptibly(0);
			if(getState() == CANCELLED){
				throw new CancellationException();
			}
			if(exception != null)
				throw new ExecutionException(exception);
			return result;
			
		}
		
		Object innerGet(long nanosTimeout) throws InterruptedException, ExecutionException, TimeoutException{
			if(!tryAcquireSharedNanos(0, nanosTimeout))
				throw new TimeoutException();
			if(getState() == CANCELLED)
				throw new CancellationException();
			if(exception != null)
				throw new ExecutionException(exception);
			return result;
		}
		
		void innerSet(Object o){
			for(;;){
				int s = getState();
				if(s == RAN)
					return;
				if(s == CANCELLED){
					releaseShared(0);
					return;
				}
				if(compareAndSetState(s, RAN)){
					result = o;
					releaseShared(0);
					done();
					return;
				}
			}
		}
		
		void innerSetException(Throwable t){
			for(;;){
				int s = getState();
				if(s == RAN)
					return;
				if(s == CANCELLED){
					releaseShared(0);
					return;
				}
				if(compareAndSetState(s, RAN)){
					exception = t;
					result = null;
					releaseShared(0);
					done();
					return;
				}
			}
		}
		
		boolean innerCancel(boolean mayInterruptIfRunning){
			for(;;){
				int s = getState();
				if(ranOrCancelled(s))
					return false;
				if(compareAndSetState(s, CANCELLED))
					break;
			}
			
			if(mayInterruptIfRunning){
				if(runner != null)
					runner.interrupt();
			}
			releaseShared(0);
			done();
			return true;
		}
		
		
		void innerRun(){
			if(!compareAndSetState(0, RUNNING))
				return;
			try {
				
				runner = Thread.currentThread();
				if(getState() == RUNNING)//rechekc after setting thread
					innerSet(callable.onTimeExecuter(GameTimeSignal.this));
				else
					releaseShared(0);//cancel
			} catch (Throwable e) {
				innerSetException(e);
			}
		}
		
		boolean innerRunAndReset(){
			if(!compareAndSetState(0, RUNNING))
				return false;
			try {
				
				runner = Thread.currentThread();
				
				if(getState() == RUNNING)
					callable.onTimeExecuter(GameTimeSignal.this);
				
				runner = null;
				
				return compareAndSetState(RUNNING, 0);
				
			} catch (Exception e) {
				innerSetException(e);
				return false;
			}
		}
		
		
		
	}
}
