package com.gmodelo.tasks;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.jboss.logging.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.gmodelo.dao.DeltasDao;

public class DeltasCron implements ServletContextListener {

	private Logger log = Logger.getLogger(DeltasCron.class.getName());
	private Scheduler scheduler;
	
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		DeltasDao cron = new DeltasDao();
		
		JobDetail job = JobBuilder.newJob(DeltasJob.class).withIdentity("JobDao", "group1").build();
		Trigger trigger = TriggerBuilder.newTrigger().withIdentity("TriggerDao", "group1")
				.withSchedule(CronScheduleBuilder.cronSchedule(cron.getCronDelta())).build();
		
		try {
			scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
			scheduler.scheduleJob(job, trigger);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

		try {
			scheduler.shutdown();
			log.info("Context Destroyed");
		} catch (SchedulerException e) {
			log.error("Some problem ocurred while trying to shutdown the ServletContextListener...");
		}

	}
}
