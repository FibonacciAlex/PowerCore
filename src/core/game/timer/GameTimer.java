package core.game.timer;

import io.netty.util.internal.PlatformDependent;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.game.util.DateUtil;

/**
 * 系统时效任务功能类
 * @author Alex
 * 2015年7月22日 上午10:21:49
 */
public final class GameTimer {
	
	private static final Logger log = LoggerFactory.getLogger(GameTimer.class);
	
	private int corePoolSize;
	private ExecutorService scheduleThreadPool;
	
	
	private final Set<GameTimerHandler> handlers = new HashSet<GameTimerHandler>();

	private final static InstanceWarnerDetector misuseDector = new InstanceWarnerDetector(256, GameTimer.class);
	
	
	private final TickerWorker worker = new TickerWorker();
	
	final Thread workerThread;
	
	final AtomicBoolean shutdown = new AtomicBoolean();
	
	final long tickDuration;
	
	private final long roundDuratioin;
	
	final Set<GameTimeSignal>[] wheel;// 将时间看作一个轮状，轮被分割成N个区间，就好像一个钟
	
	final ReusableIterator<GameTimeSignal>[] iterators;
	
	final int mask;
	
	final ReadWriteLock lock = new ReentrantReadWriteLock();
	
	volatile int wheelCursor;
	
	

	/**
	 * 通过配置文件进行初始化
	 * @param eTimer
	 */
	@SuppressWarnings("unchecked")
	public GameTimer(Element eTimer){
		corePoolSize = Integer.parseInt(eTimer.getAttributeValue("corePoolSize"));
		scheduleThreadPool = Executors.newFixedThreadPool(corePoolSize);
		tickDuration = Long.parseLong(eTimer.getAttributeValue("tickDuration"));
		
		int ticksPerWheel = Integer.parseInt(eTimer.getAttributeValue("ticksPerWheel"));
		
		roundDuratioin = tickDuration * ticksPerWheel;
		
		wheel = createWheel(ticksPerWheel);
		
		iterators = createIterators(wheel);
		
		mask = wheel.length - 1;
		 
		workerThread = Executors.defaultThreadFactory().newThread(worker);

		misuseDector.increase();
		
		List<Element> ehandlers = eTimer.getChildren("handler");
		if(ehandlers != null){
			
			
			for (Element ehandler : ehandlers) {
				String shandler = ehandler.getAttributeValue("clazz");
				if(shandler != null && shandler.length() > 0){
					GameTimerHandler handler = null;
					try {
						handler = (GameTimerHandler) Class.forName(shandler).newInstance();
						handlers.add(handler);
					} catch (Exception e) {
						throw new IllegalArgumentException("handler class", e);
					}
					if(handler != null){
						List<Element> taskConfig = ehandler.getChildren("TimerTask");
						handler.init(this, taskConfig);
					}
					
				}
			}
			log.info("timer handler size {}", handlers.size());
		}
	}

	@SuppressWarnings("unchecked")
	private ReusableIterator<GameTimeSignal>[] createIterators(
			Set<GameTimeSignal>[] wheel2) {
		ReusableIterator<GameTimeSignal>[] iterators = new ReusableIterator[wheel.length];
		for (int i = 0; i < wheel.length; i++) {
			iterators[i] = (ReusableIterator<GameTimeSignal>) wheel[i]
					.iterator();
		}
		return iterators;
	}

	@SuppressWarnings("unchecked")
	private Set<GameTimeSignal>[] createWheel(int ticksPerWheel) {
		if(ticksPerWheel <= 0){
			throw new IllegalArgumentException("ticks must be greater than 0, now :" + ticksPerWheel);
		}
		if(ticksPerWheel > 1073741824)
			throw new IllegalArgumentException("ticks may not be "
					+ "greater tham 2^30, now:" + ticksPerWheel);
		ticksPerWheel = normalizeTicksPerWheel(ticksPerWheel);
		Set<GameTimeSignal>[] wheel = new Set[ticksPerWheel];
		
		for (int i = 0; i < wheel.length; i++) {
			wheel[i] = new MapBackedSet<GameTimeSignal>(new 
					ConcurrentHashMap<GameTimeSignal, Boolean>());
		}
		
		return wheel;
	}

	//处理为2的平方数：2 4 8 16 32 64 128 256 512 1024 ....
	private int normalizeTicksPerWheel(int ticksPerWheel) {
		int index = 1;
		while (index < ticksPerWheel) {
			index <<= 1; 
		}
		return index;
	}
	
	
	
	
	
	/**
	 * Inner class {@link TickerWorker} 报时信号线程，就像一个时钟，每个固定时间间隔发送一次信号
	 * @author Alex
	 * 2015年7月31日 上午11:58:50
	 */
	private final class TickerWorker implements Runnable{
		
		private long startTime;
		
