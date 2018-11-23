package com.gmodelo.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import com.gmodelo.Exception.InvCicException;
import com.gmodelo.beans.DocInvBean;
import com.gmodelo.beans.E_Error_SapEntity;
import com.gmodelo.beans.E_Lqua_SapEntity;
import com.gmodelo.beans.E_Mard_SapEntity;
import com.gmodelo.beans.E_Mseg_SapEntity;
import com.gmodelo.beans.E_Msku_SapEntity;
import com.gmodelo.beans.E_Xtab6_SapEntity;
import com.gmodelo.structure.ZIACMF_I360_INV_MOV_1;
import com.gmodelo.structure.ZIACMF_I360_INV_MOV_2;
import com.gmodelo.structure.ZIACMF_I360_INV_MOV_3;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;

public class SapConciliationDao {

	private static final String ZIACMF_I360_INV_MOV_1 = "ZIACMF_I360_INV_MOV_1";
	private static final String ZIACMF_I360_INV_MOV_2 = "ZIACMF_I360_INV_MOV_2";
	private static final String ZIACMF_I360_INV_MOV_3 = "ZIACMF_I360_INV_MOV_3";
	private Logger log = Logger.getLogger(SapConciliationDao.class.getName());

	final SapOperationDao operationDao = new SapOperationDao();

