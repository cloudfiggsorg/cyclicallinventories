package com.gmodelo.cyclicinventories.runtime;

import java.sql.Connection;
import java.util.List;

import com.gmodelo.cyclicinventories.workservice.SapConciliationWorkService;
import com.sap.conn.jco.JCoDestination;

public class ClassificationRuntime extends Thread {

	private JCoDestination runnableDestination;
	private Connection runnableConnection;
	private List<String> materialList;
	private String deltaString;
	private String booleanString;

	public ClassificationRuntime(JCoDestination runnableDestination, Connection runnableConnection,
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
