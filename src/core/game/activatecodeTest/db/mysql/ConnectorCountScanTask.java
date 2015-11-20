package core.game.activatecodeTest.db.mysql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TimerTask;


public class ConnectorCountScanTask extends TimerTask{
	private List<DefineDataSourceManagerIF> dataSourceList = new LinkedList<DefineDataSourceManagerIF>();

	/** 将一个连接池管理器注册到扫描任务中 */
	void registerMoniter(DefineDataSourceManagerIF dataSource) {
		dataSourceList.remove(dataSource);
		dataSourceList.add(dataSource);
	}

	@Override
	public void run() {
		if (dataSourceList.size() <= 0) { // 未注册数据源
			return;
		}
		DefineDataSourceManagerIF dataSource = null;
		for (int i = 0; i < dataSourceList.size(); i++) {
			dataSource = dataSourceList.get(i);
			try {
				scanDataSource(dataSource);
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
//			System.out.println("============== [" + dataSource.getSourceName()
//					+ "]未正常关闭连接数["
//					+ DBConnectionFactory.unNormalCloseConnectorCount.get()
//					+ "] ==============");
			System.out.println("============== [" + dataSource.getSourceName()
					+ "]未正常关闭连接数["
					+ DBConnectionFactory.unNormalCloseConnectorCount.get()
					+ "] ==============");
		}
	}

	/** 扫描数据源管理器 */
	private void scanDataSource(DefineDataSourceManagerIF dataSource)
			throws SQLException {
		Set<Connection> connSet = DefineDataSourceManagerIF.connectorMap
				.keySet();
		if (connSet == null || connSet.size() <= 0) {
			return;
		}
		Iterator<Connection> connIter = connSet.iterator();

		Connection conn = null;
		long time = 0;
		while (connIter.hasNext()) {
			conn = connIter.next();
			if (conn == null) { // 该连接已失效
				continue;
			}
			if (conn.isClosed()) { // 该连接已关闭
				DefineDataSourceManagerIF.connectorMap.remove(conn); // 从监听器中移除
				continue;
			}
			time = DefineDataSourceManagerIF.connectorMap.get(conn); // 获取连接创建的时间
			if (System.currentTimeMillis() - time >= DBConnectionFactory.SCAN_INTERVAL_TIME) { // 已经超过30分钟没有释放该连接
				if (!conn.getAutoCommit()) { // 正在进行事务操作，强制回滚
					conn.rollback();
				}
				dataSource.closeConnection(conn); // 强制关闭此连接
				// DefineDataSourceManagerIF.connectorMap.remove(conn); //
				// 从监听器中移除

				DefineDataSourceManagerIF.unNormalCloseConnectorCount
						.incrementAndGet();
			}
		}
	}
}