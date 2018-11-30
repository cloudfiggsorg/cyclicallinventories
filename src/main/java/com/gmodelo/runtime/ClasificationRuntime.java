package com.gmodelo.runtime;

import java.sql.Connection;
import java.util.List;

import com.gmodelo.workservice.SapConciliationWorkService;
import com.sap.conn.jco.JCoDestination;

public class ClasificationRuntime extends Thread {

	JCoDestination runnableDestination;
	Connection runnableConnection;
	List<String> materialList;
	String deltaString;
	String booleanString;

	public ClasificationRuntime(JCoDestination runnableDestination, Connection runnableConnection,
			List<String> materialList, String deltaString, String booleanString) {
		super();
		this.runnableDestination = runnableDestination;
		this.runnableConnection = runnableConnection;
		this.materialList = materialList;
		this.deltaString = deltaString;
		this.booleanString = booleanString;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		SapConciliationWorkService conciliationWorkService = new SapConciliationWorkService();
		conciliationWorkService.clasificationSystem(runnableDestination, runnableConnection, materialList,
				booleanString, deltaString);

	}

}
