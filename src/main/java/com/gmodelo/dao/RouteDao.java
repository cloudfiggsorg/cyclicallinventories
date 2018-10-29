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

import javax.naming.NamingException;

import com.bmore.ume001.beans.User;
import com.gmodelo.beans.AbstractResultsBean;
import com.gmodelo.beans.Response;
import com.gmodelo.beans.RouteBean;
import com.gmodelo.beans.RouteGroupBean;
import com.gmodelo.beans.RoutePositionBean;
import com.gmodelo.beans.TaskBean;
import com.gmodelo.beans.ZoneBean;
import com.gmodelo.utils.ConnectionManager;
import com.gmodelo.utils.ReturnValues;

public class RouteDao {

	private Logger log = Logger.getLogger(RouteDao.class.getName());
	private UMEDaoE ume;
	private User user;

	private static final String GET_ROUTE_TYPE_BY_ID = "SELECT ROUTE_ID, ROU_DESC, ROU_TYPE FROM INV_ROUTE WITH(NOLOCK) WHERE ROUTE_ID = ?";
	private static final String CHECK_EXISTING_GROUP = "SELECT COUNT(*) AS TOTAL  FROM INV_ROUTE_GROUPS WHERE RGR_ROUTE_ID = ? AND RGR_COUNT_NUM = ?";
	private static final String RECOVER_EXISTING_PK_ASG_ID = "SELECT PK_ASG_ID, RGR_ROUTE_ID, RGR_GROUP_ID, RGR_COUNT_NUM FROM INV_ROUTE_GROUPS WITH(NOLOCK)"
			+ " WHERE RGR_ROUTE_ID = ? AND RGR_COUNT_NUM = ?";
	private static final String INV_SP_ASSIGN_GROUP_TO_ROUTE = "INV_SP_ASSIGN_GROUP_TO_ROUTE ?, ?, ?, ?, ?";

	public RouteBean getRouteTypeById(String routeId, Connection con) throws SQLException {
		PreparedStatement stm = con.prepareStatement(GET_ROUTE_TYPE_BY_ID);
		RouteBean routeBean = new RouteBean();
		stm.setString(1, routeId);
		ResultSet rs = stm.executeQuery();
		while (rs.next()) {
			routeBean.setRdesc(rs.getString("ROU_DESC"));
			routeBean.setRouteId(routeId);
			routeBean.setType(rs.getString("ROU_TYPE"));

		}
		return routeBean;
	}

