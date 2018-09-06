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
import com.gmodelo.beans.Response;
import com.gmodelo.beans.TmatnrBean;
import com.gmodelo.beans.MatnrBeanView;
import com.gmodelo.utils.ConnectionManager;
import com.gmodelo.utils.ReturnValues;

public class MatnrDao {
	
	private Logger log = Logger.getLogger( MatnrDao.class.getName());
	
	public Response<List<MatnrBeanView>> getMatnr(MatnrBeanView mantrBean, String searchFilter){
		
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		
		Response<List<MatnrBeanView>> res = new Response<List<MatnrBeanView>>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		List<MatnrBeanView> listMantrBean = new ArrayList<MatnrBeanView>(); 
		 
		String INV_VW_MATNR_BY_WERKS = "SELECT WERKS, MATNR, MAKTX  FROM [INV_CIC_DB].[dbo].[INV_VW_MATNR_BY_WERKS] WITH(NOLOCK) ";
		
		if(searchFilter != null){
			INV_VW_MATNR_BY_WERKS += " WHERE WERKS LIKE '%"+searchFilter+"%' OR MATNR LIKE '%"+searchFilter+"%' OR MAKTX LIKE '%"+searchFilter+"%' ";
		}else{
			String condition = buildCondition(mantrBean);
			if(condition != null){
				INV_VW_MATNR_BY_WERKS += condition;
				
			}
		}
		log.warning(INV_VW_MATNR_BY_WERKS);
		
		log.log(Level.WARNING,"[getMatnrDao] Preparing sentence...");
		try {
			
			stm = con.prepareStatement(INV_VW_MATNR_BY_WERKS);		
			
			log.log(Level.WARNING,"[getMatnrDao] Executing query...");
			
			ResultSet rs = stm.executeQuery();
			
			while (rs.next()){
				mantrBean = new MatnrBeanView();
				
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
			log.log(Level.WARNING,"[getMatnrDao] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[getMatnrDao] Some error occurred while was trying to execute the query: "+INV_VW_MATNR_BY_WERKS, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[getMatnrDao] Some error occurred while was trying to close the connection.", e);
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
	
	public Response<List<TmatnrBean>> getTmatnrWithMatnr(TmatnrBean tmatnrBean, String searchFilter){
		
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		
		Response<List<TmatnrBean>> res = new Response<List<TmatnrBean>>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		List<TmatnrBean> listTmatnrBean = new ArrayList<TmatnrBean>(); 
		 
		String INV_VW_TYPMATNR_BY_MATNR = "SELECT MATNR, TYP_MAT, DEN_TYP_MAT FROM [INV_CIC_DB].[dbo].[INV_VW_TYPMATNR_BY_MATNR] WITH(NOLOCK) ";
		
		if(searchFilter != null){
			INV_VW_TYPMATNR_BY_MATNR += "WHERE MATNR LIKE '%"+searchFilter+"%' OR TYP_MAT LIKE '%"+searchFilter+"%' OR DEN_TYP_MAT LIKE '%"+searchFilter+"%'";
		}else{
			String condition = buildCondition(tmatnrBean);
			if(condition != null){
				INV_VW_TYPMATNR_BY_MATNR += condition;
				log.warning(INV_VW_TYPMATNR_BY_MATNR);
			}
		}
		
		log.log(Level.WARNING,"[getTmatnrWithMatnrDao] Preparing sentence...");
		try {
			
			stm = con.prepareStatement(INV_VW_TYPMATNR_BY_MATNR);		
			
			log.log(Level.WARNING,"[getTmatnrWithMatnrDao] Executing query...");
			
			ResultSet rs = stm.executeQuery();
			
			while (rs.next()){
				tmatnrBean = new TmatnrBean();
				
				tmatnrBean.setMatnr(rs.getString(1));
				tmatnrBean.setTyp_mat(rs.getString(2));
				tmatnrBean.setDen_typ_mat(rs.getString(3));
				
				listTmatnrBean.add(tmatnrBean);
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
			log.log(Level.WARNING,"[getTmatnrWithMatnrDao] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[getTmatnrWithMatnrDao] Some error occurred while was trying to execute the query: "+INV_VW_TYPMATNR_BY_MATNR, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[getTmatnrWithMatnrDao] Some error occurred while was trying to close the connection.", e);
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs(e.getMessage());
				res.setAbstractResult(abstractResult);
				return res;
			}
		}
		
		res.setAbstractResult(abstractResult);
		res.setLsObject(listTmatnrBean);
		return res;
	}
	
	private String buildCondition(TmatnrBean tmatnrBean){
		String matnr = "";
		String tmat = "";
		String dtmat = "";
		String condition = "";
		
		matnr = (tmatnrBean.getMatnr() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ")+" MATNR LIKE '%"+ tmatnrBean.getMatnr() + "%'"  : "";
		condition+=matnr;
		tmat = (tmatnrBean.getTyp_mat() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ") + " TYP_MAT LIKE '%" + tmatnrBean.getTyp_mat() +"%' " : "";
		condition+=tmat;
		dtmat = (tmatnrBean.getDen_typ_mat() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ") + " DEN_TYP_MAT LIKE '%" + tmatnrBean.getDen_typ_mat() +"%' " : "";
		condition+=dtmat;
		condition = condition.isEmpty() ? null : condition;
		return condition;
	}
	
	private String buildCondition(MatnrBeanView mantrB){
		String werks = "";
		String matnr = "";
		String maktx = "";
		
		String condition = "";
		
		werks = (mantrB.getWerks() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ")+" WERKS LIKE '%"+ mantrB.getWerks() + "%' "  : "";
		condition+=werks;
		matnr = (mantrB.getMatnr() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ") + " MATNR LIKE '%" + mantrB.getMatnr() +"%' " : "";
		condition+=matnr;
		maktx = (mantrB.getMaktx() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ") + " MAKTX LIKE '%" + mantrB.getMaktx() +"%' " : "";
		condition+=maktx;
		condition = condition.isEmpty() ? null : condition;
		return condition;
	}


}
