package com.gmodelo.cyclicinventories.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.cyclicinventories.beans.AbstractResultsBean;
import com.gmodelo.cyclicinventories.beans.CatByMatnr;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.utils.ConnectionManager;
import com.gmodelo.cyclicinventories.utils.ReturnValues;

public class CatByMatnrDao {
	
	private Logger log = Logger.getLogger(CatByMatnrDao.class.getName());
	
	public Response<Object> saveCatByMatrnSAP(List<CatByMatnr> lsCatByMatnr, String userId) {

		Response<Object> resp = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		CallableStatement csBatch = null;	
		PreparedStatement stm = null;
		String lsMatnr = "";
		final String SP = "INV_SP_SAVE_CAT_BY_MATNR ?, ?, ?";		
		final String QUERY = "SELECT A.value FROM (SELECT * FROM STRING_SPLIT(?, ',') AS MATNR) AS A "
				+ " WHERE A.value NOT IN (SELECT SUBSTRING(MATNR, PATINDEX('%[^0 ]%', MATNR + ' '), LEN(MATNR)) FROM MAKT)";
		
		for(CatByMatnr matnr: lsCatByMatnr){
			
			lsMatnr += matnr.getMatnr() + ",";
		}
		
		lsMatnr = lsMatnr.substring(0, lsMatnr.length() -1);

		try {
			
			log.info(QUERY);
			log.info("Preparing sentence...");			
			stm = con.prepareStatement(QUERY);
			stm.setString(1, lsMatnr);
			
			log.info("Executing query...");
			ResultSet rs = stm.executeQuery();
			
			lsMatnr = "";
			
			while (rs.next()) {
				
				lsMatnr += rs.getString(1) + ", "; 
			}
			
			if(lsMatnr.trim().length() > 0){
				
				con.close();
				
				lsMatnr = lsMatnr.substring(0, lsMatnr.length() -2);
				
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs("Materiales no encontrados: " + lsMatnr + ".");
				resp.setAbstractResult(abstractResult);
				resp.setLsObject(lsMatnr);
				
				return resp;
			}
			
			con.setAutoCommit(false);
			csBatch = con.prepareCall(SP);

			for (CatByMatnr catByMatnr : lsCatByMatnr) {
				
				csBatch.setString(1, catByMatnr.getMatnr());
				csBatch.setInt(2, catByMatnr.getCatId());
				csBatch.setString(3, userId);
				csBatch.addBatch();				
			}

			log.info("[saveCatByMatrnSAP] Sentence successfully executed.");
			csBatch.executeBatch();
			con.commit();
			con.setAutoCommit(true);
			csBatch.close();

			log.info("[saveCatByMatrnSAP] Executing query...");

		} catch (Exception e) {

			try {
				log.log(Level.WARNING, "[saveCatByMatrnSAP] Execute rollback");
				con.rollback();
			} catch (SQLException e1) {
				log.log(Level.SEVERE, "[saveCatByMatrnSAP] Not rollback .", e);
			}

			log.log(Level.SEVERE,
					"[saveCatByMatrnSAP] Some error occurred while was trying to execute the S.P.: " + SP, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			resp.setAbstractResult(abstractResult);
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,
						"[saveCatByMatrnSAP] Some error occurred while was trying to close the connection.", e);
			}
		}

		resp.setAbstractResult(abstractResult);
		return resp;
	}

}
