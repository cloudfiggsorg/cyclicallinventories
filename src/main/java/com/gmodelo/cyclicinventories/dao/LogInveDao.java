package com.gmodelo.cyclicinventories.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.cyclicinventories.beans.AbstractResultsBean;
import com.gmodelo.cyclicinventories.beans.LogInve;
import com.gmodelo.cyclicinventories.beans.MessagesTypes;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.utils.ConnectionManager;
import com.gmodelo.cyclicinventories.utils.ReturnValues;

public class LogInveDao {
	
	private Logger log = Logger.getLogger(LogInveDao.class.getName());
	
	public void logInve(MessagesTypes messageType, String title, String subtitle, String description, String userId){
		
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		CallableStatement cs = null;
				
		final String SP = "INV_SP_SAVE_LOG ?, ?, ?, ?, ?"; //The Store procedure to call
		
		try {
			cs = con.prepareCall(SP);
			
			cs.setString(1, messageType.name());		
			cs.setString(2, title);
			
			if(subtitle == null){
				cs.setNull(3, Types.VARCHAR);
			}else{
				cs.setString(3, subtitle);
			}
			
			cs.setString(4, description);
			
			if(userId == null){
				cs.setNull(5, Types.VARCHAR);
			}else{
				cs.setString(5, userId);
			}
				
			cs.execute();
			
			//Retrive the warnings if there're
			SQLWarning warning = cs.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING,"[logInve] "+ warning.getMessage());
				warning = warning.getNextWarning();
			}
			
			//Free resources
			cs.close();	
			
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[logInve] Some error occurred while was trying to execute the S.P.: " + SP, e);
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[logInve] Some error occurred while was trying to close the connection.", e);
			}
		}	
	}
		
	public Response<List<LogInve>> getLogByUser(String userId) {
		
		Response<List<LogInve>> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		Statement stm = null;		
		List<LogInve> lsLog = new ArrayList<>();
		LogInve li = new LogInve();
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		
		String QUERY = "SELECT INV_LG_TYPE, INV_LG_TITLE, " 
				+ "INV_LG_SUB_TITLE, INV_LG_DESCRIPTION, " 
				+ "INV_LG_USER_ID, INV_LD_DATE "
				+ "FROM INV_LOG "
				+ "WHERE (INV_LG_USER_ID IS NULL OR INV_LG_USER_ID = ?) AND "
				+ "INV_LD_DATE >= DATEADD(HOUR, -2, GETDATE()) "
				+ "ORDER BY INV_LD_DATE ASC";
		
		log.info(QUERY);
		log.info("[getLogByUser] Preparing sentence...");
		
		try {
			stm = con.createStatement();
			log.info("[getLogByUser] Executing query...");
			ResultSet rs = stm.executeQuery(QUERY);
			
			while (rs.next()) {
				
				li = new LogInve();
				li.setType(rs.getString("INV_LG_TYPE"));
				li.setTitle(rs.getString("INV_LG_TITLE"));
				li.setSubtile(rs.getString("INV_LG_SUB_TITLE"));
				li.setDescription(rs.getString("INV_LG_DESCRIPTION"));
				li.setDate(format.format(rs.getDate("INV_LD_DATE")));
				lsLog.add(li);
			}
			log.info("[getLogByUser] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE, "[getLogByUser] Some error occurred while was trying to execute the query: "
					+ QUERY, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,
						"[getLogByUser] Some error occurred while was trying to close the connection.", e);
			}
		}
		res.setAbstractResult(abstractResult);
		res.setLsObject(lsLog);
		return res;
	}

}
