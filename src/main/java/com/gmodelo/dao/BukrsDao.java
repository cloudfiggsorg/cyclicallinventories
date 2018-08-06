package com.gmodelo.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Types;
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
		CallableStatement cs = null;
		Response<List<BukrsBean>> res = new Response<List<BukrsBean>>();
		AbstractResults abstractResult = new AbstractResults();
		List<BukrsBean> listBukrsBean = new ArrayList<BukrsBean>(); 
		
		final String INV_SP_GET_WERKS_BY_BUKRS = "INV_SP_GET_WERKS_BY_BUKRS ?, ?, ?"; //The Store procedure to call
		
		log.log(Level.WARNING,"[getBukrs] Preparing sentence...");
		try {
			cs = con.prepareCall(INV_SP_GET_WERKS_BY_BUKRS);
			
			if(bukrsBean.getBukrs() != null){
				cs.setString(1, bukrsBean.getBukrs());
			}else{
				cs.setNull(1, Types.INTEGER);
			}
			if(bukrsBean.getWerks() != null){
				cs.setString(2, bukrsBean.getWerks());
			}else{
				cs.setNull(2, Types.INTEGER);
			}
			if(bukrsBean.getWerksDesc() != null){
				cs.setString(3, bukrsBean.getWerksDesc());
			}else{
				cs.setNull(3, Types.INTEGER);
			}
			
			log.log(Level.WARNING,"[getBukrs] Executing query...");
			
			ResultSet rs = cs.executeQuery();
			
			
			while (rs.next()){
				bukrsBean = new BukrsBean();
				
				bukrsBean.setBukrs(rs.getString(1));
				bukrsBean.setBukrsDesc(null);
				bukrsBean.setWerks(rs.getString(2));
				bukrsBean.setWerksDesc(rs.getString(3));
				
				listBukrsBean.add(bukrsBean);
			}
			
			//Retrive the warnings if there're
			SQLWarning warning = cs.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING,warning.getMessage());
				warning = warning.getNextWarning();
			}
			
			//Free resources
			rs.close();
			cs.close();			
			log.log(Level.WARNING,"Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[getBukrs] Some error occurred while was trying to execute the S.P.: INV_SP_GET_WERKS_BY_BUKRS ?, ?, ?, ?, ?, ?", e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"Some error occurred while was trying to close the connection.", e);
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

}
