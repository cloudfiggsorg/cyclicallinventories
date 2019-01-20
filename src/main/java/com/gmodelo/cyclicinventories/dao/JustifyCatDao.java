package com.gmodelo.cyclicinventories.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.gmodelo.cyclicinventories.beans.AbstractResultsBean;
import com.gmodelo.cyclicinventories.beans.JustifyCat;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.utils.ConnectionManager;
import com.gmodelo.cyclicinventories.utils.ReturnValues;

public class JustifyCatDao {
	
	private Logger log = Logger.getLogger( GroupDao.class.getName());
	
	public Response<JustifyCat> addJustify(JustifyCat justify, String userId){
		
		Response<JustifyCat> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		CallableStatement cs = null;
		
		final String SP = "INV_SP_ADD_JS_CAT ?, ?, ?"; //The Store procedure to call
		
		log.info("[addGroup] Preparing sentence...");

		try {
			cs = con.prepareCall(SP);
			
			cs.setInt(1, justify.getJsId());			
			cs.setString(2, justify.getJustification());	
			cs.setString(3, userId);
			
			cs.registerOutParameter(1, Types.INTEGER);
			log.info("[addJustify] Executing query...");
			
			cs.execute();
			justify.setJsId(cs.getInt(1));
			abstractResult.setResultId(ReturnValues.ISUCCESS);
			
			//Retrive the warnings if there're
			SQLWarning warning = cs.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING,"[addJustify] "+ warning.getMessage());
				warning = warning.getNextWarning();
			}
			
			//Free resources
			cs.close();	
			
			log.info("[addJustify] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[addJustify] Some error occurred while was trying to execute the S.P.: " + SP, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[addJustify] Some error occurred while was trying to close the connection.", e);
			}
		}
	
		res.setAbstractResult(abstractResult);
		res.setLsObject(justify);
		return res ;
	}
	
	public Response<Object> deleteJustify(String jsIds){
		
		Response<Object> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		CallableStatement cs = null;
		
		final String SP = "INV_SP_DEL_JS_CAT ?"; //The Store procedure to call
		
		log.info("[deleteJustify] Preparing sentence...");
		
		try {
			
			cs = con.prepareCall(SP);			
			cs.setString(1, jsIds);
			
			log.log(Level.WARNING,"[deleteJustify] Executing query...");
			
			cs.execute();
			
			abstractResult.setResultId(ReturnValues.ISUCCESS);
			
			//Retrive the warnings if there're
			SQLWarning warning = cs.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING,"[deleteJustify] "+warning.getMessage());
				warning = warning.getNextWarning();
			}
			
			//Free resources
			cs.close();	
			
			log.info("[deleteJustify] Sentence successfully executed.");
			
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[deleteJustify] Some error occurred while was trying to execute the S.P.: "+ SP, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[deleteJustify] Some error occurred while was trying to close the connection.", e);
			}
		}
		res.setAbstractResult(abstractResult);
		return res ;
	}
	
	public Response<List<JustifyCat>> getJustifies() {
		
		Response<List<JustifyCat>> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		Statement stm = null;		
		List<JustifyCat> lsJustify = new ArrayList<>();
		JustifyCat justify = new JustifyCat();
		
		String QUERY = "SELECT JS_ID, JUSTIFICATION FROM INV_CAT_JUSTIFY ";		
		log.info(QUERY);
		log.info("[getJustifies] Preparing sentence...");
		
		try {
			stm = con.createStatement();
			log.info("[getZoneByLgortDao] Executing query...");
			ResultSet rs = stm.executeQuery(QUERY);
			
			while (rs.next()) {
				
				justify = new JustifyCat();
				justify.setJsId(rs.getInt("JS_ID"));
				justify.setJustification(rs.getString("JUSTIFICATION"));				
				lsJustify.add(justify);
			}
			log.info("[getJustifies] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE, "[getJustifies] Some error occurred while was trying to execute the query: "
					+ QUERY, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,
						"[getJustifies] Some error occurred while was trying to close the connection.", e);
			}
		}
		res.setAbstractResult(abstractResult);
		res.setLsObject(lsJustify);
		return res;
	}

}
