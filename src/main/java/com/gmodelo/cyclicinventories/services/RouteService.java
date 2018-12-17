package com.gmodelo.cyclicinventories.services;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.bmore.ume001.beans.User;
import com.gmodelo.cyclicinventories.beans.Request;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.beans.RouteBean;
import com.gmodelo.cyclicinventories.beans.RouteUserPositionBean;
import com.gmodelo.cyclicinventories.filters.HttpSessionCollector;
import com.gmodelo.cyclicinventories.workservice.RouteWorkService;

@Path("/services/RouteService")
public class RouteService {
	@Context
	private HttpServletRequest httpRequest;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/addRoute")
	public Response<RouteBean> addRoute(Request request) {
		User user = (User) httpRequest.getSession().getAttribute("user");
		return new RouteWorkService().addRoute(request, user);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/deleteRoute")
	public Response<Object> deleteRoute(Request request) {
		return new RouteWorkService().deleteRoute(request);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getRoutes")
	public Response<List<RouteBean>> getRoutes(Request request) {
		return new RouteWorkService().getRoutes(request);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getOnlyRoutes")
	public Response<List<RouteBean>> getOnlyRoutes(Request request) {
		return new RouteWorkService().getOnlyRoutes(request);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getRoutesByUser")
	public String getRoutesByUser(Request request) {

		return new RouteWorkService().getRoutesByUser(request);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getRoutesByUserLegacy")
	public String getRoutesByUserLegacy(Request request) {
		HttpSession session = HttpSessionCollector.find(request.getTokenObject().getRelationUUID());
		User user = (User) session.getAttribute("user");

		// User user = (User) httpRequest.getSession().getAttribute("user");
		// HttpSession session = httpRequest.getSession();
		return new RouteWorkService().getRoutesByUserLegacy(user, session);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/autoTaskByUserLegacy")
	public String getAutoTaskByUserLegacy(Request request) {
		HttpSession session = HttpSessionCollector.find(request.getTokenObject().getRelationUUID());
		User user = (User) session.getAttribute("user");
		return new RouteWorkService().getAutoTaskByUserLegacy(user, session);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/autoTaskByUser")
	public String getAutoTaskByUser(Request request) {
		
		return new RouteWorkService().getAutoTaskByUser(request);
	}

	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getPositionsByIdRoute")
	public Response<List<RouteUserPositionBean>> getPositionsByIdRoute(Request request) {
		return new RouteWorkService().getPositionsByIdRoute(request);
	}
}
