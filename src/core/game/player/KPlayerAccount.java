package core.game.player;

import java.util.concurrent.atomic.AtomicLong;


/**
 * 玩家货币账户，用于记录玩家余额等信息
 * @author Alex
 * 2015年7月6日 下午12:27:53
 */
public class KPlayerAccount {

	//是否可以透支
	private boolean canOverdraft;
	
	//可以透支额度
	private long canOverdraftValue;
	
	/**当前账号总额，如果可以透支， 则此值有可能为负数*/
	private final AtomicLong totalMoney;
	
	/**可以使用额度， 如果可能透支 ，则此值  = (totalMoney + canOverdraftValue - freezeMoney)}*/
	private final AtomicLong canUseMoney;
	
	/**被冻结的金额*/
	private final AtomicLong freezeMoney;
	
	/**历史总收入*/
	private final AtomicLong historyIncomeMoney;
	/**历史总支出*/
	private final AtomicLong historyOutcomeMoney;

	
	/**
	 * 构造函数
	 * @param canOverdraft 是否可以透支
	 * @param canOverdraftValue 可以透支的额度
	 * @param totalMoney  当前账户总额  
	 * @param freezeMoney  被冻结的金额
	 * @param historyIncomeMoney
	 * @param historyOutcomeMoney
	 */
	public KPlayerAccount(boolean canOverdraft, long canOverdraftValue,
			long totalMoney, long freezeMoney,
			long historyIncomeMoney, long historyOutcomeMoney) {
		super();
		this.canOverdraft = canOverdraft;
		this.canOverdraftValue = canOverdraftValue;
		this.totalMoney = new AtomicLong(totalMoney);
		this.freezeMoney = new AtomicLong(freezeMoney);
		this.historyIncomeMoney = new AtomicLong(historyIncomeMoney);
		this.historyOutcomeMoney = new AtomicLong(historyOutcomeMoney);
		if(canOverdraft){
			this.canUseMoney = new AtomicLong(totalMoney + canOverdraftValue - freezeMoney);
		}else{
			this.canUseMoney = new AtomicLong(totalMoney - freezeMoney);
		}
	}


	/**
	 * 构造函数
	 * @param canOverdraft 是否可以透支
	 */
	public KPlayerAccount(boolean canOverdraft) {
		this(canOverdraft, 0, 0, 0, 0, 0);
	}


	public boolean isCanOverdraft() {
		return canOverdraft;
	}


	public long getCanOverdraftValue() {
		return canOverdraftValue;
	}


	public AtomicLong getTotalMoney() {
		return totalMoney;
	}


	public AtomicLong getCanUseMoney() {
		return canUseMoney;
	}


	public AtomicLong getFreezeMoney() {
		return freezeMoney;
	}


	public AtomicLong getHistoryIncomeMoney() {
		return historyIncomeMoney;
	}


	public AtomicLong getHistoryOutcomeMoney() {
		return historyOutcomeMoney;
	}
	
	
}
