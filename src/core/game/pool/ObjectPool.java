package core.game.pool;

import java.rmi.server.ExportException;
import java.util.NoSuchElementException;

/**
 * <pre>
 * 一个对象池的简单接口,专门实现对象的存取和状态管理
 * 注意这里只定义了怎么获取和释放对象等操作，对象的生成交由{@link PooledObjectFactory}处理
 * A pooling simple interface.
 * </pre>
 * <p>
 * Example of use(使用例子):
 * <pre style="border:solid thin; padding: 1ex;"
 * > Object obj = <code style="color:#00C">null</code>;
 *
 * <code style="color:#00C">try</code> {
 *     obj = pool.borrowObject();
 *     <code style="color:#00C">try</code> {
 *         <code style="color:#0C0">//...use the object(使用对象)...</code>
 *     } <code style="color:#00C">catch</code>(Exception e) {
 *         <code style="color:#0C0">// invalidate the object(通知对象无效)</code>
 *         pool.invalidateObject(obj);
 *         <code style="color:#0C0">// do not return the object to the pool twice(不要再次将对象放回对象池内)</code>
 *         obj = <code style="color:#00C">null</code>;
 *     } <code style="color:#00C">finally</code> {
 *         <code style="color:#0C0">// make sure the object is returned to the pool(确认对象返还成功)</code>
 *         <code style="color:#00C">if</code>(<code style="color:#00C">null</code> != obj) {
 *             pool.returnObject(obj);
 *        }
 *     }
 * } <code style="color:#00C">catch</code>(Exception e) {
 *       <code style="color:#0C0">// failed to borrow an object(获取对象失败)</code>
 * }</pre>
 * <p>
 * See {@link BaseObjectPool} for a simple base implementation.
 * 一个简单的基础实现{@link BaseObjectPool}
 * @param <T> Type of element pooled in this pool.
 *
 * @see PooledObjectFactory
 * @see KeyedObjectPool
 * @see BaseObjectPool
 *
 * @version $Revision: 1566605 $
 *
 * @since 2.0
 */
public interface ObjectPool<T> {

	/**
	 * <pre>
	 *  从Pool获取一个对象,此操作将导致一个"对象"从Pool移除(脱离Pool管理),
	 *  调用者可以在获得"对象"引用后即可使用,且需要在使用结束后"归还"
	 *  </pre>
	 * @return
	 * @throws ExportException
	 * @throws NoSuchElementException
	 * @throws IllegalStateException
	 */
	T borrowObject()
	  throws Exception, NoSuchElementException, IllegalStateException;
	
	
	/**
	 * "归还"对象,当"对象"使用结束后,需要归还到Pool中,才能维持Pool中对象的数量可控
	 * @param paramObj
	 * @throws Exception
	 */
	void returnObject(T Obj)
	  throws Exception;
	
	/**
	 * 销毁对象
	 * @param paramObj
	 * @throws Exception
	 */
	void invalidateObject(T Obj)
	  throws Exception;
	
	/**
	 * 通过{@link PooledObjectFactory}或者其他实现机制创建一个"对象"，"钝化"对象并以idle的状态添加到Pool中
	 * <code>addObject</code>可以很方便的将一个池预加载入idle状态的对象
	 * @throws Exception
	 *                  当 {@link PooledObjectFactory#makeObject()} 执行失败
	 * @throws IllegalStateException
	 *                  当前池执行{@link #close()}之后
	 * @throws UnsupportedOperationException
	 *                  当前池无法添加idle状态的对象
	 */
	public void addObject()
	  throws Exception, IllegalStateException, UnsupportedOperationException;
	
	/**
	 * 返回当前Pool内空闲的实例
	 * 这个是Pool内可以借出对象数量的近似值
	 * 如果此信息不可用，则返回负值,见{@link BaseObjectPool}
	 * @return
	 * @throws UnsupportedOperationException
	 */
	public int getNumIlde()
	  throws UnsupportedOperationException;
	
	/**
	 * 返回从Pool内获取出来的对象数量，如果没有，则返回负值,见{@link BaseObjectPool}
	 * @return
	 * @throws UnsupportedOperationException
	 */
	public int getNumActive()
	  throws UnsupportedOperationException;
	
	/**
	 * 清除所有Pool内所有处于空闲状态的对象，释放所有占用的资源(可选操作)，
	 * 空闲的对象在清除时一定要执行 {@link PooledObjectFactory}
	 * @throws Exception
	 * @throws UnsupportedOperationException
	 */
	public void clear()
	  throws Exception, UnsupportedOperationException;
	
	/**
	 * 关闭此对象池，并且释放所有占用的资源
	 * @throws Exception
	 */
	public void close()
	  throws Exception;
	
}
