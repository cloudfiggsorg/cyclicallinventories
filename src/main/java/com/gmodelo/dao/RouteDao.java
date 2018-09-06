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
import com.gmodelo.beans.Response;
import com.gmodelo.beans.RouteBean;
import com.gmodelo.beans.RouteGroupBean;
import com.gmodelo.beans.RoutePositionBean;
import com.gmodelo.utils.ConnectionManager;
import com.gmodelo.utils.ReturnValues;

public class RouteDao{

	private Logger log = Logger.getLogger(RouteDao.class.getName());

	public Response<RouteBean> addRoute(RouteBean routeBean, String createdBy) {
		Response<RouteBean> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();

		final String INV_SP_ADD_ROUTE = "INV_SP_ADD_ROUTE ?, ?, ?, ?, ?, ?"; 		
		final String INV_SP_DEL_ROUTE_POSITION = "INV_SP_DEL_ROUTE_POSITION ?, ?";				
		final String INV_SP_ADD_ROUTE_POSITION = "INV_SP_ADD_ROUTE_POSITION ?, ?, ?, ?";
		final String INV_SP_DESASSIGN_GROUP_TO_ROUTE = "INV_SP_DESASSIGN_GROUP_TO_ROUTE ?, ?";
		final String INV_SP_ASSIGN_GROUP_TO_ROUTE = "INV_SP_ASSIGN_GROUP_TO_ROUTE ?, ?, ?, ?, ?";
		int routeId = 0;
		
		try {
			routeId = Integer.parseInt(routeBean.getRouteId());
		} catch (NumberFormatException e) {
			// TODO: handle exception
		}
		
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		CallableStatement cs = null;

		log.log(Level.WARNING, "[addRoute] Preparing sentence...");

		try {
			
			con.setAutoCommit(false);
			// ADD ROUTE
			
			cs = con.prepareCall(INV_SP_ADD_ROUTE);
			cs.setInt(1, routeId);
			cs.setString(2, routeBean.getRdesc());
			cs.setString(3, routeBean.getBukrs());
			cs.setString(4, routeBean.getWerks());
			cs.setString(5, createdBy);
			cs.setString(6, routeBean.getType());
			cs.registerOutParameter(1, Types.INTEGER);

			log.log(Level.WARNING, "[addRoute] Executing query...");
			cs.execute();
			
			routeBean.setRouteId(String.format("%08d", cs.getInt(1))); // addZeros
			
			//Eliminar posiciones
			String ids = "";
			for (int i = 0; i < routeBean.getPositions().size(); i++) {
								
				if(routeBean.getPositions().get(i).getPositionId() > 0){
					ids += routeBean.getPositions().get(i).getPositionId() + ",";
				}				
			}
												
			cs = null;
			cs = con.prepareCall(INV_SP_DEL_ROUTE_POSITION);
			cs.setInt(1, Integer.parseInt(routeBean.getRouteId()));
			cs.setString(2, ids);
			cs.execute();
			
			// INSERTAR POSICIONES
			for (int i = 0; i < routeBean.getPositions().size(); i++) {
				
				cs = null;
				log.log(Level.WARNING, "[addRoutePosition] Preparing sentence...");
				cs = con.prepareCall(INV_SP_ADD_ROUTE_POSITION);
				cs.setString(1, routeBean.getRouteId());
				cs.setInt(2, routeBean.getPositions().get(i).getPositionId());
				cs.setString(3, routeBean.getPositions().get(i).getZoneId());
				cs.setString(4, routeBean.getPositions().get(i).getSecuency());
				cs.registerOutParameter(2, Types.INTEGER);

				log.log(Level.WARNING, "[addRoutePosition] Executing query...");
				cs.execute(); 
				routeBean.getPositions().get(i).setPositionId(cs.getInt(2));
			}
			
			//Eliminar grupos
			ids = "";
			for (int i = 0; i < routeBean.getGroups().size(); i++) {
				
				if(routeBean.getGroups().get(i).getRouteGroup() > 0){
					ids += routeBean.getGroups().get(i).getRouteGroup() + ",";
				}
			}
			
			cs = null;
			cs = con.prepareCall(INV_SP_DESASSIGN_GROUP_TO_ROUTE);
			cs.setInt(1, Integer.parseInt(routeBean.getRouteId()));
			cs.setString(2, ids);
			cs.execute();

			// INSERTAR GRUPOS Y CONTEOS
			for (int i = 0; i < routeBean.getGroups().size(); i++) {

				cs = null;
				cs = con.prepareCall(INV_SP_ASSIGN_GROUP_TO_ROUTE);

				cs.setString(1, routeBean.getRouteId());
				cs.setString(2, routeBean.getGroups().get(i).getGroupId());
				cs.setString(3, routeBean.getGroups().get(i).getCountNum());
				cs.setString(4, createdBy);
				cs.setInt(5, routeBean.getGroups().get(i).getRouteGroup());
				cs.registerOutParameter(5, Types.INTEGER);

				log.log(Level.WARNING, "[assignGroupToRouteDao] Executing query...");
				cs.execute();
				routeBean.getGroups().get(i).setRouteGroup(cs.getInt(5));
			}
			
			log.log(Level.WARNING, "[addRoute] Sentence successfully executed.");

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
				log.log(Level.WARNING,"[addRoute] Execute rollback");
				con.rollback();
			} catch (SQLException e1) {
				log.log(Level.SEVERE, "[addRoute] Not rollback .", e);
			}
			log.log(Level.SEVERE,
					"[addRoute] Some error occurred while was trying to execute the S.P.: " + INV_SP_ADD_ROUTE, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE, "[addRoute] Some error occurred while was trying to close the connection.", e);
			}
		}
		res.setAbstractResult(abstractResult);
		res.setLsObject(routeBean);
		return res;
	}

	public Response<Object> deleteRoute(String arrayIdRoutes) {
		
		System.out.println(arrayIdRoutes);

		Response<Object> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		CallableStatement cs = null;

		final String INV_SP_DEL_ROUTES = "INV_SP_DEL_ROUTES ?, ?";

		log.log(Level.WARNING, "[deleteRouteDao] Preparing sentence...");

		try {
			cs = con.prepareCall(INV_SP_DEL_ROUTES);
			cs.setString(1, arrayIdRoutes);
			cs.registerOutParameter(2, Types.INTEGER);
			log.log(Level.WARNING, "[deleteRouteDao] Executing query...");

			cs.execute();

			abstractResult.setResultId(cs.getInt(2));

			// Retrive the warnings if there're
			SQLWarning warning = cs.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING, "[deleteRouteDao] " + warning.getMessage());
				warning = warning.getNextWarning();
			}

			// Free resources
			cs.close();

			log.log(Level.WARNING, "[deleteRouteDao] Sentence successfully executed.");

		} catch (SQLException e) {
			log.log(Level.SEVERE,
					"[deleteRouteDao] Some error occurred while was trying to execute the S.P.: " + INV_SP_DEL_ROUTES,
					e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE, "[deleteRouteDao] Some error occurred while was trying to close the connection.",
						e);
			}
		}
		res.setAbstractResult(abstractResult);
		return res;
	}

	public Response<List<RouteBean>> getRoutes(RouteBean routeBean, String searchFilter) {
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		PreparedStatement stm = null;

		Response<List<RouteBean>> res = new Response<List<RouteBean>>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		List<RouteBean> listRoutesBean = new ArrayList<RouteBean>();
		String INV_VW_ROUTES = null;
		int aux;		
		String searchFilterNumber = "";
		
		try {
			aux = Integer.parseInt(searchFilter); 
			searchFilterNumber += aux;
		} catch (Exception e) {
			searchFilterNumber = searchFilter;
			log.warning("Trying to convert String to Int");
		}		

		INV_VW_ROUTES = "SELECT ROUTE_ID, BUKRS, WERKS, RDESC, RTYPE, BDESC, WDESC FROM dbo.INV_VW_ROUTES WITH(NOLOCK) ";

		if (searchFilter != null) {
			INV_VW_ROUTES += "WHERE ROUTE_ID LIKE '%" + searchFilterNumber + "%' OR RDESC LIKE '%" + searchFilter + "%'";
		} else {
			String condition = buildCondition(routeBean);
			if (condition != null) {
				INV_VW_ROUTES += condition;
			}
		}
		log.warning(INV_VW_ROUTES);
		log.log(Level.WARNING, "[getRoutesDao] Preparing sentence...");
		try {
			stm = con.prepareStatement(INV_VW_ROUTES);

			log.log(Level.WARNING, "[getRoutesDao] Executing query...");

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				
				routeBean = new RouteBean();
				routeBean.setRouteId(String.format("%08d",rs.getInt(1)));
				routeBean.setBukrs(rs.getString(2));
				routeBean.setWerks(rs.getString(3));
				routeBean.setRdesc(rs.getString(4));
				routeBean.setType(rs.getString(5));
				routeBean.setBdesc(rs.getString(6));
				routeBean.setWdesc(rs.getString(7));
				
				routeBean.setPositions(this.getPositions(rs.getString(1)));
				routeBean.setGroups(this.getGroups(rs.getString(1)));

				listRoutesBean.add(routeBean);
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
			log.log(Level.WARNING, "[getRoutesDao] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE,
					"[getRoutesDao] Some error occurred while was trying to execute the query: " + INV_VW_ROUTES, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE, "[getRoutesDao] Some error occurred while was trying to close the connection.",
						e);
			}
		}

		res.setAbstractResult(abstractResult);
		res.setLsObject(listRoutesBean);
		return res;
	}

	public List<RoutePositionBean> getPositions(String idRoute) throws SQLException {

		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		PreparedStatement stm = null;

		List<RoutePositionBean> listPositions = new ArrayList<RoutePositionBean>();

		String INV_VW_ROUTES_WITH_POSITIONS = "SELECT POSITION_ID ,LGORT ,GDES ,ZONE_ID ,SECUENCY ,ZDESC FROM dbo.INV_VW_ROUTES_WITH_POSITIONS WITH(NOLOCK) WHERE ROUTE_ID = ?";

		log.warning(INV_VW_ROUTES_WITH_POSITIONS);
		log.log(Level.WARNING, "[getRoutesDao] Preparing sentence...");

		stm = con.prepareStatement(INV_VW_ROUTES_WITH_POSITIONS);
		stm.setString(1, idRoute);
		log.log(Level.WARNING, "[getRoutesDao] Executing query...");

		ResultSet rs = stm.executeQuery();

		while (rs.next()) {
			RoutePositionBean position = new RoutePositionBean();
			position.setPositionId(rs.getInt(1));
			position.setLgort(rs.getString(2));
			position.setGdesc(rs.getString(3));
			position.setZoneId(String.format("%08d",rs.getInt(4)));
			position.setSecuency(rs.getString(5));
			position.setZdesc(rs.getString(6));
			listPositions.add(position);
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
		log.log(Level.WARNING, "[getRoutesDao] Sentence successfully executed.");
		con.close();

		return listPositions;
	}

	public List<RouteGroupBean> getGroups(String idRoute) throws SQLException {

		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		PreparedStatement stm = null;

		List<RouteGroupBean> listGroups = new ArrayList<RouteGroupBean>();

		String INV_VW_ROUTE_GROUPS = "SELECT PK_ASG_ID, GROUP_ID ,GDESC ,COUNT_NUM FROM dbo.INV_VW_ROUTE_GROUPS WITH(NOLOCK) WHERE ROUTE_ID = ? ";

		log.warning(INV_VW_ROUTE_GROUPS);
		log.log(Level.WARNING, "[getGroupsDao] Preparing sentence...");

		stm = con.prepareStatement(INV_VW_ROUTE_GROUPS);
		stm.setString(1, idRoute);
		log.log(Level.WARNING, "[getGroupsDao] Executing query...");

		ResultSet rs = stm.executeQuery();
		
		GroupDao groupDAO = new GroupDao(); 

		while (rs.next()) {
			
			RouteGroupBean group = new RouteGroupBean();
			group.setRouteGroup(rs.getInt(1));
			group.setGroupId(rs.getString(2));
			group.setGdesc(rs.getString(3));
			group.setCountNum(rs.getString(4));
			group.setUsers(groupDAO.groupUsers(rs.getString(2)));
			listGroups.add(group);
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
		log.log(Level.WARNING, "[getGroupsDao] Sentence successfully executed.");

		con.close();

		return listGroups;
	}

	private String buildCondition(RouteBean routeB) {
		String routeId = "";
		String bukrs = "";
		String werks = "";
		String rdesc = "";
		String type = "";
		String condition = "";
		
		routeId = (routeB.getRouteId() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " ROUTE_ID = '" + routeB.getRouteId().replaceFirst("^0*", "") + "' "
				: "";
		condition += routeId;
		bukrs = (routeB.getBukrs() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " BUKRS = '" + routeB.getBukrs() + "' " : "";
		condition += bukrs;
		werks = (routeB.getWerks() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " WERKS = '" + routeB.getWerks() + "' " : "";
		condition += werks;
		rdesc = (routeB.getRdesc() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " RDESC = '" + routeB.getRdesc() + "' " : "";
		condition += rdesc;
		type = (routeB.getType() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " TYPE = '" + routeB.getType() + "' " : "";
		condition += type;
		condition = condition.isEmpty() ? null : condition;
		return condition;
	}

	
}
