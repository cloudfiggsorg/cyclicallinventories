package com.gmodelo.cyclicinventories.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.NamingException;

import com.bmore.ume001.beans.User;
import com.gmodelo.cyclicinventories.beans.AbstractResultsBean;
import com.gmodelo.cyclicinventories.beans.CostByMatnr;
import com.gmodelo.cyclicinventories.beans.DocInvBean;
import com.gmodelo.cyclicinventories.beans.DocInvBeanHeaderSAP;
import com.gmodelo.cyclicinventories.beans.E_Lqua_SapEntity;
import com.gmodelo.cyclicinventories.beans.E_Mard_SapEntity;
import com.gmodelo.cyclicinventories.beans.E_Mbew_SapEntity;
import com.gmodelo.cyclicinventories.beans.E_Mseg_SapEntity;
import com.gmodelo.cyclicinventories.beans.E_Msku_SapEntity;
import com.gmodelo.cyclicinventories.beans.E_Salida_SapEntity;
import com.gmodelo.cyclicinventories.beans.E_Xtab6_SapEntity;
import com.gmodelo.cyclicinventories.beans.Justification;
import com.gmodelo.cyclicinventories.beans.MaterialExplosionBean;
import com.gmodelo.cyclicinventories.beans.PosDocInvBean;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.beans.ZIACST_I360_OBJECTDATA_SapEntity;
import com.gmodelo.cyclicinventories.structure.ZIACMF_I360_EXT_SIS_CLAS;
import com.gmodelo.cyclicinventories.structure.ZIACMF_I360_INV_MOV_1;
import com.gmodelo.cyclicinventories.structure.ZIACMF_I360_INV_MOV_2;
import com.gmodelo.cyclicinventories.structure.ZIACMF_I360_INV_MOV_3;
import com.gmodelo.cyclicinventories.structure.ZIACMF_I360_MOV;
import com.gmodelo.cyclicinventories.utils.ConnectionManager;
import com.gmodelo.cyclicinventories.utils.ReturnValues;

public class SapOperationDao {

	// Selects Area

	private static final String GET_SINGLE_DOC_INV = "SELECT DOC_INV_ID, DIH_ROUTE_ID, DIH_BUKRS, DIH_CREATED_DATE,"
			+ " DIH_MODIFIED_DATE, DIH_WERKS FROM INV_DOC_INVENTORY_HEADER WITH(NOLOCK) WHERE DOC_INV_ID = ?";

	private static final String GET_SINGLE_DOC_INV_WITH_HEADERS = "SELECT DOC_INV_ID, DIH_ROUTE_ID, DIH_BUKRS, T1.BUTXT DIH_BUTXT, "
			+ "DIH_CREATED_DATE, DIH_MODIFIED_DATE, DIH_WERKS, T1W.NAME1 DIH_WERKD, DIH_TYPE FROM INV_DOC_INVENTORY_HEADER IDIH WITH(NOLOCK) "
			+ "INNER JOIN T001 T1 WITH(NOLOCK) on  IDIH.DIH_BUKRS = T1.BUKRS "
			+ "INNER JOIN T001W T1W WITH(NOLOCK) on IDIH.DIH_WERKS = T1W.WERKS WHERE DOC_INV_ID = ?";

	private static final String GET_LGORT_FROM_DOC_INV = "SELECT ZON.ZON_LGORT FROM INV_ROUTE_POSITION ROP WITH(NOLOCK) "
			+ " INNER JOIN INV_ZONE ZON WITH(NOLOCK) ON ZON.ZONE_ID = ROP.RPO_ZONE_ID"
			+ " WHERE ROP.RPO_ROUTE_ID = ? GROUP BY ZON.ZON_LGORT";

	private static final String GET_LGORT_MATNR_POS_DOC_INV = "SELECT IE.EX_LGORT FROM INV_DOC_INVENTORY_HEADER IDIH WITH (NOLOCK) "
			+ "INNER JOIN INV_DOC_INVENTORY_POSITIONS IDIP WITH (NOLOCK) ON IDIP.DIP_DOC_INV_ID = IDIH.DOC_INV_ID  "
			+ "INNER JOIN INV_EXPLOSION IE WITH (NOLOCK) ON IE.EX_MATNR = IDIP.DIP_MATNR "
			+ "WHERE IDIH.DOC_INV_ID = ? GROUP BY IE.EX_LGORT";

	private static final String GET_LGNUM_AND_LGTYP_DOC_INV = "SELECT ZPO.ZPO_LGNUM, ZPO.ZPO_LGTYP FROM INV_ROUTE_POSITION ROP WITH(NOLOCK)"
			+ " INNER JOIN INV_ZONE ZON WITH(NOLOCK) ON ZON.ZONE_ID = ROP.RPO_ZONE_ID "
			+ " INNER JOIN INV_ZONE_POSITION ZPO WITH(NOLOCK) ON ZON.ZONE_ID = ZPO.ZPO_ZONE_ID "
			+ " WHERE ROP.RPO_ROUTE_ID = ? AND ZPO.ZPO_INDICATOR_IM_WM = 'WM' GROUP BY ZPO.ZPO_LGTYP, ZPO.ZPO_LGNUM ";

	private static final String GET_MATERIALS_FOR_DOC_INV = "SELECT DIP_MATNR from INV_DOC_INVENTORY_POSITIONS WITH(NOLOCK) WHERE DIP_DOC_INV_ID = ? GROUP BY DIP_MATNR";

	private static final String GET_CLASSSYSTEM = "SELECT SUBSTRING(EC.MATNR, PATINDEX('%[^0 ]%', EC.MATNR + ' '), LEN(EC.MATNR)) MATNR,"
			+ " MKT.MAKTX, SMBEZ, ATFLV, ATNAM FROM E_CLASS EC WITH(NOLOCK) "
			+ " INNER JOIN MAKT MKT WITH(NOLOCK) ON SUBSTRING(EC.MATNR, PATINDEX('%[^0 ]%', EC.MATNR + ' '), LEN(EC.MATNR)) = "
			+ " SUBSTRING(MKT.MATNR, PATINDEX('%[^0 ]%', MKT.MATNR + ' '), LEN(MKT.MATNR)) "
			+ " GROUP BY EC.MATNR, MKT.MAKTX, SMBEZ, ATFLV, ATNAM";

	private static final String TRANSIT = "SELECT SUBSTRING(MATNR, PATINDEX('%[^0 ]%', MATNR + ' '), LEN(MATNR)) AS MATNR, SUM(CAST(MENGE AS decimal(15,3))) MENGE FROM E_XTAB6 WHERE DOC_INV_ID = ? "
			+ "GROUP BY MATNR";

	private static final String CONSIGNATION = "SELECT SUBSTRING(MATNR, PATINDEX('%[^0 ]%', MATNR + ' '), LEN(MATNR)) AS MATNR, SUM((CAST(KULAB AS decimal(15,3)) "
			+ "+ CAST(KUINS AS decimal(15,3)) + CAST(KUEIN AS decimal(15,3)))) AS CONS FROM E_MSKU_F WHERE DOC_INV_ID = ? "
			+ "GROUP BY MATNR";

	private static final String THEORIC_IM = "SELECT LGORT, SUBSTRING(MATNR, PATINDEX('%[^0 ]%', MATNR + ' '), LEN(MATNR)) AS MATNR, "
			+ "(CAST(LABST AS decimal(15,3)) "
			+ "+ CAST(UMLME AS decimal(15,3)) + CAST(INSME AS decimal(15,3)) + CAST(EINME AS decimal(15,3)) "
			+ "+ CAST(SPEME AS decimal(15,3)) + CAST(RETME AS decimal(15,3))) AS CONS FROM E_MARD WHERE LGORT = ? "
			+ "AND SUBSTRING(MATNR, PATINDEX('%[^0 ]%', MATNR + ' '), LEN(MATNR)) = ? AND DOC_INV_ID = ?";

