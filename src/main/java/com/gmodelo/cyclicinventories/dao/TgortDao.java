package com.gmodelo.cyclicinventories.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.cyclicinventories.beans.AbstractResultsBean;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.beans.TgortB;
import com.gmodelo.cyclicinventories.utils.ConnectionManager;
import com.gmodelo.cyclicinventories.utils.ReturnValues;

public class TgortDao {

	private Logger log = Logger.getLogger( TgortDao.class.getName());
	
	public Response<List<TgortB>> getTgortWithNgort(TgortB tgortBean){
		
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		
		Response<List<TgortB>> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		List<TgortB> listTgortBean = new ArrayList<>(); 
		 
		String INV_VW_TGORT_BY_NGORT = "SELECT WERKS, LGORT, LGNUM, LGTYP, LTYPT, IMWM  FROM [INV_CIC_DB].[dbo].[INV_VW_TGORT_BY_NGORT] WITH(NOLOCK) ";		
		String condition = buildCondition(tgortBean);		
		
		if(condition != null){
			INV_VW_TGORT_BY_NGORT += condition;			
		}
		
		INV_VW_TGORT_BY_NGORT += "GROUP BY WERKS, LGORT, LGNUM, LGTYP, LTYPT, IMWM ";
		
		log.info(INV_VW_TGORT_BY_NGORT);
		log.info("[getTgortWithNgortDao] Preparing sentence...");
		try {
			
			stm = con.prepareStatement(INV_VW_TGORT_BY_NGORT);		
			
			log.info("[getTgortWithNgortDao] Executing query...");
			
			ResultSet rs = stm.executeQuery();
			
			while (rs.next()){
				tgortBean = new TgortB();
				
				tgortBean.setWerks(rs.getString(1));
				tgortBean.setLgort(rs.getString(2));
				tgortBean.setLgNum(rs.getString(3));
				tgortBean.setLgTyp(rs.getString(4));
				tgortBean.setLtypt(rs.getString(5));
				tgortBean.setImwm(rs.getString(6));
				listTgortBean.add(tgortBean);
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
			log.info("[getTgortWithNgortDao] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[getTgortWithNgortDao] Some error occurred while was trying to execute the query: "+INV_VW_TGORT_BY_NGORT, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[getTgortWithNgortDao] Some error occurred while was trying to close the connection.", e);
			}
		}
		
		res.setAbstractResult(abstractResult);
		res.setLsObject(listTgortBean);
		return res;
	}
	
	public TgortB getLgTypByWerksAndLgort(TgortB tgortBean, Connection con){
		
		PreparedStatement stm = null;
		TgortB tgortBeanAux = null;
				 
		String INV_VW_TGORT_BY_NGORT = "SELECT WERKS, LGORT, LGNUM, LGTYP, LTYPT, IMWM  FROM [INV_CIC_DB].[dbo].[INV_VW_TGORT_BY_NGORT] WITH(NOLOCK) ";
		String condition = buildCondition(tgortBean);
		
		if(condition != null){
			INV_VW_TGORT_BY_NGORT += condition;
			
		}
		log.info(INV_VW_TGORT_BY_NGORT);
		log.info("[getLgTypByBukrsAndWerksDao] Preparing sentence...");
		try {
			
			stm = con.prepareStatement(INV_VW_TGORT_BY_NGORT);		
			
			log.info("[getLgTypByBukrsAndWerksDao] Executing query...");
			
			ResultSet rs = stm.executeQuery();
			
			while (rs.next()){
				
				tgortBeanAux = new TgortB();				
				tgortBeanAux.setWerks(rs.getString(1));
				tgortBeanAux.setLgort(rs.getString(2));
				tgortBeanAux.setLgNum(rs.getString(3));
				tgortBeanAux.setLgTyp(rs.getString(4));
				tgortBeanAux.setLtypt(rs.getString(5));
				tgortBeanAux.setImwm(rs.getString(6));
			}
			
			//Free resources
			rs.close();
			stm.close();
			log.info("[getLgTypByBukrsAndWerksDao] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[getLgTypByBukrsAndWerksDao] Some error occurred while was trying to execute the query: " + INV_VW_TGORT_BY_NGORT, e);
			return null;
		}
		return tgortBeanAux;
	}
	
	private String buildCondition(TgortB tgortB){
		String werks = "";
		String lgort = "";
		String lgNum = "";
		String lgTyp = "";
		String ltypt = "";
		String imwm = "";
		String condition = "";
		
		werks = (tgortB.getWerks() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ")+" WERKS = '"+ tgortB.getWerks() + "' "  : "";
		condition+=werks;
		lgort = (tgortB.getLgort() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ") + " LGORT = '" + tgortB.getLgort() +"' " : "";
		condition+=lgort;
		lgNum = (tgortB.getLgNum() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ") + " LGNUM = '" + tgortB.getLgNum() +"' " : "";
		condition+=lgNum;
		lgTyp = (tgortB.getLgTyp() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ") + " LGTYP = '" + tgortB.getLgTyp() +"' " : "";
		condition+= lgTyp;
		ltypt = (tgortB.getLtypt() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ") + " LTYPT = '" + tgortB.getLtypt() +"' " : "";
		condition+=ltypt;
		imwm = (tgortB.getImwm() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ") + " IMWM = '" + tgortB.getImwm() +"' " : "";
		condition+=imwm;
		condition = condition.isEmpty() ? null : condition;
		return condition;
	}

}
