package core.game.pool.impl;

import java.lang.ref.ReferenceQueue;
import java.util.NoSuchElementException;
import java.util.concurrent.LinkedBlockingDeque;

import core.game.pool.BaseObjectPool;
import core.game.pool.PooledObjectFactory;

public class SoftReferenceObjectPool<T> extends BaseObjectPool<T>{

	/**
	 * 池对象生成工厂 ,池内对象来源
	 */
	private final PooledObjectFactory<T> factory;
	
	/**
	 * 一些可以从池内移除的无效引用队列，这队列可以帮助{@link #getNumIlde()}以最小的性能获取更精确的值
	 */
	private final ReferenceQueue<T> refQueue = new ReferenceQueue<T>();

	/**
	 * 从池内签出的实例数量
	 */
	private int numActive = 0;
	
	/**
	 * 被销毁的实例总数量
	 */
	private long destroyCount = 0;
	
	/**
	 * 被创建的实例总数量
	 */
	private long createCount = 0;
	
	
	
	public SoftReferenceObjectPool(PooledObjectFactory<T> factory) {
		this.factory = factory;
	}

	@Override
	public T borrowObject() throws Exception, NoSuchElementException,
			IllegalStateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void returnObject(T Obj) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void invalidateObject(T Obj) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