	private static final String THEORIC_IM_BY_BUKRS = "SELECT SUBSTRING(MATNR, PATINDEX('%[^0 ]%', MATNR + ' '), LEN(MATNR)) AS MATNR, "
			+ "SUM((CAST(LABST AS decimal(15,3)) " + "+ CAST(UMLME AS decimal(15,3)) + CAST(INSME AS decimal(15,3)) "
			+ "+ CAST(EINME AS decimal(15,3)) + CAST(SPEME AS decimal(15,3)) "
			+ "+ CAST(RETME AS decimal(15,3)))) AS CONS " + "FROM E_MARD "
			+ "WHERE SUBSTRING(MATNR, PATINDEX('%[^0 ]%', MATNR + ' '), LEN(MATNR)) = ? AND DOC_INV_ID = ? "
			+ "GROUP BY MATNR";

	private static final String THEORIC_WM = "SELECT LGNUM, LGORT, LGTYP, LGPLA, SUBSTRING(MATNR, PATINDEX('%[^0 ]%', MATNR + ' '), LEN(MATNR)) AS MATNR, "
			+ "SUM(CAST(VERME AS decimal(15,3))) AS CONS FROM E_LQUA WHERE "
			+ "LGNUM = ? AND LGORT = ? AND LGTYP = ? AND LGPLA = ? AND DOC_INV_ID = ? GROUP BY LGNUM, LGORT, LGTYP, LGPLA, MATNR ";

	private static final String THEORIC_WM_BY_BUKRS = "SELECT SUBSTRING(MATNR, PATINDEX('%[^0 ]%', MATNR + ' '), LEN(MATNR)) AS MATNR, "
			+ "SUM(CAST(VERME AS decimal(15,3))) AS CONS FROM E_LQUA WHERE " + "DOC_INV_ID = ? GROUP BY MATNR ";

	private static final String MOVEMENTS_WM = "SELECT (SELECT SUM(CAST(MENGE AS decimal(15,3))) FROM E_MSEG "
			+ "WHERE LGORT = ? AND LGNUM = ? AND LGTYP = ? AND LGPLA = ? AND CAST(BUDAT_MKPF + ' ' + CPUTM_MKPF as datetime) < ? "
			+ "AND SUBSTRING(MATNR, PATINDEX('%[^0 ]%', MATNR + ' '), LEN(MATNR)) = ? AND SHKZG = 'S' AND DOC_INV_ID = ?) - "
			+ "(SELECT SUM(CAST(MENGE AS decimal(15,3))) FROM E_MSEG "
			+ "WHERE LGORT = ? AND LGNUM = ? AND LGTYP = ? AND LGPLA = ? AND CAST(BUDAT_MKPF + ' ' + CPUTM_MKPF as datetime) < ? "
			+ "AND SUBSTRING(MATNR, PATINDEX('%[^0 ]%', MATNR + ' '), LEN(MATNR)) = ? AND SHKZG = 'H' AND DOC_INV_ID = ?) AS MENGE";

	private static final String MOVEMENTS_IM = "SELECT (SELECT ISNULL(SUM(CAST(MENGE AS decimal(15,3))), 0) "
			+ "FROM E_MSEG "
			+ "WHERE LGORT = ? AND SUBSTRING(MATNR, PATINDEX('%[^0 ]%', MATNR + ' '), LEN(MATNR)) = ? AND SHKZG = 'S' AND DOC_INV_ID = ? AND CAST(BUDAT_MKPF + ' ' + CPUTM_MKPF as datetime) < ?) - "
			+ "(SELECT ISNULL(SUM(CAST(MENGE AS decimal(15,3))), 0) "
			+ "FROM E_MSEG WHERE LGORT = ? AND SUBSTRING(MATNR, PATINDEX('%[^0 ]%', MATNR + ' '), LEN(MATNR)) = ? AND SHKZG = 'H' AND DOC_INV_ID = ? AND CAST(BUDAT_MKPF + ' ' + CPUTM_MKPF as datetime) < ?) AS MENGE";

	private static final String MOVEMENTS_BY_BUKRS = "SELECT (SELECT ISNULL(SUM(CAST(MENGE AS decimal(15,3))), 0) "
			+ "FROM E_MSEG "
			+ "WHERE SUBSTRING(MATNR, PATINDEX('%[^0 ]%', MATNR + ' '), LEN(MATNR)) = ? AND SHKZG = 'S' AND DOC_INV_ID = ? AND CAST(BUDAT_MKPF + ' ' + CPUTM_MKPF as datetime) < ?) - "
			+ "(SELECT ISNULL(SUM(CAST(MENGE AS decimal(15,3))), 0) "
			+ "FROM E_MSEG WHERE SUBSTRING(MATNR, PATINDEX('%[^0 ]%', MATNR + ' '), LEN(MATNR)) = ? AND SHKZG = 'H' AND DOC_INV_ID = ? AND CAST(BUDAT_MKPF + ' ' + CPUTM_MKPF as datetime) < ?) AS MENGE";

	private static final String COUNTED_MATNRS = "SELECT LGNUM, DIP_LGORT, DIP_LGTYP, DIP_LGPLA, DIP_MATNR, DIP_COUNT_DATE FROM (SELECT LGNUM, LNUMT, "
			+ "DIP_LGORT, DIP_LGTYP, DIP_LGPLA, DIP_MATNR, MAX(DIP_COUNT_DATE) DIP_COUNT_DATE "
			+ "FROM INV_DOC_INVENTORY_POSITIONS AS A "
			+ "INNER JOIN INV_DOC_INVENTORY_HEADER AS B ON (A.DIP_DOC_INV_ID = B.DOC_INV_ID) "
			+ "INNER JOIN INV_VW_NGORT_WITH_GORT AS C ON (B.DIH_WERKS = C.WERKS AND A.DIP_LGORT = C.LGORT) "
			+ "WHERE DIP_DOC_INV_ID = ? AND LEN(LGNUM) > 0 AND DIP_COUNT_DATE IS NOT NULL "
			+ "GROUP BY LGNUM, LNUMT, DIP_LGORT, DIP_LGTYP, DIP_LGPLA, DIP_MATNR) AS TAB "
			+ "GROUP BY LGNUM, DIP_LGORT, DIP_LGTYP, DIP_LGPLA, DIP_MATNR, DIP_COUNT_DATE";

	private static final String COUNTED_MATNRS_BY_BUKRS = "SELECT MATNR, MAX(DIP_COUNT_DATE) DIP_COUNT_DATE FROM (SELECT SUBSTRING(D.IDNRK, PATINDEX('%[^0 ]%', D.IDNRK + ' '), LEN(D.IDNRK)) MATNR, DIP_COUNT_DATE "
			+ "FROM (SELECT DIP_DOC_INV_ID, DIP_MATNR MATNR, DIP_COUNT_DATE " + "FROM INV_DOC_INVENTORY_POSITIONS "
			+ "WHERE DIP_DOC_INV_ID = ? " + "GROUP BY DIP_DOC_INV_ID, DIP_MATNR, DIP_COUNT_DATE) AS A "
			+ "LEFT JOIN MAST AS B ON (A.MATNR = SUBSTRING(B.MATNR, PATINDEX('%[^0 ]%', B.MATNR + ' '), LEN(B.MATNR))) "
			+ "INNER JOIN STKO AS C ON (B.STLNR = C.STLNR)  " + "INNER JOIN STPO AS D ON (C.STLNR = D.STLNR) "
			+ "INNER JOIN INV_DOC_INVENTORY_HEADER AS IDIH ON (IDIH.DOC_INV_ID = A.DIP_DOC_INV_ID) "
			+ "WHERE DIP_DOC_INV_ID = ? " + "AND B.WERKS = IDIH.DIH_WERKS "
			+ "AND SUBSTRING(D.IDNRK, PATINDEX('%[^0 ]%', D.IDNRK + ' '), LEN(D.IDNRK)) " + "IN (SELECT EX_COMPONENT "
			+ "FROM INV_EXPLOSION WHERE EX_WERKS = IDIH.DIH_WERKS " + "AND A.MATNR = EX_MATNR AND EX_RELEVANT = 1) "
			+ "GROUP BY IDNRK, DIP_COUNT_DATE "

