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
import com.gmodelo.beans.TmatnrB;
import com.gmodelo.beans.TmatnrB;
import com.gmodelo.utils.ConnectionManager;
import com.gmodelo.utils.ReturnValues;

public class TmatnrDao {

	private Logger log = Logger.getLogger(TmatnrDao.class.getName());
	
	public Response<List<TmatnrB>> getTmatnrWithMatnr(TmatnrB tmatnrBean){
		
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		PreparedStatement stm = null;
		
		Response<List<TmatnrB>> res = new Response<List<TmatnrB>>();
		AbstractResults abstractResult = new AbstractResults();
		List<TmatnrB> listTmatnrBean = new ArrayList<TmatnrB>(); 
		 
		String INV_VW_TYPMATNR_BY_MATNR = "SELECT MATNR, TYP_MAT, DEN_TYP_MAT FROM [INV_CIC_DB].[dbo].[INV_VW_TYPMATNR_BY_MATNR] WITH(NOLOCK) ";
		
		String condition = buildCondition(tmatnrBean);
		if(condition != null){
			INV_VW_TYPMATNR_BY_MATNR += condition;
			log.warning(INV_VW_TYPMATNR_BY_MATNR);
		}
		log.log(Level.WARNING,"[getTmatnrWithMatnrDao] Preparing sentence...");
		try {
			
			stm = con.prepareStatement(INV_VW_TYPMATNR_BY_MATNR);		
			
			log.log(Level.WARNING,"[getTmatnrWithMatnrDao] Executing query...");
			
			ResultSet rs = stm.executeQuery();
			
			while (rs.next()){
				tmatnrBean = new TmatnrB();
				
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
	
	private String buildCondition(TmatnrB tmatnrBean){
		String matnr = "";
		String tmat = "";
		String dtmat = "";
		String condition = null;
		
		matnr = (tmatnrBean.getMatnr() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ")+" MATNR = '"+ tmatnrBean.getMatnr() + "' "  : "";
		condition+=matnr;
		tmat = (tmatnrBean.getTyp_mat() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ") + " TYP_MAT = '" + tmatnrBean.getTyp_mat() +"' " : "";
		condition+=tmat;
		dtmat = (tmatnrBean.getDen_typ_mat() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ") + " DEN_TYP_MAT = '" + tmatnrBean.getDen_typ_mat() +"' " : "";
		condition+=dtmat;
		
		return condition;
	}


}
