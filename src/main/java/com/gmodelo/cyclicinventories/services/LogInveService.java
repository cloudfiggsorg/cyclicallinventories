package com.gmodelo.cyclicinventories.services;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.gmodelo.cyclicinventories.beans.LogInve;
import com.gmodelo.cyclicinventories.beans.Request;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.workservice.LogInveWorkService;

@Path("/services/LogInveService")
public class LogInveService {

	@Context
	private HttpServletRequest httpRequest;	
	
	@SuppressWarnings("rawtypes")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getLog")
	public Response<List<LogInve>> getLog(Request request) {
		
		return new LogInveWorkService().getLog();
	}	
}
