package com.gmodelo.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bmore.ume001.beans.User;
import com.gmodelo.beans.LgplaValuesBean;
import com.gmodelo.beans.RouteUserBean;
import com.gmodelo.beans.RouteUserPositionBean;
import com.gmodelo.beans.TaskBean;
import com.gmodelo.beans.ZonePositionMaterialsBean;
import com.gmodelo.beans.ZoneUserBean;
import com.gmodelo.beans.ZoneUserPositionsBean;
import com.gmodelo.utils.ConnectionManager;

public class RouteUserDao {

	private Logger log = Logger.getLogger(RouteUserDao.class.getName());

	public RouteUserBean getRoutesByUserLegacy(User user) throws SQLException {

		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		CallableStatement cs = null;
		RouteUserBean routeBean = null;
		final String INV_VW_ROUTES = "INV_SP_ROUTE_USER ?, ?, ?, ?, ?, ?, ?, ?, ?, ?";
		log.info(INV_VW_ROUTES);
		log.info("[getRoutesByUserDao] Preparing sentence...");
		try {
			stm = con.prepareStatement(INV_LAST_GENERATED_ROUTE);
			stm.setString(1, user.getEntity().getIdentyId());
			ResultSet rs = stm.executeQuery();
			if (rs.next()) {
				routeBean = new RouteUserBean();
				routeBean.setRouteId(rs.getString("ROUTE_ID"));
				routeBean.setBukrs(rs.getString("BUKRS"));
				routeBean.setWerks(rs.getString("WERKS"));
				routeBean.setRdesc(rs.getString("RDESC"));
				routeBean.setType(rs.getString("RTYPE"));
				routeBean.setBdesc(rs.getString("BDESC"));
				routeBean.setWdesc(rs.getString("WDESC"));
				routeBean.setTaskId(rs.getString("TASK_ID"));
				TaskDao task = new TaskDao();
				routeBean.setDateIni(task.updateDowloadTask(routeBean.getTaskId()));
			}

			cs = con.prepareCall(INV_VW_ROUTES);
			cs.setString(1, user.getEntity().getIdentyId());
			cs.registerOutParameter(2, Types.VARCHAR); // routeId
			cs.registerOutParameter(3, Types.VARCHAR); // bukrs
			cs.registerOutParameter(4, Types.VARCHAR); // werks
			cs.registerOutParameter(5, Types.VARCHAR); // rdesc
			cs.registerOutParameter(6, Types.VARCHAR); // rtype
			cs.registerOutParameter(7, Types.VARCHAR); // bdesc
			cs.registerOutParameter(8, Types.VARCHAR); // wdesc
			cs.registerOutParameter(9, Types.VARCHAR); // taskId
			cs.registerOutParameter(10, Types.INTEGER); // return

			log.info("[getRoutesByUserDao] Executing query...");
			cs.execute();
			if (cs.getInt(10) == 1) {
				routeBean = new RouteUserBean();
				routeBean.setRouteId(cs.getString(2));
				routeBean.setBukrs(cs.getString(3));
				routeBean.setWerks(cs.getString(4));
				routeBean.setRdesc(cs.getString(5));
				routeBean.setType(cs.getString(6));
				routeBean.setBdesc(cs.getString(7));
				routeBean.setWdesc(cs.getString(8));
				routeBean.setTaskId(cs.getString(9));
				TaskDao task = new TaskDao();
				routeBean.setDateIni(task.updateDowloadTask(cs.getString(9)));
				//
				// routeBean.setPositions(this.getPositions(rs.getString("ROUTE_ID")));
			} else {
				routeBean = null;
			}
			cs.close();
			log.info("[getRoutesByUserDao] Sentence successfully executed.");
		} finally {
			con.close();
		}
		return routeBean;
	}

	private static final String GET_RECONTEO = "SELECT TASK_ID, TAS_JSON, TASK_ID_PARENT FROM INV_TASK WHERE TASK_ID = ?";

