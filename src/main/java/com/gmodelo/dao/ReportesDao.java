package com.gmodelo.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.beans.AbstractResultsBean;
import com.gmodelo.beans.ApegosBean;
import com.gmodelo.beans.ConciliacionBean;
import com.gmodelo.beans.ConciliationPositionBean;
import com.gmodelo.beans.DocInvBean;
import com.gmodelo.beans.ReporteCalidadBean;
import com.gmodelo.beans.ReporteCalidadConteosBean;
import com.gmodelo.beans.ReporteConteosBean;
import com.gmodelo.beans.ReporteDocInvBean;
import com.gmodelo.beans.ReporteDocInvBeanHeader;
import com.gmodelo.beans.Response;
import com.gmodelo.beans.TareasTiemposLgplaBean;
import com.gmodelo.beans.TareasTiemposZonasBean;
import com.gmodelo.utils.ConnectionManager;
import com.gmodelo.utils.ReturnValues;

public class ReportesDao {

	private Logger log = Logger.getLogger(ReportesDao.class.getName());

	public Response<List<ApegosBean>> getReporteApegos(ApegosBean apegosBean) {

		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;

		Response<List<ApegosBean>> res = new Response<List<ApegosBean>>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		List<ApegosBean> listApegosBean = new ArrayList<ApegosBean>();
		String INV_VW_REP_APEGOS = null;
		
		INV_VW_REP_APEGOS = "SELECT DOC_INV_ID, ROUTE_ID,RDESC, BUKRS, BDESC, WERKS, WDESC, LGORT,GDESC, TASK_ID, DTYPE, USER_DOCINV, USER_COUNT, DATE_INI, DATE_FIN, TIEMPO, GROUP_ID, CREACION, EJECUCION FROM INV_VW_REP_APEGOS WITH(NOLOCK) ";

		String condition = buildConditionApegos(apegosBean);
		if (condition != null) {
			INV_VW_REP_APEGOS += condition;
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
				apegosBean.setTiempo((rs.getString("TIEMPO") == null ? "" : (rs.getString("TIEMPO"))));
				apegosBean.setUserDocInv(rs.getString("USER_DOCINV"));
				apegosBean.setUserCount(rs.getString("USER_COUNT"));
				apegosBean.setGrupo(rs.getString("GROUP_ID"));
				apegosBean.setCreacion(rs.getString("CREACION"));
				apegosBean.setEjecucion(rs.getString("EJECUCION"));
				apegosBean.setApegos((rs.getString("CREACION").equalsIgnoreCase("100%")
						&& rs.getString("EJECUCION").equalsIgnoreCase("100%")) ? "100%" : "0%");
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
		} catch (SQLException e) {
			log.log(Level.SEVERE, "[getReporteApegosDao] Some error occurred while was trying to execute the query: "
					+ INV_VW_REP_APEGOS, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,
						"[getReporteApegosDao] Some error occurred while was trying to close the connection.", e);
			}
		}

