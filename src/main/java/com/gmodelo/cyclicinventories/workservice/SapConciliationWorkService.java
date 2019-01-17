package com.gmodelo.cyclicinventories.workservice;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.cyclicinventories.beans.AbstractResultsBean;
import com.gmodelo.cyclicinventories.beans.DocInvBean;
import com.gmodelo.cyclicinventories.beans.E_Mbew_SapEntity;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.dao.SapConciliationDao;
import com.gmodelo.cyclicinventories.dao.SapOperationDao;
import com.gmodelo.cyclicinventories.exception.InvCicException;
import com.gmodelo.cyclicinventories.runtime.ClassificationRuntime;
import com.gmodelo.cyclicinventories.runtime.ZiacmfMovFinalRuntime;
import com.gmodelo.cyclicinventories.runtime.ZiacmfMovInitialRuntime;
import com.gmodelo.cyclicinventories.structure.ZIACMF_I360_EXT_SIS_CLAS;
import com.gmodelo.cyclicinventories.structure.ZIACMF_I360_INV_MOV_1;
import com.gmodelo.cyclicinventories.structure.ZIACMF_I360_INV_MOV_2;
import com.gmodelo.cyclicinventories.structure.ZIACMF_I360_INV_MOV_3;
import com.gmodelo.cyclicinventories.structure.ZIACMF_MBEW;
import com.gmodelo.cyclicinventories.utils.ConnectionManager;
import com.gmodelo.cyclicinventories.utils.ReturnValues;
import com.gmodelo.cyclicinventories.utils.Utilities;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoException;

public class SapConciliationWorkService {

	private Logger log = Logger.getLogger(SapConciliationWorkService.class.getName());
	private ConnectionManager connectionManager = new ConnectionManager();
	private SapOperationDao operationDao = new SapOperationDao();
	private SapConciliationDao conciliationDao = new SapConciliationDao();

	public AbstractResultsBean WS_RuntimeInventorySnapShot(DocInvBean docInvBean) {
		AbstractResultsBean results = new AbstractResultsBean();
		Connection con = connectionManager.createConnection();
		try {
			JCoDestination destination = connectionManager.getSapConnection(
					new Utilities().getValueRepByKey(con, ReturnValues.REP_DESTINATION_VALUE).getStrCom1());
			new ZiacmfMovInitialRuntime(destination, con, docInvBean).start();
		} catch (InvCicException e) {
			results.setResultId(ReturnValues.IEXCEPTION);
			results.setResultMsgAbs(e.getMessage());
			log.log(Level.SEVERE, "[SapConciliationWorkService - WS_RuntimeInventorySnapShot] - InvCicException: ", e);
		} catch (JCoException e) {
			results.setResultId(ReturnValues.IEXCEPTION);
			results.setResultMsgAbs(e.getMessage());
			log.log(Level.SEVERE, "[SapConciliationWorkService - WS_RuntimeInventorySnapShot] - JCoException: ", e);
		} catch (RuntimeException e) {
			results.setResultId(ReturnValues.IEXCEPTION);
			results.setResultMsgAbs(e.getMessage());
			log.log(Level.SEVERE, "[SapConciliationWorkService - WS_RuntimeInventorySnapShot] - RuntimeException: ", e);
		} catch (Exception e) {
			results.setResultId(ReturnValues.IEXCEPTION);
			results.setResultMsgAbs(e.getMessage());
			log.log(Level.SEVERE, "[SapConciliationWorkService - WS_RuntimeInventorySnapShot] - Exception: ", e);
		}
		return results;
	}

	public void inventorySnapShot(DocInvBean docInvBean, Connection con, JCoDestination destination) {
		try {
			ZIACMF_I360_INV_MOV_2 getSnapshot = conciliationDao.getSystemSnapshot(docInvBean, con, destination);
			if (getSnapshot.geteError_SapEntities().getType().equals("S") && getSnapshot.geteLqua_SapEntities() != null
					&& getSnapshot.geteMard_SapEntities() != null && getSnapshot.geteMsku_SapEntities() != null) {
				operationDao.setZIACMF_I360_INV_MOV2(docInvBean, getSnapshot, con);
				operationDao.setUpdateInitialInventory(con, docInvBean);
			}
		} catch (SQLException e) {
			log.log(Level.SEVERE, "[SapConciliationWorkService - inventorySnapShot] - SQLException: ", e);
		} catch (InvCicException e) {
			log.log(Level.SEVERE, "[SapConciliationWorkService - inventorySnapShot] - InvCicException: ", e);
		} catch (JCoException e) {
			log.log(Level.SEVERE, "[SapConciliationWorkService - inventorySnapShot] - JCoException: ", e);
		} catch (RuntimeException e) {
			log.log(Level.SEVERE, "[SapConciliationWorkService - inventorySnapShot] - RuntimeException: ", e);
		} catch (Exception e) {
			log.log(Level.SEVERE, "[SapConciliationWorkService - inventorySnapShot] - Exception: ", e);
		} finally {
			try {
				con.close();
			} catch (Exception e) {
				log.log(Level.SEVERE, "[SapConciliationWorkService] - Finally Exception: ", e);
			}
		}

	}

