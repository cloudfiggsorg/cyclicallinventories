package com.gmodelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.beans.AbstractResultsBean;
import com.gmodelo.beans.Response;
import com.gmodelo.utils.ConnectionManager;
import com.gmodelo.utils.ReturnValues;

public class VersionDao {

	private Logger log = Logger.getLogger(VersionDao.class.getName());
	private static final String INV_VW_VERSION_APP = "SELECT TOP 1 VERSION, NAME FROM dbo.INV_VW_VERSION_APP WITH(NOLOCK) ";

	@SuppressWarnings("rawtypes")
	public Response getVersion() {
		Connection con = new ConnectionManager().createConnection();
		PreparedStatement stm = null;
		Response res = new Response();
		AbstractResultsBean abstractResult = new AbstractResultsBean();

		log.warning(INV_VW_VERSION_APP);
		log.log(Level.WARNING, "[getVersionDao] Preparing sentence...");
		try {
			stm = con.prepareStatement(INV_VW_VERSION_APP);

			log.log(Level.WARNING, "[getVersionDao] Executing query...");

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
			log.log(Level.WARNING, "[getVersionDao] Sentence successfully executed.");
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
