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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.NamingException;

import com.bmore.ume001.beans.User;
import com.gmodelo.beans.AbstractResultsBean;
import com.gmodelo.beans.DocInvBean;
import com.gmodelo.beans.DocInvPositionBean;
import com.gmodelo.beans.LagpEntity;
import com.gmodelo.beans.MaterialToZoneBean;
import com.gmodelo.beans.Response;
import com.gmodelo.beans.TgortB;
import com.gmodelo.beans.ZoneBean;
import com.gmodelo.beans.ZonePositionMaterialsBean;
import com.gmodelo.beans.ZonePositionsBean;
import com.gmodelo.utils.ConnectionManager;
import com.gmodelo.utils.ReturnValues;

public class ZoneDao {

	private Logger log = Logger.getLogger(ZoneDao.class.getName());
	private UMEDaoE ume;
	private User user;

	public Response<ZoneBean> addZone(ZoneBean zoneBean, String createdBy) {

		Response<ZoneBean> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		CallableStatement cs = null;
		CallableStatement csBatch = null;
		int idPosition = 0;
		int zoneId = 0;

		try {
			zoneId = Integer.parseInt(zoneBean.getZoneId());
		} catch (NumberFormatException e) {
			// TODO: handle exception
		}

		log.info("[addZone] Prev Validate Position");
		Response<ZoneBean> resZoneBean = validateZonePositions(zoneBean);
		log.info("[addZone] After validate positions" + zoneBean);

		if (resZoneBean.getAbstractResult().getResultId() == 1) {

			zoneBean = resZoneBean.getLsObject();
		} else {

			return resZoneBean;
		}
		String CURRENTSP = "";
		final String INV_SP_ADD_ZONE = "INV_SP_ADD_ZONE ?, ?, ?, ?, ?, ?, ?";
		final String INV_SP_DEL_ZONE_POSITION = "INV_SP_DEL_ZONE_POSITION ?";
		final String INV_SP_ADD_POSITION_ZONE = "INV_SP_ADD_POSITION_ZONE ?, ?, ?, ?, ?, ?, ?";
		final String INV_SP_ASSIGN_MATERIAL_TO_ZONE = "INV_SP_ASSIGN_MATERIAL_TO_ZONE ?, ?, ?";
		log.info("[addZone] Preparing sentence...");
		try {
			con.setAutoCommit(false);

			CURRENTSP = INV_SP_ADD_ZONE;
			cs = con.prepareCall(INV_SP_ADD_ZONE);
			cs.setString(1, zoneBean.getZoneId());
			cs.setString(2, zoneBean.getZdesc());
			cs.setString(3, zoneBean.getBukrs());
			cs.setString(4, zoneBean.getWerks());
			cs.setString(5, zoneBean.getLgort());
			cs.setString(6, createdBy);

			cs.registerOutParameter(1, Types.INTEGER);
			cs.registerOutParameter(6, Types.VARCHAR);
			cs.registerOutParameter(7, Types.VARCHAR);

			log.info("[addZone] Executing query...");

			cs.execute();

			zoneBean.setZoneId("" + cs.getInt(1)); // addZeroscs
			zoneBean.setCreatedBy(cs.getString(6));
			user = new User();
			ume = new UMEDaoE();
			user.getEntity().setIdentyId(cs.getString(6));
			ArrayList<User> ls = new ArrayList<>();
			ls.add(user);
			ls = ume.getUsersLDAPByCredentials(ls);

			if (ls.size() > 0) {

				zoneBean.setCreatedBy(cs.getString(6) + " - " + ls.get(0).getGenInf().getName() + " "
						+ ls.get(0).getGenInf().getLastName());
			} else {
				zoneBean.setCreatedBy(cs.getString(6));
			}

			user.getEntity().setIdentyId(cs.getString(7));
			ls = new ArrayList<>();
			ls.add(user);
			ls = ume.getUsersLDAPByCredentials(ls);

			if (ls.size() > 0) {

				zoneBean.setModifiedBy(cs.getString(7) + " - " + ls.get(0).getGenInf().getName() + " "
						+ ls.get(0).getGenInf().getLastName());
			} else {
				zoneBean.setModifiedBy(cs.getString(7));
			}

			// Eliminar posiciones
			CURRENTSP = INV_SP_DEL_ZONE_POSITION;
			cs = null;
			cs = con.prepareCall(INV_SP_DEL_ZONE_POSITION);
			cs.setInt(1, zoneId);
			cs.execute();

			CURRENTSP = INV_SP_ADD_POSITION_ZONE;
			csBatch = null;
			csBatch = con.prepareCall(INV_SP_ADD_POSITION_ZONE);

			for (ZonePositionsBean zonePos : zoneBean.getPositions()) {

				zonePos.setZoneId(zoneBean.getZoneId());
				if (zonePos.getMaterials() != null && !zonePos.getMaterials().isEmpty()) {
					CURRENTSP = INV_SP_ADD_POSITION_ZONE;
					cs = null;
					cs = con.prepareCall(INV_SP_ADD_POSITION_ZONE);
					cs.setString(1, zoneBean.getZoneId());
					cs.setString(2, zonePos.getLgtyp());
					cs.setString(3, zonePos.getLgpla());
					cs.setInt(4, zonePos.getSecuency());
					cs.setString(5, zonePos.getImwm());
					cs.setString(6, zonePos.getLgnum());
					cs.registerOutParameter(7, Types.INTEGER);
					log.info("[addPositionZone] Executing query...");
					cs.execute();
					idPosition = cs.getInt(7);
					zonePos.setPkAsgId(idPosition);
					// Eliminar materiales de posición
					for (ZonePositionMaterialsBean zoneMaterial : zonePos.getMaterials()) {
						CURRENTSP = INV_SP_ASSIGN_MATERIAL_TO_ZONE;
						zoneMaterial.setPosMat(idPosition);
						cs = null;
						cs = con.prepareCall(INV_SP_ASSIGN_MATERIAL_TO_ZONE);
						cs.setInt(1, zoneMaterial.getPosMat());
						cs.setString(2, zoneMaterial.getMatnr());
						cs.registerOutParameter(3, Types.INTEGER);
						log.info("[assignMaterialToZoneDao] Executing query...");

						cs.execute();
					}
				} else {
					csBatch.setString(1, zoneBean.getZoneId());
					csBatch.setString(2, zonePos.getLgtyp());
					csBatch.setString(3, zonePos.getLgpla());
					csBatch.setInt(4, zonePos.getSecuency());
					csBatch.setString(5, zonePos.getImwm());
					csBatch.setString(6, zonePos.getLgnum());
					csBatch.setNull(7, Types.INTEGER);
					csBatch.addBatch();
				}
			}
			log.info("[addPositionZone] Executing batch..." + csBatch.executeBatch());

			// Retrive the warnings if there're
			SQLWarning warning = cs.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING, warning.getMessage());
				warning = warning.getNextWarning();
			}

