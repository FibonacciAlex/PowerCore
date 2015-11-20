package core.game.activatecodeTest.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class KGameActivateCodeService {

	
	public List<String> getUnusedCode(){
		List<String> tempList = new ArrayList<String>();
		BaseDao dao = null;
		
		Connection con = null;
		PreparedStatement ps = null;
		try {
			long round = Math.round(Math.random() * 9);
			
			String sql = "select * from code_" + round;
			
			
			dao = new BaseDao();
			
			
			try {
				con = dao.getConntion();
				ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery();
				while(rs.next()){
					String name = rs.getString("role_name");
					if(name == null){
						String code = rs.getString("code");
						tempList.add(code);
					}
					if(tempList.size() == 10){
						break;
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				ps.clearParameters();
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			dao.closedConnection(con, ps);
		}
		
		return tempList;
	}
}
