package com.gmodelo.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.gmodelo.beans.Response;
import com.gmodelo.workservice.VersionWorkService;

@Path("/VersionModule")
public class VersionService {
	
	@SuppressWarnings("rawtypes")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getVersion")
	public Response getVersion() {
		return new VersionWorkService().getVersion();
	}
	
}
