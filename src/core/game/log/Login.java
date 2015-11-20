package core.game.log;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.util.ContextInitializer;

/**
 * 
 * @author Al&uumlex 
 * 2015年11月6日 下午4:14:29
 */
public class Login {

	static{
		System.setProperty(ContextInitializer.CONFIG_FILE_PROPERTY, "./res/log/" + ContextInitializer.AUTOCONFIG_FILE);
	}
	
	
	final static Logger log = LoggerFactory.getLogger(Login.class);
	
	public static void main(String[] args){
 		log.info("------------start logging!");
		Foo fo = new Foo();
		fo.doIt();
		log.info("----------end logging!");
	}
	
	
	
	static class Foo{
		
		final Logger FooLog = LoggerFactory.getLogger(Foo.class);
		
		public void doIt(){
			FooLog.info("---------------foo do it!");
		}
		
		
	}
}