	public TaskBean getTaskReconteo(String taskId) throws SQLException {
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		TaskBean reconteo = null;
		try {
			stm = con.prepareStatement(GET_RECONTEO);
			stm.setString(1, taskId);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				reconteo = new TaskBean();
				reconteo.setTaskId(taskId);
				reconteo.setTaskJSON(rs.getString("TAS_JSON"));
				reconteo.setTaskIdFather(rs.getString("TASK_ID_PARENT"));
			}
		} catch (SQLException e) {
			log.log(Level.SEVERE, "", e);
			reconteo = null;
		}
		return reconteo;
	}

	public static String INV_LAST_GENERATED_ROUTE = "SELECT TOP 1 ROUTE_ID, BUKRS, WERKS, RDESC, RTYPE, BDESC, WDESC, TASK_ID, MAX(TAS_CREATED_DATE) AS LAST_DATE "
			+ " FROM dbo.INV_VW_TASK_ROUTES_USER WITH(NOLOCK)  WHERE USER_ID = ? "
			+ " GROUP BY TAS_CREATED_DATE, ROUTE_ID, BUKRS, WERKS, RDESC, RTYPE, BDESC, WDESC, TASK_ID " 
			+ " ORDER BY TAS_CREATED_DATE DESC";

	public RouteUserBean getRoutesByUser(String user) throws SQLException {

		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		RouteUserBean routeBean = null;
		log.info(INV_LAST_GENERATED_ROUTE);
		log.info("[getRoutesByUserDao] Preparing sentence...");
		try {
			stm = con.prepareStatement(INV_LAST_GENERATED_ROUTE);
			stm.setString(1, user);
			ResultSet rs = stm.executeQuery();
			if (rs.next()) {
				routeBean = new RouteUserBean();
				routeBean.setRouteId(rs.getString("ROUTE_ID"));
				routeBean.setBukrs(rs.getString("BUKRS"));
				routeBean.setWerks(rs.getString("WERKS"));
				routeBean.setRdesc(rs.getString("RDESC"));
				routeBean.setType(rs.getString("RTYPE"));
				routeBean.setBdesc(rs.getString("BDESC"));
				routeBean.setWdesc(rs.getString("WDESC"));
				routeBean.setTaskId(rs.getString("TASK_ID"));
				TaskDao task = new TaskDao();
				log.info("getTaskId" + routeBean.getTaskId());
				routeBean.setDateIni(task.updateDowloadTask(routeBean.getTaskId()));
			} else {
				routeBean = null;
			}
			log.info("[getRoutesByUserDao] Sentence successfully executed.");
		} finally {
			con.close();
		}
		return routeBean;
	}

	public List<RouteUserPositionBean> getPositions(String idRoute, TaskBean conteo) throws SQLException {

		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		List<RouteUserPositionBean> listPositions = new ArrayList<RouteUserPositionBean>();
		String INV_VW_ROUTES_WITH_POSITIONS = "";
		if (conteo.getTaskIdFather() != null && !conteo.getTaskIdFather().isEmpty()) {
			INV_VW_ROUTES_WITH_POSITIONS = "SELECT POSITION_ID ,LGORT ,GDES ,ZONE_ID ,SECUENCY, ROUTE_ID FROM dbo.INV_VW_ROUTES_WITH_POSITIONS WITH(NOLOCK) WHERE ROUTE_ID = ? ORDER BY SECUENCY DESC";
		} else {
			INV_VW_ROUTES_WITH_POSITIONS = "SELECT POSITION_ID ,LGORT ,GDES ,ZONE_ID ,SECUENCY, ROUTE_ID FROM dbo.INV_VW_ROUTES_WITH_POSITIONS WITH(NOLOCK) WHERE ROUTE_ID = ? ORDER BY SECUENCY ASC";
		}
		try {
			log.info(INV_VW_ROUTES_WITH_POSITIONS);
			log.info("[getRoutesUserPositionDao] Preparing sentence...");
			stm = con.prepareStatement(INV_VW_ROUTES_WITH_POSITIONS);
			stm.setString(1, idRoute);
			log.info("[getRoutesUserPositionDao] Executing query...");

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				RouteUserPositionBean position = new RouteUserPositionBean();
				position.setPositionId(rs.getInt("POSITION_ID"));
				position.setLgort(rs.getString("LGORT"));
				position.setGdesc(rs.getString("GDES"));
				position.setSecuency(rs.getString("SECUENCY"));
				position.setRouteId(rs.getString("ROUTE_ID"));
				position.setZone(this.getZoneByPosition(rs.getString("ZONE_ID"), conteo));
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
			log.info("[getRoutesUserPositionDao] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE,
					"[getRoutesUserPositionDao] Some error occurred while was trying to execute the query: "
							+ INV_VW_ROUTES_WITH_POSITIONS,
					e);
			throw e;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,
						"[updateDowloadTask] Some error occurred while was trying to close the connection.", e);
			}
		}
		return listPositions;
	}

	public ZoneUserBean getZoneByPosition(String zoneId, TaskBean conteo) throws SQLException {

		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		ZoneUserBean zoneBean = new ZoneUserBean();
		final String INV_VW_ZONES = "SELECT ZONE_ID, ZDESC FROM dbo.INV_VW_ZONES WITH(NOLOCK) WHERE ZONE_ID = ?";
		try {

			log.info("[getRoutesUserZoneDao] Preparing sentence...");
			stm = con.prepareStatement(INV_VW_ZONES);
			stm.setString(1, zoneId);
			log.info("[getRoutesUserZoneDao] Executing query...");
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				zoneBean.setZoneId(rs.getString("ZONE_ID"));
				zoneBean.setZdesc(rs.getString("ZDESC"));
				zoneBean.setPositionsB(this.getPositionsZone(rs.getString("ZONE_ID"), conteo));
			}
			// Retrive the warnings if there're
			SQLWarning warning = stm.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING, warning.getMessage());
				warning = warning.getNextWarning();
			}
			rs.close();
			stm.close();
			log.info("[getRoutesUserZoneDao] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE,
					"[getZoneByPosition] Some error occurred while was trying to execute the query: " + INV_VW_ZONES,
					e);
			throw e;

		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,
						"[getZoneByPosition] Some error occurred while was trying to close the connection.", e);
			}
		}
		return zoneBean;
	}

	public List<ZoneUserPositionsBean> getPositionsZone(String zoneId, TaskBean conteo) throws SQLException {

		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		List<ZoneUserPositionsBean> listPositions = new ArrayList<ZoneUserPositionsBean>();
		String INV_VW_ZONE_WITH_POSITIONS = "";

		if (conteo.getTaskIdFather() != null && !conteo.getTaskIdFather().isEmpty()) {
			INV_VW_ZONE_WITH_POSITIONS = "SELECT PK_ASG_ID, LGTYP ,LGPLA ,SECUENCY ,IMWM FROM dbo.INV_VW_ZONE_WITH_POSITIONS WHERE ZONE_ID = ? "
					+ "GROUP BY PK_ASG_ID,LGTYP,LGPLA,SECUENCY,IMWM ORDER BY SECUENCY DESC";

		} else {
			INV_VW_ZONE_WITH_POSITIONS = "SELECT PK_ASG_ID, LGTYP ,LGPLA ,SECUENCY ,IMWM FROM dbo.INV_VW_ZONE_WITH_POSITIONS WHERE ZONE_ID = ? "
					+ "GROUP BY PK_ASG_ID,LGTYP,LGPLA,SECUENCY,IMWM ORDER BY SECUENCY ASC";
		}

		log.info(INV_VW_ZONE_WITH_POSITIONS);
		log.info("[getPositionsZoneDao] Preparing sentence...");
		try {
			HashMap<String, HashMap<String, LgplaValuesBean>> mapPosition = this.getPositionMaterials();
			stm = con.prepareCall(INV_VW_ZONE_WITH_POSITIONS);
			stm.setString(1, zoneId);
			log.info("[getPositionsZoneDao] Executing query...");
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				ZoneUserPositionsBean position = new ZoneUserPositionsBean();
				position.setPkAsgId(rs.getInt("PK_ASG_ID"));
				position.setLgtyp(rs.getString("LGTYP"));
				position.setLgpla(rs.getString("LGPLA"));
				position.setSecuency(rs.getString("SECUENCY"));
				position.setImwm(rs.getString("IMWM"));
				position.setZoneId(zoneId);
				position.setLgplaValues(mapPosition.get(rs.getString("PK_ASG_ID")));
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
			log.info("[getPositionsZoneDao] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE, "[getPositionsZoneDao] Some error occurred while was trying to execute the query: "
					+ INV_VW_ZONE_WITH_POSITIONS, e);
			throw e;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,
						"[getPositionsZoneDao] Some error occurred while was trying to close the connection.", e);
			}
		}
		return listPositions;
	}

	private static final String INV_VW_ZONE_POSITIONS_MATERIALS = "SELECT POSITION_ID, MATNR, MAKTX FROM dbo.INV_VW_ZONE_POSITIONS_MATERIALS WITH(NOLOCK) GROUP BY POSITION_ID, MATNR, MAKTX";

	private HashMap<String, HashMap<String, LgplaValuesBean>> getPositionMaterials() throws SQLException {

		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		HashMap<String, HashMap<String, LgplaValuesBean>> mapMaterials = new HashMap<>();

		log.info(INV_VW_ZONE_POSITIONS_MATERIALS);
		log.info("[getPositionMaterialsDao] Preparing sentence...");
		try {
			stm = con.prepareCall(INV_VW_ZONE_POSITIONS_MATERIALS);
			log.info("[getPositionMaterialsDao] Executing query...");
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				if (mapMaterials.containsKey(rs.getString("POSITION_ID"))) {
					LgplaValuesBean material = new LgplaValuesBean();
					material.setMatnr(rs.getString("MATNR").replaceFirst("^0*", ""));
					material.setMatkx(rs.getString("MAKTX"));
					material.setLocked(true);
					mapMaterials.get(rs.getString("POSITION_ID")).put(material.toKey(rs.getString("POSITION_ID")),
							material);
				} else {
					HashMap<String, LgplaValuesBean> listMaterials = new HashMap<>();
					LgplaValuesBean material = new LgplaValuesBean();
					material.setMatnr(rs.getString("MATNR").replaceFirst("^0*", ""));
					material.setMatkx(rs.getString("MAKTX"));
					material.setLocked(true);
					listMaterials.put(material.toKey(rs.getString("POSITION_ID")), material);
					mapMaterials.put(rs.getString("POSITION_ID"), listMaterials);
				}
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

			log.info("[getPositionMaterialsDao] Sentence successfully executed.");

		} catch (SQLException e) {
			log.log(Level.SEVERE,
					"[getPositionMaterialsDao] Some error occurred while was trying to execute the query: "
							+ INV_VW_ZONE_POSITIONS_MATERIALS,
					e);
			throw e;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,
						"[getPositionMaterialsDao] Some error occurred while was trying to close the connection.", e);
			}
		}
		return mapMaterials;
	}

	// public List<RouteGroupBean> getGroups(String idRoute) throws SQLException
	// {
	//
	// ConnectionManager iConnectionManager = new ConnectionManager();
	// Connection con =
	// iConnectionManager.createConnection(ConnectionManager.connectionBean);
	// PreparedStatement stm = null;
	//
	// List<RouteGroupBean> listGroups = new ArrayList<RouteGroupBean>();
	//
	// String INV_VW_ROUTE_GROUPS = "SELECT PK_ASG_ID, GROUP_ID ,GDESC
	// ,COUNT_NUM FROM dbo.INV_VW_ROUTE_GROUPS WITH(NOLOCK) WHERE ROUTE_ID = ?
	// ";
	//
	// log.warning(INV_VW_ROUTE_GROUPS);
	// log.log(Level.WARNING, "[getGroupsDao] Preparing sentence...");
	//
	// stm = con.prepareStatement(INV_VW_ROUTE_GROUPS);
	// stm.setString(1, idRoute);
	// log.log(Level.WARNING, "[getGroupsDao] Executing query...");
	//
	// ResultSet rs = stm.executeQuery();
	//
	// while (rs.next()) {
	//
	// RouteGroupBean group = new RouteGroupBean();
	// group.setRouteGroup(rs.getInt(1));
	// group.setGroupId(rs.getString(2));
	// group.setGdesc(rs.getString(3));
	// group.setCountNum(rs.getString(4));
	// group.setRouteId(idRoute);
	// listGroups.add(group);
	// }
	//
	// // Retrive the warnings if there're
	// SQLWarning warning = stm.getWarnings();
	// while (warning != null) {
	// log.log(Level.WARNING, warning.getMessage());
	// warning = warning.getNextWarning();
	// }
	//
	// // Free resources
	// rs.close();
	// stm.close();
	// log.log(Level.WARNING, "[getGroupsDao] Sentence successfully executed.");
	//
	// con.close();
	//
	// return listGroups;
	// }

}
