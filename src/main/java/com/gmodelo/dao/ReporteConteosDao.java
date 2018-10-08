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
import com.gmodelo.beans.ReporteConteosBean;
import com.gmodelo.beans.Response;
import com.gmodelo.utils.ConnectionManager;
import com.gmodelo.utils.ReturnValues;

public class ReporteConteosDao {
	
	private Logger log = Logger.getLogger(ReporteConteosDao.class.getName());
	
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
			String condition = buildCondition(conteosBean);
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
	
	private String buildCondition(ReporteConteosBean Bean) {
		String routeId = "";
		String bukrs = "";
		String werks = "";
		String rdesc = "";
		String user = "";
		
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
		condition = condition.isEmpty() ? null : condition;
		return condition;
	}

}
