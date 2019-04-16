package com.gmodelo.cyclicinventories.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.cyclicinventories.beans.AbstractResultsBean;
import com.gmodelo.cyclicinventories.beans.ExplosionDetailByPackingRule;
import com.gmodelo.cyclicinventories.beans.MessagesTypes;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.utils.ConnectionManager;
import com.gmodelo.cyclicinventories.utils.ReturnValues;

public class ExplosionDetailByPackingRuleDao {
	
	private Logger log = Logger.getLogger(ExplosionDetailByPackingRuleDao.class.getName());
	private static LogInveDao logInve = new LogInveDao();
	
	public Response saveExplosionDetailByPackingRule(ArrayList<ExplosionDetailByPackingRule> ed, String user) {
		
		Response<?> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		CallableStatement csBatch = null;	
		String currentSP = "";		
		
		/////////////////////////INICIO VALIDACIONES ANTES DE GUARDAR//////////////////////////////////////
		String lsLgort = "";
		for(ExplosionDetailByPackingRule item : ed){
			lsLgort += item.getLgort() + ",";
		}
		
		lsLgort = lsLgort.substring(0, lsLgort.length() - 1);
		
		//Validate lgorts 
		final String  LGORT_BY_WERK = "SELECT A.value FROM (SELECT * FROM STRING_SPLIT(?, ',')) AS A "
				+ "WHERE A.value NOT IN (SELECT DISTINCT LGORT FROM dbo.T001L WITH (NOLOCK) WHERE WERKS = ?)";
		
		logInve.log(MessagesTypes.Warning, "Explosión por Norma", "Guardado de datos...", 
				"Intentando salvar los datos de Explosión por Norma.");
		
		try {
			
			PreparedStatement stm = con.prepareStatement(LGORT_BY_WERK);
			stm.setString(1, lsLgort);
			stm.setString(2, ed.get(0).getWerks());
			
			ResultSet rs = stm.executeQuery();
			
			lsLgort = "";

			while (rs.next()){
				lsLgort += rs.getString("value") + ",";
				//System.out.println("Here " + rs.getString("value") + " " + rs.getString("value").length());
			}
									
		} catch (SQLException e) {
			log.log(Level.SEVERE, "[saveExplosionDetailByPackingRule] Ocurrió un error al validar los almacenes, query: " + LGORT_BY_WERK, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs("Error al validar almacenes.");
			res.setAbstractResult(abstractResult);
			return res;
		}
		
		logInve.log(MessagesTypes.Information, "Explosión por Norma", "Guardado de datos...", 
				"Se guardaron los datos de forma éxitosa.");
				
		if(lsLgort.length() > 0){
			
			lsLgort = lsLgort.substring(0, lsLgort.length() - 1);
			abstractResult.setResultId(ReturnValues.IERROR);
			abstractResult.setResultMsgAbs("No se encontraron los sig. almancenes para el centro indicado: " + lsLgort);
			res.setAbstractResult(abstractResult);
			return res;
		}
				
		//////////////////////////FIN VALIDACIONES ANTES DE GUARDAR///////////////////////////////////		

		final String INV_SP_SAVE_NRM_EXPL = "INV_SP_SAVE_NRM_EXPL ?, ?, ?, ?, ?, ?, ?";		
		log.info("[saveExplosionDetailByPackingRule] Preparing sentence...");
		
		try {
			con.setAutoCommit(false);
			currentSP = INV_SP_SAVE_NRM_EXPL;
			csBatch = null;
			csBatch = con.prepareCall(INV_SP_SAVE_NRM_EXPL);

			for (ExplosionDetailByPackingRule obj : ed) {
								
				csBatch.setString(1, obj.getWerks());
				csBatch.setString(2, obj.getMatnr());
				csBatch.setString(3, obj.getRuleId());
				csBatch.setString(4, obj.getComponent());
				csBatch.setByte(5, (byte) (obj.isRelevant()?1:0));
				csBatch.setString(6, (obj.getLgort() == null ? "": obj.getLgort()));
				csBatch.setString(7, user);
				csBatch.addBatch();	

			}
						
			log.info("[saveExplosionDetailByPackingRule] Executing batch..." + csBatch.executeBatch());

			// Retrive the warnings if there're
			SQLWarning warning = csBatch.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING, warning.getMessage());
				warning = warning.getNextWarning();
			}

			con.commit();
			
			log.info("[saveExplosionDetailByPackingRule] Sentence successfully executed.");			

		} catch (SQLException e) {
			
			log.log(Level.SEVERE, "[saveExplosionDetailByPackingRule] Some error occurred while was trying to execute the S.P.: " + currentSP,
					e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			res.setAbstractResult(abstractResult);
			return res;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE, "[saveExplosionDetailByPackingRule] Some error occurred while was trying to close the connection.", e);
			}
		}
		res.setAbstractResult(abstractResult);
		return res;
	}
	
	public Response<List<ExplosionDetailByPackingRule>> getPackingRuleByMatnr(String werks, String matnr, String rulePackingId) {
		
		Response<List<ExplosionDetailByPackingRule>> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();		
		PreparedStatement stm = null;		
		List<ExplosionDetailByPackingRule> lspr = new ArrayList<>();
		ExplosionDetailByPackingRule edbpr;
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

		String GET_RULES = "SELECT SUBSTRING(B.POBJID, PATINDEX('%[^0 ]%', B.POBJID + ' '), LEN(B.POBJID)) POBJID, " 
				+ "D.CONTENT, SUBSTRING(C.MATNR, PATINDEX('%[^0 ]%', C.MATNR + ' '), LEN(C.MATNR)) MATNR, " 
				+ "F.MAKTX, G.MSEH3 BASEUNIT, C.TRGQTY, " 
				+ "ISNULL(H.NRM_RELEVANT, 0) RELEVANT, " 
				+ "ISNULL(H.NRM_LGORT, '') LGORT, " 
				+ "CAST(CAST(C.LASTMODIFY AS varchar(50)) + ' ' +  SUBSTRING(CAST(C.LASTMODIFYH   AS varchar(30)) ,1 , 12 ) AS datetime ) NRM_DMODIFIED " 
				+ "FROM PACKPO AS A WITH (NOLOCK) " 
				+ "INNER JOIN PACKKP AS B WITH (NOLOCK) ON A.PACKNR = B.PACKNR " 
				+ "INNER JOIN PACKPO AS C WITH (NOLOCK) ON A.PACKNR = C.PACKNR AND C.PAITEMTYPE <> 'I' " 
				+ "INNER JOIN PACKKPS AS D WITH (NOLOCK) ON A.PACKNR = D.PACKNR " 
				+ "INNER JOIN MAKT AS F WITH (NOLOCK) ON C.MATNR = F.MATNR " 
				+ "INNER JOIN T006A AS G WITH (NOLOCK) ON G.MSEHI = C.BASEUNIT " 
				+ "LEFT JOIN INV_NRM_EXPL AS H WITH (NOLOCK) ON (H.NRM_RULE = SUBSTRING(B.POBJID, PATINDEX('%[^0 ]%', B.POBJID + ' '), LEN(B.POBJID)) "
					+ "AND H.NRM_WERKS = ? AND H.NRM_MATNR = ? "
					+ "AND SUBSTRING(C.MATNR, PATINDEX('%[^0 ]%', C.MATNR + ' '), LEN(C.MATNR)) = H.NRM_COMPONENT) "
				+ "WHERE SUBSTRING(A.MATNR, PATINDEX('%[^0 ]%', A.MATNR + ' '), LEN(A.MATNR)) = ? ";
		
				if(!rulePackingId.isEmpty()){
					
					GET_RULES += "AND SUBSTRING(B.POBJID, PATINDEX('%[^0 ]%', B.POBJID + ' '), LEN(B.POBJID)) = ? "; 
				}
				 				
				GET_RULES += "GROUP BY B.POBJID, D.CONTENT, C.MATNR, F.MAKTX, G.MSEH3, C.TRGQTY, H.NRM_RELEVANT, H.NRM_LGORT, C.LASTMODIFY, C.LASTMODIFYH "; 

		log.info(GET_RULES);

		log.info("[getPackingRuleByMatnr] Preparing sentence...");

		try {
			
			stm = con.prepareStatement(GET_RULES);
			stm.setString(1, werks);
			stm.setString(2, matnr);
			stm.setString(3, matnr);
			
			if(!rulePackingId.isEmpty()){
				stm.setString(4, rulePackingId);
			}					
			
			log.info("[getPackingRuleByMatnr] Executing query...");

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {

				edbpr = new ExplosionDetailByPackingRule();
				edbpr.setWerks(werks);
				edbpr.setMatnr(matnr);
				edbpr.setRuleId(rs.getString("POBJID"));
				edbpr.setRuleDesc(rs.getString("CONTENT"));
				edbpr.setComponent(rs.getString("MATNR"));
				edbpr.setCompDesc(rs.getString("MAKTX"));
				edbpr.setUmb(rs.getString("BASEUNIT"));
				edbpr.setQuantity(rs.getString("TRGQTY"));
				edbpr.setRelevant(rs.getBoolean("RELEVANT"));
				edbpr.setLgort(rs.getString("LGORT"));
				edbpr.setLastUpdate(format.format(rs.getTimestamp("NRM_DMODIFIED")));
				lspr.add(edbpr);
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

			log.info("[getPackingRuleByMatnr] Sentence successfully executed.");
						
			res.setAbstractResult(abstractResult);
			res.setLsObject(lspr);			
		} catch (SQLException e) {
			log.log(Level.SEVERE,
					"[getPackingRuleByMatnr] Some error occurred while was trying to execute the query: " + GET_RULES,
					e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			res.setAbstractResult(abstractResult);
			
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				
				log.log(Level.SEVERE, "[getPackingRuleByMatnr] Some error occurred while was trying to close the connection.", e);
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				res.setAbstractResult(abstractResult);
			}
		}
		
		return res;
	}

}
