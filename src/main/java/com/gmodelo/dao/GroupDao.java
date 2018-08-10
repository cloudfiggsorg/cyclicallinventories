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
import com.gmodelo.beans.GroupBean;
import com.gmodelo.beans.Response;
import com.gmodelo.utils.ConnectionManager;
import com.gmodelo.utils.ReturnValues;

public class GroupDao {
	private Logger log = Logger.getLogger( GroupDao.class.getName());
	
	public Response<Object> addGroup(GroupBean groupBean, String createdBy){
		
		Response<Object> res = new Response<>();
		AbstractResults abstractResult = new AbstractResults();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		CallableStatement cs = null;
		String resultSP = null;
		
		final String INV_SP_ADD_GROUP = "INV_SP_ADD_GROUP ?, ?, ?, ?"; //The Store procedure to call
		
		log.log(Level.WARNING,"[addGroup] Preparing sentence...");

		try {
			cs = con.prepareCall(INV_SP_ADD_GROUP);
			
			if(groupBean.getGroupId() != null){
				cs.setString(1,groupBean.getGroupId());
			}else{
				cs.setNull(1, Types.INTEGER);
			}
			if(groupBean.getGroupDesc() != null){
				cs.setString(2,groupBean.getGroupDesc());
			}else{
				cs.setNull(2, Types.INTEGER);
			}
			if(groupBean.getGroupType() != null){
				cs.setString(3,groupBean.getGroupType());
			}else{
				cs.setNull(3, Types.INTEGER);
			}
			
			cs.setString(4, createdBy);
			
			log.log(Level.WARNING,"[addGroup] Executing query...");
			
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
				log.log(Level.WARNING,"[addGroup] "+warning.getMessage());
				warning = warning.getNextWarning();
			}
			
			//Free resources
			rs.close();
			cs.close();	
			
			log.log(Level.WARNING,"[addGroup] Sentence successfully executed.");
			
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[addGroup] Some error occurred while was trying to execute the S.P.: "+INV_SP_ADD_GROUP, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[addGroup] Some error occurred while was trying to close the connection.", e);
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
