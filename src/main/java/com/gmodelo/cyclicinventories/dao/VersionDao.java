package com.gmodelo.cyclicinventories.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.cyclicinventories.beans.AbstractResultsBean;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.utils.ConnectionManager;
import com.gmodelo.cyclicinventories.utils.ReturnValues;

public class VersionDao {

	private Logger log = Logger.getLogger(VersionDao.class.getName());
	private static final String INV_VW_VERSION_APP = "SELECT MAX(VERSION) AS VERSION, NAME "
			+ "FROM dbo.INV_VW_VERSION_APP WITH(NOLOCK) "
			+ "GROUP BY VERSION, NAME";

	@SuppressWarnings("rawtypes")
	public Response getVersion() {
		Connection con = new ConnectionManager().createConnection();
		PreparedStatement stm = null;
		Response res = new Response();
		AbstractResultsBean abstractResult = new AbstractResultsBean();

		log.info(INV_VW_VERSION_APP);
		log.info("[getVersionDao] Preparing sentence...");
		try {
			stm = con.prepareStatement(INV_VW_VERSION_APP);

			log.info("[getVersionDao] Executing query...");

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				abstractResult.setIntCom1(rs.getInt("VERSION"));
				abstractResult.setStrCom1(rs.getString("NAME"));
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
			log.info("[getVersionDao] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE,
					"[getVersionDao] Some error occurred while was trying to execute the query: " + INV_VW_VERSION_APP,
					e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE, "[getVersionDao] Some error occurred while was trying to close the connection.",
						e);
			}
		}

		res.setAbstractResult(abstractResult);
		return res;
	}

}
