package com.gmodelo.workservice;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.Exception.InvCicException;
import com.gmodelo.beans.AbstractResultsBean;
import com.gmodelo.beans.DocInvBean;
import com.gmodelo.beans.Response;
import com.gmodelo.dao.SapConciliationDao;
import com.gmodelo.dao.SapOperationDao;
import com.gmodelo.runtime.ClasificationRuntime;
import com.gmodelo.structure.ZIACMF_I360_EXT_SIS_CLAS;
import com.gmodelo.structure.ZIACMF_I360_INV_MOV_1;
import com.gmodelo.structure.ZIACMF_I360_INV_MOV_2;
import com.gmodelo.structure.ZIACMF_I360_INV_MOV_3;
import com.gmodelo.utils.ConnectionManager;
import com.gmodelo.utils.ReturnValues;
import com.gmodelo.utils.Utilities;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoException;

public class SapConciliationWorkService {

	private Logger log = Logger.getLogger(SapConciliationWorkService.class.getName());

	private ConnectionManager connectionManager = new ConnectionManager();
	private SapOperationDao operationDao = new SapOperationDao();
	private SapConciliationDao conciliationDao = new SapConciliationDao();

	public void inventoryMovements(DocInvBean docInvBean, Connection con) throws SQLException {
		AbstractResultsBean results = new AbstractResultsBean();
		// Connection con = connectionManager.createConnection();
		try {
			if (con == null || !con.isValid(0) || con.isClosed()) {
				con = connectionManager.createConnection();
			}
			JCoDestination destination = connectionManager.getSapConnection(
					new Utilities().GetValueRepByKey(con, ReturnValues.REP_DESTINATION_VALUE).getStrCom1());
			ZIACMF_I360_INV_MOV_1 getMovements = conciliationDao.inventoryMovementsDao(docInvBean, con, destination);
			if (getMovements.geteError_SapEntities().getType().equals("S")
					&& getMovements.geteMseg_SapEntities() != null && !getMovements.geteMseg_SapEntities().isEmpty()) {
				results = operationDao.setZIACMF_I360_INV_MOV1(docInvBean, getMovements, con);
			}
		} catch (SQLException e) {
			log.log(Level.SEVERE, "[SapConciliationWorkService] - SQLException: ", e);
		} catch (InvCicException e) {
			log.log(Level.SEVERE, "[SapConciliationWorkService] - InvCicException: ", e);
		} catch (JCoException e) {
			log.log(Level.SEVERE, "[SapConciliationWorkService] - JCoException: ", e);
		} catch (RuntimeException e) {
			log.log(Level.SEVERE, "[SapConciliationWorkService] - RuntimeException: ", e);
		} catch (Exception e) {
			log.log(Level.SEVERE, "[SapConciliationWorkService] - Exception: ", e);
		}
	}

	public void inventorySnapShot(DocInvBean docInvBean) {
		AbstractResultsBean results = new AbstractResultsBean();
		Connection con = connectionManager.createConnection();
		try {
			JCoDestination destination = connectionManager.getSapConnection(
					new Utilities().GetValueRepByKey(con, ReturnValues.REP_DESTINATION_VALUE).getStrCom1());
			ZIACMF_I360_INV_MOV_2 getSnapshot = conciliationDao.getSystemSnapshot(docInvBean, con, destination);
			if (getSnapshot.geteError_SapEntities().getType().equals("S") && getSnapshot.geteLqua_SapEntities() != null
					&& getSnapshot.geteMard_SapEntities() != null && getSnapshot.geteMsku_SapEntities() != null) {
				results = operationDao.setZIACMF_I360_INV_MOV2(docInvBean, getSnapshot, con);
			}
		} catch (SQLException e) {
			log.log(Level.SEVERE, "[SapConciliationWorkService] - SQLException: ", e);
		} catch (InvCicException e) {
			log.log(Level.SEVERE, "[SapConciliationWorkService] - InvCicException: ", e);
		} catch (JCoException e) {
			log.log(Level.SEVERE, "[SapConciliationWorkService] - JCoException: ", e);
		} catch (RuntimeException e) {
			log.log(Level.SEVERE, "[SapConciliationWorkService] - RuntimeException: ", e);
		} catch (Exception e) {
			log.log(Level.SEVERE, "[SapConciliationWorkService] - Exception: ", e);
		} finally {
			try {
				con.close();
			} catch (Exception e) {
				log.log(Level.SEVERE, "[SapConciliationWorkService] - Finally Exception: ", e);
			}
		}

	}