	public Response<RouteBean> addRoute(RouteBean routeBean, String createdBy) {
		Response<RouteBean> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();

		final String INV_SP_ADD_ROUTE = "INV_SP_ADD_ROUTE ?, ?, ?, ?, ?, ?, ?";
		final String INV_SP_DEL_ROUTE_POSITION = "INV_SP_DEL_ROUTE_POSITION ?";
		final String INV_SP_ADD_ROUTE_POSITION = "INV_SP_ADD_ROUTE_POSITION ?, ?, ?, ?";
		final String INV_SP_DESASSIGN_GROUP_TO_ROUTE = "INV_SP_DESASSIGN_GROUP_TO_ROUTE ?";
		int routeId = 0;
		ZoneBean zb, zbAux = null; 
		RoutePositionBean rpb;

		try {
			routeId = Integer.parseInt(routeBean.getRouteId());
		} catch (NumberFormatException e) {
			// TODO: handle exception
		}

		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		CallableStatement cs = null;
		
		for(int i = 0; i < routeBean.getPositions().size(); i++){
			
			rpb = routeBean.getPositions().get(i);
			
			if(rpb.getGdesc() != null){				
				continue;
			}
			
			zb = new ZoneBean();
			zb.setBukrs(routeBean.getBukrs());
			zb.setWerks(routeBean.getWerks());
			zb.setZoneId(rpb.getZoneId());
			zbAux = new ZoneDao().getZoneById(zb, con);
			
			if(zbAux == null){
				
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs("Zona inválida: \" "+ zb.getZoneId() 
				+ " \" para los datos definidos en \"Sociedad\" y \"Centro\".");
				res.setAbstractResult(abstractResult);
				return res;
				
			}else{
				
				rpb.setZoneId(zbAux.getZoneId());
				rpb.setZdesc(zbAux.getZdesc());
				rpb.setLgort(zbAux.getLgort());
				rpb.setGdesc(zbAux.getgDesc());					
				routeBean.getPositions().set(i, rpb);
			}			
		}
				
		log.info("[addRoute] Preparing sentence...");

		try {

			con.setAutoCommit(false);
			// ADD ROUTE

			cs = con.prepareCall(INV_SP_ADD_ROUTE);
			cs.setInt(1, routeId);
			cs.setString(2, routeBean.getRdesc());
			cs.setString(3, routeBean.getBukrs());
			cs.setString(4, routeBean.getWerks());
			cs.setString(5, createdBy);
			cs.setString(7, routeBean.getType());
			cs.registerOutParameter(1, Types.INTEGER);
			cs.registerOutParameter(5, Types.VARCHAR);
			cs.registerOutParameter(6, Types.VARCHAR);

			log.info("[addRoute] Executing query...");
			cs.execute();

			routeBean.setRouteId(cs.getString(1)); // addZeros

			user = new User();
			ume = new UMEDaoE();
			user.getEntity().setIdentyId(cs.getString(5));
			ArrayList<User> ls = new ArrayList<>();
			ls.add(user);
			ls = ume.getUsersLDAPByCredentials(ls);

			if (ls.size() > 0) {

				routeBean.setCreatedBy(cs.getString(5) + " - " + ls.get(0).getGenInf().getName() + " "
						+ ls.get(0).getGenInf().getLastName());
			} else {
				routeBean.setCreatedBy(cs.getString(5));
			}

			user.getEntity().setIdentyId(cs.getString(6));
			ls = new ArrayList<>();
			ls.add(user);
			ls = ume.getUsersLDAPByCredentials(ls);

			if (ls.size() > 0) {

				routeBean.setModifiedBy(cs.getString(6) + " - " + ls.get(0).getGenInf().getName() + " "
						+ ls.get(0).getGenInf().getLastName());
			} else {
				routeBean.setModifiedBy(cs.getString(6));
			}

			//Delete route position			
			cs = null;
			cs = con.prepareCall(INV_SP_DEL_ROUTE_POSITION);
			cs.setInt(1, Integer.parseInt(routeBean.getRouteId()));
			cs.execute();
			
			//Delete groups to route
			cs = null;
			cs = con.prepareCall(INV_SP_DESASSIGN_GROUP_TO_ROUTE);
			cs.setInt(1, Integer.parseInt(routeBean.getRouteId()));
			cs.execute();

			// INSERTAR POSICIONES
			for (int i = 0; i < routeBean.getPositions().size(); i++) {

				cs = null;
				log.info("[addRoutePosition] Preparing sentence...");
				cs = con.prepareCall(INV_SP_ADD_ROUTE_POSITION);
				cs.setString(1, routeBean.getRouteId());
				cs.setInt(2, routeBean.getPositions().get(i).getPositionId());
				cs.setString(3, routeBean.getPositions().get(i).getZoneId());
				cs.setString(4, routeBean.getPositions().get(i).getSecuency());
				cs.registerOutParameter(2, Types.INTEGER);

				log.info("[addRoutePosition] Executing query...");
				cs.execute();
				routeBean.getPositions().get(i).setPositionId(cs.getInt(2));
			}

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

				log.info("[assignGroupToRouteDao] Executing query...");
				cs.execute();
				routeBean.getGroups().get(i).setRouteGroup(cs.getInt(5));
			}

			log.info("[addRoute] Sentence successfully executed.");

			// Retrive the warnings if there're
			SQLWarning warning = cs.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING, "[addRoute] " + warning.getMessage());
				warning = warning.getNextWarning();
			}

			con.commit();
			// Free resources
			cs.close();

		} catch (SQLException | NamingException e) {
			try {
				// deshace todos los cambios realizados en los datos
				log.log(Level.WARNING, "[addRoute] Execute rollback");
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
	
	public AbstractResultsBean assingRecountGroup(TaskBean task, User user) {
		AbstractResultsBean result = new AbstractResultsBean();
		Connection con = new ConnectionManager().createConnection();
		try {
			PreparedStatement stm = con.prepareStatement(CHECK_EXISTING_GROUP);
			CallableStatement cs = con.prepareCall(INV_SP_ASSIGN_GROUP_TO_ROUTE);
			stm.setString(1, task.getRub().getRouteId());
			stm.setString(2, task.getRecount());
			log.info("[assingRecountGroup] Excecuting Sentence" + CHECK_EXISTING_GROUP);
			ResultSet rs = stm.executeQuery();
			log.info("[assingRecountGroup] Sentence successfully executed.");
			boolean execute = false;
			if (rs.next()) {
				if (rs.getInt("TOTAL") > 0) {
					log.info("[assingRecountGroup] Existing count group executed.");
					stm = con.prepareStatement(RECOVER_EXISTING_PK_ASG_ID);
					stm.setString(1, task.getRub().getRouteId());
					stm.setString(2, task.getRecount());
					log.info("[assingRecountGroup] Executing Sentence" + CHECK_EXISTING_GROUP);
					rs = stm.executeQuery();
					log.info("[assingRecountGroup] Sentence successfully executed.");
					if (rs.next()) {
						cs.setString(1, task.getRub().getRouteId());
						cs.setString(2, task.getGroupId());
						cs.setString(3, task.getRecount());
						cs.setString(4, user.getEntity().getIdentyId());
						cs.setInt(5, rs.getInt("PK_ASG_ID"));
						execute = true;
					}
				} else {
					log.info("[assingRecountGroup] Unexisting count group executed.");
					cs.setString(1, task.getRub().getRouteId());
					cs.setString(2, task.getGroupId());
					cs.setString(3, task.getRecount());
					cs.setString(4, user.getEntity().getIdentyId());
					cs.setInt(5, 0);
					execute = true;
				}

				if (execute) {
					log.info("[assingRecountGroup] Executing Sentence" + CHECK_EXISTING_GROUP);
					cs.execute();
					log.info("[assingRecountGroup] Sentence successfully executed.");
				} else {
					log.info("[assingRecountGroup] Sentence " + CHECK_EXISTING_GROUP + "not Executed Sentence");
					result.setResultId(ReturnValues.IERROR);
				}
			}
		} catch (SQLException e) {
			log.log(Level.SEVERE, "[assingRecountGroup] Some error occurred while Excecuting sentences:", e);
		} finally {
			try {
				con.close();
			} catch (Exception e) {
				log.log(Level.SEVERE,
						"[assingRecountGroup] Some error occurred while was trying to close the connection.", e);
			}
		}
		return result;
	}

	public Response<Object> deleteRoute(String arrayIdRoutes) {

		log.info("[deleteRouteDao] " + arrayIdRoutes);

		Response<Object> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		CallableStatement cs = null;

		final String INV_SP_DEL_ROUTES = "INV_SP_DEL_ROUTES ?, ?";

		log.info("[deleteRouteDao] Preparing sentence...");

		try {
			cs = con.prepareCall(INV_SP_DEL_ROUTES);
			cs.setString(1, arrayIdRoutes);
			cs.registerOutParameter(2, Types.INTEGER);
			log.info("[deleteRouteDao] Executing query...");

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

			log.info("[deleteRouteDao] Sentence successfully executed.");

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
		Connection con = iConnectionManager.createConnection();
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
			log.info("[getRoutesDao] Trying to convert String to Int");
		}

		INV_VW_ROUTES = "SELECT ROUTE_ID, BUKRS, WERKS, RDESC, RTYPE, BDESC, WDESC, CREATED_BY, MODIFIED_BY FROM dbo.INV_VW_ROUTES WITH(NOLOCK) ";

		if (searchFilter != null) {
			INV_VW_ROUTES += "WHERE ROUTE_ID LIKE '%" + searchFilterNumber + "%' OR RDESC LIKE '%" + searchFilter
					+ "%'";
		} else {
			String condition = buildCondition(routeBean);
			if (condition != null) {
				INV_VW_ROUTES += condition;
			}
		}

		log.info(INV_VW_ROUTES);
		log.info("[getRoutesDao] Preparing sentence...");
		try {
			stm = con.prepareStatement(INV_VW_ROUTES);

			log.info("[getRoutesDao] Executing query...");

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {

				routeBean = new RouteBean();
				routeBean.setRouteId(rs.getString(1));
				routeBean.setBukrs(rs.getString(2));
				routeBean.setWerks(rs.getString(3));
				routeBean.setRdesc(rs.getString(4));
				routeBean.setType(rs.getString(5));
				routeBean.setBdesc(rs.getString(6));
				routeBean.setWdesc(rs.getString(7));

				user = new User();
				ume = new UMEDaoE();
				user.getEntity().setIdentyId(rs.getString("CREATED_BY"));
				ArrayList<User> ls = new ArrayList<>();
				ls.add(user);
				ls = ume.getUsersLDAPByCredentials(ls);

				if (ls.size() > 0) {

					routeBean.setCreatedBy(rs.getString("CREATED_BY") + " - " + ls.get(0).getGenInf().getName() + " "
							+ ls.get(0).getGenInf().getLastName());
				} else {
					routeBean.setCreatedBy(rs.getString("CREATED_BY"));
				}

				user.getEntity().setIdentyId(rs.getString("MODIFIED_BY"));
				ls = new ArrayList<>();
				ls.add(user);
				ls = ume.getUsersLDAPByCredentials(ls);

				if (ls.size() > 0) {

					routeBean.setModifiedBy(rs.getString("MODIFIED_BY") + " - " + ls.get(0).getGenInf().getName() + " "
							+ ls.get(0).getGenInf().getLastName());
				} else {
					routeBean.setModifiedBy(rs.getString("MODIFIED_BY"));
				}

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
			log.info("[getRoutesDao] Sentence successfully executed.");
		} catch (SQLException | NamingException e) {
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

	public Response<List<RouteBean>> getOnlyRoutes(RouteBean routeBean, String searchFilter) {
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
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
			log.info("[getOnlyRoutes] Trying to convert String to Int");
		}

		INV_VW_ROUTES = "SELECT ROUTE_ID, BUKRS, WERKS, RDESC, RTYPE, BDESC, WDESC FROM dbo.INV_VW_AVAILABLE_ROUTES WITH(NOLOCK) ";

		if (searchFilter != null) {
			INV_VW_ROUTES += "WHERE (ROUTE_ID LIKE '%" + searchFilterNumber + "%' OR RDESC LIKE '%" + searchFilter + "%') ";
		} else {
			String condition = buildCondition(routeBean);
			if (condition != null) {
				INV_VW_ROUTES += condition;
			}
		}

		log.info(INV_VW_ROUTES);
		log.info("[getOnlyRoutes] Preparing sentence...");
		try {
			stm = con.prepareStatement(INV_VW_ROUTES);

			log.info("[getOnlyRoutes] Executing query...");

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {

				routeBean = new RouteBean();
				routeBean.setRouteId(rs.getString(1));
				routeBean.setBukrs(rs.getString(2));
				routeBean.setWerks(rs.getString(3));
				routeBean.setRdesc(rs.getString(4));
				routeBean.setType(rs.getString(5));
				routeBean.setBdesc(rs.getString(6));
				routeBean.setWdesc(rs.getString(7));

				routeBean.setPositions(null);
				routeBean.setGroups(null);

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
			log.info("[getOnlyRoutes] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE,
					"[getRougetOnlyRoutestesDao] Some error occurred while was trying to execute the query: "
							+ INV_VW_ROUTES,
					e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE, "[getOnlyRoutes] Some error occurred while was trying to close the connection.",
						e);
			}
		}

		res.setAbstractResult(abstractResult);
		res.setLsObject(listRoutesBean);
		return res;
	}

	public List<RoutePositionBean> getPositions(String idRoute) throws SQLException {

		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;

		List<RoutePositionBean> listPositions = new ArrayList<RoutePositionBean>();

		String INV_VW_ROUTES_WITH_POSITIONS = "SELECT POSITION_ID ,LGORT ,GDES ,ZONE_ID ,SECUENCY ,ZDESC FROM dbo.INV_VW_ROUTES_WITH_POSITIONS WITH(NOLOCK) WHERE ROUTE_ID = ?";

		log.info(INV_VW_ROUTES_WITH_POSITIONS);
		log.info("[getPositionsDao] Preparing sentence...");

		stm = con.prepareStatement(INV_VW_ROUTES_WITH_POSITIONS);
		stm.setString(1, idRoute);
		log.info("[getPositionsDao] Executing query...");

		ResultSet rs = stm.executeQuery();

		while (rs.next()) {
			RoutePositionBean position = new RoutePositionBean();
			position.setPositionId(rs.getInt(1));
			position.setLgort(rs.getString(2));
			position.setGdesc(rs.getString(3));
			position.setZoneId(rs.getString(4));
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
		log.info("[getPositionsDao] Sentence successfully executed.");
		con.close();
		return listPositions;
	}

	public List<RouteGroupBean> getGroups(String idRoute) throws SQLException {

		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;

		List<RouteGroupBean> listGroups = new ArrayList<RouteGroupBean>();

		String INV_VW_ROUTE_GROUPS = "SELECT PK_ASG_ID, GROUP_ID ,GDESC ,COUNT_NUM FROM dbo.INV_VW_ROUTE_GROUPS WITH(NOLOCK) WHERE ROUTE_ID = ?";

		log.info(INV_VW_ROUTE_GROUPS);
		log.info("[getGroupsDao] Preparing sentence...");

		stm = con.prepareStatement(INV_VW_ROUTE_GROUPS);
		stm.setString(1, idRoute);
		log.info("[getGroupsDao] Executing query...");

		try {
			ResultSet rs = stm.executeQuery();

			GroupDao groupDAO = new GroupDao();

			while (rs.next()) {

				RouteGroupBean group = new RouteGroupBean();
				group.setRouteGroup(rs.getInt(1));
				group.setGroupId(rs.getString(2));
				group.setGdesc(rs.getString(3));
				group.setCountNum(rs.getString(4));
				group.setUsers(groupDAO.groupUsers(rs.getString(2), null));
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
			log.info("[getGroupsDao] Sentence successfully executed.");
		} catch (SQLException | NamingException e) {
			log.log(Level.SEVERE, "[getGroupsDao] Ocurrió un error", e);
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE, "[getGroupsDao] Some error occurred while was trying to close the connection.",
						e);
			}
		}

		return listGroups;
	}

	private String buildCondition(RouteBean routeB) {
		String routeId = "";
		String bukrs = "";
		String werks = "";
		String rdesc = "";
		String type = "";
		String condition = "";

		routeId = (routeB.getRouteId() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ") + " ROUTE_ID = '"
				+ routeB.getRouteId().replaceFirst("^0*", "") + "' " : "";
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
