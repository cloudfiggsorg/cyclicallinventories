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
import com.gmodelo.beans.LagpEntity;
import com.gmodelo.beans.Response;
import com.gmodelo.utils.ConnectionManager;
import com.gmodelo.utils.ReturnValues;

public class LagpDao {

	private Logger log = Logger.getLogger( BukrsDao.class.getName());
	
	public Response<List<LagpEntity>> getLagp(LagpEntity lgplaBean, String searchFilter){
		
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		
		Response<List<LagpEntity>> res = new Response<List<LagpEntity>>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		List<LagpEntity> listLgplaBean = new ArrayList<LagpEntity>(); 
		 
		String INV_VW_LAGP = "SELECT LGNUM, LGTYP, LGPLA, IMWM FROM INV_CIC_DB.dbo.INV_VW_LAGP WITH(NOLOCK) ";
		
		if(searchFilter != null){
			INV_VW_LAGP += "WHERE LGNUM LIKE '%"+searchFilter+"%' OR LGTYP LIKE '%"+searchFilter+"%' OR LGPLA LIKE '%"+searchFilter
					+"%' OR LPTYP LIKE '%"+searchFilter+"%' OR SKZUA LIKE '%"+searchFilter+"%' OR SKZUE LIKE '%"+searchFilter+"%' ";
						
		}else{
			String condition = buildCondition(lgplaBean);
			if(condition != null){
				INV_VW_LAGP += condition;
			}
		}
		

		log.info(INV_VW_LAGP);
		log.info("[getLagp] Preparing sentence...");
		try {
			
			stm = con.prepareStatement(INV_VW_LAGP);		
			
			log.info("[getLagp] Executing query...");
			
			ResultSet rs = stm.executeQuery();
			
			while (rs.next()){
				lgplaBean = new LagpEntity();
				
				lgplaBean.setLgNum(rs.getString(1));
				lgplaBean.setLgTyp(rs.getString(2));
				lgplaBean.setLgPla(rs.getString(3));
				lgplaBean.setImwm(rs.getString(4));
				listLgplaBean.add(lgplaBean);
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
			log.info("[getLagp] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[getLagp] Some error occurred while was trying to execute the query: "+INV_VW_LAGP, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[getLagp] Some error occurred while was trying to close the connection.", e);
			}
		}
		
		res.setAbstractResult(abstractResult);
		res.setLsObject(listLgplaBean);
		return res;
	}
	
	public LagpEntity getLgpla(LagpEntity lgplaBean, Connection con){
		
		ConnectionManager iConnectionManager = new ConnectionManager();
		try {
			con = con.isValid(3)? iConnectionManager.createConnection(): con;
		} catch (SQLException e1) {
			log.log(Level.SEVERE,"[getLgplaDao] Some error ocurred while was trying to check the connection");
			return null;
		}
		PreparedStatement stm = null;		
		LagpEntity lgplaBeanAux = null; 
		 
		String INV_VW_LAGP = "SELECT LGNUM, LGTYP, LGPLA, IMWM FROM INV_CIC_DB.dbo.INV_VW_LAGP WITH(NOLOCK) ";
		INV_VW_LAGP += buildCondition(lgplaBean);		

		log.info(INV_VW_LAGP);
		log.info("[getLgplaDao] Preparing sentence...");
		try {
			
			stm = con.prepareStatement(INV_VW_LAGP);		
			
			log.info("[getLgplaDao] Executing query...");
			
			ResultSet rs = stm.executeQuery();
			
			while (rs.next()){
				lgplaBeanAux = new LagpEntity();
				
				lgplaBeanAux.setLgNum(rs.getString(1));
				lgplaBeanAux.setLgTyp(rs.getString(2));
				lgplaBeanAux.setLgPla(rs.getString(3));
				lgplaBeanAux.setImwm(rs.getString(4));
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
			log.info("[getLgplaDao] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[getLgplaDao] Some error occurred while was trying to execute the query: " + INV_VW_LAGP, e);
			return null;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[getLgplaDao] Some error occurred while was trying to close the connection.", e);
			}
		}
		
		return lgplaBeanAux;
	}
	
	private String buildCondition(LagpEntity lgplaB){
				
		String lgNum = "";
		String lgTyp = "";
		String lgPla = "";
		String lpTyp = "";
		String skzua = "";
		String skzue = "";
		String imwm = "";
		String condition = "";
		
		lgNum = (lgplaB.getLgNum() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ") + " LGNUM = '" + lgplaB.getLgNum() +"' " : "";
		condition+=lgNum;
		lgTyp = (lgplaB.getLgTyp() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ") + " LGTYP = '" + lgplaB.getLgTyp() +"' " : "";
		condition+=lgTyp;
		lgPla = (lgplaB.getLgPla() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ") + " LGPLA = '" + lgplaB.getLgPla() +"' " : "";
		condition+= lgPla;
		lpTyp = (lgplaB.getLpTyp() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ") + " LGPLA = '" + lgplaB.getLgPla() +"' " : "";
		condition+=lpTyp;
		skzua = (lgplaB.getSkzua() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ") + " SKZUA = '" + lgplaB.getSkzua() +"' " : "";
		condition+=skzua;
		skzue = (lgplaB.getSkzue() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ") + " SKZUE = '" + lgplaB.getSkzue() +"' " : "";
		condition+=skzue;
		imwm = (lgplaB.getImwm() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ") + " IMWM = '" + lgplaB.getImwm() +"' " : "";
		condition+=imwm;
		condition = condition.isEmpty() ? null : condition;
		return condition;
	}


}