	public void inventorySnapShot_F(DocInvBean docInvBean, Connection con) {
		AbstractResultsBean results = new AbstractResultsBean();
		// Connection con = connectionManager.createConnection();
		try {
			if (con == null || !con.isValid(0) || con.isClosed()) {
				con = connectionManager.createConnection();
			}
			JCoDestination destination = connectionManager.getSapConnection(
					new Utilities().GetValueRepByKey(con, ReturnValues.REP_DESTINATION_VALUE).getStrCom1());
			ZIACMF_I360_INV_MOV_2 getSnapshot = conciliationDao.getSystemSnapshot(docInvBean, con, destination);
			if (getSnapshot.geteError_SapEntities().getType().equals("S") && getSnapshot.geteLqua_SapEntities() != null
					&& getSnapshot.geteMard_SapEntities() != null && getSnapshot.geteMsku_SapEntities() != null) {
				results = operationDao.setZIACMF_I360_INV_MOV2_F(docInvBean, getSnapshot, con);
			}
		} catch (SQLException e) {
			log.log(Level.SEVERE, "[SapConciliationWorkService] - SQLException: ", e);
		} catch (InvCicException e) {
			log.log(Level.SEVERE, "[SapConciliationWorkService] - InvCicException: ", e);
		} catch (JCoException e) {
			log.log(Level.SEVERE, "[SapConciliationWorkService] - JCoException: ", e);
		} catch (RuntimeException e) {
			log.log(Level.SEVERE, "[SapConciliationWorkService] - RuntimeException: ", e);
		} catch (Exception e) {
			log.log(Level.SEVERE, "[SapConciliationWorkService] - Exception: ", e);
		}

	}

	public void inventoryTransit(DocInvBean docInvBean, Connection con) {
		AbstractResultsBean results = new AbstractResultsBean();
		try {
			if (con == null || !con.isValid(0) || con.isClosed()) {
				con = connectionManager.createConnection();
			}
			JCoDestination destination = connectionManager.getSapConnection(
					new Utilities().GetValueRepByKey(con, ReturnValues.REP_DESTINATION_VALUE).getStrCom1());
			ZIACMF_I360_INV_MOV_3 getTransit = conciliationDao.getTransitMovements(docInvBean, con, destination);
			if (getTransit.geteError_SapEntities().getType().equals("S") && getTransit.getXtab6_SapEntities() != null
					&& !getTransit.getXtab6_SapEntities().isEmpty()) {
				results = operationDao.setZIACMF_I360_INV_MOV3(docInvBean, getTransit, con);
			}
		} catch (SQLException e) {
			log.log(Level.SEVERE, "[SapConciliationWorkService] - SQLException: ", e);
		} catch (InvCicException e) {
			log.log(Level.SEVERE, "[SapConciliationWorkService] - InvCicException: ", e);
		} catch (JCoException e) {
			log.log(Level.SEVERE, "[SapConciliationWorkService] - JCoException: ", e);
		} catch (RuntimeException e) {
			log.log(Level.SEVERE, "[SapConciliationWorkService] - RuntimeException: ", e);
		} catch (Exception e) {
			log.log(Level.SEVERE, "[SapConciliationWorkService] - Exception: ", e);
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

	public Response _WS_SAPClassRuntime() {

		Response response = new Response<>();
		Connection con = connectionManager.createConnection();
		AbstractResultsBean result = new AbstractResultsBean();
		response.setAbstractResult(result);
		try {
			JCoDestination destination = connectionManager.getSapConnection(
					new Utilities().GetValueRepByKey(con, ReturnValues.REP_DESTINATION_VALUE).getStrCom1());
			if (ReturnValues.REP_CLASS_UPDATED == 0) {
				new ClasificationRuntime(destination, con, null, null, "X").run();
				
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
		}finally {
			try {
				con.close();
			}catch(Exception e) {
				log.log(Level.SEVERE, "[SapConciliationWorkService-clasificationSystem] - Finally Exception: ", e);	
			}
		}

	}
}
