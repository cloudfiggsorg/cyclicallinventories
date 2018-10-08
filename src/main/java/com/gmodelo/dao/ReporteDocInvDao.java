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
import com.gmodelo.beans.ReporteDocInvBean;
import com.gmodelo.beans.Response;
import com.gmodelo.utils.ConnectionManager;
import com.gmodelo.utils.ReturnValues;

public class ReporteDocInvDao {
	
	private Logger log = Logger.getLogger(ReporteDocInvDao.class.getName());
	
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
			String condition = buildCondition(bean);
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
	
	private String buildCondition(ReporteDocInvBean Bean) {
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
