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
import com.gmodelo.beans.Lgnum;
import com.gmodelo.beans.Response;
import com.gmodelo.utils.ConnectionManager;
import com.gmodelo.utils.ReturnValues;

public class LgnumDao {
	
private Logger log = Logger.getLogger( LgnumDao.class.getName());
	
	public Response<List<Lgnum>> getLgnumByLgort(Lgnum lgnum){
		
		Response<List<Lgnum>> res = new Response<>();
		AbstractResults abstractResult = new AbstractResults();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		CallableStatement cs = null;
		List<Lgnum> listLgnum = new ArrayList<Lgnum>();
		
		final String INV_SP_GET_NGORT_BY_GORT = "INV_SP_GET_NGORT_BY_GORT ?, ?, ?, ?"; //The Store procedure to call
		
		log.log(Level.WARNING,"[getLgnumByLgort] Preparing sentence...");
		
		try {
			cs = con.prepareCall(INV_SP_GET_NGORT_BY_GORT);
			
			if(lgnum.getWerks() != null){
				cs.setString(1,lgnum.getWerks());
			}else{
				cs.setNull(1, Types.INTEGER);
			}
			if(lgnum.getLgort() != null){
				cs.setString(2,lgnum.getLgort());
			}else{
				cs.setNull(2, Types.INTEGER);
			}
			if(lgnum.getLgnum() != null){
				cs.setString(3,lgnum.getLgnum());
			}else{
				cs.setNull(3, Types.INTEGER);
			}
			if(lgnum.getLnumt() != null){
				cs.setString(4,lgnum.getLnumt());
			}else{
				cs.setNull(4, Types.INTEGER);
			}
			
			log.log(Level.WARNING,"[getLgnumByLgort] Executing query...");
			
			ResultSet rs = cs.executeQuery();
			
			while (rs.next()){
				
				lgnum = new Lgnum();
				
				lgnum.setWerks(rs.getString(1));
				lgnum.setLgort(rs.getString(2));
				lgnum.setLgnum(rs.getString(3));
				lgnum.setLnumt(rs.getString(4));
				
				listLgnum.add(lgnum);
				
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
			
			log.log(Level.WARNING,"[getLgnumByLgort] Sentence successfully executed.");
			
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[getLgnumByLgort] Some error occurred while was trying to execute the S.P.: INV_SP_GET_NGORT_BY_GORT ?, ?, ?, ?", e);
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

}
