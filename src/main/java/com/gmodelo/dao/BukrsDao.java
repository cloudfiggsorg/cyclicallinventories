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

import com.gmodelo.beans.AbstractResults;
import com.gmodelo.beans.BukrsBean;
import com.gmodelo.beans.Response;
import com.gmodelo.utils.ConnectionManager;
import com.gmodelo.utils.ReturnValues;

//Sociedad Dao
public class BukrsDao {
	
	private Logger log = Logger.getLogger( BukrsDao.class.getName());
	
	public Response<List<BukrsBean>> getBukrsWithWerks(BukrsBean bukrsBean){
		
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		PreparedStatement stm = null;
		
		Response<List<BukrsBean>> res = new Response<List<BukrsBean>>();
		AbstractResults abstractResult = new AbstractResults();
		List<BukrsBean> listBukrsBean = new ArrayList<BukrsBean>(); 
		 
		String INV_VW_WERKS_BY_BUKRS = "SELECT [BUKRS], [WERKS], [NAME1] FROM [INV_CIC_DB].[dbo].[INV_VW_WERKS_BY_BUKRS] WITH(NOLOCK) ";
		
		String condition = buildCondition(bukrsBean);
		if(condition != null){
			INV_VW_WERKS_BY_BUKRS += condition;
			log.warning(INV_VW_WERKS_BY_BUKRS);
		}
		log.log(Level.WARNING,"[getBukrs] Preparing sentence...");
		try {
			
			stm = con.prepareStatement(INV_VW_WERKS_BY_BUKRS);		
			
			log.log(Level.WARNING,"[getBukrs] Executing query...");
			
			ResultSet rs = stm.executeQuery();
			
			while (rs.next()){
				bukrsBean = new BukrsBean();
				
				bukrsBean.setBukrs(rs.getString(1));
				bukrsBean.setBukrsDesc(null);
				bukrsBean.setWerks(rs.getString(2));
				bukrsBean.setWerksDesc(rs.getString(3));
				
				listBukrsBean.add(bukrsBean);
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
			log.log(Level.WARNING,"[getBukrs] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[getBukrs] Some error occurred while was trying to execute the query: "+INV_VW_WERKS_BY_BUKRS, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[getBukrs] Some error occurred while was trying to close the connection.", e);
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs(e.getMessage());
				res.setAbstractResult(abstractResult);
				return res;
			}
		}
		
		res.setAbstractResult(abstractResult);
		res.setLsObject(listBukrsBean);
		return res;
	}
	
	private String buildCondition(BukrsBean bukrsBean){
		
		String bukrs = null;
		String werks = null;
		String werksDesc = null;
		Boolean clause = false;
		String condition = null;
		
		if(bukrsBean.getBukrs() != null){
			bukrs = "BUKRS = '" + bukrsBean.getBukrs()+"'";
			clause = true;
		}
		if(bukrsBean.getWerks() != null){
			werks = "WERKS = '" + bukrsBean.getWerks()+"'";
			clause = true;
		}
		if(bukrsBean.getWerksDesc() != null){
			werksDesc = "NAME1 = '" + bukrsBean.getWerksDesc()+"'";
			clause = true;
		}
		
		if(clause){
			condition = "WHERE ";
			if(bukrs != null){
				condition += bukrs;
				if(werks != null){
					condition += " AND "+ werks;
					if(werksDesc != null){
						condition += " AND "+ werksDesc;
					}
				}
			} else if(werks != null){
						condition += werks;
						if(werksDesc != null){
							condition += " AND "+ werksDesc;
						}
				}else if(werksDesc != null){
							condition += werksDesc;
					}
		}
		
		return condition;
	}

}
