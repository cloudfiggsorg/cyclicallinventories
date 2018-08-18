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
import com.gmodelo.beans.LgortBean;
import com.gmodelo.beans.NgortB;
import com.gmodelo.beans.Response;
import com.gmodelo.utils.ConnectionManager;
import com.gmodelo.utils.ReturnValues;

public class LgortDao {
	
	private Logger log = Logger.getLogger( LgortDao.class.getName());
	
	public Response<List<LgortBean>> getLgortByWerks(LgortBean lgortBean){
		
		Response<List<LgortBean>> res = new Response<>();
		AbstractResults abstractResult = new AbstractResults();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		PreparedStatement stm = null;
		List<LgortBean> listLgort = new ArrayList<LgortBean>();
		
		String INV_VW_GORS_BY_WERKS = "SELECT [WERKS], [LGORT], [LGOBE] FROM [INV_CIC_DB].[dbo].[INV_VW_GORS_BY_WERKS] "; //Query
		String condition = buildCondition(lgortBean);
		
		if(condition != null){
			INV_VW_GORS_BY_WERKS += condition;
			log.warning(INV_VW_GORS_BY_WERKS);
		}
		
		log.log(Level.WARNING,"[getLgortByWerks] Preparing sentence...");
		
		try {
			stm = con.prepareCall(INV_VW_GORS_BY_WERKS);
			
			log.log(Level.WARNING,"[getLgortByWerks] Executing query...");
			
			ResultSet rs = stm.executeQuery();
			
			while (rs.next()){
				
				lgortBean = new LgortBean();
				
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
			
			log.log(Level.WARNING,"[getLgortByWerks] Sentence successfully executed.");
			
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
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs(e.getMessage());
				res.setAbstractResult(abstractResult);
				return res;
			}
		}
		res.setAbstractResult(abstractResult);
		res.setLsObject(listLgort);
		return res ;
		
	}
	
	private String buildCondition(LgortBean lgortBean){
			
			String werks = null;
			String lgort = null;
			String lgobe = null;
			Boolean clause = false;
			String condition = null;
			
			if(lgortBean.getWerks() != null){
				werks = "WERKS = '" + lgortBean.getWerks()+"'";
				clause = true;
			}
			if(lgortBean.getLgort()!= null){
				lgort = "LGORT = '" + lgortBean.getLgort()+"'";
				clause = true;
			}
			if(lgortBean.getLgobe() != null){
				lgobe = "LGOBE = '" + lgortBean.getLgobe()+"'";
				clause = true;
			}
			
			if(clause){
				condition = "WHERE ";
				if(werks != null){
					condition += werks;
					if(lgort != null){
						condition += " AND "+ lgort;
						if(lgobe != null){
							condition += " AND "+ lgobe;
						}
					}
				} else if(lgort != null){
							condition += lgort;
							if(lgobe != null){
								condition += " AND "+ lgobe;
							}
					}else if(lgobe != null){
								condition += lgobe;
						}
			}
			
			return condition;
		}

	public Response<List<NgortB>> getNgorts(NgortB ngortBean){
		
		Response<List<NgortB>> res = new Response<>();
		AbstractResults abstractResult = new AbstractResults();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		PreparedStatement stm = null;
		List<NgortB> listNgort = new ArrayList<NgortB>();
		
		String INV_VW_NGORT_WITH_GORT = "SELECT WERKS, LGORT, LGNUM, LNUMT,IMWM FROM [INV_CIC_DB].[dbo].[INV_VW_NGORT_WITH_GORT] "; //Query
		String condition = buildConditionNgort(ngortBean);
		
		if(condition != null){
			INV_VW_NGORT_WITH_GORT += condition;
			log.warning(INV_VW_NGORT_WITH_GORT);
		}
		
		log.log(Level.WARNING,"[getNgortsDao] Preparing sentence...");
		
		try {
			stm = con.prepareCall(INV_VW_NGORT_WITH_GORT);
			
			log.log(Level.WARNING,"[getNgortsDao] Executing query...");
			
			ResultSet rs = stm.executeQuery();
			
			while (rs.next()){
				
				ngortBean = new NgortB();
				
				ngortBean.setWerks(rs.getString(1));
				ngortBean.setLgort(rs.getString(2));
				ngortBean.setLgnum(rs.getString(3));
				ngortBean.setImwm(rs.getString(4));
				
				listNgort.add(ngortBean);
				
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
			
			log.log(Level.WARNING,"[getNgortsDao] Sentence successfully executed.");
			
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
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs(e.getMessage());
				res.setAbstractResult(abstractResult);
				return res;
			}
		}
		res.setAbstractResult(abstractResult);
		res.setLsObject(listNgort);
		return res ;
		
	}

	private String buildConditionNgort(NgortB ngortB){
		String werks = "";
		String lgort = "";
		String lgnum = "";
		String lnumt = "";
		String imwm = "";
		String condition = "";
		
		werks = (ngortB.getWerks() 	!= null ? (condition.contains("WHERE") ? " AND " : " WHERE ") + "WERKS = '"		+ ngortB.getWerks() + "' ": "");
		condition+=werks;
		lgort = (ngortB.getLgort() 	!= null ? (condition.contains("WHERE") ? " AND " : " WHERE ") + "LGORT = '"		+ ngortB.getLgort() + "' ": "");
		condition+=lgort;
		lgnum = (ngortB.getLgnum() != null ? (condition.contains("WHERE") ? " AND " : " WHERE ") + "LGNUM = '" 	+ ngortB.getLgnum() + "' " : "");
		condition+=lgnum;
		lnumt = (ngortB.getLnumt() != null ? (condition.contains("WHERE") ? " AND " : " WHERE ") + "LNUMT = '" 	+ ngortB.getLnumt() + "' " : "");
		condition+=lnumt;
		imwm = (ngortB.getImwm() != null ? (condition.contains("WHERE") ? " AND " : " WHERE ") + "IMWM = '"+ ngortB.getImwm() + "' ": "");
		condition+=imwm;
		condition = condition.isEmpty() ? null : condition;	
		return condition;
	}

}
