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
import com.gmodelo.cyclicinventories.workservice.CatByMatnrWorkService;

@Path("/services/CatByMatnrService")
public class CatByMatnrService {

	@Context
	private HttpServletRequest httpRequest;	
	
	@SuppressWarnings("rawtypes")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/addCategory")
	public Response<Object> addJustify(Request request) {
		User user = (User) httpRequest.getSession().getAttribute("user");
		return new CatByMatnrWorkService().saveMatnrByCat(request, user.getEntity().getIdentyId());
	}
}
