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
import com.gmodelo.beans.ReporteConteosBean;
import com.gmodelo.beans.ReporteDocInvBean;
import com.gmodelo.beans.Response;
import com.gmodelo.beans.TareasTiemposLgplaBean;
import com.gmodelo.beans.TareasTiemposZonasBean;
import com.gmodelo.utils.ConnectionManager;
import com.gmodelo.utils.ReturnValues;
import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;

public class ReportesDao {
	
	private Logger log = Logger.getLogger(ReportesDao.class.getName());
	
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

		INV_VW_REP_APEGOS = "SELECT DOC_INV_ID, ROUTE_ID,RDESC, BUKRS, BDESC, WERKS, WDESC, LGORT,GDESC, TASK_ID, DTYPE, USER_DOCINV, USER_COUNT, DATE_INI, DATE_FIN, TIEMPO, GROUP_ID, CREACION, EJECUCION FROM INV_VW_REP_APEGOS WITH(NOLOCK) ";

		if (searchFilter != null) {
			INV_VW_REP_APEGOS += "WHERE ROUTE_ID LIKE '%" + searchFilterNumber + "%' OR RDESC LIKE '%" + searchFilter + "%' OR DOC_INV_ID LIKE '%" + searchFilter + "%' OR LGORT '%"+ searchFilter +"%' OR WERKS LIKE '%"+searchFilter+"%'";
		} else {
			String condition = buildConditionApegos(apegosBean);
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
				apegosBean.setApegos((rs.getString("CREACION").equalsIgnoreCase("100%") && rs.getString("EJECUCION").equalsIgnoreCase("100%")) ? "100%" : "0%");
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
		
	public Response<List<ReporteConteosBean>> getReporteConteos(ReporteConteosBean conteosBean, String searchFilter) {
		
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;

		Response<List<ReporteConteosBean>> res = new Response<List<ReporteConteosBean>>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		List<ReporteConteosBean> listBean = new ArrayList<ReporteConteosBean>();
		String INV_VW_REP = null;
		int aux;		
		String searchFilterNumber = "";
		
		try {
			aux = Integer.parseInt(searchFilter); 
			searchFilterNumber += aux;
		} catch (Exception e) {
			searchFilterNumber = searchFilter;
			log.info("[getReporteConteosDao] Trying to convert String to Int");
		}		

		INV_VW_REP = "SELECT ZONE_ID, ZONE_DESC, LGTYP, LTYPT, LGPLA,MAKTX,COU_MATNR, COU_VHILM, COU_SECUENCY, COU_TARIMAS, COU_CAMAS, COU_CANTIDAD_UNI_MED, COU_TOTAL,"
				+ " COU_START_DATE, COU_END_DATE,COU_USER_ID, TAS_GROUP_ID,TAS_DOC_INV_ID,ROUTE_ID, RDESC, BUKRS, WERKS, BDESC, WDESC FROM INV_VW_REPORTE_CONTEOS WITH(NOLOCK) ";

		if (searchFilter != null) {
			INV_VW_REP += "WHERE ROUTE_ID LIKE '%" + searchFilterNumber + "%' OR RDESC LIKE '%" + searchFilter + "%' OR DOC_INV_ID LIKE '%" + searchFilter + "%' OR COU_USER_ID LIKE '%" + searchFilter + "%' ";
		} else {
			String condition = buildConditionConteos(conteosBean);
			if (condition != null) {
				INV_VW_REP += condition;
			}
		}
		log.info(INV_VW_REP);
		log.info("[getReporteConteosDao] Preparing sentence...");
		try {
			stm = con.prepareStatement(INV_VW_REP);
			log.info("[getReporteConteosDao] Executing query...");
			ResultSet rs = stm.executeQuery();
				
				while (rs.next()) {
					conteosBean = new ReporteConteosBean();
					conteosBean.setZoneID(rs.getInt("ZONE_ID"));
					conteosBean.setZoneD(rs.getString("ZONE_DESC"));
					conteosBean.setLgtyp(rs.getString("LGTYP"));
					conteosBean.setLtypt(rs.getString("LTYPT"));
					conteosBean.setLgpla(rs.getString("LGPLA"));
					conteosBean.setMatnr(rs.getString("COU_MATNR"));
					conteosBean.setMatnrD(rs.getString("MAKTX"));
					conteosBean.setVhilm(rs.getString("COU_VHILM"));
					conteosBean.setSecuency(rs.getInt("COU_SECUENCY"));
					conteosBean.setTarimas(rs.getInt("COU_TARIMAS"));
					conteosBean.setCamas(rs.getInt("COU_CAMAS"));
					conteosBean.setUniMed(rs.getInt("COU_CANTIDAD_UNI_MED"));
					conteosBean.setTotal(rs.getInt("COU_TOTAL"));
					conteosBean.setStartDate(rs.getString("COU_START_DATE"));
					conteosBean.setEndDate((rs.getString("COU_END_DATE") == null) ? "" : (rs.getString("COU_END_DATE")));
					conteosBean.setUserID(rs.getString("COU_USER_ID"));
					conteosBean.setGroupID(rs.getString("TAS_GROUP_ID"));
					conteosBean.setDocInvID(rs.getInt("TAS_DOC_INV_ID"));				
					conteosBean.setRouteId(rs.getInt("ROUTE_ID"));
					conteosBean.setRouteD(rs.getString("RDESC"));
					conteosBean.setBukrs(rs.getString("BUKRS"));
					conteosBean.setBukrsD(rs.getString("BDESC"));
					conteosBean.setWerks(rs.getString("WERKS"));
					conteosBean.setWerksD(rs.getString("WDESC"));
					
					listBean.add(conteosBean);
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
			log.info("[getReporteConteosDao] Sentence successfully executed.");
		} catch (SQLException  e) {
			log.log(Level.SEVERE,
					"[getReporteConteosDao] Some error occurred while was trying to execute the query: " + INV_VW_REP, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE, "[getReporteConteosDao] Some error occurred while was trying to close the connection.",
						e);
			}
		}

		res.setAbstractResult(abstractResult);
		res.setLsObject(listBean);
		return res;
	}

	public Response<List<ReporteDocInvBean>> getReporteDocInv(ReporteDocInvBean bean, String searchFilter) {
		
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;

		Response<List<ReporteDocInvBean>> res = new Response<List<ReporteDocInvBean>>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		List<ReporteDocInvBean> listBean = new ArrayList<ReporteDocInvBean>();
		String INV_VW_REP = null;
		int aux;		
		String searchFilterNumber = "";
		
		try {
			aux = Integer.parseInt(searchFilter); 
			searchFilterNumber += aux;
		} catch (Exception e) {
			searchFilterNumber = searchFilter;
			log.info("[getReporteDocInvDao] Trying to convert String to Int");
		}		

		INV_VW_REP = "SELECT DOC_INV_ID , DIH_ROUTE_ID,RDESC, DIH_BUKRS, BDESC, WERKS, WDESC,DIH_STATUS, DIH_TYPE, DIH_CREATED_BY, DIH_CREATED_DATE, DIP_LGORT, LGOBE, DIP_LGTYP, LTYPT, DIP_LGPLA,"
				+ " DIP_MATNR, MAKTX, DIP_THEORIC, DIP_COUNTED, DIP_DIFF_COUNTED, DIP_DIFF_FLAG FROM INV_VW_REPORTE_DOC_INV WITH(NOLOCK) ";

		if (searchFilter != null) {
			INV_VW_REP += "WHERE ROUTE_ID LIKE '%" + searchFilterNumber + "%' OR RDESC LIKE '%" + searchFilter + "%' OR DOC_INV_ID LIKE '%" + searchFilter + "%' OR DIH_CREATED_BY LIKE '%" + searchFilter + "%' ";
		} else {
			String condition = buildConditionDocInv(bean);
			if (condition != null) {
				INV_VW_REP += condition;
			}
		}
		log.info(INV_VW_REP);
		log.info("[getReporteDocInvDao] Preparing sentence...");
		try {
			stm = con.prepareStatement(INV_VW_REP);
			log.info("[getReporteDocInvDao] Executing query...");
			ResultSet rs = stm.executeQuery();
			
			while (rs.next()) {
				bean = new ReporteDocInvBean();
				bean.setDocInvId(rs.getInt("DOC_INV_ID"));
				bean.setRouteId(rs.getInt("DIH_ROUTE_ID"));
				bean.setRouteD(rs.getString("RDESC"));
				bean.setBukrs(rs.getString("DIH_BUKRS"));
				bean.setBukrsD(rs.getString("BDESC"));
				bean.setWerks(rs.getString("WERKS"));
				bean.setWerksD(rs.getString("WDESC"));
				bean.setTypeD(rs.getString("DIH_TYPE"));
				bean.setUserID(rs.getString("DIH_CREATED_BY"));
				bean.setDate(rs.getString("DIH_CREATED_DATE"));
				bean.setLgort(rs.getString("DIP_LGORT"));
				bean.setLgortD(rs.getString("LGOBE"));
				bean.setLgtyp(rs.getString("DIP_LGTYP"));
				bean.setLtypt(rs.getString("LTYPT"));
				bean.setLgpla(rs.getString("DIP_LGPLA"));
				bean.setMatnr(rs.getString("DIP_MATNR"));
				bean.setMatnrD(rs.getString("MAKTX"));
				bean.setTheoric(rs.getString("DIP_THEORIC"));
				bean.setCounted(rs.getString("DIP_DIFF_COUNTED"));
				bean.setFlag(rs.getString("DIP_DIFF_FLAG"));				
				
				listBean.add(bean);
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
			log.info("[getReporteDocInvDao] Sentence successfully executed.");
		} catch (SQLException  e) {
			log.log(Level.SEVERE,
					"[getReporteDocInvDao] Some error occurred while was trying to execute the query: " + INV_VW_REP, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE, "[getReporteDocInvDao] Some error occurred while was trying to close the connection.",
						e);
			}
		}

		res.setAbstractResult(abstractResult);
		res.setLsObject(listBean);
		return res;
	}

	public Response<List<TareasTiemposLgplaBean>> getReporteTareasTiemposLgpla(TareasTiemposLgplaBean tareasBean, String searchFilter) {
		
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
			INV_VW_REP_TAREAS += "WHERE ROUTE_ID LIKE '%" + searchFilterNumber + "%' OR RDESC LIKE '%" + searchFilter + "%' OR DOC_INV_ID LIKE '%" + searchFilter + "%' OR ZPO_LGPLA LIKE '%"+searchFilter+"%' OR COU_START_DATE LIKE '%"+searchFilter+"%',COU_END_DATE %'"+searchFilter+"%'";
		} else {
			String condition = buildConditionTiemposLgpla(tareasBean);
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
	
	public Response<List<TareasTiemposZonasBean>> getReporteTareasTiemposZonas(TareasTiemposZonasBean tareasBean, String searchFilter) {
		
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;

		Response<List<TareasTiemposZonasBean>> res = new Response<List<TareasTiemposZonasBean>>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		List<TareasTiemposZonasBean> listTareasBean = new ArrayList<TareasTiemposZonasBean>();
		String INV_VW_REP_TAREAS = null;
		int aux;		
		String searchFilterNumber = "";
		
		try {
			aux = Integer.parseInt(searchFilter); 
			searchFilterNumber += aux;
		} catch (Exception e) {
			searchFilterNumber = searchFilter;
			log.info("[getReporteTiemposTareasZonasDao] Trying to convert String to Int");
		}		

		INV_VW_REP_TAREAS = "SELECT DOC_INV_ID,ROUTE_ID,RDESC,BUKRS,BDESC,WERKS,WDESC,TASK_ID,GROUP_ID,ZONE_ID,ZON_DESC,COU_START_DATE,COU_END_DATE,COU_USER_ID,TIEMPO FROM INV_TAREAS_TIEMPOS_ZONAS  WITH(NOLOCK) ";

		if (searchFilter != null) {
			INV_VW_REP_TAREAS += "WHERE ROUTE_ID LIKE '%" + searchFilterNumber + "%' OR RDESC LIKE '%" + searchFilter + "%' OR DOC_INV_ID LIKE '%" + searchFilter + "%' OR ZONE_ID LIKE '%" + searchFilter + "%' OR ZON_DESC LIKE '%" + searchFilter + "%' OR COU_START_DATE LIKE '%" + searchFilter+ "%' OR COU_END_DATE LIKE '%" +searchFilter+ "%'  ";
		} else {
			String condition = buildConditionTiemposZonas(tareasBean);
			if (condition != null) {
				INV_VW_REP_TAREAS += condition;
			}
		}
		log.info(INV_VW_REP_TAREAS);
		log.info("[getReporteTiemposTareasZonasDao] Preparing sentence...");
		try {
			stm = con.prepareStatement(INV_VW_REP_TAREAS);

			log.info("[getReporteTiemposTareasZonasDao] Executing query...");

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				
				tareasBean = new TareasTiemposZonasBean();
				tareasBean.setDocInvId(rs.getString("DOC_INV_ID"));
				tareasBean.setRouteId(rs.getString("ROUTE_ID"));
				tareasBean.setRdesc(rs.getString("RDESC"));
				tareasBean.setBukrs(rs.getString("BUKRS"));
				tareasBean.setBdesc(rs.getString("BDESC"));
				tareasBean.setWerks(rs.getString("WERKS"));
				tareasBean.setWdesc(rs.getString("WDESC"));
				tareasBean.setZoneId(rs.getString("ZONE_ID"));
				tareasBean.setZoneD(rs.getString("ZON_DESC"));
				tareasBean.setTaskId(rs.getString("TASK_ID"));
				tareasBean.setDateIni(rs.getString("COU_START_DATE"));
				tareasBean.setDateFin((rs.getString("COU_END_DATE") == null) ? "" : (rs.getString("COU_END_DATE")));
				tareasBean.setTiempo((rs.getString("TIEMPO") == null ? "": (rs.getString("TIEMPO"))));
				tareasBean.setUser(rs.getString("COU_USER_ID"));				
				tareasBean.setIdGrupo(rs.getString("GROUP_ID"));				
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
			log.info("[getReporteTiemposTareasZonasDao] Sentence successfully executed.");
		} catch (SQLException  e) {
			log.log(Level.SEVERE,
					"[getReporteTiemposTareasZonasDao] Some error occurred while was trying to execute the query: " + INV_VW_REP_TAREAS, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE, "[getReporteTiemposTareasZonasDao] Some error occurred while was trying to close the connection.",
						e);
			}
		}

		res.setAbstractResult(abstractResult);
		res.setLsObject(listTareasBean);
		return res;
	}
	
	private String buildConditionApegos(ApegosBean apegosB) {
		String routeId = "";
		String bukrs = "";
		String werks = "";
		String rdesc = "";
		String type = "";
		String user = "";
		String docInv = "";
		String lgort = "";
		String lgortD = "";
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
		docInv = (apegosB.getDocInvId() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " DOC_INV_ID = '" + apegosB.getDocInvId() + "' " : "";
		condition += docInv;
		
		lgort = (apegosB.getLgort() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " LGORT = '" + apegosB.getLgort() + "' " : "";
		condition += lgort;
		
		lgortD = (apegosB.getLgDesc() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " GDESC = '" + apegosB.getLgDesc() + "' " : "";
		condition += lgortD;
		
		
		condition = condition.isEmpty() ? null : condition;
		return condition;
	}
	
	private String buildConditionConteos(ReporteConteosBean Bean) {
		String routeId = "";
		String bukrs = "";
		String werks = "";
		String rdesc = "";
		String user = "";
		String docInv = "";
		
		String condition = "";
		
		routeId = (Bean.getRouteId() > 0)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " ROUTE_ID = '" + Bean.getRouteId() + "' "
				: "";
		condition += routeId;
		bukrs = (Bean.getBukrs() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " BUKRS = '" + Bean.getBukrs() + "' " : "";
		condition += bukrs;
		werks = (Bean.getWerks() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " WERKS = '" + Bean.getWerks() + "' " : "";
		condition += werks;
		rdesc = (Bean.getRouteD() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " RDESC = '" + Bean.getRouteD() + "' " : "";
		condition += rdesc;
		user = (Bean.getUserID() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " COU_USER_ID = '" + Bean.getUserID() + "' " : "";
		condition += user;
		docInv = (Bean.getDocInvID() > 0)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " TAS_DOC_INV_ID = '" + Bean.getDocInvID() + "' " : "";
		condition += docInv;
		
		
		condition = condition.isEmpty() ? null : condition;
		return condition;
	}
	
	private String buildConditionDocInv(ReporteDocInvBean Bean) {
		String routeId = "";
		String bukrs = "";
		String werks = "";
		String rdesc = "";
		String user = "";
		String docInv = "";
		
		String condition = "";
		
		routeId = (Bean.getRouteId() > 0)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " ROUTE_ID = '" + Bean.getRouteId() + "' "
				: "";
		condition += routeId;
		bukrs = (Bean.getBukrs() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " BUKRS = '" + Bean.getBukrs() + "' " : "";
		condition += bukrs;
		werks = (Bean.getWerks() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " WERKS = '" + Bean.getWerks() + "' " : "";
		condition += werks;
		rdesc = (Bean.getRouteD() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " RDESC = '" + Bean.getRouteD() + "' " : "";
		condition += rdesc;
		user = (Bean.getUserID() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " COU_USER_ID = '" + Bean.getUserID() + "' " : "";
		condition += user;
		docInv = (Bean.getDocInvId() > 0)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " DOC_INV_ID = '" + Bean.getDocInvId() + "' " : "";
		condition += docInv;
		
		condition = condition.isEmpty() ? null : condition;
		return condition;
	}

	private String buildConditionTiemposLgpla(TareasTiemposLgplaBean tareasB) {
		String routeId = "";
		String bukrs = "";
		String werks = "";
		String rdesc = "";
		String type = "";
		String user = "";
		String docInv = "";
		String lgpla = "";
		String start = "";
		String end = "";
		
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
		docInv = (tareasB.getDocInvId() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " DOC_INV_ID = '" + tareasB.getDocInvId() + "' " : "";
		condition += docInv;
		lgpla = (tareasB.getLgpla() != null)
		? (condition.contains("WHERE") ? " AND " : " WHERE ") + " ZPO_LGPLA = '" + tareasB.getLgpla() + "' " : "";
		condition += lgpla;
		
		start = (tareasB.getDateIni() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " COU_START_DATE = '" + tareasB.getDateIni() + "' " : "";
		condition += start;
				
		end = (tareasB.getDateFin() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " COU_END_DATE = '" + tareasB.getDateFin() + "' " : "";
		condition += end;		
		
		condition = condition.isEmpty() ? null : condition;
		return condition;
	}
	private String buildConditionTiemposZonas(TareasTiemposZonasBean tareasB) {
		String routeId = "";
		String bukrs = "";
		String werks = "";
		String rdesc = "";
		String type = "";
		String user = "";
		String docInv = "";
		String condition = "";
		String zone = "";
		String zoneD = "";
		String start = "";
		String end = "";
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
		docInv = (tareasB.getDocInvId() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " DOC_INV_ID = '" + tareasB.getDocInvId() + "' " : "";
		condition += docInv;
		
		zone = (tareasB.getZoneId() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " ZONE_ID = '" + tareasB.getZoneId() + "' " : "";
		condition += zone;
		
		zoneD = (tareasB.getZoneD() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " ZON_DESC = '" + tareasB.getZoneD() + "' " : "";
		condition += zoneD;
		
		start = (tareasB.getDateIni() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " COU_START_DATE = '" + tareasB.getDateIni() + "' " : "";
		condition += start;
				
		end = (tareasB.getDateFin() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " COU_END_DATE = '" + tareasB.getDateFin() + "' " : "";
		condition += end;
		
		condition = condition.isEmpty() ? null : condition;
		return condition;
	}
}
