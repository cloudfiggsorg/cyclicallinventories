package com.gmodelo.cyclicinventories.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.gmodelo.cyclicinventories.beans.DocInvBean;
import com.gmodelo.cyclicinventories.beans.E_Error_SapEntity;
import com.gmodelo.cyclicinventories.beans.E_Lqua_SapEntity;
import com.gmodelo.cyclicinventories.beans.E_Mard_SapEntity;
import com.gmodelo.cyclicinventories.beans.E_Mbew_SapEntity;
import com.gmodelo.cyclicinventories.beans.E_Mseg_SapEntity;
import com.gmodelo.cyclicinventories.beans.E_Msku_SapEntity;
import com.gmodelo.cyclicinventories.beans.E_Salida_SapEntity;
import com.gmodelo.cyclicinventories.beans.E_Xtab6_SapEntity;
import com.gmodelo.cyclicinventories.beans.ZIACST_I360_OBJECTDATA_SapEntity;
import com.gmodelo.cyclicinventories.exception.InvCicException;
import com.gmodelo.cyclicinventories.structure.ZIACMF_I360_EXT_SIS_CLAS;
import com.gmodelo.cyclicinventories.structure.ZIACMF_I360_INV_MOV_1;
import com.gmodelo.cyclicinventories.structure.ZIACMF_I360_INV_MOV_2;
import com.gmodelo.cyclicinventories.structure.ZIACMF_I360_INV_MOV_3;
import com.gmodelo.cyclicinventories.structure.ZIACMF_I360_MOV;
import com.gmodelo.cyclicinventories.structure.ZIACMF_MBEW;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;

public class SapConciliationDao {

	private static final String ZIACMF_I360_MOV = "ZIACMF_I360_MOV";
	private static final String ZIACMF_I360_INV_MOV_1 = "ZIACMF_I360_INV_MOV_1";
	private static final String ZIACMF_I360_INV_MOV_2 = "ZIACMF_I360_INV_MOV_2";
	private static final String ZIACMF_I360_INV_MOV_3 = "ZIACMF_I360_INV_MOV_3";
	private static final String ZIACMF_I360_EXT_SIS_CLAS = "ZIACMF_I360_EXT_SIS_CLAS";
	private static final String ZIACMF_I360_MBEW = "ZIACMF_MBEW";
	private Logger log = Logger.getLogger(SapConciliationDao.class.getName());
	private final SapOperationDao operationDao = new SapOperationDao();

