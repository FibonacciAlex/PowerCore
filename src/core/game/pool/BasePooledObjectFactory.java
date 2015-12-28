package core.game.pool;

/**
 * 池对象工厂{@link PooledObjectFactory}的一个基础实现，
 * <p>
 * 这里定义的所有操作基本上没有实质动作的。
 * </p>
 * 这个类是不可改变的，因此线程安全
 */
public abstract class BasePooledObjectFactory<T> implements PooledObjectFactory<T>{
	
	/**
	 * 创建一个对象实例，要求使用{@link PooledObject}包装实例
	 * <p>此方法<strong>一定要</strong>支持并发，多线程操作激活</p>
	 * @return
	 * @throws Exception
	 */
	public abstract T create() throws Exception;
	
	/**
	 * 使用{@link PooledObject} 包装实例
	 * @param obj  被包装的实例
	 * @return
	 */
	public abstract PooledObject<T> wrap(T obj);

	@Override
	public PooledObject<T> makeObject() throws Exception {
		return wrap(create());
	}

	/**
	 * 无操作
	 */
	@Override
	public void destroyObject(PooledObject<T> p) throws Exception {
	}

	/**
	 * 此实现总是返回true
	 */
	@Override
	public boolean validateObject(PooledObject<T> p) {
		return true;
	}

	/**
	 * 无操作
	 */
	@Override
	public void activateObject(PooledObject<T> p) throws Exception {
	}

	/**
	 * 无操作
	 */
	@Override
	public void passivateObject(PooledObject<T> p) throws Exception {
	}
	
}
