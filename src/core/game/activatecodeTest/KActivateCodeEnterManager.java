package core.game.activatecodeTest;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import core.game.activatecodeTest.db.DBConnectionPoolAdapter;
import core.game.activatecodeTest.db.KGameActivateCodeService;

/**
 * 激活码服务器测试程序
 * @author Alex
 * 2015年7月1日 下午5:24:00
 */
public class KActivateCodeEnterManager {

	private final static Logger log = LoggerFactory.getLogger(KActivateCodeEnterManager.class);
	
	/**启动时从数据库加载的激活码集合*/
	private List<String> tempCodeList = new ArrayList<String>();
	
	private KGameActivateCodeService codeService = new KGameActivateCodeService();
	
	private final static String MD5_KEY = "d58ca6c8g1h29to23a0rt7m1c5f6kq7p";
	
	public static String requestUrl = "http://localhost:8080/GameActivateCodeServer/getCodeRewards.action?code={}&serverID={}&playerID={}&roleID={}&roleName=Alex&sign={}";
	
	public static String comfirmUrl = "http://localhost:8080/GameActivateCodeServer/confirmCodeUsed.action?code={}&serverID={}&playerID={}&roleID={}&roleName=Alex&sign={}";
	
	private final ExecutorService executorService;
	
	
	HttpClient httpClient;
	
	private KActivateCodeEnterManager(){
		executorService = Executors.newScheduledThreadPool(4);
		httpClient = new HttpClient();
	}
	
	private static KActivateCodeEnterManager instance = null;
	
	public static KActivateCodeEnterManager getInstance(){
		if(instance == null){
			instance = new KActivateCodeEnterManager();
		}
		return instance;
	}
	
	
	public void startUp(){
		try {
			DBConnectionPoolAdapter.init();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 初始化测试用目标激活码集合
	 */
	public void initCodeList(){
		this.tempCodeList = codeService.getUnusedCode();
		if(!tempCodeList.isEmpty()){
			log.info("激活码收集成功！");
		}
	}
	
	
	/**
	 * 返回确定
	 * @param data
	 */
	public void submitResponseTask(KRewardDataTemplate data){
		CodeUsedTask task = new CodeUsedTask(data.getCode(), 1002, 101, 11003, comfirmUrl);
		this.executorService.submit(task);
	}
	
	/**
	 * 提交测试任务
	 * @param code
	 */
	public void submitRequestTask(String code){
		CodeUsedTask task = new CodeUsedTask(code, 1002, 101, 11003, requestUrl);
		this.executorService.submit(task);
	}
	
	public void starUseCode() throws InterruptedException{
		for (int i = 0; i < 50; i++) {
			submitRequestTask(getRandoomCode());
		}
		
	}
	
	
	private String getRandoomCode(){
		int index = (int) Math.round(Math.random() * 9);
		return tempCodeList.get(index);
	}
	
	public static String signFormatMD5(int serverID, long playerID, long roleID){
		StringBuilder sb = new StringBuilder();
		sb.append(serverID).append(playerID).append(roleID).append(MD5_KEY);
		MessageDigest md = null;
		try {
			
			md = MessageDigest.getInstance("MD5");
			
			md.reset();
			
			md.update(sb.toString().getBytes("UTF-8"));
			
		} catch (Exception e) {
			log.error("进入参数校验时出现异常！ServerID:" + serverID+ ", playerID:" + playerID + ", roleID:" + roleID, e);
		}
		
		byte[] bs = md.digest();
		
		StringBuffer buffer = new StringBuffer();
		
		for (int i = 0; i < bs.length; i++) {
			if (Integer.toHexString(0xFF & bs[i]).length() == 1)
				buffer.append("0").append(Integer.toHexString(0xFF & bs[i]));
			else
				buffer.append(Integer.toHexString(0xFF & bs[i]));
		}
		return buffer.toString();
	}
	
	
	public static String format(String messagePattern, Object... arguments) {
		FormattingTuple ft = MessageFormatter.arrayFormat(messagePattern, arguments);
		return ft.getMessage();
	}
	
	public static void main(String[] args){
		
		try {
			
			log.info("System start!--------------");
			
			KActivateCodeEnterManager.getInstance().startUp();
			
		} catch (Exception e) {
			log.error("system start fail");
			System.exit(0);
		}
		
		KActivateCodeEnterManager.getInstance().initCodeList();
		
		try {
			KActivateCodeEnterManager.getInstance().starUseCode();
		} catch (InterruptedException e) {
			e.printStackTrace();
			log.error("error occur when using code");
		}
		
		
	}
	
	
	class CodeUsedTask implements Runnable{

		
		private String code;
		
		private int serverID;
		
		private long roleID;

		private long playerID;
		
		private String url;

		public CodeUsedTask(String code, int serverID, long roleID,
				long playerID, String requestUrl) {
			super();
			this.code = code;
			this.serverID = serverID;
			this.roleID = roleID;
			this.playerID = playerID;
			this.url = requestUrl;
		}





		@Override
		public void run() {
			//发送http 请求到激活码服务器进行获取数据
			String sign = KActivateCodeEnterManager.signFormatMD5(serverID, playerID, roleID);
			
			
			String formatUrl = KActivateCodeEnterManager.format(url, code,serverID,playerID,roleID,sign);
			
			try {
				log.info("发送激活码请求，url" + formatUrl);
//				KActivateCodeEnterManager.getInstance().httpClient.get(formatUrl);
			} catch (Exception e) {
				e.printStackTrace();
				log.error("Execut code occur error!");
			}
			
			
		}
		
	}
	
	
	
	
	
	
}
