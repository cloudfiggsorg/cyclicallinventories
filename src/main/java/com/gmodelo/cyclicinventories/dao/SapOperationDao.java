package com.gmodelo.cyclicinventories.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.gmodelo.cyclicinventories.beans.AbstractResultsBean;
import com.gmodelo.cyclicinventories.beans.CostByMatnr;
import com.gmodelo.cyclicinventories.beans.DocInvBean;
import com.gmodelo.cyclicinventories.beans.E_Lqua_SapEntity;
import com.gmodelo.cyclicinventories.beans.E_Mard_SapEntity;
import com.gmodelo.cyclicinventories.beans.E_Mbew_SapEntity;
import com.gmodelo.cyclicinventories.beans.E_Mseg_SapEntity;
import com.gmodelo.cyclicinventories.beans.E_Msku_SapEntity;
import com.gmodelo.cyclicinventories.beans.E_Xtab6_SapEntity;
import com.gmodelo.cyclicinventories.beans.ZIACST_I360_OBJECTDATA_SapEntity;
import com.gmodelo.cyclicinventories.structure.ZIACMF_I360_EXT_SIS_CLAS;
import com.gmodelo.cyclicinventories.structure.ZIACMF_I360_INV_MOV_1;
import com.gmodelo.cyclicinventories.structure.ZIACMF_I360_INV_MOV_2;
import com.gmodelo.cyclicinventories.structure.ZIACMF_I360_INV_MOV_3;
import com.gmodelo.cyclicinventories.utils.ConnectionManager;

public class SapOperationDao {

	// Selects Area

	private static final String GET_SINGLE_DOC_INV = "SELECT DOC_INV_ID, DIH_ROUTE_ID, DIH_BUKRS, DIH_CREATED_DATE,"
			+ " DIH_MODIFIED_DATE, DIH_WERKS FROM INV_DOC_INVENTORY_HEADER WITH(NOLOCK) WHERE DOC_INV_ID = ?";

	private static final String GET_LGORT_FROM_DOC_INV = "SELECT ZON.ZON_LGORT FROM INV_ROUTE_POSITION ROP WITH(NOLOCK) "
			+ " INNER JOIN INV_ZONE ZON WITH(NOLOCK) ON ZON.ZONE_ID = ROP.RPO_ZONE_ID"
			+ " WHERE ROP.RPO_ROUTE_ID = ? GROUP BY ZON.ZON_LGORT";

	private static final String GET_LGNUM_AND_LGTYP_DOC_INV = "SELECT ZPO.ZPO_LGNUM, ZPO.ZPO_LGTYP FROM INV_ROUTE_POSITION ROP WITH(NOLOCK)"
			+ " INNER JOIN INV_ZONE ZON WITH(NOLOCK) ON ZON.ZONE_ID = ROP.RPO_ZONE_ID "
			+ " INNER JOIN INV_ZONE_POSITION ZPO WITH(NOLOCK) ON ZON.ZONE_ID = ZPO.ZPO_ZONE_ID "
			+ " WHERE ROP.RPO_ROUTE_ID = ? AND ZPO.ZPO_INDICATOR_IM_WM = 'WM' GROUP BY ZPO.ZPO_LGTYP, ZPO.ZPO_LGNUM ";

	private static final String GET_MATERIALS_FOR_DOC_INV = "SELECT DIP_MATNR from INV_DOC_INVENTORY_POSITIONS WITH(NOLOCK) WHERE DIP_DOC_INV_ID = ? GROUP BY DIP_MATNR";

	private static final String GET_CLASSSYSTEM = "SELECT EC.MATNR, MKT.MAKTX, SMBEZ, ATFLV, ATNAM FROM E_CLASS EC WITH(NOLOCK)"
			+ " INNER JOIN MAKT MKT WITH(NOLOCK) ON EC.MATNR = MKT.MATNR"
			+ " GROUP BY EC.MATNR, MKT.MAKTX, SMBEZ, ATFLV, ATNAM";

	private static final String TRANSIT = "SELECT SUBSTRING(MATNR, PATINDEX('%[^0 ]%', MATNR + ' '), LEN(MATNR)) AS MATNR, MENGE FROM E_XTAB6 WHERE DOC_INV_ID = ? ";

	private static final String CONSIGNATION = "SELECT SUBSTRING(MATNR, PATINDEX('%[^0 ]%', MATNR + ' '), LEN(MATNR)) AS MATNR, SUM((CAST(KULAB AS decimal(10,3)) "
			+ "+ CAST(KUINS AS decimal(10,3)) + CAST(KUEIN AS decimal(10,3)))) AS CONS FROM E_MSKU WHERE DOC_INV_ID = ? "
			+ "GROUP BY MATNR";

