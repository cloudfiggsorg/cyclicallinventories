package com.gmodelo.cyclicinventories.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.cyclicinventories.beans.AbstractResultsBean;
import com.gmodelo.cyclicinventories.beans.ExplosionDetail;
import com.gmodelo.cyclicinventories.beans.MatExplReport;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.utils.ConnectionManager;
import com.gmodelo.cyclicinventories.utils.ReturnValues;

public class ExplosionDetailDao {
	
	private Logger log = Logger.getLogger(ZoneDao.class.getName());
	
	public Response saveExplosionDetail(ArrayList<ExplosionDetail> ed, String user) {

		Response<?> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		CallableStatement cs = null;
		CallableStatement csBatch = null;	
		String currentSP = "";

		final String INV_SP_SAVE_EXPLOSION = "INV_SP_SAVE_EXPLOSION ?, ?, ?, ?, ?, ?";
		final String INV_SP_DEL_EXPLOSION = "INV_SP_DEL_EXPLOSION ?, ?";
		log.info("[saveExplosionDetail] Preparing sentence...");
		
		try {
			
			currentSP = INV_SP_DEL_EXPLOSION;
			
			con.setAutoCommit(false);

			cs = con.prepareCall(INV_SP_DEL_EXPLOSION);
			cs.setString(1, ed.get(0).getWerks());
			cs.setString(2, ed.get(0).getMatnr());

			log.info("Executing " + currentSP);

			cs.execute();

			currentSP = INV_SP_SAVE_EXPLOSION;
			csBatch = null;
			csBatch = con.prepareCall(INV_SP_SAVE_EXPLOSION);

			for (ExplosionDetail obj : ed) {
				
				if(obj.isRelevant()){
					
					csBatch.setString(1, obj.getWerks());
					csBatch.setString(2, obj.getMatnr());
					csBatch.setString(3, obj.getComponent());
					csBatch.setString(4, obj.getUmb());
					csBatch.setByte(5, (byte) (obj.isRelevant()?1:0));
					csBatch.setString(6, user);
					csBatch.addBatch();					
				}

			}
			log.info("[saveExplosionDetail] Executing batch..." + csBatch.executeBatch());

			// Retrive the warnings if there're
			SQLWarning warning = cs.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING, warning.getMessage());
				warning = warning.getNextWarning();
			}

			con.commit();
			// Free resources
			cs.close();

