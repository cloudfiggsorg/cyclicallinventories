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
import com.gmodelo.beans.LgortBean;
import com.gmodelo.beans.Response;
import com.gmodelo.utils.ConnectionManager;
import com.gmodelo.utils.ReturnValues;

public class LgortDao {
	
	private Logger log = Logger.getLogger( LgortDao.class.getName());
	
	public Response<List<LgortBean>> getLgortByWerks(LgortBean lgortBean){
		
		Response<List<LgortBean>> res = new Response<>();
		AbstractResults abstractResult = new AbstractResults();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		CallableStatement cs = null;
		List<LgortBean> listLgort = new ArrayList<LgortBean>();
		
		final String INV_SP_GET_GORTS_BY_WERKS = "INV_SP_GET_GORTS_BY_WERKS ?, ?, ?"; //The Store procedure to call
		
		log.log(Level.WARNING,"[getLgortByWerks] Preparing sentence...");
		
		try {
			cs = con.prepareCall(INV_SP_GET_GORTS_BY_WERKS);
			
			if(lgortBean.getWerks() != null){
				cs.setString(1,lgortBean.getWerks());
			}else{
				cs.setNull(1, Types.INTEGER);
			}
			if(lgortBean.getLgort() != null){
				cs.setString(2,lgortBean.getLgort());
			}else{
				cs.setNull(2, Types.INTEGER);
			}
			if(lgortBean.getLgobe() != null){
				cs.setString(3,lgortBean.getLgobe());
			}else{
				cs.setNull(3, Types.INTEGER);
			}
			
			log.log(Level.WARNING,"[getLgortByWerks] Executing query...");
			
			ResultSet rs = cs.executeQuery();
			
			while (rs.next()){
				
				lgortBean = new LgortBean();
				
				lgortBean.setWerks(rs.getString(1));
				lgortBean.setLgort(rs.getString(2));
				lgortBean.setLgobe(rs.getString(3));
				
				listLgort.add(lgortBean);
				
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
			
			log.log(Level.WARNING,"[getLgortByWerks] Sentence successfully executed.");
			
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[getLgortByWerks] Some error occurred while was trying to execute the S.P.: INV_SP_GET_GORTS_BY_WERKS ?, ?, ?", e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[getLgortByWerks] Some error occurred while was trying to close the connection.", e);
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs(e.getMessage());
				res.setAbstractResult(abstractResult);
				return res;
			}
		}
		res.setAbstractResult(abstractResult);
		res.setLsObject(listLgort);
		return res ;
		
	}

}
