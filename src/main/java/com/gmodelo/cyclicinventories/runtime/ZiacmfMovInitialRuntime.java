package com.gmodelo.cyclicinventories.runtime;

import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.cyclicinventories.beans.DocInvBean;
import com.gmodelo.cyclicinventories.workservice.SapConciliationWorkService;
import com.sap.conn.jco.JCoDestination;

public class ZiacmfMovInitialRuntime extends Thread {

	private JCoDestination asyncDestination;
	private Connection asyncConnection;
	private DocInvBean docInvBean;

	private Logger log = Logger.getLogger(ZiacmfMovInitialRuntime.class.getName());
	
	public ZiacmfMovInitialRuntime(JCoDestination asyncDestination, Connection asyncConnection, DocInvBean docInvBean) {
		super();
		this.asyncDestination = asyncDestination;
		this.asyncConnection = asyncConnection;
		this.docInvBean = docInvBean;
	}

	@Override
	public void run() {
		log.log(Level.INFO, "ZiacmfMovInitialRuntime - onInit");
		new SapConciliationWorkService().inventorySnapShot(docInvBean, asyncConnection, asyncDestination);
		log.log(Level.INFO, "ZiacmfMovInitialRuntime - onEnd");
	}

}
