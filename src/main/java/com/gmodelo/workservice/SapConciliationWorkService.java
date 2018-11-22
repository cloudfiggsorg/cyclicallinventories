package com.gmodelo.workservice;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.Exception.InvCicException;
import com.gmodelo.beans.AbstractResultsBean;
import com.gmodelo.beans.DocInvBean;
import com.gmodelo.dao.SapConciliationDao;
import com.gmodelo.dao.SapOperationDao;
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

	public void inventoryMovements(DocInvBean docInvBean) {
		AbstractResultsBean results = new AbstractResultsBean();
		Connection con = connectionManager.createConnection();
		try {

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
		} finally {
			try {
				con.close();
			} catch (Exception e) {
				log.log(Level.SEVERE, "[SapConciliationWorkService] - Finally Exception: ", e);
			}
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

	public void inventoryTransit(DocInvBean docInvBean) {
		AbstractResultsBean results = new AbstractResultsBean();
		Connection con = connectionManager.createConnection();
		try {

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
		} finally {
			try {
				con.close();
			} catch (Exception e) {
				log.log(Level.SEVERE, "[SapConciliationWorkService] - Finally Exception: ", e);
			}
		}

	}

	public void clasificationSystem() {

	}
}