	public ZIACMF_I360_INV_MOV_1 inventoryMovementsDao(DocInvBean docInvBean, Connection con,
			JCoDestination destination) throws JCoException, SQLException, RuntimeException, InvCicException {
		ZIACMF_I360_INV_MOV_1 ziacmf_I360_INV_MOV_1 = new ZIACMF_I360_INV_MOV_1();
		List<E_Mseg_SapEntity> msegList = new ArrayList<>();
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
			SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
			DocInvBean requestBean = operationDao.getDocInvBeanData(docInvBean, con);
			JCoFunction jcoFunction = destination.getRepository().getFunction(ZIACMF_I360_INV_MOV_1);
			jcoFunction.getImportParameterList().setValue("I_BUKRS", requestBean.getBukrs());
			jcoFunction.getImportParameterList().setValue("I_WERKS", requestBean.getWerks());
			jcoFunction.getImportParameterList().setValue("I_BUDAT_I",
					dateFormat.format(new Date(requestBean.getCreatedDate())));
			jcoFunction.getImportParameterList().setValue("I_BUDAT_F",
					dateFormat.format(new Date(requestBean.getModifiedDate())));
			jcoFunction.getImportParameterList().setValue("I_CPUTM_I",
					timeFormat.format(new Date(requestBean.getCreatedDate())));
			jcoFunction.getImportParameterList().setValue("I_CPUTM_F",
					timeFormat.format(new Date(requestBean.getModifiedDate())));
			// Form the Current Structure
			JCoTable lgortTable = jcoFunction.getImportParameterList().getTable("I_R_LGORT");
			for (String lgort : operationDao.getDocInvLgort(requestBean, con)) {
				lgortTable.appendRow();
				lgortTable.setValue("SIGN", "I");
				lgortTable.setValue("OPTION", "EQ");
				lgortTable.setValue("LOW", lgort);
				// lgortTable.setValue("High", lgort);
			}
			jcoFunction.execute(destination);
			JCoTable jcoE_MsegTable = jcoFunction.getExportParameterList().getTable("E_MSEG");
			JCoTable jcoE_Error = jcoFunction.getExportParameterList().getTable("E_ERROR");
			E_Error_SapEntity eError = new E_Error_SapEntity();
			try {
				eError = new E_Error_SapEntity(jcoE_Error);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				log.warning(e1.getMessage());
			}
			if (eError.getType().equals("S")) {
				do {

					try {
						msegList.add(new E_Mseg_SapEntity(jcoE_MsegTable));
					} catch (JCoException e) {
						// Not Readable Row or EOF
					}
				} while (jcoE_MsegTable.nextRow());
			}
			ziacmf_I360_INV_MOV_1.seteError_SapEntities(eError);
			ziacmf_I360_INV_MOV_1.seteMseg_SapEntities(msegList);
		} catch (JCoException e) {
			throw e;
		} catch (SQLException e) {
			throw e;
		} catch (RuntimeException e) {
			throw e;
		}
		return ziacmf_I360_INV_MOV_1;
	}

	public ZIACMF_I360_INV_MOV_2 getSystemSnapshot(DocInvBean docInvBean, Connection con, JCoDestination destination)
			throws JCoException, SQLException, RuntimeException, InvCicException {
		ZIACMF_I360_INV_MOV_2 ziacmf_I360_INV_MOV_2 = new ZIACMF_I360_INV_MOV_2();
		try {
			List<E_Mard_SapEntity> eMard_SapEntities = new ArrayList<>();
			List<E_Msku_SapEntity> eMsku_SapEntities = new ArrayList<>();
			List<E_Lqua_SapEntity> eLqua_SapEntities = new ArrayList<>();
			DocInvBean requestBean = operationDao.getDocInvBeanData(docInvBean, con);
			JCoFunction jcoFunction = destination.getRepository().getFunction(ZIACMF_I360_INV_MOV_2);
			jcoFunction.getImportParameterList().setValue("I_WERKS", requestBean.getWerks());
			JCoTable lgortTable = jcoFunction.getImportParameterList().getTable("I_R_LGORT");
			for (String lgort : operationDao.getDocInvLgort(requestBean, con)) {
				lgortTable.appendRow();
				lgortTable.setValue("SIGN", "I");
				lgortTable.setValue("OPTION", "EQ");
				lgortTable.setValue("LOW", lgort);
				// lgortTable.setValue("High", lgort);
			}
			JCoTable lgnumTable = jcoFunction.getImportParameterList().getTable("I_R_LGNUM");
			HashMap<String, List<String>> lgnumLgtypMap = operationDao.getDocInvLgnumLgtyp(requestBean, con);
			if (lgnumLgtypMap.get("LGNUM") != null && !lgnumLgtypMap.get("LGNUM").isEmpty()) {
				for (String lgnum : lgnumLgtypMap.get("LGNUM")) {
					lgnumTable.appendRow();
					lgnumTable.setValue("SIGN", "I");
					lgnumTable.setValue("OPTION", "EQ");
					lgnumTable.setValue("LOW", lgnum);
					// lgnumTable.setValue("High", lgnum);
				}
			}
			JCoTable lgtypTable = jcoFunction.getImportParameterList().getTable("I_R_LGTYP");
			if (lgnumLgtypMap.get("LGTYP") != null && !lgnumLgtypMap.get("LGTYP").isEmpty()) {
				for (String lgtyp : lgnumLgtypMap.get("LGTYP")) {
					lgtypTable.appendRow();
					lgtypTable.setValue("SIGN", "I");
					lgtypTable.setValue("OPTION", "EQ");
					lgtypTable.setValue("LOW", lgtyp);
					// lgtypTable.setValue("High", lgtyp);
				}
			}

			jcoFunction.execute(destination);
			JCoTable E_MARD = jcoFunction.getExportParameterList().getTable("E_MARD");
			JCoTable E_MSKU = jcoFunction.getExportParameterList().getTable("E_MSKU");
			JCoTable E_LQUA = jcoFunction.getExportParameterList().getTable("E_LQUA");
			JCoTable E_ERROR = jcoFunction.getExportParameterList().getTable("E_ERROR");
			E_Error_SapEntity eError = new E_Error_SapEntity();
			try {
				eError = new E_Error_SapEntity(E_ERROR);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				log.warning(e1.getMessage());
			}
			if (eError.getType().equals("S")) {
				// Cycle of the E_MARD Export Table
				do {
					try {
						eMard_SapEntities.add(new E_Mard_SapEntity(E_MARD));
					} catch (JCoException | RuntimeException e) {
						// Not Readable Row or EOF
						log.warning(e.getMessage());
					}
				} while (E_MARD.nextRow());

				// Cycle of the E_MSKU Export Table
				do {
					try {
						eMsku_SapEntities.add(new E_Msku_SapEntity(E_MSKU));
					} catch (JCoException | RuntimeException e) {
						// Not Readable Row or EOF
						log.warning(e.getMessage());
					}
				} while (E_MSKU.nextRow());

				// Cycle of the E_LQUA Export Table
				do {
					try {
						eLqua_SapEntities.add(new E_Lqua_SapEntity(E_LQUA));
					} catch (JCoException | RuntimeException e) {
						// Not Readable Row or EOF
						log.warning(e.getMessage());
					}
				} while (E_LQUA.nextRow());
			}
			ziacmf_I360_INV_MOV_2.seteMard_SapEntities(eMard_SapEntities);
			ziacmf_I360_INV_MOV_2.seteMsku_SapEntities(eMsku_SapEntities);
			ziacmf_I360_INV_MOV_2.seteLqua_SapEntities(eLqua_SapEntities);
			ziacmf_I360_INV_MOV_2.seteError_SapEntities(eError);
		} catch (JCoException e) {
			throw e;
		} catch (SQLException e) {
			throw e;
		} catch (RuntimeException e) {
			throw e;
		}
		return ziacmf_I360_INV_MOV_2;
	}

	
	
	public ZIACMF_I360_INV_MOV_3 getTransitMovements(DocInvBean docInvBean, Connection con, JCoDestination destination)
			throws JCoException, SQLException, RuntimeException, InvCicException {
		ZIACMF_I360_INV_MOV_3 ziacmf_I360_INV_MOV_3 = new ZIACMF_I360_INV_MOV_3();
		try {
			List<E_Xtab6_SapEntity> xtab6_SapEntities = new ArrayList<>();
			JCoFunction jcoFunction = destination.getRepository().getFunction(ZIACMF_I360_INV_MOV_3);
			DocInvBean requestBean = operationDao.getDocInvBeanData(docInvBean, con);
			JCoTable materialTable = jcoFunction.getImportParameterList().getTable("I_R_MATNR");
			for (String material : operationDao.getmaterialForDocInv(requestBean, con)) {
				materialTable.appendRow();
				materialTable.setValue("SIGN", "I");
				materialTable.setValue("OPTION", "EQ");
				materialTable.setValue("LOW", material);
			}
			JCoTable werksTable = jcoFunction.getImportParameterList().getTable("I_R_WERKS");
			werksTable.appendRow();
			werksTable.setValue("SIGN", "I");
			werksTable.setValue("OPTION", "EQ");
			werksTable.setValue("LOW", requestBean.getWerks());
			// werksTable.setValue("High", requestBean.getWerks());

			jcoFunction.execute(destination);
			JCoTable E_XTAB6 = jcoFunction.getExportParameterList().getTable("E_XTAB6");
			JCoTable E_ERROR = jcoFunction.getExportParameterList().getTable("E_ERROR");
			E_Error_SapEntity eError = new E_Error_SapEntity();
			try {
				eError = new E_Error_SapEntity(E_ERROR);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				log.warning(e1.getMessage());
			}
			if (eError.getType().equals("S")) {
				do {
					try {
						xtab6_SapEntities.add(new E_Xtab6_SapEntity(E_XTAB6));
					} catch (JCoException e) {
						// Not Readable Row or EOF
					}
				} while (E_XTAB6.nextRow());
			}
			ziacmf_I360_INV_MOV_3.setXtab6_SapEntities(xtab6_SapEntities);
			ziacmf_I360_INV_MOV_3.seteError_SapEntities(eError);
		} catch (JCoException e) {
			throw e;
		} catch (SQLException e) {
			throw e;
		} catch (RuntimeException e) {
			throw e;
		}
		return ziacmf_I360_INV_MOV_3;
	}

}
