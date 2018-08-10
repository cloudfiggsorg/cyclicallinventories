package com.gmodelo.services;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.bmore.ume001.beans.User;
import com.gmodelo.beans.LoginBean;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.workservice.RouteWorkService;
import com.gmodelo.workservice.ZoneWorkService;

@Path("/services/RouteService")
public class RouteService {
	@Context
	private HttpServletRequest httpRequest;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/GetRoute")
	public String GetRoute(Request<LoginBean<?>> request) {
		return new RouteWorkService().GetRouteService(request);
	}
	
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
	@Path("/addRoutePosition")
	public Response<Object> addRoutePosition(Request request){
		return new RouteWorkService().addRoutePosition(request);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/assignMaterialToRoute")
	public Response<Object> assignMaterialToRoute(Request request){
		return new RouteWorkService().assignMaterialToRoute(request);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/unassignMaterialToRoute")
	public Response<Object> unassignMaterialToRoute(Request request){
		return new RouteWorkService().unassignMaterialToRoute(request);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/deleteRoute")
	public Response<Object> deleteRoute(Request request){
		return new RouteWorkService().deleteRoute(request);
	}
}
