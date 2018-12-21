package com.gmodelo.cyclicinventories.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.cyclicinventories.beans.AbstractResultsBean;
import com.gmodelo.cyclicinventories.beans.ApegosBean;
import com.gmodelo.cyclicinventories.beans.ConciliationsIDsBean;
import com.gmodelo.cyclicinventories.beans.CostByMatnr;
import com.gmodelo.cyclicinventories.beans.DocInvBean;
import com.gmodelo.cyclicinventories.beans.DocInvBeanHeaderSAP;
import com.gmodelo.cyclicinventories.beans.E_Lqua_SapEntity;
import com.gmodelo.cyclicinventories.beans.E_Mard_SapEntity;
import com.gmodelo.cyclicinventories.beans.E_Mseg_SapEntity;
import com.gmodelo.cyclicinventories.beans.E_Msku_SapEntity;
import com.gmodelo.cyclicinventories.beans.PosDocInvBean;
import com.gmodelo.cyclicinventories.beans.ProductivityBean;
import com.gmodelo.cyclicinventories.beans.ReporteConteosBean;
import com.gmodelo.cyclicinventories.beans.ReporteDocInvBean;
import com.gmodelo.cyclicinventories.beans.ReporteDocInvBeanHeader;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.utils.ConnectionManager;
import com.gmodelo.cyclicinventories.utils.ReturnValues;

public class ReportesDao {

	private Logger log = Logger.getLogger(ReportesDao.class.getName());
	private SapOperationDao sod = new SapOperationDao();

