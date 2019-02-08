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
import com.gmodelo.cyclicinventories.beans.DocInvBean;
import com.gmodelo.cyclicinventories.beans.Request;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.workservice.DocInvWorkService;

@Path("/services/DocInvService")
public class DocInvService {

	@Context
	private HttpServletRequest httpRequest;

	@SuppressWarnings("rawtypes")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/addDocInv")
	public Response<DocInvBean> addDocInv(Request request) {
		User user = (User) httpRequest.getSession().getAttribute("user");
		return new DocInvWorkService().addDocInv(request, user);
	}

	@SuppressWarnings("rawtypes")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/deleteDocInv")
	public Response<Object> deleteDocInv(Request request) {
		return new DocInvWorkService().deleteDocInv(request);
	}

	@SuppressWarnings("rawtypes")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getDocInv")
	public Response<List<DocInvBean>> getDocInv(Request request) {
		return new DocInvWorkService().getDocInv(request);
	}
	
	@SuppressWarnings("rawtypes")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getOnlyDocInv")
	public Response<List<DocInvBean>> getOnlyDocInv(Request request) {
		return new DocInvWorkService().getOnlyDocInv(request);
	}
	
	@SuppressWarnings("rawtypes")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getOnlyDocInvNoTask")
	public Response<List<DocInvBean>> getOnlyDocInvNoTask(Request request) {
		return new DocInvWorkService().getOnlyDocInvNoTask(request);
	}

	@SuppressWarnings("rawtypes")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/conciDocInv")
	public Response conciDocInv(Request request) {
		User user = (User) httpRequest.getSession().getAttribute("user");
		return new DocInvWorkService().conciDocInv(request, user);
	}

}