	public AbstractResultsBean WS_RuntimeInventoryFinalSnapShot(DocInvBean docInvBean) {
		AbstractResultsBean results = new AbstractResultsBean();
		Connection con = connectionManager.createConnection();
		try {
			JCoDestination destination = connectionManager.getSapConnection(
					new Utilities().getValueRepByKey(con, ReturnValues.REP_DESTINATION_VALUE).getStrCom1());
			new ZiacmfMovFinalRuntime(destination, con, docInvBean).start();
		} catch (InvCicException e) {
			results.setResultId(ReturnValues.IEXCEPTION);
			results.setResultMsgAbs(e.getMessage());
			log.log(Level.SEVERE, "[SapConciliationWorkService - WS_RuntimeInventoryFinalSnapShot] - InvCicException: ",
					e);
		} catch (JCoException e) {
			results.setResultId(ReturnValues.IEXCEPTION);
			results.setResultMsgAbs(e.getMessage());
			log.log(Level.SEVERE, "[SapConciliationWorkService - WS_RuntimeInventoryFinalSnapShot] - JCoException: ",
					e);
		} catch (RuntimeException e) {
			results.setResultId(ReturnValues.IEXCEPTION);
			results.setResultMsgAbs(e.getMessage());
			log.log(Level.SEVERE,
					"[SapConciliationWorkService - WS_RuntimeInventoryFinalSnapShot] - RuntimeException: ", e);
		} catch (Exception e) {
			results.setResultId(ReturnValues.IEXCEPTION);
			results.setResultMsgAbs(e.getMessage());
			log.log(Level.SEVERE, "[SapConciliationWorkService - WS_RuntimeInventoryFinalSnapShot] - Exception: ", e);
		}
		return results;
	}

	public void inventoryTransit(DocInvBean docInvBean, Connection con, JCoDestination destination)
			throws SQLException, JCoException, RuntimeException, Exception {
		try {
			if (con == null || !con.isValid(0) || con.isClosed()) {
				con = connectionManager.createConnection();
			}
			ZIACMF_I360_INV_MOV_3 getTransit = conciliationDao.getTransitMovements(docInvBean, con, destination);
			if (getTransit.geteError_SapEntities().getType().equals("S") && getTransit.getXtab6_SapEntities() != null
					&& !getTransit.getXtab6_SapEntities().isEmpty()) {
				operationDao.setZIACMF_I360_INV_MOV3(docInvBean, getTransit, con);
			}
		} catch (SQLException e) {
			log.log(Level.SEVERE, "[SapConciliationWorkService - inventoryTransit] - SQLException: ", e);
			throw e;
		} catch (JCoException e) {
			log.log(Level.SEVERE, "[SapConciliationWorkService - inventoryTransit] - JCoException: ", e);
			throw e;
		} catch (RuntimeException e) {
			log.log(Level.SEVERE, "[SapConciliationWorkService - inventoryTransit] - RuntimeException: ", e);
			throw e;
		} catch (Exception e) {
			log.log(Level.SEVERE, "[SapConciliationWorkService - inventoryTransit] - Exception: ", e);
			throw e;
		}
	}

