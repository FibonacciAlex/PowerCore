package core.game.pool;

import java.io.PrintWriter;
import java.util.Deque;


/**
 * 定义了一个池内对象(pooled object)的包装类,用于追踪那些附加的信息，
 * 比如说状态信息，创建时间，激活时间，关闭时间等。这些添加的信息方便pool来管理和实现一些特定的操作。
 * <p>
 * 这个类的实现必须是线程安全的
 * </p>
 * @param <T> 池内对象的类型
 *
 * @version $Revision: $
 *
 * @since 2.0
 */
public interface PooledObject<T> extends Comparable<PooledObject<T>>{
	
	/**
	 * 获取包装类包装的对象
	 * @return 被包装的对象
	 */
	T getObject();

	
	
	/**
	 * 获取对象被创建的时间
	 * @return  被包装对象的创建时间
	 */
	long getCreateTime();
	
	/**
	 * 获取此对象在上次激活状态时长(毫秒)，
	 * 它可能还处于激活状态，而再次调用此方法的时候就会获得一个增加值
	 * @return
	 */
	long getActiveTimeMillis();
	
	
	/**
	 * 获取此对象在上次空闲时长(毫秒)
	 * 对象可能还处于空闲状态，则再次调用此方法会获得一个增加值
	 * @return
	 */
	long getIdleTimeMillis();
	
	/**
	 * 获取包装对象的上次借出时间
	 * @return
	 */
	long getLastBorrowTime();
	
	/**
	 * 获取包装对象的上次返还时间
	 * @return
	 */
	long getLastReturnTime();
	
	/**
	 * 返回此对象上次使用时长的估计值
	 * 如果这个Pooled object 类型已经实现了{@link TrackedUse} 接口，
	 * 则会返回 {@link TrackedUse#getLastUsed()}和 {@link #getLastBorrowTime()}的最大值;
	 * 否则这个方法返回值与 {@link #getLastBorrowTime()} 的返回值相同
	 * @return
	 */
	long getLastUsedTime();
	
	@Override
	int compareTo(PooledObject<T> other);
	
	@Override
	boolean equals(Object obj);
	
	@Override
	int hashCode();
	
	
	/**
	 * 提供一个用于描述包装类的字符串，可以用于debug
	 * 这个形式并不固定 ，可以在任何时间改变
	 * @return
	 */
	@Override
	String toString();
	
	/**
	 * 尝试设置pooled object为{@link PooledObjectState#EVICTION} 状态
	 * 
	 * @return  如果状态设置成功则为true,否则false
	 */
	boolean startEvictionTest();
	
	/**
	 * 通知对象逐出测试已经结束 
	 * @param idleQueue 
	 * @return
	 */
	boolean endEvictionTest(Deque<PooledObject<T>> idleQueue);
	
	/**
	 * 分配对象 
	 * @return {@code true} if the original state was {@link PooledObjectState#IDLE IDLE}
	 */
	boolean allocate();
	
	/**
	 * 释放对象，如果它当前的状态为{@link PooledObjectState#ALLOCATED ALLOCATED}则
	 * 设置为 {@link PooledObjectState#IDLE IDLE}.
	 * @return
	 */
	boolean deallocate();
	
	/**
     * Sets the state to {@link PooledObjectState#INVALID INVALID}
     */
    void invalidate();
    
    /**
     * Is abandoned object tracking being used? If this is true the
     * implementation will need to record the stack trace of the last caller to
     * borrow this object.
     *
     * @param   logAbandoned    The new configuration setting for abandoned
     *                          object tracking
     */
    void setLogAbandoned(boolean logAbandoned);
    
    /**
     * Record the current stack trace as the last time the object was used.
     */
    void use();
    
    /**
     * Prints the stack trace of the code that borrowed this pooled object and
     * the stack trace of the last code to use this object (if available) to
     * the supplied writer.
     *
     * @param   writer  The destination for the debug output
     */
    void printStackTrace(PrintWriter writer);
    
    PooledObjectState getState();
    
    /**
     * Marks the pooled object as abandoned.
     */
    void markAbandoned();
    
    
    /**
     * Marks the object as returning to the pool.
     */
    void markReturning();
}