	private static final String THEORIC_IM = "SELECT SUBSTRING(MATNR, PATINDEX('%[^0 ]%', MATNR + ' '), LEN(MATNR)) AS MATNR, (CAST(LABST AS decimal(10,3)) "
			+ "+ CAST(UMLME AS decimal(10,3)) + CAST(INSME AS decimal(10,3)) + CAST(EINME AS decimal(10,3)) "
			+ "+ CAST(SPEME AS decimal(10,3)) + CAST(RETME AS decimal(10,3))) AS CONS FROM E_MARD_F WHERE DOC_INV_ID = ?";

	private static final String THEORIC_WM = "SELECT SUBSTRING(MATNR, PATINDEX('%[^0 ]%', MATNR + ' '), LEN(MATNR)) AS MATNR, LGPLA, "
			+ "SUM(CAST(VERME AS decimal(10,3))) AS CONS FROM E_LQUA_F WHERE DOC_INV_ID = ? GROUP BY MATNR, LGPLA";

	private static final String MOVEMENTS_WM = "SELECT (SELECT SUM(CAST(MENGE AS decimal(10,3))) FROM E_MSEG "
			+ "WHERE LGORT = ? AND LGNUM = ? AND LGTYP = ? AND LGPLA = ? AND MATNR = ? AND SHKZG = 'S') - "
			+ "(SELECT SUM(CAST(MENGE AS decimal(10,3))) " + "FROM E_MSEG "
			+ "WHERE LGORT = ? AND LGNUM = ? AND LGTYP = ? AND LGPLA = ? AND MATNR = ? AND SHKZG = 'H') AS MENGE";

	private static final String MOVEMENTS_IM = "SELECT (SELECT SUM(CAST(MENGE AS decimal(10,3))) FROM E_MSEG "
			+ "WHERE LGORT = ? AND MATNR = ? AND SHKZG = 'S') - " + "(SELECT SUM(CAST(MENGE AS decimal(10,3))) "
			+ "FROM E_MSEG " + "WHERE LGORT = ? AND MATNR = ? AND SHKZG = 'H') AS MENGE ";

	private static final String GET_MBEW_PIVOT = "SELECT MATNR from INV_CIC_E_PIV_MBEW WITH(NOLOCK) "
			+ " WHERE IS_UPDATING = 1 AND DATEDIFF(DAY, LAST_UPDATED, CONVERT(DATE, GETDATE())) > "
			+ " CONVERT(INT, (SELECT STORED_VALUE FROM INV_CIC_REPOSITORY WITH(NOLOCK) WHERE STORED_KEY = 'INV_CIC_E_PIV_UPDATE_FREC' )) "
			+ " AND MATNR IN (SELECT DIP_MATNR from INV_DOC_INVENTORY_POSITIONS WITH(NOLOCK) WHERE DIP_DOC_INV_ID = ? GROUP BY DIP_MATNR)";

	private static final String GET_MBEW_COUNT_PIVOT = "SELECT COUNT(MATNR) AS MAT_UPD from INV_CIC_E_PIV_MBEW WITH(NOLOCK) "
			+ " WHERE IS_UPDATING = 0 AND DATEDIFF(DAY, LAST_UPDATED, CONVERT(DATE, GETDATE())) > "
			+ " CONVERT(INT, (SELECT STORED_VALUE FROM INV_CIC_REPOSITORY WITH(NOLOCK) WHERE STORED_KEY = 'INV_CIC_E_PIV_UPDATE_FREC' )) "
			+ " AND MATNR IN (SELECT DIP_MATNR from INV_DOC_INVENTORY_POSITIONS WITH(NOLOCK) WHERE DIP_DOC_INV_ID = ? GROUP BY DIP_MATNR)";

	private static final String COST_BY_MATNR = "SELECT SUBSTRING(MATNR, PATINDEX('%[^0 ]%', MATNR + ' '), LEN(MATNR)) MATNR, ZPRECIO " 
			+ "FROM E_MBEW " 
			+ "WHERE SUBSTRING(MATNR, PATINDEX('%[^0 ]%', MATNR + ' '), LEN(MATNR)) IN (SELECT * FROM STRING_SPLIT(?, ',')) AND BWKEY = ?";

	private static final String GET_E_MBEW = "SELECT MATNR, BWKEY, ZPRECIO FROM E_MBEW WITH(NOLOCK)";

	// INSERT AREA

	private static final String SET_E_MSEG = "INSERT INTO E_MSEG (DOC_INV_ID, MBLNR, ZEILE, BWART, MATNR, WERKS, LGORT, INSMK, SHKZG, "
			+ "MENGE, MEINS, BUKRS, LGNUM, LGTYP, LGPLA, BUDAT_MKPF, CPUTM_MKPF) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	private static final String SET_E_MARD = "INSERT INTO E_MARD (DOC_INV_ID, MATNR, WERKS, LGORT, LABST, UMLME, INSME, EINME, SPEME, RETME) "
			+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	private static final String SET_E_MSKU = "insert into E_MSKU (DOC_INV_ID, MATNR, WERKS, KULAB, KUINS, KUEIN) "
			+ "VALUES (?, ?, ?, ?, ?, ?)";

