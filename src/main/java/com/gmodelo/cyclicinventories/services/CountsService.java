package com.gmodelo.cyclicinventories.services;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.bmore.ume001.beans.User;
import com.gmodelo.cyclicinventories.beans.Request;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.workservice.CountsWorkService;
import com.gmodelo.cyclicinventories.workservice.TaskWorkService;

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
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/addContingencyTaskCount")
	public Response<Object> addContingencyTaskCount(Request request){
		User user = (User) httpRequest.getSession().getAttribute("user");
		return new CountsWorkService().addCountFromContingency(request, user);
	}
}
