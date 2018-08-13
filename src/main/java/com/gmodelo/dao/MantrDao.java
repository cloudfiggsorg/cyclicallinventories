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
import com.gmodelo.beans.Response;
import com.gmodelo.beans.MantrB;
import com.gmodelo.utils.ConnectionManager;
import com.gmodelo.utils.ReturnValues;

public class MantrDao {
	
	private Logger log = Logger.getLogger( MantrDao.class.getName());
	
	public Response<List<MantrB>> getMantr(MantrB mantrBean){
		
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		PreparedStatement stm = null;
		
		Response<List<MantrB>> res = new Response<List<MantrB>>();
		AbstractResults abstractResult = new AbstractResults();
		List<MantrB> listMantrBean = new ArrayList<MantrB>(); 
		 
		String INV_VW_MATNR_BY_WERKS = "SELECT WERKS, MATNR, MAKTX  FROM [INV_CIC_DB].[dbo].[INV_VW_MATNR_BY_WERKS] WITH(NOLOCK) ";
		
		String condition = buildCondition(mantrBean);
		if(condition != null){
			INV_VW_MATNR_BY_WERKS += condition;
			log.warning(INV_VW_MATNR_BY_WERKS);
		}
		log.log(Level.WARNING,"[getMantrDao] Preparing sentence...");
		try {
			
			stm = con.prepareStatement(INV_VW_MATNR_BY_WERKS);		
			
			log.log(Level.WARNING,"[getMantrDao] Executing query...");
			
			ResultSet rs = stm.executeQuery();
			
			while (rs.next()){
				mantrBean = new MantrB();
				
				mantrBean.setWerks(rs.getString(1));
				mantrBean.setMatnr(rs.getString(2));
				mantrBean.setMaktx(rs.getString(3));
				
				listMantrBean.add(mantrBean);
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
			log.log(Level.WARNING,"[getMantrDao] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[getMantrDao] Some error occurred while was trying to execute the query: "+INV_VW_MATNR_BY_WERKS, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[getMantrDao] Some error occurred while was trying to close the connection.", e);
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs(e.getMessage());
				res.setAbstractResult(abstractResult);
				return res;
			}
		}
		
		res.setAbstractResult(abstractResult);
		res.setLsObject(listMantrBean);
		return res;
	}
	
	private String buildCondition(MantrB mantrB){
		String werks = "";
		String matnr = "";
		String maktx = "";
		
		String condition = null;
		
		werks = (mantrB.getWerks() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ")+" WERKS = '"+ mantrB.getWerks() + "' "  : "";
		condition+=werks;
		matnr = (mantrB.getMatnr() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ") + " MATNR = '" + mantrB.getMatnr() +"' " : "";
		condition+=matnr;
		maktx = (mantrB.getMaktx() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ") + " MAKTX = '" + mantrB.getMaktx() +"' " : "";
		condition+=maktx;
		
		return condition;
	}


}
