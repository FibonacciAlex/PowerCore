package core.game.pool;

import java.util.NoSuchElementException;

/**
 * 一个{@link ObjectPool}对象池的简单基础实现 
 */
public abstract class BaseObjectPool<T> implements ObjectPool<T>{

	private volatile boolean closed = false;
	
	@Override
	public abstract T borrowObject() throws Exception, NoSuchElementException,
			IllegalStateException;

	@Override
	public abstract void returnObject(T Obj) throws Exception;

	@Override
	public abstract void invalidateObject(T Obj) throws Exception;

	/**
	 * 在此类不实现，交给子类再做具体实现
	 */
	@Override
	public void addObject() throws Exception, IllegalStateException,
			UnsupportedOperationException {
	}

	@Override
	public int getNumIlde() throws UnsupportedOperationException {
		return -1;
	}

	@Override
	public int getNumActive() throws UnsupportedOperationException {
		return -1;
	}

	
	/**
	 * 在这个基础实现类里不支持此动作，要求子类进行重写
	 */
	@Override
	public void clear() throws Exception, UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * 在<code>isClosed</code> 和
     * <code>assertOpen</code>操作之后生效.
	 */
	@Override
	public void close() throws Exception {
		closed = true;
		
	}
	
	/**
	 * 判断此对象池是否已经关
	 * @return
	 */
	public final boolean isClosed(){
		return closed;
	}


	
}
