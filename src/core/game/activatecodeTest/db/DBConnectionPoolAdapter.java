package core.game.activatecodeTest.db;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.game.activatecodeTest.db.mysql.DBConnectionFactory;
import core.game.activatecodeTest.db.mysql.DefineDataSourceManagerIF;



public class DBConnectionPoolAdapter {

	private static final Logger logger = LoggerFactory.getLogger(DBConnectionPoolAdapter.class);
	
	private static final String dbConPoolUrl = "./res/db.properties";

	private static DefineDataSourceManagerIF dBConPool;
	
	private static boolean isInitDbPool = false;
	
	public static void init() throws Exception{
		if(isInitDbPool){
			return;
		}
		
		logger.info("！！！数据库配置加载开始！！！");
		dBConPool = DBConnectionFactory.getInstance().newProxoolDataSourceInstance(dbConPoolUrl);
		isInitDbPool = true;
		logger.info("！！！数据库配置加载完成！！！");
	}
	
	public static DefineDataSourceManagerIF getDBConnectionPool(){
		return dBConPool;
	}
	
	
}

