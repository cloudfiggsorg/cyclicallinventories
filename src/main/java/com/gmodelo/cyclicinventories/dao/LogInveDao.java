package com.gmodelo.cyclicinventories.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bmore.ume001.beans.User;
import com.gmodelo.cyclicinventories.beans.AbstractResultsBean;
import com.gmodelo.cyclicinventories.beans.LogInve;
import com.gmodelo.cyclicinventories.beans.MessagesTypes;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.filters.FServices;
import com.gmodelo.cyclicinventories.utils.ConnectionManager;
import com.gmodelo.cyclicinventories.utils.ReturnValues;

public class LogInveDao {
	
	private Logger log = Logger.getLogger(LogInveDao.class.getName());
	
	public void log(MessagesTypes messageType, String title, String subtitle, String description){
		
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		CallableStatement cs = null;
		
		String userId = ((User) FServices.getSession().getAttribute("user")).getEntity().getIdentyId();
		String bukrs = ((User) FServices.getSession().getAttribute("user")).getBukrs();
		bukrs = (bukrs == null ? "":bukrs);
		String werks = ((User) FServices.getSession().getAttribute("user")).getWerks();
		werks = (werks == null ? "":werks);
				
		final String SP = "INV_SP_SAVE_LOG ?, ?, ?, ?, ?, ?, ?"; //The Store procedure to call
		
		try {
			cs = con.prepareCall(SP);
			
			cs.setString(1, messageType.name());		
			cs.setString(2, title);			
			cs.setString(3, subtitle);			
			cs.setString(4, description);
			cs.setString(5, userId);							
			cs.setString(6, bukrs);
			cs.setString(7, werks);
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
		
	public Response<List<LogInve>> getLogByUser() {
		
		Response<List<LogInve>> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;		
		List<LogInve> lsLog = new ArrayList<>();
		LogInve li = new LogInve();
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String userId = ((User) FServices.getSession().getAttribute("user")).getEntity().getIdentyId();
		@SuppressWarnings("unchecked")
		boolean isAdmin = ((ArrayList<String>) FServices.getSession().getAttribute("roles")).contains("INV_CIC_ADMIN");				 
								
		String QUERY = "SELECT INV_LG_TYPE, INV_LG_TITLE, " 
				+ "INV_LG_SUB_TITLE, INV_LG_DESCRIPTION, " 
				+ "INV_LG_USER_ID, INV_LD_DATE "
				+ "FROM INV_LOG WITH (NOLOCK) "
				+ "WHERE INV_LD_DATE >= DATEADD(HOUR, -24, GETDATE()) ";
				if(!isAdmin){
					QUERY += "AND INV_LG_USER_ID = ? ";
				}
		QUERY += "ORDER BY INV_LD_DATE DESC";
		
		log.info(QUERY);
		log.info("[getLogByUser] Preparing sentence...");
		
		try {
			
			stm = con.prepareStatement(QUERY);
			if(!isAdmin){
				stm.setString(1, userId);
			}	
			
			log.info("[getLogByUser] Executing query...");			
			ResultSet rs = stm.executeQuery();			
			
			while (rs.next()) {
				
				li = new LogInve();
				li.setType(rs.getString("INV_LG_TYPE"));
				li.setTitle(rs.getString("INV_LG_TITLE"));
				li.setSubtitle(rs.getString("INV_LG_SUB_TITLE"));
				li.setDescription(rs.getString("INV_LG_DESCRIPTION"));
				li.setDate(format.format(rs.getTimestamp("INV_LD_DATE")));
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
