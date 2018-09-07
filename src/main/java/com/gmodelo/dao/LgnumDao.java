package com.gmodelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.beans.AbstractResultsBean;
import com.gmodelo.beans.Lgnum;
import com.gmodelo.beans.Response;
import com.gmodelo.utils.ConnectionManager;
import com.gmodelo.utils.ReturnValues;

public class LgnumDao {
	
private Logger log = Logger.getLogger( LgnumDao.class.getName());
	
	public Response<List<Lgnum>> getLgnumByLgort(Lgnum lgnum){
		
		Response<List<Lgnum>> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		List<Lgnum> listLgnum = new ArrayList<Lgnum>();
		
		String INV_VW_NGORT_WITH_GORT = "SELECT [WERKS], [LGORT], [LGNUM], [LNUMT] FROM [INV_CIC_DB].[dbo].[INV_VW_NGORT_WITH_GORT] "; //The Store procedure to call
		
		String condition = buildCondition(lgnum);
		
		if(condition != null){
			INV_VW_NGORT_WITH_GORT += condition;
			log.info(INV_VW_NGORT_WITH_GORT);
		}
		
		log.info("[getLgnumByLgort] Preparing sentence...");
		
		try {
			stm = con.prepareCall(INV_VW_NGORT_WITH_GORT);
			
			log.info("[getLgnumByLgort] Executing query...");
			
			ResultSet rs = stm.executeQuery();
			
			while (rs.next()){
				
				lgnum = new Lgnum();				
				lgnum.setLgort(rs.getString(2));
				lgnum.setLgnum(rs.getString(3));
				lgnum.setLnumt(rs.getString(4));
				
				listLgnum.add(lgnum);
				
			}
			
			//Retrive the warnings if there're
			SQLWarning warning = stm.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING,warning.getMessage());
				warning = warning.getNextWarning();
			}
			
			//Free resources
			rs.close();
			stm.close();	
			
			log.info("[getLgnumByLgort] Sentence successfully executed.");
			
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[getLgnumByLgort] Some error occurred while was trying to execute the query: "+INV_VW_NGORT_WITH_GORT, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[getLgnumByLgort] Some error occurred while was trying to close the connection.", e);
			}
		}
		res.setAbstractResult(abstractResult);
		res.setLsObject(listLgnum);
		return res ;
	}
	
	private String buildCondition(Lgnum lgnumO){
			
			String lgort = "";
			String lgnum = "";
			String lnumt = "";
			String condition = "";
			
			lgnum = (lgnumO.getLgnum() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ") + " LGNUM = '" + lgnumO.getLgnum() +"' " : "";
			condition += lgnum;
			lgort = (lgnumO.getLgort() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ") + " LGORT = '" + lgnumO.getLgort() +"' " : "";
			condition += lgort;		
			lnumt = (lgnumO.getLnumt() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ") + " LGNUMT = '" + lgnumO.getLnumt() +"' " : "";
			condition += lnumt;
			condition = (condition.isEmpty() ? null : condition);
			return condition;
		}

}
