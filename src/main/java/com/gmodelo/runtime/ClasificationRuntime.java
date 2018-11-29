package com.gmodelo.runtime;

import java.sql.Connection;

import com.gmodelo.workservice.SapConciliationWorkService;
import com.sap.conn.jco.JCoDestination;

public class ClasificationRuntime implements Runnable {

	JCoDestination runnableDestination;
	Connection runnableConnection;

	public ClasificationRuntime(JCoDestination runnableDestination, Connection runnableConnection) {
		super();
		this.runnableDestination = runnableDestination;
		this.runnableConnection = runnableConnection;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		SapConciliationWorkService conciliationWorkService = new SapConciliationWorkService();
		

	}

}
