package com.gmodelo.cyclicinventories.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.cyclicinventories.beans.AbstractResultsBean;
import com.gmodelo.cyclicinventories.beans.ConciliacionBean;
import com.gmodelo.cyclicinventories.beans.ConciliationPositionBean;
import com.gmodelo.cyclicinventories.beans.ConciliationsIDsBean;
import com.gmodelo.cyclicinventories.beans.DocInvBean;
import com.gmodelo.cyclicinventories.beans.GroupBean;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.beans.TaskBean;
import com.gmodelo.cyclicinventories.utils.ConnectionManager;
import com.gmodelo.cyclicinventories.utils.ReturnValues;

public class ConciliacionDao {

	private Logger log = Logger.getLogger(ConciliacionDao.class.getName());

	private static final String GENERATE_IDDESC_CONCILIATION = "SELECT DOC_INV_ID as DOC_INV, (CONVERT(VARCHAR, DOC_INV_ID) + ' - ' + CONVERT(VARCHAR,inr.ROU_DESC)) as DESCRIPCION "
			+ " FROM INV_DOC_INVENTORY_HEADER idih WITH(NOLOCK) "
			+ " INNER JOIN INV_ROUTE inr WITH(NOLOCK) ON idih.DIH_ROUTE_ID = inr.ROUTE_ID WHERE idih.DIH_STATUS = '1'"
			+ " AND idih.DOC_FATHER_INV_ID IS NULL AND idih.DIH_BUKRS LIKE  ? AND idih.DIH_WERKS LIKE ?";

	private static final String GET_DOC_INV_VALUE = "SELECT DOC_INV_ID, DIH_ROUTE_ID, DIH_BUKRS, DIH_TYPE, DIH_CREATED_BY, DIH_CREATED_DATE, DIH_MODIFIED_BY, DIH_MODIFIED_DATE, "
			+ " DIH_JUSTIFICATION, DIH_STATUS, DIH_WERKS, DOC_FATHER_INV_ID FROM INV_DOC_INVENTORY_HEADER WITH(NOLOCK) WHERE DOC_INV_ID = ?";

