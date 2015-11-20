package core.game.timer;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 时效实例达到上限时的警告类   用于提示  避免内存溢出
 * @author Alex
 * 2015年7月30日 下午8:39:53
 */
public class InstanceWarnerDetector {

	private final int maxActiveInstanes;
	
	private final Class<?> type;
	
	private final AtomicLong activeInstances = new AtomicLong();
	
	private final AtomicBoolean logged = new AtomicBoolean();
	
	private static final Logger log = LoggerFactory.getLogger(InstanceWarnerDetector.class);

	public InstanceWarnerDetector(int maxActiveInstanes, Class<?> type) {
		if(type == null)
			throw new NullPointerException();
		this.maxActiveInstanes = maxActiveInstanes;
		this.type = type;
	}
	
	
	public void increase(){
		if(activeInstances.incrementAndGet() > maxActiveInstanes){
			if(log.isWarnEnabled()){
				if(logged.compareAndSet(false, true)){
					log.warn("System is creating too many"
							+ type.getSimpleName() + "instance."
							+ type.getSimpleName() +
							"is a shared resource that must be resued across the "
							+ "application, so that only a few instances");
				}
			}
		}
	}
	
	public void decrease(){
		activeInstances.decrementAndGet();
	}
	
	
}