	public void inventoryMovements(DocInvBean docInvBean, Connection con, JCoDestination destination)
			throws SQLException, JCoException, RuntimeException, Exception {
		try {
			if (con == null || !con.isValid(0) || con.isClosed()) {
				con = connectionManager.createConnection();
			}
			ZIACMF_I360_INV_MOV_1 getMovements = conciliationDao.inventoryMovementsDao(docInvBean, con, destination);
			if (getMovements.geteError_SapEntities().getType().equals("S")
					&& getMovements.geteMseg_SapEntities() != null && !getMovements.geteMseg_SapEntities().isEmpty()) {
				operationDao.setZIACMF_I360_INV_MOV1(docInvBean, getMovements, con);
			}
		} catch (SQLException e) {
			log.log(Level.SEVERE, "[SapConciliationWorkService - inventoryMovements] - SQLException: ", e);
			throw e;
		} catch (InvCicException e) {
			log.log(Level.SEVERE, "[SapConciliationWorkService - inventoryMovements] - InvCicException: ", e);
			throw e;
		} catch (JCoException e) {
			log.log(Level.SEVERE, "[SapConciliationWorkService - inventoryMovements] - JCoException: ", e);
			throw e;
		} catch (RuntimeException e) {
			log.log(Level.SEVERE, "[SapConciliationWorkService - inventoryMovements] - RuntimeException: ", e);
			throw e;
		} catch (Exception e) {
			log.log(Level.SEVERE, "[SapConciliationWorkService - inventoryMovements] - Exception: ", e);
			throw e;
		}
	}

	public void inventorySnapShot_F(DocInvBean docInvBean, Connection con, JCoDestination destination)
			throws SQLException, JCoException, RuntimeException, Exception {

		try {
			if (con == null || !con.isValid(0) || con.isClosed()) {
				con = connectionManager.createConnection();
			}
			ZIACMF_I360_INV_MOV_2 getSnapshot = conciliationDao.getSystemSnapshot(docInvBean, con, destination);
			if (getSnapshot.geteError_SapEntities().getType().equals("S") && getSnapshot.geteLqua_SapEntities() != null
					&& getSnapshot.geteMard_SapEntities() != null && getSnapshot.geteMsku_SapEntities() != null) {
				operationDao.setZIACMF_I360_INV_MOV2_F(docInvBean, getSnapshot, con);
			}
		} catch (SQLException e) {
			log.log(Level.SEVERE, "[SapConciliationWorkService - inventorySnapShot_F] - SQLException: ", e);
			throw e;
		} catch (InvCicException e) {
			log.log(Level.SEVERE, "[SapConciliationWorkService - inventorySnapShot_F] - InvCicException: ", e);
			throw e;
		} catch (JCoException e) {
			log.log(Level.SEVERE, "[SapConciliationWorkService -inventorySnapShot_F] - JCoException: ", e);
			throw e;
		} catch (RuntimeException e) {
			log.log(Level.SEVERE, "[SapConciliationWorkService - inventorySnapShot_F] - RuntimeException: ", e);
			throw e;
		} catch (Exception e) {
			log.log(Level.SEVERE, "[SapConciliationWorkService - inventorySnapShot_F] - Exception: ", e);
			throw e;
		}

	}