	public void fillCurrectDocInv(DocInvBean docInv) throws SQLException, Exception {
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		try {
			PreparedStatement stm = con.prepareStatement(GET_DOC_INV_VALUE);
			stm.setInt(1, docInv.getDocInvId());
			ResultSet rs = stm.executeQuery();
			if (rs.next()) {
				docInv.setBukrs(rs.getString("DIH_BUKRS"));
				docInv.setWerks(rs.getString("DIH_WERKS"));
			}
		} catch (SQLException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				con.close();
			} catch (Exception e) {
				log.log(Level.SEVERE, "Ocurrio un problema al cerrar la conexión", e);
			}
		}
	}

	public Response<List<ConciliationsIDsBean>> getConciliationIDs(String bukrs, String werks) {
		Response<List<ConciliationsIDsBean>> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		List<ConciliationsIDsBean> listConIds = new ArrayList<>();
		ConciliationsIDsBean conciliationIDsBean;

		try {
			stm = con.prepareStatement(GENERATE_IDDESC_CONCILIATION);

			if (bukrs != null && werks != null) {
				stm.setString(1, bukrs);
				stm.setString(2, werks);

			} else {
				stm.setString(1, "%");
				stm.setString(2, "%");
			}

			log.info(GENERATE_IDDESC_CONCILIATION);

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

	private static final String CLOSED_DOC_INV = "SELECT DOC_INV_ID as DOC_INV, (CONVERT(VARCHAR, DOC_INV_ID) + ' - ' "
			+ " + CONVERT(VARCHAR,inr.ROU_DESC)) AS DESCRIPCION, 0 AS STATUS, FNSAP_SNAPSHOT "
			+ " FROM INV_DOC_INVENTORY_HEADER IDIH WITH(NOLOCK) "
			+ " INNER JOIN INV_ROUTE INR WITH(NOLOCK) ON (IDIH.DIH_ROUTE_ID = INR.ROUTE_ID) "
			+ "	WHERE IDIH.DIH_STATUS = '0' " + " AND IDIH.DOC_FATHER_INV_ID IS NULL " + " AND IDIH.DIH_BUKRS LIKE ? "
			+ " AND IDIH.DIH_WERKS LIKE ? " + " AND DOC_INV_ID LIKE ? "
			+ " AND DOC_INV_ID NOT IN (SELECT CS_DOC_INV_ID FROM INV_CONS_SAP GROUP BY CS_DOC_INV_ID) " + " UNION "
			+ " SELECT ICS.CS_DOC_INV_ID, IR.ROU_DESC AS DESCRIPCION, 1 AS STS " + " FROM INV_CONS_SAP AS ICS "
			+ " INNER JOIN INV_DOC_INVENTORY_HEADER AS IDIH ON (ICS.CS_DOC_INV_ID = IDIH.DOC_INV_ID) "
			+ " INNER JOIN INV_ROUTE AS IR ON (IR.ROUTE_ID = IDIH.DIH_ROUTE_ID) " + " WHERE IDIH.DIH_BUKRS LIKE ? "
			+ " AND IDIH.DIH_WERKS LIKE ? " + " AND DOC_INV_ID LIKE ? " + " GROUP BY ICS.CS_DOC_INV_ID, ROU_DESC, FNSAP_SNAPSHOT";

	public Response<List<ConciliationsIDsBean>> getClosedConciliationIDs(DocInvBean dib) {
		Response<List<ConciliationsIDsBean>> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		List<ConciliationsIDsBean> listConIds = new ArrayList<>();
		ConciliationsIDsBean conciliationIDsBean;

		try {

			log.log(Level.INFO, dib.toString());
			stm = con.prepareStatement(CLOSED_DOC_INV);
			stm.setString(1, (dib.getBukrs() == null ? "%" : dib.getBukrs()));
			stm.setString(2, (dib.getWerks() == null ? "%" : dib.getWerks()));
			stm.setString(3, (dib.getDocInvId() == null ? "%" : dib.getDocInvId().toString()));
			stm.setString(4, (dib.getBukrs() == null ? "%" : dib.getBukrs()));
			stm.setString(5, (dib.getWerks() == null ? "%" : dib.getWerks()));
			stm.setString(6, (dib.getDocInvId() == null ? "%" : dib.getDocInvId().toString()));

			log.info(CLOSED_DOC_INV);
			log.info("[getClosedConciliationIDsDao] Executing query...");
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {

				conciliationIDsBean = new ConciliationsIDsBean();
				conciliationIDsBean.setId(rs.getString("DOC_INV"));
				conciliationIDsBean.setDesc(rs.getString("DESCRIPCION"));
				conciliationIDsBean.setStatus(rs.getBoolean("STATUS"));
				conciliationIDsBean.setAvailable(rs.getBoolean("FNSAP_SNAPSHOT"));

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

			log.info("[getClosedConciliationIDsDao] Sentence successfully executed.");

		} catch (SQLException e) {
			log.log(Level.SEVERE,
					"[getClosedConciliationIDsDao] Some error occurred while was trying to execute the query: "
							+ GENERATE_IDDESC_CONCILIATION,
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
						"[getClosedConciliationIDsDao] Some error occurred while was trying to close the connection.",
						e);
			}
		}
		res.setAbstractResult(abstractResult);
		res.setLsObject(listConIds);
		return res;
	}

	private static final String INV_VW_DOC_INV = "SELECT DOC_INV_ID, ROUTE_ID, BUKRS, BDESC, WERKS, WERKSD, TYPE,JUSTIFICATION "
			+ "FROM INV_VW_DOC_INV WITH(NOLOCK) " + "WHERE DOC_INV_ID = ? "
			+ "GROUP BY DOC_INV_ID, ROUTE_ID, BUKRS, BDESC, WERKS, WERKSD, TYPE, JUSTIFICATION";

	public Response<ConciliacionBean> getConciliacion(ConciliacionBean docInvBean) {
		Response<ConciliacionBean> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
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
				docInvBean.setPositions(getConciliationPositions(con, docInvBean));
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
		res.setLsObject(docInvBean);
		return res;
	}

	private static final String INV_FULL_COUNT = "SELECT TASK_ID, TAS_DOC_INV_ID, ZONE_ID, ZON_DESC, ZON_LGORT, LGOBE, ZPO_PK_ASG_ID, "
			+ "LGPLA, COU_TOTAL, COU_MATNR, MAKTX, MEINS FROM INV_VW_TASK_DOCINV_FULL WHERE TAS_DOC_INV_ID = ? ORDER BY TASK_ID, ZPO_PK_ASG_ID ASC";

	private static final String INV_DOC_CHILDREN = "SELECT DOC_INV_ID FROM INV_DOC_INVENTORY_HEADER WITH(NOLOCK) WHERE DOC_FATHER_INV_ID = ? ";

	private static final String TASK_ASSIGNED_FOR_DOC_INV = "SELECT COUNT(*) AS COUNTED FROM INV_TASK WITH(NOLOCK) WHERE TAS_DOC_INV_ID = ?";

	private static final String GET_NOTE_AND_PROD = "SELECT COU_POSITION_ID_ZONE, COU_MATNR, COU_NOTE, COU_PROD_DATE FROM INV_COUNT WITH(NOLOCK) "
			+ " WHERE COU_TASK_ID IN (SELECT TASK_ID FROM INV_TASK WITH(NOLOCK) WHERE TAS_DOC_INV_ID = ?) "
			+ " AND(COU_NOTE IS NOT NULL  AND COU_NOTE != '') OR (COU_PROD_DATE IS NOT NULL AND COU_PROD_DATE != '')";

	private static final String GET_MIN_MAX_LGPLA_DATE = "SELECT COU_POSITION_ID_ZONE, MIN(COU_START_DATE)  MIN_DATE, MAX(COU_END_DATE) MAX_DATE FROM INV_COUNT WITH(NOLOCK) "
			+ " WHERE COU_TASK_ID IN (SELECT TASK_ID FROM INV_TASK WITH(NOLOCK) WHERE TAS_DOC_INV_ID = ?) "
			+ " AND (COU_START_DATE IS NOT NULL) OR (COU_END_DATE IS NOT NULL)GROUP BY COU_POSITION_ID_ZONE";

	private static final String GET_MATERIAL_TO_COLOUR = "SELECT ZOPM.PK_ZONPOS_MAT, ZOPM.ZPM_MATNR FROM INV_DOC_INVENTORY_HEADER DOC WITH(NOLOCK) "
			+ " INNER JOIN INV_ROUTE_POSITION ROP WITH(NOLOCK) ON DOC.DOC_INV_ID = ROP.RPO_POSITION_ROUTE_ID "
			+ " INNER JOIN INV_ZONE_POSITION ZOP WITH(NOLOCK) ON ROP.RPO_ZONE_ID = ZOP.ZPO_ZONE_ID "
			+ " INNER JOIN INV_ZONE_POSITION_MATERIALS ZOPM WITH(NOLOCK) ON ZOP.ZPO_PK_ASG_ID = ZOPM.PK_ZONPOS_MAT "
			+ " WHERE DOC.DOC_INV_ID = ? AND ZOPM.ZPM_MATNR IS NOT NULL AND ZOPM.ZPM_MATNR != '' "
			+ " GROUP BY ZOPM.PK_ZONPOS_MAT, ZOPM.ZPM_MATNR";

	@SuppressWarnings("rawtypes")
	public List<ConciliationPositionBean> getConciliationPositions(Connection con, ConciliacionBean docInvBean) {
		PreparedStatement stm = null;
		ResultSet rs = null;
		String errorQuery = "";
		List<ConciliationPositionBean> listPositions = new ArrayList<>();
		try {
			HashMap<String, ConciliationPositionBean> notesPositions = new HashMap<>();
			HashMap<String, ConciliationPositionBean> timesMapPositisoin = new HashMap<>();

			stm = con.prepareStatement(GET_NOTE_AND_PROD);

			errorQuery = GET_NOTE_AND_PROD;
			stm.setInt(1, docInvBean.getDocInvId());
			log.info("[getPositionsConciliationDao - getConciliationPositions] GET_NOTE_AND_PROD, Executing query...");

			rs = stm.executeQuery();
			while (rs.next()) {
				String key = rs.getString("COU_POSITION_ID_ZONE") + rs.getString("COU_MATNR");
				if (notesPositions.containsKey(key)) {
					if (!notesPositions.get(key).getNote().isEmpty()) {
						notesPositions.get(key)
								.setNote(notesPositions.get(key).getNote() + rs.getString("COU_NOTE") == null ? ""
										: !rs.getString("COU_NOTE").isEmpty() ? "|" + rs.getString("COU_NOTE") : "");
					} else {
						notesPositions.get(key)
								.setNote(notesPositions.get(key).getNote() + rs.getString("COU_NOTE") == null ? ""
										: !rs.getString("COU_NOTE").isEmpty() ? rs.getString("COU_NOTE") : "");
					}
					if (!notesPositions.get(key).getProdDate().isEmpty()) {
						notesPositions.get(key).setProdDate(
								notesPositions.get(key).getProdDate() + rs.getString("COU_PROD_DATE") == null ? ""
										: !rs.getString("COU_PROD_DATE").isEmpty()
												? "|," + rs.getString("COU_PROD_DATE")
												: "");
					} else {
						notesPositions.get(key).setProdDate(
								notesPositions.get(key).getProdDate() + rs.getString("COU_PROD_DATE") == null ? ""
										: !rs.getString("COU_PROD_DATE").isEmpty() ? rs.getString("COU_PROD_DATE")
												: "");
					}
				} else {
					ConciliationPositionBean bean = new ConciliationPositionBean();
					bean.setNote(rs.getString("COU_NOTE") == null ? "" : rs.getString("COU_NOTE"));
					bean.setProdDate(rs.getString("COU_PROD_DATE") == null ? "" : rs.getString("COU_PROD_DATE"));
					notesPositions.put(rs.getString("COU_MATNR") + rs.getString("COU_POSITION_ID_ZONE"), bean);
				}
			}

			if (docInvBean.getType() != null && docInvBean.getType().equals("1")) {
				stm = con.prepareStatement(GET_MIN_MAX_LGPLA_DATE);
				errorQuery = GET_MIN_MAX_LGPLA_DATE;
				stm.setInt(1, docInvBean.getDocInvId());
				log.info(
						"[getPositionsConciliationDao - getConciliationPositions] GET_NOTE_AND_PROD, Executing query...");
				rs = stm.executeQuery();
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
				while (rs.next()) {
					ConciliationPositionBean bean = new ConciliationPositionBean();
					try {
						bean.setDateIni(
								rs.getTimestamp("MIN_DATE") == null ? "" : sdf.format(rs.getTimestamp("MIN_DATE")));
					} catch (Exception e) {
						bean.setDateIni("");
					}
					try {
						bean.setDateEnd(
								rs.getTimestamp("MAX_DATE") == null ? "" : sdf.format(rs.getTimestamp("MAX_DATE")));
					} catch (Exception e) {
						bean.setDateEnd("");
					}
					timesMapPositisoin.put(rs.getString("COU_POSITION_ID_ZONE"), bean);
				}
			}

			HashMap<String, List<String>> materialToColor = new HashMap<>();

			errorQuery = GET_MATERIAL_TO_COLOUR;
			stm = con.prepareStatement(GET_MATERIAL_TO_COLOUR);
			stm.setInt(1, docInvBean.getDocInvId());
			log.info(
					"[getPositionsConciliationDao - getConciliationPositions] GET_MATERIAL_TO_COLOUR, Executing query...");

			rs = stm.executeQuery();
			while (rs.next()) {
				if (materialToColor.containsKey(rs.getString("PK_ZONPOS_MAT"))) {
					materialToColor.get(rs.getString("PK_ZONPOS_MAT")).add(rs.getString("ZPM_MATNR"));
				} else {
					List<String> materiales = new ArrayList<>();
					materiales.add(rs.getString("ZPM_MATNR"));
					materialToColor.put(rs.getString("PK_ZONPOS_MAT"), materiales);
				}
			}
			errorQuery = INV_FULL_COUNT;
			stm = con.prepareStatement(INV_FULL_COUNT);
			stm.setInt(1, docInvBean.getDocInvId());
			log.info("[getPositionsConciliationDao - getConciliationPositions] INV_FULL_COUNT, Executing query...");
			rs = stm.executeQuery();
			String taskID = null;
			int count = 0;
			HashMap<String, ConciliationPositionBean> hashMap = new HashMap<>();
			ConciliationPositionBean bean = new ConciliationPositionBean();
			log.info(
					"[getPositionsConciliationDao - getConciliationPositions] INV_FULL_COUNT, After Excecute query...");
			while (rs.next()) {
				String total = "0";
				if (taskID == null) {
					taskID = rs.getString("TASK_ID");
				} else if (!taskID.equals(rs.getString("TASK_ID"))) {
					taskID = rs.getString("TASK_ID");
					count++;
				}
				if (hashMap.containsKey(rs.getString("COU_MATNR") + rs.getString("ZPO_PK_ASG_ID"))) {
					bean = hashMap.get(rs.getString("COU_MATNR") + rs.getString("ZPO_PK_ASG_ID"));
					if (count == 0) {
						docInvBean.setCountA(true);
						total = rs.getString("COU_TOTAL") != null ? rs.getString("COU_TOTAL") : "0";
						bean.setCount1A(total);
					} else if (count == 1) {
						docInvBean.setCountB(true);
						total = rs.getString("COU_TOTAL") != null ? rs.getString("COU_TOTAL") : "0";
						bean.setCount1B(total);
					} else if (count == 2) {
						docInvBean.setCount2(true);
						total = rs.getString("COU_TOTAL") != null ? rs.getString("COU_TOTAL") : "0";
						bean.setCount2(total);
					} else if (count == 3) {
						docInvBean.setCount3(true);
						total = rs.getString("COU_TOTAL") != null ? rs.getString("COU_TOTAL") : "0";
						bean.setCount3(total);
					}
				} else {
					bean = new ConciliationPositionBean();
					bean.setMeasureUnit(rs.getString("MEINS"));
					try {
						bean.setMatnr(String.valueOf(rs.getInt("COU_MATNR")));
					} catch (Exception e) {
						bean.setMatnr(rs.getString("COU_MATNR"));
					}
					bean.setMatnrD(rs.getString("MAKTX"));
					bean.setZoneId(rs.getString("ZONE_ID"));
					bean.setZoneD(rs.getString("ZON_DESC"));
					bean.setLgpla(rs.getString("LGPLA"));
					bean.setLgort(rs.getString("ZON_LGORT"));
					bean.setLgobe(rs.getString("LGOBE"));
					bean.setPkAsgId(rs.getString("ZPO_PK_ASG_ID"));
					bean.setFlagColor(false);
					if (materialToColor.containsKey(bean.getPkAsgId())) {
						if (materialToColor.get(bean.getPkAsgId()) != null
								&& !materialToColor.get(bean.getPkAsgId()).isEmpty()) {
							if (!materialToColor.get(bean.getPkAsgId()).contains(bean.getMatnr())) {
								bean.setFlagColor(true);
							}
						}
					}
					if (notesPositions.get(rs.getString("COU_MATNR") + rs.getString("ZPO_PK_ASG_ID")) != null) {
						bean.setNote(notesPositions.get(rs.getString("COU_MATNR") + rs.getString("ZPO_PK_ASG_ID"))
								.getNote());
						bean.setProdDate(notesPositions.get(rs.getString("COU_MATNR") + rs.getString("ZPO_PK_ASG_ID"))
								.getProdDate());
					}
					if (docInvBean.getType() != null && docInvBean.getType().equals("1")) {
						if (timesMapPositisoin.get(rs.getString("ZPO_PK_ASG_ID")) != null) {
							bean.setDateIni(timesMapPositisoin.get(rs.getString("ZPO_PK_ASG_ID")).getDateIni());
							bean.setDateEnd(timesMapPositisoin.get(rs.getString("ZPO_PK_ASG_ID")).getDateEnd());
						}
					}
					if (count == 0) {
						docInvBean.setCountA(true);
						total = rs.getString("COU_TOTAL") != null ? rs.getString("COU_TOTAL") : "0";
						bean.setCount1A(total);
					} else if (count == 1) {
						docInvBean.setCountB(true);
						total = rs.getString("COU_TOTAL") != null ? rs.getString("COU_TOTAL") : "0";
						bean.setCount1B(total);
					} else if (count == 2) {
						docInvBean.setCount2(true);
						total = rs.getString("COU_TOTAL") != null ? rs.getString("COU_TOTAL") : "0";
						bean.setCount2(total);
					} else if (count == 3) {
						docInvBean.setCount3(true);
						total = rs.getString("COU_TOTAL") != null ? rs.getString("COU_TOTAL") : "0";
						bean.setCount3(total);
					}

				}
				if (rs.getString("COU_MATNR") != null) {
					hashMap.put(rs.getString("COU_MATNR") + rs.getString("ZPO_PK_ASG_ID"), bean);
				}

			}
			errorQuery = TASK_ASSIGNED_FOR_DOC_INV;
			stm = con.prepareStatement(TASK_ASSIGNED_FOR_DOC_INV);
			stm.setInt(1, docInvBean.getDocInvId());
			rs = stm.executeQuery();
			if (rs.next()) {
				docInvBean.setCounted(rs.getInt("COUNTED"));
			}

			log.info(
					"[getPositionsConciliationDao - getConciliationPositions] INV_FULL_COUNT, WHile Rs next ENd Excecute query...");
			// Check if the inventory document has children
			log.info(
					"[getPositionsConciliationDao - getConciliationPositions] INV_DOC_CHILDREN, prev Execute Query...");

			errorQuery = INV_DOC_CHILDREN;
			stm = con.prepareStatement(INV_DOC_CHILDREN);
			stm.setInt(1, docInvBean.getDocInvId());
			ResultSet rsChl = stm.executeQuery();
			log.info("[getPositionsConciliationDao - getConciliationPositions] After, prev Execute Query...");

			while (rsChl.next()) {
				errorQuery = INV_FULL_COUNT;
				PreparedStatement stm2 = con.prepareStatement(INV_FULL_COUNT);
				stm2.setString(1, rsChl.getString("DOC_INV_ID"));
				rs = stm2.executeQuery();
				String total = "";
				while (rs.next()) {
					if (hashMap.containsKey(rs.getString("COU_MATNR") + rs.getString("ZPO_PK_ASG_ID"))) {
						bean = hashMap.get(rs.getString("COU_MATNR") + rs.getString("ZPO_PK_ASG_ID"));
						docInvBean.setCountE(true);
						total = rs.getString("COU_TOTAL") != null ? rs.getString("COU_TOTAL") : "0";
						bean.setCountX(total);
					} else {
						bean = new ConciliationPositionBean();
						bean.setMeasureUnit(rs.getString("MEINS"));
						bean.setMatnr(String.valueOf(rs.getInt("COU_MATNR")));
						bean.setMatnrD(rs.getString("MAKTX"));
						bean.setZoneId(rs.getString("ZONE_ID"));
						bean.setZoneD(rs.getString("ZON_DESC"));
						bean.setLgpla(rs.getString("LGPLA"));
						bean.setCount1A("0");
						bean.setCount1B("0");
						docInvBean.setCountE(true);
						total = rs.getString("COU_TOTAL") != null ? rs.getString("COU_TOTAL") : "0";
						bean.setCountX(total);
					}
				}
			}
			log.info(
					"[getPositionsConciliationDao - getConciliationPositions] INV_DOC_CHILDREN, WHile Rs next ENd Excecute query...");
			Iterator iteAux = hashMap.entrySet().iterator();
			while (iteAux.hasNext()) {
				Map.Entry pair = (Map.Entry) iteAux.next();
				listPositions.add((ConciliationPositionBean) pair.getValue());
			}
		} catch (Exception e) {
			log.log(Level.SEVERE,
					"[getPositionsConciliationDao - getConciliationPositions] Some error occurred while was trying to execute the query: "
							+ errorQuery,
					e);
		}
		log.info(
				"[getPositionsConciliationDao - getConciliationPositions] INV_FULL_COUNT, return positions Excecute query..."
						+ listPositions);
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

	private static final String INV_VW_AVAILABLE_GROUPS_FOR_INV_DAILY = "SELECT IG.GROUP_ID, IG.GRP_DESC "
			+ "FROM INV_GROUPS_USER AS IGU " + "INNER JOIN INV_GROUPS AS IG ON (IGU.GRU_GROUP_ID = IG.GROUP_ID) "
			+ "AND IG.GRP_BUKRS LIKE ? AND IG.GRP_WERKS LIKE ? " + "GROUP BY IG.GROUP_ID, IG.GRP_DESC";

	private static final String INV_VW_AVAILABLE_GROUPS_FOR_INV_MONTH = "SELECT IG.GROUP_ID, IG.GRP_DESC "
			+ "FROM INV_GROUPS_USER AS IGU " + "INNER JOIN INV_GROUPS AS IG ON (IGU.GRU_GROUP_ID = IG.GROUP_ID) "
			+ "WHERE GRU_USER_ID NOT IN (SELECT GRU_USER_ID FROM INV_GROUPS_USER "
			+ "WHERE GRU_GROUP_ID IN (SELECT TAS_GROUP_ID FROM INV_TASK WHERE TAS_DOC_INV_ID = ?)) "
			+ "AND GRU_GROUP_ID NOT IN (SELECT TAS_GROUP_ID FROM INV_TASK WHERE TAS_DOC_INV_ID = ?) "
			+ "AND IG.GRP_BUKRS LIKE ? AND IG.GRP_WERKS LIKE ? " + "GROUP BY IG.GROUP_ID, IG.GRP_DESC";

	public Response<List<GroupBean>> getAvailableGroups(DocInvBean docInv, String type) {
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		ResultSet rs = null;
		List<GroupBean> listGroups = new ArrayList<>();
		Response<List<GroupBean>> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		GroupBean gb = new GroupBean();
		String queryToUse = "";

		try {

			if (type.trim().equals("1")) {

				queryToUse = INV_VW_AVAILABLE_GROUPS_FOR_INV_DAILY;
				stm = con.prepareStatement(queryToUse);
				stm.setString(1, docInv.getBukrs() == null ? "%" : docInv.getBukrs());
				stm.setString(2, docInv.getWerks() == null ? "%" : docInv.getWerks());

			} else {

				queryToUse = INV_VW_AVAILABLE_GROUPS_FOR_INV_MONTH;
				stm = con.prepareStatement(queryToUse);
				stm.setInt(1, docInv.getDocInvId());
				stm.setInt(2, docInv.getDocInvId());
				stm.setString(3, docInv.getBukrs() == null ? "%" : docInv.getBukrs());
				stm.setString(4, docInv.getWerks() == null ? "%" : docInv.getWerks());
			}

			log.info(queryToUse);
			rs = stm.executeQuery();

			while (rs.next()) {

				gb = new GroupBean();
				gb.setGroupId(rs.getString(1));
				gb.setGdesc(rs.getString(2));
				listGroups.add(gb);
			}

			// Free resources
			rs.close();
			stm.close();

			log.info("[getAvailableGroups] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE,
					"[getAvailableGroups] Some error occurred while was trying to execute the query: " + queryToUse, e);
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

}
