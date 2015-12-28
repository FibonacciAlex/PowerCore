package core.game.pool.impl;

import java.io.PrintWriter;
import java.util.Deque;

import core.game.pool.PooledObject;
import core.game.pool.PooledObjectState;
import core.game.pool.TrackedUse;

public class DefaultPooledObject<T> implements PooledObject<T>{

	private final T object;
	

	private PooledObjectState state = PooledObjectState.IDLE;
	
	private final long createTime = System.currentTimeMillis();
	
	private volatile long lastBorrowTime = createTime;
	
	private volatile long lastUseTime = createTime;
	
	private volatile long lastReturnTime = createTime;
	
	private volatile boolean logAbandoned = false;
	
	private volatile Exception borrowBy = null;
	
	private volatile Exception usedBy = null;
	
	private volatile long borrowedCount = 0;
	
	/**
	 * 创建一个新的实例来包装对象，让对象池可以追踪池内对象的状态
	 * @param object  被包装的对象
	 */
	public DefaultPooledObject(T object) {
		this.object = object;
	}

	@Override
	public T getObject() {
		return object;
	}

	@Override
	public long getCreateTime() {
		return createTime;
	}

	@Override
	public long getActiveTimeMillis() {
        long rTime = lastReturnTime;
        long bTime = lastBorrowTime;

        if (rTime > bTime) {
            return rTime - bTime;
        } else {
            return System.currentTimeMillis() - bTime;
        }
	}

	@Override
	public long getIdleTimeMillis() {
		final long elapsed = System.currentTimeMillis() - lastReturnTime;
	     // elapsed may be negative if:
	     // - another thread updates lastReturnTime during the calculation window
	     // - System.currentTimeMillis() is not monotonic (e.g. system time is set back)
	     return elapsed >= 0 ? elapsed : 0;
	}

	@Override
	public long getLastBorrowTime() {
		return lastBorrowTime;
	}

	@Override
	public long getLastReturnTime() {
		return lastReturnTime;
	}

	@Override
	public long getLastUsedTime() {
		if(object instanceof TrackedUse){
			return Math.max(((TrackedUse) object).getLastUsed(), lastUseTime);
		}else{
			return lastUseTime;
		}
	}

	@Override
	public int compareTo(PooledObject<T> other) {
		final long lastActiveDiff = this.getLastReturnTime() - other.getLastReturnTime();
		
		return 0;
	}

	@Override
	public boolean startEvictionTest() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean endEvictionTest(Deque<PooledObject<T>> idleQueue) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean allocate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deallocate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void invalidate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLogAbandoned(boolean logAbandoned) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void use() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void printStackTrace(PrintWriter writer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PooledObjectState getState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void markAbandoned() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void markReturning() {
		// TODO Auto-generated method stub
		
	}

}
