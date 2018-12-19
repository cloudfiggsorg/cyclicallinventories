package com.gmodelo.cyclicinventories.runtime;

import java.sql.Connection;

import com.gmodelo.cyclicinventories.beans.DocInvBean;
import com.gmodelo.cyclicinventories.workservice.SapConciliationWorkService;
import com.sap.conn.jco.JCoDestination;

public class ZiacmfMovInitialRuntime extends Thread {

	private JCoDestination asyncDestination;
	private Connection asyncConnection;
	private DocInvBean docInvBean;

	public ZiacmfMovInitialRuntime(JCoDestination asyncDestination, Connection asyncConnection, DocInvBean docInvBean) {
		super();
		this.asyncDestination = asyncDestination;
		this.asyncConnection = asyncConnection;
		this.docInvBean = docInvBean;
	}

	@Override
	public void run() {
		new SapConciliationWorkService().inventorySnapShot(docInvBean, asyncConnection, asyncDestination);
	}

}
