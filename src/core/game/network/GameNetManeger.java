package core.game.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;









import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;









import core.game.KGameServerType;
import core.game.exception.KException;

public final class GameNetManeger {
	
	private final static Logger log = LoggerFactory.getLogger(GameNetManeger.class);

	private final AtomicBoolean shutdown = new AtomicBoolean();
	
	//服务器绑定的通信端口通道
	private Channel serverChannel;
	
	//网络通信启动器
	private ServerBootstrap bootstrap;
	
	//通信handler
	private ChannelHandler handler;
	
	//服务器类型
	private KGameServerType serverType;
	
	//ip地址
	private String ip;

	private InetSocketAddress serverAddress;

	private int allowConnect, allowOnline;

	private boolean isInitFinish;

	public GameNetManeger(ChannelHandler chandler, ServerBootstrap bootstrap,
			KGameServerType serverType) {
		super();
		this.bootstrap = bootstrap;
		this.serverType = serverType;
		this.handler = chandler;
	}

	/**
	 * 初始化系统配置
	 * 
	 * @throws Exception
	 */
	public void init(Element eNetConfig) throws Exception {

		if (shutdown.get()) {
			throw new IllegalStateException(
					"can not be started while stopping!");
		}

		String lanIP = eNetConfig.getChildTextTrim("LanIP");
		String wanIP = eNetConfig.getChildTextTrim("wanIP");

		ip = wanIP == null ? lanIP : wanIP;

		int port = Integer.parseInt(eNetConfig.getChildTextTrim("SocketPort"));
		serverAddress = new InetSocketAddress(ip, port);

		allowConnect = Integer.parseInt(eNetConfig
				.getChildTextTrim("AllowedConnect"));

		allowOnline = Integer.parseInt(eNetConfig
				.getChildTextTrim("AllowedOnline"));

		// /////////////////////////////////////////////////////////////////
		Element eIdle = eNetConfig.getChild("Idle");
		int readerIdleTimeSeconds = Integer.parseInt(eIdle
				.getAttributeValue("readerIdleTimeSeconds"));
		int writerIdleTimeSeconds = Integer.parseInt(eIdle
				.getAttributeValue("writerIdleTimeSeconds"));
		int allIdleTimeSeconds = Integer.parseInt(eIdle
				.getAttributeValue("allIdleTimeSeconds"));
		IdleStateHandler idleStateHandler = new IdleStateHandler(readerIdleTimeSeconds, writerIdleTimeSeconds, allIdleTimeSeconds);
		
		
		bootstrap.childHandler(new GameServerPipineFactory(handler, idleStateHandler, this));

		//设置部分socket参数
		Element options  = eNetConfig.getChild("Options");
		@SuppressWarnings("unchecked")
		List<Element> list = options.getChildren("Option");
		list.forEach(i -> {
			String key = i.getAttributeValue("key");
			ChannelOption<Object> option = ChannelOption.newInstance(key);
			bootstrap.option(option, i.getAttributeValue("value"));
		});
		bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT); //其实默认已经是这个  可以不用显式指定 
		bootstrap.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);//关键是这句
		isInitFinish = true;
	}

	
	public KGameServerType getServerType(){
		return serverType;
	}
	
	public void start() throws KException{
		if(shutdown.get()){
			throw new IllegalStateException("can not start when system is shuting down!");
		}
		if(!isInitFinish){
			throw new IllegalStateException("It must inited before start up");
		}
		
		//绑定端口  记录服务器本身Channel
		try {
			
			serverChannel = bootstrap.bind(serverAddress).sync().channel();
			ChannelFuture future = serverChannel.closeFuture().sync();// 这里为什么还要弄一个closeFuture 没有这个操作为什么会无法启动
		} catch (Exception e) {
			throw new KException("socket bind failed!原因：" + e.getMessage());
		}
		
		//show log
		log.info("{} is netManager start ok! Allow connected：{}, allow online：{}", serverType.getName()
				,this.allowConnect, this.allowOnline);
	}
	
	
	/**
	 * 处理停服时要发送的各种通知
	 * @throws Exception
	 */
	public void stop() throws Exception{
		if(!shutdown.compareAndSet(false, true)){
			throw new IllegalStateException("The system is shutting down!");
		}
		
		log.warn("System start shut down. Channel close");
		//关闭服务器通道
		ChannelFuture scfuture = serverChannel.closeFuture().syncUninterruptibly();
		
		log.warn("serverChannel close success? {}", scfuture.isSuccess());
		
		if(serverType.getType() == KGameServerType.SERVER_TYPE_FE){
			//处理FE逻辑
			
			
		} else if(serverType.getType() == KGameServerType.SERVER_TYPE_GS){
			//处理GS停服逻辑
			
			
		}
		
	}
	
	public void releaseExternalRes(){
		bootstrap.group().shutdownGracefully();
		bootstrap.childGroup().shutdownGracefully();
		
	}
}
