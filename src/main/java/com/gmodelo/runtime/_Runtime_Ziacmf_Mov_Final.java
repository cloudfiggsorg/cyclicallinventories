package com.gmodelo.runtime;

import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.beans.DocInvBean;
import com.gmodelo.dao.SapOperationDao;
import com.gmodelo.workservice.SapConciliationWorkService;
import com.sap.conn.jco.JCoDestination;

public class _Runtime_Ziacmf_Mov_Final extends Thread {
	JCoDestination asyncDestination;
	Connection asyncConnection;
	DocInvBean docInvBean;

	private Logger log = Logger.getLogger(_Runtime_Ziacmf_Mov_Final.class.getName());

	public _Runtime_Ziacmf_Mov_Final(JCoDestination asyncDestination, Connection asyncConnection,
			DocInvBean docInvBean) {
		super();
		this.asyncDestination = asyncDestination;
		this.asyncConnection = asyncConnection;
		this.docInvBean = docInvBean;
	}

	@Override
	public void run() {
		try {
			asyncConnection.setAutoCommit(false);
			new SapConciliationWorkService().inventorySnapShot_F(docInvBean, asyncConnection, asyncDestination);
			new SapConciliationWorkService().inventoryMovements(docInvBean, asyncConnection, asyncDestination);
			new SapConciliationWorkService().inventoryTransit(docInvBean, asyncConnection, asyncDestination);
			new SapOperationDao().setUpdateFinalInventory(asyncConnection, docInvBean);
			asyncConnection.commit();
		} catch (Exception e) {
			log.log(Level.SEVERE, "Ocurrio un error durante la ejecucion del inventario Final" + e);

			try {
				// Do a roollback
				asyncConnection.rollback();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				log.log(Level.SEVERE,
						"Ocurrio un error durante la ejecucion del rollback de carga de inventario Final" + e);
			}
		} finally {
			try {
				asyncConnection.close();
			} catch (Exception e) {
				log.log(Level.SEVERE, "Ocurrio un error durante el cierre de la conexion del inventario Final" + e);
			}
		}
	}
}
