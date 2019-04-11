package com.gmodelo.cyclicinventories.workservice;

import java.util.List;
import java.util.logging.Logger;

import com.gmodelo.cyclicinventories.beans.LogInve;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.dao.LogInveDao;

public class LogInveWorkService {
	
	private Logger log = Logger.getLogger(LogInveWorkService.class.getName());
	
	public Response<List<LogInve>> getLog() {
		
		log.info("[getLog]");
		return new LogInveDao().getLogByUser();
	}
	
}
