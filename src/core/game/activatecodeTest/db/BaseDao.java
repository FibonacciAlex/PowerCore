package core.game.activatecodeTest.db;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.game.activatecodeTest.db.mysql.DefineDataSourceManagerIF;

public class BaseDao {
	
	private final static Logger log = LoggerFactory.getLogger(BaseDao.class);
	
	private DefineDataSourceManagerIF dbPool;
	
	public BaseDao(){
		try {
			
			dbPool = DBConnectionPoolAdapter.getDBConnectionPool();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Connection getConntion() throws Exception{
		try {
			
			return dbPool.getConnection();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void closedConnection(Connection conn, PreparedStatement ps) {
		if(conn == null || ps == null){
			return;
		}
		try {
			
			dbPool.closePreparedStatement(ps);
			dbPool.closeConnection(conn);
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("关闭数据库连接时出现异常！");
		}
		
	}
	
	public PreparedStatement getWriteStatement(Connection con, String sql){
		try {
			
			return dbPool.writeStatement(con, sql);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