	private static final String SET_E_LQUA = "insert into E_LQUA (DOC_INV_ID, LGNUM, MATNR, WERKS, LGORT, LGTYP, LGPLA, VERME, MEINS, LENUM)"
			+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	private static final String SET_E_MARD_F = "INSERT INTO E_MARD_F (DOC_INV_ID, MATNR, WERKS, LGORT, LABST, UMLME, INSME, EINME, SPEME, RETME) "
			+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	private static final String SET_E_MSKU_F = "insert into E_MSKU_F (DOC_INV_ID, MATNR, WERKS, KULAB, KUINS, KUEIN) "
			+ "VALUES (?, ?, ?, ?, ?, ?)";

	private static final String SET_E_LQUA_F = "insert into E_LQUA_F (DOC_INV_ID, LGNUM, MATNR, WERKS, LGORT, LGTYP, LGPLA, VERME, MEINS, LENUM)"
			+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	private static final String SET_E_XTAB6 = "insert into E_XTAB6 (DOC_INV_ID,WERKS,MATNR,MENGE,MEINS,DMBTR,WAERS,NETWR,BWAER,EBELN,EBELP,SOBKZ,PSTYP,BSTMG,BSTME,RESWK,BSAKZ,LGORT,RESLO)"
			+ "VALUES (? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? )";

	private static final String SET_E_CLASS = "insert into E_CLASS (MATNR, SMBEZ, ATFLV, ATNAM) values (?,?,?,?)";

	private static final String SET_E_MBEW = "INSERT INTO E_MBEW (MATNR,BWKEY,ZPRECIO) VALUES (?,?,?)";

	// UPDATE AREA

	private static final String UPDATE_INITIAL_INVENTORY = "UPDATE INV_DOC_INVENTORY_HEADER SET INSAP_SNAPSHOT = '1' WHERE DOC_INV_ID = ?";

	private static final String UPDATE_FINAL_INVENTORY = "UPDATE INV_DOC_INVENTORY_HEADER SET FNSAP_SNAPSHOT = '1' WHERE DOC_INV_ID = ?";

	private static final String UPDATE_INV_CIC_MBEW_PIVOT_BEG = " UPDATE INV_CIC_E_PIV_MBEW SET IS_UPDATING = 1 WHERE IS_UPDATING = 0 "
			+ " AND DATEDIFF(DAY, LAST_UPDATED, CONVERT(DATE, GETDATE())) > "
			+ " CONVERT(INT, (SELECT STORED_VALUE FROM INV_CIC_REPOSITORY WITH(NOLOCK) WHERE STORED_KEY = 'INV_CIC_E_PIV_UPDATE_FREC' )) "
			+ " AND MATNR IN (SELECT DIP_MATNR from INV_DOC_INVENTORY_POSITIONS WITH(NOLOCK) WHERE DIP_DOC_INV_ID = ? GROUP BY DIP_MATNR)";

	private static final String UPDATE_INV_CIC_MBEW_PIVOT_END = " UPDATE INV_CIC_E_PIV_MBEW SET IS_UPDATING = 0, LAST_UPDATED = CONVERT(DATE,GETDATE()) "
			+ " WHERE IS_UPDATING = 1 AND DATEDIFF(DAY, LAST_UPDATED, CONVERT(DATE, GETDATE())) > "
			+ " CONVERT(INT, (SELECT STORED_VALUE FROM INV_CIC_REPOSITORY WITH(NOLOCK) WHERE STORED_KEY = 'INV_CIC_E_PIV_UPDATE_FREC' )) "
			+ " AND MATNR IN (SELECT DIP_MATNR from INV_DOC_INVENTORY_POSITIONS WITH(NOLOCK) WHERE DIP_DOC_INV_ID = ? GROUP BY DIP_MATNR)";

	private static final String UPDATE_E_MBEW = "UPDATE E_MBEW SET ZPRECIO = ? WHERE MATNR = ? and BWKEY = ?";

	// DELETE AREA

	private static final String POP_E_CLASS = "DELETE FROM E_CLASS";

	/*
	 * THIS IS THE SECTION FOR SELECT METHODS
	 * 
	 */

	public DocInvBean getDocInvBeanData(DocInvBean docInvBean, Connection con) throws SQLException {
		DocInvBean outputDoc = new DocInvBean();
		if (!con.isValid(0)) {
			con = new ConnectionManager().createConnection();
		}
		try {
			PreparedStatement stm = con.prepareStatement(GET_SINGLE_DOC_INV);
			stm.setInt(1, docInvBean.getDocInvId());
			ResultSet rs = stm.executeQuery();
			if (rs.next()) {
				outputDoc.setDocInvId(docInvBean.getDocInvId());
				outputDoc.setBukrs(rs.getString("DIH_BUKRS"));
				outputDoc.setWerks(rs.getString("DIH_WERKS"));
				outputDoc.setCreatedDate(rs.getDate("DIH_CREATED_DATE").getTime());
				outputDoc.setModifiedDate(rs.getDate("DIH_MODIFIED_DATE").getTime());
				outputDoc.setRoute(rs.getString("DIH_ROUTE_ID"));
			} else {
				outputDoc = null;
			}
		} catch (SQLException e) {
			throw e;
		}
		return outputDoc;
	}

