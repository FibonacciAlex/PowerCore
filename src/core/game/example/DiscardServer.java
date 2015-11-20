package core.game.example;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.util.CharsetUtil;

public class DiscardServer {

	static final boolean SSL = System.getProperty("ssl") != null;
	static final int port = 8080;

	
	
	public static void main(String[] args) throws Exception{
		 final SslContext sslCtx;
		 
	        if (SSL) {
	            SelfSignedCertificate ssc = new SelfSignedCertificate();
	            sslCtx = SslContext.newServerContext(ssc.certificate(), ssc.privateKey());
	        } else {
	            sslCtx = null;
	        }
			
			EventLoopGroup bossGroup = new NioEventLoopGroup();
			EventLoopGroup workGroup = new NioEventLoopGroup();
			
			try {
				
				ServerBootstrap b = new ServerBootstrap();
				b.group(bossGroup, workGroup);
				b.channel(NioServerSocketChannel.class)
				.handler(new LoggingHandler(LogLevel.INFO));
				b.childHandler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ChannelPipeline p = ch.pipeline();
						 if (sslCtx != null) {
	                         p.addLast(sslCtx.newHandler(ch.alloc()));
	                     }
						 p.addLast(new LineBasedFrameDecoder(1024));
						 p.addLast(new StringDecoder(CharsetUtil.UTF_8));
						 p.addLast(new StringEncoder(CharsetUtil.UTF_8));
						p.addLast(new DiscardServerHandler());
					}
				});
				b.option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);
				ChannelFuture future = b.bind(port).sync();
				future.channel().closeFuture().sync();
				
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				workGroup.shutdownGracefully();
				bossGroup.shutdownGracefully();
			}
		
	}
}
