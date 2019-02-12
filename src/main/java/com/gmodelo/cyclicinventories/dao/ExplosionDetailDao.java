package com.gmodelo.cyclicinventories.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
		
		/////////////////////////INICIO VALIDACIONES ANTES DE GUARDAR//////////////////////////////////////
		//GENERANDO LISTA DE LGORTS UNICOS
		List<String> uniqueLgortList = new ArrayList<>();
		for(ExplosionDetail item : ed){
			if(uniqueLgortList.isEmpty() && item.isRelevant()){
				uniqueLgortList.add(item.getLgort());
			}else{
				boolean existLgort = false;
				for(String l : uniqueLgortList){
					if(l.equalsIgnoreCase(item.getLgort()) && item.isRelevant()){
						existLgort = true;
						break;
					}
				}
				if(!existLgort && item.isRelevant()){
					uniqueLgortList.add(item.getLgort());
				}
			}
		}
		//Obteniendo el universo de lgorts para el centro dado
		final String  LGORT_BY_WERK = "SELECT DISTINCT LGORT FROM dbo.T001L WITH (NOLOCK) WHERE WERKS = ? ";
		List<String> lgortsList = new ArrayList<>();
		try {
			PreparedStatement stm = con.prepareStatement(LGORT_BY_WERK);
			stm.setString(1, ed.get(0).getWerks());
			
			ResultSet rs = stm.executeQuery();

			while (rs.next()){
				lgortsList.add(rs.getString("LGORT"));
			}
		} catch (SQLException e) {
			log.log(Level.SEVERE, "[saveExplosionDetail] Ocurrió un error al buscar la lista de almacenes para validar información, query: "+LGORT_BY_WERK, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}
//		verificando que la consulta no venga vacia
		if(lgortsList.isEmpty()){
			log.log(Level.SEVERE, "[saveExplosionDetail] No existen almacenes para el centro "+ed.get(0).getWerks());
			abstractResult.setResultId(ReturnValues.IERROR);
			abstractResult.setResultMsgAbs("No existen almacenes para el centro "+ed.get(0).getWerks());
			res.setAbstractResult(abstractResult);
			return res;
		}
//		Validando que el lgort ingresado exista en el centro
		String msg = "Almacenes incorrectos para centro  "+ed.get(0).getWerks()+" : ";
		int size = msg.length();
		boolean validateError = false;
		for(String lgortToValidate : uniqueLgortList){
			if(!lgortsList.contains(lgortToValidate)){
				validateError = true;
				if(msg.length() > size){
					msg+= ", "+lgortToValidate;
				}else{
					msg+= lgortToValidate;
				}
				
			}
		}
		if(validateError){
			abstractResult.setResultId(ReturnValues.IERROR);
			abstractResult.setResultMsgAbs(msg);
			res.setAbstractResult(abstractResult);
			return res;
		}
		
		//////////////////////////FIN VALIDACIONES ANTES DE GUARDAR///////////////////////////////////
		

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
					csBatch.setByte(4, (byte) (obj.isRelevant()?1:0));
					csBatch.setString(5, obj.getLgort());
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
				+ "D.MAKTX, C.MEINS, 0 IS_RELEVANT, '' LGORT " 
				+ "FROM MAST AS A " 
					+ "INNER JOIN STKO AS B ON (A.STLNR = B.STLNR) " 
					+ "INNER JOIN STPO AS C ON (A.STLNR = C.STLNR) " 
					+ "INNER JOIN INV_VW_MATNR_BY_WERKS AS D ON (SUBSTRING(C.IDNRK, PATINDEX('%[^0 ]%', C.IDNRK + ' '), LEN(C.IDNRK)) = D.MATNR) " 
				+ "WHERE A.WERKS = ? AND SUBSTRING(A.MATNR, PATINDEX('%[^0 ]%', A.MATNR + ' '), LEN(A.MATNR)) = ? " 
					+ "AND D.WERKS = ? "
					+ "AND SUBSTRING(C.IDNRK, PATINDEX('%[^0 ]%', C.IDNRK + ' '), LEN(C.IDNRK)) NOT IN (SELECT EX_COMPONENT " 
					+ "FROM INV_EXPLOSION WHERE EX_WERKS = ? AND SUBSTRING(EX_MATNR, PATINDEX('%[^0 ]%', EX_MATNR + ' '), LEN(EX_MATNR)) = ?) " 

				+ "UNION " 

				+ "SELECT A.EX_WERKS, A.EX_MATNR, A.EX_COMPONENT, B.MAKTX, C.MEINS, A.EX_RELEVANT, A.EX_LGORT LGORT " 
				+ "FROM INV_EXPLOSION AS A " 
					+ "INNER JOIN INV_VW_MATNR_BY_WERKS AS B ON (A.EX_COMPONENT = B.MATNR) " 
					+ "INNER JOIN MARA AS C ON (SUBSTRING(C.MATNR, PATINDEX('%[^0 ]%', C.MATNR + ' '), LEN(C.MATNR)) = A.EX_COMPONENT) "
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
				ed.setLgort(rs.getString("LGORT"));
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
	
	public Response<ArrayList<MatExplReport>> getExplosionReportByWerks(int docInvId) {
		
		Response<ArrayList<MatExplReport>> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();		
		PreparedStatement stm = null;		
		MatExplReport expl = new MatExplReport();
		ArrayList<MatExplReport> lsDetail = new ArrayList<>();
		HashMap<String, ArrayList<MatExplReport>> map = new HashMap<>(); 
		
		final String GET_MST_EXPL_REP = "SELECT A.MATNR, E.MAKTX, ISNULL(G.CATEGORY, ' ') CATEGORY, A.MEINS, A.COUNTED, " 
				+ "SUBSTRING(D.IDNRK, PATINDEX('%[^0 ]%', D.IDNRK + ' '), LEN(D.IDNRK)) IDNRK, D.MEINS MEINS_EXLP, " 
				+ "(SELECT MAKTX FROM MAKT WHERE MATNR = D.IDNRK) AS MATNR_EXPL_DESC, " 
				+ "CAST((CAST(D.MENGE AS decimal(20, 3)) / CAST(C.BMENG AS decimal(15, 2))) * CAST(REPLACE(A.COUNTED, ',', '') AS decimal(20, 3)) AS varchar(50)) AS QUANTITY, " 
				+ "ISNULL((SELECT CATEGORY FROM INV_CAT_CATEGORY AS ICC " 
						+ "INNER JOIN INV_REL_CAT_MAT AS IRCM ON (ICC.CAT_ID = IRCM.REL_CAT_ID) " 
						+ "WHERE REL_MATNR = SUBSTRING(D.IDNRK, PATINDEX('%[^0 ]%', D.IDNRK + ' '), LEN(D.IDNRK))), '') AS CAT_EXPL " 
			+ "FROM (SELECT DIP_MATNR MATNR, DIP_DOC_INV_ID, SUM(CAST(DIP_COUNTED AS decimal(20, 3))) COUNTED, " 
				+ "(SELECT MEINS FROM MARA WHERE SUBSTRING(MATNR, PATINDEX('%[^0 ]%', MATNR + ' '), LEN(MATNR)) = DIP_MATNR) MEINS " 
				+ "FROM INV_DOC_INVENTORY_POSITIONS " 
				+ "WHERE DIP_DOC_INV_ID = ? " 
			+ "GROUP BY DIP_DOC_INV_ID, DIP_MATNR) AS A " 
				+ "LEFT JOIN MAST AS B ON (A.MATNR = SUBSTRING(B.MATNR, PATINDEX('%[^0 ]%', B.MATNR + ' '), LEN(B.MATNR))) " 
				+ "INNER JOIN STKO AS C ON (B.STLNR = C.STLNR) "  
				+ "INNER JOIN STPO AS D ON (C.STLNR = D.STLNR) " 
				+ "INNER JOIN INV_DOC_INVENTORY_HEADER AS IDIH ON (IDIH.DOC_INV_ID = A.DIP_DOC_INV_ID) " 
				+ "INNER JOIN MAKT AS E ON (A.MATNR = SUBSTRING(E.MATNR, PATINDEX('%[^0 ]%', E.MATNR + ' '), LEN(E.MATNR))) " 
				+ "LEFT JOIN INV_REL_CAT_MAT AS F ON (F.REL_MATNR = A.MATNR) " 
				+ "LEFT JOIN INV_CAT_CATEGORY AS G ON (F.REL_CAT_ID = G.CAT_ID) " 
					+ "WHERE DIP_DOC_INV_ID = ? " 
						+ "AND B.WERKS = IDIH.DIH_WERKS " 
						+ "AND SUBSTRING(D.IDNRK, PATINDEX('%[^0 ]%', D.IDNRK + ' '), LEN(D.IDNRK)) " 
							+ "IN (SELECT EX_COMPONENT " 
								+ "FROM INV_EXPLOSION WHERE EX_WERKS = IDIH.DIH_WERKS " 
								+ "AND A.MATNR = EX_MATNR AND EX_RELEVANT = 1) " 
			+ "GROUP BY A.MATNR, E.MAKTX, G.CATEGORY, A.MEINS, COUNTED, D.IDNRK, D.MEINS, D.MENGE, C.BMENG "

			+ "UNION "

			+ "SELECT DIP_MATNR, B.MAKTX, ISNULL(D.CATEGORY, '') CATEGORY, " 
				+ "E.MEINS, DIP_COUNTED, '' IDNRK, '' MEINS_EXPL, '' DESC_MATNR_EXPL, '' QUANTITY, '' CAT_EXPL "
			+ "FROM INV_DOC_INVENTORY_POSITIONS AS A "
			+ "INNER JOIN MAKT AS B ON (SUBSTRING(B.MATNR, PATINDEX('%[^0 ]%', B.MATNR + ' '), LEN(B.MATNR)) = A.DIP_MATNR) "
			+ "LEFT JOIN INV_REL_CAT_MAT AS C ON (C.REL_MATNR = A.DIP_MATNR) " 
			+ "LEFT JOIN INV_CAT_CATEGORY AS D ON (C.REL_CAT_ID = D.CAT_ID) " 
			+ "INNER JOIN MARA AS E ON (SUBSTRING(E.MATNR, PATINDEX('%[^0 ]%', E.MATNR + ' '), LEN(E.MATNR)) = A.DIP_MATNR) "
			+ "WHERE DIP_DOC_INV_ID = ? "
			+ "ORDER BY A.MATNR, IDNRK ASC ";

		log.info(GET_MST_EXPL_REP);
		log.info("[getExplosionReportByDocInv] Preparing sentence...");

		try {
			
			//Get the VHILM by doc inventory
			ArrayList<MatExplReport> lsVHILM = getVHILMByWerks(docInvId, con);
			
			stm = con.prepareStatement(GET_MST_EXPL_REP);
			stm.setInt(1, docInvId);
			stm.setInt(2, docInvId);
			stm.setInt(3, docInvId);
			log.info("[getExplosionReportByDocInv] Executing query...");

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				
				expl = new MatExplReport();
				expl.setMatnr(rs.getString("MATNR"));
				expl.setDescription(rs.getString("MAKTX"));
				expl.setCategory(rs.getString("CATEGORY"));
				expl.setUmb(rs.getString("MEINS"));
				expl.setCounted(rs.getString("COUNTED"));
				expl.setMatnrExpl(rs.getString("IDNRK"));
				expl.setDescMantrExpl(rs.getString("MATNR_EXPL_DESC"));
				expl.setCatExpl(rs.getString("CAT_EXPL"));
				expl.setUmbExpl(rs.getString("MEINS_EXLP"));
				expl.setQuantity(rs.getString("QUANTITY"));
				
				if(map.containsKey(expl.getMatnr())){
					map.get(expl.getMatnr()).add(expl);					
				}else{
					ArrayList<MatExplReport> lsMatnr = new ArrayList<>();					
					lsMatnr.add(expl);					
					map.put(expl.getMatnr(), lsMatnr);					
				}				
			}
			
			for(MatExplReport matnr: lsVHILM){
				
				if(map.containsKey(matnr.getMatnr())){
					map.get(matnr.getMatnr()).add(matnr);
					
				}else{
					
					ArrayList<MatExplReport> lsMatnr = new ArrayList<>();
					lsMatnr.add(matnr);
					map.put(matnr.getMatnr(), lsMatnr);
				}
			}
									
			//Iterate the map and create the final list
			for (ArrayList<MatExplReport> lsMatnr : map.values()) {
			    			
				lsDetail.addAll(lsMatnr);
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
	
	private ArrayList<MatExplReport> getVHILMByWerks(int docInvId, Connection con) throws SQLException {
		
		ArrayList<MatExplReport> lsVHILM = new ArrayList<>();
		final String QUERY = "SELECT A.MATNR, A.COU_VHILM, MEINS, A.MAKTX, SUM(COUNTED) COUNTED, "
				+ "ISNULL((SELECT CATEGORY FROM INV_CAT_CATEGORY AS ICC "
				+ "INNER JOIN INV_REL_CAT_MAT AS IRCM ON (ICC.CAT_ID = IRCM.REL_CAT_ID) " 
				+ "WHERE REL_MATNR = SUBSTRING(A.MATNR, PATINDEX('%[^0 ]%', A.MATNR + ' '), LEN(A.MATNR))), '') AS CAT_EXPL "
				+ "FROM (SELECT SUBSTRING(A.COU_MATNR, PATINDEX('%[^0 ]%', A.COU_MATNR + ' '), LEN(A.COU_MATNR)) MATNR, COU_VHILM, MEINS, " 
				+ "MAKTX, CASE WHEN A.COU_VHILM_COUNT IS NULL THEN 0 "  
				+ "WHEN LEN(A.COU_VHILM_COUNT) = 0 THEN 0 "
				+ "ELSE CAST(A.COU_VHILM_COUNT AS decimal(20,3)) END COUNTED "
				+ "FROM INV_COUNT AS A "
				+ "INNER JOIN INV_TASK AS B ON (A.COU_TASK_ID = B.TASK_ID) "
				+ "INNER JOIN MAKT AS C ON (A.COU_VHILM = SUBSTRING(C.MATNR, PATINDEX('%[^0 ]%', C.MATNR + ' '), LEN(C.MATNR))) "
				+ "INNER JOIN MARA AS F ON (A.COU_VHILM = SUBSTRING(F.MATNR, PATINDEX('%[^0 ]%', F.MATNR + ' '), LEN(F.MATNR))) " 
				+ "WHERE B.TAS_DOC_INV_ID = ? "
				+ "AND COU_VHILM IS NOT NULL "
				+ "GROUP BY COU_MATNR, COU_VHILM, MEINS, COU_VHILM_COUNT, MAKTX) AS A " 
				+ "GROUP BY A.MATNR, A.COU_VHILM, MEINS, A.MAKTX";  
		PreparedStatement stm = null;	
		MatExplReport matnrRec = null;
		stm = con.prepareStatement(QUERY);
		stm.setInt(1, docInvId);
		log.info("[getVHILM] Executing query...");

		ResultSet rs = stm.executeQuery();

		while (rs.next()) {

			matnrRec = new MatExplReport();
			matnrRec.setMatnr(rs.getString("MATNR"));
			matnrRec.setMatnrExpl(rs.getString("COU_VHILM"));
			matnrRec.setCatExpl(rs.getString("CAT_EXPL"));			
			matnrRec.setUmbExpl(rs.getString("MEINS"));
			matnrRec.setDescMantrExpl(rs.getString("MAKTX"));			
			matnrRec.setQuantity(rs.getString("COUNTED"));			
			lsVHILM.add(matnrRec);
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
		
		return lsVHILM;		
	}
	
	public Response<ArrayList<MatExplReport>> getExplosionReportByLgpla(int docInvId) {
		
		Response<ArrayList<MatExplReport>> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();		
		PreparedStatement stm = null;		
		MatExplReport expl = new MatExplReport();
		ArrayList<MatExplReport> lsDetail = new ArrayList<>();
		HashMap<String, ArrayList<MatExplReport>> map = new HashMap<>(); 
		String key = "";
		
		final String GET_MST_EXPL_REP = "SELECT DIP_LGORT, LGTYP, LTYPT, A.LGPLA, A.MATNR, A.MAKTX, ISNULL(G.CATEGORY, ' ') CATEGORY, A.MEINS, A.COUNTED, " 
				+ "SUBSTRING(D.IDNRK, PATINDEX('%[^0 ]%', D.IDNRK + ' '), LEN(D.IDNRK)) IDNRK, D.MEINS MEINS_EXLP, " 
				+ "(SELECT MAKTX FROM MAKT WHERE MATNR = D.IDNRK) AS MATNR_EXPL_DESC, " 
				+ "CAST((CAST(D.MENGE AS decimal(20, 3)) / CAST(C.BMENG AS decimal(20, 3))) * CAST(REPLACE(A.COUNTED, ',', '') AS decimal(20, 3)) AS varchar(30)) AS QUANTITY, "
				+ "ISNULL((SELECT CATEGORY FROM INV_CAT_CATEGORY AS ICC " 
						+ "INNER JOIN INV_REL_CAT_MAT AS IRCM ON (ICC.CAT_ID = IRCM.REL_CAT_ID) " 
						+ "WHERE REL_MATNR = SUBSTRING(D.IDNRK, PATINDEX('%[^0 ]%', D.IDNRK + ' '), LEN(D.IDNRK))), '') AS CAT_EXPL, H.EX_LGORT " 		 
			+ "FROM (SELECT DIP_LGORT, LGTYP, LTYPT, DIP_LGPLA LGPLA, DIP_MATNR MATNR, MAKTX, DOC_INV_ID, " 
					+ "SUM(CAST(DIP_COUNTED AS decimal(20,3))) COUNTED, MEINS "
				+ "FROM INV_VW_DOC_INV_REP_LGORT_LGPLA " 
				+ "WHERE DOC_INV_ID = ? "
				+ "GROUP BY DIP_LGORT, LGTYP, LTYPT, DIP_LGPLA, DIP_MATNR, MAKTX, DOC_INV_ID, DIP_MATNR, IMWM, MEINS) AS A " 
				+ "LEFT JOIN MAST AS B ON (A.MATNR = SUBSTRING(B.MATNR, PATINDEX('%[^0 ]%', B.MATNR + ' '), LEN(B.MATNR))) " 
				+ "INNER JOIN STKO AS C ON (B.STLNR = C.STLNR) "  
				+ "INNER JOIN STPO AS D ON (C.STLNR = D.STLNR) " 
				+ "INNER JOIN INV_DOC_INVENTORY_HEADER AS IDIH ON (IDIH.DOC_INV_ID = A.DOC_INV_ID) " 
				+ "INNER JOIN MAKT AS E ON (A.MATNR = SUBSTRING(E.MATNR, PATINDEX('%[^0 ]%', E.MATNR + ' '), LEN(E.MATNR))) " 
				+ "LEFT JOIN INV_REL_CAT_MAT AS F ON (F.REL_MATNR = A.MATNR) " 
				+ "LEFT JOIN INV_CAT_CATEGORY AS G ON (F.REL_CAT_ID = G.CAT_ID) "
				+ "INNER JOIN INV_EXPLOSION AS H ON (H.EX_MATNR = A.MATNR AND H.EX_WERKS = IDIH.DIH_WERKS) " 
					+ "WHERE A.DOC_INV_ID = ? " 
						+ "AND B.WERKS = IDIH.DIH_WERKS " 
						+ "AND SUBSTRING(D.IDNRK, PATINDEX('%[^0 ]%', D.IDNRK + ' '), LEN(D.IDNRK)) " 
							+ "IN (SELECT EX_COMPONENT " 
								+ "FROM INV_EXPLOSION WHERE EX_WERKS = IDIH.DIH_WERKS " 
								+ "AND A.MATNR = EX_MATNR AND EX_RELEVANT = 1) " 
			+ "GROUP BY DIP_LGORT, LGTYP, LTYPT, LGPLA, A.MATNR, A.MAKTX, G.CATEGORY, A.MEINS, COUNTED, D.IDNRK, D.MEINS, D.MENGE, C.BMENG, H.EX_LGORT "	 

			+ "UNION "

			+ "SELECT DIP_LGORT, LGTYP, LTYPT, DIP_LGPLA LGPLA, DIP_MATNR MATNR, A.MAKTX, ISNULL(D.CATEGORY, '') CATEGORY, " 
			+ "A.MEINS, DIP_COUNTED, '' IDNRK, '' MEINS_EXPL, '' DESC_MATNR_EXPL, '' QUANTITY, '' CAT_EXPL, '' LGORT_EXPL "
			+ "FROM INV_VW_DOC_INV_REP_LGORT_LGPLA AS A "
			+ "INNER JOIN MAKT AS B ON (SUBSTRING(B.MATNR, PATINDEX('%[^0 ]%', B.MATNR + ' '), LEN(B.MATNR)) = A.DIP_MATNR) "
			+ "LEFT JOIN INV_REL_CAT_MAT AS C ON (C.REL_MATNR = A.DIP_MATNR) " 
			+ "LEFT JOIN INV_CAT_CATEGORY AS D ON (C.REL_CAT_ID = D.CAT_ID) " 
			+ "INNER JOIN MARA AS E ON (SUBSTRING(E.MATNR, PATINDEX('%[^0 ]%', E.MATNR + ' '), LEN(E.MATNR)) = A.DIP_MATNR) "
			+ "WHERE DOC_INV_ID = ? ";

		log.info(GET_MST_EXPL_REP);
		log.info("[getExplosionReportByDocInv] Preparing sentence...");

		try {
			
			//Get the VHILM by doc inventory
			ArrayList<MatExplReport> lsVHILM = getVHILMByLgpla(docInvId, con);
			
			stm = con.prepareStatement(GET_MST_EXPL_REP);
			stm.setInt(1, docInvId);
			stm.setInt(2, docInvId);
			stm.setInt(3, docInvId);
			log.info("[getExplosionReportByDocInv] Executing query...");

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				
				expl = new MatExplReport();
				expl.setLgort(rs.getString("DIP_LGORT"));
				
				try {
					expl.setLgtyp(rs.getString("LGTYP"));
				} catch (Exception e) {
					expl.setLgtyp("");
				}
				try {
					expl.setLtypt(rs.getString("LTYPT"));
				} catch (Exception e) {
					expl.setLgtyp("");
				}

				try {
					expl.setLgpla(rs.getString("LGPLA"));
				} catch (Exception e) {
					expl.setLgpla("");
				}

				expl.setMatnr(rs.getString("MATNR"));
				expl.setDescription(rs.getString("MAKTX"));
				expl.setCategory(rs.getString("CATEGORY"));
				expl.setUmb(rs.getString("MEINS"));
				expl.setCounted(rs.getString("COUNTED"));
				expl.setMatnrExpl(rs.getString("IDNRK"));
				expl.setDescMantrExpl(rs.getString("MATNR_EXPL_DESC"));
				expl.setLgortExpl(rs.getString("EX_LGORT"));
				expl.setCatExpl(rs.getString("CAT_EXPL"));
				expl.setUmbExpl(rs.getString("MEINS_EXLP"));
				expl.setQuantity(rs.getString("QUANTITY"));
				
				key = expl.getLgort() + expl.getLgtyp() + expl.getLtypt() + expl.getLgpla() + expl.getMatnr(); 
								
				if(map.containsKey(key)){
					map.get(key).add(expl);					
				}else{
					ArrayList<MatExplReport> lsMatnr = new ArrayList<>();					
					lsMatnr.add(expl);					
					map.put(key, lsMatnr);					
				}	
				
			}			
						
			for(MatExplReport matnr: lsVHILM){
				
				key = matnr.getLgort() + matnr.getLgtyp() + matnr.getLtypt() + matnr.getLgpla() + matnr.getMatnr(); 
				
				if(map.containsKey(key)){
					
					matnr.setLgort(map.get(key).get(0).getLgort());
					matnr.setLgtyp(map.get(key).get(0).getLgtyp());
					matnr.setLgpla(map.get(key).get(0).getLgpla());
					matnr.setLgortExpl(map.get(key).get(0).getLgort());
					map.get(key).add(matnr);
					
				}else{
					
					ArrayList<MatExplReport> lsMatnr = new ArrayList<>();
					lsMatnr.add(matnr);
					map.put(key, lsMatnr);
				}
			}
									
			//Iterate the map and create the final list
			for (ArrayList<MatExplReport> lsMatnr : map.values()) {
			    			
				lsDetail.addAll(lsMatnr);
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
	
	private ArrayList<MatExplReport> getVHILMByLgpla(int docInvId, Connection con) throws SQLException {
		
		ArrayList<MatExplReport> lsVHILM = new ArrayList<>();
		final String QUERY = "SELECT DIP_LGORT, LGTYP, LTYPT, DIP_LGPLA, A.MATNR, A.DIP_VHILM, MEINS, A.MAKTX, SUM(COUNTED) COUNTED, " 
				+ "ISNULL((SELECT CATEGORY FROM INV_CAT_CATEGORY AS ICC " 
				+ "INNER JOIN INV_REL_CAT_MAT AS IRCM ON (ICC.CAT_ID = IRCM.REL_CAT_ID) " 
				+ "WHERE REL_MATNR = SUBSTRING(A.MATNR, PATINDEX('%[^0 ]%', A.MATNR + ' '), LEN(A.MATNR))), '') AS CAT_EXPL " 
				+ "FROM (SELECT DIP_LGORT, LGTYP, LTYPT, DIP_LGPLA, DIP_MATNR MATNR, DIP_VHILM, C.MAKTX, F.MEINS, "
				+ "CASE WHEN A.DIP_VHILM_COUNT IS NULL THEN 0 " 
				+ "WHEN LEN(A.DIP_VHILM_COUNT) = 0 THEN 0 " 
				+ "ELSE CAST(A.DIP_VHILM_COUNT AS decimal(20,3)) END COUNTED " 
				+ "FROM INV_VW_DOC_INV_REP_LGORT_LGPLA AS A " 
				+ "INNER JOIN MAKT AS C ON (A.DIP_VHILM = SUBSTRING(C.MATNR, PATINDEX('%[^0 ]%', C.MATNR + ' '), LEN(C.MATNR))) " 
				+ "INNER JOIN MARA AS F ON (A.DIP_VHILM = SUBSTRING(F.MATNR, PATINDEX('%[^0 ]%', F.MATNR + ' '), LEN(F.MATNR))) " 
				+ "WHERE A.DOC_INV_ID = ? "
				+ "AND DIP_VHILM IS NOT NULL " 
				+ "GROUP BY DIP_LGORT, LGTYP, LTYPT, DIP_LGPLA, DIP_MATNR, DIP_VHILM, F.MEINS, A.DIP_VHILM_COUNT, C.MAKTX) AS A " 
				+ "GROUP BY DIP_LGORT, LGTYP, LTYPT, DIP_LGPLA, A.MATNR, A.DIP_VHILM, MEINS, A.MAKTX ";  
		PreparedStatement stm = null;	
		MatExplReport matnrRec = null;
		stm = con.prepareStatement(QUERY);
		stm.setInt(1, docInvId);
		log.info("[getVHILM] Executing query...");

		ResultSet rs = stm.executeQuery();

		while (rs.next()) {

			matnrRec = new MatExplReport();
			matnrRec.setLgort(rs.getString("DIP_LGORT"));
			matnrRec.setLgtyp(rs.getString("LGTYP"));
			matnrRec.setLtypt(rs.getString("LTYPT"));
			matnrRec.setLgpla(rs.getString("DIP_LGPLA"));
			matnrRec.setMatnr(rs.getString("MATNR"));
			matnrRec.setMatnrExpl(rs.getString("DIP_VHILM"));
			matnrRec.setCatExpl(rs.getString("CAT_EXPL"));			
			matnrRec.setUmbExpl(rs.getString("MEINS"));
			matnrRec.setDescMantrExpl(rs.getString("MAKTX"));			
			matnrRec.setQuantity(rs.getString("COUNTED"));			
			lsVHILM.add(matnrRec);
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
		
		return lsVHILM;		
	}
		
}
