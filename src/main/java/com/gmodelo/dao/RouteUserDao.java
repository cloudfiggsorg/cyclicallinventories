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

public class RouteUserDao{

	private Logger log = Logger.getLogger(RouteUserDao.class.getName());

	public long updateDowloadTask(String idTask) throws SQLException{
		
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		CallableStatement cs = null;
		
		final String INV_SP_UPDATE_DOWLOAD_TASK = "INV_SP_UPDATE_DOWLOAD_TASK ?, ?, ?";
		long date=0;
		
		log.log(Level.WARNING,"[updateDowloadTaskDao] Preparing sentence...");
		cs = con.prepareCall(INV_SP_UPDATE_DOWLOAD_TASK);
		
		cs.setString(1, idTask);
		cs.registerOutParameter(2, Types.INTEGER);
		cs.registerOutParameter(3, Types.DATE);
		
		log.log(Level.WARNING,"[updateDowloadTaskDao] Executing query...");
		cs.execute();
		
		if(cs.getInt(2) == 1){
			date = cs.getDate(3).getTime();
		}else{
			log.log(Level.WARNING,"[updateDowloadTaskDao] Task not update...");
		}
		
		return date;
	}
	
	public Response<List<RouteUserBean>> getRoutesByUser(User user) {

		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		PreparedStatement stm = null;

		Response<List<RouteUserBean>> res = new Response<List<RouteUserBean>>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		List<RouteUserBean> listRoutesBean = new ArrayList<RouteUserBean>();
		String INV_VW_ROUTES = null;

		INV_VW_ROUTES = "SELECT ROUTE_ID, BUKRS, WERKS, RDESC, RTYPE, BDESC, WDESC, TASK_ID FROM dbo.INV_VW_ROUTES_USER WITH(NOLOCK) WHERE USER_ID = ?";

		log.warning(INV_VW_ROUTES);
		log.log(Level.WARNING, "[getRoutesDaoByUser] Preparing sentence...");

		try {
			stm = con.prepareStatement(INV_VW_ROUTES);

			stm.setString(1, user.getEntity().getIdentyId());

			log.log(Level.WARNING, "[getRoutesDaoByUser] Executing query...");

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				RouteUserBean routeBean = new RouteUserBean();

				routeBean.setRouteId(String.format("%08d",Integer.parseInt(rs.getString("ROUTE_ID"))));
				routeBean.setBukrs(rs.getString("BUKRS"));
				routeBean.setWerks(rs.getString("WERKS"));
				routeBean.setRdesc(rs.getString("RDESC"));
				routeBean.setType(rs.getString("RTYPE"));
				routeBean.setBdesc(rs.getString("BDESC"));
				routeBean.setWdesc(rs.getString("WDESC"));
				routeBean.setDateIni(updateDowloadTask(rs.getString("TASK_ID")));
				routeBean.setPositions(this.getPositions(rs.getString("ROUTE_ID")));
				routeBean.setGroups(this.getGroups(rs.getString("ROUTE_ID")));

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
			log.log(Level.WARNING, "[getRoutesDaoByUser] Sentence successfully executed.");
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
				log.log(Level.SEVERE, "[getRoutesDaoByUser] Some error occurred while was trying to close the connection.",
						e);
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs(e.getMessage());
				res.setAbstractResult(abstractResult);
				return res;
			}
		}

