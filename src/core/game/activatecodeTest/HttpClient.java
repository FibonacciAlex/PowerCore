package core.game.activatecodeTest;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;

import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.channels.Channel;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executors;



public class HttpClient {
	
	
//	private ServerBootstrap bootstrap = null;
//	
//	ChannelGroup allChannels = null;
//
//	public HttpClient() {
//		bootstrap = new 
//		bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(
//				Executors.newCachedThreadPool(),
//				Executors.newCachedThreadPool()));
//		bootstrap.setPipelineFactory(new HttpClientPipelineFactory());
//		bootstrap.setOption("child.tcpNoDelay", true);
//	
//		allChannels = new DefaultChannelGroup();
//		
//	}
//	
//	
//	public void setOption(String Key, Object value){
//		bootstrap.setOption(Key, value);
//	}
//	
//	public ChannelPipeline retrieve(String method, String url, Map<String, Object>data,Map<String, String> cookie) throws Exception{
//		if(url == null || url.equals("")){
//			throw new Exception("url is null or length is 0");
//		}
//		URI uri = new URI(url);
//		String scheme = uri.getScheme() == null ? "http":uri.getScheme();
//		
//		String host = uri.getHost() == null ? "localhost" : uri.getHost();
//		
//		if(!scheme.equals("http")){
//			throw new Exception("It just support http protocol");
//		}
//		
//		//设置http请求的各项默认参数  构建http请求
//		HttpRequest req = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.valueOf(method), uri.toASCIIString());
//		req.setHeader(HttpHeaders.Names.HOST, host);
//		req.setHeader(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
//		if(cookie != null){
//			CookieEncoder ce = new CookieEncoder(false);
//			for (Entry<String, String> m : cookie.entrySet()) {
//				ce.addCookie(m.getKey(), m.getValue());
//				req.setHeader(HttpHeaders.Names.COOKIE, ce.encode());
//			}
//		}
//		return retrieve(req);
//	}
//	
//	public ChannelPipeline retrieve(HttpRequest req) throws Exception{
//		
//		URI uri = new URI(req.getUri());
//		int port = uri.getPort() == -1 ? 80 : uri.getPort();
//		ChannelFuture future = bootstrap.connect(new InetSocketAddress(req.getHeader(HttpHeaders.Names.HOST), port));
//		future.addListener(new ConnectOK(req));
//		allChannels.add(future.getChannel());
//		
//		return future.getChannel().getPipeline();
//	}
//	
//	
//	public ChannelPipeline retrieve(String method,String url) throws Exception{
//		return retrieve(method, url, null, null);
//	}
//	
//	public ChannelPipeline retrieve(String method, String url, Map<String, Object> data) throws Exception{
//		return retrieve(method, url, data, null);
//	}
//	
//	
//	public ChannelPipeline get(String url) throws Exception{
//		return retrieve("GET", url);
//	}
//	
//	public ChannelPipeline delete(String url) throws Exception{
//		return retrieve("DELETE", url);
//	}
//	
//	
//	public ChannelPipeline post(String url, Map<String, Object>data) throws Exception{
//		return retrieve("POST", url, data);
//	}
//	
//	
//	public void close(){
//        allChannels.close().awaitUninterruptibly();
//        bootstrap.releaseExternalResources();
//    }
//	
//	class ConnectOK implements ChannelFutureListener{
//
//		private HttpRequest req;
//		
//		
//		public ConnectOK(HttpRequest req) {
//			super();
//			this.req = req;
//		}
//
//
//
//		@Override
//		public void operationComplete(ChannelFuture future) throws Exception {
//
//			if(!future.isSuccess()){
//				future.getCause().printStackTrace();
//				return;
//			}
//			
//			Channel channel = future.getChannel();
//			channel.write(req);
//		}
//		
//	}
	

}
