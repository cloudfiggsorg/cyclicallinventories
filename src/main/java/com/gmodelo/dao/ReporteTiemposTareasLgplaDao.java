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
import com.gmodelo.beans.TareasTiemposLgplaBean;
import com.gmodelo.beans.Response;
import com.gmodelo.utils.ConnectionManager;
import com.gmodelo.utils.ReturnValues;
import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;

public class ReporteTiemposTareasLgplaDao {
	
	private Logger log = Logger.getLogger(ReporteTiemposTareasLgplaDao.class.getName());
	
	public Response<List<TareasTiemposLgplaBean>> getReporteTareasTiempos(TareasTiemposLgplaBean tareasBean, String searchFilter) {
		
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;

		Response<List<TareasTiemposLgplaBean>> res = new Response<List<TareasTiemposLgplaBean>>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		List<TareasTiemposLgplaBean> listTareasBean = new ArrayList<TareasTiemposLgplaBean>();
		String INV_VW_REP_TAREAS = null;
		int aux;		
		String searchFilterNumber = "";
		
		try {
			aux = Integer.parseInt(searchFilter); 
			searchFilterNumber += aux;
		} catch (Exception e) {
			searchFilterNumber = searchFilter;
			log.info("[getReporteTiemposTareasDao] Trying to convert String to Int");
		}		

		INV_VW_REP_TAREAS = "SELECT DOC_INV_ID,ROUTE_ID,RDESC,BUKRS,BDESC,TAS_GROUP_ID,WERKS,WDESC,TASK_ID,ZPO_LGPLA,COU_START_DATE,COU_END_DATE,COU_USER_ID,TIEMPO FROM INV_VW_REPORTE_TAREAS_CARRIL WITH(NOLOCK) ";

		if (searchFilter != null) {
			INV_VW_REP_TAREAS += "WHERE ROUTE_ID LIKE '%" + searchFilterNumber + "%' OR RDESC LIKE '%" + searchFilter + "%' OR DOC_INV_ID LIKE '%" + searchFilter + "%' ";
		} else {
			String condition = buildCondition(tareasBean);
			if (condition != null) {
				INV_VW_REP_TAREAS += condition;
			}
		}
		log.info(INV_VW_REP_TAREAS);
		log.info("[getReporteTiemposTareasDao] Preparing sentence...");
		try {
			stm = con.prepareStatement(INV_VW_REP_TAREAS);

			log.info("[getReporteTiemposTareasDao] Executing query...");

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				
				tareasBean = new TareasTiemposLgplaBean();
				tareasBean.setDocInvId(rs.getString("DOC_INV_ID"));
				tareasBean.setRouteId(rs.getString("ROUTE_ID"));
				tareasBean.setRdesc(rs.getString("RDESC"));
				tareasBean.setBukrs(rs.getString("BUKRS"));
				tareasBean.setBdesc(rs.getString("BDESC"));
				tareasBean.setWerks(rs.getString("WERKS"));
				tareasBean.setWdesc(rs.getString("WDESC"));
				tareasBean.setLgpla(rs.getString("ZPO_LGPLA"));
				tareasBean.setTaskId(rs.getString("TASK_ID"));
				tareasBean.setDateIni(rs.getString("COU_START_DATE"));
				tareasBean.setDateFin((rs.getString("COU_END_DATE") == null) ? "" : (rs.getString("COU_END_DATE")));
				tareasBean.setTiempo((rs.getString("TIEMPO") == null ? "": (rs.getString("TIEMPO"))));
				tareasBean.setUser(rs.getString("COU_USER_ID"));				
				tareasBean.setIdGrupo(rs.getString("TAS_GROUP_ID"));				
				listTareasBean.add(tareasBean);
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
			log.info("[getReporteTiemposTareasDao] Sentence successfully executed.");
		} catch (SQLException  e) {
			log.log(Level.SEVERE,
					"[getReporteTiemposTareasDao] Some error occurred while was trying to execute the query: " + INV_VW_REP_TAREAS, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE, "[getReporteTiemposTareasDao] Some error occurred while was trying to close the connection.",
						e);
			}
		}

		res.setAbstractResult(abstractResult);
		res.setLsObject(listTareasBean);
		return res;
	}
	
	private String buildCondition(TareasTiemposLgplaBean tareasB) {
		String routeId = "";
		String bukrs = "";
		String werks = "";
		String rdesc = "";
		String type = "";
		String user = "";
		
		String condition = "";
		
		routeId = (tareasB.getRouteId() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " ROUTE_ID = '" + tareasB.getRouteId() + "' "
				: "";
		condition += routeId;
		bukrs = (tareasB.getBukrs() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " BUKRS = '" + tareasB.getBukrs() + "' " : "";
		condition += bukrs;
		werks = (tareasB.getWerks() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " WERKS = '" + tareasB.getWerks() + "' " : "";
		condition += werks;
		rdesc = (tareasB.getRdesc() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " RDESC = '" + tareasB.getRdesc() + "' " : "";
		condition += rdesc;
		user = (tareasB.getUser() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " COU_USER_ID = '" + tareasB.getUser() + "' " : "";
		condition += user;
		condition = condition.isEmpty() ? null : condition;
		return condition;
	}

}