		res.setAbstractResult(abstractResult);
		res.setLsObject(listRoutesBean);
		return res;
	}
	
	public List<RouteUserPositionBean> getPositions(String idRoute) throws SQLException {

		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		PreparedStatement stm = null;

		List<RouteUserPositionBean> listPositions = new ArrayList<RouteUserPositionBean>();

		String INV_VW_ROUTES_WITH_POSITIONS = "SELECT POSITION_ID ,LGORT ,GDES ,ZONE_ID ,SECUENCY, ROUTE_ID FROM dbo.INV_VW_ROUTES_WITH_POSITIONS WITH(NOLOCK) WHERE ROUTE_ID = ? ORDER BY SECUENCY ASC";

		log.warning(INV_VW_ROUTES_WITH_POSITIONS);
		log.log(Level.WARNING, "[getRoutesUserPositionDao] Preparing sentence...");

		stm = con.prepareStatement(INV_VW_ROUTES_WITH_POSITIONS);
		stm.setString(1, idRoute);
		log.log(Level.WARNING, "[getRoutesUserPositionDao] Executing query...");

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
		log.log(Level.WARNING, "[getRoutesUserPositionDao] Sentence successfully executed.");
		con.close();

		return listPositions;
	}
	
	public ZoneUserBean getZoneByPosition(String zoneId) throws SQLException{
		
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		PreparedStatement stm = null;
		
		ZoneUserBean zoneBean = new ZoneUserBean();
		
		String INV_VW_ZONES = "SELECT ZONE_ID, ZDESC FROM dbo.INV_VW_ZONES WHERE ZONE_ID = ?";
		log.log(Level.WARNING,"[getRoutesUserZoneDao] Preparing sentence...");
		
		stm = con.prepareCall(INV_VW_ZONES);
		stm.setString(1, zoneId);
		
		log.log(Level.WARNING,"[getRoutesUserZoneDao] Executing query...");
		ResultSet rs = stm.executeQuery();
		
		while (rs.next()){
			zoneBean.setZoneId(String.format("%08d",Integer.parseInt(rs.getString("ZONE_ID"))));
			zoneBean.setZdesc(rs.getString("ZDESC"));
			zoneBean.setPositionsB(this.getPositionsZone(rs.getString("ZONE_ID")));
		}
		
		//Retrive the warnings if there're
		SQLWarning warning = stm.getWarnings();
		while (warning != null) {
			log.log(Level.WARNING,warning.getMessage());
			warning = warning.getNextWarning();
		}
		
		//Free resources
		rs.close();
		stm.close();	
		con.close();
		log.log(Level.WARNING,"[getRoutesUserZoneDao] Sentence successfully executed.");
			
		return zoneBean;
	}
	
	public List<ZoneUserPositionsBean> getPositionsZone(String zoneId){
			
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		PreparedStatement stm = null;
		List<ZoneUserPositionsBean> listPositions = new ArrayList<ZoneUserPositionsBean>();
		
		String INV_VW_ZONE_WITH_POSITIONS = "SELECT PK_ASG_ID, LGTYP ,LGPLA ,SECUENCY ,IMWM FROM dbo.INV_VW_ZONE_WITH_POSITIONS WHERE ZONE_ID = ? ";
		
		log.warning(INV_VW_ZONE_WITH_POSITIONS);
		log.log(Level.WARNING,"[getRoutesUserZonePositionDao] Preparing sentence...");
		INV_VW_ZONE_WITH_POSITIONS += " GROUP BY PK_ASG_ID, LGTYP ,LGPLA ,SECUENCY ,IMWM ORDER BY SECUENCY ASC";
		try {
			stm = con.prepareCall(INV_VW_ZONE_WITH_POSITIONS);
			stm.setString(1, zoneId);
			log.log(Level.WARNING,"[getRoutesUserZonePositionDao] Executing query...");
			ResultSet rs = stm.executeQuery();
			while (rs.next()){
				
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
			
			//Retrive the warnings if there're
			SQLWarning warning = stm.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING,warning.getMessage());
				warning = warning.getNextWarning();
			}
			
			//Free resources
			rs.close();
			stm.close();	
			
			log.log(Level.WARNING,"[getRoutesUserZonePositionDao] Sentence successfully executed.");
			
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[getRoutesUserZonePositionDao] Some error occurred while was trying to execute the query: "+INV_VW_ZONE_WITH_POSITIONS, e);
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[getRoutesUserZonePositionDao] Some error occurred while was trying to close the connection.", e);
			}
		}
		return listPositions ;
		
	}
	
	private HashMap<String, LgplaValuesBean> getPositionMaterials(String pkAsgId){
		
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		PreparedStatement stm = null;
		HashMap<String, LgplaValuesBean> listMaterials = new HashMap<String, LgplaValuesBean>();
		
		String INV_VW_ZONE_POSITIONS_MATERIALS = "SELECT MATNR, DEN_TYP_MAT FROM dbo.INV_VW_ZONE_POSITIONS_MATERIALS WHERE PK_POS_MAT = ?";
		
		log.warning(INV_VW_ZONE_POSITIONS_MATERIALS);
		log.log(Level.WARNING,"[getPositionMaterialsDao] Preparing sentence...");
		INV_VW_ZONE_POSITIONS_MATERIALS += " GROUP BY MATNR ,DEN_TYP_MAT";
		
		try {
			stm = con.prepareCall(INV_VW_ZONE_POSITIONS_MATERIALS);
			stm.setString(1,pkAsgId );
			log.log(Level.WARNING,"[getPositionMaterialsDao] Executing query...");
			ResultSet rs = stm.executeQuery();
			while (rs.next()){
				
				LgplaValuesBean material = new LgplaValuesBean();
				
				material.setMatnr(rs.getString("MATNR").replaceFirst ("^0*", ""));
				material.setMatkx(rs.getString("DEN_TYP_MAT"));
				material.setLocked(false);
				listMaterials.put(material.toKey(pkAsgId), material);
			}
			
			//Retrive the warnings if there're
			SQLWarning warning = stm.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING,warning.getMessage());
				warning = warning.getNextWarning();
			}
			
			//Free resources
			rs.close();
			stm.close();	
			
			log.log(Level.WARNING,"[getPositionMaterialsDao] Sentence successfully executed.");
			
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[getPositionMaterialsDao] Some error occurred while was trying to execute the query: "+INV_VW_ZONE_POSITIONS_MATERIALS, e);
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[getPositionMaterialsDao] Some error occurred while was trying to close the connection.", e);
			}
		}
		return listMaterials ;
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

		while (rs.next()) {
			
			RouteGroupBean group = new RouteGroupBean();
			group.setRouteGroup(rs.getInt(1));
			group.setGroupId(rs.getString(2));
			group.setGdesc(rs.getString(3));
			group.setCountNum(rs.getString(4));
			group.setRouteId(idRoute);
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
	
}
