package core.game.spider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HttpTools {
	private final static Logger log = LoggerFactory.getLogger(HttpTools.class);

	public final static String FILEDIR = "D:\\log";
	
	private final static String USER_AGENT = "Mozilla/5.0 Firefox/26.0";
	
	/**
	 * 文件下载   返回文件名
	 * @param url
	 * @return
	 */
	public static String downLoadFile(String url){
		if(url.contains("{") && url.contains("}")){
			log.debug("----不合法url[{}]",url);
			return "";
		}
		String fileName = "";
		HttpClient client = createSSLClient(null);
		
		HttpGet post = new HttpGet(url);
		
		RequestConfig config = RequestConfig.custom()
				.setConnectionRequestTimeout(5000) //设置5s超时
				.build();
		post.setConfig(config);
		
		try {
			
			
			HttpResponse response = client.execute(post);
			int statusCode = response.getStatusLine().getStatusCode();
			if(statusCode != HttpStatus.SC_OK){
				log.warn("Method fail:[{}],url:[{}]", response.getStatusLine(),url);
				return "";
			}
			
			fileName = getFileNameByUrl(url, response.getEntity().getContentType().getValue());
			saveToLocal(response.getEntity(), fileName);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileName;
	}

	
	
	/**
	 * 根据url和网页类型生成需要保存的网页和文件名，去除url中的非文件名字符
	 * @param url
	 * @param contentType
	 * @return
	 */
	public static String getFileNameByUrl(String url, String contentType){
		if(url.equals("http://easyreadfs.nos.netease.com/zrskM2F_ZhSlr__7CIFT3w==/8796093022321361549")){
			log.debug("http://easyreadfs.nos.netease.com/zrskM2F_ZhSlr__7CIFT3w==/8796093022321361549");
		}
		//先判断是不是https
		if(url.startsWith("https")){
			url = url.substring(8);
		}else{
			
			//移除http:
			url = url.substring(7);
		}
		//text/html类型
		if(contentType.indexOf("html") != -1){
			url = url.replaceAll("[\\?/:*|<>\"]", "_") + ".html";
			return url;
		}else{
			//其他类型      如application/pdf类型          ps: image/jpeg;charset=UTF-8
			String[] split = contentType.split(";");
			String target = split[0].substring(contentType.lastIndexOf("/") +1);
			return url.replaceAll("[\\?/:*|<>\"]", "_") + "." + target;
		}
	}
	
	/**
	 * 写文件到本地
	 * @param httpEntity
	 * @param fileName
	 */
	public static void saveToLocal(HttpEntity httpEntity, String fileName){
		
		try {
			
			File dir = new File(FILEDIR);
			if(!dir.isDirectory()){
				dir.mkdirs();
			}
			
			File file = new File(dir.getAbsolutePath() + "/" + fileName);
			FileOutputStream out = new FileOutputStream(file);
			InputStream in = httpEntity.getContent();
			
			
			if(!file.exists()){
				file.createNewFile();
			}
			
			byte[] data = new byte[1024];
			int legth = 0;
			while ((legth = in.read(data)) > 0) {
				out.write(data,0,legth);
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	public static void saveToLocalByBytes(byte[] data, String fileName){
		try {
			
			File dir = new File(FILEDIR);
			if(!dir.isDirectory()){
				dir.mkdirs();
			}
			File file = new File(dir.getAbsolutePath() + "/" + fileName);
			FileOutputStream out = new FileOutputStream(file);
			out.write(data);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 创建http 客户端
	 * @param cookie  可以为null  
	 * @return
	 */
	public static CloseableHttpClient createSSLClient(CookieStore cookie){
		try {
			
			SSLContext sslcontent = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
				
				@Override
				public boolean isTrusted(X509Certificate[] arg0, String arg1)
						throws CertificateException {
					return true;
				}
			}).build();
			
			
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontent);
			
			//设置请求重试处理,重试机制,这里如果请求失败会重试5次
			HttpRequestRetryHandler httpRetryHandler = new HttpRequestRetryHandler() {
				
				@Override
				public boolean retryRequest(IOException exception, int executionCount,
						HttpContext context) {
					if(executionCount >= 5){
						//如果超过5次就不要再尝试
						return false;
					}
					
					if(exception instanceof InterruptedIOException){
						//timeout
						return false;
					}
					
					if(exception instanceof UnknownHostException){
						//unknown host
						return false;
					}
					
					if(exception instanceof ConnectTimeoutException){
						return false;
					}
					
					if(exception instanceof SSLException){
						//SSL hand shake exception
						return false;
					}
					
					HttpClientContext clientContent = HttpClientContext.adapt(context);
					HttpRequest request = clientContent.getRequest();
					
					if(!(request instanceof HttpEntityEnclosingRequest)){
						// Retry if the request is considered idempotent
						return true;
					}
					
					
					return false;
				}
			};
			
			//请求参数设置,设置请求超时时间为20秒,连接超时为10秒,不允许循环重定向
			RequestConfig config = RequestConfig.custom()
					.setConnectTimeout(20000)
					.setConnectionRequestTimeout(20000)
					.setCircularRedirectsAllowed(true)//允许重定向
					.build();
			
			CloseableHttpClient client = HttpClients.custom()
					.setSSLSocketFactory(sslsf)
					.setUserAgent(USER_AGENT) //有的网站会先判别用户的请求是否是来自浏览器，如不是，则返回不正确的文本  所以在这里要记得设置一下
					.setMaxConnPerRoute(256)
					.setRetryHandler(httpRetryHandler)
					.setDefaultRequestConfig(config)
					.setDefaultCookieStore(cookie)
					.build();
			
			
			return client;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return HttpClients.createDefault();
	}
	
	public static Set<String> getImageLink(String url, LinkFilter filter){
		Set<String> links = new HashSet<String>();
		try {
			
			Parser parse = new Parser(url);
			parse.setEncoding("UTF-8");
			
			NodeFilter nfilter = new NodeFilter() {
				
				private static final long serialVersionUID = 1L;

				@Override
				public boolean accept(Node arg0) {
					if(arg0.getText().contains("img")){
						return true;
					}
					
					return false;
				}
			};
			
			
			OrFilter of = new OrFilter(new NodeClassFilter(ImageTag.class), nfilter);
			NodeList list = parse.extractAllNodesThatMatch(of);

			for (int i = 0; i < list.size(); i++) {
				Node tag = list.elementAt(i);
				if(tag instanceof ImageTag){
					String linkUrl = ((ImageTag) tag).getImageURL();
					//判断一下是不是正常的链接
					if(!linkUrl.contains("http")){
						continue;
					}
					links.add(linkUrl);
				}
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return links;
	}
	
	public static Set<String> extractLink(String url, LinkFilter filter){
		Set<String> links = new HashSet<String>();
		
		
		try {
			Parser parse = new Parser(url);
			
			//过了<frame> 标签   获取frame标签里的链接
			NodeFilter nfilter = new NodeFilter() {
				
				private static final long serialVersionUID = 1L;

				@Override
				public boolean accept(Node arg0) {
					if(arg0.getText() .contains("<iframe") || arg0.getText().contains("<frame")){
						return true;
					}
					return false;
				}
			};
			
			//这个用于过滤<a>和<frame>标签里的链接
			OrFilter of = new OrFilter(new NodeClassFilter(LinkTag.class), nfilter);
			
			//获取到所有过滤后的节点
			NodeList nodeList = parse.extractAllNodesThatMatch(of);
			
			for (int i = 0; i < nodeList.size(); i++) {
				Node tag = nodeList.elementAt(i);
				if(tag instanceof LinkTag){
					String linkUrl = ((LinkTag) tag).getLink();
					if(filter.accept(linkUrl)){
						if(!linkUrl.contains("http")){
							continue;
						}
						links.add(linkUrl);
					}
				}else{
					//frame 标签
					//frame里src属性的链接,如<frame src="test.html" />
					String frame = tag.getText();
					int index = frame.indexOf("src=");
					frame = frame.substring(index);
					index = frame.indexOf(" ");
					//这里的结束判断貌似不是很准确
					if(index == -1){
						index = frame.indexOf(">");
					}
					String frameUrl = frame.substring(5,index - 1);
					if(filter.accept(frameUrl)){
						links.add(frameUrl);
					}
				}
				
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return links;
	}
	
	
}
