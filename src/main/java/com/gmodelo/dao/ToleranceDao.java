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
	
	public Response<List<ToleranceBean>> getMATKL(ToleranceBean toleranceBean, String searchFilter){
		
		Response<List<ToleranceBean>> res = new Response<>();
		AbstractResults abstractResult = new AbstractResults();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		PreparedStatement stm = null;
		List<ToleranceBean> listMATKL = new ArrayList<ToleranceBean>();
		//verificar si es necesario agregar mas campos al select para agregarlos al ToleranceBean
		String INV_VW_MATKL = "SELECT MATKL, WGBEZ FROM [INV_CIC_DB].[dbo].[INV_VW_MATKL] "; //query
		
		if(searchFilter != null){
			INV_VW_MATKL += "WHERE MATKL LIKE '%"+searchFilter+"%' OR WGBEZ LIKE '%"+searchFilter+"%'";
		}
		
		log.log(Level.WARNING,"[getMATKLDao] Preparing sentence...");
		
		try {
			stm = con.prepareCall(INV_VW_MATKL);
			
			log.log(Level.WARNING,"[getMATKLDao] Executing query...");
			
			ResultSet rs = stm.executeQuery();
			
			while (rs.next()){
				
				toleranceBean = new ToleranceBean();
				
				toleranceBean.setMatkl(rs.getString(1));
				toleranceBean.setDesc(rs.getString(2));
				
				listMATKL.add(toleranceBean);
				
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
			
			log.log(Level.WARNING,"[getMATKLDao] Sentence successfully executed.");
			
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[getMATKLDao] Some error occurred while was trying to execute the query: "+INV_VW_MATKL, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[getMATKLDao] Some error occurred while was trying to close the connection.", e);
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs(e.getMessage());
				res.setAbstractResult(abstractResult);
				return res;
			}
		}
		res.setAbstractResult(abstractResult);
		res.setLsObject(listMATKL);
		return res ;
	}
	
	public Response<Object> addTolerance(ToleranceBean toleranceBean, String createdBy){
		
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		CallableStatement cs = null;
		
		Response<Object> res = new Response<>();
		AbstractResults abstractResult = new AbstractResults();
		
		final String INV_SP_ADD_TOLERANCE = "INV_SP_ADD_TOLERANCE ?, ?, ?, ?, ?, ?"; //The Store procedure to call
		
		log.log(Level.WARNING,"[addToleranceDao] Preparing sentence...");
		
		try {
			cs = con.prepareCall(INV_SP_ADD_TOLERANCE);
			
			cs.setInt(1, toleranceBean.getToleranceId());
			cs.setString(2, toleranceBean.getMatkl());
			cs.setString(3, toleranceBean.getDesc());
			cs.setString(4, toleranceBean.getTp());
			cs.setString(5, toleranceBean.getTc());
			cs.setString(6, createdBy);
			cs.registerOutParameter(1, Types.INTEGER);
			
			
			log.log(Level.WARNING,"[addToleranceDao] Executing query...");
			
			cs.execute();
			
			abstractResult.setResultId(cs.getInt(1));
			//Retrive the warnings if there're
			SQLWarning warning = cs.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING,warning.getMessage());
				warning = warning.getNextWarning();
			}
			
			//Free resources
			cs.close();	
			
			log.log(Level.WARNING,"[addToleranceDao] Sentence successfully executed.");
			
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[addToleranceDao] Some error occurred while was trying to execute the S.P.: "+INV_SP_ADD_TOLERANCE, e);
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
		res.setAbstractResult(abstractResult);
		
		return res;
	}
	
	public Response<Object> deleteTolerance(String arrayIdZones){
		
		Response<Object> res = new Response<>();
		AbstractResults abstractResult = new AbstractResults();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		CallableStatement cs = null;
		
		final String INV_SP_DEL_TOLERANCE = "INV_SP_DEL_TOLERANCE ?, ?"; //The Store procedure to call
		
		log.log(Level.WARNING,"[deleteToleranceDao] Preparing sentence...");
		
		try {
			cs = con.prepareCall(INV_SP_DEL_TOLERANCE);
			
			cs.setString(1,arrayIdZones);
			cs.registerOutParameter(2, Types.INTEGER);
			
			log.log(Level.WARNING,"[deleteToleranceDao] Executing query...");
			
			cs.execute();
			
			abstractResult.setResultId(cs.getInt(2));
			
			//Retrive the warnings if there're
			SQLWarning warning = cs.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING,"[deleteToleranceDao] "+warning.getMessage());
				warning = warning.getNextWarning();
			}
			
			//Free resources
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
	
public Response<List<ToleranceBean>> getTolerances(ToleranceBean toleranceBean, String searchFilter){
		
		Response<List<ToleranceBean>> res = new Response<>();
		AbstractResults abstractResult = new AbstractResults();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		PreparedStatement stm = null;
		List<ToleranceBean> listTolerance = new ArrayList<ToleranceBean>();
		//verificar si es necesario agregar mas campos al select para agregarlos al ToleranceBean
		String INV_VW_TOLERANCES = "SELECT TOLERANCE_ID, MATKL, TDESC, TP, TC FROM [INV_CIC_DB].[dbo].[INV_VW_TOLERANCES] "; //query
		
		if(searchFilter != null){
			INV_VW_TOLERANCES += "WHERE TOLERANCE_ID LIKE '%"+searchFilter+"%' OR MATKL LIKE '%"+searchFilter+"%' OR TDESC LIKE '%"+searchFilter+"%'";
		}else{
			String condition = buildCondition(toleranceBean);
			if(condition != null){
				INV_VW_TOLERANCES += condition;
				
			}
		}
		
		log.warning(INV_VW_TOLERANCES);
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