			con.commit();
			// Free resources
			cs.close();

			log.info("[addZone] Sentence successfully executed.");

		} catch (SQLException | NamingException e) {
			try {
				log.log(Level.WARNING, "[addZone] Execute rollback");
				con.rollback();
			} catch (SQLException e1) {
				log.log(Level.SEVERE, "[addZone] Not rollback .", e);
			}

			log.log(Level.SEVERE, "[addZone] Some error occurred while was trying to execute the S.P.: " + CURRENTSP,
					e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			res.setAbstractResult(abstractResult);
			return res;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE, "[addZone] Some error occurred while was trying to close the connection.", e);
			}
		}
		res.setAbstractResult(abstractResult);
		res.setLsObject(zoneBean);
		return res;
	}

	public Response<Object> deleteZone(String arrayIdZones) {

		Response<Object> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		CallableStatement cs = null;

		final String INV_SP_DEL_ZONE = "INV_SP_DEL_ZONE ?"; // The Store
															// procedure to call

		log.info("[deleteZone] Preparing sentence...");

		try {
			cs = con.prepareCall(INV_SP_DEL_ZONE);

			cs.setString(1, arrayIdZones);

			log.info("[deleteZone] Executing query...");

			cs.execute();

			// Retrive the warnings if there're
			SQLWarning warning = cs.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING, "[deleteZone] " + warning.getMessage());
				warning = warning.getNextWarning();
			}

			// Free resources
			cs.close();

			log.info("[deleteZone] Sentence successfully executed.");

		} catch (SQLException e) {
			log.log(Level.SEVERE,
					"[deleteZone] Some error occurred while was trying to execute the S.P.: " + INV_SP_DEL_ZONE, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE, "[deleteZone] Some error occurred while was trying to close the connection.", e);
			}
		}
		res.setAbstractResult(abstractResult);
		return res;
	}

	// vista se agrega LGOBE y query se modifica para que siempre incluya AND de
	// bukrs y werks y un OR de idZone y ZoneDesc
	public Response<List<ZoneBean>> getLgortByZone(ZoneBean zoneBean) {
		Response<List<ZoneBean>> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		List<ZoneBean> listZone = new ArrayList<ZoneBean>();
		String INV_VW_ZONE_BY_LGORT = "SELECT [LGORT], [LGOBE], [ZONE_ID], [ZON_DESC], [BUKRS], [WERKS] FROM [INV_CIC_DB].[dbo].[INV_VW_ZONE_BY_LGORT]"
				+ " WHERE BUKRS = ? AND WERKS = ? ";
		if (zoneBean.getZdesc() == null && zoneBean.getZoneId() == null) {
			log.info("[getZoneByLgortDao] null Params...");
			INV_VW_ZONE_BY_LGORT += "";
		} else if (zoneBean.getZdesc() == null && zoneBean.getZoneId() != null) {
			log.info("[getZoneByLgortDao] not Null zoneID Object...");
			INV_VW_ZONE_BY_LGORT += " AND ZONE_ID = " + zoneBean.getZoneId();
		} else {
			log.info("[getZoneByLgortDao] not null Params...");
			INV_VW_ZONE_BY_LGORT += " AND ( ZONE_ID LIKE '%"
					+ (zoneBean.getZoneId() != null ? zoneBean.getZoneId() : "") + "%'  OR ZON_DESC LIKE '%"
					+ (zoneBean.getZdesc() != null ? zoneBean.getZdesc() : "") + "%' ) ";
		}
		INV_VW_ZONE_BY_LGORT += " GROUP BY [LGORT], [LGOBE], [ZONE_ID], [ZON_DESC], [BUKRS], [WERKS] ";
		INV_VW_ZONE_BY_LGORT += " ORDER BY [ZONE_ID]";
		log.info(INV_VW_ZONE_BY_LGORT);
		log.info("[getZoneByLgortDao] Preparing sentence...");
		try {
			stm = con.prepareCall(INV_VW_ZONE_BY_LGORT);
			stm.setString(1, zoneBean.getBukrs());
			stm.setString(2, zoneBean.getWerks());
			log.info("[getZoneByLgortDao] Executing query...");
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				zoneBean = new ZoneBean();
				zoneBean.setLgort(rs.getString("LGORT"));
				zoneBean.setgDesc(rs.getString("LGOBE"));
				zoneBean.setZoneId(rs.getString("ZONE_ID"));
				zoneBean.setZdesc(rs.getString("ZON_DESC"));
				zoneBean.setBukrs(rs.getString("BUKRS"));
				zoneBean.setWerks(rs.getString("WERKS"));
				listZone.add(zoneBean);
			}
			log.info("[getZoneByLgortDao] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE, "[getZoneByLgortDao] Some error occurred while was trying to execute the query: "
					+ INV_VW_ZONE_BY_LGORT, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,
						"[getZoneByLgortDao] Some error occurred while was trying to close the connection.", e);
			}
		}
		res.setAbstractResult(abstractResult);
		res.setLsObject(listZone);
		return res;
	}

	public ZoneBean getZoneById(ZoneBean zoneBean, Connection con) {

		ConnectionManager iConnectionManager = new ConnectionManager();
		try {
			con = con.isValid(3) ? iConnectionManager.createConnection() : con;
		} catch (SQLException e1) {
			log.log(Level.SEVERE, "[getZoneById] Some error ocurred while was trying to check the connection");
			return null;
		}
		PreparedStatement stm = null;
		ZoneBean zb = null;

		String INV_VW_ZONE_BY_LGORT = "SELECT [LGORT], [LGOBE], [ZONE_ID], [ZON_DESC], [BUKRS], [WERKS] FROM [INV_CIC_DB].[dbo].[INV_VW_ZONE_BY_LGORT] ";
		INV_VW_ZONE_BY_LGORT += "WHERE ZONE_ID LIKE '" + zoneBean.getZoneId() + "' ";
		INV_VW_ZONE_BY_LGORT += "AND BUKRS = '" + zoneBean.getBukrs() + "' ";
		INV_VW_ZONE_BY_LGORT += "AND WERKS = '" + zoneBean.getWerks() + "' ";
		INV_VW_ZONE_BY_LGORT += "GROUP BY [LGORT], [LGOBE], [ZONE_ID], [ZON_DESC], [BUKRS], [WERKS] ";
		INV_VW_ZONE_BY_LGORT += "ORDER BY [ZONE_ID]";

		log.info(INV_VW_ZONE_BY_LGORT);

		log.info("[getZoneById] Preparing sentence...");

		try {
			stm = con.prepareCall(INV_VW_ZONE_BY_LGORT);

			log.info("[getZoneById] Executing query...");

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {

				zb = new ZoneBean();

				zb.setLgort(rs.getString("LGORT"));
				zb.setgDesc(rs.getString("LGOBE"));
				zb.setZoneId(String.format("%08d", Integer.parseInt(rs.getString("ZONE_ID"))));
				zb.setZdesc(rs.getString("ZON_DESC"));
				zb.setBukrs(rs.getString("BUKRS"));
				zb.setWerks(rs.getString("WERKS"));

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

			log.info("[getZoneById] Sentence successfully executed.");

		} catch (SQLException e) {
			log.log(Level.SEVERE,
					"[getZoneById] Some error occurred while was trying to execute the query: " + INV_VW_ZONE_BY_LGORT,
					e);
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE, "[getZoneById] Some error occurred while was trying to close the connection.", e);
			}
		}
		return zb;
	}

	public Response<List<ZoneBean>> validateZone(ZoneBean zoneBean) {

		Response<List<ZoneBean>> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		List<ZoneBean> listZone = new ArrayList<ZoneBean>();

		String INV_VW_ZONE_BY_LGORT = "SELECT [LGORT], [ZONE_ID], [ZON_DESC], [BUKRS], [WERKS] FROM [INV_CIC_DB].[dbo].[INV_VW_ZONE_BY_LGORT] "
				+ "WHERE BUKRS = '" + zoneBean.getBukrs() + "' AND WERKS = '" + zoneBean.getWerks() + "' ";

		String OR = "AND ( ZONE_ID LIKE '%" + zoneBean.getZoneId() + "%' AND ZON_DESC LIKE '%" + zoneBean.getZdesc()
				+ "%' )";
		// query

		String condition = buildCondition(zoneBean);
		if (condition != null) {
			INV_VW_ZONE_BY_LGORT += condition;
		}

		if (zoneBean.getZoneId() != null) {
			INV_VW_ZONE_BY_LGORT += OR;
		}

		INV_VW_ZONE_BY_LGORT += " GROUP BY [LGORT], [ZONE_ID], [ZON_DESC], [BUKRS], [WERKS] ";
		INV_VW_ZONE_BY_LGORT += "ORDER BY [ZONE_ID]";

		log.info(INV_VW_ZONE_BY_LGORT);

		log.info("[validateZoneDao] Preparing sentence...");

		try {
			stm = con.prepareCall(INV_VW_ZONE_BY_LGORT);

			log.log(Level.WARNING, "[validateZoneDao] Executing query...");

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {

				zoneBean = new ZoneBean();

				zoneBean.setLgort(rs.getString(1));
				zoneBean.setZoneId(rs.getString(2));
				zoneBean.setZdesc(rs.getString(3));
				zoneBean.setBukrs(rs.getString(4));
				zoneBean.setWerks(rs.getString(5));

				listZone.add(zoneBean);

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

			log.info("[validateZoneDao] Sentence successfully executed.");

		} catch (SQLException e) {
			log.log(Level.SEVERE, "[validateZoneDao] Some error occurred while was trying to execute the query: "
					+ INV_VW_ZONE_BY_LGORT, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE, "[validateZoneDao] Some error occurred while was trying to close the connection.",
						e);
			}
		}
		res.setAbstractResult(abstractResult);
		res.setLsObject(listZone);
		return res;
	}

	private String buildCondition(ZoneBean zoneBean) {
		String zoneId = "";
		String zdesc = "";
		String bukrs = "";
		String werks = "";
		String lgort = "";
		String condition = "WHERE ";

		bukrs = (zoneBean.getBukrs() != null ? " BUKRS = '" + zoneBean.getBukrs() + "'" : "");
		condition += bukrs;
		werks = (zoneBean.getWerks() != null ? " AND WERKS = '" + zoneBean.getWerks() + "'" : "");
		condition += werks;
		zoneId = (zoneBean.getZoneId() != null ? " AND ZONE_ID = " + Integer.parseInt(zoneBean.getZoneId()) : "");
		condition += zoneId;
		zdesc = (zoneBean.getZdesc() != null ? " AND ZON_DESC LIKE '%" + zoneBean.getZdesc() + "%'" : "");
		condition += zdesc;
		lgort = (zoneBean.getLgort() != null ? "AND LGORT LIKE '%" + zoneBean.getLgort() + "%'" : "");
		condition += lgort;
		condition = condition.isEmpty() ? null : condition;

		return condition;
	}

	public Response<List<ZoneBean>> getZones(ZoneBean zoneBean, String searchFilter) {
		Response<List<ZoneBean>> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		List<ZoneBean> listZone = new ArrayList<ZoneBean>();
		int aux;
		String searchFilterNumber = "";
		try {
			aux = Integer.parseInt(searchFilter);
			searchFilterNumber += aux;
		} catch (Exception e) {
			searchFilterNumber = searchFilter;
			log.info("Trying to convert String to Int");
		}
		String INV_VW_ZONES = "SELECT ZONE_ID, ZDESC, BUKRS, WERKS, LGORT, BDESC, WDESC, GDES, CREATED_BY, MODIFIED_BY FROM dbo.INV_VW_ZONES";
		if (searchFilter != null) {

			INV_VW_ZONES += " WHERE ZONE_ID LIKE '%" + searchFilterNumber + "%' OR ZDESC LIKE '%" + searchFilter
					+ "%' ";
		} else {

			String condition = buildConditionZones(zoneBean);
			if (condition != null) {
				INV_VW_ZONES += condition;
			}
		}

		log.info(INV_VW_ZONES);
		// INV_VW_ZONES += " GROUP BY ZONE_ID, ZDESC, BUKRS, WERKS, LGORT,
		// BDESC, WDESC, GDES";
		log.info("[getZonesDao] Preparing sentence...");

		ume = new UMEDaoE();
		user = new User();

		try {
			stm = con.prepareCall(INV_VW_ZONES);
			log.info("[getZonesDao] Executing query...");
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {

				zoneBean = new ZoneBean();

				zoneBean.setZoneId(rs.getString("ZONE_ID"));
				zoneBean.setZdesc(rs.getString("ZDESC"));
				zoneBean.setBukrs(rs.getString("BUKRS"));
				zoneBean.setWerks(rs.getString("WERKS"));
				zoneBean.setLgort(rs.getString("LGORT"));
				zoneBean.setbDesc(rs.getString("BDESC"));
				zoneBean.setwDesc(rs.getString("WDESC"));
				zoneBean.setgDesc(rs.getString("GDES"));

				user = new User();
				user.getEntity().setIdentyId(rs.getString("CREATED_BY"));
				ArrayList<User> ls = new ArrayList<>();
				ls.add(user);
				ls = ume.getUsersLDAPByCredentials(ls);

				if (ls.size() > 0) {

					zoneBean.setCreatedBy(rs.getString("CREATED_BY") + " - " + ls.get(0).getGenInf().getName() + " "
							+ ls.get(0).getGenInf().getLastName());
				} else {
					zoneBean.setCreatedBy(rs.getString("CREATED_BY"));
				}

				user.getEntity().setIdentyId(rs.getString("MODIFIED_BY"));
				ls = new ArrayList<>();
				ls.add(user);
				ls = ume.getUsersLDAPByCredentials(ls);

				if (ls.size() > 0) {

					zoneBean.setModifiedBy(rs.getString("MODIFIED_BY") + " - " + ls.get(0).getGenInf().getName() + " "
							+ ls.get(0).getGenInf().getLastName());
				} else {
					zoneBean.setModifiedBy(rs.getString("MODIFIED_BY"));
				}

				zoneBean.setPositions(this.getPositionsZone(rs.getString("ZONE_ID")));
				listZone.add(zoneBean);

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

			log.info("[getZonesDao] Sentence successfully executed.");

		} catch (SQLException | NamingException e) {
			log.log(Level.SEVERE,
					"[getZonesDao] Some error occurred while was trying to execute the query: " + INV_VW_ZONES, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE, "[getZonesDao] Some error occurred while was trying to close the connection.", e);
			}
		}
		res.setAbstractResult(abstractResult);
		res.setLsObject(listZone);
		return res;
	}

	public Response<List<ZoneBean>> getZonesOnly(String searchFilter) {

		Response<List<ZoneBean>> res = new Response<>();
		List<ZoneBean> listZone = new ArrayList<ZoneBean>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		int aux;
		ZoneBean zoneBean = null;
		String searchFilterNumber = "";

		try {
			aux = Integer.parseInt(searchFilter);
			searchFilterNumber += aux;
		} catch (Exception e) {
			searchFilterNumber = searchFilter;
			log.info("Trying to convert String to Int");
		}

		String INV_VW_ZONES = "SELECT ZONE_ID, ZDESC FROM dbo.INV_VW_ZONES";
		INV_VW_ZONES += " WHERE (ZONE_ID LIKE '%" + searchFilterNumber + "%' OR ZDESC LIKE '%" + searchFilter + "%') ";
		INV_VW_ZONES += " AND ZONE_ID NOT IN (SELECT IZ.ZONE_ID " + "FROM INV_DOC_INVENTORY_HEADER AS IDIH "
				+ "INNER JOIN INV_ROUTE_POSITION AS IRP ON (IDIH.DIH_ROUTE_ID = IRP.RPO_ROUTE_ID) "
				+ "INNER JOIN INV_ZONE AS IZ ON (IZ.ZONE_ID = IRP.RPO_ZONE_ID) " + "WHERE DIH_STATUS = '1')";
		INV_VW_ZONES += " GROUP BY ZONE_ID, ZDESC, BUKRS, WERKS, LGORT, BDESC, WDESC, GDES";
		log.info(INV_VW_ZONES);
		log.info("[getZonesOnlyDao] Preparing sentence...");

		try {
			stm = con.prepareCall(INV_VW_ZONES);
			log.info("[getZonesOnlyDao] Executing query...");
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {

				zoneBean = new ZoneBean();
				zoneBean.setZoneId(rs.getString("ZONE_ID"));
				zoneBean.setZdesc(rs.getString("ZDESC"));
				listZone.add(zoneBean);
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

			log.info("[getZonesOnlyDao] Sentence successfully executed.");

		} catch (SQLException e) {
			log.log(Level.SEVERE,
					"[getZonesOnlyDao] Some error occurred while was trying to execute the query: " + INV_VW_ZONES, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE, "[getZonesOnlyDao] Some error occurred while was trying to close the connection.",
						e);
			}
		}

		res.setAbstractResult(abstractResult);
		res.setLsObject(listZone);
		return res;
	}

	public Response<ZoneBean> validateZonePositions(ZoneBean zb) {

		ConnectionManager iConnectionManager = new ConnectionManager();
		Response<ZoneBean> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		Connection con = iConnectionManager.createConnection();
		List<ZonePositionsBean> lsZpb = zb.getPositions();
		TgortB tgb, tgbAux;
		String zpmb;
		HashMap<String, TgortB> evalMap = new HashMap<>();
		try {
			// Fill Unique Map
			log.info("[validateZonePositions] Fill Unique Map.");
			for (ZonePositionsBean zpToEval : lsZpb) {
				String key = zb.getWerks() + zb.getLgort() + zpToEval.getLgnum() + zpToEval.getLgtyp();
				if (zpToEval.getImwm() != null) {
					continue;
				} else {
					if (!evalMap.containsKey(key)) {
						tgb = new TgortB();
						tgb.setWerks(zb.getWerks());
						tgb.setLgort(zb.getLgort());
						tgb.setLgNum(zpToEval.getLgnum());
						tgb.setLgTyp(zpToEval.getLgtyp());
						evalMap.put(key, tgb);
					}
				}
			}
			log.info("[validateZonePositions] Get Correct Values for Unique Map.");
			if (!evalMap.isEmpty()) {
				Iterator iteEval = evalMap.entrySet().iterator();
				while (iteEval.hasNext()) {
					Map.Entry pair = (Map.Entry) iteEval.next();
					TgortB tgmpAux = (TgortB) pair.getValue();
					tgbAux = new TgortDao().getLgTypByWerksAndLgort(tgmpAux, con);
					if (tgbAux == null) {
						log.log(Level.SEVERE, "Invalid data...");
						abstractResult.setResultId(ReturnValues.IEXCEPTION);
						abstractResult.setResultMsgAbs("Datos no válidos para \"LGNUM\": " + tgmpAux.getLgNum() + " y "
								+ "\"Tipo de Almacén\": " + tgmpAux.getLgTyp());
						res.setAbstractResult(abstractResult);
						return res;
					} else {
						tgmpAux.setLtypt(tgbAux.getLtypt());
						tgmpAux.setImwm(tgbAux.getImwm());
						evalMap.put(pair.getKey().toString(), tgmpAux);
					}
				}

			} else {
				log.log(Level.SEVERE, "Invalid data...");
				abstractResult.setResultId(ReturnValues.IEMPTY);
				abstractResult.setResultMsgAbs("Datos ingresados no validos para el proceso");
				res.setAbstractResult(abstractResult);
				return res;
			}

			Iterator iteEval = evalMap.entrySet().iterator();
			HashMap<String, String> mapMatWer = new MatnrDao().getTmatnrByTmatnr(zb, con);
			log.info("[validateZonePositions] Empieza lo weno." + mapMatWer.size());
			while (iteEval.hasNext()) {
				Map.Entry pair = (Map.Entry) iteEval.next();
				TgortB tgmpAux = (TgortB) pair.getValue();
				List<String> lgplaValues = new ArrayList<>();
				LagpEntity lagpEntity = new LagpEntity();
				lagpEntity.setLgNum(tgmpAux.getLgNum());
				lagpEntity.setLgTyp(tgmpAux.getLgTyp());
				lagpEntity.setImwm(tgmpAux.getImwm());
				log.info("LagpEntity:" + lagpEntity);
				lgplaValues = new LagpDao().getLgplaList(lagpEntity, con);
				if (lgplaValues != null) {
					for (ZonePositionsBean zpToEval : lsZpb) {
						if (zpToEval.getImwm() == null) {
							if (zpToEval.getLgnum().equals(tgmpAux.getLgNum())
									&& zpToEval.getLgtyp().equals(tgmpAux.getLgTyp())) {
								zpToEval.setImwm(tgmpAux.getImwm());
								zpToEval.setLgtypDesc(tgmpAux.getLtypt());
								if (lgplaValues.contains(zpToEval.getLgpla())) {
									lgplaValues.remove(zpToEval.getLgpla());
									if (zpToEval.getMaterials() != null && !zpToEval.getMaterials().isEmpty()) {
										boolean hasData = false;
										for (ZonePositionMaterialsBean materialBean : zpToEval.getMaterials()) {
											if (materialBean.getMatnr().trim().length() == 0) {
												continue;
											}
											hasData = true;
											zpmb = mapMatWer.get(materialBean.getMatnr());
											if (zpmb == null) {
												log.log(Level.SEVERE, "Invalid data...");
												abstractResult.setResultId(ReturnValues.IEXCEPTION);
												abstractResult.setResultMsgAbs(
														"Material inválido: \"" + materialBean.getMatnr() + " \" "
																+ "para la secuencia: " + zpToEval.getSecuency());
												res.setAbstractResult(abstractResult);
												return res;
											}
											materialBean.setDescM(zpmb);
										}
										if (!hasData) {
											zpToEval.setMaterials(null);
										}
									}
								} else {
									log.log(Level.SEVERE, "Invalid data...");
									abstractResult.setResultId(ReturnValues.IEXCEPTION);
									abstractResult.setResultMsgAbs("Datos no válidos para \"LGNUM\": "
											+ zpToEval.getLgnum() + " y " + " \"Tipo de Almacén\": "
											+ zpToEval.getLgtyp() + " \"Ubicación\": " + zpToEval.getLgpla());
									res.setAbstractResult(abstractResult);
									return res;
								}
							}
						}
					}
				} else {
					log.log(Level.SEVERE, "Invalid data...");
					abstractResult.setResultId(ReturnValues.IEXCEPTION);
					abstractResult.setResultMsgAbs("Datos no válidos para \"LGNUM\": " + tgmpAux.getLgNum() + " y "
							+ "\"Tipo de Almacén\": " + tgmpAux.getLgTyp());
					res.setAbstractResult(abstractResult);
					return res;
				}
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, "Invalid data...", e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs("Ocurrio un error de ejecucion");
			res.setAbstractResult(abstractResult);
			return res;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,
						"[validateZonePositions] Some error occurred while was trying to close the connection.", e);
			}
		}
		res.setAbstractResult(abstractResult);
		res.setLsObject(zb);
		return res;
	}

	private static final String INV_VW_ZONE_WITH_POSITIONS = "SELECT PK_ASG_ID, LGTYP ,LGPLA ,SECUENCY ,IMWM, ZPO_LGNUM, LTYPT FROM dbo.INV_VW_ZONE_WITH_POSITIONS WHERE ZONE_ID = ?"
			+ " GROUP BY PK_ASG_ID, LGTYP ,LGPLA ,SECUENCY ,IMWM, ZPO_LGNUM, LTYPT";

	private List<ZonePositionsBean> getPositionsZone(String zoneId) {
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		List<ZonePositionsBean> listPositions = new ArrayList<ZonePositionsBean>();

		try {
			HashMap<String, List<ZonePositionMaterialsBean>> mapPosition = this.getPositionMaterials();
			log.info("[getZonesDao] mapPosition " + mapPosition.size());
			stm = con.prepareCall(INV_VW_ZONE_WITH_POSITIONS);
			stm.setString(1, zoneId);
			log.info(INV_VW_ZONE_WITH_POSITIONS);
			log.info("[getZonesDao] Executing query...");
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				ZonePositionsBean position = new ZonePositionsBean();
				position.setZoneId(zoneId);
				position.setPkAsgId(rs.getInt("PK_ASG_ID"));
				position.setLgtyp(rs.getString("LGTYP"));
				position.setLgpla(rs.getString("LGPLA"));
				position.setSecuency(rs.getInt("SECUENCY"));
				position.setImwm(rs.getString("IMWM"));
				position.setLgtypDesc(rs.getString("LTYPT"));
				position.setLgnum(rs.getString("ZPO_LGNUM"));
				position.setMaterials(mapPosition.get(rs.getString("PK_ASG_ID")));
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

			log.info("[getZonesDao] Sentence successfully executed.");

		} catch (SQLException e) {
			log.log(Level.SEVERE, "[getZonesDao] Some error occurred while was trying to execute the query: "
					+ INV_VW_ZONE_WITH_POSITIONS, e);
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE, "[getZonesDao] Some error occurred while was trying to close the connection.", e);
			}
		}
		return listPositions;
	}

	private static final String INV_VW_ZONE_POSITIONS_MATERIALS = "SELECT PK_POS_MAT, POSITION_ID, MATNR, MAKTX FROM INV_VW_ZONE_POSITIONS_MATERIALS WITH(NOLOCK) GROUP BY PK_POS_MAT, POSITION_ID, MATNR, MAKTX";

	private HashMap<String, List<ZonePositionMaterialsBean>> getPositionMaterials() {

		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		HashMap<String, List<ZonePositionMaterialsBean>> posMatMap = new HashMap<>();

		log.info(INV_VW_ZONE_POSITIONS_MATERIALS);
		log.info("[getZonesDao] Preparing sentence...");

		try {
			stm = con.prepareCall(INV_VW_ZONE_POSITIONS_MATERIALS);
			log.info("[getZonesDao] Executing query...");
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				if (posMatMap.containsKey(rs.getString("POSITION_ID"))) {
					ZonePositionMaterialsBean material = new ZonePositionMaterialsBean();
					material.setPosMat(rs.getInt("PK_POS_MAT"));
					material.setMatnr(rs.getString("MATNR"));
					material.setDescM(rs.getString("MAKTX"));
					posMatMap.get(rs.getString("POSITION_ID")).add(material);
				} else {
					List<ZonePositionMaterialsBean> listMaterials = new ArrayList<ZonePositionMaterialsBean>();
					ZonePositionMaterialsBean material = new ZonePositionMaterialsBean();
					material.setPosMat(rs.getInt("PK_POS_MAT"));
					material.setMatnr(rs.getString("MATNR"));
					material.setDescM(rs.getString("MAKTX"));
					listMaterials.add(material);
					posMatMap.put(rs.getString("POSITION_ID"), listMaterials);
				}

			}
			// Free resources
			rs.close();
			stm.close();
			log.info("[getZonesDao] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE, "[getZonesDao] Some error occurred while was trying to execute the query: "
					+ INV_VW_ZONE_POSITIONS_MATERIALS, e);
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE, "[getZonesDao] Some error occurred while was trying to close the connection.", e);
			}
		}
		return posMatMap;
	}

	private String buildConditionZones(ZoneBean zoneB) {
		String condition = "";
		String zoneId = "";
		String zdesc = "";
		String bukrs = "";
		String werks = "";
		String lgort = "";

		zoneId = (zoneB.getZoneId() != null ? (condition.contains("WHERE") ? " AND " : " WHERE ") + "ZONE_ID LIKE '%"
				+ zoneB.getZoneId().replaceFirst("^0*", "") + "%' " : "");
		condition += zoneId;
		zdesc = (zoneB.getZdesc() != null
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + "ZDESC LIKE '%" + zoneB.getZdesc() + "%' "
				: "");
		condition += zdesc;
		bukrs = (zoneB.getBukrs() != null
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + "BUKRS LIKE '%" + zoneB.getBukrs() + "%' "
				: "");
		condition += bukrs;
		werks = (zoneB.getWerks() != null
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + "WERKS LIKE '%" + zoneB.getWerks() + "%' "
				: "");
		condition += werks;
		lgort = (zoneB.getLgort() != null
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + "LGORT LIKE '%" + zoneB.getLgort() + "%' "
				: "");
		condition += lgort;

		condition = condition.isEmpty() ? null : condition;
		return condition;
	}

	public Response<Object> unassignMaterialToZone(MaterialToZoneBean materialToZoneBean) {

		Response<Object> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		CallableStatement cs = null;
		String resultSP = null;

		final String INV_SP_DESASSIGN_MATERIAL_TO_ZONE = "INV_SP_DESASSIGN_MATERIAL_TO_ZONE ?, ?, ?"; // The
																										// Store
																										// procedure
																										// to
																										// call

		log.info("[unassignMaterialToZoneDao] Preparing sentence...");

		try {
			cs = con.prepareCall(INV_SP_DESASSIGN_MATERIAL_TO_ZONE);

			if (materialToZoneBean.getZoneId() != null) {
				cs.setString(1, materialToZoneBean.getZoneId());
			} else {
				cs.setNull(1, Types.INTEGER);
			}
			if (materialToZoneBean.getPosition() != null) {
				cs.setString(2, materialToZoneBean.getPosition());
			} else {
				cs.setNull(2, Types.INTEGER);
			}
			if (materialToZoneBean.getMatnr() != null) {
				cs.setString(3, materialToZoneBean.getMatnr());
			} else {
				cs.setNull(3, Types.INTEGER);
			}

			log.info("[unassignMaterialToZoneDao] Executing query...");

			ResultSet rs = cs.executeQuery();

			while (rs.next()) {

				resultSP = rs.getString(1);

			}

			if (resultSP != null) {
				try {

					Integer.parseInt(resultSP);

				} catch (NumberFormatException e) {

					abstractResult.setResultId(ReturnValues.IEXCEPTION);
					abstractResult.setResultMsgAbs(resultSP);

					res.setAbstractResult(abstractResult);
					return res;
				}
			}
			// Retrive the warnings if there're
			SQLWarning warning = cs.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING, "[unassignMaterialToZoneDao] " + warning.getMessage());
				warning = warning.getNextWarning();
			}

			// Free resources
			rs.close();
			cs.close();

			log.info("[unassignMaterialToZoneDao] Sentence successfully executed.");

		} catch (SQLException e) {
			log.log(Level.SEVERE,
					"[unassignMaterialToZoneDao] Some error occurred while was trying to execute the S.P.: "
							+ INV_SP_DESASSIGN_MATERIAL_TO_ZONE,
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
						"[unassignMaterialToZoneDao] Some error occurred while was trying to close the connection.", e);
			}
		}
		abstractResult.setResultMsgAbs(resultSP);
		res.setAbstractResult(abstractResult);
		return res;
	}

	private static final String GET_ZONE_BY_ID = "SELECT ZON_DESC, ZON_BUKRS, ZON_WERKS, ZON_LGORT, ZON_CREATED_BY,"
			+ " ZON_CREATED_DATE, ZON_MODIFIED_BY, ZON_MODIFIED_DATE, ZON_STATUS FROM INV_ZONE WITH(NOLOCK)  WHERE ZONE_ID = ?";
	private static final String GET_LGNUM_FROM_LGORT = "SELECT LGNUM FROM  INV_VW_CONCILIATION_LGNUM_FROM_LGORT_AND_WERKS WHERE WERKS = ? AND LGORT = ?";
	private static final String GEZ_ZONE_POSITION_BY_ID_LGPLA = "SELECT ZPO_LGTYP FROM  "
			+ "INV_ZONE_POSITION WITH(NOLOCK) WHERE ZPO_ZONE_ID = ? AND ZPO_LGPLA = ? ";

	@SuppressWarnings("resource")
	public DocInvPositionBean getDataForConciliation(DocInvBean headerBean, DocInvPositionBean singlePosition,
			Connection con) throws SQLException {
		log.info("[getDataForConciliation] Preparing ." + GET_ZONE_BY_ID);
		PreparedStatement stm = con.prepareStatement(GET_ZONE_BY_ID);
		stm.setInt(1, singlePosition.getZoneId());
		ResultSet rs = stm.executeQuery();
		log.info("[getDataForConciliation] Preparing ." + GET_ZONE_BY_ID);
		String lgnum = "";
		if (rs.next()) {
			singlePosition.setLgort(rs.getString("ZON_LGORT"));
			log.info("[getDataForConciliation] Preparing ." + GET_LGNUM_FROM_LGORT);
			stm = con.prepareStatement(GET_LGNUM_FROM_LGORT);
			stm.setString(1, headerBean.getWerks());
			stm.setString(2, singlePosition.getLgort());
			rs = stm.executeQuery();
			log.info("[getDataForConciliation] Preparing ." + GET_LGNUM_FROM_LGORT);
			if (rs.next()) {
				lgnum = rs.getString("LGNUM");
			}
			log.info("[getDataForConciliation] is Lgnum: ." + lgnum);
			if (!lgnum.isEmpty()) {
				log.info("[getDataForConciliation] is preparing: ." + GEZ_ZONE_POSITION_BY_ID_LGPLA
						+ " AND ZPO_LGNUM = ?");
				stm = con.prepareStatement(GEZ_ZONE_POSITION_BY_ID_LGPLA + " AND ZPO_LGNUM = ?");
				stm.setInt(1, singlePosition.getZoneId());
				stm.setString(2, singlePosition.getLgpla());
				stm.setString(3, lgnum);
			} else {
				log.info("[getDataForConciliation] is preparing: ." + GEZ_ZONE_POSITION_BY_ID_LGPLA);
				stm = con.prepareStatement(GEZ_ZONE_POSITION_BY_ID_LGPLA);
				stm.setInt(1, singlePosition.getZoneId());
				stm.setString(2, singlePosition.getLgpla());
			}
			rs = stm.executeQuery();
			log.info("[getDataForConciliation] is Executed: ." + GEZ_ZONE_POSITION_BY_ID_LGPLA);
			if (rs.next()) {
				singlePosition.setLgtyp(rs.getString("ZPO_LGTYP"));
			}
		}
		return singlePosition;
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public Response<List<ZoneBean>> getZoneByLgortAndWerk(ZoneBean zoneBean) {
		Response<List<ZoneBean>> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		List<ZoneBean> listZone = new ArrayList<ZoneBean>(); 
		String INV_VW_ZONE_BY_LGORT = "SELECT [ZONE_ID], [ZON_DESC] FROM [INV_CIC_DB].[dbo].[INV_VW_ZONE_BY_LGORT] ";
		if (zoneBean.getWerks() == null && zoneBean.getLgort() == null) {
			log.info("[getZoneByLgortAndWerkDao] null Params...");
			INV_VW_ZONE_BY_LGORT += "";
		} else if (zoneBean.getWerks() != null && zoneBean.getLgort() != null) {
			log.info("getZoneByLgortAndWerkDao] not Null zoneID Object...");
			INV_VW_ZONE_BY_LGORT += " WHERE [LGORT] = '" + zoneBean.getLgort() +"' AND [WERKS] = '"+ zoneBean.getWerks()+"'";
		} 
		INV_VW_ZONE_BY_LGORT += " GROUP BY [ZONE_ID], [ZON_DESC] ";
		INV_VW_ZONE_BY_LGORT += " ORDER BY [ZONE_ID]";
		log.info(INV_VW_ZONE_BY_LGORT);
		log.info("[getZoneByLgortAndWerkDao] Preparing sentence...");
		try {
			stm = con.prepareCall(INV_VW_ZONE_BY_LGORT);
			log.info("[getZoneByLgortAndWerkDao] Executing query...");
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				zoneBean = new ZoneBean();
				zoneBean.setZoneId(rs.getString("ZONE_ID"));
				zoneBean.setZdesc(rs.getString("ZON_DESC"));
				listZone.add(zoneBean);
			}
			log.info("[getZoneByLgortAndWerkDao] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE, "[getZoneByLgortAndWerkDao] Some error occurred while was trying to execute the query: "
					+ INV_VW_ZONE_BY_LGORT, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,
						"[getZoneByLgortAndWerkDao] Some error occurred while was trying to close the connection.", e);
			}
		}
		res.setAbstractResult(abstractResult);
		res.setLsObject(listZone);
		return res;
	}

}