			+ "UNION "

			+ "SELECT DIP_MATNR MATNR, DIP_COUNT_DATE " + "FROM INV_DOC_INVENTORY_POSITIONS AS A "
			+ "WHERE DIP_DOC_INV_ID = ? "

			+ "UNION "

			+ "SELECT DISTINCT(DIP_VHILM) MATNR, MAX(DIP_COUNT_DATE) DIP_COUNT_DATE "
			+ "FROM INV_DOC_INVENTORY_POSITIONS " + "WHERE DIP_DOC_INV_ID = ? " + "GROUP BY DIP_VHILM) AS TBL "
			+ "GROUP BY MATNR, DIP_COUNT_DATE ";

	private static final String GET_MBEW_PIVOT = "SELECT MATNR from INV_CIC_E_PIV_MBEW WITH(NOLOCK) "
			+ " WHERE IS_UPDATING = 1 AND DATEDIFF(DAY, LAST_UPDATED, CONVERT(DATE, GETDATE())) > "
			+ " CONVERT(INT, (SELECT STORED_VALUE FROM INV_CIC_REPOSITORY WITH(NOLOCK) WHERE STORED_KEY = 'INV_CIC_E_PIV_UPDATE_FREC' )) "
			+ " AND (MATNR IN (SELECT DIP_MATNR from INV_DOC_INVENTORY_POSITIONS WITH(NOLOCK) WHERE DIP_DOC_INV_ID = ? GROUP BY DIP_MATNR ) "
			+ " OR MATNR IN (SELECT MATNR FROM INV_VW_GET_EXP_MAT_FOR_DOC_INV WHERE DOC_INV_ID = ?)"
			+ " OR MATNR IN (SELECT DIP_VHILM from INV_DOC_INVENTORY_POSITIONS WITH(NOLOCK) WHERE DIP_DOC_INV_ID = ? GROUP BY DIP_VHILM ) ) ";

	private static final String GET_MBEW_COUNT_PIVOT = "SELECT COUNT(MATNR) AS MAT_UPD from INV_CIC_E_PIV_MBEW WITH(NOLOCK) "
			+ " WHERE IS_UPDATING = 0 AND DATEDIFF(DAY, LAST_UPDATED, CONVERT(DATE, GETDATE())) > "
			+ " CONVERT(INT, (SELECT STORED_VALUE FROM INV_CIC_REPOSITORY WITH(NOLOCK) WHERE STORED_KEY = 'INV_CIC_E_PIV_UPDATE_FREC' )) "
			+ " AND (MATNR IN (SELECT DIP_MATNR from INV_DOC_INVENTORY_POSITIONS WITH(NOLOCK) WHERE DIP_DOC_INV_ID = ? GROUP BY DIP_MATNR ) "
			+ " OR MATNR IN (SELECT MATNR FROM INV_VW_GET_EXP_MAT_FOR_DOC_INV WHERE DOC_INV_ID = ?) "
			+ " OR MATNR IN (SELECT DIP_VHILM from INV_DOC_INVENTORY_POSITIONS WITH(NOLOCK) WHERE DIP_DOC_INV_ID = ? GROUP BY DIP_VHILM ) ) ";

	private static final String COST_BY_MATNR = "SELECT MATNR, ZPRECIO "
			+ "FROM INV_VW_GET_COSTS_FOR_DOC_INV WHERE DOC_INV_ID = ?";

	private static final String GET_E_MBEW = "SELECT MATNR, BWKEY, ZPRECIO FROM E_MBEW WITH(NOLOCK)";

	private static final String GET_JUSTIFICATION = "SELECT A.JS_ID, JS_CON_SAP, JS_QUANTITY, "
			+ "(CAST(A.JS_JUSTIFY AS varchar(200)) + ' - ' + B.JUSTIFICATION) JUSTIFICATION, JS_FILE_NAME "
			+ "FROM INV_JUSTIFY AS A " + "INNER JOIN INV_CAT_JUSTIFY AS B ON (A.JS_JUSTIFY = B.JS_ID) "
			+ "WHERE JS_CON_SAP IN (SELECT * FROM STRING_SPLIT(?, ',')) ";

	private static final String GET_POS_CONS_SAP = "SELECT CS_CON_SAP, CS_MATNR, ISNULL(CATEGORY, '') CATEGORY, MAKTX, MEINS, CS_COST_BY_UNIT, CS_THEORIC, "
			+ "CS_COUNTED, CS_COUNTED_EXPL, CS_DIFFERENCE, CS_TRANSIT, CS_CONSIGNATION, CS_IS_EXPL "
			+ "FROM INV_VW_CONC_SAP AS A " + "LEFT JOIN INV_REL_CAT_MAT AS B ON (A.CS_MATNR = B.REL_MATNR) "
			+ "LEFT JOIN INV_CAT_CATEGORY AS C ON (B.REL_CAT_ID = C.CAT_ID) " + "WHERE DOC_INV_ID = ? "
			+ "GROUP BY CS_CON_SAP, CS_MATNR, CATEGORY, MAKTX, MEINS, CS_COST_BY_UNIT, CS_THEORIC, "
			+ "CS_COUNTED, CS_COUNTED_EXPL, CS_DIFFERENCE, CS_TRANSIT, CS_CONSIGNATION, CS_IS_EXPL";

	// CONCILIATION FOR WM - LGORT_LGPLA

	private static final String INV_VW_REP_HEADER = "SELECT DOC_INV_ID, DIH_BUKRS, BUTXT, DIH_WERKS, NAME1, DIH_TYPE, "
			+ "DIH_CLSD_SAP_BY, DIH_CLSD_SAP_DATE, DIH_ROUTE_ID, ROU_DESC, DIH_CREATED_DATE, DIH_MODIFIED_DATE "
			+ "FROM INV_VW_DOC_INV_REP_HEADER WHERE DOC_INV_ID = ?";

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

	private static final String SET_E_SALIDA = "INSERT INTO E_SALIDA (DOC_INV_ID, LGNUM, LGTYP, NLPLA, MATNR, NISTM, VISTM, MEINS, QDATU, QZEIT) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	// UPDATE AREA

	private static final String UPDATE_INITIAL_INVENTORY = "UPDATE INV_DOC_INVENTORY_HEADER SET INSAP_SNAPSHOT = '1' WHERE DOC_INV_ID = ?";

	private static final String UPDATE_FINAL_INVENTORY = "UPDATE INV_DOC_INVENTORY_HEADER SET FNSAP_SNAPSHOT = '1' WHERE DOC_INV_ID = ?";

	private static final String UPDATE_INV_CIC_MBEW_PIVOT_BEG = " UPDATE INV_CIC_E_PIV_MBEW SET IS_UPDATING = 1 WHERE IS_UPDATING = 0 "
			+ " AND DATEDIFF(DAY, LAST_UPDATED, CONVERT(DATE, GETDATE())) > "
			+ " CONVERT(INT, (SELECT STORED_VALUE FROM INV_CIC_REPOSITORY WITH(NOLOCK) WHERE STORED_KEY = 'INV_CIC_E_PIV_UPDATE_FREC' )) "
			+ " AND (MATNR IN (SELECT DIP_MATNR from INV_DOC_INVENTORY_POSITIONS WITH(NOLOCK) WHERE DIP_DOC_INV_ID = ? GROUP BY DIP_MATNR ) "
			+ " OR MATNR IN (SELECT MATNR FROM INV_VW_GET_EXP_MAT_FOR_DOC_INV WHERE DOC_INV_ID = ?)"
			+ " OR MATNR IN (SELECT DIP_VHILM from INV_DOC_INVENTORY_POSITIONS WITH(NOLOCK) WHERE DIP_DOC_INV_ID = ? GROUP BY DIP_VHILM ) ) ";

