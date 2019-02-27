package com.gmodelo.cyclicinventories.workservice;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.cyclicinventories.beans.Request;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.dao.VersionDao;

public class VersionWorkService {
	
	private Logger log = Logger.getLogger(VersionWorkService.class.getName());
	
	@SuppressWarnings("rawtypes")
	public Response getVersion(Request request){
		log.log(Level.WARNING,"[getVersionWorkService] ");
		return new VersionDao().getVersion(request);
	}
}
