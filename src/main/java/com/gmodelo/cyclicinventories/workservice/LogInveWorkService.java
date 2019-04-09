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
		String userId = user.getEntity().getIdentyId();
		Response<List<LogInve>> res = new LogInveDao().getLogByUser(userId);
		return res;
	}

}
