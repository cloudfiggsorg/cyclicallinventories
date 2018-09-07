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
import com.gmodelo.beans.BukrsBean;
import com.gmodelo.beans.Response;
import com.gmodelo.utils.ConnectionManager;
import com.gmodelo.utils.ReturnValues;

//Sociedad Dao
public class BukrsDao {
	
	private Logger log = Logger.getLogger( BukrsDao.class.getName());
	
	public Response<List<BukrsBean>> getBukrs(BukrsBean bukrsBean, String searchFilter){
		
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		
		Response<List<BukrsBean>> res = new Response<List<BukrsBean>>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		List<BukrsBean> listBukrsBean = new ArrayList<BukrsBean>(); 
		 
		String INV_VW_BUKRS = "SELECT [BUKRS], [BUTXT] FROM [INV_CIC_DB].[dbo].[INV_VW_BUKRS] WITH(NOLOCK) ";
		if(searchFilter ==  null){
			String condition = buildCondition(bukrsBean);
			if(condition != null){
				INV_VW_BUKRS += condition;
				
			}
		}else{
			INV_VW_BUKRS += "WHERE BUKRS LIKE '%"+searchFilter+"%' OR BUTXT LIKE '%"+searchFilter+"%'";
		}
		
		log.warning(INV_VW_BUKRS);
		
		log.log(Level.WARNING,"[getBukrsDao] Preparing sentence...");
		try {
			
			stm = con.prepareStatement(INV_VW_BUKRS);		
			
			log.log(Level.WARNING,"[getBukrsDao] Executing query...");
			
			ResultSet rs = stm.executeQuery();
			
			while (rs.next()){
				bukrsBean = new BukrsBean();
				
				bukrsBean.setBukrs(rs.getString(1));
				bukrsBean.setBukrsDesc(rs.getString(2));
				
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
			log.log(Level.WARNING,"[getBukrsDao] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[getBukrsDao] Some error occurred while was trying to execute the query: "+INV_VW_BUKRS, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[getBukrsDao] Some error occurred while was trying to close the connection.", e);
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
	
	public Response<List<BukrsBean>> getBukrsWithWerks(BukrsBean bukrsBean, String searchFilter){
		
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		
		Response<List<BukrsBean>> res = new Response<List<BukrsBean>>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		List<BukrsBean> listBukrsBean = new ArrayList<BukrsBean>(); 
		 
		String INV_VW_WERKS_BY_BUKRS = "SELECT [BUKRS], [WERKS], [NAME1] FROM [INV_CIC_DB].[dbo].[INV_VW_WERKS_BY_BUKRS] WITH(NOLOCK) ";
		
		if(searchFilter ==  null){
			String condition = buildCondition(bukrsBean);
			if(condition != null){
				INV_VW_WERKS_BY_BUKRS += condition;
				
			}
		}else{
			INV_VW_WERKS_BY_BUKRS += "WHERE BUKRS LIKE '%"+searchFilter+"%' OR WERKS LIKE '%"+searchFilter+"%'" +"%' OR NAME1 LIKE '%"+searchFilter+"%'";
		}
		log.warning(INV_VW_WERKS_BY_BUKRS);
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
		
		String bukrs = "";
		String bukrsDesc = "";
		String werks = "";
		String werksDesc = "";
		String condition = "";
		
		bukrs = (bukrsBean.getBukrs() != null ? (condition.contains("WHERE") ? "AND " : "WHERE ") + "BUKRS LIKE '%" + bukrsBean.getBukrs()+"%'" : "");
		condition += bukrs;
		
		bukrsDesc = (bukrsBean.getBukrsDesc() != null ? (condition.contains("WHERE") ? "AND " : "WHERE ") + "BUKRS LIKE '%" + bukrsBean.getBukrsDesc()+"%'" : "");
		condition += bukrsDesc;
		
		werks = (bukrsBean.getWerks() != null ? (condition.contains("WHERE") ? "AND " : "WHERE ") + "WERKS LIKE '%" + bukrsBean.getWerks()+"%'" : "");
		condition += werks;
		
		werksDesc = (bukrsBean.getWerksDesc() != null ? (condition.contains("WHERE") ? "AND " : "WHERE ") + "NAME1 LIKE '%" + bukrsBean.getWerksDesc()+"%'" : "");
		condition += werksDesc;
		
		condition = (condition.isEmpty() ? null : condition);
		
		return condition;
	}

}