		private long tick;
		
		private Date tickDate;

		@Override
		public void run() {

			List<GameTimeSignal> timeSignal = new ArrayList<GameTimeSignal>();
			
			startTime = System.currentTimeMillis();
			
			tick = 0;//默认指针的起始位置
			
			tickDate = new Date(startTime);
			
			log.info("GameTimer ticker start @{}", DateUtil.FORMAT_DEFAULT.format(tickDate));
			
			while (!shutdown.get()) {
				final long dealLine = waitForNextTick();
				if(dealLine > 0){
					fetchTimeSignal(timeSignal,dealLine);
					notifyTimeSignals(timeSignal);
				}
			}
		}

		private void notifyTimeSignals(List<GameTimeSignal> timeSignal) {
			for(int i = timeSignal.size() - 1; i>= 0; i--){
				GameTimeSignal signal = null;
				try {
					//提交任务到线程池进行处理
					signal = timeSignal.get(i);
					scheduleThreadPool.execute(signal);
					
				} catch (NullPointerException e) {
					continue;
				} catch (RejectedExecutionException e) {
					if(signal != null){
						signal.getTask().rejectedExecute(e);
					}
				}
			}
			
			//clean up the temporary list
			timeSignal.clear();
		}

		private void fetchTimeSignal(List<GameTimeSignal> timeSignal,
				long dealLine) {
			lock.writeLock().lock();
			try {
				int newWheelCursor = wheelCursor = (wheelCursor + 1) & mask;
				ReusableIterator<GameTimeSignal> itr = iterators[newWheelCursor];
				fetchExpiredTimeSignals(timeSignal, itr, dealLine);
				
			} finally {
				lock.writeLock().unlock();
			}
		}

		private void fetchExpiredTimeSignals(List<GameTimeSignal> timeSignal,
				ReusableIterator<GameTimeSignal> itr, long dealLine) {
			List<GameTimeSignal> slipped = null;
			itr.rewind();
			while (itr.hasNext()) {
				GameTimeSignal signal = (GameTimeSignal) itr.next();
				if(signal.remainingRounds <= 0){
					itr.remove();
					if(signal.deadline <= dealLine){
						timeSignal.add(signal);
					}else{
						// Handle the case where the timesignal is put into a
						// wrong place, usually one tick earlier. For now, just add
						// it to a temporary list - we will reschedule it in a
						// separate loop.
						if(slipped == null){
							slipped = new ArrayList<GameTimeSignal>();
						}
						slipped.add(signal);
					}
				}else{
					signal.remainingRounds -- ;
				}
			}
			
			//reschedule the slipped timesignals
			if(slipped != null){
				slipped.forEach(i -> {
					scheduleTimeSignal(i,i.deadline - dealLine);
				});
			}
			
			
		}

		void scheduleTimeSignal(GameTimeSignal signal, long delay) {
			// delay must be equal to or greater than tickDuration so that the
			// worker thread never misses the TimeSignal.
			if(delay < tickDuration){
				delay = tickDuration;
			}
			
			//prepare the required parameters to schedule the time signal object
			long lastRoundDelay = delay % roundDuratioin;
			long lastTickDelay = delay % tickDuration;
			long relativeIndex = lastRoundDelay / tickDuration
					+ (lastTickDelay != 0 ? 1 : 0);
			long remainingRounds = delay / roundDuratioin
					- (delay % roundDuratioin == 0 ? 1 : 0);
			
			//add the signal to the wheel
			lock.readLock().lock();
			try {
				
				int stopIndex = (int) ((wheelCursor + relativeIndex) & mask);
				signal.stopIndex = stopIndex;
				signal.remainingRounds = remainingRounds;

				//logger.debug("lastRoundDelay={},remainingRounds={},relativeIndex={},stopIndex={},wheelCursor={}",lastRoundDelay,remainingRounds,relativeIndex,stopIndex,wheelCursor);
				
				wheel[stopIndex].add(signal);
			} finally {
				lock.readLock().unlock();
			}
			
			
		}

		private long waitForNextTick() {

			for(;;){
				long sleepTime = tickDuration * tick - 
						(System.currentTimeMillis() - startTime);
				//检查是否在windows 下运行， 如果在win OS  则要重新计算 sleepTime
				//详见 https://github.com/netty/netty/issues/356
				if(PlatformDependent.isWindows()){
					sleepTime = sleepTime /10 * 10;
				}
				
				if(sleepTime <= 0){
					break;
				}
				try {
					
					Thread.sleep(sleepTime);
				} catch (Exception e) {
					if(shutdown.get()){
						return -1;
					}
					log.warn(TickerWorker.class.getName() 
							+ "caught a InterruptedException");
				}
			}
			
			tick ++;
			long deadline = startTime + tickDuration * tick;
			return deadline;
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
