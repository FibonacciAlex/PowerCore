package core.game.pool;

/**
 * 提供{@link PooledObject} 的各种可能状态 
 */
public enum PooledObjectState {
	
	/**
     * In the queue, not in use(在队伍中，还没有被使用).
     */
    IDLE,

    /**
     * In use(在使用状态).
     */
    ALLOCATED,

    /**
     * In the queue, currently being tested for possible eviction.
     * (在队列中，正测试是否可以逐出)
     */
    EVICTION,

    /**
     * Not in the queue, currently being tested for possible eviction. An
     * attempt to borrow the object was made while being tested which removed it
     * from the queue. It should be returned to the head of the queue once
     * eviction testing completes.
     * (不在队列中，正尝试逐出. 当尝试逐出的时候，准备从队列借出这个动作应该出现.一旦尝试结束，对象就应该出现在队列的顶部)
     * TODO: Consider allocating object and ignoring the result of the eviction
     *       test.
     */
    EVICTION_RETURN_TO_HEAD,

    /**
     * In the queue, currently being validated.
     * (队列中，目前正在验证)
     */
    VALIDATION,

    /**
     * Not in queue, currently being validated. The object was borrowed while
     * being validated and since testOnBorrow was configured, it was removed
     * from the queue and pre-allocated. It should be allocated once validation
     * completes.
     * (不在队列中，目前正在验证. 对象在验证的时候已经被借出并且借出测试已经配置完成，它应该已经从队列中移除并且准备分配
     * 验证完成同时也应该完成分配)
     */
    VALIDATION_PREALLOCATED,

    /**
     * Not in queue, currently being validated. An attempt to borrow the object
     * was made while previously being tested for eviction which removed it from
     * the queue. It should be returned to the head of the queue once validation
     * completes.
     * (不在队列中，目前正在验证。当尝试逐出的时候，准备从队列借出这个动作应该出现.当验证结束时，对象应该出现在队列的顶部)
     */
    VALIDATION_RETURN_TO_HEAD,

    /**
     * Failed maintenance (e.g. eviction test or validation) and will be / has
     * been destroyed
     * (验证或测试逐出失败，准备/已经销毁对象)
     */
    INVALID,

    /**
     * Deemed abandoned, to be invalidated.
     * (视为放弃，是无效的)
     */
    ABANDONED,

    /**
     * Returning to the pool.
     * (返还到池)
     */
    RETURNING

}
