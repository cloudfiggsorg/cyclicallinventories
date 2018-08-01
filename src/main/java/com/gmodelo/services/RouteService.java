package com.gmodelo.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.gmodelo.beans.LoginBean;
import com.gmodelo.beans.Request;
import com.gmodelo.workservice.RouteWorkService;

@Path("/RouteService")
public class RouteService {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/GetRoute")
	public String GetRoute(Request<LoginBean<?>> request) {
		return new RouteWorkService().GetRouteService(request);
	}
}