	public void getZiacmfMbew(DocInvBean docInvBean, Connection con, JCoDestination destination) {
		try {
			if (con == null || !con.isValid(0) || con.isClosed()) {
				con = connectionManager.createConnection();
			}
			Integer materialCount = operationDao.getCountMaterialForPivotDocInv(docInvBean, con);
			if (materialCount > 0) {
				operationDao.setUpdatePivotBegin(con, docInvBean);
				List<String> listMaterial = operationDao.getmaterialForPivotDocInv(docInvBean, con);
				operationDao.setUpdatePivotEnd(con, docInvBean);
				List<String> pivotList = new ArrayList<>();
				ZIACMF_MBEW ziacmf_MBEW = new ZIACMF_MBEW();
				List<E_Mbew_SapEntity> emE_Mbew_SapEntities = new ArrayList<>();
				ziacmf_MBEW.seteMbewSapEntities(emE_Mbew_SapEntities);
				if (listMaterial.size() >= 50) {
					for (String material : listMaterial) {
						pivotList.add(material);
						if (pivotList.size() >= 50) {
							ZIACMF_MBEW ziacmf_MBEW_pvt = conciliationDao.getCostByMaterial(con, destination,
									pivotList);
							if (ziacmf_MBEW_pvt.geteError_SapEntities().getType().equals("S")
									&& ziacmf_MBEW_pvt.geteMbewSapEntities() != null) {
								ziacmf_MBEW.geteMbewSapEntities().addAll(ziacmf_MBEW_pvt.geteMbewSapEntities());
								ziacmf_MBEW.seteError_SapEntities(ziacmf_MBEW_pvt.geteError_SapEntities());
								ziacmf_MBEW_pvt = new ZIACMF_MBEW();
							}
						}
					}
					if (pivotList.size() > 0) {
						ZIACMF_MBEW ziacmf_MBEW_pvt = conciliationDao.getCostByMaterial(con, destination, pivotList);
						if (ziacmf_MBEW_pvt.geteError_SapEntities().getType().equals("S")
								&& ziacmf_MBEW_pvt.geteMbewSapEntities() != null) {
							ziacmf_MBEW.geteMbewSapEntities().addAll(ziacmf_MBEW_pvt.geteMbewSapEntities());
							ziacmf_MBEW.seteError_SapEntities(ziacmf_MBEW_pvt.geteError_SapEntities());
						}
					}
				} else {
					ziacmf_MBEW = conciliationDao.getCostByMaterial(con, destination, listMaterial);
				}
				if (ziacmf_MBEW.geteError_SapEntities().getType().equals("S")
						&& ziacmf_MBEW.geteMbewSapEntities() != null) {

					log.log(Level.INFO,
							"[SapConciliationWorkService-getZiacmfMbew] : ziacmf_MBEW " + ziacmf_MBEW.toString());

					HashMap<String, List<String>> matWerkMap = operationDao.getMbewValues(con);
					List<E_Mbew_SapEntity> toInsertMbew = new ArrayList<>();
					List<E_Mbew_SapEntity> toUpdateMbew = new ArrayList<>();
					for (E_Mbew_SapEntity entity : ziacmf_MBEW.geteMbewSapEntities()) {
						if (matWerkMap.containsKey(entity.getMatnr())) {
							if (matWerkMap.get(entity.getMatnr()).contains(entity.getBwkey())) {
								toUpdateMbew.add(entity);
							} else {
								toInsertMbew.add(entity);
							}
						} else {
							toInsertMbew.add(entity);
						}
					}
					if (toInsertMbew.size() > 0) {
						operationDao.setZIACMF_E_MBEW(con, toInsertMbew);
						log.log(Level.INFO,
								"[SapConciliationWorkService-getZiacmfMbew] : toInsert: " + toInsertMbew.toString());
					}
					if (toUpdateMbew.size() > 0) {
						operationDao.setZIACMF_E_MBEW_UPD(con, toUpdateMbew);
						log.log(Level.INFO,
								"[SapConciliationWorkService-getZiacmfMbew] : toUpdate: " + toUpdateMbew.toString());
					}
				}else{
					log.log(Level.SEVERE,"[SapConciliationWorkService-getZiacmfMbew] :"+ziacmf_MBEW.geteError_SapEntities().toString());
				}
			}
		} catch (SQLException e) {
			log.log(Level.SEVERE, "[SapConciliationWorkService-getZiacmfMbew] - SQLException: ", e);

		} catch (InvCicException e) {
			log.log(Level.SEVERE, "[SapConciliationWorkService-getZiacmfMbew] - InvCicException: ", e);

		} catch (JCoException e) {
			log.log(Level.SEVERE, "[SapConciliationWorkService-getZiacmfMbew] - JCoException: ", e);

		} catch (RuntimeException e) {
			log.log(Level.SEVERE, "[SapConciliationWorkService-getZiacmfMbew] - RuntimeException: ", e);

		} catch (Exception e) {
			log.log(Level.SEVERE, "[SapConciliationWorkService-getZiacmfMbew] - Exception: ", e);
		}
	}

	public Response<ZIACMF_I360_EXT_SIS_CLAS> WS_getClassSystem() {
		Response<ZIACMF_I360_EXT_SIS_CLAS> response = new Response<>();
		AbstractResultsBean result = new AbstractResultsBean();
		response.setAbstractResult(result);
		try {
			ZIACMF_I360_EXT_SIS_CLAS i360_EXT_SIS_CLAS = operationDao.getClassSystem();
			if (i360_EXT_SIS_CLAS.getObjectData() != null && !i360_EXT_SIS_CLAS.getObjectData().isEmpty()) {
				response.setLsObject(i360_EXT_SIS_CLAS);
			} else {
				result.setResultId(ReturnValues.IERROR);
				result.setResultMsgAbs("Sistema de Clasificacion no cargado anteriormente, favor de generar carga");
				log.log(Level.SEVERE,
						"[SapConciliationWorkService - WS_getClassSystem] - Sistema de Clasificacion no cargado anteriormente, favor de generar carga ");
			}
		} catch (SQLException e) {
			result.setResultId(ReturnValues.IEXCEPTION);
			result.setResultMsgAbs(e.getMessage());
			log.log(Level.SEVERE, "[SapConciliationWorkService- WS_getClassSystem] - SQLException: ", e);

		}
		return response;
	}

