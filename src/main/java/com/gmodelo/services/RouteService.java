package com.gmodelo.services;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.bmore.ume001.beans.User;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.beans.RouteBean;
import com.gmodelo.workservice.RouteWorkService;

@Path("/services/RouteService")
public class RouteService {
	@Context
	private HttpServletRequest httpRequest;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/addRoute")
	public Response<Object> addRoute(Request request){
		User user = (User) httpRequest.getSession().getAttribute("user");
		return new RouteWorkService().addRoute(request, user);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/deleteRoute")
	public Response<Object> deleteRoute(Request request){
		return new RouteWorkService().deleteRoute(request);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getRoutes")
	public Response<List<RouteBean>> getRoutes(Request request){
		return new RouteWorkService().getRoutes(request);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getRoutesByUser")
	public Response<List<RouteBean>> getRoutesByUser(Request request){
		return new RouteWorkService().getRoutesByUser(request);
	}
}
