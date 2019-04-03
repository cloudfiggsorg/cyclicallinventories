package com.gmodelo.cyclicinventories.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.cyclicinventories.beans.AbstractResultsBean;
import com.gmodelo.cyclicinventories.beans.ExplosionDetailByPackingRule;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.utils.ConnectionManager;
import com.gmodelo.cyclicinventories.utils.ReturnValues;

public class ExplosionDetailByPackingRuleDao {
	
	private Logger log = Logger.getLogger(ExplosionDetailByPackingRuleDao.class.getName());
	
	public Response<List<ExplosionDetailByPackingRule>> getPackingRuleByMatnr(String matnr, String rulePackingId) {
		
		Response<List<ExplosionDetailByPackingRule>> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();		
		PreparedStatement stm = null;		
		List<ExplosionDetailByPackingRule> lspr = new ArrayList<>();
		ExplosionDetailByPackingRule edbpr;
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

		final String GET_RULES = "SELECT A.PACKNR, SUBSTRING(B.POBJID, PATINDEX('%[^0 ]%', B.POBJID + ' '), LEN(B.POBJID)) POBJID, " 
				+ "D.CONTENT, SUBSTRING(C.MATNR, PATINDEX('%[^0 ]%', C.MATNR + ' '), LEN(C.MATNR)) MATNR, "
				+ "F.MAKTX, G.MSEH3 BASEUNIT, C.TRGQTY " 
				+ "FROM PACKPO AS A WITH (NOLOCK) "
				+ "INNER JOIN PACKKP AS B WITH (NOLOCK) ON A.PACKNR = B.PACKNR "
				+ "INNER JOIN PACKPO AS C WITH (NOLOCK) ON A.PACKNR = C.PACKNR AND C.PAITEMTYPE <> 'I' "
				+ "INNER JOIN PACKKPS AS D WITH (NOLOCK) ON A.PACKNR = D.PACKNR "
				+ "INNER JOIN MAKT AS F WITH (NOLOCK) ON C.MATNR = F.MATNR "
				+ "INNER JOIN T006A AS G WITH (NOLOCK) ON G.MSEHI = C.BASEUNIT "
				+ "WHERE SUBSTRING(A.MATNR, PATINDEX('%[^0 ]%', A.MATNR + ' '), LEN(A.MATNR)) = ? "
				+ "AND SUBSTRING(B.POBJID, PATINDEX('%[^0 ]%', B.POBJID + ' '), LEN(B.POBJID)) = ? "
				+ "GROUP BY A.PACKNR, B.POBJID, D.CONTENT, C.MATNR, F.MAKTX, G.MSEH3, C.TRGQTY " 
				+ "ORDER BY A.PACKNR";

		log.info(GET_RULES);

		log.info("[getPackingRuleByMatnr] Preparing sentence...");

		try {
			
			stm = con.prepareStatement(GET_RULES);
			stm.setString(1, matnr);
			stm.setString(2, rulePackingId);
			
			log.info("[getPackingRuleByMatnr] Executing query...");

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {

				edbpr = new ExplosionDetailByPackingRule();
				edbpr.setRuleId(rs.getString("POBJID"));
				edbpr.setRuleDesc(rs.getString("CONTENT"));
				edbpr.setMantr(rs.getString("MATNR"));
				edbpr.setMaktx(rs.getString("MAKTX"));
				edbpr.setUmb(rs.getString("BASEUNIT"));
				edbpr.setQuantity(rs.getString("TRGQTY"));
				edbpr.setRelevant(true);
				edbpr.setLgort("");
				edbpr.setLastUpdate(format.format(new Date()));
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
