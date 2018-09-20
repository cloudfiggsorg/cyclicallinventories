package com.gmodelo.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.beans.AbstractResultsBean;
import com.gmodelo.beans.LgTypIM;
import com.gmodelo.beans.Response;
import com.gmodelo.beans.RouteBean;
import com.gmodelo.utils.ConnectionManager;
import com.gmodelo.utils.ReturnValues;

public class LgTypIMDao {
	
	private Logger log = Logger.getLogger(LgTypIMDao.class.getName());

	public Response<LgTypIM> addRoute(LgTypIM lgTypIM, String createdBy) {
		Response<LgTypIM> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();

		final String INV_SP_ADD_LGTYPE_IM = "INV_SP_ADD_LGTYPE_IM ?, ?, ?, ?, ?, ?, ?"; 		
		final String INV_SP_DEL_LGPLA_IM = "INV_SP_DEL_LGPLA_IM ?, ?";				
		final String INV_SP_ADD_LGPLA_IM = "INV_SP_ADD_LGPLA_IM ?, ?, ?, ?, ?";
		
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		CallableStatement cs = null;

		log.info("[addRoute] Preparing sentence...");

		try {
			
			con.setAutoCommit(false);
			// ADD ROUTE
			
			cs = con.prepareCall(INV_SP_ADD_LGTYPE_IM);
			cs.setString(1, lgTypIM.getLgTyp());
			cs.setString(2, lgTypIM.getLtypt());
			cs.setString(3, lgTypIM.getBukrs());
			cs.setString(4, lgTypIM.getWerks());
			cs.setString(5, lgTypIM.getLgort());
			cs.setString(6, lgTypIM.getLgnum());
			cs.setString(7, createdBy);
			cs.registerOutParameter(1, Types.INTEGER);

			log.info("[addLGTYP] Executing query...");
			cs.execute();
			
			lgTypIM.setLgTyp(cs.getString(1));
			
			//Eliminar posiciones
			String ids = "";
			for (int i = 0; i < lgTypIM.getLsLgPla().size(); i++) {
								
				if(lgTypIM.getLsLgPla().get(i).getLgPlaId() > 0){
					ids += lgTypIM.getLsLgPla().get(i).getLgPlaId() + ",";
				}				
			}
												
			cs = null;
			cs = con.prepareCall(INV_SP_DEL_LGPLA_IM);
			cs.setString(1, lgTypIM.getLgTyp());
			cs.setString(2, ids);
			cs.execute();
			
			// INSERTAR POSICIONES
			for (int i = 0; i < lgTypIM.getLsLgPla().size(); i++) {
				
				cs = null;
				log.info("[addLGTYPPosition] Preparing sentence...");
				cs = con.prepareCall(INV_SP_ADD_LGPLA_IM);
				cs.setInt(1, lgTypIM.getLsLgPla().get(i).getLgPlaId());
				cs.setString(2, lgTypIM.getLsLgPla().get(i).getGltypId());
				cs.setString(3, lgTypIM.getLsLgPla().get(i).getDescription());
				cs.setByte(4, (byte) (lgTypIM.getLsLgPla().get(i).isStatus()? 1 : 0));
				cs.setString(5, createdBy);
				cs.registerOutParameter(1, Types.INTEGER);

				log.info("[addLGTYPPosition] Executing query...");
				cs.execute(); 
				lgTypIM.getLsLgPla().get(i).setLgPlaId(cs.getInt(1));
			}
			
			log.info("[addLGTYP] Sentence successfully executed.");

			// Retrive the warnings if there're
			SQLWarning warning = cs.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING, "[addRoute] " + warning.getMessage());
				warning = warning.getNextWarning();
			}

			con.commit();
			// Free resources
			cs.close();

		} catch (SQLException e) {
			try {
				//deshace todos los cambios realizados en los datos
				log.log(Level.WARNING,"[addLGTYP] Execute rollback");
				con.rollback();
			} catch (SQLException e1) {
				log.log(Level.SEVERE, "[addLGTYP] Not rollback .", e);
			}
			log.log(Level.SEVERE,
					"[addLGTYP] Some error occurred while was trying to insert an LGTYP", e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE, "[addLGTYP] Some error occurred while was trying to close the connection.", e);
			}
		}
		res.setAbstractResult(abstractResult);
		res.setLsObject(lgTypIM);
		return res;
	}
	
