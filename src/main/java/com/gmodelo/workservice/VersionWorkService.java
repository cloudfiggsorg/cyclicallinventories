package com.gmodelo.workservice;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.beans.Response;
import com.gmodelo.dao.VersionDao;

public class VersionWorkService {
	
	private Logger log = Logger.getLogger(VersionWorkService.class.getName());
	
	@SuppressWarnings("rawtypes")
	public Response getVersion(){
		log.log(Level.WARNING,"[getVersionWorkService] ");
		return new VersionDao().getVersion();
	}
}
