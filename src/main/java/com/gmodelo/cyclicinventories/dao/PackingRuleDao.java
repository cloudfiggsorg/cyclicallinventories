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
import com.gmodelo.cyclicinventories.beans.PackingRule;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.utils.ConnectionManager;
import com.gmodelo.cyclicinventories.utils.ReturnValues;

public class PackingRuleDao {
		
	private Logger log = Logger.getLogger(PackingRuleDao.class.getName());
	
	public Response<List<PackingRule>> getPackingRuleByMatnr(String matnr) {
		
		Response<List<PackingRule>> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();		
		PreparedStatement stm = null;		
		List<PackingRule> lspr = new ArrayList<>();
		PackingRule pr;

		final String GET_RULES = "SELECT SUBSTRING(B.POBJID, PATINDEX('%[^0 ]%', B.POBJID + ' '), LEN(B.POBJID)) POBJID, D.CONTENT "
				+ "FROM PACKPO AS A WITH (NOLOCK) " 
				+ "INNER JOIN PACKKP AS B WITH (NOLOCK) ON A.PACKNR = B.PACKNR "
				+ "INNER JOIN PACKKPS AS D WITH (NOLOCK) ON A.PACKNR = D.PACKNR "
				+ "WHERE SUBSTRING(A.MATNR, PATINDEX('%[^0 ]%', A.MATNR + ' '), LEN(A.MATNR)) = ? "
				+ "GROUP BY B.POBJID, D.CONTENT "
				+ "ORDER BY B.POBJID ";

		log.info(GET_RULES);

		log.info("[getPackingRuleByMatnr] Preparing sentence...");

		try {
			
			stm = con.prepareStatement(GET_RULES);
			stm.setString(1, matnr);
			
			log.info("[getPackingRuleByMatnr] Executing query...");

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {

				pr = new PackingRule();
				pr.setRuleId(rs.getString("POBJID"));
				pr.setRuleDesc(rs.getString("CONTENT"));
				lspr.add(pr);
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