//	public Response<List<LgTypIM>> getRoutes(LgTypIM lgTypIM, String searchFilter) {
//		ConnectionManager iConnectionManager = new ConnectionManager();
//		Connection con = iConnectionManager.createConnection();
//		PreparedStatement stm = null;
//
//		Response<List<RouteBean>> res = new Response<List<RouteBean>>();
//		AbstractResultsBean abstractResult = new AbstractResultsBean();
//		List<RouteBean> listRoutesBean = new ArrayList<RouteBean>();
//		String INV_VW_ROUTES = null;
//		int aux;		
//
//		INV_VW_ROUTES = "SELECT ROUTE_ID, BUKRS, WERKS, RDESC, RTYPE, BDESC, WDESC FROM dbo.INV_VW_ROUTES WITH(NOLOCK) ";
//
//		if (searchFilter != null) {
//			INV_VW_ROUTES += "WHERE ROUTE_ID LIKE '%" + searchFilter + "%' OR RDESC LIKE '%" + searchFilter + "%'";
//		} else {
//			String condition = buildCondition(lgTypIM);
//			if (condition != null) {
//				INV_VW_ROUTES += condition;
//			}
//		}
//		log.info(INV_VW_ROUTES);
//		log.info("[getRoutesDao] Preparing sentence...");
//		try {
//			stm = con.prepareStatement(INV_VW_ROUTES);
//
//			log.info("[getRoutesDao] Executing query...");
//
//			ResultSet rs = stm.executeQuery();
//
//			while (rs.next()) {
//				
//				routeBean = new RouteBean();
//				routeBean.setRouteId(String.format("%08d",rs.getInt(1)));
//				routeBean.setBukrs(rs.getString(2));
//				routeBean.setWerks(rs.getString(3));
//				routeBean.setRdesc(rs.getString(4));
//				routeBean.setType(rs.getString(5));
//				routeBean.setBdesc(rs.getString(6));
//				routeBean.setWdesc(rs.getString(7));
//				
//				routeBean.setPositions(this.getPositions(rs.getString(1)));
//				routeBean.setGroups(this.getGroups(rs.getString(1)));
//
//				listRoutesBean.add(routeBean);
//			}
//
//			// Retrive the warnings if there're
//			SQLWarning warning = stm.getWarnings();
//			while (warning != null) {
//				log.log(Level.WARNING, warning.getMessage());
//				warning = warning.getNextWarning();
//			}
//
//			// Free resources
//			rs.close();
//			stm.close();
//			log.info("[getRoutesDao] Sentence successfully executed.");
//		} catch (SQLException e) {
//			log.log(Level.SEVERE,
//					"[getRoutesDao] Some error occurred while was trying to execute the query: " + INV_VW_ROUTES, e);
//			abstractResult.setResultId(ReturnValues.IEXCEPTION);
//			abstractResult.setResultMsgAbs(e.getMessage());
//			res.setAbstractResult(abstractResult);
//			return res;
//		} finally {
//			try {
//				con.close();
//			} catch (SQLException e) {
//				log.log(Level.SEVERE, "[getRoutesDao] Some error occurred while was trying to close the connection.",
//						e);
//			}
//		}
//
//		res.setAbstractResult(abstractResult);
//		res.setLsObject(listRoutesBean);
//		return res;
//	}
	
//	private String buildCondition(LgTypIM lgTypIM) {
//		String routeId = "";
//		String bukrs = "";
//		String werks = "";
//		String rdesc = "";
//		String type = "";
//		String condition = "";
//		
//		routeId = (routeB.getRouteId() != null)
//				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " ROUTE_ID = '" + routeB.getRouteId().replaceFirst("^0*", "") + "' "
//				: "";
//		condition += routeId;
//		bukrs = (routeB.getBukrs() != null)
//				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " BUKRS = '" + routeB.getBukrs() + "' " : "";
//		condition += bukrs;
//		werks = (routeB.getWerks() != null)
//				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " WERKS = '" + routeB.getWerks() + "' " : "";
//		condition += werks;
//		rdesc = (routeB.getRdesc() != null)
//				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " RDESC = '" + routeB.getRdesc() + "' " : "";
//		condition += rdesc;
//		type = (routeB.getType() != null)
//				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " TYPE = '" + routeB.getType() + "' " : "";
//		condition += type;
//		condition = condition.isEmpty() ? null : condition;
//		return condition;
//	}

}
