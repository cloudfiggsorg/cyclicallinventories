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
	
	private static final String INV_VW_GORS_BY_WERKS = "SELECT [WERKS], [LGORT], [LGOBE] FROM [INV_CIC_DB].[dbo].[INV_VW_GORS_BY_WERKS] WHERE WERKS = ? "; //Query
	
	public Response<List<LgortBeanView>> getLgortByWerks(LgortBeanView lgortBean){
		
		Response<List<LgortBeanView>> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		List<LgortBeanView> listLgort = new ArrayList<LgortBeanView>();
		
		log.info("[getLgortByWerks] Preparing sentence...");
		
		try {
			stm = con.prepareCall(INV_VW_GORS_BY_WERKS);
			
			log.info("[getLgortByWerks] Executing query...");
			
			stm.setString(1, lgortBean.getWerks());
			ResultSet rs = stm.executeQuery();
			
			
			while (rs.next()){
				lgortBean = new LgortBeanView();
				lgortBean.setWerks(rs.getString(1));
				lgortBean.setLgort(rs.getString(2));
				lgortBean.setLgobe(rs.getString(3));
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
	
	private static final String INV_VW_NGORT_WITH_GORT = "SELECT WERKS, LGORT, LGOBE, LGNUM, LNUMT,"
			+ " IMWM FROM [INV_CIC_DB].[dbo].[INV_VW_NGORT_WITH_GORT] WHERE WERKS = ?"; //Query

	public Response<List<LgortBeanView>> getNgorts(LgortBeanView lgortBeanView){
		
		Response<List<LgortBeanView>> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		List<LgortBeanView> listNgort = new ArrayList<LgortBeanView>();
		
		
	
		log.info("[getNgortsDao] Preparing sentence...");
		
		try {
			stm = con.prepareStatement(INV_VW_NGORT_WITH_GORT);
			stm.setString(1, lgortBeanView.getWerks());
			
			log.info("[getNgortsDao] Executing query...");
			
			ResultSet rs = stm.executeQuery();
			
			while (rs.next()){
				
				lgortBeanView = new LgortBeanView();
				
				lgortBeanView.setWerks(rs.getString(1));
				lgortBeanView.setLgort(rs.getString(2));
				lgortBeanView.setLgobe(rs.getString(3));
				lgortBeanView.setLgNum(rs.getString(4));
				lgortBeanView.setLnumt(rs.getString(5));
				lgortBeanView.setImwm(rs.getString(6));
				
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
	
	private static final String INV_VW_NGORT_WITH_GORT_IM = "SELECT WERKS, LGORT, LGOBE, LGNUM, LNUMT,"
			+ " IMWM FROM [INV_CIC_DB].[dbo].[INV_VW_NGORT_WITH_GORT] WHERE WERKS = ?"; //Query

public Response<List<LgortBeanView>> getNgortsIM(LgortBeanView lgortBeanView){
		
		Response<List<LgortBeanView>> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		List<LgortBeanView> listNgort = new ArrayList<LgortBeanView>();
		
		
	
		log.info("[getNgortsDao] Preparing sentence...");
		
		try {
			stm = con.prepareStatement(INV_VW_NGORT_WITH_GORT);
			stm.setString(1, lgortBeanView.getWerks());
			
			log.info("[getNgortsDao] Executing query...");
			
			ResultSet rs = stm.executeQuery();
			
			while (rs.next()){
				lgortBeanView = new LgortBeanView();
				lgortBeanView.setWerks(rs.getString(1));
				lgortBeanView.setLgort(rs.getString(2));
				lgortBeanView.setLgobe(rs.getString(3));
				lgortBeanView.setLgNum(rs.getString(4));
				lgortBeanView.setLnumt(rs.getString(5));
				lgortBeanView.setImwm(rs.getString(6));
				listNgort.add(lgortBeanView);
				
			}
			
			log.info("[getNgortsDao] Sentence successfully executed.");
			
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[getNgortsDao] Some error occurred while was trying to execute the query: "+ INV_VW_NGORT_WITH_GORT, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			res.setAbstractResult(abstractResult);
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

}
