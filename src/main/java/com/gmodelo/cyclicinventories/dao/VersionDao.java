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
import com.gmodelo.cyclicinventories.exception.InvCicException;
import com.gmodelo.cyclicinventories.utils.ConnectionManager;
import com.gmodelo.cyclicinventories.utils.ReturnValues;
import com.gmodelo.cyclicinventories.utils.Utilities;
import com.google.gson.Gson;

public class VersionDao {

	private Logger log = Logger.getLogger(VersionDao.class.getName());
	private static final String INV_IS_ADMIN_ENABLED = "INV_CIC_ADMIN_APP_MANAGED";
	private static final String INV_VW_VERSION_APP = "SELECT VA_VERSION VERSION, VA_NAME [NAME], VA_CREATED_DATE FROM INV_VERSION_APP WITH(NOLOCK)";
	private final ConnectionManager iConnectionManager = new ConnectionManager();

	@SuppressWarnings("rawtypes")
	public Response getVersion(Request request) {
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		Response res = new Response();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		AbstractResultsBean requestResult = new AbstractResultsBean();
		log.info("[getVersionDao] Preparing sentence...");
		try {
			requestResult = new Gson().fromJson(request.getLsObject().toString(), AbstractResultsBean.class);
			abstractResult.setResultId(ReturnValues.IERROR);
			stm = con.prepareStatement(INV_VW_VERSION_APP);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				if (requestResult.getIntCom1() == rs.getInt("VERSION")) {
					abstractResult.setResultId(ReturnValues.ISUCCESS);
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

	@SuppressWarnings("rawtypes")
	public Response isAdminEnabled(Request request) {
		Connection con = iConnectionManager.createConnection();
		Response res = new Response();
		AbstractResultsBean abstractResultsBean = new AbstractResultsBean();
		log.info("[isAdminEnabled] Getting Data.");
		try {
			AbstractResultsBean result = new Utilities().getValueRepByKey(con, INV_IS_ADMIN_ENABLED);
			AbstractResultsBean resultCompare = new Gson().fromJson(request.getLsObject().toString(),
					AbstractResultsBean.class);
			if (result.getResultId() == ReturnValues.ISUCCESS) {
				if (!result.getStrCom1().equals(resultCompare.getStrCom1())) {
					abstractResultsBean.setResultId(ReturnValues.IPASSWORDNOTMATCH);
					abstractResultsBean.setResultMsgAbs("Contraseña Incorrecta, contacte Administrador");
				}
			} else {
				abstractResultsBean.setResultId(ReturnValues.IREPKEYNOTFOUND);
				abstractResultsBean.setResultMsgAbs("Contraseña Incorrecta, contacte Administrador");
			}
		} catch (InvCicException e) {
			abstractResultsBean.setResultId(ReturnValues.IEXCEPTION);
			abstractResultsBean.setResultMsgAbs(e.getMessage());
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE, "[isAdminEnabled] Some error occurred while was trying to close the connection.",
						e);
			}
		}
		res.setAbstractResult(abstractResultsBean);
		return res;
	}
}
