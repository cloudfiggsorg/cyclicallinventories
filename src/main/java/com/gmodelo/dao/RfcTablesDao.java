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
import com.gmodelo.beans.RfcTablesB;
import com.gmodelo.utils.ConnectionManager;
import com.gmodelo.utils.ReturnValues;

public class RfcTablesDao {
	
	private Logger log = Logger.getLogger( RfcTablesDao.class.getName());
	
	public Response<List<RfcTablesB>> getRfcTables(RfcTablesB rfcBean){
		
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		PreparedStatement stm = null;
		
		Response<List<RfcTablesB>> res = new Response<List<RfcTablesB>>();
		AbstractResults abstractResult = new AbstractResults();
		List<RfcTablesB> listRfcTablesBean = new ArrayList<RfcTablesB>(); 
		 
		String INV_VW_RFC_TABLE = "SELECT TABLE_NAME, TABLE_VALUES, LAST_REQUEST,DEVICE  FROM [INV_CIC_DB].[dbo].[INV_VW_RFC_TABLE] WITH(NOLOCK) ";
		
		String condition = buildCondition(rfcBean);
		if(condition != null){
			INV_VW_RFC_TABLE += condition;
			log.warning(INV_VW_RFC_TABLE);
		}
		log.log(Level.WARNING,"[getRfcTablesDao] Preparing sentence...");
		try {
			
			stm = con.prepareStatement(INV_VW_RFC_TABLE);		
			
			log.log(Level.WARNING,"[getRfcTablesDao] Executing query...");
			
			ResultSet rs = stm.executeQuery();
			
			while (rs.next()){
				rfcBean = new RfcTablesB();
				
				rfcBean.setName(rs.getString(1));
				rfcBean.setValues(rs.getString(2));
				rfcBean.setDate(rs.getString(3));
				rfcBean.setDevice(rs.getString(4));
				
				listRfcTablesBean.add(rfcBean);
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
			log.log(Level.WARNING,"[getRfcTablesDao] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[getRfcTablesDao] Some error occurred while was trying to execute the query: "+INV_VW_RFC_TABLE, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[getRfcTablesDao] Some error occurred while was trying to close the connection.", e);
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs(e.getMessage());
				res.setAbstractResult(abstractResult);
				return res;
			}
		}
		
		res.setAbstractResult(abstractResult);
		res.setLsObject(listRfcTablesBean);
		return res;
	}

	private String buildCondition(RfcTablesB rfcTablesB){
		String name = "";
		String values = "";
		String date = "";
		String device = "";
		
		String condition = null;
		//TABLE_NAME, TABLE_VALUES, LAST_REQUEST,DEVICE
		name = (rfcTablesB.getName() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ")+" TABLE_NAME = '"+ rfcTablesB.getName() + "' "  : "";
		condition+=name;
		values = (rfcTablesB.getValues() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ") + " TABLE_VALUES = '" + rfcTablesB.getValues() +"' " : "";
		condition+=values;
		date = (rfcTablesB.getDate() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ") + " LAST_REQUEST = '" + rfcTablesB.getDate() +"' " : "";
		condition+=date;
		device = (rfcTablesB.getDevice() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ") + " DEVICE = '" + rfcTablesB.getDevice() +"' " : "";
		condition+=device;
		
		return condition;
	}
}
