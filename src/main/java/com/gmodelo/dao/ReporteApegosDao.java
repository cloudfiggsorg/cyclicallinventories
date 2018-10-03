package com.gmodelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.beans.AbstractResultsBean;
import com.gmodelo.beans.ApegosBean;
import com.gmodelo.beans.Response;
import com.gmodelo.utils.ConnectionManager;
import com.gmodelo.utils.ReturnValues;
import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;

public class ReporteApegosDao {
	
	private Logger log = Logger.getLogger(ReporteApegosDao.class.getName());
	
	public Response<List<ApegosBean>> getReporteApegos(ApegosBean apegosBean, String searchFilter) {
		
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;

		Response<List<ApegosBean>> res = new Response<List<ApegosBean>>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		List<ApegosBean> listApegosBean = new ArrayList<ApegosBean>();
		String INV_VW_REP_APEGOS = null;
		int aux;		
		String searchFilterNumber = "";
		
		try {
			aux = Integer.parseInt(searchFilter); 
			searchFilterNumber += aux;
		} catch (Exception e) {
			searchFilterNumber = searchFilter;
			log.info("[getReporteApegosDao] Trying to convert String to Int");
		}		

		INV_VW_REP_APEGOS = "SELECT DOC_INV_ID, ROUTE_ID,RDESC, BUKRS, BDESC, WERKS, WDESC, LGORT, LGORT,GDESC, TASK_ID, DTYPE, USER_DOCINV, USER_COUNT, DATE_INI, DATE_FIN, TIEMPO, GROUP_ID, CREACION, EJECUCION FROM INV_VW_REP_APEGOS WITH(NOLOCK) ";

		if (searchFilter != null) {
			INV_VW_REP_APEGOS += "WHERE ROUTE_ID LIKE '%" + searchFilterNumber + "%' OR RDESC LIKE '%" + searchFilter + "%' OR DOC_INV_ID LIKE '%" + searchFilter + "%' ";
		} else {
			String condition = buildCondition(apegosBean);
			if (condition != null) {
				INV_VW_REP_APEGOS += condition;
			}
		}
		log.info(INV_VW_REP_APEGOS);
		log.info("[getReporteApegosDao] Preparing sentence...");
		try {
			stm = con.prepareStatement(INV_VW_REP_APEGOS);

			log.info("[getReporteApegosDao] Executing query...");

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				
				apegosBean = new ApegosBean();
				apegosBean.setDocInvId(rs.getString("DOC_INV_ID"));
				apegosBean.setRouteId(rs.getString("ROUTE_ID"));
				apegosBean.setRdesc(rs.getString("RDESC"));
				apegosBean.setBukrs(rs.getString("BUKRS"));
				apegosBean.setBdesc(rs.getString("BDESC"));
				apegosBean.setWerks(rs.getString("WERKS"));
				apegosBean.setWdesc(rs.getString("WDESC"));
				apegosBean.setLgort(rs.getString("LGORT"));
				apegosBean.setLgDesc(rs.getString("GDESC"));
				apegosBean.setdType((rs.getString("DTYPE").equalsIgnoreCase("1")) ? "DIARIO" : "MENSUAL");
				apegosBean.setTaskId(rs.getString("TASK_ID"));
				apegosBean.setDateIni(rs.getString("DATE_INI"));
				apegosBean.setDateFin((rs.getString("DATE_FIN") == null) ? "" : (rs.getString("DATE_FIN")));
				apegosBean.setTiempo((rs.getString("TIEMPO") == null ? "": (rs.getString("TIEMPO"))));
				apegosBean.setUserDocInv(rs.getString("USER_DOCINV"));
				apegosBean.setUserCount(rs.getString("USER_COUNT"));
				apegosBean.setGrupo(rs.getString("GROUP_ID"));
				apegosBean.setCreacion(rs.getString("CREACION"));
				apegosBean.setEjecucion(rs.getString("EJECUCION"));
				apegosBean.setApegos((rs.getString("CREACION").equalsIgnoreCase("100%") && rs.getString("EJECUCION").equalsIgnoreCase("100%")) ? "100%" : "");
				listApegosBean.add(apegosBean);
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
			log.info("[getReporteApegosDao] Sentence successfully executed.");
		} catch (SQLException  e) {
			log.log(Level.SEVERE,
					"[getReporteApegosDao] Some error occurred while was trying to execute the query: " + INV_VW_REP_APEGOS, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE, "[getReporteApegosDao] Some error occurred while was trying to close the connection.",
						e);
			}
		}

		res.setAbstractResult(abstractResult);
		res.setLsObject(listApegosBean);
		return res;
	}
	
	private String buildCondition(ApegosBean apegosB) {
		String routeId = "";
		String bukrs = "";
		String werks = "";
		String rdesc = "";
		String type = "";
		String user = "";
		
		String condition = "";
		
		routeId = (apegosB.getRouteId() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " ROUTE_ID = '" + apegosB.getRouteId() + "' "
				: "";
		condition += routeId;
		bukrs = (apegosB.getBukrs() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " BUKRS = '" + apegosB.getBukrs() + "' " : "";
		condition += bukrs;
		werks = (apegosB.getWerks() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " WERKS = '" + apegosB.getWerks() + "' " : "";
		condition += werks;
		rdesc = (apegosB.getRdesc() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " RDESC = '" + apegosB.getRdesc() + "' " : "";
		condition += rdesc;
		user = (apegosB.getUserCount() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " USER_COUNT = '" + apegosB.getUserCount() + "' " : "";
		condition += user;
		condition = condition.isEmpty() ? null : condition;
		return condition;
	}

}
