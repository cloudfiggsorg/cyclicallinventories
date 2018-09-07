package com.gmodelo.tasks;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.gmodelo.dao.DeltasDao;

public class DeltasJob implements Job{
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("deltas");
		/*
		 * INSERTAR AQUI EL CODIGO DE DELTA DAO
		 * 
		 * DeltasDao dao = new DeltasDao();
			dao.connectDestination();
			dao.RequestFirstRun();
		 * 
		 * 
		 * */
	}

}
