package com.gmodelo.cyclicinventories.services;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.gmodelo.cyclicinventories.beans.BukrsBean;
import com.gmodelo.cyclicinventories.beans.Request;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.workservice.BukrsWorkService;

@Path("/services/BukrsService")
public class BukrsService {
	@Context
	private HttpServletRequest httpRequest;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getBukrs")
	public Response<List<BukrsBean>> getBukrs(Request request){
		return new BukrsWorkService().getBukrs(request);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getBukrsAndWerks")
	public Response<List<BukrsBean>> getBukrsAndWerks(Request request){
		return new BukrsWorkService().getBukrsWerks(request);
	}
}
