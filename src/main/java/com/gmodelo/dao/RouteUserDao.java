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

import com.bmore.ume001.beans.Entity;
import com.bmore.ume001.beans.User;
import com.gmodelo.beans.AbstractResultsBean;
import com.gmodelo.beans.LgplaValuesBean;
import com.gmodelo.beans.Response;
import com.gmodelo.beans.RouteGroupBean;
import com.gmodelo.beans.RouteUserBean;
import com.gmodelo.beans.RouteUserPositionBean;
import com.gmodelo.beans.ZoneUserBean;
import com.gmodelo.beans.ZoneUserPositionsBean;
import com.gmodelo.utils.ConnectionManager;
import com.gmodelo.utils.ReturnValues;

public class RouteUserDao {

	private Logger log = Logger.getLogger(RouteUserDao.class.getName());

	public RouteUserBean getRoutesByUser(User user) throws SQLException {

		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		RouteUserBean routeBean = null;
		final String INV_VW_ROUTES = "SELECT ROUTE_ID, BUKRS, WERKS, RDESC, RTYPE, BDESC, WDESC, TASK_ID FROM dbo.INV_VW_ROUTES_USER WITH(NOLOCK) WHERE USER_ID = ?";
		log.info(INV_VW_ROUTES);
		log.info("[getRoutesByUserDao] Preparing sentence...");
		try {
			stm = con.prepareStatement(INV_VW_ROUTES);
			stm.setString(1, user.getEntity().getIdentyId());
			log.info("[getRoutesByUserDao] Executing query...");
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
				routeBean.setDateIni(updateDowloadTask(rs.getString("TASK_ID")));
				// routeBean.setPositions(this.getPositions(rs.getString("ROUTE_ID")));
			}
			rs.close();
			stm.close();
			log.info("[getRoutesByUserDao] Sentence successfully executed.");
		} finally {
			con.close();
		}
		return routeBean;
	}

	public long updateDowloadTask(String idTask) throws SQLException {
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		CallableStatement cs = null;

		final String INV_SP_UPDATE_DOWLOAD_TASK = "INV_SP_UPDATE_DOWLOAD_TASK ?, ?, ?";
		long date = 0;

		log.info("[updateDowloadTaskDao] Preparing sentence...");
		try {
			cs = con.prepareCall(INV_SP_UPDATE_DOWLOAD_TASK);
			cs.setString(1, idTask);
			cs.registerOutParameter(2, Types.INTEGER);
			cs.registerOutParameter(3, Types.DATE);
			log.info("[updateDowloadTaskDao] Executing query...");
			cs.execute();
			date = cs.getDate(3).getTime();
		} catch (SQLException e) {
			log.log(Level.SEVERE, "[updateDowloadTask] Some error occurred while was trying to execute the query: "
					+ INV_SP_UPDATE_DOWLOAD_TASK, e);
			throw e;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,
						"[updateDowloadTask] Some error occurred while was trying to close the connection.", e);
			}
		}
		return date;
	}

	public List<RouteUserPositionBean> getPositions(String idRoute) throws SQLException {

		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		List<RouteUserPositionBean> listPositions = new ArrayList<RouteUserPositionBean>();
		final String INV_VW_ROUTES_WITH_POSITIONS = "SELECT POSITION_ID ,LGORT ,GDES ,ZONE_ID ,SECUENCY, ROUTE_ID FROM dbo.INV_VW_ROUTES_WITH_POSITIONS WITH(NOLOCK) WHERE ROUTE_ID = ? ORDER BY SECUENCY ASC";

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
				position.setZone(this.getZoneByPosition(rs.getString("ZONE_ID")));
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

	public ZoneUserBean getZoneByPosition(String zoneId) throws SQLException {

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
				zoneBean.setPositionsB(this.getPositionsZone(rs.getString("ZONE_ID")));
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

	public List<ZoneUserPositionsBean> getPositionsZone(String zoneId) throws SQLException {

		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		List<ZoneUserPositionsBean> listPositions = new ArrayList<ZoneUserPositionsBean>();

		final String INV_VW_ZONE_WITH_POSITIONS = "SELECT PK_ASG_ID, LGTYP ,LGPLA ,SECUENCY ,IMWM FROM dbo.INV_VW_ZONE_WITH_POSITIONS WHERE ZONE_ID = ? "
				+ "GROUP BY PK_ASG_ID,LGTYP,LGPLA,SECUENCY,IMWM ORDER BY SECUENCY ASC";

		log.info(INV_VW_ZONE_WITH_POSITIONS);
		log.info("[getPositionsZoneDao] Preparing sentence...");
		try {
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
				position.setLgplaValues(this.getPositionMaterials(rs.getString("PK_ASG_ID")));
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
			log.log(Level.SEVERE,
					"[getPositionsZoneDao] Some error occurred while was trying to execute the query: "
							+ INV_VW_ZONE_WITH_POSITIONS,
					e);
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

	private HashMap<String, LgplaValuesBean> getPositionMaterials(String pkAsgId) throws SQLException {

		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		HashMap<String, LgplaValuesBean> listMaterials = new HashMap<String, LgplaValuesBean>();

		final String INV_VW_ZONE_POSITIONS_MATERIALS = "SELECT MATNR, MAKTX FROM dbo.INV_VW_ZONE_POSITIONS_MATERIALS WHERE POSITION_ID = ? GROUP BY MATNR ,MAKTX";

		log.info(INV_VW_ZONE_POSITIONS_MATERIALS);
		log.info("[getPositionMaterialsDao] Preparing sentence...");
		try {
			stm = con.prepareCall(INV_VW_ZONE_POSITIONS_MATERIALS);
			stm.setString(1, pkAsgId);
			log.info("[getPositionMaterialsDao] Executing query...");
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {

				LgplaValuesBean material = new LgplaValuesBean();

				material.setMatnr(rs.getString("MATNR").replaceFirst("^0*", ""));
				material.setMatkx(rs.getString("MAKTX"));
				material.setLocked(true);
				listMaterials.put(material.toKey(pkAsgId), material);
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
		return listMaterials;
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