	public ZIACMF_I360_INV_MOV_1 inventoryMovementsDao(DocInvBean docInvBean, Connection con,
			JCoDestination destination) throws JCoException, SQLException, RuntimeException, InvCicException {
		ZIACMF_I360_INV_MOV_1 ziacmf_I360_INV_MOV_1 = new ZIACMF_I360_INV_MOV_1();
		List<E_Mseg_SapEntity> msegList = new ArrayList<>();
		try {

			DocInvBean requestBean = operationDao.getDocInvBeanData(docInvBean, con);
			JCoFunction jcoFunction = destination.getRepository().getFunction(ZIACMF_I360_INV_MOV_1);
			jcoFunction.getImportParameterList().setValue("I_BUKRS", requestBean.getBukrs());
			jcoFunction.getImportParameterList().setValue("I_WERKS", requestBean.getWerks());
			jcoFunction.getImportParameterList().setValue("I_BUDAT_I", new Date(requestBean.getCreatedDate()));
			jcoFunction.getImportParameterList().setValue("I_BUDAT_F", new Date(requestBean.getModifiedDate()));
			jcoFunction.getImportParameterList().setValue("I_CPUTM_I", new Date(requestBean.getCreatedDate()));
			jcoFunction.getImportParameterList().setValue("I_CPUTM_F", new Date(requestBean.getModifiedDate()));
			// Form the Current Structure
			JCoTable lgortTable = jcoFunction.getImportParameterList().getTable("I_R_LGORT");
			for (String lgort : operationDao.getDocInvLgort(requestBean, con)) {
				lgortTable.appendRow();
				lgortTable.setValue("SIGN", "I");
				lgortTable.setValue("OPTION", "EQ");
				lgortTable.setValue("LOW", lgort);
			}
			jcoFunction.execute(destination);
			JCoTable jcoE_MsegTable = jcoFunction.getExportParameterList().getTable("E_MSEG");
			JCoTable jcoE_Error = jcoFunction.getExportParameterList().getTable("E_ERROR");
			E_Error_SapEntity eError = new E_Error_SapEntity();
			try {
				eError = new E_Error_SapEntity(jcoE_Error);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				log.warning("SapConciliationDao: [ZIACMF_I360_INV_MOV_1 -inventoryMovementsDao]:" + e1.getMessage());
			}
			if (eError.getType().equals("S")) {
				do {

					try {
						msegList.add(new E_Mseg_SapEntity(jcoE_MsegTable));
					} catch (JCoException | RuntimeException e) {
						log.warning(
								"SapConciliationDao: [ZIACMF_I360_INV_MOV_1 -inventoryMovementsDao]:" + e.getMessage());
						// Not Readable Row or EOF
					}
				} while (jcoE_MsegTable.nextRow());
			} else {
				log.severe("Regresó con E_ERROR " + eError.toString());
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ZIACMF_I360_MOV wm_InventoryMovementDao(DocInvBean docInvBean, Connection con, JCoDestination destination)
			throws JCoException, SQLException, RuntimeException {
		ZIACMF_I360_MOV ziacmf_I360_MOV = new ZIACMF_I360_MOV();
		List<E_Salida_SapEntity> salidaList = new ArrayList<>();
		try {
			DocInvBean requestBean = operationDao.getDocInvBeanData(docInvBean, con);
			HashMap<String, List<String>> mapLgnumLgtyp = operationDao.getMapLgnumLgtyp(docInvBean, con);
			if (!mapLgnumLgtyp.isEmpty()) {
				Iterator it = mapLgnumLgtyp.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pair = (Map.Entry) it.next();
					String lgnum = (String) pair.getKey();
					List<String> lgtyps = (List<String>) pair.getValue();
					for (String lgtyp : lgtyps) {
						JCoFunction jcoFunction = destination.getRepository().getFunction(ZIACMF_I360_MOV);
						jcoFunction.getImportParameterList().setValue("I_WERKS", requestBean.getWerks());
						jcoFunction.getImportParameterList().setValue("I_LGNUM", lgnum);
						jcoFunction.getImportParameterList().setValue("I_VLTYP", lgtyp);
						jcoFunction.getImportParameterList().setValue("I_QDATU_L",
								new Date(requestBean.getCreatedDate()));
						jcoFunction.getImportParameterList().setValue("I_QDATU_H",
								new Date(requestBean.getModifiedDate()));
						jcoFunction.getImportParameterList().setValue("I_QZEIT_L",
								new Date(requestBean.getCreatedDate()));
						jcoFunction.getImportParameterList().setValue("I_QZEIT_L",
								new Date(requestBean.getModifiedDate()));
						jcoFunction.execute(destination);
						JCoTable jcoE_Salida = jcoFunction.getExportParameterList().getTable("E_SALIDA");
						JCoTable jcoE_Error = jcoFunction.getExportParameterList().getTable("E_ERROR");
						E_Error_SapEntity eError = new E_Error_SapEntity();
						try {
							eError = new E_Error_SapEntity(jcoE_Error);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							log.warning("SapConciliationDao: [ZIACMF_I360_MOV - wm_InventoryMovementDao]:"
									+ e1.getMessage());
						}
						if (eError.getType().equals("S")) {
							do {

								try {
									salidaList.add(new E_Salida_SapEntity(lgnum, lgtyp, jcoE_Salida));
								} catch (JCoException | RuntimeException e) {
									log.warning("SapConciliationDao: [ZIACMF_I360_MOV - wm_InventoryMovementDao]:"
											+ e.getMessage());
								}
							} while (jcoE_Salida.nextRow());
						} else {
							log.severe("Regresó con E_ERROR " + eError.toString());
						}
					}
				}
			}
			ziacmf_I360_MOV.seteSalida_SapEntities(salidaList);
		} catch (JCoException e) {
			throw e;
		} catch (SQLException e) {
			throw e;
		} catch (RuntimeException e) {
			throw e;
		}
		return ziacmf_I360_MOV;
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
				log.warning("SapConciliationDao: [ZIACMF_I360_INV_MOV_2 -getSystemSnapshot]: " + e1.getMessage());
			}
			if (eError.getType().equals("S")) {
				// Cycle of the E_MARD Export Table
				do {
					try {
						eMard_SapEntities.add(new E_Mard_SapEntity(E_MARD));
					} catch (JCoException | RuntimeException e) {
						// Not Readable Row or EOF
						log.warning(
								"SapConciliationDao: [ZIACMF_I360_INV_MOV_2 - getSystemSnapshot]: " + e.getMessage());
					}
				} while (E_MARD.nextRow());

				// Cycle of the E_MSKU Export Table
				do {
					try {
						eMsku_SapEntities.add(new E_Msku_SapEntity(E_MSKU));
					} catch (JCoException | RuntimeException e) {
						// Not Readable Row or EOF
						log.warning(
								"SapConciliationDao: [ZIACMF_I360_INV_MOV_2 - getSystemSnapshot]: " + e.getMessage());
					}
				} while (E_MSKU.nextRow());

				// Cycle of the E_LQUA Export Table
				do {
					try {
						eLqua_SapEntities.add(new E_Lqua_SapEntity(E_LQUA));
					} catch (JCoException | RuntimeException e) {
						// Not Readable Row or EOF
						log.warning(
								"SapConciliationDao: [ZIACMF_I360_INV_MOV_2 - getSystemSnapshot]: " + e.getMessage());
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
	
	public ZIACMF_I360_INV_MOV_2 getSystemSnapshot_F(DocInvBean docInvBean, Connection con, JCoDestination destination)
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
			 List<String> listDIL = operationDao.getDocInvLgort(requestBean, con);
			 List<String> listDIMRL = operationDao.getDocInvMatRelevLgort(docInvBean, con);
			 for(String l : listDIMRL){
				 if(!listDIL.contains(l)){
					 listDIL.add(l);
				 }
			 }
			
			for (String lgort : listDIL) {
				lgortTable.appendRow();
				lgortTable.setValue("SIGN", "I");
				lgortTable.setValue("OPTION", "EQ");
				lgortTable.setValue("LOW", lgort);
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
				log.warning("SapConciliationDao: [ZIACMF_I360_INV_MOV_2 -getSystemSnapshot]: " + e1.getMessage());
			}
			if (eError.getType().equals("S")) {
				// Cycle of the E_MARD Export Table
				do {
					try {
						eMard_SapEntities.add(new E_Mard_SapEntity(E_MARD));
					} catch (JCoException | RuntimeException e) {
						// Not Readable Row or EOF
						log.warning(
								"SapConciliationDao: [ZIACMF_I360_INV_MOV_2 - getSystemSnapshot]: " + e.getMessage());
					}
				} while (E_MARD.nextRow());

				// Cycle of the E_MSKU Export Table
				do {
					try {
						eMsku_SapEntities.add(new E_Msku_SapEntity(E_MSKU));
					} catch (JCoException | RuntimeException e) {
						// Not Readable Row or EOF
						log.warning(
								"SapConciliationDao: [ZIACMF_I360_INV_MOV_2 - getSystemSnapshot]: " + e.getMessage());
					}
				} while (E_MSKU.nextRow());

				// Cycle of the E_LQUA Export Table
				do {
					try {
						eLqua_SapEntities.add(new E_Lqua_SapEntity(E_LQUA));
					} catch (JCoException | RuntimeException e) {
						// Not Readable Row or EOF
						log.warning(
								"SapConciliationDao: [ZIACMF_I360_INV_MOV_2 - getSystemSnapshot]: " + e.getMessage());
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
				try {
					materialTable.setValue("LOW", String.format("%018d", Integer.parseInt(material)));
				} catch (Exception e) {
					materialTable.setValue("LOW", material);
				}
			}
			JCoTable werksTable = jcoFunction.getImportParameterList().getTable("I_R_WERKS");
			werksTable.appendRow();
			werksTable.setValue("SIGN", "I");
			werksTable.setValue("OPTION", "EQ");
			werksTable.setValue("LOW", requestBean.getWerks());

			jcoFunction.execute(destination);
			JCoTable E_XTAB6 = jcoFunction.getExportParameterList().getTable("E_XTAB6");
			JCoTable E_ERROR = jcoFunction.getExportParameterList().getTable("E_ERROR");
			E_Error_SapEntity eError = new E_Error_SapEntity();
			try {
				eError = new E_Error_SapEntity(E_ERROR);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				log.warning("SapConciliationDao: [ZIACMF_I360_INV_MOV_3 - getTransitMovements]: " + e1.getMessage());
			}
			if (eError.getType().equals("S")) {
				do {
					try {
						xtab6_SapEntities.add(new E_Xtab6_SapEntity(E_XTAB6));
					} catch (JCoException | RuntimeException e) {
						log.warning(
								"SapConciliationDao: [ZIACMF_I360_INV_MOV_3 - getTransitMovements]: " + e.getMessage());
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

	public ZIACMF_MBEW getCostByMaterial(Connection con, JCoDestination destination, List<String> materialList)
			throws JCoException, RuntimeException, InvCicException {
		ZIACMF_MBEW ziacmf_MBEW = new ZIACMF_MBEW();
		try {
			List<E_Mbew_SapEntity> mbewData = new ArrayList<>();
			JCoFunction jcoFunction = destination.getRepository().getFunction(ZIACMF_I360_MBEW);
			JCoTable i_matnr = jcoFunction.getImportParameterList().getTable("I_MATNR");
			if (materialList != null && !materialList.isEmpty()) {

				for (String materialS : materialList) {
					i_matnr.appendRow();
					i_matnr.setValue("SIGN", "I");
					i_matnr.setValue("OPTION", "EQ");
					try {
						i_matnr.setValue("LOW", String.format("%018d", Integer.parseInt(materialS)));
					} catch (Exception e) {
						i_matnr.setValue("LOW", materialS);
					}

				}
			}
			jcoFunction.execute(destination);
			JCoTable E_MBEW = jcoFunction.getExportParameterList().getTable("E_MBEW");
			JCoTable E_ERROR = jcoFunction.getExportParameterList().getTable("E_ERROR");
			E_Error_SapEntity eError = new E_Error_SapEntity();
			try {
				eError = new E_Error_SapEntity(E_ERROR);
			} catch (Exception e1) {
				log.warning("SapConciliationDao: [getCostByMaterial]: " + e1.getMessage());
			}
			if (eError.getType().equals("S")) {
				do {
					try {
						mbewData.add(new E_Mbew_SapEntity(E_MBEW));
					} catch (JCoException | RuntimeException e) {
						log.warning("SapConciliationDao: [getCostByMaterial]: " + e.getMessage());
					}
				} while (E_MBEW.nextRow());
			}
			ziacmf_MBEW.seteError_SapEntities(eError);
			ziacmf_MBEW.seteMbewSapEntities(mbewData);
		} catch (JCoException e) {
			throw e;
		} catch (RuntimeException e) {
			throw e;
		}
		return ziacmf_MBEW;

	}

	public ZIACMF_I360_EXT_SIS_CLAS getClassSystem(Connection con, JCoDestination destination,
			List<String> materialList, String booleanDelta, String dateDelta)
			throws JCoException, RuntimeException, InvCicException {
		ZIACMF_I360_EXT_SIS_CLAS ziacmf_I360_EXT_SIS_CLAS = new ZIACMF_I360_EXT_SIS_CLAS();
		try {
			List<ZIACST_I360_OBJECTDATA_SapEntity> objectData = new ArrayList<>();
			JCoFunction jcoFunction = destination.getRepository().getFunction(ZIACMF_I360_EXT_SIS_CLAS);
			if (materialList != null && !materialList.isEmpty()) {
				JCoTable i_object = jcoFunction.getImportParameterList().getTable("I_OBJECT");
				for (String materialS : materialList) {
					i_object.appendRow();
					i_object.setValue("OBJEK", materialS);
				}
			} else {
				jcoFunction.getImportParameterList().setValue("I_DELTA", booleanDelta);
				if (dateDelta != null && !dateDelta.isEmpty()) {
					jcoFunction.getImportParameterList().setValue("I_F_DELTA", dateDelta);
				}
			}
			jcoFunction.execute(destination);
			JCoTable objectTable = jcoFunction.getExportParameterList().getTable("E_OBJECTDATA");
			do {
				objectData.add(new ZIACST_I360_OBJECTDATA_SapEntity(objectTable));
			} while (objectTable.nextRow());
			ziacmf_I360_EXT_SIS_CLAS.setObjectData(objectData);
		} catch (JCoException e) {
			throw e;
		} catch (RuntimeException e) {
			throw e;
		}
		return ziacmf_I360_EXT_SIS_CLAS;
	}

}
