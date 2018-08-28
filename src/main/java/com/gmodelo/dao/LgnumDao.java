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
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		PreparedStatement stm = null;
		List<Lgnum> listLgnum = new ArrayList<Lgnum>();
		
		String INV_VW_NGORT_WITH_GORT = "SELECT [WERKS], [LGORT], [LGNUM], [LNUMT] FROM [INV_CIC_DB].[dbo].[INV_VW_NGORT_WITH_GORT] "; //The Store procedure to call
		
		String condition = buildCondition(lgnum);
		
		if(condition != null){
			INV_VW_NGORT_WITH_GORT += condition;
			log.warning(INV_VW_NGORT_WITH_GORT);
		}
		
		log.log(Level.WARNING,"[getLgnumByLgort] Preparing sentence...");
		
		try {
			stm = con.prepareCall(INV_VW_NGORT_WITH_GORT);
			
			log.log(Level.WARNING,"[getLgnumByLgort] Executing query...");
			
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
			
			log.log(Level.WARNING,"[getLgnumByLgort] Sentence successfully executed.");
			
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
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs(e.getMessage());
				res.setAbstractResult(abstractResult);
				return res;
			}
		}
		res.setAbstractResult(abstractResult);
		res.setLsObject(listLgnum);
		return res ;
	}
	
	private String buildCondition(Lgnum lgnumO){
			
			String werks = null;
			String lgort = null;
			String lgnum = null;
			String lnumt = null;
			Boolean clause = false;
			String condition = null;
			
			if(lgnumO.getLgort()!= null){
				lgort = "LGORT = '" + lgnumO.getLgort()+"'";
				clause = true;
			}
			if(lgnumO.getLgnum() != null){
				lgnum = "LGNUM = '" + lgnumO.getLgnum()+"'";
				clause = true;
			}
			if(lgnumO.getLnumt() != null){
				lnumt = "LNUMT = '" + lgnumO.getLnumt()+"'";
				clause = true;
			}
			
			if(clause){
				condition = "WHERE ";
				if(werks != null){
					condition += werks;
					if(lgort != null){
						condition += " AND "+ lgort;
						if(lgnum != null){
							condition += " AND "+ lgnum;
						}
					}
				} else if(lgort != null){
							condition += lgort;
							if(lgnum != null){
								condition += " AND "+ lgnum;
							}
					}else if(lgnum != null){
								condition += lgnum;
								if(lnumt != null){
									condition += " AND "+ lnumt;
								}
						}else if(lnumt != null){
									condition += lnumt;
							}
			}
			
			return condition;
		}

}