	@SuppressWarnings("rawtypes")
	public Response _WS_SAPClassRuntime() {
		Response response = new Response<>();
		Connection con = connectionManager.createConnection();
		AbstractResultsBean result = new AbstractResultsBean();
		response.setAbstractResult(result);
		try {
			JCoDestination destination = connectionManager.getSapConnection(
					new Utilities().getValueRepByKey(con, ReturnValues.REP_DESTINATION_VALUE).getStrCom1());
			if (ReturnValues.REP_CLASS_UPDATED == 0) {
				new ClassificationRuntime(destination, con, null, null, "X").start();
			} else {
				result.setResultId(ReturnValues.IERROR);
				result.setResultMsgAbs("Ejecución en progreso");
			}
		} catch (SQLException e) {
			result.setResultId(ReturnValues.IEXCEPTION);
			result.setResultMsgAbs(e.getMessage());
			log.log(Level.SEVERE, "[SapConciliationWorkService-_WS_SAPClassRuntime] - SQLException: ", e);
		} catch (JCoException e) {
			result.setResultId(ReturnValues.IEXCEPTION);
			result.setResultMsgAbs(e.getMessage());
			log.log(Level.SEVERE, "[SapConciliationWorkService-_WS_SAPClassRuntime] - JCoException: ", e);
		} catch (InvCicException e) {
			result.setResultId(ReturnValues.IEXCEPTION);
			result.setResultMsgAbs(e.getMessage());
			log.log(Level.SEVERE, "[SapConciliationWorkService-_WS_SAPClassRuntime] - InvCicException: ", e);
		} catch (RuntimeException e) {
			result.setResultId(ReturnValues.IEXCEPTION);
			result.setResultMsgAbs(e.getMessage());
			log.log(Level.SEVERE, "[SapConciliationWorkService-_WS_SAPClassRuntime] - RuntimeException: ", e);
		} catch (Exception e) {
			result.setResultId(ReturnValues.IEXCEPTION);
			log.log(Level.SEVERE, "[SapConciliationWorkService-_WS_SAPClassRuntime] - Exception: ", e);
		}
		return response;
	}

	public void clasificationSystem(JCoDestination destination, Connection con, List<String> materialList,
			String booleanDelta, String dateDelta) {

		AbstractResultsBean results = new AbstractResultsBean();
		ReturnValues.REP_CLASS_UPDATED = 1;
		try {
			ZIACMF_I360_EXT_SIS_CLAS ziacmf_I360_EXT_SIS_CLAS = conciliationDao.getClassSystem(con, destination,
					materialList, booleanDelta, dateDelta);
			if (ziacmf_I360_EXT_SIS_CLAS.getObjectData() != null
					&& !ziacmf_I360_EXT_SIS_CLAS.getObjectData().isEmpty()) {
				results = operationDao.setZIACMF_I360_EXT_SIS_CLAS(con, ziacmf_I360_EXT_SIS_CLAS);
				if (results.getResultId() == ReturnValues.ISUCCESS) {
					ReturnValues.REP_CLASS_UPDATED = 0;
				}
			}
		} catch (SQLException e) {
			log.log(Level.SEVERE, "[SapConciliationWorkService-clasificationSystem] - SQLException: ", e);
			ReturnValues.REP_CLASS_UPDATED = 0;
		} catch (InvCicException e) {
			log.log(Level.SEVERE, "[SapConciliationWorkService-clasificationSystem] - InvCicException: ", e);
			ReturnValues.REP_CLASS_UPDATED = 0;
		} catch (JCoException e) {
			log.log(Level.SEVERE, "[SapConciliationWorkService-clasificationSystem] - JCoException: ", e);
			ReturnValues.REP_CLASS_UPDATED = 0;
		} catch (RuntimeException e) {
			log.log(Level.SEVERE, "[SapConciliationWorkService-clasificationSystem] - RuntimeException: ", e);
			ReturnValues.REP_CLASS_UPDATED = 0;
		} catch (Exception e) {
			log.log(Level.SEVERE, "[SapConciliationWorkService-clasificationSystem] - Exception: ", e);
			ReturnValues.REP_CLASS_UPDATED = 0;
		} finally {
			try {
				con.close();
			} catch (Exception e) {
				log.log(Level.SEVERE, "[SapConciliationWorkService-clasificationSystem] - Finally Exception: ", e);
			}
		}

	}
}
