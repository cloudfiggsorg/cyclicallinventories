package com.gmodelo.cyclicinventories.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.gmodelo.cyclicinventories.beans.Request;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.workservice.VersionWorkService;

@Path("/VersionModule")
public class VersionService {

	@SuppressWarnings("rawtypes")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getVersion")
	public Response getVersion(Request request) {
		return new VersionWorkService().getVersion(request);
	}

	@SuppressWarnings("rawtypes")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/validateAdmin")
	public Response validateAdmin(Request request) {
		return new VersionWorkService().isAdminEnabled(request);
	}
}
