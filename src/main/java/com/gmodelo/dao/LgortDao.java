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
import com.gmodelo.beans.LgortBeanView;
import com.gmodelo.beans.Response;
import com.gmodelo.utils.ConnectionManager;
import com.gmodelo.utils.ReturnValues;

public class LgortDao {
	
	private Logger log = Logger.getLogger( LgortDao.class.getName());
	
	public Response<List<LgortBeanView>> getLgortByWerks(LgortBeanView lgortBean){
		
		Response<List<LgortBeanView>> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		List<LgortBeanView> listLgort = new ArrayList<LgortBeanView>();
		
		String INV_VW_GORS_BY_WERKS = "SELECT [WERKS], [LGORT], [LGOBE] , [LGNUM] FROM [INV_CIC_DB].[dbo].[INV_VW_GORS_BY_WERKS] "; //Query
		String condition = buildCondition(lgortBean);
		
		if(condition != null){
			INV_VW_GORS_BY_WERKS += condition;
			log.info(INV_VW_GORS_BY_WERKS);
		}
		
		log.info("[getLgortByWerks] Preparing sentence...");
		
		try {
			stm = con.prepareCall(INV_VW_GORS_BY_WERKS);
			
			log.info("[getLgortByWerks] Executing query...");
			
			ResultSet rs = stm.executeQuery();
			
			while (rs.next()){
				
				lgortBean = new LgortBeanView();
				
				lgortBean.setWerks(rs.getString(1));
				lgortBean.setLgort(rs.getString(2));
				lgortBean.setLgobe(rs.getString(3));
				lgortBean.setLgNum(rs.getString(4));
				listLgort.add(lgortBean);
				
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
			
			log.info("[getLgortByWerks] Sentence successfully executed.");
			
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[getLgortByWerks] Some error occurred while was trying to execute the query: "+INV_VW_GORS_BY_WERKS, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[getLgortByWerks] Some error occurred while was trying to close the connection.", e);
			}
		}
		res.setAbstractResult(abstractResult);
		res.setLsObject(listLgort);
		return res ;
		
	}
	
	private String buildCondition(LgortBeanView lgortBean){
			
			String werks = "";
			String lgort = "";
			String lgobe = "";
			String lgNum = "";
			String condition = "";
			
			werks = (lgortBean.getWerks() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ") + " WERKS LIKE '%" + lgortBean.getWerks() +"%' " : "";
			condition += werks;
			lgort = (lgortBean.getLgort() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ") + " LGORT LIKE '%" + lgortBean.getLgort() +"%' " : "";
			condition += lgort;
			lgobe = (lgortBean.getLgobe() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ") + " LGOBE LIKE '%" + lgortBean.getLgobe() +"%' " : "";
			condition += lgobe;
			lgNum = (lgortBean.getLgobe() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ") + " LGNUM LIKE '%" + lgortBean.getLgNum() +"%' " : "";
			condition += lgNum;
			condition = (condition.isEmpty() ? null : condition);
			return condition;
		}

	public Response<List<LgortBeanView>> getNgorts(LgortBeanView lgortBeanView){
		
		Response<List<LgortBeanView>> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		List<LgortBeanView> listNgort = new ArrayList<LgortBeanView>();
		
		String INV_VW_NGORT_WITH_GORT = "SELECT WERKS, LGORT, LGNUM, LNUMT,IMWM FROM [INV_CIC_DB].[dbo].[INV_VW_NGORT_WITH_GORT] "; //Query
		String condition = buildConditionNgort(lgortBeanView);
		
		if(condition != null){
			INV_VW_NGORT_WITH_GORT += condition;
			log.info(INV_VW_NGORT_WITH_GORT);
		}
		
		log.info("[getNgortsDao] Preparing sentence...");
		
		try {
			stm = con.prepareCall(INV_VW_NGORT_WITH_GORT);
			
			log.info("[getNgortsDao] Executing query...");
			
			ResultSet rs = stm.executeQuery();
			
			while (rs.next()){
				
				lgortBeanView = new LgortBeanView();
				
				lgortBeanView.setWerks(rs.getString(1));
				lgortBeanView.setLgort(rs.getString(2));
				lgortBeanView.setImwm(rs.getString(4));
				
				listNgort.add(lgortBeanView);
				
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
			
			log.info("[getNgortsDao] Sentence successfully executed.");
			
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[getNgortsDao] Some error occurred while was trying to execute the query: "+ INV_VW_NGORT_WITH_GORT, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[getLgortByWerks] Some error occurred while was trying to close the connection.", e);
			}
		}
		res.setAbstractResult(abstractResult);
		res.setLsObject(listNgort);
		return res ;
		
	}

	private String buildConditionNgort(LgortBeanView lgortBeanView){
		String werks = "";
		String lgort = "";
		String lnumt = "";
		String imwm = "";
		String condition = "";
		
		werks = (lgortBeanView.getWerks() 	!= null ? (condition.contains("WHERE") ? " AND " : " WHERE ") + "WERKS = '"		+ lgortBeanView.getWerks() + "' ": "");
		condition+=werks;
		lgort = (lgortBeanView.getLgort() 	!= null ? (condition.contains("WHERE") ? " AND " : " WHERE ") + "LGORT = '"		+ lgortBeanView.getLgort() + "' ": "");
		condition+=lgort;
		lnumt = (lgortBeanView.getLnumt() != null ? (condition.contains("WHERE") ? " AND " : " WHERE ") + "LNUMT = '" 	+ lgortBeanView.getLnumt() + "' " : "");
		condition+=lnumt;
		imwm = (lgortBeanView.getImwm() != null ? (condition.contains("WHERE") ? " AND " : " WHERE ") + "IMWM = '"+ lgortBeanView.getImwm() + "' ": "");
		condition+=imwm;
		condition = condition.isEmpty() ? null : condition;	
		return condition;
	}

}