		res.setAbstractResult(abstractResult);
		res.setLsObject(listApegosBean);
		return res;
	}

	public Response<List<ReporteConteosBean>> getReporteConteos(ReporteConteosBean conteosBean) {

		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;

		Response<List<ReporteConteosBean>> res = new Response<List<ReporteConteosBean>>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		List<ReporteConteosBean> listBean = new ArrayList<ReporteConteosBean>();
		String INV_VW_REP = null;

		INV_VW_REP = "SELECT ZONE_ID, ZONE_DESC, LGTYP, LTYPT, LGPLA,MAKTX,COU_MATNR, COU_VHILM, COU_SECUENCY, COU_TARIMAS, COU_CAMAS, COU_CANTIDAD_UNI_MED, COU_TOTAL,"
				+ " COU_START_DATE, COU_END_DATE,COU_USER_ID, TAS_GROUP_ID,TAS_DOC_INV_ID,ROUTE_ID, RDESC, BUKRS, WERKS, BDESC, WDESC FROM INV_VW_REPORTE_CONTEOS WITH(NOLOCK) ";

		String condition = buildConditionConteos(conteosBean);
		if (condition != null) {
			INV_VW_REP += condition;
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
		} catch (SQLException e) {
			log.log(Level.SEVERE,
					"[getReporteConteosDao] Some error occurred while was trying to execute the query: " + INV_VW_REP,
					e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,
						"[getReporteConteosDao] Some error occurred while was trying to close the connection.", e);
			}
		}

		res.setAbstractResult(abstractResult);
		res.setLsObject(listBean);
		return res;
	}

	private static final String INV_VW_REP = "SELECT DOC_INV_ID, DIH_ROUTE_ID,RDESC, DIH_BUKRS, BDESC, WERKS, WDESC,DIH_STATUS, DIH_TYPE, DIH_CREATED_BY, DIH_CREATED_DATE, DIP_LGORT, LGOBE, DIP_LGTYP, LTYPT, DIP_LGPLA,"
			+ " DIP_MATNR, MAKTX, DIP_THEORIC, DIP_COUNTED, DIP_DIFF_COUNTED, DIP_DIFF_FLAG FROM INV_VW_REPORTE_DOC_INV WITH(NOLOCK) WHERE DOC_INV_ID = ?";

	private static final String INV_VW_REP_HEADER = "SELECT DOC_INV_ID, DIH_BUKRS, BUTXT, DIH_WERKS, NAME1, DIH_TYPE, "
			+ "DIH_ROUTE_ID, ROU_DESC, DIH_CREATED_DATE, DIH_MODIFIED_DATE FROM INV_VW_DOC_INV_REP_HEADER WHERE  DOC_INV_ID = ?";

	private static final String INV_VW_REP_POSITIONS = "SELECT DIP_LGORT, LGOBE, LGTYP, LTYPT, DIP_LGPLA, DIP_MATNR, "
			+ " MAKTX, DIP_THEORIC, DIP_COUNTED, DIP_DIFF_COUNTED, IMWM FROM INV_VW_DOC_INV_REP_POSITIONS WITH(NOLOCK) WHERE DOC_INV_ID = ?";

	public Response<ReporteDocInvBeanHeader> getReporteDocInv(DocInvBean docInvBean) {

		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		ReporteDocInvBeanHeader bean = new ReporteDocInvBeanHeader();
		Response<ReporteDocInvBeanHeader> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		List<ReporteDocInvBean> listBean = new ArrayList<ReporteDocInvBean>();
		log.info(INV_VW_REP_HEADER);
		log.info("[getReporteDocInvDao] Preparing sentence...");
		try {
			stm = con.prepareStatement(INV_VW_REP_HEADER);
			stm.setInt(1, docInvBean.getDocInvId());
			log.info("[getReporteDocInvDao] Executing query...");
			ResultSet rs = stm.executeQuery();
			if (rs.next()) {
				bean.setDocInvId(docInvBean.getDocInvId());
				bean.setBukrs(rs.getString("DIH_BUKRS"));
				bean.setBukrsD(rs.getString("BUTXT"));
				bean.setRoute(rs.getString("ROU_DESC"));
				bean.setWerks(rs.getString("DIH_WERKS"));
				bean.setWerksD(rs.getString("NAME1"));
				bean.setType(rs.getString("DIH_TYPE"));
				bean.setCreationDate(rs.getString("DIH_CREATED_DATE"));
				bean.setConciliationDate(rs.getString("DIH_MODIFIED_DATE"));

				log.info(INV_VW_REP_POSITIONS);
				log.info("[getReporteDocInvDao] Preparing sentence...");
				stm = con.prepareStatement(INV_VW_REP_POSITIONS);
				stm.setInt(1, docInvBean.getDocInvId());
				rs = stm.executeQuery();
				while (rs.next()) {
					ReporteDocInvBean positionBean = new ReporteDocInvBean();
					positionBean.setLgort(rs.getString("DIP_LGORT"));
					positionBean.setLgortD(rs.getString("LGOBE"));
					positionBean.setLgtyp(rs.getString("LGTYP"));
					positionBean.setLtypt(rs.getString("LTYPT"));
					positionBean.setLgpla(rs.getString("DIP_LGPLA"));
					positionBean.setMatnr(rs.getString("DIP_MATNR"));
					positionBean.setMatnrD(rs.getString("MAKTX"));
					positionBean.setImwmMarker(rs.getString("IMWM"));
					if (rs.getString("DIP_THEORIC") != null)
						positionBean.setTheoric(rs.getString("DIP_THEORIC"));
					else
						positionBean.setTheoric("");
					positionBean.setCounted(rs.getString("DIP_COUNTED"));
					if (rs.getString("DIP_DIFF_COUNTED") != null)
						positionBean.setDiff(rs.getString("DIP_DIFF_COUNTED"));
					else
						positionBean.setDiff("");

					listBean.add(positionBean);
				}

				List<ReporteDocInvBean> wmList = new ArrayList<>();
				List<ReporteDocInvBean> imList = new ArrayList<>();
				List<ReporteDocInvBean> imPList = new ArrayList<>();

				// Hashmap Llave Material- Almacen, Contenido Lista de Objetos
				// que hagan match material almacen.
				HashMap<String, List<ReporteDocInvBean>> supportMap = new HashMap<>();

				if (!listBean.isEmpty()) {
					for (ReporteDocInvBean singlePos : listBean) {
						if (singlePos.getImwmMarker().equals("WM")) {
							wmList.add(singlePos);
						} else {
							imList.add(singlePos);
						}
					}
					if (!imList.isEmpty()) {
						for (ReporteDocInvBean singleIM : imList) {
							String key = singleIM.getLgort() + singleIM.getMatnr();
							if (supportMap.containsKey(key)) {
								supportMap.get(key).add(singleIM);
							} else {
								List<ReporteDocInvBean> addMapList = new ArrayList<>();
								addMapList.add(singleIM);
								supportMap.put(key, addMapList);
							}
						}
						Iterator iteAux = supportMap.entrySet().iterator();
						while (iteAux.hasNext()) {
							Map.Entry pair = (Map.Entry) iteAux.next();
							ReporteDocInvBean supportBean = null;
							log.info("[getReporteDocInvDao] Iterating hashmap key..." + pair.getKey());
							BigDecimal supportValue = new BigDecimal("0");
							for (ReporteDocInvBean singleIM : (List<ReporteDocInvBean>) pair.getValue()) {
								if (supportBean == null) {
									log.info("[getReporteDocInvDao] support bean null... ");
									supportBean = singleIM;
								}
								log.info("[getReporteDocInvDao] singleImbean: " + singleIM);
								log.info("[getReporteDocInvDao] supportValue prevAssigned: " + supportValue);
								BigDecimal toAdd = new BigDecimal(singleIM.getCounted());
								log.info("[getReporteDocInvDao] toAdd prevAssigned: " + toAdd);
								log.info("[getReporteDocInvDao] singleIM.getCounted() value: " + singleIM.getCounted());
								supportValue.add(toAdd);
								log.info("[getReporteDocInvDao] supportValue afterAssign: " + supportValue);
							}
							log.info("[getReporteDocInvDao] supportValue toAssign: " + supportValue);
							supportBean.setLgtyp("");
							supportBean.setLtypt("");
							supportBean.setLgpla("");
							supportBean.setCounted(String.valueOf(supportValue));
							log.info("[getReporteDocInvDao] final object toAdd: " + supportBean);
							imPList.add(supportBean);
						}
						listBean = new ArrayList<>();
						listBean.addAll(wmList);
						listBean.addAll(imPList);
						bean.setDocInvPosition(listBean);
					} else {
						bean.setDocInvPosition(listBean);
					}
				}
			} else {
				bean = null;
				abstractResult.setResultId(ReturnValues.IERROR);
				abstractResult.setResultMsgAbs(
						"Ocurrio un Error al recuperar los datos de Documento de Invetnario ó Documento Inexistente");
			}
			log.info("[getReporteDocInvDao] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE,
					"[getReporteDocInvDao] Some error occurred while was trying to execute the query: " + INV_VW_REP,
					e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,
						"[getReporteDocInvDao] Some error occurred while was trying to close the connection.", e);
			}
		}

		res.setAbstractResult(abstractResult);
		res.setLsObject(bean);
		return res;
	}

	public Response<List<TareasTiemposLgplaBean>> getReporteTareasTiemposLgpla(TareasTiemposLgplaBean tareasBean) {

		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;

		Response<List<TareasTiemposLgplaBean>> res = new Response<List<TareasTiemposLgplaBean>>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		List<TareasTiemposLgplaBean> listTareasBean = new ArrayList<TareasTiemposLgplaBean>();
		String INV_VW_REP_TAREAS = null;
		INV_VW_REP_TAREAS = "SELECT DOC_INV_ID,ROUTE_ID,RDESC,BUKRS,BDESC,TAS_GROUP_ID,WERKS,WDESC,TASK_ID,ZPO_LGPLA,COU_START_DATE,COU_END_DATE,COU_USER_ID,TIEMPO FROM INV_VW_REPORTE_TAREAS_CARRIL WITH(NOLOCK) ";

		String condition = buildConditionTiemposLgpla(tareasBean);
		if (condition != null) {
			INV_VW_REP_TAREAS += condition;
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
				tareasBean.setTiempo((rs.getString("TIEMPO") == null ? "" : (rs.getString("TIEMPO"))));
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
		} catch (SQLException e) {
			log.log(Level.SEVERE,
					"[getReporteTiemposTareasDao] Some error occurred while was trying to execute the query: "
							+ INV_VW_REP_TAREAS,
					e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,
						"[getReporteTiemposTareasDao] Some error occurred while was trying to close the connection.",
						e);
			}
		}

		res.setAbstractResult(abstractResult);
		res.setLsObject(listTareasBean);
		return res;
	}

	public Response<List<TareasTiemposZonasBean>> getReporteTareasTiemposZonas(TareasTiemposZonasBean tareasBean) {

		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;

		Response<List<TareasTiemposZonasBean>> res = new Response<List<TareasTiemposZonasBean>>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		List<TareasTiemposZonasBean> listTareasBean = new ArrayList<TareasTiemposZonasBean>();
		String INV_VW_REP_TAREAS = null;

		INV_VW_REP_TAREAS = "SELECT DOC_INV_ID,ROUTE_ID,RDESC,BUKRS,BDESC,WERKS,WDESC,TASK_ID,GROUP_ID,ZONE_ID,ZON_DESC,COU_START_DATE,COU_END_DATE,COU_USER_ID,TIEMPO FROM INV_TAREAS_TIEMPOS_ZONAS  WITH(NOLOCK) ";

		String condition = buildConditionTiemposZonas(tareasBean);
		if (condition != null) {
			INV_VW_REP_TAREAS += condition;
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
				tareasBean.setTiempo((rs.getString("TIEMPO") == null ? "" : (rs.getString("TIEMPO"))));
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
		} catch (SQLException e) {
			log.log(Level.SEVERE,
					"[getReporteTiemposTareasZonasDao] Some error occurred while was trying to execute the query: "
							+ INV_VW_REP_TAREAS,
					e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,
						"[getReporteTiemposTareasZonasDao] Some error occurred while was trying to close the connection.",
						e);
			}
		}

		res.setAbstractResult(abstractResult);
		res.setLsObject(listTareasBean);
		return res;
	}

	public Response<List<ReporteCalidadBean>> getReporteCalidad(ReporteCalidadBean bean) {

		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;

		Response<List<ReporteCalidadBean>> res = new Response<List<ReporteCalidadBean>>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		List<ReporteCalidadBean> list = new ArrayList<ReporteCalidadBean>();
		String INV_VW_REP = null;
		INV_VW_REP = "SELECT TAS_DOC_INV_ID,TASK_ID, TAS_GROUP_ID, COU_USER_ID,TAS_DOWLOAD_DATE, TAS_UPLOAD_DATE FROM INV_VW_REPORTE_CALIDAD_CABECERA WITH(NOLOCK) ";

		String condition = buildConditionCalidad(bean);
		if (condition != null) {
			INV_VW_REP += condition;
		}
	
		log.info(INV_VW_REP);
		INV_VW_REP += "GROUP BY TAS_DOC_INV_ID,TASK_ID, TAS_GROUP_ID, COU_USER_ID,TAS_DOWLOAD_DATE, TAS_UPLOAD_DATE";
		log.info("[getReporteCalidadDao] Preparing sentence...");
		try {
			stm = con.prepareStatement(INV_VW_REP);

			log.info("[getReporteCalidadDao] Executing query...");

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {

				bean = new ReporteCalidadBean();
				bean.setDocInvId(rs.getString("TAS_DOC_INV_ID"));
				bean.setTaskId(rs.getString("TASK_ID"));
				bean.setGroupId(rs.getString("TAS_GROUP_ID"));
				bean.setUserId(rs.getString("COU_USER_ID"));
				bean.setDateDowload(rs.getString("TAS_DOWLOAD_DATE"));
				bean.setDateUpload(rs.getString("TAS_UPLOAD_DATE"));
				bean.setConteos(this.getConteos(rs.getInt("TAS_DOC_INV_ID")));
				list.add(bean);
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
			log.info("[getReporteCalidadDao] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE,
					"[getReporteCalidadDao] Some error occurred while was trying to execute the query: " + INV_VW_REP,
					e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,
						"[getReporteCalidadDao] Some error occurred while was trying to close the connection.", e);
			}
		}

		res.setAbstractResult(abstractResult);
		res.setLsObject(list);
		return res;
	}

	private List<ReporteCalidadConteosBean> getConteos(int docInvId) {
		List<ReporteCalidadConteosBean> list = new ArrayList<>();

		ConciliacionBean docInvBean = new ConciliacionBean();
		docInvBean.setDocInvId(docInvId);
		ConciliacionDao dao = new ConciliacionDao();
		List<ConciliationPositionBean> conteos = dao.getConciliationPositions(docInvBean);

		if (conteos.size() > 0) {
			for (int i = 0; i < conteos.size(); i++) {
				ConciliationPositionBean row = conteos.get(i);
				ReporteCalidadConteosBean bean = new ReporteCalidadConteosBean();

				bean.setZoneId(row.getZoneId());
				bean.setZoneD(row.getZoneD());
				bean.setMatnr(row.getMatnr());
				bean.setMatnrD(row.getMatnrD());
				bean.setLgpla(row.getLgpla());
				bean.setMeasureUnit(row.getMeasureUnit());

				bean.setCount1A(row.getCount1A());
				bean.setCount1B(row.getCount1B());
				bean.setCount2(row.getCount2());
				bean.setCount3(row.getCount3());

				String calidad = "-";
				String count1A = bean.getCount1A() != null ? bean.getCount1A() : "";
				String count1B = bean.getCount1B() != null ? bean.getCount1B() : "";
				String count2 = bean.getCount2() != null ? bean.getCount2() : "";

				if (count1A.equalsIgnoreCase(count1B) && count1A.length() > 0 && count1B.length() > 0) {
					calidad = "1A-1B";
				} else {
					if (!count1A.equalsIgnoreCase(count1B) && count1A.equalsIgnoreCase(count2) && count1A.length() > 0
							&& count2.length() > 0) {
						calidad = "1A";
					} else {
						if (!count1A.equalsIgnoreCase(count1B) && count1B.equalsIgnoreCase(count2)
								&& count1B.length() > 0 && count2.length() > 0) {
							calidad = "1B";
						}
					}
				}

				bean.setCalidad(calidad);
				list.add(bean);
			}
		}

		return list;
	}

	private String buildConditionCalidad(ReporteCalidadBean bean) {
		String docInvId = "";
		String taskId = "";
		String groupId = "";
		String user = "";
		String dowload = "";
		String upload = "";

		String condition = "";

		docInvId = (bean.getDocInvId() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " ROUTE_ID = '" + bean.getDocInvId() + "' "
				: "";
		condition += docInvId;
		taskId = (bean.getTaskId() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " BUKRS = '" + bean.getTaskId() + "' " : "";
		condition += taskId;
		groupId = (bean.getGroupId() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " WERKS = '" + bean.getGroupId() + "' " : "";
		condition += groupId;
		user = (bean.getUserId() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " RDESC = '" + bean.getUserId() + "' " : "";
		condition += user;
		dowload = (bean.getDateDowload() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " USER_COUNT = '" + bean.getDateDowload() + "' "
				: "";
		condition += dowload;
		upload = (bean.getDateUpload() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " DOC_INV_ID = '" + bean.getDateUpload() + "' "
				: "";
		condition += upload;

		condition = condition.isEmpty() ? null : condition;
		return condition;
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
		user = (apegosB.getUserCount() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ")
				+ " USER_COUNT = '" + apegosB.getUserCount() + "' " : "";
		condition += user;
		docInv = (apegosB.getDocInvId() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " DOC_INV_ID = '" + apegosB.getDocInvId() + "' "
				: "";
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
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " ROUTE_ID = '" + Bean.getRouteId() + "' " : "";
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
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " COU_USER_ID = '" + Bean.getUserID() + "' "
				: "";
		condition += user;
		docInv = (Bean.getDocInvID() > 0) ? (condition.contains("WHERE") ? " AND " : " WHERE ") + " TAS_DOC_INV_ID = '"
				+ Bean.getDocInvID() + "' " : "";
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
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " COU_USER_ID = '" + tareasB.getUser() + "' "
				: "";
		condition += user;
		docInv = (tareasB.getDocInvId() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " DOC_INV_ID = '" + tareasB.getDocInvId() + "' "
				: "";
		condition += docInv;
		lgpla = (tareasB.getLgpla() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " ZPO_LGPLA = '" + tareasB.getLgpla() + "' "
				: "";
		condition += lgpla;

		start = (tareasB.getDateIni() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ")
				+ " COU_START_DATE = '" + tareasB.getDateIni() + "' " : "";
		condition += start;

		end = (tareasB.getDateFin() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ") + " COU_END_DATE = '"
				+ tareasB.getDateFin() + "' " : "";
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
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " COU_USER_ID = '" + tareasB.getUser() + "' "
				: "";
		condition += user;
		docInv = (tareasB.getDocInvId() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " DOC_INV_ID = '" + tareasB.getDocInvId() + "' "
				: "";
		condition += docInv;

		zone = (tareasB.getZoneId() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " ZONE_ID = '" + tareasB.getZoneId() + "' "
				: "";
		condition += zone;

		zoneD = (tareasB.getZoneD() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " ZON_DESC = '" + tareasB.getZoneD() + "' "
				: "";
		condition += zoneD;

		start = (tareasB.getDateIni() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ")
				+ " COU_START_DATE = '" + tareasB.getDateIni() + "' " : "";
		condition += start;

		end = (tareasB.getDateFin() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ") + " COU_END_DATE = '"
				+ tareasB.getDateFin() + "' " : "";
		condition += end;

		condition = condition.isEmpty() ? null : condition;
		return condition;
	}

}