	public Response<List<ApegosBean>> getReporteApegos(ApegosBean apegosBean) {

		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;

		Response<List<ApegosBean>> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		List<ApegosBean> listApegosBean = new ArrayList<>();
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
			
			if(apegosBean.getDateIni() != null && apegosBean.getDateFin() == null){
				java.util.Date utilDate = new java.util.Date(Long.parseLong(apegosBean.getDateIni()));
			    java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
				stm.setString(1, sqlDate + "%");
			}
			
			if(apegosBean.getDateIni() == null && apegosBean.getDateFin() != null){
				java.util.Date utilDate = new java.util.Date(Long.parseLong(apegosBean.getDateFin()));
			    java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
				stm.setString(1, sqlDate + "%");
			}
			
			if(apegosBean.getDateIni() != null && apegosBean.getDateFin() != null){
				java.util.Date utilDate = new java.util.Date(Long.parseLong(apegosBean.getDateIni()));
			    java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
				stm.setString(1, sqlDate + "");
				java.util.Date utilDate2 = new java.util.Date(Long.parseLong(apegosBean.getDateFin()));
			    java.sql.Date sqlDate2 = new java.sql.Date(utilDate2.getTime());
				stm.setString(2, sqlDate2 + "");
			}
			
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

		Response<List<ReporteConteosBean>> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		List<ReporteConteosBean> listBean = new ArrayList<>();
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
			+ " MAKTX,MEINS, DIP_THEORIC, DIP_COUNTED, DIP_DIFF_COUNTED, IMWM FROM INV_VW_DOC_INV_REP_POSITIONS WITH(NOLOCK) WHERE DOC_INV_ID = ?";
	
	public Response<ReporteDocInvBeanHeader> getReporteDocInv(DocInvBean docInvBean) {
		
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		ReporteDocInvBeanHeader bean = new ReporteDocInvBeanHeader();
		Response<ReporteDocInvBeanHeader> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		List<ReporteDocInvBean> listBean = new ArrayList<>();
		log.info(INV_VW_REP_HEADER);
		log.info("[getReporteDocInvDao] Preparing sentence...");
		try {
			stm = con.prepareStatement(INV_VW_REP_HEADER);
			stm.setInt(1, docInvBean.getDocInvId());
			log.info("[getReporteDocInvDao] Executing query...");
			ResultSet rs = stm.executeQuery();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd - HH:mm:ss");
			if (rs.next()) {
				bean.setDocInvId(docInvBean.getDocInvId());
				bean.setBukrs(rs.getString("DIH_BUKRS"));
				bean.setBukrsD(rs.getString("BUTXT"));
				bean.setRoute(rs.getString("ROU_DESC"));
				bean.setWerks(rs.getString("DIH_WERKS"));
				bean.setWerksD(rs.getString("NAME1"));
				bean.setType(rs.getString("DIH_TYPE"));
				bean.setCreationDate(sdf.format(new Date(rs.getTimestamp("DIH_CREATED_DATE").getTime())));
				bean.setConciliationDate(sdf.format(new Date(rs.getTimestamp("DIH_MODIFIED_DATE").getTime())));
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
					positionBean.setMeins(rs.getString("MEINS"));
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
							for (ReporteDocInvBean singleIM : (List<ReporteDocInvBean>) pair.getValue()) {
								if (supportBean == null) {
									log.info("[getReporteDocInvDao] support bean null... ");
									supportBean = singleIM;
								} else {
									supportBean.setCounted(String.valueOf(new BigDecimal(supportBean.getCounted())
											.add(new BigDecimal(singleIM.getCounted()))));
								}
								log.info("[getReporteDocInvDao] fos Single IM : " + singleIM);
								log.info("[getReporteDocInvDao] for supportBean: " + supportBean);
							}
							supportBean.setLgtyp("");
							supportBean.setLtypt("");
							supportBean.setLgpla("");
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
	
	public Response<DocInvBeanHeaderSAP> getConsDocInv(DocInvBean docInvBean) {
		
		if(docInvBean.getStatus().equalsIgnoreCase("TRUE")){
			return new SapConciliationDao().getClosedConsSapReport(docInvBean);
		}else{
			return getNoClosedConsSapReport(docInvBean);
		}
		
	}
	
	private static final String INV_VW_REP_POS_SAP = "SELECT DIP_LGORT, " 
	+ "CASE "
		+ "WHEN IMWM = 'IM' THEN NULL "
		+ "WHEN IMWM = 'WM' THEN (SELECT TOP 1 LGNUM "
			+ "FROM INV_VW_NGORT_WITH_GORT INVG " 
			+ "WHERE WERKS = ? " 
			+ "AND LEN(LGNUM) > 0) "
	+ "END LGNUM, LGTYP, DIP_LGPLA, DIP_MATNR, " 
	+ "MAKTX,MEINS, DIP_THEORIC, DIP_COUNTED, DIP_DIFF_COUNTED, IMWM " 
	+ "FROM INV_VW_DOC_INV_REP_POSITIONS WITH(NOLOCK) WHERE DOC_INV_ID = ?";
	
	public Response<DocInvBeanHeaderSAP> getNoClosedConsSapReport(DocInvBean docInvBean) {
		
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		DocInvBeanHeaderSAP bean = new DocInvBeanHeaderSAP();
		Response<DocInvBeanHeaderSAP> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		List<PosDocInvBean> listBean = new ArrayList<>();
		String lsMatnr = "";
		E_Mseg_SapEntity emse;
		
		
		log.info(INV_VW_REP_HEADER);
		log.info("[getReporteDocInvDao] Preparing sentence...");
		try {
			stm = con.prepareStatement(INV_VW_REP_HEADER);
			stm.setInt(1, docInvBean.getDocInvId());
			log.info("[getReporteDocInvDao] Executing query...");
			ResultSet rs = stm.executeQuery();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd - HH:mm:ss");
			if (rs.next()) {
				bean.setDocInvId(docInvBean.getDocInvId());
				bean.setBukrs(rs.getString("DIH_BUKRS"));
				bean.setBukrsD(rs.getString("BUTXT"));
				bean.setRoute(rs.getString("ROU_DESC"));
				bean.setWerks(rs.getString("DIH_WERKS"));
				bean.setWerksD(rs.getString("NAME1"));
				bean.setType(rs.getString("DIH_TYPE"));
				bean.setCreationDate(sdf.format(new Date(rs.getTimestamp("DIH_CREATED_DATE").getTime())));
				bean.setConciliationDate(sdf.format(new Date(rs.getTimestamp("DIH_MODIFIED_DATE").getTime())));
				log.info(INV_VW_REP_POS_SAP);
				log.info("[getReporteDocInvDao] Preparing sentence...");
				
				stm = con.prepareStatement(INV_VW_REP_POS_SAP);				
				stm.setString(1, docInvBean.getWerks());
				stm.setInt(2, docInvBean.getDocInvId());
				
				rs = stm.executeQuery();				
				while (rs.next()) {
					
					PosDocInvBean positionBean = new PosDocInvBean();
					positionBean.setDoncInvId(docInvBean.getDocInvId());
					positionBean.setLgort(rs.getString("DIP_LGORT"));
					positionBean.setLgNum(rs.getString("LGNUM"));
					positionBean.setLgtyp(rs.getString("LGTYP"));
					positionBean.setLgpla(rs.getString("DIP_LGPLA"));
					positionBean.setMatnr(rs.getString("DIP_MATNR"));
					positionBean.setMatnrD(rs.getString("MAKTX"));
					positionBean.setMeins(rs.getString("MEINS"));
					positionBean.setCounted(rs.getString("DIP_COUNTED"));
					positionBean.setImwmMarker(rs.getString("IMWM"));					
					emse = new E_Mseg_SapEntity();
					
					//Set the movements by matnr on theoric
					if(positionBean.getImwmMarker().equalsIgnoreCase("IM")){
						
						emse.setLgort(positionBean.getLgort());
						emse.setMatnr(positionBean.getMatnr());
						positionBean.setTheoric(Double.toString(new SapOperationDao().getMatnrMovementsIM(emse, con)));
					}else{
						 		
						emse.setLgort(positionBean.getLgort());
						emse.setLgnum(positionBean.getLgNum());						
						emse.setLgtyp(positionBean.getLgtyp());
						emse.setLgpla(positionBean.getLgpla());
						emse.setMatnr(positionBean.getMatnr());						
						positionBean.setTheoric(Double.toString(new SapOperationDao().getMatnrMovementsWM(emse, con)));
					}
					
					listBean.add(positionBean);
					lsMatnr += positionBean.getMatnr() + ", ";
				}
				
				ArrayList<E_Mard_SapEntity> lsTheoricIM = sod.getMatnrTheoricIM(docInvBean.getDocInvId(), con);
				ArrayList<E_Lqua_SapEntity> lsTheoricWM = sod.getMatnrTheoricWM(docInvBean.getDocInvId(), con);
				boolean found = false;
				double theoric = 0;
				
				//Sum the theoric before group by matnr
				for(PosDocInvBean pb: listBean){
				
					found = false;
					
					//Set the theoric 
					if (pb.getImwmMarker().equals("WM")) {
						
						for(E_Lqua_SapEntity ojb: lsTheoricWM){//Set the theoric WM for this matnr
																				
							if(ojb.getMatnr().contentEquals(pb.getMatnr())){
								
								found = true;								
								theoric = Double.parseDouble(pb.getTheoric());
								theoric += Double.parseDouble(ojb.getVerme());
								pb.setTheoric(Double.toString(theoric));
								break;
							}
						}	
													
					} else {
						
						for(E_Mard_SapEntity ojb: lsTheoricIM){//Set the theoric IM for this matnr
																			
							if(ojb.getMatnr().contentEquals(pb.getMatnr()) ){
								
								found = true;
								theoric = Double.parseDouble(pb.getTheoric());
								theoric += Double.parseDouble(ojb.getRetme());
								pb.setTheoric(Double.toString(theoric));
								break;
							}
						}	
					}					
				}
				
				//Group by matnr
				HashMap<String, PosDocInvBean> mapByMatNr = new HashMap<>();				
				PosDocInvBean pbAux = null;
				double sumCounted = 0;
				theoric = 0;
				
				for(PosDocInvBean pb: listBean){
					
					if (mapByMatNr.containsKey(pb.getMatnr())) {
						
						pbAux = mapByMatNr.get(pb.getMatnr());
						sumCounted = Double.parseDouble(pbAux.getCounted());
						sumCounted += Double.parseDouble(pb.getCounted());
						pbAux.setCounted(Double.toString(sumCounted));
						theoric = Double.parseDouble(pbAux.getTheoric());
						theoric += Double.parseDouble(pb.getTheoric());
						pbAux.setTheoric(Double.toString(theoric));
						
						mapByMatNr.replace(pb.getMatnr(), pbAux);
					} else {
						mapByMatNr.put(pb.getMatnr(), pb);
					}
				}
				
				//Reset the list and make a new one with filtered values
				listBean.clear();
				
				for (Map.Entry<String, PosDocInvBean> mapEntry : mapByMatNr.entrySet()){
					listBean.add(mapEntry.getValue());
				}				
								
				ArrayList<E_Mseg_SapEntity> lsTransit = sod.getMatnrOnTransit(docInvBean.getDocInvId(), con);
				ArrayList<E_Msku_SapEntity> lsCons = sod.getMatnrOnCons(docInvBean.getDocInvId(), con);
				ArrayList<CostByMatnr> lsMatnrCost = sod.getCostByMatnr(lsMatnr, bean.getWerks(), con);
																			
				if (!listBean.isEmpty()) {
					for (PosDocInvBean singlePos : listBean) {
						
						found = false;
						
						//Set the value per matnr
						for(CostByMatnr matnrCost: lsMatnrCost){
							
							if(matnrCost.getMatnr().contentEquals(singlePos.getMatnr())){
								
								found = true;
								singlePos.setCostByUnit(matnrCost.getCost());
								break;
							}
						}
						
						if(!found){
							singlePos.setCostByUnit("0");
						}
						
						found = false;
						
						//Set the transit measure for this matnr
						for(E_Mseg_SapEntity ojb: lsTransit){
							
							if(ojb.getMatnr().contentEquals(singlePos.getMatnr())){
								
								found = true;
								singlePos.setTransit(ojb.getMeins());
								break;
							}
						}
						
						if(!found){
							singlePos.setTransit("0");
						}
						
						//Set the consignation measure for this matnr
						for(E_Msku_SapEntity ojb: lsCons){
							
							if(ojb.getMatnr().contentEquals(singlePos.getMatnr())){
								
								found = true;
								singlePos.setConsignation(ojb.getKulab());
								break;
							}
						}
						
						if(!found){
							singlePos.setConsignation("0");
						}
																	
					}										
				}
				
				bean.setDocInvPosition(listBean);
			} else {
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
	
	public Response<List<ProductivityBean>> getCountedProductivityDao(ProductivityBean tareasBean) {

		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;

		Response<List<ProductivityBean>> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		List<ProductivityBean> listTareasBean = new ArrayList<>();
		String INV_VW_COUNTED_PRODUCTIVITY_REPORT  = "SELECT DOC_INV_ID, DIH_BUKRS, DIH_WERKS,"
				+ " DIH_ROUTE_ID, DIP_LGORT, COUNTEDLGPLA, DIH_CREATED_DATE, ASSIGNEDGROUP "
				+ " FROM INV_VW_COUNTED_PRODUCTIVITY_REPORT  WITH(NOLOCK) ";

		String condition = buildConditionCountedProductivity(tareasBean);
		if (condition != null) {
			INV_VW_COUNTED_PRODUCTIVITY_REPORT += condition;
		}

		log.info(INV_VW_COUNTED_PRODUCTIVITY_REPORT);
		log.info("[getCountedProductivityDao] Preparing sentence...");
		try {
			stm = con.prepareStatement(INV_VW_COUNTED_PRODUCTIVITY_REPORT);

			log.info("[getCountedProductivityDao] Executing query...");

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {

				tareasBean = new ProductivityBean();
				tareasBean.setDocInvId(rs.getString("DOC_INV_ID"));
				tareasBean.setRouteId(rs.getString("DIH_ROUTE_ID"));
				tareasBean.setBukrs(rs.getString("DIH_BUKRS"));
				tareasBean.setWerks(rs.getString("DIH_WERKS"));
				tareasBean.setLgort(rs.getString("DIP_LGORT"));
				tareasBean.setDateIni(rs.getString("DIH_CREATED_DATE"));
				tareasBean.setUser(rs.getString("ASSIGNEDGROUP"));
				tareasBean.setCountedLgpla(String.valueOf(rs.getInt("COUNTEDLGPLA")));
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
			log.info("[getCountedProductivityDao] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE,
					"[getCountedProductivityDao] Some error occurred while was trying to execute the query: "
							+ INV_VW_COUNTED_PRODUCTIVITY_REPORT,
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
						"[getCountedProductivityDao] Some error occurred while was trying to close the connection.",
						e);
			}
		}

		res.setAbstractResult(abstractResult);
		res.setLsObject(listTareasBean);
		return res;
	}

	public Response<List<ProductivityBean>> getUserProductivityDao(ProductivityBean tareasBean) {

		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;

		Response<List<ProductivityBean>> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		List<ProductivityBean> listTareasBean = new ArrayList<>();
		String INV_VW_USER_PRODUCTIVITY_REPORT = null;

		INV_VW_USER_PRODUCTIVITY_REPORT = "SELECT DOC_INV_ID,DIH_BUKRS,DIH_WERKS,RPO_ZONE_ID,"
				+ "TAS_DOWLOAD_DATE,TAS_UPLOAD_DATE,COU_USER_ID,DIH_ROUTE_ID, DIP_LGORT, COUNTEDLGPLA, "
				+ "DATEDIFF ( minute , TAS_DOWLOAD_DATE , TAS_UPLOAD_DATE ) AS TIME   FROM INV_VW_USER_PRODUCTIVITY_REPORT  WITH(NOLOCK) ";

		String condition = buildConditionProductivity(tareasBean);
		if (condition != null) {
			INV_VW_USER_PRODUCTIVITY_REPORT += condition;
		}
		
		INV_VW_USER_PRODUCTIVITY_REPORT += " ORDER BY DOC_INV_ID ASC, TAS_DOWLOAD_DATE";
		log.info(INV_VW_USER_PRODUCTIVITY_REPORT);
		log.info("[getUserProductivityDao] Preparing sentence...");
		try {
			stm = con.prepareStatement(INV_VW_USER_PRODUCTIVITY_REPORT);

			log.info("[getUserProductivityDao] Executing query...");

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {

				tareasBean = new ProductivityBean();
				tareasBean.setDocInvId(rs.getString("DOC_INV_ID"));
				tareasBean.setRouteId(rs.getString("DIH_ROUTE_ID"));
				tareasBean.setBukrs(rs.getString("DIH_BUKRS"));
				tareasBean.setWerks(rs.getString("DIH_WERKS"));
				tareasBean.setLgort(rs.getString("DIP_LGORT"));
				tareasBean.setZoneId(rs.getString("RPO_ZONE_ID"));
				tareasBean.setDateIni(rs.getString("TAS_DOWLOAD_DATE"));
				tareasBean.setDateFin((rs.getString("TAS_UPLOAD_DATE") == null) ? "" : (rs.getString("TAS_UPLOAD_DATE")));
				tareasBean.setTiempo((rs.getString("TIME") == null ? "" : (rs.getString("TIME"))));
				tareasBean.setUser(rs.getString("COU_USER_ID"));
				tareasBean.setCountedLgpla(String.valueOf(rs.getInt("COUNTEDLGPLA")));
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
			log.info("[getUserProductivityDao] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE,
					"[getUserProductivityDao] Some error occurred while was trying to execute the query: "
							+ INV_VW_USER_PRODUCTIVITY_REPORT,
					e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,
						"[getUserProductivityDao] Some error occurred while was trying to close the connection.",
						e);
			}
		}

		res.setAbstractResult(abstractResult);
		res.setLsObject(listTareasBean);
		return res;
	}
	
	private static final String GENERATE_IDDESC_QUALITY_REPORT = "SELECT DOC_INV_ID as DOC_INV, (CONVERT(VARCHAR, DOC_INV_ID) + ' - ' + CONVERT(VARCHAR,inr.ROU_DESC)) as DESCRIPCION "
			+ " FROM INV_DOC_INVENTORY_HEADER idih WITH(NOLOCK) "
			+ " INNER JOIN INV_ROUTE inr WITH(NOLOCK) ON idih.DIH_ROUTE_ID = inr.ROUTE_ID WHERE idih.DIH_STATUS = '0'"
			+ " AND idih.DOC_FATHER_INV_ID IS NULL AND inr.ROU_TYPE = '2' AND idih.DIH_BUKRS LIKE  ? AND idih.DIH_WERKS LIKE ?";

	public Response<List<ConciliationsIDsBean>> getReporteCalidad(String bukrs, String werks) {

		Response<List<ConciliationsIDsBean>> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		List<ConciliationsIDsBean> listConIds = new ArrayList<>();
		ConciliationsIDsBean conciliationIDsBean;

		try {
			stm = con.prepareStatement(GENERATE_IDDESC_QUALITY_REPORT);

			if (bukrs != null && werks != null) {
				stm.setString(1, bukrs);
				stm.setString(2, werks);

			} else {
				stm.setString(1, "%");
				stm.setString(2, "%");
			}

			log.info(GENERATE_IDDESC_QUALITY_REPORT);

			log.info("[getReporteCalidadDao] Executing query...");
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {

				conciliationIDsBean = new ConciliationsIDsBean();
				conciliationIDsBean.setId(rs.getString("DOC_INV"));
				conciliationIDsBean.setDesc(rs.getString("DESCRIPCION"));

				listConIds.add(conciliationIDsBean);
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
			log.log(Level.SEVERE, "[getReporteCalidadDao] Some error occurred while was trying to execute the query: "
					+ GENERATE_IDDESC_QUALITY_REPORT, e);
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
		res.setLsObject(listConIds);
		return res;
	
	}

	private String buildConditionApegos(ApegosBean apegosB) {
		String routeId = "";
		String bukrs = "";
		String werks = "";
		String rdesc = "";
		String user = "";
		String docInv = "";
		String lgort = "";
		String lgortD = "";
		String condition = "";
		String dateIni = "";
		String dateFin = "";
		String fechas = "";
		
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
		
		dateIni = (apegosB.getDateIni() != null && apegosB.getDateFin() == null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " CONVERT(VARCHAR(25), DATE_INI, 126) LIKE ? " : "";
		condition += dateIni;
		
		dateFin = (apegosB.getDateIni() == null && apegosB.getDateFin() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " CONVERT(VARCHAR(25), DATE_FIN, 126) LIKE ? " : "";
		condition += dateFin;

		fechas = (apegosB.getDateIni() != null && apegosB.getDateFin() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " CONVERT(VARCHAR(25), DATE_INI, 126) >= ? AND CONVERT(VARCHAR(25), DATE_FIN, 126) <= ? " : "";
		condition += fechas;

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

	private String buildConditionCountedProductivity(ProductivityBean tareasB) {
		
		String bukrs = "";
		String werks = "";
		String lgort = "";
		String user = "";
		String condition = "";
		String zone = "";
		String dates = "";
		
		bukrs = (tareasB.getBukrs() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " DIH_BUKRS = '" + tareasB.getBukrs() + "' " : "";
		condition += bukrs;
		werks = (tareasB.getWerks() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " DIH_WERKS = '" + tareasB.getWerks() + "' " : "";
		condition += werks;
		lgort = (tareasB.getLgort() != null)
				?(condition.contains("WHERE") ? " AND " : " WHERE ") + " DIP_LGORT = '" + tareasB.getLgort() + "' " : "";
		condition += lgort;
		user = (tareasB.getUser() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " COU_USER_ID = '" + tareasB.getUser() + "' "
				: "";
		condition += user;
		zone = (tareasB.getZoneId() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " RPO_ZONE_ID = '" + tareasB.getZoneId() + "' "
				: "";
		condition += zone;
		if(tareasB.getDateIni() != null && tareasB.getDateFin() != null){
			dates = (condition.contains("WHERE") ? " AND " : " WHERE ") + " DIH_CREATED_DATE BETWEEN '" + new java.sql.Date(Long.parseLong((tareasB.getDateIni()))) + "' "
					+( " AND  '" + new java.sql.Date(Long.parseLong(tareasB.getDateFin())) + "' ");
		}else if(tareasB.getDateIni() != null && tareasB.getDateFin() == null){
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(Long.parseLong(tareasB.getDateIni()));
			c.add(Calendar.DATE, 1);
			
			dates = (condition.contains("WHERE") ? " AND " : " WHERE ") + " DIH_CREATED_DATE BETWEEN '" + new java.sql.Date(Long.parseLong((tareasB.getDateIni()))) + "' "
					+( " AND  '" + new java.sql.Date(c.getTimeInMillis()) + "' ");
		}
		condition += dates;

		condition = condition.isEmpty() ? null : condition;
		return condition;
	}

	private String buildConditionProductivity(ProductivityBean tareasB) {
		
		String bukrs = "";
		String werks = "";
		String lgort = "";
		String user = "";
		String condition = "";
		String zone = "";
		String dates = "";
		bukrs = (tareasB.getBukrs() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " DIH_BUKRS = '" + tareasB.getBukrs() + "' " : "";
		condition += bukrs;
		werks = (tareasB.getWerks() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " DIH_WERKS = '" + tareasB.getWerks() + "' " : "";
		condition += werks;
		lgort = (tareasB.getLgort() != null)
				?(condition.contains("WHERE") ? " AND " : " WHERE ") + " DIP_LGORT = '" + tareasB.getLgort() + "' " : "";
		condition += lgort;
		user = (tareasB.getUser() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " COU_USER_ID = '" + tareasB.getUser() + "' "
				: "";
		condition += user;
		zone = (tareasB.getZoneId() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " RPO_ZONE_ID = '" + tareasB.getZoneId() + "' "
				: "";
		condition += zone;
		if(tareasB.getDateIni() != null && tareasB.getDateFin() != null){
			dates = (condition.contains("WHERE") ? " AND " : " WHERE ") + " TAS_DOWLOAD_DATE BETWEEN '" + new java.sql.Date(Long.parseLong((tareasB.getDateIni()))) + "' "
					+( " AND  '" + new java.sql.Date(Long.parseLong(tareasB.getDateFin())) + "' ");
		}else if(tareasB.getDateIni() != null && tareasB.getDateFin() == null){
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(Long.parseLong(tareasB.getDateIni()));
			c.add(Calendar.DATE, 1);
			
			dates = (condition.contains("WHERE") ? " AND " : " WHERE ") + " TAS_DOWLOAD_DATE BETWEEN '" + new java.sql.Date(Long.parseLong((tareasB.getDateIni()))) + "' "
					+( " AND  '" + new java.sql.Date(c.getTimeInMillis()) + "' ");
		}
		condition += dates;

		condition = condition.isEmpty() ? null : condition;
		return condition;
	}

}