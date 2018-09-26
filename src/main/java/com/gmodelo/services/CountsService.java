package com.gmodelo.services;

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
import com.gmodelo.workservice.CountsWorkService;
import com.gmodelo.workservice.RouteWorkService;

@Path("/services/CountsService")
public class CountsService {
	@Context
	private HttpServletRequest httpRequest;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/addCount")
	public Response<Object> addCount(Request request) {
		//User user = (User) httpRequest.getSession().getAttribute("user");
		return new CountsWorkService().addCount(request);
	}
}