	private static final String UPDATE_INV_CIC_MBEW_PIVOT_END = " UPDATE INV_CIC_E_PIV_MBEW SET IS_UPDATING = 0, LAST_UPDATED = CONVERT(DATE,GETDATE()) "
			+ " WHERE IS_UPDATING = 1 AND DATEDIFF(DAY, LAST_UPDATED, CONVERT(DATE, GETDATE())) > "
			+ " CONVERT(INT, (SELECT STORED_VALUE FROM INV_CIC_REPOSITORY WITH(NOLOCK) WHERE STORED_KEY = 'INV_CIC_E_PIV_UPDATE_FREC' )) "
			+ " AND (MATNR IN (SELECT DIP_MATNR from INV_DOC_INVENTORY_POSITIONS WITH(NOLOCK) WHERE DIP_DOC_INV_ID = ? GROUP BY DIP_MATNR ) "
			+ " OR MATNR IN (SELECT MATNR FROM INV_VW_GET_EXP_MAT_FOR_DOC_INV WHERE DOC_INV_ID = ?)"
			+ " OR MATNR IN (SELECT DIP_VHILM from INV_DOC_INVENTORY_POSITIONS WITH(NOLOCK) WHERE DIP_DOC_INV_ID = ? GROUP BY DIP_VHILM ) ) ";

	private static final String UPDATE_E_MBEW = "UPDATE E_MBEW SET ZPRECIO = ? WHERE MATNR = ? and BWKEY = ?";

	// DELETE AREA

	private static final String POP_E_CLASS = "DELETE FROM E_CLASS";

	// SP CALLS

	private static final String INV_SP_ADD_CON_POS_SAP = "INV_SP_ADD_CON_POS_SAP ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?";
	private static final String INV_SP_ADD_JUSTIFY = "INV_SP_ADD_JUSTIFY ?, ?, ?, ?, ?";
	private static final String INV_CLS_SAP_DOC_INV = "INV_CLS_SAP_DOC_INV ?, ?";
	private static final String INV_SP_DEL_CONS_SAP = "INV_SP_DEL_CONS_SAP ?";
	/*
	 * THIS IS THE SECTION FOR SELECT METHODS
	 * 
	 */

