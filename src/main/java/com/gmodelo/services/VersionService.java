package com.gmodelo.services;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.jboss.logging.Logger;

import com.gmodelo.beans.Response;
import com.gmodelo.workservice.VersionWorkService;

@Path("/VersionModule")
public class VersionService {

	@Context
	private HttpServletRequest request;

	private Logger log = Logger.getLogger(VersionService.class.getName());
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getVersion")
	public Response<Object> getVersion() {
		log.warn((request.getSession().getAttribute("user") != null) ? "Hay sesion" : "NO sesion");
		return new VersionWorkService().getVersion(request);
	}
	
}
