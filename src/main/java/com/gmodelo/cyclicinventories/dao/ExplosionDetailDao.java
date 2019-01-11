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

				csBatch.setString(1, obj.getWerks());
				csBatch.setString(2, obj.getMatnr());
				csBatch.setString(3, obj.getComponent());
				csBatch.setString(4, obj.getUmb());
				csBatch.setByte(5, (byte) (obj.isRelevant()?1:0));
				csBatch.setString(6, user);
				csBatch.addBatch();
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

		final String GET_EXPL_DET = "SELECT  A.WERKS, A.MATNR, SUBSTRING(C.IDNRK, PATINDEX('%[^0 ]%', C.IDNRK + ' '), LEN(C.IDNRK)) AS MATNR_EXPL, " 
				+ "D.MAKTX, C.MEINS, 0 IS_RELEVANT "
				+ "FROM MAST AS A "
				+ "INNER JOIN STKO AS B ON (A.STLNR = B.STLNR) "
				+ "INNER JOIN STPO AS C ON (A.STLNR = C.STLNR) "
				+ "INNER JOIN INV_VW_MATNR_BY_WERKS AS D ON (C.IDNRK = D.MATNR) "
				+ "WHERE A.WERKS = ? AND A.MATNR = ? "
				+ "AND C.IDNRK NOT IN (SELECT EX_COMPONENT " 
				+ "FROM INV_EXPLOSION WHERE EX_WERKS = ? AND EX_MATNR = ?) "
				+ "UNION " 
				+ "SELECT A.EX_WERKS, A.EX_MATNR, A.EX_COMPONENT, B.MAKTX, A.EX_UMB, A.EX_RELEVANT "
				+ "FROM INV_EXPLOSION AS A "
				+ "INNER JOIN INV_VW_MATNR_BY_WERKS AS B ON (A.EX_COMPONENT = B.MATNR) "
				+ "WHERE EX_WERKS = ? AND EX_MATNR = ? "
				+ "AND EX_MATNR IN (SELECT SUBSTRING(C.IDNRK, PATINDEX('%[^0 ]%', C.IDNRK + ' '), LEN(C.IDNRK)) AS MATNR_EXPL "
				+ "FROM MAST AS A "
				+ "INNER JOIN STKO AS B ON (A.STLNR = B.STLNR) "
				+ "INNER JOIN STPO AS C ON (A.STLNR = C.STLNR) "
				+ "INNER JOIN INV_VW_MATNR_BY_WERKS AS D ON (C.IDNRK = D.MATNR) "
				+ "WHERE A.WERKS = ? AND A.MATNR = ?) ";

		log.info(GET_EXPL_DET);

		log.info("[getExplosionDetailByMatnr] Preparing sentence...");

		try {
			stm = con.prepareStatement(GET_EXPL_DET);
			stm.setString(1, ed.getWerks());
			stm.setString(2, ed.getMatnr());
			stm.setString(3, ed.getWerks());
			stm.setString(4, ed.getMatnr());
			stm.setString(5, ed.getWerks());
			stm.setString(6, ed.getMatnr());
			stm.setString(7, ed.getWerks());
			stm.setString(8, ed.getMatnr());
			
			log.info("[getExplosionDetailByMatnr] Executing query...");

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {

				ed = new ExplosionDetail();
				ed.setWerks(rs.getString("EX_WERKS"));
				ed.setMatnr(rs.getString("EX_MATNR"));
				ed.setComponent(rs.getString("EX_COMPONENT"));
				ed.setCompDesc(rs.getString("EX_MAKTX"));
				ed.setUmb(rs.getString("EX_UMB"));
				ed.setRelevant(rs.getBoolean("EX_RELEVANT"));
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
		final String GET_MST_EXPL_REP = "";

		log.info(GET_MST_EXPL_REP);
		log.info("[getExplosionReportByDocInv] Preparing sentence...");

		try {
			stm = con.prepareStatement(GET_MST_EXPL_REP);
			//stm.setString(1, ed.getWerks());
			
			log.info("[getExplosionReportByDocInv] Executing query...");

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {

				expl = new MatExplReport();
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
