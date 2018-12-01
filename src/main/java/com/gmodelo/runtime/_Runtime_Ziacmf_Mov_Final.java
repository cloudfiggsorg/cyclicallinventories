package com.gmodelo.runtime;

import java.sql.Connection;

import com.gmodelo.beans.DocInvBean;
import com.gmodelo.workservice.SapConciliationWorkService;
import com.sap.conn.jco.JCoDestination;

public class _Runtime_Ziacmf_Mov_Final extends Thread {
	JCoDestination asyncDestination;
	Connection asyncConnection;
	DocInvBean docInvBean;

	public _Runtime_Ziacmf_Mov_Final(JCoDestination asyncDestination, Connection asyncConnection,
			DocInvBean docInvBean) {
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
