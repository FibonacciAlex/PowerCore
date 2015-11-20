package core.game.server;





import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;






import org.jdom.Document;
import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.game.KGameServerType;
import core.game.exception.KException;
import core.game.timer.GameTimer;
import core.game.tips.GameTips;
import core.game.util.XmlUtil;


/**
 * 游戏服务器启动器，配置参数的入口
 * @author Alex
 * @create 2015年6月18日 下午12:08:48
 */
public final class GameServer implements KGameServerType{

	private final static Logger log = LoggerFactory.getLogger(GameServer.class);
	
	//服务器是否正在停服标志
	private final AtomicBoolean shutdown = new AtomicBoolean(false);

	//服务器初次启动文件路径
	private final String GS_FIRST_START_FILE_PATH = "";
	
	private long _gsfirststarttime;
	
	private int gsid;
	
	private GameTimer timer;
	
	private static GameServer instance;
	
	public static GameServer getInstance(){
		return instance;
	}
	
	
	public static void main(String[] args) {
		log.info("-----System start!");
		instance = new GameServer();
		try {
			
		
			instance.startup();
			
		} catch (Exception e) {
			log.error("System start fail!");
			System.exit(1);
		}

	}



	void startup() throws KException{
		
		if(shutdown.get()){
			throw new IllegalStateException("cannot be started when stopping");
		}
		
		try {
			
			this.initFirstTime();
			
			//init system config file
			String networkConfigFilePath = "./res/config/gs/kgame_network.properties";//网络配置文件
			String systemConfig = "res/config/gs/KGameServer.xml";
			Document doc = XmlUtil.openXml(systemConfig);
			Element root = doc.getRootElement();
			
			
			
			Properties network = new Properties();
			try {
				network.load(new FileInputStream(networkConfigFilePath));
			} catch (Exception e) {
				
			}
			
			
			//init db
			
			
			//loading system tips
			GameTips.load();
			
			String sGSID = network.getProperty("GS_ID");
			gsid = Integer.parseInt(sGSID);
			
			
			
			//初始化通信模块		
//			ChannelFactory channelFactory = new NioServerSocketChannelFactory(
//					Executors.newCachedThreadPool(),
//					Executors.newCachedThreadPool());
//			
//			ServerBootstrap bootstrap = new ServerBootstrap(channelFactory);
//			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}




	/**
	 * 初始化系统启动文件
	 * @throws Exception
	 */
	private void initFirstTime() throws Exception{
		File file = new File(GS_FIRST_START_FILE_PATH);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(file.exists()){
			DataInputStream dis = new DataInputStream(new FileInputStream(file));
			byte[] array = new byte[1024];
			int length = dis.read(array);
			
			String dataStr = new String(Arrays.copyOfRange(array, 0, length),"utf-8");
			_gsfirststarttime = sdf.parse(dataStr).getTime();
			if(_gsfirststarttime > System.currentTimeMillis()){
				_gsfirststarttime = System.currentTimeMillis();
			}
			dis.close();
		}else{
			_gsfirststarttime = System.currentTimeMillis();
			Date date = new Date(_gsfirststarttime);
			RandomAccessFile rdaf = new RandomAccessFile(file, "rw");
			rdaf.write(sdf.format(date).getBytes("UTF-8"));
			rdaf.close();
			String osName = System.getProperty("os.name");
			if(osName != null  && osName.startsWith("windows")){
				//执行系统指令  隐藏文件属性
				Runtime.getRuntime().exec(String.format("attrib \"{}\" + H", file.getAbsoluteFile()));
			}
		}
		
	}


	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return 0;
	}




	public GameTimer getTimer(){
		return timer;
	}



	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "GameServer";
	}

}
