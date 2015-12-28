package core.game.pool;


/**
 *  此接口可以记录Pooled object在什么时候和怎样在对象池内生效.
 *  Pooled object 可能需要，但不是必须，使用这些信息去描述它的状态--例如此对象是否
 *  已经弃用
 * 
 *  @version $Revision:$
 *
 *  @since 2.0
 */
public interface TrackedUse {

	/**
	 * 获取此对象的上次使用时间(ms)
	 * @return
	 */
	long getLastUsed();
}
