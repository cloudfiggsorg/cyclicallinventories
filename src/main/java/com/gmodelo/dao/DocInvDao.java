package com.gmodelo.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Types;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.beans.AbstractResults;
import com.gmodelo.beans.DocInvB;
import com.gmodelo.beans.Response;
import com.gmodelo.utils.ConnectionManager;
import com.gmodelo.utils.ReturnValues;

public class DocInvDao {
	private Logger log = Logger.getLogger( DocInvDao.class.getName());
	
	public Response<Object> addDocInv(DocInvB docInvBean, String createdBy){
		
		Response<Object> res = new Response<>();
		AbstractResults abstractResult = new AbstractResults();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		CallableStatement cs = null;
		String resultSP = null;
		//ROUTE_ID, BUKRS, TYPE, CREATED_BY, JUSTIFICATION, DOC_INV_ID
		final String INV_SP_ADD_DOC_INVENTOY_HEADER = "INV_SP_ADD_ZONE ?, ?, ?, ?, ?, ?"; //The Store procedure to call
		
		log.log(Level.WARNING,"[addDocInv] Preparing sentence...");
		
		try {
			cs = con.prepareCall(INV_SP_ADD_DOC_INVENTOY_HEADER);
			
			if(docInvBean.getRouteId() != null){
				cs.setString(1,docInvBean.getRouteId());
			}else{
				cs.setNull(1, Types.INTEGER);
			}
			if(docInvBean.getBukrs() != null){
				cs.setString(2,docInvBean.getBukrs());
			}else{
				cs.setNull(2, Types.INTEGER);
			}
			if(docInvBean.getType() != null){
				cs.setString(3,docInvBean.getType());
			}else{
				cs.setNull(3, Types.INTEGER);
			}
			
			cs.setString(4,createdBy);
			
			if(docInvBean.getJustification()!= null){
				cs.setString(5,docInvBean.getJustification());
			}else{
				cs.setNull(5, Types.INTEGER);
			}
			
			if(docInvBean.getDocInvId() != null){
				cs.setString(6,docInvBean.getDocInvId());
			}else{
				cs.setNull(6, Types.INTEGER);
			}
			
			log.log(Level.WARNING,"[addDocInv] Executing query...");
			
			ResultSet rs = cs.executeQuery();
			
			while (rs.next()){
				
				resultSP = rs.getString(1);
				
			}
			
			if(resultSP != null){
				try {
					
					Integer.parseInt(resultSP);
					
				} catch (NumberFormatException e) {
					
					abstractResult.setResultId(ReturnValues.IEXCEPTION);
					abstractResult.setResultMsgAbs(resultSP);
					
					res.setAbstractResult(abstractResult);
					return res;
				}
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
			
			log.log(Level.WARNING,"[addDocInv] Sentence successfully executed.");
			
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[addDocInv] Some error occurred while was trying to execute the S.P.: "+ INV_SP_ADD_DOC_INVENTOY_HEADER, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[addDocInv] Some error occurred while was trying to close the connection.", e);
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs(e.getMessage());
				res.setAbstractResult(abstractResult);
				return res;
			}
		}
		abstractResult.setResultMsgAbs(resultSP);
		res.setAbstractResult(abstractResult);
		return res ;
	}

}
