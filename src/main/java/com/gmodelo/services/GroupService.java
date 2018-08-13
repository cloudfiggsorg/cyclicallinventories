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
import com.gmodelo.beans.GroupsB;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.workservice.GroupWorkService;
import com.gmodelo.workservice.RouteWorkService;

@Path("/services/GroupService")
public class GroupService {
	
	@Context
	private HttpServletRequest httpRequest;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/addGroup")
	public Response<Object> addGroup(Request request){
		User user = (User) httpRequest.getSession().getAttribute("user");
		return new GroupWorkService().addGroup(request, user);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/assignGroupToUser")
	public Response<Object> assignGroupToUser(Request request){
		User user = (User) httpRequest.getSession().getAttribute("user");
		return new GroupWorkService().assignGroupToUser(request, user);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/unassignGroupToUser")
	public Response<Object> unassignGroupToUser(Request request){
		return new GroupWorkService().unassignGroupToUser(request);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/assignGroupToRoute")
	public Response<Object> assignGroupToRoute(Request request){
		return new GroupWorkService().assignGroupToRoute(request);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/unassignGroupToRoute")
	public Response<Object> unassignGroupToRoute(Request request){
		return new GroupWorkService().unassignGroupToRoute(request);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/deleteGroup")
	public Response<Object> deleteGroup(Request request){
		return new GroupWorkService().deleteGroup(request);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getGroups")
	public Response<List<GroupsB>> getGroups(Request request){
		return new GroupWorkService().getGroups(request);
	}
}
