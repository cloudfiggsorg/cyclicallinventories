package com.gmodelo.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.beans.AbstractResults;
import com.gmodelo.beans.Response;
import com.gmodelo.beans.ToleranceBean;
import com.gmodelo.utils.ConnectionManager;
import com.gmodelo.utils.ReturnValues;

public class ToleranceDao {
	
	private Logger log = Logger.getLogger( ToleranceDao.class.getName());
	
	public Response<Object> addTolerance(ToleranceBean toleranceBean, String createdBy){
		
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		CallableStatement cs = null;
		
		Response<Object> res = new Response<>();
		AbstractResults abstractResult = new AbstractResults();
		String resultSP = null;
		
		final String INV_SP_ADD_TOLERANCE = "INV_SP_ADD_TOLERANCE ?, ?, ?, ?, ?, ?"; //The Store procedure to call
		
		log.log(Level.WARNING,"[addToleranceDao] Preparing sentence...");
		
		try {
			cs = con.prepareCall(INV_SP_ADD_TOLERANCE);
			
			if(toleranceBean.getToleranceId() != null){
				cs.setInt(1, toleranceBean.getToleranceId());
			}else{
				cs.setNull(1, Types.INTEGER);
			}
			if(toleranceBean.getMatkl() != null){
				cs.setString(2, toleranceBean.getMatkl());
			}else{
				cs.setNull(2, Types.INTEGER);
			}
			if(toleranceBean.getDesc() != null){
				cs.setString(3, toleranceBean.getDesc());
			}else{
				cs.setNull(3, Types.INTEGER);
			}
			if(toleranceBean.getTp() != null){
				cs.setString(4, toleranceBean.getTp());
			}else{
				cs.setNull(4, Types.INTEGER);
			}
			if(toleranceBean.getTc() != null){
				cs.setString(5, toleranceBean.getTc());
			}else{
				cs.setNull(5, Types.INTEGER);
			}
			
			cs.setString(6, createdBy);
			
			
			log.log(Level.WARNING,"[addToleranceDao] Executing query...");
			
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
			
			log.log(Level.WARNING,"[addToleranceDao] Sentence successfully executed.");
			
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[addToleranceDao] Some error occurred while was trying to execute the S.P.: INV_SP_ADD_ZONE ?, ?, ?, ?, ?, ?", e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[addToleranceDao] Some error occurred while was trying to close the connection.", e);
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs(e.getMessage());
				res.setAbstractResult(abstractResult);
				return res;
			}
		}
		abstractResult.setResultMsgAbs(resultSP);
		res.setAbstractResult(abstractResult);
		
		return res;
	}
	
	public Response<Object> deleteTolerance(String arrayIdZones){
		
		Response<Object> res = new Response<>();
		AbstractResults abstractResult = new AbstractResults();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		CallableStatement cs = null;
		
		final String INV_SP_DEL_TOLERANCE = "INV_SP_DEL_TOLERANCE ?"; //The Store procedure to call
		
		log.log(Level.WARNING,"[deleteToleranceDao] Preparing sentence...");
		
		try {
			cs = con.prepareCall(INV_SP_DEL_TOLERANCE);
			
			if(arrayIdZones != null && !arrayIdZones.isEmpty()){
				cs.setString(1,arrayIdZones);
			}else{
				cs.setNull(1, Types.INTEGER);
			}
			
			log.log(Level.WARNING,"[deleteToleranceDao] Executing query...");
			
			ResultSet rs = cs.executeQuery();
			
			//Retrive the warnings if there're
			SQLWarning warning = cs.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING,"[deleteToleranceDao] "+warning.getMessage());
				warning = warning.getNextWarning();
			}
			
			//Free resources
			rs.close();
			cs.close();	
			
			log.log(Level.WARNING,"[deleteToleranceDao] Sentence successfully executed.");
			
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[deleteToleranceDao] Some error occurred while was trying to execute the S.P.: "+INV_SP_DEL_TOLERANCE, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[deleteToleranceDao] Some error occurred while was trying to close the connection.", e);
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs(e.getMessage());
				res.setAbstractResult(abstractResult);
				return res;
			}
		}
		res.setAbstractResult(abstractResult);
		return res ;
	}
	
public Response<List<ToleranceBean>> getTolerances(ToleranceBean toleranceBean){
		
		Response<List<ToleranceBean>> res = new Response<>();
		AbstractResults abstractResult = new AbstractResults();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		PreparedStatement stm = null;
		List<ToleranceBean> listTolerance = new ArrayList<ToleranceBean>();
		//verificar si es necesario agregar mas campos al select para agregarlos al ToleranceBean
		String INV_VW_TOLERANCES = "SELECT TOLERANCE_ID, MATKL, TDESC, TP, TC FROM [INV_CIC_DB].[dbo].[INV_VW_TOLERANCES] "; //query
		
		String condition = buildCondition(toleranceBean);
		if(condition != null){
			INV_VW_TOLERANCES += condition;
			log.warning(INV_VW_TOLERANCES);
		}
		log.log(Level.WARNING,"[getTolerancesDao] Preparing sentence...");
		
		try {
			stm = con.prepareCall(INV_VW_TOLERANCES);
			
			log.log(Level.WARNING,"[getTolerancesDao] Executing query...");
			
			ResultSet rs = stm.executeQuery();
			
			while (rs.next()){
				
				toleranceBean = new ToleranceBean();
				
				toleranceBean.setToleranceId(rs.getInt(1));
				toleranceBean.setMatkl(rs.getString(2));
				toleranceBean.setDesc(rs.getString(3));
				toleranceBean.setTp(rs.getString(4));
				toleranceBean.setTc(rs.getString(5));
				
				listTolerance.add(toleranceBean);
				
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
			
			log.log(Level.WARNING,"[getTolerancesDao] Sentence successfully executed.");
			
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[getTolerancesDao] Some error occurred while was trying to execute the query: "+INV_VW_TOLERANCES, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[getTolerancesDao] Some error occurred while was trying to close the connection.", e);
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs(e.getMessage());
				res.setAbstractResult(abstractResult);
				return res;
			}
		}
		res.setAbstractResult(abstractResult);
		res.setLsObject(listTolerance);
		return res ;
	}

	private String buildCondition(ToleranceBean toleranceBean) {
		
		String condition = "";
		String toleranceId ="";
		String matkl = "";
		String desc = "";
		String tp = "";
		String tc = "";
		toleranceId = (toleranceBean.getToleranceId() != null ? (condition.contains("WHERE") ? " AND " : " WHERE ") + "TOLERANCE_ID = " + toleranceBean.getToleranceId() : "");
		condition+=toleranceId;
		matkl = (toleranceBean.getMatkl() != null ? (condition.contains("WHERE") ? " AND " : " WHERE ") + "MATKL = '" + toleranceBean.getMatkl() + "' ": "");
		condition+=matkl;
		desc = (toleranceBean.getDesc() != null ? (condition.contains("WHERE") ? " AND " : " WHERE ") + "TDESC = '"	+ toleranceBean.getDesc() + "' ": "");
		condition+=desc;
		tp = (toleranceBean.getTp() != null ? (condition.contains("WHERE") ? " AND " : " WHERE ") + "TP = '" + toleranceBean.getTp() + "' ": "");
		condition+=tp;
		tc = (toleranceBean.getTc() != null ? (condition.contains("WHERE") ? " AND " : " WHERE ") + "TC = '" + toleranceBean.getTc() + "' ": "");
		condition+=tc;
		condition = condition.isEmpty() ? null : condition;
		return condition;
	}

}
