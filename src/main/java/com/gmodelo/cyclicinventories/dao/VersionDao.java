package com.gmodelo.cyclicinventories.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.cyclicinventories.beans.AbstractResultsBean;
import com.gmodelo.cyclicinventories.beans.Request;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.utils.ConnectionManager;
import com.gmodelo.cyclicinventories.utils.ReturnValues;
import com.google.gson.Gson;

public class VersionDao {

	private Logger log = Logger.getLogger(VersionDao.class.getName());
	private static final String INV_VW_VERSION_APP = "SELECT VA_VERSION VERSION, VA_NAME [NAME], VA_CREATED_DATE FROM INV_VERSION_APP WITH(NOLOCK)";

	@SuppressWarnings("rawtypes")
	public Response getVersion(Request request) {
		Connection con = new ConnectionManager().createConnection();
		PreparedStatement stm = null;
		Response res = new Response();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		AbstractResultsBean requestResult = new AbstractResultsBean();
		log.info(INV_VW_VERSION_APP);
		log.info("[getVersionDao] Preparing sentence...");
		try {
			requestResult = new Gson().fromJson(request.getLsObject().toString(), AbstractResultsBean.class);
			requestResult.setResultId(ReturnValues.IERROR);
			stm = con.prepareStatement(INV_VW_VERSION_APP);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				if (requestResult.getIntCom1() == rs.getInt("VERSION")) {
					requestResult.setResultId(ReturnValues.ISUCCESS);
					break;
				}
			}
		} catch (SQLException e) {
			log.log(Level.SEVERE,
					"[getVersionDao] Some error occurred while was trying to execute the query: " + INV_VW_VERSION_APP,
					e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
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
