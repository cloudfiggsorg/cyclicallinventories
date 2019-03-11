package com.gmodelo.cyclicinventories.services;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.gmodelo.cyclicinventories.beans.Repository;
import com.gmodelo.cyclicinventories.beans.Request;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.workservice.RepositoryWorkService;

@Path("/services/Repository")
public class RepositoryService {

	@Context
	private HttpServletRequest httpRequest;	
	
	@SuppressWarnings("rawtypes")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/saveOption")
	public Response<Repository> addJustify(Request request) {
		return new RepositoryWorkService().saveOption(request);
	}

	@SuppressWarnings("rawtypes")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/deleteOptions")
	public Response<Object> deleteOptions(Request request) {
		return new RepositoryWorkService().deleteOptions(request);
	}

	@SuppressWarnings("rawtypes")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getOptions")
	public Response<List<Repository>> getJustifies(Request request) {
		return new RepositoryWorkService().getOptions();
	}
}