	private Logger log = Logger.getLogger(SapOperationDao.class.getName());

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
				outputDoc.setCreatedDate(rs.getTimestamp("DIH_CREATED_DATE").getTime());
				outputDoc.setModifiedDate(rs.getTimestamp("DIH_MODIFIED_DATE").getTime());
				outputDoc.setRoute(rs.getString("DIH_ROUTE_ID"));
			} else {
				outputDoc = null;
			}
		} catch (SQLException e) {
			throw e;
		}
		return outputDoc;
	}

	// End Conci LGPLA

	private static final String INV_VW_DOC_INV_REP_LGORT_LGPLA = "SELECT  DOC_INV_ID, DIP_LGORT, LGOBE, LGTYP, LTYPT, DIP_LGPLA, "
			+ " DIP_MATNR, MAKTX, MEINS, DIP_COUNTED, DIP_COUNT_DATE, DIP_COUNT_DATE_INI, "
			+ " DIP_VHILM_COUNT, DIP_VHILM,LGNUM, IMWM FROM INV_VW_DOC_INV_REP_LGORT_LGPLA WITH(NOLOCK) WHERE DOC_INV_ID = ? ORDER BY DIP_COUNT_DATE ASC ";

	private static final String GET_E_SALIDA_BY_DOC = "SELECT LGNUM, LGTYP, NLPLA, MATNR, NISTM ,VISTM, QDATU, QZEIT, MEINS FROM E_SALIDA WITH(NOLOCK) WHERE DOC_INV_ID = ? ";

	private static final String GET_COST_BY_MATNR_DOC_INV = "SELECT MATNR, ZPRECIO FROM INV_VW_GET_COSTS_FOR_DOC_INV WITH(NOLOCK) WHERE DOC_INV_ID = ?";

	private static final String GET_E_LQUA_FOR_WM_LANES = "SELECT EL.LGNUM, SUBSTRING(EL.MATNR, PATINDEX('%[^0 ]%', EL.MATNR + ' '), LEN(EL.MATNR)) MATNR, "
			+ " EL.MEINS, EL.LGORT, EL.LGTYP, EL.LGPLA, TL.LGOBE, MK.MAKTX, SUM(CONVERT(NUMERIC(23,3),EL.VERME)) VERME FROM E_LQUA EL WITH(NOLOCK) "
			+ " INNER JOIN T001L TL ON EL.WERKS = TL.WERKS AND EL.LGORT = TL.LGORT "
			+ " INNER JOIN MAKT MK ON EL.MATNR = MK.MATNR WHERE DOC_INV_ID = ? "
			+ " GROUP BY EL.LGNUM, EL.MATNR, EL.MEINS, EL.LGORT, EL.LGTYP, EL.LGPLA,TL.LGOBE, MK.MAKTX";

	private static final String GET_E_MARD_FOR_IM_DATA = "SELECT SUBSTRING(MATNR, PATINDEX('%[^0 ]%', MATNR + ' '), LEN(MATNR)) MATNR, LGORT, LABST "
			+ " FROM E_MARD WITH(NOLOCK) WHERE DOC_INV_ID = ? "
			+ " GROUP BY SUBSTRING(MATNR, PATINDEX('%[^0 ]%', MATNR + ' '), LEN(MATNR)), LGORT, LABST";

	private static final String GET_QUA_EXP_BY_DOC_INV = "SELECT DIP_MATNR, BMEIN, BMENG, MENGE, BMCAL, IDNRK, MEINS, EX_LGORT, MAKTX, LGOBE "
			+ " FROM INV_VW_GET_QUA_EXP_BY_DOC_INV WITH(NOLOCK) WHERE DOC_INV_ID = ?";

	public List<PosDocInvBean> getDocInvPositions(DocInvBean docInvBean, Connection con) throws SQLException {
		if (!con.isValid(0)) {
			con = new ConnectionManager().createConnection();
		}
		List<PosDocInvBean> docInvBeans = new ArrayList<>();
		try {
			PreparedStatement stm = con.prepareStatement(INV_VW_DOC_INV_REP_LGORT_LGPLA);
			stm.setInt(1, docInvBean.getDocInvId());
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				PosDocInvBean bean = new PosDocInvBean();
				bean.setMatnr(rs.getString("DIP_MATNR"));
				bean.setMatnrD(rs.getString("MAKTX"));
				bean.setMeins(rs.getString("MEINS"));
				bean.setLgort(rs.getString("DIP_LGORT"));
				bean.setLgortD(rs.getString("LGOBE"));
				bean.setLgNum(rs.getString("LGNUM"));
				bean.setLgtyp(rs.getString("LGTYP"));
				bean.setLtypt(rs.getString("LTYPT"));
				bean.setLgpla(rs.getString("DIP_LGPLA"));
				bean.setCounted(rs.getString("DIP_COUNTED"));
				bean.setDateIniCounted(rs.getTimestamp("DIP_COUNT_DATE_INI").getTime());
				bean.setDateEndCounted(rs.getTimestamp("DIP_COUNT_DATE").getTime());
				bean.setVhilm(rs.getString("DIP_VHILM"));
				bean.setVhilmCounted(rs.getString("DIP_VHILM_COUNT"));
				bean.setImwmMarker(rs.getString("IMWM"));
				docInvBeans.add(bean);
			}
		} catch (SQLException e) {
			throw e;
		}
		return docInvBeans;
	}

	public HashMap<String, List<MaterialExplosionBean>> getExplotionDetailDocInv(DocInvBean docInvBean, Connection con)
			throws SQLException {
		if (!con.isValid(0)) {
			con = new ConnectionManager().createConnection();
		}
		HashMap<String, List<MaterialExplosionBean>> mapExplotion = new HashMap<>();
		try {
			PreparedStatement stm = con.prepareStatement(GET_QUA_EXP_BY_DOC_INV);
			stm.setInt(1, docInvBean.getDocInvId());
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				if (mapExplotion.containsKey(rs.getString("DIP_MATNR"))) {
					mapExplotion.get(rs.getString("DIP_MATNR")).add(new MaterialExplosionBean(rs));
				} else {
					List<MaterialExplosionBean> listExplosion = new ArrayList<>();
					listExplosion.add(new MaterialExplosionBean(rs));
					mapExplotion.put(rs.getString("DIP_MATNR"), listExplosion);
				}
			}
		} catch (SQLException e) {
			throw e;
		}

		return mapExplotion;
	}

	public HashMap<String, E_Mard_SapEntity> getEmardforDocInv(DocInvBean docInvBean, Connection con)
			throws SQLException {
		if (!con.isValid(0)) {
			con = new ConnectionManager().createConnection();
		}
		HashMap<String, E_Mard_SapEntity> mapMard = new HashMap<>();
		try {
			PreparedStatement stm = con.prepareStatement(GET_E_MARD_FOR_IM_DATA);
			stm.setInt(1, docInvBean.getDocInvId());
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				E_Mard_SapEntity entity = new E_Mard_SapEntity();
				entity.setMatnr(rs.getString("MATNR"));
				entity.setLgort(rs.getString("LGORT"));
				entity.setLabst(rs.getString("LABST"));
				mapMard.put(rs.getString("MATNR"), entity);
			}
		} catch (SQLException e) {
			throw e;
		}
		return mapMard;
	}

	public HashMap<String, HashMap<String, E_Lqua_SapEntity>> getElquaforDocInv(DocInvBean docInvBean, Connection con)
			throws SQLException {
		if (!con.isValid(0)) {
			con = new ConnectionManager().createConnection();
		}
		HashMap<String, HashMap<String, E_Lqua_SapEntity>> e_Lqua_SapEntities = new HashMap<>();
		try {
			PreparedStatement stm = con.prepareStatement(GET_E_LQUA_FOR_WM_LANES);
			stm.setInt(1, docInvBean.getDocInvId());
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				E_Lqua_SapEntity entity = new E_Lqua_SapEntity();
				entity.setLgnum(rs.getString("LGNUM"));
				entity.setLgpla(rs.getString("LGPLA"));
				entity.setMatnr(rs.getString("MATNR"));
				entity.setLgort(rs.getString("LGORT"));
				entity.setLgtyp(rs.getString("LGTYP"));
				entity.setVerme(rs.getString("VERME"));
				entity.setLgortD(rs.getString("LGOBE"));
				entity.setMaktx(rs.getString("MAKTX"));
				entity.setMeins(rs.getString("MEINS"));
				if (e_Lqua_SapEntities.containsKey(entity.E_Lqua_SapEntity_Key())) {
					e_Lqua_SapEntities.get(entity.E_Lqua_SapEntity_Key()).put(rs.getString("MATNR"), entity);
				} else {
					HashMap<String, E_Lqua_SapEntity> inMap = new HashMap<>();
					inMap.put(rs.getString("MATNR"), entity);
					e_Lqua_SapEntities.put(entity.E_Lqua_SapEntity_Key(), inMap);
				}
			}
		} catch (SQLException e) {
			throw e;
		}
		return e_Lqua_SapEntities;
	}

	public HashMap<String, HashMap<String, List<E_Salida_SapEntity>>> getEsalidaDataDocInv(DocInvBean docInvBean,
			Connection con) throws SQLException {
		if (!con.isValid(0)) {
			con = new ConnectionManager().createConnection();
		}
		HashMap<String, HashMap<String, List<E_Salida_SapEntity>>> e_Salida_SapEntities = new HashMap<>();
		try {
			PreparedStatement stm = con.prepareStatement(GET_E_SALIDA_BY_DOC);
			stm.setInt(1, docInvBean.getDocInvId());
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				E_Salida_SapEntity entity = new E_Salida_SapEntity(rs);
				String key = entity.getLgnum() + "" + entity.getLgtyp() + "" + entity.getNlpla();
				if (e_Salida_SapEntities.containsKey(key)) {
					if (e_Salida_SapEntities.get(key).containsKey(entity.getMatnr())) {
						e_Salida_SapEntities.get(key).get(entity.getMatnr()).add(entity);
					} else {
						List<E_Salida_SapEntity> inList = new ArrayList<>();
						inList.add(entity);
						e_Salida_SapEntities.get(key).put(entity.getMatnr(), inList);
					}
				} else {
					HashMap<String, List<E_Salida_SapEntity>> inMap = new HashMap<>();
					List<E_Salida_SapEntity> inList = new ArrayList<>();
					inList.add(entity);
					inMap.put(entity.getMatnr(), inList);
					e_Salida_SapEntities.put(key, inMap);
				}
			}
		} catch (SQLException e) {
			throw e;
		}
		return e_Salida_SapEntities;
	}

	public HashMap<String, String> getCostByMaterial(DocInvBean docInvBean, Connection con) throws SQLException {
		if (!con.isValid(0)) {
			con = new ConnectionManager().createConnection();
		}
		HashMap<String, String> mapMaterialCost = new HashMap<>();
		try {
			PreparedStatement stm = con.prepareStatement(GET_COST_BY_MATNR_DOC_INV);
			stm.setInt(1, docInvBean.getDocInvId());
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				mapMaterialCost.put(rs.getString("MATNR"), rs.getString("ZPRECIO"));
			}
		} catch (SQLException e) {
			throw e;
		}
		return mapMaterialCost;
	}

	// End Conci LGPLA

	public DocInvBean getDocInvBeanDataHeaders(DocInvBean docInvBean, Connection con) throws SQLException {
		DocInvBean outputDoc = new DocInvBean();
		if (!con.isValid(0)) {
			con = new ConnectionManager().createConnection();
		}
		try {
			PreparedStatement stm = con.prepareStatement(GET_SINGLE_DOC_INV_WITH_HEADERS);
			stm.setInt(1, docInvBean.getDocInvId());
			ResultSet rs = stm.executeQuery();
			if (rs.next()) {
				outputDoc.setDocInvId(docInvBean.getDocInvId());
				outputDoc.setBukrs(rs.getString("DIH_BUKRS"));
				outputDoc.setWerks(rs.getString("DIH_WERKS"));
				outputDoc.setBukrsD(rs.getString("DIH_BUTXT"));
				outputDoc.setWerksD(rs.getString("DIH_WERKD"));
				outputDoc.setCreatedDate(rs.getTimestamp("DIH_CREATED_DATE").getTime());
				outputDoc.setModifiedDate(rs.getTimestamp("DIH_MODIFIED_DATE").getTime());
				outputDoc.setRoute(rs.getString("DIH_ROUTE_ID"));
				outputDoc.setType(rs.getString("DIH_TYPE"));
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

	public List<String> getDocInvMatRelevLgort(DocInvBean docInvBean, Connection con) throws SQLException {
		List<String> lgortList = new ArrayList<>();
		if (!con.isValid(0)) {
			con = new ConnectionManager().createConnection();
		}
		try {
			PreparedStatement stm = con.prepareStatement(GET_LGORT_MATNR_POS_DOC_INV);
			stm.setInt(1, docInvBean.getDocInvId());
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				lgortList.add(rs.getString("EX_LGORT"));
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

	public HashMap<String, List<String>> getMapLgnumLgtyp(DocInvBean docInvBean, Connection con) throws SQLException {
		if (!con.isValid(0)) {
			con = new ConnectionManager().createConnection();
		}
		HashMap<String, List<String>> lgnumLgtypMap = new HashMap<>();
		try {
			PreparedStatement stm = con.prepareStatement(GET_LGNUM_AND_LGTYP_DOC_INV);
			stm.setString(1, docInvBean.getRoute());
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				if (lgnumLgtypMap.containsKey(rs.getString("ZPO_LGNUM"))) {
					lgnumLgtypMap.get(rs.getString("ZPO_LGNUM")).add(rs.getString("ZPO_LGTYP"));
				} else {
					List<String> lgtypList = new ArrayList<>();
					lgtypList.add(rs.getString("ZPO_LGTYP"));
					lgnumLgtypMap.put(rs.getString("ZPO_LGNUM"), lgtypList);
				}
			}
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
			stm.setInt(2, docInvBean.getDocInvId());
			stm.setInt(3, docInvBean.getDocInvId());
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
			stm.setInt(2, docInvBean.getDocInvId());
			stm.setInt(3, docInvBean.getDocInvId());
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

	public E_Mard_SapEntity getMatnrTheoricIM(int docInvId, PosDocInvBean pb, Connection con) throws SQLException {

		PreparedStatement stm = null;
		stm = con.prepareStatement(THEORIC_IM);
		stm.setString(1, pb.getLgort());
		stm.setString(2, pb.getMatnr());
		stm.setInt(3, docInvId);
		ResultSet rs = stm.executeQuery();

		E_Mard_SapEntity ems = new E_Mard_SapEntity();
		ems.setRetme("0");

		while (rs.next()) {
			ems.setMatnr(rs.getString("MATNR"));
			ems.setLgort(rs.getString("LGORT"));
			ems.setRetme(rs.getString("CONS"));// The total here
		}

		return ems;
	}

	public E_Mard_SapEntity getMatnrTheoricImByBukrs(int docInvId, PosDocInvBean pb, Connection con)
			throws SQLException {

		PreparedStatement stm = null;
		stm = con.prepareStatement(THEORIC_IM_BY_BUKRS);
		stm.setString(1, pb.getMatnr());
		stm.setInt(2, docInvId);
		ResultSet rs = stm.executeQuery();

		E_Mard_SapEntity ems = new E_Mard_SapEntity();
		ems.setRetme("0");

		while (rs.next()) {
			ems.setMatnr(rs.getString("MATNR"));
			ems.setRetme(rs.getString("CONS"));// The total here
		}

		return ems;
	}

	public E_Lqua_SapEntity getMatnrTheoricWM(int docInvId, PosDocInvBean pb, Connection con) throws SQLException {

		PreparedStatement stm = null;
		stm = con.prepareStatement(THEORIC_WM);
		stm.setString(1, pb.getLgNum());
		stm.setString(2, pb.getLgort());
		stm.setString(3, pb.getLgtyp());
		stm.setString(4, pb.getLgpla());
		stm.setInt(5, docInvId);

		ResultSet rs = stm.executeQuery();

		E_Lqua_SapEntity els = new E_Lqua_SapEntity();
		els.setVerme("0");

		while (rs.next()) {
			els.setLgnum(rs.getString("LGNUM"));
			els.setLgort(rs.getString("LGORT"));
			els.setLgtyp(rs.getString("LGTYP"));
			els.setLgpla(rs.getString("LGPLA"));
			els.setMatnr(rs.getString("MATNR"));
			els.setVerme(rs.getString("CONS"));// The total here
		}

		return els;
	}

	public E_Lqua_SapEntity getMatnrTheoricWmByBukrs(int docInvId, PosDocInvBean pb, Connection con)
			throws SQLException {

		PreparedStatement stm = null;
		stm = con.prepareStatement(THEORIC_WM_BY_BUKRS);
		stm.setInt(1, docInvId);

		ResultSet rs = stm.executeQuery();

		E_Lqua_SapEntity els = new E_Lqua_SapEntity();
		els.setVerme("0");

		while (rs.next()) {
			els.setMatnr(rs.getString("MATNR"));
			els.setVerme(rs.getString("CONS"));// The total here
		}

		return els;
	}

	public ArrayList<CostByMatnr> getCostByMatnr(int docInvId, Connection con) throws SQLException {

		PreparedStatement stm = null;
		stm = con.prepareStatement(COST_BY_MATNR);
		stm.setInt(1, docInvId);
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

	public long getMatnrMovementsIM(PosDocInvBean pdib, int docInvId, Date dcounted, Connection con)
			throws SQLException {

		PreparedStatement stm = null;
		stm = con.prepareStatement(MOVEMENTS_IM);
		stm.setString(1, pdib.getLgort());
		stm.setString(2, pdib.getMatnr());
		stm.setInt(3, docInvId);
		stm.setTimestamp(4, new java.sql.Timestamp(dcounted.getTime()));

		stm.setString(5, pdib.getLgort());
		stm.setString(6, pdib.getMatnr());
		stm.setInt(7, docInvId);
		stm.setTimestamp(8, new java.sql.Timestamp(dcounted.getTime()));

		ResultSet rs = stm.executeQuery();
		long menge = 0;

		while (rs.next()) {
			menge = rs.getLong("MENGE");// The total here
		}

		return menge;
	}

	public long getMatnrMovementsByBukrs(PosDocInvBean pdib, int docInvId, Date dcounted, Connection con)
			throws SQLException {

		PreparedStatement stm = null;
		stm = con.prepareStatement(MOVEMENTS_BY_BUKRS);
		stm.setString(1, pdib.getMatnr());
		stm.setInt(2, docInvId);
		stm.setTimestamp(3, new java.sql.Timestamp(dcounted.getTime()));

		stm.setString(4, pdib.getMatnr());
		stm.setInt(5, docInvId);
		stm.setTimestamp(6, new java.sql.Timestamp(dcounted.getTime()));

		ResultSet rs = stm.executeQuery();
		long menge = 0;

		while (rs.next()) {
			menge = rs.getLong("MENGE");// The total here
		}

		return menge;
	}

	public long getMatnrMovementsWM(PosDocInvBean pdib, int docInvId, Date dcounted, Connection con)
			throws SQLException {

		PreparedStatement stm = null;
		stm = con.prepareStatement(MOVEMENTS_WM);

		stm.setString(1, pdib.getLgort());
		stm.setString(2, pdib.getLgNum());
		stm.setString(3, pdib.getLgtyp());
		stm.setString(4, pdib.getLgpla());
		stm.setTimestamp(5, new java.sql.Timestamp(dcounted.getTime()));
		stm.setString(6, pdib.getMatnr());
		stm.setInt(7, docInvId);

		stm.setString(8, pdib.getLgort());
		stm.setString(9, pdib.getLgNum());
		stm.setString(10, pdib.getLgtyp());
		stm.setString(11, pdib.getLgpla());
		stm.setTimestamp(12, new java.sql.Timestamp(dcounted.getTime()));
		stm.setString(13, pdib.getMatnr());
		stm.setInt(14, docInvId);

		ResultSet rs = stm.executeQuery();
		long menge = 0;

		while (rs.next()) {
			menge = rs.getLong("MENGE");// The total here
		}

		return menge;
	}

	public ArrayList<PosDocInvBean> getMatnrDates(int docInvId, Connection con) throws SQLException {

		PreparedStatement stm = null;
		stm = con.prepareStatement(COUNTED_MATNRS);
		stm.setInt(1, docInvId);
		ResultSet rs = stm.executeQuery();
		ArrayList<PosDocInvBean> lsMatnr = new ArrayList<>();
		PosDocInvBean matnr;

		while (rs.next()) {
			matnr = new PosDocInvBean();
			matnr.setLgNum(rs.getString("LGNUM"));
			matnr.setLgort(rs.getString("DIP_LGORT"));
			matnr.setLgtyp(rs.getString("DIP_LGTYP"));
			matnr.setLgpla(rs.getString("DIP_LGPLA"));
			matnr.setMatnr(rs.getString("DIP_MATNR"));
			matnr.setdCounted(rs.getTimestamp("DIP_COUNT_DATE"));
			lsMatnr.add(matnr);
		}

		return lsMatnr;
	}

	public ArrayList<PosDocInvBean> getMatnrDatesByBukrs(int docInvId, Connection con) throws SQLException {

		PreparedStatement stm = null;
		stm = con.prepareStatement(COUNTED_MATNRS_BY_BUKRS);
		stm.setInt(1, docInvId);
		stm.setInt(2, docInvId);
		stm.setInt(3, docInvId);
		stm.setInt(4, docInvId);
		ResultSet rs = stm.executeQuery();
		ArrayList<PosDocInvBean> lsMatnr = new ArrayList<>();
		PosDocInvBean matnr;

		while (rs.next()) {
			matnr = new PosDocInvBean();
			matnr.setMatnr(rs.getString("MATNR"));
			matnr.setdCounted(rs.getTimestamp("DIP_COUNT_DATE"));
			lsMatnr.add(matnr);
		}

		return lsMatnr;
	}

	/*
	 * THIS IS THE SECTION FOR INSERT METHODS
	 * 
	 */

	public AbstractResultsBean setZIACMF_I360_MOV(DocInvBean docInvBean, ZIACMF_I360_MOV ziacmf_I360_MOV,
			Connection con) throws SQLException {
		AbstractResultsBean result = new AbstractResultsBean();
		if (!con.isValid(0)) {
			con = new ConnectionManager().createConnection();
		}
		try {
			PreparedStatement stm = con.prepareStatement(SET_E_SALIDA);
			for (E_Salida_SapEntity esalidaEntity : ziacmf_I360_MOV.geteSalida_SapEntities()) {
				stm.setInt(1, docInvBean.getDocInvId());
				stm.setString(2, esalidaEntity.getLgnum());
				stm.setString(3, esalidaEntity.getLgtyp());
				stm.setString(4, esalidaEntity.getNlpla());
				stm.setString(5, esalidaEntity.getMatnr());
				stm.setString(6, esalidaEntity.getNistm());
				stm.setString(7, esalidaEntity.getVistm());
				stm.setString(8, esalidaEntity.getMeins());
				stm.setString(9, esalidaEntity.getQdatu());
				stm.setString(10, esalidaEntity.getQzeit());
				stm.addBatch();
			}
			stm.executeBatch();
		} catch (SQLException e) {
			throw e;
		}
		return result;
	}

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
			stm.setInt(2, docInvBean.getDocInvId());
			stm.setInt(3, docInvBean.getDocInvId());
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
			stm.setInt(2, docInvBean.getDocInvId());
			stm.setInt(3, docInvBean.getDocInvId());
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

	// SapConciliationDao Moved Code

	public Response<DocInvBeanHeaderSAP> saveConciliationSAP(DocInvBeanHeaderSAP dibhSAP, String userId) {

		Response<DocInvBeanHeaderSAP> resp = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		List<PosDocInvBean> lsPositionBean = dibhSAP.getDocInvPosition();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		CallableStatement cs = null;
		CallableStatement csBatch = null;

		String CURRENTSP = "";
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

		try {
			con.setAutoCommit(false);
			csBatch = con.prepareCall(INV_SP_ADD_CON_POS_SAP);

			for (PosDocInvBean dipb : lsPositionBean) {

				CURRENTSP = INV_SP_ADD_CON_POS_SAP;

				if (!dipb.getLsJustification().isEmpty()) {

					cs = null;
					cs = con.prepareCall(INV_SP_ADD_CON_POS_SAP);

					cs.setInt(1, dibhSAP.getDocInvId());
					cs.setString(2, dipb.getLgort());
					cs.setString(3, dipb.getImwmMarker());
					cs.setString(4, dipb.getLgtyp());
					cs.setString(5, dipb.getLgpla());
					cs.setString(6, dipb.getMatnr());
					cs.setString(7, dipb.getCostByUnit());
					cs.setString(8, dipb.getMeins());
					cs.setString(9, dipb.getCounted());
					cs.setString(10, dipb.getCountedExpl());
					cs.setString(11, dipb.getTheoric());
					cs.setString(12, dipb.getDiff());
					cs.setString(13, dipb.getConsignation());
					cs.setString(14, dipb.getTransit());
					cs.setByte(15, (byte) (dipb.isExplosion() ? 1 : 0));
					cs.setString(16, userId);
					cs.registerOutParameter(17, Types.BIGINT);

					cs.execute();

					log.info("[saveConciliationSAP] Sentence successfully executed. " + CURRENTSP);

					dipb.setPosId(cs.getInt(17));
					CURRENTSP = INV_SP_ADD_JUSTIFY;

					ArrayList<Justification> lsJustification = dipb.getLsJustification();

					// Insert the justification per position
					for (Justification js : lsJustification) {

						cs = null;
						cs = con.prepareCall(INV_SP_ADD_JUSTIFY);
						cs.setLong(1, dipb.getPosId());
						cs.setString(2, js.getQuantity());
						cs.setInt(3, js.getJsId());
						cs.setString(4, js.getFileName());
						cs.registerOutParameter(5, Types.BIGINT);
						cs.execute();

						js.setJsId(cs.getInt(5));

						log.info("[saveConciliationSAP] Sentence successfully executed. " + CURRENTSP);
					}

				} else {

					csBatch.setInt(1, dibhSAP.getDocInvId());
					csBatch.setString(2, dipb.getLgort());
					csBatch.setString(3, dipb.getImwmMarker());
					csBatch.setString(4, dipb.getLgtyp());
					csBatch.setString(5, dipb.getLgpla());
					csBatch.setString(6, dipb.getMatnr());
					csBatch.setString(7, dipb.getCostByUnit());
					csBatch.setString(8, dipb.getMeins());
					csBatch.setString(9, dipb.getCounted());
					csBatch.setString(10, dipb.getCountedExpl());
					csBatch.setString(11, dipb.getTheoric());
					csBatch.setString(12, dipb.getDiff());
					csBatch.setString(13, dipb.getConsignation());
					csBatch.setString(14, dipb.getTransit());
					csBatch.setByte(15, (byte) (dipb.isExplosion() ? 1 : 0));
					csBatch.setString(16, userId);
					csBatch.setNull(17, Types.NULL);
					csBatch.addBatch();
				}
			}

			log.info("[saveConciliationSAP] Sentence successfully executed.");
			csBatch.executeBatch();

			cs = null;
			cs = con.prepareCall(INV_CLS_SAP_DOC_INV);
			cs.setInt(1, dibhSAP.getDocInvId());
			cs.setString(2, userId);
			cs.execute();

			UMEDaoE ume = new UMEDaoE();
			User user = new User();
			user.getEntity().setIdentyId(userId);
			ArrayList<User> ls = new ArrayList<>();
			ls.add(user);
			ls = ume.getUsersLDAPByCredentials(ls);

			if (ls.size() > 0) {

				dibhSAP.setCreatedBy(userId + " - " + ls.get(0).getGenInf().getName() + " "
						+ ls.get(0).getGenInf().getLastName() + " - " + format.format(new Date()));
			} else {
				dibhSAP.setCreatedBy(userId + " - " + format.format(new Date()));
			}

			con.commit();
			con.setAutoCommit(true);
			cs.close();

			log.info("[saveConciliationSAP] Executing query...");

		} catch (Exception e) {

			try {
				log.log(Level.WARNING, "[saveConciliationSAP] Execute rollback");
				con.rollback();
			} catch (SQLException e1) {
				log.log(Level.SEVERE, "[saveConciliationSAP] Not rollback .", e);
			}

			log.log(Level.SEVERE,
					"[saveConciliationSAP] Some error occurred while was trying to execute the S.P.: " + CURRENTSP, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			resp.setAbstractResult(abstractResult);
			return resp;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,
						"[saveConciliationSAP] Some error occurred while was trying to close the connection.", e);
			}
		}

		resp.setAbstractResult(abstractResult);
		resp.setLsObject(dibhSAP);
		return resp;

	}

	public Response<Object> deleteConciliationSAP(int docInvId) {

		Response<Object> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		CallableStatement cs = null;

		// The Store procedure to
		// call
		log.info("[deleteConciliationSAP] Preparing sentence...");

		try {
			cs = con.prepareCall(INV_SP_DEL_CONS_SAP);
			cs.setInt(1, docInvId);
			log.log(Level.WARNING, "[deleteConciliationSAP] Executing query...");
			cs.execute();
			abstractResult.setResultId(ReturnValues.ISUCCESS);
			// Retrive the warnings if there're
			// Free resources
			cs.close();
			log.info("[deleteConciliationSAP] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE, "[deleteConciliationSAP] Some error occurred while was trying to execute the S.P.: "
					+ INV_SP_DEL_CONS_SAP, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,
						"[deleteConciliationSAP] Some error occurred while was trying to close the connection.", e);
			}
		}
		res.setAbstractResult(abstractResult);
		return res;
	}

	public Response<DocInvBeanHeaderSAP> getClosedConsSapReport(DocInvBean docInvBean) {

		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		DocInvBeanHeaderSAP bean = new DocInvBeanHeaderSAP();
		Response<DocInvBeanHeaderSAP> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		log.info(INV_VW_REP_HEADER);
		log.info("[getReporteDocInvDao] Preparing sentence...");

		try {
			stm = con.prepareStatement(INV_VW_REP_HEADER);
			stm.setInt(1, docInvBean.getDocInvId());
			log.info("[getReporteDocInvDao] Executing query...");
			ResultSet rs = stm.executeQuery();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd - HH:mm:ss");

			if (rs.next()) {

				bean.setDocInvId(docInvBean.getDocInvId());
				bean.setBukrs(rs.getString("DIH_BUKRS"));
				bean.setBukrsD(rs.getString("BUTXT"));
				bean.setRoute(rs.getString("ROU_DESC"));
				bean.setWerks(rs.getString("DIH_WERKS"));
				bean.setWerksD(rs.getString("NAME1"));
				bean.setType(rs.getString("DIH_TYPE"));
				bean.setCreationDate(sdf.format(new Date(rs.getTimestamp("DIH_CREATED_DATE").getTime())));
				bean.setConciliationDate(sdf.format(new Date(rs.getTimestamp("DIH_MODIFIED_DATE").getTime())));
				bean.setDocInvPosition(getConciliationSAPPositions(bean.getDocInvId(), con));
				bean.setCreatedBy(rs.getString("DIH_CLSD_SAP_BY"));
				bean.setConcSAPDate(sdf.format(new Date(rs.getTimestamp("DIH_CLSD_SAP_DATE").getTime())));

				User user = new User();
				UMEDaoE ume = new UMEDaoE();
				user.getEntity().setIdentyId(bean.getCreatedBy());
				ArrayList<User> ls = new ArrayList<>();
				ls.add(user);
				ls = ume.getUsersLDAPByCredentials(ls);
				bean.setCreatedBy(bean.getCreatedBy() + " - " + ls.get(0).getGenInf().getName() + " "
						+ ls.get(0).getGenInf().getLastName() + " - " + bean.getConcSAPDate());

			}
		} catch (SQLException | NamingException e) {
			log.log(Level.SEVERE, "[getClosedConsSap] Some error occurred while was trying to execute the query: "
					+ INV_VW_REP_HEADER, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,
						"[getClosedConsSap] Some error occurred while was trying to close the connection.", e);
			}
		}

		res.setAbstractResult(abstractResult);
		res.setLsObject(bean);

		return res;
	}

	public ArrayList<PosDocInvBean> getConciliationSAPPositions(int docInvId, Connection con) throws SQLException {

		PreparedStatement ps = null;
		ResultSet rs;
		PosDocInvBean pdib;
		String lsPosIds = "";
		ArrayList<PosDocInvBean> lsPdib = new ArrayList<>();
		try {

			ps = con.prepareStatement(GET_POS_CONS_SAP);
			ps.setInt(1, docInvId);
			rs = ps.executeQuery();

			while (rs.next()) {

				pdib = new PosDocInvBean();
				pdib.setPosId(rs.getInt("CS_CON_SAP"));
				pdib.setMatnr(rs.getString("CS_MATNR"));
				pdib.setMatnrD(rs.getString("MAKTX"));
				pdib.setCategory(rs.getString("CATEGORY"));
				pdib.setMeins(rs.getString("MEINS"));
				pdib.setCostByUnit(rs.getString("CS_COST_BY_UNIT"));
				pdib.setTheoric(rs.getString("CS_THEORIC"));
				pdib.setCounted(rs.getString("CS_COUNTED"));
				pdib.setCountedExpl(rs.getString("CS_COUNTED_EXPL"));
				pdib.setDiff(rs.getString("CS_DIFFERENCE"));
				pdib.setTransit(rs.getString("CS_TRANSIT"));
				pdib.setConsignation(rs.getString("CS_CONSIGNATION"));
				pdib.setExplosion(rs.getBoolean("CS_IS_EXPL"));
				lsPosIds += pdib.getPosId() + ",";
				lsPdib.add(pdib);
			}

			// Get the justifications
			ArrayList<Justification> lsJustify = getJustification(lsPosIds, con);

			// Add the justification to positions
			for (PosDocInvBean obj : lsPdib) {

				for (Justification js : lsJustify) {

					if (js.getConsPosSAPId() == obj.getPosId()) {
						obj.getLsJustification().add(js);
					}
				}
			}

		} catch (SQLException e) {
			log.log(Level.SEVERE,
					"[getConciliationSAPPositions] Some error occurred while was trying to retrive the positions.", e);
			throw e;
		}

		return lsPdib;
	}

	private ArrayList<Justification> getJustification(String ids, Connection con) throws SQLException {

		ArrayList<Justification> lsJustification = new ArrayList<>();
		Justification js;
		PreparedStatement stm = null;
		ResultSet rs;
		try {

			stm = con.prepareStatement(GET_JUSTIFICATION);
			stm.setString(1, ids);
			rs = stm.executeQuery();

			while (rs.next()) {

				js = new Justification();
				js.setJsId(rs.getInt("JS_ID"));
				js.setConsPosSAPId(rs.getInt("JS_CON_SAP"));
				js.setQuantity(rs.getString("JS_QUANTITY"));
				js.setJustify(rs.getString("JUSTIFICATION"));
				js.setFileName(rs.getString("JS_FILE_NAME"));
				lsJustification.add(js);
			}
		} catch (SQLException e) {
			log.log(Level.SEVERE,
					"[getJustification] Some error occurred while was trying to retrive the justifications.", e);
			throw e;
		}
		return lsJustification;
	}

}
