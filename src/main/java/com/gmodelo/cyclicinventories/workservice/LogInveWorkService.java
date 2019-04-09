package com.gmodelo.cyclicinventories.workservice;

import java.util.List;
import java.util.logging.Logger;

import com.bmore.ume001.beans.User;
import com.gmodelo.cyclicinventories.beans.LogInve;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.dao.LogInveDao;

public class LogInveWorkService {
	
	private Logger log = Logger.getLogger(LogInveWorkService.class.getName());
	
	public Response<List<LogInve>> getLog(User user) {
		
		log.info("[getLog]");
		String userId = null;
		
		if(user != null){
			userId = user.getEntity().getIdentyId();
		}
				 
		return new LogInveDao().getLogByUser(userId);
	}
	
	public Response<String> getLogCount(User user) {
		
		log.info("[getLogCount]");
		String userId = null;
		
		if(user != null){
			userId = user.getEntity().getIdentyId();
		}
		
		return new LogInveDao().getLogCountByUser(userId);
	}

}
