package com.gmodelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.beans.AbstractResultsBean;
import com.gmodelo.beans.Response;
import com.gmodelo.beans.RouteBean;
import com.gmodelo.beans.VersionBean;
import com.gmodelo.utils.ConnectionManager;
import com.gmodelo.utils.ReturnValues;

public class VersionDao {
	
	private Logger log = Logger.getLogger( VersionDao.class.getName());
	
	public Response<Object> getVersion() {
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		PreparedStatement stm = null;

		Response<Object> res = new Response<Object>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		String INV_VW_VERSION_APP = "SELECT VERSION, NAME FROM dbo.INV_VW_VERSION_APP WITH(NOLOCK) ";

		log.warning(INV_VW_VERSION_APP);
		log.log(Level.WARNING, "[getVersionDao] Preparing sentence...");
		try {
			stm = con.prepareStatement(INV_VW_VERSION_APP);

			log.log(Level.WARNING, "[getVersionDao] Executing query...");

			ResultSet rs = stm.executeQuery();
			
			VersionBean versionBean = new VersionBean();
			
			while (rs.next()) {
				versionBean.setVersion(rs.getInt(1));
				versionBean.setName(rs.getString(2));
			}
	
			abstractResult.setIntCom1(versionBean.getVersion());
			abstractResult.setStrCom1(versionBean.getName());
			
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
			log.log(Level.SEVERE,"[getVersionDao] Some error occurred while was trying to execute the query: " + INV_VW_VERSION_APP, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE, "[getVersionDao] Some error occurred while was trying to close the connection.",e);
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs(e.getMessage());
				res.setAbstractResult(abstractResult);
				return res;
			}
		}

		res.setAbstractResult(abstractResult);
		return res;
	}

	public static void main(String[] args) {
		VersionDao d = new VersionDao();
		Response<Object> x = d.getVersion();
		x.getAbstractResult().getIntCom1();
		x.getAbstractResult().getStrCom1();
	}

}