			log.info("[saveExplosionDetail] Sentence successfully executed.");

		} catch (SQLException e) {
			try {
				log.log(Level.WARNING, "[saveExplosionDetail] Execute rollback");
				con.rollback();
			} catch (SQLException e1) {
				log.log(Level.SEVERE, "[saveExplosionDetail] Not rollback .", e);
			}

			log.log(Level.SEVERE, "[saveExplosionDetail] Some error occurred while was trying to execute the S.P.: " + currentSP,
					e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			res.setAbstractResult(abstractResult);
			return res;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE, "[saveExplosionDetail] Some error occurred while was trying to close the connection.", e);
			}
		}
		res.setAbstractResult(abstractResult);
		return res;
	}
	
	public Response<ArrayList<ExplosionDetail>> getExplosionDetailByMatnr(ExplosionDetail ed) {
		
		Response<ArrayList<ExplosionDetail>> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();		
		PreparedStatement stm = null;		
		ArrayList<ExplosionDetail> lsEd = new ArrayList<>();

		final String GET_EXPL_DET = "SELECT  A.WERKS, SUBSTRING(A.MATNR, PATINDEX('%[^0 ]%', A.MATNR + ' '), LEN(A.MATNR)) AS MATNR, "
				+ "SUBSTRING(C.IDNRK, PATINDEX('%[^0 ]%', C.IDNRK + ' '), LEN(C.IDNRK)) AS MATNR_EXPL, " 
				+ "D.MAKTX, C.MEINS, 0 IS_RELEVANT " 
				+ "FROM MAST AS A " 
					+ "INNER JOIN STKO AS B ON (A.STLNR = B.STLNR) " 
					+ "INNER JOIN STPO AS C ON (A.STLNR = C.STLNR) " 
					+ "INNER JOIN INV_VW_MATNR_BY_WERKS AS D ON (SUBSTRING(C.IDNRK, PATINDEX('%[^0 ]%', C.IDNRK + ' '), LEN(C.IDNRK)) = D.MATNR) " 
				+ "WHERE A.WERKS = ? AND SUBSTRING(A.MATNR, PATINDEX('%[^0 ]%', A.MATNR + ' '), LEN(A.MATNR)) = ? " 
					+ "AND D.WERKS = ? "
					+ "AND SUBSTRING(C.IDNRK, PATINDEX('%[^0 ]%', C.IDNRK + ' '), LEN(C.IDNRK)) NOT IN (SELECT EX_COMPONENT " 
					+ "FROM INV_EXPLOSION WHERE EX_WERKS = ? AND SUBSTRING(EX_MATNR, PATINDEX('%[^0 ]%', EX_MATNR + ' '), LEN(EX_MATNR)) = ?) " 

				+ "UNION " 

				+ "SELECT A.EX_WERKS, A.EX_MATNR, A.EX_COMPONENT, B.MAKTX, A.EX_UMB, A.EX_RELEVANT " 
				+ "FROM INV_EXPLOSION AS A " 
					+ "INNER JOIN INV_VW_MATNR_BY_WERKS AS B ON (A.EX_COMPONENT = B.MATNR) " 
				+ "WHERE EX_WERKS = ? " 
					+ "AND B.WERKS = ? "
					+ "AND SUBSTRING(EX_MATNR, PATINDEX('%[^0 ]%', EX_MATNR + ' '), LEN(EX_MATNR))  = ? " 
					+ "AND A.EX_COMPONENT IN (SELECT SUBSTRING(C.IDNRK, PATINDEX('%[^0 ]%', C.IDNRK + ' '), LEN(C.IDNRK)) " 
						+ "FROM MAST AS A " 
							+ "INNER JOIN STKO AS B ON (A.STLNR = B.STLNR) " 
							+ "INNER JOIN STPO AS C ON (A.STLNR = C.STLNR) " 
							+ "INNER JOIN INV_VW_MATNR_BY_WERKS AS D ON (SUBSTRING(C.IDNRK, PATINDEX('%[^0 ]%', C.IDNRK + ' '), LEN(C.IDNRK)) = D.MATNR) " 
						+ "WHERE A.WERKS = ? " 
						+ "AND SUBSTRING(A.MATNR, PATINDEX('%[^0 ]%', A.MATNR + ' '), LEN(A.MATNR)) = ? "
						+ "AND D.WERKS = ?) ";

		log.info(GET_EXPL_DET);

		log.info("[getExplosionDetailByMatnr] Preparing sentence...");

		try {
			stm = con.prepareStatement(GET_EXPL_DET);
			stm.setString(1, ed.getWerks());
			stm.setString(2, ed.getMatnr());
			stm.setString(3, ed.getWerks());			
			stm.setString(4, ed.getWerks());
			stm.setString(5, ed.getMatnr());	
			
			stm.setString(6, ed.getWerks());
			stm.setString(7, ed.getWerks());
			stm.setString(8, ed.getMatnr());
			stm.setString(9, ed.getWerks());
			stm.setString(10, ed.getMatnr());
			stm.setString(11, ed.getWerks());
			
			log.info("[getExplosionDetailByMatnr] Executing query...");

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {

				ed = new ExplosionDetail();
				ed.setWerks(rs.getString("WERKS"));
				ed.setMatnr(rs.getString("MATNR"));
				ed.setComponent(rs.getString("MATNR_EXPL"));				
				ed.setCompDesc(rs.getString("MAKTX"));
				ed.setUmb(rs.getString("MEINS"));
				ed.setRelevant(rs.getBoolean("IS_RELEVANT"));
				lsEd.add(ed);
			}

			// Retrive the warnings if there're
			SQLWarning warning = stm.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING, warning.getMessage());
				warning = warning.getNextWarning();
			}

			// Free resources
			rs.close();
			stm.close();

			log.info("[getExplosionDetailByMatnr] Sentence successfully executed.");
						
			res.setAbstractResult(abstractResult);
			res.setLsObject(lsEd);			
		} catch (SQLException e) {
			log.log(Level.SEVERE,
					"[getExplosionDetailByMatnr] Some error occurred while was trying to execute the query: " + GET_EXPL_DET,
					e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			res.setAbstractResult(abstractResult);
			
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				
				log.log(Level.SEVERE, "[getExplosionDetailByMatnr] Some error occurred while was trying to close the connection.", e);
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				res.setAbstractResult(abstractResult);
			}
		}
		
		return res;
	}
	
	public Response<ArrayList<MatExplReport>> getExplosionReportByDocInv(int docInvId) {
		
		Response<ArrayList<MatExplReport>> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();		
		PreparedStatement stm = null;		
		MatExplReport expl = new MatExplReport();
		ArrayList<MatExplReport> lsDetail = new ArrayList<>();
		final String GET_MST_EXPL_REP = "SELECT A.CS_MATNR, E.MAKTX, A.CS_UM, A.CS_COUNTED, D.IDNRK, D.MEINS, " 
				+ "(SELECT MAKTX FROM MAKT WHERE MATNR = D.IDNRK) AS MATNR_EXPL_DESC, " 
				+ "(CAST(D.MENGE AS decimal(10, 2)) / CAST(C.BMENG AS decimal(10, 2))) * CAST(REPLACE(A.CS_COUNTED, ',', '') AS decimal(10, 2)) AS QUANTITY " 
			+ "FROM INV_CONS_SAP AS A " 
				+ "LEFT JOIN MAST AS B ON (A.CS_MATNR = SUBSTRING(B.MATNR, PATINDEX('%[^0 ]%', B.MATNR + ' '), LEN(B.MATNR))) " 
				+ "INNER JOIN STKO AS C ON (B.STLNR = C.STLNR) " 
				+ "INNER JOIN STPO AS D ON (C.STLNR = D.STLNR) " 
				+ "INNER JOIN INV_DOC_INVENTORY_HEADER AS IDIH ON (IDIH.DOC_INV_ID = A.CS_DOC_INV_ID) " 
				+ "INNER JOIN MAKT AS E ON (A.CS_MATNR = SUBSTRING(E.MATNR, PATINDEX('%[^0 ]%', E.MATNR + ' '), LEN(E.MATNR))) "
			+ "WHERE CS_DOC_INV_ID = ? " 
				+ "AND B.WERKS = IDIH.DIH_WERKS " 
				+ "AND SUBSTRING(D.IDNRK, PATINDEX('%[^0 ]%', D.IDNRK + ' '), LEN(D.IDNRK)) IN (SELECT EX_COMPONENT FROM INV_EXPLOSION WHERE EX_WERKS = IDIH.DIH_WERKS AND A.CS_MATNR = EX_MATNR AND EX_RELEVANT = 1) " 
			+"GROUP BY CS_MATNR, E.MAKTX, CS_UM, CS_COUNTED, D.IDNRK, D.MEINS, D.MENGE, C.BMENG ";

		log.info(GET_MST_EXPL_REP);
		log.info("[getExplosionReportByDocInv] Preparing sentence...");

		try {
			stm = con.prepareStatement(GET_MST_EXPL_REP);
			stm.setInt(1, docInvId);
			log.info("[getExplosionReportByDocInv] Executing query...");

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {

				expl = new MatExplReport();
				expl.setMatnr(rs.getString("CS_MATNR"));
				expl.setDescription(rs.getString("MAKTX"));
				expl.setUmb(rs.getString("CS_UM"));
				expl.setCounted(rs.getString("CS_COUNTED"));
				expl.setMatnrExpl(rs.getString("IDNRK"));
				expl.setDescMantrExpl(rs.getString("MATNR_EXPL_DESC"));
				expl.setUmbExpl(rs.getString("MEINS"));
				expl.setQuantity(rs.getString("QUANTITY"));
				lsDetail.add(expl);
			}

			// Retrive the warnings if there're
			SQLWarning warning = stm.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING, warning.getMessage());
				warning = warning.getNextWarning();
			}

			// Free resources
			rs.close();
			stm.close();

			log.info("[getExplosionReportByDocInv] Sentence successfully executed.");
						
			res.setAbstractResult(abstractResult);
			res.setLsObject(lsDetail);			
		} catch (SQLException e) {
			log.log(Level.SEVERE,
					"[getExplosionReportByDocInv] Some error occurred while was trying to execute the query: " + GET_MST_EXPL_REP,
					e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			res.setAbstractResult(abstractResult);
			
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				
				log.log(Level.SEVERE, "[getExplosionReportByDocInv] Some error occurred while was trying to close the connection.", e);
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				res.setAbstractResult(abstractResult);
			}
		}
		
		return res;
	}
		
}
