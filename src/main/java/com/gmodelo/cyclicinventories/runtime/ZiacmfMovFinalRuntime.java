package com.gmodelo.cyclicinventories.runtime;

import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.cyclicinventories.beans.DocInvBean;
import com.gmodelo.cyclicinventories.dao.SapOperationDao;
import com.gmodelo.cyclicinventories.workservice.SapConciliationWorkService;
import com.sap.conn.jco.JCoDestination;

public class ZiacmfMovFinalRuntime extends Thread {

	private JCoDestination asyncDestination;
	private Connection asyncConnection;
	private DocInvBean docInvBean;

	private Logger log = Logger.getLogger(ZiacmfMovFinalRuntime.class.getName());

	public ZiacmfMovFinalRuntime(JCoDestination asyncDestination, Connection asyncConnection, DocInvBean docInvBean) {
		super();
		this.asyncDestination = asyncDestination;
		this.asyncConnection = asyncConnection;
		this.docInvBean = docInvBean;
	}

	@Override
	public void run() {
		try {
			log.log(Level.INFO, "ZiacmfMovFinalRuntime - onInit");
			asyncConnection.setAutoCommit(false);
			new SapConciliationWorkService().inventorySnapShot_F(docInvBean, asyncConnection, asyncDestination);
			log.log(Level.INFO, "ZiacmfMovFinalRuntime - inventorySnapShot_F end");
			new SapConciliationWorkService().inventoryMovements_WM(docInvBean, asyncConnection, asyncDestination);
			log.log(Level.INFO, "ZiacmfMovFinalRuntime - inventoryMovements_WM end");
			new SapConciliationWorkService().inventoryMovements(docInvBean, asyncConnection, asyncDestination);
			log.log(Level.INFO, "ZiacmfMovFinalRuntime - inventoryMovements end");
			new SapConciliationWorkService().inventoryTransit(docInvBean, asyncConnection, asyncDestination);
			log.log(Level.INFO, "ZiacmfMovFinalRuntime - inventoryTransit end");
			new SapConciliationWorkService().getZiacmfMbew(docInvBean, asyncConnection, asyncDestination);
			log.log(Level.INFO, "ZiacmfMovFinalRuntime - getZiacmfMbew end");
			new SapOperationDao().setUpdateFinalInventory(asyncConnection, docInvBean);
			log.log(Level.INFO, "ZiacmfMovFinalRuntime - onEnd");
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
