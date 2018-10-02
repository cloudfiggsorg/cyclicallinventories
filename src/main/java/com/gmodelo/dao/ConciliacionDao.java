package com.gmodelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.beans.AbstractResultsBean;
import com.gmodelo.beans.ConciliacionBean;
import com.gmodelo.beans.ConciliationPositionBean;
import com.gmodelo.beans.ConciliationsIDsBean;
import com.gmodelo.beans.GroupBean;
import com.gmodelo.beans.Response;
import com.gmodelo.beans.TaskBean;
import com.gmodelo.beans.ZonePositionsBean;
import com.gmodelo.utils.ConnectionManager;
import com.gmodelo.utils.ReturnValues;

public class ConciliacionDao {

	private Logger log = Logger.getLogger(ConciliacionDao.class.getName());

	public Response<List<ConciliationsIDsBean>> getConciliationIDs() {
		Response<List<ConciliationsIDsBean>> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		List<ConciliationsIDsBean> listConIds = new ArrayList<ConciliationsIDsBean>();
		ConciliationsIDsBean conciliationIDsBean;
		final String GENERATE_IDDESC_CONCILIATION = "select DOC_INV_ID as DOC_INV, (CONVERT(VARCHAR, DOC_INV_ID) + ' - ' + CONVERT(VARCHAR,inr.ROU_DESC)) as DESCRIPCION from INV_DOC_INVENTORY_HEADER idih WITH(NOLOCK)  Inner join INV_ROUTE inr WITH(NOLOCK) on idih.DIH_ROUTE_ID = inr.routE_ID WHERE idih.DIH_STATUS = '1'";

		try {
			stm = con.prepareCall(GENERATE_IDDESC_CONCILIATION);
			log.info("[getConciliationIDsDao] Executing query...");
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

			log.info("[getConciliationIDsDao] Sentence successfully executed.");

		} catch (SQLException e) {
			log.log(Level.SEVERE, "[getConciliationIDsDao] Some error occurred while was trying to execute the query: "
					+ GENERATE_IDDESC_CONCILIATION, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,
						"[getConciliationIDsDao] Some error occurred while was trying to close the connection.", e);
			}
		}
		res.setAbstractResult(abstractResult);
		res.setLsObject(listConIds);
		return res;
	}

	public static final String INV_VW_DOC_INV = "SELECT DOC_INV_ID, ROUTE_ID, BUKRS, BDESC, WERKS, WERKSD, TYPE,JUSTIFICATION FROM INV_VW_DOC_INV WITH(NOLOCK)"
			+ " WHERE DOC_INV_ID = ? "
			+ " GROUP BY DOC_INV_ID, ROUTE_ID, BUKRS, BDESC, WERKS, WERKSD, TYPE,JUSTIFICATION";

	public Response<List<ConciliacionBean>> getConciliacion(ConciliacionBean docInvBean) {
		Response<List<ConciliacionBean>> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		List<ConciliacionBean> listDocInv = new ArrayList<ConciliacionBean>();
		log.info(INV_VW_DOC_INV);
		log.info("[ConciliacionDao getConciliacion] Preparing sentence...");
		try {
			stm = con.prepareStatement(INV_VW_DOC_INV);
			stm.setInt(1, docInvBean.getDocInvId());
			log.info("[ConciliacionDao getConciliacion] Executing query...");
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				docInvBean = new ConciliacionBean();
				docInvBean.setRoute(rs.getString("ROUTE_ID"));
				docInvBean.setType(rs.getString("TYPE"));
				docInvBean.setDocInvId(rs.getInt("DOC_INV_ID"));
				docInvBean.setBukrs(rs.getString("BUKRS"));
				docInvBean.setBukrsD(rs.getString("BDESC"));
				docInvBean.setWerks(rs.getString("WERKS"));
				docInvBean.setWerksD(rs.getString("WERKSD"));
				docInvBean.setJustification(rs.getString("JUSTIFICATION"));
				docInvBean.setPositions(getPositions(rs.getInt("DOC_INV_ID")));
				listDocInv.add(docInvBean);
			}

			SQLWarning warning = stm.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING, warning.getMessage());
				warning = warning.getNextWarning();
			}

			// Free resources
			rs.close();
			stm.close();
			log.info("[getDocInvDao] Sentence successfully executed.");

		} catch (SQLException e) {
			log.log(Level.SEVERE,
					"[getDocInvDao] Some error occurred while was trying to execute the query: " + INV_VW_DOC_INV, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE, "[getDocInvDao] Some error occurred while was trying to close the connection.",
						e);
			}
		}
		res.setAbstractResult(abstractResult);
		res.setLsObject(listDocInv);
		return res;
	}

	private static final String TASK_DOC_INV = "SELECT TASK_ID, TAS_DOC_INV_ID,  ZONE_ID, ZON_DESC, ZPO_PK_ASG_ID ,LGPLA  FROM INV_VW_TASK_DOCINV WHERE TAS_DOC_INV_ID= ? "
			+ "ORDER BY TASK_ID ASC";

	private static final String INV_COUNT = "SELECT M.MAKTX MATDESC, MA.MEINS, C.COU_TASK_ID, C.COU_MATNR, C.COU_TOTAL FROM INV_COUNT C "
			+ "INNER JOIN MAKT M ON (M.MATNR = C.COU_MATNR) " + "INNER JOIN MARA MA ON (MA.MATNR = C.COU_MATNR) "
			+ "WHERE C.COU_POSITION_ID_ZONE = ? AND C.COU_TASK_ID = ?";

	private static final String INV_COUNT_TOT = "SELECT COUNT(M.MAKTX) MATDESC FROM INV_COUNT C "
			+ "INNER JOIN MAKT M ON (M.MATNR = C.COU_MATNR) " + "INNER JOIN MARA MA ON (MA.MATNR = C.COU_MATNR) "
			+ "WHERE C.COU_POSITION_ID_ZONE = ? AND C.COU_TASK_ID = ?";

	private List<ConciliationPositionBean> getPositions(int docInv) {
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		ResultSet rs = null;
		List<ConciliationPositionBean> listPositions = new ArrayList<ConciliationPositionBean>();
		try {
			stm = con.prepareCall(TASK_DOC_INV);
			stm.setInt(1, docInv);
			log.info("[getPositionsConciliationDao] TABLE_CONCILIATION_POSITIONS Temp, Executing query...");
			rs = stm.executeQuery();

			HashMap<String, List<ZonePositionsBean>> map = new HashMap<>();

			while (rs.next()) {
				ZonePositionsBean position = new ZonePositionsBean();

				position.setPkAsgId(rs.getInt("ZPO_PK_ASG_ID"));
				position.setZoneId(rs.getString("ZONE_ID"));
				position.setZoneD(rs.getString("ZON_DESC"));
				position.setLgpla(rs.getString("LGPLA"));

				if (map.containsKey(rs.getString("TASK_ID"))) {
					List<ZonePositionsBean> list = new ArrayList<>();
					list = map.get(rs.getString("TASK_ID"));
					list.add(position);
					map.put(rs.getString("TASK_ID"), list);

				} else {
					List<ZonePositionsBean> list = new ArrayList<>();
					list.add(position);
					map.put(rs.getString("TASK_ID"), list);
				}
			}

			String key = "";
			Iterator ite = map.entrySet().iterator();
			List<ZonePositionsBean> listPos = new ArrayList<>();
			String task = "";

			HashMap<String, ConciliationPositionBean> mapAux = new HashMap<>();
			ConciliationPositionBean conci;

			int count = 0;
			while (ite.hasNext()) {// while para tareas

				Map.Entry pair = (Map.Entry) ite.next();
				task = pair.getKey().toString();
				listPos = (List<ZonePositionsBean>) pair.getValue();

				for (ZonePositionsBean pos : listPos) { // For para posiciones
														// de zona
					int resultCount = 0;
					stm = con.prepareStatement(INV_COUNT_TOT);
					stm.setInt(1, pos.getPkAsgId());
					stm.setString(2, task);
					rs = stm.executeQuery();
					if (rs.next()) {
						resultCount = rs.getInt("MATDESC");
					}

					stm = con.prepareStatement(INV_COUNT);
					stm.setInt(1, pos.getPkAsgId());
					stm.setString(2, task);
					log.info("[getPositionsConciliationDao] TABLE_CONCILIATION_POSITIONS Temp, Executing query...");
					rs = stm.executeQuery();
					if (resultCount == 0) {
						conci = new ConciliationPositionBean();
						conci.setZoneId(pos.getZoneId());
						conci.setZoneD(pos.getZoneD());
						conci.setLgpla(pos.getLgpla());
						conci.setCount1A("0");
						conci.setCount1B("0");
						conci.setCount2("0");
						conci.setCount3("0");
						conci.setMeasureUnit("-");
						conci.setMatnrD("N/A");
						conci.setMatnr("N/A");
						mapAux.put(pos.getZoneId() + pos.getZoneD() + pos.getLgpla(), conci);
					}

					while (rs.next()) { // While para sumar conteos en un map

						key = rs.getString("COU_MATNR") + rs.getString("COU_POSITION_ID_ZONE");

						if (mapAux.containsKey(key)) {

							conci = (ConciliationPositionBean) mapAux.get(rs.getString("COU_MATNR"));

							int total = rs.getInt("COU_TOTAL");
							if (count == 0) {
								total += Integer.parseInt(conci.getCount1A());
								conci.setCount1A("" + total);
							} else if (count == 1) {
								total += Integer.parseInt(conci.getCount1B());
								conci.setCount1B("" + total);
							} else if (count == 2) {
								total += Integer.parseInt(conci.getCount2());
								conci.setCount2("" + total);
							} else if (count == 3) {
								total += Integer.parseInt(conci.getCount3());
								conci.setCount3("" + total);
							}

						} else {

							conci = new ConciliationPositionBean();
							conci.setZoneId(pos.getZoneId());
							conci.setZoneD(pos.getZoneD());
							conci.setLgpla(pos.getLgpla());
							conci.setCount1A("0");
							conci.setCount1B("0");
							conci.setCount2("0");
							conci.setCount3("0");
							conci.setMeasureUnit(rs.getString("MEINS"));
							conci.setMatnrD(rs.getString("MATDESC"));
							conci.setMatnr(String.valueOf(rs.getInt("COU_MATNR")));

							String total = rs.getString("COU_TOTAL");
							if (count == 0) {
								conci.setCount1A(total);
							} else if (count == 1) {
								conci.setCount1B(total);
							} else if (count == 2) {
								conci.setCount2(total);
							} else if (count == 3) {
								conci.setCount3(total);
							}
						}

						mapAux.put(key, conci);
					}
				}
				count++;
			}

			Iterator iteAux = mapAux.entrySet().iterator();

			while (iteAux.hasNext()) {
				Map.Entry pair = (Map.Entry) iteAux.next();
				task = pair.getKey().toString();
				listPositions.add((ConciliationPositionBean) pair.getValue());
			}

			// Free resources
			if (rs != null) {
				rs.close();
			}
			if (stm != null) {
				stm.close();
			}

			log.info("[getPositionsConciliationDao] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE,
					"[getPositionsConciliationDao] Some error occurred while was trying to execute the query: "
							+ TASK_DOC_INV,
					e);
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,
						"[getPositionsConciliationDao] Some error occurred while was trying to close the connection.",
						e);
			}
		}
		return listPositions;
	}

	public String getRowCount(int docInvId, String conteo, String idZone, String lgpla, String matnr, String meins,
			Connection con, PreparedStatement stm) {
		String result = "";
		stm = null;
		String INV_VW_COUNT_POSITIONS = "SELECT SUM(TOTAL) TOTAL FROM INV_VW_COUNT_POSITIONS_BASE WITH(NOLOCK) WHERE TAS_DOC_INV_ID= ? AND ZONE_ID=? AND LGPLA = ? "
				+ " AND MATNR=? AND COUNT_NUM=?";
		try {
			stm = con.prepareStatement(INV_VW_COUNT_POSITIONS);
			stm.setInt(1, docInvId);
			stm.setString(2, idZone);
			stm.setString(3, lgpla);
			stm.setString(4, matnr);
			stm.setString(5, conteo);
			ResultSet res = stm.executeQuery();
			while (res.next()) {
				result = res.getString("TOTAL");
			}
		} catch (SQLException e1) {
			log.log(Level.SEVERE, "[getRowCountConciliationDao] Some error occurred while was execute: "
					+ INV_VW_COUNT_POSITIONS + "Exception: " + e1.getMessage());
		}
		return result;
	}

	private String buildCondition(ConciliacionBean docInvB) {
		String condition = "";
		String DOC_INV_ID = "";
		String ROUTE_ID = "";
		String bukrs = "";
		String werks = "";
		String JUSTIFICATION = "";

		DOC_INV_ID = (docInvB.getDocInvId() != 0
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + "DOC_INV_ID = '" + docInvB.getDocInvId() + "' "
				: "");
		condition += DOC_INV_ID;
		ROUTE_ID = (docInvB.getRoute() != null
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + "ROUTE_ID = '" + docInvB.getRoute() + "' "
				: "");
		condition += ROUTE_ID;
		bukrs = (docInvB.getBukrs() != null
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + "BUKRS = '" + docInvB.getBukrs() + "' " : "");
		condition += bukrs;
		werks = (docInvB.getWerks() != null
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + "WERKS = '" + docInvB.getWerks() + "' " : "");
		condition += werks;
		JUSTIFICATION = (docInvB.getJustification() != null ? (condition.contains("WHERE") ? " AND " : " WHERE ")
				+ "JUSTIFICATION = '=" + docInvB.getJustification() + "' " : "");
		condition += JUSTIFICATION;

		condition = condition.isEmpty() ? null : condition;
		return condition;
	}

	public Response<List<GroupBean>> getAvailableGroups(int docInvId) {
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		ResultSet rs = null;
		List<GroupBean> listGroups = new ArrayList<GroupBean>();
		Response<List<GroupBean>> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		GroupBean gb = new GroupBean();
		GroupDao gpDao = new GroupDao();

		String INV_VW_AVAILABLE_GROUPS = "SELECT GRPS.GROUP_ID, GRPS.GRP_DESC " + "FROM INV_ROUTE_GROUPS AS IRG "
				+ "INNER JOIN INV_DOC_INVENTORY_HEADER AS IDIH ON (IRG.RGR_ROUTE_ID = IDIH.DIH_ROUTE_ID) "
				+ "INNER JOIN INV_GROUPS AS GRPS ON(GRPS.GROUP_ID = IRG.RGR_GROUP_ID) "
				+ "AND RGR_GROUP_ID NOT IN(SELECT TAS_GROUP_ID " + "FROM INV_TASK " + "WHERE TAS_DOC_INV_ID = ?) "
				+ "WHERE IDIH.DOC_INV_ID = ? " + "ORDER BY IRG.RGR_COUNT_NUM ASC";

		try {
			stm = con.prepareCall(INV_VW_AVAILABLE_GROUPS);
			stm.setInt(1, docInvId);
			stm.setInt(2, docInvId);
			log.info(INV_VW_AVAILABLE_GROUPS);
			rs = stm.executeQuery();

			if (rs.next()) {

				gb = new GroupBean();
				gb.setGroupId(rs.getString(1));
				gb.setGdesc(rs.getString(2));
				gb.setUsers(gpDao.groupUsers(gb.getGroupId(), null));
				listGroups.add(gb);
			}

			// Free resources
			rs.close();
			stm.close();

			log.info("[getAvailableGroups] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE, "[getAvailableGroups] Some error occurred while was trying to execute the query: "
					+ INV_VW_AVAILABLE_GROUPS, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,
						"[getAvailableGroups] Some error occurred while was trying to close the connection.", e);
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs(e.getMessage());
				res.setAbstractResult(abstractResult);
				return res;

			}
		}

		res.setAbstractResult(abstractResult);
		res.setLsObject(listGroups);
		return res;
	}

	public Response<TaskBean> getFatherTaskByDocId(int docInvId) {
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		ResultSet rs = null;
		TaskBean tb = null;
		Response<TaskBean> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();

		String SQL_GET_PARENT_TASK = "SELECT TASK_ID, TAS_GROUP_ID, TAS_CREATED_DATE, "
				+ "TAS_DOWLOAD_DATE, TAS_UPLOAD_DATE, TASK_ID_PARENT " + "FROM INV_TASK "
				+ "INNER JOIN INV_DOC_INVENTORY_HEADER ON INV_TASK.TAS_DOC_INV_ID = INV_DOC_INVENTORY_HEADER.DOC_INV_ID "
				+ "WHERE TAS_DOC_INV_ID = ? AND TAS_STATUS = '1' " + "AND INV_DOC_INVENTORY_HEADER.DIH_STATUS = '1' "
				+ "AND TASK_ID_PARENT IS NULL";

		try {
			stm = con.prepareCall(SQL_GET_PARENT_TASK);
			stm.setInt(1, docInvId);
			log.info(SQL_GET_PARENT_TASK);
			rs = stm.executeQuery();

			if (rs.next()) {

				tb = new TaskBean();
				tb.setTaskId(rs.getString(1));
				tb.setGroupId(rs.getString(2));

				try {
					tb.setdCreated(rs.getDate(3).getTime());
				} catch (NullPointerException e) {
					tb.setdCreated(0);
				}

				try {
					tb.setdDownlad(rs.getDate(4).getTime());
				} catch (NullPointerException e) {
					tb.setdDownlad(0);
				}

				try {
					tb.setdUpload(rs.getDate(5).getTime());
				} catch (Exception e) {
					tb.setdUpload(0);
				}

				try {
					tb.setTaskIdFather(rs.getString(6));
				} catch (Exception e) {
					tb.setTaskIdFather(null);
				}
			}

			// Free resources
			rs.close();
			stm.close();

			log.info("[getFatherTaskByDocId] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE, "[getFatherTaskByDocId] Some error occurred while was trying to execute the query: "
					+ SQL_GET_PARENT_TASK, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,
						"[getFatherTaskByDocId] Some error occurred while was trying to close the connection.", e);
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs(e.getMessage());
				res.setAbstractResult(abstractResult);
				return res;

			}
		}

		res.setAbstractResult(abstractResult);
		res.setLsObject(tb);
		return res;
	}

	public Response<String> getZonePosition(int zoneId, String lgpla) {
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		ResultSet rs = null;
		String posId = null;
		Response<String> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();

		String SQL_GET_ZONE_POSITION = "SELECT ZPO_PK_ASG_ID " + "FROM INV_ZONE_POSITION "
				+ "WHERE ZPO_ZONE_ID = ? AND ZPO_LGPLA = ? ";

		try {
			stm = con.prepareCall(SQL_GET_ZONE_POSITION);
			stm.setInt(1, zoneId);
			stm.setString(2, lgpla);
			log.info(SQL_GET_ZONE_POSITION);
			rs = stm.executeQuery();

			while (rs.next()) {

				posId = rs.getString(1);
			}

			// Free resources
			rs.close();
			stm.close();

			log.info("[getZonePosition] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE, "[getZonePosition] Some error occurred while was trying to execute the query: "
					+ SQL_GET_ZONE_POSITION, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE, "[getZonePosition] Some error occurred while was trying to close the connection.",
						e);
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs(e.getMessage());
				res.setAbstractResult(abstractResult);
				return res;

			}
		}

		res.setAbstractResult(abstractResult);
		res.setLsObject(posId);
		return res;
	}

	/*
	 * public static void main(String args[]){ ConciliacionDao dao = new
	 * ConciliacionDao(); ConciliacionBean docInvBean = new ConciliacionBean();
	 * // docInvBean.setDocInvId(3); String searchFilter = "";
	 * Response<List<ConciliacionBean>> x = dao.getConciliacion(docInvBean,
	 * searchFilter); for(int i=0; i < x.getLsObject().size(); i++){
	 * System.out.println(x.getLsObject().get(i).toString()); } }
	 */
}