	public List<String> getDocInvLgort(DocInvBean docInvBean, Connection con) throws SQLException {
		List<String> lgortList = new ArrayList<>();
		if (!con.isValid(0)) {
			con = new ConnectionManager().createConnection();
		}
		try {
			PreparedStatement stm = con.prepareStatement(GET_LGORT_FROM_DOC_INV);
			stm.setString(1, docInvBean.getRoute());
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				lgortList.add(rs.getString("ZON_LGORT"));
			}
		} catch (SQLException e) {
			throw e;
		}
		return lgortList;
	}

	public HashMap<String, List<String>> getDocInvLgnumLgtyp(DocInvBean docInvBean, Connection con)
			throws SQLException {
		if (!con.isValid(0)) {
			con = new ConnectionManager().createConnection();
		}
		HashMap<String, List<String>> lgnumLgtypMap = new HashMap<>();
		List<String> lgnumList = new ArrayList<>();
		List<String> lgtypList = new ArrayList<>();
		try {
			PreparedStatement stm = con.prepareStatement(GET_LGNUM_AND_LGTYP_DOC_INV);
			stm.setString(1, docInvBean.getRoute());
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				if (!lgnumList.contains(rs.getString("ZPO_LGNUM"))) {
					lgnumList.add(rs.getString("ZPO_LGNUM"));
				}
				if (!lgtypList.contains(rs.getString("ZPO_LGTYP"))) {
					lgtypList.add(rs.getString("ZPO_LGTYP"));
				}
			}
			lgnumLgtypMap.put("LGNUM", lgnumList);
			lgnumLgtypMap.put("LGTYP", lgtypList);
		} catch (SQLException e) {
			throw e;
		}
		return lgnumLgtypMap;
	}

	public List<String> getmaterialForDocInv(DocInvBean docInvBean, Connection con) throws SQLException {
		List<String> materialList = new ArrayList<>();
		if (!con.isValid(0)) {
			con = new ConnectionManager().createConnection();
		}
		try {
			PreparedStatement stm = con.prepareStatement(GET_MATERIALS_FOR_DOC_INV);
			stm.setInt(1, docInvBean.getDocInvId());
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				if (!materialList.contains(rs.getString("DIP_MATNR"))) {
					materialList.add(rs.getString("DIP_MATNR"));
				}
			}
		} catch (SQLException e) {
			throw e;
		}
		return materialList;
	}

	public List<String> getmaterialForPivotDocInv(DocInvBean docInvBean, Connection con) throws SQLException {
		List<String> materialList = new ArrayList<>();
		if (!con.isValid(0)) {
			con = new ConnectionManager().createConnection();
		}
		try {
			PreparedStatement stm = con.prepareStatement(GET_MBEW_PIVOT);
			stm.setInt(1, docInvBean.getDocInvId());
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				if (!materialList.contains(rs.getString("MATNR"))) {
					materialList.add(rs.getString("MATNR"));
				}
			}
		} catch (SQLException e) {
			throw e;
		}
		return materialList;
	}

	public Integer getCountMaterialForPivotDocInv(DocInvBean docInvBean, Connection con) throws SQLException {
		Integer materialCount = 0;
		if (!con.isValid(0)) {
			con = new ConnectionManager().createConnection();
		}
		try {
			PreparedStatement stm = con.prepareStatement(GET_MBEW_COUNT_PIVOT);
			stm.setInt(1, docInvBean.getDocInvId());
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				materialCount = rs.getInt("MAT_UPD");
			}
		} catch (SQLException e) {
			throw e;
		}
		return materialCount;
	}

	public HashMap<String, List<String>> getMbewValues(Connection con) throws SQLException {
		HashMap<String, List<String>> matnrCenterMap = new HashMap<>();
		if (!con.isValid(0)) {
			con = new ConnectionManager().createConnection();
		}
		try {
			PreparedStatement stm = con.prepareStatement(GET_E_MBEW);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				if (matnrCenterMap.containsKey(rs.getString("MATNR"))) {
					if (!matnrCenterMap.get(rs.getString("MATNR")).contains(rs.getString("BWKEY"))) {
						matnrCenterMap.get(rs.getString("MATNR")).add(rs.getString("BWKEY"));
					}
				} else {
					List<String> centerList = new ArrayList<>();
					centerList.add(rs.getString("BWKEY"));
					matnrCenterMap.put(rs.getString("MATNR"), centerList);
				}
			}
		} catch (SQLException e) {
			throw e;
		}
		return matnrCenterMap;
	}

	public ZIACMF_I360_EXT_SIS_CLAS getClassSystem() throws SQLException {
		ZIACMF_I360_EXT_SIS_CLAS ziacmf_I360_EXT_SIS_CLAS = new ZIACMF_I360_EXT_SIS_CLAS();
		List<ZIACST_I360_OBJECTDATA_SapEntity> i360_OBJECTDATA_SapEntities = new ArrayList<>();
		Connection con = new ConnectionManager().createConnection();
		PreparedStatement stm = con.prepareStatement(GET_CLASSSYSTEM);
		ResultSet rs = stm.executeQuery();
		while (rs.next()) {
			i360_OBJECTDATA_SapEntities.add(new ZIACST_I360_OBJECTDATA_SapEntity(rs));
		}
		ziacmf_I360_EXT_SIS_CLAS.setObjectData(i360_OBJECTDATA_SapEntities);
		return ziacmf_I360_EXT_SIS_CLAS;
	}

	public ArrayList<E_Mseg_SapEntity> getMatnrOnTransit(int docInvId, Connection con) throws SQLException {

		PreparedStatement stm = null;
		stm = con.prepareStatement(TRANSIT);
		stm.setInt(1, docInvId);
		ResultSet rs = stm.executeQuery();

		ArrayList<E_Mseg_SapEntity> lsMatnr = new ArrayList<>();
		E_Mseg_SapEntity emse;

		while (rs.next()) {
			emse = new E_Mseg_SapEntity();
			emse.setMatnr(rs.getString("MATNR"));
			emse.setMenge(rs.getString("MENGE"));
			lsMatnr.add(emse);
		}

		return lsMatnr;
	}

	public ArrayList<E_Msku_SapEntity> getMatnrOnCons(int docInvId, Connection con) throws SQLException {

		PreparedStatement stm = null;
		stm = con.prepareStatement(CONSIGNATION);
		stm.setInt(1, docInvId);
		ResultSet rs = stm.executeQuery();

		ArrayList<E_Msku_SapEntity> lsMatnr = new ArrayList<>();
		E_Msku_SapEntity emskuEntity;

		while (rs.next()) {
			emskuEntity = new E_Msku_SapEntity();
			emskuEntity.setMatnr(rs.getString("MATNR"));
			emskuEntity.setKulab(rs.getString("CONS"));// The total here
			lsMatnr.add(emskuEntity);
		}

		return lsMatnr;
	}

	public ArrayList<E_Mard_SapEntity> getMatnrTheoricIM(int docInvId, Connection con) throws SQLException {

		PreparedStatement stm = null;
		stm = con.prepareStatement(THEORIC_IM);
		stm.setInt(1, docInvId);
		ResultSet rs = stm.executeQuery();

		ArrayList<E_Mard_SapEntity> lsMatnr = new ArrayList<>();
		E_Mard_SapEntity ems;

		while (rs.next()) {
			ems = new E_Mard_SapEntity();
			ems.setMatnr(rs.getString("MATNR"));
			ems.setRetme(rs.getString("CONS"));// The total here
			lsMatnr.add(ems);
		}

		return lsMatnr;
	}

	public ArrayList<E_Lqua_SapEntity> getMatnrTheoricWM(int docInvId, Connection con) throws SQLException {

		PreparedStatement stm = null;
		stm = con.prepareStatement(THEORIC_WM);
		stm.setInt(1, docInvId);
		ResultSet rs = stm.executeQuery();

		ArrayList<E_Lqua_SapEntity> lsMatnr = new ArrayList<>();
		E_Lqua_SapEntity els;

		while (rs.next()) {
			els = new E_Lqua_SapEntity();
			els.setMatnr(rs.getString("MATNR"));
			els.setLgpla(rs.getString("LGPLA"));
			els.setVerme(rs.getString("CONS"));// The total here
			lsMatnr.add(els);
		}

		return lsMatnr;
	}
	
	public ArrayList<CostByMatnr> getCostByMatnr(String matnrIds, String werks, Connection con) throws SQLException {

		PreparedStatement stm = null;
		stm = con.prepareStatement(COST_BY_MATNR);
		stm.setString(1, matnrIds);
		stm.setString(2, werks);		
		
		ResultSet rs = stm.executeQuery();

		ArrayList<CostByMatnr> lsMatnr = new ArrayList<>();
		CostByMatnr els;

		while (rs.next()) {
			els = new CostByMatnr();
			els.setMatnr(rs.getString("MATNR"));
			els.setCost(rs.getString("ZPRECIO"));
			lsMatnr.add(els);
		}
		
		return lsMatnr;
	}

	public long getMatnrMovementsWM(E_Mseg_SapEntity emse, Connection con) throws SQLException {

		PreparedStatement stm = null;
		stm = con.prepareStatement(MOVEMENTS_WM);
		stm.setString(1, emse.getLgort());
		stm.setString(2, emse.getLgnum());
		stm.setString(3, emse.getLgtyp());
		stm.setString(4, emse.getLgpla());
		stm.setString(5, emse.getMatnr());
		stm.setString(6, emse.getLgort());
		stm.setString(7, emse.getLgnum());
		stm.setString(8, emse.getLgtyp());
		stm.setString(9, emse.getLgpla());
		stm.setString(10, emse.getMatnr());

		ResultSet rs = stm.executeQuery();
		long menge = 0;

		while (rs.next()) {
			menge = rs.getLong("MENGE");// The total here
		}

		return menge;
	}

	public long getMatnrMovementsIM(E_Mseg_SapEntity emse, Connection con) throws SQLException {

		PreparedStatement stm = null;
		stm = con.prepareStatement(MOVEMENTS_IM);
		stm.setString(1, emse.getLgort());
		stm.setString(2, emse.getMatnr());
		stm.setString(3, emse.getLgort());
		stm.setString(4, emse.getMatnr());

		ResultSet rs = stm.executeQuery();
		long menge = 0;

		while (rs.next()) {
			menge = rs.getLong("MENGE");// The total here
		}

		return menge;
	}

	/*
	 * THIS IS THE SECTION FOR INSERT METHODS
	 * 
	 */

	public AbstractResultsBean setZIACMF_I360_INV_MOV1(DocInvBean docInvBean, ZIACMF_I360_INV_MOV_1 i360_INV_MOV_1,
			Connection con) throws SQLException {
		AbstractResultsBean result = new AbstractResultsBean();
		if (!con.isValid(0)) {
			con = new ConnectionManager().createConnection();
		}
		try {
			PreparedStatement stm = con.prepareStatement(SET_E_MSEG);
			for (E_Mseg_SapEntity emsegEntity : i360_INV_MOV_1.geteMseg_SapEntities()) {
				stm.setString(1, String.valueOf(docInvBean.getDocInvId()));
				stm.setString(2, emsegEntity.getMblnr());
				stm.setString(3, emsegEntity.getZeile());
				stm.setString(4, emsegEntity.getBwart());
				stm.setString(5, emsegEntity.getMatnr());
				stm.setString(6, emsegEntity.getWerks());
				stm.setString(7, emsegEntity.getLgort());
				stm.setString(8, emsegEntity.getInsmk());
				stm.setString(9, emsegEntity.getShkzg());
				stm.setString(10, emsegEntity.getMenge());
				stm.setString(11, emsegEntity.getMeins());
				stm.setString(12, emsegEntity.getBukrs());
				stm.setString(13, emsegEntity.getLgnum());
				stm.setString(14, emsegEntity.getLgtyp());
				stm.setString(15, emsegEntity.getLgpla());
				stm.setString(16, emsegEntity.getBudat_mkpf());
				stm.setString(17, emsegEntity.getCputm_mkpf());
				stm.addBatch();
			}
			stm.executeBatch();
		} catch (SQLException e) {
			throw e;
		}
		return result;
	}

	public void setZIACMF_I360_INV_MOV2(DocInvBean docInvBean, ZIACMF_I360_INV_MOV_2 i360_INV_MOV_2, Connection con)
			throws SQLException {
		if (!con.isValid(0)) {
			con = new ConnectionManager().createConnection();
		}
		try {
			con.setAutoCommit(false);
			PreparedStatement stm = con.prepareStatement(SET_E_MARD);
			for (E_Mard_SapEntity emardEntity : i360_INV_MOV_2.geteMard_SapEntities()) {
				stm.setString(1, String.valueOf(docInvBean.getDocInvId()));
				stm.setString(2, emardEntity.getMatnr());
				stm.setString(3, emardEntity.getWerks());
				stm.setString(4, emardEntity.getLgort());
				stm.setString(5, emardEntity.getLabst());
				stm.setString(6, emardEntity.getUmlme());
				stm.setString(7, emardEntity.getInsme());
				stm.setString(8, emardEntity.getEinme());
				stm.setString(9, emardEntity.getSpeme());
				stm.setString(10, emardEntity.getRetme());
				stm.addBatch();
			}
			stm.executeBatch();

			stm = con.prepareStatement(SET_E_MSKU);
			for (E_Msku_SapEntity emskuEntity : i360_INV_MOV_2.geteMsku_SapEntities()) {
				stm.setString(1, String.valueOf(docInvBean.getDocInvId()));
				stm.setString(2, emskuEntity.getMatnr());
				stm.setString(3, emskuEntity.getWerks());
				stm.setString(4, emskuEntity.getKulab());
				stm.setString(5, emskuEntity.getKuins());
				stm.setString(6, emskuEntity.getKuein());
				stm.addBatch();
			}
			stm.executeBatch();

			stm = con.prepareStatement(SET_E_LQUA);
			for (E_Lqua_SapEntity elquaEntity : i360_INV_MOV_2.geteLqua_SapEntities()) {
				stm.setString(1, String.valueOf(docInvBean.getDocInvId()));
				stm.setString(2, elquaEntity.getLgnum());
				stm.setString(3, elquaEntity.getMatnr());
				stm.setString(4, elquaEntity.getWerks());
				stm.setString(5, elquaEntity.getLgort());
				stm.setString(6, elquaEntity.getLgtyp());
				stm.setString(7, elquaEntity.getLgpla());
				stm.setString(8, elquaEntity.getVerme());
				stm.setString(9, elquaEntity.getMeins());
				stm.setString(10, elquaEntity.getLenum());
				stm.addBatch();
			}
			stm.executeBatch();
			con.commit();
			con.setAutoCommit(true);
		} catch (SQLException e) {
			con.rollback();
			con.setAutoCommit(true);
			throw e;
		}
	}

	public AbstractResultsBean setZIACMF_I360_INV_MOV2_F(DocInvBean docInvBean, ZIACMF_I360_INV_MOV_2 i360_INV_MOV_2,
			Connection con) throws SQLException {
		AbstractResultsBean result = new AbstractResultsBean();
		if (!con.isValid(0)) {
			con = new ConnectionManager().createConnection();
		}
		try {
			PreparedStatement stm = con.prepareStatement(SET_E_MARD_F);
			for (E_Mard_SapEntity emardEntity : i360_INV_MOV_2.geteMard_SapEntities()) {
				stm.setString(1, String.valueOf(docInvBean.getDocInvId()));
				stm.setString(2, emardEntity.getMatnr());
				stm.setString(3, emardEntity.getWerks());
				stm.setString(4, emardEntity.getLgort());
				stm.setString(5, emardEntity.getLabst());
				stm.setString(6, emardEntity.getUmlme());
				stm.setString(7, emardEntity.getInsme());
				stm.setString(8, emardEntity.getEinme());
				stm.setString(9, emardEntity.getSpeme());
				stm.setString(10, emardEntity.getRetme());
				stm.addBatch();
			}
			stm.executeBatch();
			stm = con.prepareStatement(SET_E_MSKU_F);
			for (E_Msku_SapEntity emskuEntity : i360_INV_MOV_2.geteMsku_SapEntities()) {
				stm.setString(1, String.valueOf(docInvBean.getDocInvId()));
				stm.setString(2, emskuEntity.getMatnr());
				stm.setString(3, emskuEntity.getWerks());
				stm.setString(4, emskuEntity.getKulab());
				stm.setString(5, emskuEntity.getKuins());
				stm.setString(6, emskuEntity.getKuein());
				stm.addBatch();
			}
			stm.executeBatch();
			stm = con.prepareStatement(SET_E_LQUA_F);
			for (E_Lqua_SapEntity elquaEntity : i360_INV_MOV_2.geteLqua_SapEntities()) {
				stm.setString(1, String.valueOf(docInvBean.getDocInvId()));
				stm.setString(2, elquaEntity.getLgnum());
				stm.setString(3, elquaEntity.getMatnr());
				stm.setString(4, elquaEntity.getWerks());
				stm.setString(5, elquaEntity.getLgort());
				stm.setString(6, elquaEntity.getLgtyp());
				stm.setString(7, elquaEntity.getLgpla());
				stm.setString(8, elquaEntity.getVerme());
				stm.setString(9, elquaEntity.getMeins());
				stm.setString(10, elquaEntity.getLenum());
				stm.addBatch();
			}
			stm.executeBatch();
		} catch (SQLException e) {
			throw e;
		}
		return result;
	}

	public AbstractResultsBean setZIACMF_I360_INV_MOV3(DocInvBean docInvBean, ZIACMF_I360_INV_MOV_3 i360_INV_MOV_3,
			Connection con) throws SQLException {
		AbstractResultsBean result = new AbstractResultsBean();
		if (!con.isValid(0)) {
			con = new ConnectionManager().createConnection();
		}
		try {
			PreparedStatement stm = con.prepareStatement(SET_E_XTAB6);
			for (E_Xtab6_SapEntity extab6Entity : i360_INV_MOV_3.getXtab6_SapEntities()) {
				stm.setString(1, String.valueOf(docInvBean.getDocInvId()));
				stm.setString(2, extab6Entity.getWerks());
				stm.setString(3, extab6Entity.getMatnr());
				stm.setString(4, extab6Entity.getMenge());
				stm.setString(5, extab6Entity.getMeins());
				stm.setString(6, extab6Entity.getDmbtr());
				stm.setString(7, extab6Entity.getWaers());
				stm.setString(8, extab6Entity.getNetwr());
				stm.setString(9, extab6Entity.getBwaer());
				stm.setString(10, extab6Entity.getEbeln());
				stm.setString(11, extab6Entity.getEbelp());
				stm.setString(12, extab6Entity.getSobkz());
				stm.setString(13, extab6Entity.getPstyp());
				stm.setString(14, extab6Entity.getBstmg());
				stm.setString(15, extab6Entity.getBstme());
				stm.setString(16, extab6Entity.getReswk());
				stm.setString(17, extab6Entity.getBsakz());
				stm.setString(18, extab6Entity.getLgort());
				stm.setString(19, extab6Entity.getReslo());
				stm.addBatch();
			}
			stm.executeBatch();
		} catch (SQLException e) {
			throw e;
		}
		return result;
	}

	public AbstractResultsBean setZIACMF_I360_EXT_SIS_CLAS(Connection con,
			ZIACMF_I360_EXT_SIS_CLAS ziacmf_i360_ext_sis_clas) throws SQLException {
		AbstractResultsBean result = new AbstractResultsBean();
		if (!con.isValid(0)) {
			con = new ConnectionManager().createConnection();
		}
		try {
			PreparedStatement stm = con.prepareStatement(SET_E_CLASS);
			PreparedStatement stmDel = con.prepareStatement(POP_E_CLASS);
			for (ZIACST_I360_OBJECTDATA_SapEntity ziaEntity : ziacmf_i360_ext_sis_clas.getObjectData()) {
				stm.setString(1, ziaEntity.getObject());
				stm.setString(2, ziaEntity.getSmbez());
				stm.setString(3, ziaEntity.getAtflv());
				stm.setString(4, ziaEntity.getAtnam());
				stm.addBatch();
			}
			stmDel.executeUpdate();
			stm.executeBatch();
		} catch (SQLException e) {
			throw e;
		}
		return result;
	}

	public AbstractResultsBean setZIACMF_E_MBEW(Connection con, List<E_Mbew_SapEntity> e_Mbew_SapEntities)
			throws SQLException {
		AbstractResultsBean result = new AbstractResultsBean();
		if (!con.isValid(0)) {
			con = new ConnectionManager().createConnection();
		}
		try {
			PreparedStatement stm = con.prepareStatement(SET_E_MBEW);
			for (E_Mbew_SapEntity entity : e_Mbew_SapEntities) {
				stm.setString(1, entity.getMatnr());
				stm.setString(2, entity.getBwkey());
				stm.setString(3, entity.getZprecio());
				stm.addBatch();
			}
			stm.executeBatch();
		} catch (SQLException e) {
			throw e;
		}
		return result;

	}

	/*
	 * THIS IS THE SECTION FOR UPDATE METHODS
	 * 
	 */

	public void setUpdateInitialInventory(Connection con, DocInvBean docInvBean) throws SQLException {
		if (!con.isValid(0)) {
			con = new ConnectionManager().createConnection();
		}
		try {
			PreparedStatement stm = con.prepareStatement(UPDATE_INITIAL_INVENTORY);
			stm.setInt(1, docInvBean.getDocInvId());
			stm.executeUpdate();
		} catch (SQLException e) {
			throw e;
		}
	}

	public void setUpdateFinalInventory(Connection con, DocInvBean docInvBean) throws SQLException {
		if (!con.isValid(0)) {
			con = new ConnectionManager().createConnection();
		}
		try {
			PreparedStatement stm = con.prepareStatement(UPDATE_FINAL_INVENTORY);
			stm.setInt(1, docInvBean.getDocInvId());
			stm.executeUpdate();
		} catch (SQLException e) {
			throw e;
		}
	}

	public void setUpdatePivotBegin(Connection con, DocInvBean docInvBean) throws SQLException {
		if (!con.isValid(0)) {
			con = new ConnectionManager().createConnection();
		}
		try {
			PreparedStatement stm = con.prepareStatement(UPDATE_INV_CIC_MBEW_PIVOT_BEG);
			stm.setInt(1, docInvBean.getDocInvId());
			stm.executeUpdate();
		} catch (SQLException e) {
			throw e;
		}
	}

	public void setUpdatePivotEnd(Connection con, DocInvBean docInvBean) throws SQLException {
		if (!con.isValid(0)) {
			con = new ConnectionManager().createConnection();
		}
		try {
			PreparedStatement stm = con.prepareStatement(UPDATE_INV_CIC_MBEW_PIVOT_END);
			stm.setInt(1, docInvBean.getDocInvId());
			stm.executeUpdate();
		} catch (SQLException e) {
			throw e;
		}
	}

	public AbstractResultsBean setZIACMF_E_MBEW_UPD(Connection con, List<E_Mbew_SapEntity> e_Mbew_SapEntities)
			throws SQLException {
		AbstractResultsBean result = new AbstractResultsBean();
		if (!con.isValid(0)) {
			con = new ConnectionManager().createConnection();
		}
		try {
			PreparedStatement stm = con.prepareStatement(UPDATE_E_MBEW);
			for (E_Mbew_SapEntity entity : e_Mbew_SapEntities) {
				stm.setString(1, entity.getZprecio());
				stm.setString(2, entity.getMatnr());
				stm.setString(3, entity.getBwkey());
				stm.addBatch();
			}
			stm.executeBatch();
		} catch (SQLException e) {
			throw e;
		}
		return result;

	}

}
