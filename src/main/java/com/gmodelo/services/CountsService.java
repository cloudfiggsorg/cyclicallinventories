package com.gmodelo.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.filters.HttpSessionCollector;
import com.gmodelo.workservice.CountsWorkService;

@Path("/services/CountsService")
public class CountsService {
	@Context
	private HttpServletRequest httpRequest;

	
	@SuppressWarnings("rawtypes")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/addCount")
	public Response addCount(Request request) {
		return new CountsWorkService().addCount(request);
	}
}
