package core.game.frontend;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.net.InetSocketAddress;
import java.nio.channels.Channel;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jdom.Document;
import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.util.ContextInitializer;
import core.game.KGameServerType;
import core.game.exception.KException;
import core.game.network.GameMessageDecoder;
import core.game.network.GameMessageEncoder;
import core.game.network.GameNetHeartbeat;
import core.game.network.GameNetManeger;
import core.game.network.GameServerPipineFactory;
import core.game.timer.GameTimer;
import core.game.util.XmlUtil;

/**
 * 登录服务器
 * @author Alex
 * 2015年7月27日 上午10:17:16
 */
public class GameFrontend implements KGameServerType{
	
	private static final Logger log = LoggerFactory.getLogger(GameFrontend.class);

	private static GameFrontend instance;
	
	private final AtomicBoolean shutdown = new AtomicBoolean();
	
	private GameFrontendHandler handler;
	
	private GameTimer timer;
	
	private GameNetManeger netManeger;
	
	private GameGSManeger gsManeger;
	
	
	
	public static void main(String[] args){
		try {
			
			instance = new GameFrontend();
			instance.startup();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}


	public static GameFrontend getInstance(){
		return instance;
	}
	
	public boolean isShutdown(){
		return shutdown.get();
	}
	
	@Override
	public int getType() {
		return SERVER_TYPE_FE;
	}

	@Override
	public String getName() {
		return "GameFE";
	}
	
	
	/**
	 * 处理登录服启动的具体逻辑
	 * @throws Exception 
	 */
	void startup() throws Exception{
		log.info("startup ------>FE Server...");
		if(isShutdown()){
			throw new IllegalStateException("can not start when system is shuting down!");
		}
		
		//init DB
		
		
		//init system tips
		
		
		//init FE config
		String xmlPath = "./res/config/GameFrontend.xml";
		Document doc = XmlUtil.openXml(xmlPath);
		Element root = doc.getRootElement();
		
		//初始化网络管理及相关资源配置接口(netty)
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();
		
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(bossGroup, workGroup);
		
		bootstrap.channel(NioServerSocketChannel.class);
		handler = new GameFrontendHandler(this);
		netManeger = new GameNetManeger(handler, bootstrap, this);
		
		
		
		
		
		
		
		
		//启动时效
//		Element eTimer = root.getChild("Timer");
//		timer = new GameTimer(eTimer);
//		log.info("start up FE timer!");
		
		netManeger.init(root.getChild("Network"));
		
		//开始对外
		netManeger.start();
		log.info("startup>>>Communication Started.{}",netManeger);
		
		log.info("startup>>>OKOKOKOKOKOKOKOKOKOKOKOKOKOKOKOKOKOKOK");

	}
	
	
	public void shutdown(final Channel cmd) throws KException
	{
		if(!shutdown.compareAndSet(false, true)){
			reportshutdowninfo2client(cmd, "shutdown call");
			throw new IllegalStateException("shutdown repeated call");
		}
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				//执行停服操作
				
			}
		}).start();
	}
	
	public static void reportshutdowninfo2client(Channel channel, String info){
//		if(channel == null || (!channel.isConnected())){
//			return;
//		}
//		//发送关机协议
//		GameNetMessage msg = null;
//		
//		
//		channel.write(msg);
	}
	
	
	
}
