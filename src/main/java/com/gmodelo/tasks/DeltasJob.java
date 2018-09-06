package com.gmodelo.tasks;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class DeltasJob implements Job{
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("hola deltas");
		///////////////
	}

}
