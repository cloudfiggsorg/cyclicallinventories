package com.gmodelo.cyclicinventories.services;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.bmore.ume001.beans.User;
import com.gmodelo.cyclicinventories.beans.JustifyCat;
import com.gmodelo.cyclicinventories.beans.Request;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.workservice.JustifyCatWorkService;

@Path("/services/JustifyCatService")
public class JustifyCatService {

	@Context
	private HttpServletRequest httpRequest;	
	
	@SuppressWarnings("rawtypes")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/addJustify")
	public Response<JustifyCat> addJustify(Request request) {
		User user = (User) httpRequest.getSession().getAttribute("user");
		return new JustifyCatWorkService().addJustify(request, user.getEntity().getIdentyId());
	}

	@SuppressWarnings("rawtypes")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/deleteJustifies")
	public Response<Object> deleteJustifies(Request request) {
		return new JustifyCatWorkService().deleteJustifies(request);
	}

	@SuppressWarnings("rawtypes")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getJustifies")
	public Response<List<JustifyCat>> getJustifies(Request request) {
		return new JustifyCatWorkService().getJustifies();
	}
}
