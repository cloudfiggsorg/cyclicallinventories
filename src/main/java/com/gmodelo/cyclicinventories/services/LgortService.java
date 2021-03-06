package com.gmodelo.cyclicinventories.services;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.gmodelo.cyclicinventories.beans.LgortBeanView;
import com.gmodelo.cyclicinventories.beans.Request;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.workservice.LgortWorkService;

@Path("/services/LgortService")
public class LgortService {

	@Context
	private HttpServletRequest httpRequest;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getLgortAndWerks")
	public Response<List<LgortBeanView>> getLgortByWerks(Request request){
		return new LgortWorkService().getLgortByWerks(request);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getNgorts")
	public Response<List<LgortBeanView>> getNgorts(Request request){
		return new LgortWorkService().getNgorts(request);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getNgortsIM")
	public Response<List<LgortBeanView>> getNgortsIM(Request request){
		return new LgortWorkService().getNgortsIM(request);
	}

}
